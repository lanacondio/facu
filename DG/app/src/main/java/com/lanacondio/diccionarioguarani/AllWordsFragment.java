package com.lanacondio.diccionarioguarani;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.ResultCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllWordsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllWordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllWordsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnFragmentInteractionListener mListener;

    private TranslationDbHelper mTranslationDbHelper;

    private ListView mResultList;
    private TextView mWord;
    private ResultCursorAdapter mResultAdapter;
    private String wtoFind;


    public AllWordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllWordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllWordsFragment newInstance() {
        AllWordsFragment fragment = new AllWordsFragment();
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
        View root = inflater.inflate(R.layout.fragment_all_words, container, false);

        // Referencias UI
        wtoFind = getArguments().getString("wordtf");

        getActivity().setTitle(wtoFind);
        mResultList = (ListView) root.findViewById(R.id.all_words_list);

        mResultAdapter = new ResultCursorAdapter(getActivity(), null);

       // Setup
        mResultList.setAdapter(mResultAdapter);

        // Instancia de helper
        mTranslationDbHelper = new TranslationDbHelper(getActivity());

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

            return mTranslationDbHelper.getTranslations(wtoFind);
            //return mTranslationDbHelper.getAllLawyers();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mResultAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }
}
