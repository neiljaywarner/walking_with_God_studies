package com.njwapps.walkingwithgodstudies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
            is = this.getActivity().getAssets().open("test.json"); //TODO: Constant, maybe constants file?

            inputStreamReader = new InputStreamReader(is, "UTF-8");

            Verses verses = gson.fromJson(inputStreamReader,Verses.class);
            Log.i("NJW", "verseTitle=" + verses.title);
            Log.i("NJW", "ref0="+ verses.dataset.get(0).ref);

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

    class Verses {
        public String title;
        List<Data> dataset = new ArrayList<>();


    }

    class Data {
        public String ref;
        public String note;
    }




}
