/**
 *
 */
package globaz.osiris.utils;

import globaz.aquila.api.ICOContentieux;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CACouvertureSection;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAEcheancePlanManager;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.db.contentieux.CAParametreEtape;
import globaz.osiris.db.contentieux.CAParametreEtapeManager;
import globaz.osiris.print.itext.CASommation;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvEcheancier;
import globaz.osiris.print.itext.list.CAIRappelPlanRecouv;
import globaz.osiris.translation.CACodeSystem;
import java.util.Iterator;
import java.util.List;

/**
 * Classe regroupeant des m�thodes utilis�es pour les sursis au paiement
 * 
 * @author SEL
 */
public class CASursisPaiement {

    /**
     * Annule le plan et pour chaque section couverte du plan :
     * <ul>
     * <li>Termine le blocage de la section pour le contentieux
     * <li>Ins�re une �tape de sommation pour cette section.
     * </ul>
     * 
     * @param session
     * @param transaction
     * @param idPlanRecouvrement
     * @param dateReference
     * @param sendSommation
     * @throws Exception
     */
    public static void annulerPlan(BProcess process, BTransaction transaction, CAPlanRecouvrement planRecouvrement,
            String dateReference, Boolean sendSommation) throws Exception {
        planRecouvrement.setIdEtat(CAPlanRecouvrement.CS_ANNULE);
        planRecouvrement.update(transaction);

        // pour chaque section couverte du plan
        List sectionsCouvertes = planRecouvrement.fetchSectionsCouvertes().getContainer();

        for (Iterator sectionIter = sectionsCouvertes.iterator(); sectionIter.hasNext();) {
            CASection section = ((CACouvertureSection) sectionIter.next()).getSection();

            if ((section != null)) {
                // d�terminer si on utilise l'ancien ou le nouveau contentieux
                CAApplication app = (CAApplication) GlobazSystem
                        .getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);

                if (app.getCAParametres().isContentieuxAquila()) {
                    CASursisPaiement.terminerBlocageSectionContentieuxAquila(transaction, section, dateReference);
                    if (sendSommation.booleanValue()) {
                        try {
                            CASursisPaiement.insererSommationContentieuxAquila(transaction, section, dateReference);
                        } catch (Exception e) {
                            process.getMemoryLog()
                                    .logMessage(e.getMessage(), FWMessage.INFORMATION, "CASursisPaiement");
                            process.getMemoryLog().logMessage(transaction.getErrors().toString(),
                                    FWMessage.INFORMATION, "CASursisPaiement");
                            transaction.clearErrorBuffer();
                        }
                    }
                } else {
                    CASursisPaiement.terminerBlocageSectionAncienContentieux(transaction, section);
                    if (sendSommation.booleanValue()) {
                        CASursisPaiement.insererSommationAncienContentieux(transaction, section, dateReference);
                    }
                }
            }
        }
    }

    /**
     * Pr�pare et retourne le document "Ech�ancier"
     * 
     * @param process
     * @param session
     * @param transaction
     * @param plan
     * @return
     * @throws FWIException
     * @throws Exception
     */
    public static CAILettrePlanRecouvEcheancier createEcheancier(BProcess process, BTransaction transaction,
            CAPlanRecouvrement plan) throws FWIException, Exception {
        // Instancie le document : �ch�ancier
        CAILettrePlanRecouvEcheancier documentE = new CAILettrePlanRecouvEcheancier(process);
        documentE.setSession(transaction.getSession());
        documentE.setPlanRecouvrement(plan);
        // Demander le traitement du document
        documentE.setEMailAddress(process.getEMailAddress());
        documentE.executeProcess();

        return documentE;
    }

    /**
     * Cr�ation d'un nouvel �v�nement contentieux.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param dateReference
     * @param parametreEtape
     * @throws Exception
     */
    private static void createEvenementContentieux(BTransaction transaction, CASection section, String dateReference,
            CAParametreEtape parametreEtape) throws Exception {
        // il n'y a pas d'�v�nement pour cette �tape, on va en cr�er un
        CAEvenementContentieux evenementContentieux = new CAEvenementContentieux();
        evenementContentieux.setSession(transaction.getSession());

        evenementContentieux.setDateDeclenchement(dateReference);
        evenementContentieux.setDateExecution(dateReference);
        evenementContentieux.setEstDeclenche(Boolean.TRUE);

        evenementContentieux.setEstIgnoree(Boolean.TRUE);

        // par d�faut: evenementContentieux.setEstExtourne(Boolean.FALSE);
        // par d�faut: evenementContentieux.setEstModifie(Boolean.FALSE);
        evenementContentieux.setIdParametreEtape(parametreEtape.getIdParametreEtape());

        evenementContentieux.setIdSection(section.getIdSection());

        evenementContentieux.add(transaction);
    }

    /**
     * Cr�e le document Rappel pour un sursis au paiement
     * 
     * @param process
     * @param session
     * @param idPlanRecouvrement
     * @param dateReference
     * @return
     * @throws Exception
     */
    public static CAIRappelPlanRecouv createRappel(BProcess process, BSession session, String idPlanRecouvrement,
            String dateReference) throws Exception {
        // Instancie le document du plan de recouvrement : D�cision
        CAIRappelPlanRecouv document = new CAIRappelPlanRecouv(process);
        document.setSession(session);
        // Demander le traitement du document
        document.setEMailAddress(process.getEMailAddress());
        document.setDateRef(dateReference);

        if (JadeStringUtil.isBlank(idPlanRecouvrement)) {
            throw new Exception(session.getLabel("OSIRIS_ERR_PLAN_ID_IS_NULL"));
        }
        document.setIdPlanRecouvrement(idPlanRecouvrement);
        document.executeProcess();

        if (document.getDocumentList().size() <= 0) {
            throw new Exception(document.getClass().getName() + "._executeProcess() : Error, document "
                    + document.getImporter().getDocumentTemplate() + " can not be created !");
        }

        return document;
    }

    /**
     * Cr�er le premi�re �v�nement de l'ancien contentieux, que l'�tape soit rappel ou sommation.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param dateReference
     * @throws Exception
     */
    public static void creerPremiereEtapeAncienContentieux(BTransaction transaction, CASection section,
            String dateReference) throws Exception {
        CAParametreEtapeManager parametreEtapeManager = CASursisPaiement.getEtapes(transaction, section);

        if (!parametreEtapeManager.isEmpty()) {
            CAParametreEtape parametreEtape = (CAParametreEtape) parametreEtapeManager.getFirstEntity();

            if (parametreEtape.getEvenementContentieux(section) == null) {
                CASursisPaiement.createEvenementContentieux(transaction, section, dateReference, parametreEtape);
            }
        }
    }

    /**
     * D�cale la date d'�ch�ance de la derni�re �tape du nombre de jours pass� en param�tre.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param nombreJours
     * @throws Exception
     */
    public static void decalerEcheance(BTransaction transaction, CASection section, int nombreJours) throws Exception {
        CAEvenementContentieux evenement = section.getLastEvenementContentieux();

        if (evenement != null) {
            evenement.setSession(transaction.getSession());

            JACalendarGregorian calendar = new JACalendarGregorian();
            evenement.setDateDeclenchement(calendar.addDays(section.getDateEcheance(), nombreJours));

            evenement.update(transaction);
        }
    }

    /**
     * Return les etapes de param�trages de l'ancien contentieux.
     * 
     * @param session
     * @param transaction
     * @param section
     * @return
     * @throws Exception
     */
    private static CAParametreEtapeManager getEtapes(BTransaction transaction, CASection section) throws Exception {
        // trouver l'id de la s�quence de l'ancien contentieux
        String idSequenceContentieux = "-1";

        if (!JadeStringUtil.isIntegerEmpty(section.getIdSequenceContentieux())) {
            idSequenceContentieux = section.getIdSequenceContentieux();
        } else {
            idSequenceContentieux = section.getTypeSection().getIdSequenceContentieux();
        }

        // charger les param�tres d'�tapes pour l'ancien contentieux
        CAParametreEtapeManager parametreEtapeManager = new CAParametreEtapeManager();

        parametreEtapeManager.setSession(transaction.getSession());
        parametreEtapeManager.setForIdSequenceContentieux(idSequenceContentieux);
        parametreEtapeManager.find(transaction);
        return parametreEtapeManager;
    }

    /**
     * Charge le plan si n�cessaire et le retourne.
     * 
     * @param session
     * @param transaction
     * @param idPlanRecouvrement
     * @return
     * @throws Exception
     */
    public static CAPlanRecouvrement getPlanRecouvrement(BTransaction transaction, String idPlanRecouvrement)
            throws Exception {
        CAPlanRecouvrement planRecouvrement = new CAPlanRecouvrement();
        planRecouvrement.setSession(transaction.getSession());
        planRecouvrement.setIdPlanRecouvrement(idPlanRecouvrement);
        planRecouvrement.retrieve(transaction);

        if (planRecouvrement.isNew() || planRecouvrement.hasErrors()) {
            throw new Exception(transaction.getSession().getLabel("PLAN_INTROUVABLE"));
        }

        return planRecouvrement;
    }

    /**
     * Ins�re une �tape de sommation dans l'ancien contentieux pour cette section.
     * <p>
     * Si une �tape de sommation existe d�j�, ne fait rien.
     * </p>
     * 
     * @param session
     * @param transaction
     * @param section
     * @param dateReference
     * @throws Exception
     */
    public static void insererSommationAncienContentieux(BTransaction transaction, CASection section,
            String dateReference) throws Exception {
        CAParametreEtapeManager parametreEtapeManager = CASursisPaiement.getEtapes(transaction, section);

        /*
         * les param�tres sont tri�s par num�ro de s�quence, on va it�rer jusqu'� trouver la sommation et cr�er les
         * �v�nements si n�cessaire
         */
        boolean evtCtx = true;
        boolean isSommationReached = false;

        for (int id = 0; id < parametreEtapeManager.size(); ++id) {
            CAParametreEtape parametreEtape = (CAParametreEtape) parametreEtapeManager.get(id);

            isSommationReached = CASommation.class.getName().equals(parametreEtape.getNomClasseImpl());

            // d�terminer s'il existe d�j� un �v�nement contentieux pour cette �tape
            if (evtCtx) {
                // note: ce test sera effectu� tant qu'il y a un �v�nement, d�s qu'il n'y en a plus, on skippera
                evtCtx = parametreEtape.getEvenementContentieux(section) != null;
            }

            if (!evtCtx) {
                CASursisPaiement.createEvenementContentieux(transaction, section, dateReference, parametreEtape);
            }

            if (isSommationReached) {
                // nous sommes all�s jusqu'� la sommation, on arr�te ici
                break;
            }
        }
    }

    /**
     * Ins�re une �tape de sommation dans le contentieux Aquila pour cette section.
     * <p>
     * Si une �tape de sommation existe d�j�, ne fait rien.
     * </p>
     * 
     * @param session
     * @param transaction
     * @param section
     * @param dateReference
     * @throws Exception
     */
    private static void insererSommationContentieuxAquila(BTransaction transaction, CASection section,
            String dateReference) throws Exception {
        try {
            // charger le contentieux s'il existe
            ICOContentieux helper = (ICOContentieux) transaction.getSession().getAPIFor(ICOContentieux.class);

            helper.creerSommationSursisPaiement(transaction.getSession(), transaction, section.getIdSection(),
                    dateReference);
        } catch (Exception e) {
            if (e.getMessage() != null) {
                throw new Exception(e);
            } else {
                throw new Exception("Error in globaz.osiris.utils.CASursisPaiement.insererSommationContentieuxAquila()");
            }
        }
    }

    /**
     * Termine le blocage de la section pour l'ancien contentieux.
     * <ul>
     * <li>On met une date de fin de suspension � la date du jour dans la section</li>
     * <li>On met faux pour le booleen d'indication de blocage dans la section</li>
     * <li>On met vide pour le type de blocage dans la section</li>
     * <li>On met 0 pour l'id du plan dans la section</li>
     * </ul>
     * 
     * @param transaction
     * @param section
     *            la section dont on veut modifier le blocage
     * @throws Exception
     */
    private static void terminerBlocageSectionAncienContentieux(BTransaction transaction, CASection section)
            throws Exception {
        if (CACodeSystem.CS_PLAN_RECOUVREMENT.equals(section.getIdMotifContentieuxSuspendu())) {
            if (section.getContentieuxEstSuspendu().booleanValue()) {
                section.setContentieuxEstSuspendu(Boolean.FALSE);
                section.setIdMotifContentieuxSuspendu("");
                section.setDateSuspendu("");
            }
            section.setIdPlanRecouvrement("0"); // Permet de refaire un plan sur une section dont le plan a �t� annul�
            section.update(transaction);
        }
    }

    /**
     * Termine le blocage de la section.
     * 
     * @param transaction
     * @param section
     * @param dateReference
     */
    public static void terminerBlocageSectionContentieux(BTransaction transaction, CASection section) throws Exception {
        if (((CAApplication) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)).getCAParametres()
                .isContentieuxAquila()) {
            CASursisPaiement
                    .terminerBlocageSectionContentieuxAquila(transaction, section, JACalendar.todayJJsMMsAAAA());
        } else {
            CASursisPaiement.terminerBlocageSectionAncienContentieux(transaction, section);
        }
    }

    /**
     * Termine le blocage de la section pour le contentieux Aquila.
     * <ul>
     * <li>On met une date de fin pour le motif dont le type est li� au plan de paiement.</li>
     * <li>S'il n'y a pas d'autres blocages pour cette section, on met faux pour le booleen d'indication de blocage dans
     * le section</li>
     * <li>On met 0 pour l'id du plan dans la section</li>
     * </ul>
     * 
     * @param session
     * @param transaction
     * @param section
     *            la section dont on veut modifier le blocage
     * @param dateReference
     * @throws Exception
     */
    private static void terminerBlocageSectionContentieuxAquila(BTransaction transaction, CASection section,
            String dateReference) throws Exception {
        try {
            if (section.getContentieuxEstSuspendu().booleanValue()) {
                CAMotifContentieuxManager motifContentieuxManager = new CAMotifContentieuxManager();

                motifContentieuxManager.setForIdSection(section.getIdSection());
                // la date de fin � la date du jour bloque encore durant le jour.
                motifContentieuxManager.setFromDateBetweenDebutFin(dateReference);
                motifContentieuxManager.setSession(transaction.getSession());
                motifContentieuxManager.find(transaction);

                for (int id = 0; id < motifContentieuxManager.size(); ++id) {
                    CAMotifContentieux motifContentieux = (CAMotifContentieux) motifContentieuxManager.get(id);

                    if (CACodeSystem.CS_PLAN_RECOUVREMENT.equals(motifContentieux.getIdMotifBlocage())
                            || CACodeSystem.CS_SURSIS_PMT_PART_PENALE.equals(motifContentieux.getIdMotifBlocage())) {
                        // mettre une date de fin au bloquage li� au recouvrement
                        motifContentieux.setDateFin(dateReference);
                        motifContentieux.update(transaction);
                    }
                }
            }
            // SEL : Il ne faut pas mettre � FALSE ce champs. Risque d'erreur � la suppression des motif B4323
            // section.setContentieuxEstSuspendu(Boolean.FALSE);

            if (!JadeStringUtil.isBlankOrZero(section.getIdPlanRecouvrement())) {
                section.setIdPlanRecouvrement("0"); // Permet de refaire un plan sur une section dont le plan a �t�
                                                    // annul�
                section.update(transaction);
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                throw new Exception(e);
            } else {
                throw new Exception(
                        "Error in globaz.osiris.utils.CASursisPaiement.terminerBlocageSectionContentieuxAquila()");
            }
        }
    }

    /**
     * Met � jour la date de rappel des �ch�ances dont le delai entre la date d'�ch�ance <br />
     * et la date de rappel (date sur document) est d�pass�.
     * 
     * @param session
     * @param transaction
     * @param idPlanRecouvrement
     * @param dateReference
     * @param dateLimite
     * @throws Exception
     */
    public static void updateEcheancesEchues(BTransaction transaction, String idPlanRecouvrement, String dateReference,
            JADate dateLimite) throws Exception {
        CAEcheancePlanManager echeancePlanManager = new CAEcheancePlanManager();
        // Recherche les �cheances pour le plan de recouvrement dont l'�ch�ance d�passe le d�lai
        echeancePlanManager.setSession(transaction.getSession());
        echeancePlanManager.setForIdPlanRecouvrement(idPlanRecouvrement);
        echeancePlanManager.setForDateEffectiveIsNull(); // Ech�ance sans paiement
        echeancePlanManager.setToDateExigibilite(dateLimite.toStr("."));
        echeancePlanManager.find(transaction);

        // Parcours des ech�ances
        for (int i = 0; i < echeancePlanManager.size(); i++) {
            CAEcheancePlan echeance = (CAEcheancePlan) echeancePlanManager.getEntity(i);
            echeance.setDateRappel(dateReference);
            echeance.update(transaction);
        }
    }
}
