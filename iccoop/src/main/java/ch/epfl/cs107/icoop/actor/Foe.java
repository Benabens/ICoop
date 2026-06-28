package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.Drawable;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Graphics;
import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public abstract class Foe extends MovableAreaEntity implements Interactor {

    private enum HealthState {
        ALIVE,
        DEAD
    }

    private static final int MAX_LIFE = 100;
    private final Animation death = new Animation("icoop/vanish", 7, 1, 1, this, 32, 32, new Vector(0.f, 0.f), 5, false);
    private final Health health;
    private final List<ElementalEntity.NaturalElement> vulnerabilites;
    private HealthState state = HealthState.ALIVE;

    protected abstract OrientedAnimation getAliveAnimation();

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    protected Foe(Area area, Orientation orientation, DiscreteCoordinates position,
               List<ElementalEntity.NaturalElement> vulnerabilities, int maxLife) {
        super(area, orientation, position);
        health = new Health(this, Transform.I.translated(0, 1.75f), maxLife, false);
        this.vulnerabilites = vulnerabilities;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    public void hit(int damage, ElementalEntity.NaturalElement element) {
        if (state == HealthState.DEAD)
            return;
        if (element == null || !vulnerabilites.contains(element))
            health.decrease(damage);
        if (health.isOff()) {
            state = HealthState.DEAD;
            death.reset();
        }
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public boolean takeCellSpace() {
        return state == HealthState.ALIVE;
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
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        switch (state) {
            case ALIVE:
                getAliveAnimation().update(deltaTime);
                break;
            case DEAD:
                death.update(deltaTime);
                if (death.isCompleted())
                    getOwnerArea().unregisterActor(this);
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch (state) {
            case ALIVE:
                getAliveAnimation().draw(canvas);
                health.draw(canvas);
                break;
            case DEAD:
                death.draw(canvas);
                break;
        }
    }
}
