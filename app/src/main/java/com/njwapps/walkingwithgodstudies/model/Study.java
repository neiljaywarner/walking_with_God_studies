package com.njwapps.walkingwithgodstudies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by warner on 7/30/15.
 */
public class Study {
    public String title;
  //  public String versesTextUrl; //TODO: Later allow for user to set versions.
    private List<VerseInfo> dataset = new ArrayList<>();

    public List<VerseInfo> getItems() {
        return dataset;
    }

    public String getTitle() {
        return title;
    }

    public String getShareText() {
        String shareText = getTitle() + "\n";
        for (int i = 0; i < getItems().size(); i++) {
            VerseInfo verseInfo = getItems().get(i);
            shareText += "\n" + verseInfo.getRef();
            shareText += "\n" + verseInfo.getNote() + "\n";
        }
        return shareText;
    }
}
