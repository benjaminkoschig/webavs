package ch.globaz.vulpecula.external.fichierPlat;

import java.util.List;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.process.communicationsalaires.SalairesAAnnoncer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.fs.JadeFsFacade;

public class CommunicationSalairesExporter extends FichierPlatExporterAbstract {

    private static final CommunicationSalairesExporter instance = new CommunicationSalairesExporter();
    private static final String NUM_AFF_BMS_ENDING = "3";

    private CommunicationSalairesExporter() {
    }

    public static void export(List<SalairesAAnnoncer> listeSalairesAAnnoncer, String exportDirectoryPath,
            String fileName) throws Exception {
        if (JadeFsFacade.exists(exportDirectoryPath + fileName)) {
            JadeFsFacade.delete(exportDirectoryPath + fileName);
        }
        String content = CommunicationSalairesExporter.createLines(listeSalairesAAnnoncer);
        instance.createExportFile(content, exportDirectoryPath, fileName);
    }

    private static String createLines(List<SalairesAAnnoncer> listeSalairesAAnnoncer) {
        StringBuilder lines = new StringBuilder();

        int i = 0;
        for (SalairesAAnnoncer salairesAAnnoncer : listeSalairesAAnnoncer) {
            if (salairesAAnnoncer.getListeDecomptes() != null && salairesAAnnoncer.getListeDecomptes().size() > 0) {
                for (DecompteSalaire salaire : salairesAAnnoncer.getListeDecomptes()) {
                    if (!salaire.getSalaireTotal().isZero()) {
                        if (i != 0) {
                            lines.append(NEW_LINE);
                        }
                        i++;
                        // N°Affilié 0-10
                        lines.append(String.format("%-10s",
                                getNumeroAffilieFormate(salaire.getEmployeur().getAffilieNumero())));
                        // N°AVS 10-21
                        lines.append(String.format("%-11s", getNumeroAVS(salaire.getTravailleur().getNumAvsActuel())));
                        // Nom, Prénom 21-81
                        lines.append(String.format("%-60s", getNomPrenom(salaire.getTravailleur().getDesignation1(),
                                salaire.getTravailleur().getDesignation2())));
                        // Mois de début 81-83
                        lines.append(salaire.getPeriodeDebut().getMois());
                        // Mois de fin 83-85
                        lines.append(salaire.getPeriodeFin().getMois());
                        // Année 85-89
                        lines.append(salaire.getPeriodeDebut().getAnnee());
                        // Montant avec point
                        lines.append(String.format("%12s", salaire.getSalaireTotalAsValue()));
                    }

                }
            }

            if (salairesAAnnoncer.getListeAJ() != null && salairesAAnnoncer.getListeAJ().size() > 0) {
                for (AbsenceJustifiee aj : salairesAAnnoncer.getListeAJ()) {
                    if (aj.isAnnoncableAVS()) {
                        if (i != 0) {
                            lines.append(NEW_LINE);
                        }
                        i++;
                        // N°Affilié 0-10
                        lines.append(
                                String.format("%-10s", getNumeroAffilieFormate(aj.getEmployeur().getAffilieNumero())));
                        // N°AVS 10-21
                        lines.append(String.format("%-11s", getNumeroAVS(aj.getTravailleur().getNumAvsActuel())));
                        // Nom, Prénom 21-81
                        lines.append(String.format("%-60s", getNomPrenom(aj.getNomPrenomTravailleur())));
                        // Mois de début 81-83
                        lines.append(aj.getDateVersement().getMois());
                        // Mois de fin 83-85
                        lines.append(aj.getDateVersement().getMois());
                        // Année 85-89
                        lines.append(aj.getDateVersement().getAnnee());
                        // Montant avec point
                        lines.append(String.format("%12s", aj.getMasseAVS()));
                    }
                }
            }

            if (salairesAAnnoncer.getListeCP() != null && salairesAAnnoncer.getListeCP().size() > 0) {
                for (CongePaye cp : salairesAAnnoncer.getListeCP()) {
                    if (cp.isAnnoncableAVS()) {
                        if (i != 0) {
                            lines.append(NEW_LINE);
                        }
                        i++;
                        // N°Affilié 0-10
                        lines.append(
                                String.format("%-10s", getNumeroAffilieFormate(cp.getEmployeur().getAffilieNumero())));
                        // N°AVS 10-21
                        lines.append(String.format("%-11s", getNumeroAVS(cp.getTravailleur().getNumAvsActuel())));
                        // Nom, Prénom 21-81
                        lines.append(String.format("%-60s", getNomPrenom(cp.getNomPrenomTravailleur())));
                        // Mois de début 81-83
                        lines.append(cp.getDateVersement().getMois());
                        // Mois de fin 83-85
                        lines.append(cp.getDateVersement().getMois());
                        // Année 85-89
                        lines.append(cp.getDateVersement().getAnnee());
                        // Montant avec point
                        lines.append(String.format("%12s", cp.getMasseAVS()));
                    }
                }
            }
        }

        return lines.toString();
    }

    private static String getNumeroAffilieFormate(String numeroAffilie) {
        // On supprime les . et les -
        numeroAffilie = numeroAffilie.replace(".", "").replace("-", "");
        // On remplace les 2 derniers chiffres par l'extension BMS -3
        numeroAffilie = numeroAffilie.substring(0, numeroAffilie.length() - 2);
        numeroAffilie = numeroAffilie + NUM_AFF_BMS_ENDING;
        return numeroAffilie;
    }

    private static String getNumeroAVS(String numeroAvs) {
        if (!JadeStringUtil.isEmpty(numeroAvs) && numeroAvs.length() > 3) {
            numeroAvs = numeroAvs.substring(3, numeroAvs.length());
            numeroAvs = "-" + numeroAvs;
            numeroAvs = numeroAvs.replace(".", "");
        } else {
            numeroAvs = "00000000000";
        }
        return numeroAvs;
    }

    private static String getNomPrenom(String designation1, String designation2) {
        String designation = designation1 + " " + designation2;
        if (designation.length() > 60) {
            designation = designation.substring(0, 60);
        }
        return designation;
    }

    private static String getNomPrenom(String designation) {
        if (designation.length() > 60) {
            designation = designation.substring(0, 60);
        }
        return designation;
    }
}
