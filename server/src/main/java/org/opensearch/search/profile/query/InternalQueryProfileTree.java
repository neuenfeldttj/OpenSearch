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

import org.apache.lucene.search.Query;
import org.opensearch.common.annotation.PublicApi;
import org.opensearch.search.profile.Metric;
import org.opensearch.search.profile.ProfileResult;
import org.opensearch.search.profile.Timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class returns a list of {@link ProfileResult} that can be serialized back to the client in the non-concurrent execution.
 *
 * @opensearch.internal
 */
@PublicApi(since="3.0.0")
public class InternalQueryProfileTree extends AbstractQueryProfileTree {

    @Override
    protected ProfileResult createProfileResult(String type, String description, AbstractQueryProfileBreakdown breakdown, List<ProfileResult> childrenProfileResults) {
        return new ProfileResult(
            type,
            description,
            breakdown.toBreakdownMap(),
            breakdown.getContextInstance().toString(),
            breakdown.toDebugMap(),
            breakdown.toNodeTime(),
            childrenProfileResults
        );
    }

    @Override
    protected AbstractQueryProfileBreakdown createProfileBreakdown(Query query) {
        Map<String, Class<? extends Metric>> metricClasses = new HashMap<>();
        for(QueryTimingType type : QueryTimingType.values()) {
            metricClasses.put(type.toString(), Timer.class);
        }
        return new QueryProfileBreakdown(metricClasses);
    }
}
