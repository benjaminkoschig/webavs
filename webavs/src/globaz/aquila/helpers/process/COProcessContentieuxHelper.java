/*
 * Créé le 16 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.helpers.process;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.db.access.batch.COSequenceManager;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.batch.COTransitionManager;
import globaz.aquila.process.COProcessContentieux;
import globaz.aquila.vb.process.COProcessContentieuxViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CARoleManager;
import globaz.osiris.db.comptes.CATypeSectionManager;
import java.util.HashSet;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COProcessContentieuxHelper extends FWHelper {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COProcessContentieuxHelper.
     */
    public COProcessContentieuxHelper() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWHelper#_retrieve(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        COProcessContentieuxViewBean pcViewBean = (COProcessContentieuxViewBean) viewBean;

        // charger les données qui seront affichées dans l'écran
        // -----------------------------------------------
        // -- les types de sections
        CATypeSectionManager manTypeSection = new CATypeSectionManager();

        manTypeSection.setISession(session);
        manTypeSection.find();

        pcViewBean.setTypesSections(manTypeSection.getContainer());

        // -- les roles
        CARoleManager manRole = new CARoleManager();

        manRole.setISession(viewBean.getISession());
        manRole.find();

        pcViewBean.setRoles(manRole.getContainer());

        // -- rechercher les étapes qui suivent immédiatement la création
        COEtapeManager etapeManager = new COEtapeManager();

        etapeManager.setISession(session);
        etapeManager.setForLibEtape(ICOEtape.CS_AUCUNE);
        etapeManager.find();

        COTransitionManager transitionManager = new COTransitionManager();
        Set idsEtapesDependentCreation = new HashSet();

        transitionManager.setISession(session);

        for (int id = 0; id < etapeManager.size(); ++id) {
            COEtape etapeAucune = (COEtape) etapeManager.get(id);
            transitionManager.setForAuto(Boolean.TRUE);
            transitionManager.setForIdEtape(etapeAucune.getIdEtape());
            transitionManager.find();

            for (int id1 = 0; id1 < transitionManager.getSize(); ++id1) {
                COTransition transition = (COTransition) transitionManager.get(id1);

                idsEtapesDependentCreation.add(transition.getIdEtapeSuivante());
            }
        }

        // -- les sequences
        COSequenceManager sequences = new COSequenceManager();

        sequences.setISession(session);
        sequences.find();

        pcViewBean.setSequences(sequences.getContainer());

        // -- les etapes pour chaque sequence, on ne prend que celles pour
        // lesquelles il existe une transition auto.
        etapeManager.setForLibEtape(""); // on annule le critère précédent
        etapeManager.setForExisteTransitionAutoVers("true");
        etapeManager.addForAlwaysLibEtapeIn(ICOEtape.CS_CONTENTIEUX_CREE);
        etapeManager.addForNeverLibEtapeIn(ICOEtape.CS_AUCUNE);
        etapeManager.setOrderByLibEtapeCSOrder(Boolean.TRUE.toString());

        for (int idSequence = 0; idSequence < sequences.size(); ++idSequence) {
            // on stocke les etapes dans une map du viewbean
            COSequence sequence = (COSequence) sequences.get(idSequence);

            etapeManager.setForIdSequence(sequence.getIdSequence());
            etapeManager.find();

            // ajouter les étapes dans la liste du viewBean
            for (int id = 0; id < etapeManager.size(); ++id) {
                COEtape etape = (COEtape) etapeManager.get(id);

                pcViewBean.addEtape(etape, idsEtapesDependentCreation.contains(etape.getIdEtape()));
            }
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#_start(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        try {
            COProcessContentieuxViewBean processContentieuxViewBean = (COProcessContentieuxViewBean) viewBean;
            COProcessContentieux processContentieux = new COProcessContentieux();

            processContentieux.setImprimerJournalContentieuxExcelml(processContentieuxViewBean
                    .getImprimerJournalContentieuxExcelml());
            processContentieux.setDateSurDocument(processContentieuxViewBean.getDateSurDocument());
            processContentieux.setDateReference(processContentieuxViewBean.getDateReference());
            processContentieux.setDateDelaiPaiement(processContentieuxViewBean.getDateDelaiPaiement());
            processContentieux.setEMailAddress(processContentieuxViewBean.getEMailAddress());
            processContentieux.setImprimerDocument(processContentieuxViewBean.getImprimerDocument());
            processContentieux
                    .setImprimerListeDeclenchement(processContentieuxViewBean.getImprimerListeDeclenchement());
            processContentieux.setImprimerListePourOP(processContentieuxViewBean.getImprimerListePourOP());
            processContentieux.setISession(processContentieuxViewBean.getISession());
            processContentieux.setLibelleJournal(processContentieuxViewBean.getLibelleJournal());
            processContentieux.setPrevisionnel(processContentieuxViewBean.getModePrevisionnel());
            processContentieux.setRoles(processContentieuxViewBean.getIdRoles());
            processContentieux.setSelectionTriListeCA(processContentieuxViewBean.getSelectionTriListeCA());
            processContentieux.setSelectionTriListeSection(processContentieuxViewBean.getSelectionTriListeSection());
            processContentieux.setTypesSections(processContentieuxViewBean.getIdTypesSections());
            processContentieux.setSelections(processContentieuxViewBean.getSelection());
            processContentieux.setFromNoAffilie(processContentieuxViewBean.getFromNoAffilie());
            processContentieux.setBeforeNoAffilie(processContentieuxViewBean.getBeforeNoAffilie());
            processContentieux.setExecuteTraitementSpecifique(processContentieuxViewBean
                    .getExecuteTraitementSpecifique());
            processContentieux.setUserIdCollaborateur(processContentieuxViewBean.getUserIdCollaborateur());
            processContentieux.setForIdCategorie(processContentieuxViewBean.getForIdCategorie());
            processContentieux.setForIdGenreCompte(processContentieuxViewBean.getForIdGenreCompte());
            processContentieux.setForIdSequence(processContentieuxViewBean.getForIdSequence());

            processContentieux.setSendCompletionMail(true);
            processContentieux.setSendMailOnError(true);
            processContentieux.setControleTransaction(true);

            BProcessLauncher.start(processContentieux);
        } catch (Exception e) {
            ((BSession) session).addError(e.getMessage());
        }
    }
}
