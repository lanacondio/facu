package com.lanacondio.diccionarioguarani;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button allWords;
    TextView languajeText;
    EditText textToFind;
    Switch lselector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allWords = (Button)findViewById(R.id.findResultButton);
        allWords.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                EditText wordtf = (EditText) findViewById(R.id.textToFind);

                String valueToFind = wordtf.getText().toString();
                if(TextUtils.isEmpty(valueToFind)) {
                    wordtf.setError("Debe ingresar un valor");
                    return;
                }
                else{
                    Intent allWords = new Intent(MainActivity.this, AllWordsActivity.class);
                    allWords.putExtra("wordtf",valueToFind);
                    startActivity(allWords);
                }

            }

        });

        Switch lselector=(Switch) findViewById(R.id.selectLanguageSwitch);
        lselector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                languajeText = (TextView)findViewById(R.id.languageTextView);
                if(isChecked){
                    languajeText.setText(getResources().getString(R.string.GuaraniSpanish));
                }else{
                    languajeText.setText(getResources().getString(R.string.SpanishGuarani));
                }

            }
        });

    }

}
