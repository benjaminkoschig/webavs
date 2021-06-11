package globaz.apg.module.calcul.wrapper;

import globaz.framework.util.FWCurrency;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Value(staticConstructor="of")
public final class APMontantJour {
    private final BigDecimal montant;
    private final Integer jours;
    private final String situationProfessionelle;

    public BigDecimal calculMontantJournalierParRapportAuTotal(int total) {
        return montant.multiply(BigDecimal.valueOf(jours)).divide(BigDecimal.valueOf(total), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal calculMontantPrestation() {
        return montant.multiply(BigDecimal.valueOf(jours));
    }
}
