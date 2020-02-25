package globaz.musca.itext;

import globaz.aquila.service.cataloguetxt.COCatalogueTextesService;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.impfactbvrutil.FAImpFactDataSource;
import globaz.musca.itext.newimpbvrutil.FANewImpFactDataSource;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.db.utils.CAReferenceQR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
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

    protected FAImpFactDataSource currentDataSource = null;
    protected FANewImpFactDataSource newCurrentDataSource = null;
    protected CAReferenceQR qrFacture = null;
    protected FWCurrency tmpCurrency = null;
    protected boolean reporterMontant = false;
    protected boolean factureMontantReport = false;
    protected boolean modeReporterMontantMinime;
    protected String adresseDebiteur = "";
    protected boolean factureAvecMontantMinime = false;
    protected CAReferenceBVR bvr = null;


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
     * @param textString
     *            java.lang.String
     * @param stretchChar
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
     * @param textString
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
     * @param newFactureImpressionNo
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

    public void initVariableQR() {

        qrFacture.setMontant(Objects.isNull(tmpCurrency)? "" : tmpCurrency.toString());


        try {
            // La monnaie n'est pas géré dans le module Facturation. Par défaut nous mettrons CHF
            qrFacture.setMonnaie(qrFacture.DEVISE_DEFAUT);

            //qrFacture.setCrePays(qrFacture.getCodePays());
            qrFacture.recupererIban();
            if (!qrFacture.genererAdresseDebiteur(currentDataSource.getEnteteFacture().getIdTiers())) {
                // si l'adresse n'est pas trouvé en DB, alors chargement d'une adresse Combiné
                qrFacture.setDebfAdressTyp(CAReferenceQR.COMBINE);
                //
                qrFacture.setDebfRueOuLigneAdresse1(currentDataSource.getAdressePrincipale());
            }
            qrFacture.genererReferenceQRFact(currentDataSource.getEnteteFacture(), isFactureAvecMontantMinime(), reporterMontant);

            // Il n'existe pas pour l'heure actuel d'adresse de créditeur en DB.
            // Elle est récupérée depuis le catalogue de texte au format Combinée
            qrFacture.genererCreAdresse();
            //qrFacture.setDebfRueOuLigneAdresse1(getAdresseDestinataire());
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche des élements de la sommation : " + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }


    }

    protected void initCommonVar() {

        tmpCurrency = new FWCurrency(currentDataSource.getEnteteFacture().getTotalFacture());

        if (!(tmpCurrency.isNegative() || tmpCurrency.isZero())) {
            _initMontant();
        }

        if (isFactureMontantReport() && modeReporterMontantMinime) {
            reporterMontant = true;
        }

        // commencer à écrire les paramètres

        adresseDebiteur = currentDataSource.getAdressePrincipale();

    }

    public void _initMontant() {
        String montantFacture = JANumberFormatter.deQuote(currentDataSource.getEnteteFacture().getTotalFacture());
        // convertir le montant en entier (BigInteger)
        montantSansCentime = JAUtil.createBigDecimal(montantFacture).toBigInteger().toString();

        java.math.BigDecimal montantSansCentimeBig = JAUtil.createBigDecimal(montantSansCentime);
        // convertir le montant avec centimes en BigDecimal
        java.math.BigDecimal montantAvecCentimeBig = JAUtil.createBigDecimal(montantFacture);

        // les centimes représentés en entier
        centimes = montantAvecCentimeBig.subtract(montantSansCentimeBig).toString().substring(2, 4);
    }

    public boolean isFactureMontantReport() {
        return factureMontantReport;
    }


    public void setFactureMontantReport(boolean factureMontantReport) {
        this.factureMontantReport = factureMontantReport;
    }

    /**
     * @return
     */
    public boolean isModeReporterMontantMinimal() {
        return modeReporterMontantMinime;
    }

    /**
     * Returns the factureAvecMontantMinime.
     *
     * @return boolean
     */
    public boolean isFactureAvecMontantMinime() {
        return factureAvecMontantMinime;
    }

    /**
     * Sets the factureAvecMontantMinime.
     *
     * @param factureAvecMontantMinime
     *            The factureAvecMontantMinime to set
     */
    public void setFactureAvecMontantMinime(boolean factureAvecMontantMinime) {
        this.factureAvecMontantMinime = factureAvecMontantMinime;
    }

    /**
     * Renvoie la référence BVR.
     *
     * @return la référence BVR.
     */
    public CAReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new CAReferenceBVR();
        }
        return bvr;
    }
}
