package d.pintoptech.ten.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import d.pintoptech.ten.R;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    private Context context;
    private List<WalletRepo> arrayList;

    public WalletAdapter(Context context, List<WalletRepo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        WalletRepo repo = arrayList.get(position);

        holder.date.setText(repo.getDday()+" "+repo.getDmonth()+", "+repo.getDyear()+"  - ");
        holder.desc.setText(repo.getDdesc());
        holder.time.setText(repo.getDtime());
        String type = repo.getDtype();
        if(type.equals("debit")){
            holder.amount.setText("-₦"+repo.getDamount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.colorDanger));
        }else {
            holder.amount.setText("+₦"+repo.getDamount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date,  desc,time, amount;

        private ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.desc);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.amount);

        }
    }
}