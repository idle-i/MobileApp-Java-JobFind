package com.example.jobfind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class InfoActivity extends AppCompatActivity {

    public static JobModel job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        findViewById(R.id.info_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(this)
                .load(job.getCompanyLogo())
                .into((ImageView) findViewById(R.id.info_image));

        TextView info_title = findViewById(R.id.info_title);
        info_title.setText(job.getTitle());

        TextView info_location_title = findViewById(R.id.info_location_title);
        info_location_title.setText(job.getLocation());

        TextView info_text = findViewById(R.id.info_text);
        info_text.setText(Html.fromHtml(job.getDescription()));

        TextView info_location = findViewById(R.id.info_location);
        info_location.setText(String.format("Location: %s", job.getLocation()));

        TextView info_date = findViewById(R.id.info_date);
        info_date.setText(String.format("Created date: %s", job.getCreatedAt()));

        final ImageButton info_favorite = findViewById(R.id.info_favorite);
        if (FragmentFavorite.favorite_jobs.contains(job)) {
            info_favorite.setImageResource(R.drawable.ic_heart_white);
        }

        info_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FragmentFavorite.favorite_jobs.contains(job)) {
                    FragmentFavorite.favorite_jobs.remove(job);

                    Toast.makeText(getApplicationContext(), "Removed from favorite list", Toast.LENGTH_SHORT).show();
                    info_favorite.setImageResource(R.drawable.ic_heart);

                    try {
                        if (MainActivity.fragmentManager.getFragments().get(1).getTag().equals("FragmentFavorite")) {
                            finishAffinity();

                            Intent i = new Intent(InfoActivity.this, MainActivity.class);
                            i.putExtra("startFavoriteList", 1);
                            startActivity(i);
                        }
                    } catch (Exception ignored) {
                    }
                } else {
                    FragmentFavorite.favorite_jobs.add(job);

                    Toast.makeText(getApplicationContext(), "Added to favorite list", Toast.LENGTH_SHORT).show();
                    info_favorite.setImageResource(R.drawable.ic_heart_white);
                }
            }
        });

        findViewById(R.id.info_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(job.getUrl())));
            }
        });
    }
}
