package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
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

                CACompteAnnexe compteAnnexe = new CACompteAnnexe();
                compteAnnexe.setIdRole(Role.AFFILIE_PARITAIRE.getValue());
                compteAnnexe.setIdExterneRole(decompte.getEmployeurAffilieNumero());
                compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compteAnnexe.setSession(session);
                compteAnnexe.retrieve();

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
