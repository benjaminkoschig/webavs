package globaz.cygnus.topaz.regime;

/*
 * author fha
 */
import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.cygnus.api.codesystem.IRFCatalogueTexte;
import globaz.cygnus.process.RFDocumentsProcess;
import globaz.cygnus.topaz.RFAbstractDocumentOO;
import globaz.cygnus.vb.decisions.RFCopieDecisionsValidationData;
import globaz.globall.util.JACalendar;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import java.util.Vector;
import ch.globaz.cygnus.process.document.RFDocumentEnum;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RFFormulaireCompleterCMSOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_DOCUMENTS";
    protected String adresse = "";
    protected final String ADRESSE = "{adresse}";
    // constantes utilisés pour remplir le document
    protected final String ADRESSE_ASSURE = "{adresseAssure}";
    protected String adresseComplete = "";
    protected final String CAISSE_MALADIE = "{caisseMaladieAssure}";
    protected String caisseMaladie = "";
    protected String case_postale = "";
    protected final String CASE_POSTALE = "{casePostaleAssure}";
    protected String complement = "";
    // aussi dateNaissance, employeur, tel prive prof, caisse mal, numero avs
    protected final String DATE_NAISSANCE = "{dateNaissanceAssure}";
    protected String dateNaissance = "";
    protected String employeur = "";
    protected final String EMPLOYEUR = "{employeurAssure}";

    protected String lieu = "";
    protected final String LIEU = "{lieuAssure}";
    protected String npa = "";
    protected final String NPA = "{npaAssure}";
    protected final String NUMERO_AVS = "{avsAssure}";
    protected String numeroAvs = "";
    protected final String TEL_PRIVE = "{telPriveAssure}";
    protected final String TEL_PROF = "{telProfAssure}";
    protected String telPrive = "";
    protected String telProf = "";
    protected String titre = "";

    protected final String TITRE = "{titreAssure}";

    @Override
    public void chargerCatalogueTexte() throws Exception {

        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IRFCatalogueTexte.CS_RFM);
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_FORMULAIRE_EVALUATION_CMS);
        documentHelper.setNom("openOffice");
        documentHelper.setDefault(Boolean.FALSE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            documentPrincipale = documents[0];
        }
    }

    // peut sans doute mettre dans la classe mere
    @Override
    public void chargerDonneesEnTete() throws Exception {

        try {
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            // Recherche de l'adresse du tiers
            String adresse = PRTiersHelper.getAdresseDomicileFormatee(session, idTiers).toString();

            crBean.setAdresse(adresse);

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName(RFFormulaireCompleterCMSOO.FICHIER_MODELE_DOCUMENTS_RFM);

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            // Chargement du Header
            data.addData("idEntete", "HEADER_CAISSE");

        } catch (Exception e) {
            throw new Exception(e.toString());
        }

    }

    @Override
    public void generationLettre(RFCopieDecisionsValidationData... copie) throws Exception {

        // Chargement informations principales
        data = new DocumentData();

        // Chargement du document
        data.addData("idProcess", "RFFormulaireCompleterCMSOO");

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
            titre = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_TITRE);
            adresseComplete = PRTiersHelper.getAdresseCourrierFormatee(getSession(), idTiers, null, "");

            PRInfoCompl pr = new PRInfoCompl();
            Vector v = new Vector();
            v = PRAffiliationHelper.getAffiliationsTiers(session, idTiers);

            dateNaissance = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

            PersonneEtendueComplexModel pecm = new PersonneEtendueComplexModel();
            pecm.getPersonne().setIdTiers(idTiers);
            pecm.getPersonne().getDateNaissance();

            // //////////////
            employeur = "";
            telPrive = "";
            telProf = "";
            caisseMaladie = pr.getNoCaisse();

            numeroAvs = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            adresse = "";
            npa = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA);
            lieu = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA_SUP);

            // Chargement des catalogues de texte
            chargerCatalogueTexte();

            // Création des paramètres pour l'en-tête
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout des copies
            // ajoutCopiesAnnexes();
            setDocumentData(data);

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (RFDecisionRefusOO.generationLettre())");
        }

    }

    public JadePrintDocumentContainer remplir(JadePrintDocumentContainer documentContainer, RFDocumentsProcess process,
            boolean miseEnGed) throws Exception {

        setSession(process.getSession());
        setDateSurDocument(process.getDateDocument());
        setIdTiers(process.getIdTiers());
        setIsCopie(isCopie);

        return super.remplir(documentContainer, process, process.getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_FORMULAIRE_EVALUATION_AU_CMS.getNoInforom(), miseEnGed);
    }

    // à faire !!!
    @Override
    public void remplirDocument() throws Exception {

        try {
            // %=OBJET=%
            data.addData("OBJET", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // %=BUT=%
            data.addData("BUT", documentPrincipale.getTextes(1).getTexte(2).getDescription());
            // %=INFO_BENEF=%
            data.addData("INFO_BENEF", documentPrincipale.getTextes(1).getTexte(3).getDescription());
            // %=BENEFICIAIRE=%
            data.addData("BENEFICIAIRE", documentPrincipale.getTextes(2).getTexte(1).getDescription());
            // %=NOM=%
            data.addData("NOM", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=DATE_NAISSANCE=%
            data.addData("DATE_NAISSANCE", documentPrincipale.getTextes(2).getTexte(3).getDescription());
            // %=NSS=%
            data.addData("NSS", documentPrincipale.getTextes(2).getTexte(4).getDescription());
            // %=ADRESSE=%
            data.addData("ADRESSE", documentPrincipale.getTextes(2).getTexte(5).getDescription());
            // %=TELEPHONE=%
            data.addData("TELEPHONE", documentPrincipale.getTextes(2).getTexte(6).getDescription());
            // %=DIETETICIENNE=%
            data.addData("DIETETICIENNE", documentPrincipale.getTextes(3).getTexte(1).getDescription());
            // %=SANTE_BENEF=%
            data.addData("SANTE_BENEF", documentPrincipale.getTextes(3).getTexte(2).getDescription());
            // %=MOTIF=%
            data.addData("MOTIF", documentPrincipale.getTextes(3).getTexte(3).getDescription());
            // %=BENEF_TAB=%
            data.addData("BENEF_TAB", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=AGE=%
            data.addData("AGE", documentPrincipale.getTextes(4).getTexte(2).getDescription());
            // %=MALADIE=%
            data.addData("MALADIE", documentPrincipale.getTextes(4).getTexte(3).getDescription());
            // %=ACCIDENT=%
            data.addData("ACCIDENT", documentPrincipale.getTextes(4).getTexte(4).getDescription());
            // %=INVALIDITE=%
            data.addData("INVALIDITE", documentPrincipale.getTextes(4).getTexte(5).getDescription());
            // %=FRAIS_REGIME=%
            data.addData("FRAIS_REGIME", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=SANTE=%
            data.addData("SANTE", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=GLUTEN=%
            data.addData("GLUTEN", documentPrincipale.getTextes(5).getTexte(3).getDescription());
            // %=LACTOSE=%
            data.addData("LACTOSE", documentPrincipale.getTextes(5).getTexte(4).getDescription());
            // %=DENUTRITION=%
            data.addData("DENUTRITION", documentPrincipale.getTextes(5).getTexte(5).getDescription());
            // %=AUTRE=%
            data.addData("AUTRE", documentPrincipale.getTextes(5).getTexte(6).getDescription());
            // %=CERTIFICAT=%
            data.addData("CERTIFICAT", documentPrincipale.getTextes(5).getTexte(7).getDescription());
            // %=SURCOUT=%
            data.addData("SURCOUT", documentPrincipale.getTextes(5).getTexte(8).getDescription());
            // %=DUREE=%
            data.addData("DUREE", documentPrincipale.getTextes(6).getTexte(1).getDescription());
            // %=DUREE_DETERMINEE=%
            data.addData("DUREE_DETERMINEE", documentPrincipale.getTextes(6).getTexte(2).getDescription());
            // %=DUREE_INDETERMINEE=%
            data.addData("DUREE_INDETERMINEE", documentPrincipale.getTextes(6).getTexte(3).getDescription());
            // %=REEVALUATION=%
            data.addData("REEVALUATION", documentPrincipale.getTextes(6).getTexte(4).getDescription());
            // %=REMARQUE=%
            data.addData("REMARQUE", documentPrincipale.getTextes(7).getTexte(1).getDescription());
            // %=NOM_EVALUATEUR=%
            data.addData("NOM_EVALUATEUR", documentPrincipale.getTextes(7).getTexte(2).getDescription());
            // %=TEL=%
            data.addData("TEL", documentPrincipale.getTextes(7).getTexte(3).getDescription());
            // %=DATE=%
            data.addData("DATE", documentPrincipale.getTextes(7).getTexte(4).getDescription());
            // %=SIGNATURE=%
            data.addData("SIGNATURE", documentPrincipale.getTextes(7).getTexte(5).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

}
