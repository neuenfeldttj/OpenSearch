/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.search.profile.query;

import org.apache.lucene.search.Query;
import org.opensearch.search.profile.AbstractProfileBreakdown;
import org.opensearch.search.profile.AbstractProfileBreakdown;
import org.opensearch.search.profile.Metric;
import org.opensearch.search.profile.Timer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * A {@link AbstractProfileBreakdown} for query timings.
 */
public class QueryProfileBreakdown extends AbstractQueryProfileBreakdown {

    public QueryProfileBreakdown(List<Metric> pluginMetrics) {
        super(pluginMetrics); // TODO: add query timers somewhere
    }

    @Override
    public Map<String, Long> toImportantMetricsMap() {
        Map<String, Long> map = new HashMap<>();
        map.put(NODE_TIME_RAW, toNodeTime());
        return map;
    }

    @Override
    public AbstractQueryProfileBreakdown context(Object context) {
        return this;
    }
}
