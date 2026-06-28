package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public class FireWall extends ElementalWall {

    public FireWall(Area area, Logic signal, Orientation orientation, DiscreteCoordinates position) {
        super("fire_wall", NaturalElement.FIRE, area, signal, orientation, position);
    }

    @Override
    public void damage(ICoopPlayer player) {
        player.hit(1, NaturalElement.FIRE);
    }
}
