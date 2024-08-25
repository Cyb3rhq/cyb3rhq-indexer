/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.common.cache.store.builders;

import org.opensearch.common.annotation.ExperimentalApi;
import org.opensearch.common.cache.ICache;
import org.opensearch.common.cache.RemovalListener;
import org.opensearch.common.settings.Settings;
import org.opensearch.common.unit.TimeValue;

import java.util.function.ToLongBiFunction;

/**
 * Builder for store aware cache.
 * @param <K> Type of key.
 * @param <V> Type of value.
 *
 * @opensearch.experimental
 */
@ExperimentalApi
public abstract class ICacheBuilder<K, V> {

    private long maxWeightInBytes;

    private ToLongBiFunction<K, V> weigher;

    private TimeValue expireAfterAcess;

    private Settings settings;

    private RemovalListener<K, V> removalListener;

    public ICacheBuilder() {}

    public ICacheBuilder<K, V> setMaximumWeightInBytes(long sizeInBytes) {
        this.maxWeightInBytes = sizeInBytes;
        return this;
    }

    public ICacheBuilder<K, V> setWeigher(ToLongBiFunction<K, V> weigher) {
        this.weigher = weigher;
        return this;
    }

    public ICacheBuilder<K, V> setExpireAfterAccess(TimeValue expireAfterAcess) {
        this.expireAfterAcess = expireAfterAcess;
        return this;
    }

    public ICacheBuilder<K, V> setSettings(Settings settings) {
        this.settings = settings;
        return this;
    }

    public ICacheBuilder<K, V> setRemovalListener(RemovalListener<K, V> removalListener) {
        this.removalListener = removalListener;
        return this;
    }

    public long getMaxWeightInBytes() {
        return maxWeightInBytes;
    }

    public TimeValue getExpireAfterAcess() {
        return expireAfterAcess;
    }

    public ToLongBiFunction<K, V> getWeigher() {
        return weigher;
    }

    public RemovalListener<K, V> getRemovalListener() {
        return this.removalListener;
    }

    public Settings getSettings() {
        return settings;
    }

    public abstract ICache<K, V> build();
}
