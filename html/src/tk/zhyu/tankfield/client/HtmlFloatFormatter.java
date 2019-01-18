package tk.zhyu.tankfield.client;

import com.google.gwt.i18n.client.NumberFormat;

import tk.zhyu.tankfield.FloatFormatter;

public class HtmlFloatFormatter implements FloatFormatter {

    @Override
    public String getFormattedString(float value) {
        NumberFormat format = NumberFormat.getFormat("#.#");
        return format.format(value);
    }
}