/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.server;

import com.github.fabriciofx.fake.server.Script;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.EmptySqlScript;
import com.github.fabriciofx.fake.server.db.source.PgsqlSource;
import java.io.IOException;
import javax.sql.DataSource;
import org.cactoos.text.TextOf;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * PostgreSQL server, for unit testing.
 *
 * @since 0.0.1
 */
public final class PgsqlServer implements Server<DataSource> {
    /**
     * The container.
     */
    private final JdbcDatabaseContainer<?> container;

    /**
     * SQL Script to initialize the database.
     */
    private final Script<DataSource> script;

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
    public PgsqlServer(final Script<DataSource> script) {
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
    public DataSource resource() throws Exception {
        return new PgsqlSource(
            new TextOf(this.container.getJdbcUrl()),
            this.container.getUsername(),
            this.container.getPassword()
        );
    }

    @Override
    public void close() throws IOException {
        this.container.stop();
    }
}
