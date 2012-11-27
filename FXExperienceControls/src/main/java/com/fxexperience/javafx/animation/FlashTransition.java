package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a flash effect on a node
 * 
 * Port of Flash from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes flash {
 * 	0%, 50%, 100% {opacity: 1;}	
 * 	25%, 75% {opacity: 0;}
 * }
 * 
 * @author Jasper Potts
 */
public class FlashTransition extends CachedTimelineTransition {
    /**
     * Create new FlashTransition
     * 
     * @param node The node to affect
     */
    public FlashTransition(final Node node) {
        super(
            node,
            TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    new KeyValue(node.opacityProperty(), 1, WEB_EASE)),
                    new KeyFrame(Duration.millis(250),  new KeyValue(node.opacityProperty(), 0, WEB_EASE)),
                    new KeyFrame(Duration.millis(500),  new KeyValue(node.opacityProperty(), 1, WEB_EASE)),
                    new KeyFrame(Duration.millis(750),  new KeyValue(node.opacityProperty(), 0, WEB_EASE)),
                    new KeyFrame(Duration.millis(1000),  new KeyValue(node.opacityProperty(), 1, WEB_EASE))
                )
                .build()
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }
}
