package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

public enum ICoopItem implements InventoryItem {

    EXPLOSIVE("Explosive", "explosive", 0),
    SWORD("Sword", "sword.icon", 1),
    FIRE_KEY("FireKey", "key_red", 2),
    WATER_KEY("WaterKey", "key_blue", 3),
    FIRE_STAFF("FireStaff", "staff_fire.icon", 4),
    WATER_STAFF("WaterStaff", "staff_water.icon", 5);

    private final String name;
    private final String sprite;
    private final int order;

    /**
     * Default constructor for ICoopItem
     * @param name (String): the name of the item
     * @param sprite (String): the sprite of the item
     * @param order (int): the order of the item
     */
    ICoopItem(String name, String sprite, int order) {
        this.name = name;
        this.sprite = sprite;
        this.order = order;
    }

    @Override
    public int getPocketId() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getSprite() {
        return sprite;
    }

    /**
     * Getter for the order of the item. Could be used to switch items in the inventory
     * @return (int): the order of the item
     */
    public int getOrder() {
        return order;
    }


}
