package ch.globaz.pegasus.business.vo.pcaccordee;

import java.io.Serializable;

/**
 * 
 * @author BSC
 * 
 */
public class PCAccordeeVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeRente = null;
    private String csGenrePC = null;
    private String csTypePC = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idTiers = null;
    private Boolean isCalculManuel = null;

    /**
     * Creation d'un PCAccordeeVO
     * 
     * @param csGenrePC
     * @param csTypePC
     * @param dateDebut
     * @param dateFin
     * @param codeRente
     * @param isCalculManuel
     */
    public PCAccordeeVO(String csGenrePC, String csTypePC, String dateDebut, String dateFin, String codeRente,
            Boolean isCalculManuel) {
        super();
        this.csGenrePC = csGenrePC;
        this.csTypePC = csTypePC;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.codeRente = codeRente;
        this.isCalculManuel = isCalculManuel;
    }

    /**
     * @return the codeRente
     */
    public String getCodeRente() {
        return codeRente;
    }

    /**
     * @return the csGenrePC
     */
    public String getCsGenrePC() {
        return csGenrePC;
    }

    /**
     * @return the csTypePC
     */
    public String getCsTypePC() {
        return csTypePC;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the isCalculManuel
     */
    public Boolean getIsCalculManuel() {
        return isCalculManuel;
    }

    public void setCodeRente(String codeRente) {
        this.codeRente = codeRente;
    }

    public void setCsGenrePC(String csGenrePC) {
        this.csGenrePC = csGenrePC;
    }

    public void setCsTypePC(String csTypePC) {
        this.csTypePC = csTypePC;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsCalculManuel(Boolean isCalculManuel) {
        this.isCalculManuel = isCalculManuel;
    }

}
