package com.lanacondio.diccionarioguarani;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.FavoriteDbHelper;
import com.lanacondio.diccionarioguarani.repository.com.lanacondio.diccionarioguarani.repository.models.TranslationDbHelper;
import com.lanacondio.diccionarioguarani.service.FavoritesCursorAdapter;
import com.lanacondio.diccionarioguarani.service.PredictiveResultCursorAdapter;
import com.lanacondio.diccionarioguarani.service.ResultCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnFragmentInteractionListener mListener;
    private FavoriteDbHelper mFavoritesDbHelper;
    private ListView mResultList;
    private String swtoFind;
    private TextView mWord;
    private FavoritesCursorAdapter mResultAdapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllWordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
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
        final View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        getActivity().setTitle(R.string.show_favorites);
        // Referencias UI

        final SearchView wordtf = (SearchView) root.findViewById(R.id.favoritesToFind);

        wordtf.setIconifiedByDefault(true);
        wordtf.setFocusable(true);
        wordtf.setIconified(false);
        wordtf.requestFocusFromTouch();

        wordtf.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //EditText wordtf = (EditText) findViewById(R.id.textToFind);

                String valueToFind = query;
                if(TextUtils.isEmpty(valueToFind)) {

                    return false;
                }
                else{

                    mResultList = (ListView) root.findViewById(R.id.favorites_list);

                    mResultAdapter = new FavoritesCursorAdapter(getActivity(), null);

                    // Setup
                    mResultList.setAdapter(mResultAdapter);

                    // Instancia de helper
                    mFavoritesDbHelper = new FavoriteDbHelper(getActivity());

                    // Carga de datos
                    loadResults();

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // you can call or do what you want with your EditText here
                String valueToFind = newText;
                swtoFind = valueToFind;

                if(swtoFind != null && !swtoFind.isEmpty()){

                    mResultList = (ListView) getView().findViewById(R.id.favorites_list);

                    mResultAdapter = new FavoritesCursorAdapter(getContext(), null);

                    // Setup
                    mResultList.setAdapter(mResultAdapter);

                    // Instancia de helper
                    FavoriteDbHelper mTranslationDbHelper = new FavoriteDbHelper(getContext());

                    // Carga de datos
                    loadPredictiveResults();

                }else{

                    mResultList = (ListView) root.findViewById(R.id.favorites_list);

                    mResultAdapter = new FavoritesCursorAdapter(getActivity(), null);

                    // Setup
                    mResultList.setAdapter(mResultAdapter);

                    // Instancia de helper
                    mFavoritesDbHelper = new FavoriteDbHelper(getActivity());

                    // Carga de datos
                    loadResults();
                }
                return true;
            }
        });


        mResultList = (ListView) root.findViewById(R.id.favorites_list);

        mResultAdapter = new FavoritesCursorAdapter(getActivity(), null);

       // Setup
        mResultList.setAdapter(mResultAdapter);

        // Instancia de helper
        mFavoritesDbHelper = new FavoriteDbHelper(getActivity());

        // Carga de datos
        loadResults();

        return root;
    }



    private void loadResults() {
        new ResultLoadTask().execute();
    }

    private void loadPredictiveResults() {
        new ResultPredictiveLoadTask().execute();
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

            return mFavoritesDbHelper.getFavorites();

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

    private class ResultPredictiveLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {

            return mFavoritesDbHelper.getPredictiveFavorites(swtoFind);

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
