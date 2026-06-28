package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Ball extends Projectile {

    public class BallHandler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Foe enemy, boolean isCellInteraction) {
            System.out.println("BallHandler.interactWith(Foe)");
            enemy.hit(1, element);
            stop();
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            explosive.setExploding();
            stop();
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            rock.destroy();
            stop();
        }
    }

    private final Animation animation;
    private final ElementalEntity.NaturalElement element;
    private static final int ANIMATION_DURATION = 12;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Ball(Area area, Orientation orientation, ElementalEntity.NaturalElement element, DiscreteCoordinates position) {
        super(area, orientation, position, 20, 2);
        animation = new Animation(getSpriteName(element), 4, 1, 1, this , 32, 32,
                ANIMATION_DURATION / 4, true);
        this.element = element;
    }

    /**
     * Get the name of the sprite corresponding to the element
     * @param element (ElementalEntity.NaturalElement): the element of the ball
     * @return (String): the name of the sprite
     */
    private String getSpriteName(ElementalEntity.NaturalElement element) {
        return switch (element) {
            case FIRE -> "icoop/magicFireProjectile";
            case WATER -> "icoop/magicWaterProjectile";
            default -> null;
        };
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new BallHandler(), isCellInteraction);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        animation.draw(canvas);
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }
}
