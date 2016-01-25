/*
 * Créé le 6 août 07
 */
package globaz.corvus.helpers.interetsmoratoires;

import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.taux.IRETaux;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoireManager;
import globaz.corvus.db.interetsmoratoires.REInteretMoratoire;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.taux.RETaux;
import globaz.corvus.db.taux.RETauxManager;
import globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean;
import globaz.corvus.vb.interetsmoratoires.REInteretMoratoireViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author BSC
 * 
 */
public class REInteretMoratoireHelper extends PRAbstractHelper {

    private static final BigDecimal _0 = new BigDecimal("0.00");
    private static final BigDecimal _100 = new BigDecimal("100.00");
    private static final BigDecimal _1200 = new BigDecimal("1200.00");

    private Map<String, REPrestationsDuesJointDemandeRente> prestationsMensuelles = new HashMap<String, REPrestationsDuesJointDemandeRente>();
    private Map<String, RETaux> tauxInterets = new HashMap<String, RETaux>();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redefini pour renseigner les champs du viewbean qui sera affiche dans l'ecran rc.
     * 
     * <p>
     * Cherche le montant retroactif du tiers beneficiaire et obtient une adresse de paiement valide.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        actionPreparerChercher(viewBean, action, (BSession) session);
    }

    /**
     * redefini pour charger l'adresse de paiement.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        // charger l'adresse de paiement
        rechargerAdressePaiement((BSession) session, (REInteretMoratoireViewBean) viewBean);
    }

    /**
     * prepare un viewBean pour l'affichage d'informations dans la page rc de la ca page.
     * 
     * <p>
     * Cherche le montant retroactif du tiers beneficiaire et obtient une adresse de paiement valide.
     * </p>
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
    public FWViewBeanInterface actionPreparerChercher(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        REInteretMoratoireViewBean imViewBean = (REInteretMoratoireViewBean) viewBean;

        // recharger l'adresse de paiement pour les cas ou l'adresse est
        // invalide
        rechargerAdressePaiement(session, imViewBean);

        return imViewBean;
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
    public FWViewBeanInterface calculerInteretMoratoire(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RECalculInteretMoratoireViewBean cimViewBean = (RECalculInteretMoratoireViewBean) viewBean;

        // transfert des donnees
        REInteretMoratoireViewBean imViewBean = new REInteretMoratoireViewBean();
        imViewBean.setSession(cimViewBean.getSession());
        imViewBean.setDateDebutDroit(cimViewBean.getDateDebutDroit());
        imViewBean.setDateDecision(cimViewBean.getDateDecision());
        imViewBean.setDateDepotDemande(cimViewBean.getDateDepotDemande());
        imViewBean.setIdTiersDemandeRente(cimViewBean.getIdTiersDemandeRente());
        imViewBean.setIdDemandeRente(cimViewBean.getIdDemandeRente());
        imViewBean.setDecisionDepuis(cimViewBean.getDecisionDepuis());

        BITransaction transaction = null;

        try {
            transaction = session.newTransaction();
            transaction.openTransaction();

            // on cherche les différentes adresse de paiement pour les rentes
            // accordees d'une demande de rente
            RECalculInteretMoratoireManager cimManager = new RECalculInteretMoratoireManager();
            cimManager.setSession(cimViewBean.getSession());
            cimManager.setForIdDemandeRente(imViewBean.getIdDemandeRente());
            cimManager.find(transaction);

            // la liste des differentes adresses de paiment des rentes accordees
            // de la demande de rente
            List<String> adressePaiement = new ArrayList<String>();

            Iterator<?> iter = cimManager.iterator();
            while (iter.hasNext()) {
                RECalculInteretMoratoire cim = (RECalculInteretMoratoire) iter.next();

                if (!adressePaiement.contains(cim.getIdTiersAdrPmt())) {
                    adressePaiement.add(cim.getIdTiersAdrPmt());
                }
            }

            // on regroupe le calcul des interets moratoires par adresse de
            // paiement
            Iterator<String> iterAdressePaiement = adressePaiement.iterator();

            while (iterAdressePaiement.hasNext()) {
                String idAdressePaiement = iterAdressePaiement.next();

                String interetMoratoireTotalAssure = calculerInteretsMoratoireTotalAssure(session, transaction,
                        imViewBean, idAdressePaiement);
                repartirInteretMoratoireTotal(session, transaction, imViewBean, interetMoratoireTotalAssure,
                        idAdressePaiement);

                // reset
                prestationsMensuelles = new HashMap<String, REPrestationsDuesJointDemandeRente>();
                tauxInterets = new HashMap<String, RETaux>();
            }

            if (transaction.hasErrors()) {
                transaction.rollback();
                imViewBean.setMessage(transaction.getErrors().toString());
                imViewBean.setMsgType(FWViewBeanInterface.ERROR);
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            imViewBean.setMessage(e.toString());
            imViewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

        return imViewBean;
    }

    /**
     * Calcul des interets moratoires selon Art. 26, al. 2, LPGA ; art. OPGA
     * 
     * ---------------------------------------------------------------------- Notations:
     * 
     * i = Taux de l'interet moratoire en pour cent. M1 = Mois du depot de la demande. A1 = Annee de depot de la
     * demande. M2 = Mois au cours duquel la premiere prestation aurait du être versee. A2 = Annee au cours de laquelle
     * la premiere prestation aurait du être versee. M3 = Mois au cours duquel a lieu le paiement de arrieres et des
     * interets moratoires. A3 = Annee au cours de laquelle a lieu le paiement de arrieres et des interets moratoires. S
     * = Somme des versements retroactif sur lesquels des interets moratoires sont dus. Sc = Montant non soumis a un
     * versement d'interets moratoires (dettes). Sa = Versement retroactif en main de l'assure. n = Nombre total des
     * prestations mensuelles due. Rj = jeme prestation mensuelle R1 designe la premiere prestation due, Rn la
     * prestation due pendant le mois precedant le paiement (Si plusieurs prestation pour un mois donne, on les
     * additionne). D1, D2 = Grandeurs auxiliaires pour la determination du moment ou les interets commencent a courir.
     * m = Nombre de mois a compter de l'echeance de la premiere prestation jusqu'au momentou les interets moratoires
     * commencent a courir. It = Interets moratoires dus en francs. Ia = Interets moratoires en faveur de l'assure.
     * 
     * -------------------------------------------------------------------------
     * 
     * @param session
     * @param transaction
     * @param imViewBean
     * @return
     */
    private String calculerInteretsMoratoireTotalAssure(BSession session, BITransaction transaction,
            REInteretMoratoireViewBean imViewBean, String idAdressePaiement) throws Exception {

        JADate date1 = new JADate(imViewBean.getDateDepotDemande());
        JADate date2 = new JADate(imViewBean.getDateDebutDroit());
        JADate date3 = new JADate(imViewBean.getDateDecision());

        String moisFinRetro = null;
        REDecisionJointDemandeRenteManager decisionManager = new REDecisionJointDemandeRenteManager();
        decisionManager.setSession(session);
        decisionManager.setForCsTypeDecision(IREDecision.CS_TYPE_DECISION_COURANT);
        decisionManager.setForCsEtatDecisions(IREDecision.CS_ETAT_VALIDE);
        decisionManager.setForNoDemandeRente(imViewBean.getIdDemandeRente());
        decisionManager.find();

        if (decisionManager.size() > 0) {
            REDecisionJointDemandeRente decision = (REDecisionJointDemandeRente) decisionManager.get(0);
            moisFinRetro = JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
                    "01." + decision.getDateDebutDroit(), -1));
        }

        int M1 = date1.getMonth();
        int A1 = date1.getYear();
        int M2 = date2.getMonth();
        int A2 = date2.getYear();
        int M3 = date3.getMonth();
        int A3 = date3.getYear();

        // initialisation des preststion mensuelles de la demande de rente
        loadPrestationsMensuelles(session, transaction, imViewBean, idAdressePaiement);

        // le nombre total des prestations dues
        int n = (M3 - M2) + (12 * (A3 - A2));
        BigDecimal S = REInteretMoratoireHelper._0;
        BigDecimal Rj = null;
        JADate moisCourant = date2;

        // pour toutes les preststions dues
        for (int j = 1; j <= n; j++) {
            // on cherche le montant total des prestations pour ce mois
            Rj = getMontantMensuel(moisCourant, moisFinRetro);

            S = S.add(Rj);
            moisCourant = moisSuivant(moisCourant);
        }

        // le total des dettes de la preparation au calcul des interets
        // moratoires
        BigDecimal Sc = retrieveDettes(session, transaction, imViewBean, idAdressePaiement);

        // le montant en main de l'assure
        BigDecimal Sa = S.subtract(Sc);

        // determine le nombre de mois a compter de l'echeance de la premiere
        // prestation jusqu'au moment ou les interets commencent a courir
        int m = 25;
        int D1 = (M1 - M2) + (12 * (A1 - A2)) + 13;
        int D2 = (1 - M2) + (12 * (2003 - A2)) + 1;

        if (m < D1) {
            m = D1;
        }

        if (m < D2) {
            m = D2;
        }

        if (n < (m - 1)) {
            // pas de droit aux interets moratoires
            return "0.0";
        } else {

            S = REInteretMoratoireHelper._0;
            BigDecimal It = REInteretMoratoireHelper._0;
            It = It.setScale(4, BigDecimal.ROUND_DOWN);

            int j = 1;
            moisCourant = new JADate(imViewBean.getDateDebutDroit());

            // avant le droit aux interets moratoires
            while (j < m) {
                // on cherche le montant total des prestations pour ce mois
                Rj = getMontantMensuel(moisCourant, moisFinRetro);

                S = S.add(Rj);
                moisCourant = moisSuivant(moisCourant);
                j++;
            }

            // initialisation de la Map des taux
            loadTauxInteretMoratoire(session, transaction);

            BigDecimal i = getTauxInteretMoratoire(moisCourant);

            BigDecimal im = S.multiply(i);
            im = im.divide(REInteretMoratoireHelper._1200, 4, BigDecimal.ROUND_DOWN);
            It = It.add(im);

            // des le droit aux interetsmoratoires
            while (!(j > n)) {
                // on cherche le montant total des prestations pour ce mois
                Rj = getMontantMensuel(moisCourant, moisFinRetro);

                i = getTauxInteretMoratoire(moisCourant);

                S = S.add(Rj);
                moisCourant = moisSuivant(moisCourant);
                j++;

                im = S.multiply(i);
                im = im.divide(REInteretMoratoireHelper._1200, 4, BigDecimal.ROUND_DOWN);
                It = It.add(im);
            }

            // on arrondit le montant total
            It = It.add(new BigDecimal("0.5"));
            It = It.setScale(0, BigDecimal.ROUND_DOWN);

            // l'interets moratoires en faveur de l'assure
            BigDecimal Ia = It.multiply(Sa);
            if (S.compareTo(REInteretMoratoireHelper._0) == 0) {
                Ia = REInteretMoratoireHelper._0;
            } else {
                Ia = Ia.divide(S, 4, BigDecimal.ROUND_DOWN);
                Ia = Ia.add(new BigDecimal("0.5"));
                Ia = Ia.setScale(0, BigDecimal.ROUND_DOWN);
            }

            return Ia.toString();
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

    /**
     * donne le montant de toutes les prestations de type mensuelle valables a la date donnee
     * 
     * @param moisCourant
     * @return
     */
    private BigDecimal getMontantMensuel(JADate moisCourant, String moisFinRetro) throws Exception {

        BigDecimal total = REInteretMoratoireHelper._0;

        Iterator<String> iter = prestationsMensuelles.keySet().iterator();

        String keyTest;
        if ((moisFinRetro != null)
                && JadeDateUtil.isDateMonthYearAfter(JadeDateUtil.convertDateMonthYear(moisCourant.toStr(".")),
                        moisFinRetro)) {
            keyTest = PRDateFormater.convertDate_MMAAAA_to_AAAAMM(moisFinRetro);
        } else {
            // la cle du mois courant
            keyTest = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(moisCourant.toStr("."));
        }

        while (iter.hasNext()) {

            // AAAAMM|AAAAMM|NN
            // borne inf.|borne sup.|pas utilise
            String key = iter.next();
            String keySupp = key.substring(6, 12);
            String keyInf = key.substring(0, 6);

            // si la prestation doit etre prise en compte on l'ajoute au total
            if ((keyTest.compareTo(keySupp) <= 0) && (keyTest.compareTo(keyInf) >= 0)) {
                total = total.add(new BigDecimal(prestationsMensuelles.get(key).getMontant()));
            }
        }

        return total;
    }

    /**
     * donne le taux du type "interet moratoire" pour la date donnee si pas de taux pour la date donnee retourne 0.00
     * 
     * @param moisCourant
     * @return
     */
    private BigDecimal getTauxInteretMoratoire(JADate moisCourant) throws Exception {

        BigDecimal taux = REInteretMoratoireHelper._0;

        Iterator<String> iter = tauxInterets.keySet().iterator();

        // la cle du mois courant
        String keyTest = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(moisCourant.toStr("."));

        while (iter.hasNext()) {

            // AAAAMMJJ|AAAAMMJJ
            // borne inf.|borne sup.
            String key = iter.next();
            String keySupp = key.substring(8, 16);
            String keyInf = key.substring(0, 8);

            // si la prestation doit etre prise en compte on l'ajoute au total
            if ((keyTest.compareTo(keySupp) <= 0) && (keyTest.compareTo(keyInf) >= 0)) {
                taux = new BigDecimal(tauxInterets.get(key).getTaux());
            }
        }

        return taux;
    }

    /**
     * Charge toute les prestations de type mensuelle pour une demande de rente dans une Map
     * 
     * La clé vaut: Date debut|Date fin|Identifiant unique AAAAMMAAAAMMNN
     * 
     * @param session
     * @param transaction
     * @param imViewBean
     * @throws Exception
     */
    private void loadPrestationsMensuelles(BSession session, BITransaction transaction,
            REInteretMoratoireViewBean imViewBean, String idAdressePaiement) throws Exception {

        REPrestationsDuesJointDemandeRenteManager pdjdrManager = new REPrestationsDuesJointDemandeRenteManager();
        pdjdrManager.setSession(session);
        pdjdrManager.setForNoDemandeRente(imViewBean.getIdDemandeRente());
        pdjdrManager.setForCsTypePrestationDue(IREPrestationDue.CS_TYPE_PMT_MENS);
        pdjdrManager.setForIncudeRAWithInteretMoratoireOnly(Boolean.TRUE);
        pdjdrManager.setForIdTiersAdrPmt(idAdressePaiement);
        pdjdrManager.find(transaction);

        // on ajoute les prestation de la demande de rente
        for (int i = 0; i < pdjdrManager.getSize(); i++) {
            REPrestationsDuesJointDemandeRente pdjdr = (REPrestationsDuesJointDemandeRente) pdjdrManager.getEntity(i);

            // si pas de date de fin la cle vaut: AAAAMM999999NN
            String key = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(pdjdr.getDateDebutPaiement())
                    + ((JadeStringUtil.isEmpty(pdjdr.getDateFinPaiement())) ? "999999" : PRDateFormater
                            .convertDate_JJxMMxAAAA_to_AAAAMM(pdjdr.getDateFinPaiement()))
                    + JadeStringUtil.fillWithZeroes(Integer.toString(i), 2);

            prestationsMensuelles.put(key, pdjdr);
        }
    }

    /**
     * Charge toute les taux du type "interet moratoire" dans une Map
     * 
     * La clé vaut: Date debut|Date fin AAAAMMJJAAAAMMJJ
     * 
     * @param session
     * @param transaction
     * @throws Exception
     */
    private void loadTauxInteretMoratoire(BSession session, BITransaction transaction) throws Exception {

        RETauxManager tManager = new RETauxManager();
        tManager.setSession(session);
        tManager.setForCsType(IRETaux.CS_TYPE_INTERET_MORATOIRE);
        tManager.find(transaction);

        // on ajoute les taux
        for (int i = 0; i < tManager.getSize(); i++) {
            RETaux t = (RETaux) tManager.getEntity(i);

            // si pas de date de fin la cle vaut: AAAAMMJJ99999999
            String key = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(t.getDateDebut())
                    + ((JadeStringUtil.isEmpty(t.getDateFin())) ? "99999999" : PRDateFormater
                            .convertDate_JJxMMxAAAA_to_AAAAMMJJ(t.getDateFin()));

            tauxInterets.put(key, t);
        }
    }

    /**
     * Donne la JADate correspondant au mois suivant
     * 
     * @param moisCourant
     */
    private JADate moisSuivant(JADate moisCourant) throws Exception {
        JADate moisSuivant = moisCourant;
        if (moisCourant.getMonth() == 12) {
            moisSuivant.setYear(moisCourant.getYear() + 1);
            moisSuivant.setMonth(1);
        } else {
            moisSuivant.setMonth(moisCourant.getMonth() + 1);
        }
        return moisSuivant;
    }

    /**
     * charge une adresse de paiement valide.
     * 
     * <p>
     * si les id adresse de paiment et domaine d'adresses sont renseignes, charge et formatte l'adresse correspondante.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param rpViewBean
     * 
     * @throws Exception
     */
    private void rechargerAdressePaiement(BSession session, REInteretMoratoireViewBean imViewBean) throws Exception {

        // si le tiers beneficiaire est null, il ne sert a rien de faire une
        // recherche
        // ce cas de figure peut survenir lors du chargement du viewBean utilise
        // dans l'ecran rc
        if (JadeStringUtil.isIntegerEmpty(imViewBean.getIdTiersAdrPmt())) {
            return;
        }

        // charcher une adresse de paiement pour ce beneficiaire

        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), imViewBean.getIdTiersAdrPmt(), imViewBean.getCsDomaineAdrPmt(),
                imViewBean.getIdAffilieAdrPmt(), JACalendar.todayJJsMMsAAAA());

        imViewBean.setAdressePaiement(adresse);

        // formatter les infos de l'adresse pour l'affichage correct dans
        // l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

            source.load(adresse);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                imViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                imViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            imViewBean.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
        } else {
            imViewBean.setCcpOuBanqueFormatte("");
            imViewBean.setAdresseFormattee("");
        }
    }

    /**
     * Repartit l'interet moratoire entre les rentes accordee de la demande de rente
     * 
     * @param session
     * @param transaction
     * @param imViewBean
     * @param interetMoratoireTotal
     */
    private void repartirInteretMoratoireTotal(BSession session, BITransaction transaction,
            REInteretMoratoireViewBean imViewBean, String interetMoratoireTotalAssure, String idAdressePaiement)
            throws Exception {

        RECalculInteretMoratoireManager cimManager = new RECalculInteretMoratoireManager();
        cimManager.setSession(session);
        cimManager.setForIdDemandeRente(imViewBean.getIdDemandeRente());
        cimManager.setForIdTiersAdrPmt(idAdressePaiement);
        cimManager.setOrderBy(RECalculInteretMoratoire.FIELDNAME_MONTANT_RETRO + "-"
                + RECalculInteretMoratoire.FIELDNAME_MONTANT_DETTE);
        cimManager.find(transaction);

        // Versement retroactif total en main des assures
        BigDecimal SaTotal = REInteretMoratoireHelper._0;
        BigDecimal Sa = null;

        // on fait la somme de Sa de toutes les preparations au calcul de im
        for (int i = 0; i < cimManager.getSize(); i++) {
            // calcul du versement retroactif total en main de cet assure
            RECalculInteretMoratoire cim = (RECalculInteretMoratoire) cimManager.getEntity(i);
            Sa = new BigDecimal(cim.getMontantRetro());
            Sa = Sa.subtract(new BigDecimal(cim.getMontantDette()));

            // ici on ne prend aussi les montants negatifs pour avoir le total
            // juste
            SaTotal = SaTotal.add(Sa);

        }

        // on repartit le total des interets moratoires entre les rentes
        // accordees
        BigDecimal ItDejaReparti = REInteretMoratoireHelper._0;
        BigDecimal tauxDejaReparti = REInteretMoratoireHelper._0;
        BigDecimal Sc = null;

        for (int i = 0; i < cimManager.getSize(); i++) {
            // calcul du versement retroactif total en main de cet assure
            RECalculInteretMoratoire cim = (RECalculInteretMoratoire) cimManager.getEntity(i);
            Sa = new BigDecimal(cim.getMontantRetro());
            Sc = new BigDecimal(cim.getMontantDette());
            Sa = Sa.subtract(Sc);

            BigDecimal ItRep = null;
            BigDecimal taux = null;

            // si zero ou negatif, on fixe le taux et le montant a zero
            if (Sa.signum() < 1) {

                taux = REInteretMoratoireHelper._0;
                ItRep = REInteretMoratoireHelper._0;

            } else {

                if (i == (cimManager.getSize() - 1)) {
                    // pour la derniere repartition, on fait la dif. entre
                    // l' interet total et l'interet deja reparti
                    ItRep = new BigDecimal(interetMoratoireTotalAssure).subtract(ItDejaReparti);
                    taux = REInteretMoratoireHelper._100.subtract(tauxDejaReparti);
                } else {

                    // calcul du taux
                    taux = REInteretMoratoireHelper._100;
                    taux = taux.multiply(Sa);
                    taux = taux.divide(SaTotal, 2, BigDecimal.ROUND_DOWN);

                    // repartition de l'interet moratoire
                    ItRep = new BigDecimal(interetMoratoireTotalAssure);
                    ItRep = ItRep.multiply(taux);
                    ItRep = ItRep.divide(REInteretMoratoireHelper._100, 2, BigDecimal.ROUND_DOWN);
                    // arrondi au franc
                    ItRep = JANumberFormatter.round(ItRep, 1, 2, JANumberFormatter.NEAR);

                    ItDejaReparti = ItDejaReparti.add(ItRep);
                    tauxDejaReparti = tauxDejaReparti.add(taux);
                }
            }

            // creation du resultat
            REInteretMoratoire im = new REInteretMoratoire();
            im.setSession(session);

            // retrouver l'adresse de paiement dans l'information complementaire
            // de la demande de rente
            RERenteAccordeeManager raManager = new RERenteAccordeeManager();
            raManager.setSession(session);
            raManager.setForIdCalulInteretMoratoire(cim.getIdCalculInteretMoratoire());
            raManager.find(transaction);

            // ici on considere qu l'adresse de paiement est deja reprise
            // correctement dans la rente accordee.
            RERenteAccordee ra = null;
            if (raManager.getSize() > 0) {

                ra = (RERenteAccordee) raManager.getFirstEntity();

                if (!JadeStringUtil.isIntegerEmpty(ra.getIdInfoCompta())) {

                    REInformationsComptabilite ic = new REInformationsComptabilite();
                    ic.setSession(session);
                    ic.setIdInfoCompta(ra.getIdInfoCompta());
                    ic.retrieve(transaction);

                    im.setIdTiersAdrPmt(ic.getIdTiersAdressePmt());
                    im.setIdAffilieAdrPmt("");

                    // Si le domaine rente n'existe pas, on récupère le domaine
                    // standard.
                    TIAdressePaiementData adr = PRTiersHelper.getAdressePaiementData(session,
                            (BTransaction) transaction, ic.getIdTiersAdressePmt(),
                            IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

                    if ((adr == null) || adr.isNew()) {
                        throw new Exception(session.getLabel("ERREUR_AUCUNE_ADRESSE_PMT_TROUVEE")
                                + ra.getIdPrestationAccordee() + "/" + ic.getIdTiersAdressePmt());
                    }

                    im.setCsDomaineAdrPmt(adr.getIdApplication());
                } else {
                    im.setIdTiersAdrPmt(cim.getIdTiers());
                    im.setCsDomaineAdrPmt(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
                }
            } else {
                im.setIdTiersAdrPmt(cim.getIdTiers());
                im.setCsDomaineAdrPmt(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            }

            im.setMontantInteret(ItRep.toString());
            im.setTauxRepartition(taux.toString());
            im.setDateCalculIM(imViewBean.getDateDecision());
            im.add(transaction);

            // mise a jours du calcul interet moratoire
            cim.retrieve(transaction);
            cim.setIdInteretMoratoire(im.getIdInteretMoratoire());
            cim.update(transaction);

        }
    }

    /**
     * Donne la somme des dettes donnee lors de la preparation au calcul des interets moratoires
     * 
     * @param session
     * @param transaction
     * @param imViewBean
     * @return
     */
    private BigDecimal retrieveDettes(BSession session, BITransaction transaction,
            REInteretMoratoireViewBean imViewBean, String idAdressePaiement) throws Exception {

        RECalculInteretMoratoireManager cimManager = new RECalculInteretMoratoireManager();
        cimManager.setSession(session);
        cimManager.setForIdDemandeRente(imViewBean.getIdDemandeRente());
        cimManager.setForIdTiersAdrPmt(idAdressePaiement);
        return cimManager.getSum(RECalculInteretMoratoire.FIELDNAME_MONTANT_DETTE, transaction);
    }
}
