package io.github.JumperOnJava.lavajumper.gui.widgets;

import net.minecraft.text.Text;

import java.util.function.Consumer;

/**
 * Slider widget for general use.
 */
public class SliderWidget extends net.minecraft.client.gui.widget.SliderWidget {
    private final float round;
    private Consumer<Double> changedListener;
    private Text text;
    public double min, max;

    /**
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param text
     * @param minValue
     * @param maxValue
     * @param value
     * @param round rounding of value.
     *              For example when round==5 then value range from 0 to 256
     *              values would only be numbers that are divisible by five (0,5,15,230,255).
     *              This rule is not applied to value contructor parameter
     */
    public SliderWidget(int x, int y, int width, int height, Text text, double minValue, double maxValue, double value,float round) {
        super(x, y, width, height, text, value);
        this.min=minValue;
        this.max=maxValue;
        this.text=text;
        this.round=round;
        super.setValue(scaleValueTo01(value));
        setMessage(text);
    }
    public SliderWidget(int x, int y, int width, int height, Text text, double maxValue, double value,int round){
        this(x, y, width, height, text,0,maxValue, value,round);
    }

    @Override
    protected void updateMessage() {
    }
    public void setText(Text text){
        this.text=text;
        updateMessage();
    }
    public Text getText(){
        return text;
    }

    @Override
    public Text getMessage() {
        var val = round(scaleValueFrom01(value));
        var isInt = round == (int) round;
        var s = isInt ? String.valueOf((int) val) : String.valueOf(val);
        return this.text.copy().append(Text.literal(" "+s));
    }
    public double round(double value){
        return ((float)Math.round(value/round))*round;
    }
    @Override
    protected void applyValue() {
        if(changedListener==null)
            return;
        changedListener.accept(round(scaleValueFrom01(value)));
    }
    private double scaleValueFrom01(double value){
        return value*(max-min)+min;
    }
    private double scaleValueTo01(double value){
        return (value-min)/(max-min);
    }

    public void setChangedListener(Consumer<Double> changedListener) {
        this.changedListener = changedListener;
    }
}
