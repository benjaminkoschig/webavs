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
 * Viewbean gérant un modèle représentant une décision
 * 
 * @author JER
 */
public class ALDecisionViewBean extends BJadePersistentObjectViewBean {
    /**
     * Model simple représentant un commentaire de décision
     */
    private CommentaireModel commentaireModel = null;
    /**
     * Modèle complexe représentant une copie de décision
     */
    private CopieComplexModel copieComplexModel = null;

    /**
     * Modèle de recherche pour les copies de décision
     */
    private CopieComplexSearchModel copieComplexSearchModel = null;

    /**
     * La date d'impression du document
     */
    private String dateImpression = null;

    /**
     * Modèle complexe représentant un dossier AF
     */
    private DossierDecisionComplexModel dossierDecisionComplexModel = null;

    /**
     * edition decision
     */
    private Boolean editionDecompteAvecDecision = true;

    /**
     * paramètre fromDecompte
     */
    private String fromDecompte = null;
    /**
     * l'id de la copie à delete définit via customAction
     */
    private String idCopieToDelete = null;

    /**
     * identifiant du décompte adi à imprimer
     */
    private String idDecompteAdi = null;

    /**
     * Variable pour "recevoir" l'id du dossier (par paramètre(ex: imprimer depuis décompte))
     */
    private String idDossier = null;

    /**
     * Chaîne récapitulative des cases à cocher
     */
    private String impressionBatchOverview = null;

    /**
     * indique si la décision est un aperçu
     */
    private boolean isPreview = true;
    /**
     * indique si la décision doit être envoyé en GED
     */
    private boolean isPrintGed = false;
    /**
     * Nombre de copie affiché à l'écran
     */
    private int nbOfColumns;
    /**
     * Indexe de la copie sélectionnée par l'utilisateur
     */
    private int selectedIndex;

    /**
     * type de décompte ADI
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
     * @return Modèle simple de commentaire
     */
    public CommentaireModel getCommentaireModel() {
        return commentaireModel;
    }

    /**
     * @param idx
     *            L'indexe de la copie
     * @return Le modèle complexe de copie situé à l'indexe
     */
    public CopieComplexModel getCopieAt(int idx) {
        return idx < copieComplexSearchModel.getSize() ? (CopieComplexModel) copieComplexSearchModel.getSearchResults()[idx]
                : new CopieComplexModel();
    }

    /**
     * @return Un modèle complexe de copie
     */
    public CopieComplexModel getCopieComplexModel() {
        return copieComplexModel;
    }

    /**
     * @return Le modèle de recherche des copies
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
     * @return Un modèle complexe du dossier AF
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
     * @return les résultats des cases à cocher si les copies sont imprimées par le batch ou pas
     */
    public String getImpressionBatchOverview() {
        return impressionBatchOverview;
    }

    /**
     * Retourne le libellé à afficher pour la copie en question
     * 
     * @param idx
     *            index dans la copie dans la liste des copies à l'écran
     * @return le libellé
     * @exception java.lang.Exception
     *                si la récupération du libellé a échoué
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
            // si on ne gère pas l'exception, l'écran ne s'affiche pas
            try {
                return ALServiceLocator.getCopiesBusinessService().getLibelleCopie(dossierDecisionComplexModel,
                        copie.getCopieModel().getIdTiersDestinataire(), ALCSCopie.TYPE_DECISION);
            } catch (ALDocumentAddressException e) {
                return "Adresse invalide pour le tiers choisi (" + copie.getCopieModel().getIdTiersDestinataire() + ")";
            }

        }
    }

    /**
     * @return Le nombre de copies actuellement chargées à l'écran
     */
    public int getNbOfColumns() {
        return nbOfColumns;
    }

    /**
     * @return L'indexe de la copie que l'utilisateur à sélectionnée
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
     * @return isPrintGed , décision en GED ou pas
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

        // Chargement des copies liées au dossier
        copieComplexSearchModel.setForIdDossier(dossierDecisionComplexModel.getId());
        copieComplexSearchModel.setForTypeCopie(ALCSCopie.TYPE_DECISION);
        copieComplexSearchModel = ALServiceLocator.getCopieComplexModelService().search(copieComplexSearchModel);

    }

    /**
     * @param commentaireModel
     *            Un modèle de commentaire de décision
     */
    public void setCommentaireModel(CommentaireModel commentaireModel) {
        this.commentaireModel = commentaireModel;
    }

    /**
     * @param copieComplexModel
     *            Un modèle complexe de copie de décision
     */
    public void setCopieComplexModel(CopieComplexModel copieComplexModel) {
        this.copieComplexModel = copieComplexModel;
    }

    /**
     * @param copieComplexSearchModel
     *            Un modèle de recherche de copies de décision
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
     *            Un modèle complexe de dossier AF
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
     * permet de récupérer l'id de la copie à deleter selon la custom action
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
     *            les états des cases à cocher des copies
     */
    public void setImpressionBatchOverview(String impressionBatchOverview) {
        this.impressionBatchOverview = impressionBatchOverview;

    }

    /**
     * définit l'id tiers destinataire récupéré de la séléction via [...]
     * 
     * @param idDest
     *            l'id récupéré
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
     *            définie si la décision doit partir en GED ou pas
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
     *            La copie sélectionnée par l'utilisateur
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

        // On ne met pas à jour le modèle complexe complet, mais uniquement le
        // dossierModel (pour le champ référence, seul champ éditable)
        dossierDecisionComplexModel.setDossierModel(ALServiceLocator.getDossierModelService().update(
                dossierDecisionComplexModel.getDossierModel()));

        // Mise à jour du texte libre
        if (!JadeStringUtil.isEmpty(commentaireModel.getTexte())) {
            if (commentaireModel.isNew()) {
                ALServiceLocator.getCommentaireModelService().create(commentaireModel);
            } else {
                ALServiceLocator.getCommentaireModelService().update(commentaireModel);
            }
        } else if (!commentaireModel.isNew()) {
            ALServiceLocator.getCommentaireModelService().delete(commentaireModel);
        }

        // on récupère la valeur de chaque case à cocher (stocké sous forme de
        // string)

        String delimiter = "\\,";
        String[] temp = impressionBatchOverview.split(delimiter);

        // Mise à jour des cases à cocher des copies
        for (int i = 0; i < copieComplexSearchModel.getSize(); i++) {
            CopieComplexModel copieComplexModel = ((CopieComplexModel) copieComplexSearchModel.getSearchResults()[i]);
            // modification de impression batch selon case à cocher
            copieComplexModel.getCopieModel().setImpressionBatch(Boolean.valueOf(temp[i]));
            ALServiceLocator.getCopieComplexModelService().update(copieComplexModel);
        }

        if (!JadeNumericUtil.isEmptyOrZero(getCopieComplexModel().getCopieModel().getIdTiersDestinataire())) {
            ALServiceLocator.getCopieComplexModelService().create(copieComplexModel);
        }
    }
}
