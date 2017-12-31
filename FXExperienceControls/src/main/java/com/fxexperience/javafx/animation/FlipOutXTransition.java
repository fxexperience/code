package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Animate a flip out x effect on the given node
 * 
 * Port of FlipOutX from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes flipOutX {
 *     0% {
 *         transform: perspective(400px) rotateX(0deg);
 *         opacity: 1;
 *     }
 * 	100% {
 *         transform: perspective(400px) rotateX(90deg);
 *         opacity: 0;
 *     }
 * }
 *
 * @author Jasper Potts
 */
public class FlipOutXTransition extends CachedTimelineTransition {
    private Camera oldCamera;
    
    /**
     * Create new FlipOutXTransition
     * 
     * @param node The node to affect
     */
    public FlipOutXTransition(final Node node) {
        super(
            node,
            TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0), 
                        new KeyValue(node.rotateProperty(), 0, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000), 
                        new KeyValue(node.rotateProperty(), -90, WEB_EASE),
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE)
                    )
                )
                .build()
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        node.setRotationAxis(Rotate.X_AXIS);
        oldCamera = node.getScene().getCamera();
        node.getScene().setCamera(new PerspectiveCamera());
    }

    @Override protected void stopping() {
        super.stopping();
        node.setRotate(0);
        node.setRotationAxis(Rotate.Z_AXIS);
        node.getScene().setCamera(oldCamera);
    }
}
