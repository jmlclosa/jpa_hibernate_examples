package com.jmlclosa.jpaexamples.multibag;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HibernateSessionFactoryBuilder extends ExternalResource {

    private final List<String> sqlFiles;
    private final List<Class>  annotatedClasses;
    private       Session      session;

    public HibernateSessionFactoryBuilder(String additionalSqlFiles) {
        this.sqlFiles = new ArrayList<>();
        this.sqlFiles.add("sql/load_script.sql");
        this.sqlFiles.add(additionalSqlFiles);
        this.annotatedClasses = Collections.emptyList();
    }

    public HibernateSessionFactoryBuilder(String additionalSqlFiles, List<Class> annotatedClasses) {
        this.sqlFiles = new ArrayList<>();
        this.sqlFiles.add(additionalSqlFiles);
        this.annotatedClasses = annotatedClasses;
    }

    public void before() throws Throwable {
        Configuration configuration = new Configuration();

        for (Class c : annotatedClasses) {
            configuration.addAnnotatedClass(c);
        }

        configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.H2Dialect");
        configuration.setProperty(AvailableSettings.DRIVER, "org.h2.Driver");
        configuration.setProperty(AvailableSettings.URL, "jdbc:h2:mem:test");
        configuration.setProperty(AvailableSettings.SHOW_SQL, "true");
        configuration.setProperty(AvailableSettings.GENERATE_STATISTICS, "true");
//        configuration.setProperty(AvailableSettings.FORMAT_SQL, "true");
        configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "create");
        configuration.setProperty(AvailableSettings.HBM2DDL_IMPORT_FILES, String.join(",", this.sqlFiles));
        configuration.setProperty(AvailableSettings.HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR,
                "org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor");

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        this.session = sessionFactory.openSession();
    }

    public Session getSession() {
        return session;
    }

    public void doInTransaction(Runnable codeToExecute) {
        this.session.beginTransaction();
        codeToExecute.run();
        this.session.getTransaction().commit();
    }

    @Override
    protected void after() {
        if (this.session != null) {
            this.session.close();
        }
    }
}