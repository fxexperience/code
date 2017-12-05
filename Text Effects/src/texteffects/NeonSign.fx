package texteffects;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;
import javafx.scene.control.*;

var width = 600;
var height = 300;
var text:Text;
var textBox:TextBox;
Stage {
    title: "Text Effects: Neon Sign"
    resizable: false
    scene: Scene {
        width: width, height: height
        content: [
            ImageView { image: Image{ url: "{__DIR__}bricks.jpg"} }
            Rectangle {
                width: width, height: height
                fill: RadialGradient {
                    centerX: 0.5, centerY: 0.5
                    radius: 0.7, proportional: true
                    stops: [
                        Stop{offset: 0.4, color: Color.rgb(0,0,0,0.1)}
                        Stop{offset: 1, color: Color.rgb(0,0,0,0.8)}
                    ]
                }
            }
            text = Text {
                effect: Blend{
                    mode: BlendMode.MULTIPLY
                    bottomInput: DropShadow {
                        color: Color.rgb(254,235,66,0.3)
                        offsetX: 5, offsetY: 5
                        radius: 5
                        spread: 0.2
                    }
                    topInput: Blend{
                        mode: BlendMode.MULTIPLY
                        bottomInput: DropShadow {
                            color: Color.web("#f13a00")
                            radius: 20
                            spread: 0.2
                        }
                        topInput: Blend{
                            mode: BlendMode.MULTIPLY
                            bottomInput: InnerShadow {
                                color: Color.web("#feeb42")
                                radius: 9
                                choke: 0.8
                            }
                            topInput: InnerShadow {
                                color: Color.web("#f13a00")
                                radius: 5
                                choke: 0.4
                            }
                        }
                    }
                }

                fill: Color.WHITE
                font : Font { name: "Harlow" size : 110 }
                layoutX: bind (width - text.layoutBounds.width)/2
                layoutY: bind (height/2) + 20
                content: bind textBox.rawText
            }
            textBox = TextBox {
                text:"Neon Sign"
                layoutX: 100
                layoutY: height - 40
                width: width-200
            }
        ]
    }
}