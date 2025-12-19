/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server;

import java.io.IOException;

/**
 * Server.
 *
 * <p>Represents a server.</p>
 * @param <T> The resource type provided by the server
 * @since 0.0.1
 */
public interface Server<T> extends AutoCloseable {
    /**
     * Start the server.
     * @throws Exception If fails
     */
    void start() throws Exception;

    /**
     * Stop the server.
     * @throws Exception If fails
     */
    void stop() throws Exception;

    /**
     * Create a resource from server.
     * @return A resource T
     * @throws Exception if fails
     */
    T resource() throws Exception;

    @Override
    void close() throws IOException;
}
