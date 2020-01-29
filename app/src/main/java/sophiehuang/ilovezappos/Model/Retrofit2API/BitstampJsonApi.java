package sophiehuang.ilovezappos.Model.Retrofit2API;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import sophiehuang.ilovezappos.Model.DataObjects.OrderList;
import sophiehuang.ilovezappos.Model.DataObjects.Ticker;
import sophiehuang.ilovezappos.Model.DataObjects.Transaction;

public interface BitstampJsonApi {
    @GET("transactions/btcusd")
    Call<List<Transaction>> getTransactions();

    @GET("order_book/btcusd")
    Call<OrderList> getOrders();

    @GET("ticker_hour/btcusd")
    Call<Ticker> getTicker();
}
