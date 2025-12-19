/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db;

import com.github.fabriciofx.fake.server.Script;
import com.github.fabriciofx.fake.server.Servers;
import com.github.fabriciofx.fake.server.db.script.SqlScript;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.github.fabriciofx.fake.server.db.server.MysqlServer;
import com.github.fabriciofx.fake.server.db.server.PgsqlServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import org.cactoos.Text;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Select tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle NestedTryDepthCheck (500 lines)
 */
@SuppressWarnings("PMD.NestedTryDepthCheck")
final class SelectTest {
    @Test
    void select() throws Exception {
        final Script<DataSource> script = new SqlScript(
            new Joined(
                " ",
                "CREATE TABLE employee (id INT,",
                "name VARCHAR(50), birthday DATE,",
                "address VARCHAR(100),",
                "married BOOLEAN, salary DECIMAL(20,2),",
                "PRIMARY KEY (id))"
            )
        );
        try (
            Servers<DataSource> servers = new Servers<>(
                new H2Server(script),
                new MysqlServer(script),
                new PgsqlServer(script)
            )
        ) {
            for (final DataSource source : servers.resources()) {
                try (Connection connection =  source.getConnection()) {
                    try (PreparedStatement insert = connection.prepareStatement(
                        new Joined(
                            " ",
                            "INSERT INTO employee",
                            "(id, name, birthday, address, married, salary)",
                            "VALUES (1, 'John Wick', '1980-08-15',",
                            "'Boulevard Street, 34', false, 13456.00)"
                        ).asString()
                    )) {
                        insert.execute();
                    }
                    try (PreparedStatement insert = connection.prepareStatement(
                        new Joined(
                            " ",
                            "INSERT INTO employee",
                            "(id, name, birthday, address, married, salary)",
                            "VALUES (2, 'Adam Park', '1985-07-09',",
                            "'Sunset Place, 14', true, 12345.00)"
                        ).asString()
                    )) {
                        insert.execute();
                    }
                    try (PreparedStatement select = connection.prepareStatement(
                        "SELECT * FROM employee"
                    )) {
                        try (ResultSet rset = select.executeQuery()) {
                            if (rset.next()) {
                                final Text john = new FormattedText(
                                    "%d:%s:%s:%s:%b:%s",
                                    rset.getInt(1),
                                    rset.getString(2),
                                    rset.getDate(3),
                                    rset.getString(4),
                                    rset.getBoolean(5),
                                    rset.getBigDecimal(6).toString()
                                );
                                rset.next();
                                Assertions.assertEquals(
                                    "1:John Wick:1980-08-15:Boulevard Street, 34:false:13456.00",
                                    john.asString()
                                );
                            }
                            if (rset.next()) {
                                final Text adam = new FormattedText(
                                    "%d:%s:%s:%s:%b:%s",
                                    rset.getInt(1),
                                    rset.getString(2),
                                    rset.getDate(3),
                                    rset.getString(4),
                                    rset.getBoolean(5),
                                    rset.getBigDecimal(6).toString()
                                );
                                Assertions.assertEquals(
                                    "2:Adam Park:1985-07-09:Sunset Place, 14:true:12345.00",
                                    adam.asString()
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}
