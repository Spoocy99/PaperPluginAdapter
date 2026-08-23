package dev.spoocy.adapter.log.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class LoggerComponentSerializer {

    public static final ComponentFlattener DEFAULT_FLATTENER = ComponentFlattener
            .basic()
            .toBuilder()
            .unknownMapper(component -> {
                throw new UnsupportedOperationException("Don't know how to turn " + component.getClass().getSimpleName() + " into a string");
            })
            .build();

    public static LoggerComponentSerializer create(final Function<Component, String> serializer) {
        return new LoggerComponentSerializer(serializer);
    }

    private final Function<Component, String> serializer;

    private LoggerComponentSerializer(final Function<Component, String> serializer) {
        this.serializer = serializer;
    }

    public String serialize(final Component input) {
        return input == null ? null : this.serializer.apply(input);
    }

    public Object[] maybeSerialize(final @NotNull Object... args) {
        Object[] writable = Arrays.copyOf(args, args.length);
        Throwable t = null;

        for(int i = 0; i < writable.length; ++i) {

            if (writable[i] instanceof ComponentLike) {
                writable[i] = this.serialize( ((ComponentLike) writable[i]).asComponent() );
            }

            if (writable[i] instanceof ComponentMessageThrowable) {
                writable[i] = UnpackedComponentThrowable.unpack((Throwable) writable[i], this.serializer);
            }

            if (writable[i] instanceof Throwable) {
                t = (Throwable) writable[i];
            }

        }

        if(t != null && writable[writable.length - 1] != t) {
            writable = Arrays.copyOf(writable, writable.length + 1);
            writable[writable.length - 1] = t;
        }

        return writable;
    }

    static final class UnpackedComponentThrowable extends Throwable {
        private static final long serialVersionUID = -1L;
        private final Class<? extends Throwable> backingType;

        static Throwable unpack(final Throwable maybeRich, final Function<Component, String> serializer) {
            if (!(maybeRich instanceof ComponentMessageThrowable)) {
                return maybeRich;
            } else {
                Component message = ((ComponentMessageThrowable) maybeRich).componentMessage();

                Throwable cause = maybeRich.getCause() != null ? unpack(maybeRich.getCause(), serializer) : null;
                Throwable[] suppressed = maybeRich.getSuppressed();

                UnpackedComponentThrowable ret = new UnpackedComponentThrowable(maybeRich.getClass(), serializer.apply(message), cause);
                ret.setStackTrace(maybeRich.getStackTrace());

                for (Throwable throwable : suppressed) {
                    ret.addSuppressed(unpack(throwable, serializer));
                }

                return ret;
            }
        }

        private UnpackedComponentThrowable(final Class<? extends Throwable> backingType, final String serializedMessage, final Throwable cause) {
            super(serializedMessage, cause);
            this.backingType = backingType;
        }

        public String toString() {
            String className = this.backingType.getName();
            String message = this.getMessage();
            return message == null ? className : className + ":" + message;
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }


}
