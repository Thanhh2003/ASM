package fpt.poly.thanhddph23154.asmapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fpt.poly.thanhddph23154.asmapi.Adapter.CartAdapter;
import fpt.poly.thanhddph23154.asmapi.model.CarModel;

public class GioHang extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView tvEmpty, tvTotalPrice;
    private List<CarModel> cartList = new ArrayList<>();
    private CartAdapter cartAdapter;
    private SharedPreferences sharedPreferences;
    private static final String CART_PREFS = "CART_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        recyclerCart = findViewById(R.id.rcygio);
        tvEmpty = findViewById(R.id.tvEmpty1);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        sharedPreferences = getSharedPreferences(CART_PREFS, MODE_PRIVATE);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        cartAdapter = new CartAdapter(this, cartList, sharedPreferences);
        recyclerCart.setAdapter(cartAdapter);

        // Load items from SharedPreferences
        loadCartItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    // Method to load cart items from SharedPreferences
    private void loadCartItems() {
        cartList.clear(); // Clear current list
        Map<String, ?> allPrefs = sharedPreferences.getAll();
        Gson gson = new Gson();

        // Iterate through SharedPreferences and load only valid CarModel objects
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            try {
                String json = entry.getValue().toString();
                CarModel car = gson.fromJson(json, CarModel.class);
                cartList.add(car);
            } catch (Exception e) {
                e.printStackTrace(); // Skip invalid or corrupted data
            }
        }

        // Update UI based on the cart list
        cartAdapter.notifyDataSetChanged();
        toggleEmptyMessage(cartList.isEmpty());
        updateTotalPrice();
    }

    private void toggleEmptyMessage(boolean show) {
        tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // Calculate total price of items in the cart
    public void updateTotalPrice() {
        int total = 0;
        for (CarModel item : cartList) {
            total += item.getGia() * item.getSoluong();  // Gia * So luong
        }
        tvTotalPrice.setText(String.format("%,d VNĐ", total));
    }
}
