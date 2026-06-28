package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Rock extends Obstacle {

    private Boolean isDestroyed = false;

    public Rock(Area area, DiscreteCoordinates position) {
        super("rock.1", area, position);
    }

    public void destroy() {
        isDestroyed = true;
    }

    @Override
    public boolean takeCellSpace() {
        return !isDestroyed;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDestroyed) {
            super.draw(canvas);
        }
    }

    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
