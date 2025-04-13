package fpt.poly.thanhddph23154.asmapi.model;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServer {

    // Địa chỉ máy chủ của API (thay đổi theo cấu hình máy chủ của bạn)
    String DOMAIN = "http://192.168.10.112:3000/";

    // Lấy danh sách xe
    @GET("api/list")
    Call<List<CarModel>> getCars();

    // Lấy chi tiết xe theo ID
    @GET("api/list/{id}")
    Call<CarModel> getCarDetail(@Path("id") String id);

    // Tìm kiếm xe theo từ khóa
    @GET("api/search")
    Call<List<CarModel>> searchCar(@Query("key") String key);

    // Cập nhật trạng thái like của xe
    @PATCH("car/update-like/{id}")
    Call<CarModel> updateLikeStatus(@Path("id") String id, @Body Map<String, Boolean> liked);

    // Lấy các sản phẩm yêu thích
    @POST("api/favorite")
    Call<List<CarModel>> getFavoriteCars(@Body Map<String, List<String>> ids);

    // Lấy các sản phẩm trong giỏ hàng
    @POST("api/cart")
    Call<List<CarModel>> getCarItem(@Body Map<String, List<String>> ids);

    // Thêm sản phẩm vào giỏ hàng
    @POST("api/cart/add")
    Call<String> addToCart(@Body CarModel car);

    // Xóa sản phẩm khỏi giỏ hàng
    @DELETE("api/cart/remove/{id}")
    Call<String> removeFromCart(@Path("id") String id);

    // Lấy tổng tiền của giỏ hàng
    @GET("api/cart/total")
    Call<Integer> getTotalPrice();
}
