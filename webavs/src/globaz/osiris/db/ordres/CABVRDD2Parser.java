/*
 * Créé le 6 sept. 05
 */
package globaz.osiris.db.ordres;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.osiris.parser.IntBVRDDType2Parser;
import java.io.BufferedReader;

/**
 * @author jts 6 sept. 05 12:27:15 CABVRDD2Parser (BVRDebitDirectType2Parser)
 */
public class CABVRDD2Parser implements IntBVRDDType2Parser {
    private String cleTri;
    private String codeMonnaieISO;
    private String codeMonnaieISOPrix;
    private String codeRefus;
    private String currentBuffer = null;
    private String dateEcheance;

    private String dateEcriture;
    private String dateEtablissementSupport;
    private String dateValeur;
    private boolean echoToConsole;
    private String genreLivraison;
    private String genreTransaction;
    private BufferedReader inputReader = null;
    private FWMemoryLog memoryLog;
    private String montant;
    private String montantContrePassation;
    private String montantRefus;
    private String nbrTransactionsContrePassation;
    private String nbrTransactionsEcrituresCredit;
    private String nbrTransactionsRefus;
    private String numCompteDeb;
    private String numCourantTransaction;

    private String numeroAdherent;
    private int numeroLigne = 0;
    private String numeroReference;
    private String numOrdreDDAdherent;
    private String prix;
    private String referenceInterne;
    private BSession session = null;
    private String totalPrix;

    /**
     * Vérifie si la chaîne passée en paramettre est un nombre
     * 
     * @param str
     *            chaîne à vérifier
     * @return true si str est un nombre, false sinon
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getCleTri()
     */
    @Override
    public String getCleTri() {
        return cleTri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getCodeMonnaieISO()
     */
    @Override
    public String getCodeMonnaieISO() {
        return codeMonnaieISO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getCodeMonnaieISOPrix()
     */
    @Override
    public String getCodeMonnaieISOPrix() {
        return codeMonnaieISOPrix;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getCodeRefus()
     */
    @Override
    public String getCodeRefus() {
        return codeRefus;
    }

    /**
     * @return
     */
    @Override
    public String getCurrentBuffer() {
        return currentBuffer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getDateEcheance()
     */
    @Override
    public String getDateEcheance() {
        return dateEcheance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getDateEcriture()
     */
    @Override
    public String getDateEcriture() {
        return dateEcriture;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getDateEtablissementSupport()
     */
    @Override
    public String getDateEtablissementSupport() {
        return dateEtablissementSupport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getDateValeur()
     */
    @Override
    public String getDateValeur() {
        return dateValeur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getEchoToConsole()
     */
    @Override
    public boolean getEchoToConsole() {
        return echoToConsole;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getGenreLivraison()
     */
    @Override
    public String getGenreLivraison() {
        return genreLivraison;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getGenreTransaction()
     */
    @Override
    public String getGenreTransaction() {
        return genreTransaction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getInputReader()
     */
    @Override
    public BufferedReader getInputReader() {
        return inputReader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getMemoryLog()
     */
    @Override
    public FWMemoryLog getMemoryLog() {
        if (memoryLog == null) {
            memoryLog = new FWMemoryLog();
        }
        memoryLog.setSession(getSession());
        return memoryLog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getMontant()
     */
    @Override
    public String getMontant() {
        return montant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getMontantContrePassation()
     */
    @Override
    public String getMontantContrePassation() {
        return montantContrePassation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getMontantRefus()
     */
    @Override
    public String getMontantRefus() {
        return montantRefus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNbrTransactionsContrePassation ()
     */
    @Override
    public String getNbrTransactionsContrePassation() {
        return nbrTransactionsContrePassation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNbrTransactions()
     */
    @Override
    public String getNbrTransactionsEcrituresCredit() {
        return nbrTransactionsEcrituresCredit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNbrTransactionsRefus()
     */
    @Override
    public String getNbrTransactionsRefus() {
        return nbrTransactionsRefus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNumCompteDeb()
     */
    @Override
    public String getNumCompteDeb() {
        return numCompteDeb;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNumCourantTransaction()
     */
    @Override
    public String getNumCourantTransaction() {
        return numCourantTransaction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNumeroAdherent()
     */
    @Override
    public String getNumeroAdherent() {
        return numeroAdherent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNumeroReference()
     */
    @Override
    public String getNumeroReference() {
        return numeroReference;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getNumOrdreDDAdherent()
     */
    @Override
    public String getNumOrdreDDAdherent() {
        return numOrdreDDAdherent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getPrix()
     */
    @Override
    public String getPrix() {
        return prix;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getReferenceInterne()
     */
    @Override
    public String getReferenceInterne() {
        return referenceInterne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getSession()
     */
    @Override
    public BSession getSession() {
        return session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#getTotalPrix()
     */
    @Override
    public String getTotalPrix() {
        return totalPrix;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#parseNextElement()
     */
    @Override
    public boolean parseNextElement() {
        // Initialiser
        String s = null;
        final String EMPTY = "";

        // Vider les zones cache
        genreTransaction = EMPTY;
        genreLivraison = EMPTY;
        numeroAdherent = EMPTY;
        numeroReference = EMPTY;
        codeMonnaieISO = EMPTY;
        montant = EMPTY;
        referenceInterne = EMPTY;
        dateEcheance = EMPTY;
        dateValeur = EMPTY;
        dateEcriture = EMPTY;
        numCompteDeb = EMPTY;
        codeRefus = EMPTY;
        numOrdreDDAdherent = EMPTY;
        numCourantTransaction = EMPTY;
        codeMonnaieISOPrix = EMPTY;
        prix = EMPTY;

        totalPrix = EMPTY;
        nbrTransactionsContrePassation = EMPTY;
        montantContrePassation = EMPTY;
        nbrTransactionsRefus = EMPTY;
        montantRefus = EMPTY;
        nbrTransactionsEcrituresCredit = EMPTY;
        cleTri = EMPTY;
        dateEtablissementSupport = EMPTY;

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
        if (currentBuffer.length() != 200) {
            getMemoryLog().logMessage("5303", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Numéroter les transaction
        numCourantTransaction = String.valueOf(++numeroLigne);

        // Récupérer le genre de transaction
        genreTransaction = currentBuffer.substring(0, 3);
        if (!_isNumeric(genreTransaction)) {
            getMemoryLog().logMessage("5304", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Récupérer le genre de livraison
        genreLivraison = currentBuffer.substring(5, 6);
        if (!_isNumeric(genreLivraison)) {
            getMemoryLog().logMessage("7386", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Récupérer le numéro d'adhérent
        numeroAdherent = currentBuffer.substring(6, 12);
        if (!_isNumeric(numeroAdherent)) {
            getMemoryLog().logMessage("5305", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Récupérer le code ISO de la monnaie
        codeMonnaieISO = currentBuffer.substring(42, 45);

        // Récupérer le montant
        montant = currentBuffer.substring(45, 55) + "." + currentBuffer.substring(55, 57);
        try {
            FWCurrency cMontant = new FWCurrency(montant);
            montant = cMontant.toString();
        } catch (Exception e) {
            getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Si enregistrement total
        if (genreTransaction.equals("097")) {
            // Récupération du nombre de transactions écriture au crédit
            nbrTransactionsEcrituresCredit = currentBuffer.substring(57, 69);
            if (!_isNumeric(nbrTransactionsEcrituresCredit)) {
                getMemoryLog().logMessage("7387", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer la date d'établissement du support de données
            s = currentBuffer.substring(92, 100);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("7388", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateEtablissementSupport = s.substring(6) + "." + s.substring(4, 6) + "." + s.substring(0, 4);

            // Montant refus
            montantRefus = currentBuffer.substring(77, 87) + "." + currentBuffer.substring(87, 89);
            try {
                FWCurrency cMontant = new FWCurrency(montantRefus);
                montantRefus = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // nombre de transaction refus
            nbrTransactionsRefus = currentBuffer.substring(89, 101);
            if (!_isNumeric(nbrTransactionsRefus)) {
                getMemoryLog().logMessage("5310", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Montant contre-passation
            montantContrePassation = currentBuffer.substring(101, 111) + "." + currentBuffer.substring(111, 113);
            try {
                FWCurrency cMontant = new FWCurrency(montantContrePassation);
                montantContrePassation = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // nombre de contre-passation
            nbrTransactionsContrePassation = currentBuffer.substring(0, 3);
            if (!_isNumeric(nbrTransactionsContrePassation)) {
                getMemoryLog().logMessage("5310", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer le code ISO du prix
            codeMonnaieISOPrix = currentBuffer.substring(128, 131);

            // Récupérer le Total prix
            totalPrix = currentBuffer.substring(131, 141) + "." + currentBuffer.substring(141, 142);
            try {
                FWCurrency cMontant = new FWCurrency(totalPrix);
                totalPrix = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("7391", null, FWMessage.ERREUR, this.getClass().getName());
            }

        } else {
            // Récupérer le numéro de référence
            numeroReference = currentBuffer.substring(15, 42);
            if (!_isNumeric(numeroReference)) {
                getMemoryLog().logMessage("5306", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer la référence interne
            referenceInterne = currentBuffer.substring(57, 92);

            // Récupérer la date d'échéance
            s = currentBuffer.substring(92, 100);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("5313", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateEcheance = s.substring(6) + "." + s.substring(4, 6) + "." + s.substring(0, 4);

            // Récupérer la date valeur
            s = currentBuffer.substring(100, 108);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("5312", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateValeur = s.substring(6) + "." + s.substring(4, 6) + "." + s.substring(0, 4);

            // Récupérer la date d'écriture
            s = currentBuffer.substring(108, 116);
            if (!_isNumeric(s)) {
                getMemoryLog().logMessage("5314", null, FWMessage.ERREUR, this.getClass().getName());
            }
            dateEcriture = s.substring(6) + "." + s.substring(4, 6) + "." + s.substring(0, 4);

            // Récupérer le numéro de compte du débiteur
            numCompteDeb = currentBuffer.substring(119, 128);
            if (!_isNumeric(numCompteDeb)) {
                getMemoryLog().logMessage("7390", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer le code de refus
            codeRefus = currentBuffer.substring(128, 130);
            if (codeRefus.equalsIgnoreCase("01")) {
                getMemoryLog().logMessage("5270", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (codeRefus.equalsIgnoreCase("02")) {
                getMemoryLog().logMessage("5276", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (codeRefus.equalsIgnoreCase("03")) {
                getMemoryLog().logMessage("5271", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (codeRefus.equalsIgnoreCase("04")) {
                getMemoryLog().logMessage("5272", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (codeRefus.equalsIgnoreCase("05")) {
                getMemoryLog().logMessage("5273", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (codeRefus.equalsIgnoreCase("06")) {
                getMemoryLog().logMessage("5274", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (codeRefus.equalsIgnoreCase("07")) {
                getMemoryLog().logMessage("5275", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer le numéro d'ordre DD de l'adhérent
            numOrdreDDAdherent = currentBuffer.substring(130, 132);
            if (!_isNumeric(numOrdreDDAdherent)) {
                getMemoryLog().logMessage("5281", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Récupérer le prix
            prix = currentBuffer.substring(141, 145) + "." + currentBuffer.substring(145, 147);
            try {
                FWCurrency cPrix = new FWCurrency(prix);
                prix = cPrix.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("7391", null, FWMessage.ERREUR, this.getClass().getName());
            }
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#setEchoToConsole(boolean)
     */
    @Override
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.osiris.db.ordres.IntBVRDDType2Parser#setInputReader(java.io. BufferedReader)
     */
    @Override
    public void setInputReader(BufferedReader newInputReader) {
        inputReader = newInputReader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#setMemoryLog(globaz.framework .util.FWMemoryLog)
     */
    @Override
    public void setMemoryLog(FWMemoryLog newMemoryLog) {
        memoryLog = newMemoryLog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.ordres.IntBVRDDType2Parser#setSession(globaz.globall .db.BSession)
     */
    @Override
    public void setSession(BSession newSession) {
        session = newSession;
    }

}
