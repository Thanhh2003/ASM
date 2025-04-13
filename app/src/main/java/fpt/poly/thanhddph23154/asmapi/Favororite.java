package fpt.poly.thanhddph23154.asmapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.poly.thanhddph23154.asmapi.Adapter.FavoriteAdapter;
import fpt.poly.thanhddph23154.asmapi.model.ApiServer;
import fpt.poly.thanhddph23154.asmapi.model.CarModel;
import fpt.poly.thanhddph23154.asmapi.model.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favororite extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView imback;
    private FavoriteAdapter favoriteAdapter;
    private List<CarModel> favoriteCars = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fravorite);

        recyclerView = findViewById(R.id.rcylike);
        tvEmpty = findViewById(R.id.tvEmpty);
        imback = findViewById(R.id.btnBack1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteAdapter = new FavoriteAdapter(this, favoriteCars);
        recyclerView.setAdapter(favoriteAdapter);

        sharedPreferences = getSharedPreferences("LIKED_PREFS", MODE_PRIVATE);

        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResume();  // Ensure the data is reloaded when coming back
                finish();    // Close the current activity and return to the previous one
            }
        });

        getFavoriteCarsFromApi(); // Initial load of favorite cars
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavoriteCarsFromApi(); // Reload the list when the activity resumes
    }

    private void getFavoriteCarsFromApi() {
        Map<String, ?> allPrefs = sharedPreferences.getAll();
        List<String> likedIds = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            if (entry.getValue() instanceof Boolean && (Boolean) entry.getValue()) {
                likedIds.add(entry.getKey());
            }
        }

        if (likedIds.isEmpty()) {
            favoriteCars.clear();
            favoriteAdapter.notifyDataSetChanged();
            showEmptyMessage(true);
            return;
        }

        showEmptyMessage(false); // Hide the "no favorites" message

        Map<String, List<String>> body = new HashMap<>();
        body.put("ids", likedIds);

        ApiServer apiServer = RetrofitClient.getInstance().create(ApiServer.class);
        Call<List<CarModel>> call = apiServer.getFavoriteCars(body);
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteCars.clear();
                    favoriteCars.addAll(response.body());
                    favoriteAdapter.notifyDataSetChanged();
                    showEmptyMessage(favoriteCars.isEmpty());
                } else {
                    Toast.makeText(Favororite.this, "Không thể tải sản phẩm yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Toast.makeText(Favororite.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyMessage(boolean show) {
        if (tvEmpty != null) {
            tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
