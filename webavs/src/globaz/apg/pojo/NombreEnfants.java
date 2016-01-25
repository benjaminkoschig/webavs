package globaz.apg.pojo;

import java.math.BigDecimal;

public enum NombreEnfants {
    DesTroisEnfants(BigDecimal.valueOf(3.0)),
    DeuxEnfants(BigDecimal.valueOf(2.0)),
    Indefini(BigDecimal.valueOf(-1.0)),
    SansEnfant(BigDecimal.ZERO),
    UnEnfant(BigDecimal.ONE);

    private BigDecimal nombreEnfant;

    private NombreEnfants(BigDecimal nombreEnfant) {
        this.nombreEnfant = nombreEnfant;
    }

    public BigDecimal getNombreEnfant() {
        return nombreEnfant;
    }
}
