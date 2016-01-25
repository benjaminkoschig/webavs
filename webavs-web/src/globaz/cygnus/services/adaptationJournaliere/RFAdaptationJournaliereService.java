package globaz.cygnus.services.adaptationJournaliere;

import globaz.cygnus.api.adaptationsJournalieres.IRFAdaptationJournaliere;
import globaz.cygnus.db.adaptationsJournalieres.RFAdaptationJournaliere;
import globaz.cygnus.db.adaptationsJournalieres.RFAdaptationJournaliereManager;
import globaz.cygnus.db.adaptationsJournalieres.RFDateTraitementAdaptationJournaliere;
import globaz.cygnus.db.adaptationsJournalieres.RFDateTraitementAdaptationJournaliereManager;
import globaz.cygnus.services.RFDecisionPcService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;

/**
 * 
 * Crée et met à jour les Qds impactées par les décisions PC récemment validées
 * 
 * @author jje
 * 
 */
public class RFAdaptationJournaliereService {

    // Liste contenant le context propre à chaque adaptation
    private List<RFAdaptationJournaliereContext> contextsListe = new ArrayList<RFAdaptationJournaliereContext>();
    private List<String> dates;
    private String idGestionnaire = "";
    // Liste de logs, String[idAdaptation,idTiersBeneficiaire,idDecision,libelle]
    private List<String[]> logsList = new ArrayList<String[]>();

    private final int NB_MAX_JOURS_ENTRE_DERNIER_TRAITEMENT_AUJOURDHUI = 730;

    private BSession session = null;

    private BITransaction transaction = null;

    public RFAdaptationJournaliereService(String idGestionnaire, BSession session, BITransaction transaction) {
        super();
        this.session = session;
        this.transaction = transaction;
        this.idGestionnaire = idGestionnaire;
    }

    private void ajouterAdaptationJournaliere(DecisionPcVO decisionPcVoCourant) throws ParseException, Exception {
        RFAdaptationJournaliere rfAdaJou = new RFAdaptationJournaliere();
        rfAdaJou.setSession(getSession());

        // System.out.println("NoDéc: " + decisionPcVoCourant.getNoDecision() + " Ddéb: "+
        // decisionPcVoCourant.getDateDebut() + " TypePC(sup:02): "+
        // decisionPcVoCourant.getCsTypeDecision());

        rfAdaJou.setDateDeDebut("01." + decisionPcVoCourant.getDateDebut());
        String dateFinStr = "";

        if (!JadeStringUtil.isBlankOrZero(decisionPcVoCourant.getDateFin())) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            GregorianCalendar calendar = new GregorianCalendar();

            Date dateFinDate = dateFormat.parse("01." + decisionPcVoCourant.getDateFin());
            calendar.setTime(dateFinDate);

            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_WEEK, -1);
            dateFinDate = calendar.getTime();
            dateFinStr = dateFormat.format(dateFinDate);

        }

        rfAdaJou.setDateDeFin(dateFinStr);
        rfAdaJou.setEtat(IRFAdaptationJournaliere.ETAT_A_TRAITER);
        rfAdaJou.setIdDecisionPc(decisionPcVoCourant.getIdDecision());
        rfAdaJou.setIdGestionnaire(getIdGestionnaire());
        rfAdaJou.setIdTiersBeneficiaire(decisionPcVoCourant.getIdTiersBeneficiaire());
        rfAdaJou.setNumeroDecisionPc(decisionPcVoCourant.getNoDecision());
        rfAdaJou.setTypeDeDecisionPc(decisionPcVoCourant.getCsTypeDecision());
        rfAdaJou.setNss(decisionPcVoCourant.getNss());

        rfAdaJou.add(getTransaction());
    }

    private void ajouterDatesATraiter() throws Exception {
        for (String dateCourante : dates) {

            RFDateTraitementAdaptationJournaliere rfDatTraAdaJou = new RFDateTraitementAdaptationJournaliere();
            rfDatTraAdaJou.setSession(getSession());
            rfDatTraAdaJou.setDateTraitement(dateCourante);

            rfDatTraAdaJou.add(getTransaction());
        }
    }

    private void commitTransaction() throws Exception {

        if (hasLogListError(getLogsList()) || getTransaction().hasErrors() || getTransaction().isRollbackOnly()) {

            getTransaction().rollback();

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "",
                    "L'ajout des nouvelles adaptations a échoué", getLogsList());

        } else {

            getTransaction().commit();

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.OK, "", "", "", "", "",
                    "L'ajout des nouvelles adaptations s'est effectué avec succès", getLogsList());
        }
    }

    /**
     * 
     * Génére une liste de dates (jj.mm.aaaa) correspondant aux jours entre la date de dernier traitement et aujourd'hui
     * 
     * 
     * @param String
     *            dateDernierTraitementJJsMMsAAAA
     * @return List<String> datesSet
     * @throws Exception
     */
    private List<String> datesEntreDernierTraitementEtAujourdhui(String dateDernierTraitementJJsMMsAAAA)
            throws Exception {

        List<String> datesSet = new ArrayList<String>();

        if (!JadeStringUtil.isBlankOrZero(dateDernierTraitementJJsMMsAAAA)) {

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            GregorianCalendar calendar = new GregorianCalendar();

            Date dateDernierTraitementDate = dateFormat.parse(dateDernierTraitementJJsMMsAAAA);
            Date aujourdhuiDate = dateFormat.parse(JACalendar.todayJJsMMsAAAA());

            if (dateDernierTraitementDate.getTime() < aujourdhuiDate.getTime()) {

                String dateATraiterStr = "";
                String aujourdhuiStr = dateFormat.format(aujourdhuiDate);
                Date dateATraiter = dateDernierTraitementDate;
                int i = 0;
                while (!dateATraiterStr.equals(aujourdhuiStr) && (i < NB_MAX_JOURS_ENTRE_DERNIER_TRAITEMENT_AUJOURDHUI)) {

                    calendar.setTime(dateATraiter);
                    calendar.add(Calendar.DATE, 1);
                    dateATraiter = calendar.getTime();
                    dateATraiterStr = dateFormat.format(dateATraiter);

                    datesSet.add(dateATraiterStr);

                    i++;
                }

                if (i >= NB_MAX_JOURS_ENTRE_DERNIER_TRAITEMENT_AUJOURDHUI) {
                    datesSet = null;

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.WARNING, "", "", "", "", "", "Plus de "
                            + NB_MAX_JOURS_ENTRE_DERNIER_TRAITEMENT_AUJOURDHUI
                            + " jours séparent la date de dernier traitement à aujourd'hui", getLogsList());
                }

            } else {
                datesSet = null;

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "",
                        "Date de dernier traitement plus grande ou égale à la date du jour", getLogsList());
            }

        } else {
            datesSet = null;
        }

        return datesSet;

    }

    /**
     * Initialisation du contexte
     * 
     * @throws Exception
     */
    public void executerAdaptations() throws Exception {

        // Recherche des adaptations à traiter
        RFAdaptationJournaliereManager rfAdaJouMgr = new RFAdaptationJournaliereManager();
        rfAdaJouMgr.setSession(getSession());

        String[] forEtatsAdaptation = new String[] { IRFAdaptationJournaliere.ETAT_A_TRAITER,
                IRFAdaptationJournaliere.ETAT_ECHEC };
        rfAdaJouMgr.setForEtatsAdaptation(forEtatsAdaptation);
        rfAdaJouMgr.changeManagerSize(0);
        rfAdaJouMgr.find();

        Iterator<RFAdaptationJournaliere> rfAdaJouItr = rfAdaJouMgr.iterator();

        while (rfAdaJouItr.hasNext()) {

            RFAdaptationJournaliere rfAdaJouCourante = rfAdaJouItr.next();

            if (null != rfAdaJouCourante) {

                RFAdaptationJournaliereContext rfAdajouCon = initialiserContext(rfAdaJouCourante);
                getContextsListe().add(rfAdajouCon);

            } else {
                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "",
                        "Impossible de retrouver l'adaptation journalière (initialisation contexte)", getLogsList());
            }
        }

        // Fermeture de la transaction du process
        if (transaction != null) {
            transaction.closeTransaction();
        }

        // Appel du provider pour chaque context, commit par context
        for (RFAdaptationJournaliereContext contextCourant : getContextsListe()) {

            // System.out.println("idAdaptation: " + contextCourant.getIdAdaptationJournaliere());

            try {

                transaction = (session).newTransaction();

                if (getTransaction() != null) {

                    transaction.openTransaction();

                    contextCourant = RFAdaptationJournaliereProvider.getHandler(contextCourant, session,
                            (BTransaction) getTransaction(), logsList, contextCourant.getGestionnaire(), false)
                            .executerAdaptation();

                    if (contextCourant.getEtat().equals(IRFAdaptationJournaliere.ETAT_ECHEC) || hasTransactionErrors()) {

                        reInitTransaction();

                        contextCourant.setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);
                        contextCourant = majEtatAdaptation(contextCourant);

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR,
                                contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(),
                                "Impossible d'effectuer l'adaptation relative à la décision", getLogsList());

                        reCommitTransaction();

                    } else {

                        contextCourant = majEtatAdaptation(contextCourant);

                        getTransaction().commit();

                        RFUtils.ajouterLogAdaptation(FWViewBeanInterface.OK,
                                contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                                contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                                contextCourant.getNumeroDecisionPc(), "l'adaptation s'est effectuée avec succés ",
                                getLogsList());

                    }

                } else {

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.OK, contextCourant.getIdAdaptationJournaliere(),
                            contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                            contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                            "transaction null, Impossible d'effectuer l'adaptation", getLogsList());

                }

            } catch (Exception e) {

                reInitTransaction();

                contextCourant.setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);
                contextCourant = majEtatAdaptation(contextCourant);

                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                        contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                        contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(), e.getMessage(),
                        getLogsList());

                reCommitTransaction();

            } finally {
                transaction.closeTransaction();
            }
        }

    }

    private List<RFAdaptationJournaliereContext> getContextsListe() {
        return contextsListe;
    }

    public List<String> getDates() {
        return dates;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public List<String[]> getLogsList() {
        return logsList;
    }

    public BSession getSession() {
        return session;
    }

    public BITransaction getTransaction() {
        return transaction;
    }

    private boolean hasLogListError(List<String[]> logList) {

        for (String[] logCourant : logList) {
            if ((logCourant != null) && logCourant[0].equals(FWViewBeanInterface.ERROR)) {
                return true;
            }
        }

        return false;

    }

    private boolean hasTransactionErrors() {
        return getTransaction().hasErrors() || getTransaction().isRollbackOnly();
    }

    /**
     * Initialisation du contexte (Type de décision PC, Pot Qd, Infos droit PC nouvelle décision, Qds exisitantes)
     */
    private RFAdaptationJournaliereContext initialiserContext(RFAdaptationJournaliere rfAdaJouCourante) {

        RFAdaptationJournaliereContext nouveauContext = new RFAdaptationJournaliereContext();
        nouveauContext.setIdAdaptationJournaliere(rfAdaJouCourante.getIdAdaptationJournaliere());
        nouveauContext.setTypeDeDecisionPc(rfAdaJouCourante.getTypeDeDecisionPc());
        nouveauContext.setDateDebutDecision(rfAdaJouCourante.getDateDeDebut());
        nouveauContext.setDateFinDecision(rfAdaJouCourante.getDateDeFin());
        nouveauContext.setGestionnaire(rfAdaJouCourante.getIdGestionnaire());
        nouveauContext.setIdTiersBeneficiaire(rfAdaJouCourante.getIdTiersBeneficiaire());
        nouveauContext.setIdDecisionPc(rfAdaJouCourante.getIdDecisionPc());
        nouveauContext.setNumeroDecisionPc(rfAdaJouCourante.getNumeroDecisionPc());
        nouveauContext.setNssTiersBeneficiaire(rfAdaJouCourante.getNss());
        // nouveauContext.setDateDernierPmt(this.getDateDernierPmt());

        return nouveauContext;

    }

    private boolean isDecisionDom2RNonTraitee(Set<String> idDecConjointSet, DecisionPcVO decisionPcVoCourant) {

        return (!JadeStringUtil.isBlankOrZero(decisionPcVoCourant.getIdDecisionConjoint()) && !idDecConjointSet
                .contains(decisionPcVoCourant.getIdDecisionConjoint()))
                || JadeStringUtil.isBlankOrZero(decisionPcVoCourant.getIdDecisionConjoint());
    }

    private boolean isDecisionPcTraite(String forIdDecision) throws Exception {
        RFAdaptationJournaliereManager rfAdaptationMgr = new RFAdaptationJournaliereManager();
        rfAdaptationMgr.setSession(getSession());
        rfAdaptationMgr.setForIdDecision(forIdDecision);
        // TODO: possibilité de relancer le batch -> non
        // rfAdaptationMgr.setForEtatsAdaptation(new String[] { IRFAdaptationJournaliere.ETAT_SUCCES });
        rfAdaptationMgr.changeManagerSize(0);
        rfAdaptationMgr.find();

        if (rfAdaptationMgr.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isNotDecisionPcRefusSansCalculOuAdaptationAc(DecisionPcVO decisionPcVoCourant) {
        return !(decisionPcVoCourant.getCsTypeDecision().equals(IPCDecision.CS_TYPE_REFUS_SC) || decisionPcVoCourant
                .getCsTypeDecision().equals(IPCDecision.CS_TYPE_ADAPTATION_AC));
    }

    private RFAdaptationJournaliereContext majEtatAdaptation(RFAdaptationJournaliereContext contextCourant)
            throws Exception {

        // Màj de l'état et de la date de traitement de l'adaptation
        RFAdaptationJournaliere rfAdaJou = new RFAdaptationJournaliere();
        rfAdaJou.setSession(getSession());

        rfAdaJou.setIdAdaptationJournaliere(contextCourant.getIdAdaptationJournaliere());
        rfAdaJou.retrieve();

        if (!rfAdaJou.isNew()) {
            rfAdaJou.setDateDeTraitement(JACalendar.todayJJsMMsAAAA());
            rfAdaJou.setEtat(contextCourant.getEtat());

            rfAdaJou.update(getTransaction());

        } else {

            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                    "Impossible de mettre à jour l'état de l'adaptation relative à la décision ", getLogsList());

            contextCourant.setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);
        }

        return contextCourant;

    }

    /**
     * 
     * Ajoute les nouvelles décisions PC dans la table des adaptations journalières à traiter
     * 
     * @throws Exception
     */
    public void rechercherAdaptations() throws Exception {

        // Recherche des dates à traiter
        String dateDernierTraitement = rechercherDerniereDateDeTraitement();

        dates = datesEntreDernierTraitementEtAujourdhui(dateDernierTraitement);

        if (null != dates) {

            ajouterDatesATraiter();

            // Appel du service PC retournant les dernières décisions PC validées selon les dates à traiter
            RFDecisionPcService rfDecisionPcService = new RFDecisionPcService((BTransaction) getTransaction());
            List<DecisionPcVO> decisionPcVos = rfDecisionPcService.getDecisionPc(dates);

            // déclaration d'une set permettant d'exclure la décision du conjoint d'un couple DOM2R (domicile avec 2
            // rentes séparées)
            Set<String> idDecConjointSet = new HashSet<String>();

            trierDecisionsParNumeroDecision(decisionPcVos);

            // Ajout des nouvelles décisions dans la table des adaptations journalières
            for (DecisionPcVO decisionPcVoCourant : decisionPcVos) {
                if (null != decisionPcVoCourant) {

                    if (isNotDecisionPcRefusSansCalculOuAdaptationAc(decisionPcVoCourant)
                            && isDecisionDom2RNonTraitee(idDecConjointSet, decisionPcVoCourant)
                            && !isDecisionPcTraite(decisionPcVoCourant.getIdDecision())) {

                        if (!JadeStringUtil.isBlankOrZero(decisionPcVoCourant.getIdDecisionConjoint())) {
                            idDecConjointSet.add(decisionPcVoCourant.getIdDecisionConjoint());
                            idDecConjointSet.add(decisionPcVoCourant.getIdDecision());
                        }

                        ajouterAdaptationJournaliere(decisionPcVoCourant);

                    }
                } else {
                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "",
                            "Impossible de retrouver la decision PC", getLogsList());
                }
            }
        } else {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "",
                    "Impossible de retrouver les dates à traiter", getLogsList());
        }

        try {
            commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    private String rechercherDerniereDateDeTraitement() throws Exception {

        String dateDernierTraitement = "";

        RFDateTraitementAdaptationJournaliereManager rfDatTraiAdaJouMgr = new RFDateTraitementAdaptationJournaliereManager();
        rfDatTraiAdaJouMgr.setSession(getSession());
        rfDatTraiAdaJouMgr.setForDerniereDateDeTraitement(true);
        rfDatTraiAdaJouMgr.changeManagerSize(0);
        rfDatTraiAdaJouMgr.find();

        if (rfDatTraiAdaJouMgr.size() == 1) {
            RFDateTraitementAdaptationJournaliere rfDatTraAdaJou = (RFDateTraitementAdaptationJournaliere) rfDatTraiAdaJouMgr
                    .getFirstEntity();

            if (null != rfDatTraAdaJou) {
                dateDernierTraitement = rfDatTraAdaJou.getDateTraitement();
            } else {
                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "",
                        "Impossible de retrouver la dernière date de traitement (date null)", getLogsList());
            }

        } else {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "",
                    "Impossible de retrouver la dernière date de traitement", getLogsList());
        }

        return dateDernierTraitement;
    }

    private void reCommitTransaction() throws Exception {
        if (hasTransactionErrors()) {
            getTransaction().rollback();
        } else {
            getTransaction().commit();
        }
    }

    private void reInitTransaction() throws Exception {

        getTransaction().rollback();
        getTransaction().closeTransaction();

        transaction = (session).newTransaction();
        transaction.openTransaction();
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setLogsList(List<String[]> logsList) {
        this.logsList = logsList;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BITransaction transaction) {
        this.transaction = transaction;
    }

    private void trierDecisionsParNumeroDecision(List<DecisionPcVO> decisionPcVos) {
        Comparator<DecisionPcVO> compareListDecisionPc = new Comparator<DecisionPcVO>() {
            @Override
            public int compare(DecisionPcVO o1, DecisionPcVO o2) {
                int o1NoPc = new Integer(o1.getNoDecision().replace("-", "")).intValue();
                int o2NoPc = new Integer(o2.getNoDecision().replace("-", "")).intValue();

                if (o1NoPc > o2NoPc) {
                    return 1;
                } else {
                    if (o1NoPc == o2NoPc) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        };

        Collections.sort(decisionPcVos, compareListDecisionPc);
    }

}