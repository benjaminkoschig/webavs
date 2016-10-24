package globaz.cygnus.service;

import globaz.cygnus.mappingXmlml.RFXmlmlMappingLogImport;
import globaz.cygnus.process.demande.LigneImport;
import globaz.cygnus.process.financementSoin.CellulesExcelEnum;
import globaz.cygnus.process.financementSoin.NSS;
import globaz.cygnus.process.financementSoin.RFLigneFichierExcel;
import globaz.cygnus.process.soinAdomicile.LigneFichier;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.globall.db.BSession;
import globaz.jade.smtp.JadeSmtpClient;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.domaine.Montant;
import com.google.common.base.Throwables;

public class RFimportService implements Comparable<RFLigneFichierExcel> {

    private static String NSS_A_ZERO = "000.0000.0000.00";
    private NumberFormat formatter = new DecimalFormat("###,##0.00");
    private List<RFLigneFichierExcel> lignesTriees = new ArrayList<RFLigneFichierExcel>();

    @Override
    public int compareTo(RFLigneFichierExcel o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Methode pour créer le contenu du mail (ligne refusées + lignes acceptées)
     * 
     * @param mapLigneEnTraitement
     * @return
     */
    private String createMessageOfLinesInBodyMail(BSession session,
            Map<NSS, ArrayList<RFLigneFichierExcel>> mapLigneEnTraitement) {
        StringBuffer bodyMail = new StringBuffer();

        String nssTiers = "";

        bodyMail.append("<b>" + session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_RESULTAT") + "</b>" + "\n \n \n");

        // Ordres les lignes par numéro de ligne
        lignesTriees = ordrerEntitesParNumeroDeLigne(mapLigneEnTraitement);

        // Creation des lignes en erreurs
        bodyMail = loadMessageOfLinesWithError(session, lignesTriees, nssTiers, bodyMail);

        // Creation des lignes sans erreurs
        bodyMail = loadMessageOfLinesWithoutErrors(session, lignesTriees, nssTiers, bodyMail);

        return bodyMail.toString();
    }

    /**
     * Methode pour construire les lignes en erreurs
     * 
     * @param session
     * @param mapLigneEnTraitement
     * @param nssTiers
     * @param bodyMail
     * @return
     */
    private StringBuffer loadMessageOfLinesWithError(BSession session,
            List<RFLigneFichierExcel> listeTrieesParNumeroLigne, String nssTiers, StringBuffer bodyMail) {

        bodyMail.append("<b>" + session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_LIGNES_EN_ERREURS") + "</b>");
        bodyMail.append("\n");

        for (RFLigneFichierExcel ligne : listeTrieesParNumeroLigne) {
            if (ligne.getCellulesEnErreur().size() > 0) {

                // Si ligne == 000.0000.0000.00 (= ligne incomplète)
                if (ligne.getNumNss().equals(RFimportService.NSS_A_ZERO)) {
                    nssTiers = null;
                } else {
                    nssTiers = ligne.getNumNss();
                }

                for (CellulesExcelEnum label : ligne.getCellulesEnErreur()) {
                    bodyMail.append(session.getLabel(label.getKeyLabel()));
                    bodyMail.append("; ");
                }

                bodyMail.append("\n");

                // Incrémentation du numéro de ligne de 1, pour être juste avec la ligne du fichier importé
                Integer numLigneIncremente = Integer.valueOf(ligne.getNumeroLigne()) + 1;

                bodyMail.append("<b>" + numLigneIncremente.toString() + "</b>" + " / " + nssTiers + " / "
                        + ligne.getNomPrenom() + " / " + ligne.getDateDebut() + " - " + ligne.getDateFin() + " / "
                        + ligne.getFraisJournalier() + " / " + ligne.getNbJours() + " / " + ligne.getMontantTotal()
                        + " / " + ligne.getDateDecompte());

                bodyMail.append(".");
                bodyMail.append("\n");
                bodyMail.append("---");
                bodyMail.append("\n");
            }
        }

        return bodyMail;
    }

    /**
     * Methode pour construire les lignes sans erreurs
     * 
     * @param session
     * @param mapLigneEnTraitement
     * @param nssTiers
     * @param bodyMail
     * @return
     */
    private StringBuffer loadMessageOfLinesWithoutErrors(BSession session,
            List<RFLigneFichierExcel> listeTrieeParNumeroDeLigne, String nssTiers, StringBuffer bodyMail) {

        BigDecimal additionMontantsTotaux = new BigDecimal(0);

        bodyMail.append("\n \n");
        bodyMail.append("<b>" + session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_LIGNES_SANS_ERREURS") + "</b>");
        bodyMail.append("\n");

        for (RFLigneFichierExcel ligne : listeTrieeParNumeroDeLigne) {
            if (ligne.getCellulesEnErreur().size() == 0) {

                // Récupération du nss
                nssTiers = ligne.getNumNss();

                // Addition chaque montant pour récapitulatif en bas de mail
                additionMontantsTotaux = additionMontantsTotaux.add(new BigDecimal(ligne.getMontantTotal()));

                // Incrémentation du numéro de ligne de 1, pour être juste avec la ligne du fichier importé
                Integer numLigneIncremente = Integer.valueOf(ligne.getNumeroLigne()) + 1;

                bodyMail.append("<b>" + numLigneIncremente.toString() + "</b>" + " / " + nssTiers + " / "
                        + ligne.getNomPrenom() + " / " + ligne.getDateDebut() + " - " + ligne.getDateFin() + " / "
                        + ligne.getFraisJournalier() + " / " + ligne.getNbJours() + " / " + ligne.getMontantTotal()
                        + " / " + ligne.getDateDecompte());

                bodyMail.append(".");
                bodyMail.append("\n");
                bodyMail.append("---");
                bodyMail.append("\n");
            }
        }

        String montantTotal = formatter.format(Double.valueOf(additionMontantsTotaux.toString()));

        bodyMail.append("---------------------------------------------------------------------------" + "\n");
        bodyMail.append(session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_MONTANT_TOTAL"));
        bodyMail.append(" CHF ");
        bodyMail.append(montantTotal + "\n");
        bodyMail.append("---------------------------------------------------------------------------" + "\n");
        bodyMail.append("---------------------------------------------------------------------------");

        return bodyMail;
    }

    /**
     * Methode pour ordrer les entités par numéro de ligne.
     * 
     * @param mapLigneEnTraitement
     * @return
     */
    private List<RFLigneFichierExcel> ordrerEntitesParNumeroDeLigne(
            Map<NSS, ArrayList<RFLigneFichierExcel>> mapLigneEnTraitement) {

        List<RFLigneFichierExcel> listeTriees = new ArrayList<RFLigneFichierExcel>();

        for (NSS keyNss : mapLigneEnTraitement.keySet()) {
            for (RFLigneFichierExcel ligne : mapLigneEnTraitement.get(keyNss)) {
                listeTriees.add(ligne);
            }
        }
        Collections.sort(listeTriees);

        return listeTriees;
    }

    /**
     * Methode pour ordrer les entités par numéro de ligne.
     * 
     * @param mapLigneEnTraitement
     * @return
     */
    private List<LigneImport> createListAndSortByNumeroDeLigne(Map<NSS, List<LigneFichier>> mapLigneEnTraitement) {

        List<LigneImport> listeTriees = new ArrayList<LigneImport>();

        for (NSS keyNss : mapLigneEnTraitement.keySet()) {
            for (LigneImport ligne : mapLigneEnTraitement.get(keyNss)) {
                listeTriees.add(ligne);
            }
        }
        Collections.sort(listeTriees);

        return listeTriees;
    }

    /**
     * Methode pour construire le mail et le fichier joint (pour le home)
     * 
     * @param mapLigneEnTraitement
     * @param emailDestinataire
     * @param gestionnaire
     */
    public void sendMail(BSession session, Map<NSS, ArrayList<RFLigneFichierExcel>> mapLigneEnTraitement,
            String emailDestinataire, String gestionnaire) {

        try {

            List<ArrayList<String>> lignesRetournees = new ArrayList<ArrayList<String>>();
            boolean createFileXml = false;
            String docPath = "";
            String textesBodyMail = "";

            for (NSS keyNss : mapLigneEnTraitement.keySet()) {
                for (RFLigneFichierExcel ligne : mapLigneEnTraitement.get(keyNss)) {
                    if (ligne.getCellulesEnErreur().size() > 0) {

                        ArrayList<String> cellulesLigne = new ArrayList<String>();
                        cellulesLigne.add(ligne.getNumHome());
                        cellulesLigne.add(ligne.getNomHome());
                        cellulesLigne.add(ligne.getNumNss());
                        cellulesLigne.add(ligne.getNomPrenom());
                        cellulesLigne.add(ligne.getFraisJournalier());
                        cellulesLigne.add(ligne.getDateDebut());
                        cellulesLigne.add(ligne.getDateFin());
                        cellulesLigne.add(ligne.getNbJours());
                        cellulesLigne.add(ligne.getMontantTotal());
                        cellulesLigne.add(ligne.getDateDecompte());

                        lignesRetournees.add(lignesRetournees.size(), cellulesLigne);

                        createFileXml = true;
                    }
                }
            }

            if (createFileXml) {
                RFXmlmlContainer container = RFXmlmlMappingLogImport.loadResults(lignesRetournees);
                String nomDoc = session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_FICHIER_A_RETOURNER");
                docPath = RFExcelmlUtils.createDocumentExcel("FR/" + "importationFinancementSoins.xml", nomDoc,
                        container);
            }

            // Si aucune erreur, cahrgement des lignes traités dans le body du mail.
            if (!session.hasErrors()) {
                textesBodyMail = createMessageOfLinesInBodyMail(session, mapLigneEnTraitement);
            } else {
                // Sinon, chargement de l'erreur rencontrée.
                textesBodyMail = session.getErrors().toString();
            }

            JadeSmtpClient.getInstance().sendMail(emailDestinataire,
                    session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_TITRE_PROCESS"), textesBodyMail,
                    new String[] { docPath });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasError(Map<NSS, List<LigneFichier>> map) {
        for (Entry<NSS, List<LigneFichier>> entry : map.entrySet()) {
            for (LigneFichier ligne : entry.getValue()) {
                if (ligne.hasError()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Methode pour construire le mail et le fichier joint (pour le home)
     * 
     * @param mapLignesEnTraitement
     * @param emailDestinataire
     */
    public void sendMailForSoinDomicile(BSession session, Map<NSS, List<LigneFichier>> map, String emailDestinataire) {

        StringBuilder bodyMail = new StringBuilder();
        List<LigneImport> listeTrieesParNumeroLigne = createListAndSortByNumeroDeLigne(map);

        // Si aucune erreur, chargement des lignes traités dans le body du mail.
        if (!session.hasErrors()) {
            if (hasError(map)) {
                bodyMail.append("<b>" + session.getLabel("MAIL_RF_IMPORT_SOIN_DOMICILE_RESULTAT") + "</b>" + "\n \n \n");
                // Ordres les lignes par numéro de ligne
                // Creation des lignes en erreurs
                bodyMail.append("<b>" + session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_LIGNES_EN_ERREURS") + "</b>");

                bodyMail.append("\n");
                for (LigneImport ligne : listeTrieesParNumeroLigne) {
                    // Incrémentation du numéro de ligne de 1, pour être juste avec la ligne du fichier importé
                    bodyMail.append("\n<b> Ligne " + (ligne.getNumeroLigne()) + " en erreur : </b>"
                            + ligne.getDescription());
                    bodyMail.append("<ul>");
                    for (Entry<String, List<String>> entry : ligne.getErrors().entrySet()) {
                        bodyMail.append("<li>");
                        bodyMail.append(MessageFormat.format(
                                session.getLabel("PROCESS_RF_IMPORT_SOIN_DOMICILE_ERREUR_" + entry.getKey()).replace(
                                        "'", "'\'"), entry.getValue().toArray()));
                        bodyMail.append("</li>");
                    }

                    if (ligne.getException() != null) {
                        bodyMail.append("<li>");
                        bodyMail.append(Throwables.getStackTraceAsString(ligne.getException()));
                        bodyMail.append("</li>");
                    }
                    bodyMail.append("</ul>");
                }
            } else {
                bodyMail.append("\n \n");
                bodyMail.append("<b>" + session.getLabel("MAIL_RF_IMPORT_SOIN_DOMICIL_LIGNES_SANS_ERREUR") + "</b>");
                bodyMail.append("\n");
                Montant total = Montant.ZERO;
                for (LigneImport ligne : listeTrieesParNumeroLigne) {
                    total = total.add(ligne.getTotal());
                }
                bodyMail.append("\n\n");
                bodyMail.append(session.getLabel("MAIL_RF_IMPORT_SOIN_DOMICILE_MONTANT_TOTAL"));
                bodyMail.append(" CHF ");
                bodyMail.append(total.toStringFormat() + "\n");
            }
        } else {
            // Sinon, chargement de l'erreur rencontrée.
            bodyMail.append(session.getErrors().toString());
        }

        try {
            JadeSmtpClient.getInstance().sendMail(emailDestinataire,
                    session.getLabel("MAIL_RF_IMPORT_SOIN_DOMICILE_TITRE_PROCESS"), bodyMail.toString(), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMailFichierEnErreur(BSession session, String typeErreur, String emailDestinataire,
            String gestionnaire) {

        try {

            JadeSmtpClient.getInstance().sendMail(emailDestinataire,
                    session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_TITRE_PROCESS"),
                    session.getLabel("MAIL_RF_IMPORT_FINANCEMENT_SOIN_FICHIER_VIDE"), null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}