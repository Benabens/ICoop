package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Staff extends ElementalItem {

    private final Animation animation;
    private final NaturalElement element;

    public Staff(ICoopArea area, NaturalElement element, Orientation orientation, DiscreteCoordinates position) {
        super(area, element, orientation, position);
        animation = new Animation(getSpriteName(element), 8, 2, 2, this , 32, 32,
                new Vector(-0.5f, 0),
                5, true);
        this.element = element;
    }

    private String getSpriteName(NaturalElement element) {
        return switch (element) {
            case FIRE -> "icoop/staff_fire";
            case WATER -> "icoop/staff_water";
            default -> null;
        };
    }

    @Override
    public ICoopItem getItem() {
        return switch (element) {
            case FIRE -> ICoopItem.FIRE_STAFF;
            case WATER -> ICoopItem.WATER_STAFF;
            default -> null;
        };
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isCollected()) {
            animation.draw(canvas);
        }
    }
}
