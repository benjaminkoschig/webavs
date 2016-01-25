package ch.globaz.amal.business.calcul;

import java.util.LinkedHashMap;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;

/**
 * Cette classe contient des informations complémentaire pour la simulation et le calcul des subsides
 * 
 * Ces informations seront affichées sur la page de simulation des subsides dans le tableau.
 * 
 * @author CBU
 * @version 17.06.2011
 */
public class AmalSubsideContainer {
    private String anneeSubside = null;
    private String contribuableName = null;
    private String idRevenuPrisEnCompte = null;
    private LinkedHashMap<String, SimpleDetailFamille> mapSimpleDetailFamille = null;
    private String montantContributionPCAssisteTotal = null;
    private String montantContributionsTotal = null;
    private String montantSupplementContribution = null;
    private int returnCode = 0;
    private Boolean revenuPriseEnCompteIsRDU = null;
    private String revenuPrisEnCompte = null;
    private RevenuHistoriqueComplex selectedRevenu = null;

    public AmalSubsideContainer() {
        montantContributionPCAssisteTotal = "";
        montantContributionsTotal = "";
        montantSupplementContribution = "";
        revenuPrisEnCompte = "";
        mapSimpleDetailFamille = new LinkedHashMap<String, SimpleDetailFamille>();
    }

    /**
     * @return the anneeSubside
     */
    public String getAnneeSubside() {
        return anneeSubside;
    }

    public String getContribuableName() {
        return contribuableName;
    }

    /**
     * @return the idRevenuPrisEnCompte
     */
    public String getIdRevenuPrisEnCompte() {
        return idRevenuPrisEnCompte;
    }

    public LinkedHashMap<String, SimpleDetailFamille> getMapSimpleDetailFamille() {
        return mapSimpleDetailFamille;
    }

    public String getMontantContributionPCAssisteTotal() {
        return montantContributionPCAssisteTotal;
    }

    public String getMontantContributionsTotal() {
        return montantContributionsTotal;
    }

    public String getMontantSupplementContribution() {
        return montantSupplementContribution;
    }

    public int getReturnCode() {
        return returnCode;
    }

    /**
     * @return the revenuPriseEnCompteIsRDU
     */
    public Boolean getRevenuPriseEnCompteIsRDU() {
        return revenuPriseEnCompteIsRDU;
    }

    public String getRevenuPrisEnCompte() {
        return revenuPrisEnCompte;
    }

    /**
     * @return the selectedRevenu
     */
    public RevenuHistoriqueComplex getSelectedRevenu() {
        return selectedRevenu;
    }

    /**
     * @param anneeSubside
     *            the anneeSubside to set
     */
    public void setAnneeSubside(String anneeSubside) {
        this.anneeSubside = anneeSubside;
    }

    public void setContribuableName(String contribuableName) {
        this.contribuableName = contribuableName;
    }

    /**
     * @param idRevenuPrisEnCompte
     *            the idRevenuPrisEnCompte to set
     */
    public void setIdRevenuPrisEnCompte(String idRevenuPrisEnCompte) {
        this.idRevenuPrisEnCompte = idRevenuPrisEnCompte;
    }

    public void setMapSimpleDetailFamille(LinkedHashMap<String, SimpleDetailFamille> mapSimpleDetailFamille) {
        this.mapSimpleDetailFamille = mapSimpleDetailFamille;
    }

    public void setMontantContributionPCAssisteTotal(String montantContributionPCAssisteTotal) {
        this.montantContributionPCAssisteTotal = montantContributionPCAssisteTotal;
    }

    public void setMontantContributionsTotal(String montantContributionsTotal) {
        this.montantContributionsTotal = montantContributionsTotal;
    }

    public void setMontantSupplementContribution(String montantSupplementContribution) {
        this.montantSupplementContribution = montantSupplementContribution;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @param revenuPriseEnCompteIsRDU
     *            the revenuPriseEnCompteIsRDU to set
     */
    public void setRevenuPriseEnCompteIsRDU(Boolean revenuPriseEnCompteIsRDU) {
        this.revenuPriseEnCompteIsRDU = revenuPriseEnCompteIsRDU;
    }

    public void setRevenuPrisEnCompte(String revenuPrisEnCompte) {
        this.revenuPrisEnCompte = revenuPrisEnCompte;
    }

    /**
     * @param selectedRevenu
     *            the selectedRevenu to set
     */
    public void setSelectedRevenu(RevenuHistoriqueComplex selectedRevenu) {
        this.selectedRevenu = selectedRevenu;
    }
}
