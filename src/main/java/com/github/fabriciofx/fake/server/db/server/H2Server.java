/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.server;

import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.Script;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.EmptySqlScript;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * H2 server, for unit testing.
 *
 * @since 0.0.1
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class H2Server implements Server<Connection> {
    /**
     * The Database name.
     */
    private final Unchecked<String> dbname;

    /**
     * SQL Script to initialize the database.
     */
    private final Script<Connection> script;

    /**
     * Ctor.
     */
    public H2Server() {
        this(new EmptySqlScript());
    }

    /**
     * Ctor.
     * @param script SqlScript to initialize the database.
     */
    public H2Server(final Script<Connection> script) {
        this.dbname = new Unchecked<>(
            new Sticky<>(
                () -> new RandomName().asString()
            )
        );
        this.script = script;
    }

    @Override
    public void start() throws Exception {
        this.script.run(this.resource());
    }

    @Override
    public void stop() throws Exception {
        try (Connection connection = this.resource()) {
            try (
                PreparedStatement shutdown = connection.prepareStatement(
                    "SHUTDOWN"
                )
            ) {
                shutdown.execute();
            }
        }
    }

    @Override
    public Connection resource() throws Exception {
        return new H2Source(this.dbname.value()).getConnection();
    }

    @Override
    public void close() throws IOException {
        try {
            this.stop();
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }
}
