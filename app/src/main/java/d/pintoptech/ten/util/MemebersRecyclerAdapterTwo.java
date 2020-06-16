package d.pintoptech.ten.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import d.pintoptech.ten.R;
import d.pintoptech.ten.ui.ShowBeneficiary;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 4/23/2019.
 */

public class MemebersRecyclerAdapterTwo extends RecyclerView.Adapter<MemebersRecyclerAdapterTwo.ViewHolder> {

    private Context context;
    private List<MembersRepo> arrayList;
    private Activity activity;

    public MemebersRecyclerAdapterTwo(Context context, List<MembersRepo> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_two,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final MembersRepo repo = arrayList.get(position);

        holder.name.setText(repo.getName());
        holder.did.setText(repo.getDid());
        String imageUri = "https://www.theempowermentnetwork.ng/"+repo.getAvatar();
        Picasso.with(context).load(imageUri).placeholder(R.drawable.unknown_person).error(R.drawable.unknown_person).into(holder.avatar);
        holder.MenuOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.MenuOptions);
                popup.inflate(R.menu.beneficiaries_menu_add);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_view:
                                final String id = repo.getDid();
                                FragmentManager fm =  ((AppCompatActivity) context).getSupportFragmentManager();
                                Bundle arguments = new Bundle();
                                arguments.putString("id", id);
                                ShowBeneficiary myFragment = new ShowBeneficiary();
                                myFragment.setArguments(arguments);
                                fm.beginTransaction().replace(R.id.content_frame, myFragment).addToBackStack("dashboard").commit();
                                return true;
                            case R.id.nav_call:
                                if (Build.VERSION.SDK_INT > 22) {

                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 101);

                                    }
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + repo.getPhone()));
                                    activity.startActivity(callIntent);
                                } else {

                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + repo.getPhone()));
                                    activity.startActivity(callIntent);
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, did, MenuOptions;
        private CircleImageView avatar;

        private ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            avatar = itemView.findViewById(R.id.avatar);
            did = itemView.findViewById(R.id.id);
            MenuOptions = itemView.findViewById(R.id.options);

        }
    }
}