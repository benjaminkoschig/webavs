package globaz.osiris.client.cfc.contentieux;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BIDocument;
import globaz.globall.db.BTransaction;
import ch.globaz.common.document.reference.ReferenceBVR;
import globaz.osiris.print.itext.CADocumentContentieux;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * @author S�bastien Chappatte
 * @version 1.0.0 Pr�parer les donn�es � remplir pour impression du premier rappel
 */
public class CFPremierRappel extends CADocumentContentieux {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    ReferenceBVR bvr = null;
    private String centimes = new String();
    private String civilite = new String();
    private String dateEcheance = new String();
    private String dateSection = new String();
    private String ligneAdresse1 = new String();
    private String ligneAdresse2 = new String();
    private String ligneAdresse3 = new String();
    private String ligneAdresse4 = new String();
    private String ligneAdresse5 = new String();
    private String ligneAdresse6 = new String();
    private String ligneAdresse7 = new String();
    private String montant = new String();
    private String montantFacture = new String();
    private String montantSansCentime = new String();
    private String noCompte;
    private String noFacture = new String();
    private String ocrb = new String();
    private String reference = new String();
    private String taxe = new String();

    /**
     * Commentaire relatif au constructeur AJPremierRappel.
     * 
     * @throws Exception
     */
    public CFPremierRappel() throws Exception {
        super();
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
        FWCurrency cMontant = new FWCurrency(0.00);
        cMontant.add(getSection().getSolde());
        cMontant.add(getTaxe());
        montant = cMontant.toStringFormat();
        montantFacture = getSection().getSoldeFormate();
        dateEcheance = getSection().getDateEcheance();

        // Recherche les informations pour remplir le bordereau
        montantSansCentime = globaz.globall.util.JAUtil.createBigDecimal(cMontant.toString()).toBigInteger().toString();

        java.math.BigDecimal montantSansCentimeBigDecimal = globaz.globall.util.JAUtil
                .createBigDecimal(montantSansCentime);
        java.math.BigDecimal montantAvecCentimeBigDecimal = globaz.globall.util.JAUtil.createBigDecimal(cMontant
                .toString());
        centimes = montantAvecCentimeBigDecimal.subtract(montantSansCentimeBigDecimal).toString().substring(2, 4);

        montantSansCentime = globaz.globall.util.JANumberFormatter.formatNoRound(montantSansCentime);

        getBvr().setSession(transaction.getSession());
        getBvr().setBVR(getSection(), cMontant.toString());
        ocrb = getBvr().getOcrb();
        reference = getBvr().getLigneReference();
    }

    @Override
    public void createDataSource() throws Exception {
        // TODO Auto-generated method stub

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
     * Renvoie la r�f�rence BVR.
     * 
     * @return la r�f�rence BVR.
     */
    public ReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new ReferenceBVR();
        }
        return bvr;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.06.2002 11:00:09)
     * 
     * @return String
     */
    public String getCentimes() {
        return centimes;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 14:44:46)
     * 
     * @return String
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:05:40)
     * 
     * @return String
     */
    public String getDateEcheance() {
        return dateEcheance;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 14:42:03)
     * 
     * @return String
     */
    public String getDateSection() {
        return dateSection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (01.07.2002 18:14:45)
     * 
     * @return globaz.framework.printing.FWDocument
     */
    @Override
    public BIDocument getDocumentClass() throws Exception {
        return new CFPremierRappel_Doc();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:19:20)
     * 
     * @return String
     */
    public String getLigneAdresse1() {
        return ligneAdresse1;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.06.2002 08:51:27)
     * 
     * @return String
     */
    public String getLigneAdresse2() {
        return ligneAdresse2;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:19:40)
     * 
     * @return String
     */
    public String getLigneAdresse3() {
        return ligneAdresse3;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:20:23)
     * 
     * @return String
     */
    public String getLigneAdresse4() {
        return ligneAdresse4;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:20:47)
     * 
     * @return String
     */
    public String getLigneAdresse5() {
        return ligneAdresse5;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:23:56)
     * 
     * @return String
     */
    public String getLigneAdresse6() {
        return ligneAdresse6;
    }

    public String getLigneAdresse7() {
        return ligneAdresse7;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:05:07)
     * 
     * @return String
     */
    public String getMontant() {
        return montant;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.07.2003 11:54:10)
     * 
     * @return String
     */
    public String getMontantFacture() {
        return montantFacture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (17.06.2002 10:59:50)
     * 
     * @return String
     */
    public String getMontantSansCentime() {
        return montantSansCentime;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 11:31:55)
     * 
     * @return int
     */
    public String getNoCompte() {
        return noCompte;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 10:38:58)
     * 
     * @return String
     */
    public String getNoFacture() {
        return noFacture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.06.2002 13:06:52)
     * 
     * @return String
     */
    public String getOcrb() {
        return ocrb;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.06.2002 15:24:21)
     * 
     * @return String
     */
    public String getReference() {
        return reference;
    }

    /**
     * Cette m�thode permet de r�cup�rer le total des taxes Date de cr�ation : (18.07.2003 09:53:30)
     * 
     * @return String
     */
    public String getTaxe() {
        // R�ciup�ration des diff�rentes taxes
        Enumeration taxes = listTaxes();
        globaz.osiris.db.contentieux.CATaxe t;
        globaz.framework.util.FWCurrency cMontant = new globaz.framework.util.FWCurrency(0.00);
        if (taxes.hasMoreElements()) {
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.05.2003 13:06:03)
     * 
     * @return String
     * @param param
     *            String
     * @param delim
     *            String
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
     * @see globaz.osiris.external.IntDocumentContentieux#setModePrevisonnel(java .lang.Boolean)
     */
    @Override
    public void setModePrevisonnel(Boolean modePervisionnel) {
        // TODO Raccord de m�thode auto-g�n�r�

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.07.2003 11:54:10)
     * 
     * @param newMontantFacture
     *            String
     */
    public void setMontantFacture(String newMontantFacture) {
        montantFacture = newMontantFacture;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.07.2003 09:53:30)
     * 
     * @param newTaxe
     *            String
     */
    public void setTaxe(String newTaxe) {
        taxe = newTaxe;
    }
}
