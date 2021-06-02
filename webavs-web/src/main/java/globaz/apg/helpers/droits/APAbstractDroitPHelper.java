package globaz.apg.helpers.droits;

import java.util.Date;
import java.util.Hashtable;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.annonces.APBreakRule;
import globaz.apg.db.annonces.APBreakRuleManager;
import globaz.apg.db.droits.*;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.APAbstractDroitProxyViewBean;
import globaz.apg.vb.droits.APDroitMatPViewBean;
import globaz.apg.vb.droits.APDroitPatPViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFAffiliation;
import globaz.naos.application.AFApplication;
import globaz.osiris.external.IntRole;
import globaz.phenix.api.ICPDecision;
import globaz.phenix.api.ICPDonneesCalcul;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRAVSUtils;
import globaz.prestation.tools.PRSession;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;
import org.apache.commons.lang.StringUtils;

/**
 * <H1>Description</H1> Créé le 6 juil. 05
 *
 * @author vre
 */
public class APAbstractDroitPHelper extends PRAbstractHelper {

    private static final String ERREUR_DROITS_ACQUIS_PAS_ARRONDI = "DROITS_ACQUIS_PAS_ARRONDI";
    private static final String ERREUR_NOM_OU_PRENOM_INVALIDE = "NOM_OU_PRENOM_INVALIDE";
    protected boolean hasBeanInsertedIntoTiers = false;

    /**
     * @see globaz.framework.controller.FWHelper#_add(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        APAbstractDroitProxyViewBean droitVB = (APAbstractDroitProxyViewBean) viewBean;

        if (droitVB.getDroit().isCallValidate()) {
            preparerDroitAvantAjout(droitVB, (BSession) session);
        }

        if (!JadeStringUtil.isEmpty(droitVB.getDroit().getDroitAcquis())) {
            if (!JadeStringUtil.endsWith(droitVB.getDroit().getDroitAcquis(), "5")) {
                if (!JadeStringUtil.endsWith(droitVB.getDroit().getDroitAcquis(), "0")) {
                    droitVB.setMsgType(FWViewBeanInterface.ERROR);
                    droitVB.setMessage(
                            droitVB.getSession().getLabel(APAbstractDroitPHelper.ERREUR_DROITS_ACQUIS_PAS_ARRONDI));
                }
            }
        }

        PRDemande demande = droitVB.loadDemande();

        // A faire uniquement dans les cas maternité- ce job est fait ailleurs pour les droit APG et Paternité
        if (this instanceof APDroitMatPHelper) {
            if (!FWViewBeanInterface.ERROR.equals(droitVB.getMsgType())) {
                droitVB.getDroit().add();

                if (((BSession) session).hasErrors() || FWViewBeanInterface.ERROR.equals(droitVB.getMsgType())) {
                    /*
                     * pour les cas ou l'ajout dans la table parente a reussi mais le validate de l'ajout dans la table
                     * enfant a lance une erreur, on efface l'id du droit qui a ete genere lors de l'ajout dans la table
                     * parente
                     */
                    droitVB.setIdDroit(PRDemande.ID_TIERS_DEMANDE_BIDON);

                }
            }

            creerSituationProf(droitVB.getIdDroit(), demande.getIdTiers(), (BSession) session);
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#_delete(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession bSession = (BSession) session;
        APAbstractDroitProxyViewBean droitVB = (APAbstractDroitProxyViewBean) viewBean;
        droitVB.setISession(session);
        droitVB.getDroit().retrieve(bSession.getCurrentThreadTransaction());
        droitVB.getDroit().delete(bSession.getCurrentThreadTransaction());
    }

    /**
     * @see globaz.framework.controller.FWHelper#_init(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        viewBean.setISession(session);
    }

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        viewBean.setISession(session);
        if(viewBean instanceof APAbstractDroitProxyViewBean){
        ((APAbstractDroitProxyViewBean) viewBean).getDroit().retrieve();
    }
    }

    /**
     * @see globaz.framework.controller.FWHelper#_update(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        APAbstractDroitProxyViewBean droitVB = (APAbstractDroitProxyViewBean) viewBean;

        droitVB.setISession(session);
        if (droitVB.getDroit().isCallValidate()) {
            preparerDroitAvantAjout(droitVB, (BSession) session);
        }

        if (!JadeStringUtil.isEmpty(droitVB.getDroit().getDroitAcquis())) {
            if (!JadeStringUtil.endsWith(droitVB.getDroit().getDroitAcquis(), "5")) {
                if (!JadeStringUtil.endsWith(droitVB.getDroit().getDroitAcquis(), "0")) {
                    droitVB.setMsgType(FWViewBeanInterface.ERROR);
                    droitVB.setMessage(
                            droitVB.getSession().getLabel(APAbstractDroitPHelper.ERREUR_DROITS_ACQUIS_PAS_ARRONDI));
                }
            }
        }

        if((droitVB instanceof APDroitMatPViewBean || droitVB instanceof APDroitPatPViewBean) &&
                StringUtils.equals(droitVB.getDroit().getEtat(), IAPDroitLAPG.CS_ETAT_DROIT_ENREGISTRE)) {
            droitVB.getDroit().setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        }

        if (!FWViewBeanInterface.ERROR.equals(droitVB.getMsgType())) {
            droitVB.getDroit().update();
        }

        PRDemande demande = droitVB.loadDemande();
        creerSituationProf(droitVB.getIdDroit(), demande.getIdTiers(), (BSession) session);
    }

    public FWViewBeanInterface arreterEtape1(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        APAbstractDroitProxyViewBean droitVB = (APAbstractDroitProxyViewBean) viewBean;

        droitVB.setISession(session);
        preparerDroitAvantAjout(droitVB, session);
        droitVB.getDroit().wantCallValidate(false);

        if (droitVB.getDroit().isNew()) {
            // on ne sauvegarde le droit que si un NSS a ete choisi
            if (!JadeStringUtil.isEmpty(droitVB.getNss())) {
                droitVB.getDroit().add();
            }
        } else {
            droitVB.getDroit().update();
        }

        // si le doit est lie a la demande APG bidon, on le supprime
        // uniquement pour les APG car le ajouter periode insert le droit dans
        // la DB
        String demande = IPRDemande.CS_TYPE_APG;
        if (droitVB instanceof APDroitMatPViewBean) {
            demande = IPRDemande.CS_TYPE_MATERNITE;
        }
        if (droitVB instanceof APDroitPatPViewBean) {
            demande = IPRDemande.CS_TYPE_PATERNITE;
        }

        if (droitVB.getIdDemande().equals(PRDemande.getDemandeBidon(session,demande)
                .getIdDemande())) {
            APAbstractDroitProxyViewBean viewBeanProxy = droitVB;

            viewBeanProxy.setISession(session);
            viewBeanProxy.getDroit().retrieve(session.getCurrentThreadTransaction());
            viewBeanProxy.getDroit().delete(session.getCurrentThreadTransaction());
        }

        return droitVB;
    }

    /**
     * Si pas de situation prof. et que le tiers possède une affiliation personnelle en cours durant la période du
     * droit, on crée une situation prof. avec cette affiliation
     *
     * @param idTiers
     * @param session
     */
    protected void creerSituationProf(String idDroit, String idTiers, BSession session) throws Exception {

        // on cherche la situation prof. pour ce droit
        APSituationProfessionnelleManager spManager = new APSituationProfessionnelleManager();
        spManager.setSession(session);
        spManager.setForIdDroit(idDroit);
        int spNbEmployeur = spManager.getCount();
        String masseAnnuel = "0";

        // si pas de situation prof. on cherche les affiliations pour ce tiers
        if (spNbEmployeur == 0) {
            IAFAffiliation affiliation = (IAFAffiliation) session.getAPIFor(IAFAffiliation.class);
            affiliation.setISession(PRSession.connectSession(session, AFApplication.DEFAULT_APPLICATION_NAOS));
            Hashtable param = new Hashtable();
            param.put(IAFAffiliation.FIND_FOR_IDTIERS, idTiers);
            IAFAffiliation[] affiliations = affiliation.findAffiliation(param);

            if (affiliations.length > 0) {
                APDroitLAPG droit = new APDroitLAPG();
                droit.setSession(session);
                droit.setIdDroit(idDroit);
                droit.retrieve();

                for (int i = 0; i < affiliations.length; i++) {
                    IAFAffiliation aff = affiliations[i];

                    // InfoRom531 : On ne reprend que les indépendants et indépendant + employeur.
                    if (IAFAffiliation.TYPE_AFFILI_INDEP.equals(aff.getTypeAffiliation())
                            || IAFAffiliation.TYPE_AFFILI_INDEP_EMPLOY.equals(aff.getTypeAffiliation())) {

                        boolean dateDebutDroitGreaterOrEqualDateDebutApg = BSessionUtil
                                .compareDateFirstGreaterOrEqual(session, droit.getDateDebutDroit(), aff.getDateDebut());
                        boolean dateDebutDroitLowerOrEqualDateFinApg = BSessionUtil
                                .compareDateFirstLowerOrEqual(session, droit.getDateDebutDroit(), aff.getDateFin());
                        // si l'affiliation est en cours
                        if (dateDebutDroitGreaterOrEqualDateDebutApg && (dateDebutDroitLowerOrEqualDateFinApg
                                || globaz.jade.client.util.JadeStringUtil.isEmpty(aff.getDateFin()))) {

                            // creation de l'employeur
                            APEmployeur emp = new APEmployeur();
                            emp.setSession(session);
                            emp.setIdTiers(aff.getIdTiers());
                            emp.setIdAffilie(aff.getAffiliationId());
                            emp.add(session.getCurrentThreadTransaction());

                            // retrouver la masse annuelle dans les cotisations
                            // pers.

                            // on cherche la decision
                            ICPDecision decision = (ICPDecision) session.getAPIFor(ICPDecision.class);
                            decision.setISession(PRSession.connectSession(session, "PHENIX"));

                            Hashtable params = new Hashtable();
                            params.put(ICPDecision.FIND_FOR_ANNEE_DECISION,
                                    Integer.toString(new JADate(droit.getDateDebutDroit()).getYear()));
                            params.put(ICPDecision.FIND_FOR_ID_AFFILIATION, aff.getAffiliationId());
                            params.put(ICPDecision.FIND_FOR_IS_ACTIVE, Boolean.TRUE);

                            ICPDecision[] decisions = decision.findDecisions(params);

                            // on cherche les donnees calculee en fonction de la
                            // decision
                            if ((decisions != null) && (decisions.length > 0)) {

                                ICPDonneesCalcul donneesCalcul = (ICPDonneesCalcul) session
                                        .getAPIFor(ICPDonneesCalcul.class);
                                decision.setISession(PRSession.connectSession(session, "PHENIX"));

                                Hashtable parms = new Hashtable();
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DECISION, decisions[0].getIdDecision());
                                parms.put(ICPDonneesCalcul.FIND_FOR_ID_DONNEES_CALCUL, ICPDonneesCalcul.CS_REV_CI);

                                ICPDonneesCalcul[] donneesCalculs = donneesCalcul.findDonneesCalcul(parms);

                                if ((donneesCalculs != null) && (donneesCalculs.length > 0)) {
                                    masseAnnuel = donneesCalculs[0].getMontant();
                                }
                            }

                            // creation de la situation prof.
                            APSituationProfessionnelle sp = new APSituationProfessionnelle();
                            sp.setSession(session);
                            sp.setIdDroit(idDroit);
                            sp.setIdEmployeur(emp.getIdEmployeur());
                            sp.setIsIndependant(Boolean.TRUE);
                            sp.setIsVersementEmployeur(Boolean.TRUE);
                            // si pas "non-actif" on donne les allocations
                            // d'exploitation
                            if (!IAFAffiliation.TYPE_AFFILI_NON_ACTIF.equals(aff.getTypeAffiliation())) {

                                // pas d'allocations d'exploitation si pour un droit
                                // maternite
                                if (!IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(droit.getGenreService())
                                        && !APGUtils.isTypeAllocationJourIsole(droit.getGenreService())) {
                                    sp.setIsAllocationExploitation(Boolean.TRUE);
                                }
                            }

                            // on set la masse annuelle
                            sp.setRevenuIndependant(masseAnnuel);

                            sp.wantCallValidate(false);
                            sp.add(session.getCurrentThreadTransaction());
                        }
                    }
                }
            }
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private PRTiersWrapper insererNouveauTiers(APAbstractDroitProxyViewBean droitPViewBean, BSession session)
            throws Exception {
        // les noms et prenoms doivent être renseignés pour insérer un nouveau tiers
        if (JadeStringUtil.isEmpty(droitPViewBean.getNom()) || JadeStringUtil.isEmpty(droitPViewBean.getPrenom())) {
            session.addError(session.getLabel(APAbstractDroitPHelper.ERREUR_NOM_OU_PRENOM_INVALIDE));
            droitPViewBean.setMsgType(FWViewBeanInterface.ERROR);
            droitPViewBean.setMessage(session.getLabel(APAbstractDroitPHelper.ERREUR_NOM_OU_PRENOM_INVALIDE));

            return null;
        }

        // date naissance obligatoire pour inserer
        if (JAUtil.isDateEmpty(droitPViewBean.getDateNaissance())) {
            session.addError(session.getLabel("DATE_NAISSANCE_INCORRECTE"));
            droitPViewBean.setMsgType(FWViewBeanInterface.ERROR);
            droitPViewBean.setMessage(session.getLabel("DATE_NAISSANCE_INCORRECTE"));

            return null;
        }

        // Etat Civil obligatoire pour inserer
        if (JadeStringUtil.isEmpty(droitPViewBean.getCsEtatCivil())) {
            session.addError(session.getLabel("ETAT_CIVIL_INCORRECT"));

            droitPViewBean.setMsgType(FWViewBeanInterface.ERROR);
            droitPViewBean.setMessage(session.getLabel("ETAT_CIVIL_INCORRECT"));

            return null;
        }

        // recherche du canton si le npa est renseigné
        String canton = "";

        if (!JadeStringUtil.isIntegerEmpty(droitPViewBean.getNpa())) {
            try {
                canton = PRTiersHelper.getCanton(session, droitPViewBean.getNpa());

                if (canton == null) {
                    // canton non trouvé
                    canton = "";
                }
            } catch (Exception e1) {
                droitPViewBean._addError(session.getLabel("CANTON_INTROUVABLE"));

                droitPViewBean.setMsgType(FWViewBeanInterface.ERROR);
                droitPViewBean.setMessage(session.getLabel("CANTON_INTROUVABLE"));

            }
        }

        // insertion du tiers
        // si son numero AVS est suisse on lui met suisse comme pays, sinon on
        // lui met un pays bidon qu'on pourrait
        // interpreter comme "etranger"
        PRAVSUtils avsUtils = PRAVSUtils.getInstance(droitPViewBean.getNss());

        String idTiers = PRTiersHelper.addTiers(session, droitPViewBean.getNss(), droitPViewBean.getNom(),
                droitPViewBean.getPrenom(), droitPViewBean.getCsSexe(), droitPViewBean.getDateNaissance(),
                droitPViewBean.getDateDeces(),
                avsUtils.isSuisse(droitPViewBean.getNss()) ? TIPays.CS_SUISSE : PRTiersHelper.ID_PAYS_BIDON, canton, "",
                droitPViewBean.getCsEtatCivil());

        // on remonte les erreurs dans le viewBean si jamais.
        if (session.hasErrors()) {
            droitPViewBean.setMsgType(FWViewBeanInterface.ERROR);
            droitPViewBean.setMessage(session.getErrors().toString());
        }
        // si le tiers a ete ajoute, on change la provenance pour eviter un
        // nouvel ajout
        else {
            droitPViewBean.setProvenance(PRUtil.PROVENANCE_TIERS);
            hasBeanInsertedIntoTiers = true;
        }

        return PRTiersHelper.getTiersParId(session, idTiers);
    }

    private void preparerDroitAvantAjout(APAbstractDroitProxyViewBean droitPViewBean, BSession session)
            throws Exception {
        // préparation du tiers
        String idTiers = PRDemande.ID_TIERS_DEMANDE_BIDON;

        if (droitPViewBean.isTrouveDansCI()) {
            // le tiers a précédemment été trouvé dans les CI, il faut maintant
            // l'insérer dans les tiers
            PRTiersWrapper tiers = insererNouveauTiers(droitPViewBean, session);
            // si l'insertion dans les tiers a réussi, mettre à jour l'id du
            // tiers courant
            if (tiers != null) {
                idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                droitPViewBean.setIdAssure(idTiers);
                droitPViewBean.setTrouveDansTiers(true); // BZ 8372
            } else {
                droitPViewBean._addError(session.getLabel("ERROR_CREATION_TIERS"));
            }
        }
        if (droitPViewBean.isTrouveDansTiers()) {

            idTiers = droitPViewBean.getIdAssure();

            TIRoleManager roleManager = new TIRoleManager();
            roleManager.setSession(session);
            roleManager.setForIdTiers(idTiers);
            BStatement statement = null;
            BTransaction trans = (BTransaction) session.newTransaction();
            try {
                trans.openTransaction();
                statement = roleManager.cursorOpen(session.getCurrentThreadTransaction());
                TIRole role = null;
                boolean isRolePresent = false;

                while ((role = (TIRole) roleManager.cursorReadNext(statement)) != null) {
                    if (IntRole.ROLE_APG.equals(role.getRole())) {
                        isRolePresent = true;
                    }
                }

                if (!isRolePresent) {
                    // on ajoute le rôle APG au Tier si il ne l'a pas deja
                    ITIRole newRole = (ITIRole) session.getAPIFor(ITIRole.class);
                    newRole.setIdTiers(idTiers);
                    newRole.setISession(PRSession.connectSession(session, TIApplication.DEFAULT_APPLICATION_PYXIS));
                    newRole.setRole(IntRole.ROLE_APG);
                    newRole.add(session.getCurrentThreadTransaction());
                }
                trans.commit();
            } catch (Exception e) {
                if (trans != null) {
                    trans.rollback();
                }
                throw e;
            } finally {
                try {
                    if (statement != null) {
                        try {
                            roleManager.cursorClose(statement);
                        } finally {
                            statement.closeStatement();
                        }
                    }
                } finally {
                    trans.closeTransaction();
                }
            }

        } else {
            // sinon on se base sur l'ancien id du tiers contenu dans la
            // demande.
            idTiers = droitPViewBean.loadDemande().getIdTiers();
            // Dernier contrôle, en cas d'erreur d'insertion du tiers, ou autre
            // erreur due à une mauvaise saisie,
            // idTiers de la demande peut être vide. On va donc essayer de le
            // réinséré dans les tiers.
            if (JadeStringUtil.isIntegerEmpty(idTiers)) {
                PRTiersWrapper tiers = insererNouveauTiers(droitPViewBean, session);
                if (tiers != null) {
                    idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
                } else {
                    droitPViewBean._addError("ERROR_CREATION_TIERS");
                }

            }

        }

        // on set une demande de toutes facons pour ce droit, qu'il soit déjà
        // sauvé ou non.
        setDemande(droitPViewBean.getDroit(), idTiers, session, true);
    }

    protected PRDemande setDemande(APDroitLAPG droit, String idTiers, BSession session, boolean creeSiNecessaire)
            throws Exception {
        PRDemande retValue = null;
        String typeDemande;
        if(droit instanceof APDroitMaternite){
            typeDemande = IPRDemande.CS_TYPE_MATERNITE;
        }else if(droit instanceof APDroitPaternite){
            typeDemande = IPRDemande.CS_TYPE_PATERNITE;
        } else if(droit instanceof APDroitProcheAidant){
            typeDemande = IPRDemande.CS_TYPE_PROCHE_AIDANT;
        } else if(droit instanceof APDroitPandemie){
            typeDemande = IPRDemande.CS_TYPE_PANDEMIE;
        } else {
            typeDemande = IPRDemande.CS_TYPE_APG;
        }

        if (PRDemande.ID_TIERS_DEMANDE_BIDON.equals(idTiers)
                && (PRDemande.getDemandeBidon(session, typeDemande) != null)) {
            retValue = PRDemande.getDemandeBidon(session, typeDemande);
        } else {
            PRDemandeManager mgr = new PRDemandeManager();

            mgr.setSession(session);
            mgr.setForIdTiers(JadeStringUtil.isEmpty(idTiers) ? PRDemande.ID_TIERS_DEMANDE_BIDON : idTiers);
            mgr.setForTypeDemande(typeDemande);
            mgr.find();

            if (mgr.isEmpty() && creeSiNecessaire) {
                retValue = new PRDemande();
                retValue.setIdTiers(idTiers);
                retValue.setEtat(IPRDemande.CS_ETAT_OUVERT);
                retValue.setTypeDemande(typeDemande);
                retValue.setSession(session);
                retValue.add();
            } else if (!mgr.isEmpty()) {
                retValue = (PRDemande) mgr.get(0);
            }
        }

        if (retValue != null) {
            droit.setDemande(retValue);
        }

        return retValue;
    }

    protected void traiterBreakRules(String breakRules, String idDroit, BSession session) throws Exception {
        if (!JadeNumericUtil.isEmptyOrZero(idDroit)) {

            // On supprime tous les breakRules
            APBreakRuleManager brManager = new APBreakRuleManager();
            brManager.setSession(session);
            brManager.setForIdDroit(idDroit);
            brManager.delete();

            // On ajoute les nouveaux
            if (!JadeStringUtil.isEmpty(breakRules)) {
                for (String breakRuleCode : breakRules.split(",")) {
                    APBreakRule br = new APBreakRule();
                    br.setSession(session);
                    br.setDateQuittance(JadeDateUtil.getGlobazFormattedDate(new Date()));
                    br.setGestionnaire(session.getUserName());
                    br.setBreakRuleCode(breakRuleCode);
                    br.setIdDroit(idDroit);
                    br.add();
                }
            }
        }

    }
}
