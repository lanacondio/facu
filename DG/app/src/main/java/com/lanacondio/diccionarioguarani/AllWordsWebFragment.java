package com.lanacondio.diccionarioguarani;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.Translation;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationContract;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.GdiccApiClient;
import com.lanacondio.diccionarioguarani.service.ResultCursorAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllWordsWebFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllWordsWebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllWordsWebFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnFragmentInteractionListener mListener;
    private TranslationDbHelper mTranslationDbHelper;
    private ListView mResultList;
    private TextView mWord;
    private ResultCursorAdapter mResultAdapter;
    private GdiccApiClient mApiServiceClient;
    private String wtoFind;
    private Integer originalLanguaje;

    public AllWordsWebFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllWordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllWordsWebFragment newInstance() {
        AllWordsWebFragment fragment = new AllWordsWebFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_all_words_web, container, false);

        // Referencias UI
        wtoFind = getArguments().getString("wordtf");
        originalLanguaje = getArguments().getInt("olanguage");

        getActivity().setTitle(wtoFind.toUpperCase());
        mResultList = (ListView) root.findViewById(R.id.all_words_web_list);

        mResultAdapter = new ResultCursorAdapter(getActivity(), null);

       // Setup
        mResultList.setAdapter(mResultAdapter);

        // Instancia de helper
        mTranslationDbHelper = new TranslationDbHelper(getActivity());

        mApiServiceClient = new GdiccApiClient();
        // Carga de datos
        loadResults();

        return root;
    }



    private void loadResults() {
        new ResultLoadTask().execute();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    private class ResultLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {

            MatrixCursor matrixCursor = new MatrixCursor(new String[] {TranslationContract.TranslationEntry._ID,
                    TranslationContract.TranslationEntry.TRANSLATION,
                    TranslationContract.TranslationEntry.CONTEXT,
                    TranslationContract.TranslationEntry.TYPE });


            GdiccApiClient client = new GdiccApiClient();
            List<Translation> translations = new ArrayList<>();
            try {
                client.getWords(wtoFind);
                translations =  client.getTranslations();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0; i<translations.size(); i++)
            {

                matrixCursor.addRow(new Object[] {translations.get(i).getId(),
                        translations.get(i).getTranslationResult(),
                        translations.get(i).getContext(),
                        translations.get(i).getType() });
                mTranslationDbHelper.addTranslation(translations.get(i));
            }

            return matrixCursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mResultAdapter.swapCursor(cursor);
            }
            else
            {
                MatrixCursor matrixCursor = new MatrixCursor(new String[] {TranslationContract.TranslationEntry._ID, TranslationContract.TranslationEntry.TRANSLATION,
                        TranslationContract.TranslationEntry.CONTEXT,TranslationContract.TranslationEntry.TYPE });
                matrixCursor.addRow(new Object[] { 1, getContext().getString(R.string.not_found_word), "", "" });
                MergeCursor mergeCursor = new MergeCursor(new Cursor[] { matrixCursor });
                mResultAdapter.swapCursor(mergeCursor);
            }

        }
    }



}
