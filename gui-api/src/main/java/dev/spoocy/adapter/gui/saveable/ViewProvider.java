package dev.spoocy.adapter.gui.saveable;

import dev.spoocy.adapter.gui.view.GuiView;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public interface ViewProvider<K, V extends GuiView> {

    V getOrCreate(@NotNull K key);

    V open(@NotNull K key);

    Collection<V> all();

    Collection<V> all(@NotNull Predicate<V> filter);

    void clearCache();

    void redrawAll();

}
