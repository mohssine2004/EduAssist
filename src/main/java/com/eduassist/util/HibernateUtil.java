package com.eduassist.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Objects;

/**
 * Utility to build a Hibernate SessionFactory for local/dev usage.
 * It reads common environment variables if present, otherwise uses sensible defaults.
 * Note: in a Java EE container your application likely uses container-managed EntityManager (@PersistenceContext)
 * and does not need this class — this is provided for local scripts/tests or standalone use.
 */
public final class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private HibernateUtil() {}

    private static SessionFactory buildSessionFactory() {
        // Allow overriding via environment variables for portability
        String url = System.getenv("JDBC_URL");
        String user = System.getenv("JDBC_USER");
        String pass = System.getenv("JDBC_PASS");

        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

        if (!Objects.isNull(url) && !Objects.isNull(user)) {
            // If env vars provided, apply them
            registryBuilder
                    .applySetting("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .applySetting("hibernate.connection.url", url)
                    .applySetting("hibernate.connection.username", user)
                    .applySetting("hibernate.connection.password", pass == null ? "" : pass)
                    .applySetting("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
                    .applySetting("hibernate.hbm2ddl.auto", "update")
                    .applySetting("hibernate.show_sql", "false");
        } else {
            // Defaults compatible with persistence.xml values (for local dev)
            registryBuilder
                    .applySetting("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .applySetting("hibernate.connection.url", "jdbc:mysql://localhost:3306/eduassist_db?createDatabaseIfNotExist=true&serverTimezone=UTC")
                    .applySetting("hibernate.connection.username", "root")
                    .applySetting("hibernate.connection.password", "root")
                    .applySetting("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
                    .applySetting("hibernate.hbm2ddl.auto", "update")
                    .applySetting("hibernate.show_sql", "false");
        }

        final StandardServiceRegistry registry = registryBuilder.build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

