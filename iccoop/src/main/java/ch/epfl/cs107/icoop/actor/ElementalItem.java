package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {

    private final NaturalElement element;

    /**
     * Default constructor for ElementalItem
     * @param area (ICoopArea): The area, not null
     * @param element (NaturalElement): The element of the item, not null
     * @param orientation (Orientation): The orientation of the item, not null
     * @param position (DiscreteCoordinates): The coordinates of the item, not null
     */
    protected ElementalItem(ICoopArea area, NaturalElement element, Orientation orientation,
    DiscreteCoordinates position) {
        super(area, orientation, position);
        this.element = element;
    }

    /**
     * Default constructor for ElementalItem
     * @param area (ICoopArea): The area, not null
     * @param element (NaturalElement): The element of the item, not null
     * @param orientation (Orientation): The orientation of the item, not null
     * @param position (DiscreteCoordinates): The coordinates of the item, not null
     * @param isCollected (Boolean): The state of the item, not null
     */
    protected ElementalItem(ICoopArea area, NaturalElement element, Orientation orientation,
    DiscreteCoordinates position, Boolean isCollected) {
        super(area, orientation, position, isCollected);
        this.element = element;
    }

    @Override
    public NaturalElement element() {
        return element;
    }

    @Override
    public boolean isOn() {
        return isCollected();
    }

    @Override
    public boolean isOff() {
        return !isCollected();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
