package pl.planta.thread;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pl.planta.app.AppConfiguration;
import pl.planta.app.AppController;
import pl.planta.helper.SQLiteHandler;

/**
 * Created by panko on 19.06.2016.
 */
public class Save {

    private static final String TAG = Save.class.getSimpleName();

    private Context mContext;
    private SQLiteHandler mSQLiteHandler;

    public Save(Context mContext) {
        this.mContext = mContext;
        mSQLiteHandler = new SQLiteHandler(mContext);
    }

    private HashMap<String, String> userUID = mSQLiteHandler.getUserUid();
    final String uid = userUID.get("uid");

    // zapis wungla
    private void saveCoalAmountOnServer(final String uid, final int coalAmount) {
        String tag_string_req = "req_update_coal_amount";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_COAL_AMOUNT_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("coal_amount", String.valueOf(coalAmount));

                return params;
            }
        };
        AppController.getInstance(mContext).addToRequestQueue(stringRequest, tag_string_req);
    }
    // zapis wody
    private void savePipeAmountOnServer(final String uid, final int pipeAmount) {
        String tag_string_req = "req_update_coal_amount";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_PIPE_AMOUNT_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("pipe_amount", String.valueOf(pipeAmount));

                return params;
            }
        };
        AppController.getInstance(mContext).addToRequestQueue(stringRequest, tag_string_req);
    }
    // zapis prundu
    private void saveElecAmountOnServer(final String uid, final int elecAmount) {
        String tag_string_req = "req_update_coal_amount";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfiguration.URL_ELEC_AMOUNT_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Odpowiedz aktualizacji wynikow: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Problem aktualizacji wynikow: " + error.getMessage());
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("uid", uid);
                params.put("elec_amount", String.valueOf(elecAmount));

                return params;
            }
        };
        AppController.getInstance(mContext).addToRequestQueue(stringRequest, tag_string_req);
    }
}
