package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author EBKO
 */
public class SimpleSejourMoisPartielHome extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDonneeFinanciereHeader = null;
    private String idSejourMoisPartielHome = null;

    private String prixJournalier = null;
    private String fraisNourriture = null;
    private String nbJours = null;

    private String idHome = null;
    private String idTypeChambre = null;
    private String textLibre = null;
    private Boolean isVersementDirect = Boolean.FALSE;
    private String idAdressePaiement = null;

    @Override
    public String getId() {
        return idSejourMoisPartielHome;
    }

    @Override
    public void setId(String id) {
        idSejourMoisPartielHome = id;
    }

    public String getIdSejourMoisPartielHome() {
        return idSejourMoisPartielHome;
    }

    public void setIdSejourMoisPartielHome(String idSejourMoisPartielHome) {
        this.idSejourMoisPartielHome = idSejourMoisPartielHome;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public String getPrixJournalier() {
        return prixJournalier;
    }

    public void setPrixJournalier(String prixJournalier) {
        this.prixJournalier = prixJournalier;
    }

    public String getFraisNourriture() {
        return fraisNourriture;
    }

    public void setFraisNourriture(String fraisNourriture) {
        this.fraisNourriture = fraisNourriture;
    }

    public String getNbJours() {
        return nbJours;
    }

    public void setNbJours(String nbJours) {
        this.nbJours = nbJours;
    }

    public String getIdHome() {
        return idHome;
    }

    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    public String getIdTypeChambre() {
        return idTypeChambre;
    }

    public void setIdTypeChambre(String idTypeChambre) {
        this.idTypeChambre = idTypeChambre;
    }

    public String getTextLibre() {
        return textLibre;
    }

    public void setTextLibre(String textLibre) {
        this.textLibre = textLibre;
    }

    public Boolean getIsVersementDirect() {
        return isVersementDirect;
    }

    public void setIsVersementDirect(Boolean isVersementDirect) {
        this.isVersementDirect = isVersementDirect;
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }
}
