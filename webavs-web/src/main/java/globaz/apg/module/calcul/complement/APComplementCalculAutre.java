package globaz.apg.module.calcul.complement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.util.JANumberFormatter;

public class APComplementCalculAutre extends APComplementCalculateur{

    private static final BigDecimal DIVISION_AUTRE = new BigDecimal("21.75");

    private static final Integer[] joursOuvrables = {1, 2, 3, 4, 5, 5, 5,
            6, 7, 8, 9, 10, 10, 10,
            11, 12, 13, 14, 15, 15, 15,
            16, 17, 18, 19, 20, 20, 20,
            21, 22, 23};

    public APComplementCalculAutre(Map<EMontantsMax, BigDecimal> montantsMax, ECanton canton, String dateDebut) {
        super(montantsMax, canton, dateDebut);
    }

    @Override
    BigDecimal calculeSalaireJournalierCOMCIAB(BigDecimal salaireMensuel) throws Exception {
        BigDecimal salaireJournalier = arrondir(salaireMensuel.divide(DIVISION_AUTRE,2, RoundingMode.HALF_UP));
        BigDecimal salaireMax = ECanton.JU.equals(canton) ? montantsMax.get(EMontantsMax.COMCIABJUA): montantsMax.get(EMontantsMax.COMCIABBEA);
        if(salaireJournalier.compareTo(salaireMax)>0) {
            montantJournalier = salaireMax;
        } else {
            montantJournalier = salaireJournalier;
        }
        return montantJournalier;
    }

    /**
     * Calcule le montant journalier limité par le montant max par canton
     *
     * @param salaireJournalier
     * @return le montantJournalier
     */
    @Override
    BigDecimal calculeSalaireJournalierMATCIAB1(BigDecimal salaireJournalier) {
        BigDecimal salaireJournalierMATCIAB1 = arrondir(salaireJournalier);
        montantJournalier = salaireJournalierMATCIAB1;
        return montantJournalier;
    }

    @Override
    BigDecimal calculMontantTotalCOMCIAB(BigDecimal salaireJournalier, int nbJourSolde) {
        nbJourPrisEnCompte = getNbJourOuvrable(nbJourSolde);
        return salaireJournalier.multiply(BigDecimal.valueOf(nbJourPrisEnCompte));
    }

    @Override
    BigDecimal calculMontantTotalMATCIAB1(BigDecimal salaireJournalier, int nombreJoursSoldesPeriodePriseEnCompte) {
        salaireJournalier = arrondirFranc(salaireJournalier);
        return salaireJournalier.multiply(BigDecimal.valueOf(nombreJoursSoldesPeriodePriseEnCompte));
    }

    /**
     * Règle retrouvée dans le code...</br>
     * arrondi à 0 chiffres après la virgule, à 1Chf près.
     *
     * @param montant
     * @return
     */
    private BigDecimal arrondirFranc(BigDecimal montant) {
        // arrondi à 0 chiffres après la virgule, à 1Chf près.
        return new BigDecimal(JANumberFormatter
                .deQuote(JANumberFormatter.format(montant.toString(), 1, 2, JANumberFormatter.SUP)));
    }

    private Integer getNbJourOuvrable(int nbJourSolde) {
        if(nbJourSolde > 31) {
            return joursOuvrables[30];
        }
        return joursOuvrables[nbJourSolde-1];

    }

}
