package globaz.ccvd.hermes;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hermes.application.HEProperties;
import globaz.hermes.utils.HEUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

public class HEExtractRentesPrevData extends HEExtractRentesData {
    // redéfinition du traitement des annonces
    // principe, extraire que les nouveaux extraits
    //
    private ArrayList referenceMemory;

    @Override
    protected void commitTreatReference(BSession session, BTransaction transaction) throws Exception {
        HEUtil.commitTreatReference(referenceMemory, session, transaction);
    }

    @Override
    protected boolean isReferenceInterneATraiter(String crtEnr, String motif, BSession session, BTransaction transaction) {
        // filtrer uniquement les motifs 97 et 98. Selon info CCVD
        if ("97".equals(motif) || "98".equals(motif)) {
            String referenceAExclure = HEProperties.getParameter("CODEEXTEXC", session, transaction);
            if (!JadeStringUtil.isEmpty(referenceAExclure)) {
                return JadeStringUtil.contains(crtEnr, referenceAExclure);
            } else {
                // si aucun paramétrage --> ne rien filtrer
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    protected boolean isSelectDateReceptionVide() {
        return true;
    }

    @Override
    protected void memoryReference(String crtRefUnique) {
        // on mémorise la référence au vu de la tagger
        if (referenceMemory == null) {
            referenceMemory = new ArrayList();
        }
        referenceMemory.add(crtRefUnique);
    }

}
