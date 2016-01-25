package globaz.cygnus.db.dossiers;

import globaz.jade.client.util.JadeDateUtil;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * Wrapper de période de contribution d'asistance AI. Utilisée par l'écran des dossiers RFM (InfoRom D0034) et dans la
 * saisie d'une demande de maintient à domicile.
 * </p>
 * <p>
 * L'implémentation de {@link Comparable} permet de trier une {@link List} par date de début (décroissant) puis par date
 * de fin (si pas de date de fin, sera mis en haut de la liste).
 * </p>
 * 
 * @see Collections#sort(List)
 * 
 * @author PBA
 */
public class RFPeriodeCAAIWrapper implements Comparable<RFPeriodeCAAIWrapper> {

    private String codeAPI;
    private String dateDebutCAAI;
    private String dateFinCAAI;
    private String idContributionAssistanceAI;
    private String montantAPI;
    private String montantCAAI;

    public RFPeriodeCAAIWrapper() {
        codeAPI = null;
        dateDebutCAAI = null;
        dateFinCAAI = null;
        idContributionAssistanceAI = null;
        montantAPI = null;
        montantCAAI = null;
    }

    @Override
    public int compareTo(RFPeriodeCAAIWrapper o) {
        if ((dateDebutCAAI != null) && !dateDebutCAAI.equals(o.dateDebutCAAI)) {
            if (JadeDateUtil.isDateAfter(dateDebutCAAI, o.dateDebutCAAI)) {
                return 1;
            } else if (JadeDateUtil.isDateBefore(dateDebutCAAI, o.dateDebutCAAI)) {
                return -1;
            }
        }
        if ((dateFinCAAI != null) && !dateFinCAAI.equals(o.dateFinCAAI)) {
            if (JadeDateUtil.isDateAfter(dateFinCAAI, o.getDateFinCAAI())) {
                return 1;
            } else if (JadeDateUtil.isDateBefore(dateFinCAAI, o.getDateFinCAAI())) {
                return -1;
            }
        }
        return 0;
    }

    public String getCodeAPI() {
        return codeAPI;
    }

    public String getDateDebutCAAI() {
        return dateDebutCAAI;
    }

    public String getDateFinCAAI() {
        return dateFinCAAI;
    }

    public String getIdContributionAssistanceAI() {
        return idContributionAssistanceAI;
    }

    public String getMontantAPI() {
        return montantAPI;
    }

    public String getMontantCAAI() {
        return montantCAAI;
    }

    public void setCodeAPI(String codeAPI) {
        this.codeAPI = codeAPI;
    }

    public void setDateDebutCAAI(String dateDebutCAAI) {
        this.dateDebutCAAI = dateDebutCAAI;
    }

    public void setDateFinCAAI(String dateFinCAAI) {
        this.dateFinCAAI = dateFinCAAI;
    }

    public void setIdContributionAssistanceAI(String idContributionAssistanceAI) {
        this.idContributionAssistanceAI = idContributionAssistanceAI;
    }

    public void setMontantAPI(String montantAPI) {
        this.montantAPI = montantAPI;
    }

    public void setMontantCAAI(String montantCAAI) {
        this.montantCAAI = montantCAAI;
    }
}
