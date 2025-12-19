/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.server;

import com.github.fabriciofx.fake.server.Server;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

/**
 * Fake MysqlServer tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.0.2
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle NestedTryDepthCheck (500 lines)
 */
final class MysqlServerTest {
    @Test
    void mustStartAndStop() throws Exception {
        try (Server<DataSource> server = new MysqlServer()) {
            server.start();
            server.stop();
        }
    }

    @Test
    void mustCheckConnection() throws Exception {
        try (Server<DataSource> server = new MysqlServer()) {
            server.start();
            final DataSource source = server.resource();
            try (Connection connection = source.getConnection()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("SELECT 1");
                }
            }
        }
    }
}
