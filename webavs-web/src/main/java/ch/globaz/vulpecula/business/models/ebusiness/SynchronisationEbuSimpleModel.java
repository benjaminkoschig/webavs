/**
 * 
 */
package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author sel
 * 
 */
public class SynchronisationEbuSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -6919039078282510074L;

    private String id;
    private String idDecompte;
    private String dateAjout;
    private String dateSynchronisation;
    private String idAnnonce;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the idDecompte
     */
    public String getIdDecompte() {
        return idDecompte;
    }

    /**
     * @param idDecompte the idDecompte to set
     */
    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    /**
     * @return the dateAjout
     */
    public String getDateAjout() {
        return dateAjout;
    }

    /**
     * @param dateAjout the dateAjout to set
     */
    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    /**
     * @return the dateSynchronisation
     */
    public String getDateSynchronisation() {
        return dateSynchronisation;
    }

    /**
     * @param dateSynchronisation the dateSynchronisation to set
     */
    public void setDateSynchronisation(String dateSynchronisation) {
        this.dateSynchronisation = dateSynchronisation;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

}
