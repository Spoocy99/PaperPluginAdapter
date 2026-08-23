package dev.spoocy.adapter.reflection.border;

import com.google.common.base.Preconditions;
import dev.spoocy.adapter.reflection.MinecraftFields;
import dev.spoocy.adapter.reflection.MinecraftMethods;
import dev.spoocy.adapter.reflection.MinecraftReflection;
import dev.spoocy.utils.reflection.Reflection;
import dev.spoocy.utils.reflection.accessor.ConstructorAccessor;
import dev.spoocy.utils.reflection.accessor.FieldAccessor;
import dev.spoocy.utils.reflection.accessor.MethodAccessor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Shows a border only to the player, not to the server. Useful so Mobs etc. can move through the border and spawn.
 * Based on NMS of 1.21.
 *
 * @author Spoocy99 | GitHub: Spoocy99
 */
public class PlayerSideBorder implements WorldBorder {

    private final World world;
    private final List<Player> players;
    private final Object handle;

    public PlayerSideBorder(@NotNull World world) {
        this.world = world;
        this.players = new ArrayList<>();
        this.handle = Reflection.getConstructor(MinecraftReflection.getWorldBorderClass()).invoke();

        Object worldServer = MinecraftReflection.getCraftObject(world);
        Reflection.getField(this.handle.getClass(), "world", null).set(this.handle, worldServer);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void addPlayer(@NotNull Player player) {
        Object packet = getInitializePacket();

        Object playerconnection = MinecraftFields.getPlayerConnection(player);
        MinecraftMethods.getSendPacketMethod().invoke(playerconnection, packet);

        update(player);

        if(players.contains(player)) return;
        this.players.add(player);
    }

    public void removePlayer(@NotNull Player player) {
        if(player.isOnline()) {
            player.setWorldBorder(player.getWorld().getWorldBorder());
        }
        this.players.remove(player);
    }

    public void removeAll() {
        for (Player player : this.players) {
            if(player != null && player.isOnline()) {
                player.setWorldBorder(player.getWorld().getWorldBorder());
            }
        }
        this.players.clear();
    }

    public void update() {
        Object centerPacket = getSetCenterPacket();
        Object sizePacket = getBorderSizePacket();
        Object warningDistancePacket = getWarningDistancePacket();

        for (Player player : this.players) {
            Object playerconnection = MinecraftFields.getPlayerConnection(player);
            MinecraftMethods.getSendPacketMethod().invoke(playerconnection, centerPacket);
            MinecraftMethods.getSendPacketMethod().invoke(playerconnection, sizePacket);
            MinecraftMethods.getSendPacketMethod().invoke(playerconnection, warningDistancePacket);
        }
    }

    public void update(Player player) {
        Object centerPacket = getSetCenterPacket();
        Object sizePacket = getBorderSizePacket();
        Object warningDistancePacket = getWarningDistancePacket();

        Object playerconnection = MinecraftFields.getPlayerConnection(player);
        MinecraftMethods.getSendPacketMethod().invoke(playerconnection, centerPacket);
        MinecraftMethods.getSendPacketMethod().invoke(playerconnection, sizePacket);
        MinecraftMethods.getSendPacketMethod().invoke(playerconnection, warningDistancePacket);
    }

    @Nullable
    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public void reset() {
        FieldAccessor f = Reflection.getField(this.handle.getClass(), "d", null);
        if (f == null) {
            throw new UnsupportedOperationException("DefaultValue Field not found.");
        }
        Object defaultValue = f.get(this.handle);

        MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "a");
        if (m == null) {
            throw new UnsupportedOperationException("Reset Method not found.");
        }
        m.invoke(this.handle, defaultValue);
    }

    @Override
    public double getSize() {
        MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "i");
        if (m == null) {
            throw new UnsupportedOperationException("GetSize Method not found.");
        }
        return (double) m.invoke(this.handle);
    }

    @Override
    public void setSize(double newSize) {
        this.setSize(newSize, 0L);
    }

    @Override
    public void setSize(double newSize, long time) {
        this.setSize(Math.min(this.getMaxSize(), Math.max(1.0, newSize)), TimeUnit.SECONDS, Math.min(9223372036854775L, Math.max(0L, time)));
    }

    @Override
    public void setSize(double newSize, TimeUnit unit, long time) {
        Preconditions.checkArgument(unit != null, "TimeUnit cannot be null.");
        Preconditions.checkArgument(time >= 0L, "time cannot be lower than 0");
        Preconditions.checkArgument(newSize >= 1.0 && newSize <= this.getMaxSize(), "newSize must be between 1.0D and %s", this.getMaxSize());

        if (time > 0L) {
            MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "i");
            if (m == null) {
                throw new UnsupportedOperationException("GetSize Method not found.");
            }
            Object i = m.invoke(handle);

            MethodAccessor setMethod = Reflection.getMethod(this.handle.getClass(), "a", i.getClass(), int.class, long.class);
            if (setMethod == null) {
                throw new UnsupportedOperationException("SetSize Method not found.");
            }
            setMethod.invoke(this.handle, i, newSize, unit.toMillis(time));
            return;
        }

        MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "a", double.class);
        if (m == null) {
            throw new UnsupportedOperationException("SetSize Method not found.");
        }
        m.invoke(this.handle, newSize);
    }

    @NotNull
    @Override
    public Location getCenter() {
        double x = (double) Reflection.getMethod(this.handle.getClass(), "a").invoke(this.handle);
        double  z = (double) Reflection.getMethod(this.handle.getClass(), "b").invoke(this.handle);
        return new Location(this.world, x, 0.0, z);
    }

    @Override
    public void setCenter(double x, double z)  {
        Preconditions.checkArgument(Math.abs(x) <= this.getMaxCenterCoordinate(), "x coordinate cannot be outside +- %s", this.getMaxCenterCoordinate());
        Preconditions.checkArgument(Math.abs(z) <= this.getMaxCenterCoordinate(), "z coordinate cannot be outside +- %s", this.getMaxCenterCoordinate());

        MethodAccessor method = Reflection.getMethod(this.handle.getClass(), "c", double.class, double.class);
        if (method == null) {
            throw new UnsupportedOperationException("SetCenter Method not found.");
        }
         method.invoke(this.handle, x, z);
    }

    @Override
    public void setCenter(@NotNull Location location) {
        this.setCenter(location.getX(), location.getZ());
    }

    @Override
    public double getDamageBuffer() {
        return 0.0D;
    }

    @Override
    public void setDamageBuffer(double blocks) {
        throw new UnsupportedOperationException("Not supported for Client-Side WorldBorder");
    }

    @Override
    public double getDamageAmount() {
        return 0.0D;
    }

    @Override
    public void setDamageAmount(double v) {
        throw new UnsupportedOperationException("Not supported for Client-Side WorldBorder");
    }

    @Override
    public int getWarningTime() {
        return (int) Reflection.getMethod(this.handle.getClass(), "q").invoke(this.handle);
    }

    @Override
    public void setWarningTime(int i) {
        MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "a", int.class);
        if (m == null) {
            throw new UnsupportedOperationException("SetWarningTime Method not found.");
        }
        Reflection.getMethod(this.handle.getClass(), "b", int.class).invoke(this.handle, i);
    }

    @Override
    public int getWarningDistance() {
        MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "r");
        if (m == null) {
            throw new UnsupportedOperationException("GetWarningDistance Method not found.");
        }
        return (int) m.invoke(this.handle);
    }

    @Override
    public void setWarningDistance(int i) {
        MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "a", int.class);
        if (m == null) {
            throw new UnsupportedOperationException("SetWarningDistance Method not found.");
        }
        m.invoke(this.handle, i);
    }

    @Override
    public boolean isInside(@NotNull Location location) {
        Preconditions.checkArgument(location != null, "location cannot be null");

        Object blockPosition = Reflection.getMethod(MinecraftReflection.getBlockPositionClass(), "a" ,double.class, double.class, double.class)
                .invoke(null, location.getX(), location.getY(), location.getZ());

        MethodAccessor m = Reflection.getMethod(this.handle.getClass(), "a", blockPosition.getClass());
        if (m == null) {
            throw new UnsupportedOperationException("IsInside Method not found.");
        }

        return (this.world == null
                || location.getWorld().equals(this.world))
                && (Boolean) m.invoke(this.handle, blockPosition);
    }

    @Override
    public double getMaxSize() {
        return 5.9999968E7;
    }

    @Override
    public double getMaxCenterCoordinate() {
        return 2.9999984E7;
    }

    private Class<?> getSetCenterPacketClass() {
        return MinecraftReflection.getMinecraftClass("network.protocol.game.ClientboundSetBorderCenterPacket", "ClientboundSetBorderCenterPacket");
    }

    private Class<?> getInitializePacketClass() {
        return MinecraftReflection.getMinecraftClass("network.protocol.game.ClientboundInitializeBorderPacket", "ClientboundInitializeBorderPacket");
    }

    private Class<?> getBorderSizePacketClass() {
        return MinecraftReflection.getMinecraftClass("network.protocol.game.ClientboundSetBorderSizePacket", "ClientboundSetBorderSizePacket");
    }

    private Class<?> getWarningDistancePacketClass() {
        return MinecraftReflection.getMinecraftClass("network.protocol.game.ClientboundSetBorderWarningDistancePacket", "ClientboundSetBorderWarningDistancePacket");
    }

    private ConstructorAccessor setCenterConstructor;
    private Object getSetCenterPacket() {
        if (setCenterConstructor == null) {
            setCenterConstructor = Reflection.getConstructor(getSetCenterPacketClass(), this.handle.getClass());
        }
        return setCenterConstructor.invoke(this.handle);
    }

    private ConstructorAccessor initializeConstructor;
    private Object getInitializePacket() {
        if (initializeConstructor == null) {
            initializeConstructor = Reflection.getConstructor(getInitializePacketClass(), this.handle.getClass());
        }
        return initializeConstructor.invoke(this.handle);
    }

    private ConstructorAccessor borderSizeConstructor;
    private Object getBorderSizePacket() {
        if (borderSizeConstructor == null) {
            borderSizeConstructor = Reflection.getConstructor(getBorderSizePacketClass(), this.handle.getClass());
        }
        return borderSizeConstructor.invoke(this.handle);
    }

    private ConstructorAccessor warningDistanceConstructor;
    private Object getWarningDistancePacket() {
        if (warningDistanceConstructor == null) {
            warningDistanceConstructor = Reflection.getConstructor(getWarningDistancePacketClass(), this.handle.getClass());
        }
        return warningDistanceConstructor.invoke(this.handle);
    }

}
