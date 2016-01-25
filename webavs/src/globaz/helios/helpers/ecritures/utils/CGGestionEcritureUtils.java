package globaz.helios.helpers.ecritures.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.helios.api.ICGJournal;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.db.solde.CGSoldeUpdater;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Iterator;

public class CGGestionEcritureUtils {

    public static final int NUMBER_ECRITURE_DOUBLE = 2;

    /**
     * Le solde pour la période éxiste ? <br/>
     * Si le solde n'existe pas : creation else error.
     * 
     * @param transaction
     * @param ecriture
     * @param journal
     * @param compte
     * @param isSoldePeriode
     * @param isComptabiliser
     * @throws Exception
     */
    private static void checkAndCreateSoldeForPeriode(BISession session, BTransaction transaction,
            CGEcritureViewBean ecriture, String idPeriodeComptable, String idCentreCharge) throws Exception {
        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setSession((BSession) session);

        soldeManager.setForIdPeriodeComptable(idPeriodeComptable);

        if (JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
            soldeManager.setForEstPeriode(new Boolean(false));
        } else {
            soldeManager.setForEstPeriode(new Boolean(true));
        }

        soldeManager.setForIdCompte(ecriture.getIdCompte());
        soldeManager.setForIdCentreCharge(idCentreCharge);
        soldeManager.setForIdExerComptable(ecriture.getIdExerciceComptable());
        soldeManager.setForIdMandat(ecriture.getIdMandat());
        soldeManager.find(transaction, 2);

        if (soldeManager.getSize() > 1) {
            throw new Exception(((BSession) session).getLabel("SOLDE_ADD_ECRITURE_ERROR"));
        } else if (soldeManager.isEmpty()) {
            CGSolde solde = new CGSolde();
            solde.setSession((BSession) session);

            solde.setIdCompte(ecriture.getIdCompte());
            solde.setIdCentreCharge(idCentreCharge);
            solde.setIdExerComptable(ecriture.getIdExerciceComptable());
            solde.setIdMandat(ecriture.getExerciceComptable().getIdMandat());

            solde.setIdPeriodeComptable(idPeriodeComptable);

            if (JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
                solde.setEstPeriode(new Boolean(false));
            } else {
                solde.setEstPeriode(new Boolean(true));
            }

            solde.add(transaction);
        }
    }

    /**
     * Complète et ajoute une écriture en bd.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @param entete
     * @param ecriture
     * @return
     * @throws Exception
     */
    public static CGEcritureViewBean createEcriture(BISession session, BTransaction transaction,
            CGGestionEcritureViewBean ecritures, CGEnteteEcritureViewBean entete, CGEcritureViewBean ecriture)
            throws Exception {
        ecriture.setSession((BSession) session);

        ecriture.setIdEnteteEcriture(entete.getIdEnteteEcriture());
        ecriture.setIdJournal(ecritures.getIdJournal());
        ecriture.setIdExerciceComptable(ecritures.getJournal().getIdExerciceComptable());
        ecriture.setIdMandat(ecritures.getJournal().getExerciceComptable().getIdMandat());

        ecriture.wantEstActive(new Boolean(true));

        ecriture.setDate(ecritures.getDateValeur());
        ecriture.setDateValeur(ecritures.getDateValeur());

        ecriture.setPiece(ecritures.getPiece());
        ecriture.setRemarque(ecritures.getRemarque());

        ecriture.add(transaction);

        if (ecriture.hasErrors() || ecriture.isNew()) {
            throw new Exception(((BSession) session).getLabel("GESTION_ECRITURES_ECRITURE_CREATION_PROBLEME"));
        }

        return ecriture;
    }

    /**
     * Retrouve l'entête des écritures.
     * 
     * @param session
     * @param transaction
     * @param idEntete
     * @return
     * @throws Exception
     */
    public static CGEnteteEcritureViewBean getEntete(BISession session, BTransaction transaction, String idEntete)
            throws Exception {
        CGEnteteEcritureViewBean entete = new CGEnteteEcritureViewBean();
        entete.setSession((BSession) session);

        entete.setIdEnteteEcriture(idEntete);

        entete.retrieve(transaction);

        if (entete.isNew() || entete.hasErrors()) {
            throw new Exception(((BSession) session).getLabel("GESTION_ECRITURES_ENTETE_NON_RESOLUE"));
        }

        return entete;
    }

    /**
     * Return l'id externe d'un compte.
     * 
     * @param session
     * @param ecriture
     * @return
     * @throws Exception
     */
    public static String getIdExterneCompte(BISession session, CGEcritureViewBean ecriture) throws Exception {
        CGPlanComptableManager manager = new CGPlanComptableManager();
        manager.setSession((BSession) session);

        manager.setForIdExerciceComptable(ecriture.getIdExerciceComptable());
        manager.setForIdCompte(ecriture.getIdCompte());

        manager.find();

        if (manager.hasErrors() || manager.isEmpty()) {
            throw new Exception(((BSession) session).getLabel("AUCUN_COMPTE_RESOLU"));
        }

        return ((CGPlanComptableViewBean) manager.getFirstEntity()).getIdExterne();
    }

    /**
     * Une écriture collective ne peut-être passée sur un compte affilie.
     * 
     * @param session
     * @param transaction
     * @param ecritures
     * @throws Exception
     */
    public static void testCompteAffilie(BISession session, BTransaction transaction,
            CGGestionEcritureViewBean ecritures) throws Exception {
        if (ecritures.getJournal().getExerciceComptable().getMandat().isEstComptabiliteAVS().booleanValue()
                && (ecritures.getEcritures().size() > CGGestionEcritureUtils.NUMBER_ECRITURE_DOUBLE)) {
            Iterator<?> it = ecritures.getEcritures().iterator();
            while (it.hasNext()) {
                CGEcritureViewBean ecriture = (CGEcritureViewBean) it.next();

                if (!JadeStringUtil.isDecimalEmpty(ecriture.getMontantBase())
                        || !JadeStringUtil.isDecimalEmpty(ecriture.getMontantBaseMonnaie())) {
                    ecriture.setSession((BSession) session);
                    ecriture.setIdJournal(ecritures.getIdJournal());
                    if (ecriture.isForCompteAffillie(transaction)) {
                        throw new Exception(((BSession) session).getLabel("ENTETE_ECRITURE_CONFLIT_ECRITURE_AFFILIE"));
                    }
                }
            }
        }
    }

    /**
     * La date saisie par l'utilisateur est-elle conforme ?
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    public static void testDateValeur(BISession session, CGGestionEcritureViewBean ecritures) throws Exception {
        if (JadeStringUtil.isBlank(ecritures.getDateValeur())) {
            throw new Exception(((BSession) session).getLabel("ECRITURE_DATE_NON_RENSEIGNE"));
        }

        BSessionUtil.checkDateGregorian(((BSession) session), ecritures.getDateValeur());
    }

    /**
     * Contrôle si le journal est en état annulé ou en traitement
     * 
     * @param session
     * @param journal
     * @throws Exception
     */
    public static void testJournalAnnule(BISession session, CGJournal journal) throws Exception {
        if ((journal != null)
                && (journal.getIdEtat().equals(ICGJournal.CS_ETAT_ANNULE) || journal.getIdEtat().equals(
                        ICGJournal.CS_ETAT_TRAITEMENT))) {
            throw new Exception(((BSession) session).getLabel("ECRITURE_NON_TRAITEE_JOURNAL_ANNULE"));

        }
    }

    /**
     * L'écriture doit contenir plus d'une ligne. Minimum un débit et un crédit.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    public static void testMinimumDebitCredit(BISession session, CGGestionEcritureViewBean ecritures) throws Exception {
        if ((ecritures.getEcritures() == null) || (ecritures.getEcritures().size() <= 1)) {
            throw new Exception(((BSession) session).getLabel("GESTION_ECRITURES_DEBIT_CREDIT_MINIMUM"));
        }
    }

    /**
     * Contrôle le journal (public ou user=CGAdmin).
     * 
     * @param session
     * @param journal
     * @throws Exception
     */
    public static void testSaisieAutresUtilisateurs(BISession session, CGJournal journal) throws Exception {
        if ((journal != null) && !journal.isEstPublic().booleanValue()) {
            if (!session.getUserId().equalsIgnoreCase(journal.getProprietaire())
                    && !(CGApplication.isUserChefComptable((BSession) session))) {
                throw new Exception(((BSession) session).getLabel("SAISIE_JRN_AUTRES_UTILISATEURS_INTERDITE"));
            }
        }
    }

    /**
     * Contrôle que le journal soit en état ouvert.
     * 
     * @param session
     * @param journal
     * @throws Exception
     */
    public static void testSaisieEcranEtJournalOuvert(BISession session, CGGestionEcritureViewBean ecritures)
            throws Exception {
        if (ecritures.getSaisieEcran() && !ecritures.isJournalEditable()) {
            throw new Exception(((BSession) session).getLabel("SAISIE_JRN_ETAT_NON_OUVERT_INTERDITE") + " jrn : "
                    + ecritures.getIdJournal());
        }
    }

    /**
     * Le total du débit des écritures doit être égal au total des crédits. <br/>
     * Prévient l'utilisation du "Control-Enter" de l'utilisateur.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    public static void testTotalDebitCredit(BISession session, CGGestionEcritureViewBean ecritures) throws Exception {
        FWCurrency totalDebit = new FWCurrency();
        FWCurrency totalCredit = new FWCurrency();

        Iterator<?> it = ecritures.getEcritures().iterator();
        while (it.hasNext()) {
            CGEcritureViewBean ecriture = (CGEcritureViewBean) it.next();

            if (ecriture.getCodeDebitCredit().equals(CodeSystem.CS_DEBIT)
                    || ecriture.getCodeDebitCredit().equals(CodeSystem.CS_EXTOURNE_DEBIT)) {
                totalDebit.add(ecriture.getMontantBase());
            } else {
                totalCredit.add(ecriture.getMontantBase());
            }
        }

        totalCredit.negate();
        if (totalCredit.compareTo(totalDebit) != 0) {
            throw new Exception(((BSession) session).getLabel("COMPTABILISER_JOURNAL_WARN_TOT_DEBIT_CREDIT_DIFF"));
        }
    }

    /**
     * Mise à jour de l'entête une fois les écritures ajoutées.
     * 
     * @param transaction
     * @param entete
     * @param lastLibelleUsed
     * @param totalDebit
     * @param totalCredit
     * @param countDebit
     * @param countCredit
     * @param idContreEcritureDebit
     * @param idContreEcritureCredit
     * @throws Exception
     */
    public static void updateEnteteWithEcritures(BTransaction transaction, CGEnteteEcritureViewBean entete,
            String lastLibelleUsed, FWCurrency totalDebit, FWCurrency totalCredit, int countDebit, int countCredit,
            String idContreEcritureDebit, String idContreEcritureCredit) throws Exception {
        entete.setLibelle(lastLibelleUsed);

        entete.setTotalDoit(totalDebit.toString());
        entete.setNombreDoit("" + countDebit);

        if (countDebit == 1) {
            entete.setIdContrepartieDoit(idContreEcritureDebit);
        } else {
            entete.setIdContrepartieDoit("0");
        }

        entete.setTotalAvoir(totalCredit.toString());
        entete.setNombreAvoir("" + countCredit);

        if (countCredit == 1) {
            entete.setIdContrepartieAvoir(idContreEcritureCredit);
        } else {
            entete.setIdContrepartieAvoir("0");
        }

        entete.update(transaction);
    }

    /**
     * Mise à jour d'une ligne de la table des soldes.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param idCompte
     * @param idPeriodeComptable
     * @param idCentreCharge
     * @param addMode
     * @param updateOnlyProvisoire
     * @throws Exception
     */
    public static void updateSolde(BISession session, BTransaction transaction, CGJournal journal, String idCompte,
            String idPeriodeComptable, String idCentreCharge, boolean updateOnlyProvisoire) throws Exception {
        CGSoldeUpdater soldeUpdater = new CGSoldeUpdater(journal.getExerciceComptable().getIdMandat(),
                journal.getIdExerciceComptable(), idPeriodeComptable, idCompte, idCentreCharge, updateOnlyProvisoire);
        soldeUpdater.update((BSession) session, transaction);
    }

    /**
     * Mise à jour de la table des soldes pour l'écriture, solde provisoire et solde définitif (comptabilisé => aucune
     * contrôle de la ligne de solde existante).
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param ecriture
     * @throws Exception
     */
    public static void updateSoldesDefinitif(BISession session, BTransaction transaction, CGJournal journal,
            CGEcritureViewBean ecriture) throws Exception {
        CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                journal.getIdPeriodeComptable(), CGCompte.AUCUN_CENTRE_CHARGE, false);
        CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, CGCompte.AUCUN_CENTRE_CHARGE, false);

        if (!JadeStringUtil.isIntegerEmpty(ecriture.getIdCentreCharge())) {
            CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                    journal.getIdPeriodeComptable(), ecriture.getIdCentreCharge(), false);
            CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                    CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, ecriture.getIdCentreCharge(), false);
        }
    }

    /**
     * Mise à jour de la table des soldes pour l'écriture.
     * 
     * @param session
     * @param transaction
     * @param journal
     * @param ecriture
     * @throws Exception
     */
    public static void updateSoldesProvisoire(BISession session, BTransaction transaction, CGJournal journal,
            CGEcritureViewBean ecriture) throws Exception {
        CGGestionEcritureUtils.checkAndCreateSoldeForPeriode(session, transaction, ecriture,
                journal.getIdPeriodeComptable(), CGCompte.AUCUN_CENTRE_CHARGE);
        CGGestionEcritureUtils.checkAndCreateSoldeForPeriode(session, transaction, ecriture,
                CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, CGCompte.AUCUN_CENTRE_CHARGE);

        if (!JadeStringUtil.isIntegerEmpty(ecriture.getIdCentreCharge())) {
            CGGestionEcritureUtils.checkAndCreateSoldeForPeriode(session, transaction, ecriture,
                    journal.getIdPeriodeComptable(), ecriture.getIdCentreCharge());
            CGGestionEcritureUtils.checkAndCreateSoldeForPeriode(session, transaction, ecriture,
                    CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, ecriture.getIdCentreCharge());
        }

        CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                journal.getIdPeriodeComptable(), CGCompte.AUCUN_CENTRE_CHARGE, true);
        CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, CGCompte.AUCUN_CENTRE_CHARGE, true);

        if (!JadeStringUtil.isIntegerEmpty(ecriture.getIdCentreCharge())) {
            CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                    journal.getIdPeriodeComptable(), ecriture.getIdCentreCharge(), true);
            CGGestionEcritureUtils.updateSolde(session, transaction, journal, ecriture.getIdCompte(),
                    CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE, ecriture.getIdCentreCharge(), true);
        }
    }
}
