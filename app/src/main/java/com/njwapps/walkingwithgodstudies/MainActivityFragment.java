package com.njwapps.walkingwithgodstudies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    VerseListAdapter mAdapter;
    StudiesList mStudiesList;
    private Study mStudy;
    private int mSectionNumber;

    private AbsListView mListView;

    private Tracker mTracker;

    public MainActivityFragment() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainActivityFragment newInstance(int sectionNumber) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //for sharing
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        // Retrieve the share menu item
        MenuItem shareItem = menu.findItem(R.id.menu_share);

        // Now get the ShareActionProvider from the item
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        shareActionProvider.setShareIntent(getShareIntent(mStudy.getShareText()));
        shareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {

                // Build and send social interaction.
                mTracker.send(new HitBuilders.SocialBuilder()
                        .setNetwork(intent.getPackage())
                        .setAction("share")
                        .setTarget(source.toString())
                        .build());
                return false;  //return result ignored, return false for consistency.
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    //TODO: newInstance method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //for sharing

        AnalyticsApplication application = (AnalyticsApplication) this.getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        // get args
        mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        InputStream is= null;
        InputStreamReader inputStreamReader = null;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            is = this.getActivity().getAssets().open("default_studies.json"); //TODO: Constant, maybe constants file?

            inputStreamReader = new InputStreamReader(is, "UTF-8");

            mStudiesList = gson.fromJson(inputStreamReader, StudiesList.class);

            inputStreamReader.close();

        } catch (IOException e) {
            Log.e("NJW", e.getMessage());
            e.printStackTrace();
        }


        mAdapter = new VerseListAdapter(getActivity());

        mStudy = mStudiesList.studies.get(mSectionNumber); //for test purposes.


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_verse_list, container, false);


        mListView = (AbsListView) root.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        //progress bar?


        mAdapter.setData(mStudy.getItems());


        return root;
    }

    /**
     * Returns an {@link android.content.Intent} which can be used to share this item's content with other
     * applications.
     *
     * @return Intent to be given to a ShareActionProvider.
     */
    public Intent getShareIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        //TODO: Optionally could set a subject.
        intent.setType("text/plain");
        // Get the string resource and bundle it as an intent extra
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    public class VerseListAdapter extends ArrayAdapter<VerseInfo> {
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

            final VerseInfo item = getItem(position);

            ((TextView) view.findViewById(R.id.text_verse)).setText(item.getRef());

            ((TextView) view.findViewById(R.id.text_note)).setText(item.getNote());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://www.biblegateway.com/passage/?search=" + item.getRef();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    MainActivityFragment.this.getActivity().startActivity(i);
                }
            });

            return view;
        }
    }

    class StudiesList {
        public String title;
        List<Study> studies = new ArrayList<>();


    }


}
