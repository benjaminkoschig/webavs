package globaz.prestation.itext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.ij.application.IJApplication;
import globaz.ij.helpers.process.IJDecisionACaisseReportHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.external.IntRole;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersAdresseCopyFormater03;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRSession;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PRLettreEnTete extends FWIDocumentManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String CDT_SIGNATURE = "{Signature}";

    public static final String DOMAINE_APG = "APG";
    public static final String DOMAINE_CORVUS = "CORVUS";
    public static final String DOMAINE_IJAI = "IJAI";
    public static final String DOMAINE_MAT = "MAT";

    public static final String FICHIER_MODELE_ENTETE_APG = "AP_LETTRE_ENTETE";
    public static final String FICHIER_MODELE_ENTETE_CORVUS = "RE_LETTRE_ENTETE";
    public static final String FICHIER_MODELE_ENTETE_IJ = "IJ_LETTRE_ENTETE";

    private ICaisseReportHelper caisseHelper;
    private String codeIsoLangue = "";
    private String dateDecision = "";
    private ICTDocument document;
    private ICTDocument documentHelper;
    private String domaineLettreEnTete = "";
    private String idAffilie;
    private List<String> lignes = new LinkedList<String>();
    private int nbTrue = 0;
    private String noOfficeAI = "";
    private String numeroDeDecisionAI = "";
    private String periodeDecision = "";
    private String specificHeader = "";
    private PRTiersWrapper tierAdresse;

    @Override
    public void afterBuildReport() {

        if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_APG)
                || getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_MAT)) {
            getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LETTRE_ACCOMPAGNEMENT_APG);
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {
            getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LETTRE_ACCOMPAGNEMENT_IJ);
        } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_CORVUS)) {
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LETTRE_ACCOMPAGNEMENT_DE_COPIE_RENTES);
        }

        JadePublishDocumentInfo docInfo = getDocumentInfo();
        docInfo.setArchiveDocument(false);
        docInfo.setPublishDocument(false);

        try {
            String annee = "";
            if (!JadeStringUtil.isEmpty(getDateDecision())) {
                annee = JADate.getYear(getDateDecision()).toString();
            } else {
                annee = JADate.getYear(JACalendar.todayJJsMMsAAAA().toString()).toString();
            }
            docInfo.setDocumentProperty("annee", annee);

        } catch (JAException e) {
            e.printStackTrace();
        }

        try {
            // Récupération du numéro d'affilié si existant
            String numAffilieFormatte = tierAdresse.getProperty(PRTiersWrapper.PROPERTY_NUM_AFFILIE);

            // Renseignement du docInfo avec le numéro d'affilié
            if (!JadeStringUtil.isBlankOrZero(numAffilieFormatte)) {
                String naffNonFormatte = PRAbstractApplication.getAffileFormater().unformat(numAffilieFormatte);
                TIDocumentInfoHelper.fill(docInfo, tierAdresse.getIdTiers(), getSession(), IntRole.ROLE_APG,
                        numAffilieFormatte, naffNonFormatte);
            } else {
                // Sinon, renseignement du docInfo sans
                TIDocumentInfoHelper
                        .fill(docInfo, tierAdresse.getIdTiers(), getSession(), IntRole.ROLE_APG, null, null);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {

            // Reprise ou remise à zéro des autres paramètres
            Map<String, String> parametres = getImporter().getParametre();

            if (parametres == null) {
                parametres = new HashMap<String, String>();
                getImporter().setParametre(parametres);
            } else {
                parametres.clear();
            }

            // creation des parametres pour l'en-tete
            // ---------------------------------------------------------------------

            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            String adresse = "";

            if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_APG)) {

                adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(),
                        tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(), "519002");

            } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_MAT)) {

                adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(),
                        tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(), "519003");

            } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {
                BISession remoteSession = PRSession.connectSession(getSession(), "IJ");

                if ("true".equals(((BSession) remoteSession).getApplication().getProperty(
                        IJApplication.PROPERTY_DOC_NOMCOLABO))) {
                    // nom du collaborateur
                    crBean.setNomCollaborateur(getSession().getUserFullName());
                }
                adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(),
                        tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(), "519009");

                // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
                // document
                if ("true".equals(((BSession) remoteSession).getApplication().getProperty(
                        IJApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                    crBean.setConfidentiel(true);
                }

            } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_CORVUS)) {

                adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(),
                        tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getIdAffilie(), "519006");

            }

            crBean.setAdresse(adresse);

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(getSession().getApplication().getProperty("documents.is.confidentiel"))) {
                crBean.setConfidentiel(true);
            }

            // Ajoute la date dans l'entête de la lettre sauf pour les documents
            // IJ
            if (!getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {

                String date = JACalendar.todayJJsMMsAAAA();
                crBean.setDate(JACalendar.format(date, codeIsoLangue));
                caisseHelper.addHeaderParameters(getImporter(), crBean);

            }

            setDocumentTitle("En-tête copie pour " + tierAdresse.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase()
                    + " " + tierAdresse.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            // Création du document d'en-tête des copies
            StringBuffer buffer = new StringBuffer();

            // Insertion du titre du tiers
            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable<String, String> params = new Hashtable<String, String>();
            params.put(ITITiers.FIND_FOR_IDTIERS, tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }

            // Ajout du numéro de décision pour les documents IJ
            if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {

                String cdtDateDecision = PRStringUtils.replaceString(
                        document.getTextes(3).getTexte(1).getDescription(), "{DateSaisie}",
                        JACalendar.format(dateDecision, codeIsoLangue));
                buffer.append(cdtDateDecision);

                if (!JadeStringUtil.isBlankOrZero(numeroDeDecisionAI)) {
                    String cdtDateNumeroDeDecisionAI = PRStringUtils.replaceString(document.getTextes(3).getTexte(2)
                            .getDescription(), "{NoDecisionAI}", numeroDeDecisionAI);
                    buffer.append(cdtDateNumeroDeDecisionAI);
                }

                buffer.append("\n\n");

            }

            String titre = tiersTitre.getFormulePolitesse(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            buffer.append(PRStringUtils.replaceString(document.getTextes(1).getTexte(1).getDescription(),
                    "{titreTiers}", titre));

            // Insertion du corps du texte
            // ---------------------------------------------------------------------------
            buffer.append("\n\n");
            buffer.append(document.getTextes(1).getTexte(2).getDescription());

            // Insertion des salutations
            // -----------------------------------------------------------------------------
            buffer.append("\n\n");
            buffer.append(PRStringUtils.replaceString(document.getTextes(1).getTexte(3).getDescription(),
                    "{titreTiers}", titre));

            parametres.put("PARAM_ZONE_1", buffer.toString());
            buffer.setLength(0);

            // Insertion de la signature (Office AI du canton de ...) pour les
            // documents IJ et ajout de l'adresse de l'office AI dans l'entête
            if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {

                TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
                tiAdministrationMgr.setSession(getSession());
                tiAdministrationMgr.setForCodeAdministration(noOfficeAI);
                tiAdministrationMgr.setForGenreAdministration("509004");

                tiAdministrationMgr.find();

                TIAdministrationViewBean tiAdministration = (TIAdministrationViewBean) tiAdministrationMgr
                        .getFirstEntity();

                String adresseOfficeAi = "";

                if (tiAdministration != null) {

                    adresseOfficeAi = tiAdministration.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IJApplication.CS_DOMAINE_ADRESSE_IJAI, JACalendar.todayJJsMMsAAAA(),
                            new PRTiersAdresseCopyFormater03());

                }

                if (caisseHelper instanceof IJDecisionACaisseReportHelper) {
                    ((IJDecisionACaisseReportHelper) caisseHelper).addHeaderParameters(getImporter(), crBean,
                            adresseOfficeAi, codeIsoLangue, "");
                }
                // crBean.setAdresse(adresseOfficeAi);
                // caisseHelper.addHeaderParameters(this, crBean);

                TIAdresseDataManager tiAdresseDatamgr = new TIAdresseDataManager();
                tiAdresseDatamgr.setSession(getSession());
                tiAdresseDatamgr.setForIdTiers(tiAdministration.getIdTiers());
                tiAdresseDatamgr.changeManagerSize(0);
                tiAdresseDatamgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
                tiAdresseDatamgr.find();

                TIAdresseDataSource dataSource = new TIAdresseDataSource();
                dataSource.setLangue(codeIsoLangue);
                dataSource.load((TIAbstractAdresseData) tiAdresseDatamgr.getFirstEntity(), "");

                try {
                    // if (null != dataSource) {
                    // CaisseSignatureReportBean csBean = new CaisseSignatureReportBean();
                    // csBean.setSignatureCaisse(PRStringUtils.replaceString(document.getTextes(1).getTexte(4)
                    // .getDescription(), PRLettreEnTete.CDT_SIGNATURE, dataSource.ligne1));
                    // caisseHelper.addSignatureParameters(this, csBean);
                    // }

                    if ((caisseHelper instanceof IJDecisionACaisseReportHelper) && (null != dataSource)) {

                        ((IJDecisionACaisseReportHelper) caisseHelper).addSignatureParameters(getImporter(), null,
                                PRStringUtils.replaceString(document.getTextes(1).getTexte(4).getDescription(),
                                        PRLettreEnTete.CDT_SIGNATURE, dataSource.ligne1));
                    }
                } catch (Exception e) {
                    throw new FWIException("Impossible de charger le pied de page", e);
                }

            } else {

                try {

                    caisseHelper.addSignatureParameters(getImporter());
                } catch (Exception e) {
                    throw new FWIException("Impossible de charger le pied de page", e);
                }
            }

            // Insertion de l'annexe
            // ---------------------------------------------------------------------------------
            buffer.append(document.getTextes(2).getTexte(1).getDescription());
            parametres.put("PARAM_ZONE_COPIE_ANNEXE", buffer.toString());
            buffer.setLength(0);

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJDecision");
            abort();
        }

    }

    @Override
    public void beforeExecuteReport() throws FWIException {

        try {

            if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_APG)
                    || getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_MAT)) {
                try {
                    // le modele
                    String extensionModelCaisse = getSession().getApplication()
                            .getProperty("extensionModelITextCaisse");
                    if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                        setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_APG + extensionModelCaisse);
                        FWIImportManager im = getImporter();
                        File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                                + FWITemplateType.TEMPLATE_JASPER.toString());
                        if ((sourceFile != null) && sourceFile.exists()) {
                            ;
                        } else {
                            setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_APG);
                        }
                    } else {
                        setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_APG);
                    }
                } catch (Exception e) {
                    setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_APG);
                }
                getImporter().setImportPath("apgRoot");
                getExporter().setExportApplicationRoot("apgRoot");
            } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {
                try {
                    // le modele
                    String extensionModelCaisse = getSession().getApplication()
                            .getProperty("extensionModelITextCaisse");
                    if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                        setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_IJ + extensionModelCaisse);
                        FWIImportManager im = getImporter();
                        File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                                + FWITemplateType.TEMPLATE_JASPER.toString());
                        if ((sourceFile != null) && sourceFile.exists()) {
                            ;
                        } else {
                            setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_IJ);
                        }
                    } else {
                        setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_IJ);
                    }
                } catch (Exception e) {
                    setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_IJ);
                }
                getImporter().setImportPath("ijRoot");
                getExporter().setExportApplicationRoot("ijRoot");
            } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_CORVUS)) {
                setTemplateFile(PRLettreEnTete.FICHIER_MODELE_ENTETE_CORVUS);
                getImporter().setImportPath("corvusRoot");
                getExporter().setExportApplicationRoot("corvusRoot");
            }

            codeIsoLangue = getSession().getCode(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();

            String annee = "";
            if (!JadeStringUtil.isEmpty(getDateDecision())) {
                annee = JADate.getYear(getDateDecision()).toString();
            } else {
                annee = JADate.getYear(JACalendar.todayJJsMMsAAAA().toString()).toString();
            }
            docInfo.setDocumentProperty("annee", annee);

            // creation du helper pour les entetes et pieds de page
            if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {
                docInfo.setDocumentTypeNumber(IPRConstantesExternes.LETTRE_ACCOMPAGNEMENT_IJ);
                // caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(docInfo,
                // getSession().getApplication(), codeIsoLangue);
                // TODO vérifier les check d'instance ijdeci
                caisseHelper = new IJDecisionACaisseReportHelper(docInfo);
                caisseHelper.init(getSession().getApplication(), codeIsoLangue);

            } else {
                docInfo.setDocumentTypeNumber(IPRConstantesExternes.LETTRE_ACCOMPAGNEMENT_APG);
                caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(docInfo,
                        getSession().getApplication(), codeIsoLangue);

            }

            if (!JadeStringUtil.isBlankOrZero(specificHeader)) {
                caisseHelper.setHeaderName(specificHeader);
            }

            // chargement du catalogue de texte
            documentHelper = PRBabelHelper.getDocumentHelper(getISession());

            if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_APG)
                    || getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_MAT)) {
                documentHelper.setCsDomaine("52018001");
                documentHelper.setCsTypeDocument("52019008");
            } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_IJAI)) {
                documentHelper.setCsDomaine("52425001");
                documentHelper.setCsTypeDocument("52426008");
            } else if (getDomaineLettreEnTete().equals(PRLettreEnTete.DOMAINE_CORVUS)) {
                documentHelper.setCsDomaine("52843001");
                documentHelper.setCsTypeDocument("52844002");
            }

            documentHelper.setDefault(Boolean.TRUE);
            documentHelper.setActif(Boolean.TRUE);

            documentHelper.setCodeIsoLangue(codeIsoLangue);

            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                getMemoryLog().logMessage("impossible de charger le catalogue de texte", FWMessage.ERREUR,
                        "En-tête Copie");
                abort();
            } else {
                document = documents[0];
            }

        } catch (Exception e) {
            throw new FWIException("impossible de charger le catalogue de texte pour la lettre en-tête", e);
        }

    }

    @Override
    public void createDataSource() throws Exception {
        lignes = new LinkedList<String>();
        lignes.add("");
        this.setDataSource(lignes);
    }

    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDomaineLettreEnTete() {
        return domaineLettreEnTete;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public List<String> getLignes() {
        return lignes;
    }

    public int getNbTrue() {
        return nbTrue;
    }

    public String getNoOfficeAI() {
        return noOfficeAI;
    }

    public String getNumeroDeDecisionAI() {
        return numeroDeDecisionAI;
    }

    public String getPeriodeDecision() {
        return periodeDecision;
    }

    public String getSpecificHeader() {
        return specificHeader;
    }

    public PRTiersWrapper getTierAdresse() {
        return tierAdresse;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (nbTrue == 0) {
            nbTrue++;
            return true;
        } else {
            return false;
        }
    }

    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDomaineLettreEnTete(String domaineLettreEnTete) {
        this.domaineLettreEnTete = domaineLettreEnTete;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setLignes(List<String> lignes) {
        this.lignes = lignes;
    }

    public void setNbTrue(int nbTrue) {
        this.nbTrue = nbTrue;
    }

    public void setNoOfficeAI(String noOfficeAI) {
        this.noOfficeAI = noOfficeAI;
    }

    public void setNumeroDeDecisionAI(String numeroDeDecisionAI) {
        this.numeroDeDecisionAI = numeroDeDecisionAI;
    }

    public void setPeriodeDecision(String periodeDecision) {
        this.periodeDecision = periodeDecision;
    }

    public void setSpecificHeader(String specificHeader) {
        this.specificHeader = specificHeader;
    }

    public void setTierAdresse(PRTiersWrapper tierAdresse) {
        this.tierAdresse = tierAdresse;
    }

}
