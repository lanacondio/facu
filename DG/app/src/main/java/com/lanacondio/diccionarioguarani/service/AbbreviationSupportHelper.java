package com.lanacondio.diccionarioguarani.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.R;

public class AbbreviationSupportHelper {
    public boolean Show = false;

    public void IntroSupport(Activity activity){

        final Session session= new Session(activity);


        String alert2 = "ABREVIATURAS.";
        String alert3 = "a.n. : accidente nominal \n" +
                "a.v.: accidente verbal \n" +
                "adj.: adjetivo \n" +
                "adj. f: adjetivo femenino \n" +
                "adj. m: adjetivo masculino \n" +
                "adv.: adverbio \n" +
                "af.: aféresis \n" +
                "air: aireal \n" +
                "aamb: sustantivo de género ambiguo \n" +
                "ap.: apócope \n" +
                "arc: arcaísmo \n" +
                "art.: artículo \n" +
                "át.: átona/o \n" +
                "atr.: atributivo \n" +
                "aux.: auxiliar \n" +
                "bif.: biforme \n" +
                "com.: sustantivo común de dos géneros \n" +
                "conj.: conjunción \n" +
                "def.: defectivo \n" +
                "dem.: demostrativo \n" +
                "excl.: excluyente \n" +
                "exclam.: exclamativo \n" +
                "exp.: expresión \n" +
                "f.: sustantivo femenino \n" +
                "fig.: figurativo \n" +
                "h.: hispanismo \n" +
                "imp.: impersonal \n"+
                "incl.: incluyente \n" +
                "ind.: indefinido \n" +
                "int.: interrogativo \n"+
                "interj.: interjección \n" +
                "intr.: verbo intransitivo \n" +
                "irr.: irregular \n"+
                "m.: sustantivo masculino \n" +
                "neg.: negativo \n" +
                "neol.: neologísmo \n" +
                "núm.: número \n" +
                "p. n.: posposición nominal \n" +
                "p. v.: posposición verbal \n" +
                "parag.: paraguayismo \n" +
                "per.: persona \n" +
                "pers.: personal \n" +
                "pl.: plural \n" +
                "pos.: posesivo \n" +
                "pref.: prefijo \n" +
                "pref. a. n.: prefijo de accidente nominal \n" +
                "pref. a. v.: prefijo de accidente verbal \n" +
                "prep.: preposición \n" +
                "prnl.: verbo pronominal \n"+
                "pron.: pronombre \n" +
                "rel.: relativo \n" +
                "s.: sustantivo \n"+
                "sing.: singular \n" +
                "sub.: subordinativa \n" +
                "suf.: sufijo \n"+
                "suf. a. n.: sufijo de accidente nominal\n"+
                "suf. a. v.: sufijo de accidente verbal\n"+
                "t.: triforme \n" +
                "tón.: tónica/o \n" +
                "tr.: verbo transitivo \n"+
                "U. t. c.: úsase también como  \n" +
                "U. t. c. s.: úsase también como sustantivo \n" +
                "v.: verbo \n" +
                "v. air.: verbo aireal \n"+
                "v. atr.: verbo atributivo \n"+
                "v. pr.: verbo propio \n"+
                "voc.: vocativo \n";

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ImageView image = new ImageView(activity);
        //image.setImageResource(R.mipmap.ic_bandera);

        TextView title = new TextView(activity);
        title.setText("Bienvenido a GDICC!");
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setPadding(0,20,0,20);
        title.setGravity(Gravity.CENTER);

        LayoutInflater inflater= LayoutInflater.from(activity);
        View view=inflater.inflate(R.layout.abbreviation_popup, null);


        TextView textview=(TextView)view.findViewById(R.id.textAbb);
        textview.setText(alert2  +"\n"+""+"\n"+ alert3+"\n"+""+"\n");

/*
        textView.setScroller(new Scroller(this));
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new ScrollingMovementMethod());
*/
        builder.setIcon(R.mipmap.ic_launcher)
                .setCustomTitle(title)
                .setView(image)
                //.setMessage(alert2  +"\n"+""+"\n"+ alert3+"\n"+""+"\n")
                .setNegativeButton("Entendido!",null)
                .setPositiveButton("No volver a mostrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.setShowAbbreviationHelp(false);
                    }
                });
        builder.setCancelable(false);
        AlertDialog about = builder.create();
        about.setView(view);
        about.show();
        TextView messageText = about.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        Button nbutton = about.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
    }


}
