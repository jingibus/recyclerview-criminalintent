package com.bignerdranch.android.criminalintent;

import java.io.Serializable;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo implements Serializable { 
    private static final long serialVersionUID = 1L;

    private static final String JSON_FILENAME = "filename";

    private String mFilename;

    /** create a new Photo with a generated filename */
    public Photo() {
        this(UUID.randomUUID().toString() + ".jpg");
    }

    /** create a Photo representing an existing file on disk */
    public Photo(String filename) {
        mFilename = filename;
    }

    /** create a Photo from a JSONObject */
    public Photo(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }
}

