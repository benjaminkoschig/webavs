package globaz.al.vb.decision;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.exceptions.decision.ALDecisionException;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.al.business.models.dossier.CommentaireModel;
import ch.globaz.al.business.models.dossier.CommentaireSearchModel;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Viewbean g�rant un mod�le repr�sentant une d�cision
 * 
 * @author JER
 */
public class ALDecisionViewBean extends BJadePersistentObjectViewBean {
    /**
     * Model simple repr�sentant un commentaire de d�cision
     */
    private CommentaireModel commentaireModel = null;
    /**
     * Mod�le complexe repr�sentant une copie de d�cision
     */
    private CopieComplexModel copieComplexModel = null;

    /**
     * Mod�le de recherche pour les copies de d�cision
     */
    private CopieComplexSearchModel copieComplexSearchModel = null;

    /**
     * La date d'impression du document
     */
    private String dateImpression = null;

    /**
     * Mod�le complexe repr�sentant un dossier AF
     */
    private DossierDecisionComplexModel dossierDecisionComplexModel = null;

    /**
     * edition decision
     */
    private Boolean editionDecompteAvecDecision = true;

    /**
     * param�tre fromDecompte
     */
    private String fromDecompte = null;
    /**
     * l'id de la copie � delete d�finit via customAction
     */
    private String idCopieToDelete = null;

    /**
     * identifiant du d�compte adi � imprimer
     */
    private String idDecompteAdi = null;

    /**
     * Variable pour "recevoir" l'id du dossier (par param�tre(ex: imprimer depuis d�compte))
     */
    private String idDossier = null;

    /**
     * Cha�ne r�capitulative des cases � cocher
     */
    private String impressionBatchOverview = null;

    /**
     * indique si la d�cision est un aper�u
     */
    private boolean isPreview = true;
    /**
     * indique si la d�cision doit �tre envoy� en GED
     */
    private boolean isPrintGed = false;
    /**
     * Nombre de copie affich� � l'�cran
     */
    private int nbOfColumns;
    /**
     * Indexe de la copie s�lectionn�e par l'utilisateur
     */
    private int selectedIndex;

    /**
     * type de d�compte ADI
     */

    private String typeDecompte = null;

    /**
     * Constructeur de la classe
     */
    public ALDecisionViewBean() {
        super();
        commentaireModel = new CommentaireModel();
        dossierDecisionComplexModel = new DossierDecisionComplexModel();
        copieComplexSearchModel = new CopieComplexSearchModel();
        copieComplexModel = new CopieComplexModel();
        setIdDecompteAdi(idDecompteAdi);
        setFromDecompte(fromDecompte);

        setEditionDecompteAvecDecision(editionDecompteAvecDecision);
        setTypeDecompte(typeDecompte);
    }

    /**
     * Constructeur de la classe
     * 
     * @param _dossierDecisionComplexModel
     *            Un dossier d'allocation familiale
     */
    public ALDecisionViewBean(DossierDecisionComplexModel _dossierDecisionComplexModel) {
        super();
        commentaireModel = new CommentaireModel();
        dossierDecisionComplexModel = _dossierDecisionComplexModel;
        copieComplexSearchModel = new CopieComplexSearchModel();
        copieComplexModel = new CopieComplexModel();
        setIdDecompteAdi(idDecompteAdi);
        setFromDecompte(fromDecompte);

        setEditionDecompteAvecDecision(editionDecompteAvecDecision);
        setTypeDecompte(typeDecompte);
    }

    @Override
    public void add() throws Exception {
        throw new ALDecisionException("ALDecisionViewBean#Method add might be never called ");

    }

    @Override
    public void delete() throws Exception {
        throw new ALDecisionException("ALDecisionViewBean#Method delete might be never called ");

    }

    /**
     * @return Mod�le simple de commentaire
     */
    public CommentaireModel getCommentaireModel() {
        return commentaireModel;
    }

    /**
     * @param idx
     *            L'indexe de la copie
     * @return Le mod�le complexe de copie situ� � l'indexe
     */
    public CopieComplexModel getCopieAt(int idx) {
        return idx < copieComplexSearchModel.getSize() ? (CopieComplexModel) copieComplexSearchModel.getSearchResults()[idx]
                : new CopieComplexModel();
    }

    /**
     * @return Un mod�le complexe de copie
     */
    public CopieComplexModel getCopieComplexModel() {
        return copieComplexModel;
    }

    /**
     * @return Le mod�le de recherche des copies
     */
    public CopieComplexSearchModel getCopieComplexSearchModel() {
        return copieComplexSearchModel;
    }

    /**
     * @return String La date d'impression du document
     */
    public String getDateImpression() {
        return dateImpression;
    }

    /**
     * @return Un mod�le complexe du dossier AF
     */
    public DossierDecisionComplexModel getDossierDecisionComplexModel() {
        return dossierDecisionComplexModel;
    }

    /**
     * @return the editionDecompteAvecDecision
     */
    public Boolean getEditionDecompteAvecDecision() {
        return editionDecompteAvecDecision;
    }

    public String getFromDecompte() {
        return fromDecompte;
    }

    @Override
    public String getId() {
        return dossierDecisionComplexModel.getId();
    }

    /**
     * 
     * @return idCopieToDelete
     */
    public String getIdCopieToDelete() {
        return idCopieToDelete;
    }

    /**
     * @return the idDecompteAdi
     */
    public String getIdDecompteAdi() {
        return idDecompteAdi;
    }

    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return les r�sultats des cases � cocher si les copies sont imprim�es par le batch ou pas
     */
    public String getImpressionBatchOverview() {
        return impressionBatchOverview;
    }

    /**
     * Retourne le libell� � afficher pour la copie en question
     * 
     * @param idx
     *            index dans la copie dans la liste des copies � l'�cran
     * @return le libell�
     * @exception java.lang.Exception
     *                si la r�cup�ration du libell� a �chou�
     */
    public String getLibelleCopie(int idx) throws Exception {
        CopieComplexModel copie = null;
        if (idx < copieComplexSearchModel.getSize()) {
            copie = (CopieComplexModel) copieComplexSearchModel.getSearchResults()[idx];

        } else {
            copie = copieComplexModel;

        }
        if (JadeNumericUtil.isEmptyOrZero(copie.getCopieModel().getIdTiersDestinataire())) {
            return "";
        } else {
            // si on ne g�re pas l'exception, l'�cran ne s'affiche pas
            try {
                return ALServiceLocator.getCopiesBusinessService().getLibelleCopie(dossierDecisionComplexModel,
                        copie.getCopieModel().getIdTiersDestinataire(), ALCSCopie.TYPE_DECISION);
            } catch (ALDocumentAddressException e) {
                return "Adresse invalide pour le tiers choisi (" + copie.getCopieModel().getIdTiersDestinataire() + ")";
            }

        }
    }

    /**
     * @return Le nombre de copies actuellement charg�es � l'�cran
     */
    public int getNbOfColumns() {
        return nbOfColumns;
    }

    /**
     * @return L'indexe de la copie que l'utilisateur � s�lectionn�e
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @return La session en cours d'utilisation
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (dossierDecisionComplexModel != null) && !dossierDecisionComplexModel.isNew() ? new BSpy(
                dossierDecisionComplexModel.getSpy()) : new BSpy(getSession());
    }

    /**
     * @return La date du jour
     */
    public String getTodayDate() {
        Date date = new Date();
        return JadeDateUtil.getGlobazFormattedDate(date);
    }

    /**
     * @return the typeDecompte
     */
    public String getTypeDecompte() {
        return typeDecompte;
    }

    public String idDossierForDecompte(String idDecompte) throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {
        String idDossier = null;

        return idDossier = ALServiceLocator.getAdiDecompteComplexModelService().read(idDecompte).getDecompteAdiModel()
                .getIdDossier();
    }

    /**
     * @return isPrintGed , d�cision en GED ou pas
     */
    public boolean isPrintGed() {
        return isPrintGed;
    }

    public boolean isPrintPreview() {
        return isPreview;
    }

    @Override
    public void retrieve() throws Exception {

        // chargement du dossier fait dans le Helper

        // Chargement du texte libre du dossier
        CommentaireSearchModel commentaireSearch = new CommentaireSearchModel();
        commentaireSearch.setForIdDossier(dossierDecisionComplexModel.getId());
        commentaireSearch = ALServiceLocator.getCommentaireModelService().search(commentaireSearch);
        if (commentaireSearch.getSize() == 0) {
            commentaireModel = new CommentaireModel();
        } else {
            commentaireModel = (CommentaireModel) commentaireSearch.getSearchResults()[0];
        }

        // Chargement des copies li�es au dossier
        copieComplexSearchModel.setForIdDossier(dossierDecisionComplexModel.getId());
        copieComplexSearchModel.setForTypeCopie(ALCSCopie.TYPE_DECISION);
        copieComplexSearchModel = ALServiceLocator.getCopieComplexModelService().search(copieComplexSearchModel);

    }

    /**
     * @param commentaireModel
     *            Un mod�le de commentaire de d�cision
     */
    public void setCommentaireModel(CommentaireModel commentaireModel) {
        this.commentaireModel = commentaireModel;
    }

    /**
     * @param copieComplexModel
     *            Un mod�le complexe de copie de d�cision
     */
    public void setCopieComplexModel(CopieComplexModel copieComplexModel) {
        this.copieComplexModel = copieComplexModel;
    }

    /**
     * @param copieComplexSearchModel
     *            Un mod�le de recherche de copies de d�cision
     */
    public void setCopieComplexSearchModel(CopieComplexSearchModel copieComplexSearchModel) {
        this.copieComplexSearchModel = copieComplexSearchModel;
    }

    /**
     * @param dateImpression
     *            La date d'impression du document
     */
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /**
     * @param dossierDecisionComplexModel
     *            Un mod�le complexe de dossier AF
     */
    public void setDossierDecisionComplexModel(DossierDecisionComplexModel dossierDecisionComplexModel) {
        this.dossierDecisionComplexModel = dossierDecisionComplexModel;
    }

    /**
     * @param editionDecompteAvecDecision
     *            the editionDecompteAvecDecision to set
     */
    public void setEditionDecompteAvecDecision(Boolean editionDecompteAvecDecision) {
        this.editionDecompteAvecDecision = editionDecompteAvecDecision;
    }

    public void setFromDecompte(String fromDecompte) {
        this.fromDecompte = fromDecompte;
    }

    @Override
    public void setId(String newIdDossier) {
        dossierDecisionComplexModel.setId(newIdDossier);
    }

    /**
     * permet de r�cup�rer l'id de la copie � deleter selon la custom action
     */
    public void setIdCopieToDelete(String newId) {
        idCopieToDelete = newId;
    }

    /**
     * @param idDecompteAdi
     *            the idDecompteAdi to set
     */
    public void setIdDecompteAdi(String idDecompteAdi) {
        this.idDecompteAdi = idDecompteAdi;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param impressionBatchOverview
     *            les �tats des cases � cocher des copies
     */
    public void setImpressionBatchOverview(String impressionBatchOverview) {
        this.impressionBatchOverview = impressionBatchOverview;

    }

    /**
     * d�finit l'id tiers destinataire r�cup�r� de la s�l�ction via [...]
     * 
     * @param idDest
     *            l'id r�cup�r�
     */
    public void setNewIdTiersDestinataire(String idDest) {
        if (copieComplexSearchModel.getSize() > selectedIndex) {
            ((CopieComplexModel) copieComplexSearchModel.getSearchResults()[selectedIndex]).getCopieModel()
                    .setIdTiersDestinataire(idDest);
        } else {
            copieComplexModel.getCopieModel().setIdTiersDestinataire(idDest);
        }

    }

    /**
     * @param isPrintGed
     *            d�finie si la d�cision doit partir en GED ou pas
     */
    public void setPrintGed(String isPrintGed) {
        if ("true".equals(isPrintGed)) {
            this.isPrintGed = true;
        }
        if ("false".equals(isPrintGed)) {
            this.isPrintGed = false;
        }

    }

    public void setPrintPreview(String isPreview) {
        this.isPreview = new Boolean(isPreview);
    }

    /**
     * @param selectedIndex
     *            La copie s�lectionn�e par l'utilisateur
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * @param typeDecompte
     *            the typeDecompte to set
     */
    public void setTypeDecompte(String typeDecompte) {
        this.typeDecompte = typeDecompte;
    }

    @Override
    public void update() throws Exception {

        // On ne met pas � jour le mod�le complexe complet, mais uniquement le
        // dossierModel (pour le champ r�f�rence, seul champ �ditable)
        dossierDecisionComplexModel.setDossierModel(ALServiceLocator.getDossierModelService().update(
                dossierDecisionComplexModel.getDossierModel()));

        // Mise � jour du texte libre
        if (!JadeStringUtil.isEmpty(commentaireModel.getTexte())) {
            if (commentaireModel.isNew()) {
                ALServiceLocator.getCommentaireModelService().create(commentaireModel);
            } else {
                ALServiceLocator.getCommentaireModelService().update(commentaireModel);
            }
        } else if (!commentaireModel.isNew()) {
            ALServiceLocator.getCommentaireModelService().delete(commentaireModel);
        }

        // on r�cup�re la valeur de chaque case � cocher (stock� sous forme de
        // string)

        String delimiter = "\\,";
        String[] temp = impressionBatchOverview.split(delimiter);

        // Mise � jour des cases � cocher des copies
        for (int i = 0; i < copieComplexSearchModel.getSize(); i++) {
            CopieComplexModel copieComplexModel = ((CopieComplexModel) copieComplexSearchModel.getSearchResults()[i]);
            // modification de impression batch selon case � cocher
            copieComplexModel.getCopieModel().setImpressionBatch(Boolean.valueOf(temp[i]));
            ALServiceLocator.getCopieComplexModelService().update(copieComplexModel);
        }

        if (!JadeNumericUtil.isEmptyOrZero(getCopieComplexModel().getCopieModel().getIdTiersDestinataire())) {
            ALServiceLocator.getCopieComplexModelService().create(copieComplexModel);
        }
    }
}
