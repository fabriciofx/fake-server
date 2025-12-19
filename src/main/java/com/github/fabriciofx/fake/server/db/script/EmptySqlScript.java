/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server.db.script;

import com.github.fabriciofx.fake.server.Script;
import java.sql.Connection;

/**
 * An empty SQL Script.
 *
 * @since 0.0.1
 */
public final class EmptySqlScript implements Script<Connection> {
    @Override
    public void run(final Connection connection) throws Exception {
        // Intended empty.
    }
}
