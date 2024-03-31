package io.taranis.opencluster.common.utils;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

public final class Exceptions {
	
	private Exceptions() {
		
	}

	public static <X extends Throwable> void throwIf(
	        final boolean condition,
	        @Nonnull final Supplier<X> errorSupplier
	) throws X {
        if (condition) {
            throw errorSupplier.get();
        }
    }

	public static <X extends Throwable> void throwIf(
            final boolean condition,
            @Nonnull final Function<String, X> errorFactory,
            final String message
    ) throws X {
        if (condition) {
            throw errorFactory.apply(message);
        }
    }

	public static <X extends Throwable> void throwIfNot(
	        final boolean condition,
	        @Nonnull final Supplier<X> errorSupplier
	) throws X {
        throwIf(!condition, errorSupplier);
    }

	public static <X extends Throwable> void throwIfNot(
            final boolean condition,
            @Nonnull final Function<String, X> errorConstructor,
            final String message
    ) throws X {
        throwIf(!condition, errorConstructor, message);
    }

	public static <T, X extends Throwable> T throwIfNull(
            @Nullable final T nullable,
            @Nonnull final Function<String, X> errorFactory,
            final String message
    ) throws X {
	    throwIf(isNull(nullable), errorFactory, message);
        return nullable;
    }

	public static <T, X extends Throwable> T throwIfNull(
            @Nullable final T nullable,
            @Nonnull final Supplier<X> errorSupplier
    ) throws X {
        if (nullable == null) {
            throw errorSupplier.get();
        }
        return nullable;
    }
	
    public static <T, X extends Throwable> T throwIfNotPresent(
            @Nonnull final Optional<T> optional,
            @Nonnull final Supplier<X> errorSupplier
    ) throws X {
        if (!optional.isPresent()) {
            throw errorSupplier.get();
        }
        return optional.get();
    }

}