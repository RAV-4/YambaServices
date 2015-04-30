package com.example.rafael.yamba;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;


public class StatusFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = StatusFragment.class.getSimpleName();
    private EditText editStatus;
    private Button buttonTweet;
    private TextView textCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, null, false);

        editStatus = (EditText) view.findViewById(R.id.editStatus);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        textCount = (TextView) view.findViewById(R.id.textCount);

        buttonTweet.setOnClickListener(this);

        buttonTweet.setEnabled(false);

        editStatus.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int count = 140 - editStatus.length();
                        if (count < 140) {
                            buttonTweet.setEnabled(true);
                            buttonTweet.setTextColor(Color.parseColor("#9d5e9d"));
                            editStatus.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            buttonTweet.setEnabled(false);
                            buttonTweet.setTextColor(Color.parseColor("#fefffe"));
                            editStatus.setTextColor(Color.parseColor("#efefef"));
                        }
                        textCount.setText(Integer.toString(count));
                        if (count < 10) {
                            textCount.setTextColor(Color.RED);
                        } else {
                            textCount.setTextColor(Color.parseColor("#fefffe"));
                        }

                    }
                }

        );

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String status = editStatus.getText().toString();
        Log.d(TAG, "onClicked with status: " + status);

        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editStatus.getWindowToken(), 0);

        editStatus.setText("");
        buttonTweet.setEnabled(false);
        buttonTweet.setTextColor(Color.parseColor("#fefffe"));

        new PostTask().execute(status);
    }

    private final class PostTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Posting",
                    "Please wait...");
            progress.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getActivity()); //
                String username = prefs.getString("username", ""); //
                String password = prefs.getString("password", "");

                if (TextUtils.isEmpty(username) ||
                        TextUtils.isEmpty(password)) {
                    getActivity().startActivity(
                            new Intent(getActivity(), SettingsActivity.class));
                    return "Please update your username and password";
                }
                YambaClient yambaCloud = new YambaClient(username, password);
                yambaCloud.postStatus(params[0]);
                return "Succesfully posted";
            }

                catch (Exception e) {
                e.printStackTrace();
                return "Failed to post to yamba service";
                }

        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            super.onPostExecute(result);
            Toast.makeText(StatusFragment.this.getActivity(), result, Toast.LENGTH_LONG).show();
        }

    }
}





