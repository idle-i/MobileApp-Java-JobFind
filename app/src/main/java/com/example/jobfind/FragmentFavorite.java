package com.example.jobfind;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavorite extends Fragment {

    static List<JobModel> favorite_jobs = new ArrayList<>();
    static RecyclerView favorite_recycler;
    static boolean isActive = false;
    private Context context;

    FragmentFavorite(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        isActive = true;

        favorite_recycler = view.findViewById(R.id.favorite_recycler);
        favorite_recycler.setLayoutManager(new LinearLayoutManager(context));
        favorite_recycler.setAdapter(new JobListAdapter(context, favorite_jobs));

        LinearLayout main_linear_layout = ((Activity) context).findViewById(R.id.main_linear_layout);
        View top_layout = getLayoutInflater().inflate(R.layout.favorite_top_layout, main_linear_layout, false);

        main_linear_layout.removeAllViews();
        main_linear_layout.addView(top_layout);

        EditText search = top_layout.findViewById(R.id.search);
        search.setText("");
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<JobModel> search_result = new ArrayList<>();
                s = s.toString().toLowerCase();

                for (JobModel job : favorite_jobs) {
                    if (job.getTitle().toLowerCase().contains(s) ||
                            job.getDescription().toLowerCase().contains(s) ||
                            job.getCompany().toLowerCase().contains(s)) {
                        search_result.add(job);
                    }
                }

                favorite_recycler.setAdapter(new JobListAdapter(context, search_result));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return view;
    }
}
