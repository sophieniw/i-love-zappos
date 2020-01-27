package sophiehuang.ilovezappos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sophiehuang.ilovezappos.Model.ApiClient;
import sophiehuang.ilovezappos.Model.BitstampJsonApi;
import sophiehuang.ilovezappos.Model.Ticker;
import sophiehuang.ilovezappos.Model.Transaction;

public class PriceAlertFragment extends Fragment {

    BitstampJsonApi bitstampJsonApi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_alert, container, false);

        bitstampJsonApi = ApiClient.getClient().create(BitstampJsonApi.class);


        Call<Ticker> call = bitstampJsonApi.getTicker();
        Toast.makeText(getActivity(), "ENTERING ENQUEUE", Toast.LENGTH_SHORT).show();
        try {
            call.enqueue(new Callback<Ticker>() {
                @Override
                public void onResponse(Call<Ticker> call, Response<Ticker> response) {
                    Toast.makeText(getActivity(), "OnResponse", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<Ticker> call, Throwable t) {
                    Toast.makeText(getActivity(), "OnFailure", Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "exception", Toast.LENGTH_SHORT).show();
        }

        return view;

    }

//    private void setLineEntries() {
//        Call<Ticker> call = bitstampJsonApi.getTicker();
//        Toast.makeText(getActivity(), "ENTERING ENQUEUE", Toast.LENGTH_SHORT).show();
//        call.enqueue(new Callback<Ticker>() {
//            @Override
//            public void onResponse(Call<Ticker> call, Response<Ticker> response) {
//                Toast.makeText(getActivity(), "OnResponse", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(Call<Ticker> call, Throwable t) {
//                Toast.makeText(getActivity(), "OnFailure", Toast.LENGTH_LONG).show();
//
//            }
//        });
//    }
}
