/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.source;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.cactoos.Text;
import org.cactoos.scalar.Solid;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.FormattedText;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * PostgreSQL data source, for unit testing.
 *
 * @since 0.2
 * @checkstyle ParameterNumberCheck (500 lines)
 */
public final class PgsqlSource implements DataSource {
    /**
     * Default port.
     */
    private static final int PORT = 5432;

    /**
     * Origin DataSource.
     */
    private final Unchecked<PGSimpleDataSource> origin;

    /**
     * Default username.
     */
    private final String uname;

    /**
     * Default password.
     */
    private final String pass;

    /**
     * Ctor.
     *
     * @param dbname Database name
     * @param username Default username
     * @param password Default password
     */
    public PgsqlSource(
        final String dbname,
        final String username,
        final String password
    ) {
        this("localhost", dbname, username, password);
    }

    /**
     * Ctor.
     *
     * @param hostname Server hostname or IPv4 Address
     * @param dbname Database name
     * @param username Default username
     * @param password Default password
     */
    public PgsqlSource(
        final String hostname,
        final String dbname,
        final String username,
        final String password
    ) {
        this(hostname, PgsqlSource.PORT, dbname, username, password);
    }

    /**
     * Ctor.
     *
     * @param hostname Server hostname or IPv4 Address
     * @param port Server port
     * @param dbname Database name
     * @param username Default username
     * @param password Default password
     */
    public PgsqlSource(
        final String hostname,
        final int port,
        final String dbname,
        final String username,
        final String password
    ) {
        this(
            new FormattedText(
                "jdbc:pgsql://%s:%d/%s",
                hostname,
                port,
                dbname
            ),
            username,
            password
        );
    }

    /**
     * Ctor.
     * @param url JDBC url
     * @param username Default username
     * @param password Default password
     */
    public PgsqlSource(
        final Text url,
        final String username,
        final String password
    ) {
        this.origin = new Unchecked<>(
            new Solid<>(
                () -> {
                    final PGSimpleDataSource pds = new PGSimpleDataSource();
                    pds.setUrl(url.asString());
                    return pds;
                }
            )
        );
        this.uname = username;
        this.pass = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.origin.value().getConnection(this.uname, this.pass);
    }

    @Override
    public Connection getConnection(
        final String username,
        final String password
    ) throws SQLException {
        return this.origin.value().getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() {
        return this.origin.value().getLogWriter();
    }

    @Override
    public void setLogWriter(final PrintWriter writer) throws SQLException {
        this.origin.value().setLogWriter(writer);
    }

    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        this.origin.value().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() {
        return this.origin.value().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.origin.value().getParentLogger();
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return this.origin.value().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return this.origin.value().isWrapperFor(iface);
    }
}
