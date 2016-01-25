package globaz.cygnus.topaz;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.api.decisions.IRFCodesIsoLangue;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.pyxis.api.ITITiers;
import java.util.Hashtable;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author fha
 */
public class RFLettreBordereauOO {
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public static final String DOMAINE_CYGNUS = "CYGNUS";
    public static final String FICHIER_MODELE_ANNEXE_RFM = "RFM_BORBEREAU_CCVD";
    private ICaisseReportHelperOO caisseHelper;
    private Hashtable catalogueMultiLangue = new Hashtable();
    private String catTexteBordereau = "catTexteBordereau";
    private String codeIsoLangue = "";
    private DocumentData data;
    private String dateDecision = "";
    private String dateDocument = "";
    private ICTDocument document;
    private DocumentData documentData;
    private ICTDocument documentHelper;
    private String domaineLettreEnTete = "";
    private String idAffilie;
    private String idTiers = "";
    private boolean isDemandeCompensation = false;
    private Boolean isEnteteOAI = Boolean.FALSE;
    private int nbTrue = 0;
    private String nom = "";
    private String noOfficeAI = "";
    private String nss = "";
    private String numeroDeDecisionAI = "";
    private String periodeDecision = "";
    private String prenom = "";
    private BSession session;
    private String specificHeader = "";
    private PRTiersWrapper tierAdresse;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    protected PRTiersWrapper tiersWrapper;

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    private void chargementCatalogueTexte() throws Exception {

        // chargement du catalogue de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());

        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_DECISION_ANNEXE_BORDEREAU);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        // Chargement des textes en français
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_FR);
        ICTDocument[] documentsFr = documentHelper.load();
        catalogueMultiLangue.put(catTexteBordereau + "_" + IRFCodesIsoLangue.LANGUE_ISO_FR, documentsFr[0]);

        // Chargement des textes en allemand
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_DE);
        ICTDocument[] documentsDe = documentHelper.load();
        catalogueMultiLangue.put(catTexteBordereau + "_" + IRFCodesIsoLangue.LANGUE_ISO_DE, documentsDe[0]);

        // Chargement des textes en italien
        documentHelper.setCodeIsoLangue(IRFCodesIsoLangue.LANGUE_ISO_IT);
        ICTDocument[] documentsIt = documentHelper.load();
        catalogueMultiLangue.put(catTexteBordereau + "_" + IRFCodesIsoLangue.LANGUE_ISO_IT, documentsIt[0]);

    }

    public DocumentData generationLettre() throws Exception {

        // Retrieve d'informations pour la création de la décision
        tiersWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);
        if (null == tiersWrapper) {
            tiersWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tiersWrapper) {
            codeIsoLangue = getSession().getCode(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            nom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);
            prenom = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            nss = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

            chargementCatalogueTexte();

            data = new DocumentData();

            // Chargement du template
            data.addData("idProcess", "RFLettreBordereauOO");

            remplissageDocument();

            return getDocumentData();

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (RFLettreBordereauOO.generationLettre())");
        }
    }

    private void gestionEnTeteEtSignature() throws Exception {

    }

    /**
     * Methode pour formatter une adresse dans le catalogue de texte. Remplace les virgules par des retours à la ligne.
     * 
     * @param texteInitiale
     * @return
     */
    private String getAdresseCatTextFormattee(String texteInitiale) {
        return texteInitiale.replaceAll(",", "\n");
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getDomaineLettreEnTete() {
        return domaineLettreEnTete;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsEnteteOAI() {
        return isEnteteOAI;
    }

    public int getNbTrue() {
        return nbTrue;
    }

    public String getNom() {
        return nom;
    }

    public String getNoOfficeAI() {
        return noOfficeAI;
    }

    public String getNss() {
        return nss;
    }

    public String getNumeroDeDecisionAI() {
        return numeroDeDecisionAI;
    }

    public String getPeriodeDecision() {
        return periodeDecision;
    }

    public String getPrenom() {
        return prenom;
    }

    public BSession getSession() {
        return session;
    }

    public String getSpecificHeader() {
        return specificHeader;
    }

    public PRTiersWrapper getTierAdresse() {
        return tierAdresse;
    }

    public boolean isDemandeCompensation() {
        return isDemandeCompensation;
    }

    private void remplissageDocument() {

        try {
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            String adresse = "";

            if (getDomaineLettreEnTete().equals(RFLettreBordereauOO.DOMAINE_CYGNUS)) {
                adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                        tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(),
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
            }

            crBean.setAdresse(adresse);

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);

            if (getDomaineLettreEnTete().equals(RFLettreBordereauOO.DOMAINE_CYGNUS)) {
                caisseHelper.setTemplateName(RFLettreBordereauOO.FICHIER_MODELE_ANNEXE_RFM);
            }

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable params = new Hashtable();
            params.put(ITITiers.FIND_FOR_IDTIERS, tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }

            StringBuffer buffer = new StringBuffer();

            buffer.append(tiersTitre.getFormulePolitesse(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));

            // Si langue français, utilisation du texte en français
            if (IRFCodesIsoLangue.LANGUE_ISO_FR.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteBordereau + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_FR);
            }
            // Sinon si langue allemande, utilisation du texte en allemand
            else if (IRFCodesIsoLangue.LANGUE_ISO_DE.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteBordereau + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_DE);
            }
            // Sinon si langue en italien, utilisation du texte en italien
            else if (IRFCodesIsoLangue.LANGUE_ISO_IT.equals(getSession().getCode(
                    tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE)))) {
                document = (ICTDocument) catalogueMultiLangue.get(catTexteBordereau + "_"
                        + IRFCodesIsoLangue.LANGUE_ISO_IT);
            }
            // Sinon, erreur dans le chargement du catalogue de texte
            else {
                throw new Exception(
                        "Erreur dans le chargement du texte de la lettre en tête / gestionEnTeteEtSignature");
            }

            data.addData("NSS", document.getTextes(1).getTexte(1).getDescription());
            data.addData("NUMERO_NSS", nss);
            data.addData("NOM", document.getTextes(1).getTexte(2).getDescription());
            data.addData("BENEFICIAIRE", nom + " " + prenom);
            data.addData("BORDEREAU", document.getTextes(2).getTexte(2).getDescription());
            // Ajout de l'adresse de l'agence, si celle-ci est présente en catalogue de textes.
            try {

                data.addData("ADRESSE_CAISSE", getAdresseCatTextFormattee(document.getTextes(2).getTexte(1)
                        .getDescription()));
            } catch (IndexOutOfBoundsException e) {
                // HACK : on ne remonte pas d'erreur si pas de texte.
                // L'absence de ce texte peut arriver dans certaines caisses.
            }

            setDocumentData(data);

        } catch (Exception e) {

            e.getMessage();
        }
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDemandeCompensation(boolean isDemandeCompensation) {
        this.isDemandeCompensation = isDemandeCompensation;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setDomaineLettreEnTete(String domaineLettreEnTete) {
        this.domaineLettreEnTete = domaineLettreEnTete;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsEnteteOAI(Boolean isEnteteOAI) {
        this.isEnteteOAI = isEnteteOAI;
    }

    public void setNbTrue(int nbTrue) {
        this.nbTrue = nbTrue;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNoOfficeAI(String noOfficeAI) {
        this.noOfficeAI = noOfficeAI;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumeroDeDecisionAI(String numeroDeDecisionAI) {
        this.numeroDeDecisionAI = numeroDeDecisionAI;
    }

    public void setPeriodeDecision(String periodeDecision) {
        this.periodeDecision = periodeDecision;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setSpecificHeader(String specificHeader) {
        this.specificHeader = specificHeader;
    }

    public void setTierAdresse(PRTiersWrapper tierAdresse) {
        this.tierAdresse = tierAdresse;
    }
}
