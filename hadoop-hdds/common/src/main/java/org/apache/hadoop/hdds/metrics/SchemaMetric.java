/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hdds.metrics;

import org.apache.hadoop.hdds.utils.MetricPriority;
import org.apache.hadoop.metrics2.annotation.Metric;

/**
 * Representation of a generated static metric definition.
 *
 * @see org.apache.hadoop.hdds.metrics.MetricScanner
 */
public class SchemaMetric {
  private final String bean;
  private final String className;
  private final String field;
  private final String numerator;
  private final Metric.Type type;
  private final MetricPriority priority;
  private final String description;

  // TODO: builder?

  public SchemaMetric(
      String bean,
      String className,
      String field,
      String numerator,
      Metric.Type type,
      MetricPriority priority,
      String description) {
    this.bean = bean;
    this.className = className;
    this.field = field;
    this.numerator = numerator;
    this.type = type;
    this.priority = priority;
    this.description = description;
  }

  public String getBean() {
    return bean;
  }

  public String getClassName() {
    return className;
  }

  public String getField() {
    return field;
  }

  public String getNumerator() {
    return numerator;
  }

  public Metric.Type getType() {
    return type;
  }

  public MetricPriority getPriority() {
    return priority;
  }

  public String getDescription() {
    return description;
  }
}
