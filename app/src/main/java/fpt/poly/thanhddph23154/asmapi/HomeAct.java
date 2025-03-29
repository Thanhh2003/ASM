package fpt.poly.thanhddph23154.asmapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    ListView listView;

    List<CarModel> listCarModel ;
    CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.listviewMain);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServer.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServer apiServer = retrofit.create(ApiServer.class);
        Call<List<CarModel>> call = apiServer.getCars();

        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if(response.isSuccessful()){
                    listCarModel = response.body();
                   // Log.d("list","response: "+response);
                    carAdapter = new CarAdapter(getApplicationContext(),listCarModel);

                    listView.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {

            }
        });

    }
}