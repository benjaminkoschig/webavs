package globaz.libra.interfaces;

import globaz.globall.db.BTransaction;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.journalisation.access.JOGroupeJournal;
import globaz.journalisation.implementation.adapter.journalisation.JOCreationJournalisationHandler;
import globaz.journalisation.implementation.service.JOServiceLocator;
import globaz.libra.db.dossiers.LIDossiers;
import globaz.libra.db.utils.LIUtilsEntity;
import ch.globaz.libra.api.ILIEcheancesInterfaces;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * Fait :
 * 
 * [1] > Cr�ation d'�ch�ance simple li�e � un tiers et au dossier si souhait� [2] > Cr�ation d'�ch�ance simple li�e � un
 * tiers et au dossier si souhait� avec cr�ation dossier si inexistant
 * 
 * Attente besoin pour autres interfaces
 */
public class LIEcheancesInterfaces implements ILIEcheancesInterfaces {

    // ~Constructor------------------------------------------------------------------------------------------------------

    public LIEcheancesInterfaces() {
        super();
    }

    // ~Methods----------------------------------------------------------------------------------------------------------

    private JOCreationJournalisationHandler addEcheance(BTransaction transaction, String dateRappel, String idExterne,
            String libelle, boolean isDossier, String csTypeEchance) throws Exception, LibraException,
            JadeApplicationServiceNotAvailableException {

        LIDossiers dossier = findDossierAndOpenIfClosed(transaction, idExterne);

        JOServiceLocator serviceLocator = JOServiceLocator.getInstance();

        // cr�ation groupeJournal
        JOGroupeJournal groupeJournal = serviceLocator.getGroupeJournalService().create(transaction, dateRappel);

        // Cr�ation de l'ensemble des entit�s :
        JOCreationJournalisationHandler handler = serviceLocator.getJournalisationService()
                .getCreationJournalisationHandler(transaction, groupeJournal);

        // D�fini les valeurs pour la journalisation
        handler.setJournalisationValues(JOConstantes.CS_JO_RAPPEL, transaction.getSession().getUserId(), libelle);

        // cr�ation complementJournal fmt syst�me
        handler.registerComplementJournal(JOConstantes.GR_CS_JO_FMT_ID, csTypeEchance);

        if (isDossier) {
            // cr�ation ReferenceProvenance "dossier"
            handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_DOSSIER, dossier.getIdDossier());
        }

        // cr�ation ReferenceProvenance "tiers"
        handler.registerReferenceProvenance(ILIConstantesExternes.REF_PRO_TIERS, dossier.getIdTiers());

        // Lance les processus
        handler.processJournalisation();
        return handler;
    }

    private void createDossierIfNotExite(BTransaction transaction, String idExterne, String idTiers, String csDomaine)
            throws Exception, LibraException, JadeApplicationServiceNotAvailableException {
        LIDossiers dossier = findDossierAndOpenIfClosed(transaction, idExterne);

        // Si dossier inexistant, le cr�er
        if (dossier.isNew()) {
            LibraServiceLocator.getDossierService().createDossier(idTiers, csDomaine, idExterne);
        }
    }

    public String createManuelWithTestDossier(BTransaction transaction, String dateRappel, String idExterne,
            String libelle, String remarque, String idTiers, String csDomaine, boolean isDossier)
            throws JadeApplicationServiceNotAvailableException, LibraException, Exception {

        createDossierIfNotExite(transaction, idExterne, idTiers, csDomaine);

        JOCreationJournalisationHandler handler = addEcheance(transaction, dateRappel, idExterne, libelle, isDossier,
                JOConstantes.CS_JO_FMT_MANUELLE);

        // ajout de la remarque
        LIUtilsEntity utils = new LIUtilsEntity();
        utils.setIdJournalisation(handler.getIdJournalisation());
        utils.setRemarque(remarque);
        utils.add(transaction);

        return csDomaine;
    }

    /**
     * [1] Cr�ation d'�ch�ance simple.
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait�
     * 
     * ==> Cette m�thode n�cessite qu'un dossier existe d�j� dans LIBRA
     * 
     */
    @Override
    public void createRappel(BTransaction transaction, String dateRappel, String idExterne, String libelle,
            boolean isDossier) {
        try {
            LIDossiers dossier = findDossierAndOpenIfClosed(transaction, idExterne);
            // Si dossier inexistant, erreur
            if (dossier.isNew()) {
                throw new Exception("Dossier inexistant dans Web@Journalisations");
            }
            addEcheance(transaction, dateRappel, idExterne, libelle, isDossier, JOConstantes.CS_JO_AVS_FMT_RAPPEL);
        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }
    }

    /**
     * [2] Cr�ation d'�ch�ance simple.
     * 
     * -> Li� au tiers de toute fa�on -> boolean permettant de lier au dossier si souhait�
     * 
     * ==> Cette m�thode permet de tester s'il existe un dossier ou non et de le cr�er s'il n'existe pas
     * 
     */
    @Override
    public void createRappelWithTestDossier(BTransaction transaction, String dateRappel, String idExterne,
            String libelle, String idTiers, String csDomaine, boolean isDossier) {

        try {
            createDossierIfNotExite(transaction, idExterne, idTiers, csDomaine);

            addEcheance(transaction, dateRappel, idExterne, libelle, isDossier, JOConstantes.CS_JO_RAPPEL);

        } catch (Exception e) {
            transaction.getSession().addError(e.toString());
            transaction.addErrors(e.toString());
        }

    }

    private LIDossiers findDossierAndOpenIfClosed(BTransaction transaction, String idExterne) throws Exception,
            LibraException, JadeApplicationServiceNotAvailableException {
        LIDossiers dossier = new LIDossiers();
        dossier.setSession(transaction.getSession());
        dossier.setAlternateKey(LIDossiers.ALTERNATE_KEY_ID_EXTERNE);
        dossier.setIdExterne(idExterne);
        dossier.retrieve();
        if (!dossier.isNew()) {
            // Si dossier clos, r�ouvrir
            if (dossier.getCsEtat().equals(ILIConstantesExternes.CS_ETAT_CLOS)) {
                LibraServiceLocator.getDossierService().reactivationDossier(idExterne);
            }
        }
        return dossier;
    }

}
