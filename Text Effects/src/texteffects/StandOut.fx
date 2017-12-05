package texteffects;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;
import javafx.scene.control.*;

var width = 600;
var height = 300;
var text:Text;
var textBox:TextBox;
Stage {
    title: "Text Effects: Stand Out"
    resizable: false
    scene: Scene {
        width: width
        height: height
        content: [
            Rectangle {
                width: width, height: height
                fill: LinearGradient {
                    startX: 0, endX: 0, startY: 0, endY: 1
                    stops: [
                        Stop { offset: 0, color: Color.web("#6c6a6a") }
                        Stop { offset: 1, color: Color.web("#3e3d3d") }
                    ]
                }
            }
            text = Text {
                effect: Reflection {
                        topOffset: -4
                }
                fill: LinearGradient {
                    startX: 0, endX: 0, startY: 0, endY: 1
                    stops: [
                        Stop { offset: 0.03, color: Color.web("#cccccc") }
                        Stop { offset: 0.499, color: Color.web("#fdfdfd") }
                        Stop { offset: 0.5, color: Color.web("#999999") }
                        Stop { offset: 0.99, color: Color.web("#dddddd") }
                    ]
                }
                stroke: LinearGradient {
                    startX: 0, endX: 0, startY: 0, endY: 1
                    stops: [
                        Stop { offset: 0.03, color: Color.web("#e9e5e5") }
                        Stop { offset: 0.06, color: Color.web("#909090") }
                        Stop { offset: 0.23, color: Color.web("#d1d1d1") }
                        Stop { offset: 0.34, color: Color.web("#9f9f9f") }
                        Stop { offset: 0.60, color: Color.web("#7b7b7b") }
                        Stop { offset: 0.67, color: Color.web("#aeaeae") }
                        Stop { offset: 0.76, color: Color.web("#7b7b7b") }
                        Stop { offset: 0.94, color: Color.web("#676766") }
                        Stop { offset: 0.99, color: Color.web("#e5e5e4") }
                    ]
                }
                font : Font { name: "Arial Black", size : 80 }
                layoutX: bind (width - text.layoutBounds.width)/2
                layoutY: bind (height/2) + 20
                content: bind textBox.rawText
            }
            textBox = TextBox {
                text:"STAND OUT"
                layoutX: 100
                layoutY: height - 40
                width: width-200
            }
        ]
    }
}