package com.jagger.alertobarangay;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ReportAdapter extends FirebaseRecyclerAdapter<Report,ReportAdapter.ReportViewHolder>  {
    //private int selectedPos = RecyclerView.NO_POSITION;
    Context mContext;
    public ReportAdapter(@NonNull FirebaseRecyclerOptions<Report> options, Context context)
    {
        super(options);
        mContext=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position, @NonNull Report model) {
       // holder.itemView.setSelected(selectedPos == position);
        holder.key = model.id;
        holder.tvType.setText(model.type);
        holder.tvDate.setText(model.date);
        holder.comments.setText(model.comments);
        holder.tvStatus.setText(model.status);
        holder.tvId.setText(model.id);
        if(model.type.equals("Fire Emergency"))
        {
            holder.imgIncidentType.setImageResource(R.drawable.ic_fire);
        }
        else if(model.type.equals("Health Emergency"))
        {
            holder.imgIncidentType.setImageResource(R.drawable.ic_ambulance);
        }
        else if(model.type.equals("Crime Incident"))
        {
            holder.imgIncidentType.setImageResource(R.drawable.ic_thief);
        }
        else
        {
            holder.imgIncidentType.setImageResource(R.drawable.ic_info);
        }
    }

    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_view, parent, false);
        return new ReportAdapter.ReportViewHolder(view);
    }




    class ReportViewHolder  extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        String key;
        TextView comments,tvType,tvDate,tvStatus,tvId,maxid;
        ImageView imgIncidentType;
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.tvIncidentType);
            imgIncidentType = itemView.findViewById(R.id.imgIncident);
            comments = itemView.findViewById(R.id.tvComments);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvId = itemView.findViewById(R.id.tvID);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {

            TextView tvId= v.findViewById(R.id.tvID);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(true);
            builder.setTitle("Delete Report");
            builder.setMessage("Are you sure you want to delete this report?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Report").child(tvId.getText().toString()).removeValue();

                        }
                    });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
    }

}
