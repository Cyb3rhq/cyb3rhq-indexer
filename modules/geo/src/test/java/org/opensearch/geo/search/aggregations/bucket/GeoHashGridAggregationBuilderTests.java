/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.geo.search.aggregations.bucket;

import org.opensearch.LegacyESVersion;
import org.opensearch.Version;
import org.opensearch.common.geo.GeoBoundingBox;
import org.opensearch.common.geo.GeoPoint;
import org.opensearch.common.io.stream.BytesStreamOutput;
import org.opensearch.core.common.io.stream.NamedWriteableAwareStreamInput;
import org.opensearch.core.common.io.stream.NamedWriteableRegistry;
import org.opensearch.core.common.io.stream.StreamInput;
import org.opensearch.geo.GeoModulePlugin;
import org.opensearch.geo.search.aggregations.bucket.geogrid.GeoGridAggregationBuilder;
import org.opensearch.geo.search.aggregations.bucket.geogrid.GeoHashGridAggregationBuilder;
import org.opensearch.geo.tests.common.RandomGeoGenerator;
import org.opensearch.plugins.Plugin;
import org.opensearch.search.aggregations.BaseAggregationTestCase;
import org.opensearch.test.VersionUtils;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;

public class GeoHashGridAggregationBuilderTests extends BaseAggregationTestCase<GeoGridAggregationBuilder> {

    protected Collection<Class<? extends Plugin>> getPlugins() {
        return Collections.singletonList(GeoModulePlugin.class);
    }

    @Override
    protected GeoHashGridAggregationBuilder createTestAggregatorBuilder() {
        String name = randomAlphaOfLengthBetween(3, 20);
        GeoHashGridAggregationBuilder factory = new GeoHashGridAggregationBuilder(name);
        factory.field("foo");
        if (randomBoolean()) {
            int precision = randomIntBetween(1, 12);
            factory.precision(precision);
        }
        if (randomBoolean()) {
            factory.size(randomIntBetween(1, Integer.MAX_VALUE));
        }
        if (randomBoolean()) {
            factory.shardSize(randomIntBetween(1, Integer.MAX_VALUE));
        }
        if (randomBoolean()) {
            factory.setGeoBoundingBox(RandomGeoGenerator.randomBBox());
        }
        return factory;
    }

    public void testSerializationPreBounds() throws Exception {
        Version noBoundsSupportVersion = VersionUtils.randomVersionBetween(random(), LegacyESVersion.V_7_0_0, LegacyESVersion.V_7_5_0);
        GeoHashGridAggregationBuilder builder = createTestAggregatorBuilder();
        try (BytesStreamOutput output = new BytesStreamOutput()) {
            output.setVersion(LegacyESVersion.V_7_6_0);
            builder.writeTo(output);
            try (
                StreamInput in = new NamedWriteableAwareStreamInput(
                    output.bytes().streamInput(),
                    new NamedWriteableRegistry(Collections.emptyList())
                )
            ) {
                in.setVersion(noBoundsSupportVersion);
                GeoHashGridAggregationBuilder readBuilder = new GeoHashGridAggregationBuilder(in);
                assertThat(
                    readBuilder.geoBoundingBox(),
                    equalTo(new GeoBoundingBox(new GeoPoint(Double.NaN, Double.NaN), new GeoPoint(Double.NaN, Double.NaN)))
                );
            }
        }
    }
}
