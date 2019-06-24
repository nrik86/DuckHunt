package com.enriquejimenez.duckhunt.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.enriquejimenez.duckhunt.R;
import com.enriquejimenez.duckhunt.RankingActivity;
import com.enriquejimenez.duckhunt.ui.fragment.TimerListDialogFragment;
import com.enriquejimenez.duckhunt.models.User;
import com.enriquejimenez.duckhunt.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity implements TimerListDialogFragment.Listener {

    private EditText userNameEditText;
    private Button changeTimerButton;
    private Button startButton;
    private Button goRankingButton;
    private String userName;
    private int timer = 60;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Instance DB FIRESTORE
        db = FirebaseFirestore.getInstance();

        startButton = findViewById(R.id.buttonStart);
        changeTimerButton = findViewById(R.id.buttonTimer);
        goRankingButton = findViewById(R.id.buttonGoRanking);
        userNameEditText = findViewById(R.id.editTextUserName);


        //Change font type
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        startButton.setTypeface(typeface);
        userNameEditText.setTypeface(typeface);
        changeTimerButton.setTypeface(typeface);
        goRankingButton.setTypeface(typeface);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName =userNameEditText.getText().toString();
                if(userName.isEmpty()){
                    userNameEditText.setError("Es necesario un nombre de usuario");
                }else if(userName.length() < 3){
                    userNameEditText.setError("El nombre de usuario tiene que tener más de 2 caracteres");
                }else{
                    lookDB();
                }
            }
        });

        changeTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogTimer();
            }
        });

        goRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RankingActivity.class));
            }
        });
    }

    private void lookDB() {

        db.collection("users").whereEqualTo("name", userName)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    userNameEditText.setError("El nombre de usuario no está disponible");
                }else{
                    insertDB();
                }
            }
        });
    }



    private void insertDB() {

        db.collection(Constants.DB_USERS)
                .add(new User(userName, new Long(0)))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                        intent.putExtra(Constants.EXTRA_USER_NAME, userName);
                        intent.putExtra(Constants.EXTRA_TIMER, timer);
                        intent.putExtra(Constants.USER_ID, documentReference.getId());
                        startActivity(intent);
                    }
                });
    }

    private void openDialogTimer() {
        TimerListDialogFragment dialogTimer = TimerListDialogFragment.newInstance();
        dialogTimer.show(getSupportFragmentManager(), "TimerListDialogFragment");
    }
    @Override
    public void onTimerClicked(int timer) {
        this.timer = timer;
    }
}
