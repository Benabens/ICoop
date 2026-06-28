package ch.epfl.cs107.icoop.area;

import java.util.List;

import ch.epfl.cs107.icoop.DialogHandler;
import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Maze extends ICoopArea {

    private Background background;
    private Foreground foreground;
    private final FireWall[] fireWalls = new FireWall[3];
    private final FireWall[] fireWalls2 = new FireWall[3];
    private final FireWall[] fireWalls3 = new FireWall[3];
    private final WaterWall[] waterWalls = new WaterWall[3];
    private final HellSkull[] hellSkulls = new HellSkull[10];
    private final Heart[] hearts = new Heart[4];
    private final BombFoe[] bombFoes = new BombFoe[5];
    private PressurePlate pressurePlate;
    private Staff waterStaff;
    private Staff fireStaff;
    private Door doorToArena;


    public Maze(DialogHandler dialogHandler) {
        super(dialogHandler);
    }

    /**
     * Create the hearts
     */
    private void createHearts() {
        hearts[0] = new Heart(this, new DiscreteCoordinates(15, 18));
        hearts[1] = new Heart(this, new DiscreteCoordinates(16, 19));
        hearts[2] = new Heart(this, new DiscreteCoordinates(14, 19));
        hearts[3] = new Heart(this, new DiscreteCoordinates(14, 17));
    }

    /**
     * Create the bomb foes
     */
    private void createBombFoes() {
        bombFoes[0] = new BombFoe(this, Orientation.RIGHT, new DiscreteCoordinates(5, 15));
        bombFoes[1] = new BombFoe(this, Orientation.RIGHT, new DiscreteCoordinates(3, 10));
        bombFoes[2] = new BombFoe(this, Orientation.RIGHT, new DiscreteCoordinates(9, 11));
        bombFoes[3] = new BombFoe(this, Orientation.RIGHT, new DiscreteCoordinates(10, 17));
        bombFoes[4] = new BombFoe(this, Orientation.RIGHT, new DiscreteCoordinates(5, 14));
    }

    /**
     * Create the hell skulls
     */
    private void createHellSkulls() {
        hellSkulls[0] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 33));
        hellSkulls[1] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 31));
        hellSkulls[2] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 29));
        hellSkulls[3] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 27));
        hellSkulls[4] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(12, 25));
        hellSkulls[5] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 33));
        hellSkulls[6] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 32));
        hellSkulls[7] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 30));
        hellSkulls[8] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 28));
        hellSkulls[9] = new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(10, 26));
    }

    /**
     * Create the fire walls
     */
    private void createFireWalls() {
        for (int i = 0; i < 3; i++) {
            fireWalls[i] = new FireWall(this, pressurePlate, Orientation.LEFT, new DiscreteCoordinates(6, 34 + i));
            fireWalls2[i] = new FireWall(this, pressurePlate, Orientation.DOWN, new DiscreteCoordinates(2 + i, 34));
            fireWalls3[i] = new FireWall(this, pressurePlate, Orientation.DOWN, new DiscreteCoordinates(8, 21));
        }
    }

    /**
     * Create the water walls
     */
    private void createWaterWalls() {
        for (int i = 0; i < 3; i++) {
            waterWalls[i] = new WaterWall(this, pressurePlate, Orientation.LEFT, new DiscreteCoordinates(4, 34 + i));
        }
    }

    /**
     * Create all the ressources of the area
     */
    private void createRessources() {
        background = new Background(this);
        foreground = new Foreground(this);
        pressurePlate = new PressurePlate(this, new DiscreteCoordinates(9, 25));
        fireStaff = new Staff(this, ElementalEntity.NaturalElement.FIRE, Orientation.DOWN, new DiscreteCoordinates(13, 2));
        waterStaff = new Staff(this, ElementalEntity.NaturalElement.WATER, Orientation.DOWN, new DiscreteCoordinates(8, 2));
        createFireWalls();
        createWaterWalls();
        createHearts();
        createHellSkulls();
        createBombFoes();
        createDoorToArena();
    }

    /**
     * Create the door to the Arena
     */
    private void createDoorToArena() {
        List<DiscreteCoordinates> destinations = List.of(new DiscreteCoordinates(4, 5), new DiscreteCoordinates(14, 15));
        DiscreteCoordinates mainCellCoordinates = new DiscreteCoordinates(19, 6);
        DiscreteCoordinates otherCell = new DiscreteCoordinates(19, 7);
        doorToArena = new Door("Arena", this, Logic.TRUE,  destinations, mainCellCoordinates, otherCell);
    }

    @Override
    public String getTitle() {
        return "Maze";
    }

    @Override
    protected void createArea() {
        createRessources();
        registerActor(foreground);
        registerActor(background);
        registerActor(pressurePlate);
        registerActor(fireStaff);
        registerActor(waterStaff);
        registerActor(doorToArena);
        for (int i = 0; i < 4; i++) {
            registerActor(hearts[i]);
        }
        for (int i = 0; i < 3; i++) {
            registerActor(fireWalls2[i]);
            registerActor(waterWalls[i]);
            registerActor(fireWalls[i]);
            registerActor(fireWalls3[i]);
        }
        for (int i = 0; i < 10; i++) {
            registerActor(hellSkulls[i]);
        }
        for (int i = 0; i < 5; i++) {
            registerActor(bombFoes[i]);
        }
    }
    @Override
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return List.of(new DiscreteCoordinates(13, 2), new DiscreteCoordinates(8, 2));
    }

    @Override
    public boolean isOn() {
        return new And(fireStaff, waterStaff).isOn();
    }

    @Override
    public boolean isOff() {
        return new And(fireStaff, waterStaff).isOff();
    }
}
