package d.pintoptech.ten.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import d.pintoptech.ten.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    Context context;
    List<DashboardRepo> arrayList;

    public RecyclerviewAdapter(Context context, List<DashboardRepo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        DashboardRepo repo = arrayList.get(position);

        holder.name.setText(repo.getDname());
        holder.action.setText(repo.getDaction());
        holder.time.setText(repo.getDtime());
        holder.id.setText(repo.getDid());
        String imageUri = repo.getDimage();
        if(repo.getStatus().isEmpty() || repo.getStatus().equals("not viewed")){
            holder.CONTAIN.setBackground(context.getResources().getDrawable(R.drawable.task_overview_bg));
        }else {
            holder.CONTAIN.setBackground(context.getResources().getDrawable(R.drawable.task_overview_bg_viewed));
        }
        if(repo.getDaction().isEmpty()){
            holder.action.setVisibility(View.GONE);
        }
        Picasso.with(context).load(imageUri).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, action, time, id;
        public CircleImageView image;
        RelativeLayout CONTAIN;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            action = itemView.findViewById(R.id.action);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);
            id = itemView.findViewById(R.id.did);
            CONTAIN = itemView.findViewById(R.id.contain);
        }
    }
}