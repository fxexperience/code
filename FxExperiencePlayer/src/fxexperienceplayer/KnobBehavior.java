package fxexperienceplayer;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Slider;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;
import javafx.scene.input.MouseEvent;

/**
 * A Behavior for slider that makes it into a circular knob
 * 
 * @author Jasper Potts
 */
public class KnobBehavior extends BehaviorBase<Slider> {
    protected static final List<KeyBinding> SLIDER_BINDINGS = new ArrayList<KeyBinding>();
    static {
        SLIDER_BINDINGS.add(new KeyBinding(TAB, "TraverseNext"));
        SLIDER_BINDINGS.add(new KeyBinding(TAB, "TraversePrevious").shift());
        SLIDER_BINDINGS.add(new KeyBinding(UP, "IncrementValue"));
        SLIDER_BINDINGS.add(new KeyBinding(KP_UP, "IncrementValue"));
        SLIDER_BINDINGS.add(new KeyBinding(DOWN, "DecrementValue"));
        SLIDER_BINDINGS.add(new KeyBinding(KP_DOWN, "DecrementValue"));
        SLIDER_BINDINGS.add(new KeyBinding(LEFT, "TraverseLeft"));
        SLIDER_BINDINGS.add(new KeyBinding(KP_LEFT, "TraverseLeft"));
        SLIDER_BINDINGS.add(new KeyBinding(RIGHT, "TraverseRight"));
        SLIDER_BINDINGS.add(new KeyBinding(KP_RIGHT, "TraverseRight"));
        SLIDER_BINDINGS.add(new KeyBinding(HOME, KEY_RELEASED, "Home"));
        SLIDER_BINDINGS.add(new KeyBinding(END, KEY_RELEASED, "End"));
    }
    private double dragStartX,dragStartY;

    @Override protected void callAction(String name) {
        if ("Home".equals(name)) home();
        else if ("End".equals(name)) end();
        else if ("IncrementValue".equals(name)) incrementValue();
        else if ("DecrementValue".equals(name)) decrementValue();
        else super.callAction(name);
    }

    public KnobBehavior(Slider slider) {
        super(slider);
    }

    @Override protected List<KeyBinding> createKeyBindings() {
        return SLIDER_BINDINGS;
    }

    /**
     * @param position The position of mouse in 0=min to 1=max range
     */
    public void knobRelease(MouseEvent e, double position) {
        final Slider slider = getControl();
        slider.setValueChanging(false);
        // detect click rather than drag
        if(Math.abs(e.getX()-dragStartX) < 3 && Math.abs(e.getY()-dragStartY) < 3) {
            slider.adjustValue((position+slider.getMin()) * (slider.getMax()-slider.getMin()));
        }
    }

     /**
     * @param position The position of mouse in 0=min to 1=max range
     */
    public void knobPressed(MouseEvent e, double position) {
        // If not already focused, request focus
        final Slider slider = getControl();
        if (!slider.isFocused())  slider.requestFocus();
        slider.setValueChanging(true);
        dragStartX = e.getX();
        dragStartY = e.getY();
    }

    /**
     * @param position The position of mouse in 0=min to 1=max range
     */
    public void knobDragged(MouseEvent e, double position) {
        final Slider slider = getControl();
        slider.adjustValue((position+slider.getMin()) * (slider.getMax()-slider.getMin()));
    }

    void home() {
        final Slider slider = getControl();
        slider.adjustValue(slider.getMin());
    }

    void decrementValue() {
        getControl().decrement();
    }

    void end() {
        final Slider slider = getControl();
        slider.adjustValue(slider.getMax());
    }

    void incrementValue() {
        getControl().increment();
    }
}
