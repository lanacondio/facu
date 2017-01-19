package com.lanacondio.diccionarioguarani;

/**
 * Created by lanacondio on 12/1/2017.
 */

public enum FontColor {
    Black(R.style.FontColor_ColorBlack, "Black"),
    White(R.style.FontColor_ColorWhite, "White"),
    Red(R.style.FontColor_ColorRed, "Red");

    private int resId;
    private String title;

    public int getResId() {
        return resId;
    }

    public String getTitle() {
        return title;
    }

    FontColor(int resId, String title) {
        this.resId = resId;
        this.title = title;
    }
}