package globaz.cygnus.services.comptabilite;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fha
 */
public class RFPrestationData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String datePrestation = "";
    private String etatPrestation = "";
    private String idAdresseDePaiement = "";
    private String idDecision = "";
    private String idLot = "";
    private String idPrestation = "";
    private String idTiersBeneficiaire = "";
    private Boolean isLAPRAMS = Boolean.FALSE;
    private Boolean isRI = Boolean.FALSE;
    private String montantPrestation = "";
    private Set<RFOrdreVersementData> ordresVersement = new HashSet<RFOrdreVersementData>();
    private String remboursementConjoint = "";
    private String remboursementRequerant = "";
    private String typePrestation = "";
    private String csGenrePrestation = "";

    public String getCsGenrePrestation() {
        return csGenrePrestation;
    }

    public void setCsGenrePrestation(String csGenrePrestation) {
        this.csGenrePrestation = csGenrePrestation;
    }

    public RFPrestationData(String idPrestation, String datePrestation, String montantPrestation, String idLot,
            String etatPrestation, Set<RFOrdreVersementData> ordresVersement, String typePrestation, String idDecision,
            String remboursementRequerant, String remboursementConjoint, Boolean isRI, Boolean isLAPRAMS,
            String idAdresseDePaiement, String idTiersBeneficiaire) {
        super();
        this.idPrestation = idPrestation;
        this.datePrestation = datePrestation;
        this.montantPrestation = montantPrestation;
        this.idLot = idLot;
        this.etatPrestation = etatPrestation;
        this.ordresVersement = ordresVersement;
        this.typePrestation = typePrestation;
        this.idDecision = idDecision;
        this.remboursementConjoint = remboursementConjoint;
        this.remboursementRequerant = remboursementRequerant;
        this.idTiersBeneficiaire = idTiersBeneficiaire;
        this.isRI = isRI;
        this.isLAPRAMS = isLAPRAMS;
        this.idAdresseDePaiement = idAdresseDePaiement;
    }

    public RFPrestationData(String idPrestation, String datePrestation, String montantPrestation, String idLot,
            String etatPrestation, Set<RFOrdreVersementData> ordresVersement, String typePrestation, String idDecision,
            String remboursementRequerant, String remboursementConjoint, Boolean isRI, Boolean isLAPRAMS,
            String idAdresseDePaiement, String idTiersBeneficiaire, String csGenrePrestation) {
        super();
        this.idPrestation = idPrestation;
        this.datePrestation = datePrestation;
        this.montantPrestation = montantPrestation;
        this.idLot = idLot;
        this.etatPrestation = etatPrestation;
        this.ordresVersement = ordresVersement;
        this.typePrestation = typePrestation;
        this.idDecision = idDecision;
        this.remboursementConjoint = remboursementConjoint;
        this.remboursementRequerant = remboursementRequerant;
        this.idTiersBeneficiaire = idTiersBeneficiaire;
        this.isRI = isRI;
        this.isLAPRAMS = isLAPRAMS;
        this.idAdresseDePaiement = idAdresseDePaiement;
        this.csGenrePrestation = csGenrePrestation;

    }

    public String getDatePrestation() {
        return datePrestation;
    }

    public String getEtatPrestation() {
        return etatPrestation;
    }

    public String getIdAdresseDePaiement() {
        return idAdresseDePaiement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Boolean getIsLAPRAMS() {
        return isLAPRAMS;
    }

    public Boolean getIsRI() {
        return isRI;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public Set<RFOrdreVersementData> getOrdresVersement() {
        return ordresVersement;
    }

    public String getRemboursementConjoint() {
        return remboursementConjoint;
    }

    public String getRemboursementRequerant() {
        return remboursementRequerant;
    }

    public String getTypePrestation() {
        return typePrestation;
    }

    public void setDatePrestation(String datePrestation) {
        this.datePrestation = datePrestation;
    }

    public void setEtatPrestation(String etatPrestation) {
        this.etatPrestation = etatPrestation;
    }

    public void setIdAdresseDePaiement(String idAdresseDePaiement) {
        this.idAdresseDePaiement = idAdresseDePaiement;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIsLAPRAMS(Boolean isLAPRAMS) {
        this.isLAPRAMS = isLAPRAMS;
    }

    public void setIsRI(Boolean isRI) {
        this.isRI = isRI;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setOrdresVersement(Set<RFOrdreVersementData> ordresVersement) {
        this.ordresVersement = ordresVersement;
    }

    public void setRemboursementConjoint(String remboursementConjoint) {
        this.remboursementConjoint = remboursementConjoint;
    }

    public void setRemboursementRequerant(String remboursementRequerant) {
        this.remboursementRequerant = remboursementRequerant;
    }

    public void setTypePrestation(String typePrestation) {
        this.typePrestation = typePrestation;
    }

}
