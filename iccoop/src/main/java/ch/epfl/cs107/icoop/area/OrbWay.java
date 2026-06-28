package ch.epfl.cs107.icoop.area;

import java.util.List;

import ch.epfl.cs107.icoop.DialogHandler;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.FireWall;
import ch.epfl.cs107.icoop.actor.Heart;
import ch.epfl.cs107.icoop.actor.Orb;
import ch.epfl.cs107.icoop.actor.PressurePlate;
import ch.epfl.cs107.icoop.actor.WaterWall;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public class OrbWay extends ICoopArea {

    private Door doorToSpawn1;
    private Door doorToSpawn2;
    private Background background;
    private Foreground foreground;
    private Orb fireOrb;
    private Orb waterOrb;
    private final FireWall[] fireWalls = new FireWall[5];
    private final WaterWall[] waterWalls = new WaterWall[5];
    private Heart[] hearts;
    private PressurePlate pressurePlate1;
    private PressurePlate pressurePlate2;

    public OrbWay(DialogHandler dialogHandler) {
        super(dialogHandler);
    }

    /**
     * Create the fire walls
     */
    private void createFireWalls() {
        for (int i = 0; i < 5; i++) {
            fireWalls[i] = new FireWall(this, pressurePlate1, Orientation.LEFT, new DiscreteCoordinates(12, 10+i));
            registerActor(fireWalls[i]);
        }
    }

    /**
     * Create the water walls
     */
    private void createWaterWalls() {
        for (int i = 0; i < 5; i++) {
            waterWalls[i] = new WaterWall(this, pressurePlate2, Orientation.LEFT, new DiscreteCoordinates(12, 4+i));
            registerActor(waterWalls[i]);
        }
    }

    /**
     * Create the ressources of the area
     */
    private void createRessources() {
        DiscreteCoordinates door1MainCoordinates = new DiscreteCoordinates(0, 14);
        DiscreteCoordinates door2MainCoordinates = new DiscreteCoordinates(0, 8);
        DiscreteCoordinates[] door1otherCoordinates = {
            new DiscreteCoordinates(0, 13),
            new DiscreteCoordinates(0, 12),
            new DiscreteCoordinates(0, 11),
            new DiscreteCoordinates(0, 10)
        };
        DiscreteCoordinates[] door2OotherCoordinates = {
            new DiscreteCoordinates(0, 7),
            new DiscreteCoordinates(0, 6),
            new DiscreteCoordinates(0, 5),
            new DiscreteCoordinates(0, 4)
        };
        List<DiscreteCoordinates> destinations = List.of(new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15));
        doorToSpawn1 = new Door("Spawn", this, Logic.TRUE, destinations, door1MainCoordinates, door1otherCoordinates);
        doorToSpawn2 = new Door("Spawn", this, Logic.TRUE, destinations, door2MainCoordinates, door2OotherCoordinates);
        background = new Background(this);
        foreground = new Foreground(this);
        pressurePlate1 = new PressurePlate(this, new DiscreteCoordinates(5, 7));
        pressurePlate2 = new PressurePlate(this, new DiscreteCoordinates(5, 10));
        fireOrb = new Orb(this, getDialogHandler(), Orb.NaturalElement.FIRE, new DiscreteCoordinates(17, 12));
        waterOrb = new Orb(this, getDialogHandler(), Orb.NaturalElement.WATER, new DiscreteCoordinates(17, 6));
        createFireWalls();
        createWaterWalls();
        createHearts();
    }

    /**
     * Create the hearts
     */
    private void createHearts() {
        hearts = new Heart[] {
            new Heart(this, new DiscreteCoordinates(8, 4)),
            new Heart(this, new DiscreteCoordinates(10, 6)),
            new Heart(this, new DiscreteCoordinates(5, 13)),
            new Heart(this, new DiscreteCoordinates(10, 11)),
        };
    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }

    @Override
    protected void createArea() {
        createRessources();
        registerActor(foreground);
        registerActor(background);
        registerActor(doorToSpawn1);
        registerActor(doorToSpawn2);
        registerActor(fireOrb);
        registerActor(waterOrb);
        registerActor(pressurePlate1);
        registerActor(pressurePlate2);
        for (int i = 0; i < 4; i++) {
            registerActor(hearts[i]);
        }
    }

    @Override
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5));
    }

    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public boolean isOff() {
        return false;
    }
}

