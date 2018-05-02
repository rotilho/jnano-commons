package org.jnano;

import java.util.function.Supplier;

final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(supplier.get());
        }
    }
}
