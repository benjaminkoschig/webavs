package globaz.osiris.process.journal;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPeriodeComptableManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.interets.CAGenreInteret;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoireManager;
import globaz.osiris.external.IntJournalCG;
import java.util.ArrayList;

/**
 * Class utilitaire, méthode partagée par CAComptabiliserJournal et CAAnnulerJournal.
 * 
 * @author dda
 */
public class CAUtilsJournal {
    public static final String INTERFACE_COMPTABILITE_GENERALE = "osiris.application.interfaceComptabiliteGenerale";
    public static final String LABEL_5013 = "5013";

    private static final String LABEL_5019 = "5019";

    private CAApplication currentApplication;

    /**
     * Renvoie l'instance de l'application courrante attachée à la session enregistrée dans le système.
     * 
     * @param session
     * @return CAApplication : l'application correspondant à l'id de la session.
     */
    public CAApplication getCurrentApplication(BSession session) {
        if (currentApplication == null) {
            currentApplication = CAApplication.getApplicationOsiris();
        }
        return currentApplication;
    }

    /**
     * Retourne le journal de la compta général lié au journal d'osiris
     * 
     * @param session
     * @param transaction
     * @param idJournalCg
     * @return IntJournalCg : Le journal de la comptabilité générale. Null si le chargement n'a pas fonctionné.
     */
    public IntJournalCG getJournalCG(BSession session, BTransaction transaction, String idJournalCg) {
        IntJournalCG journalCG = null;

        if (!JadeStringUtil.isIntegerEmpty(idJournalCg)) {
            try {
                // Récupérer le journal selon l'interface
                CAApplication currentApplication = CAApplication.getApplicationOsiris();
                journalCG = (IntJournalCG) GlobazServer.getCurrentSystem()
                        .getApplication(currentApplication.getCAParametres().getApplicationExterne())
                        .getImplementationFor(session, IntJournalCG.class);
                journalCG.setIdJournal(idJournalCg);
                journalCG.retrieve(session, transaction, true);
                if (journalCG.isOnError()) {
                    journalCG = null;
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                journalCG = null;
                transaction.addErrors(session.getLabel("7004") + e.getMessage());
            }
        }

        // Retourner le cache
        return journalCG;
    }

    /**
     * Un journal qui contient des intérêts tardifs déjà comptabilisés ne peut être annulé.
     * 
     * @param session
     * @param transaction
     * @param idJournal
     * @return
     */
    public boolean hasInteretsComptabilises(BSession session, BTransaction transaction, String idJournal) {
        try {
            CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
            manager.setSession(session);
            manager.setForIdJournalCalcul(idJournal);

            ArrayList forIdGenreInteretIn = new ArrayList();
            forIdGenreInteretIn.add(CAGenreInteret.CS_TYPE_TARDIF);
            forIdGenreInteretIn.add(CAGenreInteret.CS_TYPE_COTISATIONS_PERSONNELLES);
            manager.setForIdGenreInteretIn(forIdGenreInteretIn);

            manager.setForMotifCalcul(CAInteretMoratoire.CS_SOUMIS);

            manager.find(transaction);

            for (int i = 0; i < manager.getSize(); i++) {
                if (CAInteretMoratoire.STATUS_COMPTABILISE.equals(((CAInteretMoratoire) manager.get(i)).getStatus())) {
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    /**
     * L'application est-elle de type AVS ? <br>
     * Exemple AIJ ne fonctionne pas comme la comptabilite AVS.
     * 
     * @param session
     * @return True : Si la comptabilité est en mode AVS.
     */
    public boolean isComptabiliteAvs(BSession session) {
        return getCurrentApplication(session).getCAParametres().isComptabiliteAvs();
    }

    /**
     * Vérifier si l'interface CG est active.
     * 
     * @param session
     * @return True : Si l'interface comptabilité générale est active.
     */
    public boolean isInterfaceCgActive(BSession session) {
        String sInterfaceCG = getCurrentApplication(session)
                .getProperty(CAUtilsJournal.INTERFACE_COMPTABILITE_GENERALE);
        if (sInterfaceCG != null) {
            return Boolean.valueOf(sInterfaceCG).booleanValue();
        } else {
            return false;
        }
    }

    /**
     * La période comptable AVS en comptabilité générale pour la date de valeur du journal est-elle ouverte ?
     * 
     * @param session
     * @param transaction
     * @param dateValeurCG
     * @return
     */
    public boolean isPeriodeComptableOuverte(BSession session, BTransaction transaction, String dateValeurCG) {
        return this.isPeriodeComptableOuverte(session, transaction, dateValeurCG, CGMandat.MANDAT_AVS_DEFAULT_NUMBER);
    }

    /**
     * La période comptable en comptabilité générale pour la date de valeur du journal est-elle ouverte ?
     * 
     * @param session
     * @param transaction
     * @param dateValeurCG
     * @param forIdMandat
     * @return
     */
    public boolean isPeriodeComptableOuverte(BSession session, BTransaction transaction, String dateValeurCG,
            String forIdMandat) {
        try {
            CGExerciceComptableManager exerciceManager = new CGExerciceComptableManager();
            exerciceManager.setSession(session);

            exerciceManager.setForIdMandat(forIdMandat);
            exerciceManager.setForExerciceOuvert(new Boolean(true));
            exerciceManager.setBetweenDateDebutDateFin(dateValeurCG);

            exerciceManager.find(transaction);

            if (exerciceManager.hasErrors() || exerciceManager.isEmpty()) {
                transaction.addErrors(session.getLabel("EXERCICE_COMPTABLE_ERREUR"));
                return false;
            }

            CGExerciceComptable exercice = (CGExerciceComptable) exerciceManager.getFirstEntity();

            CGPeriodeComptableManager manager = new CGPeriodeComptableManager();
            manager.setSession(session);

            manager.setForDateInPeriode(dateValeurCG);
            manager.setForIdExerciceComptable(exercice.getIdExerciceComptable());

            manager.find(transaction);

            if (manager.hasErrors() || manager.isEmpty()) {
                transaction.addErrors(session.getLabel("PERIODE_INEXISTANTE"));
                return false;
            }

            CGPeriodeComptable periode = (CGPeriodeComptable) manager.getFirstEntity();

            if (periode.isEstCloture().booleanValue()) {
                transaction.addErrors(session.getLabel("PERIODE_CLOTUREE"));
            }

            return !periode.isEstCloture().booleanValue();
        } catch (Exception e) {
            transaction.addErrors(e.getMessage());
        }

        return false;
    }

    /**
     * Mise à jour des comptes courants. <br/>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param session
     * @param transaction
     * @param idCompteCourant
     * @param value
     * @param add
     * @return
     * @throws Exception
     */
    private boolean updateCompteCourant(BSession session, BTransaction transaction, String idCompteCourant,
            String value, boolean add) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(idCompteCourant)) {
            FWCurrency montant = new FWCurrency(value);

            CACompteCourant compteCourant = new CACompteCourant();
            compteCourant.setSession(session);
            compteCourant.setIdCompteCourant(idCompteCourant);

            compteCourant.retrieve(transaction);

            if (compteCourant.isNew()) {
                transaction.addErrors(session.getLabel(CAUtilsJournal.LABEL_5019));
                return false;
            }

            FWCurrency solde = new FWCurrency(compteCourant.getSolde());

            if (add) {
                solde.add(montant);
            } else {
                solde.sub(montant);
            }

            compteCourant.setSolde(solde.toString());

            compteCourant.update(transaction);

            if (transaction.hasErrors()) {
                transaction.addErrors(session.getLabel(CAUtilsJournal.LABEL_5019));
                transaction.rollback();
                return false;
            } else {
                transaction.commit();
            }
        }
        return true;
    }

    /**
     * Mise à jour des comptes courants pendant l'annulation des opérations.
     * 
     * @param session
     * @param transaction
     * @param idCompteCourant
     * @param value
     * @return
     * @throws Exception
     */
    public boolean updateCompteCourantForAnnulation(BSession session, BTransaction transaction, String idCompteCourant,
            String value) throws Exception {
        return updateCompteCourant(session, transaction, idCompteCourant, value, false);
    }

    /**
     * Mise à jour des comptes courants lors de la comptabilisation des opérations.
     * 
     * @param session
     * @param transaction
     * @param idCompteCourant
     * @param value
     * @return
     * @throws Exception
     */
    public boolean updateCompteCourantForComptabilisation(BSession session, BTransaction transaction,
            String idCompteCourant, String value) throws Exception {
        return updateCompteCourant(session, transaction, idCompteCourant, value, true);
    }
}
