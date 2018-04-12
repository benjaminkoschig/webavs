package globaz.musca.itext;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public abstract class FAImpressionFacturation extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DECOMPTE_COTISATION = "910020";
    public static final String DOMAINE_FACTURATION = "910010";
    public final static String TYPE_FACTURE = "910021";
    public final static String TYPE_LETTRE = "910022";
    protected java.lang.String centimes;
    protected java.lang.String dateImpression = new String();
    protected FAEnteteFacture entity;
    protected Iterator entityList = null;
    protected int factureImpressionNo = 0;
    /**
	 * 
	 */
    private JadeUser jadeUser = null;
    protected java.lang.String montantSansCentime;
    protected FAPassage passage;
    protected globaz.framework.util.FWCurrency totalMontant;
    private Boolean isEbusiness = false;

    /**
     * Constructor for FAImpressionFacturation.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public FAImpressionFacturation(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Constructor for FAImpressionFacturation.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public FAImpressionFacturation(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        super.DocumentSort();
        return super.beforePrintDocument();
    }

    protected String getAnneeFromEntete(FAEnteteFacture entity) {
        String annee = "";
        if ((entity != null) && (entity.getIdExterneFacture() != null)) {
            if (entity.getIdExterneFacture().length() >= 4) {
                annee = entity.getIdExterneFacture().substring(0, 4);
            }
        }
        return annee;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 11:43:01)
     * 
     * @return java.lang.String
     */
    public final java.lang.String getCentimes() {
        return centimes;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.06.2002 07:42:07)
     * 
     * @return java.lang.String
     */
    public final java.lang.String getDateImpression() {
        return dateImpression;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 09:49:49)
     * 
     * @return globaz.musca.db.facturation.FAEnteteFactureViewBean
     */
    public final FAEnteteFacture getEntity() {
        return entity;
    }

    /**
     * Returns the entityList.
     * 
     * @return Iterator
     */
    protected Iterator getEntityList() {
        return entityList;
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 14:23:25)
     * 
     * @return int
     */
    public final int getFactureImpressionNo() {
        return factureImpressionNo;
    }

    public JadeUser getJadeUser() {
        return jadeUser;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 11:42:25)
     * 
     * @return java.lang.String
     */
    public final java.lang.String getMontantSansCentime() {
        return montantSansCentime;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 10:16:42)
     * 
     * @return globaz.musca.db.facturation.FAPassage
     */
    public final globaz.musca.db.facturation.FAPassage getPassage() {
        return passage;
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
    public final String getTextStrechedByChar(String textString, String stretchChar) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < textString.length(); i++) {
            sb.append(textString.charAt(i));
            sb.append(stretchChar);
        }
        return sb.toString();
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
    public final String getTextWithoutDelimiter(String textString, String delim) {
        StringTokenizer st = new java.util.StringTokenizer(textString, delim);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
        }
        return sb.toString();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 11:43:05)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public final globaz.framework.util.FWCurrency getTotalMontant() {
        return totalMontant;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.06.2002 07:42:07)
     * 
     * @param newDateImpression
     *            java.lang.String
     */
    public final void setDateImpression(java.lang.String newDateImpression) {
        dateImpression = newDateImpression;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 09:49:49)
     * 
     * @param newEntity
     *            globaz.musca.db.facturation.FAEnteteFactureViewBean
     */
    public final void setEntity(globaz.musca.db.facturation.FAEnteteFacture newEntity) {
        entity = newEntity;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 09:49:49)
     * 
     * @param newEntity
     *            globaz.musca.db.facturation.FAEnteteFactureViewBean
     */
    public final void setEntity(globaz.musca.db.facturation.FAEnteteFactureViewBean newEntity) {
        entity = newEntity;
    }

    /**
     * Sets the entityList.
     * 
     * @param entityList
     *            The entityList to set
     */
    public void setEntityList(ArrayList entityList) {
        this.entityList = entityList.iterator();
    }

    /**
     * Insert the method's description here. Creation date: (20.06.2003 14:23:25)
     * 
     * @param newFacturImpressionNo
     *            int
     */
    public final void setFactureImpressionNo(int newFactureImpressionNo) {
        factureImpressionNo = newFactureImpressionNo;
    }

    public void setJadeUser(JadeUser jadeUser) {
        this.jadeUser = jadeUser;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.05.2003 10:16:42)
     * 
     * @param newPassage
     *            globaz.musca.db.facturation.FAPassage
     */
    public final void setPassage(globaz.musca.db.facturation.FAPassage newPassage) {
        passage = newPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 11:43:05)
     * 
     * @param newTotalMontant
     *            globaz.framework.util.FWCurrency
     */
    public final void setTotalMontant(globaz.framework.util.FWCurrency newTotalMontant) {
        totalMontant = newTotalMontant;
    }

    /*
     * OCA - Permet de choisir si l'on veut afficher le header sur chaque page. ceci est utile lorsque les ligne de
     * facture prennent plus d'une page, certain client veulent que les ligne commence dès le début de la page suivante,
     * d'autre, comme la FER ont du papier préimprimé et doivent donc répéter le header sur chaque page.
     * 
     * Voir également : FAImpressionFacture_Param
     */
    public boolean wantHeaderOnEachPage() throws Exception {
        boolean headerOnEachPage = false;
        String headerOnEachPageTxt = getSession().getApplication().getProperty("headerOnEachPage");
        if ("true".equals(headerOnEachPageTxt)) {
            headerOnEachPage = true;
        }
        return headerOnEachPage;
    }

    public Boolean getIsEbusiness() {
        return isEbusiness;
    }

    public void setIsEbusiness(Boolean isEbusiness) {
        this.isEbusiness = isEbusiness;
    }

}
