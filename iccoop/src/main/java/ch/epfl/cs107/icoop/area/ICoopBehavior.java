package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.actor.Obstacle;
import ch.epfl.cs107.icoop.actor.Unstoppable;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;

public class ICoopBehavior extends AreaBehavior {

    public ICoopBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                IcoopCellType type = IcoopCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, type.canWalk, type.canFly));
            }
        }
    }

    public List<DiscreteCoordinates> getRockPositions() {
        List<DiscreteCoordinates> rockPositions = new ArrayList<>();
        for (int y = 0; y < getHeight(); y++) {
            int r = getHeight() - 1 - y;
            for (int x = 0; x < getWidth(); x++) {
                if (getRGB(r, x) == IcoopCellType.ROCK.type) {
                    rockPositions.add(new DiscreteCoordinates(x, y));
                }
            }
        }
        return rockPositions;
    }

    public List<DiscreteCoordinates> getObstaclePositions() {
        List<DiscreteCoordinates> obstaclePositions = new ArrayList<>();
        for (int y = 0; y < getHeight(); y++) {
            int r = getHeight() - 1 - y;
            for (int x = 0; x < getWidth(); x++) {
                if (getRGB(r, x) == IcoopCellType.OBSTACLE.type) {
                    obstaclePositions.add(new DiscreteCoordinates(x, y));
                }
            }
        }
        return obstaclePositions;
    }

    public enum IcoopCellType {
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NULL(0, false , true),
        WALL(-16777216, false , true),
        IMPASSABLE (-8750470, false , true),
        INTERACT(-256, true , true),
        DOOR(-195580, true , true),
        WALKABLE(-1, true , true),
        ROCK(-16777204, true , true),
        OBSTACLE (-16723187, true , true)
        ;

        final int type;
        final boolean canWalk;
        final boolean canFly;

        IcoopCellType(int type, boolean isWalkable, boolean isFlyable) {
            this.type = type;
            this.canWalk = isWalkable;
            this.canFly = isFlyable;
        }

        public static IcoopCellType toType(int type) {
            for (IcoopCellType ict : IcoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            return NULL;
        }
    }

    public class ICoopCell extends Cell {

        private final boolean canWalk;
        private final boolean canFly;

        public ICoopCell(int x, int y, boolean canWalk, boolean canFly) {
            super(x, y);
            this.canWalk = canWalk;
            this.canFly = canFly;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            if (entity instanceof Unstoppable) {
                return canFly;
            }
            if (!canWalk)
                return false;
            if (entity instanceof ElementalEntity) {
                return canEnter((ElementalEntity) entity);
            }
            for (Interactable e : entities) {
                if (e.takeCellSpace()) {
                    return false;
                }
            }
            return true;
        }

        /**
         * ensure that 2 entities of different elements cannot be in the same cell
         * @param entity (ElementalEntity): the entity that wants to enter the cell
         * @return (boolean): true if the entity can enter the cell, false otherwise
         */
        protected boolean canEnter(ElementalEntity entity) {
            for (Interactable e : entities) {
                if (e instanceof ElementalEntity) {
                    ElementalEntity.NaturalElement element = ((ElementalEntity) e).element();
                    if (element != null && element != entity.element()) {
                        return false;
                    }
                }
                if (e.takeCellSpace()) {
                    return false;
                }
            }
            return true;
        }
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        public boolean isCellInteractable() {
            for (Interactable e : entities) {
                if (e.isCellInteractable()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isViewInteractable() {
            for (Interactable e : entities) {
                if (e.isViewInteractable()) {
                    return true;
                }
            }
            return false;
        }

          @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }
    }
}
