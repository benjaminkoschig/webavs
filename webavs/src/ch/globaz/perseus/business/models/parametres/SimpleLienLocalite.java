package ch.globaz.perseus.business.models.parametres;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleLienLocalite extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String dateFin = null;
    private String idLienLocalite = null;
    private String idLocalite = null;
    private String idZone = null;

    public SimpleLienLocalite() {
        super();
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idLienLocalite;
    }

    public String getIdLienLocalite() {
        return idLienLocalite;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public String getIdZone() {
        return idZone;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idLienLocalite = id;
    }

    public void setIdLienLocalite(String idLienLocalite) {
        this.idLienLocalite = idLienLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

}
