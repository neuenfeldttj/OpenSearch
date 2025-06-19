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

package org.opensearch.search.profile.query;

import org.opensearch.common.annotation.PublicApi;

/**
 * This class acts as a thread-local storage for profiling a query.  It also
 * builds a representation of the query tree which is built constructed
 * "online" as the weights are wrapped by ContextIndexSearcher.  This allows us
 * to know the relationship between nodes in tree without explicitly
 * walking the tree or pre-wrapping everything
 * <p>
 * A Profiler is associated with every Search, not per Search-Request. E.g. a
 * request may execute two searches (query + global agg).  A Profiler just
 * represents one of those
 *
 * @opensearch.api
 */
@PublicApi(since = "1.0.0")
public class QueryProfiler extends AbstractQueryProfiler {

    public QueryProfiler(Class<? extends AbstractQueryProfileBreakdown> breakdownClass) {
        super(new InternalQueryProfileTree(breakdownClass));
    }

    public void startRewriteTime() {
        ((AbstractQueryProfileTree) profileTree).startRewriteTime();
    }

    public void stopAndAddRewriteTime() {
        ((AbstractQueryProfileTree) profileTree).stopAndAddRewriteTime();
    }

    public long getRewriteTime() {
        return ((AbstractQueryProfileTree) profileTree).getRewriteTime();
    }

    @Override
    public QueryProfileShardResult createProfileShardResult() {
        return new QueryProfileShardResult(getTree(), getRewriteTime(), getCollector());
    }
}
