package ch.epfl.cs107.icoop;


import java.util.List;
import java.util.Objects;

import ch.epfl.cs107.icoop.ElementalEntity.NaturalElement;
import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.Key;
import ch.epfl.cs107.icoop.area.*;
import ch.epfl.cs107.icoop.handler.ICoopPlayerStatusGUI;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICoop extends AreaGame implements DialogHandler {

    private final String[] areaNames = {"Spawn", "OrbWay", "Maze", "Arena"};
    private ICoopPlayer redPlayer;
    private ICoopPlayer bluePlayer;
    private CenterOfMass camera;
    private Dialog dialog = null;
    private ICoopPlayerStatusGUI redPlayerStatusGUI;
    private ICoopPlayerStatusGUI bluePlayerStatusGUI;
    private final ICoopArea[] areas = new ICoopArea[4];

    @Override
    public String getTitle() {
        return "ICoop";
    }


    /**
     * Create the areas of the game
     */
    private void createAreas() {
        areas[3] = new Arena(this);
        areas[2] = new Maze(this);
        areas[1] = new OrbWay(this);
        areas[0] = new Spawn(this, new And(areas[3], areas[2]));
        addArea(areas[0]);
        addArea(areas[1]);
        addArea(areas[2]);
        addArea(areas[3]);
    }

    /**
     * Match the area name with the index in the areaNames array
     * @param areaName the name of the area
     * @return the index of the area in the areaNames array
     */
    private Integer matchAreaIndex(String areaName) {
        for (int i = 0; i < areaNames.length; i++) {
            if (areaNames[i].equals(areaName)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Switch the area of the game
     * @param areaName the name of the area to switch to
     * @param playerPositions the positions of the players in the new area
     */
    public void switchArea(String areaName, List<DiscreteCoordinates> playerPositions) {
        Integer index = matchAreaIndex(areaName);
        if (index == null)
            return;
        redPlayer.leaveArea();
        bluePlayer.leaveArea();
        ICoopArea area = (ICoopArea)getCurrentArea();
        area.leave();
        setCurrentArea(areaName, false);
        area = (ICoopArea)getCurrentArea();
        area.enter();
        redPlayer.enterArea(area, playerPositions.get(0));
        bluePlayer.enterArea(area, playerPositions.get(1));
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            ICoopArea area = (ICoopArea)setCurrentArea(areaNames[2], true);
            List<DiscreteCoordinates> coords = area.getPlayerSpawnPosition();
            redPlayer = new ICoopPlayer(area, this,"icoop/player", NaturalElement.FIRE, coords.get(0), KeyBindings.RED_PLAYER_KEY_BINDINGS);
            bluePlayer = new ICoopPlayer(area, this, "icoop/player2", NaturalElement.WATER, coords.get(1), KeyBindings.BLUE_PLAYER_KEY_BINDINGS);
            redPlayerStatusGUI = new ICoopPlayerStatusGUI(redPlayer, false);
            bluePlayerStatusGUI = new ICoopPlayerStatusGUI(bluePlayer, true);
            camera = new CenterOfMass(redPlayer, bluePlayer);
            return true;
        }
        return false;
    }

    /**
     * Handle the reset of the area
     */
    public void handleAreaReset() {
        ICoopArea area = (ICoopArea)getCurrentArea();
        List<DiscreteCoordinates> coords = area.getPlayerSpawnPosition();
        redPlayer.leaveArea();
        bluePlayer.leaveArea();
        area.begin(getWindow(), getFileSystem());
        redPlayer.enterArea(area, coords.get(0));
        bluePlayer.enterArea(area, coords.get(1));
        redPlayer.reset();
        bluePlayer.reset();
    }

    /**
     * Handle the reset of the game
     */
    public void handleGameReset() {

        this.begin(getWindow(), getFileSystem());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector redPlayerPos = redPlayer.getPosition();
        Vector bluePlayerPos = bluePlayer.getPosition();
        Keyboard kbd = getWindow().getKeyboard();
        if (dialog != null && dialog.isCompleted())
            dialog = null;
        if (kbd.get(KeyBindings.RESET_GAME).isPressed())
            handleGameReset();
        if (kbd.get(KeyBindings.RESET_AREA).isPressed())
            handleAreaReset();
        if (kbd.get(KeyBindings.NEXT_DIALOG).isPressed() && dialog != null) {
            dialog.update(deltaTime);
        }
        ICoopArea area = (ICoopArea) getCurrentArea();
        area.setCameraScaleFactor(redPlayerPos, bluePlayerPos);
        area.setViewCandidate(camera);
    }

    @Override
    public void publish(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void draw() {
        super.draw();
        if (dialog != null) {
            dialog.draw(getWindow());
        }
        redPlayerStatusGUI.draw(getWindow());
        bluePlayerStatusGUI.draw(getWindow());
    }
}
