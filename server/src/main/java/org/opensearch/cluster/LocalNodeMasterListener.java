/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.cluster;

import org.opensearch.common.annotation.DeprecatedApi;

/**
 * Enables listening to cluster-manager changes events of the local node (when the local node becomes the cluster-manager, and when the local
 * node cease being a cluster-manager).
 *
 * @opensearch.api
 * @deprecated As of 2.2, because supporting inclusive language, replaced by {@link LocalNodeClusterManagerListener}
 */
@Deprecated
@DeprecatedApi(since = "2.2.0")
public interface LocalNodeMasterListener extends LocalNodeClusterManagerListener {

    /**
     * Called when local node is elected to be the cluster-manager.
     * @deprecated As of 2.2, because supporting inclusive language, replaced by {@link #onClusterManager()}
     */
    @Deprecated
    void onMaster();

    /**
     * Called when the local node used to be the cluster-manager, a new cluster-manager was elected and it's no longer the local node.
     * @deprecated As of 2.2, because supporting inclusive language, replaced by {@link #offClusterManager()}
     */
    @Deprecated
    void offMaster();

    /**
     * Called when local node is elected to be the cluster-manager.
     */
    @Override
    default void onClusterManager() {
        onMaster();
    }

    /**
     * Called when the local node used to be the cluster-manager, a new cluster-manager was elected and it's no longer the local node.
     */
    @Override
    default void offClusterManager() {
        offMaster();
    }
}
