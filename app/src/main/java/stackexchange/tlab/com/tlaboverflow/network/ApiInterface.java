package stackexchange.tlab.com.tlaboverflow.network;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Query;
import stackexchange.tlab.com.tlaboverflow.model.Schema;

public interface ApiInterface {

    @GET("search")
    Call<Schema> getSearch(@Query("pagesize") String pagesize,
                           @Query("order") String order,
                           @Query("fromdate") String min,
                           @Query("todate") String max,
                           @Query("sort") String sort,
                           @Query("tagged") String tagged,
                           @Query("site") String site);
}
