package ch.globaz.vulpecula.external.models.affiliation;

import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class SuiviCaisse {
    private String id;
    private Periode periode;
    private String genreCaisse;

    private String idTiersCaisse;

    // private Employeur employeur;
    private Administration caisse;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the periode
     */
    public Periode getPeriode() {
        return periode;
    }

    /**
     * @return the genreCaisse
     */
    public String getGenreCaisse() {
        return genreCaisse;
    }

    /**
     * @return the idTiersCaisse
     */
    public String getIdTiersCaisse() {
        return idTiersCaisse;
    }

    /**
     * @return the caisse
     */
    public Administration getCaisse() {
        return caisse;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param periode the periode to set
     */
    public void setPeriode(Periode periode) {
        this.periode = periode;
    }

    /**
     * @param genreCaisse the genreCaisse to set
     */
    public void setGenreCaisse(String genreCaisse) {
        this.genreCaisse = genreCaisse;
    }

    /**
     * @param idTiersCaisse the idTiersCaisse to set
     */
    public void setIdTiersCaisse(String idTiersCaisse) {
        this.idTiersCaisse = idTiersCaisse;
    }

    /**
     * @param caisse the caisse to set
     */
    public void setCaisse(Administration caisse) {
        this.caisse = caisse;
    }
}
