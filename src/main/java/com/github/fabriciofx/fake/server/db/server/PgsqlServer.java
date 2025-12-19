/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.server;

import com.github.fabriciofx.fake.server.Script;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.EmptySqlScript;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * PostgreSQL server, for unit testing.
 *
 * @since 0.2
 */
public final class PgsqlServer implements Server<Connection> {
    /**
     * The container.
     */
    private final JdbcDatabaseContainer<?> container;

    /**
     * SQL Script to initialize the database.
     */
    private final Script<Connection> script;

    /**
     * Ctor.
     */
    public PgsqlServer() {
        this(new EmptySqlScript());
    }

    /**
     * Ctor.
     *
     * @param script SQL Script to initialize the database
     */
    public PgsqlServer(final Script<Connection> script) {
        this.container = new PostgreSQLContainer<>("postgres:latest");
        this.script = script;
    }

    @Override
    public void start() throws Exception {
        this.container.start();
        this.script.run(this.resource());
    }

    @Override
    public void stop() throws Exception {
        this.container.stop();
    }

    @Override
    public Connection resource() throws Exception {
        return DriverManager.getConnection(
            this.container.getJdbcUrl(),
            this.container.getUsername(),
            this.container.getPassword()
        );
    }

    @Override
    public void close() throws IOException {
        this.container.stop();
    }
}
