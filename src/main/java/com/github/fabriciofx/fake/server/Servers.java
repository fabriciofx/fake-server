/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.cactoos.list.ListOf;

/**
 * Represents a set of servers.
 *
 * @param <T> The type of resource provided by servers
 * @since 0.0.1
 */
@SuppressWarnings("PMD.AvoidCatchingGenericException")
public final class Servers<T> implements Closeable {
    /**
     * All servers.
     */
    private final List<Server<T>> all;

    /**
     * Ctor.
     * @param srvs Servers.
     */
    @SafeVarargs
    public Servers(final Server<T>... srvs) {
        this.all = new ListOf<>(srvs);
    }

    /**
     * Get a list of valid resources.
     * @return A list of resources
     * @throws Exception if fails
     */
    public Iterable<T> resources() throws Exception {
        final List<T> resources = new LinkedList<>();
        for (final Server<T> server : this.all) {
            server.start();
            resources.add(server.resource());
        }
        return resources;
    }

    @Override
    public void close() throws IOException {
        try {
            for (final Server<T> server : this.all) {
                server.stop();
            }
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }
}
