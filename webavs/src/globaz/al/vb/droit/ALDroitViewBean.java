package globaz.al.vb.droit;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import java.util.Date;
import org.apache.commons.lang.StringEscapeUtils;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.enumerations.droit.ALEnumMsgDroitPC;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * ViewBean g�rant le mod�le repr�sentant un droit complet.
 * 
 * @author GMO
 * 
 */
public class ALDroitViewBean extends BJadePersistentObjectViewBean {
    private static final int LINE_HEIGHT_MSG_POPUP = 70;
    private static final String URL_ADD_PAIEMENT_TIERS = "/pyxis?userAction=pyxis.adressepaiement.avoirPaiement.afficher&_method=add&back=_sl&idTiers=";

    private String addWarnings = "";
    /**
     * Repr�sente la valeur du b�n�ficiaire � l'entr�e dans l'�cran
     */
    private String beneficiaireAvantModification = null;

    /**
     * indique si le champ attestation � l'allocataire doit �tre visible sur l'�cran droit
     */
    private Boolean afficheAttesAlloc = null;
    /**
     * Mod�le du dossier auquel le droit appartient, utile pour infos alloc, et pour calcul �ch�ance
     */
    private DossierComplexModel dossierComplexModel = null;
    /**
     * Mod�le contenant le droit complet
     */
    private DroitComplexModel droitComplexModel = null;
    /**
     * Echeance calcul� du droit
     */
    private String echeanceCalculee = null;

    /**
     * Entier repr�sente le nb de droits pour lequel l'enfant li� au droit est utilis�
     */
    private int enfantAlreadyActif = 0;
    /**
	 * 
	 */
    private String idMessageDroitPC = null;

    /**
     * Indique si l'application est une caisse horlog�re
     */
    private Boolean isCaisseHorlogere = null;

    /**
     * Indique si l'application est configur�e pour g�rer le supp fnb
     */
    private Boolean isFnbActive = null;
    /**
     * Montant calcul� du droit
     */
    private String montantCalcule = null;

    /**
     * Constructeur du viewBean
     */
    public ALDroitViewBean() {
        super();
        droitComplexModel = new DroitComplexModel();
        dossierComplexModel = new DossierComplexModel();
    }

    /**
     * Constructeur du viewBean, avec initialisation selon un DroitComplexModel
     * 
     * @param droitComplexModel
     *            le droit � utiliser
     */
    public ALDroitViewBean(DroitComplexModel droitComplexModel) {
        super();
        this.droitComplexModel = droitComplexModel;
        dossierComplexModel = new DossierComplexModel();
    }

    /**
     * G�re l'ajout d'un droit depuis l'�cran
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // D�finit la date de d�but du droit
        droitComplexModel.getDroitModel().setDebutDroit(
                ALServiceLocator.getDatesEcheanceService().getDateDebutValiditeDroit(droitComplexModel,
                        dossierComplexModel.getDossierModel().getDebutValidite()));

        // mise � jour de la date de fin de droit
        droitComplexModel = ALServiceLocator.getDroitBusinessService().setDateFinDroitForce(droitComplexModel);
        echeanceCalculee = droitComplexModel.getDroitModel().getFinDroitForcee();

        // Caisse cantonale ? Propri�t� dans ALPARAM
        if (ALServiceLocator.getParametersServices().isCheckPCFamille()) {
            // Est-ce un PSA (Non-actif) ?
            if (ALCSDossier.ACTIVITE_NONACTIF.equals(dossierComplexModel.getDossierModel().getActiviteAllocataire())) {

                // v�rifie si l'enfant ou le b�n�ficiaire � d�j� une PC
                String nssAllocataire = dossierComplexModel.getAllocataireComplexModel()
                        .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel();
                String nssEnfant = droitComplexModel.getEnfantComplexModel().getPersonneEtendueComplexModel()
                        .getPersonneEtendue().getNumAvsActuel();

                ALEnumMsgDroitPC msg = ALServiceLocator.getDroitBusinessService().checkPCFamille(nssAllocataire,
                        nssEnfant);
                if (msg != null) {
                    idMessageDroitPC = msg.getMessage();
                }
            }
        }

        droitComplexModel = ALServiceLocator.getDroitBusinessService().createDroitEtEnvoyeAnnoncesRafam(
                droitComplexModel, false);

    }

    /**
     * G�re la suppression d'un droit depuis l'�cran
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        droitComplexModel = ALServiceLocator.getDroitBusinessService().deleteDroit(droitComplexModel);

    }

    public String getBeneficiaireAvantModification() {
        return beneficiaireAvantModification;
    }

    public Boolean getAfficheAttesAlloc() throws JadeApplicationException, JadePersistenceException {

        return afficheAttesAlloc;
    }

    /**
     * 
     * @return <code>true</code> si l'allocation de naissance a d�j� �t� vers�e, <code>false</code> sinon
     */
    public Boolean getAllocationNaissanceVersee() {
        return droitComplexModel.getEnfantComplexModel().getEnfantModel().getAllocationNaissanceVersee();
    }

    /**
     * Retourne le dossier auquel le droit appartient
     * 
     * @return dossierComplexModel
     */
    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    /**
     * Retourne le DroitComplexModel contenu dans le viewBean
     * 
     * @return the droitComplexModel
     */
    public DroitComplexModel getDroitComplexModel() {
        return droitComplexModel;
    }

    /**
     * Retourne la date d'�ch�ance calcul�e du droit
     * 
     * @return echeanceCalculee l'�ch�ance calcul�e
     */
    public String getEcheanceCalculee() {
        return echeanceCalculee;
    }

    /**
     * Retourne le nombre de droits pour lequel l'enfant du droit courant est actif
     * 
     * @return enfantAlreadyActif nombre de droits
     */
    public int getEnfantAlreadyActif() {
        return enfantAlreadyActif;
    }

    /**
     * Retourne l'id du droit
     * 
     * @return droitComplexModel.getId()
     */
    @Override
    public String getId() {
        return droitComplexModel.getId();
    }

    /**
     * @return the message
     */
    public String getIdMessageDroitPC() {
        return idMessageDroitPC;
    }

    public String getInfosBeneficiaire() {
        String infos = "";
        TiersSimpleModel simpleTiersSimpleModel = null;
        // if (!JadeStringUtil.isBlankOrZero(this.droitComplexModel.getDroitModel().getIdTiersBeneficiaire())) {
        // simpleTiersSimpleModel = this.droitComplexModel.getTiersBeneficiaireModel();
        // } else {
        // simpleTiersSimpleModel = this.dossierComplexModel.getTiersBeneficiaireModel();
        // }
        // if ((simpleTiersSimpleModel != null) && !JadeStringUtil.isEmpty(simpleTiersSimpleModel.getIdTiers())) {
        // infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
        // }

        simpleTiersSimpleModel = droitComplexModel.getTiersBeneficiaireModel();
        infos = simpleTiersSimpleModel.getDesignation1() + " " + simpleTiersSimpleModel.getDesignation2();
        return infos;

    }

    /**
     * Retourne le montant calcul� pour le droit
     * 
     * @return montantCalcule
     */
    public String getMontantCalcule() {
        return montantCalcule;
    }

    /**
     * M�thode retournant le mode de paiement du dossier en fonction du tiers b�n�ficiaire d�fini
     * 
     * @return <ul>
     *         <li><code>ALCSDossier.PAIEMENT_DIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_INDIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_TIERS</code></li>
     *         </ul>
     * 
     */
    public String getPaiementMode() {

        String idTiersBeneficiaireDossier = dossierComplexModel.getDossierModel().getIdTiersBeneficiaire();
        String idTiersBeneficiaireDroit = droitComplexModel.getDroitModel().getIdTiersBeneficiaire();

        String idTiersAllocataire = dossierComplexModel.getAllocataireComplexModel().getAllocataireModel()
                .getIdTiersAllocataire();

        // if ((idTiersBeneficiaireDossier.equals(idTiersAllocataire) && JadeStringUtil
        // .isBlankOrZero(idTiersBeneficiaireDroit))
        // || (!JadeStringUtil.isBlankOrZero(idTiersBeneficiaireDroit) && idTiersBeneficiaireDroit
        // .equals(idTiersAllocataire))) {
        // return ALCSDossier.PAIEMENT_DIRECT;
        // } else if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaireDossier)) {
        // return ALCSDossier.PAIEMENT_INDIRECT;
        // } else {
        // return ALCSDossier.PAIEMENT_TIERS;
        // }

        if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaireDossier)) {
            return ALCSDossier.PAIEMENT_INDIRECT;
        } else {
            if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaireDroit)) {
                return ALCSDossier.PAIEMENT_DIRECT;
            } else {
                return ALCSDossier.PAIEMENT_TIERS;
            }

        }

    }

    private void handleWarningsForPopup() {

        // Traitement des avertissements en session
        JadeBusinessMessage[] messages = (JadeBusinessMessage[]) getSession().getAttribute("addWarnings");
        if (messages != null) {

            StringBuffer strTextErrors = new StringBuffer();

            String typePopup = "simple";
            int height = 0;
            String urlAdrPaiement = URL_ADD_PAIEMENT_TIERS;
            String idTiersBenef = droitComplexModel.getTiersBeneficiaireModel().getIdTiers();
            urlAdrPaiement = urlAdrPaiement.concat(idTiersBenef);

            for (int i = 0; i < messages.length; i++) {
                height += LINE_HEIGHT_MSG_POPUP;
                String messageId = messages[i].getMessageId();

                if (i == 0) {
                    strTextErrors.append("[\"");
                }

                strTextErrors.append(StringEscapeUtils.escapeJavaScript(JadeI18n.getInstance().getMessage(
                        getSession().getUserInfo().getLanguage(), messages[i].getMessageId())));

                if ((i + 1) == messages.length) {
                    strTextErrors.append("\"]");
                } else {
                    strTextErrors.append("\",\"");
                }

                if (messageId.contains("popup-redirect")) {
                    typePopup = "redirect";
                }

            }

            String strHeight = Integer.toString(height);
            String json = "{\"type\":\"";
            json = json.concat(typePopup).concat("\",\"options\":{\"title\":\"Avertissement\",\"text\":");
            json = json.concat(strTextErrors.toString());
            json = json.concat(",\"iconcss\":\"warningIcon\",\"url\":\"");
            json = json.concat(urlAdrPaiement);
            json = json.concat("\",\"height\":").concat(strHeight).concat("},\"buttons\":[\"Compl�ter\",\"Valider\"]}");

            setAddWarnings(json);

            getSession().removeAttribute("addWarnings");
        }

    }

    /**
     * @return the session
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * Retourne le spy du droit
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (droitComplexModel != null) && !droitComplexModel.isNew() ? new BSpy(droitComplexModel.getSpy())
                : new BSpy(getSession());
    }

    public boolean isCaisseHorlogere() throws JadeApplicationException, JadePersistenceException {
        if (isCaisseHorlogere == null) {
            // FIXME: le projet webavs ne doit pas r�f�rence ALImpl, mais uniquement AL
            isCaisseHorlogere = ALImplServiceLocator.getHorlogerBusinessService().isCaisseHorlogere();
        }

        return isCaisseHorlogere;
    }

    public boolean isFnbActive() throws JadeApplicationException, JadePersistenceException {
        if (isFnbActive == null) {
            isFnbActive = ALServiceLocator.getParametersServices().isFnbEnabled(
                    JadeDateUtil.getGlobazFormattedDate(new Date()));
        }

        return isFnbActive;
    }

    /**
     * M�thode retournant si la config du versement d�coule du dossier ou non
     * 
     * @return <ul>
     *         <li><code>ALCSDossier.PAIEMENT_DIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_INDIRECT</code></li>
     *         <li><code>ALCSDossier.PAIEMENT_TIERS</code></li>
     *         </ul>
     * 
     */
    public boolean isPaiementFromDossier() {
        String idTiersBeneficiaireDossier = dossierComplexModel.getDossierModel().getIdTiersBeneficiaire();
        String idTiersBeneficiaireDroit = droitComplexModel.getDroitModel().getIdTiersBeneficiaire();
        /*
         * String idTiersAllocataire = this.dossierComplexModel.getAllocataireComplexModel().getAllocataireModel()
         * .getIdTiersAllocataire();
         */

        if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaireDossier)) {
            return true;
        } else if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaireDroit)
                && !JadeStringUtil.isBlankOrZero(idTiersBeneficiaireDossier)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * G�re la lecture (chargement) du droit lors de l'affichage depuis l'�cran
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        droitComplexModel = ALServiceLocator.getDroitComplexModelService().read(getId());
        beneficiaireAvantModification = droitComplexModel.getDroitModel().getIdTiersBeneficiaire();
        if (!JadeStringUtil.isEmpty(droitComplexModel.getEnfantComplexModel().getId())) {
            enfantAlreadyActif = ALServiceLocator.getEnfantBusinessService().getNombreDroitsActifs(
                    droitComplexModel.getEnfantComplexModel().getId());
        }

        // on lit le dossier li� au droit, pour pouvoir avoir acc�s aux donn�es
        // pour le calcul d'�ch�ance, et les infos alloc
        dossierComplexModel = ALServiceLocator.getDossierComplexModelService().read(
                droitComplexModel.getDroitModel().getIdDossier());

        if (ALCSDroit.TYPE_ENF.equals(droitComplexModel.getDroitModel().getTypeDroit())
                || ALCSDroit.TYPE_FORM.equals(droitComplexModel.getDroitModel().getTypeDroit())) {
            echeanceCalculee = ALServiceLocator.getDatesEcheanceService().getDateFinValiditeDroitCalculee(
                    droitComplexModel);
        }

        handleWarningsForPopup();
    }

    public void setAfficheAttesAlloc(Boolean afficheAttesAlloc) {
        this.afficheAttesAlloc = afficheAttesAlloc;
    }

    /**
     * D�finit si l'allocation de naissance a �t� vers�e
     * 
     * @param naissanceVersee
     *            <code>true</code> si l'allocation de naissance a d�j� �t� vers�e, <code>false</code> sinon
     */
    public void setAllocationNaissanceVersee(Boolean naissanceVersee) {
        droitComplexModel.getEnfantComplexModel().getEnfantModel().setAllocationNaissanceVersee(naissanceVersee);
    }

    /**
     * D�finit le mod�le du dossier auquel le droit appartient
     * 
     * @param dossierComplexModel
     *            Le mod�le du dossier
     */
    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    /**
     * D�finit le droit � utiliser en passant son mod�le
     * 
     * @param droitComplexModel
     *            le mod�le du droit
     */
    public void setDroitComplexModel(DroitComplexModel droitComplexModel) {
        this.droitComplexModel = droitComplexModel;
    }

    /**
     * D�finit l'�ch�ance calcul�e
     * 
     * @param echeanceCalculee
     *            l'�ch�ance calcul�e
     */
    public void setEcheanceCalculee(String echeanceCalculee) {
        this.echeanceCalculee = echeanceCalculee;
    }

    /**
     * D�finit l'id du droit
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        droitComplexModel.setId(newId);

    }

    /**
     * @param message
     *            the message to set
     */
    public void setIdMessageDroitPC(String message) {
        idMessageDroitPC = message;
    }

    /**
     * D�finit le montant calcul� du droit
     * 
     * @param montantCalcule
     *            le montant calcul�
     */
    public void setMontantCalcule(String montantCalcule) {
        this.montantCalcule = montantCalcule;
    }

    /**
     * G�re la mise � jour du droit par l'�cran
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        droitComplexModel = ALServiceLocator.getDroitBusinessService().updateDroitEtEnvoyeAnnoncesRafam(
                droitComplexModel);

    }

    public String getAddWarnings() {
        return addWarnings;
    }

    public void setAddWarnings(String addWarnings) {
        this.addWarnings = addWarnings;
    }

}
