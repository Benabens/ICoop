package ch.epfl.cs107.icoop.actor;

import java.util.List;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.io.ResourcePath;

public class Obstacle extends AreaEntity {

    private final ImageGraphics sprite;

    public Obstacle(String spriteName, Area area, DiscreteCoordinates position) {
        super(area, null, position);
        Vector vector = new Vector(0.5f, 0.5f);
        sprite = new ImageGraphics(ResourcePath.getSprite(spriteName), 1, 1, null, vector, 1.0f, -Float.MAX_VALUE);
        sprite.setParent(this);
    }

    public Obstacle(Area area, DiscreteCoordinates position) {
        super(area, null, position);
        Vector vector = new Vector(0.5f, 0.5f);
        sprite = new ImageGraphics(ResourcePath.getSprite("rock.2"), 1, 1, null, vector, 1.0f, -Float.MAX_VALUE);
        sprite.setParent(this);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    };

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
}
