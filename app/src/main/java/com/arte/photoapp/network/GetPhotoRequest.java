package com.arte.photoapp.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arte.photoapp.model.Photo;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPhotoRequest {

    private static final String GET_PHOTO_URL = "http://jsonplaceholder.typicode.com/photos/";

    public interface Callbacks {
        void onGetPhotoSuccess(Photo photo);

        void onGetPhotoError();
    }

    private Context mContext;
    private Callbacks mCallbacks;
    private String mId;

    public GetPhotoRequest(Context context, Callbacks callbacks, String id) {
        mContext = context;
        mCallbacks = callbacks;
        mId = id;
    }

    public void execute() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl(mId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Photo photo = new Photo();
                try {
                    photo.setId(String.valueOf(response.getInt("id")));
                    photo.setTitle(response.getString("title"));
                    photo.setUrl(response.getString("url"));
                    photo.setThumbnailUrl(response.getString("thumbnailUrl"));
                } catch (JSONException e) {
                    Log.e(GetPhotoListRequest.class.getSimpleName(), "Error deserializando JSON", e);
                    mCallbacks.onGetPhotoError();
                    return;
                }
                mCallbacks.onGetPhotoSuccess(photo);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mCallbacks.onGetPhotoError();
            }
        });

        RequestQueueManager.getInstance(mContext).addToRequestQueue(request);
    }

    private String getUrl(String id) {
        return GET_PHOTO_URL + id;
    }
}
