package com.fxexperience.tools.derivationcalc;

import com.fxexperience.javafx.scene.control.colorpicker.ColorPicker;
import com.sun.javafx.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * @author Jasper Potts
 */
public class DerivationCalcContent implements Initializable {
    
    @FXML private AnchorPane anchorPane;
    @FXML private GridPane gridPane;
    @FXML private Label forwardDerivationLabel;
    @FXML private Slider derivationSlider;
    @FXML private Label derivedResultLabel;
    @FXML private Label reverseDerivationLabel;
    @FXML private Label reverseResultLabel;
    @FXML private ImageView alert;
    private ColorPicker baseColorPicker = new ColorPicker(Color.GREY);
    private ColorPicker desiredColorPicker = new ColorPicker(Color.GREY);
    private Region derivedResultColor;
    private Region reverseResultColor;
    
    @Override public void initialize(URL url, ResourceBundle rb) {
        anchorPane.getStylesheets().add(DerivationCalcContent.class.getResource("DerivationCalc.css").toString());  
        gridPane.getChildren().addAll(baseColorPicker,desiredColorPicker);
        GridPane.setConstraints(baseColorPicker, 1, 1);
        baseColorPicker.setPrefWidth(120);
        baseColorPicker.setMaxWidth(120);
        GridPane.setConstraints(desiredColorPicker, 1, 6);
        desiredColorPicker.setPrefWidth(120);
        desiredColorPicker.setMaxWidth(120);
        // FORWARD
        forwardDerivationLabel.textProperty().bind(new StringBinding() {
            { bind(derivationSlider.valueProperty()); }
            @Override protected String computeValue() {
                return String.format("%3.1f%%", derivationSlider.getValue());
            }
        });
        derivedResultColor = new Region();
        derivedResultColor.setPrefSize(50, 20);
        derivedResultLabel.setGraphic(derivedResultColor);
        derivedResultColor.styleProperty().bind(new StringBinding() {
            { bind(derivationSlider.valueProperty(),baseColorPicker.colorProperty()); }
            @Override protected String computeValue() {
                return "-fx-border-color: black; -fx-background-color: derive("+baseColorPicker.getWebColor()+", "+derivationSlider.getValue()+"%);";
            }
        });
        derivedResultLabel.textProperty().bind(new StringBinding() {
            { bind(derivationSlider.valueProperty(),baseColorPicker.colorProperty()); }
            @Override protected String computeValue() {
                Color base = baseColorPicker.getColor();
                double derivation = derivationSlider.getValue();
                Color result = Utils.deriveColor(base, derivation/100);
                return getColorString(result);
            }
        });
        // BACKWARD
        reverseResultColor = new Region();
        reverseResultColor.setPrefSize(50, 20);
        reverseResultLabel.setGraphic(reverseResultColor);
        ChangeListener<Color> updateReverse = new ChangeListener<Color>() {
            @Override public void changed(ObservableValue<? extends Color> ov, Color t, Color desiredColor) {
                updateReverse();
            }
        };
        baseColorPicker.colorProperty().addListener(updateReverse);
        desiredColorPicker.colorProperty().addListener(updateReverse);
    }    
    
    private void updateReverse() {
        Color desiredColor = desiredColorPicker.getColor();
        final Color base = baseColorPicker.getColor();
//                System.out.println("base = " + base);
        double desiredBrightness = desiredColor.getBrightness();
//                System.out.println("desiredBrightness = " + desiredBrightness);
        double desiredSaturation = desiredColor.getSaturation();
//                System.out.println("desiredSaturation = " + desiredSaturation);
        double derivation = 0, max = 1, min = -1;
        Color derivedColor = Color.WHITE;
        for(int i=0; i< 100;i++){
//                    System.out.println("---------- "+i+" ----------------");
//                    System.out.println("derivation = " + derivation);
//                    System.out.println("max = " + max);
//                    System.out.println("min = " + min);
            derivedColor = Utils.deriveColor(base, derivation);
            double derivedBrightness = derivedColor.getBrightness();
//                    System.out.println("derivedBrightness = " + derivedBrightness);
            double derivedSaturation = derivedColor.getSaturation();
//                    System.out.println("derivedSaturation = " + derivedSaturation);
            double saturationDifference = Math.abs(derivedSaturation-desiredSaturation);
//                    System.out.println("saturationDifference = " + saturationDifference);
            double difference = Math.abs(derivedBrightness-desiredBrightness);
//                    System.out.println("brightness difference = " + difference);
            if (difference < 0.0001) { // GOOD ENOUGH
                break;
            } else if(min == 1 || max == -1) { // TO DIFFERENT
                break;
            } else if(derivedBrightness > desiredBrightness) { // TO BRIGHT
//                        System.out.println("NEED DARKER");
                max = derivation;
                derivation = derivation + ((min-derivation)/2);
            } else { // TO DARK
//                        System.out.println("NEED BRIGHTER");
                min = derivation;
                derivation = derivation + ((max-derivation)/2);
            }
        }

//                System.out.println("\nFINAL \nderivation = " + derivation+"\n\n");
        reverseDerivationLabel.setText(String.format("%3.3f%%", derivation));
        reverseResultLabel.setText(getColorString(derivedColor));
        reverseResultColor.setStyle("-fx-border-color: black; "
                + "-fx-background-color: "+getWebColor(derivedColor) +";");

        alert.setVisible(!getWebColor(desiredColor).equals(getWebColor(derivedColor)));
    }
    
    
    private static String getColorString(Color color) {
        final int red = (int)(color.getRed()*255);
        final int green = (int)(color.getGreen()*255);
        final int blue = (int)(color.getBlue()*255);
        return String.format("#%02X%02X%02X R:%d G:%d B:%d", red,green,blue, red,green,blue);
    }
    
    private static String getWebColor(Color color) {
        final int red = (int)(color.getRed()*255);
        final int green = (int)(color.getGreen()*255);
        final int blue = (int)(color.getBlue()*255);
        return String.format("#%02X%02X%02X", red,green,blue);
    }
    
    
}
