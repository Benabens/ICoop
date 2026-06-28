package ch.epfl.cs107.icoop.area;

import java.util.List;

import ch.epfl.cs107.icoop.DialogHandler;
import ch.epfl.cs107.icoop.actor.Obstacle;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

public abstract class ICoopArea extends Area implements Logic {

    public final static float DEFAULT_SCALE_FACTOR = 8.f;
	private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;
    protected final DialogHandler dialogHandler;
    protected Boolean isInArea = false;

    public ICoopArea(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    abstract public List<DiscreteCoordinates> getPlayerSpawnPosition();

    protected abstract void createArea();

    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     * Set the isInArea attribute to true
     */
    public void enter() {
        isInArea = true;
    }

    /**
     * Set the isInArea attribute to false
     */
    public void leave() {
        isInArea = false;
    }

    /**
     * tells if the area is currently displayed
     * used to ensure no new actors are added when an area switch is in progress to avoid race conditions
     * @return true if the area is currently displayed
     */
    public Boolean isInArea() {
        return isInArea;
    }

    public DialogHandler getDialogHandler() {
        return dialogHandler;
    }

    @Override
    public float getCameraScaleFactor() {
        return cameraScaleFactor;
    }

    /**
     * Compute the distance between two players
     * @param redPlayer the position of the red player
     * @param bluePlayer the position of the blue player
     * @return the distance between the two players
     */
    public float distanceBetweenPlayers(Vector redPlayer, Vector bluePlayer) {
        return (float)Math.sqrt(Math.pow(redPlayer.x - bluePlayer.x, 2) + Math.pow(redPlayer.y - bluePlayer.y, 2));
    }

    /**
     * Set the camera scale factor based on the distance between the two players.
     * leads to a zoom out effect when the players are far from each other
     * @param redPlayerPos the position of the red player
     * @param bluePlayerPos the position of the blue player
     */
    public void setCameraScaleFactor(Vector redPlayerPos, Vector bluePlayerPos) {
        this.cameraScaleFactor = (float)Math.max( DEFAULT_SCALE_FACTOR,
        DEFAULT_SCALE_FACTOR * 0.75 + distanceBetweenPlayers(redPlayerPos, bluePlayerPos) / 2);
    }

    @Override
    public boolean isViewCentered() {
        return true;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            ICoopBehavior behavior = new ICoopBehavior(window, getTitle());
            setBehavior(behavior);
            List<DiscreteCoordinates> rocks = behavior.getRockPositions();
            List<DiscreteCoordinates> obstacles = behavior.getObstaclePositions();
            createArea();
            for (DiscreteCoordinates rock : rocks) {
                registerActor(new Rock(this, rock));
            }
            for (DiscreteCoordinates obstacle : obstacles) {
                registerActor(new Obstacle(this, obstacle));
            }
            return true;
        }
        return false;
    }
}
