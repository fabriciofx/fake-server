/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.script;

import com.github.fabriciofx.fake.server.Script;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import org.cactoos.Input;
import org.cactoos.Text;
import org.cactoos.io.InputOf;

/**
 * Read and execute a SQL Script.
 *
 * @since 0.0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class SqlScript implements Script<Connection> {
    /**
     * Input.
     */
    private final Input input;

    /**
     * Ctor.
     *
     * @param sql The SQL code
     */
    public SqlScript(final Text sql) {
        this(new InputOf(sql));
    }

    /**
     * Ctor.
     *
     * @param npt The SQL Script input
     */
    public SqlScript(final Input npt) {
        this.input = npt;
    }

    /**
     * Execute this Script on the connection.
     *
     * @param connection The connection
     * @throws Exception if fails
     */
    public void run(final Connection connection) throws Exception {
        final List<String> lines = new LinkedList<>();
        try (
            Reader reader = new InputStreamReader(
                this.input.stream(),
                StandardCharsets.UTF_8
            )
        ) {
            try (LineNumberReader liner = new LineNumberReader(reader)) {
                while (true) {
                    final String line = liner.readLine();
                    if (line == null) {
                        break;
                    }
                    final String trimmed = line.trim();
                    if (!trimmed.isEmpty()
                        && !trimmed.startsWith("--")
                        && !trimmed.startsWith("//")) {
                        lines.add(trimmed);
                    }
                }
            }
        }
        final StringJoiner joiner = new StringJoiner(" ");
        for (final String line : lines) {
            joiner.add(line);
        }
        final String[] cmds = joiner.toString().split(";");
        try (Statement stmt = connection.createStatement()) {
            for (final String cmd : cmds) {
                final String sql = cmd.trim();
                stmt.execute(sql);
            }
        }
    }
}
