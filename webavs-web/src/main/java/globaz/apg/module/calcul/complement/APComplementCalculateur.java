package globaz.apg.module.calcul.complement;

import java.math.BigDecimal;
import java.util.Map;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.module.calcul.constantes.ECanton;
import globaz.apg.module.calcul.constantes.EMontantsMax;
import globaz.globall.util.JANumberFormatter;

public abstract class APComplementCalculateur {
    
    protected BigDecimal montantJournalier;
    protected Integer nbJourPrisEnCompte;
    protected String dateDebut;
    protected Map<EMontantsMax, BigDecimal> montantsMax;
    protected ECanton canton;
    
    public APComplementCalculateur(Map<EMontantsMax, BigDecimal> montantsMax, ECanton canton, String dateDebut) {
        this.dateDebut = dateDebut;
        this.montantsMax = montantsMax;
        this.canton = canton;
    }
    
    public static APComplementCalculateur getCalculateur(Map<EMontantsMax, BigDecimal> montantsMax, ECanton canton, String dateDebut, String typeAllocation, int nbEnfant) {
        if(isRecrue(typeAllocation, nbEnfant)) {
            return new APComplementCalculRecrue(montantsMax, canton, dateDebut);
        } else {
            return new APComplementCalculAutre(montantsMax, canton, dateDebut);
        }
    }
    
    public BigDecimal calculerMontantCOMBIAB(BigDecimal salaireMensuel, int nbJourSolde) throws Exception {
        BigDecimal salaireJournalier = calculeSalaireJournalierCOMCIAB(salaireMensuel);
        return calculMontantTotal(salaireJournalier, nbJourSolde);
    }

    //ESVE MATERNITE MONTANT MAX
    public BigDecimal calculerMontantMATCIAB(BigDecimal salaireMensuel, int nbJourSolde) throws Exception {
        BigDecimal salaireJournalier = calculeSalaireJournalierMATCIAB(salaireMensuel);
        return calculMontantTotal(salaireJournalier, nbJourSolde);
    }
    
    abstract BigDecimal calculeSalaireJournalierCOMCIAB(BigDecimal salaireMensuel) throws Exception;
    abstract BigDecimal calculeSalaireJournalierMATCIAB(BigDecimal salaireMensuel) throws Exception;

    abstract BigDecimal calculMontantTotal(BigDecimal salairejournalier, int nbJourSolde);
    
    private static boolean isRecrue(String typeAllocation, int nbEnfant) {
        if(nbEnfant > 0) {
            return false;
        } else if (IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE.equals(typeAllocation) 
                || IAPDroitLAPG.CS_FORMATION_DE_BASE.equals(typeAllocation)
                || IAPDroitLAPG.CS_RECRUTEMENT.equals(typeAllocation)
                || IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES.equals(typeAllocation)
                ||IAPDroitLAPG.CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF.equals(typeAllocation)) {
            return true;
        }
        return false;
    }
    
    /**
     * arrondi à 2 chiffres après la virgule, à 5cts près.
     * 
     * @param montant
     * @return
     */
    public BigDecimal arrondir(BigDecimal montant) {
        // arrondi à 2 chiffres après la virgule, à 5cts près.
        return new BigDecimal(JANumberFormatter.deQuote(JANumberFormatter.format(montant.toString(), 0.05, 2,
                JANumberFormatter.NEAR)));
    }

    public BigDecimal getMontantJournalier() {
        return montantJournalier;
    }

    public Integer getNbJourPrisEnCompte() {
        return nbJourPrisEnCompte;
    }

}
