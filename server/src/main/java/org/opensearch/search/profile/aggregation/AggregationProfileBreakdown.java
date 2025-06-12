/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.search.profile.aggregation;

import org.opensearch.common.annotation.PublicApi;
import org.opensearch.search.profile.AbstractProfileBreakdown;
import org.opensearch.search.profile.Metric;
import org.opensearch.search.profile.Timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * A profile breakdown for aggregations.
 */
@PublicApi(since = "3.0.0")
public class AggregationProfileBreakdown extends AbstractProfileBreakdown {
    private final Map<String, Object> extra = new HashMap<>();

    public AggregationProfileBreakdown(List<Metric> timers) {
        super(timers);
    }

    @Override
    public Map<String, Long> toImportantMetricsMap() {
        return Map.of();
    }

    /**
     * Add extra debugging information about the aggregation.
     */
    public void addDebugInfo(String key, Object value) {
        extra.put(key, value);
    }

    @Override
    public Map<String, Object> toDebugMap() {
        return unmodifiableMap(extra);
    }
}
