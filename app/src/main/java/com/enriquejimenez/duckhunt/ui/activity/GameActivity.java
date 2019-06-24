package com.enriquejimenez.duckhunt.ui.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.enriquejimenez.duckhunt.R;
import com.enriquejimenez.duckhunt.RankingActivity;
import com.enriquejimenez.duckhunt.utils.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView countTextView;
    TextView userNameTextView;
    TextView timerNameTextView;
    TextView finalTextView;
    ImageView duckImageView;
    Button retryButton;
    Button exitButton;
    ConstraintLayout retryLayout;
    TextView gameOverTextView;


    private int counter = 0;
    private int timer;
    private int widthDisplay;
    private int heightDisplay;
    private Random random;
    private String userName;
    private boolean gameOver;
    private String id;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        db = FirebaseFirestore.getInstance();
        initViews();
        setFontType();
        getExtras();
        setClickEvents();
        initScreen();
        moveDuck();
        initCountTimer();
    }

    private void initViews() {
        countTextView = findViewById(R.id.textViewCounter);
        userNameTextView = findViewById(R.id.textViewDucks);
        timerNameTextView = findViewById(R.id.textViewTimer);
        finalTextView = findViewById(R.id.textViewFinal);
        duckImageView = findViewById(R.id.imageViewDuck);
        retryButton = findViewById(R.id.buttonRetry);
        exitButton = findViewById(R.id.buttonExit);
        retryLayout = findViewById(R.id.layoutRetry);
        gameOverTextView = findViewById(R.id.textViewGameOver);
    }

    private void setFontType() {
        //Change font type
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        countTextView.setTypeface(typeface);
        userNameTextView.setTypeface(typeface);
        timerNameTextView.setTypeface(typeface);
        finalTextView.setTypeface(typeface);
        retryButton.setTypeface(typeface);
        exitButton.setTypeface(typeface);
        gameOverTextView.setTypeface(typeface);
    }

    private void getExtras() {
        Bundle args = getIntent().getExtras();
        id = args.getString(Constants.USER_ID);
        userName = args.getString(Constants.EXTRA_USER_NAME);
        timer = args.getInt(Constants.EXTRA_TIMER);
        userNameTextView.setText(userName);
    }

    private void setClickEvents() {
        duckImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {
                    counter++;
                    countTextView.setText(String.valueOf(counter));
                    duckImageView.setImageResource(R.drawable.duck_clicked);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            duckImageView.setImageResource(R.drawable.duck);
                            moveDuck();
                        }
                    }, 250);
                }
            }
        });
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               retryGame();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });
    }

    private void initScreen() {
        //Get size display
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthDisplay = size.x;
        heightDisplay = size.y;
        random = new Random();
    }

    private void moveDuck() {

        int min = 0;
        int maxX = widthDisplay - duckImageView.getWidth();
        int maxY = heightDisplay - duckImageView.getHeight();

        //Generamos 2 num random uno para X otro para Y
        int randomX = random.nextInt(((maxX - min)+1)+min);
        int randomY = random.nextInt(((maxY - min)+1)+min);

        //Usamos los num random para mover el pato a esa posición
        duckImageView.setX(randomX);
        duckImageView.setY(randomY);

    }

    private void initCountTimer() {
        new CountDownTimer(timer*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerNameTextView.setText((millisUntilFinished/1000) + "s");
            }
            @Override
            public void onFinish() {
                Log.e("TIMER", "0" + "s");
                endGame();
            }
        }.start();
    }

    private void endGame() {
        saveResultFirestore();
        duckImageView.setVisibility(View.GONE);
        retryLayout.setVisibility(View.VISIBLE);
        gameOver = true;
        finalTextView.setText("ENHORABUENA\n\n " + userName + "\n\nTU PUNTUACIÓN HA SIDO DE:\n\n" + String.valueOf(counter) + " patos");
    }

    private void retryGame(){
        duckImageView.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
        gameOver = false;
        counter = 0;
        moveDuck();
        initCountTimer();
    }
    private void saveResultFirestore() {
        db.collection(Constants.DB_USERS).document(id).update("ducks", new Long(counter));
    }

}
