package dev.spoocy.adapter.compatibility.annotations;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface On {

    @NotNull
    Class<? extends ItemMeta> value();

}
