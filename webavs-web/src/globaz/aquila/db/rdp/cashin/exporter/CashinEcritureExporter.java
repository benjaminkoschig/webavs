package globaz.aquila.db.rdp.cashin.exporter;

import globaz.aquila.db.rdp.cashin.model.Ecriture;
import globaz.aquila.db.rdp.cashin.model.Litige;
import java.util.List;

public class CashinEcritureExporter extends CashinExporterAbstract {

    private static final CashinEcritureExporter instance = new CashinEcritureExporter();

    private CashinEcritureExporter() {
    }

    public static void export(List<Litige> litiges, String exportDirectoryPath) throws Exception {
        String content = CashinEcritureExporter.createEcrituresLines(litiges);
        instance.createExportFile(content, exportDirectoryPath);
    }

    private static String createEcrituresLines(List<Litige> litiges) {

        StringBuilder lines = new StringBuilder();

        for (Litige litige : litiges) {

            for (Ecriture ecriture : litige.getEcritures()) {

                lines.append(litige.getNumeroExterneLitige()).append(SEPARATOR);
                lines.append(ecriture.getNumeroExterneFacture()).append(SEPARATOR);
                lines.append(ecriture.getMontant()).append(SEPARATOR);
                lines.append(ecriture.getDateEcriture()).append(SEPARATOR);
                lines.append(ecriture.getLibelle()).append(SEPARATOR);
                lines.append(ecriture.getTypeEcriture()).append(SEPARATOR);
                lines.append(ecriture.getNumeroExterneEcriture()).append(SEPARATOR);
                lines.append(ecriture.getNumeroExterneEcritureAAnnuler()).append(NEW_LINE);
            }
        }

        return lines.toString();
    }

    @Override
    protected String getFilename() {
        return "ecritures" + EXTENSION;
    }
}
