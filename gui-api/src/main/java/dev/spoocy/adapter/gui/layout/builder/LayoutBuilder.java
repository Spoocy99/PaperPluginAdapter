package dev.spoocy.adapter.gui.layout.builder;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public abstract class LayoutBuilder {

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getSize();

    public abstract Layout build();
}
