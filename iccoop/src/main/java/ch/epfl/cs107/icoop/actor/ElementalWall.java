package ch.epfl.cs107.icoop.actor;

import java.util.List;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public abstract class ElementalWall extends AreaEntity implements ElementalEntity, Interactor {

    private final NaturalElement element;
    private final Logic signal;
    private final Sprite[] wallSprites;
    private final Sprite sprite;
    private final ElementalWallInteractionHandler interactionHandler = new ElementalWallInteractionHandler(); 

    abstract public void damage(ICoopPlayer player);

    /**
     * Default constructor for ElementalWall
     * @param spriteName (String): The name of the sprite, not null
     * @param element (NaturalElement): The element of the wall, not null
     * @param area (Area): The area, not null
     * @param signal (Logic): The signal that determines if the wall is active, not null
     * @param orientation (Orientation): The orientation of the wall, not null
     * @param position (DiscreteCoordinates): The coordinates of the wall, not null
     */
    public ElementalWall(String spriteName, NaturalElement element, Area area, Logic signal, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.element = element;
        this.signal = signal;
        wallSprites = RPGSprite.extractSprites(spriteName, 4, 1, 1, this, Vector.ZERO, 256, 256);
        sprite = wallSprites[getOrientation().ordinal()];
    }

    public class ElementalWallInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isActive())
                damage(player);
        }
    } 

    @Override
    public NaturalElement element() {
        return isActive() ? element : null;
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
    public boolean wantsCellInteraction() { return true; }

    @Override   
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /**
     * @return Boolean: false if the wall is inactive (a related pressure plate is actived or any other logic signal is off), true otherwise
     */
    public Boolean isActive() {
        return !signal.isOn();
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(
            getCurrentMainCellCoordinates().jump(Orientation.DOWN.toVector()),
            getCurrentMainCellCoordinates().jump(Orientation.UP.toVector()),
            getCurrentMainCellCoordinates().jump(Orientation.RIGHT.toVector()),
            getCurrentMainCellCoordinates().jump(Orientation.LEFT.toVector())
        );
    }

    public void destroy() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isActive())
            sprite.draw(canvas);
    }
}
