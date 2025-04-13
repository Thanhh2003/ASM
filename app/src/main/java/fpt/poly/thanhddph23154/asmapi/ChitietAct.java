package fpt.poly.thanhddph23154.asmapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.UUID;

import fpt.poly.thanhddph23154.asmapi.model.ApiServer;
import fpt.poly.thanhddph23154.asmapi.model.CarModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChitietAct extends AppCompatActivity {

    ImageView imgBack, imgDetail;
    TextView tvTen, tvXuatxu, tvGia, tvmota, tvtotal;
    Button btncong, btntru, btnAddToCart;
    EditText edtQuantity;
    int gia = 0;
    CarModel currentCar; // Giữ lại đối tượng xe

    SharedPreferences sharedPreferences;
    private static final String CART_PREFS = "CART_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitiet);

        // Ánh xạ view
        tvTen = findViewById(R.id.tenchitiet);
        tvXuatxu = findViewById(R.id.xxchitiet);
        tvGia = findViewById(R.id.giact);
        tvmota = findViewById(R.id.mota);
        tvtotal = findViewById(R.id.total);
        imgDetail = findViewById(R.id.imgct);
        imgBack = findViewById(R.id.btnBack);
        btncong = findViewById(R.id.btncong);
        btntru = findViewById(R.id.btntru);
        btnAddToCart = findViewById(R.id.mua); // nút thêm vào giỏ
        edtQuantity = findViewById(R.id.edtQuantity);

        sharedPreferences = getSharedPreferences(CART_PREFS, MODE_PRIVATE);

        String id = getIntent().getStringExtra("id");

        if (id != null && !id.isEmpty()) {
            getCarDetailById(id);
        } else {
            Toast.makeText(this, "Không có ID sản phẩm", Toast.LENGTH_SHORT).show();
        }

        imgBack.setOnClickListener(v -> finish());

        btntru.setOnClickListener(view -> {
            int current = getQuantityFromEditText();
            if (current > 1) {
                current--;
                edtQuantity.setText(String.valueOf(current));
                updateTotalPrice(current);
            }
        });

        btncong.setOnClickListener(view -> {
            int current = getQuantityFromEditText();
            current++;
            edtQuantity.setText(String.valueOf(current));
            updateTotalPrice(current);
        });

        edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int qty = getQuantityFromEditText();
                updateTotalPrice(qty);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnAddToCart.setOnClickListener(v -> {
            if (currentCar != null) {
                int quantity = getQuantityFromEditText();
                currentCar.setSoluong(quantity);
                saveToCart(currentCar);
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChitietAct.this, GioHang.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private int getQuantityFromEditText() {
        String text = edtQuantity.getText().toString().trim();
        if (text.isEmpty()) return 1;
        try {
            int qty = Integer.parseInt(text);
            return Math.max(qty, 1);
        } catch (Exception e) {
            return 1;
        }
    }

    private void updateTotalPrice(int quantity) {
        int total = gia * quantity;
        tvtotal.setText("Tổng tiền: " + total + " VNĐ");
    }

    private void getCarDetailById(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServer.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServer api = retrofit.create(ApiServer.class);
        Call<CarModel> call = api.getCarDetail(id);

        call.enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CarModel car = response.body();
                    currentCar = car;
                    tvTen.setText("Tên: " + car.getTen());
                    tvXuatxu.setText("Xuất xứ: " + car.getXuatxu());
                    tvGia.setText("Giá: " + car.getGia());
                    tvmota.setText("Mô tả: " + car.getMota());
                    gia = (int) car.getGia();

                    Glide.with(ChitietAct.this)
                            .load(car.getImg())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.loadloi)
                            .into(imgDetail);
                }
            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                Toast.makeText(ChitietAct.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToCart(CarModel car) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String id = car.get_id(); // hoặc UUID.randomUUID().toString() nếu không có _id
        String json = new Gson().toJson(car);
        editor.putString(id, json);
        editor.apply();
    }
}
