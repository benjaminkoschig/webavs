package globaz.corvus.vb.process;

import globaz.babel.api.ICTDocument;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.db.creances.RECreancier;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.creances.RECreancierViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REGenererDemandeCompensationViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private Boolean afficherTauxInv = Boolean.FALSE;
    private String commentaires;
    private String displaySendToGed = "0";
    private ICTDocument document;
    private ICTDocument documentHelper;

    private String eMailAddress = "";

    private String idCreancier = "";
    private String idDemandeRente = "";
    private Boolean isImprimerTous = Boolean.FALSE;
    private Boolean isSendToGed = Boolean.FALSE;

    private String moisAnnee = "";
    private String montant1 = "";
    private String montant2 = "";
    private String montant3 = "";

    private String montant4 = "";
    private String texte1 = "";

    private String texte2 = "";
    private String texte3 = "";
    private String texte4 = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private void chargementCatalogueTexte() throws Exception {

        // Chargement du catalogue de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
        documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_DEMANDE_COMPENSATION);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);

        // Langue de l'assuré
        RECreancier creancier = new RECreancier();
        creancier.setSession(getSession());
        creancier.setIdCreancier(getIdCreancier());
        creancier.retrieve();

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(getSession());
        demandeRente.setIdDemandeRente(getIdDemandeRente());
        demandeRente.retrieve();

        PRDemande demandePrest = new PRDemande();
        demandePrest.setSession(getSession());
        demandePrest.setIdDemande(demandeRente.getIdDemandePrestation());
        demandePrest.retrieve();

        PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), demandePrest.getIdTiers());

        if (null != tier) {
            documentHelper.setCodeIsoLangue(PRUtil.getISOLangueTiers(getSession().getCode(
                    tier.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));
        } else {
            documentHelper.setCodeIsoLangue(getSession().getUserInfo().getLanguage());
        }

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            document = documents[0];
        }

    }

    public Boolean getAfficherTauxInv() {
        return afficherTauxInv;
    }

    public String getCommentaires() {
        return commentaires;
    }

    /**
     * getter pour la description d'un créancier
     * 
     * @throws Exception
     */
    public String getDescriptionCreancier(String idCreancier) throws Exception {

        setIdCreancier(idCreancier);

        String descriptionCreancier = "";

        // Retrieve du créancier
        RECreancierViewBean creancier = new RECreancierViewBean();
        creancier.setSession(getSession());
        creancier.setIdCreancier(idCreancier);
        creancier.retrieve();

        descriptionCreancier = creancier.getTiersNomPrenom();

        if (JadeStringUtil.isBlankOrZero(descriptionCreancier)) {
            descriptionCreancier = getSession().getLabel("JSP_GDC_D_CREANCIER");
        }

        return descriptionCreancier;
    }

    public String getDisplaySendToGed() {
        return displaySendToGed;
    }

    /**
     * getter pour l'attribut EMail address
     * 
     * @return la valeur courante de l'attribut EMail address
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getIdCreancier() {
        return idCreancier;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public Boolean getIsImprimerTous() {
        return isImprimerTous;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String[] getMoisDefault() {

        String[] moisDefault = { REPmtMensuel.getDateDernierPmt(getSession()),
                REPmtMensuel.getDateProchainPmt(getSession()) };

        return moisDefault;
    }

    public String getMontant1() {
        return montant1;
    }

    public String getMontant2() {
        return montant2;
    }

    public String getMontant3() {
        return montant3;
    }

    public String getMontant4() {
        return montant4;
    }

    public String getTexte1() {
        return texte1;
    }

    public String getTexte1jsp() throws Exception {
        chargementCatalogueTexte();
        texte1 = document.getTextes(6).getTexte(9).getDescriptionBrut();
        return texte1;
    }

    public String getTexte2() {
        return texte2;
    }

    public String getTexte2jsp() {
        texte2 = document.getTextes(6).getTexte(11).getDescriptionBrut();
        return texte2;
    }

    public String getTexte3() {
        return texte3;
    }

    public String getTexte3jsp() {
        texte3 = document.getTextes(6).getTexte(16).getDescriptionBrut();
        return texte3;
    }

    public String getTexte4() {
        return texte4;
    }

    public void setAfficherTauxInv(Boolean afficherTauxInv) {
        this.afficherTauxInv = afficherTauxInv;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public void setDisplaySendToGed(String displaySendToGed) {
        this.displaySendToGed = displaySendToGed;
    }

    /**
     * setter pour l'attribut EMail address
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setEMailAddress(String string) {
        eMailAddress = string;
    }

    public void setIdCreancier(String idCreancier) {
        this.idCreancier = idCreancier;
    }

    public void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIsImprimerTous(Boolean isImprimerTous) {
        this.isImprimerTous = isImprimerTous;
    }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setMontant1(String montant1) {
        this.montant1 = montant1;
    }

    public void setMontant2(String montant2) {
        this.montant2 = montant2;
    }

    public void setMontant3(String montant3) {
        this.montant3 = montant3;
    }

    public void setMontant4(String montant4) {
        this.montant4 = montant4;
    }

    public void setTexte1(String texte1) {
        this.texte1 = texte1;
    }

    public void setTexte2(String texte2) {
        this.texte2 = texte2;
    }

    public void setTexte3(String texte3) {
        this.texte3 = texte3;
    }

    public void setTexte4(String texte4) {
        this.texte4 = texte4;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
