package globaz.osiris.db.ordres;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.osiris.parser.IntLSVParser;
import java.io.BufferedReader;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 16:38:34)
 * 
 * @author: Administrator
 */
public final class CALSVParser implements IntLSVParser {
    private java.lang.String codeRefus = new String();
    private java.lang.String communication1 = new String();
    private java.lang.String communication2 = new String();
    private java.lang.String communication3 = new String();
    private java.lang.String communication4 = new String();
    private java.lang.String communication5 = new String();
    private java.lang.String currentBuffer = null;
    private java.lang.String dateEcheance = new String();
    private java.lang.String designationSuppDebiteur = new String();
    private java.lang.String designationSuppDonneurOrdre = new String();
    private boolean echoToConsole;
    private java.lang.String elementEcritureCreditCode = new String();
    private java.lang.String elementEcritureCreditMontantTotal = new String();
    private java.lang.String elementEcritureCreditNbreTransaction = new String();
    private java.lang.String elementEcritureRefusCode = new String();
    private java.lang.String elementEcritureRefusMontantTotal = new String();
    private java.lang.String elementEcritureRefusNbreTransaction = new String();
    private String genreTransaction = new String();
    private java.lang.String identificateurFichier = new String();
    private BufferedReader inputReader = null;
    private java.lang.String lieuDebiteur = new String();
    private java.lang.String lieuDonneurOrdre = new String();
    private FWMemoryLog memoryLog;
    private String montant = new String();
    private java.lang.String montantTotalPrix = new String();
    private String nombreTransactions = new String();
    private java.lang.String nomDebiteur = new String();
    private java.lang.String nomDonneurOrdre = new String();
    private java.lang.String NPADebiteur = new String();
    private java.lang.String NPADonneurOrdre = new String();
    private String numeroAdherent = new String();
    private java.lang.String numeroComptePostalDebiteur = new String();
    private int numeroLigne = 0;
    private java.lang.String numeroOrdre = new String();
    private String numeroReference = new String();
    private java.lang.String numeroTransaction = new String();
    private java.lang.String prix = new String();
    private java.lang.String rueDebiteur = new String();
    private java.lang.String rueDonneurOrdre = new String();
    private globaz.globall.db.BSession session = null;
    private java.lang.String typeTransaction = new String();

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
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:49:47)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCodeRefus() {
        return codeRefus;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:43:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCommunication1() {
        return communication1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:44:05)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCommunication2() {
        return communication2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:44:31)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCommunication3() {
        return communication3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:44:52)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCommunication4() {
        return communication4;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:45:08)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCommunication5() {
        return communication5;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2002 13:43:21)
     * 
     * @return java.lang.String
     */
    @Override
    public String getCurrentBuffer() {
        return currentBuffer;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:38:20)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:40:49)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDesignationSuppDebiteur() {
        return designationSuppDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:45:54)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDesignationSuppDonneurOrdre() {
        return designationSuppDonneurOrdre;
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
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:29:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getElementEcritureCreditCode() {
        return elementEcritureCreditCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:31:53)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getElementEcritureCreditMontantTotal() {
        return elementEcritureCreditMontantTotal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:30:19)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getElementEcritureCreditNbreTransaction() {
        return elementEcritureCreditNbreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:33:29)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getElementEcritureRefusCode() {
        return elementEcritureRefusCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:34:05)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getElementEcritureRefusMontantTotal() {
        return elementEcritureRefusMontantTotal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:34:59)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getElementEcritureRefusNbreTransaction() {
        return elementEcritureRefusNbreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getGenreTransaction() {
        return genreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:37:55)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdentificateurFichier() {
        return identificateurFichier;
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
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:42:09)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLieuDebiteur() {
        return lieuDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:47:04)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLieuDonneurOrdre() {
        return lieuDonneurOrdre;
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
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontant() {
        return montant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:51:00)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMontantTotalPrix() {
        return montantTotalPrix;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNombreTransactions() {
        return nombreTransactions;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:40:19)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNomDebiteur() {
        return nomDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:45:29)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNomDonneurOrdre() {
        return nomDonneurOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:41:42)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNPADebiteur() {
        return NPADebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:46:35)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNPADonneurOrdre() {
        return NPADonneurOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumeroAdherent() {
        return numeroAdherent;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:39:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumeroComptePostalDebiteur() {
        return numeroComptePostalDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:39:05)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumeroOrdre() {
        return numeroOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 08:48:20)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumeroReference() {
        return numeroReference;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.02.2002 12:46:08)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:43:23)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getPrix() {
        return prix;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:41:16)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getRueDebiteur() {
        return rueDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:46:15)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getRueDonneurOrdre() {
        return rueDonneurOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.05.2003 13:26:41)
     * 
     * @return globaz.globall.db.BSession
     */
    @Override
    public globaz.globall.db.BSession getSession() {
        return session;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 14:22:55)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTypeTransaction() {
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
        identificateurFichier = EMPTY;
        dateEcheance = EMPTY;
        numeroAdherent = EMPTY;
        numeroOrdre = EMPTY;
        genreTransaction = EMPTY;
        numeroTransaction = EMPTY;
        montant = EMPTY;
        numeroComptePostalDebiteur = EMPTY;
        numeroReference = EMPTY;
        nomDebiteur = EMPTY;
        designationSuppDebiteur = EMPTY;
        rueDebiteur = EMPTY;
        NPADebiteur = EMPTY;
        lieuDebiteur = EMPTY;
        prix = EMPTY;
        communication1 = EMPTY;
        communication2 = EMPTY;
        communication3 = EMPTY;
        communication4 = EMPTY;
        communication5 = EMPTY;
        nomDonneurOrdre = EMPTY;
        designationSuppDonneurOrdre = EMPTY;
        rueDonneurOrdre = EMPTY;
        NPADonneurOrdre = EMPTY;
        lieuDonneurOrdre = EMPTY;
        codeRefus = EMPTY;
        elementEcritureCreditCode = EMPTY;
        elementEcritureCreditMontantTotal = EMPTY;
        elementEcritureCreditNbreTransaction = EMPTY;
        elementEcritureRefusCode = EMPTY;
        elementEcritureRefusMontantTotal = EMPTY;
        elementEcritureRefusNbreTransaction = EMPTY;
        montantTotalPrix = EMPTY;

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
        if (currentBuffer.length() != 700) {
            getMemoryLog().logMessage("5277", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        // Numéroter les transaction
        numeroTransaction = String.valueOf(++numeroLigne);

        // Identificateur de fichier
        identificateurFichier = currentBuffer.substring(0, 3);
        if (!_isNumeric(identificateurFichier)) {
            getMemoryLog().logMessage("5278", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Date d'échéance
        s = currentBuffer.substring(3, 9);
        if (!_isNumeric(s)) {
            getMemoryLog().logMessage("5308", null, FWMessage.ERREUR, this.getClass().getName());
        }
        dateEcheance = s.substring(4) + "." + s.substring(2, 4) + ".20" + s.substring(0, 2);

        // Numéro d'adhérent
        numeroAdherent = currentBuffer.substring(9, 15);
        if (!_isNumeric(numeroAdherent)) {
            getMemoryLog().logMessage("5280", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Numéro d'ordre
        numeroOrdre = currentBuffer.substring(33, 35);
        if (!_isNumeric(numeroOrdre)) {
            getMemoryLog().logMessage("5281", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // Genre de transaction
        genreTransaction = currentBuffer.substring(35, 37);
        if (!_isNumeric(genreTransaction)) {
            getMemoryLog().logMessage("5282", null, FWMessage.ERREUR, this.getClass().getName());
        }

        // S'il s'agit d'un record de total
        if (genreTransaction.equals("98")) {

            // Type de transaction
            typeTransaction = IntLSVParser.FOOTER;

            // Element relatif à la monnaie écritures au crédit
            // Code
            elementEcritureCreditCode = currentBuffer.substring(50, 52);
            if (!elementEcritureCreditCode.equals("01")) {
                getMemoryLog().logMessage("5283", null, FWMessage.ERREUR, this.getClass().getName());
            }
            // Nombre de transactions
            elementEcritureCreditNbreTransaction = currentBuffer.substring(52, 58);
            if (!_isNumeric(elementEcritureCreditNbreTransaction)) {
                getMemoryLog().logMessage("5310", null, FWMessage.ERREUR, this.getClass().getName());
            }
            // Montant
            elementEcritureCreditMontantTotal = currentBuffer.substring(58, 69) + "." + currentBuffer.substring(69, 71);
            try {
                FWCurrency cMontant = new FWCurrency(elementEcritureCreditMontantTotal);
                montant = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }
            // Element relatif à la monnaie écritures refus contre-passation
            // Code
            elementEcritureRefusCode = currentBuffer.substring(71, 73);
            if (!elementEcritureCreditCode.equals("01")) {
                getMemoryLog().logMessage("5283", null, FWMessage.ERREUR, this.getClass().getName());
            }
            // Nombre de transactions
            elementEcritureRefusNbreTransaction = currentBuffer.substring(73, 79);
            if (!_isNumeric(elementEcritureRefusNbreTransaction)) {
                getMemoryLog().logMessage("5310", null, FWMessage.ERREUR, this.getClass().getName());
            }
            // Montant
            elementEcritureRefusMontantTotal = currentBuffer.substring(79, 90) + "." + currentBuffer.substring(90, 92);
            try {
                FWCurrency cMontant = new FWCurrency(elementEcritureCreditMontantTotal);
                montant = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }
            // Montant total des prix
            montantTotalPrix = currentBuffer.substring(92, 96) + "." + currentBuffer.substring(96, 98);
            try {
                FWCurrency cMontant = new FWCurrency(elementEcritureCreditMontantTotal);
                montant = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Autres types de record

        } else {

            // Type de transaction
            typeTransaction = IntLSVParser.TRANSACTION;

            // Montant
            montant = currentBuffer.substring(51, 59) + "." + currentBuffer.substring(59, 61);
            try {
                FWCurrency cMontant = new FWCurrency(montant);
                montant = cMontant.toString();
            } catch (Exception e) {
                getMemoryLog().logMessage("5307", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Numéro de référence
            numeroReference = currentBuffer.substring(97, 124);
            if (!_isNumeric(numeroReference)) {
                getMemoryLog().logMessage("5280", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Nom débiteur
            nomDebiteur = currentBuffer.substring(125, 155);

            // Communication 1
            communication1 = currentBuffer.substring(373, 401);

            // Communication 2
            communication2 = currentBuffer.substring(401, 429);

            // Communication 3
            communication3 = currentBuffer.substring(429, 457);

            // Communication 4
            communication4 = currentBuffer.substring(457, 485);

            // Refus contre-passation
            if (genreTransaction.equals("85")) {
                codeRefus = currentBuffer.substring(513, 515);
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
            }

        }

        return true;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:49:47)
     * 
     * @param newCodeRefus
     *            java.lang.String
     */
    public void setCodeRefus(java.lang.String newCodeRefus) {
        codeRefus = newCodeRefus;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:43:44)
     * 
     * @param newCommunication1
     *            java.lang.String
     */
    public void setCommunication1(java.lang.String newCommunication1) {
        communication1 = newCommunication1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:44:05)
     * 
     * @param newCommunication2
     *            java.lang.String
     */
    public void setCommunication2(java.lang.String newCommunication2) {
        communication2 = newCommunication2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:44:31)
     * 
     * @param newCommunication3
     *            java.lang.String
     */
    public void setCommunication3(java.lang.String newCommunication3) {
        communication3 = newCommunication3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:44:52)
     * 
     * @param newCommunication4
     *            java.lang.String
     */
    public void setCommunication4(java.lang.String newCommunication4) {
        communication4 = newCommunication4;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:45:08)
     * 
     * @param newCommunication5
     *            java.lang.String
     */
    public void setCommunication5(java.lang.String newCommunication5) {
        communication5 = newCommunication5;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:38:20)
     * 
     * @param newDateEcheance
     *            java.lang.String
     */
    public void setDateEcheance(java.lang.String newDateEcheance) {
        dateEcheance = newDateEcheance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:40:49)
     * 
     * @param newDesignationSuppDebiteur
     *            java.lang.String
     */
    public void setDesignationSuppDebiteur(java.lang.String newDesignationSuppDebiteur) {
        designationSuppDebiteur = newDesignationSuppDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:45:54)
     * 
     * @param newDesignationSuppDonneurOrdre
     *            java.lang.String
     */
    public void setDesignationSuppDonneurOrdre(java.lang.String newDesignationSuppDonneurOrdre) {
        designationSuppDonneurOrdre = newDesignationSuppDonneurOrdre;
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
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:29:44)
     * 
     * @param newElementEcritureCreditCode
     *            java.lang.String
     */
    public void setElementEcritureCreditCode(java.lang.String newElementEcritureCreditCode) {
        elementEcritureCreditCode = newElementEcritureCreditCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:31:53)
     * 
     * @param newElementEcritureCreditMontantTotal
     *            java.lang.String
     */
    public void setElementEcritureCreditMontantTotal(java.lang.String newElementEcritureCreditMontantTotal) {
        elementEcritureCreditMontantTotal = newElementEcritureCreditMontantTotal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:30:19)
     * 
     * @param newElementEcritureCreditNbreTransaction
     *            java.lang.String
     */
    public void setElementEcritureCreditNbreTransaction(java.lang.String newElementEcritureCreditNbreTransaction) {
        elementEcritureCreditNbreTransaction = newElementEcritureCreditNbreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:33:29)
     * 
     * @param newElementEcritureRefusCode
     *            java.lang.String
     */
    public void setElementEcritureRefusCode(java.lang.String newElementEcritureRefusCode) {
        elementEcritureRefusCode = newElementEcritureRefusCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:34:05)
     * 
     * @param newElementEcritureRefusMontantTotal
     *            java.lang.String
     */
    public void setElementEcritureRefusMontantTotal(java.lang.String newElementEcritureRefusMontantTotal) {
        elementEcritureRefusMontantTotal = newElementEcritureRefusMontantTotal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 13:34:59)
     * 
     * @param newElementEcritureRefusNbreTransaction
     *            java.lang.String
     */
    public void setElementEcritureRefusNbreTransaction(java.lang.String newElementEcritureRefusNbreTransaction) {
        elementEcritureRefusNbreTransaction = newElementEcritureRefusNbreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:37:55)
     * 
     * @param newIdentificateurFichier
     *            java.lang.String
     */
    public void setIdentificateurFichier(java.lang.String newIdentificateurFichier) {
        identificateurFichier = newIdentificateurFichier;
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
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:42:09)
     * 
     * @param newLieuDebiteur
     *            java.lang.String
     */
    public void setLieuDebiteur(java.lang.String newLieuDebiteur) {
        lieuDebiteur = newLieuDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:47:04)
     * 
     * @param newLieuDonneurOrdre
     *            java.lang.String
     */
    public void setLieuDonneurOrdre(java.lang.String newLieuDonneurOrdre) {
        lieuDonneurOrdre = newLieuDonneurOrdre;
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
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:51:00)
     * 
     * @param newMontantTotalPrix
     *            java.lang.String
     */
    public void setMontantTotalPrix(java.lang.String newMontantTotalPrix) {
        montantTotalPrix = newMontantTotalPrix;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:40:19)
     * 
     * @param newNomDebiteur
     *            java.lang.String
     */
    public void setNomDebiteur(java.lang.String newNomDebiteur) {
        nomDebiteur = newNomDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:45:29)
     * 
     * @param newNomDonneurOrdre
     *            java.lang.String
     */
    public void setNomDonneurOrdre(java.lang.String newNomDonneurOrdre) {
        nomDonneurOrdre = newNomDonneurOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:41:42)
     * 
     * @param newNPADebiteur
     *            java.lang.String
     */
    public void setNPADebiteur(java.lang.String newNPADebiteur) {
        NPADebiteur = newNPADebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:46:35)
     * 
     * @param newNPADonneurOrdre
     *            java.lang.String
     */
    public void setNPADonneurOrdre(java.lang.String newNPADonneurOrdre) {
        NPADonneurOrdre = newNPADonneurOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:39:44)
     * 
     * @param newNumeroComptePostalDebiteur
     *            java.lang.String
     */
    public void setNumeroComptePostalDebiteur(java.lang.String newNumeroComptePostalDebiteur) {
        numeroComptePostalDebiteur = newNumeroComptePostalDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:39:05)
     * 
     * @param newNumeroOrdre
     *            java.lang.String
     */
    public void setNumeroOrdre(java.lang.String newNumeroOrdre) {
        numeroOrdre = newNumeroOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:43:23)
     * 
     * @param newPrix
     *            java.lang.String
     */
    public void setPrix(java.lang.String newPrix) {
        prix = newPrix;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:41:16)
     * 
     * @param newRueDebiteur
     *            java.lang.String
     */
    public void setRueDebiteur(java.lang.String newRueDebiteur) {
        rueDebiteur = newRueDebiteur;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.12.2002 15:46:15)
     * 
     * @param newRueDonneurOrdre
     *            java.lang.String
     */
    public void setRueDonneurOrdre(java.lang.String newRueDonneurOrdre) {
        rueDonneurOrdre = newRueDonneurOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.05.2003 13:26:41)
     * 
     * @param newSession
     *            globaz.globall.db.BSession
     */
    @Override
    public void setSession(globaz.globall.db.BSession newSession) {
        session = newSession;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.12.2002 14:22:55)
     * 
     * @param newTypeTransaction
     *            java.lang.String
     */
    public void setTypeTransaction(java.lang.String newTypeTransaction) {
        typeTransaction = newTypeTransaction;
    }
}
