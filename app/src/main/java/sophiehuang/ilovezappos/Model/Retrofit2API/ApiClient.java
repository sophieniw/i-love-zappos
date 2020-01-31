package sophiehuang.ilovezappos.Model.Retrofit2API;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//==========================================
// CODE SNAPSHOT
// Along with BitstampJsonApi interface, ApiClient is used to prevent repeated codes since
// retrofit2 API calling is used a lot in this project .
// You could declare a BitstampJsonApi object as below:
// BitstampJsonApi bitstampJsonApi = ApiClient.getClient().create(BitstampJsonApi.class);
// and then get your calls like this:
// Call<List<Transaction>> call = bitstampJsonApi.getTransactions();
// Call<OrderList> call = bitstampJsonApi.getOrders();

public class ApiClient {
    private static final String baseURL = "https://www.bitstamp.net/api/v2/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
