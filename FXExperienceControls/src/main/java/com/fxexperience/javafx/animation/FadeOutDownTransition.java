package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade out Down effect on a node
 * 
 * Port of FadeInDown from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeOutDown {
 * 	0% {
 * 		opacity: 1;
 * 		transform: translateY(0);
 * 	}
 * 	100% {
 * 		opacity: 0;
 * 		transform: translateY(20px);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeOutDownTransition extends CachedTimelineTransition {
    /**
     * Create new FadeOutDownTransition
     * 
     * @param node The node to affect
     */
    public FadeOutDownTransition(final Node node) {
        super(
            node,
            TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateYProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateYProperty(), 20, WEB_EASE)
                    )
                )
                .build()
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateY(0); // restore default
    }
}
