package com.fxexperience.javafx.scene.control.skin;

import com.fxexperience.javafx.scene.control.IntegerField;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;

/**
 */
public class IntegerFieldSkin extends InputFieldSkin {
    private InvalidationListener integerFieldValueListener;

    /**
     * Create a new IntegerFieldSkin.
     * @param control The IntegerField
     */
    public IntegerFieldSkin(final IntegerField control) {
        super(control);

        // Whenever the value changes on the control, we need to update the text
        // in the TextField. The only time this is not the case is when the update
        // to the control happened as a result of an update in the text textField.
        control.valueProperty().addListener(integerFieldValueListener = new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                updateText();
            }
        });
    }

    @Override public IntegerField getSkinnable() {
        return (IntegerField) control;
    }

    @Override public Node getNode() {
        return getTextField();
    }

    /**
     * Called by a Skinnable when the Skin is replaced on the Skinnable. This method
     * allows a Skin to implement any logic necessary to clean up itself after
     * the Skin is no longer needed. It may be used to release native resources.
     * The methods {@link #getSkinnable()} and {@link #getNode()}
     * should return null following a call to dispose. Calling dispose twice
     * has no effect.
     */
    @Override
    public void dispose() {
        ((IntegerField) control).valueProperty().removeListener(integerFieldValueListener);
        super.dispose();
    }

    protected boolean accept(String text) {
        if (text.length() == 0) return true;
        if (text.matches("[0-9]*")) {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException ex) { }
        }
        return false;
    }

    protected void updateText() {
        getTextField().setText("" + ((IntegerField) control).getValue());
    }

    protected void updateValue() {
        int value = ((IntegerField) control).getValue();
        int newValue;
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim();
        try {
            newValue = Integer.parseInt(text);
            if (newValue != value) {
                ((IntegerField) control).setValue(newValue);
            }
        } catch (NumberFormatException ex) {
            // Empty string most likely
            ((IntegerField) control).setValue(0);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    getTextField().positionCaret(1);
                }
            });
        }
    }
}
