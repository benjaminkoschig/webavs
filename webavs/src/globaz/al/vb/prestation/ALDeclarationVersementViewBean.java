package globaz.al.vb.prestation;

import globaz.al.process.declarationVersement.ALDeclarationVersementImpressionProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * 
 * ViewBean gérant le modèle permettant l'impression des déclarations de versement
 * 
 * @author PTA
 * 
 */
public class ALDeclarationVersementViewBean extends BJadePersistentObjectViewBean {

    /**
     * Date à faire figure sur le document de déclaration de versement
     */

    private String dateImpression = null;

    /**
     * identifiant du dossier
     */
    private String idDossier = null;

    /**
     * identifiant de l'affilié
     * 
     */
    private String numAffilie = null;

    /**
     * date Debut période
     */

    private String periodeDebut = null;
    /**
     * période fin prestation
     */
    private String periodeFin = null;

    // /**
    // * période de la prestation pour les frontaliers
    // */
    // private String periodePrestationFontaliers = null;
    //
    // /**
    // * periode pour la prestation pour un imposé à la source
    // */
    // private String periodePrestationImposeSource = null;

    // /**
    // * période prestation pour un non actifs
    // */
    // private String periodePrestationNonActifs = null;
    //
    // /**
    // * période de la prestation pour un non imposé à source
    // */
    // private String periodePrestationNonImposeSource = null;
    /**
     * prendre en compte les dossiers adi
     */
    private Boolean texteImpot = null;
    /**
     * type de déclaration de versemen
     */
    private String typeDeclarationVersement = null;

    /**
     * type de document
     */

    private String typeDocument = null;

    /**
     * Constructeur de la classe
     */
    public ALDeclarationVersementViewBean() {
        super();

    }

    @Override
    public void add() throws Exception {
        ALDeclarationVersementImpressionProcess declarationVersement = new ALDeclarationVersementImpressionProcess();
        declarationVersement.setDateImpression(dateImpression);
        declarationVersement.setNumAffilie(numAffilie);
        declarationVersement.setIdDossier(idDossier);
        declarationVersement.setDateDebut(periodeDebut);
        declarationVersement.setDateFin(periodeFin);
        declarationVersement.setTypeDeclarationVersement(typeDeclarationVersement);
        declarationVersement.setTypeDocument(typeDocument);
        declarationVersement.setTextImpot(texteImpot);

        declarationVersement.setSession(getSession());
        BProcessLauncher.start(declarationVersement, false);

    }

    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");
    }

    /**
     * @return the dateImpression
     */
    public String getDateImpression() {
        return dateImpression;
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return the periodeDebut
     */
    public String getPeriodeDebut() {
        return periodeDebut;
    }

    /**
     * @return the periodeFin
     */
    public String getPeriodeFin() {
        return periodeFin;
    }

    // /**
    // * @return the periodePrestationFontaliers
    // */
    // public String getPeriodePrestationFontaliers() {
    // return this.periodePrestationFontaliers;
    // }
    //
    // /**
    // * @return the periodePrestation
    // */
    // public String getPeriodePrestationImposeSource() {
    // return this.periodePrestationImposeSource;
    // }
    //
    // /**
    // * @return the periodePrestationNonActifs
    // */
    // public String getPeriodePrestationNonActifs() {
    // return this.periodePrestationNonActifs;
    // }
    //
    // /**
    // * @return the periodePrestationNonImposeSource
    // */
    // public String getPeriodePrestationNonImposeSource() {
    // return this.periodePrestationNonImposeSource;
    // }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * date fin Période
     */

    @Override
    public BSpy getSpy() {
        return null;
    }

    public Boolean getTexteImpot() {
        return texteImpot;
    }

    /**
     * @return the typeDeclarationVersement
     */
    public String getTypeDeclarationVersement() {
        return typeDeclarationVersement;
    }

    /**
     * @return the typeDocument
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");
    }

    /**
     * @param dateImpression
     *            the dateImpression to set
     */
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    @Override
    public void setId(String newId) {
        // DO NOTHING
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param numAffilie
     *            the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @param periodeDebut
     *            the periodeDebut to set
     */
    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    /**
     * @param periodeFin
     *            the periodeFin to set
     */
    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    /**
     * @param periodePrestationFontaliers
     *            the periodePrestationFontaliers to set
     */
    // public void setPeriodePrestationFontaliers(String periodePrestationFontaliers) {
    // this.periodePrestationFontaliers = periodePrestationFontaliers;
    // }
    //
    // /**
    // * @param periodePrestationImposeSource
    // * the periodePrestation to set
    // */
    // public void setPeriodePrestationImposeSource(String periodePrestationImposeSource) {
    // this.periodePrestationImposeSource = periodePrestationImposeSource;
    // }
    //
    // /**
    // * @param periodePrestationNonActifs
    // * the periodePrestationNonActifs to set
    // */
    // public void setPeriodePrestationNonActifs(String periodePrestationNonActifs) {
    // this.periodePrestationNonActifs = periodePrestationNonActifs;
    // }
    //
    // /**
    // * @param periodePrestationNonImposeSource
    // * the periodePrestationNonImposeSource to set
    // */
    // public void setPeriodePrestationNonImposeSource(String periodePrestationNonImposeSource) {
    // this.periodePrestationNonImposeSource = periodePrestationNonImposeSource;
    // }

    public void setTexteImpot(Boolean texteImpot) {
        this.texteImpot = texteImpot;
    }

    /**
     * @param typeDeclarationVersement
     *            the typeDeclarationVersement to set
     */
    public void setTypeDeclarationVersement(String typeDeclarationVersement) {
        this.typeDeclarationVersement = typeDeclarationVersement;
    }

    /**
     * @param typeDocument
     *            the typeDocument to set
     */
    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");
    }

}
