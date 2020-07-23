package com.example.jobfind;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentError extends Fragment {
    private Context context;
    private FragmentManager fragmentManager;

    FragmentError(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);

        LinearLayout main_linear_layout = ((Activity) context).findViewById(R.id.main_linear_layout);
        main_linear_layout.removeAllViews();

        view.findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(((Activity) context).findViewById(R.id.main).getId(), new FragmentJobList(context, fragmentManager))
                        .commit();
            }
        });

        return view;
    }
}
