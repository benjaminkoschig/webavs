package globaz.osiris.print.itext;

import globaz.globall.api.BIDocument;
import globaz.globall.db.BTransaction;

/**
 * @author S�bastien Chappatte
 * @version 1.0.0 Pr�parer les donn�es � remplir pour impression du premier rappel
 */
public class CARappel extends CADocumentContentieux {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String dateDocument = new String();
    private java.lang.String dateSection = new String();
    private java.lang.String ligneAdresse1 = new String();
    private java.lang.String ligneAdresse2 = new String();
    private java.lang.String ligneAdresse3 = new String();
    private java.lang.String ligneAdresse4 = new String();
    private java.lang.String ligneAdresse5 = new String();
    private java.lang.String ligneAdresse6 = new String();
    private java.lang.String ligneAdresse7 = new String();
    private Boolean modePrevisionnel = new Boolean(false);
    private java.lang.String montantFacture = new String();
    private java.lang.String noCompte;
    private java.lang.String noFacture = new String();

    /**
     * Commentaire relatif au constructeur AJPremierRappel.
     * 
     * @throws Exception
     */
    public CARappel() throws Exception {
        super();
    }

    @Override
    public String getJasperTemplate() {
        return null;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.06.2002 10:51:26)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (23.05.2002 15:05:13)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void beforePrint(BTransaction transaction) throws Exception {
        // Rechercher les donn�es pour afficher sur l'�cran
        // Rechercher Adresse
        globaz.osiris.formatter.CAAdresseCourrierFormatter fmt = new globaz.osiris.formatter.CAAdresseCourrierFormatter(
                getTiers(), getAdresseCourrier());
        // Affiche le tiers avec le pr�nom puis le nom
        fmt.setNomFirst(false);
        String[] adresseCourrierAffiche = fmt.getAdresseLines(7);
        // Recherche Nom et pr�nom
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
        montantFacture = getSection().getSoldeFormate();
        dateDocument = getDateDocument();
    }

    @Override
    public void createDataSource() throws Exception {
        // N'est pas utilis� mais doit �tre pr�sent !
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (23.05.2002 15:05:13)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public String directPrint() throws Exception {
        return null;
    }

    /**
     * @return
     */
    public java.lang.String getDateDocument() {
        return dateDocument;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.06.2002 11:00:09)
     * 
     * @return java.lang.String
     */

    public java.lang.String getDateSection() {
        return dateSection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.07.2002 18:14:45)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    @Override
    public BIDocument getDocumentClass() throws Exception {
        return new CARappel_Doc();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:19:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse1() {
        return ligneAdresse1;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.06.2002 08:51:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse2() {
        return ligneAdresse2;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:19:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse3() {
        return ligneAdresse3;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:20:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse4() {
        return ligneAdresse4;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:20:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLigneAdresse5() {
        return ligneAdresse5;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:23:56)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:05:07)
     * 
     * @return java.lang.String
     */

    public java.lang.String getMontantFacture() {
        return montantFacture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.07.2003 09:53:30)
     * 
     * @param newTaxe
     *            java.lang.String
     */

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.06.2002 10:59:50)
     * 
     * @return java.lang.String
     */

    public java.lang.String getNoCompte() {
        return noCompte;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 10:38:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNoFacture() {
        return noFacture;
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.07.2003 11:54:10)
     * 
     * @param newMontantFacture
     *            java.lang.String
     */
    public void setMontantFacture(java.lang.String newMontantFacture) {
        montantFacture = newMontantFacture;
    }

}
