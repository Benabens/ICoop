package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BombFoe extends Foe {

    private class BombFoeHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            state = State.ATTACKING;
            target = player;
        }
    }

    /**
     * The different states of the foe
     */
    private enum State {
        IDLE,
        ATTACKING,
        PROTECTING
    }

    /**
     * Speed factors for the different states
     */
    private enum SpeedFactor {
        N0RMAL(2),
        FAST(6),
        SLOW(1);

        private final int value;

        SpeedFactor(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    private final OrientedAnimation idleAnimation;
    private final OrientedAnimation protectingAnimation;
    private final float[] minMax = new float[]{0.5f, 2.5f};
    private float idleTime;
    private State state = State.IDLE;
    private final int VIEW_DEPTH = 8;
    private final int MAX_IDLE_TIME = 24;
    private ICoopPlayer target = null;
    private final int ANIMATION_DURATION = 5;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area            (Area): Owner area. Not null
     * @param orientation     (Orientation): Initial orientation of the entity. Not null
     * @param position        (Coordinate): Initial position of the entity. Not null
     */

    public BombFoe(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, List.of(ElementalEntity.NaturalElement.AIR, ElementalEntity.NaturalElement.WATER), 100);
        Orientation[] orders = new Orientation []{ Orientation.UP, Orientation.LEFT , Orientation.DOWN , Orientation.RIGHT};
        idleAnimation =  new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION, this,
                Vector.ZERO, orders, 4, 2, 2, 32, 32, true);
        protectingAnimation =  new OrientedAnimation("icoop/bombFoe.protecting", ANIMATION_DURATION, this,
                Vector.ZERO, orders, 4, 2, 2, 32, 32, true);
    }

    @Override
    protected OrientedAnimation getAliveAnimation() {
        if (state == State.PROTECTING) {
            return protectingAnimation;
        }
        return idleAnimation;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new BombFoeHandler(), isCellInteraction);
    }

    public void resetInterval() {
        idleTime = RandomGenerator.getInstance().nextFloat(0, MAX_IDLE_TIME);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    private void throwObject() {
        Explosive explosive = new Explosive((ICoopArea) getOwnerArea(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        if (getOwnerArea().canEnterAreaCells(explosive, List.of(explosive.getCurrentMainCellCoordinates()))) {
            getOwnerArea().registerActor(explosive);
            explosive.setExploding();
        }
    }

    /**
     * Orientate the foe in the given direction if possible
     */
    private void initiateRandomMove() {
        float orientationChangeProbability = 0.4f;
        double randDouble = RandomGenerator.getInstance().nextDouble();
        if (randDouble < orientationChangeProbability) {
            orientate(Orientation.fromInt(RandomGenerator.getInstance().nextInt(4)));
        };
    }

    /**
     * Move the foe towards the player
     */
    private void initiateTowardsPlayerMove() {
        if (target == null) {
            return;
        }
        Vector playerPosition = target.getCurrentMainCellCoordinates().toVector();
        Vector currentPosition = getCurrentMainCellCoordinates().toVector();
        Vector diff = playerPosition.sub(currentPosition);
        float deltaX = diff.x;
        float deltaY = diff.y;
        Vector vector = Math.abs(deltaX) > Math.abs(deltaY) ? new Vector(deltaX, 0) : new Vector(0, deltaY);
        if (!orientate(Orientation.fromVector(vector))) {
            move(ANIMATION_DURATION / SpeedFactor.FAST.getValue());
        } else
            move(ANIMATION_DURATION / SpeedFactor.N0RMAL.getValue());
    }

    /**
     * Triggered when the idle time ends, decides what to do next based on the current state
     */
    private void handleIdleEnd()  {
        idleTime = 0;
        if (state == State.IDLE &&  !isDisplacementOccurs()) {
            initiateRandomMove();
            resetInterval();
        }
        if (state == State.ATTACKING) {
            initiateTowardsPlayerMove();
            throwObject();
            state = State.PROTECTING;
        }
        if (state == State.PROTECTING) {
            move(ANIMATION_DURATION / SpeedFactor.SLOW.getValue());
            target = null;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        idleTime -= 1;
        if (idleTime <= 0)
            handleIdleEnd();
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> view = new ArrayList<>();
        if (state == State.ATTACKING)
            return List.of(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        for (int i = 1; i <= VIEW_DEPTH; i++) {
            view.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().resized(i)));
        }
        return view;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return state == State.IDLE || state == State.ATTACKING;
    }
}
