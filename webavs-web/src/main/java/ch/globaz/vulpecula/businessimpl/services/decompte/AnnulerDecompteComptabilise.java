package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import java.util.Arrays;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.external.models.pyxis.Role;

public class AnnulerDecompteComptabilise {
    private JournalSimpleModel journalSimpleModel;
    private CAJournal journal;

    private List<Decompte> decomptes;

    public AnnulerDecompteComptabilise(TaxationOffice taxationOffice) {
        this(Arrays.asList(taxationOffice.getDecompte()));
    }

    public AnnulerDecompteComptabilise(List<Decompte> decomptes) {
        this.decomptes = decomptes;
    }

    public boolean annulerSection(String libelleJournal) {
        try {

            BSession session = BSessionUtil.getSessionFromThreadContext();
            createAndFindJournal(libelleJournal);

            for (Decompte decompte : decomptes) {

                CACompteAnnexe compteAnnexe = retrieveCompteAnnexe(session, decompte);

                if (JadeStringUtil.isBlank(compteAnnexe.getIdCompteAnnexe())) {
                    continue;
                }
                CASectionManager sectionManager = new CASectionManager();
                sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
                sectionManager.setForIdExterne(decompte.getNumeroSection());
                sectionManager.setForIdTypeSection(decompte.getTypeSection().getValue());
                sectionManager.setSession(session);
                sectionManager.find();

                if (session.hasErrors()) {
                    throw new Exception(session.getErrors().toString());
                }

                for (Object o : sectionManager) {
                    CASection section = (CASection) o;
                    section.extournerEcritures(session.getCurrentThreadTransaction(), journal, "");
                }
            }
            CABusinessServiceLocator.getJournalService().comptabilise(journalSimpleModel);
            return true;
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    public String annulerSectionTO(String libelleJournal) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        BTransaction transaction = session.getCurrentThreadTransaction();

        try {
            if (decomptes.size() != 1) {
                throw new Exception("AnnulerDecompteComptabilise : Decomptes a une taille différente de 1 !");
            }

            createAndFindJournal(libelleJournal);
            Decompte decompte = decomptes.get(0);
            CACompteAnnexe compteAnnexe = retrieveCompteAnnexe(session, decompte);

            if (JadeStringUtil.isBlank(compteAnnexe.getIdCompteAnnexe())) {
                throw new Exception("AnnulerDecompteComptabilise : Compte annexe pas trouvé pour le décomtpe id = "
                        + decompte.getId() + " !");
            }

            CASection section = retrieveSection(session, decompte, compteAnnexe);

            if (session.hasErrors()) {
                throw new Exception("AnnulerDecompteComptabilise : " + session.getErrors().toString());
            }

            // Récupérer les opérations
            CAEcritureManager mgr = new CAEcritureManager();
            mgr.setSession(session);
            mgr.setForIdSection(section.getIdSection());
            mgr.setForIdJournal(section.getIdJournal());
            mgr.find();

            // Extourne les opérations
            for (int i = 0; i < mgr.size(); i++) {
                // Récupérer l'opération
                CAOperation op = (CAOperation) mgr.getEntity(i);
                // Si l'opération est active et extournable
                if (op.getEstActive()) {
                    op = op.getOperationFromType(transaction);
                    if (!transaction.hasErrors() && op.isOperationExtournable()) {

                        op.extourner(transaction, journal, "Annulation de la TO");

                        // Sortir s'il y a des erreurs
                        if (transaction.hasErrors()) {
                            transaction.addErrors(op.toString());
                            break;
                        }
                    }
                }
            }
            ComptabiliserJournalCA comptabilisation = new ComptabiliserJournalCA(journal.getIdJournal());
            comptabilisation.setSession(BSessionUtil.getSessionFromThreadContext());
            BProcessLauncher.start(comptabilisation);

            return section.getId();

        } catch (Exception e) {
            transaction.addErrors(e.getMessage());
            return "";
        }
    }

    private CASection retrieveSection(BSession session, Decompte decompte, CACompteAnnexe compteAnnexe)
            throws Exception {
        CASectionManager sectionManager = new CASectionManager();
        sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
        sectionManager.setForIdExterne(decompte.getNumeroSection());
        sectionManager.setForIdTypeSection(decompte.getTypeSection().getValue());
        sectionManager.setSession(session);
        sectionManager.find();

        if (sectionManager.size() != 1) {
            // TODO erreur
        }

        CASection section = (CASection) sectionManager.getFirstEntity();

        // Refuser si section pas instanciée
        if (section.isNew()) {
            // throw new Exception(transaction.getSession().getLabel(CASection.LABEL_SECTION_NON_RENSEIGNEE));
        }

        return section;
    }

    private CACompteAnnexe retrieveCompteAnnexe(BSession session, Decompte decompte) throws Exception {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setIdRole(Role.AFFILIE_PARITAIRE.getValue());
        compteAnnexe.setIdExterneRole(decompte.getEmployeurAffilieNumero());
        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compteAnnexe.setSession(session);
        compteAnnexe.retrieve();
        return compteAnnexe;
    }

    private void createAndFindJournal(String libelleJournal) {
        try {
            createJournal(libelleJournal);
            journal = new CAJournal();
            journal.setId(journalSimpleModel.getId());
            journal.retrieve();
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    private void createJournal(String libelleJournal) throws Exception {
        journalSimpleModel = CABusinessServiceLocator.getJournalService().createJournal(libelleJournal,
                Date.now().getSwissValue());
    }
}
