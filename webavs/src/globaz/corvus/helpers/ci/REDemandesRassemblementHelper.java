package globaz.corvus.helpers.ci;

import globaz.corvus.api.ci.IRERassemblementCI;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.ci.RECompteIndividuel;
import globaz.corvus.db.ci.RECompteIndividuelManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.vb.ci.REDemandesRassemblementViewBean;
import globaz.corvus.vb.ci.RERassemblementCIListViewBean;
import globaz.corvus.vb.ci.RERassemblementCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.hermes.application.HEApplication;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.PRStringUtils;
import globaz.webavs.common.CommonProperties;

/**
 * @author BSC
 * 
 */

public class REDemandesRassemblementHelper extends PRAbstractHelper {

    // ~ Fields
    // -------------------------------------------------------------------------------------

    public static final String CODE_APPLICATION_11 = "11";
    public static final String CODE_ENREGISTREMENT_01 = "01";
    public static final String CODE_ENREGISTREMENT_04 = "04";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_init(globaz.framework.bean. FWViewBeanInterface,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._init(viewBean, action, session);

        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            REDemandesRassemblementViewBean draViewBean = (REDemandesRassemblementViewBean) viewBean;

            // recherche du requérant
            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession((BSession) session);
            demandeRente.setIdDemandeRente(draViewBean.getIdDemandeRente());
            demandeRente.retrieve(transaction);

            PRDemande demande = new PRDemande();
            demande.setSession((BSession) session);
            demande.setIdDemande(demandeRente.getIdDemandePrestation());
            demande.retrieve(transaction);

            draViewBean.setIdDemande(demande.getIdDemande());
            draViewBean.setIdRequerant(demande.getIdTiers());

            // rechercher les membres de la famille
            globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(
                    (BSession) session, ISFSituationFamiliale.CS_DOMAINE_RENTES, draViewBean.getIdRequerant());
            ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(draViewBean.getIdRequerant());

            // Pour chaque membre
            for (int i = 0; (membresFamille != null) && (i < membresFamille.length); i++) {
                ISFMembreFamilleRequerant mf = membresFamille[i];

                // rechercher le CI de ce membre
                if (JadeStringUtil.isIntegerEmpty(mf.getIdTiers())
                        || ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(mf.getRelationAuRequerant())) {
                    // si pas d'idTiers, pas de rente
                    // on ne considere pas les enfants
                    continue;
                } else {
                    RECompteIndividuelManager ciManager = new RECompteIndividuelManager();
                    ciManager.setSession((BSession) session);
                    ciManager.setForIdTiers(mf.getIdTiers());
                    ciManager.find(transaction);

                    if (ciManager.getSize() == 0) {

                        // pas de CI

                        // creation du CI
                        RECompteIndividuel ci = new RECompteIndividuel();
                        ci.setSession((BSession) session);
                        ci.setIdTiers(mf.getIdTiers());
                        ci.add(transaction);

                        // creation du rassemblement CI
                        RERassemblementCIViewBean rassCI = new RERassemblementCIViewBean();
                        rassCI.setSession((BSession) session);
                        rassCI.setIdCI(ci.getIdCi());
                        // si une date de deces, le tiers n'est pas l'ayant
                        // droit
                        if (!JadeStringUtil.isEmpty(mf.getDateDeces())) {
                            rassCI.setIsTiersAyantDroit(Boolean.FALSE);

                            // on cherche un nouvel ayant droit
                            rassCI.setIdTiersAyantDroit(rechercherIdTiersAyantDroit((BSession) session, mf,
                                    draViewBean.getIdRequerant()));
                        } else {
                            rassCI.setIsTiersAyantDroit(Boolean.TRUE);
                            rassCI.setIdTiersAyantDroit(mf.getIdTiers());
                        }

                        rassCI.setDateCloture(chercherDateCloture(demandeRente));

                        rassCI.add(transaction);

                        draViewBean.addRassemblementCI(rassCI);
                        draViewBean.addSitFamToRassemblementCI(mf, rassCI.getIdRCI());

                    } else if (ciManager.getSize() == 1) {

                        // le CI existe deja

                        // on cherche les rassemblement CI
                        RERassemblementCIListViewBean rCiManager = new RERassemblementCIListViewBean();
                        rCiManager.setSession((BSession) session);
                        rCiManager.setForIdTiers(mf.getIdTiers());
                        rCiManager.setOrderBy(" YOIRCI DESC ");
                        rCiManager.find(transaction);

                        RERassemblementCIViewBean rci = null;
                        if (rCiManager.getSize() > 0) {
                            // on ne recupere que le dernier rassemblement
                            rci = (RERassemblementCIViewBean) rCiManager.getFirstEntity();

                            // si pas de status la demande est en preparation,
                            // on regarde
                            // si une date de dece n'a pas ete ajoutee
                            if (JadeStringUtil.isIntegerEmpty(rci.getCsEtat())) {
                                // si une date de deces, le tiers n'est pas
                                // l'ayant droit
                                if (!JadeStringUtil.isEmpty(mf.getDateDeces())) {
                                    rci.setIsTiersAyantDroit(Boolean.FALSE);

                                    // on cherche un nouvel ayant droit
                                    rci.setIdTiersAyantDroit(rechercherIdTiersAyantDroit((BSession) session, mf,
                                            draViewBean.getIdRequerant()));
                                } else {
                                    rci.setIsTiersAyantDroit(Boolean.TRUE);
                                    rci.setIdTiersAyantDroit(mf.getIdTiers());
                                }

                                if (JadeStringUtil.isEmpty(rci.getDateCloture())) {
                                    rci.setDateCloture(chercherDateCloture(demandeRente));
                                }
                            }
                        } else {
                            // on cree un rassemblement
                            RECompteIndividuel ci = (RECompteIndividuel) ciManager.getFirstEntity();

                            rci = new RERassemblementCIViewBean();
                            rci.setSession((BSession) session);
                            rci.setIdCI(ci.getIdCi());
                            // si une date de deces, le tiers n'est pas l'ayant
                            // droit
                            if (!JadeStringUtil.isEmpty(mf.getDateDeces())) {
                                rci.setIsTiersAyantDroit(Boolean.FALSE);

                                // on cherche un nouvel ayant droit
                                rci.setIdTiersAyantDroit(rechercherIdTiersAyantDroit((BSession) session, mf,
                                        draViewBean.getIdRequerant()));
                            } else {
                                rci.setIsTiersAyantDroit(Boolean.TRUE);
                                rci.setIdTiersAyantDroit(mf.getIdTiers());
                            }

                            rci.setDateCloture(chercherDateCloture(demandeRente));

                            rci.add(transaction);
                        }

                        draViewBean.addRassemblementCI(rci);
                        draViewBean.addSitFamToRassemblementCI(mf, rci.getIdRCI());

                    } else {
                        throw new Exception(((BSession) session).getLabel("ERREUR_PLS_CI_POUR_TIERS") + mf.getIdTiers());
                    }
                }
            }
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.addErrors(e.getMessage());
                transaction.rollback();
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionEnvoyerDemandeRassemblement(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        RERassemblementCIViewBean rciViewBean = (RERassemblementCIViewBean) viewBean;

        BITransaction transaction = null;
        BITransaction remoteTransaction = null;
        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            BISession remoteSession = PRSession.connectSession(session, HEApplication.DEFAULT_APPLICATION_HERMES);
            remoteTransaction = ((BSession) remoteSession).newTransaction();
            if (!remoteTransaction.isOpened()) {
                remoteTransaction.openTransaction();
            }

            // envoie de la demande
            String refArc = envoyerAnnonceDemandeRassemblement(remoteTransaction, rciViewBean);

            if (!remoteTransaction.hasErrors() && (refArc != null)) {
                // mise à jour de la demande
                rciViewBean.setMotif(rciViewBean.getMotifForDemande());
                rciViewBean.setDateCloture(rciViewBean.getDateClotureForDemande());
                rciViewBean.setCsEtat(IRERassemblementCI.CS_ETAT_ENCOURS);
                rciViewBean.setReferenceUniqueArc(refArc);
                rciViewBean.update(transaction);

                transaction.commit();
                remoteTransaction.commit();
            } else {
                throw new Exception(remoteTransaction.getErrors().toString());
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                if (remoteTransaction != null) {
                    remoteTransaction.rollback();
                }
            }
        } finally {
            try {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors()) {
                            rciViewBean.setMsgType(FWViewBeanInterface.ERROR);
                            rciViewBean.setMessage(transaction.getErrors().toString());
                        }
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            } finally {
                if (remoteTransaction != null) {
                    try {
                        if (remoteTransaction.hasErrors()) {
                            rciViewBean.setMsgType(FWViewBeanInterface.ERROR);
                            rciViewBean.setMessage(remoteTransaction.getErrors().toString());
                        }
                    } finally {
                        remoteTransaction.closeTransaction();
                    }
                }
            }
            remoteTransaction.closeTransaction();
        }

        return rciViewBean;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionNouvelleDemandeRassemblement(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        RERassemblementCIViewBean rciViewBean = (RERassemblementCIViewBean) viewBean;

        // la nouvelle demande de rassemblement
        RERassemblementCIViewBean newRciViewBean = new RERassemblementCIViewBean();
        newRciViewBean.setSession(session);
        newRciViewBean.setIdCI(rciViewBean.getIdCI());
        newRciViewBean.add();

        return newRciViewBean;
    }

    /**
     * Donne la date de cloture de rassemblement d'une demande de rente
     * 
     * @param demandeRente
     * @return
     */
    private String chercherDateCloture(REDemandeRente demandeRente) {

        // pas possible de connaitre la date de cloture avant le calcul

        // try {
        // JADate dateDebut = new JADate(demandeRente.getDateDebut());
        // int year = dateDebut.getYear() -1;
        // return "12."+year;
        // } catch (JAException e) {
        // return "";
        // }

        return "";
    }

    /**
     * Envoie les annonce de demande de rassemblement a HERMES
     * 
     * ------------------------------------------------------------------- Enregistrement 01
     * -----+----------+-------------------------------------+------------ champ| position | Contenu | Observation
     * -----+----------+-------------------------------------+------------ 1 | 1- 2 | Code application: 11 | 2 | 3- 4 |
     * Code enregistrement: 01 | 3 | 5- 7 | Numero de la caisse | 1 4 | 8- 10 | Numero de l'agence | 1 5 | 11- 30 |
     * Reference interne de la caisse | 4 6 | 31- 36 | Numero de l'annonce | 1, 3 7 | 37- 47 | Numero d'assure | 2 8 |
     * 48- 87 | Etat nominatif | 2 9 | 88 | Sexe | 10 | 89- 96 | Date naissance = JJMMAAAA | 11 | 97- 99 | Etat
     * d'origine | 12 | 100- 101 | Motif de l'annonce | 13 | 102- 112 | Numéro d'assuré 1 utilise jusqu'ici | 2, 5 14 |
     * 113- 120 | Reserve: a blanc | -----+----------+-------------------------------------+------------ Les champs
     * inutilises sont a blanc. 1 = Cadre a droite, les positions inoccupees sont completees par des zeros. 2 = Cadre a
     * gauche, les positions inoccupees sont a blanc. 3 = Numerotation sequentielle de la caisse/agence 4 =
     * Alphanumerique 5 = En cas d'ARC 33/43: - Pour l'impression d'un CA sans liaison: completer avec des blancs - Pour
     * une liaison (en plus de l'impression d'un CA): introduire le numéro d'assure
     * ------------------------------------------------------------------- Enregistrement 04
     * -----+----------+-------------------------------------+------------ champ| position | Contenu | Observation
     * -----+----------+-------------------------------------+------------ 3 | 5 | Ayant droit | 5 | 17- 56 | Etat
     * nominatif de l'ayant droit | 6 | 57- 60 | Date de cloture MMAA | 7 | 61 | Reserve : a blanc | 8 | 62- 67 | Date
     * de l'ordre |
     * 
     * 
     * @param transaction
     * @param rciViewBean
     */
    private String envoyerAnnonceDemandeRassemblement(BITransaction transaction, RERassemblementCIViewBean rciViewBean) {
        String refUnique = null;
        try {
            if ((transaction != null) && !transaction.hasErrors()) {

                // création de l'API
                IHEInputAnnonce remoteEcritureAnnonce = (IHEInputAnnonce) transaction.getISession().getAPIFor(
                        IHEInputAnnonce.class);

                // attributs standards ARC
                remoteEcritureAnnonce.setIdProgramme(REApplication.DEFAULT_APPLICATION_CORVUS);
                remoteEcritureAnnonce.setUtilisateur(transaction.getISession().getUserId());
                remoteEcritureAnnonce.setTypeLot(IHELotViewBean.TYPE_ENVOI);

                // attributs de l'enregistrement
                RECompteIndividuel ci = new RECompteIndividuel();
                ci.setSession((BSession) transaction.getISession());
                ci.setIdCi(rciViewBean.getIdCI());
                ci.retrieve(transaction);

                PRTiersWrapper tiers = PRTiersHelper.getPersonneAVS(transaction.getISession(), ci.getIdTiers());

                // ///////////////////////////////////////////////////////////////////////
                // Enregistrement 01
                // ///////////////////////////////////////////////////////////////////////
                // 1 | Code application: 11
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_APPLICATION,
                        REDemandesRassemblementHelper.CODE_APPLICATION_11);

                // 2 | Code enregistrement: 01
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                        REDemandesRassemblementHelper.CODE_ENREGISTREMENT_01);

                // 3 | Numero de la caisse
                String noCaisse = PRAbstractApplication.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS)
                        .getProperty(CommonProperties.KEY_NO_CAISSE);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_CAISSE,
                        formatXPosAppendWithZero(3, true, noCaisse));

                // 4 | Numero de l'agence
                String noAgence = PRAbstractApplication.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS)
                        .getProperty(CommonProperties.KEY_NO_AGENCE);
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_AGENCE,
                        formatXPosAppendWithZero(3, true, noAgence));

                // 5 | Reference interne de la caisse
                JadeUser user = ((BSession) transaction.getISession()).getUserInfo();
                String userVisa = user.getVisa().toUpperCase();
                // on limite le visa a 8 positions
                if (userVisa.length() > 8) {
                    userVisa = userVisa.substring(0, 8);
                }
                String refInterne = "REN/" + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD)
                        + userVisa;
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE,
                        formatXPosAppendWithBlank(20, false, refInterne));

                // 6 | Numero de l'annonce
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ANNONCE,
                        formatXPosAppendWithZero(6, true, rciViewBean.getIdRCI()));

                // 7 | Numero d'assure
                String noAvs = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                noAvs = PRStringUtils.replaceString(noAvs, ".", "");
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE,
                        formatXPosAppendWithBlank(11, false, noAvs));

                // 8 | Etat nominatif
                // Pas utilise
                // remoteEcritureAnnonce.put(IHEAnnoncesViewBean.ETAT_NOMINATIF,
                // formatXPosAppendWithBlank(40, false, ""));

                // 9 | Sexe
                // Pas utilise

                // 10 | Date naissance = JJMMAAAA
                // Pas utilise

                // 11 | Etat d'origine
                // Pas utilise

                // 12 | Motif de l'annonce
                int intMotif = Integer.parseInt(rciViewBean.getMotifForDemande());
                remoteEcritureAnnonce.put(IHEAnnoncesViewBean.MOTIF_ANNONCE, rciViewBean.getMotifForDemande());

                // 13 | Numéro d'assuré 1 utilise jusqu'ici
                remoteEcritureAnnonce
                        .put(IHEAnnoncesViewBean.NUMERO_ASSURE_1, formatXPosAppendWithBlank(11, false, ""));

                // 14 | Reserve: a blanc
                // Pas utilise
                // remoteEcritureAnnonce.put(IHEAnnoncesViewBean.RESERVE_A_BLANC,
                // formatXPosAppendWithBlank(8, false, ""));

                // Ajout de l'annonce dans HERMES
                remoteEcritureAnnonce.add(transaction);

                // ///////////////////////////////////////////////////////////////////////
                // Enregistrement 04
                // ///////////////////////////////////////////////////////////////////////
                if (!transaction.hasErrors()) {
                    // 2 | Code enregistrement: 01
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT,
                            REDemandesRassemblementHelper.CODE_ENREGISTREMENT_04);

                    // 3 | Ayant droit
                    // Pour les motifs (71, 75, 79, 81, 85 et (98))
                    if ((intMotif == 71) || (intMotif == 75) || (intMotif == 79) || (intMotif == 81)
                            || (intMotif == 85) || (intMotif == 98)) {

                        // si le tiers n'a pas l'ayant droit => AYANT_DROIT = 0
                        // et on donne le nouvel ayant droit
                        if (!rciViewBean.getIsTiersAyantDroit().booleanValue()
                                && !JadeStringUtil.isNull(rciViewBean.getIdTiersAyantDroit())) {

                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.AYANT_DROIT, "0");
                            PRTiersWrapper tiersAyantDroit = PRTiersHelper.getPersonneAVS(transaction.getISession(),
                                    rciViewBean.getIdTiersAyantDroit());
                            String noAvsAyantDroit = tiersAyantDroit
                                    .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                            noAvsAyantDroit = PRStringUtils.replaceString(noAvsAyantDroit, ".", "");
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT,
                                    formatXPosAppendWithBlank(11, false, noAvsAyantDroit));
                        } else {
                            remoteEcritureAnnonce.put(IHEAnnoncesViewBean.AYANT_DROIT, "1");
                        }
                    }

                    // 5 | Etat nominatif
                    // Pas utilise

                    // 6 | Date de cloture MMAA
                    // Pas de date de cloture pour les motifs > 91
                    if (!(intMotif > 91)) {
                        // MM.AAAA -> MMAA
                        String dateCloture = rciViewBean.getDateClotureForDemande();
                        if (!JadeStringUtil.isEmpty(dateCloture)) {
                            String MM = dateCloture.substring(0, dateCloture.indexOf("."));
                            String AA = dateCloture.substring(dateCloture.indexOf(".") + 3, dateCloture.length());

                            dateCloture = MM + AA;
                        }

                        remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_CLOTURE_MMAA, dateCloture);
                    }

                    // 7 | Reserve : a blanc

                    // 8 | 62- 67 | Date de l'ordre
                    remoteEcritureAnnonce.put(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA,
                            JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDMMYY));

                    // Ajout de l'annonce dans HERMES
                    remoteEcritureAnnonce.add(transaction);

                    if (!transaction.hasErrors()) {
                        refUnique = remoteEcritureAnnonce.getRefUnique();
                    }
                }
            }
        } catch (Exception ex) {
            transaction.addErrors(ex.toString());
        }

        return refUnique;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * 
     * @param nombrePos
     * @param isAppendLeft
     * @param value
     * @return
     */
    private String formatXPosAppendWithBlank(int nombrePos, boolean isAppendLeft, String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append(" ");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append(" ");
                }
            }
        }
        return result.toString();
    }

    /**
     * 
     * @param nombrePos
     * @param isAppendLeft
     * @param value
     * @return
     */
    private String formatXPosAppendWithZero(int nombrePos, boolean isAppendLeft, String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append("0");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
            }
        }
        return result.toString();
    }

    /**
     * Cherche un idTiers d'un ayant droit pour un tiers decede. (Le dernier des conjoints)
     * 
     * @param session
     * @param idTiersRequerant
     * @return
     */
    private String rechercherIdTiersAyantDroit(BSession session, ISFMembreFamilleRequerant mfRequerant,
            String idTiersRequerant) throws Exception {

        String idTiersAyantDroit = "";
        ISFRelationFamiliale relationRetenue = null;
        ISFMembreFamilleRequerant enfantRetenu = null;

        // rechercher les membres de la famille
        globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);
        ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiersRequerant);

        // Pour chaque membre de la famille
        for (int i = 0; (membresFamille != null) && (i < membresFamille.length); i++) {
            ISFMembreFamilleRequerant mf = membresFamille[i];

            // si pas d'idTiers, pas de rente
            // si une date de deces, ce n'est pas un ayant droit envisageable
            // on ne considere pas les enfants
            if (JadeStringUtil.isIntegerEmpty(mf.getIdTiers()) || !JadeStringUtil.isEmpty(mf.getDateDeces())
                    || ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(mf.getRelationAuRequerant())) {

                continue;
            } else {
                // on prend toutes les relations (dans l'ordre decroissant)
                // entre les deux membres
                ISFRelationFamiliale[] relationsFam = sf.getToutesRelationsConjoints(idTiersRequerant,
                        mf.getIdMembreFamille(), Boolean.FALSE);

                // si il y a des relations on prend la premiere
                for (int j = 0; (relationsFam != null) && (j < relationsFam.length); j++) {
                    ISFRelationFamiliale rf = relationsFam[j];
                    if (!JadeStringUtil.isEmpty(rf.getDateFin())) {
                        continue;
                    }

                    // si pas encore de relation
                    // ou la relation courante est une relation de conjoint
                    if ((relationRetenue == null)
                            || ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(rf.getTypeLien())) {
                        relationRetenue = rf;
                        idTiersAyantDroit = mf.getIdTiers();
                    }
                }
            }
        }

        // si on ne trouve pas de conjoint on regarde dans les enfants
        if (relationRetenue == null) {
            for (int i = 0; (membresFamille != null) && (i < membresFamille.length); i++) {
                ISFMembreFamilleRequerant mf = membresFamille[i];

                // on considere uniquement les enfants
                // si pas d'idTiers, pas de rente
                // si une date de deces, ce n'est pas un ayant droit
                // envisageable
                if (!JadeStringUtil.isIntegerEmpty(mf.getIdTiers()) || JadeStringUtil.isEmpty(mf.getDateDeces())
                        || ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(mf.getRelationAuRequerant())) {

                    // si pas encore d'enfant retenu
                    // ou l'enfant courant est plus age que l'enfant retenu
                    if ((enfantRetenu == null)
                            || BSessionUtil.compareDateFirstLower(session, mf.getDateNaissance(),
                                    enfantRetenu.getDateNaissance())) {
                        enfantRetenu = mf;
                        idTiersAyantDroit = mf.getIdTiers();
                    }
                }
            }
        }

        return idTiersAyantDroit;
    }
}
