package fpt.poly.thanhddph23154.asmapi.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fpt.poly.thanhddph23154.asmapi.R;
import fpt.poly.thanhddph23154.asmapi.model.CarModel;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {

    private Context context;
    private List<CarModel> list;
    private SharedPreferences sharedPreferences;

    public FavoriteAdapter(Context context, List<CarModel> list) {
        this.context = context;
        this.list = list;
        this.sharedPreferences = context.getSharedPreferences("LIKED_PREFS", Context.MODE_PRIVATE);
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_like, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {
        CarModel car = list.get(position);
        holder.tvTen.setText(car.getTen());
        holder.tvXuatXu.setText(car.getXuatxu());

        Glide.with(context)
                .load(car.getImg())
                .placeholder(R.drawable.load)
                .error(R.drawable.loadloi)
                .into(holder.img);

        holder.imgLike.setImageResource(R.drawable.heart1); // trái tim đỏ

        holder.imgLike.setOnClickListener(v -> {
            // Bỏ thích
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(car.get_id(), false);
            editor.apply();

            // Xoá khỏi danh sách hiển thị
            int pos = holder.getAdapterPosition();
            list.remove(pos);
            notifyItemRemoved(pos);

            Toast.makeText(context, "Đã bỏ thích", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        ImageView img, imgLike;
        TextView tvTen, tvXuatXu;

        public FavViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgFav);
            tvTen = itemView.findViewById(R.id.tvFavTen);
            tvXuatXu = itemView.findViewById(R.id.tvFavXuatXu);
            imgLike = itemView.findViewById(R.id.imgFavLike);
        }
    }
}
