package org.apache.hadoop.hdds.metrics;

import org.apache.hadoop.hdds.utils.MetricPriority;
import org.apache.hadoop.metrics2.annotation.Metric;

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
