package sophiehuang.ilovezappos.Model.DataObjects;

import java.util.List;

public class OrderList {
    private String timestamp;
    private List<List> bids;
    private List<List> asks;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List getBids() {
        return bids;
    }

    public void setBids(List bids) {
        this.bids = bids;
    }

    public List getAsks() {
        return asks;
    }

    public void setAsks(List asks) {
        this.asks = asks;
    }
}
