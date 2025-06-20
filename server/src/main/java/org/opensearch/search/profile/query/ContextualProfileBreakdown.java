/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.search.profile.query;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Query;
import org.opensearch.common.annotation.PublicApi;
import org.opensearch.search.profile.AbstractProfileBreakdown;
import org.opensearch.search.profile.Metric;
import org.opensearch.search.profile.Timer;

import java.util.List;
import java.util.Map;

/**
 * A {@link AbstractProfileBreakdown} for query timings with contexts.
 *
 * @opensearch.internal
 */
@PublicApi(since = "3.0.0")
public abstract class ContextualProfileBreakdown extends AbstractProfileBreakdown {

    private Query query;

    public abstract ContextualProfileBreakdown context(Object context);

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }

    public void associateCollectorToLeaves(Collector collector, LeafReaderContext leaf) {}

    public void associateCollectorsToLeaves(Map<Collector, List<LeafReaderContext>> collectorToLeaves) {}
}
