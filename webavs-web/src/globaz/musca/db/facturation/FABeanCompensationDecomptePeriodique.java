/**
 * 
 */
package globaz.musca.db.facturation;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author MMO
 * @since 28 juin 2012
 */

public class FABeanCompensationDecomptePeriodique {

    private Boolean compteAnnexeASurveiller = new Boolean(false);
    private String idPassage = "";
    private String idTiers = "";

    /**
     * mmo 29.06.2012 ATTENTION !!! les deux maps ci-dessous sont de type LinkedHashMap afin de garantir l'ordre
     * d'insertion.
     * 
     * L'ordre d'insertion est fixe. Il est défini par la méthode
     * FACompensationDecomptePeriodiqueManager._get_getOrder() qui place les sections avec le plus grand montant
     * disponible pour une compensation en premier afin d'optimiser le processus de compensations
     * 
     */
    private Map<String, FABeanCompensationDecomptePeriodiqueFacture> mapFacture = new LinkedHashMap<String, FABeanCompensationDecomptePeriodiqueFacture>();
    private Map<String, FABeanCompensationDecomptePeriodiqueSection> mapSection = new LinkedHashMap<String, FABeanCompensationDecomptePeriodiqueSection>();

    private String numeroAffilie = "";
    private String role = "";

    public Boolean getCompteAnnexeASurveiller() {
        return compteAnnexeASurveiller;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Map<String, FABeanCompensationDecomptePeriodiqueFacture> getMapFacture() {
        return mapFacture;
    }

    public Map<String, FABeanCompensationDecomptePeriodiqueSection> getMapSection() {
        return mapSection;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getRole() {
        return role;
    }

    public void setCompteAnnexeASurveiller(Boolean compteAnnexeASurveiller) {
        this.compteAnnexeASurveiller = compteAnnexeASurveiller;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
