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

package org.opensearch.wildfly.transport;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.SuppressForbidden;
import org.opensearch.common.io.PathUtils;

import java.nio.file.Path;

import jakarta.enterprise.inject.Produces;

@SuppressWarnings("unused")
public final class RestHighLevelClientProducer {

    @Produces
    public RestHighLevelClient createRestHighLevelClient() {
        String httpUri = System.getProperty("opensearch.uri");

        return new RestHighLevelClient(RestClient.builder(HttpHost.create(httpUri)));
    }

    @SuppressForbidden(reason = "get path not configured in environment")
    private Path getPath(final String OpenSearchProperties) {
        return PathUtils.get(OpenSearchProperties);
    }
}
