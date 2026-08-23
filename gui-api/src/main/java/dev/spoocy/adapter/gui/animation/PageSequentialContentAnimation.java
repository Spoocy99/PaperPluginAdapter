package dev.spoocy.adapter.gui.animation;

import dev.spoocy.adapter.gui.items.Item;
import dev.spoocy.adapter.gui.layout.Coordinate;
import dev.spoocy.adapter.gui.types.PageGui;

import java.util.List;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class PageSequentialContentAnimation extends AbstractAnimation<PageGui> {

    private List<Item> allContent;
    private Coordinate[] slots;
    private int slotIndex;

    public PageSequentialContentAnimation(int ticksPerFrame) {
        super(ticksPerFrame);
    }

    @Override
    protected void onStart() {
        getGui().reset();
        getGui().setContentHidden(true);
        this.allContent = getGui().getContentProvider().getItemList(0, Integer.MAX_VALUE);
        this.slots = getGui().getContentSlots();
        this.slotIndex = 0;
    }

    @Override
    protected void onEnd() {
        getGui().setContentHidden(false);
    }

    @Override
    protected void playFrame(int frame) {

        if(frame >= this.allContent.size()) {
            stop();
            return;
        }

        Coordinate coords = this.slots[slotIndex];
        Item content = this.allContent.get(frame);
        slotIndex++;

        int x = coords.getX();
        int y = coords.getY();
        getGui().setContentItemUntilUpdate(x, y, content);

        if(slotIndex >= this.slots.length) {
            slotIndex = 0;
            getGui().goForward(1);
            getGui().setContentHidden(true); // clear content again
        }


    }


}
