package io.github.JumperOnJava.jjpizza.datatypes;

public enum AngleType{
    RADIANS(1),
    DEGREES(1f/180f*(float)Math.PI),
    PERCENT(2*(float)Math.PI)
    ;
    private final float multiplier;
    public float apply(float angle){
        return angle*multiplier;
    }
    AngleType(float multiplier) {
        this.multiplier =multiplier;
    }

    public float fromAngle(Angle angle) {
        return angle.getRadian()/multiplier;
    }
}