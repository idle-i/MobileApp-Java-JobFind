package com.example.jobfind;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {
    private Context context;
    private List<JobModel> job_list;

    JobListAdapter(Context context, List<JobModel> job_list) {
        this.context = context;
        this.job_list = job_list;
    }

    @NonNull
    @Override
    public JobListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobListAdapter.ViewHolder holder, int position) {
        final JobModel job = job_list.get(position);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoActivity.job = job;
                context.startActivity(new Intent(context, InfoActivity.class));
            }
        });

        Glide.with(context)
                .load(job.getCompanyLogo())
                .into(holder.cardImage);

        holder.cardTitle.setText(job.getTitle());
        holder.cardLocation.setText(job.getLocation());
        holder.cardText.setText(Html.fromHtml(job.getDescription()));
    }

    @Override
    public int getItemCount() {
        return job_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        ImageView cardImage;
        TextView cardTitle;
        TextView cardLocation;
        TextView cardText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
            cardImage = itemView.findViewById(R.id.cardImage);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardLocation = itemView.findViewById(R.id.cardLocation);
            cardText = itemView.findViewById(R.id.cardText);
        }
    }
}
