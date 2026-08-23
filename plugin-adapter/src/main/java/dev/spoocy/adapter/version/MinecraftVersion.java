package dev.spoocy.adapter.version;

import dev.spoocy.utils.common.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class MinecraftVersion implements Version {

    public static final MinecraftVersion
            LATEST = new MinecraftVersion("26.2"),
            V26_1 = new MinecraftVersion("26.1"),
            V1_21_11 = new MinecraftVersion("1.21.11"),
            V1_21_10 = new MinecraftVersion("1.21.10"),
            V1_21_8 = new MinecraftVersion("1.21.8"),
            V1_21_7 = new MinecraftVersion("1.21.7"),
            V1_21_6 = new MinecraftVersion("1.21.6"),
            V1_21_5 = new MinecraftVersion("1.21.5"),
            V1_21_1 = new MinecraftVersion("1.21.1"),
            V_21 = new MinecraftVersion("1.21"),
            V1_20_6 = new MinecraftVersion("1.20.6"),      // similar to 1.21
            V1_20_4 = new MinecraftVersion("1.20.4"),      // changes to Text Components
            V1_20_3 = new MinecraftVersion("1.20.3"),      // Some packages work slightly different even though fields are the same
            V1_20_2 = new MinecraftVersion("1.20.2"),      // Use of Records for certain packages
            V1_20 = new MinecraftVersion("1.20"),
            V1_19_4 = new MinecraftVersion("1.19.4"),
            V1_19 = new MinecraftVersion("1.19"),
            V1_18_2 = new MinecraftVersion("1.18.2"),
            V1_18 = new MinecraftVersion("1.18"),
            V1_17_1 = new MinecraftVersion("1.17.1"),
            V1_17 = new MinecraftVersion("1.17"),
            V1_16_5 = new MinecraftVersion("1.16.5"),
            V1_16_4 = new MinecraftVersion("1.16.4"),      // R_3
            V1_16_2 = new MinecraftVersion("1.16.2"),      // R_2
            V1_16 = new MinecraftVersion("1.16"),          // R_1
            V1_15_2 = new MinecraftVersion("1.15.2"),
            V1_15 = new MinecraftVersion("1.15"),
            V1_14_4 = new MinecraftVersion("1.14.4"),
            V1_14 = new MinecraftVersion("1.14"),
            V1_13_2 = new MinecraftVersion("1.13.2"),
            V1_13 = new MinecraftVersion("1.13"),
            V1_12_2 = new MinecraftVersion("1.12.2"),
            V1_12 = new MinecraftVersion("1.12"),
            V1_11_2 = new MinecraftVersion("1.11.2"),
            V1_11 = new MinecraftVersion("1.11"),
            V1_10_2 = new MinecraftVersion("1.10.2"),
            V1_10 = new MinecraftVersion("1.10"),
            V1_9_4 = new MinecraftVersion("1.9.4"),
            V1_9 = new MinecraftVersion("1.9"),
            V1_8_8 = new MinecraftVersion("1.8.8"),
            V1_8 = new MinecraftVersion("1.8");

    private static final Pattern VERSION_PATTERN = Pattern.compile(".*\\(.*MC.\\s*([a-zA-z0-9\\-.]+).*");

    private static MinecraftVersion CURRENT;

    public static MinecraftVersion getCurrent() {
        if(CURRENT == null) {
            CURRENT = fromServer(Bukkit.getServer());
        }
        return CURRENT;
    }

    public static MinecraftVersion fromServer(@NotNull Server server) {
        return fromServerVersion(server.getVersion());
    }

    public static MinecraftVersion fromServerVersion(@NotNull String serverVersion) {
        serverVersion = extractVersion(serverVersion);
        String[] section = serverVersion.split("-");
        int[] numbers;

        try {
            numbers = parseVersion(section[0]);
        } catch (NumberFormatException cause) {
            throw new IllegalArgumentException("Cannot parse version String '" + serverVersion + "'", cause);
        }

        return new MinecraftVersion(numbers[0], numbers[1], numbers[2]);
    }

    private final int major;
    private final int minor;
    private final int build;

    public MinecraftVersion(int major, int minor, int build) {
        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    private MinecraftVersion(@NotNull String version) {
        String[] section = version.split("-");
        int[] numbers;

        try {
            numbers = parseVersion(section[0]);
        } catch (NumberFormatException cause) {
            throw new IllegalArgumentException("Cannot parse version String '" + version + "'", cause);
        }

        this.major = numbers[0];
        this.minor = numbers[1];
        this.build = numbers[2];
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getBuild() {
        return this.build;
    }

    @Override
    public boolean isPreRelease() {
        return false;
    }

    @Override
    public @Nullable String getPreReleaseIdentifier() {
        return null;
    }

    @Override
    public @Nullable String getBuildMetaData() {
        return null;
    }

    @Override
    public String format() {
        return String.format("%s.%s.%s", this.getMajor(), this.getMinor(), this.getBuild());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        if (obj instanceof MinecraftVersion) {
            MinecraftVersion other = (MinecraftVersion) obj;

            return this.getMajor() == other.getMajor() &&
                    this.getMinor() == other.getMinor() &&
                    this.getBuild() == other.getBuild()
            ;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMajor(), this.getMinor(), this.getBuild());
    }

    @Override
    public String toString() {
        return this.format();
    }

    private static String extractVersion(@NotNull String text) {
        Matcher version = VERSION_PATTERN.matcher(text);

        if (version.matches() && version.group(1) != null) {
            return version.group(1);

        } else {
            throw new IllegalStateException("Cannot parse version String '" + text + "'");
        }
    }

    private static int[] parseVersion(String version) {
        String[] elements = version.split("\\.");
        int[] numbers = new int[3];

        if (elements.length < 1) {
            throw new IllegalStateException("Corrupt MC version: " + version);
        }

        for (int i = 0; i < Math.min(numbers.length, elements.length); i++) {
            numbers[i] = Integer.parseInt(elements[i].trim());
        }
        return numbers;
    }

}
