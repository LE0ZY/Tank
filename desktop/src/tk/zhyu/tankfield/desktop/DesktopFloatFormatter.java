package tk.zhyu.tankfield.desktop;

import tk.zhyu.tankfield.FloatFormatter;

public class DesktopFloatFormatter implements FloatFormatter {

    @Override
    public String getFormattedString(float value) {
        return String.format("%.1f", value);
    }
}