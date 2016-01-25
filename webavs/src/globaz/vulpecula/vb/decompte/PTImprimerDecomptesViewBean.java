package globaz.vulpecula.vb.decompte;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import ch.globaz.vulpecula.web.views.decompte.DecompteViewService;

public class PTImprimerDecomptesViewBean extends BJadeSearchObjectELViewBean {
    private List<CodeSystem> etatDecomptes;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        etatDecomptes = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUPE_ETAT_DECOMPTES);
    }

    @Override
    public void setId(String arg0) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public List<CodeSystem> getEtatDecomptes() {
        return etatDecomptes;
    }

    public String getDecompteViewService() {
        return DecompteViewService.class.getName();
    }

    public String getMessagePasDeDocumentSelectionne() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DIALOG_PAS_DE_DOCUMENT_SELECTIONNE");
    }

    public String getEtatsEnSuspens() {
        return EtatDecompte.ERREUR.getValue() + "," + EtatDecompte.RECEPTIONNE.getValue() + ","
                + EtatDecompte.OUVERT.getValue() + "," + EtatDecompte.GENERE.getValue();
    }
}
