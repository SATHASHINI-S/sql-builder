package com.techatpark;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Basic Setup for Test cases.
 */
class BaseTest {
    protected final JdbcDataSource dataSource;

    BaseTest() {
        try {
            // Setup
            dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:file:./target/sampledb");
            dataSource.setUser("sa");
            dataSource.setPassword("");

            RunScript.execute(dataSource.getConnection(),
                    new FileReader(Objects.requireNonNull(getClass().getClassLoader()
                            .getResource("init.sql")).getFile()));

        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        SqlBuilder.prepareSql("""
                TRUNCATE TABLE movie
                """)
                .execute(dataSource);
    }

    public static Movie mapMovie(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getShort(1),
                rs.getString(2),
                rs.getString(3));
    }

}
