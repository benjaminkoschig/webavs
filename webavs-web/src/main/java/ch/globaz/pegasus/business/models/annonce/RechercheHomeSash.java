/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author eco
 */
public class RechercheHomeSash extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csServiceEtat = null;
    private String idDroitMembreFamille = null;
    private String idSimplePeriodeServiceEtat = null;
    private String idTaxeJournaliereHome = null;
    private String noVersion = null;
    private String csDestinationSortie = null;
    private String dateDebut = null;
    private String dateFin = null;

    public RechercheHomeSash() {
        super();
    }

    public String getCsServiceEtat() {
        return csServiceEtat;
    }

    @Override
    public String getId() {
        return idSimplePeriodeServiceEtat;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public String getIdSimplePeriodeServiceEtat() {
        return idSimplePeriodeServiceEtat;
    }

    public String getIdTaxeJournaliereHome() {
        return idTaxeJournaliereHome;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCsServiceEtat(String csServiceEtat) {
        this.csServiceEtat = csServiceEtat;
    }

    @Override
    public void setId(String id) {
        idSimplePeriodeServiceEtat = id;
    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    public void setIdSimplePeriodeServiceEtat(String idSimplePeriodeServiceEtat) {
        this.idSimplePeriodeServiceEtat = idSimplePeriodeServiceEtat;
    }

    public void setIdTaxeJournaliereHome(String idTaxeJournaliereHome) {
        this.idTaxeJournaliereHome = idTaxeJournaliereHome;
    }

    @Override
    public void setSpy(String spy) {

    }

    public String getNoVersion() {
        return noVersion;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public String getCsDestinationSortie() {
        return csDestinationSortie;
    }

    public void setCsDestinationSortie(String csDestinationSortie) {
        this.csDestinationSortie = csDestinationSortie;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

}
