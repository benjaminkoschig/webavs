package ch.globaz.pegasus.business.vo.decompte;

import java.math.BigDecimal;

public class PCAVerseVO {

    private String label = null;
    private BigDecimal totalPCAVerse = null;

    public PCAVerseVO() {
        super();
    }

    public PCAVerseVO(String label, BigDecimal totalPCAVerse) {
        super();
        this.label = label;
        this.totalPCAVerse = totalPCAVerse;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getTotalPCAVerse() {
        return totalPCAVerse;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTotalPCAVerse(BigDecimal totalPCAVerse) {
        this.totalPCAVerse = totalPCAVerse;
    }

}
