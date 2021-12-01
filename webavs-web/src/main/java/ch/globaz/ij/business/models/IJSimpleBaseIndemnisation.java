package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;
import lombok.Getter;
import lombok.Setter;

public class IJSimpleBaseIndemnisation extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csCantonImposition;

    /**
     * Date de d�but Associ� au champ : XKDDEB
     */
    private String dateDeDebut;
    /**
     * Date de d�but Associ� au champ : XKDFIN
     */
    private String dateDeFin;

    /**
     * Etat de la base d'ind�mnisation Liste des codes syst�mes : 52412001 - OUVERT 52412002 - VALIDE 52412003 -
     * COMMUNIQUE 52412004 - ANNULE
     */
    private String etatCS;

    /**
     * Id de la base d'indemnisation Associ� au champ : XKIBIN
     */
    private String idBaseIndemnisation;

    /**
     * Id du parent
     */
    private String idParent;

    private String idPrononce;

    /**
     * Nombres de jours externes Associ� au champ : XKNJIN
     */
    private String joursExternes;

    /**
     * Nombres de jours externes Associ� au champ : XKNJEX
     */
    private String joursInternes;

    /**
     * Nombres de jours d'Interruption Associ� au champ : XKNJOI
     */
    private String joursInterruption;

    /**
     * Nombres de jours d'Interruption Associ� au champ : XKNJOI
     */
    @Getter
    @Setter
    private String joursNonCouverts;

    /**
     * Motif d'Interruption Associ� au champ : XKTMOI
     */
    private String motifInterruption;
    private String remarque;
    private String tauxImposition;

    public IJSimpleBaseIndemnisation() {
        dateDeDebut = "";
        dateDeFin = "";
        etatCS = "";
        idBaseIndemnisation = "";
        idParent = "";
        joursExternes = "";
        joursInternes = "";
        joursInterruption = "";
        joursNonCouverts = "";
        motifInterruption = "";
        idPrononce = "";
        csCantonImposition = "";
        tauxImposition = "";
        remarque = "";
    }

    public String getCsCantonImposition() {
        return csCantonImposition;
    }

    /**
     * @return the dateDeDebut
     */
    public String getDateDeDebut() {
        return dateDeDebut;
    }

    /**
     * @return the dateDeFin
     */
    public String getDateDeFin() {
        return dateDeFin;
    }

    public final String getEtatCS() {
        return etatCS;
    }

    /**
     * Retourne l'id de la base d'indemnisation
     */
    @Override
    public String getId() {
        return idBaseIndemnisation;
    }

    /**
     * Retourne l'id de la base d'indemnisation Similaire � getId();
     * 
     * @return id de la base d'indemnisation
     */
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    public String getIdParent() {
        return idParent;
    }

    public final String getIdPrononce() {
        return idPrononce;
    }

    /**
     * @return the joursExternes
     */
    public String getJoursExternes() {
        return joursExternes;
    }

    /**
     * @return the joursStringernes
     */
    public String getJoursInternes() {
        return joursInternes;
    }

    /**
     * @return the joursInterruption
     */
    public String getJoursInterruption() {
        return joursInterruption;
    }

    /**
     * @return the motifInterruption
     */
    public String getMotifInterruption() {
        return motifInterruption;
    }

    public String getRemarque() {
        return remarque;
    }

    public String getTauxImposition() {
        return tauxImposition;
    }

    public void setCsCantonImposition(String cantonImposition) {
        csCantonImposition = cantonImposition;
    }

    /**
     * @param dateDeDebut
     */
    public void setDateDeDebut(String dateDeDebut) {
        this.dateDeDebut = dateDeDebut;
    }

    /**
     * @param dateDeFin
     */
    public void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }

    public final void setEtatCS(String etatCS) {
        this.etatCS = etatCS;
    }

    /**
     * D�finit l'id de la base d'indemnisation
     */
    @Override
    public void setId(String id) {
        idBaseIndemnisation = id;
    }

    /**
     * D�finit l'id de la base d'indemnisation Similaire � setId(String id);
     * 
     * @param idBaseIndemnisation
     *            id de la base d'indemnisation
     */
    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        this.idBaseIndemnisation = idBaseIndemnisation;
    }

    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    public final void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    /**
     * @param joursExternes
     */
    public void setJoursExternes(String joursExternes) {
        this.joursExternes = joursExternes;
    }

    /**
     * @param joursInternes
     */
    public void setJoursInternes(String joursInternes) {
        this.joursInternes = joursInternes;
    }

    public void setJoursInterruption(String joursInterruption) {
        this.joursInterruption = joursInterruption;
    }

    public void setMotifInterruption(String motifInterruption) {
        this.motifInterruption = motifInterruption;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }
}
