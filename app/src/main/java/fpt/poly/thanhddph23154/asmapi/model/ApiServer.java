package fpt.poly.thanhddph23154.asmapi.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServer {

     String DOMAIN = "http://192.168.10.108:3000/";


   @GET("api/list")
    Call<List<CarModel>> getCars();


}
