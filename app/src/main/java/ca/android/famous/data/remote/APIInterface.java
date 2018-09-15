package ca.android.famous.data.remote;

import java.util.ArrayList;

import ca.android.famous.model.PostComment;
import ca.android.famous.model.Product;
import ca.android.famous.model.UserDetails;
import ca.android.famous.model.UserInfo;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("/posts")
    Call<ArrayList<UserDetails>> postDetails();

   // @GET("devStepin2IT/apitest/productData")
    //@GET("Hiral19494/FakeServer/productSS")
   /* Call<ArrayList<Product>> productDetails();*/
    @GET("devStepin2IT/apitest/productData")
    Observable<ArrayList<Product>> productDetails();


    @GET("/posts/{postId}/comments")
    Call<ArrayList<PostComment>> commentDetails(@Path("postId") String postId);

    @FormUrlEncoded
    @POST("/api/login")
    Call<UserInfo> userLogin(@Field("email") String email, @Field("password") String password);

}
