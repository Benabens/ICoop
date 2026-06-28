package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.DialogHandler;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Orb extends ElementalItem {

    private final DialogHandler dialogHandler;
    private final OrientedAnimation animation;
    private final Dialog dialog;

    private Orientation matchOrientation(NaturalElement element) {
        return switch (element) {
            case EARTH -> Orientation.RIGHT;
            case FIRE -> Orientation.UP;
            case AIR -> Orientation.LEFT;
            default -> Orientation.DOWN;
        };
    }
    
    // ElementalItem(ICoopArea area, NaturalElement element, Orientation orientation, DiscreteCoordinates position)
    public Orb(ICoopArea area, DialogHandler dialogHandler, NaturalElement element, DiscreteCoordinates position) {
        super(area, element, null, position);
        this.dialogHandler = dialogHandler;
        orientate(matchOrientation(element));
        String dialogName = element == NaturalElement.FIRE ? "orb_fire_msg" : "orb_water_msg";
        dialog = new Dialog(dialogName);
        Orientation[] orientations = new Orientation[] { Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT };
        animation = new OrientedAnimation("icoop/orb", 5, this, new Vector(0, 0), orientations, 4, 1, 1, 32, 32, true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (dialog.isCompleted() && isCollected())
            return;
        animation.draw(canvas);
    }

    @Override
    public ICoopItem getItem() {
        return null;
    }

    @Override
    public void collect() {
        super.collect();
        dialogHandler.publish(dialog);
    }
}
