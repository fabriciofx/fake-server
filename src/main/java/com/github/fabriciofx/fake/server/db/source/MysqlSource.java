/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.source;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.cactoos.Text;
import org.cactoos.scalar.Solid;
import org.cactoos.scalar.Unchecked;
import org.cactoos.text.Concatenated;
import org.cactoos.text.FormattedText;

/**
 * MySQL datasource, for unit testing.
 *
 * @since 0.0.1
 * @checkstyle ParameterNumberCheck (500 lines)
 */
public final class MysqlSource implements DataSource {
    /**
     * Default port.
     */
    private static final int PORT = 3306;

    /**
     * Origin DataSource.
     */
    private final Unchecked<MysqlDataSource> origin;

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
    public MysqlSource(
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
    public MysqlSource(
        final String hostname,
        final String dbname,
        final String username,
        final String password
    ) {
        this(hostname, MysqlSource.PORT, dbname, username, password);
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
    public MysqlSource(
        final String hostname,
        final int port,
        final String dbname,
        final String username,
        final String password
    ) {
        this(
            new FormattedText(
                "jdbc:mysql://%s:%d/%s",
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
     *
     * @param url JDBC url
     * @param username Default username
     * @param password Default password
     */
    public MysqlSource(
        final Text url,
        final String username,
        final String password
    ) {
        this.origin = new Unchecked<>(
            new Solid<>(
                () -> {
                    final MysqlDataSource mds = new MysqlDataSource();
                    mds.setUrl(
                        new Concatenated(
                            url.asString(),
                            "?useSSL=false&useTimezone=true&serverTimezone=UTC"
                        ).asString()
                    );
                    return mds;
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
