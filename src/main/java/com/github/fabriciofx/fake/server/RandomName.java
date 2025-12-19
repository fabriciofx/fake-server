/*
 * SPDX-FileCopyrightText: Copyright (C) 2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.fake.server;

import org.cactoos.Text;
import org.cactoos.text.Randomized;

/**
 * Random name for tests.
 *
 * @since 0.0.1
 */
public final class RandomName implements Text {
    /**
     * Name length.
     */
    private static final int LENGTH = 5;

    /**
     * Name.
     */
    private final Text name;

    /**
     * Ctor.
     */
    public RandomName() {
        this(
            new Randomized(
                RandomName.LENGTH,
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
            )
        );
    }

    /**
     * Ctor.
     * @param txt Name
     */
    public RandomName(final Text txt) {
        this.name = txt;
    }

    @Override
    public String asString() throws Exception {
        return this.name.asString();
    }
}
