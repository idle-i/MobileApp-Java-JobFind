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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentJobList extends Fragment {
    static int emp_active = 0;
    private static List<JobModel> job_list = new ArrayList<>();
    private Context context;
    private FragmentManager fragmentManager;
    private Thread thread;
    private String[] emp_types = {
            "None",
            "Full Time",
            "Part Time"
    };

    FragmentJobList(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_job_list, container, false);

        LinearLayout main_linear_layout = ((Activity) context).findViewById(R.id.main_linear_layout);
        main_linear_layout.removeAllViews();
        View top_layout = getLayoutInflater().inflate(R.layout.main_top_layout, main_linear_layout, false);
        main_linear_layout.addView(top_layout);

        final EditText search = top_layout.findViewById(R.id.search);
        search.setText(MainActivity.search_text);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.search_text = search.getText().toString();

                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                }
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getJobs(view, search.getText().toString(), thread.getId(), emp_active);
                    }
                });
                thread.start();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageButton filter = top_layout.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("Employment type");
                builder.setSingleChoiceItems(emp_types, emp_active, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (thread != null && thread.isAlive()) {
                            thread.interrupt();
                        }
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                emp_active = which;
                                getJobs(view, search.getText().toString(), thread.getId(), which);
                                dialog.dismiss();
                            }
                        });
                        thread.start();
                    }
                });
                builder.show();
            }
        });

        if (job_list.isEmpty()) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    getJobs(view, search.getText().toString(), thread.getId(), emp_active);
                }
            });
            thread.start();
        } else {
            view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            RecyclerView job_list_recycler = view.findViewById(R.id.job_list_recycler);
            job_list_recycler.setLayoutManager(new LinearLayoutManager(context));
            job_list_recycler.setAdapter(new JobListAdapter(context, job_list));
        }

        return view;
    }

    private void getJobs(final View view, String search, final long id, int emp) {
        synchronized (this) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecyclerView job_list_recycler = view.findViewById(R.id.job_list_recycler);
                    job_list_recycler.setAdapter(new JobListAdapter(context, new ArrayList<JobModel>()));
                    view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                }
            });

            OkHttpClient innerClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://jobs.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(innerClient)
                    .build();

            JsonApi jsonApi = retrofit.create(JsonApi.class);

            Call<List<JobModel>> call;
            if (emp == 0) {
                call = jsonApi.getJobList(search, "");
            } else {
                call = jsonApi.getJobList(search, emp_types[emp]);
            }
            call.enqueue(new Callback<List<JobModel>>() {
                @Override
                public void onResponse(Call<List<JobModel>> call, Response<List<JobModel>> response) {
                    if (thread != null && thread.getId() == id && response.body() != null) {
                        job_list = new ArrayList<>();

                        if (response.body().isEmpty()) {
                            fragmentManager.beginTransaction()
                                    .replace(((Activity) context).findViewById(R.id.main).getId(), new FragmentEmpty(context, fragmentManager))
                                    .commit();
                        } else {
                            job_list.addAll(response.body());

                            RecyclerView job_list_recycler = view.findViewById(R.id.job_list_recycler);
                            job_list_recycler.removeAllViews();
                            job_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                            job_list_recycler.setAdapter(new JobListAdapter(context, job_list));
                        }

                        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<JobModel>> call, Throwable t) {
                    fragmentManager.beginTransaction()
                            .replace(((Activity) context).findViewById(R.id.main).getId(), new FragmentError(context, fragmentManager))
                            .commit();

                    t.printStackTrace();
                }
            });
        }
    }
}
