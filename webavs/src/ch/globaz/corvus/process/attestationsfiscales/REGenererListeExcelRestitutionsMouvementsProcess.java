package ch.globaz.corvus.process.attestationsfiscales;

import globaz.corvus.excel.REListeExcelRestitutionsMouvements;
import ch.globaz.utils.excel.ExcelJob;

public class REGenererListeExcelRestitutionsMouvementsProcess extends ExcelJob<REListeExcelRestitutionsMouvements> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REGenererListeExcelRestitutionsMouvementsProcess(REListeExcelRestitutionsMouvements documentGenerator,
            String email) {
        super(documentGenerator, email, true, "Liste des restitutions - mouvements", "Liste excel");
    }
}
