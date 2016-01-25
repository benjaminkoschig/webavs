package globaz.osiris.print.itext;

import globaz.globall.api.BIDocument;
import globaz.globall.db.BTransaction;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * @author Sébastien Chappatte
 * @version 1.0.0 Préparer les données à remplir pour impression du premier rappel
 */
public class CARequisition extends CADocumentContentieux {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String centimes = new String();
    private java.lang.String civilite = new String();
    private java.lang.String dateDocument = new String();
    private java.lang.String dateEcheance = new String();
    private java.lang.String dateSection = new String();
    private java.lang.String ligneAdresse1 = new String();
    private java.lang.String ligneAdresse2 = new String();
    private java.lang.String ligneAdresse3 = new String();
    private java.lang.String ligneAdresse4 = new String();
    private java.lang.String ligneAdresse5 = new String();
    private java.lang.String ligneAdresse6 = new String();
    private java.lang.String ligneAdresse7 = new String();
    private Boolean modePrevisionnel = new Boolean(false);
    private java.lang.String montant = new String();
    private java.lang.String montantFacture = new String();
    private java.lang.String montantSansCentime = new String();
    private java.lang.String noCompte;
    private java.lang.String noFacture = new String();
    private java.lang.String ocrb = new String();
    private java.lang.String reference = new String();
    private java.lang.String taxe = new String();

    /**
     * Commentaire relatif au constructeur AJPremierRappel.
     * 
     * @throws Exception
     */
    public CARequisition() throws Exception {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.06.2002 10:51:26)
     * 
     * @throws java.lang.Exception
     *             La description de l'exception.
     */
    @Override
    public void afterPrint(BTransaction transaction) throws Exception {
    }

    /**
     * Method batchPrint.
     * 
     * @return String
     * @throws Exception
     *             Exception
     */
    public String batchPrint() throws Exception {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.05.2002 15:05:13)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void beforePrint(BTransaction transaction) throws Exception {
        // Rechercher les données pour afficher sur l'écran
        // Rechercher Adresse
        globaz.osiris.formatter.CAAdresseCourrierFormatter fmt = new globaz.osiris.formatter.CAAdresseCourrierFormatter(
                getTiers(), getAdresseCourrier());
        // Affiche le tiers avec le prénom puis le nom
        fmt.setNomFirst(false);
        String[] adresseCourrierAffiche = fmt.getAdresseLines(7);
        // Recherche Nom et prénom
        ligneAdresse1 = adresseCourrierAffiche[0];
        ligneAdresse2 = adresseCourrierAffiche[1];
        ligneAdresse3 = adresseCourrierAffiche[2];
        ligneAdresse4 = adresseCourrierAffiche[3];
        ligneAdresse5 = adresseCourrierAffiche[4];
        ligneAdresse6 = adresseCourrierAffiche[5];
        ligneAdresse7 = adresseCourrierAffiche[6];
        // rechercher info section
        dateSection = getSection().getDateSection();
        noCompte = getSection().getCompteAnnexe().getIdExterneRole();
        noFacture = getSection().getIdExterne();
        dateEcheance = getSection().getDateEcheance();
        dateDocument = getDateDocument();
    }

    @Override
    public void createDataSource() throws Exception {
        // Doit-etre implémenté

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.05.2002 15:05:13)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public String directPrint() throws Exception {
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 11:00:09)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCentimes() {
        return centimes;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 14:44:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCivilite() {
        return civilite;
    }

    /**
     * @return
     */
    public java.lang.String getDateDocument() {
        return dateDocument;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:05:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 14:42:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateSection() {
        return dateSection;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.07.2002 18:14:45)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    @Override
    public BIDocument getDocumentClass() throws Exception {
        return new CARequisition_Doc();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:19:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse1() {
        return ligneAdresse1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 08:51:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse2() {
        return ligneAdresse2;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:19:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse3() {
        return ligneAdresse3;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:20:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse4() {
        return ligneAdresse4;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:20:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse5() {
        return ligneAdresse5;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:23:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse6() {
        return ligneAdresse6;
    }

    public java.lang.String getLigneAdresse7() {
        return ligneAdresse7;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:05:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontant() {
        return montant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.07.2003 11:54:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantFacture() {
        return montantFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 10:59:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMontantSansCentime() {
        return montantSansCentime;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 11:31:55)
     * 
     * @return int
     */
    public java.lang.String getNoCompte() {
        return noCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 10:38:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNoFacture() {
        return noFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2002 13:06:52)
     * 
     * @return java.lang.String
     */
    public java.lang.String getOcrb() {
        return ocrb;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.06.2002 15:24:21)
     * 
     * @return java.lang.String
     */
    public java.lang.String getReference() {
        return reference;
    }

    /**
     * Cette méthode permet de récupérer le total des taxes Date de création : (18.07.2003 09:53:30)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTaxe() {
        // Réciupération des différentes taxes
        Enumeration taxes = listTaxes();
        globaz.osiris.db.contentieux.CATaxe t;
        globaz.framework.util.FWCurrency cMontant = new globaz.framework.util.FWCurrency(0.00);
        if ((taxes != null) && taxes.hasMoreElements()) {
            while (taxes.hasMoreElements()) {
                t = (globaz.osiris.db.contentieux.CATaxe) taxes.nextElement();
                cMontant.add(t.getMontantTaxe());
            }
            taxe = cMontant.toString();
        } else {
            taxe = "0.00";
        }
        return taxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 13:06:03)
     * 
     * @return java.lang.String
     * @param param
     *            java.lang.String
     * @param delim
     *            java.lang.String
     */
    public String getTextWithoutDelimiter(String textString, String delim) {
        StringTokenizer st = new java.util.StringTokenizer(textString, delim);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntDocumentContentieux#isModePrevisionnel()
     */
    @Override
    public Boolean isModePrevisionnel() {
        return modePrevisionnel;
    }

    /**
     * @param string
     */
    public void setDateDocument(java.lang.String string) {
        dateDocument = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 10:50:40)
     * 
     * @param modePervisionnel
     *            boolean
     */
    public void setModePrevisonnel(boolean modePervisionnel) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntDocumentContentieux#setModePrevisonnel(java .lang.Boolean)
     */
    @Override
    public void setModePrevisonnel(Boolean booleen) {
        modePrevisionnel = booleen;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.07.2003 11:54:10)
     * 
     * @param newMontantFacture
     *            java.lang.String
     */
    public void setMontantFacture(java.lang.String newMontantFacture) {
        montantFacture = newMontantFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.07.2003 09:53:30)
     * 
     * @param newTaxe
     *            java.lang.String
     */
    public void setTaxe(java.lang.String newTaxe) {
        taxe = newTaxe;
    }

}
