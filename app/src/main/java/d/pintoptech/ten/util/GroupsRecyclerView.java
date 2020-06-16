package d.pintoptech.ten.util;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import d.pintoptech.ten.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class GroupsRecyclerView extends RecyclerView.Adapter<GroupsRecyclerView.ViewHolder> {

    Context context;
    List<GroupsRepo> arrayList;

    public GroupsRecyclerView(Context context, List<GroupsRepo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groups_list,parent,false);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        GroupsRepo repo = arrayList.get(position);

        holder.name.setText( repo.getGroupName());
        holder.desc.setText(repo.getDesc());
        holder.Did.setText(repo.getId());
        holder.Counts.setText(repo.getCounts()+" members");
        String imageUri = repo.getThumb();
        if(!imageUri.equals("")){
            try {
                String[] splitArray = imageUri.split("\\s*,\\s*");
                for(int i = 0;i <= splitArray.length-1;i++){
                    if(!splitArray[i].isEmpty()) {
                        if(!splitArray[i].isEmpty()){
                            if(i == 0){
                                Picasso.with(context).load("https://www.theempowermentnetwork.ng/"+splitArray[i]).into(holder.thumb1);
                            }else if(i == 1){
                                Picasso.with(context).load("https://www.theempowermentnetwork.ng/"+splitArray[i]).into(holder.thumb2);
                            }else if(i == 2){
                                Picasso.with(context).load("https://www.theempowermentnetwork.ng/"+splitArray[i]).into(holder.thumb3);
                            }else if(i == 3){
                                Picasso.with(context).load("https://www.theempowermentnetwork.ng/"+splitArray[i]).into(holder.thumb4);
                            }
                        }
                    }
                }

            } catch (PatternSyntaxException ex) {
                ex.printStackTrace();
            } catch(ArrayIndexOutOfBoundsException ax){
                ax.printStackTrace();
            }
        }else {
            holder.thumb1.setImageDrawable(context.getResources().getDrawable(R.drawable.groups));
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, desc, Did, Counts;
        public CircleImageView thumb1, thumb2, thumb3, thumb4;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.dname);
            thumb1 = itemView.findViewById(R.id.dthumb1);
            thumb2 = itemView.findViewById(R.id.dthumb2);
            thumb3 = itemView.findViewById(R.id.dthumb3);
            thumb4 = itemView.findViewById(R.id.dthumb4);
            desc = itemView.findViewById(R.id.ddesc);
            Did = itemView.findViewById(R.id.did);
            Counts = itemView.findViewById(R.id.counts);
        }
    }
}