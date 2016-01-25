package ch.globaz.corvus.process.attestationsfiscales;

import globaz.corvus.excel.REListeExcelAttestationsFiscalesNonSorties;
import ch.globaz.utils.excel.ExcelJob;

public class REGenrererListeExcelAttestationsNonSortiesProcess extends
        ExcelJob<REListeExcelAttestationsFiscalesNonSorties> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REGenrererListeExcelAttestationsNonSortiesProcess(
            REListeExcelAttestationsFiscalesNonSorties documentGenerator, String email) {
        super(documentGenerator, email, true, "Liste des attestations fiscales non sorties", "Liste excel");
    }
}
