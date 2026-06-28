package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.engine.actor.Graphics;
import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * A GUI that shows information about the player on the screen.
 */
public class ICoopPlayerStatusGUI implements Graphics {

    private final static int DEPTH = 2000;
    private final ICoopPlayer player;
    private final boolean flipped;
    private ImageGraphics sprite;


    /**
     * Constructor for the ICoopPlayerStatusGUI
     * @param player (ICoopPlayer): the player to show the status of
     * @param flipped (boolean): whether the GUI should be flipped or not (left or right side of the screen)
     */
    public ICoopPlayerStatusGUI(ICoopPlayer player, boolean flipped) {
        this.player = player;
        this.flipped = flipped;
    }

    /**
     * return the sprite of the current item in the player's inventory
     * @param anchor (Vector): the anchor of the sprite
     *               (the bottom left corner of the screen is (0, 0))
     *               (the top right corner of the screen is (1, 1))
     * @param height (float): the height of the sprite
     
     */
    public ImageGraphics getCurrentItemSprite(Vector anchor, float height) {
        ICoopItem item = player.getCurrentInventoryItem();
        if (item == null)
            return null;
        String spriteName = "icoop/" + item.getSprite();
        return new ImageGraphics(ResourcePath.getSprite(spriteName), 0.5f,
                0.5f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new
                Vector (0.5f, height - 1.25f)), 1, DEPTH);
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getTransform().getX().getX();
        float height = canvas.getTransform().getY().getY();

        float ratio = canvas.getWidth() / (float) canvas.getHeight();
        if (ratio > 1)
            height = width / ratio;
        else
            width = height * ratio;

        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(flipped ? (-width / 2 + 2) : width / 2, height / 2));
        ImageGraphics sprite = getCurrentItemSprite(anchor, height);
        ImageGraphics gearDisplay = new ImageGraphics(ResourcePath.getSprite("icoop/gearDisplay"), 1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0, height - 1.75f)), 1, DEPTH);
        gearDisplay.draw(canvas);
        if (sprite != null)
            sprite.draw(canvas);
    }
}
