package sophiehuang.ilovezappos.Model.Retrofit2API;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import sophiehuang.ilovezappos.Model.DataObjects.OrderList;
import sophiehuang.ilovezappos.Model.DataObjects.Ticker;
import sophiehuang.ilovezappos.Model.DataObjects.Transaction;


//==========================================
// CODE SNAPSHOT
// To be used with ApiClient class to create calls:
// BitstampJsonApi bitstampJsonApi = ApiClient.getClient().create(BitstampJsonApi.class);
// and then get your calls like this:
// Call<List<Transaction>> call = bitstampJsonApi.getTransactions();
// Call<OrderList> call = bitstampJsonApi.getOrders();
//
// BitstampJsonApi interface serves as a direction as to which path to go and what objects to cast
// the data into

public interface BitstampJsonApi {
    @GET("transactions/btcusd")
    Call<List<Transaction>> getTransactions();

    @GET("order_book/btcusd")
    Call<OrderList> getOrders();

    @GET("ticker_hour/btcusd")
    Call<Ticker> getTicker();
}
