package io.github.JumperOnJava.jjpizza.datatypes;

public class CircleSlice {
    public final Angle startAngle;
    public final Angle endAngle;

    public Angle getMidAngle() {
        return Angle.newRadian((this.startAngle.getRadian()+this.endAngle.getRadian())/2+(float)(endAngle.getRadian()<startAngle.getRadian()?Math.PI:0));
    }
    public CircleSlice(Angle startAngle, Angle endAngle){
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    public static CircleSlice radians(float startAngle, float endAngle) {
        return new CircleSlice(Angle.newRadian(startAngle),Angle.newRadian(endAngle));
    }
    public static CircleSlice percent(float startAngle, float endAngle) {
        return new CircleSlice(Angle.newPercent(startAngle),Angle.newPercent(endAngle));
    }

    public boolean inInSlice(Angle angle){
        var minAngle = Math.min(startAngle.getRadian(), endAngle.getRadian());
        var maxAngle = Math.max(startAngle.getRadian(), endAngle.getRadian());
        /*try {
            LavaJumper.log(startAngle.getRadian()+" "+endAngle.getRadian()+" "+angle.getRadian());
        }
        catch (Exception e){
            //throw e;
        }**/
        var anglee = angle.getRadian();
        var r1 = anglee >= minAngle;
        var r2 = anglee < maxAngle;
        var r3 = startAngle.getRadian()>endAngle.getRadian();
        var o1 = r1&&r2;
        var o2 = o1 ^ r3;
        //return false;
        return o2;

    }
}
