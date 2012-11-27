package com.fxexperience.javafx.scene.control.colorpicker;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

/**
 * @author Jasper Potts
 */
public class ColorPicker extends Button {
    
    private ObjectProperty<Color> color = new SimpleObjectProperty<Color>(Color.RED);
    public ObjectProperty<Color> colorProperty() { return color; }
    public Color getColor() { return color.get(); }
    public void setColor(Color newColor) { color.set(newColor); }
    
    private ColorPickerPopover popover;
    
    public ColorPicker() {
        this(Color.BLACK);
    }
    
    public ColorPicker(Color startColor) {
        setMinWidth(USE_PREF_SIZE);
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(Pos.BASELINE_LEFT);
        getStyleClass().add("color-picker");
        color.set(startColor);
        textProperty().bind(new StringBinding() {
            { bind(color); }
            @Override protected String computeValue() {
                return getWebColor(getColor());
            }
        });
        setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                if (popover == null) {
                    popover = new ColorPickerPopover();
                    popover.colorProperty().addListener(new ChangeListener<Color>() {
                        @Override public void changed(ObservableValue<? extends Color> arg0, Color arg1, Color newValue) {
                            setColor(newValue);
                        }
                    });
                }
                if (popover.isShowing()) {
                    popover.hide();
                } else {
                    popover.setColor(getColor());
                    popover.show(ColorPicker.this);
                }
            }
        });
        Rectangle colorRect = RectangleBuilder.create()
                .width(16).height(16)
                .build();
        colorRect.fillProperty().bind(new ObjectBinding<Paint>() {
            { bind(color); }
            @Override protected Paint computeValue() {
                return getColor();
            }
        });
        colorRect.setEffect(new DropShadow(3, 0, 1, Color.rgb(0, 0, 0, 0.8)));
        setGraphic(colorRect);
    }
    
    private static String getWebColor(Color color) {
        final int red = (int)(color.getRed()*255);
        final int green = (int)(color.getGreen()*255);
        final int blue = (int)(color.getBlue()*255);
        return "#" + String.format("%02X", red) +
                          String.format("%02X", green) +
                          String.format("%02X", blue);
    }
    
    public String getWebColor() {
        return getWebColor(getColor());
    }
}