package globaz.prestation.acor;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import java.io.PrintWriter;
import java.util.Map;

/**
 * <p>
 * Classe de base permettant la création d'un fichier bat (exécutable sous windows) contenant des commandes qui vont
 * copier le contenu des fichiers ACOR au bon endroit sur le disque puis exécuter ACOR.
 * </p>
 * 
 * @author vre
 */
public abstract class PRAbstractACORBatchFilePrinter {

    public void printBatchFile(Map<String, PRAcorFileContent> fileContent, BSession session, PRACORAdapter adapter,
            String dossierRacineAcor) throws PRACORException {

        // impression des fichiers
        boolean premiereLigne;

        for (PRFichierACORPrinter fichier : adapter.getFichiersACOR()) {

            premiereLigne = true;
            PRAcorFileContent fc = null;

            if (fileContent.containsKey(fichier.getNomFichier())) {
                fc = fileContent.get(fichier.getNomFichier());
            } else {
                fc = new PRAcorFileContent();
                fc.setFileName(fichier.getNomFichier());
                fileContent.put(fc.getFileName(), fc);
            }

            if (fichier.hasLignes()) {
                do {
                    printEchoFichier(fc, fichier, dossierRacineAcor, fichier.getNomFichier(), premiereLigne);
                    premiereLigne = false;
                } while (fichier.hasLignes());
            } else if (fichier.isForcerFichierVide()) {

                this.printFichierVide(fc, dossierRacineAcor, fichier.getNomFichier());
            }
            fichier.dispose();
        }
    }

    /**
     * Inscrit une commande permettant d'inscrire une ligne dans un fichier nommé.
     * <p>
     * Cette méthode inscrit simplement "ECHO ligne > nomFichier"
     * </p>
     * 
     * @param shellCmds
     *            les commandes shell a executer
     * @param printer
     *            le fichier dont on veut écrire une ligne
     * @param dossierRacineAcor
     *            le chemin complet vers la racine du dossier ACOR.
     * @param nomFichier
     *            le nom du fichier dans lequel écrire cette ligne
     * @param premiereLigne
     *            DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected void printEchoFichier(PRAcorFileContent fileContent, PRFichierACORPrinter printer,
            String dossierRacineAcor, String nomFichier, boolean premiereLigne) throws PRACORException {

        StringBuffer cmd = new StringBuffer();
        printer.printLigne(cmd);
        fileContent.addLine(cmd.toString());
    }

    /**
     * inscrit une commande qui efface les anciens fichiers contenus dans le dossier ACOR.
     * 
     * @param writer
     *            le writer
     * @param dossierRacineAcor
     *            le chemin complet vers la racine du dossier ACOR.
     */
    protected void printEffacerAnciensfichiers(PrintWriter writer, String dossierRacineAcor) {
        writer.println();
        writer.print("del \"");
        writer.print(dossierRacineAcor);
        writer.print(PRACORConst.DOSSIER_IN_HOST);
        writer.println("*\" /q /f");
        writer.println();
    }

    /**
     * Inscrit l'en-tete du fichier bat.
     * 
     * <p>
     * Cette méthode inscrit une petite en-tete contenant le no AVS de l'assuré ayant fait la demande de prestation.
     * </p>
     * 
     * @param writer
     *            le writer
     * @param adapter
     *            l'adapter permettant de récupérer le no AVS de l'assuré.
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected void printEnTete(PrintWriter writer, PRACORAdapter adapter) throws PRACORException {
        writer.println();
        writer.println("@ECHO OFF"); // désactive l'affichage des commandes
        // executées par ce script à l'écran
        writer.println();
        this.printRemarqueln(writer, "-[GLOBAZ SA]--------------------------------------------------------------------");
        this.printRemarqueln(writer, "script de préparation d'ACOR");
        this.printRemarqueln(writer, "prestation pour assuré: " + adapter.numeroAVSAssure());
        this.printRemarqueln(writer, "-------------------------------------------------------------------["
                + JACalendar.todayJJsMMsAAAA() + "]-");
    }

    protected void printFichierVide(PRAcorFileContent fc, String dossierRacineAcor, String nomFichier) {

        fc.addLine(".");
    }

    /**
     * inscrit un fichier vide de tout contenu.
     * 
     * @param writer
     *            DOCUMENT ME!
     * @param dossierRacineAcor
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    protected void printFichierVide(PrintWriter writer, String dossierRacineAcor, String nomFichier) {
        writer.write("ECHO. > \"");
        writer.print(dossierRacineAcor);
        writer.print(PRACORConst.DOSSIER_IN_HOST);
        writer.print(nomFichier);
        writer.println("\"");
    }

    /**
     * @param writer
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    protected void printNomFichierRemarque(PrintWriter writer, String nomFichier) {
        writer.println();
        this.printRemarqueln(writer, nomFichier);
    }

    /**
     * inscrit une commande 'pause', utile pour le debuggage.
     * 
     * @param writer
     *            DOCUMENT ME!
     */
    protected void printPause(PrintWriter writer) {
        writer.println();
        writer.println("PAUSE");
    }

    /**
     * Inscrit le pied de page.
     * 
     * <p>
     * Cette méthode inscrit la commande permettant de lancer l'application ACOR.
     * </p>
     * 
     * @param writer
     *            le writer
     * @param dossierRacineACOR
     *            le chemin complet vers la racine du dossier ACOR.
     */
    protected void printPiedDePage(PrintWriter writer, String dossierRacineACOR) {
        writer.println();
        writer.print("\"");
        writer.print(dossierRacineACOR);
        writer.print(PRACORConst.EXECUTABLE_ACOR);
        writer.println("\"");
    }

    /**
     * Inscrit une remarque (commentaire) dans le fichier bat.
     * 
     * <p>
     * Cette méthode préfixe simplement le message avec la chaine 'REM ';
     * </p>
     * 
     * @param writer
     *            le writer
     * @param message
     *            le message à inscrire en remarque
     */
    protected void printRemarqueln(PrintWriter writer, String message) {
        writer.print("REM ");
        writer.println(message);
    }

    protected void printRemarqueln(StringBuffer cmd, String message) {
        cmd.append("REM ");
        cmd.append(message);
        cmd.append("\n");
    }
}
