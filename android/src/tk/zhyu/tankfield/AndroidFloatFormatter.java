package tk.zhyu.tankfield;

public class AndroidFloatFormatter implements FloatFormatter {

    @Override
    public String getFormattedString(float value) {
        return String.format("%.1f", value);
    }
}