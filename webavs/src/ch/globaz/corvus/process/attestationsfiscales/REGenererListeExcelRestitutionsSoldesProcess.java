package ch.globaz.corvus.process.attestationsfiscales;

import globaz.corvus.excel.REListeExcelRestitutionsSoldes;
import ch.globaz.utils.excel.ExcelJob;

public class REGenererListeExcelRestitutionsSoldesProcess extends ExcelJob<REListeExcelRestitutionsSoldes> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REGenererListeExcelRestitutionsSoldesProcess(REListeExcelRestitutionsSoldes documentGenerator, String email) {
        super(documentGenerator, email, true, "Liste des restitutions - soldes", "Liste excel");
    }
}
