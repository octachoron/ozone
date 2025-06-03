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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hdds.annotation.ExtendedMetricTag;
import org.apache.hadoop.metrics2.annotation.Metric;
import org.apache.hadoop.metrics2.annotation.Metrics;
import org.apache.hadoop.metrics2.lib.MutableCounter;
import org.apache.hadoop.metrics2.lib.MutableGauge;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public class MetricScanner {

  private static final String DEFAULT_OUTPUT_FILE = "gen-metrics.txt";

  private static final Map<Class<?>, Metric.Type> METRIC_TYPES = ImmutableMap.of(
      MutableCounter.class, Metric.Type.COUNTER,
      MutableGauge.class, Metric.Type.GAUGE);

  private static String serviceName;

  private static void scan(OutputStream outputStream, String[] packages) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    Reflections reflections = new Reflections(
        new ConfigurationBuilder()
            .forPackages(packages)
            .setScanners(Scanners.FieldsAnnotated));

    List<SchemaMetric> metricInfos = new ArrayList<>();

    Set<Field> metrics = reflections.getFieldsAnnotatedWith(ExtendedMetricTag.class);
    for (Field field : metrics) {
      metricInfos.add(buildSchemaMetric(field));
    }

    writer.writeValue(outputStream, metricInfos);
  }

  private static Metric.Type getMetricType(Field field) {
    Class<?> fieldType = field.getType();

    for (Map.Entry<Class<?>, Metric.Type> entry : METRIC_TYPES.entrySet()) {
      if (entry.getKey().isAssignableFrom(fieldType)) {
        return entry.getValue();
      }
    }

    // TODO: maybe log it?
    return Metric.Type.DEFAULT;
  }

  private static String getNumerator(ExtendedMetricTag annotation, Field field) {
    Metric.Type type = getMetricType(field);

    String numerator = annotation.numerator();
    if (StringUtils.isEmpty(numerator)) {
      numerator = type == Metric.Type.COUNTER ? "count" : "dimensionless";
    }

    return numerator;
  }

  private static String getDescription(Field metricField) {
    Metric metricAnnotation = metricField.getAnnotation(Metric.class);

    if (StringUtils.isNotBlank(metricAnnotation.about())) {
      return metricAnnotation.about();
    }

    String[] value = metricAnnotation.value();
    // Get the description.
    if (value.length == 1) {
      return value[0];
    } else if (value.length == 2) {
      return value[1];
    }

    return String.format(
        "%s.%s",
        metricField.getDeclaringClass().getSimpleName(),
        metricField.getName());
  }

  private static SchemaMetric buildSchemaMetric(Field field) {
    ExtendedMetricTag annotation = field.getAnnotation(ExtendedMetricTag.class);
    assert annotation != null; //FIXME
    Class<?> metricsClass = field.getDeclaringClass();
    Metrics metricsAnnotation = metricsClass.getAnnotation(Metrics.class);
    assert metricsAnnotation != null; //FIXME

    String beanName = String.format(
        "Hadoop:service=%s,name=%s",
        serviceName,
        metricsClass.getSimpleName());

    SchemaMetric schemaMetric = new SchemaMetric(
        beanName,
        metricsClass.getSimpleName(),
        field.getName(),
        getNumerator(annotation, field),
        getMetricType(field),
        annotation.priority(),
        getDescription(field));

    return schemaMetric;
  }

  private static OutputStream openOutput() throws IOException {
    return Files.newOutputStream(Paths.get(DEFAULT_OUTPUT_FILE));
  }

  private static void usage() {
    System.err.printf(
        "Usage: java -cp {classpath} %s {service name}%n [package...]",
        MetricScanner.class.getName());
    System.exit(1);
  }

  public static void main(String[] args) throws IOException {
    try {
      serviceName = args[0];
    } catch(ArrayIndexOutOfBoundsException e) {
      usage();
    }

    String[] packages = Arrays.copyOfRange(args, 1, args.length);

    try (OutputStream outputStream = openOutput()) {
      scan(outputStream, packages);
    }
  }
}
