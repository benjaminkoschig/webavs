package ch.globaz.vulpecula.comptabilite.importationcg;

import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.comptabilite.importationcg.csv.CsvFile;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Codedebitcredit;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Date;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Dateentete;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Ecriture;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.EcritureCollective;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.EcritureDouble;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Idexternecompte;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Idexternecomptecredite;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Idexternecomptedebite;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.ImportEcritures;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Libelle;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Libelleentete;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Mandat;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Montant;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.ObjectFactory;
import ch.globaz.vulpecula.comptabilite.importationcg.xmlobject.Pieceentete;

/**
 * Converti les données CSV pour générer un fichier XML compatible pour l'import d'écritures Helios
 * 
 * @since WebBMS 0.5
 */
public class Csv2JaxbBuilder {
    private static final String JDBC = "jdbc://";
    private static final String WORK_DIR = "work/";
    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String DATE_PATTERN_SWISS = "dd.MM.yyyy";
    private final static Logger LOGGER = LoggerFactory.getLogger(Csv2JaxbBuilder.class);

    /**
     * @param csvfilePath
     * @return
     */
    public static List<MyProdisMyAccCsv> readCsv(String csvFilename) {
        String filepath = Jade.getInstance().getHomeDir() + Csv2JaxbBuilder.WORK_DIR + csvFilename;

        // Lecture
        try {
            String uriFile = Csv2JaxbBuilder.JDBC + Jade.getInstance().getDefaultJdbcSchema() + "/" + csvFilename;

            JadeFsFacade.copyFile(uriFile, filepath);
            JadeFsFacade.delete(uriFile);
        } catch (JadeServiceLocatorException e) {
            LOGGER.error(e.toString());
        } catch (JadeServiceActivatorException e) {
            LOGGER.error(e.toString());
        } catch (NullPointerException e) {
            LOGGER.error(e.toString());
        } catch (ClassCastException e) {
            LOGGER.error(e.toString());
        } catch (JadeClassCastException e) {
            LOGGER.error(e.toString());
        }

        CsvFile csv = new CsvFile(filepath);
        List<Map<String, String>> mappedData = csv.getMappedData(MyProdisMyAccCsv.TITLES);
        List<MyProdisMyAccCsv> listeLines = new ArrayList<MyProdisMyAccCsv>();

        for (Map<String, String> map : mappedData) {
            MyProdisMyAccCsv line = new MyProdisMyAccCsv();
            line.setTypeEnregistrement(map.get(MyProdisMyAccCsv.COL_TYPE_ENREGISTREMENT));
            line.setNoEnregistrement(map.get(MyProdisMyAccCsv.COL_NO_ENREGISTREMENT));
            line.setEmetteur(map.get(MyProdisMyAccCsv.COL_EMETTEUR));
            line.setIdSociete(map.get(MyProdisMyAccCsv.COL_ID_SOCIETE));
            line.setIdGroupeEcriture(map.get(MyProdisMyAccCsv.COL_ID_ECRITURE));
            line.setIdPartenerUnique(map.get(MyProdisMyAccCsv.COL_ID_PARTENER_UNIQUE));
            line.setPiece(formatPiece(map.get(MyProdisMyAccCsv.COL_PIECE_COMPTABLE)));
            line.setDateValeur(formatDate(map.get(MyProdisMyAccCsv.COL_DATE_VALEUR)));
            line.setLibelle(formatLibelle(map.get(MyProdisMyAccCsv.COL_LIBELLE)));
            line.setCompteDebit(map.get(MyProdisMyAccCsv.COL_NO_COMPTE_DEBIT));
            line.setCompteCredit(map.get(MyProdisMyAccCsv.COL_NO_COMPTE_CREDIT));
            line.setMontant(formatMontant(map.get(MyProdisMyAccCsv.COL_MONTANT_CHF)));

            listeLines.add(line);
        }

        return listeLines;
    }

    /**
     * @param map
     * @return la date formatée
     */
    private static String formatDate(String dateValue) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        SimpleDateFormat dateFormatSwiss = new SimpleDateFormat(DATE_PATTERN_SWISS);

        if (dateValue == null || "".equals(dateValue)) {
            throw new IllegalArgumentException("La date ne peut être null");
        }
        java.util.Date date = new java.util.Date();
        try {
            date = dateFormat.parse(dateValue);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format(
                    "La date '%s' doit respecter un des formats suivants : yyyyMMdd ; dd.MM.yyyy ; MM.yyyy ; yyyyMM",
                    date));
        }

        return dateFormatSwiss.format(date);
    }

    /**
     * Construit la structure xml en fonction des données csv
     * 
     * @param listeLines
     * @param libelleJournal
     * @param dateValeurJournal
     */
    public static ImportEcritures buildXml(List<MyProdisMyAccCsv> listeLines, String libelleJournal,
            String dateValeurJournal) {
        ImportEcritures importEcriture = ObjectFactory.createImportEcritures();
        if (libelleJournal != null && libelleJournal.length() > 0) {
            importEcriture.setLibelleJournal(libelleJournal);
        }
        if (dateValeurJournal != null && dateValeurJournal.length() > 0) {
            importEcriture.setDateValeur(dateValeurJournal);
        }

        EcritureCollective ecritureCollective = null;
        for (MyProdisMyAccCsv myProdisMyAccCsv : listeLines) {
            if (ecritureCollective == null) {
                ecritureCollective = buildEcritureCollective(myProdisMyAccCsv);
            } else if (!ecritureCollective.getId().equals(myProdisMyAccCsv.getIdGroupeEcriture())) {
                if (ecritureCollective.size() < 2) {
                    LOGGER.error("Problème : nb d'ecriture collective < 2");
                }
                importEcriture.addEcritureCollective(ecritureCollective);
                ecritureCollective = buildEcritureCollective(myProdisMyAccCsv);
            }

            ecritureCollective.addEcriture(buildEcriture(myProdisMyAccCsv.getLibelle(), myProdisMyAccCsv.getCompte(),
                    myProdisMyAccCsv.getCodeDebitCredit(), myProdisMyAccCsv.getMontant()));
        }
        importEcriture.addEcritureCollective(ecritureCollective);
        return importEcriture;
    }

    /**
     * Ecrit le fichier sur le disque
     * 
     * @param importEcriture
     * @param filePathDestination
     */
    public static void createXmlFile(ImportEcritures importEcriture, String filePathDestination) {
        // Create file
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ImportEcritures.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Dans un fichier
            File file = new File(filePathDestination);
            jaxbMarshaller.marshal(importEcriture, file);
            // Dans la console pour test
            // jaxbMarshaller.marshal(importEcriture, System.out);
        } catch (JAXBException e) {
            LOGGER.error("Error : impossible de créer le fichier xml.");
        }
    }

    /**
     * 
     */
    private static Ecriture buildEcriture(String libelle, String idExterneCompte, String codeDebitCredit, String montant) {
        Ecriture ecriture = new Ecriture();
        ecriture.setLibelle(new Libelle(libelle));
        ecriture.setIdexternecompte(new Idexternecompte(idExterneCompte));
        ecriture.setCodedebitcredit(new Codedebitcredit(codeDebitCredit));
        ecriture.setMontant(new Montant(montant));

        return ecriture;
    }

    /**
     * @return
     */
    private static EcritureCollective buildEcritureCollectiveWithData(String id, String dateEntete, String libelle,
            String piece, String mandat, List<Ecriture> listEcritures) {
        EcritureCollective ecritureCollective = buildEcritureCollective(id, dateEntete, libelle, piece, mandat);

        for (Ecriture ecriture : listEcritures) {
            ecritureCollective.addEcriture(ecriture);
        }

        return ecritureCollective;
    }

    /**
     * @param id
     * @param dateEntete
     * @param libelle
     * @param piece
     * @param mandat
     * @return
     */
    private static EcritureCollective buildEcritureCollective(String id, String dateEntete, String libelle,
            String piece, String mandat) {
        EcritureCollective ecritureCollective = new EcritureCollective();
        ecritureCollective.setId(id);
        ecritureCollective.setDateentete(new Dateentete(dateEntete));
        ecritureCollective.setLibelleentete(new Libelleentete(libelle));
        ecritureCollective.setPieceentete(new Pieceentete(piece));
        ecritureCollective.setMandat(new Mandat(mandat));
        return ecritureCollective;
    }

    private static EcritureCollective buildEcritureCollective(MyProdisMyAccCsv myProdisMyAccCsv) {
        EcritureCollective ecritureCollective = new EcritureCollective();
        ecritureCollective.setId(myProdisMyAccCsv.getIdGroupeEcriture());
        ecritureCollective.setDateentete(new Dateentete(myProdisMyAccCsv.getDateValeur()));
        ecritureCollective.setLibelleentete(new Libelleentete(myProdisMyAccCsv.getLibelle()));
        ecritureCollective.setPieceentete(new Pieceentete(myProdisMyAccCsv.getPiece()));
        ecritureCollective.setMandat(new Mandat(myProdisMyAccCsv.getIdSociete()));
        return ecritureCollective;
    }

    /**
     * @return
     */
    private static EcritureDouble buildEcritureDouble(String id, String date, String libelle, String compteDebit,
            String compteCredit, String montant) {
        EcritureDouble ecritureDouble = ObjectFactory.createDouble();
        ecritureDouble.setId(id);
        ecritureDouble.setDate(new Date(date));
        ecritureDouble.setLibelle(new Libelle(libelle));
        ecritureDouble.setIdexternecomptedebite(new Idexternecomptedebite(compteDebit));
        ecritureDouble.setIdexternecomptecredite(new Idexternecomptecredite(compteCredit));
        ecritureDouble.setMontant(new Montant(montant));
        return ecritureDouble;
    }

    /**
     * @param map
     * @return
     */
    private static String formatLibelle(String libelle) {
        if (libelle.length() > 40) {
            return libelle.substring(0, 40);
        }
        return libelle;
    }

    /**
     * @param map
     * @return
     */
    private static String formatPiece(String piece) {
        if (piece.length() > 10) {
            // TODO return warning
            return piece.substring(0, 4) + piece.substring(piece.length() - 6, piece.length());
        }
        return piece;
    }

    /**
     * @param map
     * @return
     */
    private static String formatMontant(String montant) {
        if (montant == null || montant.isEmpty()) {
            return "0.00";
        }

        String newValue = null;
        if (montant.indexOf('\'') != -1) {
            newValue = montant.replaceAll("\'", "");
        } else {
            newValue = montant;
        }

        if (!isNumeric(newValue)) {
            throw new IllegalArgumentException("Le montant doit être un double");
        }

        BigDecimal big = new BigDecimal(newValue);

        return big.abs().setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * Contrôle si la chaîne de caractères est de type numérique
     * 
     * @param value Une valeur
     * @return true si la valeur est numeric
     */
    public static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
