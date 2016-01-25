package globaz.osiris.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAParametreEtape;
import globaz.osiris.db.contentieux.CAParametreEtapeManager;
import globaz.osiris.print.itext.CASommation;

public class CAContentieux {

    /**
     * Création d'un nouvel événement contentieux.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param dateReference
     * @param parametreEtape
     * @throws Exception
     */
    private static void createEvenementContentieux(BSession session, BTransaction transaction, CASection section,
            String dateReference, CAParametreEtape parametreEtape) throws Exception {
        // il n'y a pas d'événement pour cette étape, on va en créer un
        CAEvenementContentieux evenementContentieux = new CAEvenementContentieux();
        evenementContentieux.setSession(session);

        evenementContentieux.setDateDeclenchement(dateReference);
        evenementContentieux.setEstDeclenche(Boolean.FALSE);
        evenementContentieux.setEstIgnoree(Boolean.FALSE);
        evenementContentieux.setEstExtourne(Boolean.FALSE);
        evenementContentieux.setEstModifie(Boolean.TRUE);
        evenementContentieux.setIdParametreEtape(parametreEtape.getIdParametreEtape());
        evenementContentieux.setIdSection(section.getIdSection());

        evenementContentieux.add(transaction);
    }

    /**
     * Créer le première événement de l'ancien contentieux, que l'étape soit rappel ou sommation.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param dateReference
     * @throws Exception
     */
    public static void creerPremiereEtapeAncienContentieux(BSession session, BTransaction transaction,
            CASection section, String dateReference) throws Exception {
        CAParametreEtapeManager parametreEtapeManager = CAContentieux.getEtapes(session, transaction, section);

        if (!parametreEtapeManager.isEmpty()) {
            CAParametreEtape parametreEtape = (CAParametreEtape) parametreEtapeManager.getFirstEntity();

            if (parametreEtape.getEvenementContentieux(section) == null) {
                String dateEtape = new String(dateReference);
                JACalendarGregorian calendar = new JACalendarGregorian();
                if (calendar.compare(dateReference,
                        calendar.addDays(section.getDateEcheance(), new Integer(parametreEtape.getDelai()).intValue())) == JACalendar.COMPARE_FIRSTUPPER) {
                    CAContentieux.createEvenementContentieux(session, transaction, section, dateEtape, parametreEtape);
                }
            }
        }
    }

    /**
     * Décale la date d'échéance de la dernière étape du nombre de jours passé en paramètre.
     * 
     * @param session
     * @param transaction
     * @param section
     * @param nombreJours
     * @throws Exception
     */
    public static void decalerEcheance(BSession session, BTransaction transaction, CASection section, int nombreJours)
            throws Exception {
        CAEvenementContentieux evenement = new CAEvenementContentieux();
        JACalendarGregorian calendar = new JACalendarGregorian();
        if (!JadeStringUtil.isBlank(section.getFirstIdEvContentieuxNonExecute())) {
            evenement.setSession(session);
            evenement.setIdEvenementContentieux(section.getFirstIdEvContentieuxNonExecute());
            evenement.retrieve(transaction);
            if (!evenement.isNew() && !transaction.hasErrors()) {
                if (calendar.compare(calendar.addDays(section.getDateEcheance(), nombreJours),
                        evenement.getDateDeclenchement()) == JACalendar.COMPARE_FIRSTUPPER) {
                    evenement.setDateDeclenchement(calendar.addDays(section.getDateEcheance(), nombreJours));
                    evenement.update(transaction);
                }
            }
        }
    }

    /**
     * Return les etapes de paramétrages de l'ancien contentieux.
     * 
     * @param session
     * @param transaction
     * @param section
     * @return
     * @throws Exception
     */
    private static CAParametreEtapeManager getEtapes(BSession session, BTransaction transaction, CASection section)
            throws Exception {
        // trouver l'id de la séquence de l'ancien contentieux
        String idSequenceContentieux = "-1";

        if (!JadeStringUtil.isIntegerEmpty(section.getIdSequenceContentieux())) {
            idSequenceContentieux = section.getIdSequenceContentieux();
        } else {
            idSequenceContentieux = section.getTypeSection().getIdSequenceContentieux();
        }

        // charger les paramètres d'étapes pour l'ancien contentieux
        CAParametreEtapeManager parametreEtapeManager = new CAParametreEtapeManager();

        parametreEtapeManager.setSession(session);
        parametreEtapeManager.setForIdSequenceContentieux(idSequenceContentieux);
        parametreEtapeManager.find(transaction);
        return parametreEtapeManager;
    }

    /**
     * Insére une étape de sommation dans l'ancien contentieux pour cette section.
     * <p>
     * Si une étape de sommation existe déjà, ne fait rien.
     * </p>
     * 
     * @param session
     * @param transaction
     * @param section
     * @param dateReference
     * @throws Exception
     */
    public static void insererSommationAncienContentieux(BSession session, BTransaction transaction, CASection section,
            String dateReference) throws Exception {
        CAParametreEtapeManager parametreEtapeManager = CAContentieux.getEtapes(session, transaction, section);

        /*
         * les paramètres sont triés par numéro de séquence, on va itérer jusqu'à trouver la sommation et créer les
         * événements si nécessaire
         */
        boolean evtCtx = true;
        boolean isSommationReached = false;

        for (int id = 0; id < parametreEtapeManager.size(); ++id) {
            CAParametreEtape parametreEtape = (CAParametreEtape) parametreEtapeManager.get(id);

            isSommationReached = CASommation.class.getName().equals(parametreEtape.getNomClasseImpl());

            // déterminer s'il existe déjà un événement contentieux pour cette
            // étape
            if (evtCtx) {
                // note: ce test sera effectué tant qu'il y a un événement, dès
                // qu'il n'y en a plus, on skippera
                evtCtx = parametreEtape.getEvenementContentieux(section) != null;
            }

            if (!evtCtx) {

                JACalendarGregorian calendar = new JACalendarGregorian();
                if (section.getLastEvenementContentieux() != null) {
                    if (calendar.compare(dateReference, calendar.addDays(section.getLastEvenementContentieux()
                            .getDateExecution(), new Integer(parametreEtape.getDelai()).intValue())) == JACalendar.COMPARE_FIRSTUPPER) {
                        CAContentieux.createEvenementContentieux(session, transaction, section, dateReference,
                                parametreEtape);
                    }
                }
            }

            if (isSommationReached) {
                // nous sommes allés jusqu'à la sommation, on arrête ici
                break;
            }
        }
    }
}
