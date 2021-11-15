package com.example.projectpb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectpb.data.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>,MasakanAdapter.OnItemClickCallback{
    private MasakanAdapter mAdapter;
    private EditText mMasakInput;
    private TextView mTitleText;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMasakInput = (EditText)findViewById(R.id.textInputEditText);
        mTitleText = (TextView)findViewById(R.id.title);
        mAdapter = new MasakanAdapter();
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        getResult();
        mAdapter.setOnItemClickCallback(this::onItemClicked);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    public void searchMasakan(View view) {
        // Get the search string from the input field.
        String queryString = mMasakInput.getText().toString();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

        }else {
            if (queryString.length() == 0) {
                mTitleText.setText(R.string.no_search_term);
            } else {
                mTitleText.setText(R.string.no_network);
            }
        }
    }

    private void getResult() {
        final ArrayList<Result> listResult = new ArrayList<>();
        String baseURL = "https://masak-apa-tomorisakura.vercel.app/api/recipes/1";
        AndroidNetworking.get(baseURL).setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response", response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject result = jsonArray.getJSONObject(i);
                        Result itemResult = new Result();
                        itemResult.setTitle(result.getString("title"));
                        itemResult.setThumb(result.getString("thumb"));
                        itemResult.setKey(result.getString("key"));
                        listResult.add(itemResult);
                    }
                    mAdapter.setData(listResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.d("onFailure", anError.getMessage());
            }
        });
    }

    @Override
    public void onItemClicked(Result data) {
            String key = data.getKey();
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("key", key);
            startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if (args != null) {
            queryString = args.getString("queryString");
        }

        return new MasakLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            int i = 0;
            String title = null;
            String authors = null;

            while (i < itemsArray.length() &&
                    (authors == null && title == null)) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            if (title != null && authors != null) {
                mTitleText.setText(title);
            }else {
                mTitleText.setText(R.string.no_search_term);
            }

        } catch (JSONException e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mTitleText.setText(R.string.no_search_term);
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}


