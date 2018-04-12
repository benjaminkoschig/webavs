package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

public class DernierRevenuDeterminantEtBase {
    private BigDecimal revenuDeterminant;
    private BigDecimal revenuBase;

    public DernierRevenuDeterminantEtBase() {
        // need by jaxws
    }

    public DernierRevenuDeterminantEtBase(BigDecimal revenuDeterminant, BigDecimal revenuBase) {
        super();
        this.revenuDeterminant = revenuDeterminant;
        this.revenuBase = revenuBase;
    }

    public BigDecimal getRevenuDeterminant() {
        return revenuDeterminant;
    }

    public void setRevenuDeterminant(BigDecimal revenuDeterminant) {
        this.revenuDeterminant = revenuDeterminant;
    }

    public BigDecimal getRevenuBase() {
        return revenuBase;
    }

    public void setRevenuBase(BigDecimal revenuBase) {
        this.revenuBase = revenuBase;
    }

}
