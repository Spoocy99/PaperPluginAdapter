package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.core.config.PluginConfig;
import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.saveable.ViewProvider;
import dev.spoocy.adapter.inventory.InventoryManager;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.messages.Localization;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractGuiView implements GuiView {

    protected final Player viewer;
    protected Localization locale;
    protected boolean closeable;
    protected Function<Localization, Component> title;
    protected boolean resetOnClose, resetOnSwitch;
    protected boolean shouldHandleClose;
    protected boolean currentlyOpen;

    @NotNull
    protected Function<GuiView, GuiView> exitView;

    public AbstractGuiView(
            @NotNull Player viewer,
            @NotNull Localization locale,
            @NotNull Function<Localization, Component> title,
            boolean closeable
    ) {
        this.viewer = viewer;
        this.locale = locale;
        this.title = title;
        this.closeable = closeable;
        this.resetOnClose = false;
        this.resetOnSwitch = false;
        this.shouldHandleClose = true;
        this.currentlyOpen = false;
        this.exitView = v -> null;
    }

    @Override
    public @NotNull Player getViewer() {
        return this.viewer;
    }

    @Override
    public @NotNull Component getTitle() {
        return this.title.apply(this.locale);
    }

    @Override
    public boolean isOpen() {
        return this.currentlyOpen;
    }

    @Override
    public void open() {
        BukkitLogger.trace("Opening view {} for player {} (currently open: {})", this.getClass().getSimpleName(), this.viewer.getName(), this.isOpen());
        if(this.isOpen()) return;

        GuiView currentlyOpen = InventoryManager.INSTANCE.getCurrentlyOpen(this.viewer);
        if(currentlyOpen != null) {
            currentlyOpen.close(InventoryCloseReason.REPLACED);
        }

        this.currentlyOpen = true;
        InventoryManager.INSTANCE.markOpen(this);
        openView();
    }
    protected abstract void openView();

    @Override
    public void close(@NotNull InventoryCloseReason reason) {
        BukkitLogger.trace("Closing view {} for player {} with {} (currently open: {})", this.getClass().getSimpleName(), this.viewer.getName(), reason, this.isOpen());
        if(!this.isOpen()) return;
        this.currentlyOpen = false;
        this.shouldHandleClose = false;

        if(reason == InventoryCloseReason.REPLACED) {
            return;
        }

        InventoryManager.INSTANCE.markClosed(this.viewer);
        this.closeView();
    }
    protected abstract void closeView();

    @Override
    public @Nullable GuiView getExitView() {
        return exitView.apply(this);
    }

    @Override
    public void setExitView(@Nullable Function<GuiView, GuiView> view) {
        this.exitView = view == null ? v -> null : view;
    }

    @Override
    public GuiView exit() {
        GuiView exitView = this.getExitView();
        if (exitView != null) {

            if(!exitView.getViewer().equals(this.viewer)) {
                throw new IllegalStateException("Viewer of Exit View must be the same as Viewer of current view.");
            }

            exitView.open();
            return exitView;
        }

        this.close();
        return null; // No exit view set
    }

    @Override
    public Localization getLocale() {
        return this.locale;
    }

    @Override
    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    @Override
    public boolean isCloseable() {
        return this.closeable;
    }

    @Override
    public boolean isResetOnClose() {
        return this.resetOnClose;
    }

    @Override
    public boolean isResetOnSwitch() {
        return this.resetOnSwitch;
    }

    @Override
    public void setResetWhen(boolean onClose, boolean onSwitch) {
        this.resetOnClose = onClose;
        this.resetOnSwitch = onSwitch;
    }

    @Override
    public void setLocale(@NotNull Localization locale) {
        if(this.locale == locale) return;
        this.locale = locale;
        updateLocale(locale);
    }
    protected abstract void updateLocale(@NotNull Localization locale);

    @Override
    public void onUpdate(int x, int y, @Nullable Item item, @Nullable Item previousItem) {
        if(item == previousItem) {
            this.redraw(x, y);
            return;
        }

        if (previousItem != null) {
            previousItem.unsubscribe(this);
        }

        if (item != null) {
            item.subscribe(this);
        }

        this.redraw(x, y);
    }

    public abstract static class Builder<B extends GuiView.Builder<B, G>, G extends GuiView> implements GuiView.Builder<B, G> {

        protected Function<Localization, Component> title;
        protected boolean closeable = true;
        protected boolean resetOnClose = false;
        protected boolean resetOnSwitch = false;
        protected Localization locale;
        protected Runnable onClose, onOpen;
        protected Function<GuiView, GuiView> exitView;

        @Override
        public B title(@NotNull Function<Localization, Component> title) {
            this.title = title;
            return instance();
        }

        @Override
        public B closeable(boolean closeable) {
            this.closeable = closeable;
            return instance();
        }

        @Override
        public B resetWhen(boolean onClose, boolean onSwitch) {
            this.resetOnClose = onClose;
            this.resetOnSwitch = onSwitch;
            return instance();
        }

        @Override
        public B locale(@NotNull Localization locale) {
            this.locale = locale;
            return instance();
        }

        @Override
        public B exitView(@Nullable GuiView view) {
            if(view == null) {
                this.exitView = null;
                return instance();
            }
            return exit(v -> view);
        }

        @Override
        public B exit(@Nullable ViewProvider<Player, ?> viewProvider) {
            if(viewProvider == null) {
                this.exitView = null;
                return instance();
            }
            return exit(v -> viewProvider.getOrCreate(v.getViewer()));
        }

        @Override
        public B exit(@NotNull Function<GuiView, GuiView> view) {
            this.exitView = view;
            return instance();
        }

        @Override
        public B onOpenAction(@NotNull Runnable runnable) {
            this.onOpen = runnable;
            return instance();
        }

        @Override
        public B onCloseAction(@NotNull Runnable runnable) {
            this.onClose = runnable;
            return instance();
        }

        protected abstract B instance();

        protected void validate() {
            Objects.requireNonNull(this.title, "Title cannot be null");
            Objects.requireNonNull(this.locale, "Locale cannot be null");
        }

        @Override
        public @NotNull G build(@NotNull Player viewer) {
            if(this.locale == null) {
                this.locale = PluginConfig.globalTranslation().playerLocale(viewer);
            }
            validate();
            G view = createGuiView(viewer);
            view.setExitView(this.exitView);
            view.setResetWhen(this.resetOnClose, this.resetOnSwitch);

            if (this.onOpen != null) {
                view.onOpen(this.onOpen);
            }
            if (this.onClose != null) {
                view.onClose(this.onClose);
            }

            return view;
        }

        protected abstract G createGuiView(@NotNull Player viewer);
    }

}
