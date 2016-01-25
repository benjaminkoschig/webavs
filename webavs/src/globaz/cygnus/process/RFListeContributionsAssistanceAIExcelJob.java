package globaz.cygnus.process;

import globaz.cygnus.helpers.process.RFListeExcelContributionsAssistanceAI;
import globaz.globall.db.BSession;
import ch.globaz.utils.excel.ExcelJob;

/**
 * @author PBA
 */
public class RFListeContributionsAssistanceAIExcelJob extends ExcelJob<RFListeExcelContributionsAssistanceAI> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFListeContributionsAssistanceAIExcelJob(BSession session,
            RFListeExcelContributionsAssistanceAI documentGenerator, String email) {
        super(documentGenerator, email, true, session.getLabel("PROCESS_LISTE_CAAI"), session
                .getLabel("PROCESS_LISTE_CAAI"));
    }
}
