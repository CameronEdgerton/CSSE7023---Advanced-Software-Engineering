package bms.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A Singleton class which manages all the timed items.
 */
public class TimedItemManager implements TimedItem {

    // The instance of the TimedItemManager
    private static TimedItemManager INSTANCE;
    // The list holding all timed item registrations
    private List<TimedItem> timedItemList = new ArrayList<>();

    /*
    * Private empty constructor which is limited to the class itself.
     */
    private TimedItemManager() {
    }

    /**
     * @return the current instance of the TimedItemManager class
     */
    public static TimedItemManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimedItemManager();
        }
        return INSTANCE;
    }

    /**
     * Registers a timed item to the list of timed items.
     * @param timedItem The item to register.
     */
    public void registerTimedItem(TimedItem timedItem) {
        this.timedItemList.add(timedItem);
    }

    /**
     * Elapses one minute for all timed items that have been registered.
     */
    @Override
    public void elapseOneMinute() {
        for (TimedItem timedItem : timedItemList) {
            timedItem.elapseOneMinute();
        }
    }
}
