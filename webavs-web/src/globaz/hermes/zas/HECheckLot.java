package globaz.hermes.zas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class HECheckLot {
    public static final String ENCODING = "Cp037";

    /**
     * Insérez la description de la méthode ici. Date de création : (12.05.2003 13:54:49)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        try {
            new HECheckLot().readFile(args[0]);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private char[] fileBuffer = new char[120];
    boolean hasCarriageReturns = false;
    /** ***** a modifier si changement */
    // /////////
    boolean isFileEBCDIC = true;
    /** ****************************************** */

    boolean isLotOuvert = false;
    boolean isPremiereAnnonceFichier = false;

    int nbreAnnonce = 0;

    private boolean isRecord(String line) throws Exception {

        if (line.startsWith("01")) {
            // jj
            StringBuffer date = new StringBuffer(line.substring(26, 28));
            // mois
            date.append(line.substring(28, 30));
            // annee
            date.append("20");
            date.append(line.substring(30, 32));
            isLotOuvert = true;
            nbreAnnonce = 0;
            System.out.println(date);

        }

        if (line.startsWith("99")) {

            // contrôle si le nombre d'annonce traitée est
            // égal au nombre d'annonce spéicifié dans la trame 99
            String nbreAnnonceSpec = line.substring(32, 38);
            int nbreSpec = Integer.parseInt(nbreAnnonceSpec);

            // ancienne facon de faire posant problème lorsque l'on reçoit deux
            // lots dans un meme fichier
            // et que sur un lot ce sont des 52 (par ex) aucune annonce ajoutee
            // c'est-à-dire que pas besoin de contrôler le lot
            // HEOutputAnnonceListViewBean listeAjoutee = new
            // HEOutputAnnonceListViewBean();
            // listeAjoutee.setSession(getSession());
            // listeAjoutee.setForIdLot(lot.getIdLot());
            // int nbreAjout = listeAjoutee.getCount(getTransaction());
            if ((nbreAnnonce != 0) && (nbreSpec != nbreAnnonce)) {
                throw new Exception(
                        "Securite : le nombre d'annonces ajoutees ne correspond pas au nombre mentionne dans la trame 99 : "
                                + nbreSpec + " vs " + nbreAnnonce);
            }

            if (isLotOuvert) {
                isLotOuvert = false;
            } else {
                throw new Exception("Securite : le lot ne commence pas par une trame 01 !!!");
            }

            // jj
            StringBuffer date99 = new StringBuffer(line.substring(26, 28));
            // mois
            date99.append(line.substring(28, 30));
            // AAAA
            date99.append("20");
            date99.append(line.substring(30, 32));
            System.out.println(date99);
            System.out.println("lot de " + nbreAnnonce + " contrôlé !");
        }

        return ((line.trim().length() != 0) && !line.startsWith("01") && !line.startsWith("99") && (line
                .startsWith("11")
                || line.startsWith("20")
                || line.startsWith("21")
                || line.startsWith("22")
                || line.startsWith("23")
                || line.startsWith("24")
                || line.startsWith("25")
                || line.startsWith("29")
                || line.startsWith("38") || line.startsWith("39") || line.startsWith("52")));
    }

    /**
     * Method ajoutAnnonces.
     * 
     * @param file
     * @param typeLot
     * @throws Exception
     */
    /**
     * ???
     */
    public void readFile(String file) throws Exception {

        File sourceFile = new File(file);
        BufferedReader r;
        String line;
        isPremiereAnnonceFichier = true;

        if (isFileEBCDIC) {

            // je lis de l'EBCDIC
            r = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), ENCODING));
        } else {
            r = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
        }

        // l'annonce à ajouter
        while ((line = readLine(r, hasCarriageReturns)) != null) {

            // vérifie que ça commence pas par 01 ou 99
            if (isPremiereAnnonceFichier) {

                if (!line.startsWith("01")) {
                    throw new Exception("Securite : Le début du fichier ne commence pas par une trame 01");
                }

                isPremiereAnnonceFichier = false;
            }

            if (isRecord(line)) {

                nbreAnnonce++;
            }
        } // ??

        // contrôle que le(s) lots soit bien cadré par des trames 01-99
        if (isLotOuvert) {
            throw new Exception("Securite : il n'y a pas de trame pour délimiter le dernier lot !!!");
        }

        r.close();
        System.gc();
    }

    private String readLine(BufferedReader file, boolean hasCarridgeReturn) throws Exception {

        if (hasCarridgeReturn) {
            return file.readLine();
        } else {
            int nread = 0;

            if ((nread = file.read(fileBuffer)) >= 0) {
                return String.valueOf(fileBuffer, 0, nread);
            } else {
                return null;
            }
        }
    }

}
