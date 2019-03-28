package com.android.loanassistant.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.loanassistant.R;

public class CustomProgressDialog {
    private MediaPlayer mediaPlayer;
    public ProgressDialog dialog;
    public Context context;
    private InputMethodManager imm;

    public CustomProgressDialog(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    public void showDialog(String msg) {
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.cancel();
                    Toast.makeText(context, "Internet not available!", Toast.LENGTH_LONG).show();
                }
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 5000);
    }

    public void showDialog(String msg, int time) {
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.cancel();
                    Toast.makeText(context, "Internet not available!", Toast.LENGTH_LONG).show();
                }
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, time * 1000);
    }

    public void stopDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public void stopDialog(int count) {
        if (dialog.isShowing())
            dialog.dismiss();

        if (count < 1) {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("ProductionTracker")
                    .setMessage("No items found!!")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            dialog1.dismiss();
                        }
                    }).show();
        }
    }

   /* public void beep() {
        mediaPlayer = MediaPlayer.create(context, R.raw.beep); // instantiate
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); // set as media sound
        mediaPlayer.setLooping(false); // disable repetition
        mediaPlayer.start(); // plays the mp3 sound
    }*/

    public void hideKeyboard(View view) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    public void showKeyboard(View view) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}