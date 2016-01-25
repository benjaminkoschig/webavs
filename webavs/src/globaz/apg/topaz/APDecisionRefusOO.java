package globaz.apg.topaz;

import globaz.apg.api.codesystem.IAPCatalogueTexte;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.vb.droits.APInfoComplViewBean;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.doc.ICTScalableDocumentAnnexe;
import globaz.babel.api.doc.ICTScalableDocumentCopie;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelperOO;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater04;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiers;
import java.util.Hashtable;
import java.util.Iterator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Création de la décision de refus pour la MAT
 * 
 * @author JJE
 */
public class APDecisionRefusOO {

    private static final String CDT_ADRESSECAISSE = "{adresseCaisse}";
    private static final String CDT_TITRE = "{titre}";
    private Iterator annexes = null;
    private ICaisseReportHelperOO caisseHelper;

    private String codeIsoLangue = "";
    private Iterator copies = null;
    private DocumentData data;
    private String dateSurDocument = "";
    private ICTDocument document;

    private DocumentData documentData;

    private ICTDocument documentHelper;

    private String domaineLettreEnTete = "";

    private String idDroit = "";
    private String idTiers = "";

    private boolean isCopie = false;

    private BSession session;
    private PRTiersWrapper tiersWrapper;

    private void ajoutCopiesAnnexes() throws Exception {

        StringBuffer strAnnexesCopiesTitreBfr = new StringBuffer();
        StringBuffer strAnnexesCopiesValeurBfr = new StringBuffer();
        StringBuffer strDeuxPointsBfr = new StringBuffer();

        if (null != copies && copies.hasNext()) {
            // Ajout du libelle "copie à:"
            strAnnexesCopiesTitreBfr.append(document.getTextes(4).getTexte(1).getDescription());
            strDeuxPointsBfr.append(document.getTextes(4).getTexte(4).getDescription());
            // Ajout des copies
            while (copies.hasNext()) {
                ICTScalableDocumentCopie intervenantCopie = (ICTScalableDocumentCopie) copies.next();
                if (null != intervenantCopie) {
                    if (copies.hasNext()) {
                        strAnnexesCopiesValeurBfr.append(getAdresseTiers(intervenantCopie.getIdTiers()) + "\n");
                        strAnnexesCopiesTitreBfr.append("\n");
                        strDeuxPointsBfr.append("\n");
                    } else {
                        strAnnexesCopiesValeurBfr.append(getAdresseTiers(intervenantCopie.getIdTiers()));
                    }
                }
            }
        }

        if (null != annexes && annexes.hasNext()) {
            // Ajout du libelle "annexes:"
            if (null != copies) {
                strAnnexesCopiesTitreBfr.append("\n\n" + document.getTextes(4).getTexte(2).getDescription());
                strAnnexesCopiesValeurBfr.append("\n\n");
                strDeuxPointsBfr.append("\n\n" + document.getTextes(4).getTexte(4).getDescription());
            } else {
                strAnnexesCopiesTitreBfr.append(document.getTextes(4).getTexte(2).getDescription());
                strDeuxPointsBfr.append(document.getTextes(4).getTexte(4).getDescription());
            }
            // Ajout des annexes
            while (annexes.hasNext()) {
                ICTScalableDocumentAnnexe nomDocumentAnnexe = (ICTScalableDocumentAnnexe) annexes.next();
                if (null != nomDocumentAnnexe) {
                    if (annexes.hasNext()) {
                        strAnnexesCopiesValeurBfr.append(nomDocumentAnnexe.getLibelle() + "\n");
                        strAnnexesCopiesTitreBfr.append("\n");
                        strDeuxPointsBfr.append("\n");
                    } else {
                        strAnnexesCopiesValeurBfr.append(nomDocumentAnnexe.getLibelle());
                    }
                }
            }
        }

        if (!JadeStringUtil.isEmpty(strAnnexesCopiesTitreBfr.toString())) {
            data.addData("TITRE_ANNEXE_COPIE", strAnnexesCopiesTitreBfr.toString());
            data.addData("VALEUR_ANNEXE_COPIE", strAnnexesCopiesValeurBfr.toString());
            data.addData("DP", strDeuxPointsBfr.toString());
        }
    }

    private void chargerCatalogueTexte() throws Exception {

        // creation du helper pour le catalogue de texte
        documentHelper = PRBabelHelper.getDocumentHelper(getSession());
        documentHelper.setCsDomaine(IAPCatalogueTexte.CS_MATERNITE);
        documentHelper.setCsTypeDocument(IAPCatalogueTexte.CS_DECISION_REFUS_MAT);
        documentHelper.setDefault(Boolean.TRUE);
        documentHelper.setActif(Boolean.TRUE);
        documentHelper.setCodeIsoLangue(codeIsoLangue);

        ICTDocument[] documents = documentHelper.load();

        if ((documents == null) || (documents.length == 0)) {
            throw new Exception(getSession().getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
        } else {
            document = documents[0];
        }
    }

    private void chargerDonneesEnTete() throws Exception {

        try {

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            // Recherche de l'adresse du tiers
            String adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(),
                    tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), "",
                    APApplication.CS_DOMAINE_ADRESSE_APG);

            crBean.setAdresse(adresse);

            // Lieu et date
            crBean.setDate(JACalendar.format(dateSurDocument, codeIsoLangue));

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(getSession().getApplication().getProperty(APApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            // Ajout du NSS
            crBean.setNoAvs(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            // Ajouter la mention traité par :
            /*
             * APDroitMaternite apDroitMaternite = new APDroitMaternite(); apDroitMaternite.setIdDroit(idDroit);
             * apDroitMaternite.setSession(getSession()); apDroitMaternite.retrieve();
             * 
             * if (!apDroitMaternite.isNew()){ if (getSession().getApplication().
             * getProperty("isTraitePar").equals("true")){ try { JadeUser userName =
             * getSession().getApplication()._getSecurityManager().getUserForVisa (getSession(),
             * apDroitMaternite.getIdGestionnaire()); String user = userName.getFirstname () + " " +
             * userName.getLastname(); crBean. setNomCollaborateur(getSession().getLabel("LISTE_DECISION") + " " +
             * user); crBean.setTelCollaborateur(userName.getPhone()); } catch (Exception e) { throw new
             * Exception(e.toString()); } } }
             */

            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelperOO(getSession().getApplication(),
                    codeIsoLangue);
            caisseHelper.setTemplateName("AP_DECISION_REFUS");

            // Insertion de l'entête
            data = caisseHelper.addHeaderParameters(data, crBean, Boolean.FALSE);

            // Insertion de la signature
            data = caisseHelper.addSignatureParameters(data, crBean);

            // utilise le fichier topaz-config.xml
            data.addData("idEntete", "CAISSE");

        } catch (Exception e) {
            throw new Exception(e.toString());
        }

    }

    private boolean contientLamatDansSP() throws Exception {

        boolean contientLamat = false;

        try {

            APSituationProfessionnelleManager apSitProMgr = new APSituationProfessionnelleManager();
            apSitProMgr.setSession(getSession());
            apSitProMgr.setForIdDroit(idDroit);
            apSitProMgr.find();

            for (Iterator iter = apSitProMgr.iterator(); iter.hasNext();) {
                APSituationProfessionnelle apSitPro = (APSituationProfessionnelle) iter.next();
                if (apSitPro.getHasLaMatPrestations().booleanValue()) {
                    contientLamat = true;
                }
            }

        } catch (Exception e) {
            throw new Exception(e.toString());
        }

        return contientLamat;
    }

    public void generationLettre() throws Exception {

        // Chargement informations principales
        data = new DocumentData();
        data.addData("idProcess", "APDecisionRefusOO");

        // Retrieve d'informations pour la création de la décision
        tiersWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);
        if (null == tiersWrapper) {
            tiersWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
        }

        if (null != tiersWrapper) {

            codeIsoLangue = getSession().getCode(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            // Chargement du catalogue de texte
            chargerCatalogueTexte();

            // Création des paramètres pour l'en-tête
            chargerDonneesEnTete();

            // Remplissage des champs du document
            remplirDocument();

            // Ajout des copies
            ajoutCopiesAnnexes();

            setDocumentData(data);

        } else {
            throw new Exception("Erreur : Pas d'adresse tiers (APDecisionRefusOO.generationLettre())");
        }

    }

    private String getAdresseTiers(String idTiers) throws Exception {

        TITiers tiTierCopie = new TITiers();
        tiTierCopie.setSession(getSession());
        tiTierCopie.setIdTiers(idTiers);
        tiTierCopie.retrieve();

        if (null != tiTierCopie) {
            return tiTierCopie.getAdresseAsString(TIAvoirAdresse.CS_COURRIER, APApplication.CS_DOMAINE_ADRESSE_APG,
                    JACalendar.todayJJsMMsAAAA(), new PRTiersAdresseCopyFormater04());
        } else {
            return "";
        }
    }

    public Iterator getAnnexes() {
        return annexes;
    }

    public Iterator getCopies() {
        return copies;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public DocumentData getDocumentData() {
        return documentData;
    }

    public String getDomaineLettreEnTete() {
        return domaineLettreEnTete;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public BSession getSession() {
        return session;
    }

    public PRTiersWrapper getTiersWrapper() {
        return tiersWrapper;
    }

    public boolean isCopie() {
        return isCopie;
    }

    private void remplirDocument() throws Exception {

        try {

            // Si copie, on renseigne la champ TEXTE_COPIE
            if (isCopie()) {
                data.addData("TEXTE_COPIE", document.getTextes(4).getTexte(1).getDescription());
            }

            // Objet
            data.addData("LETTRE_CONCERNE", document.getTextes(1).getTexte(1).getDescription());

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable params = new Hashtable();
            params.put(ITITiers.FIND_FOR_IDTIERS, idTiers);
            ITITiers[] t = tiersTitre.findTiers(params);
            if (t != null && t.length > 0) {
                tiersTitre = t[0];
            }

            data.addData("titreTiers",
                    PRStringUtils.replaceString(document.getTextes(1).getTexte(2).getDescription(), CDT_TITRE,
                            tiersTitre.getFormulePolitesse(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));

            // Insertion des deux premiers paragraphes
            if (contientLamatDansSP()) {
                data.addData("LETTRE_TEXTE_P1", document.getTextes(1).getTexte(4).getDescription());
                data.addData("LETTRE_TEXTE_P2", document.getTextes(1).getTexte(6).getDescription());
            } else {
                data.addData("LETTRE_TEXTE_P1", document.getTextes(1).getTexte(3).getDescription());
                data.addData("LETTRE_TEXTE_P2", document.getTextes(1).getTexte(5).getDescription());
            }

            data.addData("LETTRE_TEXTE_P3", document.getTextes(1).getTexte(8).getDescription());
            data.addData("LETTRE_TEXTE_P4", document.getTextes(1).getTexte(9).getDescription());

            // Insertion du motif de refus
            APDroitLAPG apDroitLApg = new APDroitLAPG();
            apDroitLApg.setSession(getSession());
            apDroitLApg.setIdDroit(idDroit);
            apDroitLApg.retrieve();

            if (!apDroitLApg.isNew()) {

                APInfoComplViewBean apInfoCompVb = new APInfoComplViewBean();
                apInfoCompVb.setSession(getSession());
                apInfoCompVb.setIdInfoCompl(apDroitLApg.getIdInfoCompl());
                apInfoCompVb.retrieve();

                if (!apInfoCompVb.isNew()) {
                    if (!JadeStringUtil.isEmpty(apInfoCompVb.getMotif())) {
                        if (apInfoCompVb.getMotif().equals(IAPDroitLAPG.CS_SANS_ACTIVITE_ACCOUCHEMENT)) {
                            data.addData("LETTRE_TEXTE_MOTIF", document.getTextes(2).getTexte(4).getDescription());
                        } else if (apInfoCompVb.getMotif().equals(IAPDroitLAPG.CS_CINQ_MOIS_ACTIVITE)) {
                            data.addData("LETTRE_TEXTE_MOTIF", document.getTextes(2).getTexte(3).getDescription());
                        } else if (apInfoCompVb.getMotif().equals(IAPDroitLAPG.CS_NEUF_MOIS_ASSUJETTISSEMENT)) {
                            data.addData("LETTRE_TEXTE_MOTIF", document.getTextes(2).getTexte(2).getDescription());
                        } else if (apInfoCompVb.getMotif().equals(IAPDroitLAPG.CS_SANS_ACTIVITE_LUCRATIVE)) {
                            data.addData("LETTRE_TEXTE_MOTIF", document.getTextes(2).getTexte(1).getDescription());
                        }
                    } else {
                        throw new Exception("Erreur : Pas de motif de refus (APDecisionRefusOO.remplirDocument())");
                    }
                } else {
                    throw new Exception(
                            "Erreur : impossible de retrouver le motif de refus (APDecisionRefusOO.remplirDocument())");
                }
            }

            // Insertion du dernier paragraphe

            // Recherche de l'adresse de la caisse sur une seule ligne
            TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
            tiAdminCaisseMgr.setSession(session);
            tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                    session.getApplication()));
            tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
            tiAdminCaisseMgr.find();

            TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

            String idTiersCaisse = "";
            if (tiAdminCaisse != null) {
                idTiersCaisse = tiAdminCaisse.getIdTiersAdministration();
            }

            String adresseCaisse = "";
            adresseCaisse = PRTiersHelper.getAdresseCourrierFormatee(getSession(), idTiersCaisse, "",
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_APG);
            String[] adresseCaisseSplite = adresseCaisse.split("\n");
            StringBuffer adresseCaisseFinale = new StringBuffer();

            for (int i = 0; i < adresseCaisseSplite.length; i++) {
                if (i == adresseCaisseSplite.length - 1) {
                    adresseCaisseFinale.append(adresseCaisseSplite[i]);
                } else {
                    adresseCaisseFinale.append(adresseCaisseSplite[i] + ", ");
                }
            }

            data.addData("LETTRE_TEXTE_P5", PRStringUtils.replaceString(document.getTextes(1).getTexte(7)
                    .getDescription(), CDT_ADRESSECAISSE, adresseCaisseFinale.toString()));
            // Insertion des salutations
            data.addData("LETTRE_SALUTATIONS", PRStringUtils.replaceString(document.getTextes(3).getTexte(1)
                    .getDescription(), CDT_TITRE,
                    tiersTitre.getFormulePolitesse(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_LANGUE))));

        } catch (Exception e) {
            throw new Exception(e.toString());
        }

    }

    public void run() {
        // TODO Auto-generated method stub

    }

    public void setAnnexes(Iterator annexes) {
        this.annexes = annexes;
    }

    public void setCopies(Iterator copies) {
        this.copies = copies;
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDocumentData(DocumentData documentData) {
        this.documentData = documentData;
    }

    public void setDomaineLettreEnTete(String domaineLettreEnTete) {
        this.domaineLettreEnTete = domaineLettreEnTete;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsCopie(boolean isCopie) {
        this.isCopie = isCopie;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTiersWrapper(PRTiersWrapper tiersWrapper) {
        this.tiersWrapper = tiersWrapper;
    }

}
