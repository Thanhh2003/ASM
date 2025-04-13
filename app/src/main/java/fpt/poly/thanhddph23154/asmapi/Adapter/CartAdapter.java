package fpt.poly.thanhddph23154.asmapi.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import fpt.poly.thanhddph23154.asmapi.GioHang;
import fpt.poly.thanhddph23154.asmapi.R;
import fpt.poly.thanhddph23154.asmapi.model.CarModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CarModel> cartItems;
    private SharedPreferences sharedPreferences;

    public CartAdapter(Context context, List<CarModel> cartItems, SharedPreferences sharedPreferences) {
        this.context = context;
        this.cartItems = cartItems;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giohang, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CarModel item = cartItems.get(position);

        holder.tvName.setText("Tên: " + item.getTen());
        holder.tvOrigin.setText("Giá: " + item.getGia());
        holder.tvQuantity.setText("Số lượng: " + item.getSoluong());

        Glide.with(context).load(item.getImg())
                .placeholder(R.drawable.load)
                .error(R.drawable.loadloi)
                .into(holder.imgProduct);

        holder.btnPlus.setOnClickListener(v -> {
            item.setSoluong(item.getSoluong() + 1);
            updateCart(item);
            notifyItemChanged(holder.getAdapterPosition());
            if (context instanceof GioHang) {
                ((GioHang) context).updateTotalPrice();
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getSoluong() > 1) {
                item.setSoluong(item.getSoluong() - 1);
                updateCart(item);
                notifyItemChanged(holder.getAdapterPosition());
                if (context instanceof GioHang) {
                    ((GioHang) context).updateTotalPrice();
                }
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(item.get_id());
            editor.apply();

            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());

            if (context instanceof GioHang) {
                ((GioHang) context).updateTotalPrice();
            }
        });
    }

    private void updateCart(CarModel item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String carId = item.get_id();
        String json = new Gson().toJson(item);
        editor.putString(carId, json);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnDelete;
        TextView tvName, tvOrigin, tvQuantity;
        Button btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCart);
            tvName = itemView.findViewById(R.id.tvtenGio);
            tvOrigin = itemView.findViewById(R.id.tvxxgio);
            tvQuantity = itemView.findViewById(R.id.tvsogio);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
