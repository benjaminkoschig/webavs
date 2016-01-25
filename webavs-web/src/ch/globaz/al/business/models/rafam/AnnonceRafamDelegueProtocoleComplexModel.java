package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe d'une annonce RAFAM déléguépour le protocole
 * 
 * @author gmo
 * 
 */
public class AnnonceRafamDelegueProtocoleComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idAnnonce = null;
    private String naissanceEnfant = null;
    private String nomAllocataire = null;
    private String nomEnfant = null;
    private String nssAllocataire = null;
    private String nssEnfant = null;
    private String prenomAllocataire = null;

    private String prenomEnfant = null;
    private String recordNumber = null;

    public AnnonceRafamDelegueProtocoleComplexModel() {
        super();

    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getNaissanceEnfant() {
        return naissanceEnfant;
    }

    public String getNomAllocataire() {
        return nomAllocataire;
    }

    public String getNomEnfant() {
        return nomEnfant;
    }

    public String getNssAllocataire() {
        return nssAllocataire;
    }

    public String getNssEnfant() {
        return nssEnfant;
    }

    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    public String getPrenomEnfant() {
        return prenomEnfant;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public void setNaissanceEnfant(String naissanceEnfant) {
        this.naissanceEnfant = naissanceEnfant;
    }

    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    public void setNomEnfant(String nomEnfant) {
        this.nomEnfant = nomEnfant;
    }

    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    public void setNssEnfant(String nssEnfant) {
        this.nssEnfant = nssEnfant;
    }

    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    public void setPrenomEnfant(String prenomEnfant) {
        this.prenomEnfant = prenomEnfant;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }

}
