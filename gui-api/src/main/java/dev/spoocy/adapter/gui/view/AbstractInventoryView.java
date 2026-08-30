package dev.spoocy.adapter.gui.view;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.inventory.CustomInventory;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.messages.Localization;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class AbstractInventoryView extends AbstractGuiView {

    protected CustomInventory inventory;
    protected Runnable onClose, onOpen;

    public AbstractInventoryView(
            @NotNull Player viewer,
            @NotNull Localization locale,
            @NotNull Function<Localization, Component> title,
            boolean closeable
    ) {
        super(viewer, locale, title, closeable);
    }

    @Override
    public void onOpen(@NotNull Runnable runnable) {
        this.onOpen = runnable;
    }

    @Override
    public void onClose(@NotNull Runnable runnable) {
        this.onClose = runnable;
    }

    @Override
    protected void openView() {
        this.inventory.open(this.viewer);
    }

    @Override
    protected void closeView() {
        this.inventory.close();
    }

    @Override
    protected void updateLocale(@NotNull Localization locale) {
        this.buildInventory();
        this.redraw();
    }

    protected void buildInventory() {
        if(this.inventory != null) {
            this.inventory.setListeningForActions(false);
            this.inventory.onClick( e -> { });
            this.inventory.onOpen(e -> { });
            this.inventory.onClose(e -> { });
        }

        this.inventory = createInventory(this.getTitle());

        if(this.isOpen()) {
            this.openView();
        }

        applyActionsToInventory(this.inventory);
    }
    protected abstract CustomInventory createInventory(@NotNull Component title);

    @Override
    public void simulateClick(@NotNull InventoryClickEvent event) {
        //BukkitLogger.trace("InventoryView clicked for " + viewer.getUniqueId() + " on slot " + event.getSlot());
        handleClick(event);
    }

    private void applyActionsToInventory(@NotNull CustomInventory inventory) {

        inventory.onClick(this::simulateClick);

        inventory.onOpen(event -> {
            //BukkitLogger.trace("InventoryView opened for " + viewer.getUniqueId());

            if (this.onOpen != null) {
                this.onOpen.run();
            }

            handleOpen(event);
        });

        inventory.onClose(event -> {
            //BukkitLogger.trace("InventoryView closed for " + viewer.getUniqueId());

            // View is closed by other view
            if(!super.shouldHandleClose) {
                BukkitLogger.trace("Skipping close handling for " + viewer.getUniqueId() + " because another view was opened.");
                super.shouldHandleClose = true;

                if(this.isResetOnSwitch()) {
                    this.resetDisplayedGui();
                }

                return;
            }

            // -- View is closed by the user

            if(!super.closeable) {
                BukkitLogger.trace("Re-opening non-closeable view for " + viewer.getUniqueId());
                Bukkit.getScheduler().runTaskLater(PluginAdapter.getInstance(), this::openView, 2);
                return;
            }

            // -- View should be closed

            if(this.isResetOnClose()) {
                this.resetDisplayedGui();
            }

            if (this.onClose != null) {
                this.onClose.run();
            }

            handleClose(event);

            this.currentlyOpen = false;
            //BukkitLogger.trace("Finished close handling for " + viewer.getUniqueId());
        });


    }
    protected abstract void handleOpen(@NotNull InventoryOpenEvent event);
    protected abstract void handleClose(@NotNull InventoryCloseEvent event);
    protected abstract void handleClick(@NotNull InventoryClickEvent event);

}
