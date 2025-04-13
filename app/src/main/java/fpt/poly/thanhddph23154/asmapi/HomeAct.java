package fpt.poly.thanhddph23154.asmapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

import fpt.poly.thanhddph23154.asmapi.Adapter.CarAdapter;
import fpt.poly.thanhddph23154.asmapi.model.ApiServer;
import fpt.poly.thanhddph23154.asmapi.model.CarModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeAct extends AppCompatActivity {

    private RecyclerView rcvProduct;
    private CarAdapter carAdapter;
    private List<CarModel> listCarModel;

    private ViewPager2 viewPager;
    private fpt.poly.thanhddph23154.asmapi.SlideAdapter slideAdapter;
    private Handler handler = new Handler();
    private Runnable slideRunnable;

    private EditText edtSearch;

    ImageView imCart;

    Button btnyeuthich, btnBuy;

    Retrofit retrofit;
    ApiServer apiServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ
        rcvProduct = findViewById(R.id.rcv_Product);
        btnyeuthich = findViewById(R.id.btnFavorite);

        viewPager = findViewById(R.id.viewPager);
        imCart = findViewById(R.id.imgCart);
        edtSearch = findViewById(R.id.edt_search); // đảm bảo bạn có EditText trong layout

        btnyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAct.this, Favororite.class);
                startActivity(intent);
            }
        });



        imCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =new Intent(HomeAct.this, GioHang.class);
               startActivity(intent);
            }
        });



        // Setup RecyclerView dạng lưới
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        // Khởi tạo retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiServer.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServer = retrofit.create(ApiServer.class);

        // Gọi API ban đầu
        getDataFromApi();

        // Thiết lập Slide ảnh
        setupSlideShow();

        // Tìm kiếm realtime
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString().trim();
                if (key.isEmpty()) {
                    getDataFromApi(); // load lại toàn bộ khi rỗng
                } else {
                    searchCarByName(key);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromApi(); // Load lại dữ liệu sau khi quay lại trang
    }

    private void getDataFromApi() {
        Call<List<CarModel>> call = apiServer.getCars();
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    listCarModel = response.body();
                    carAdapter = new CarAdapter(HomeAct.this, listCarModel);
                    rcvProduct.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("API Error", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }

    private void searchCarByName(String key) {
        Call<List<CarModel>> call = apiServer.searchCar(key);
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    listCarModel = response.body();
                    carAdapter = new CarAdapter(HomeAct.this, listCarModel);
                    rcvProduct.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("Search Error", "Lỗi tìm kiếm: " + t.getMessage());
            }
        });
    }

    private void setupSlideShow() {
        List<Integer> imageList = Arrays.asList(
                R.drawable.slide1,
                R.drawable.slide3,
                R.drawable.slide1
        );

        slideAdapter = new fpt.poly.thanhddph23154.asmapi.SlideAdapter(this, imageList);
        viewPager.setAdapter(slideAdapter);

        slideRunnable = new Runnable() {
            @Override
            public void run() {
                int current = viewPager.getCurrentItem();
                int next = (current + 1) % imageList.size();
                viewPager.setCurrentItem(next, true);
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(slideRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(slideRunnable);
    }
}
