package com.njwapps.walkingwithgodstudies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njwapps.walkingwithgodstudies.model.Study;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
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

            StudiesList studiesList = gson.fromJson(inputStreamReader,StudiesList.class);

            Study study = studiesList.studies.get(0);

            ///TODO:Depending on bundle args - based on which navdrawer item is clicked - study Index.
            //study.getItems() can bind to a listView

            inputStreamReader.close();

        } catch (IOException e) {
            Log.e("NJW", e.getMessage());
            e.printStackTrace();
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    class StudiesList {
        public String title;
        List<Study> studies = new ArrayList<>();


    }

}
