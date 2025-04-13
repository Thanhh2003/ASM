package fpt.poly.thanhddph23154.asmapi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.poly.thanhddph23154.asmapi.ChitietAct;
import fpt.poly.thanhddph23154.asmapi.R;
import fpt.poly.thanhddph23154.asmapi.model.CarModel;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private Context context;
    private List<CarModel> carModelList;
    private Map<String, Boolean> likedMap = new HashMap<>();
    private SharedPreferences sharedPreferences;

    public CarAdapter(Context context, List<CarModel> carModelList) {
        this.context = context;
        this.carModelList = new ArrayList<>(carModelList); // Copy tránh ảnh hưởng gốc
        sharedPreferences = context.getSharedPreferences("LIKED_PREFS", Context.MODE_PRIVATE);
        loadLikedStates();
    }

    private void loadLikedStates() {
        for (CarModel car : carModelList) {
            boolean isLiked = sharedPreferences.getBoolean(car.get_id(), false);
            likedMap.put(car.get_id(), isLiked);
        }
    }

    public void updateList(List<CarModel> newList) {
        carModelList.clear();
        carModelList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dsxe, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        CarModel car = carModelList.get(position);

        holder.tvName.setText("Tên sản phẩm: " + car.getTen());
        holder.tvNamSX.setText("Năm SX: " + car.getNamsx());
        holder.tvHang.setText("Xuất xứ: " + car.getXuatxu());
        holder.tvGia.setText("Giá: " + car.getGia());

        Glide.with(context)
                .load(car.getImg())
                .placeholder(R.drawable.load)
                .error(R.drawable.loadloi)
                .into(holder.imgAvatar);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChitietAct.class);
            intent.putExtra("id", car.get_id());
            intent.putExtra("img", car.getImg());
            intent.putExtra("ten", car.getTen());
            intent.putExtra("xuatxu", car.getXuatxu());
            context.startActivity(intent);
        });

        // Xử lý trạng thái thích sản phẩm
        boolean isLiked = likedMap.getOrDefault(car.get_id(), false);
        holder.imglike.setImageResource(isLiked ? R.drawable.heart1 : R.drawable.heart);

        holder.imglike.setOnClickListener(view -> {
            boolean currentLike = likedMap.getOrDefault(car.get_id(), false);
            boolean newLike = !currentLike;
            likedMap.put(car.get_id(), newLike);

            // Lưu trạng thái vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(car.get_id(), newLike);
            editor.apply();

            // Cập nhật icon thích
            holder.imglike.setImageResource(newLike ? R.drawable.heart1 : R.drawable.heart);

            // Thông báo trạng thái like
            Toast.makeText(context, newLike ? "Đã thích" : "Bỏ thích", Toast.LENGTH_SHORT).show();
        });

        // Xử lý thêm sản phẩm vào giỏ hàng
        holder.btnbuy.setOnClickListener(view -> {
            SharedPreferences cartPrefs = context.getSharedPreferences("CART_PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = cartPrefs.edit();

            // Lấy ID sản phẩm và thông tin sản phẩm
            String carId = car.get_id();
            String imgUrl = car.getImg();
            String carName = car.getTen();
            String carOrigin = car.getXuatxu();
            int quantity = 1;  // Mặc định là 1 khi thêm mới sản phẩm

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            String existingData = cartPrefs.getString(carId, null);

            if (existingData != null) {
                // Nếu sản phẩm đã có, tăng số lượng
                String[] existingProduct = existingData.split("\\|");
                int existingQuantity = Integer.parseInt(existingProduct[3]);
                quantity = existingQuantity + 1;  // Tăng số lượng lên 1

                // Cập nhật lại thông tin với số lượng mới
                String updatedData = imgUrl + "|" + carName + "|" + carOrigin + "|" + quantity;
                editor.putString(carId, updatedData);

                // Thông báo sản phẩm đã có trong giỏ hàng
                Toast.makeText(context, "Sản phẩm đã có trong giỏ hàng. Số lượng đã được tăng lên!", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu sản phẩm chưa có, thêm mới thông tin sản phẩm vào giỏ hàng
                String newProductData = imgUrl + "|" + carName + "|" + carOrigin + "|" + quantity;
                editor.putString(carId, newProductData);

                // Thông báo sản phẩm đã được thêm vào giỏ hàng
                Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }

            editor.apply();  // Lưu thay đổi vào SharedPreferences
        });

    }

    @Override
    public int getItemCount() {
        return carModelList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, imglike;
        Button btnbuy;
        TextView tvName, tvNamSX, tvHang, tvGia;

        public CarViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatatr);
            tvName = itemView.findViewById(R.id.tv_Name);
            tvNamSX = itemView.findViewById(R.id.tv_NamSX);
            tvHang = itemView.findViewById(R.id.tv_Hang);
            tvGia = itemView.findViewById(R.id.tv_Gia);
            imglike = itemView.findViewById(R.id.like);
            btnbuy = itemView.findViewById(R.id.btnthem);
        }
    }
}
