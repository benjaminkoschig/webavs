package ch.globaz.pegasus.business.models.mutation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RecapListMutation {
    List<RecapMutation> list = new ArrayList<RecapMutation>();
    BigDecimal totalAllocationNoel = new BigDecimal(0);
    BigDecimal totalAugmentation = new BigDecimal(0);
    BigDecimal totalDiminution = new BigDecimal(0);
    BigDecimal totalJoursAppoint = new BigDecimal(0);
    BigDecimal totalRetro = new BigDecimal(0);

    public void addTotalAllocationNoel(BigDecimal totalAllocationNoel) {
        this.totalAllocationNoel = this.totalAllocationNoel.add(totalAllocationNoel);
    }

    public void addTotalAugmentation(BigDecimal totalAugmentation) {
        this.totalAugmentation = this.totalAugmentation.add(totalAugmentation);
    }

    public void addTotalDiminution(BigDecimal totalDiminution) {
        this.totalDiminution = this.totalDiminution.add(totalDiminution);
    }

    public void addTotalJoursAppoint(BigDecimal totalJoursAppoint) {
        this.totalJoursAppoint = this.totalJoursAppoint.add(totalJoursAppoint);
    }

    public void addTotalRetro(BigDecimal totalRetro) {
        this.totalRetro = this.totalRetro.add(totalRetro);
    }

    public List<RecapMutation> getList() {
        return list;
    }

    public BigDecimal getTotalAllocationNoel() {
        return totalAllocationNoel;
    }

    public BigDecimal getTotalAugmentation() {
        return totalAugmentation;
    }

    public BigDecimal getTotalDiminution() {
        return totalDiminution;
    }

    public BigDecimal getTotalJoursAppoint() {
        return totalJoursAppoint;
    }

    public BigDecimal getTotalRetro() {
        return totalRetro;
    }

    public void setList(List<RecapMutation> list) {
        this.list = list;
    }

}
