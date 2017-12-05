package texteffects;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;
import javafx.scene.control.*;

var width = 600;
var height = 400;
var text:Text;
var textBox:TextBox;
Stage {
    title: "Text Effects: Snow & Ice"
    resizable: false
    scene: Scene {
        width: width
        height: height
        content: [
            Rectangle {
                width: width, height: height
                fill: RadialGradient {
                    centerX: 0.5, centerY: 0.5
                    radius: 0.7, proportional: true
                    stops: [
                        Stop{offset: 0.4, color: Color.web("#34365e")}
                        Stop{offset: 1, color: Color.web("#0d0f3a")}
                    ]
                }
            }
            text = Text {
                effect: Blend{
                    mode: BlendMode.SRC_OVER
                    bottomInput: DropShadow {
                        color: Color.rgb(0,0,0,0.1)
                        offsetX: 8, offsetY: 8
                        radius: 2
                    }
                    topInput: Blend{
                        mode: BlendMode.SRC_OVER
                        bottomInput: DropShadow {
                            color: Color.web("#21233f")
                            radius: 4
                            spread: 0.9
                        }
                        topInput: Blend{
                            mode: BlendMode.DARKEN
                            bottomInput: InnerShadow {
                                color: Color.web("#cee3f4")
                                radius: 5
                                choke: 0.7
                                offsetX: -4, offsetY: -4
                            }
                            topInput: Blend{
                                mode: BlendMode.MULTIPLY
                                bottomInput: InnerShadow {
                                    color: Color.web("#6c7fee")
                                    radius: 7
                                    choke: 0.2
                                    offsetX: 2, offsetY: 2
                                }
                                topInput: InnerShadow {
                                    color: Color.web("#a5ebff")
                                    radius: 4
                                    offsetX: -2, offsetY: -2
                                }
                            }
                        }
                    }
                }
                fill: Color.web("#f7fafb")//web("#e5f5fb")
                strokeWidth: 3
                font : Font {
                    name: "Kabel"
                    size : 120
                }
                layoutX: bind (width - text.layoutBounds.width)/2
                layoutY: bind (height/2) - 20
                content: bind if (textBox.rawText == "") then "Snow \n& Ice" else textBox.rawText
            }
            textBox = TextBox {
                layoutX: 100
                layoutY: height - 40
                width: width-200
            }
        ]
    }
}