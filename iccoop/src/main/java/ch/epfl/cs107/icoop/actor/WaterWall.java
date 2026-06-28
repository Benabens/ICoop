package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public class WaterWall extends ElementalWall {

    // public ElementalWall(String spriteName, NaturalElement element, Area area, Logic signal, Orientation orientation, DiscreteCoordinates position) {
    public WaterWall(Area area, Logic signal, Orientation orientation, DiscreteCoordinates position) {
        super("water_wall", NaturalElement.WATER, area, signal, orientation, position);
    }

    @Override
    public void damage(ICoopPlayer player) {
        player.hit(1, NaturalElement.FIRE);
    }
}
