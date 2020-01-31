package sophiehuang.ilovezappos;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import sophiehuang.ilovezappos.Model.MPAndroidChart.CustomMarkerView;
import sophiehuang.ilovezappos.Model.Retrofit2API.ApiClient;
import sophiehuang.ilovezappos.Model.Retrofit2API.BitstampJsonApi;
import sophiehuang.ilovezappos.Model.DataObjects.Transaction;

//==========================================
// CODE SNAPSHOT
// Transactions Fragment handles a line chart created based on MPAndroidChart.
// In the onCreateView, the fragment invokes setLineEntries() method. Within the setLineEntries()
// method, an API call to the recent BTC transactions is made and the data is passed through
// to the method generateChart(...) in order to generate a line chart.
//


public class TransactionsFragment extends Fragment {

    private LineChart linechart;
    private List<Entry> lineEntries;
    private BitstampJsonApi bitstampJsonApi;

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

                String[] unixTimes = new String[transactions.size()];

                int count = 0;
                for (int i = transactions.size() - 1; i >= 0; i--) {
                    Transaction ts = transactions.get(i);
                    String price = ts.getPrice();
                    String unixDate = ts.getDate();

                    Float price_f = Float.parseFloat(price);

                    unixTimes[count] = unixDate;
                    lineEntries.add(new Entry(count, price_f));
                    count++;
                }

                String[] datetimes = convertUnixToDateTime(unixTimes);

                generateChart(lineEntries, datetimes);
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Log.e("TRANSACTIONS_CALL_ENQUEUE_ERROR", t.getMessage());
            }
        });

    }

    private String[] convertUnixToDateTime(String[] strUnixs) {
        String[] dates = new String[strUnixs.length];
        int count = 0;
        for (String unix : strUnixs) {
            long longUnix = Long.parseLong(unix);
            Date date = new java.util.Date(longUnix * 1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
            String strDate = sdf.format(date);

            dates[count] = strDate;
            count++;
        }
        return dates;
    }

    private void generateChart(List<Entry> dataEntries, String[] datetimes) {
        LineDataSet dataSet = new LineDataSet(dataEntries, "Recent BTC Transaction History (GMT)");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setHighlightEnabled(true);
        dataSet.setHighLightColor(Color.parseColor("#008577"));
        dataSet.setCircleRadius(1.0f);
        dataSet.setDrawFilled(true);

        LineData data = new LineData(dataSet);
        XAxis xAxis = linechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(datetimes));
        xAxis.setLabelRotationAngle(-65);

        YAxis rightAxis = linechart.getAxisRight();
        rightAxis.setDrawLabels(false);

        Legend legend = linechart.getLegend();
        legend.setYOffset(10);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(12);

        linechart.setTouchEnabled(true);
        linechart.getDescription().setEnabled(false);
        linechart.setData(data);
        linechart.setAutoScaleMinMaxEnabled(true);

        //create a markerView
        CustomMarkerView mv = new CustomMarkerView(getActivity(), R.layout.custom_marker_view_layout);
        // set the marker to the chart
        linechart.setMarkerView(mv);

        linechart.notifyDataSetChanged();
        linechart.invalidate();
    }
}





