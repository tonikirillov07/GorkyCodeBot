package org.ds.db.config;

import org.ds.db.DatabaseInitializer;
import org.ds.db.entity.UserEntity;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {
    /*
    TODO: Подключить базу данных
     */

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
                .addAnnotatedClass(UserEntity.class)
                .addPackage("org.ds")
                .setProperty("hibernate.connection.url", "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10804563?connectTimeout=10000&socketTimeout=30000")
                .setProperty("hibernate.connection.username", "sql10804563")
                .setProperty("hibernate.connection.password", "hMCGNGqREk")
                .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop");

        return configuration.buildSessionFactory();

    }
}
