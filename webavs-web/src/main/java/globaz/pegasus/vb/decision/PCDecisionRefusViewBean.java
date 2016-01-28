/**
 * 
 */
package globaz.pegasus.vb.decision;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Date;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.DecisionRefusSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author SCE 14 juil. 2010
 */
public class PCDecisionRefusViewBean extends BJadePersistentObjectViewBean implements IPCDecisionViewBean {

    // Code systeme motifs
    public static String CS_MOTIF = "PCMOTIFDER";
    // Liste motifs et sous motifs

    // instance de la classe métier
    private DecisionRefus decisionRefus = null;
    private String ERROR_ADRESS_MESSAGE = null;
    // Id decision header passé en param
    private String idDecision = null;
    // Id demande passé en param
    private String idDemande = null;
    // gestion des utilisateurs-gestionnaire, à voir
    private JadeUser[] users = null;
    // Etat de la validation, si validation¨
    private String validAction = null;

    /**
     * Constructeur simple
     * 
     * @throws JadePersistenceException
     */
    public PCDecisionRefusViewBean() throws JadePersistenceException {
        super();
        decisionRefus = new DecisionRefus();
        ERROR_ADRESS_MESSAGE = ((BSession) getISession()).getLabel("JSP_PC_ADRESSE_INTROUVABLE");

    }

    public String getCsCodeRenonciation() {
        return IPCDecision.CS_MOIF_REFUS_RENONCIATION;
    }

    /**
     * Constructeur avec moddèle en paramètre
     * 
     * @param decisionRefus
     * @throws JadePersistenceException
     */
    public PCDecisionRefusViewBean(DecisionRefus decisionRefus) throws JadePersistenceException {
        super();
        this.decisionRefus = decisionRefus;

    }

    /**
     * Ajout de l'entité
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void add() throws DecisionException, DossierException, DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        decisionRefus = PegasusServiceLocator.getDecisionRefusService().create(decisionRefus);
    }

    /**
     * Supression de l'entité
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        decisionRefus = PegasusServiceLocator.getDecisionRefusService().delete(decisionRefus);
    }

    /**
     * Check si décision ok, pas de la reprise
     * 
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws Exception
     */
    public void detail() throws PCAccordeeException, JadeApplicationServiceNotAvailableException, DecisionException,
            JadePersistenceException, Exception {
        // / create objet recherche
        DecisionRefusSearch search = new DecisionRefusSearch();
        search.setForIdDecisionHeader(getIdDecision());
        int nbre = PegasusServiceLocator.getDecisionRefusService().count(search);

        // Si pas de résultat, décision de reprise, pas de détails
        if (nbre == 0) {
            throw new DecisionException("pegasus.decisions.reprise.nodetail");
        }
    }

    /**
     * Retourne l'adresse de courier du tiers
     * 
     * @return String, adresse de courrier
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdresseCourrier() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                decisionRefus.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire(), Boolean.TRUE,
                JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                AdresseService.CS_TYPE_COURRIER, null);

        return detailTiers.getAdresseFormate() != null ? detailTiers.getAdresseFormate() : ERROR_ADRESS_MESSAGE;
    }

    /**
     * Retourne l'adresse de paiement du tiers
     * 
     * @return String, adresse de paiement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiement() throws JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                decisionRefus.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire(), Boolean.TRUE,
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), null);

        return detailTiers.getAdresseFormate() != null ? detailTiers.getAdresseFormate() : ERROR_ADRESS_MESSAGE;
    }

    /**
     * Retourne le nom de l'utilisateur cournat
     * 
     * @return
     */
    public String getCurrentUserName() {
        return getSession().getUserName();
    }

    /**
     * @return the decisionRefus
     */
    public DecisionRefus getDecisionRefus() {
        return decisionRefus;
    }

    /**
     * Retourne le gestionnaire
     * 
     * @return String, nom du gestionnaire
     */
    public String getGestionnaire() {
        return decisionRefus.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar();
    }

    /**
     * Retourne l'id de l'entité
     * 
     * @return id
     */
    @Override
    public String getId() {
        return decisionRefus.getId();
    }

    /**
     * @return the idDecision
     */
    @Override
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * Retourne le modele simple de la personne
     * 
     * @return personneSimpleModel
     */
    private PersonneSimpleModel getPersonne() {
        return decisionRefus.getDecisionHeader().getPersonneEtendue().getPersonne();
    }

    /**
     * Retourne le modele complex de la personne
     * 
     * @return personneComplexModel
     */
    private PersonneEtendueSimpleModel getPersonneEtendue() {
        return decisionRefus.getDecisionHeader().getPersonneEtendue().getPersonneEtendue();

    }

    /**
     * Formatte une chaine de caratere pour afficher les infos du requérant
     * 
     * @return
     */
    public String getRequerantInfos() {

        String NSS = getPersonneEtendue().getNumAvsActuel();
        String NomPrenom = getTiers().getDesignation1() + " " + getTiers().getDesignation2();
        String dateNaissance = getPersonne().getDateNaissance();
        String sexe = getSession().getCodeLibelle(getPersonne().getSexe());
        String nationalite = PCUserHelper.getLibellePays(decisionRefus.getDecisionHeader().getPersonneEtendue());// this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * Retourne l'objet BSpy
     * 
     * @return objet BSpy
     */
    @Override
    public BSpy getSpy() {
        return (decisionRefus != null) && !decisionRefus.isNew() ? new BSpy(decisionRefus.getSpy()) : new BSpy(
                getSession());
    }

    /**
     * Formate la ligne vide (&nbsp) si chaine vide, pour tableau jsp
     * 
     * @param valueForTab
     * @return valueForTab si pas null, sinon &nbsp
     */
    public String getStringForTabValue(String valueForTab) {
        return JadeStringUtil.isEmpty(valueForTab) ? "&nbsp;" : valueForTab;
    }

    /**
     * Retourne le Tiers
     * 
     * @return
     */
    private TiersSimpleModel getTiers() {
        return decisionRefus.getDecisionHeader().getPersonneEtendue().getTiers();
    }

    /**
     * @return the users
     */
    public JadeUser[] getUsers() {
        try {
            JadeUserService userService = JadeAdminServiceLocatorProvider.getLocator().getUserService();
            users = userService.findAllUsers();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return users;
    }

    /**
     * @return the validAction
     */
    public String getValidAction() {
        return validAction;
    }

    /**
     * Retourne vrai si etat enregistré
     * 
     * @return Boolean etat enregistre
     */
    public Boolean isEnregistre() {
        return IPCDecision.CS_ETAT_ENREGISTRE.equals(decisionRefus.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());
    }

    /**
     * Retourne vrai si etat pre valider
     * 
     * @return Boolean etat pre valider
     */
    public Boolean isPreValider() {
        return IPCDecision.CS_ETAT_PRE_VALIDE.equals(decisionRefus.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());
    }

    /**
     * Retourne vrai si etat valider
     * 
     * @return Boolean etat valider
     */
    public Boolean isValider() {
        return IPCDecision.CS_VALIDE.equals(decisionRefus.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());
    }

    public String motifsHasSousMotifs() {
        return decisionRefus.getSimpleDecisionRefus().getCsSousMotif();
    }

    public void prevalider() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        decisionRefus.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_PRE_VALIDE);

        decisionRefus = PegasusServiceLocator.getValidationDecisionService().preValidDecisionRefus(decisionRefus);

    }

    /**
     * Lit l'instance
     * 
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void retrieve() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DemandeException {
        // create objet recherche
        DecisionRefusSearch search = new DecisionRefusSearch();
        // Seach avec id passé en parametre
        search.setForIdDecisionHeader(getIdDecision());
        decisionRefus = (DecisionRefus) PegasusServiceLocator.getDecisionRefusService().search(search)
                .getSearchResults()[0];
        // ajout de la demande
        decisionRefus.setDemande(PegasusServiceLocator.getDemandeService().read(getIdDemande()));
    }

    /**
     * @param monnaieEtrangere
     *            the monnaieEtrangere to set
     */
    public void setDecisionRefus(DecisionRefus decisionRefus) {
        this.decisionRefus = decisionRefus;
    }

    /**
     * Set l'id de l'entité
     */
    @Override
    public void setId(String newId) {
        decisionRefus.setId(newId);

    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param validAction
     *            the validAction to set
     */
    public void setValidAction(String validAction) {
        this.validAction = validAction;
    };

    /**
     * Mise à jour de l'entité
     * 
     * @throws MonnaieEtrangereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        // gestion header, on repasse à enregistré
        decisionRefus.getDecisionHeader().getSimpleDecisionHeader().setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
        decisionRefus = PegasusServiceLocator.getDecisionRefusService().update(decisionRefus);

    };

    public void valider() throws DecisionException, DemandeException, DossierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Validé par
        decisionRefus.getDecisionHeader().getSimpleDecisionHeader().setValidationPar(getCurrentUserName());
        // Date validation
        decisionRefus.getDecisionHeader().getSimpleDecisionHeader()
                .setDateValidation(JadeDateUtil.getGlobazFormattedDate(new Date()));

        decisionRefus = PegasusServiceLocator.getValidationDecisionService().validDecisionRefus(decisionRefus);

    }
}
