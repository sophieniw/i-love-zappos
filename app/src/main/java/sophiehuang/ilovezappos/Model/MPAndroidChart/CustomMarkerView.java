package sophiehuang.ilovezappos.Model.MPAndroidChart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import sophiehuang.ilovezappos.R;


//==========================================
// CODE SNAPSHOT
// CustomMarkerView class is later used in the TransactionsFragment to create a marker view on the
// line chart. The refreshContent method in this class retrieve data from each data point entry of
// the chart and set the marker (a textview) content to show the entry.
//

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText("$" + e.getY() + "/BTC");

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

}
