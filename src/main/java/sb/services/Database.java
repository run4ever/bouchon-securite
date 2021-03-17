package sb.services;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.core.JdbcTemplate;

public class Database {
   private static EmbeddedDatabase db = null;

   public static JdbcTemplate getDb()
   {
      if (db == null)
      {
         EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
         db = builder.setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:db/hsqldb/db.sql")
            .build();
      }
      return new JdbcTemplate(db);
   }
}
