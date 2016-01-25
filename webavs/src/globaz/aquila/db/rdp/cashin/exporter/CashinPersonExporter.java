package globaz.aquila.db.rdp.cashin.exporter;

import globaz.aquila.db.rdp.cashin.model.Personne;

public class CashinPersonExporter extends CashinExporterAbstract {

    private static final CashinPersonExporter instance = new CashinPersonExporter();

    private CashinPersonExporter() {
    }

    public static void export(Personne personne, String exportDirectoryPath) throws Exception {
        String content = CashinPersonExporter.createPersonLine(personne).toString();
        instance.createExportFile(content, exportDirectoryPath);
    }

    private static String createPersonLine(Personne personne) {
        StringBuilder line = new StringBuilder();

        line.append(personne.getNumExterne()).append(SEPARATOR);
        line.append(personne.getNom()).append(SEPARATOR);
        line.append(personne.getPrenom()).append(SEPARATOR);
        line.append(personne.getAutreNom()).append(SEPARATOR);
        line.append(personne.getTitre()).append(SEPARATOR);
        line.append(personne.getLangue()).append(SEPARATOR);
        line.append(personne.getProfession()).append(SEPARATOR);
        line.append(personne.getCritereRechercheLibre()).append(SEPARATOR);
        line.append(personne.getNumeroCompte()).append(SEPARATOR);
        line.append(personne.getNaissance()).append(SEPARATOR);
        line.append(personne.getRue()).append(SEPARATOR);
        line.append(personne.getChez()).append(SEPARATOR);
        line.append(personne.getNpa()).append(SEPARATOR);
        line.append(personne.getLocalite()).append(SEPARATOR);
        line.append(personne.getLieuDit()).append(SEPARATOR);
        line.append(personne.getDescTelephone()).append(SEPARATOR);
        line.append(personne.getNumTelephone()).append(SEPARATOR);
        line.append(personne.getPays()).append(SEPARATOR);
        line.append(personne.getOrigMutAdresse()).append(SEPARATOR);
        line.append(personne.getStatut()).append(SEPARATOR);
        line.append(personne.getSexe()).append(SEPARATOR);
        line.append(personne.getStatutLiquidation()).append(SEPARATOR);
        line.append(personne.getNss()).append(SEPARATOR);
        line.append(personne.getPersonneMorale()).append(SEPARATOR);
        line.append(personne.getNumeroEnregistrementRc()).append(SEPARATOR);
        line.append(personne.getSiegeSocial()).append(SEPARATOR);
        line.append(personne.getNotesPersonne()).append(SEPARATOR);
        line.append(personne.getDateFinRelation());

        return line.toString();
    }

    @Override
    protected String getFilename() {
        return "personnes" + EXTENSION;
    }
}
