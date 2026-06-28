package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;

import java.util.LinkedList;
import java.util.List;

public class HellSkull extends Foe {

    private static class HellSkullHandler implements ICoopInteractionVisitor {

    }


    private final OrientedAnimation animation;
    private final float[] minMax = new float[]{0.5f, 2.5f};
    private float interval;
    private final List<Actor> actorsToAdd = new LinkedList<>();

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     */

    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, List.of(ElementalEntity.NaturalElement.AIR, ElementalEntity.NaturalElement.WATER), 100);
        Orientation[] orders = new Orientation []{ Orientation.UP, Orientation.LEFT , Orientation.DOWN , Orientation.RIGHT};
        animation =  new OrientedAnimation("icoop/flameskull", 5, this,
                new Vector(-0.5f, -0.5f), orders, 3, 2, 2, 32, 32, true);
        resetInterval();
    }

    public void resetInterval() {
        interval = RandomGenerator.getInstance().nextFloat(minMax[0], minMax[1]);
    }

    @Override
    protected OrientedAnimation getAliveAnimation() {
        return animation;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new HellSkullHandler(), isCellInteraction);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        interval -= deltaTime;
        if (interval <= 0) {
            resetInterval();
            Fire fire = new Fire(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
            actorsToAdd.add(fire);
        }
        if (((ICoopArea)getOwnerArea()).isInArea()) {
            for (Actor actor : actorsToAdd) {
                getOwnerArea().registerActor(actor);
            }
            actorsToAdd.clear();
        }
    }
}
