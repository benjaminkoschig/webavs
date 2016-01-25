/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.topaz.regime;

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
import globaz.prestation.tools.PRStringUtils;
import java.util.Vector;
import ch.globaz.cygnus.process.document.RFDocumentEnum;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * author fha
 */
public class RFPrescriptionDietetiqueFormulaireOO extends RFAbstractDocumentOO {

    public static final String FICHIER_MODELE_DOCUMENTS_RFM = "RF_LETTRE_TYPE_REGIME_PRESCRIPTION_DIETETIQUE";
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
        documentHelper.setCsTypeDocument(IRFCatalogueTexte.CS_ANNEXE_PRESCRIPTION_DIETETIQUE_FORMULAIRE);
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
            caisseHelper.setTemplateName(RFPrescriptionDietetiqueFormulaireOO.FICHIER_MODELE_DOCUMENTS_RFM);

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
        data.addData("idProcess", "RFPrescriptionDietetiqueFormulaireOO");

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
            setTitre(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_TITRE));

            PRInfoCompl pr = new PRInfoCompl();
            Vector v = new Vector();
            v = PRAffiliationHelper.getAffiliationsTiers(session, idTiers);

            dateNaissance = tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);

            PersonneEtendueComplexModel pecm = new PersonneEtendueComplexModel();
            pecm.getPersonne().setIdTiers(idTiers);
            pecm.getPersonne().getDateNaissance();

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

        return super.remplir(documentContainer, process, getIdTiers(),
                RFDocumentEnum.RFM_LETTRE_TYPE_REGIME_PRESCRIPTION_DIETETIQUE_FORMULAIRE.getNoInforom(), miseEnGed);
    }

    @Override
    public void remplirDocument() throws Exception {

        try {
            // TITRE_DOC
            data.addData("TITRE_DOC", documentPrincipale.getTextes(1).getTexte(1).getDescription());
            // DETAIL_TITRE
            data.addData("DETAIL_TITRE", documentPrincipale.getTextes(1).getTexte(2).getDescription());
            // %=DONNEES_PERSONNELLES=%
            data.addData("DONNEES_PERSONNELLES", documentPrincipale.getTextes(2).getTexte(1).getDescription());
            // %=NOM=%
            data.addData("NOM_ASSURE", documentPrincipale.getTextes(2).getTexte(2).getDescription());
            // %=PRENOM=%
            data.addData("PRENOM_ASSURE", documentPrincipale.getTextes(2).getTexte(3).getDescription());
            // %=DATE_NAISSANCE=%
            data.addData("DATE_NAISSANCE", documentPrincipale.getTextes(2).getTexte(4).getDescription());
            // %=ADRESSE=%
            data.addData("ADRESSE_ASSURE", documentPrincipale.getTextes(2).getTexte(5).getDescription());
            // %=NPA=%
            data.addData("NPA_ASSURE", documentPrincipale.getTextes(2).getTexte(6).getDescription());
            // %=TEL_PRIVE=%
            data.addData("TEL_PRIVE", documentPrincipale.getTextes(2).getTexte(7).getDescription());
            // %=EMPLOYEUR=%
            data.addData("EMPLOYEUR", documentPrincipale.getTextes(2).getTexte(8).getDescription());
            // %=TEL_PROF=%
            data.addData("TEL_PROF", documentPrincipale.getTextes(2).getTexte(9).getDescription());
            // %=ASSUREUR_MALADIE=%
            data.addData("ASSUREUR_MALADIE", documentPrincipale.getTextes(2).getTexte(10).getDescription());
            // %=NUMERO_ASSURE=%
            data.addData("NUMERO_ASSURE", documentPrincipale.getTextes(2).getTexte(11).getDescription());
            // %=MALADIE=%
            data.addData("MALADIE", documentPrincipale.getTextes(2).getTexte(12).getDescription());
            // %=ACCIDENT=%
            data.addData("ACCIDENT", documentPrincipale.getTextes(2).getTexte(13).getDescription());
            // %=INVALIDITE=%
            data.addData("INVALIDITE", documentPrincipale.getTextes(2).getTexte(14).getDescription());
            // %=NB_CONSULTATION=%
            data.addData("NB_CONSULTATION", documentPrincipale.getTextes(2).getTexte(15).getDescription());

            // %=ADRESSE=%
            data.addData("ADRESSE", PRStringUtils.replaceString(documentPrincipale.getTextes(3).getTexte(1)
                    .getDescription(), ADRESSE, adresseComplete));

            // %=MALADIE=%
            data.addData("MALADIE", documentPrincipale.getTextes(4).getTexte(1).getDescription());
            // %=METABOLISME=%
            data.addData("METABOLISME", documentPrincipale.getTextes(4).getTexte(2).getDescription());
            // %=OBESITE=%
            data.addData("OBESITE", documentPrincipale.getTextes(4).getTexte(3).getDescription());
            // %=CARDIO=%
            data.addData("CARDIO", documentPrincipale.getTextes(4).getTexte(4).getDescription());
            // %=DIGESTIF=%
            data.addData("DIGESTIF", documentPrincipale.getTextes(4).getTexte(5).getDescription());
            // %=REINS=%
            data.addData("REINS", documentPrincipale.getTextes(4).getTexte(6).getDescription());
            // %=MALNUTRITION=%
            data.addData("MALNUTRITION", documentPrincipale.getTextes(4).getTexte(7).getDescription());
            // %=ALIMENTAIRE=%
            data.addData("ALIMENTAIRE", documentPrincipale.getTextes(4).getTexte(8).getDescription());
            // %=DATE=%
            data.addData("DATE", documentPrincipale.getTextes(5).getTexte(1).getDescription());
            // %=SIG1=%
            data.addData("SIG1", documentPrincipale.getTextes(5).getTexte(2).getDescription());
            // %=SIG2=%
            data.addData("SIG2", documentPrincipale.getTextes(5).getTexte(3).getDescription());
            // %=MEDECIN=%
            data.addData("MEDECIN", documentPrincipale.getTextes(5).getTexte(4).getDescription());
            // %=DIETETICIENNE=%
            data.addData("DIETETICIENNE", documentPrincipale.getTextes(5).getTexte(5).getDescription());
            // %=TIMBRE=%
            data.addData("TIMBRE", documentPrincipale.getTextes(5).getTexte(6).getDescription());
            // %=POUR_DIETETICIENNE=%
            data.addData("POUR_DIETETICIENNE", documentPrincipale.getTextes(6).getTexte(1).getDescription());
            // %=MEDICAMENTS=%
            data.addData("MEDICAMENTS", documentPrincipale.getTextes(6).getTexte(2).getDescription());
            // %=RES_ANALYSE=%
            data.addData("RES_ANALYSE", documentPrincipale.getTextes(6).getTexte(3).getDescription());
            // %=DIAGNOSTIC=%
            data.addData("DIAGNOSTIC", documentPrincipale.getTextes(6).getTexte(4).getDescription());
            // %=REMARQUE=%
            data.addData("REMARQUE", documentPrincipale.getTextes(6).getTexte(5).getDescription());
            // %=AMMEXE=%
            data.addData("RAPPORT", documentPrincipale.getTextes(7).getTexte(1).getDescription());
            // %=RAPPORT=%
            data.addData("RAPPORT", documentPrincipale.getTextes(7).getTexte(2).getDescription());
            // %=ECRIT=%
            data.addData("ECRIT", documentPrincipale.getTextes(7).getTexte(3).getDescription());
            // %=TELEPHONE=%
            data.addData("TELEPHONE", documentPrincipale.getTextes(7).getTexte(4).getDescription());

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

}
