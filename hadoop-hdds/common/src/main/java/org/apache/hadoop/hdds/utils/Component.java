package org.apache.hadoop.hdds.utils;

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

  public String getJmxService() {
    return jmxService;
  }
}
