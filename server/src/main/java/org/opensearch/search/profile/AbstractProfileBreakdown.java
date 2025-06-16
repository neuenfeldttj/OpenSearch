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

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * A record of timings for the various operations that may happen during query execution.
 * A node's time may be composed of several internal attributes (rewriting, weighting,
 * scoring, etc).
 *
 * @opensearch.internal
 */
public abstract class AbstractProfileBreakdown {

    private final Map<String, Metric> metrics;

    /** Sole constructor. */
    public AbstractProfileBreakdown(Map<String, Class<? extends Metric>> metricClasses) {
        Map<String, Metric> map = new HashMap<>();
        for(Map.Entry<String, Class<? extends Metric>> entry : metricClasses.entrySet()) {
            try {
                map.put(entry.getKey(), entry.getValue().getConstructor(String.class).newInstance(entry.getKey()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.metrics = map;
    }

    public Metric getMetric(String name) {
        return metrics.get(name);
    }

    /**
     * Build a breakdown for current instance
     */
    public Map<String, Long> toBreakdownMap() {
        Map<String, Long> map = new HashMap<>();
        for(Map.Entry<String, Metric> entry : metrics.entrySet()) {
            map.putAll(entry.getValue().toBreakdownMap());
        }
        return map;
    }

    public long toNodeTime() {
        long total = 0;
        for(Map.Entry<String, Metric> entry : metrics.entrySet()) {
            if(entry.getValue() instanceof Timer) {
                total += ((Timer) entry.getValue()).getApproximateTiming();
            }
        }
        return total;
    }


    /**
     * Fetch extra debugging information.
     */
    public Map<String, Object> toDebugMap() {
        return emptyMap();
    }

}
