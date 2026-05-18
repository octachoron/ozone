package org.apache.hadoop.ozone.admin.upgrade;

import java.io.IOException;
import java.util.concurrent.Callable;
import org.apache.hadoop.hdds.cli.AbstractSubcommand;
import org.apache.hadoop.hdds.cli.HddsVersionProvider;
import org.apache.hadoop.ozone.admin.om.OmAddressOptions;
import org.apache.hadoop.ozone.om.protocol.OzoneManagerProtocol;
import picocli.CommandLine;

@CommandLine.Command(
    name = "om-status",
    description = "Show a simplified view of whether the OM cluster needs finalization",
    mixinStandardHelpOptions = true,
    versionProvider = HddsVersionProvider.class)
public class OmStatusSubCommand extends AbstractSubcommand implements Callable<Void> {

  @CommandLine.Mixin
  private OmAddressOptions.OptionalServiceIdOrHostMixin omAddressOptions;

  protected OzoneManagerProtocol newOmClient() throws IOException {
    return omAddressOptions.newClient();
  }

  @Override
  public Void call() throws Exception {
    try (OzoneManagerProtocol omClient = newOmClient()) {
      boolean omFinalized = omClient.getUpgradeStatus();
      out().println("OM Finalized: " + omFinalized);
    } catch (Exception e) {
      out().println("Failed to get OM Finalized status: " + e.getMessage());
    }

    return null;
  }
}
