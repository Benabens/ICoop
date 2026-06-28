package ch.epfl.cs107.icoop.area;

import java.util.List;

import ch.epfl.cs107.icoop.DialogHandler;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosive;
import ch.epfl.cs107.icoop.actor.Heart;
import ch.epfl.cs107.icoop.actor.PressurePlate;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Spawn extends ICoopArea {

    private Door doorToOrbway;
    private Door doorToMaze;
    private Rock rock;
    private Explosive explosive;
    private Background background;
    private Foreground foreground;
    private Heart heart;
    private PressurePlate pressurePlate;
    private ManorDoor manorDoor;
    private final Logic isManorDoorOpen;


    /**
     * Door to the Manor, same as a Door but with triggers some dialog when it is closed
     */
    private class ManorDoor extends Door {
        public ManorDoor(String destination, Area area, Logic signal, List<DiscreteCoordinates> destinationCoordinates, DiscreteCoordinates mainCellCoordinates) {
            super(destination, area, signal, destinationCoordinates, mainCellCoordinates);
        }

        @Override
        public boolean isOpen() {
            dialogHandler.publish(new Dialog(super.isOpen() ? "victory" : "key_required"));
            return false;
        }
    }


    /**
     * Default constructor for Spawn
     * @param game (DialogHandler): The dialog handler, not null
     * @param isManorDoorOpen (Logic): The logic that determines if the manor door is open, not null
     */
    public Spawn(DialogHandler game, Logic isManorDoorOpen) {
        super(game);
        game.publish(new Dialog("welcome"));
        this.isManorDoorOpen = isManorDoorOpen;
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }

    /**
     * Create the door to the Orbway
     */
    private void createDoorToOrbway() {
        List<DiscreteCoordinates> destinations = List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5));
        DiscreteCoordinates mainCoordinates = new DiscreteCoordinates(19, 15);
        DiscreteCoordinates otherCoordinates = new DiscreteCoordinates(19, 16);
        doorToOrbway = new Door("OrbWay", this, Logic.TRUE, destinations, mainCoordinates, otherCoordinates);
    }

    /**
     * Create the door to the Maze
     */
    private void createDoorToMaze() {
        List<DiscreteCoordinates> destinations = List.of(new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39));
        DiscreteCoordinates mainCoordinates = new DiscreteCoordinates(4, 0);
        DiscreteCoordinates otherCoordinates = new DiscreteCoordinates(5, 0);
        doorToMaze = new Door("Maze", this, Logic.TRUE, destinations, mainCoordinates, otherCoordinates);
    }

    /**
     * Create all the ressources of the area
     */
    private void createRessouces() {
        createDoorToMaze();
        createDoorToOrbway();
        manorDoor = new ManorDoor("Arena", this, isManorDoorOpen, null, new DiscreteCoordinates(6, 11));
        rock = new Rock(this, new DiscreteCoordinates(10, 10));
        explosive = new Explosive(this, new DiscreteCoordinates(11, 10));
        background = new Background(this);
        foreground = new Foreground(this);
        heart = new Heart(this, new DiscreteCoordinates(12, 14));
        pressurePlate = new PressurePlate(this, new DiscreteCoordinates(12, 8));
    }

    @Override
    protected void createArea() {
        createRessouces();
        registerActor(background);
        registerActor(foreground);
        registerActor(doorToOrbway);
        registerActor(rock);
        registerActor(explosive);
        registerActor(heart);
        registerActor(pressurePlate);
        registerActor(doorToMaze);
        registerActor(manorDoor);
    }

    @Override
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return List.of(new DiscreteCoordinates(12, 6), new DiscreteCoordinates(14, 6));
    }

    @Override
    public boolean isOff() {
        return false;
    }

    @Override
    public boolean isOn() {
        return true;
    }
}
