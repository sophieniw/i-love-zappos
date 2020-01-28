package sophiehuang.ilovezappos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import sophiehuang.ilovezappos.Model.ApiClient;
import sophiehuang.ilovezappos.Model.BitstampJsonApi;
import sophiehuang.ilovezappos.Model.Transaction;

public class TransactionsFragment extends Fragment {

    LineChart linechart;
    List<Entry> lineEntries;
    BitstampJsonApi bitstampJsonApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        linechart = view.findViewById(R.id.lineChart);
        lineEntries = new ArrayList<>();

        linechart.setNoDataText("");
        bitstampJsonApi = ApiClient.getClient().create(BitstampJsonApi.class);


        setLineEntries();


        return view;
    }


    //a function to get the bitstamp api from base url
    private void setLineEntries() {
        Call<List<Transaction>> call = bitstampJsonApi.getTransactions();
        Toast.makeText(getActivity(), "ENTERING ENQUEUE", Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Response 404", Toast.LENGTH_LONG).show();
                    return;
                }

                //response.body() returns the a list of Transaction objects
                //fields of Transaction objects are automatically filled
                List<Transaction> transactions = response.body();

                int count = 0;
                for (int i = transactions.size() - 1; i >= 0; i--) {
                    Transaction ts = transactions.get(i);
                    String price = ts.getPrice();
                    Float price_f = Float.parseFloat(price);
                    lineEntries.add(new Entry(count, price_f));
                    count++;
                }


                LineDataSet dataSet = new LineDataSet(lineEntries, "Recent Transaction History");
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                dataSet.setHighlightEnabled(false);
                dataSet.setCircleRadius(1.0f);

                LineData data = new LineData(dataSet);
                XAxis xAxis = linechart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                linechart.setTouchEnabled(true);
                linechart.setData(data);
                linechart.notifyDataSetChanged();
                linechart.invalidate();
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}





