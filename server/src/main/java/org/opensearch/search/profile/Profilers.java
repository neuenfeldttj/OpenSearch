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

package org.opensearch.search.profile;

import org.apache.lucene.search.Query;
import org.opensearch.common.annotation.PublicApi;
import org.opensearch.search.internal.ContextIndexSearcher;
import org.opensearch.search.profile.aggregation.AggregationProfiler;
import org.opensearch.search.profile.aggregation.ConcurrentAggregationProfiler;
import org.opensearch.search.profile.query.*;

import java.util.*;

/**
 * Wrapper around all the profilers that makes management easier.
 *
 * @opensearch.api
 */
@PublicApi(since = "1.0.0")
public final class Profilers {

    private final ContextIndexSearcher searcher;
    private final List<AbstractQueryProfiler> queryProfilers;
    private final AggregationProfiler aggProfiler;
    private final boolean isConcurrentSegmentSearchEnabled;
    private final List<AbstractQueryProfiler> pluginProfilers;

    public static Map<Query, Set<AbstractQueryProfileBreakdown>> queriesToBreakdowns = new HashMap<>();

    /** Sole constructor. This {@link Profilers} instance will initially wrap one {@link QueryProfiler}. */
    public Profilers(ContextIndexSearcher searcher, boolean isConcurrentSegmentSearchEnabled) {
        this.searcher = searcher;
        this.isConcurrentSegmentSearchEnabled = isConcurrentSegmentSearchEnabled;
        this.queryProfilers = new ArrayList<>();
        this.aggProfiler = isConcurrentSegmentSearchEnabled ? new ConcurrentAggregationProfiler() : new AggregationProfiler();
        addQueryProfiler();
        this.pluginProfilers = new ArrayList<>();
        this.searcher.setPluginProfilers(pluginProfilers);
    }

    /** Switch to a new profile. */
    public AbstractQueryProfiler addQueryProfiler() {
        AbstractQueryProfiler profiler = isConcurrentSegmentSearchEnabled
            ? new ConcurrentQueryProfiler(QueryProfileBreakdown.class)
            : new QueryProfiler(QueryProfileBreakdown.class);
        searcher.setQueryProfiler(profiler);
        queryProfilers.add(profiler);
        return profiler;
    }

    public void addPluginProfiler(AbstractQueryProfiler pluginProfiler) {
        pluginProfilers.add(pluginProfiler);
    }

    public List<AbstractQueryProfiler> getPluginProfilers() {
        return Collections.unmodifiableList(pluginProfilers);
    }

    /** Get the current profiler. */
    public AbstractQueryProfiler getCurrentQueryProfiler() {
        return queryProfilers.getLast();
    }

    /** Return the list of all created {@link QueryProfiler}s so far. */
    public List<AbstractQueryProfiler> getQueryProfilers() {
        return Collections.unmodifiableList(queryProfilers);
    }

    /** Return the {@link AggregationProfiler}. */
    public AggregationProfiler getAggregationProfiler() {
        return aggProfiler;
    }

}
