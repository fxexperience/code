package com.fxexperience.tools.caspianstyler;

/**
 * Gradient Descriptor
 * 
 * @author Jasper Potts
 */
public class Gradient {
    private final String css;
    private final String name;
    private final double topDerivation;
    private final double topMidDerivation;
    private final double bottomMidDerivation;
    private final double bottomDerivation;
    private final boolean shinny;

    public Gradient(String name, double topDerivation, double bottomDerivation) {
        this.name = name;
        this.topDerivation = topDerivation;
        this.bottomDerivation = bottomDerivation;
        topMidDerivation = Double.NaN;
        bottomMidDerivation = Double.NaN;
        shinny = false;
        css = "linear-gradient( to bottom, derive(-fx-color, "+topDerivation+"%) 0%, derive(-fx-color, "+bottomDerivation+"%) 100%);";
    }

    public Gradient(String name, double topDerivation, double topMidDerivation, double bottomMidDerivation, double bottomDerivation) {
        this.name = name;
        this.topDerivation = topDerivation;
        this.topMidDerivation = topMidDerivation;
        this.bottomMidDerivation = bottomMidDerivation;
        this.bottomDerivation = bottomDerivation;
        shinny = true;
        css = "linear-gradient( to bottom, "
                + "derive(-fx-color, "+topDerivation+"%) 0%, "
                + "derive(-fx-color, "+topMidDerivation+"%) 50%, "
                + "derive(-fx-color, "+bottomMidDerivation+"%) 50.5%, "
                + "derive(-fx-color, "+bottomDerivation+"%) 100%);";
    }

    public String getCss() {
        return css;
    }

    public String getName() {
        return name;
    }

    public double getBottomDerivation() {
        return bottomDerivation;
    }

    public double getBottomMidDerivation() {
        return bottomMidDerivation;
    }

    public boolean isShinny() {
        return shinny;
    }

    public double getTopDerivation() {
        return topDerivation;
    }

    public double getTopMidDerivation() {
        return topMidDerivation;
    }

    @Override public String toString() {
        return name;
    }
    
    public static final Gradient[] GRADIENTS = new Gradient[]{
        new Gradient("Default",34,-18),
        new Gradient("Subtle",35,-6),
        new Gradient("Shinny",45,34,5,-10)
    };
}
