package ch.epfl.cs107.icoop.actor;

import java.util.List;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public abstract class ICoopCollectable extends CollectableAreaEntity {

    public ICoopCollectable(ICoopArea area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    public ICoopCollectable(ICoopArea area, Orientation orientation, DiscreteCoordinates position, Boolean isCollected) {
        super(area, orientation, position, isCollected);
    } 

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isCollected())
            super.draw(canvas);
    }

    abstract public ICoopItem getItem();

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
