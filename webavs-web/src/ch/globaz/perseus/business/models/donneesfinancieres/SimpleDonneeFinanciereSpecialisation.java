package ch.globaz.perseus.business.models.donneesfinancieres;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDonneeFinanciereSpecialisation extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean avec13eme = false;
    private String dateCession = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idDonneeFinanciere = null;
    private String idDonneeFinanciereSpecialisation = null;
    private String listCsTypeRentes = null;
    private String nbPersonnesLogement = null;
    private String nomHoirie = null;
    private Boolean penurieLogement = true;
    private Boolean plusieursEmployeurs = false;
    private String tailleUniteAssistance = null;
    private String tauxOccupation = null;

    public SimpleDonneeFinanciereSpecialisation() {
        super();
    }

    /**
     * @return the avec13eme
     */
    public Boolean getAvec13eme() {
        return avec13eme;
    }

    /**
     * @return the dateCession
     */
    public String getDateCession() {
        return dateCession;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idDonneeFinanciereSpecialisation;
    }

    /**
     * @return the idDonneeFinanciere
     */
    public String getIdDonneeFinanciere() {
        return idDonneeFinanciere;
    }

    /**
     * @return the idDonneeFinanciereSpecialisation
     */
    public String getIdDonneeFinanciereSpecialisation() {
        return idDonneeFinanciereSpecialisation;
    }

    /**
     * @return the listCsTypeRentes
     */
    public String getListCsTypeRentes() {
        return listCsTypeRentes;
    }

    /**
     * @return the nbPersonnesLogement
     */
    public String getNbPersonnesLogement() {
        return nbPersonnesLogement;
    }

    /**
     * @return the nomHoirie
     */
    public String getNomHoirie() {
        return nomHoirie;
    }

    /**
     * @return the penurieLogement
     */
    public Boolean getPenurieLogement() {
        return penurieLogement;
    }

    /**
     * @return the plusieursEmployeurs
     */
    public Boolean getPlusieursEmployeurs() {
        return plusieursEmployeurs;
    }

    /**
     * @return the tailleUniteAssistance
     */
    public String getTailleUniteAssistance() {
        return tailleUniteAssistance;
    }

    /**
     * @return the tauxOccupation
     */
    public String getTauxOccupation() {
        return tauxOccupation;
    }

    /**
     * @param avec13eme
     *            the avec13eme to set
     */
    public void setAvec13eme(Boolean avec13eme) {
        this.avec13eme = avec13eme;
    }

    /**
     * @param dateCession
     *            the dateCession to set
     */
    public void setDateCession(String dateCession) {
        this.dateCession = dateCession;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idDonneeFinanciereSpecialisation = id;
    }

    /**
     * @param idDonneeFinanciere
     *            the idDonneeFinanciere to set
     */
    public void setIdDonneeFinanciere(String idDonneeFinanciere) {
        this.idDonneeFinanciere = idDonneeFinanciere;
    }

    /**
     * @param idDonneeFinanciereSpecialisation
     *            the idDonneeFinanciereSpecialisation to set
     */
    public void setIdDonneeFinanciereSpecialisation(String idDonneeFinanciereSpecialisation) {
        this.idDonneeFinanciereSpecialisation = idDonneeFinanciereSpecialisation;
    }

    /**
     * @param listCsTypeRentes
     *            the listCsTypeRentes to set
     */
    public void setListCsTypeRentes(String listCsTypeRentes) {
        this.listCsTypeRentes = listCsTypeRentes;
    }

    /**
     * @param nbPersonnesLogement
     *            the nbPersonnesLogement to set
     */
    public void setNbPersonnesLogement(String nbPersonnesLogement) {
        this.nbPersonnesLogement = nbPersonnesLogement;
    }

    /**
     * @param nomHoirie
     *            the nomHoirie to set
     */
    public void setNomHoirie(String nomHoirie) {
        this.nomHoirie = nomHoirie;
    }

    /**
     * @param penurieLogement
     *            the penurieLogement to set
     */
    public void setPenurieLogement(Boolean penurieLogement) {
        this.penurieLogement = penurieLogement;
    }

    /**
     * @param plusieursEmployeurs
     *            the plusieursEmployeurs to set
     */
    public void setPlusieursEmployeurs(Boolean plusieursEmployeurs) {
        this.plusieursEmployeurs = plusieursEmployeurs;
    }

    /**
     * @param tailleUniteAssistance
     *            the tailleUniteAssistance to set
     */
    public void setTailleUniteAssistance(String tailleUniteAssistance) {
        this.tailleUniteAssistance = tailleUniteAssistance;
    }

    /**
     * @param tauxOccupation
     *            the tauxOccupation to set
     */
    public void setTauxOccupation(String tauxOccupation) {
        this.tauxOccupation = tauxOccupation;
    }

}
