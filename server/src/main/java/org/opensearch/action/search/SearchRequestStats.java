/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.action.search;

import org.opensearch.common.annotation.PublicApi;
import org.opensearch.common.inject.Inject;
import org.opensearch.common.metrics.CounterMetric;
import org.opensearch.common.metrics.MeanMetric;
import org.opensearch.common.settings.ClusterSettings;
import org.opensearch.common.settings.Setting;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Request level search stats to track coordinator level node search latencies
 *
 * @opensearch.api
 */
@PublicApi(since = "2.11.0")
public final class SearchRequestStats extends SearchRequestOperationsListener {
    Map<SearchPhaseName, StatsHolder> phaseStatsMap = new EnumMap<>(SearchPhaseName.class);

    public static final String SEARCH_REQUEST_STATS_ENABLED_KEY = "search.request_stats_enabled";
    public static final Setting<Boolean> SEARCH_REQUEST_STATS_ENABLED = Setting.boolSetting(
        SEARCH_REQUEST_STATS_ENABLED_KEY,
        false,
        Setting.Property.Dynamic,
        Setting.Property.NodeScope
    );

    @Inject
    public SearchRequestStats(ClusterSettings clusterSettings) {
        this.setEnabled(clusterSettings.get(SEARCH_REQUEST_STATS_ENABLED));
        clusterSettings.addSettingsUpdateConsumer(SEARCH_REQUEST_STATS_ENABLED, this::setEnabled);
        for (SearchPhaseName searchPhaseName : SearchPhaseName.values()) {
            phaseStatsMap.put(searchPhaseName, new StatsHolder());
        }
    }

    public long getPhaseCurrent(SearchPhaseName searchPhaseName) {
        return phaseStatsMap.get(searchPhaseName).current.count();
    }

    public long getPhaseTotal(SearchPhaseName searchPhaseName) {
        return phaseStatsMap.get(searchPhaseName).total.count();
    }

    public long getPhaseMetric(SearchPhaseName searchPhaseName) {
        return phaseStatsMap.get(searchPhaseName).timing.sum();
    }

    @Override
    protected void onPhaseStart(SearchPhaseContext context) {
        phaseStatsMap.get(context.getCurrentPhase().getSearchPhaseName()).current.inc();
    }

    @Override
    protected void onPhaseEnd(SearchPhaseContext context, SearchRequestContext searchRequestContext) {
        StatsHolder phaseStats = phaseStatsMap.get(context.getCurrentPhase().getSearchPhaseName());
        phaseStats.current.dec();
        phaseStats.total.inc();
        phaseStats.timing.inc(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - context.getCurrentPhase().getStartTimeInNanos()));
    }

    @Override
    protected void onPhaseFailure(SearchPhaseContext context, Throwable cause) {
        phaseStatsMap.get(context.getCurrentPhase().getSearchPhaseName()).current.dec();
    }

    /**
     * Holder of statistics values
     *
     * @opensearch.internal
     */

    public static final class StatsHolder {
        CounterMetric current = new CounterMetric();
        CounterMetric total = new CounterMetric();
        MeanMetric timing = new MeanMetric();
    }
}
