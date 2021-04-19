package com.lanacondio.diccionarioguarani;

import java.util.Locale;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.FavoriteDbHelper;
import com.lanacondio.diccionarioguarani.service.FavoritesCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigurationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigurationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnFragmentInteractionListener mListener;
    private Spinner languagelist;


    public ConfigurationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllWordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigurationFragment newInstance() {
        ConfigurationFragment fragment = new ConfigurationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private String actualValue;
    private int actualindex;
    private String actualLanguage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_configuration, container, false);

        getActivity().setTitle(R.string.show_configuration);
        // Referencias UI


        ImageButton smallFont = (ImageButton) root.findViewById(R.id.ib_smallfs);
        ImageButton medFont = (ImageButton) root.findViewById(R.id.ib_medfs);
        ImageButton bigFont = (ImageButton) root.findViewById(R.id.ib_bigfs);


        Button blackFont = (Button) root.findViewById(R.id.blackbutton);
        Button whiteFont = (Button) root.findViewById(R.id.whitebutton);
        Button redFont = (Button) root.findViewById(R.id.redbutton);

        actualValue = getLocale();
        actualLanguage = getResources().getString(R.string.Spanish);
        actualindex = getActivity().getIntent().getIntExtra("actualIndex",0);

        if(!(actualValue.equals("es_US") || actualValue.equals("es") )){actualLanguage = getResources().getString(R.string.Guarani); actualindex=1;}

        smallFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences prefs = new Preferences(getContext());
                prefs.setFontStyle(FontStyle.Small);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getContext(), "Se cambia el tamaño de la fuente", duration);
                toast.show();
            }
        });

        medFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences prefs = new Preferences(getContext());
                prefs.setFontStyle(FontStyle.Medium);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getContext(), "Se cambia el tamaño de la fuente", duration);
                toast.show();
            }
        });


        bigFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences prefs = new Preferences(getContext());
                prefs.setFontStyle(FontStyle.Large);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getContext(), "Se cambia el tamaño de la fuente", duration);
                toast.show();
            }
        });


        blackFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences prefs = new Preferences(getContext());
                prefs.setFontColor(FontColor.Black);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getContext(), "Se cambia el color de la fuente", duration);
                toast.show();
            }
        });

        whiteFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences prefs = new Preferences(getContext());
                prefs.setFontColor(FontColor.White);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getContext(), "Se cambia el color de la fuente", duration);
                toast.show();
            }
        });


        redFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences prefs = new Preferences(getContext());
                prefs.setFontColor(FontColor.Red);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getContext(), "Se cambia el color de la fuente", duration);
                toast.show();
            }
        });

        languagelist = (Spinner) root.findViewById(R.id.conf_language_spinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        languagelist.setAdapter(adapter);

        languagelist.setSelection(actualindex, false);

        languagelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String language = languagelist.getSelectedItem().toString();

                if (!language.equals(actualLanguage)) {
                    if(language == getResources().getString(R.string.Spanish))
                    {
                        setLocale("es", 0, getResources().getString(R.string.Spanish));
                    }
                    else
                    {
                        setLocale("gu", 1, getResources().getString(R.string.Guarani));
                    }
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), "Se cambia el idioma", duration);
                    toast.show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });



        return root;
    }


    public void setLocale(String lang, int selectedIndex, String language) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getContext(), ConfigurationActivity.class);
        refresh.putExtra("actualIndex",selectedIndex);
        startActivity(refresh);
    }

    public String getLocale() {
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        return  conf.locale.toString();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
