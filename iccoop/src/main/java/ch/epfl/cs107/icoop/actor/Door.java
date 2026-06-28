package ch.epfl.cs107.icoop.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Door extends AreaEntity {

    private final List<DiscreteCoordinates> coordinates;
    private final List<DiscreteCoordinates> destinationCoordinates;
    private final Logic signal;
    private final String destination;


    /**
     * Default constructor for Door
     * @param destination (String): The name of the destination area, not null
     * @param area (Area): The area, not null
     * @param signal (Logic): The signal that determines if the door is open, not null
     * @param destinationCoordinates (List<DiscreteCoordinates>): The coordinates of the destination, not null
     * @param mainCellCoordinates (DiscreteCoordinates): The coordinates of the main cell, not null
     */
    public Door(String destination, Area area, Logic signal, List<DiscreteCoordinates> destinationCoordinates,
    DiscreteCoordinates mainCellCoordinates) {
        super(area, Orientation.DOWN, mainCellCoordinates);
        coordinates = Collections.singletonList(mainCellCoordinates);
        this.signal = signal;
        this.destination = destination;
        this.destinationCoordinates = destinationCoordinates;
    }

    /**
     * Same as the default constructor but for an acessibility bigger than one cell
     * @param destination (String): The name of the destination area, not null
     * @param area (Area): The area, not null
     * @param signal (Logic): The signal that determines if the door is open, not null
     * @param destinationCoordinates (List<DiscreteCoordinates>): The coordinates of the destination, not null
     * @param mainCellCoordinates (DiscreteCoordinates): The coordinates of the main cell, not null
     * @param otherCells (DiscreteCoordinates): The coordinates of the other cells, not null
     */
    public Door(String destination, Area area, Logic signal, List<DiscreteCoordinates> destinationCoordinates,
    DiscreteCoordinates mainCellCoordinates, DiscreteCoordinates... otherCells) {
        super(area, Orientation.DOWN, mainCellCoordinates);
        coordinates = new ArrayList<>(List.of(mainCellCoordinates));
        coordinates.addAll(List.of(otherCells));
        this.signal = signal;
        this.destination = destination;
        this.destinationCoordinates = destinationCoordinates;
    }

    public boolean isOpen() {
        return signal.isOn();
    }
    
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return coordinates;
    }

    public String getDestinationName() {
        return destination;
    }

    public List<DiscreteCoordinates> getDestinationCoordinates() {
        return destinationCoordinates;
    }
        
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
