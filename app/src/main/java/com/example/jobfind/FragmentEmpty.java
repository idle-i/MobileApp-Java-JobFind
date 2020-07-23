package com.example.jobfind;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragmentEmpty extends Fragment {
    private Context context;
    private FragmentManager fragmentManager;

    private String[] emp_types = {
            "None",
            "Full Time",
            "Part Time"
    };

    FragmentEmpty(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);

        final EditText search = ((Activity) context).findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fragmentManager.beginTransaction()
                        .replace(((Activity) context).findViewById(R.id.main).getId(), new FragmentJobList(context, fragmentManager))
                        .commit();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton filter = ((Activity) context).findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("Employment type");
                builder.setSingleChoiceItems(emp_types, FragmentJobList.emp_active, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        fragmentManager.beginTransaction()
                                .replace(((Activity) context).findViewById(R.id.main).getId(), new FragmentJobList(context, fragmentManager))
                                .commit();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        return view;
    }
}
