package globaz.osiris.db.ordres;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.osiris.parser.IntBVRFlatFileParser;
import globaz.osiris.parser.IntBVRPojo;
import java.io.BufferedReader;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 16:38:34)
 * 
 * @author: Administrator
 */
public final class CABVR3Parser implements IntBVRFlatFileParser {
    private String codeRejet = new String();
    private String currentBuffer = null;
    private String dateDepot = new String();
    private String dateInscription = new String();
    private String dateTraitement = new String();
    private boolean echoToConsole;
    private String genreEcriture = new String();
    private String genreTransaction = new String();
    private BufferedReader inputReader = null;
    private FWMemoryLog memoryLog;
    private String montant = new String();
    private String nombreTransactions = new String();
    private String numeroAdherent = new String();
    private int numeroLigne = 0;
    private String numeroReference = new String();
    private String numeroTransaction = new String();
    private String referenceInterne = new String();
    private BSession session = null;
    private String taxeTraitement = new String();
    private String taxeVersement = new String();
    private String typeTransaction = new String();
    private String codeMonnaie = new String();

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 17:08:43)
     * 
     * @return boolean
     */
    private boolean _isNumeric(String str) {
        if (str == null) {
            return false;
        } else {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDefined(str.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getCodeRejet() {
        return codeRejet;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2002 13:43:21)
     * 
     * @return String
     */
    @Override
    public String getCurrentBuffer() {
        return currentBuffer;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getDateDepot() {
        return dateDepot;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getDateInscription() {
        return dateInscription;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getDateTraitement() {
        return dateTraitement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 09:08:14)
     * 
     * @return boolean
     */
    @Override
    public boolean getEchoToConsole() {
        return echoToConsole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 09:28:43)
     * 
     * @return String
     */
    @Override
    public String getGenreEcriture() {
        return genreEcriture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getGenreTransaction() {
        return genreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 09:08:14)
     * 
     * @return java.io.BufferedReader
     */
    @Override
    public java.io.BufferedReader getInputReader() {
        return inputReader;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 09:08:14)
     * 
     * @return globaz.framework.util.FWMemoryLog
     */
    @Override
    public FWMemoryLog getMemoryLog() {
        if (memoryLog == null) {
            memoryLog = new FWMemoryLog();
        }
        memoryLog.setSession(getSession());
        return memoryLog;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getMontant() {
        return montant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getNombreTransactions() {
        return nombreTransactions;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getNumeroAdherent() {
        return numeroAdherent;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getNumeroReference() {
        return numeroReference;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:46:08)
     * 
     * @return String
     */
    @Override
    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getReferenceInterne() {
        return referenceInterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getTaxeTraitement() {
        return taxeTraitement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return String
     */
    @Override
    public String getTaxeVersement() {
        return taxeVersement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:43:54)
     * 
     * @return String
     */
    @Override
    public String getTypeTransaction() {
        return typeTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 16:31:51)
     * 
     * @return boolean
     */
    @Override
    public boolean parseNextElement() {

        // Initialiser
        String s = null;
        final String EMPTY = "";

        // Vider les zones cache
        codeRejet = EMPTY;
        dateDepot = EMPTY;
        dateInscription = EMPTY;
        dateTraitement = EMPTY;
        montant = EMPTY;
        nombreTransactions = EMPTY;
        numeroAdherent = EMPTY;
        referenceInterne = EMPTY;
        taxeTraitement = EMPTY;
        taxeVersement = EMPTY;
        genreEcriture = EMPTY;
        typeTransaction = EMPTY;
        numeroTransaction = EMPTY;

        // Vérifier que le stream est fourni
        if (getInputReader() == null) {
            getMemoryLog().logMessage("5302", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Lire la prochaine ligne
        try {
            currentBuffer = getInputReader().readLine();
            if (getEchoToConsole()) {
                System.out.println(currentBuffer);
            }
        } catch (Exception ex) {
            getMemoryLog().logMessage(ex.getMessage(), null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // S'il n'y a rien, on sort
        if (currentBuffer == null) {
            return false;
        }

        // Vérifier la longueur
        if ((currentBuffer.length() != 100) && (currentBuffer.length() != 126) && (currentBuffer.length() != 87)) {
            getMemoryLog().logMessage("5303", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Numéroter les transaction
        numeroTransaction = String.valueOf(++numeroLigne);

        // Récupérer le genre de transaction
        genreTransaction = currentBuffer.substring(0, 3);
        if (!_isNumeric(genreTransaction)) {
            getMemoryLog().logMessage("5304", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Récupérer le numéro d'adhérent
        numeroAdherent = currentBuffer.substring(3, 12);
        if (!_isNumeric(numeroAdherent)) {
            getMemoryLog().logMessage("5305", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Récupérer le numéro de référence
        numeroReference = currentBuffer.substring(12, 39);
        if (!_isNumeric(numeroReference)) {
            getMemoryLog().logMessage("5306", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // S'il s'agit d'un record de total
        if (genreTransaction.equals("999") || genreTransaction.equals("995")) {

            // Type de transaction
            typeTransaction = IntBVRFlatFileParser.FOOTER;

            // Récupérer le total
            montant = currentBuffer.substring(39, 49) + "." + currentBuffer.substring(49, 51);
            try {
                FWCurrency cMontant = new FWCurrency(montant);
                montant = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Déterminer le genre d'écriture
            if (genreTransaction.equals("999")) {
                genreEcriture = IntBVRPojo.GENRE_CREDIT;
            } else {
                genreEcriture = IntBVRPojo.GENRE_DEBIT;
            }

            // Nombre de transactions
            nombreTransactions = currentBuffer.substring(51, 63);
            if (!_isNumeric(nombreTransactions)) {
                getMemoryLog().logMessage("5310", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer la date d'établissement du support
            s = currentBuffer.substring(63, 69);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("5308", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateTraitement = s.substring(4) + "." + s.substring(2, 4) + "." + s.substring(0, 2);

            // Récupérer les taxes de versement
            taxeVersement = currentBuffer.substring(69, 76) + "." + currentBuffer.substring(76, 78);
            try {
                FWCurrency cTaxeVersement = new FWCurrency(taxeVersement);
                taxeVersement = cTaxeVersement.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Taxes ultérieures
            taxeTraitement = currentBuffer.substring(78, 85) + "." + currentBuffer.substring(85, 87);
            try {
                FWCurrency cTaxeTraitement = new FWCurrency(taxeTraitement);
                taxeTraitement = cTaxeTraitement.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Autres types de record

        } else {

            // Type de transaction
            typeTransaction = IntBVRFlatFileParser.TRANSACTION;

            // Récupérer le montant
            montant = currentBuffer.substring(39, 47) + "." + currentBuffer.substring(47, 49);
            try {
                FWCurrency cMontant = new FWCurrency(montant);
                montant = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Déterminer le genre d'écriture
            if (genreTransaction.endsWith("2")) {
                genreEcriture = IntBVRPojo.GENRE_CREDIT;
            } else if (genreTransaction.endsWith("1")) {
                genreEcriture = IntBVRPojo.GENRE_CREDIT;
            } else if (genreTransaction.endsWith("8")) {
                genreEcriture = IntBVRPojo.GENRE_CREDIT;
            } else if (genreTransaction.endsWith("5")) {
                genreEcriture = IntBVRPojo.GENRE_DEBIT;
            } else if (genreTransaction.endsWith("4")) {
                genreEcriture = IntBVRPojo.GENRE_DEBIT;
            } else {
                getMemoryLog().logMessage("5316", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer la référence interne
            referenceInterne = currentBuffer.substring(49, 59) + " " + currentBuffer.substring(77, 86);

            // Récupérer la date de dépôt
            s = currentBuffer.substring(59, 65);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("5312", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateDepot = s.substring(4) + "." + s.substring(2, 4) + ".20" + s.substring(0, 2);

            // Récupérer la date du traitement
            s = currentBuffer.substring(65, 71);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("5313", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateTraitement = s.substring(4) + "." + s.substring(2, 4) + ".20" + s.substring(0, 2);

            // Récupérer la date d'inscription
            s = currentBuffer.substring(71, 77);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("5314", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateInscription = s.substring(4) + "." + s.substring(2, 4) + ".20" + s.substring(0, 2);

            // Récupérer la taxe de versement
            taxeVersement = currentBuffer.substring(96, 98) + "." + currentBuffer.substring(98, 100);
            try {
                FWCurrency cTaxeVersement = new FWCurrency(taxeVersement);
                taxeVersement = cTaxeVersement.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5315", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer le code rejet
            codeRejet = currentBuffer.substring(86, 87);

        }

        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 09:08:14)
     * 
     * @param newEchoToConsole
     *            boolean
     */
    @Override
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 09:08:14)
     * 
     * @param newInputReader
     *            java.io.BufferedReader
     */
    @Override
    public void setInputReader(java.io.BufferedReader newInputReader) {
        inputReader = newInputReader;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 09:08:14)
     * 
     * @param newMemoryLog
     *            globaz.framework.util.FWMemoryLog
     */
    @Override
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.05.2003 13:21:10)
     * 
     * @return BSession
     */
    @Override
    public BSession getSession() {
        return session;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.05.2003 13:21:10)
     * 
     * @param newSession
     *            BSession
     */
    @Override
    public void setSession(BSession newSession) {
        session = newSession;
    }

    @Override
    public String getBankTransactionCode() {
        return null;
    }

    @Override
    public String getAccountServicerReference() {
        return null;
    }

    @Override
    public String getDebtor() {
        return null;
    }

    @Override
    public String getCodeMonnaie() {
        return codeMonnaie;
    }

    @Override
    public String getEndToEndId() {
        return null;
    }
}
