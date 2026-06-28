package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopBehavior.ICoopCell;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 * This visitor is used to interact with the entities of the ICoop game
 * No default implementation is provided but a default implementation for each entity is provided so all interaction can be customized with more or less precision
 */
public interface ICoopInteractionVisitor extends AreaInteractionVisitor {

    default void interactWith(ICoopCell cell, boolean isCellInteraction) {
    }

    default void interactWith(Door door, boolean isCellInteraction) {
    }

    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {
    }

    default void interactWith(Explosive explosive, boolean isCellInteraction) {
    }

    default void interactWith(Rock rock, boolean isCellInteraction) {
    }

    default void interactWith(ICoopCollectable collectable, boolean isCellInteraction) {
    }

    default public void interactWith(ElementalItem item, boolean isCellInteraction) {
    }

    default public void interactWith(ElementalWall wall, boolean isCellInteraction) {
    }

    default public void interactWith(PressurePlate pressurePlate, boolean isCellInteractable) {
    }

    default public void interactWith(AreaEntity entity, boolean isCellInteraction) {
    }

    default public void interactWith(Heart heart, boolean isCellInteraction) {
    }

    default public void interactWith(Foe foe, boolean isCellInteraction) {

    }
}
