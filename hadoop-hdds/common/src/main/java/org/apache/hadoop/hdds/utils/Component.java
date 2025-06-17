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

package org.apache.hadoop.hdds.utils;

/**
 * Represents a component of Ozone for the purpose of selecting relevant ones
 * in various contexts, and/or looking up basic metadata about them. AUTO instructs
 * an implementation reading the selection to infer it in a way it sees fit.
 */
public enum Component {
  AUTO(null),
  DATANODE("HddsDatanode"),
  HTTPFS_GATEWAY("HttpFSServer"),
  OM("OzoneManager"),
  RECON("Recon"),
  S3_GATEWAY("S3Gateway"),
  SCM("StorageContainerManager");

  private final String jmxService;

  Component(String jmxService) {
    this.jmxService = jmxService;
  }

  /**
   * Returns the string used as the "service" tag in the names of JMX MBeans
   * created from Metrics2 metrics.
   *
   * @return The service name string
   */
  public String getJmxService() {
    return jmxService;
  }
}
