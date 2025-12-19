/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server;

/**
 * Script.
 *
 * <p>There is no thread-safety guarantee.
 * @param <T> The resource type when script will run
 * @since 0.0.1
 */
@FunctionalInterface
public interface Script<T> {
    /**
     * Execute this Script on the resource.
     * @param resource The resource
     * @throws Exception if fails
     */
    void run(T resource) throws Exception;
}
