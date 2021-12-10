package globaz.ij.itext;

import globaz.apg.api.codesystem.IAPCatalogueTexte;
import globaz.babel.api.ICTDocument;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.ij.application.IJApplication;
import globaz.ij.helpers.process.IJDecisionACaisseReportHelper;
import globaz.ij.vb.prononces.IJAbstractPrononceProxyViewBean;
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
import java.util.*;

public class IJDecomptesEntete extends IJDecomptes {

    public static final String FICHIER_MODELE_ENTETE_IJ = "IJ_LETTRE_ENTETE";
    private String idAffilie;
    private List<String> lignes = new LinkedList<>();
    private int nbTrue = 0;
    private String noOfficeAI = "";
    private String specificHeader = "";
    private PRTiersWrapper tierAdresse;

    public IJDecomptesEntete() throws FWIException {
        super();
    }

    public IJDecomptesEntete(BProcess parent) throws FWIException {
        super(parent);
    }

    public IJDecomptesEntete(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    public IJDecomptesEntete(BSession session) throws FWIException {
        super(session);
    }

    public IJDecomptesEntete(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    @Override
    public void createDataSource() throws Exception {
        if(isCopie && isEntete) {
            setTailleLot(1);
            setImpressionParLot(true);
            try {

                try {
                    tierAdresse = PRTiersHelper.getTiersAdresseParId(getSession(), getDemandeDecompteCourant().getIdTiers());

                    // le modele
                    String extensionModelCaisse = getSession().getApplication()
                            .getProperty("extensionModelITextCaisse");
                    if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                        setTemplateFile(FICHIER_MODELE_ENTETE_IJ + extensionModelCaisse);
                        FWIImportManager im = getImporter();
                        File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                                + FWITemplateType.TEMPLATE_JASPER.toString());
                        if ((sourceFile != null) && sourceFile.exists()) {
                            ;
                        } else {
                            setTemplateFile(FICHIER_MODELE_ENTETE_IJ);
                        }
                    } else {
                        setTemplateFile(FICHIER_MODELE_ENTETE_IJ);
                    }
                } catch (Exception e) {
                    setTemplateFile(FICHIER_MODELE_ENTETE_IJ);
                }
                getImporter().setImportPath(IJApplication.APPLICATION_IJ_REP);
                getExporter().setExportApplicationRoot(IJApplication.APPLICATION_IJ_REP);

                codeIsoLangue = getSession().getCode(tierAdresse.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
                codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

                JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();

                String annee = "";
                annee = JADate.getYear(JACalendar.todayJJsMMsAAAA().toString()).toString();

                docInfo.setDocumentProperty("annee", annee);

                // creation du helper pour les entetes et pieds de page
                docInfo.setDocumentTypeNumber(IPRConstantesExternes.LETTRE_ACCOMPAGNEMENT_IJ);
                caisseHelper = new IJDecisionACaisseReportHelper(docInfo);
                caisseHelper.init(getSession().getApplication(), codeIsoLangue);

                if (!JadeStringUtil.isBlankOrZero(specificHeader)) {
                    caisseHelper.setHeaderName(specificHeader);
                }

                // chargement du catalogue de texte
                documentHelper = PRBabelHelper.getDocumentHelper(getISession());

                // Domaine APG pour l'entête car entête identique pour l'ensemble des documents APG / Maternité / IJ etc...
                documentHelper.setCsDomaine(IAPCatalogueTexte.CS_APG);
                documentHelper.setCsTypeDocument(IAPCatalogueTexte.CS_LETTRE_ENTETE_APG);


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

            lignes = new LinkedList<>();
            lignes.add("");
            this.setDataSource(lignes);
        } else {
            super.createDataSource();
        }
    }

    @Override
    public void afterBuildReport() {
        if(isCopie && isEntete) {
            getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LETTRE_ACCOMPAGNEMENT_IJ);

            JadePublishDocumentInfo docInfo = getDocumentInfo();
            docInfo.setArchiveDocument(false);
            docInfo.setPublishDocument(false);

            try {
                docInfo.setDocumentProperty("annee", JADate.getYear(JACalendar.todayJJsMMsAAAA().toString()).toString());

            } catch (JAException e) {
                getMemoryLog().logMessage("IJDecompte afterBuildReport():" + e.getMessage(), FWMessage.ERREUR,
                        "IJDecomptes");
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
                getMemoryLog().logMessage("IJDecompte afterBuildReport():" + e.getMessage(), FWMessage.ERREUR,
                        "IJDecomptes");
            }
            isEntete = false;
        } else if(isCopie){
            isEntete = true;
        }

    }

    @Override
    public void beforeBuildReport() throws FWIException {
        if (isCopie && isEntete) {
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

                BISession remoteSession = PRSession.connectSession(getSession(), "IJ");

                if ("true".equals(((BSession) remoteSession).getApplication().getProperty(
                        IJApplication.PROPERTY_DOC_NOMCOLABO))) {
                    // nom du collaborateur
                    crBean.setNomCollaborateur(getSession().getUserFullName());
                }
                adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(),
                        tierAdresse.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), idAffilie, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_IJAI);

                // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
                // document
                if ("true".equals(((BSession) remoteSession).getApplication().getProperty(
                        IJApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                    crBean.setConfidentiel(true);
                }

                crBean.setAdresse(adresse);

                // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
                // document
                if ("true".equals(getSession().getApplication().getProperty("documents.is.confidentiel"))) {
                    crBean.setConfidentiel(true);
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
                buffer.append("\n\n");

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

                TIAdministrationManager tiAdministrationMgr = new TIAdministrationManager();
                tiAdministrationMgr.setSession(getSession());
                tiAdministrationMgr.setForCodeAdministration(noOfficeAI);
                tiAdministrationMgr.setForGenreAdministration(IJAbstractPrononceProxyViewBean.CS_ADMIN_GENRE_OFFICE_AI);

                tiAdministrationMgr.find(1);

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

                TIAdresseDataManager tiAdresseDatamgr = new TIAdresseDataManager();
                tiAdresseDatamgr.setSession(getSession());
                tiAdresseDatamgr.setForIdTiers(tiAdministration.getIdTiers());
                tiAdresseDatamgr.changeManagerSize(0);
                tiAdresseDatamgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
                tiAdresseDatamgr.find(1);

                TIAdresseDataSource dataSource = new TIAdresseDataSource();
                dataSource.setLangue(codeIsoLangue);
                dataSource.load((TIAbstractAdresseData) tiAdresseDatamgr.getFirstEntity(), "");

                buffer.setLength(0);

            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJDecision");
                abort();
            }
        } else {
            super.beforeBuildReport();
        }

    }
}
