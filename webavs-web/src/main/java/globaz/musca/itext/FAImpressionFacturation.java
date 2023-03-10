package globaz.musca.itext;

import ch.globaz.common.document.reference.ReferenceBVR;
import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.util.GenerationQRCode;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.impfactbvrutil.FAImpFactDataSource;
import globaz.musca.itext.newimpbvrutil.FANewImpFactDataSource;
import globaz.pyxis.constantes.IConstantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public abstract class FAImpressionFacturation extends FWIDocumentManager {

    private static final Logger LOG = LoggerFactory.getLogger(FAImpressionFacturation.class);
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
    protected ReferenceQR qrFacture = null;
    protected List<ReferenceQR> qrFactures = new ArrayList<>();
    protected FWCurrency tmpCurrency = null;
    protected boolean reporterMontant = false;
    protected boolean factureMontantReport = false;
    protected boolean modeReporterMontantMinime;
    protected String adresseDebiteur = "";
    protected boolean factureAvecMontantMinime = false;
    protected ReferenceBVR bvr = null;
    protected Boolean computePageActive = false;


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

    @Override
    public void afterExecuteReport() {
        super.afterExecuteReport();
        try {
            GenerationQRCode.deleteQRCodeImages(qrFactures);
        } catch (IOException e) {
            getMemoryLog().logMessage("Erreur lors de la suppression de l'image QR-Code : " + e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
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
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (07.05.2003 11:43:01)
     * 
     * @return java.lang.String
     */
    public final java.lang.String getCentimes() {
        return centimes;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (20.06.2002 07:42:07)
     * 
     * @return java.lang.String
     */
    public final java.lang.String getDateImpression() {
        return dateImpression;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (02.05.2003 09:49:49)
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
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (07.05.2003 11:42:25)
     * 
     * @return java.lang.String
     */
    public final java.lang.String getMontantSansCentime() {
        return montantSansCentime;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (02.05.2003 10:16:42)
     * 
     * @return globaz.musca.db.facturation.FAPassage
     */
    public final globaz.musca.db.facturation.FAPassage getPassage() {
        return passage;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (07.05.2003 13:06:03)
     * 
     * @return java.lang.String
     * @param textString
     *            java.lang.String
     * @param stretchChar
     *            java.lang.String
     */
    public final String getTextStrechedByChar(String textString, String stretchChar) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textString.length(); i++) {
            sb.append(textString.charAt(i));
            sb.append(stretchChar);
        }
        return sb.toString();
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (07.05.2003 13:06:03)
     * 
     * @return java.lang.String
     * @param textString
     *            java.lang.String
     * @param delim
     *            java.lang.String
     */
    public final String getTextWithoutDelimiter(String textString, String delim) {
        StringTokenizer st = new java.util.StringTokenizer(textString, delim);
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
        }
        return sb.toString();
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (30.04.2003 11:43:05)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public final globaz.framework.util.FWCurrency getTotalMontant() {
        return totalMontant;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (20.06.2002 07:42:07)
     * 
     * @param newDateImpression
     *            java.lang.String
     */
    public final void setDateImpression(java.lang.String newDateImpression) {
        dateImpression = newDateImpression;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (02.05.2003 09:49:49)
     * 
     * @param newEntity
     *            globaz.musca.db.facturation.FAEnteteFactureViewBean
     */
    public final void setEntity(globaz.musca.db.facturation.FAEnteteFacture newEntity) {
        entity = newEntity;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (02.05.2003 09:49:49)
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
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (02.05.2003 10:16:42)
     * 
     * @param newPassage
     *            globaz.musca.db.facturation.FAPassage
     */
    public final void setPassage(globaz.musca.db.facturation.FAPassage newPassage) {
        passage = newPassage;
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (30.04.2003 11:43:05)
     * 
     * @param newTotalMontant
     *            globaz.framework.util.FWCurrency
     */
    public final void setTotalMontant(globaz.framework.util.FWCurrency newTotalMontant) {
        totalMontant = newTotalMontant;
    }

    /*
     * OCA - Permet de choisir si l'on veut afficher le header sur chaque page. ceci est utile lorsque les ligne de
     * facture prennent plus d'une page, certain client veulent que les ligne commence d?s le d?but de la page suivante,
     * d'autre, comme la FER ont du papier pr?imprim? et doivent donc r?p?ter le header sur chaque page.
     * 
     * Voir ?galement : FAImpressionFacture_Param
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

    public void initVariableQR(String langueTier) {

        qrFacture.setMontant(Objects.isNull(tmpCurrency)? "" : tmpCurrency.toString());
        qrFacture.setLangueDoc(langueTier);

        try {
            // La monnaie n'est pas g?r? dans le module Facturation. Par d?faut nous mettrons CHF
            qrFacture.setMonnaie(qrFacture.DEVISE_DEFAUT);

            qrFacture.recupererIban();

            if (!genererAdresseDebiteurStructure(currentDataSource.getEnteteFacture())) {
                // si l'adresse n'est pas trouv? en DB, alors chargement d'une adresse Combin?
                qrFacture.setDebfAdressTyp(ReferenceQR.COMBINE);
                // S'il s'agit d'une adresse combin?, et que le nombre de caract?re d?passe les 70
                // Il faut donc s?par? l'adresse sur deux lignes, et mettre la deuxi?me partie sur la ligne 2
                String adresseDebiteur = currentDataSource.getAdressePrincipale();
                try {
                    qrFacture.insertAdresseDebFAsStringInQrFacture(adresseDebiteur);
                } catch (Exception e) {
                    LOG.info(this.getClass().getName() + " - Erreur lors de recherche de l'adresse Debiteur : " + e.getMessage());
                }

            }
            qrFacture.genererReferenceQRFact(currentDataSource.getEnteteFacture());

            // Il n'existe pas pour l'heure actuel d'adresse de cr?diteur en DB.
            // Elle est r?cup?r?e depuis le catalogue de texte au format Combin?e
            qrFacture.genererCreAdresse();
        } catch (Exception e) {

            getMemoryLog().logMessage(
                    "Erreur lors de recherche des ?lements de la sommation : " + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

    }

    public boolean genererAdresseDebiteurStructure(FAEnteteFacture enteteFacture) throws Exception {
        String courrier;
        String domaineCourrier;
        String datePassage = "";
        if (!JadeStringUtil.isIntegerEmpty(enteteFacture.getIdTypeCourrier())) {
            courrier = enteteFacture.getIdTypeCourrier();
        } else {
            courrier = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        }
        if (!JadeStringUtil.isIntegerEmpty(enteteFacture.getIdDomaineCourrier())) {
            domaineCourrier = enteteFacture.getIdDomaineCourrier();
        } else {
            domaineCourrier = IConstantes.CS_APPLICATION_FACTURATION;
        }
        if(enteteFacture.getPassage() != null){
            datePassage = enteteFacture.getPassage().getDateFacturation();
    }
        return qrFacture.genererAdresseDebiteur(enteteFacture.getIdTiers(), courrier, domaineCourrier, enteteFacture.getIdExterneRole(), true, datePassage);
    }

    protected void initCommonVar() {

        tmpCurrency = new FWCurrency(currentDataSource.getEnteteFacture().getTotalFacture());

        if (!(tmpCurrency.isNegative() || tmpCurrency.isZero())) {
            _initMontant();
        }

        if (isFactureMontantReport() && modeReporterMontantMinime) {
            reporterMontant = true;
        } else {
            reporterMontant = false;
        }

        // commencer ? ?crire les param?tres

        adresseDebiteur = currentDataSource.getAdressePrincipale();

    }

    public void _initMontant() {
        String montantFacture = JANumberFormatter.deQuote(currentDataSource.getEnteteFacture().getTotalFacture());
        // convertir le montant en entier (BigInteger)
        montantSansCentime = JAUtil.createBigDecimal(montantFacture).toBigInteger().toString();

        java.math.BigDecimal montantSansCentimeBig = JAUtil.createBigDecimal(montantSansCentime);
        // convertir le montant avec centimes en BigDecimal
        java.math.BigDecimal montantAvecCentimeBig = JAUtil.createBigDecimal(montantFacture);

        // les centimes repr?sent?s en entier
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
     * Renvoie la r?f?rence BVR.
     *
     * @return la r?f?rence BVR.
     */
    public ReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new ReferenceBVR();
        }
        return bvr;
    }

    /**
     * Renvoie la r?f?rence QR.
     *
     * @return la r?f?rence QR.
     */
    public ReferenceQR getQrFacture() {
        return qrFacture;
    }



    /**
     * Retourne le template Jasper PRINCIPAL utilis? pour g?n?rer le document. (p.ex : DOCUMENT_VIDE pour
     * DOCUMENT_VIDE.japser)
     *
     * @return String repr?sentant le fichier jasper pour la g?n?ration du document.
     */
    public abstract String getJasperTemplate();

}
