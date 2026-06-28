package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Key extends ICoopCollectable implements ElementalEntity, Logic {

    private final NaturalElement element;
    private final Sprite sprite;

    public Key(ICoopArea area, NaturalElement element, DiscreteCoordinates position) {
        super(area, null, position);
        this.element = element;
        this.sprite = new Sprite(getSpriteName(), 1, 1, this);
    }

    private String getSpriteName() {
        return switch (element) {
            case FIRE -> "icoop/key_red";
            case WATER -> "icoop/key_blue";
            default -> null;
        };
    };

    @Override
    public ICoopItem getItem() {
        return switch (element) {
            case FIRE -> ICoopItem.FIRE_KEY;
            case WATER -> ICoopItem.WATER_KEY;
            default -> null;
        };
    }

    @Override
    public NaturalElement element() {
        return element;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!isCollected()) {
            sprite.draw(canvas);
        }
    }

    @Override
    public boolean isOn() {
        return isCollected();
    }

    @Override
    public boolean isOff() {
        return !isCollected();
    }
}
