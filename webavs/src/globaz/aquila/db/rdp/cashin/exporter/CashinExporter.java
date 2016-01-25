package globaz.aquila.db.rdp.cashin.exporter;

import globaz.aquila.db.rdp.cashin.model.RequisitionPoursuite;

public class CashinExporter {

    private CashinExporter() {
    }

    public static void export(RequisitionPoursuite requisitionPoursuite, String exportPath) throws Exception {
        CashinPersonExporter.export(requisitionPoursuite.getPersonne(), exportPath);
        CashinLitigeExporter.export(requisitionPoursuite.getLitiges(), exportPath);
    }
}
