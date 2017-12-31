package com.fxexperience.javafx.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Animate a bounce in up big effect on a node
 * 
 * Port of BounceInUpBig from Animate.css http://daneden.me/animate by Dan Eden
 * 
 * {@literal @}bounceInUp {
 * 	0% {
 * 		opacity: 0;
 * 		transform: translateY(2000px);
 * 	}
 * 	60% {
 * 		opacity: 1;
 * 		transform: translateY(-30px);
 * 	}
 * 	80% {
 * 		transform: translateY(10px);
 * 	}
 * 	100% {
 * 		transform: translateY(0);
 * 	}
 * }
 * 
 * @author Jasper Potts
 */
public class BounceInUpTransition extends CachedTimelineTransition {
    /**
     * Create new BounceInUpBigTransition
     * 
     * @param node The node to affect
     */
    public BounceInUpTransition(final Node node) {
        super(node, null);
        setCycleDuration(Duration.seconds(1));
        setDelay(Duration.seconds(0.2));
    }

    @Override protected void starting() {
        double startY = node.getScene().getHeight() - node.localToScene(0, 0).getY();
        timeline = TimelineBuilder.create()
                .keyFrames(
                    new KeyFrame(Duration.millis(0),    
                        new KeyValue(node.opacityProperty(), 0, WEB_EASE),
                        new KeyValue(node.translateYProperty(), startY, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(600),    
                        new KeyValue(node.opacityProperty(), 1, WEB_EASE),
                        new KeyValue(node.translateYProperty(), -30, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(800),    
                        new KeyValue(node.translateYProperty(), 10, WEB_EASE)
                    ),
                    new KeyFrame(Duration.millis(1000),    
                        new KeyValue(node.translateYProperty(), 0, WEB_EASE)
                    )
                )
                .build();
        super.starting();
    }
}
