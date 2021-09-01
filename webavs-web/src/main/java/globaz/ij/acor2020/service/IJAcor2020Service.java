package globaz.ij.acor2020.service;

import acor.ij.xsd.ij.out.FCalcul;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IJAcor2020Service {

    public InHostType createInHost(String idPrononce) {
        IJExportationCalculAcor exporter = new IJExportationCalculAcor();
        return exporter.createInHost(idPrononce);
    }

    public void importCalculAcor(String idPrononce, FCalcul fCalcul){
        IJImportationCalculAcor importer = new IJImportationCalculAcor();
        importer.importCalculAcor(idPrononce, fCalcul);
    }
}
