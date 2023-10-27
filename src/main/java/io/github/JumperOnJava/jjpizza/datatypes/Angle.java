package io.github.JumperOnJava.jjpizza.datatypes;

public class Angle {
    public final float radianAngle;
    private Angle(float angle){
        this.radianAngle=limitAngle(angle);
    }
    public static Angle newRadian(float radianAngle){
        return new Angle(AngleType.RADIANS.apply(radianAngle));
    }
    public static Angle newPercent(float percentAngle){
        return new Angle(AngleType.PERCENT.apply(percentAngle));
    }
    public static Angle newDegree(float degreeAngle){
        return new Angle(AngleType.DEGREES.apply(degreeAngle));
    }
    private static float limitAngle(float angle){
        return mod(angle, (float) (2*Math.PI));
    }
    private static float mod(float a,float b){
        return (float) (a-(b*Math.floor(a/b)));
    }
    public Angle add(Angle angle){
        return Angle.newRadian(angle.getRadian()+this.radianAngle);
    }
    public float getRadian() {
        return this.radianAngle;
    }

    public float getDegree() {
        return AngleType.DEGREES.fromAngle(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==this)
            return true;
        if(obj instanceof Angle angle)
            return this.getRadian() == angle.getRadian();
        else return false;
    }
}
