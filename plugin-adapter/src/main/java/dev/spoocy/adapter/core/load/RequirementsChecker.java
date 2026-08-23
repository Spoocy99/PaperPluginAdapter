package dev.spoocy.adapter.core.load;

import dev.spoocy.adapter.core.PluginAdapter;
import dev.spoocy.adapter.log.BukkitLogger;
import dev.spoocy.adapter.version.MinecraftVersion;
import dev.spoocy.utils.common.version.Version;
import dev.spoocy.utils.config.ConfigSection;
import dev.spoocy.utils.security.CheckResult;
import dev.spoocy.utils.security.SecurityTest;
import dev.spoocy.utils.security.TestResult;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class RequirementsChecker {

    private final PluginAdapter plugin;

    private final boolean checkSpigot;
    private final @Nullable Version requiredVersion;

    public RequirementsChecker(@NotNull PluginAdapter plugin) {
        this.plugin = plugin;

        ConfigSection config = plugin.getRequirementsYML();

        if (config == null) {
            BukkitLogger.warn("No env-requirements.yml found in jar! Skipping checks.");
            this.checkSpigot = false;
            this.requiredVersion = null;
            return;
        }

        this.checkSpigot = config.getBoolean("spigot", false);
        this.requiredVersion = config.getVersion("version", null);
    }

    @SecurityTest(
            value = "Spigot Requirements Check",
            priority = 5,
            stage = SecurityTest.Stage.INIT,
            resultOnException = CheckResult.SKIPPED
    )
    public TestResult checkSpigot() {
        if (!this.checkSpigot) {
            return new TestResult(CheckResult.SKIPPED, "Spigot check is disabled (plugin.yml).");
        }

        try {
            Bukkit.spigot();
            return new TestResult(CheckResult.PASSED, "Server is running Spigot. (Version: " + Bukkit.getVersion() + ")");
        } catch (Throwable ex) {
            return new TestResult(CheckResult.ERROR, "Your Server is not running Spigot! Please download Spigot in order to use this plugin. (Currently running " + Bukkit.getVersion() + ")");
        }

    }

    @SecurityTest(
            value = "Server Version Requirements Check",
            priority = 6,
            stage = SecurityTest.Stage.INIT,
            resultOnException = CheckResult.SKIPPED
    )
    public TestResult checkMinecraftVersion() {
        if (this.requiredVersion == null) {
            return new TestResult(CheckResult.SKIPPED, "Required Version could not be read (plugin.yml).");
        }

        Version current = MinecraftVersion.getCurrent();
        if (current.isAtLeast(this.requiredVersion)) {
            return new TestResult(CheckResult.PASSED, "Server is running a supported Minecraft version.");
        }

        return new TestResult(CheckResult.ERROR, "Your Server is running an outdated version! Please update your server to at least " + this.requiredVersion + " in order to use this plugin. (Currently running " + current + ")");
    }

}
