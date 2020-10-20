package globaz.apg.module.calcul.complement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;

public class APComplementCalculRecrue extends APComplementCalculateur{
    
    private static final BigDecimal DIVISION_RECRUE = new BigDecimal("30");
    
    public APComplementCalculRecrue(Map<EMontantsMax, BigDecimal> montantsMax, ECanton canton, String dateDebut) {
        super(montantsMax, canton, dateDebut);
    }
    
    @Override
    BigDecimal calculeSalaireJournalierCOMCIAB(BigDecimal salaireMensuel) throws Exception {
        BigDecimal salaireJournalier = arrondir(salaireMensuel.divide(DIVISION_RECRUE));
        BigDecimal salaireMax = ECanton.JU.equals(canton) ? montantsMax.get(EMontantsMax.COMCIABJUR): montantsMax.get(EMontantsMax.COMCIABBER);
        if(salaireJournalier.compareTo(salaireMax)>0) {
            montantJournalier = salaireMax;
        } else {
            montantJournalier = salaireJournalier;
        }
        return montantJournalier;
    }

    @Override
    //ESVE MATERNITE MONTANT MAX
    BigDecimal calculeSalaireJournalierMATCIAB(BigDecimal salaireMensuel) throws Exception {
        BigDecimal salaireJournalier = arrondir(salaireMensuel.divide(DIVISION_RECRUE,2, RoundingMode.HALF_UP));
        BigDecimal salaireMax = ECanton.JU.equals(canton) ? montantsMax.get(EMontantsMax.MATCIABJUM): montantsMax.get(EMontantsMax.MATCIABBEM);
        if(salaireJournalier.compareTo(salaireMax)>0) {
            montantJournalier = salaireMax;
        } else {
            montantJournalier = salaireJournalier;
        }
        return montantJournalier;
    }

    @Override
    BigDecimal calculMontantTotal(BigDecimal salaireJournalier, int nbJourSolde) {
        nbJourPrisEnCompte = nbJourSolde;
        return salaireJournalier.multiply(BigDecimal.valueOf(nbJourPrisEnCompte));
    }
    
}
