package proj.me.discovery.services;



import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by root on 20/12/15.
 */
public interface RetrofitImpl {
    /*@POST("/get_user")
    void loginUser(@Body LoginRequest loginRequest, Callback<LoginResponse> response);*/
    @GET("/get_all_fests")
    void getAllFestivals(Callback<FestResponse> response);
    @POST("/loginUser")
    void loginUser(@Body LoginRequest loginRequest, Callback<LoginResponse> response);
    @POST("/createUser")
    void registerUser(@Body RegisterRequest registerRequest, Callback<RegisterResponse> response);

    /*@POST("/getAllFests")
    void getAllFests(@Body LoginRequest loginRequest, Callback<FestResponse> response);*/
}
