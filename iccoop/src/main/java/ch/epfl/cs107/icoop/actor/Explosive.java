package ch.epfl.cs107.icoop.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Explosive extends ICoopCollectable implements Interactor {

    private enum State {
        UNEXPLODED,
        EXPLODING,
        EXPLODED,
        DESTROYED
    }

    private boolean handleInteraction = false;
    private class ExplosiveInteractionHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if (!handleInteraction)
                return;
            rock.destroy();
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (!handleInteraction)
                return;
            player.hit(30, null);
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (!handleInteraction)
                return;
            explosive.setExploding();
        }

        @Override
        public void interactWith(ElementalWall wall, boolean isCellInteraction) {
            if (!handleInteraction)
                return;
            wall.destroy();
        }
    }

    @Override
    public ICoopItem getItem() {
        return ICoopItem.EXPLOSIVE;
    }

    private final float explosionTime = 5.0f;
    private float explosionTimer = 0.0f;
    private Animation sprite;
    private final Animation unexploded;
    private final Animation exploded;
    private State state = State.UNEXPLODED;
    private final ExplosiveInteractionHandler interactionHandler = new ExplosiveInteractionHandler();

    public Explosive(ICoopArea area, DiscreteCoordinates position) {
        super(area, null, position);
        unexploded = new Animation("icoop/explosive", 2, 1.5f, 1.5f, this, 16, 16, new Vector(0.f, 0.f), 5, true);
        exploded = new Animation("icoop/explosion", 7, 1.5f, 1.5f, this, 32, 32, new Vector(0.f, 0.f), 5, false);
        sprite = unexploded;
        sprite.switchPause();
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        DiscreteCoordinates[] fieldCoordinates = {
            getCurrentMainCellCoordinates().jump(Orientation.DOWN.toVector()),
            getCurrentMainCellCoordinates().jump(Orientation.UP.toVector()),
            getCurrentMainCellCoordinates().jump(Orientation.LEFT.toVector()),
            getCurrentMainCellCoordinates().jump(Orientation.RIGHT.toVector())
        };
        return List.of(fieldCoordinates);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        sprite.update(deltaTime);
        if (handleInteraction)
            handleInteraction = false;
        if (state == State.EXPLODED && sprite.isCompleted()) {
            getOwnerArea().unregisterActor(this);
        }
        if (state == State.EXPLODING) {
            explosionTimer += deltaTime;
            if (explosionTimer >= explosionTime) {
                setExploded();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!sprite.isCompleted() && !isCollected())
            sprite.draw(canvas);
    }

    // INTERACTOR ----------------------------------------------------------------

    @Override
    public boolean wantsCellInteraction() {
        return state == State.EXPLODED;
    }

    @Override
    public boolean wantsViewInteraction() {
        return state == State.EXPLODED;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    };

    // ----------------------------------------------------------------------------

    // INTERACTABLE ---------------------------------------------------------------

    @Override
    public boolean isCellInteractable() {
        return state == State.UNEXPLODED;
    }

    @Override
    public boolean isViewInteractable() {
        return state == State.UNEXPLODED || state == State.EXPLODING;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    // ----------------------------------------------------------------------------

    public void setExploding() {
        state = State.EXPLODING;
        sprite.switchPause();
    }

    public void setExploded() {
        handleInteraction = true;
        state = State.EXPLODED;
        sprite = exploded;
        sprite.reset();
    }

    // public void interactWith(Rock other, boolean isCellInteraction) {
    //     other.acceptInteraction(null, isCellInteractable());
    // }
}
