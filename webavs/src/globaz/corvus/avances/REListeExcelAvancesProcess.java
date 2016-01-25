package globaz.corvus.avances;

import globaz.corvus.excel.REListeExcelAvances;
import ch.globaz.utils.excel.ExcelJob;

public class REListeExcelAvancesProcess extends ExcelJob<REListeExcelAvances> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REListeExcelAvancesProcess(REListeExcelAvances documentGenerator, String email) {
        super(documentGenerator, email, true, "Liste des avances", "Liste excel");
    }
}
