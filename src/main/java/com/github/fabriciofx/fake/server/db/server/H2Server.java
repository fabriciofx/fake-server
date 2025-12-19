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
import javax.sql.DataSource;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

/**
 * H2 server, for unit testing.
 *
 * @since 0.0.1
 * @checkstyle IllegalCatchCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class H2Server implements Server<DataSource> {
    /**
     * The Database source.
     */
    private final Scalar<DataSource> source;

    /**
     * SQL Script to initialize the database.
     */
    private final Script<DataSource> script;

    /**
     * Ctor.
     */
    public H2Server() {
        this(new EmptySqlScript());
    }

    /**
     * Ctor.
     *
     * @param script SqlScript to initialize the database.
     */
    public H2Server(final Script<DataSource> script) {
        this.source = new Sticky<>(
            () -> new H2Source(new RandomName().asString())
        );
        this.script = script;
    }

    @Override
    public void start() throws Exception {
        this.script.run(this.resource());
    }

    @Override
    public void stop() throws Exception {
        try (Connection connection = this.resource().getConnection()) {
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
    public DataSource resource() throws Exception {
        return this.source.value();
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
