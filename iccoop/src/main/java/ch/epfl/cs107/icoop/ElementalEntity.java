package ch.epfl.cs107.icoop;

public interface ElementalEntity {

    /**
     * Represents the natural element of an entity
     * Only FIRE and WATER are used in the game but the other elements are defined for future use.
     */
    public enum NaturalElement {
        WATER,
        EARTH,
        FIRE,
        AIR,
    }

    /**
     * Getter for the natural element of the entity
     * @return (NaturalElement): the natural element of the entity
     */
    public NaturalElement element();
}
