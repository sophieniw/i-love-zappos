package sophiehuang.ilovezappos.Model.RecyclerViewAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sophiehuang.ilovezappos.R;

//==========================================
// CODE SNAPSHOT
// OrderAdapter is a recycler view adapter used to set content into different visual components properly
//

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<List> orders;
    private float sum = 0f;


    public OrderAdapter(List<List> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {

        String price = orders.get(position).get(0).toString();
        String amount = orders.get(position).get(1).toString();
        Float value = Float.parseFloat(price) * Float.parseFloat(amount);
        String strValue = Float.toString(value);

        sum += Float.parseFloat(amount);


        holder.tvPrice.setText(price);
        holder.tvAmount.setText(amount);
        holder.tvValue.setText(strValue);
        holder.tvSum.setText(Float.toString(sum));


    }

    @Override
    public int getItemCount() {
        try {
            return orders.size();
        } catch (Exception e) {
            return -1;
        }

    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvAmount, tvSum, tvValue;

        OrderViewHolder(View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvSum = itemView.findViewById(R.id.tvSum);
        }
    }
}
