package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ElementalEntity;
import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.icoop.KeyBindings.PlayerKeyBindings;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopInventory;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.swing.Item;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements Interactor, ElementalEntity {

  private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {

    @Override
    public void interactWith(Door door, boolean isCellInteraction) {
      if (door.isOpen() && !isPassingDoor) {
        setDoorIsPassed(door);
        System.out.println("Door passed");
      }
    }

    @Override
    public void interactWith(ICoopCollectable collectable, boolean isCellInteraction) {
      Keyboard kbd = getOwnerArea().getKeyboard();
      if (isCellInteraction && kbd.get(playerKeyBindings.useItem()).isPressed()) {
        collectable.collect();
        isInteracting = true;
        ICoopItem item = collectable.getItem();
        if (item != null) {
          inventory.addPocketItem(item, 1);
        }
      }
  }

    @Override
    public void interactWith(Heart heart, boolean isCellInteraction) {
      isInteracting = true;
      Keyboard kbd = getOwnerArea().getKeyboard();
      if (isCellInteraction && kbd.get(playerKeyBindings.useItem()).isPressed()) {
        heart.collect();
        heartsCount++;
      }
    }

    @Override
    public void interactWith(Explosive explosive, boolean isCellInteraction) {
      Keyboard kbd = getOwnerArea().getKeyboard();
      isInteracting = true;
      if (!kbd.get(playerKeyBindings.useItem()).isPressed())
          return;
        if (!isCellInteraction)
          explosive.setExploding();
      this.interactWith((ICoopCollectable) explosive, isCellInteraction);
    }

    @Override
    public void interactWith(ElementalItem item, boolean isCellInteraction) {
      Keyboard kbd = getOwnerArea().getKeyboard();
      if (!kbd.get(playerKeyBindings.useItem()).isPressed())
        return;
      if (isCellInteraction && element == item.element()) {
        item.collect();
        elementalInventory.add(item);
      }
      this.interactWith((ICoopCollectable) item, isCellInteraction);
    }

    public void interactWith(PressurePlate pressurePlate, boolean isCellInteractable) {
      if (isCellInteractable)
        enterPressurePlate(pressurePlate);
    }

    @Override
    public void interactWith(Foe foe, boolean isCellInteraction) {
      if (state == State.SWORD_USE) {
        foe.hit(1, null);
      }
      if (state == State.STAFF_USE) {
        foe.hit(1, element);
      }
    }

    @Override
    public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
      if (state == State.SWORD_USE) {
        if (player != ICoopPlayer.this)
          player.hit(1, null);
      }
    }
  }

  private final PlayerKeyBindings playerKeyBindings;
  private final OrientedAnimation sprite;
  private final OrientedAnimation swordSprite;
  private final OrientedAnimation staffSprite;
  private final static int MOVE_DURATION = 16;
  private final ICoopPlayerInteractionHandler interactionHandler = new ICoopPlayerInteractionHandler();
  private final static int MAX_LIFE = 100;
  private final Health health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, true);
  private final NaturalElement element;
  private final static int DAMAGE_COOLDOWN = 24;
  private final ICoopInventory inventory = new ICoopInventory();
  private final ICoop game;
  private final LinkedList<ElementalItem> elementalInventory = new LinkedList<>();
  private int damageCooldown = 0;
  private State state = State.IDLE;
  private PressurePlate pressurePlate = null;
  private int heartsCount = 0;
  private Boolean isInteracting = false;
  private Boolean isPassingDoor = false;

  private enum State {
    IDLE,
    SWORD_USE,
    STAFF_USE,
    HIT
  }

  private OrientedAnimation getSwordSprite(String prefix) {
    final Vector anchor = new Vector(-.5f, 0);
    final Orientation[] orders = {DOWN , UP, RIGHT , LEFT};
    final int SWORD_ANIMATION_DURATION = 2;
    return new OrientedAnimation(prefix+".sword", SWORD_ANIMATION_DURATION, this,
            anchor, orders, 4, 2, 2, 32, 32);
  }

  private OrientedAnimation getStaffSprite(String prefix) {
    final Vector anchor = new Vector(-.5f, -.20f);
    final Orientation[] orders = {DOWN, UP, RIGHT, LEFT};
    String staffName = element == NaturalElement.FIRE ? ".staff_fire" : ".staff_water";
    final int STAFF_ANIMATION_DURATION = 2;
    return new OrientedAnimation(prefix + staffName, STAFF_ANIMATION_DURATION, this,
            anchor, orders, 4, 2, 2, 32, 32);
  }

  private OrientedAnimation getSprite(String prefix) {
    final Vector anchor = new Vector(0, 0);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    final int ANIMATION_DURATION = 4;
    return new OrientedAnimation(prefix, ANIMATION_DURATION, this,
            anchor, orders, 4, 1, 2, 16, 32, true);
  }


  public ICoopPlayer(ICoopArea area, ICoop game, String spriteName, NaturalElement element, DiscreteCoordinates spawnPositionCoordinates, PlayerKeyBindings playerKeyBindings) {
    super(area, DOWN, spawnPositionCoordinates);
    this.element = element;
    this.sprite = getSprite(spriteName);
    this.swordSprite = getSwordSprite(spriteName);
    this.staffSprite = getStaffSprite(spriteName);
    this.playerKeyBindings = playerKeyBindings;
    this.game = game;
    enterArea(area, spawnPositionCoordinates);
  }

  @Override
  public NaturalElement element() {
    return element;
  }

  /**
   * makes the player entering a given area
   * @param area     (Area):  the area to be entered, not null
   * @param destination (DiscreteCoordinates): initial position in the entered area, not null
   */
  public void enterArea(ICoopArea area, DiscreteCoordinates destination) {
    setCurrentPosition(destination.toVector());
    area.registerActor(this);
    setOwnerArea(area);
    isPassingDoor = false;
  }

  public void leaveArea() {
    resetMotion();
    getOwnerArea().unregisterActor(this);
  }

  public void enterPressurePlate(PressurePlate targetPlate) {
    pressurePlate = targetPlate;
    pressurePlate.enter();
  }

  public void leavePressurePlate() {
    pressurePlate.leave();
    pressurePlate = null;
  }

  @Override
  public void onLeaving(List<DiscreteCoordinates> coordinates) {
    super.onLeaving(coordinates);
    if (pressurePlate == null)
      return;
    if (new HashSet<>(coordinates).containsAll(pressurePlate.getCurrentCells())) {
      leavePressurePlate();
    }
  }

  public ICoopItem getCurrentInventoryItem() {
    return inventory.getCurrentItem();
  }

  public void reset() {
    resetMotion();
    health.resetHealth();
    state = State.IDLE;
    damageCooldown = 0;
    orientate(DOWN);
    elementalInventory.clear();
  }

  @Override
  public boolean takeCellSpace() {
    return true;
  }

  @Override
  public boolean isCellInteractable() {
    return true;
  }

  @Override
  public boolean isViewInteractable() {
    return true;
  }

  @Override
  public List<DiscreteCoordinates> getCurrentCells() {
      return Collections.singletonList(getCurrentMainCellCoordinates());
  }

  @Override
  public List<DiscreteCoordinates> getFieldOfViewCells() {
    return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
  }

  @Override
  public boolean wantsCellInteraction() {
    return true;
  }

  public Boolean canResist(NaturalElement element) {
    for (ElementalItem item : elementalInventory) {
      if (item.element() == element) {
        return true;
      }
    }
    return false;
  }

  public void handleDeath() {
    if (heartsCount > 0) {
      heartsCount--;
      health.resetHealth();
    } else {
      game.handleAreaReset();
    }
  }

  public void hit(int damage, NaturalElement element) {
    if (!canResist(element)) {
      health.decrease(damage);
      if (health.isOff())
        handleDeath();
      else
        setHitState();
    }
  }

  @Override
  public void interactWith(Interactable other, boolean isCellInteraction) {
      other.acceptInteraction(interactionHandler, isCellInteraction);
  }

  @Override
  public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
  }

  public void setDoorIsPassed(Door door) {
    resetMotion();
    isPassingDoor = true;
    game.switchArea(door.getDestinationName(), door.getDestinationCoordinates());
  }

  @Override
  public boolean wantsViewInteraction() {
    return true;
  }

  public Orientation movePlayer(Keyboard keyboard) {

    if (state != State.IDLE) {
      return null;
    }
    if (keyboard.get(playerKeyBindings.up()).isDown()) {
      return Orientation.UP;
    } else if (keyboard.get(playerKeyBindings.down()).isDown()) {
      return DOWN;
    } else if (keyboard.get(playerKeyBindings.left()).isDown()) {
      return LEFT;
    } else if (keyboard.get(playerKeyBindings.right()).isDown()) {
      return RIGHT;
    }
    return null;
  }

  public Boolean ensurePlayerDoesntMoveOutside(Orientation orientation) {
    int width = getOwnerArea().getWidth();
    int height = getOwnerArea().getHeight();
    Vector nextPosition = getCurrentMainCellCoordinates().jump(orientation.toVector()).toVector();
    return nextPosition.x >= 0 && nextPosition.x < width && nextPosition.y >= 0 && nextPosition.y < height;
  }

  private void setHitState() {
    state = State.HIT;
    damageCooldown = DAMAGE_COOLDOWN;
  }

  private void handleHitState() {
    if (damageCooldown > 0) {
      damageCooldown--;
    } else {
      state = State.IDLE;
    }
  }

  private void handleInventory(Keyboard kbd) {

    if (state != State.IDLE) {
      return;
    }

    if (kbd.get(playerKeyBindings.switchItem()).isPressed()) {
      inventory.switchItem();
    }

    if (kbd.get(playerKeyBindings.useItem()).isPressed() && !isInteracting) {
      ICoopItem currentItem = inventory.getCurrentItem();
      if (currentItem == null)
        return;
      switch (currentItem) {
        case EXPLOSIVE:
          Explosive explosive = new Explosive((ICoopArea) getOwnerArea(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
          getOwnerArea().registerActor(explosive);
          inventory.removePocketItem(currentItem, 1);
          break;
        case FIRE_STAFF, WATER_STAFF:
          state = State.STAFF_USE;
          staffSprite.reset();
          break;
          case SWORD:
            System.out.println(getCurrentCells());
            state = State.SWORD_USE;
            swordSprite.reset();
            break;
      }
    }
  }

  private OrientedAnimation getAnimation() {
      return switch (state) {
          case SWORD_USE -> swordSprite;
          case STAFF_USE -> staffSprite;
          default -> sprite;
      };
  }

  public void update(float deltaTime) {
    Keyboard keyboard = getOwnerArea().getKeyboard();
    super.update(deltaTime);
    handleInventory(keyboard);
    if (isInteracting)
        isInteracting = false;
    switch (state) {
      case IDLE:
        Orientation move = movePlayer(keyboard);
        if (move != null) {
          if (!isDisplacementOccurs()){
            orientate(move);
            move(MOVE_DURATION / 2);
          }
          getAnimation().update(deltaTime);
        } else
          getAnimation().reset();
        break;
      case HIT:
        handleHitState();
        break;
      case SWORD_USE:
        if (swordSprite.isCompleted()) {
          state = State.IDLE;
        }
        swordSprite.update(deltaTime);
        break;
      case STAFF_USE:
        staffSprite.update(deltaTime);
        if (staffSprite.isCompleted()) {
          Ball ball = new Ball(getOwnerArea(), getOrientation(), element, getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
          getOwnerArea().registerActor(ball);
          state = State.IDLE;
        }
        staffSprite.update(deltaTime);
        break;
    }
  }

  @Override
  public void draw(Canvas canvas) {
    OrientedAnimation animation = getAnimation();
    if (state != State.HIT || damageCooldown % 2 == 0) {
      animation.draw(canvas);
    }
      health.draw(canvas);
  }
}
