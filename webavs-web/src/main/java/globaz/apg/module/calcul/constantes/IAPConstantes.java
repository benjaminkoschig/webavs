package globaz.apg.module.calcul.constantes;

import java.math.BigDecimal;

public interface IAPConstantes {

    // Deja présente dans les plages de valeurs : APGMINALEX
    public static final BigDecimal ALLOCATION_JOURNALIERE_EXPLOITATION = BigDecimal.valueOf(67.00);

    // Non présente encore dans les plages de valeurs.
    public static final BigDecimal ALLOCATION_JOURNALIERE_MAX_FRAIS_GARDE = BigDecimal.valueOf(67.00);
    public static final BigDecimal ALLOCATION_POUR_ENFANT = BigDecimal.valueOf(20.00);
    public static final BigDecimal APG_JOURNALIERE_MAX = BigDecimal.valueOf(196.00);
    public static final BigDecimal GARANTIE_MINIMALE_CADRE_SERVICE_LONG_1_ENFANT = BigDecimal.valueOf(135.00);
    public static final BigDecimal GARANTIE_MINIMALE_CADRE_SERVICE_LONG_2_ENFANT_OU_PLUS = BigDecimal.valueOf(152.00);
    public static final BigDecimal GARANTIE_MINIMALE_CADRE_SERVICE_LONG_SANS_ENFANT = BigDecimal.valueOf(91.00);
    public static final BigDecimal GARANTIE_MINIMALE_GENERALE_1_ENFANT = BigDecimal.valueOf(98.00);
    public static final BigDecimal GARANTIE_MINIMALE_GENERALE_2_ENFANT_OU_PLUS = BigDecimal.valueOf(123.00);
    public static final BigDecimal GARANTIE_MINIMALE_GENERALE_SANS_ENFANT = BigDecimal.valueOf(62.00);
    public static final BigDecimal GARANTIE_MINIMALE_SERVICE_AVANCEMENT_1_ENFANT = BigDecimal.valueOf(160.00);
    public static final BigDecimal GARANTIE_MINIMALE_SERVICE_AVANCEMENT_2_ENFANT_OU_PLUS = BigDecimal.valueOf(172.00);
    public static final BigDecimal GARANTIE_MINIMALE_SERVICE_AVANCEMENT_SANS_ENFANT = BigDecimal.valueOf(111.00);
    public static final BigDecimal MONTANT_JOURNALIER_MAX = BigDecimal.valueOf(245.00);
    public static final BigDecimal MONTANT_JOURNALIER_MAX_AVANT_2008 = BigDecimal.valueOf(237.60);
}
