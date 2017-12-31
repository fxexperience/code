package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a roll in left effect on a node
 * 
 * Port of RollIn from Animate.css http://daneden.me/animate by Dan Eden
 * Which was originally authored by Nick Pettit - https://github.com/nickpettit/glide 
 * 
 * {@literal @}keyframes rollIn {
 * 	0% { opacity: 0; transform: translateX(-100%) rotate(-120deg); }
 * 	100% { opacity: 1; transform: translateX(0px) rotate(0deg); }
 * }
 * 
 * @author Jasper Potts
 */
public class RollInTransition extends CachedTimelineTransition {
    /**
     * Create new RollInTransition
     * 
     * @param node The node to affect
     */
    public RollInTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        super.starting();
        timeline = TimelineBuilder.create()
            .keyFrames(
                new KeyFrame(Duration.millis(0),    
                    new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                    new KeyValue(node.translateXProperty(), -node.getBoundsInLocal().getWidth(), WEB_EASE),
                    new KeyValue(node.rotateProperty(), -120, WEB_EASE)
                ),
                new KeyFrame(Duration.millis(1000),    
                    new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                    new KeyValue(node.translateXProperty(), 0, WEB_EASE),
                    new KeyValue(node.rotateProperty(), 0, WEB_EASE)
                )
            )
            .build();
    }
}
