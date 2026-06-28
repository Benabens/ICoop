package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.Inventory;

public class ICoopInventory extends Inventory {

    private ICoopItem currentItem = null;

    private final ICoopItem[] order = {
        ICoopItem.EXPLOSIVE,
        ICoopItem.SWORD,
        ICoopItem.FIRE_KEY,
        ICoopItem.WATER_KEY,
        ICoopItem.FIRE_STAFF,
        ICoopItem.WATER_STAFF
    };

    /**
     * Default constructor for ICoopInventory
     * Every player starts with a sword
     */
    public ICoopInventory() {
        super("Pocket");
        addPocketItem(ICoopItem.SWORD, 1);
    }
    /* *
     * Adds an item to the inventory and sets it as the current item if there is no current item
     * @param item (ICoopItem): the item to add
     * @param quantity (int): the quantity of the item to add
     */
    public void addPocketItem(ICoopItem item, int quantity) {
        if (super.addPocketItem(item, quantity)) {
            if (currentItem == null) {
                currentItem = item;
            }
        }
    }

    /**
     * Removes an item from the inventory and switches the current item if the removed item was the current item
     * @param item (ICoopItem): the item to remove
     * @param quantity (int): the quantity of the item to remove
     */
    public void removePocketItem(ICoopItem item, int quantity) {
        if (super.removePocketItem(item, quantity)) {
            if (currentItem == item) {
                if (!contains(item)) {
                    switchItem();
                }
            }
        }
    }

    /**
     * Switches the current item to the next item based on the order array
     */
    public void switchItem() {
        int index = currentItem == null ? 0 : currentItem.getOrder();
        for (int i = 0; i < order.length; ++i) {
            index = (index + 1) % order.length;
            if (this.contains(order[index])) {
                currentItem = order[index];
                return;
            }
        }
        currentItem = null;
    }

    /**
     * Getter for the current item
     * @return (ICoopItem): the current item
     */
    public ICoopItem getCurrentItem() {
        return currentItem;
    }
}
