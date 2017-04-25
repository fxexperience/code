package com.fxexperience.javafx.scene.control.colorpicker;

import com.fxexperience.javafx.scene.control.IntegerField;
import com.fxexperience.javafx.scene.control.WebColorField;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

import javafx.stage.Popup;

/**
 * @author Jasper Potts
 */
class ColorPickerPopover extends Group {

    private static final int PICKER_PADDING = 20;
    private static final int RECT_SIZE = 170;
    private static final int ARROW_SIZE = 10;
    private static final int ARROW_X = 150;
    private static final int PICKER_WIDTH = 220;
    private static final int PICKER_HEIGHT = 262;
    private static final int RADIUS = 8;

    private Popup popup = new Popup();
    private boolean changeIsLocal = false;
    private DoubleProperty hue = new SimpleDoubleProperty() {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                color.set(Color.hsb(hue.get(), clamp(sat.get() / 100), clamp(bright.get() / 100)));
                changeIsLocal = false;
            }
        }
    };
    private DoubleProperty sat = new SimpleDoubleProperty() {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                color.set(Color.hsb(hue.get(), clamp(sat.get() / 100), clamp(bright.get() / 100)));
                changeIsLocal = false;
            }
        }
    };
    private DoubleProperty bright = new SimpleDoubleProperty() {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                color.set(Color.hsb(hue.get(), clamp(sat.get() / 100), clamp(bright.get() / 100)));
                changeIsLocal = false;
            }
        }
    };
    private ObjectProperty<Color> color = new SimpleObjectProperty<Color>(Color.RED) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                final Color c = get();
                hue.set(c.getHue());
                sat.set(c.getSaturation() * 100);
                bright.set(c.getBrightness() * 100);
                changeIsLocal = false;
            }
        }
    };

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color newColor) {
        color.set(newColor);
    }

    public ColorPickerPopover() {
        getStyleClass().add("color-picker-popover");
        popup.setAutoHide(true);

        // add this to popup
        popup.getContent().add(this);

        // load stylesheet
        getStylesheets().add(ColorPicker.class.getResource("ColorPicker.css").toString());

        // create popover path for main shape
        final Path p = new Path();
        p.getElements().addAll(
                new MoveTo(PICKER_PADDING, PICKER_PADDING + ARROW_SIZE + RADIUS),
                new ArcTo(RADIUS, RADIUS, 90, PICKER_PADDING + RADIUS, PICKER_PADDING + ARROW_SIZE, false, true),
                new LineTo(PICKER_PADDING + ARROW_X - (ARROW_SIZE * 0.8), PICKER_PADDING + ARROW_SIZE),
                new LineTo(PICKER_PADDING + ARROW_X, PICKER_PADDING),
                new LineTo(PICKER_PADDING + ARROW_X + (ARROW_SIZE * 0.8), PICKER_PADDING + ARROW_SIZE),
                new LineTo(PICKER_PADDING + PICKER_WIDTH - RADIUS, PICKER_PADDING + ARROW_SIZE),
                new ArcTo(RADIUS, RADIUS, 90, PICKER_PADDING + PICKER_WIDTH, PICKER_PADDING + ARROW_SIZE + RADIUS, false, true),
                new LineTo(PICKER_PADDING + PICKER_WIDTH, PICKER_PADDING + ARROW_SIZE + PICKER_HEIGHT - RADIUS),
                new ArcTo(RADIUS, RADIUS, 90, PICKER_PADDING + PICKER_WIDTH - RADIUS, PICKER_PADDING + ARROW_SIZE + PICKER_HEIGHT, false, true),
                new LineTo(PICKER_PADDING + RADIUS, PICKER_PADDING + ARROW_SIZE + PICKER_HEIGHT),
                new ArcTo(RADIUS, RADIUS, 90, PICKER_PADDING, PICKER_PADDING + ARROW_SIZE + PICKER_HEIGHT - RADIUS, false, true),
                new ClosePath());
        p.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#313131")), new Stop(0.5, Color.web("#5f5f5f")), new Stop(1, Color.web("#313131"))));
        p.setStroke(null);
        p.setEffect(new DropShadow(15, 0, 1, Color.gray(0, 0.6)));
        p.setCache(true);

        // create rectangle to capture mouse events to hide
        Rectangle windowClickRect = new Rectangle();
        windowClickRect.setWidth(PICKER_PADDING + PICKER_WIDTH + PICKER_PADDING);
        windowClickRect.setHeight(PICKER_PADDING + PICKER_HEIGHT + PICKER_PADDING);
        windowClickRect.setFill(Color.TRANSPARENT);
        windowClickRect.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("x= " + event.getX());
            System.out.println("p.contains(event.getX(), event.getY()) = " + p.contains(event.getX(), event.getY()));
            if (!p.contains(event.getX(), event.getY())) {
                hide();
            }
        });

        final Circle colorRectIndicator = new Circle();
        colorRectIndicator.setCenterX(60);
        colorRectIndicator.setCenterY(60);
        colorRectIndicator.setRadius(5);
        colorRectIndicator.setStroke(Color.WHITE);
        colorRectIndicator.setFill(null);
        colorRectIndicator.setEffect(new DropShadow(2, 0, 1, Color.BLACK));

        colorRectIndicator.centerXProperty().bind(new DoubleBinding() {
            {
                bind(sat);
            }

            @Override
            protected double computeValue() {
                return (PICKER_PADDING + 10) + (RECT_SIZE * (sat.get() / 100));
            }
        });

        colorRectIndicator.centerYProperty().bind(new DoubleBinding() {
            {
                bind(bright);
            }

            @Override
            protected double computeValue() {
                return (PICKER_PADDING + ARROW_SIZE + 10) + (RECT_SIZE * (1 - (bright.get() / 100)));
            }
        });

        final Rectangle colorRect = new Rectangle();
        colorRect.setX(PICKER_PADDING + 10);
        colorRect.setY(PICKER_PADDING + ARROW_SIZE + 10);
        colorRect.setWidth(RECT_SIZE);
        colorRect.setHeight(RECT_SIZE);

        colorRect.fillProperty().bind(new ObjectBinding<Paint>() {
            {
                bind(color);
            }

            @Override
            protected Paint computeValue() {
                return Color.hsb(hue.getValue(), 1, 1);
            }
        });

        Rectangle colorRectOverlayOne = new Rectangle();
        colorRectOverlayOne.setX(PICKER_PADDING + 10);
        colorRectOverlayOne.setY(PICKER_PADDING + ARROW_SIZE + 10);
        colorRectOverlayOne.setWidth(RECT_SIZE);
        colorRectOverlayOne.setHeight(RECT_SIZE);
        colorRectOverlayOne.setFill(new LinearGradient(0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 1)),
                new Stop(1, Color.rgb(255, 255, 255, 0))));

        EventHandler<MouseEvent> rectMouseHandler = (MouseEvent event) -> {
            final double x = event.getX() - colorRect.getX();
            final double y = event.getY() - colorRect.getY();
            sat.set(clamp(x / RECT_SIZE) * 100);
            bright.set(100 - (clamp(y / RECT_SIZE) * 100));
        };

        Rectangle colorRectOverlayTwo = new Rectangle();
        colorRectOverlayTwo.setX(PICKER_PADDING + 10);
        colorRectOverlayTwo.setY(PICKER_PADDING + ARROW_SIZE + 10);
        colorRectOverlayTwo.setWidth(RECT_SIZE);
        colorRectOverlayTwo.setHeight(RECT_SIZE);
        colorRectOverlayTwo.setFill(new LinearGradient(0, 0, 0, 1, true,
                CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(0, 0, 0, 0)),
                new Stop(1, Color.rgb(0, 0, 0, 1))));
        colorRectOverlayTwo.setOnMouseDragged(rectMouseHandler);
        colorRectOverlayTwo.setOnMouseClicked(rectMouseHandler);

        final Rectangle colorBar = new Rectangle();
        colorBar.setX(PICKER_PADDING + PICKER_WIDTH - 30);
        colorBar.setY(PICKER_PADDING + ARROW_SIZE + 10);
        colorBar.setWidth(20);
        colorBar.setHeight(RECT_SIZE);
        colorBar.setFill(createHueGradient());

        final Rectangle colorBarIndicator = new Rectangle();
        colorBarIndicator.setX(PICKER_PADDING + PICKER_WIDTH - 32);
        colorBarIndicator.setY(PICKER_PADDING + ARROW_SIZE + 15);
        colorBarIndicator.setWidth(24);
        colorBarIndicator.setHeight(10);
        colorBarIndicator.setArcWidth(4);
        colorBarIndicator.setArcHeight(4);
        colorBarIndicator.setStroke(Color.WHITE);
        colorBarIndicator.setFill(null);
        colorBarIndicator.setEffect(new DropShadow(2, 0, 1, Color.BLACK));

        colorBarIndicator.yProperty().bind(new DoubleBinding() {
            {
                bind(hue);
            }

            @Override
            protected double computeValue() {
                return (PICKER_PADDING + ARROW_SIZE + 5) + (RECT_SIZE * (hue.get() / 360));
            }
        });
        EventHandler<MouseEvent> barMouseHandler = (MouseEvent event) -> {
            final double y = event.getY() - colorBar.getY();
            hue.set(clamp(y / RECT_SIZE) * 360);
        };
        colorBar.setOnMouseDragged(barMouseHandler);
        colorBar.setOnMouseClicked(barMouseHandler);

        Label brightnessLabel = new Label("Brightness:");
        brightnessLabel.setMinWidth(Control.USE_PREF_SIZE);
        GridPane.setConstraints(brightnessLabel, 0, 0);

        Slider brightnessSlider = new Slider();
        brightnessSlider.setMin(0);
        brightnessSlider.setMax(100);
        brightnessSlider.setId("BrightnessSlider");
        brightnessSlider.valueProperty().bindBidirectional(bright);
        GridPane.setConstraints(brightnessSlider, 1, 0);

        IntegerField brightnessField = new IntegerField();
        brightnessField.valueProperty().bindBidirectional(bright);
        brightnessField.setPrefColumnCount(7);
        GridPane.setConstraints(brightnessField, 2, 0);

        Label saturationLabel = new Label("Saturation:");
        saturationLabel.setMinWidth(Control.USE_PREF_SIZE);
        saturationLabel.setId("Saturation-label");
        GridPane.setConstraints(saturationLabel, 0, 1);

        Slider saturationSlider = new Slider();
        saturationSlider.setMin(0);
        saturationSlider.setMax(100);
        saturationSlider.setId("SaturationSlider");
        saturationSlider.valueProperty().bindBidirectional(sat);
        GridPane.setConstraints(saturationSlider, 1, 1);

        saturationSlider.styleProperty().bind(new StringBinding() {
            {
                bind(color);
            }

            @Override
            protected String computeValue() {
                return "picker-color: hsb(" + hue.get() + ",100%,100%);";
            }
        });

        IntegerField saturationField = new IntegerField();
        saturationField.valueProperty().bindBidirectional(sat);
        saturationField.setPrefColumnCount(7);
        GridPane.setConstraints(saturationField, 2, 1);

        Label webLabel = new Label("Web:");
        webLabel.setMinWidth(Control.USE_PREF_SIZE);
        GridPane.setConstraints(webLabel, 0, 2);

        WebColorField webField = new WebColorField();
        webField.valueProperty().bindBidirectional(color);
        GridPane.setConstraints(webField, 1, 2, 2, 1);

        GridPane controls = new GridPane();
        controls.setVgap(5);
        controls.setHgap(5);
        controls.getChildren().addAll(brightnessLabel, brightnessSlider, brightnessField, saturationLabel, saturationSlider, saturationField, webLabel, webField);
        controls.setManaged(false);
        controls.resizeRelocate(
                PICKER_PADDING + 10,
                PICKER_PADDING + ARROW_SIZE + 10 + 170 + 10,
                PICKER_WIDTH - 20,
                controls.getPrefHeight());

        getChildren().addAll(windowClickRect, p, colorRect, colorRectOverlayOne, colorRectOverlayTwo, colorBar, colorRectIndicator, colorBarIndicator, controls);
    }

    public void show(Control ownerControl) {
        Point2D point = ownerControl.localToScene(ownerControl.getWidth() / 2, ownerControl.getHeight());
        double x = point.getX() + ownerControl.getScene().getX() + ownerControl.getScene().getWindow().getX();
        double y = point.getY() + ownerControl.getScene().getY() + ownerControl.getScene().getWindow().getY();
        ColorPickerPopover colorPickerPopover = new ColorPickerPopover();
        popup.show(ownerControl, x - getPopoverPointX(), y - getPopoverPointY());
    }

    public void hide() {
        popup.hide();
    }

    public boolean isShowing() {
        return popup.isShowing();
    }

    private static double getPopoverPointX() {
        return PICKER_PADDING + ARROW_X;
    }

    private static double getPopoverPointY() {
        return PICKER_PADDING;
    }

    private static double clamp(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value;
    }

    private static LinearGradient createHueGradient() {
        double offset;
        Stop[] stops = new Stop[255];
        for (int y = 0; y < 255; y++) {
            offset = (double) (1 - (1.0 / 255) * y);
            int h = (int) ((y / 255.0) * 360);
            stops[y] = new Stop(offset, Color.hsb(h, 1.0, 1.0));
        }
        return new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, stops);
    }
}
