package org.apache.hadoop.hdds.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.hadoop.hdds.utils.MetricPriority;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExtendedMetricTag {
  MetricPriority priority();
  String numerator() default "";
}
