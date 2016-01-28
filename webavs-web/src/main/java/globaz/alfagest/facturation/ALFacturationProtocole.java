package globaz.alfagest.facturation;

import globaz.alfagest.db.ALPrestationPaiement;
import globaz.alfagest.db.ALPrestationPaiementManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Créé le 18 janv. 06
 * 
 * @author dch
 * 
 *         Crée le protocole des compensations sur facture et des paiements directs.
 *         Crée un fichier HTML, et permet l'envoye à l'e-mail de l'utilisateur de la session connectée
 */
public class ALFacturationProtocole {
    // constantes pour spécifier le type du protocole à générer
    public static int PROTOCOLE_PAIEMENTS_DIRECTS = 10;
    public static int PROTOCOLE_PAIEMENTS_INDIRECTS = 11;

    // paramètres du protocole
    ALPrestationPaiementManager prestations = null;
    int typePaiement = 0;
    String selectionFacture = null;
    String numeroPassage = null;
    boolean modeSimulation = true;
    String totalFacture = null;
    BigDecimal totalRecap = new BigDecimal("0.00");
    BigDecimal totalError = new BigDecimal("0.00");
    BSession session = null;
    BTransaction transaction = null;
    boolean facturationOK = true;

    // fichier du protocole
    private File file = null;

    // pour écrire dans le fichier
    PrintStream ps = null;

    // contient les erreurs de génération des prestations
    private ArrayList errors = null;

    // contient les avertissements d'affiliation
    private ArrayList warning = null;

    /*
     * Crée le fichier HTML corresponant au JBPrestationPaiementsSetTM passé en paramètre
     * 
     * @param prestations les prestations de paiement
     * 
     * @param tableAvertissement les différentes prestations qui possèdent un avertissement de l'affiliation
     * 
     * @param typePaiement le type de paiement, PROTOCOLE_PAIEMENTS_DIRECTS ou PROTOCOLE_PAIEMENTS_INDIRECTS
     * 
     * @param numeroFacture le numéro de facture
     * 
     * @param numeroPassage le numéro de passage
     * 
     * @param modeSimulation true si on est en mode simulation, false sinon
     */
    public boolean create(ALPrestationPaiementManager myPrestations, ArrayList tableAvertissement, int myTypePaiement,
            String mySelectionFacture, String myNumeroPassage, boolean myModeSimulation, BSession mySession,
            BTransaction myTransaction, String myTotalFacture) throws Exception {
        FileOutputStream outputStream = null;

        prestations = myPrestations;
        warning = tableAvertissement;
        typePaiement = myTypePaiement;
        selectionFacture = mySelectionFacture;
        numeroPassage = myNumeroPassage;
        modeSimulation = myModeSimulation;
        totalFacture = myTotalFacture;
        session = mySession;
        transaction = myTransaction;

        // ouverture du fichier
        try {
            file = new File(Jade.getInstance().getPersistenceDir() + "/"
                    + JadeFilenameUtil.addFilenameSuffixUID("Protocole.html"));
            file.delete();
            file.createNewFile();
            file.deleteOnExit();
            outputStream = new FileOutputStream(file);
            ps = new PrintStream(outputStream);
        } catch (IOException e) {
            throw new IOException("Impossible de créer un fichier: " + e.getMessage());
        }

        // on charge les erreurs depuis la table des recaps de prestations
        getErreurs();

        // remplissage de la page HTML
        fillHTML();

        try {
            ps.close();
            outputStream.close();
        } catch (Exception e) {
            throw new IOException("Impossible de fermer un fichier: " + e.getMessage());
        }

        return facturationOK;
    }

    /*
     * Envoye le fichier par e-mail
     */
    public void sendByEmail() throws Exception {
        String attachement[] = new String[1];

        if (file == null) {
            throw new Exception("Fichier invalide");
        }

        attachement[0] = file.getAbsolutePath();

        JadeSmtpClient.getInstance()
                .sendMail(session.getUserEMail(), getTypePaiement(), getTypePaiement(), attachement);
    }

    /*
     * Remplit le fichier HTML par l'intermédiaire d'un PrintStream
     */
    private void fillHTML() throws Exception {
        ps.println("<html>");

        fillHTMLHead();
        fillHTMLBody();

        ps.println("</html>");
    }

    /*
     * Remplit le <head> du fichier HTML
     */
    private void fillHTMLHead() {

        ps.println("<head>");
        ps.println("<title>");
        ps.println(getTypePaiement());
        ps.println("</title>");
        ps.println("</head>");
    }

    /*
     * Remplit le <body> du fichier HTML
     */
    private void fillHTMLBody() throws Exception {
        ps.println("<body>");

        fillHTMLBodyEntete();
        fillHTMLBodyRecaps();
        fillHTMLBodyWarning();
        fillHTMLBodyErreurs();

        // fin
        ps.println("</body>");
    }

    /*
     * Remplit le début du <body> du fichier HTML
     */
    private void fillHTMLBodyEntete() {
        int sizeError = 0;

        if (errors != null) {
            sizeError = errors.size();
        }

        // titre
        ps.println("<h2><b>" + getTypePaiement());
        if (modeSimulation) {
            ps.println("<br><br>Simulation");
        }
        ps.println("</b></h2>");

        // informations
        ps.println("<b>Utilisateur: </b>" + session.getUserName() + "<br>");
        ps.println("<b>Date: </b>" + getDate() + "<br>");
        ps.println("<b>Heure: </b>" + getTime() + "<br>");
        ps.println("<b>Sélection facture: </b>" + selectionFacture + "<br>");
        ps.println("<b>N° de passage: </b>" + numeroPassage + "<br>");
        ps.println("<b>Récaps comptabilisées: </b>" + (prestations.size() - sizeError) + "<br>");

        if (errors == null) {
            ps.println("<b>Erreurs: </b> Aucune erreur détectée <br><br>");
        } else {
            ps.println("<b>Erreurs: </b>" + errors.size() + "<br><br>");
        }
    }

    /*
     * Remplit le tableau des récaps du <body> du fichier HTML
     */
    private void fillHTMLBodyRecaps() {
        // prestation utilisée pour itérer
        ALPrestationPaiement prestation = null;

        // déclaration du tableau
        ps.println("<table border=\"1\" cellpadding=\"4\" bgcolor=\"#CCCCCC\">");

        // première ligne du tableau
        ps.println("<tr>");
        ps.println("<th>N° affilié</th>");
        ps.println("<th>Nom</th>");
        ps.println("<th>N° récap</th>");
        ps.println("<th>N° facture</th>");
        ps.println("<th colspan=\"2\">Période</th>");
        ps.println("<th>Compte</th>");
        ps.println("<th>Rubrique</th>");
        ps.println("<th>Montant</th>");
        ps.println("</tr>");

        // on boucle pour chaque prestation pour remplir le tableau
        for (int i = 0; i < prestations.size(); i++) {
            prestation = (ALPrestationPaiement) prestations.get(i);

            if (prestation.getEtatRecap().equals("CO")) {
                if (i % 2 == 0) {
                    ps.println("<tr bgcolor=\"#EEEEEE\">");
                } else {
                    ps.println("<tr bgcolor=\"#CCCCCC\">");
                }

                ps.println("<td align=center>" + prestation.getNumeroAffilie() + "</td>");
                ps.println("<td align=left>" + prestation.getDenomination1() + " " + prestation.getDenomination2()
                        + "</td>");
                ps.println("<td align=center>" + prestation.getIdRecap() + "</td>");
                ps.println("<td align=center>" + prestation.getNumeroFacture() + "</td>");
                ps.println("<td align=center>" + prestation.getPeriodeRecapDe().substring(4, 6) + "."
                        + prestation.getPeriodeRecapDe().substring(0, 4) + "</td>");
                ps.println("<td align=center>" + prestation.getPeriodeRecapA().substring(4, 6) + "."
                        + prestation.getPeriodeRecapA().substring(0, 4) + "</td>");
                ps.println("<td align=center>" + prestation.getChiffreStatistique() + "</td>");
                ps.println("<td align=center>" + prestation.getCategorieRubrique() + "</td>");
                ps.println("<td align=right>"
                        + JANumberFormatter.fmt(prestation.getTotalMontant(), true, true, false, 2) + "</td>");
                ps.println("</tr>");

                totalRecap = totalRecap.add(new BigDecimal(prestation.getTotalMontant()));
            }
        }

        // dernière ligne: total final
        if (prestations.size() % 2 == 0) {
            ps.println("<tr bgcolor=\"#EEEEEE\">");
        } else {
            ps.println("<tr bgcolor=\"#CCCCCC\">");
        }

        ps.println("<td><b>Total</b></td>");
        for (int i = 0; i < 7; i++) {
            ps.println("<td>&nbsp;</td>");
        }
        ps.println("<td><b>" + JANumberFormatter.fmt(totalRecap.toString(), true, true, false, 2) + "</b></td>");
        ps.println("</tr>");

        // fin du tableau
        ps.println("</table><br>");
    }

    /*
     * Remplit les erreurs du <body> du fichier HTML
     */
    private void fillHTMLBodyWarning() throws Exception {

        // prestation utilisée pour itérer
        ALPrestationPaiement prestation = null;

        // Titre de la section
        ps.println("<b>Diagnostic</b><br>");

        // Titre de la section
        ps.println("<b>Avertissements</b><br>");

        // déclaration du tableau
        ps.println("<table border=\"1\" cellpadding=\"4\" bgcolor=\"#CCCCCC\">");

        // première ligne du tableau
        ps.println("<tr>");
        ps.println("<th>N° affilié</th>");
        ps.println("<th>Nom</th>");
        ps.println("<th>N° récap</th>");
        ps.println("<th>N° facture</th>");
        ps.println("<th colspan=\"2\">Période</th>");
        ps.println("<th>Compte</th>");
        ps.println("<th>Rubrique</th>");
        ps.println("<th>Avertissement</th>");
        ps.println("</tr>");

        // on boucle pour chaque prestation pour remplir le tableau
        for (int i = 0; i < warning.size(); i++) {
            prestation = (ALPrestationPaiement) warning.get(i);

            if (i % 2 == 0) {
                ps.println("<tr bgcolor=\"#EEEEEE\">");
            } else {
                ps.println("<tr bgcolor=\"#CCCCCC\">");
            }

            ps.println("<td align=center>" + prestation.getNumeroAffilie() + "</td>");
            ps.println("<td align=left>" + prestation.getDenomination1() + " " + prestation.getDenomination2()
                    + "</td>");
            ps.println("<td align=center>" + prestation.getIdRecap() + "</td>");
            ps.println("<td align=center>" + prestation.getNumeroFacture() + "</td>");
            ps.println("<td align=center>" + prestation.getPeriodeRecapDe().substring(4, 6) + "."
                    + prestation.getPeriodeRecapDe().substring(0, 4) + "</td>");
            ps.println("<td align=center>" + prestation.getPeriodeRecapA().substring(4, 6) + "."
                    + prestation.getPeriodeRecapA().substring(0, 4) + "</td>");
            ps.println("<td align=center>" + prestation.getChiffreStatistique() + "</td>");
            ps.println("<td align=center>" + prestation.getCategorieRubrique() + "</td>");
            ps.println("<td align=center>" + prestation.getMsgErreur() + "</td>");
            ps.println("</tr>");
        }

        // fin du tableau
        ps.println("</table><br>");
    }

    /*
     * Remplit les erreurs du <body> du fichier HTML
     */
    private void fillHTMLBodyErreurs() throws Exception {

        // prestation utilisée pour itérer
        ALPrestationPaiement prestation = null;

        // Titre de la section
        ps.println("<b>Erreurs</b><br>");

        // déclaration du tableau
        ps.println("<table border=\"1\" cellpadding=\"4\" bgcolor=\"#CCCCCC\">");

        // première ligne du tableau
        ps.println("<tr>");
        ps.println("<th>N° affilié</th>");
        ps.println("<th>Nom</th>");
        ps.println("<th>N° récap</th>");
        ps.println("<th>N° facture</th>");
        ps.println("<th colspan=\"2\">Période</th>");
        ps.println("<th>Compte</th>");
        ps.println("<th>Rubrique</th>");
        ps.println("<th>Message d'erreur</th>");
        ps.println("<th>Montant</th>");
        ps.println("</tr>");

        // on boucle pour chaque prestation pour remplir le tableau
        for (int i = 0; i < prestations.size(); i++) {
            prestation = (ALPrestationPaiement) prestations.get(i);

            if (prestation.getEtatRecap().equals("ER")) {
                if (i % 2 == 0) {
                    ps.println("<tr bgcolor=\"#EEEEEE\">");
                } else {
                    ps.println("<tr bgcolor=\"#CCCCCC\">");
                }

                ps.println("<td align=center>" + prestation.getNumeroAffilie() + "</td>");
                ps.println("<td align=left>" + prestation.getDenomination1() + " " + prestation.getDenomination2()
                        + "</td>");
                ps.println("<td align=center>" + prestation.getIdRecap() + "</td>");
                ps.println("<td align=center>" + prestation.getNumeroFacture() + "</td>");
                ps.println("<td align=center>" + prestation.getPeriodeRecapDe().substring(4, 6) + "."
                        + prestation.getPeriodeRecapDe().substring(0, 4) + "</td>");
                ps.println("<td align=center>" + prestation.getPeriodeRecapA().substring(4, 6) + "."
                        + prestation.getPeriodeRecapA().substring(0, 4) + "</td>");
                ps.println("<td align=center>" + prestation.getChiffreStatistique() + "</td>");
                ps.println("<td align=center>" + prestation.getCategorieRubrique() + "</td>");
                ps.println("<td align=center>" + prestation.getMsgErreur() + "</td>");
                ps.println("<td align=center>"
                        + JANumberFormatter.fmt(prestation.getTotalMontant(), true, true, false, 2) + "</td>");
                ps.println("</tr>");

                totalError = totalError.add(new BigDecimal(prestation.getTotalMontant()));
            }
        }

        // dernière ligne: total final
        if (prestations.size() % 2 == 0) {
            ps.println("<tr bgcolor=\"#EEEEEE\">");
        } else {
            ps.println("<tr bgcolor=\"#CCCCCC\">");
        }

        ps.println("<td><b>Total</b></td>");
        for (int i = 0; i < 8; i++) {
            ps.println("<td>&nbsp;</td>");
        }
        ps.println("<td><b>" + JANumberFormatter.fmt(totalError.toString(), true, true, false, 2) + "</b></td>");
        ps.println("</tr>");

        // fin du tableau
        ps.println("</table><br>");

        // Vérifie que la somme des récaps AF corresponde à la somme passée dans MUSCA, si non on met la gen en erreur
        if (!JANumberFormatter.fmt(totalFacture, false, true, false, 2).equals(totalRecap.toString())) {
            ps.println("<H3>Attention : Le total des récaps facturées dans ALFA-Gest ne correspond pas au total des récaps facturées dans MUSCA !</H3><br>");
            ps.println("<H3>Annuler le passage de facturation<H3><br>");
            facturationOK = false;
        }
    }

    /*
     * Remplit l'arrayList décrivant les erreurs de génération des prestations
     */
    private void getErreurs() {
        // Retrouve les prestations en erreurs depuis la liste de facturation
        // prestation utilisée pour itérer
        ALPrestationPaiement prestation = null;
        errors = new ArrayList();

        for (int i = 0; i < prestations.size(); i++) {
            try {
                prestation = (ALPrestationPaiement) prestations.get(i);

                if (prestation.getEtatRecap().equals("ER")) {
                    errors.add(prestation);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /*
     * Retourne le type de paiement
     */
    private String getTypePaiement() {
        if (typePaiement == PROTOCOLE_PAIEMENTS_DIRECTS) {
            return "Protocole : paiements directs";
        } else if (typePaiement == PROTOCOLE_PAIEMENTS_INDIRECTS) {
            return "Protocole : compensation sur factures";
        }

        return "";
    }

    /*
     * Retourne la date
     */
    private String getDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        if (day.length() == 1) {
            day = "0" + day;
        }

        if (month.length() == 1) {
            month = "0" + month;
        }

        return day + "." + month + "." + year;
    }

    /*
     * Retourne l'heure
     */
    private String getTime() {
        GregorianCalendar calendar = new GregorianCalendar();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));

        if (hour.length() == 1) {
            hour = "0" + hour;
        }

        if (minute.length() == 1) {
            minute = "0" + minute;
        }

        return hour + ":" + minute;
    }
}