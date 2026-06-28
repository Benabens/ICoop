package ch.epfl.cs107.icoop;

import ch.epfl.cs107.play.engine.actor.Dialog;

public interface DialogHandler {

    /**
     * Publish a dialog that will be displayed on the window.
     * @param dialog (Dialog): the dialog to publish
     */
    public void publish(Dialog dialog);
}
