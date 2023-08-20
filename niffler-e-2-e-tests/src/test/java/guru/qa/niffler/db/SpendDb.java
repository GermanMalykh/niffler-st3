package guru.qa.niffler.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class SpendDb {
    public static void removeCategory(String category) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/niffler-spend");
        dataSource.setUsername("postgres");
        dataSource.setPassword("secret");

        System.out.println("Executing query: DELETE FROM categories WHERE category = '" + category + "';");

        new JdbcTemplate(dataSource).execute("DELETE FROM categories WHERE category = '" + category + "';");
    }
}
