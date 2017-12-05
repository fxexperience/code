package texteffects;

import javafx.stage.Stage;
import javafx.scene.*;
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
    title: "Text Effects: Recessed"
    resizable: false
    scene: Scene {
        width: width
        height: height
        content: [
            ImageView {
                image: Image{ url: "{__DIR__}bricks.jpg"}
            }
            Rectangle {
                width: width, height: height
                fill: LinearGradient {
                    startX: 0, endX: 0, startY: 0, endY: 1
                    stops: [
                        Stop { offset: 0, color: Color.rgb(202,202,202) }
                        Stop { offset: 1, color: Color.rgb(97,97,97) }
                    ]
                }

            }
//            Group {
//                effect: DropShadow {
//                        color: Color.rgb(255,255,255,0.5)
//                        offsetX: 1, offsetY: 1
//                        radius: 0
//                }
//                content: text = Text {
//                    effect: InnerShadow {
//                        color: Color.rgb(0,0,0,0.7)
//                        offsetX: 2, offsetY: 2
//                        radius: 5
//                    }
//                    fill: LinearGradient {
//                        startX: 0, endX: 0.01, startY: 0, endY: 1
//                        stops: [
//                            Stop { offset: 0, color: Color.rgb(140,140,140) }
//                            Stop { offset: 1, color: Color.rgb(110,110,110) }
//                        ]
//                    }
//                    font : Font {
//                        name: "Arial Black"
//                        size : 90
//                    }
//                    layoutX: bind (width - text.layoutBounds.width)/2
//                    layoutY: bind (height/2) + 20
//                    content: bind textBox.rawText
//                }
//            }

            text = Text {
                effect: Blend{
                    mode: BlendMode.MULTIPLY
                    bottomInput: DropShadow {
                        color: Color.rgb(255,255,255,0.5)
                        offsetX: 1, offsetY: 1
                        radius: 0
                    }
                    topInput: InnerShadow {
                        color: Color.rgb(0,0,0,0.7)
                        offsetX: 2, offsetY: 2
                        radius: 5
                    }
                }
                fill: LinearGradient {
                    startX: 0, endX: 0.01, startY: 0, endY: 1
                    stops: [
                        Stop { offset: 0, color: Color.rgb(190,190,190) }
                        Stop { offset: 1, color: Color.rgb(170,170,170) }
                    ]
                }
                font : Font {
                    name: "Arial Black"
                    size : 90
                }
                layoutX: bind (width - text.layoutBounds.width)/2
                layoutY: bind (height/2) + 20
                content: bind textBox.rawText
            }
            textBox = TextBox {
                text:"Recessed"
                layoutX: 100
                layoutY: height - 40
                width: width-200
            }
        ]
    }
}