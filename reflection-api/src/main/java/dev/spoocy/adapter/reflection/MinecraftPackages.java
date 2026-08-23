package dev.spoocy.adapter.reflection;

import dev.spoocy.adapter.version.MinecraftVersion;
import dev.spoocy.utils.reflection.ReflectionPackage;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public final class MinecraftPackages {

    private MinecraftPackages() { }

    private static final Pattern PACKAGE_VERSION_MATCHER = Pattern.compile(".*\\.(v\\d+_\\d+_\\w*\\d+)");
    private static final String FORGE_ENTITY_PACKAGE = "net.minecraft.entity";

    private static String MINECRAFT_PREFIX_PACKAGE = "net.minecraft.server";
    private static String MINECRAFT_PACKAGE_NAME;
    private static String CRAFTBUKKIT_PACKAGE_NAME;

    public static String getCraftBukkitPackage() {
        if (CRAFTBUKKIT_PACKAGE_NAME == null) {
            setPackageNames();
        }
        return CRAFTBUKKIT_PACKAGE_NAME;
    }

    public static String getMinecraftPackageName() {
        if (MINECRAFT_PACKAGE_NAME == null) {
            setPackageNames();
        }
        return MINECRAFT_PACKAGE_NAME;
    }

    private static void setPackageNames() {
        try {
            Server craftServer = Bukkit.getServer();
            CRAFTBUKKIT_PACKAGE_NAME = craftServer.getClass().getPackage().getName();

            if (MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.V1_17)) {
                // Rework of the NMS structure in 1.17
                MINECRAFT_PACKAGE_NAME = MINECRAFT_PREFIX_PACKAGE + "net.minecraft";
            } else {
                Method getHandle = MinecraftReflection.getCraftEntityClass().getMethod("getHandle");
                MINECRAFT_PACKAGE_NAME = getHandle.getReturnType().getPackage().getName();

                if (!MINECRAFT_PACKAGE_NAME.startsWith(MINECRAFT_PREFIX_PACKAGE)) {

                    if (MINECRAFT_PACKAGE_NAME.equals(FORGE_ENTITY_PACKAGE)) {
                        // Use the standard NMS versioned package
                        String version = extractVersion(CRAFTBUKKIT_PACKAGE_NAME);
                        MINECRAFT_PACKAGE_NAME = ReflectionPackage.combine(MINECRAFT_PREFIX_PACKAGE, version);
                        return;
                    }

                    MINECRAFT_PREFIX_PACKAGE = MINECRAFT_PACKAGE_NAME;
                }

            }

        } catch (Exception exception) {
            throw new IllegalStateException("Cannot find package names for Reflection.", exception);
        }
    }

    private static String extractVersion(String packageName) {
        Matcher packageMatcher = PACKAGE_VERSION_MATCHER.matcher(packageName);

        if (packageMatcher.matches()) {
            return packageMatcher.group(1);
        }

        MinecraftVersion version = MinecraftVersion.fromServer(Bukkit.getServer());
        return  "v" + version.getMajor() + "_" + version.getMinor() + "_R1";
    }

}
