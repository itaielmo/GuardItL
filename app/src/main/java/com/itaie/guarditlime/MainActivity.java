package com.itaie.guarditlime;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    //private ToggleButton mLockToggleButton;
    private ImageView mBackgroundImage;
    private ProgressBar mLoadingProgress;
    private View buttonWrapper;
    private boolean lock = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RequestQueue queue = Volley.newRequestQueue(this);
        mBackgroundImage = findViewById(R.id.background_image);
        mLoadingProgress = findViewById(R.id.loading_progress);
        buttonWrapper = findViewById(R.id.button_wrapper);
        buttonWrapper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                // Instantiate the RequestQueue.
                lock = !lock;
                buttonWrapper.setClickable(false);
                showLoadingProgress(true);
                String url = lock ? "http://192.168.43.194/close" : "http://192.168.43.194/open";
                sendHttpCall(url,queue);
            }
        });
    }

    private void showLoadingProgress(boolean show) {
        if (show) {
            mBackgroundImage.setAlpha(150);
            mLoadingProgress.setVisibility(View.VISIBLE);
            mLoadingProgress.setAlpha(0.0f);
            mLoadingProgress.animate().alpha(1.0f).setDuration(200).setListener(null).start();
        } else {
            mLoadingProgress.animate().alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBackgroundImage.setAlpha(255);
                    mLoadingProgress.setVisibility(View.GONE);
                }
            }).start();

        }
    }

    private void sendHttpCall(String url, final RequestQueue queue) {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        buttonWrapper.setClickable(true);
                        mBackgroundImage.setBackgroundResource(lock ? R.drawable.lock_activity_lime : R.drawable.unlock_activity_lime);
                        showLoadingProgress(false);
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                buttonWrapper.setClickable(true);
                showLoadingProgress(false);
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
