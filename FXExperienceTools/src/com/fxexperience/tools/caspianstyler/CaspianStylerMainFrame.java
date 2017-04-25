package com.fxexperience.tools.caspianstyler;

import com.fxexperience.javafx.scene.control.IntegerField;
import com.fxexperience.javafx.scene.control.colorpicker.ColorPicker;
import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 * @author Jasper Potts
 */
public class CaspianStylerMainFrame implements Initializable {
    private String css;
   
    // Content Panel
    @FXML private GridPane contentPanel;
    @FXML private ChoiceBox choiceBox;
    @FXML private ComboBox comboBox;
  
    // Common Properties
    @FXML private GridPane propertiesPanel;
    @FXML private ChoiceBox<String> fontChoiceBox;
    @FXML private CheckBox fontDefaultCheckBox;
    @FXML private Slider fontSizeSlider;
    @FXML private Slider paddingSlider;
    @FXML private Slider borderWidthSlider;
    @FXML private Slider borderRadiusSlider;
   
    // Tabs
    @FXML private GridPane simpleGridPane;
    @FXML private CheckBox textColorAutoComboBox;
    @FXML private CheckBox fieldTextAutoCheckBox;
    private final ColorPicker baseColorPicker = new ColorPicker(Color.web("#d0d0d0"));
    private final ColorPicker backgroundColorPicker = new ColorPicker(Color.web("#f4f4f4"));
    private final ColorPicker focusColorPicker = new ColorPicker(Color.web("#0093ff"));
    private final ColorPicker textColorPicker = new ColorPicker(Color.web("#000000"));
    private final ColorPicker bkgdTextColorPicker = new ColorPicker(Color.web("#000000"));
    private final ColorPicker fieldBackgroundPicker = new ColorPicker(Color.web("#ffffff"));
    private final ColorPicker fieldTextColorPicker = new ColorPicker(Color.web("#000000"));
    @FXML private Slider topHighlightSlider;
    @FXML private Slider bottomHighlightSlider;
    @FXML private ComboBox<Gradient> gradientComboBox;
   
    // Advanced Tab
    @FXML private Slider bodyTopSlider;
    @FXML private Slider bodyTopMiddleSlider;
    @FXML private Slider bodyBottomMiddleSlider;
    @FXML private Slider bodyBottomSlider;
    @FXML private Slider borderSlider;
    @FXML private Slider shadowSlider;
    @FXML private Slider inputBorderSlider;
    @FXML private Slider inputOuterBorderSlider;
    @FXML private CheckBox bodyTopMiddleComboBox;
    @FXML private CheckBox bodyBottomMiddleComboBox;
    @FXML private CheckBox borderComboBox;
    @FXML private CheckBox shadowComboBox;
    @FXML private CheckBox inputBorderComboBox;
    @FXML private CheckBox inputOuterBorderComboBox;
    @FXML private CheckBox bkgdTextColorAutoComboBox;
    
    // Bottom
    @FXML private Button copyCSS;
    @FXML private Button saveCSS;
    
    @Override public void initialize(URL url, ResourceBundle rb) {
       
        // load stylesheet
        propertiesPanel.getStylesheets().add(CaspianStylerMainFrame.class.getResource("CaspianStyler.css").toExternalForm());
       
        // tweek preview content panel
        choiceBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().select(0);
      
        // populate fonts choicebox
        fontChoiceBox.getItems().setAll(Font.getFamilies());
        fontChoiceBox.getSelectionModel().select("System");
        
        fontDefaultCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean newValue) {
                if (newValue) fontChoiceBox.getSelectionModel().select("System");
            }
        });
        
        fontChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> arg0, String arg1, String newValue) {
                if (!"System".equalsIgnoreCase(newValue)) {
                    fontDefaultCheckBox.setSelected(false);
                }
            }
        });
        
        // create listener to call update css
        ChangeListener updateCssListener = new ChangeListener() {
            @Override public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                updateCss();
            }
        };
        
        // add listeners to call update css
        fontChoiceBox.valueProperty().addListener(updateCssListener);
        fontSizeSlider.valueProperty().addListener(updateCssListener);
        paddingSlider.valueProperty().addListener(updateCssListener);
        borderRadiusSlider.valueProperty().addListener(updateCssListener);
        borderWidthSlider.valueProperty().addListener(updateCssListener);
       
        // create Integer Fields
        createNumberFieldForSlider(fontSizeSlider, propertiesPanel);
        createNumberFieldForSlider(paddingSlider, propertiesPanel);
        createNumberFieldForSlider(borderWidthSlider, propertiesPanel);
        createNumberFieldForSlider(borderRadiusSlider, propertiesPanel);
        
        // ------------- SIMPLE TAB --------------------------------------------
        simpleGridPane.getChildren().addAll(
                baseColorPicker, backgroundColorPicker, focusColorPicker, 
                textColorPicker, fieldBackgroundPicker, fieldTextColorPicker,
                bkgdTextColorPicker);
       
        // create color pickers
        GridPane.setConstraints(baseColorPicker, 1, 1);
        GridPane.setConstraints(textColorPicker, 1, 2);
        GridPane.setConstraints(backgroundColorPicker, 1, 3);
        GridPane.setConstraints(bkgdTextColorPicker, 1, 4);
        GridPane.setConstraints(fieldBackgroundPicker, 1, 5);
        GridPane.setConstraints(fieldTextColorPicker, 1, 6);
        GridPane.setConstraints(focusColorPicker, 1, 7);
        baseColorPicker.colorProperty().addListener(updateCssListener);
        backgroundColorPicker.colorProperty().addListener(updateCssListener);
        focusColorPicker.colorProperty().addListener(updateCssListener);
        textColorPicker.colorProperty().addListener(updateCssListener);
        textColorAutoComboBox.selectedProperty().addListener(updateCssListener);
        textColorPicker.disableProperty().bind(textColorAutoComboBox.selectedProperty());
        fieldBackgroundPicker.colorProperty().addListener(updateCssListener);
        fieldTextColorPicker.colorProperty().addListener(updateCssListener);
        fieldTextAutoCheckBox.selectedProperty().addListener(updateCssListener);
        fieldTextColorPicker.disableProperty().bind(fieldTextAutoCheckBox.selectedProperty());
        bkgdTextColorPicker.colorProperty().addListener(updateCssListener);
        bkgdTextColorAutoComboBox.selectedProperty().addListener(updateCssListener);
        bkgdTextColorPicker.disableProperty().bind(bkgdTextColorAutoComboBox.selectedProperty());
      
        // add listeners to sliders
        topHighlightSlider.valueProperty().addListener(updateCssListener);
        bottomHighlightSlider.valueProperty().addListener(updateCssListener);
       
        // Populate gradient combo
        gradientComboBox.getItems().addAll(Gradient.GRADIENTS);
        gradientComboBox.setCellFactory(new Callback<ListView<Gradient>, ListCell<Gradient>>() {
            @Override public ListCell<Gradient> call(ListView<Gradient> gradientList) {
                ListCell<Gradient> cell = new ListCell<Gradient>() {
                    @Override protected void updateItem(Gradient gradient, boolean empty) {
                        super.updateItem(gradient, empty);
                        if (empty || gradient == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(gradient.getName());
                            Region preview = new Region();
                            preview.setPrefSize(30, 30);
                            preview.setStyle("-fx-border-color: #111; -fx-background-color: "+ gradient.getCss());
                            setGraphic(preview);
                        }
                    }
                };
                cell.setStyle("-fx-cell-size: 40;");
                return cell;
            }
        });
        
        gradientComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Gradient>() {
            @Override public void changed(ObservableValue<? extends Gradient> arg0, Gradient arg1, Gradient newGradient) {
                bodyTopSlider.setValue(newGradient.getTopDerivation());
                bodyBottomSlider.setValue(newGradient.getBottomDerivation());
                if (newGradient.isShinny()) {
                    bodyTopMiddleComboBox.setSelected(true);
                    bodyBottomMiddleComboBox.setSelected(true);
                    bodyTopMiddleSlider.setValue(newGradient.getTopMidDerivation());
                    bodyBottomMiddleSlider.setValue(newGradient.getBottomMidDerivation());
                } else {
                    bodyTopMiddleComboBox.setSelected(false);
                    bodyBottomMiddleComboBox.setSelected(false);
                }
                updateCss();
            }
        });
        
        gradientComboBox.getSelectionModel().select(0);
        
        // ------------- ADVANCED TAB --------------------------------------------
        bodyTopSlider.valueProperty().addListener(updateCssListener);
        bodyTopMiddleSlider.valueProperty().addListener(updateCssListener);
        bodyBottomMiddleSlider.valueProperty().addListener(updateCssListener);
        bodyBottomSlider.valueProperty().addListener(updateCssListener);
        borderSlider.valueProperty().addListener(updateCssListener);
        shadowSlider.valueProperty().addListener(updateCssListener);
        inputBorderSlider.valueProperty().addListener(updateCssListener);
        inputOuterBorderSlider.valueProperty().addListener(updateCssListener);
        bodyTopMiddleSlider.disableProperty().bind(bodyTopMiddleComboBox.selectedProperty().not());
        bodyBottomMiddleSlider.disableProperty().bind(bodyBottomMiddleComboBox.selectedProperty().not());
        borderSlider.disableProperty().bind(borderComboBox.selectedProperty().not());
        shadowSlider.disableProperty().bind(shadowComboBox.selectedProperty().not());
        inputBorderSlider.disableProperty().bind(inputBorderComboBox.selectedProperty().not());
        inputOuterBorderSlider.disableProperty().bind(inputOuterBorderComboBox.selectedProperty().not());
        
        // ------------- SAVE AND COPY --------------------------------------------
        saveCSS.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(propertiesPanel.getScene().getWindow());
                if (file != null && !file.exists() && file.getParentFile().isDirectory()) {
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(file);
                        writer.write(createCSS(true));
                        writer.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(CaspianStylerMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                            Logger.getLogger(CaspianStylerMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
        copyCSS.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                Clipboard.getSystemClipboard().setContent(
                        Collections.singletonMap(DataFormat.PLAIN_TEXT,(Object)createCSS(true)));
            }
        });
    }    
    
    /**
     * The SceneBuilder does not work with custom controls yet so create a 
     * IntegerField and add to GridPane
     * 
     * @param slider The slider to bind to and sit next to
     * @param parent The GridPane to add to
     */
    private void createNumberFieldForSlider(Slider slider, GridPane parent){
        IntegerField field = new IntegerField();
        field.setMaxHeight(IntegerField.USE_PREF_SIZE);
        field.setMaxWidth(IntegerField.USE_PREF_SIZE);
        field.setPrefColumnCount(3);
        parent.getChildren().add(field);
        GridPane.setColumnIndex(field, GridPane.getColumnIndex(slider)+1);
        GridPane.setRowIndex(field, GridPane.getRowIndex(slider));
        field.valueProperty().bindBidirectional(slider.valueProperty());
    }
    
    private void updateCss() {
        css = createCSS(true);
        contentPanel.setStyle(css);
    }
    
    private String createCSS(boolean isRoot) {
        int fontSize = (int) fontSizeSlider.getValue();
        int borderWidth = (int) borderWidthSlider.getValue();
        int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
        int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
        int borderRadius = (int) borderRadiusSlider.getValue();
        double checkPadding = (((0.25 * fontSize) + borderWidthForPadding) / fontSize);
        double radioPadding = (((0.333333 * fontSize) + borderWidthForPadding) / fontSize);
       
        StringBuilder cssBuffer = new StringBuilder();
        if (isRoot) {
            cssBuffer.append(".root {\n");
        } else {
            cssBuffer.append("#ContentPanel {\n");
        }
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-font-family: \"" + fontChoiceBox.getValue() + "\";\n", 4));
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-font-size: " + fontSizeSlider.getValue() + "px;\n", 4));
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-base: " + baseColorPicker.getWebColor() + ";\n", 4));
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-background: " + backgroundColorPicker.getWebColor() + ";\n", 4));
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-focus-color: " + focusColorPicker.getWebColor() + ";\n", 4));
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-control-inner-background: " + fieldBackgroundPicker.getWebColor() + ";\n", 4));
        if (!textColorAutoComboBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-base-color: "
                    + textColorPicker.getWebColor() + ";\n", 4));
        }
        if (!bkgdTextColorAutoComboBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-background-color: "
                    + bkgdTextColorPicker.getWebColor() + ";\n", 4));
        }
        if (!fieldTextAutoCheckBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-inner-color: "
                    + fieldTextColorPicker.getWebColor() + ";\n", 4));
        }

        double innerTopDerivation = bodyTopSlider.getValue() 
                + ((100 - bodyTopSlider.getValue()) * (topHighlightSlider.getValue() / 100));
        double innerBottomDerivation = bodyBottomSlider.getValue()
                + ((100 - bodyBottomSlider.getValue()) * (bottomHighlightSlider.getValue() / 100));
        
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-inner-border: linear-gradient(to bottom, "
                + "derive(-fx-color," + innerTopDerivation + "%) 0%, "
                + "derive(-fx-color," + innerBottomDerivation + "%) 100%);\n", 4));

        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-body-color: linear-gradient( to bottom, ", 4));
        cssBuffer.append(CaspianStringUtil.padWithSpaces("derive(-fx-color, "
                + bodyTopSlider.getValue() + "%) 0%, ", 0));
        
        if (bodyTopMiddleComboBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("derive(-fx-color, "
                    + bodyTopMiddleSlider.getValue() + "%) 50%, ", 0));
        }
        if (bodyBottomMiddleComboBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("derive(-fx-color, "
                    + bodyBottomMiddleSlider.getValue() + "%) 50.5%, ", 0));
        }
        cssBuffer.append(CaspianStringUtil.padWithSpaces("derive(-fx-color, "
                + bodyBottomSlider.getValue() + "%) 100%);\n", 0));
        
        if (borderComboBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-outer-border: derive(-fx-color,"
                    + borderSlider.getValue() + "%);\n", 4));
        }

        if (shadowComboBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-shadow-highlight-color: derive(-fx-background,"
                    + shadowSlider.getValue() + "%);\n", 4));
        }

        if (inputBorderComboBox.isSelected()) {
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-box-border: derive(-fx-background,"
                    + inputBorderSlider.getValue() + "%);\n", 4));
        }

        cssBuffer.append("}\n");
        cssBuffer.append(".button, .toggle-button, .choice-box {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-background-radius: "
                + borderRadius + ", " + borderRadius + ", " + (borderRadius - 1) + ", " 
                + (borderRadius - 2) + ";\n", 4));
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: " + padding + "px " 
                + (padding + 7) + "px " + padding + "px " + (padding + 7) + "px;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-background-radius: " + borderRadius
                + ", " + borderRadius + ", " + (borderRadius - 1) + ", " + (borderRadius - 2) 
                + ";\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .label {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: " + padding + "px " 
                + (padding + 15) + "px " + padding + "px " + (padding + 7) + "px;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .arrow-button {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: " + padding + "px " 
                + (padding + 3) + "px " + padding + "px 0px;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: 0 " + (padding + 3) 
                + "px 0 0;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box .label {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: " + padding + "px " 
                + (padding + 1) + "px " + padding + "px " + (padding + 3) + "px;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box .open-button {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: 1 0 0 " + (padding + 5) 
                + "px;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".combo-box-base:editable .text-field, .combo-box-base .arrow-button, "
                + ".combo-box .list-cell {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: " + padding + "px " 
                + (padding + 3) + "px " + padding + "px " + (padding + 3) + "px;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".check-box .box {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: " + checkPadding 
                + "em;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".radio-button .radio {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-padding: " + radioPadding 
                + "em;\n", 4));
        cssBuffer.append("}\n");
        if (!textColorAutoComboBox.isSelected()) {
            cssBuffer.append(".hyperlink, {\n");
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-fill: -fx-text-background-color;\n", 4));
            cssBuffer.append("}\n");
            cssBuffer.append(".toggle-button:selected {\n");
            cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;\n", 4));
            cssBuffer.append("}\n");
        }
        cssBuffer.append(".label, .check-box, .radio-button {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-fill: -fx-text-background-color;\n", 4));
        cssBuffer.append("}\n");

        cssBuffer.append(".button, .toggle-button, .check-box .box, .radio-button .radio, "
                + ".choice-box, .menu-button, .tab, .combo-box-base {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-background-insets: 0 0 -1 0, 0, "
                + borderWidth + ", " + (borderWidth + 1) + ";\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".button:focused, .toggle-button:focused, .check-box:focused .box, "
                + ".radio-button:focused .radio, .choice-box:focused, .menu-button:focused, "
                + ".combo-box-base:focused {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-background-insets: -1.4, 0, " 
                + borderWidth + ", " + (borderWidth + 1) + ";\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".combo-box-base .arrow-button {\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-background-insets: 0, " 
                + borderWidth + ", " + (borderWidth + 1) + ";\n", 4));
        cssBuffer.append("}\n");

        cssBuffer.append(".choice-box .label { /* Workaround for RT-20015 */\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;\n", 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .label { /* Workaround for RT-20015 */\n");
        cssBuffer.append(CaspianStringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;\n", 4));
        cssBuffer.append("}\n");

        return cssBuffer.toString();
    } 
}
