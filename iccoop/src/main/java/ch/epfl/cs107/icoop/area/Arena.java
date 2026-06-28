package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.DialogHandler;
import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Arena extends ICoopArea {

    private static class Portal extends Door {

        private final Sprite sprite = new Sprite("shadow", 1, 1, this , new RegionOfInterest(0, 0, 32, 32));

        public Portal(String destination, Area area, Logic signal, List<DiscreteCoordinates> destinationCoordinates, DiscreteCoordinates mainCellCoordinates, DiscreteCoordinates otherCellCoordinates) {
            super(destination, area, signal, destinationCoordinates, mainCellCoordinates, otherCellCoordinates);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (super.isOpen()) {
                sprite.draw(canvas);
            }
        }
    }

    private Background background;
    private Foreground foreground;
    private final Key redKey;
    private final Key blueKey;
    private final Portal portal;

    public Arena(DialogHandler game) {
        super(game);
        redKey = new Key(this, ElementalEntity.NaturalElement.FIRE, new DiscreteCoordinates(9, 16));
        blueKey = new Key(this, ElementalEntity.NaturalElement.WATER, new DiscreteCoordinates(9, 4));
        DiscreteCoordinates mainCoordinates = new DiscreteCoordinates(19, 15);
        DiscreteCoordinates otherCoordinates = new DiscreteCoordinates(19, 16);
        portal = new Portal("Spawn", this, new And(redKey, blueKey), List.of(new DiscreteCoordinates(6, 11)), mainCoordinates, otherCoordinates);
    }

    @Override
    public String getTitle() {
        return "Arena";
    }

    /**
     * Create the ressources of the area
     */
    private void createRessouces() {
        background = new Background(this);
        foreground = new Foreground(this);
    }

    @Override
    protected void createArea() {
        createRessouces();
        registerActor(background);
        registerActor(foreground);
        registerActor(redKey);
        registerActor(blueKey);
        registerActor(portal);
    }

    @Override
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {

        return List.of(new DiscreteCoordinates(4, 5), new DiscreteCoordinates(14, 15));
    }

    @Override
    public boolean isOn() {
        return new And(redKey, blueKey).isOn();
    }

    @Override
    public boolean isOff() {
        return new And(redKey, blueKey).isOff();
    }
}
