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
import globaz.jade.json.MultiSelectHandler;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.DecisionUtils;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.DecisionSuppressionSearch;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleCopiesDecision;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppression;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import com.google.gson.Gson;

/**
 * @author SCE 14 juil. 2010
 */
public class PCDecisionSuppressionViewBean extends BJadePersistentObjectViewBean implements IPCDecisionViewBean {
    public class ElementMultiString {
        public String libelle;
        public String value;

        ElementMultiString(String value, String libelle) {
            this.value = value;
            this.libelle = libelle;
        }
    }

    // Code systeme motifs
    public static String CS_MOTIF = "PCMOTIFDES";

    // Liste motifs et sous motifs
    private static String csCodesListe = new MultiSelectHandler()
            .createMotifsWithSousMotifs(PCDecisionSuppressionViewBean.CS_MOTIF);

    // instance de la classe métier
    private DecisionSuppression decisionSuppression = null;
    private String ERROR_ADRESS_MESSAGE = null;

    private String gestMail = null;

    // Id decision header passé en param
    private String idDecision = null;
    private boolean isLotOuvert = false;

    private String[] listeAnnexes = null;
    private String[] listeCopies = null;

    private String nouveauDomicile;
    private String nouvelleCaisse;

    // objet Transfert de dossier lié à la décision lorsque celle-ci a un motif de transfert
    private TransfertDossierSuppression transfert = null;
    // Id Droit et version Droit
    // private String idDroit = null;
    // private String idVersionDroit = null;
    // gestion des utilisateurs-gestionnaire, à voir
    private JadeUser[] users = null;

    // Etat de la validation, si validation
    private String validAction = null;

    private BigDecimal montantDecision = BigDecimal.ZERO;
    private boolean isComptabilisationAuto = false;
    private String mailProcessCompta = getSession().getUserEMail();

    /**
     * Constructeur simple
     * 
     * @throws JadePersistenceException
     */
    public PCDecisionSuppressionViewBean() throws JadePersistenceException {
        super();
        decisionSuppression = new DecisionSuppression();
        ERROR_ADRESS_MESSAGE = ((BSession) getISession()).getLabel("JSP_PC_ADRESSE_INTROUVABLE");
    }

    public String getCsCodeTexteLibreMotif() {
        return IPCDecision.CS_MOTIF_SUPP_TEXTE_LIBRE;
    }

    /**
     * Constructeur avec moddèle en paramètre
     * 
     * @param decisionRefus
     * @throws JadePersistenceException
     */
    public PCDecisionSuppressionViewBean(DecisionSuppression decisionSuppression) throws JadePersistenceException {
        super();
        this.decisionSuppression = decisionSuppression;

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
        // this.decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().create(
        // this.decisionSuppression);
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
        decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().delete(decisionSuppression);
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
        DecisionSuppressionSearch search = new DecisionSuppressionSearch();
        search.setForIdDecisionHeader(getIdDecision());
        int nbre = PegasusServiceLocator.getDecisionSuppressionService().count(search);

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
                decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().getIdTiersCourrier(), Boolean.TRUE,
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
                decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire(),
                Boolean.TRUE, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(),
                null);

        return detailTiers.getAdresseFormate() != null ? detailTiers.getAdresseFormate() : ERROR_ADRESS_MESSAGE;
    }

    public String getAnnexesJSArray() {

        String result = "";

        if (decisionSuppression.getDecisionHeader().getListeAnnexes().size() > 0) {

            List<ElementMultiString> lignes = new ArrayList<ElementMultiString>();

            for (SimpleAnnexesDecision annexe : decisionSuppression.getDecisionHeader().getListeAnnexes()) {
                lignes.add(new ElementMultiString(annexe.getValeur(), annexe.getValeur()));
            }

            Gson gson = new Gson();
            result = gson.toJson(lignes);
        }
        if (JadeStringUtil.isEmpty(result)) {
            result = "[]";
        }
        return result;
    }

    public String getCopiesJSArray() {

        String result = "";

        if (decisionSuppression.getDecisionHeader().getListeCopies().size() > 0) {

            List<ElementMultiString> lignes = new ArrayList<ElementMultiString>();

            if (decisionSuppression.getDecisionHeader().getListeCopies().size() > 0) {
                for (CopiesDecision copie : decisionSuppression.getDecisionHeader().getListeCopies()) {
                    lignes.add(new ElementMultiString(copie.getSimpleCopiesDecision().getIdTiersCopie(), copie
                            .getDesignation1() + " " + copie.getDesignation2()));
                }
            }

            Gson gson = new Gson();
            result = gson.toJson(lignes);
        }
        if (JadeStringUtil.isEmpty(result)) {
            result = "[]";
        }

        return result;

    }

    public String getCsList() {

        return PCDecisionSuppressionViewBean.csCodesListe;
    }

    /**
     * Retourne le nom de l'utilisateur cournat
     * 
     * @return
     */
    public String getCurrentUserName() {
        return getSession().getUserName();
    }

    public String getDateSuppressionFormatee() {
        String dateSupression = decisionSuppression.getSimpleDecisionSuppression().getDateSuppression();

        if (!JadeStringUtil.isBlank(dateSupression)) {
            return decisionSuppression.getSimpleDecisionSuppression().getDateSuppression();
        }
        return dateSupression;

    }

    /**
     * @return the decisionRefus
     */
    public DecisionSuppression getDecisionSuppression() {
        return decisionSuppression;
    }

    /**
     * Formatte une chaine de caratere pour afficher les infos du requérant
     * 
     * @return
     */
    public String getTiersInfosAsString(String membre) {

        // String membre = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;

        String NSS = getPersonneEtendue(membre).getNumAvsActuel();
        String NomPrenom = getTiers(membre).getDesignation1() + " " + getTiers(membre).getDesignation2();
        String dateNaissance = getPersonne(membre).getDateNaissance();
        String sexe = getSession().getCodeLibelle(getPersonne(membre).getSexe());
        String nationalite = PCUserHelper.getLibellePays(getPersonneEtendueComplex(membre).getTiers());// this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    private PersonneEtendueComplexModel getPersonneEtendueComplex(String membre) {
        // Requerant d'apres le dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionSuppression.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue();
        } else {
            // Sinon personne bénéficiaire de la decision
            return decisionSuppression.getDecisionHeader().getPersonneEtendue();
        }
    }

    /**
     * Retourne le modele simple de la personne
     * 
     * @return personneSimpleModel
     */
    private PersonneSimpleModel getPersonne(String membre) {
        // Requerant d'apres dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionSuppression.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue().getPersonne();
        }
        // PErsonne beneficiare d'apres decision
        else {
            return decisionSuppression.getDecisionHeader().getPersonneEtendue().getPersonne();

        }

    }

    /**
     * Retourne le Tiers
     * 
     * @return
     */
    public TiersSimpleModel getTiers(String membre) {
        // requerant d'apres dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionSuppression.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue().getTiers();
        }
        // Personne beneficiare d'apres decision
        else {
            return decisionSuppression.getDecisionHeader().getPersonneEtendue().getTiers();
        }

    }

    /**
     * Retourne le modele complex de la personne
     * 
     * @return personneComplexModel
     */
    private PersonneEtendueSimpleModel getPersonneEtendue(String membre) {
        // Requerant d'apres le dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionSuppression.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue().getPersonneEtendue();
        } else {
            // Sinon personne bénéficiaire de la decision
            return decisionSuppression.getDecisionHeader().getPersonneEtendue().getPersonneEtendue();
        }

    }

    /**
     * Retourne le gestionnaire
     * 
     * @return String, nom du gestionnaire
     */
    public String getGestionnaire() {
        return decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar();
    }

    public String getGestMail() {
        return gestMail;
    }

    /**
     * Retourne l'id de l'entité
     * 
     * @return id
     */
    @Override
    public String getId() {
        return decisionSuppression.getId();
    }

    /**
     * @return the idDecision
     */
    @Override
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * Retourne l'id de la demande PC
     * 
     * @return
     */
    public String getIdDemandePc() {
        return decisionSuppression.getVersionDroit().getSimpleDroit().getIdDemandePC();
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return decisionSuppression.getVersionDroit().getSimpleDroit().getIdDroit();
    }

    /**
     * Donne l'id de la nouvelle caisse pour le transfert si transfert != null, sinon un String vide
     * 
     * @return
     */
    public String getIdNouvelleCaisseTransfert() {
        return getTransfert() == null ? "" : getTransfert().getSimpleTransfertDossierSuppression()
                .getIdNouvelleCaisse();
    }

    public String getIdTierRequerant() {
        return getPersonneEtendue().getIdTiers();
    }

    public String getIdVersionDroit() {
        return decisionSuppression.getVersionDroit().getSimpleVersionDroit().getIdVersionDroit();
    }

    public String getNouveauDomicile() {
        return nouveauDomicile;
    }

    public String getNouvelleCaisse() {
        return nouvelleCaisse;
    }

    /**
     * @return the noDroit
     */
    public String getNoVersion() {
        return decisionSuppression.getVersionDroit().getSimpleVersionDroit().getNoVersion();
    }

    /**
     * @return the idVersionDroit
     */
    public String getNoVersionDroit() {
        return decisionSuppression.getVersionDroit().getSimpleVersionDroit().getNoVersion();
    }

    /**
     * Retourne le modele simple de la personne
     * 
     * @return personneSimpleModel
     */
    private PersonneSimpleModel getPersonne() {
        return decisionSuppression.getDecisionHeader().getPersonneEtendue().getPersonne();
    }

    /**
     * Retourne le modele complex de la personne
     * 
     * @return personneComplexModel
     */
    private PersonneEtendueSimpleModel getPersonneEtendue() {
        return decisionSuppression.getDecisionHeader().getPersonneEtendue().getPersonneEtendue();

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
        String nationalite = PCUserHelper.getLibellePays(decisionSuppression.getDecisionHeader().getPersonneEtendue());// this.getSession().getCodeLibelle(

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
        return (decisionSuppression != null) && !decisionSuppression.isNew() ? new BSpy(decisionSuppression.getSpy())
                : new BSpy(getSession());
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
     * Daonne le motif de contact si transfert !U null, sinon un String vide
     * 
     * @return
     */
    public String getTextMotifContact() {
        return getTransfert() == null ? "" : getTransfert().getSimpleTransfertDossierSuppression()
                .getTextMotifContact();
    }

    /**
     * Donne le motif de transfert si transfert!=null, sinon un String vide
     * 
     * @return
     */
    public String getTextMotifTransfert() {
        return getTransfert() == null ? "" : getTransfert().getSimpleTransfertDossierSuppression()
                .getTextMotifTransfert();
    }

    /**
     * Retourne le Tiers
     * 
     * @return
     */
    private TiersSimpleModel getTiers() {
        return decisionSuppression.getDecisionHeader().getPersonneEtendue().getTiers();
    }

    public TransfertDossierSuppression getTransfert() {
        return transfert;
    }

    /**
     * @return the users
     */
    public JadeUser[] getUsers() {
        try {
            JadeUserService userService = JadeAdminServiceLocatorProvider.getLocator().getUserService();
            users = userService.findAllUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public String getValidAction() {
        return validAction;
    }

    public void imprimer() throws Exception {
        // si c'est un cas de transfert de dossier
        if (IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER.equals(decisionSuppression
                .getSimpleDecisionSuppression().getCsMotif())) {
            PegasusServiceLocator.getTransfertDossierPCProviderService().genereDocument(transfert, decisionSuppression,
                    gestMail);
        }
    }

    public Boolean isDevalidable() {
        return isValider() && isLotOuvert;
    }

    /**
     * Retourne vrai si etat pre valider
     * 
     * @return Boolean etat pre valider
     */
    public Boolean isPreValider() {
        return IPCDecision.CS_ETAT_PRE_VALIDE.equals(decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());
    }

    /**
     * Retourne vrai si etat valider
     * 
     * @return Boolean etat valider
     */
    public Boolean isValider() {
        return IPCDecision.CS_VALIDE.equals(decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());
    }

    /**
     * Détermine si le traitement complet d'information de la restitution doit être appliqué. Un message spécifiant que
     * la comptabilistaion peut ^tre effectué, ainsi que le montant:
     * - décision non validé ET montant > 0
     * 
     * @return
     */
    public Boolean displayCompleteRestitutionMessage() {
        return !isValider() && hasMontantRestitution();
    }

    public Boolean hasMontantRestitution() {
        return getMontantDecision().compareTo(BigDecimal.ZERO) > 0;
    }

    public String motifsHasSousMotifs() {
        return decisionSuppression.getSimpleDecisionSuppression().getCsSousMotif();
    }

    public void prevalider() throws Exception {

        DecisionUtils.checkIfDateDecisionCompriseEntreDernierEtProchainPaiement(decisionSuppression.getDecisionHeader()
                .getSimpleDecisionHeader().getDateDecision());
        // si c'est un cas de transfert de dossier
        if (IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER.equals(decisionSuppression
                .getSimpleDecisionSuppression().getCsMotif())) {
            // update annexes et copies
            processTransfert();
        }

        try {
            // update decSup, remise etat enregistré
            decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                    .setCsEtatDecision(IPCDecision.CS_ETAT_PRE_VALIDE);
            decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().update(decisionSuppression);
        } catch (PmtMensuelException e) {
            throw new DecisionException("An error happened during the update of Decision Supression", e);
        }
    }

    private void processAnnexes() {

        decisionSuppression.getDecisionHeader().getListeAnnexes().clear();

        if (listeAnnexes != null) {

            for (String annexe : listeAnnexes) {
                if (!JadeStringUtil.isEmpty(annexe)) {
                    SimpleAnnexesDecision simpleAnnexesDecision = new SimpleAnnexesDecision();
                    simpleAnnexesDecision.setValeur(annexe);
                    simpleAnnexesDecision.setIdDecisionHeader(decisionSuppression.getDecisionHeader()
                            .getSimpleDecisionHeader().getIdDecisionHeader());
                    simpleAnnexesDecision.setCsType(IPCDecision.ANNEXE_COPIE_MANUEL);
                    decisionSuppression.getDecisionHeader().addToListeAnnexes(simpleAnnexesDecision);
                }
            }
        }

    }

    private void processCopies() {

        decisionSuppression.getDecisionHeader().getListeCopies().clear();

        if (listeCopies != null) {

            for (String copie : listeCopies) {
                if (!JadeStringUtil.isEmpty(copie)) {
                    CopiesDecision copiesDecision = new CopiesDecision();
                    SimpleCopiesDecision simpleCopiesDecision = copiesDecision.getSimpleCopiesDecision();
                    simpleCopiesDecision.setIdTiersCopie(copie);
                    simpleCopiesDecision.setIdDecisionHeader(decisionSuppression.getDecisionHeader()
                            .getSimpleDecisionHeader().getIdDecisionHeader());

                    simpleCopiesDecision.setAnnexes(false);
                    simpleCopiesDecision.setCopies(false);
                    simpleCopiesDecision.setMoyensDeDroit(false);
                    simpleCopiesDecision.setPageDeGarde(false);
                    simpleCopiesDecision.setPlandeCalcul(false);
                    simpleCopiesDecision.setRecapitulatif(false);
                    simpleCopiesDecision.setRemarque(false);
                    simpleCopiesDecision.setSignature(false);
                    simpleCopiesDecision.setVersementA(false);

                    decisionSuppression.getDecisionHeader().addToListCopies(copiesDecision);
                }
            }
        }

    }

    private void processTransfert() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PmtMensuelException {
        if (transfert != null) {
            try {
                processAnnexes();
                processCopies();
                PegasusServiceLocator.getTransfertDossierSuppressionService().update(transfert);
            } catch (TransfertDossierException e) {
                throw new DecisionException("An error happened during the update of Transfert dossier", e);
            }
        }
    }

    /**
     * Lit l'instance
     * 
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PmtMensuelException
     * @throws PCAccordeeException
     * @throws OrdreVersementException
     */
    @Override
    public void retrieve() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, DemandeException, OrdreVersementException, PCAccordeeException,
            PmtMensuelException {
        // create objet recherche
        DecisionSuppressionSearch search = new DecisionSuppressionSearch();
        // Seach avec id passé en parametre
        search.setForIdDecisionHeader(getIdDecision());
        decisionSuppression = (DecisionSuppression) PegasusServiceLocator.getDecisionSuppressionService()
                .search(search).getSearchResults()[0];
        decisionSuppression.setDecisionHeader(PegasusServiceLocator.getDecisionHeaderService().readAnnexesCopies(
                decisionSuppression.getDecisionHeader()));

        // gestion du cas de transfert de dossier
        if (IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER.equals(decisionSuppression
                .getSimpleDecisionSuppression().getCsMotif())) {

            try {
                transfert = PegasusServiceLocator.getTransfertDossierPCProviderService()
                        .readTransfertByIdDecisionHeader(
                                decisionSuppression.getSimpleDecisionSuppression().getIdDecisionHeader());

                // récupère détails pour les widgets
                if (!JadeStringUtil.isBlankOrZero(transfert.getSimpleTransfertDossierSuppression()
                        .getIdNouveauDomicile())) {
                    LocaliteSimpleModel localite = TIBusinessServiceLocator.getAdresseService().readLocalite(
                            transfert.getSimpleTransfertDossierSuppression().getIdNouveauDomicile());
                    nouveauDomicile = localite.getNumPostal() + ", " + localite.getLocalite();
                }

                if (!JadeStringUtil.isBlankOrZero(transfert.getSimpleTransfertDossierSuppression()
                        .getIdNouvelleCaisse())) {
                    AdministrationComplexModel administration = TIBusinessServiceLocator.getAdministrationService()
                            .read(transfert.getSimpleTransfertDossierSuppression().getIdNouvelleCaisse());
                    nouvelleCaisse = administration.getAdmin().getCodeAdministration() + " "
                            + administration.getTiers().getDesignation1() + " "
                            + administration.getTiers().getDesignation2();
                }

            } catch (TransfertDossierException e) {
                throw new DecisionException("An error happened while retrieving TransfertDossierSuppression!", e);
            } catch (JadeApplicationException e) {
                throw new DecisionException("An error happened while retrieving localité!", e);
            }
        }
        try {
            // si la décision est déjà validée, chercher si le lot est ouvert afin de savoir si la décision est
            // dévalidable
            isLotOuvert = PegasusServiceLocator.getDecisionService().isDecisionDevalidable(
                    decisionSuppression.getDecisionHeader().getSimpleDecisionHeader(),
                    decisionSuppression.getVersionDroit().getSimpleVersionDroit())
                    && !IPCDroits.CS_ANNULE.equals(decisionSuppression.getVersionDroit().getSimpleVersionDroit()
                            .getCsEtatDroit());

        } catch (PrestationException e) {
            throw new DecisionException("An error happened while retrieving prestation!", e);
        } catch (LotException e) {
            throw new DecisionException("An error happened while retrieving lot!", e);
        }

        if (isValider()) {
            String idPrestation = decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().getIdPrestation();
            Prestation prest = null;
            try {
                prest = PegasusServiceLocator.getPrestationService().read(idPrestation);
            } catch (PrestationException e) {
                throw new DecisionException("Unabled to read prestation (id :" + idPrestation + ")", e);
            }

            montantDecision = new BigDecimal(prest.getSimplePrestation().getMontantTotal()).abs();
        } else {
            montantDecision = PegasusServiceLocator.getPCAccordeeService().calculerMontantRestitution(
                    decisionSuppression.getVersionDroit().getSimpleDroit().getId(),
                    decisionSuppression.getVersionDroit().getSimpleVersionDroit().getNoVersion(),
                    decisionSuppression.getSimpleDecisionSuppression().getDateSuppression());
        }
    }

    /**
     * @param monnaieEtrangere
     *            the monnaieEtrangere to set
     */
    public void setDecisionSuppression(DecisionSuppression decisionSuppression) {
        this.decisionSuppression = decisionSuppression;
    }

    public void setGestMail(String gestMail) {
        this.gestMail = gestMail;
    }

    /**
     * Set l'id de l'entité
     */
    @Override
    public void setId(String newId) {
        decisionSuppression.setId(newId);

    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setlisteAnnexes(String annexes) {
        listeAnnexes = annexes.split("¦");
    }

    public void setListeCopies(String copies) {
        listeCopies = copies.split("¦");
    }

    public void setNouveauDomicile(String nouveauDomicile) {
        this.nouveauDomicile = nouveauDomicile;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    // public void setIdDroit(String idDroit) {
    // this.idDroit = idDroit;
    // }

    public void setTransfert(TransfertDossierSuppression transfert) {
        this.transfert = transfert;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    // public void setIdVersionDroit(String idVersionDroit) {
    // this.idVersionDroit = idVersionDroit;
    // }

    public void setValidAction(String validAction) {
        this.validAction = validAction;
    }

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

        DecisionUtils.checkIfDateDecisionCompriseEntreDernierEtProchainPaiement(decisionSuppression.getDecisionHeader()
                .getSimpleDecisionHeader().getDateDecision());
        try {
            // si c'est un cas de transfert de dossier
            if (IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER.equals(decisionSuppression
                    .getSimpleDecisionSuppression().getCsMotif())) {
                // update annexes et copies
                processTransfert();
            }
            // update decSup, remise etat enregistré
            decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                    .setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
            decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().update(decisionSuppression);
            processTransfert();
        } catch (PmtMensuelException e) {
            throw new DecisionException("An error happened during the update of Decision Supression", e);
        }

    }

    public void valider() throws Exception {

        DecisionUtils.checkIfDateDecisionCompriseEntreDernierEtProchainPaiement(decisionSuppression.getDecisionHeader()
                .getSimpleDecisionHeader().getDateDecision());
        // si c'est un cas de transfert de dossier
        if (IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER.equals(decisionSuppression
                .getSimpleDecisionSuppression().getCsMotif())) {
            // update annexes et copies
            processTransfert();
        }

        // Validé par
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader().setValidationPar(getCurrentUserName());
        // Date validation
        decisionSuppression.getDecisionHeader().getSimpleDecisionHeader()
                .setDateValidation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        decisionSuppression = PegasusServiceLocator.getDecisionSuppressionService().update(decisionSuppression);
        // Validation de la décision
        PegasusServiceLocator.getValidationDecisionService().validerDecisionSuppression(decisionSuppression,
                isComptabilisationAuto(), mailProcessCompta);
    }

    public BigDecimal getMontantDecision() {
        return montantDecision;
    }

    public void setMontantDecision(BigDecimal montantDecision) {
        this.montantDecision = montantDecision;
    }

    public boolean isComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public boolean getIsComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public void setIsComptabilisationAuto(boolean isComptabilisationAuto) {
        this.isComptabilisationAuto = isComptabilisationAuto;
    }

    public String getMailProcessCompta() {
        return mailProcessCompta;
    }

    public void setMailProcessCompta(String mailProcessCompta) {
        this.mailProcessCompta = mailProcessCompta;
    }
}
