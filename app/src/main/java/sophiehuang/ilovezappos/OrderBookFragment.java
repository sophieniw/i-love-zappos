package sophiehuang.ilovezappos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sophiehuang.ilovezappos.Model.Retrofit2API.ApiClient;
import sophiehuang.ilovezappos.Model.Retrofit2API.BitstampJsonApi;
import sophiehuang.ilovezappos.Model.RecyclerViewAdapter.OrderAdapter;
import sophiehuang.ilovezappos.Model.DataObjects.OrderList;


//=====================================================
// CODE SNAPSHOT
// The OrderBookFragment handles 2 recycler views to display a bid table and a ask table;
// it has 3 private methods: 1. setBidsData() uses Retrofit2 to handle an API call to retrieve bids
// data from Bitstamp; 2. setAsksData() has same functionality as setBidsData() but for Asks orders;
// 3. generateOrderInRecyclerView(...) is used to implement data into recycler views in methods
// setBidsData() and setAsksData
//
//

public class OrderBookFragment extends Fragment {

    //declare variables
    private BitstampJsonApi bitstampJsonApi;
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView, recyclerView2;


    private List bids;
    private List asks;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_book, container, false);

        bitstampJsonApi = ApiClient.getClient().create(BitstampJsonApi.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView2 = view.findViewById(R.id.recyclerView2);

        setBidsData();
        setAsksData();

        return view;
    }

    private void setBidsData() {
        Call<OrderList> ordersCall = bitstampJsonApi.getOrders();

        ordersCall.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_SHORT).show();
                    return;
                }

                bids = response.body().getBids();
                generateOrderInRecyclerView(bids, "bids");
            }

            @Override
            public void onFailure(Call<OrderList> call, Throwable t) {
                Log.e("ORDERLIST_ENQUEUE_ERROR", t.getMessage());

            }
        });
    }

    private void setAsksData() {
        Call<OrderList> ordersCall = bitstampJsonApi.getOrders();

        ordersCall.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_SHORT).show();
                    return;
                }

                asks = response.body().getAsks();
                generateOrderInRecyclerView(asks, "asks");

            }

            @Override
            public void onFailure(Call<OrderList> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void generateOrderInRecyclerView(List orders, String orderType) {
        orderAdapter = new OrderAdapter(orders);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        if (orderType.equals("bids")) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(orderAdapter);
        }
        if (orderType.equals("asks")) {
            recyclerView2.setLayoutManager(layoutManager);
            recyclerView2.setAdapter(orderAdapter);
        }

    }
}
