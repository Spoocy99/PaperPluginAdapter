package dev.spoocy.adapter.gui.implementations;

import com.google.common.base.Preconditions;
import dev.spoocy.adapter.gui.animation.Animation;
import dev.spoocy.adapter.gui.layout.slot.GuiChangeSubscriber;
import dev.spoocy.adapter.gui.saveable.DefaultViewProvider;
import dev.spoocy.adapter.gui.saveable.ViewProvider;
import dev.spoocy.adapter.gui.types.Gui;
import dev.spoocy.adapter.gui.view.GuiView;
import dev.spoocy.utils.common.collections.Collector;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractGui implements Gui {

    protected final int height, width, size;
    protected boolean frozen;
    protected Set<GuiChangeSubscriber> subscribers = new HashSet<>();
    protected Animation<?> currentAnimation;

    protected AbstractGui(int width, int height) {
        this.width = width;
        this.height = height;
        this.size = width * height;
        this.frozen = false;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    public boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public void subscribe(@NotNull GuiChangeSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(@NotNull GuiChangeSubscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    @Override
    public Set<GuiChangeSubscriber> getSubscribers() {
        return Collections.unmodifiableSet(this.subscribers);
    }

    @Override
    public Collection<GuiView> getViews() {
        return Collector.of(this.subscribers)
                .filter(GuiView.class::isInstance)
                .map(GuiView.class::cast)
                .asList();
    }

    @Override
    public boolean isAnimationPlaying() {
        return this.currentAnimation != null;
    }

    @Override
    public @Nullable Animation<?> getCurrentAnimation() {
        return this.currentAnimation;
    }

    protected <G extends Gui> void playAnimationInternally(@NotNull Animation<G> animation) {
        animation.onStop(() -> this.currentAnimation = null);
        animation.setGui((G) this);
        animation.start();
    }

    @Override
    public void cancelAnimation() {
        if(!isAnimationPlaying()) return;
        this.currentAnimation.stop();
    }

    public abstract static class Builder<G extends Gui, B extends Builder<G, B>> {

        protected int width, height;
        protected boolean frozen = false;
        protected NamespacedKey key;

        protected Builder(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public B frozen(boolean frozen) {
            this.frozen = frozen;
            return this.instance();
        }

        @NotNull
        protected abstract B instance();

        public void validate() {
            Preconditions.checkArgument(this.width > 0 && this.height > 0, "Width and height must be set.");
        }

        public G build() {
            this.validate();

            G gui = this.build0();
            gui.setFrozen(this.frozen);

            applyBuilder(gui);

            return gui;
        }

        public GuiView.NormalBuilder normalView() {
            return GuiView.normal().gui(build());
        }

        public ViewProvider<Player, GuiView.NormalView> normalViews(@NotNull BiConsumer<Player, GuiView.NormalBuilder> builder) {
            return normalViews(builder, false, true);
        }

        public ViewProvider<Player, GuiView.NormalView> normalViews(@NotNull BiConsumer<Player, GuiView.NormalBuilder> builder, boolean clearOnDisconnect) {
            return normalViews(builder, false, clearOnDisconnect);
        }

        public ViewProvider<Player, GuiView.NormalView> normalViews(@NotNull BiConsumer<Player, GuiView.NormalBuilder> builder, boolean shared, boolean clearOnDisconnect) {
            if(shared) {
                Gui gui = build();
                return new DefaultViewProvider<>(player -> {
                    GuiView.NormalBuilder viewBuilder = GuiView.normal().gui(gui);
                    builder.accept(player, viewBuilder);
                    return viewBuilder.build(player);
                }, clearOnDisconnect);
            } else {
                return new DefaultViewProvider<>(player -> {
                    GuiView.NormalBuilder viewBuilder = GuiView.normal().gui(build());
                    builder.accept(player, viewBuilder);
                    return viewBuilder.build(player);
                }, clearOnDisconnect);
            }
        }

        protected abstract void applyBuilder(G gui);
        protected abstract G build0();
    }

}


