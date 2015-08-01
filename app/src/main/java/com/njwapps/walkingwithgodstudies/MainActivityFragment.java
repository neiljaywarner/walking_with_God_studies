package com.njwapps.walkingwithgodstudies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njwapps.walkingwithgodstudies.model.Study;
import com.njwapps.walkingwithgodstudies.model.VerseInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    VerseListAdapter mAdapter;
    StudiesList mStudiesList;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: Set Empty Text.

        // Create an empty adapter we will use to display the loaded data.


    }

    //TODO: newInstance method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get args
        InputStream is= null;
        InputStreamReader inputStreamReader = null;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            is = this.getActivity().getAssets().open("default_studies.json"); //TODO: Constant, maybe constants file?

            inputStreamReader = new InputStreamReader(is, "UTF-8");

            mStudiesList = gson.fromJson(inputStreamReader, StudiesList.class);


            ///TODO:Depending on bundle args - based on which navdrawer item is clicked - study Index.
            //study.getItems() can bind to a listView

            inputStreamReader.close();

        } catch (IOException e) {
            Log.e("NJW", e.getMessage());
            e.printStackTrace();
        }


        mAdapter = new VerseListAdapter(getActivity());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_verse_list, container, false);


        mListView = (AbsListView) root.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        //progress bar?

        Study study = mStudiesList.studies.get(1); //for test purposes.

        mAdapter.setData(study.getItems());


        return root;
    }

    public static class VerseListAdapter extends ArrayAdapter<VerseInfo> {
        private final LayoutInflater mInflater;

        public VerseListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<VerseInfo> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        /**
         * Populate new items in the list.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_verse, parent, false);
            } else {
                view = convertView;
            }

            VerseInfo item = getItem(position);

            ((TextView) view.findViewById(R.id.text_verse)).setText(item.getRef());

            ((TextView) view.findViewById(R.id.text_note)).setText(item.getNote());


            return view;
        }
    }

    class StudiesList {
        public String title;
        List<Study> studies = new ArrayList<>();


    }


}
