package globaz.naos.db.listeDeces;

import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pyxis.db.tiers.TIRole;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

public class AFListeDecesBatchPrinter {

    public AFListeDecesBatchPrinter() {
    }

    public PrintWriter createBatchPrinter(String dateDuJour) throws Exception {
        PrintWriter printer = new PrintWriter(new FileWriter("listeDesDeces" + dateDuJour + ".csv"));
        StringBuffer csvHeader = new StringBuffer();
        csvHeader.append("ROLE").append(";");
        csvHeader.append("NSS").append(";");
        csvHeader.append("NOM").append(";");
        csvHeader.append("PRENOM").append(";");
        csvHeader.append("DATE DE NAISSANCE").append(";");
        csvHeader.append("DATE DE DECES").append(";");
        csvHeader.append("ADRESSE DE DOMICILE").append(";");
        csvHeader.append("NUMERO AFFILIE").append(";");
        csvHeader.append("DATE DEBUT AFFILIATION").append(";");
        csvHeader.append("DATE FIN AFFILIATION").append(";");
        printer.println(csvHeader.toString());
        printer.flush();
        return printer;
    }

    public StringBuffer printLine(BSession session, List affiliationList, List roleList, String nss, String nom,
            String prenom, String dateNaissance, String dateDeces, String adresseDomicile, String dateDuJour)
            throws Exception {

        StringBuffer csvLine = new StringBuffer();

        if (roleList.isEmpty() && affiliationList.isEmpty()) {
            csvLine.append(" ").append(";");
            csvLine.append(nss).append(";");
            csvLine.append(nom).append(";");
            csvLine.append(prenom).append(";");
            csvLine.append(dateNaissance).append(";");
            csvLine.append(dateDeces).append(";");
            csvLine.append(adresseDomicile).append(";");
            csvLine.append(" ").append(";");
            csvLine.append(" ").append(";");
            csvLine.append(" ").append(";");
            csvLine.append("\n");
        }

        for (Iterator iterator = roleList.iterator(); iterator.hasNext();) {
            TIRole role = (TIRole) iterator.next();
            String rol = role.getRole();
            String roleFinal = session.getCodeLibelle(rol);

            if (!affiliationList.isEmpty() && roleFinal.startsWith("Assu")) {
                for (Iterator iter = affiliationList.iterator(); iter.hasNext();) {
                    AFAffiliation aff = (AFAffiliation) iter.next();

                    csvLine.append(roleFinal).append(";");
                    csvLine.append(nss).append(";");
                    csvLine.append(nom).append(";");
                    csvLine.append(prenom).append(";");
                    csvLine.append(dateNaissance).append(";");
                    csvLine.append(dateDeces).append(";");
                    csvLine.append(adresseDomicile).append(";");
                    csvLine.append(aff.getAffilieNumero()).append(";");
                    csvLine.append(aff.getDateDebut()).append(";");
                    csvLine.append(aff.getDateFin()).append(";");
                    csvLine.append("\n");

                }
            } else {
                csvLine.append(roleFinal).append(";");
                csvLine.append(nss).append(";");
                csvLine.append(nom).append(";");
                csvLine.append(prenom).append(";");
                csvLine.append(dateNaissance).append(";");
                csvLine.append(dateDeces).append(";");
                csvLine.append(adresseDomicile).append(";");
                csvLine.append(" ").append(";");
                csvLine.append(" ").append(";");
                csvLine.append(" ").append(";");
                csvLine.append("\n");
            }
        }
        return csvLine;
    }
}
