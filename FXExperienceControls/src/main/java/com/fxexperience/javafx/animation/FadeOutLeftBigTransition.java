package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade out left big effect on a node
 * 
 * Port of FadeOutLeftBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeOutLeftBig {
 * 	0% {
 * 		opacity: 1;
 * 		transform: translateX(0);
 * 	}
 * 	100% {
 * 		opacity: 0;
 * 		transform: translateX(-2000px);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeOutLeftBigTransition extends CachedTimelineTransition {
    /**
     * Create new FadeOutLeftBigTransition
     * 
     * @param node The node to affect
     */
    public FadeOutLeftBigTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double endX = -node.localToScene(0, 0).getX() -node.getBoundsInParent().getWidth();
        timeline = TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), endX, WEB_EASE)
                    )
                )
                .build();
        super.starting();
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateX(0); // restore default
    }
    
    
}
