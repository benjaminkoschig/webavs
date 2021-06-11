package globaz.apg.module.calcul.wrapper;

import globaz.framework.util.FWCurrency;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Value(staticConstructor="of")
public final class APMontantJourCurrency {
    private final FWCurrency montant;
    private final Integer jours;
    private final String situationProfessionelle;
}
