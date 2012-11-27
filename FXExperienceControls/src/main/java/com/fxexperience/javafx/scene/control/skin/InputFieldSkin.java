package com.fxexperience.javafx.scene.control.skin;

import com.sun.javafx.event.EventDispatchChainImpl;
import com.fxexperience.javafx.scene.control.InputField;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;

/**
 */
public abstract class InputFieldSkin implements Skin<InputField> {
    /**
     * The {@code Control} that is referencing this Skin. There is a
     * one-to-one relationship between a {@code Skin} and a {@code Control}.
     * When a {@code Skin} is set on a {@code Control}, this variable is
     * automatically updated.
     */
    protected InputField control;

    /**
     * This textField is used to represent the InputField.
     */
    private InnerTextField textField;

    private InvalidationListener InputFieldFocusListener;
    private InvalidationListener InputFieldStyleClassListener;

    /**
     * Create a new InputFieldSkin.
     * @param control The InputField
     */
    public InputFieldSkin(final InputField control) {
        this.control = control;

        // Create the TextField that we are going to use to represent this InputFieldSkin.
        // The textField restricts input so that only valid digits that contribute to the
        // Money can be input.
        textField = new InnerTextField() {
            @Override public void replaceText(int start, int end, String text) {
                String t = textField.getText() == null ? "" : textField.getText();
                t = t.substring(0, start) + text + t.substring(end);
                if (accept(t)) {
                    super.replaceText(start, end, text);
                }
            }

            @Override public void replaceSelection(String text) {
                String t = textField.getText() == null ? "" : textField.getText();
                int start = Math.min(textField.getAnchor(), textField.getCaretPosition());
                int end = Math.max(textField.getAnchor(), textField.getCaretPosition());
                t = t.substring(0, start) + text + t.substring(end);
                if (accept(t)) {
                    super.replaceSelection(text);
                }
            }
        };

        textField.setId("input-text-field");
        textField.getStyleClass().setAll(control.getStyleClass());
        control.getStyleClass().addListener(InputFieldStyleClassListener = new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                textField.getStyleClass().setAll(control.getStyleClass());
            }
        });

//        // Align the text to the right
//        textField.setAlignment(Pos.BASELINE_RIGHT);
        textField.promptTextProperty().bind(control.promptTextProperty());
        textField.editableProperty().bind(control.editableProperty());
        textField.prefColumnCountProperty().bind(control.prefColumnCountProperty());

        // Whenever the text of the textField changes, we may need to
        // update the value.
        textField.textProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                updateValue();
            }
        });

        // Right now there is some funny business regarding focus in JavaFX. So
        // we will just make sure the TextField gets focus whenever somebody tries
        // to give it to the InputField. This isn't right, but we need to fix
        // this in JavaFX, I don't think I can hack around it
        textField.setFocusTraversable(false);
        control.focusedProperty().addListener(InputFieldFocusListener = new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                textField.handleFocus(control.isFocused());
            }
        });

        control.addEventFilter(InputEvent.ANY, new EventHandler<InputEvent>() {
            @Override public void handle(InputEvent t) {
                if (textField == null) return;
                textField.fireEvent(t);
            }
        });

        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                // Because TextFieldBehavior fires an action event on the parent of the TextField
                // (maybe a misfeature?) I don't need to do this. But I think this is
                // a bug, because having to add an empty event handler to get an
                // event on the control is odd to say the least!
//                control.fireEvent(new ActionEvent(textField, textField));
            }
        });

        updateText();
    }

    @Override public InputField getSkinnable() {
        return control;
    }

    @Override public Node getNode() {
        return textField;
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
        control.getStyleClass().removeListener(InputFieldStyleClassListener);
        control.focusedProperty().removeListener(InputFieldFocusListener);
        textField = null;
    }

    protected abstract boolean accept(String text);
    protected abstract void updateText();
    protected abstract void updateValue();

    protected TextField getTextField() {
        return textField;
    }

    private class InnerTextField extends TextField {
        public void handleFocus(boolean b) {
            setFocused(b);
        }

        @Override public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
            EventDispatchChain chain = new EventDispatchChainImpl();
            chain.append(textField.getEventDispatcher());
            return chain;
        }
   }
}
