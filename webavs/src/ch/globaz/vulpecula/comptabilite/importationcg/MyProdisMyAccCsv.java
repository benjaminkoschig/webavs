package ch.globaz.vulpecula.comptabilite.importationcg;

import java.util.Map;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de mapping représentant une ligne CSV
 * 
 * @since WebBMS 0.6
 */
public class MyProdisMyAccCsv {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyProdisMyAccCsv.class);

    public static final String NULL = "#null#";
    public static final String CODE_DEBIT = "debit";
    public static final String CODE_CREDIT = "credit";

    public static final String COL_NO_COMPTE_CREDIT = "noCompteCredit";
    public static final String COL_NO_COMPTE_DEBIT = "noCompteDebit";
    public static final String COL_MONTANT_CHF = "montantCHF";
    public static final String COL_DEVISE = "devise";
    public static final String COL_MONTANT_DEVISE = "montantDevise";
    public static final String COL_LIBELLE = "libelle";
    public static final String COL_DATE_COMPTABILISATION = "dateComptabilisation";
    public static final String COL_DATE_VALEUR = "dateValeur";
    public static final String COL_PIECE_COMPTABLE = "pieceComptable";
    public static final String COL_ID_PARTENER_UNIQUE = "idPartenerUnique";
    public static final String COL_ID_ECRITURE = "idEcriture";
    public static final String COL_ID_SOCIETE = "idSociete";
    public static final String COL_EMETTEUR = "emetteur";
    public static final String COL_NO_ENREGISTREMENT = "noEnregistrement";
    public static final String COL_TYPE_ENREGISTREMENT = "typeEnregistrement";

    public static final String[] TITLES = { COL_TYPE_ENREGISTREMENT, COL_NO_ENREGISTREMENT, COL_EMETTEUR,
            COL_ID_SOCIETE, COL_ID_ECRITURE, COL_ID_PARTENER_UNIQUE, COL_PIECE_COMPTABLE, COL_DATE_VALEUR,
            COL_DATE_COMPTABILISATION, COL_LIBELLE, COL_MONTANT_DEVISE, COL_DEVISE, COL_MONTANT_CHF,
            COL_NO_COMPTE_DEBIT, COL_NO_COMPTE_CREDIT };

    private String typeEnregistrement;
    private String noEnregistrement;
    private String emetteur;
    private String idSociete;
    private String idGroupeEcriture;
    private String idPartenerUnique;
    private String piece;
    private String dateValeur;
    private String libelle;
    private String compteDebit;
    private String compteCredit;
    private String montant;

    /**
     * @return the typeEnregistrement
     */
    public String getTypeEnregistrement() {
        return typeEnregistrement;
    }

    /**
     * @return the noEnregistrement
     */
    public String getNoEnregistrement() {
        return noEnregistrement;
    }

    /**
     * @return the emetteur
     */
    public String getEmetteur() {
        return emetteur;
    }

    /**
     * @return the idSociete
     */
    public String getIdSociete() {
        return idSociete;
    }

    /**
     * @return the idGroupeEcriture
     */
    public String getIdGroupeEcriture() {
        return idGroupeEcriture;
    }

    /**
     * @return the idPartenerUnique
     */
    public String getIdPartenerUnique() {
        return idPartenerUnique;
    }

    /**
     * @return the piece
     */
    public String getPiece() {
        return piece;
    }

    /**
     * @return the dateValeur
     */
    public String getDateValeur() {
        return dateValeur;
    }

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @return the compteDebit
     */
    public String getCompteDebit() {
        return compteDebit;
    }

    /**
     * @return the compteCredit
     */
    public String getCompteCredit() {
        return compteCredit;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @param typeEnregistrement the typeEnregistrement to set
     */
    public void setTypeEnregistrement(String typeEnregistrement) {
        this.typeEnregistrement = typeEnregistrement;
    }

    /**
     * @param noEnregistrement the noEnregistrement to set
     */
    public void setNoEnregistrement(String noEnregistrement) {
        this.noEnregistrement = noEnregistrement;
    }

    /**
     * @param emetteur the emetteur to set
     */
    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    /**
     * @param idSociete the idSociete to set
     */
    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
    }

    /**
     * @param idGroupeEcriture the idGroupeEcriture to set
     */
    public void setIdGroupeEcriture(String idGroupeEcriture) {
        this.idGroupeEcriture = idGroupeEcriture;
    }

    /**
     * @param idPartenerUnique the idPartenerUnique to set
     */
    public void setIdPartenerUnique(String idPartenerUnique) {
        this.idPartenerUnique = idPartenerUnique;
    }

    /**
     * @param piece the piece to set
     */
    public void setPiece(String piece) {
        this.piece = piece;
    }

    /**
     * @param dateValeur the dateValeur to set
     */
    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    /**
     * @param libelle the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * @param compteDebit the compteDebit to set
     */
    public void setCompteDebit(String compteDebit) {
        this.compteDebit = compteDebit;
    }

    /**
     * @param compteCredit the compteCredit to set
     */
    public void setCompteCredit(String compteCredit) {
        this.compteCredit = compteCredit;
    }

    /**
     * @param montant the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * Détermine le bon code en fonction du compte renseigné dans le fichier source.
     * 
     * @return le code débit crédit
     */
    public String getCodeDebitCredit() {
        if (!MyProdisMyAccCsv.NULL.equals(getCompteDebit())) {
            return CODE_DEBIT;
        } else if (!MyProdisMyAccCsv.NULL.equals(getCompteCredit())) {
            return CODE_CREDIT;
        }

        LOGGER.error("Probleme ! Aucun compte renseigné, impossible de déterminer le codeDebitCredit");
        return "";
    }

    /**
     * @return le numéro du compte
     */
    public String getCompte() {
        if (!MyProdisMyAccCsv.NULL.equals(getCompteDebit())) {
            return getCompteDebit();
        } else if (!MyProdisMyAccCsv.NULL.equals(getCompteCredit())) {
            return getCompteCredit();
        }

        LOGGER.error("Probleme ! Aucun compte renseigné");
        return "";
    }

    /**
     * Va chercher la correspondance entre la société indiqué dans le fichier CSV et le mandat comptable dans un fichier
     * de mapping.
     * 
     * @return le mandat comptable
     */
    public static String getMandat(String mandatKey) {

        ResourceBundle bundle = ResourceBundle.getBundle("mandat");
        if (bundle.containsKey(mandatKey)) {
            return bundle.getString(mandatKey);
            // } else if (bundle.containsValue(mandatKey)) {
            // return getKeyFromValue(prop, mandatKey);
        }

        // String propFileName = "mandat.properties";
        // Properties prop = new Properties();
        //
        // InputStream inputStream =
        // Thread.currentThread().getClass().getClassLoader().getResourceAsStream(propFileName);
        // if (inputStream != null) {
        // try {
        // prop.load(inputStream);
        // } catch (IOException e) {
        // LOGGER.error("Error : impossible de charger le fichier de mapping des mandats.");
        // }
        // } else {
        // LOGGER.error("Error : property file " + propFileName + " not found in the classpath");
        // }
        //
        // if (prop.containsKey(mandatKey)) {
        // return prop.getProperty(mandatKey);
        // } else if (prop.containsValue(mandatKey)) {
        // return getKeyFromValue(prop, mandatKey);
        // }
        return "";
    }

    /**
     * @param hm
     * @param value
     * @return
     */
    public static String getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return (String) o;
            }
        }
        return "";
    }
}
