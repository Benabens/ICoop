package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import java.util.List;

public abstract class Projectile extends Unstoppable implements Interactor {

    private final int maxDistance;
    private int distance = 0;
    private static final int MOVE_DURATION = 8;
    private final int speed;
    private boolean isStopped = false;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int maxDistance, int speed) {
        super(area, orientation, position);
        this.maxDistance = maxDistance;
        this.speed = speed;
    }

    private void checkEndOfProjectile() {
        if (isStopped)
            return;
        move(MOVE_DURATION / speed);
        distance++;
        if (distance == maxDistance) {
            isStopped = true;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkEndOfProjectile();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return isStopped;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    public void stop() {
        isStopped = true;
    }
}
