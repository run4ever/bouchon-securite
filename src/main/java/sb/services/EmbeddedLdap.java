package sb.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.sdk.LDAPException;

public class EmbeddedLdap {
   private static InMemoryListenerConfig listenerConfig = null;
   private static InMemoryDirectoryServerConfig serverConfig = null;
   private static InMemoryDirectoryServer server = null;
   private static Logger log;

   public static InMemoryDirectoryServer getServer() {
      if (log == null)
         log = LoggerFactory.getLogger("EmbeddedLdap");

      if (server == null) {
         log.info("Starting LDAP server");

         try {
            listenerConfig = InMemoryListenerConfig.createLDAPConfig("default", 10389);

            serverConfig = new InMemoryDirectoryServerConfig("dc=superbouchons,dc=org");
            serverConfig.setListenerConfigs(listenerConfig);
            serverConfig.addAdditionalBindCredentials("uid=admin,ou=system", "secret");
            serverConfig.setSchema(null);

            server = new InMemoryDirectoryServer(serverConfig);
            server.importFromLDIF(true, "src/main/resources/data.ldif");
            server.startListening();
            return server;
         }
         catch (LDAPException e) {
            log.info(e.getMessage());
            return null;
         }
      }
      return server;
   }
}
