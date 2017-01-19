package com.lanacondio.diccionarioguarani;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lanacondio on 12/1/2017.
 */

public class Preferences {
    private final static String FONT_STYLE = "FONT_STYLE";
    private final static String FONT_COLOR = "FONT_COLOR";

    private final Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    protected SharedPreferences open() {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor edit() {
        return open().edit();
    }

    public FontStyle getFontStyle() {
        return FontStyle.valueOf(open().getString(FONT_STYLE,
                FontStyle.Medium.name()));
    }

    public void setFontStyle(FontStyle style) {
        edit().putString(FONT_STYLE, style.name()).commit();
    }

    public FontColor getFontColor() {
        return FontColor.valueOf(open().getString(FONT_COLOR,
                FontColor.White.name()));
    }

    public void setFontColor(FontColor style) {
        edit().putString(FONT_COLOR, style.name()).commit();
    }

}
