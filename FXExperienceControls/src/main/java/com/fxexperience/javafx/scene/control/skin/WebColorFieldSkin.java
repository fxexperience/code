package com.fxexperience.javafx.scene.control.skin;

import com.fxexperience.javafx.scene.control.WebColorField;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 */
public class WebColorFieldSkin extends InputFieldSkin {
    private InvalidationListener integerFieldValueListener;

    /**
     * Create a new IntegerFieldSkin.
     * @param control The IntegerField
     */
    public WebColorFieldSkin(final WebColorField control) {
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

    @Override public WebColorField getSkinnable() {
        return (WebColorField) control;
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
    @Override public void dispose() {
        ((WebColorField) control).valueProperty().removeListener(integerFieldValueListener);
        super.dispose();
    }

    protected boolean accept(String text) {
        if (text.length() == 0) return true;
        if (text.matches("#[a-fA-F0-9]{0,6}")) {
            return true;
        }
        return false;
    }

    protected void updateText() {
        Color color = ((WebColorField) control).getValue();
        if (color == null) color = Color.BLACK;
        getTextField().setText(getWebColor(color));
    }

    protected void updateValue() {
        Color value = ((WebColorField) control).getValue();
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim().toUpperCase();
        if (text.matches("#[A-F0-9]{6}")) {
            try {
                Color newValue = Color.web(text);
                if (!newValue.equals(value)) {
                    ((WebColorField) control).setValue(newValue);
                }
            } catch (java.lang.IllegalArgumentException ex) {
                System.out.println("Failed to parse ["+text+"]");
            }
        }
    }
    
    
    private static String getWebColor(Color color) {
        final int red = (int)(color.getRed()*255);
        final int green = (int)(color.getGreen()*255);
        final int blue = (int)(color.getBlue()*255);
        return "#" + String.format("%02X", red) +
                          String.format("%02X", green) +
                          String.format("%02X", blue);
    }
}
