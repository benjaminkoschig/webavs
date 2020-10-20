package globaz.apg.module.calcul.complement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;

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

    @Override
    //ESVE MATERNITE MONTANT MAX
    BigDecimal calculeSalaireJournalierMATCIAB(BigDecimal salaireMensuel) throws Exception {
        BigDecimal salaireJournalier = arrondir(salaireMensuel.divide(DIVISION_AUTRE,2, RoundingMode.HALF_UP));
        BigDecimal salaireMax = ECanton.JU.equals(canton) ? montantsMax.get(EMontantsMax.MATCIABJUM): montantsMax.get(EMontantsMax.MATCIABBEM);
        if(salaireJournalier.compareTo(salaireMax)>0) {
            montantJournalier = salaireMax;
        } else {
            montantJournalier = salaireJournalier;
        }
        return montantJournalier.multiply(BigDecimal.valueOf(2));
    }

    @Override
    BigDecimal calculMontantTotal(BigDecimal salaireJournalier, int nbJourSolde) {
        nbJourPrisEnCompte = getNbJourOuvrable(nbJourSolde);
        return salaireJournalier.multiply(BigDecimal.valueOf(nbJourPrisEnCompte));
    }
    
    private Integer getNbJourOuvrable(int nbJourSolde) {
        if(nbJourSolde > 31) {
            return joursOuvrables[30];
        }
        return joursOuvrables[nbJourSolde-1];
        
    }
    
}
