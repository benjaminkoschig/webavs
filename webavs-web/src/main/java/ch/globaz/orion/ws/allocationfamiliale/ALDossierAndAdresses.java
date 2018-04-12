package ch.globaz.orion.ws.allocationfamiliale;

import java.util.Date;

/**
 * Représente un dossier AF au niveau de Orion
 * 
 * 
 */
public class ALDossierAndAdresses {
    String nssAllocataire;
    String nomAllocataire;
    String prenomAllocataire;
    Integer numeroDossier;
    ALDossierEtat etatDossier;
    Date dateRadiation;
    ALAdresse adresseDomicile;
    ALAdresse adresseCourrier;

    public ALDossierAndAdresses() {
        super();
    }

    public ALDossierAndAdresses(String nssAllocataire, String nomAllocataire, String prenomAllocataire,
            Integer numeroDossier, ALDossierEtat etatDossier, Date dateRadiation, ALAdresse adresseDomicile,
            ALAdresse adresseCourrier) {
        super();
        this.nssAllocataire = nssAllocataire;
        this.nomAllocataire = nomAllocataire;
        this.prenomAllocataire = prenomAllocataire;
        this.numeroDossier = numeroDossier;
        this.etatDossier = etatDossier;
        this.dateRadiation = dateRadiation;
        this.adresseDomicile = adresseDomicile;
        this.adresseCourrier = adresseCourrier;
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

    public ALAdresse getAdresseDomicile() {
        return adresseDomicile;
    }

    public void setAdresseDomicile(ALAdresse adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
    }

    public ALAdresse getAdresseCourrier() {
        return adresseCourrier;
    }

    public void setAdresseCourrier(ALAdresse adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

}
