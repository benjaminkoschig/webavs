package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimpleCotisationsPsal extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEcheance = null;
    private String idCaisseCompensation = null;
    private String idCotisationsPsal = null;
    private String idDonneeFinanciereHeader = null;
    private String montantCotisationsAnnuelles = null;

    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idCotisationsPsal;
    }

    public String getIdCaisseCompensation() {
        return idCaisseCompensation;
    }

    public String getIdCotisationsPsal() {
        return idCotisationsPsal;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getMontantCotisationsAnnuelles() {
        return montantCotisationsAnnuelles;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idCotisationsPsal = id;
    }

    public void setIdCaisseCompensation(String idCaisseCompensation) {
        this.idCaisseCompensation = idCaisseCompensation;
    }

    public void setIdCotisationsPsal(String idCotisationsPsal) {
        this.idCotisationsPsal = idCotisationsPsal;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setMontantCotisationsAnnuelles(String montantCotisationsAnnuelles) {
        this.montantCotisationsAnnuelles = montantCotisationsAnnuelles;
    }

}
