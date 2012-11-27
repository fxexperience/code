package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade out up big effect on a node
 * 
 * Port of FadeOutUpBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeOutUpBig {
 * 	0% {
 * 		opacity: 1;
 * 		transform: translateY(0);
 * 	}
 * 	100% {
 * 		opacity: 0;
 * 		transform: translateY(-2000px);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeOutUpBigTransition extends CachedTimelineTransition {
    /**
     * Create new FadeOutUpBigTransition
     * 
     * @param node The node to affect
     */
    public FadeOutUpBigTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double endY = -node.localToScene(0, 0).getY() -node.getBoundsInParent().getHeight();
        timeline = TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateYProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateYProperty(), endY, WEB_EASE)
                    )
                )
                .build();
        super.starting();
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateY(0); // restore default
    }
    
    
}
