package stackexchange.tlab.com.tlaboverflow.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import stackexchange.tlab.com.tlaboverflow.MainActivity;
import stackexchange.tlab.com.tlaboverflow.R;
import stackexchange.tlab.com.tlaboverflow.model.Items;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    List<Items> itemsList;

    public ListItemAdapter(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ListItemAdapter.ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {

        final Context context = holder.itemView.getContext();
        holder.txtTitle.setText(itemsList.get(position).getTitle());

        String dataku = itemsList.get(position).getCreation_date();
        long timestamp = Long.parseLong(dataku);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();

        holder.txtCreationDate.setText("Created at "+ date);
        holder.txtDisplayName.setText(itemsList.get(position).getOwner().getDisplay_name());
        final String link = itemsList.get(position).getLink();
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    context.startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request."
                            + " Please install a Web Browser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        String photoUrl = itemsList.get(position).getOwner().getProfile_image();
        Glide.with(context).load(photoUrl)
                .thumbnail(0.5f)
                .into(holder.imgProfil);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgProfil)
        CircleImageView imgProfil;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtCreationDate)
        TextView txtCreationDate;
        @BindView(R.id.txtDisplayName)
        TextView txtDisplayName;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
