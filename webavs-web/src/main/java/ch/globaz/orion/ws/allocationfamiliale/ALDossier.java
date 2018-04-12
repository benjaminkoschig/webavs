package ch.globaz.orion.ws.allocationfamiliale;

import java.util.Date;

/**
 * Représente un dossier AF au niveau de Orion
 * 
 * 
 */
public class ALDossier {
    String nssAllocataire;
    String nomAllocataire;
    String prenomAllocataire;
    Integer numeroDossier;
    ALDossierEtat etatDossier;
    Date dateRadiation;

    public ALDossier() {
        super();
    }

    public ALDossier(String nssAllocataire, String nomAllocataire, String prenomAllocataire, Integer numeroDossier,
            ALDossierEtat etatDossier, Date dateRadiation) {
        super();
        this.nssAllocataire = nssAllocataire;
        this.nomAllocataire = nomAllocataire;
        this.prenomAllocataire = prenomAllocataire;
        this.numeroDossier = numeroDossier;
        this.etatDossier = etatDossier;
        this.dateRadiation = dateRadiation;
    }

    public String getNssAllocataire() {
        return nssAllocataire;
    }

    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    public String getNomAllocataire() {
        return nomAllocataire;
    }

    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    public Integer getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(Integer numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public ALDossierEtat getEtatDossier() {
        return etatDossier;
    }

    public void setEtatDossier(ALDossierEtat etatDossier) {
        this.etatDossier = etatDossier;
    }

    public Date getDateRadiation() {
        return dateRadiation;
    }

    public void setDateRadiation(Date dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

}
