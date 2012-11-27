package com.fxexperience.tools.splineeditor;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * @author Jasper Potts
 */
public class SplineEditorControl extends XYChart<Number, Number> {
    
    private DoubleProperty controlPoint1x = new SimpleDoubleProperty(0.8);
    public double getControlPoint1x() { return controlPoint1x.get(); }
    public DoubleProperty controlPoint1xProperty() { return controlPoint1x; }
    private DoubleProperty controlPoint1y = new SimpleDoubleProperty(0.2);
    public double getControlPoint1y() { return controlPoint1y.get(); }
    public DoubleProperty controlPoint1yProperty() { return controlPoint1y; }
    private DoubleProperty controlPoint2x = new SimpleDoubleProperty(0.2);
    public double getControlPoint2x() { return controlPoint2x.get(); }
    public DoubleProperty controlPoint2xProperty() { return controlPoint2x; }
    private DoubleProperty controlPoint2y = new SimpleDoubleProperty(0.8);
    public double getControlPoint2y() { return controlPoint2y.get(); }
    public DoubleProperty controlPoint2yProperty() { return controlPoint2y; }
    
    private Circle controlPoint1Circle = CircleBuilder.create()
            .fill(Color.WHITE)
            .stroke(Color.RED)
            .strokeWidth(3)
            .radius(6)
            .cursor(Cursor.HAND)
            .build();
    private Circle controlPoint2Circle =  CircleBuilder.create()
            .fill(Color.WHITE)
            .stroke(Color.RED)
            .strokeWidth(3)
            .radius(6)
            .cursor(Cursor.HAND)
            .build();
    private Line cp1Line = LineBuilder.create()
            .stroke(Color.RED)
            .strokeWidth(2)
            .build();
    private Line cp2Line = LineBuilder.create()
            .stroke(Color.RED)
            .strokeWidth(2)
            .build();
    private Path dottedLinesPath = PathBuilder.create()
            .stroke(Color.WHITE)
            .strokeWidth(1)
            .strokeDashArray(1d,8d)
            .build();
    private Path splinePath = PathBuilder.create()
            .stroke(Color.WHITE)
            .strokeWidth(2)
            .build();
    
    private double dragStartX, dragStartY;
    
    public SplineEditorControl() {
        super(new NumberAxis(0, 1, 0.1), new NumberAxis(0, 1, 0.1));
        setAnimated(false);
        getXAxis().setAutoRanging(false);
        getXAxis().setAutoRanging(false);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setAlternativeRowFillVisible(false);
        getPlotChildren().addAll(splinePath,dottedLinesPath, cp1Line,cp2Line,controlPoint1Circle, controlPoint2Circle);
        
        setData(FXCollections.observableArrayList(new Series<Number, Number>()));
        
        controlPoint1Circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                final double x = event.getX()+controlPoint1Circle.getLayoutX();
                final double y = event.getY()+controlPoint1Circle.getLayoutY();
                final double dataX = getXAxis().getValueForDisplay(x).doubleValue();
                final double dataY = getYAxis().getValueForDisplay(y).doubleValue();
                controlPoint1x.set(clamp(dataX));
                controlPoint1y.set(clamp(dataY));
                requestChartLayout();
            }
        });
        controlPoint2Circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                final double x = event.getX()+controlPoint2Circle.getLayoutX();
                final double y = event.getY()+controlPoint2Circle.getLayoutY();
                final double dataX = getXAxis().getValueForDisplay(x).doubleValue();
                final double dataY = getYAxis().getValueForDisplay(y).doubleValue();
                controlPoint2x.set(clamp(dataX));
                controlPoint2y.set(clamp(dataY));
                requestChartLayout();
            }
        });
    }

    @Override protected void layoutPlotChildren() {
        double cp1x = getXAxis().getDisplayPosition(controlPoint1x.get());
        double cp1y = getYAxis().getDisplayPosition(controlPoint1y.get());
        double cp2x = getXAxis().getDisplayPosition(controlPoint2x.get());
        double cp2y = getYAxis().getDisplayPosition(controlPoint2y.get());
        double minx = getXAxis().getZeroPosition();
        double miny = getYAxis().getZeroPosition();
        double maxx = getXAxis().getDisplayPosition(1);
        double maxy = getYAxis().getDisplayPosition(1);
        controlPoint1Circle.setLayoutX(cp1x);
        controlPoint1Circle.setLayoutY(cp1y);
        controlPoint2Circle.setLayoutX(cp2x);
        controlPoint2Circle.setLayoutY(cp2y);
        cp1Line.setStartX(minx);
        cp1Line.setStartY(miny);
        cp1Line.setEndX(cp1x);
        cp1Line.setEndY(cp1y);
        cp2Line.setStartX(maxx);
        cp2Line.setStartY(maxy);
        cp2Line.setEndX(cp2x);
        cp2Line.setEndY(cp2y);
        dottedLinesPath.getElements().setAll(
                new MoveTo(minx-0.5, cp1y-0.5),
                new LineTo(cp1x-0.5,cp1y-0.5),
                new LineTo(cp1x-0.5,miny-0.5),
                new MoveTo(minx-0.5, cp2y-0.5),
                new LineTo(cp2x-0.5,cp2y-0.5),
                new LineTo(cp2x-0.5,miny-0.5)
                );
        splinePath.getElements().setAll(
                new MoveTo(minx, miny),
                new CubicCurveTo(cp1x, cp1y, cp2x, cp2y, maxx, maxy));
    }
    
    private static double clamp(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value;
    }
    
    @Override protected void dataItemAdded(Series<Number, Number> series, int i, Data<Number, Number> data) {}
    @Override protected void dataItemRemoved(Data<Number, Number> data, Series<Number, Number> series) {}
    @Override protected void dataItemChanged(Data<Number, Number> data) {}
    @Override protected void seriesAdded(Series<Number, Number> series, int i) {}
    @Override protected void seriesRemoved(Series<Number, Number> series) {}
}
