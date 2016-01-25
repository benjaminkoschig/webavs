package globaz.aquila.db.rdp.cashin.exporter;

import globaz.aquila.db.rdp.cashin.model.Creance;
import globaz.aquila.db.rdp.cashin.model.Litige;
import java.util.List;

public class CashinLitigeExporter extends CashinExporterAbstract {

    private static final CashinLitigeExporter instance = new CashinLitigeExporter();

    private CashinLitigeExporter() {
    }

    public static void export(List<Litige> litiges, String exportDirectoryPath) throws Exception {

        String content = CashinLitigeExporter.createLitigeLines(litiges);
        instance.createExportFile(content, exportDirectoryPath);
    }

    private static String createLitigeLines(List<Litige> litiges) {

        StringBuilder lines = new StringBuilder();

        for (Litige litige : litiges) {

            for (Creance creance : litige.getCreances()) {
                lines.append(litige.getNumExterneDossier()).append(SEPARATOR);
                lines.append(litige.getNumExterneDebiteur()).append(SEPARATOR);
                lines.append(litige.getNumeroExterneLitige()).append(SEPARATOR);
                lines.append(litige.getCodeGestionnaire()).append(SEPARATOR);

                lines.append(creance.getFraisSommation()).append(SEPARATOR);
                lines.append(creance.getNumeroExterneCreance()).append(SEPARATOR);
                lines.append(creance.getTitreCreance()).append(SEPARATOR);
                lines.append(creance.getMontant()).append(SEPARATOR);
                lines.append(creance.getTauxInteret()).append(SEPARATOR);
                lines.append(creance.getDateDebutInteret()).append(SEPARATOR);
                lines.append(creance.getCommentaire()).append(SEPARATOR);
                lines.append(creance.getNumeroExterneMandataire()).append(SEPARATOR);
                lines.append(creance.getNumeroExterneAgent()).append(SEPARATOR);
                lines.append(creance.getNumeroExterneSociete()).append(SEPARATOR);
                lines.append(creance.getNumerosAutresCreanciers()).append(SEPARATOR);
                lines.append(creance.getCreancePartagee()).append(SEPARATOR);
                lines.append(creance.getCodeUtilisateur()).append(SEPARATOR);
                lines.append(creance.getModeImportation()).append(SEPARATOR);
                lines.append(creance.getOption1ConcDebiteur()).append(SEPARATOR);
                lines.append(creance.getOption2ConcDebiteur()).append(SEPARATOR);
                lines.append(creance.getOption3ConcDebiteur()).append(SEPARATOR);
                lines.append(creance.getOption4ConcDebiteur()).append(SEPARATOR);
                lines.append(creance.getOption5ConcDebiteur()).append(SEPARATOR);
                lines.append(creance.getCodePremiereEtape()).append(SEPARATOR);
                lines.append(creance.getVentilationComptable()).append(SEPARATOR);
                lines.append(creance.getRepartitionCreance()).append(SEPARATOR);
                lines.append(creance.getDatePremiereEtape()).append(SEPARATOR);
                lines.append(litige.getDateCreationLitige()).append(NEW_LINE);
            }
        }

        return lines.toString().trim();
    }

    @Override
    protected String getFilename() {
        return "factures" + EXTENSION;
    }
}
