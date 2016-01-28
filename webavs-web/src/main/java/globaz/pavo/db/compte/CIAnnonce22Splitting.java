package globaz.pavo.db.compte;

import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEOutputAnnonce;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.splitting.CIEcrituresSplittingContainer;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Wrapper de l'annonce 22. Date de création : (20.12.2002 09:51:15)
 * 
 * @author: Administrator
 */
public class CIAnnonce22Splitting extends CIAnnonceWrapper {
    // id années splitting de l'annonce 22
    private final static String[] idAnneeSplitting = new String[] {
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_01,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_02,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_03,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_04,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_05,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_06,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_07,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_08,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_09,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_10,
            IHEAnnoncesViewBean.LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_11 };
    private boolean dejaTraitee = false;
    String id65 = null;

    /**
     * Constructeur CIAnnonce22Splitting.
     * 
     * @param annonce
     *            l'annonce en suspens
     */
    public CIAnnonce22Splitting(CIAnnonceSuspens annonce) {
        super(annonce);
    }

    /**
     * Retourne le contenu de l'annonce. Date de création : (23.12.2002 07:51:18)
     * 
     * @return le contenu de l'annonce.
     */
    private String getContenuAnnonce() throws Exception {
        StringBuffer contenuAnnonce = new StringBuffer();
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_AVS"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
        contenuAnnonce.append(" ou ");
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_NOM"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_MOTIF"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_DATE"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.DATE_CLOTURE_OU_ORDRE_SPLITTING_MMAA));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_CAISSE"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_AGENCE"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
        contenuAnnonce.append("\n");
        contenuAnnonce.append(getSession().getLabel("MSG_ANNONCE_EMAIL_REF"));
        contenuAnnonce.append(remoteAnnonce.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
        return contenuAnnonce.toString();
    }

    /**
     * Annonce le CI après un splitting.<br>
     * Remplace la méthode de la super-classes. Date de création : (06.01.2003 15:25:17)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @exception si
     *                une erreur survient
     */
    @Override
    public void terminer(BTransaction transaction, ArrayList allAnnonces) throws Exception {
        if (!isAnnonceSuspens() && id65 == null) { // modif 12.01.04
            // !isCanceled()) {
            if (!dejaTraitee) {
                // assuré
                ArrayList result = new ArrayList();
                ArrayList resultC = new ArrayList();
                CIRassemblementOuvertureManager rass = new CIRassemblementOuvertureManager();
                rass.setSession(getSession());
                rass.setForDateOrdre(getDateBidouillee(remoteAnnonceCompl
                        .getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA)));
                rass.setForMotifArc(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
                rass.setForCompteIndividuelId(compte.getCompteIndividuelId());
                rass.find(transaction);
                // recherche du bon enregistrement, si plusieurs
                CIRassemblementOuverture rassBen = null;
                if (rass.size() > 1) {
                    // agence
                    String agenceTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI));
                    String caisseTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI));
                    for (int i = 0; i < rass.size(); i++) {
                        rassBen = (CIRassemblementOuverture) rass.getEntity(i);
                        if (application.isAnnoncesWA()) {
                            if (rassBen.getTypeEnregistrement().substring(3, 4).equals(agenceTCI)) {
                                // même agence, on garde celui-ci
                                break;
                            }
                        } else {
                            if (CIUtil.isCaisseDifferente(getSession())) {
                                if (rassBen.getRealCaisse().trim().equals(caisseTCI)
                                        && rassBen.getCaisseTenantCI().trim().equals(agenceTCI)) {
                                    // todo comparaison caisse/agence tenant CI
                                    break;
                                }

                            } else {
                                if (rassBen.getCaisseTenantCI().trim().equals(agenceTCI)) {
                                    // todo comparaison caisse/agence tenant CI
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    rassBen = (CIRassemblementOuverture) rass.getFirstEntity();
                }
                if (rassBen != null) {
                    result = rassBen.rassemblerEcritures(transaction);
                    annonceExtraitCI(transaction, result, false);
                } else {
                    annonceExtraitCI(transaction, null, false);
                }
                // conjoint
                if (ciConjoint != null && ciConjoint.isCiOuvert().booleanValue()
                        && !CICompteIndividuel.CS_REGISTRE_PROVISOIRE.equals(ciConjoint.getRegistre())) {
                    // si conjoint existant, test si le 95 est présent dans la
                    // liste des CI à annoncer
                    boolean contains95C = false;
                    Iterator it = allAnnonces.iterator();
                    while (it.hasNext() && !contains95C) {
                        if (((CIAnnonceWrapper) it.next()).getNumeroAvs().equals(ciConjoint.getNumeroAvs())) {
                            // 95 du conjoint trouvé, ne pas l'annonce
                            // maintenant
                            contains95C = true;
                        }
                    }
                    if (rassBen != null) {
                        resultC = rassBen.rassemblerEcrituresConjoint(transaction, ciConjoint.getCompteIndividuelId());
                    }
                    if (!contains95C) {
                        annonceExtraitCI(transaction, resultC, true);
                        // annonceExtraitCI(transaction, null, true);
                    }
                }
                /*
                 * if(listeCIAnnonces==null) { listeCIAnnonces = new ArrayList(); } if (compte != null) {
                 * listeCIAnnonces.add(compte.getCompteIndividuelId()); } if (ciConjoint != null) {
                 * listeCIAnnonces.add(ciConjoint.getCompteIndividuelId()); }
                 */
            } else {
                if (!"-3".equals(annonceSuspens.getIdLog())) {
                    // annoncer un RCI vide (-2)
                    annonceExtraitCI(transaction, new ArrayList(), false);
                } else {
                    // annoncer uniquement le CI du conjoint (-3)
                    annonceExtraitCI(transaction, null, true);
                }
            }
            // ajout dans la liste des CI envoyés (afin d'éviter les doublons
            // pour le conjoint)
            if (!transaction.hasErrors() && !remoteTransaction.hasErrors()) {
                // effacement de l'annonce
                annonceTraitee(transaction);
                transaction.commit();
                remoteTransaction.commit();
            } else {
                transaction.rollback();
                remoteTransaction.rollback();
                String error = "";
                if (transaction.hasErrors()) {
                    error += transaction.getErrors().toString();
                    transaction.clearErrorBuffer();
                }
                if (remoteTransaction.hasErrors()) {
                    error += remoteTransaction.getErrors().toString();
                    // remoteTransaction.clearErrorBuffer();
                }
                if (!JAUtil.isStringEmpty(error)) {
                    // ajout des erreur de transaction de le log de l'annonce
                    createLog(transaction, error);
                }
                suspendreAnnonce(transaction);
                transaction.commit();
            }
        }
    }

    /**
     * Traîte l'annonce 22 (splitting). Date de création : (20.12.2002 09:48:45)
     * 
     * @param transaction
     *            la transaction à utiliser
     * @param testFinal
     *            doit être à true pour signifier si les tests doivent être effectués.
     */
    @Override
    public void traitementAnnonce(BTransaction transaction, boolean testFinal) throws Exception {
        // recherche ci au ra avec no avs
        compte = CICompteIndividuel.loadCI(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), transaction);
        if (compte == null) {
            // n'existe pas. Recherche ci au ra avec no avs avant le 1.7.72
            // note -> envoi email erreur
            // compte =
            // CICompteIndividuel.loadCI(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972),
            // transaction);
        }
        // lecture du 22 02
        remoteAnnonceCompl = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
        remoteAnnonceCompl.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable" });
        remoteAnnonceCompl.setIdAnnonce(CIUtil.inc(remoteAnnonce.getIdAnnonce()));
        remoteAnnonceCompl.retrieve(remoteTransaction);
        // lecture du 22 03 (période de splitting)
        remoteAnnonce03 = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
        remoteAnnonce03.setMethodsToLoad(new String[] { "getIdAnnonce", "getInputTable" });
        remoteAnnonce03.setIdAnnonce(CIUtil.inc(remoteAnnonce.getIdAnnonce(), 2));
        remoteAnnonce03.retrieve(remoteTransaction);
        // test si un 65 a été envoyé ultérieurement et si les annonces de
        // retour sont présentes
        if (!JAUtil.isIntegerEmpty(annonceSuspens.getIdArc65())) {
            id65 = annonceSuspens.getIdArc65();
            // vérification extrait CI
            IHEOutputAnnonce remoteLectureAnnonce = (IHEOutputAnnonce) remoteSession.getAPIFor(IHEOutputAnnonce.class);
            remoteLectureAnnonce
                    .setMethodsToLoad(new String[] { "getIdAnnonce", "getAllAnnonceRetour", "getInputTable" });
            remoteLectureAnnonce.setIdAnnonce(annonceSuspens.getIdArc65());
            remoteLectureAnnonce.retrieve(remoteTransaction);
            if (remoteLectureAnnonce.getAllAnnonceRetour().booleanValue()) {
                // 65 arrivé. Tester si ci conjoint effectivement ouvert
                String conjoint65 = remoteLectureAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
                ciConjoint = CICompteIndividuel.loadCI(conjoint65, transaction);
                if (ciConjoint != null && ciConjoint.isCiOuvert().booleanValue()) {
                    id65 = null;
                }
            }
            return;
        }
        if (compte != null && (compte.isCiOuvert().booleanValue() || "-3".equals(annonceSuspens.getIdLog()))) {
            String idCaisse = application.getAdministration(getSession(),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE),
                    remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE),
                    new String[] { "getIdTiersAdministration" }).getIdTiersAdministration();
            // modifier le ci
            checkAndUpdateCI(transaction);
            // caisse tenant CI
            String caisseTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI));
            String agenceTCI = CIUtil.unPad(remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI));
            // container des écritures de splitting
            CIEcrituresSplittingContainer container = new CIEcrituresSplittingContainer();
            // ci trouvé, recherche des périodes de splitting déjà existante,
            // associées au CI
            CIPeriodeSplittingManager splitting = new CIPeriodeSplittingManager();
            splitting.setSession(getSession());
            splitting.setForCompteIndividuelId(compte.getCompteIndividuelId());
            splitting.find(transaction);
            // lire toutes les périodes de l'annonce 22 03
            for (int periodeAnnonce = 0; periodeAnnonce < idAnneeSplitting.length && !isAnnonceSuspens(); periodeAnnonce++) {
                // années de début et de fin + code particulier
                String triplet = remoteAnnonce03.getField(idAnneeSplitting[periodeAnnonce]);
                if (!JAUtil.isStringEmpty(triplet) && triplet.length() >= 8) {
                    String anneeDebutAnnonce = triplet.substring(0, 4);
                    String anneeFinAnnonce = triplet.substring(4, 8);
                    String particulierAnnonce = triplet.substring(8);
                    if (JAUtil.isStringEmpty(particulierAnnonce)) {
                        // mandat normal
                        particulierAnnonce = CIEcriture.CS_MANDAT_NORMAL;
                    } else {
                        // convertion code system du code particulier
                        particulierAnnonce = CIEcriture.CS_MANDAT_NORMAL.substring(0,
                                CIEcriture.CS_MANDAT_NORMAL.length() - 1)
                                + particulierAnnonce;
                    }
                    // tester les périodes existantes (révocation +
                    // chevauchement)
                    for (int i = 0; i < splitting.size(); i++) {
                        CIPeriodeSplitting periodeExistante = (CIPeriodeSplitting) splitting.getEntity(i);
                        // ne pas prendre en compte les splitting manuel (1-6)
                        if (!CIPeriodeSplitting.CS_SPLITTING_MANUEL.equals(periodeExistante.getTypeEnregistrementWA())) {
                            // test si période plausible (pas de chevauchement)
                            int anneeInt = Integer.parseInt(periodeExistante.getAnneeFin());
                            int anneeDebutAnnonceInt = Integer.parseInt(anneeDebutAnnonce);
                            if (anneeInt >= anneeDebutAnnonceInt) {
                                anneeInt = Integer.parseInt(periodeExistante.getAnneeDebut());
                                int anneeFinAnnonceInt = Integer.parseInt(anneeFinAnnonce);
                                if (anneeInt <= anneeDebutAnnonceInt || anneeInt <= anneeFinAnnonceInt) {
                                    // chevauchement
                                    // tester date révocation
                                    if (JAUtil.isStringEmpty(periodeExistante.getDateRevocation())) {
                                        boolean periodeTrouvee = false;
                                        // test caisse commettante
                                        if (!periodeExistante.getCaisseCommettante().equals(idCaisse)) {
                                            periodeTrouvee = true;
                                        } else {
                                            dejaTraitee = true;
                                            // test de la caisse tenant le CI
                                            if (application.isAnnoncesWA()) {
                                                if (application.getProperty(CIApplication.CODE_CAISSE)
                                                        .equals(caisseTCI)) {
                                                    if (periodeExistante.getTypeEnregistrement().length() > 3
                                                            && periodeExistante.getTypeEnregistrement().substring(3, 4)
                                                                    .equals(agenceTCI)) {
                                                        // même caisse
                                                        periodeTrouvee = true;
                                                    }
                                                } else {
                                                    // autre que 26? ne devrait
                                                    // pas arriver
                                                    periodeTrouvee = true;
                                                }
                                            } else {
                                                if (application.getProperty(CIApplication.CODE_CAISSE)
                                                        .equals(caisseTCI)) {
                                                    if (periodeExistante.getTypeEnregistrement().length() > 3
                                                            && periodeExistante.getCaisseTenantCI().trim()
                                                                    .equals(agenceTCI)) {
                                                        // même caisse
                                                        periodeTrouvee = true;
                                                    }
                                                } else {
                                                    if (CIUtil.isCaisseDifferente(getSession())) {
                                                        if (periodeExistante.getTypeEnregistrement().length() > 3
                                                                && periodeExistante.getRealCaisse().trim()
                                                                        .equals(caisseTCI)
                                                                && periodeExistante.getCaisseTenantCI().trim()
                                                                        .equals(agenceTCI)) {

                                                            // même agence
                                                            periodeTrouvee = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (periodeTrouvee) {
                                            if ("-1".equals(annonceSuspens.getIdLog())
                                                    || "-2".equals(annonceSuspens.getIdLog())
                                                    || "-3".equals(annonceSuspens.getIdLog())) {
                                                // aucun traitement, simplement
                                                // annoncer le CI (à la suite
                                                // d'un blocage de traitement)
                                                if ("-1".equals(annonceSuspens.getIdLog())) {
                                                    // ci complet à envoyer
                                                    dejaTraitee = false;
                                                }
                                                ciConjoint = CICompteIndividuel
                                                        .loadCI(remoteAnnonce03
                                                                .getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE),
                                                                transaction);
                                                return;
                                            }
                                            // annuler les périodes ajoutées
                                            transaction.rollback();
                                            // envoi email
                                            String message = java.text.MessageFormat.format(
                                                    getSession().getLabel("MSG_ANNONCE_22_SPL_EMAIL_MESSAGE"),
                                                    new Object[] {
                                                            remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE),
                                                            getContenuAnnonce() });
                                            ArrayList to = application.getEMailResponsableCI(transaction);
                                            envoiEmail(to, getSession().getLabel("MSG_ANNONCE_22_SPL_EMAIL_SUJET"),
                                                    message);
                                            // log
                                            createLog(transaction,
                                                    getSession().getLabel("MSG_ANNONCE_22_SPL_EMAIL_SUJET"));
                                            // mettre l'annonce en suspens
                                            suspendreAnnonce(transaction);
                                            // valider la suspension
                                            transaction.commit();
                                            return;
                                        }
                                    }
                                }
                            }
                        } // si diff de splitting manuel
                    } // for périodes de splitting
                    if (!isAnnonceSuspens()) {
                        // ajout de la période si aucun problème trouvé
                        CIPeriodeSplitting nouvellePeriode = new CIPeriodeSplitting();
                        nouvellePeriode.setSession(getSession());
                        nouvellePeriode.setCompteIndividuelId(compte.getCompteIndividuelId());
                        nouvellePeriode.setCaisseCommettante(idCaisse);
                        // String caisseTCI =
                        // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI);
                        // String agenceTCI =
                        // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI);
                        if (application.isAnnoncesWA()
                                && application.getProperty(CIApplication.CODE_CAISSE).equals(caisseTCI)) {
                            nouvellePeriode.setTypeEnregistrement(CIPeriodeSplitting.CS_SPLITTING.substring(0, 3)
                                    + agenceTCI.trim() + CIPeriodeSplitting.CS_SPLITTING.substring(4));
                        } else {
                            nouvellePeriode.setTypeEnregistrement(CIPeriodeSplitting.CS_SPLITTING);
                            // todo: ajout caisse tenant ci
                            nouvellePeriode.setCaisseTenantCI(agenceTCI.trim());
                            if (CIUtil.isCaisseDifferente(getSession())) {
                                nouvellePeriode.setRealCaisse(caisseTCI);
                            }
                        }
                        nouvellePeriode.setPartenaireNumeroAvs(remoteAnnonce03
                                .getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE));
                        nouvellePeriode.setAnneeDebut(anneeDebutAnnonce);
                        nouvellePeriode.setAnneeFin(anneeFinAnnonce);
                        nouvellePeriode.setParticulier(particulierAnnonce);
                        if (!dejaTraitee) {
                            // splitting des inscription
                            container = nouvellePeriode.splitter(transaction, compte, application, container);
                            id65 = container.getIdAnnonce65();
                            if (transaction.hasErrors()) {
                                // erreur sur transaction
                                // remoteTransaction.setRollbackOnly();
                                transaction.rollback();
                                StringBuffer errors = transaction.getErrors();
                                transaction.clearErrorBuffer();
                                String message = errors.toString();
                                // java.text.MessageFormat.format(
                                // getSession().getLabel("MSG_ANNONCE_22E_SPL_EMAIL_MESSAGE"),
                                // new Object[] { errors.toString() });
                                createLog(transaction, message);
                                suspendreAnnonce(transaction);
                                // valider la suspension
                                transaction.commit();
                                return;
                            } else if (id65 != null) {
                                // attente de la réponse au 65
                                // modif 12.01.04 transaction.rollback();
                                createLog(transaction, getSession().getLabel("MSG_ANNONCE_22C_SPL_EMAIL_SUJET"));
                                // modif 12.01.04 setCanceled(true);
                                annonceSuspens.setIdArc65(id65);
                                annonceSuspens.update(transaction);
                            }
                        }
                        // ajout de la période de splitting
                        nouvellePeriode.wantCallValidate(false);
                        nouvellePeriode.add(transaction);
                        // ajout du ci du conjoint
                        ciConjoint = container.getCI(
                                remoteAnnonce03.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE),
                                transaction);
                    }
                } // if années trouvées dans l'annonce
            } // for périodes de l'annonce
              // if (!isAnnonceSuspens()) { // modif 12.01.04 && !isCanceled()) {
              // ajout de l'annonce 95
              // créer clôture
            CIRassemblementOuverture rOsplitting = new CIRassemblementOuverture();
            rOsplitting.setSession(getSession());
            rOsplitting.setDateOrdre(getDateBidouillee(remoteAnnonceCompl
                    .getField(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA)));
            // application.getCalendar().todayjjMMMMaaaa());
            if (application.isAnnoncesWA() && application.getProperty(CIApplication.CODE_CAISSE).equals(caisseTCI)) {
                rOsplitting.setTypeEnregistrement(CIRassemblementOuverture.CS_SPLITTING.substring(0, 3)
                        + agenceTCI.trim() + CIRassemblementOuverture.CS_SPLITTING.substring(4));
            } else {
                rOsplitting.setTypeEnregistrement(CIRassemblementOuverture.CS_SPLITTING);
                // todo: ajout caisse tenant ci
                rOsplitting.setCaisseTenantCI(agenceTCI.trim());
                if (CIUtil.isCaisseDifferente(getSession())) {
                    rOsplitting.setRealCaisse(caisseTCI.trim());
                }
            }
            rOsplitting.setMotifArc(remoteAnnonce.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE));
            rOsplitting.setCaisseCommettante(idCaisse);
            rOsplitting.setReferenceInterne(remoteAnnonce
                    .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
            rOsplitting.setCompteIndividuelId(compte.getCompteIndividuelId());
            rOsplitting.add(transaction);
            // liens
            /*
             * if (container != null) { ArrayList ecrs =
             * container.getEcrituresSplitting(compte.getCompteIndividuelId()); if (ecrs != null) { Iterator it =
             * ecrs.iterator(); while (it.hasNext()) { CIEcriture ecr = (CIEcriture) it.next(); CIEcritureAnnonce lien =
             * new CIEcritureAnnonce(); lien.setSession(getSession()); lien.setIdEcritureAssure(ecr.getEcritureId());
             * lien.setIdRassemblement (rOsplitting.getRassemblementOuvertureId()); lien.add(transaction); } } }
             */
            // effacement de l'annonce
            // correction: effectuer tout à la fin
            // annonceTraitee(transaction);
            if (dejaTraitee) {
                // pour fin de traitement si bug: -2 = annoncer CI vide
                annonceSuspens.setIdLog("-2");
            } else {
                // pour fin de traitement si bug: -1 = annoncer CI plein
                annonceSuspens.setIdLog("-1");
            }
            annonceSuspens.update(transaction);
            // }
        } else {
            // ci n'existe pas non plus, envoi message d'erreur
            String message = java.text.MessageFormat.format(getSession().getLabel("MSG_ANNONCE_22B_SPL_EMAIL_MESSAGE"),
                    new Object[] { remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE), getContenuAnnonce() });
            ArrayList to = application.getEMailResponsableCI(transaction);
            envoiEmail(to, getSession().getLabel("MSG_ANNONCE_22B_SPL_EMAIL_SUJET"), message);
            // annule les modifications effectuées
            // remoteTransaction.setRollbackOnly();
            transaction.rollback();
            // log
            createLog(transaction, getSession().getLabel("MSG_ANNONCE_22B_SPL_EMAIL_SUJET"));
            // modification annonce
            // annonceSuspens.setNumeroCaisse(
            // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_CAISSE_COMMETTANTE)
            // + "."
            // +
            // remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_COMMETTANTE));
            // mettre l'annonce en suspens
            suspendreAnnonce(transaction);
            // valider la suspension
            transaction.commit();
        }
    }

    /**
     * Modifie l'état du CI avec copie si nécessaire.<br>
     * Note: cette méthode est appelée par la super-classe.
     * 
     * @param transaction
     *            la transaction à utiliser. Date de création : (27.11.2002 14:24:57)
     */
    @Override
    public void updateCI(BTransaction transaction) throws Exception {
        // modification: pas de mise à jour de l'en-tête pour les 22
        /*
         * // nom compte.setNomPrenom(checkAndSet(compte.getNomPrenom(),
         * remoteAnnonce.getField(IHEOutputAnnonce.ETAT_NOMINATIF))); // no avs précédant String result =
         * remoteAnnonce.getField(IHEAnnoncesViewBean.NUMERO_ASSURE_AVANT_1_7_1972 ); if (!JAUtil.isStringEmpty(result))
         * { compte.setNumeroAvsPrecedant(result); } // référence interne result = remoteAnnonce
         * .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE); if (!JAUtil.isStringEmpty(result)) {
         * compte.setReferenceInterne(result); }
         */
    }
}
