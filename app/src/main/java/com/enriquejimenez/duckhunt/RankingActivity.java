package com.enriquejimenez.duckhunt;

import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RankingActivity extends AppCompatActivity {

    private TextView rankingEditText;
    private TextView nameEditText;
    private TextView ducksEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new UserRankingFragment()).commit();

        rankingEditText = findViewById(R.id.textViewRanking);
        nameEditText = findViewById(R.id.textViewName);
        ducksEditText = findViewById(R.id.textViewDucks);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        rankingEditText.setTypeface(typeface);
        nameEditText.setTypeface(typeface);
        ducksEditText.setTypeface(typeface);

    }

}
