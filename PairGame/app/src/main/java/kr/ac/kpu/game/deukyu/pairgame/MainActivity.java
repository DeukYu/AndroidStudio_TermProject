package kr.ac.kpu.game.deukyu.pairgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int[] BUTTON_IDS = {
            R.id.b_00, R.id.b_01, R.id.b_02, R.id.b_03,
            R.id.b_10, R.id.b_11, R.id.b_12, R.id.b_13,
            R.id.b_20, R.id.b_21, R.id.b_22, R.id.b_23,
            R.id.b_30, R.id.b_31, R.id.b_32, R.id.b_33
    };

    private static final int[] IMAGE_RES_IDS = {
            R.mipmap.card_2c, R.mipmap.card_3d, R.mipmap.card_4h, R.mipmap.card_5s,
            R.mipmap.card_jc, R.mipmap.card_qh, R.mipmap.card_kd, R.mipmap.card_as,
    };
    private ImageButton lastButton;
    private int flips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartGame();
    }

    private void StartGame() {
        int[] buttonIds = shuffleButtonIds();

        for(int i = 0; i < BUTTON_IDS.length; ++i){
            ImageButton btn = findViewById(buttonIds[i]);
            int resId = IMAGE_RES_IDS[i / 2];
            btn.setTag(resId);
            btn.setImageResource(R.mipmap.card_blue_back);
            btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            btn.setEnabled(true);
        }

        lastButton = null;
        flips = 0;
        showScore();
    }

    private void showScore() {
        TextView textView = findViewById(R.id.scoreTextView);
        Resources res = getResources();
        String fmt = res.getString(R.string.flips_fmt);
        String text = String.format(fmt, flips);
        textView.setText(text);
    }

    private int[] shuffleButtonIds() {
        int[] buttonIds = Arrays.copyOf(BUTTON_IDS, BUTTON_IDS.length);
        Random rand = new Random();
        for(int i = 0; i < BUTTON_IDS.length; ++i){
            int r = rand.nextInt(buttonIds.length);
            int temp = buttonIds[i];
            buttonIds[i] = buttonIds[r];
            buttonIds[r] = temp;
        }
        return buttonIds;
    }

    public void onBtnItem(View view) {
        Log.d(TAG, "Button ID = " + view.getId());
        Log.d(TAG, "Button Index = " + (view.getId() - R.id.b_00));
        ImageButton btn = (ImageButton) view;
        int resId = (int) btn.getTag();
        btn.setImageResource(resId);
        btn.setEnabled(false);

        if(lastButton == null){
            lastButton = btn;
            return;
        }

        if((int)lastButton.getTag() == (int)btn.getTag()){
            lastButton = null;
            return;
        }

        lastButton.setImageResource(R.mipmap.card_blue_back);
        lastButton.setEnabled(true);
        lastButton = btn;

        ++flips;
        showScore();
    }

    public void onBtnRestart(View view) {
        Log.d(TAG, "onBtnRestart()");
        StartGame();
        new AlertDialog.Builder(this)
                .setTitle(R.string.restartTitle)
                .setMessage(R.string.restartMessage)
                .setNegativeButton(R.string.restartNo, null)
                .setPositiveButton(R.string.restartYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartGame();
                    }
                })
                .create()
                .show();
    }
}
