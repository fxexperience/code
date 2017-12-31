package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a fade out right effect on a node
 * 
 * Port of FadeOutRight from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}keyframes fadeOutRight {
 * 	0% {
 * 		opacity: 0;
 * 		transform: translateX(0);
 * 	}
 * 	100% {
 * 		opacity: 1;
 * 		transform: translateX(20px);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class FadeOutRightTransition extends CachedTimelineTransition {
    /**
     * Create new FadeOutRightTransition
     * 
     * @param node The node to affect
     */
    public FadeOutRightTransition(final Node node) {
        super(
            node,
            TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 0, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateXProperty(), 20, WEB_EASE)
                    )
                )
                .build()
            );
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void stopping() {
        super.stopping();
        node.setTranslateX(0); // restore default
    }
}
