/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.search.profile.query;

import org.apache.lucene.search.Query;
import org.opensearch.common.annotation.PublicApi;
import org.opensearch.search.profile.AbstractProfiler;

import java.util.Objects;

/**
 * Base class for all query profilers
 */
@PublicApi(since = "3.0.0")
public abstract class AbstractQueryProfiler extends AbstractProfiler<AbstractQueryProfileBreakdown, Query, QueryProfileShardResult> {

    /**
     * The root Collector used in the search
     */
    private InternalProfileComponent collector;

    public AbstractQueryProfiler(AbstractQueryProfileTree profileTree) {
        super(profileTree);
    }

    /** Set the collector that is associated with this profiler. */
    public void setCollector(InternalProfileComponent collector) {
        if (this.collector != null) {
            throw new IllegalStateException("The collector can only be set once.");
        }
        this.collector = Objects.requireNonNull(collector);
    }

    /**
     * Begin timing the rewrite phase of a request.  All rewrites are accumulated together into a
     * single metric
     */
    public abstract void startRewriteTime();

    /**
     * Stop recording the current rewrite and add it's time to the total tally, returning the
     * cumulative time so far.
     */
    public abstract void stopAndAddRewriteTime();

    /**
     * The rewriting process is complex and hard to display because queries can undergo significant changes.
     * Instead of showing intermediate results, we display the cumulative time for the non-concurrent search case.
     * @return total time taken to rewrite all queries in this profile
     */
    public abstract long getRewriteTime();

    /**
     * Return the current root Collector for this search
     */
    public CollectorResult getCollector() {
        return collector.getCollectorTree();
    }

    @Override
    public QueryProfileShardResult createProfileShardResult() {
        return new QueryProfileShardResult(getTree(), getRewriteTime(), getCollector());
    }
}
