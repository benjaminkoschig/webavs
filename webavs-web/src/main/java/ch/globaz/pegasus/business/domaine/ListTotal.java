package ch.globaz.pegasus.business.domaine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ListTotal<T> {
    private List<T> list = null;
    private BigDecimal total = null;

    public ListTotal() {
        this.list = new ArrayList<T>();
        this.total = new BigDecimal(0);
    }

    public List<T> getList() {
        return this.list;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
