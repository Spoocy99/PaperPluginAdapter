package dev.spoocy.adapter.message.style;

import dev.spoocy.utils.common.misc.Args;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import java.util.*;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */
@Unmodifiable
@SuppressWarnings("EnumOrdinal")
public final class DecorationMap extends AbstractMap<TextDecoration, TextDecoration.State> {

    public static final TextDecoration[] DECORATIONS = TextDecoration.values();

    private static final TextDecoration.State[] STATES = TextDecoration.State.values();
    private static final int MAP_SIZE = DECORATIONS.length;

    // Set a maximum limit on cached combinations to avoid OOME if TextDecorations size unexpectedly grows
    private static final int MAX_CACHE_DECORATIONS = 8; // Size at 8: 1 << 16 = 65536 objects
    private static final int CACHE_SIZE = MAP_SIZE <= MAX_CACHE_DECORATIONS ? (1 << (MAP_SIZE * 2)) : 0;

    private static final DecorationMap[] CACHE = CACHE_SIZE > 0 ? new DecorationMap[CACHE_SIZE] : null;
    private static final KeySet KEY_SET = new KeySet();

    private static final DecorationMap EMPTY;
    private static final DecorationMap NONE;

    static {
        EMPTY = new DecorationMap(0); // NOT_SET = 0

        if (CACHE != null) {
            CACHE[0] = EMPTY;
        }


        Map<TextDecoration, TextDecoration.State> noDecs = new EnumMap<>(TextDecoration.class);
        for (TextDecoration decoration : DECORATIONS) {
            noDecs.put(decoration, TextDecoration.State.FALSE);
        }

        NONE = fromMap(noDecs);
    }

    /**
     * {@code decorations} with all states set to {@link TextDecoration.State#NOT_SET}.
     */
    @NotNull
    public static DecorationMap empty() {
        return EMPTY;
    }

    /**
     * {@code decorations} with all states set to {@link TextDecoration.State#FALSE}.
     */
    @NotNull
    public static DecorationMap none() {
        return NONE;
    }

    @NotNull
    public static DecorationMap fromMap(@NotNull Map<TextDecoration, TextDecoration.State> decorationMap) {
        Args.notNull(decorationMap, "decorationMap");

        if (decorationMap instanceof DecorationMap) {
            return (DecorationMap) decorationMap;
        }

        int bitSet = 0;
        for (final TextDecoration decoration : DECORATIONS) {
            final TextDecoration.State state = decorationMap.getOrDefault(decoration, TextDecoration.State.NOT_SET);
            if (state != null) {
                bitSet |= state.ordinal() << (decoration.ordinal() * 2);
            }
        }

        return withBitSet(bitSet);
    }

    @NotNull
    public static DecorationMap merge(
            @NotNull Map<TextDecoration, TextDecoration.State> first,
            @NotNull Map<TextDecoration, TextDecoration.State> second
    ) {
        Args.notNull(first, "first");
        Args.notNull(second, "second");

        int bitSet = 0;
        for (TextDecoration decoration : DECORATIONS) {
            // First takes precedence, but ONLY if it is not explicitly NOT_SET.
            TextDecoration.State state = first.get(decoration);
            
            // If the first map doesn't specify or specifies NOT_SET, fallback to the second map.
            if (state == null || state == TextDecoration.State.NOT_SET) {
                state = second.get(decoration);
                if (state == null) {
                    state = TextDecoration.State.NOT_SET;
                }
            }
            
            bitSet |= state.ordinal() << (decoration.ordinal() * 2);
        }

        return withBitSet(bitSet);
    }

    private static DecorationMap withBitSet(final int bitSet) {
        if (bitSet == 0) return EMPTY; // Ensures we always return EMPTY for 0

        if (CACHE != null && bitSet > 0 && bitSet < CACHE_SIZE) {
            DecorationMap map = CACHE[bitSet];
            if (map == null) {
                map = new DecorationMap(bitSet);
                CACHE[bitSet] = map; // benign data race is fully safe
            }
            return map;
        }
        return new DecorationMap(bitSet);
    }

    private final int bitSet;

    @Nullable
    private transient EntrySet entrySet;

    @Nullable
    private transient Values values;

    private DecorationMap(final int bitSet) {
        this.bitSet = bitSet;
    }

    @Override
    public TextDecoration.@Nullable State get(final Object o) {

        if (o instanceof TextDecoration) {
            TextDecoration textDecoration = (TextDecoration) o;
            return STATES[(this.bitSet >> (textDecoration.ordinal() * 2)) & 0b11];
        }

        return null;
    }

    @NotNull
    public TextDecoration.State get(@NotNull TextDecoration decoration) {
        Args.notNull(decoration, "decoration");
        return STATES[(this.bitSet >> (decoration.ordinal() * 2)) & 0b11];
    }

    @NotNull
    public TextDecoration.State obfuscated() {
        return get(TextDecoration.OBFUSCATED);
    }

    @NotNull
    public TextDecoration.State bold() {
        return get(TextDecoration.BOLD);
    }

    @NotNull
    public TextDecoration.State strikethrough() {
        return get(TextDecoration.STRIKETHROUGH);
    }

    @NotNull
    public TextDecoration.State underlined() {
        return get(TextDecoration.UNDERLINED);
    }

    @NotNull
    public TextDecoration.State italic() {
        return get(TextDecoration.ITALIC);
    }

    @CheckReturnValue
    @NotNull
    public DecorationMap withObfuscated(final TextDecoration.State state) {
        return with(TextDecoration.OBFUSCATED, state);
    }

    @CheckReturnValue
    @NotNull
    public DecorationMap withBold(final TextDecoration.State state) {
        return with(TextDecoration.BOLD, state);
    }

    @CheckReturnValue
    @NotNull
    public DecorationMap withItalic(final TextDecoration.State state) {
        return with(TextDecoration.ITALIC, state);
    }

    @CheckReturnValue
    @NotNull
    public DecorationMap withUnderlined(final TextDecoration.State state) {
        return with(TextDecoration.UNDERLINED, state);
    }

    @CheckReturnValue
    @NotNull
    public DecorationMap withStrikethrough(final TextDecoration.State state) {
        return with(TextDecoration.STRIKETHROUGH, state);
    }

    @CheckReturnValue
    @NotNull
    public DecorationMap with(final TextDecoration decoration, final TextDecoration.State state) {
        Args.notNull(state, "state");
        Args.notNull(decoration, "decoration");

        final int shift = decoration.ordinal() * 2;
        // 'reset' the state bits for the given decoration, and 'merge' the new state's bits
        final int newBitSet = (this.bitSet & ~(0b11 << shift)) | (state.ordinal() << shift);

        return withBitSet(newBitSet);
    }

    @CheckReturnValue
    @NotNull
    public DecorationMap getChild(@NotNull Component component) {
        boolean hasOverrides = false;
        // Iterate over values avoiding map copies/gets

        for (TextDecoration.State state : component.decorations().values()) {
            if (state != TextDecoration.State.NOT_SET) {
                hasOverrides = true;
                break;
            }
        }

        if (!hasOverrides) {
            return this;
        }

        // The child component's decorations should take HIGHER priority over the parent.
        // Therefore, we pass component.decorations() first, and 'this' second so 'this' acts as the fallback base map.
        return merge(component.decorations(), this);
    }

    @Override
    public boolean containsKey(final Object key) {
        return key instanceof TextDecoration;
    }

    @Override
    public int size() {
        return MAP_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull Set<Entry<TextDecoration, TextDecoration.State>> entrySet() {
        EntrySet es = this.entrySet;
        return es != null ? es : (this.entrySet = new EntrySet());
    }

    @Override
    public @NotNull Set<TextDecoration> keySet() {
        return KEY_SET;
    }

    @Override
    public @NotNull Collection<TextDecoration.State> values() {
        Values vs = this.values;
        return vs != null ? vs : (this.values = new Values());
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) return true;
        if (other == null || other.getClass() != DecorationMap.class) return false;
        return this.bitSet == ((DecorationMap) other).bitSet;
    }

    @Override
    public int hashCode() {
        return this.bitSet;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("DecorationMap{");

        for (int i = 0; i < MAP_SIZE; i++) {
            if (i > 0) builder.append(", ");

            TextDecoration decoration = DECORATIONS[i];

            builder.append(decoration.toString())
                    .append('=')
                    .append(STATES[(this.bitSet >> (decoration.ordinal() * 2)) & 0b11].toString());
        }

        builder.append('}');
        return builder.toString();
    }

    private final class EntrySet extends AbstractSet<Entry<TextDecoration, TextDecoration.State>> {

        @Override
        public @NotNull Iterator<Entry<TextDecoration, TextDecoration.State>> iterator() {
            return new Iterator<>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return this.index < MAP_SIZE;
                }

                @Override
                public Entry<TextDecoration, TextDecoration.State> next() {
                    if (!this.hasNext()) throw new NoSuchElementException();

                    final TextDecoration decoration = DECORATIONS[this.index++];
                    final TextDecoration.State state = STATES[(bitSet >> (decoration.ordinal() * 2)) & 0b11];
                    return new SimpleImmutableEntry<>(decoration, state);
                }
            };
        }

        @Override
        public int size() {
            return MAP_SIZE;
        }

    }

    private final class Values extends AbstractCollection<TextDecoration.State> {

        @Override
        public @NotNull Iterator<TextDecoration.State> iterator() {
            return new Iterator<>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return this.index < MAP_SIZE;
                }

                @Override
                public TextDecoration.State next() {
                    if (!this.hasNext()) throw new NoSuchElementException();

                    final TextDecoration decoration = DECORATIONS[this.index++];
                    return STATES[(bitSet >> (decoration.ordinal() * 2)) & 0b11];
                }
            };
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public @NotNull Object @NotNull [] toArray() {
            final Object[] states = new Object[MAP_SIZE];
            for (int i = 0; i < MAP_SIZE; i++) {
                states[i] = STATES[(bitSet >> (DECORATIONS[i].ordinal() * 2)) & 0b11];
            }
            return states;
        }

        @Override
        @SuppressWarnings("unchecked")
        public @NotNull <T> T @NotNull [] toArray(T[] dest) {
            if (dest.length < MAP_SIZE) {
                return (T[]) Arrays.copyOf(this.toArray(), MAP_SIZE, dest.getClass());
            }
            System.arraycopy(this.toArray(), 0, dest, 0, MAP_SIZE);
            if (dest.length > MAP_SIZE) {
                dest[MAP_SIZE] = null;
            }
            return dest;
        }

        @Override
        public boolean contains(final Object o) {
            return o instanceof TextDecoration.State && super.contains(o);
        }

        @Override
        public int size() {
            return MAP_SIZE;
        }
    }

    private static final class KeySet extends AbstractSet<TextDecoration> {

        @Override
        public boolean contains(final Object o) {
            return o instanceof TextDecoration;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public @NotNull Object @NotNull [] toArray() {
            return Arrays.copyOf(DECORATIONS, MAP_SIZE, Object[].class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public @NotNull <T> T @NotNull [] toArray(final T[] dest) {
            if (dest.length < MAP_SIZE) {
                return (T[]) Arrays.copyOf(DECORATIONS, MAP_SIZE, dest.getClass());
            }
            System.arraycopy(DECORATIONS, 0, dest, 0, MAP_SIZE);
            if (dest.length > MAP_SIZE) {
                dest[MAP_SIZE] = null;
            }
            return dest;
        }

        @Override
        public @NotNull Iterator<TextDecoration> iterator() {
            return Arrays.asList(DECORATIONS).iterator();
        }

        @Override
        public int size() {
            return MAP_SIZE;
        }

    }
}