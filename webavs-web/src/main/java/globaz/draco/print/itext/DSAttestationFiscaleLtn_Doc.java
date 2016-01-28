package globaz.draco.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIPersonneAvsManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author BJO <h1>description</h1> Classe permettant d'imprimer les attestations fiscales pour tous les salariés dont
 *         l'employeur a payé ses cotisations, et qu'aucune attesation n'a encore été imprimée (LTN009) en deux
 *         exemplaires (salarié et fisc) avec la dernière adresse connue du salarié.
 */
public class DSAttestationFiscaleLtn_Doc extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String DS_ATTESTATION = "125005";
    public final static String DS_DOMAINE = "124001";
    public final static String numeroInforom = "0198CDS";

    /**
     * Remplace dans message {n} par args[n].
     * <p>
     * Evite que {@link MessageFormat} ne lance une erreur ou ne se comporte pas correctement si le message contient des
     * apostrophes
     * </p>
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);

        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }

        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }
        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
    }

    private String adresseSalarie = "";
    private AFAffiliation affEnCours;
    private String annee;
    private AFAssurance assuranceImpotCantonal;
    private AFAssurance assuranceImpotFederal;
    private String casePostaleEmployeur;
    private ICTDocument catalogue;
    private String currentNssKey;
    private String dateDebut;
    private String dateFin;
    private String dateNaissanceSalarie = "";
    private String dateValeur = JACalendar.todayJJsMMsAAAA();
    private DSDeclarationViewBean declaration = null;
    private String emailAddress;
    private String idDeclaration = "";
    private BigDecimal impotCantonal;
    private BigDecimal impotFederal;
    private int indiceDocument = 0;
    private int indiceSalarie = 0;
    private DSInscriptionsIndividuellesManager inscriptionIndManager;
    private HashMap<String, ArrayList<DSInscriptionsIndividuelles>> inscriptionsIndividuellesMap;
    private String langueIsoTiers = "";// langue du tier
    private String localiteCourtEmployeur;
    private String MODEL_NAME = "";
    private int nbCopieParDocument = 2;// définit le nombre de copie que l'on veut pour chaque attestation
    private int nbSalarie;
    private String nomEmployeur;
    private String npaEmployeur;
    private Set nssKeys;
    private Iterator nssKeysIterator;
    private String nssSalarie = "";
    private String numEmployeur;
    private boolean processMenu = false;// définit si le process est lancé depuis le process général (menu)
    private String rueEmployeur;
    private FWCurrency salaireBrutAvsSalarie;
    private boolean simulation = false;
    private AFTauxAssurance tauxCantonal;
    private AFTauxAssurance tauxFederal;
    private TITiersViewBean tiers = null;

    private BigDecimal totalImpotSource;

    public DSAttestationFiscaleLtn_Doc() throws Exception {

    }

    /**
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public DSAttestationFiscaleLtn_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    public DSAttestationFiscaleLtn_Doc(globaz.globall.db.BSession session) throws Exception {
        super(session, DSApplication.DEFAULT_APPLICATION_ROOT, "ImpressionDeclaration");
    }

    protected void _afterTable() throws Exception {
        super.setParametres(DSAttestationFiscaleLtn_Param.P_EMPLOYEUR_LABEL, getTexte(3, 1, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_EMPLOYEUR_NOM,
                getTexte(3, 2, new Object[] { nomEmployeur }));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_EMPLOYEUR_RUE,
                getTexte(3, 3, new Object[] { rueEmployeur, numEmployeur }));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_EMPLOYEUR_NPA,
                getTexte(3, 4, new Object[] { npaEmployeur }));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_EMPLOYEUR_LOCALITE,
                getTexte(3, 5, new Object[] { localiteCourtEmployeur }));

        if (!JadeStringUtil.isEmpty(casePostaleEmployeur)) {
            super.setParametres(DSAttestationFiscaleLtn_Param.P_EMPLOYEUR_CASE,
                    getTexte(3, 6, new Object[] { casePostaleEmployeur }));
        }
        super.setParametres(DSAttestationFiscaleLtn_Param.P_SIGNATURE, getTexte(3, 7, null));
    }

    protected void _beforeTable() throws Exception {
        super.setParametres(DSAttestationFiscaleLtn_Param.P_TITRE, getTexte(1, 1, new Object[] { annee }));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_PERSONNE_ASSUJETTIE_LABEL, getTexte(1, 2, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_PERSONNE_ASSUJETTIE_DATA,
                getTexte(1, 3, new Object[] { nssSalarie }));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_RETENUE_IMPOT_LABEL, getTexte(1, 4, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_RETENUE_IMPOT_DATA,
                getTexte(1, 5, new Object[] { dateDebut, dateFin }));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_DATE_NAISSANCE,
                getTexte(1, 6, new Object[] { dateNaissanceSalarie }));
    }

    /**
     * Envoi les paramètre au header du documents
     * 
     * @param bean
     * @throws Exception
     */
    private void _setHeader(CaisseHeaderReportBean bean) throws Exception {
        bean.setAdresse(adresseSalarie);
        bean.setNoAffilie(affEnCours.getAffilieNumero());

        // Renseinge le Numéro IDE
        AFIDEUtil.addNumeroIDEInDoc(bean, affEnCours.getNumeroIDE(), affEnCours.getIdeStatut());

        bean.setDate(dateValeur);
        bean.setNomCollaborateur(getSession().getUserFullName());
        bean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        bean.setUser(getSession().getUserInfo());
    }

    protected void _table() throws Exception {
        super.setParametres(DSAttestationFiscaleLtn_Param.P_LIBELLE_COL1, getTexte(2, 1, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_LIBELLE_COL2, getTexte(2, 2, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_LIBELLE_COL3, getTexte(2, 3, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_LIBELLE_COL4, getTexte(2, 4, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_LIBELLE_COL5, getTexte(2, 3, null));
        super.setParametres(DSAttestationFiscaleLtn_Param.P_LIBELLE_COL6, getTexte(2, 5, null));

        super.setParametres(DSAttestationFiscaleLtn_Param.P_VALEUR_COL1, salaireBrutAvsSalarie.toStringFormat());
        super.setParametres(DSAttestationFiscaleLtn_Param.P_VALEUR_COL2,
                new FWCurrency(impotFederal.toString()).toStringFormat());
        super.setParametres(DSAttestationFiscaleLtn_Param.P_VALEUR_COL3, tauxFederal.getTauxSansFraction());
        super.setParametres(DSAttestationFiscaleLtn_Param.P_VALEUR_COL4,
                new FWCurrency(impotCantonal.toString()).toStringFormat());
        super.setParametres(DSAttestationFiscaleLtn_Param.P_VALEUR_COL5, tauxCantonal.getTauxSansFraction());
        super.setParametres(DSAttestationFiscaleLtn_Param.P_VALEUR_COL6,
                new FWCurrency(totalImpotSource.toString()).toStringFormat());
    }

    protected void abort(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
        this.abort();
    }

    @Override
    public void afterExecuteReport() {
        // Fusionne tous les documents en 1 seul document
        try {
            if (!getProcessMenu()) {
                JadePublishDocumentInfo docInfo = createDocumentInfo();
                docInfo.setDocumentTypeNumber(DSAttestationFiscaleLtn_Doc.numeroInforom);
                docInfo.setPublishDocument(true);
                if (simulation) {
                    docInfo.setDocumentProperty("simulation", "true");
                }
                docInfo.setArchiveDocument(false);
                this.mergePDF(docInfo, false, 500, false, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // flag la déclaration avec la date saisie par l'utilisateur si la case simulation est décochée
        if (!simulation) {
            try {
                BTransaction myTransaction = new BTransaction(getSession());
                DSDeclarationViewBean declaration = getDeclaration();
                declaration.setDateImpressionAttestations(dateValeur);
                declaration.wantCallValidate(false);// afin de pouvoir modifier une déclaration comptablilisée
                declaration.wantCallMethodBefore(false);// afin de pouvoir modifier une déclaration comptablilisée
                declaration.update(myTransaction);
                myTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            setDocumentTitle(nssSalarie);
            MODEL_NAME = "DRACO_ATTESTATION_FISCALE_LTN";
            super.setTemplateFile(MODEL_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setImpressionParLot(true);
        setTailleLot(0);

        // récupération des inscriptions individuelles de la DS LTN principale (33)
        try {
            inscriptionIndManager = new DSInscriptionsIndividuellesManager();
            inscriptionIndManager.setSession(getSession());
            inscriptionIndManager.setForIdDeclaration(getIdDeclaration());
            inscriptionIndManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new FWIException("unable to find individual inscription" + e.getMessage());
        }

        // instanciation du hashmap qui va contenir toutes les inscriptions individuelles pour l'affilié (33 et 38)
        inscriptionsIndividuellesMap = new HashMap<String, ArrayList<DSInscriptionsIndividuelles>>();
        // insertion des inscriptions de la déclaration principale (33)
        for (int i = 0; i < inscriptionIndManager.size(); i++) {

            DSInscriptionsIndividuelles inscriptionInd = (DSInscriptionsIndividuelles) inscriptionIndManager
                    .getEntity(i);
            if (inscriptionsIndividuellesMap.containsKey(inscriptionInd.getNumeroAvs())) {
                // on insère à la bonne place dans le hashmap
                inscriptionsIndividuellesMap.get(inscriptionInd.getNumeroAvs()).add(inscriptionInd);
            } else {
                ArrayList<DSInscriptionsIndividuelles> inscriptionIndividuelleArray = new ArrayList<DSInscriptionsIndividuelles>();
                inscriptionIndividuelleArray.add(inscriptionInd);
                inscriptionsIndividuellesMap.put(inscriptionInd.getNumeroAvs(), inscriptionIndividuelleArray);
            }
        }

        // récupération de l'année de la déclaration
        annee = getDeclaration().getAnnee();
        dateDebut = "01.01." + annee;
        dateFin = "31.12." + annee;

        // récupération des informations sur l'affilié
        recupInfoAffilie();
        setFileTitle(getSession().getLabel("ATTESTATION_FISCALE_LTN_TITRE_MAIL") + " " + affEnCours.getAffilieNumero());

        // regarde si il existe un 38 pour cet affilié et cette année
        DSDeclarationListViewBean declarationCompManager = new DSDeclarationListViewBean();
        declarationCompManager.setSession(getSession());
        declarationCompManager.setForAffiliationId(affEnCours.getAffiliationId());
        declarationCompManager.setForTypeDeclaration(DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE);
        declarationCompManager.setForAnnee(annee);
        try {
            declarationCompManager.find();
        } catch (Exception e1) {
            throw new FWIException("unable to find complement declaration" + e1.getMessage());
        }

        // si on a au moins une 38, on parcours et on insère dans le hashmap
        if (declarationCompManager.size() > 0) {
            for (int i = 0; i < declarationCompManager.size(); i++) {
                DSDeclarationViewBean declarationComp = (DSDeclarationViewBean) declarationCompManager.get(i);
                DSInscriptionsIndividuellesManager inscriptionIndCompManager = new DSInscriptionsIndividuellesManager();
                inscriptionIndCompManager.setSession(getSession());
                inscriptionIndCompManager.setForIdDeclaration(declarationComp.getIdDeclaration());
                try {
                    inscriptionIndCompManager.find(BManager.SIZE_NOLIMIT);
                } catch (Exception e) {
                    throw new FWIException("unable to find complement inscription" + e.getMessage());
                }

                // on parcours les insriptions et on insère dans le hashmap
                for (int j = 0; j < inscriptionIndCompManager.size(); j++) {
                    DSInscriptionsIndividuelles inscriptionComp = (DSInscriptionsIndividuelles) inscriptionIndCompManager
                            .get(j);
                    if (inscriptionsIndividuellesMap.containsKey(inscriptionComp.getNumeroAvs())) {
                        // on insère à la bonne place dans le hashmap
                        inscriptionsIndividuellesMap.get(inscriptionComp.getNumeroAvs()).add(inscriptionComp);
                    } else {
                        // création d'une nouvelle key
                        ArrayList<DSInscriptionsIndividuelles> inscriptionCompArray = new ArrayList<DSInscriptionsIndividuelles>();
                        inscriptionCompArray.add(inscriptionComp);
                        inscriptionsIndividuellesMap.put(inscriptionComp.getNumeroAvs(), inscriptionCompArray);
                    }
                }
            }
        }

        // suppression des cas dont le salaireAvsBrut = 0
        Set tempKeys = inscriptionsIndividuellesMap.keySet();
        Iterator tempIterator = tempKeys.iterator();
        while (tempIterator.hasNext()) {
            FWCurrency salaireAvs = new FWCurrency("0");
            String key = (String) tempIterator.next();
            ArrayList<DSInscriptionsIndividuelles> inscriptionIndArray = inscriptionsIndividuellesMap.get(key);
            for (DSInscriptionsIndividuelles inscriptionInd : inscriptionIndArray) {
                if (inscriptionInd.getGenreEcriture().equals("11")) {
                    salaireAvs.sub(inscriptionInd.getMontant());
                } else {
                    salaireAvs.add(inscriptionInd.getMontant());
                }
            }
            if (salaireAvs.isZero()) {
                tempIterator.remove();
                // Affiche une information dans l'email pour indiquer qu'aucune attestation ne sera imprimée pour cet
                // assuré car son montant avs est égal à 0
                getMemoryLog().logMessage(
                        getSession().getLabel("INFO_MAIL_MONTANTAVS_ZERO") + ": " + NSUtil.formatAVSUnknown(key),
                        FWMessage.INFORMATION, "");
            }
        }

        // création de l'itérateur pour parcourir le hashmap définitif
        nssKeys = inscriptionsIndividuellesMap.keySet();
        nssKeysIterator = nssKeys.iterator();
        nbSalarie = inscriptionsIndividuellesMap.size();

        // pas effectué quand le process est exécuté depuis le process général
        if (!getProcessMenu()) {
            try {
                // progress counter
                setProgressScaleValue(nbSalarie * 2);

                // vérifie que la déclaration soit bien de type LTN
                DSDeclarationViewBean declaration = new DSDeclarationViewBean();
                declaration.setSession(getSession());
                declaration.setIdDeclaration(getIdDeclaration());
                declaration.retrieve();
                if (!declaration.getTypeDeclaration().equals(DSDeclarationViewBean.CS_LTN)) {
                    this.abort(getSession().getLabel("NON_LTN"), FWMessage.ERREUR);
                }

                // récupération du compte annexe et vérification du solde des cotisations
                BigDecimal sommeSolde = null;
                BISession osirisSession = GlobazSystem.getApplication("OSIRIS").newSession(getSession());
                // Récupérer le compte annexe
                CACompteAnnexe compteAnnexe = new CACompteAnnexe();
                compteAnnexe.setISession(osirisSession);
                compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                compteAnnexe.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(
                        getSession().getApplication()));
                compteAnnexe.setIdExterneRole(affEnCours.getAffilieNumero());
                compteAnnexe.retrieve(getTransaction());
                if (JadeStringUtil.isBlank(compteAnnexe.getIdCompteAnnexe())) {
                    this.abort(getSession().getLabel("COMPTE_ANNEXE_INEXISTANT"), "");
                }

                CASectionManager sectionManager = new CASectionManager();
                sectionManager.setISession(osirisSession);
                sectionManager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());

                // on sélectionne les DS LTN et les DS LTN COMPLEMENTAIRE (les 2 doivent être soldées)
                ArrayList<String> categorieList = new ArrayList<String>();
                categorieList.add(APISection.ID_CATEGORIE_SECTION_LTN);
                categorieList.add(APISection.ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE);
                sectionManager.setForCategorieSectionIn(categorieList);

                sectionManager.setForIdTypeSection(APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION);
                sectionManager.setLikeIdExterne(annee);
                sectionManager.find(BManager.SIZE_NOLIMIT);

                int nbSection = 0;
                // BZ xxx - I140409_003 0> ignorer les sections issues des intérêts
                for (int j = 0; j < sectionManager.getSize(); j++) {
                    CASection section = (CASection) sectionManager.getEntity(j);
                    if (!"9".equals(section.getIdExterne().substring(6, 7))) {
                        if (sommeSolde == null) {
                            sommeSolde = new BigDecimal(section.getSolde());
                        } else {
                            sommeSolde = sommeSolde.add(new BigDecimal(section.getSolde()));
                        }
                        nbSection++;
                    }
                }

                // si il n'existe pas le même nombre de section (osiris) que le nombre de DS (draco) c'est qu'il y a une
                // erreur => on abort
                if (nbSection != (declarationCompManager.size() + 1)) {
                    this.abort(getSession().getLabel("ERREUR_LTN_INCOHERENCE_COMPTA_DS"), FWMessage.ERREUR);
                }

                if (sommeSolde != null) {
                    // Si le solde est positif on imprime pas les attestations (l'employeur n'a pas payé)
                    if (sommeSolde.doubleValue() != 0) {
                        this.abort(getSession().getLabel("EMPLOYEUR_PAS_PAYE_COTISATIONS"), FWMessage.ERREUR);
                    }
                }
                // si la somme est null on imprime pas les attestations car la déclaration n'est pas en comptabilité
                // (OSIRIS)
                if (sommeSolde == null) {
                    this.abort(getSession().getLabel("DECL_NON_COMPTABILISE"), FWMessage.ERREUR);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void catalogue() throws FWIException {
        // if (catalogue == null) {
        try {
            // Recherche le catalogue
            ICTDocument helper = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            helper.setCsDomaine(DSAttestationFiscaleLtn_Doc.DS_DOMAINE); // domaine avs
            helper.setCsTypeDocument(DSAttestationFiscaleLtn_Doc.DS_ATTESTATION); // pour le type de catalogue
            helper.setCodeIsoLangue(langueIsoTiers); // dans la langue du salarié
            helper.setActif(Boolean.TRUE); // actif
            helper.setDefault(Boolean.TRUE); // et par défaut

            // charger le catalogue de texte
            ICTDocument[] candidats = helper.load();

            if ((candidats != null) && (candidats.length > 0)) {
                catalogue = candidats[0];
            }
        } catch (Exception e) {
            catalogue = null;
        }
        // }

        if (catalogue == null) {
            this.abort(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR);
            throw new FWIException(getSession().getLabel("CATALOGUE_INTROUVABLE"));
        }
    }

    @Override
    public void createDataSource() throws Exception {
        // récupération des informations sur le salarié
        recupInfoSalarie();

        getDocumentInfo().setDocumentTypeNumber(DSAttestationFiscaleLtn_Doc.numeroInforom);
        getDocumentInfo().setDocumentProperty("pyxis.tiers.numero.avs.non.formatte", NSUtil.unFormatAVS(nssSalarie));
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", affEnCours.getAffilieNumero());
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(affEnCours.getAffilieNumero()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affEnCours.getAffilieNumero());
        }
        getDocumentInfo().setDocumentProperty("annee", annee);
        getDocumentInfo().setDocumentDate(dateValeur);
        TIDocumentInfoHelper.fill(getDocumentInfo(), tiers.getIdTiers(), getSession(), ITIRole.CS_ASSURE, nssSalarie,
                NSUtil.unFormatAVS(nssSalarie));

        if (!getProcessMenu()) {
            setProgressDescription(affEnCours.getAffilieNumero());
        }

        // création du document
        _beforeTable();
        _table();
        _afterTable();

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), langueIsoTiers);
        setTemplateFile(MODEL_NAME);
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        _setHeader(headerBean);
        caisseReportHelper.addHeaderParameters(this, headerBean);
        getImporter().getParametre().put(
                ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                        + getTemplateProperty(getDocumentInfo(), "header.filename"));

        // pour placer le numéro nss en bas du doc pour la mise sous pli

        if (!getSimulation()) {
            // on insert dans la GED qu'une attestation par salarié
            if ((indiceDocument % 2) != 0) {
                getDocumentInfo().setArchiveDocument(false);
                getDocumentInfo().setPublishDocument(false);
            } else {
                getDocumentInfo().setArchiveDocument(true);
                getDocumentInfo().setPublishDocument(false);
            }
        } else {
            getDocumentInfo().setArchiveDocument(false);
            getDocumentInfo().setPublishDocument(false);
        }
    }

    /**
     * remplace dans message {n} par args[n].
     * 
     * @param message
     *            le message dans lequel se trouve les groupes à remplacer
     * @param args
     *            les valeurs de remplacement (les nulls sont permis, ils seront remplacés par "")
     * @return le message formatté
     * @see MessageFormat
     */
    protected String formatMessage(StringBuffer message, Object[] args) {
        return DSAttestationFiscaleLtn_Doc.formatMessage(message.toString(), args);
    }

    public AFAffiliation getAffilie() {
        if (affEnCours == null) {
            affEnCours = getDeclaration().getAffiliation();
        }
        return affEnCours;

    }

    public String getDateValeur() {
        return dateValeur;
    }

    public DSDeclarationViewBean getDeclaration() {
        if (declaration == null) {
            try {
                declaration = new DSDeclarationViewBean();
                declaration.setSession(getSession());
                declaration.setIdDeclaration(getIdDeclaration());
                declaration.retrieve();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return declaration;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    /**
     * retourne "true" si le document est généré par le processus général (depuis le menu principal)
     * 
     * @return
     */
    public boolean getProcessMenu() {
        return processMenu;
    }

    public boolean getSimulation() {
        return simulation;
    }

    /**
     * Récupère le texte du catalogue en fonction du niveau et de la position, et remplace les {n} par les textes passés
     * dans le tableau d'objet "args"
     * 
     * @param niveau
     * @param position
     * @param args
     * @return
     * @throws FWIException
     */
    protected String getTexte(int niveau, int position, Object[] args) throws FWIException {
        String texte;
        try {
            if (args != null) {
                texte = DSAttestationFiscaleLtn_Doc.formatMessage(catalogue.getTextes(niveau).getTexte(position)
                        .getDescription(), args);
            } else {
                texte = catalogue.getTextes(niveau).getTexte(position).getDescription();
            }
            return texte;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        if (!isAborted()) {
            // chaque document doit être imprimé 2x
            if (indiceDocument < (nbSalarie * nbCopieParDocument)) {
                // on incrémente et récupère la clé du hashmap (1 fois sur 2)
                if ((indiceDocument % 2) == 0) {
                    currentNssKey = (String) nssKeysIterator.next();
                }
                if (!getProcessMenu()) {
                    incProgressCounter();
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Récupère les informations sur l'affilié (l'employeur) : nom et adresse
     */
    private void recupInfoAffilie() {

        // récupération de l'affilié
        affEnCours = getAffilie();

        try {
            nomEmployeur = affEnCours.getTiersNom();
            TIAvoirAdresse avoirAdresse = TITiers.findAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, affEnCours.getAffilieNumero(), JACalendar.todayJJsMMsAAAA(),
                    affEnCours.getIdTiers(), getSession());
            rueEmployeur = avoirAdresse.getRue();
            numEmployeur = avoirAdresse.getNumeroRue();
            casePostaleEmployeur = avoirAdresse.getCasePostale();

            TILocaliteManager localiteManager = new TILocaliteManager();
            localiteManager.setSession(getSession());
            localiteManager.setForIdLocalite(avoirAdresse.getIdLocalite());
            localiteManager.find();
            TILocalite localite = (TILocalite) localiteManager.getFirstEntity();

            npaEmployeur = localite.getNumPostal();
            localiteCourtEmployeur = localite.getLocaliteCourt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère les informations sur le salarié en cours : nss,adresse,salaire avs brut,date de naissance
     */
    private void recupInfoSalarie() {
        try {
            // récupération des inscriptions individuelles pour le salarié en cours
            ArrayList<DSInscriptionsIndividuelles> inscriptionIndArray = inscriptionsIndividuellesMap
                    .get(currentNssKey);

            // récupération de l'inscription de la DS33 (toujours première case de l'arrayList)
            DSInscriptionsIndividuelles inscriptionInd = inscriptionIndArray.get(0);

            nssSalarie = inscriptionInd.getNumeroAvs().toString();
            nssSalarie = NSUtil.formatAVSUnknown(nssSalarie);
            // salaire avs brut
            salaireBrutAvsSalarie = new FWCurrency("0");

            // prise en compte des compléments LTN pour le salaire brut
            for (int i = 0; i < inscriptionIndArray.size(); i++) {
                DSInscriptionsIndividuelles inscription = inscriptionIndArray.get(i);
                // si il s'agit d'une extourne (montant négatif) on soustrait
                if (inscription.getGenreEcriture().equals("11")) {
                    salaireBrutAvsSalarie.sub(inscription.getMontant());
                } else {
                    salaireBrutAvsSalarie.add(inscription.getMontant());
                }

            }

            TIPersonneAvsManager personneAvsManager = new TIPersonneAvsManager();
            personneAvsManager.setSession(getSession());
            personneAvsManager.setForNumAvsActuel(nssSalarie);
            personneAvsManager.find();
            tiers = (TITiersViewBean) personneAvsManager.getFirstEntity();

            // si le tiers est null (si le nss a changé par exemple, on recherche dans l'historique)
            if (tiers == null) {
                System.out.println("tiers non trouvé -> recherche dans l'historique : " + nssSalarie);
                TIHistoriqueAvsManager historiqueAVSManager = new TIHistoriqueAvsManager();
                historiqueAVSManager.setSession(getSession());
                historiqueAVSManager.setForNumAvs(nssSalarie);
                historiqueAVSManager.find();
                if (historiqueAVSManager.size() > 0) {
                    TIHistoriqueAvs historiqueAvs = (TIHistoriqueAvs) historiqueAVSManager.getFirstEntity();
                    tiers = new TITiersViewBean();
                    tiers.setSession(getSession());
                    tiers.setIdTiers(historiqueAvs.getIdTiers());
                    tiers.retrieve();
                }
            }

            // définit la langue du document en fonction de la langue du salarié
            langueIsoTiers = tiers.getLangueIso();
            // si la langue du tiers n'est pas francais allemand ou italien on utilise la langue de la session
            if (!langueIsoTiers.equalsIgnoreCase("fr") && !langueIsoTiers.equalsIgnoreCase("de")
                    && !langueIsoTiers.equalsIgnoreCase("it")) {
                langueIsoTiers = getSession().getIdLangueISO();
            }

            // chargement du catalogue en fonction de la langue (langueIsoTiers)
            catalogue();

            adresseSalarie = tiers.getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    DSApplication.CS_DOMAINE_DECLARATION_SALAIRES, JACalendar.todayJJsMMsAAAA());

            if (adresseSalarie == null) {
                System.out.println("Adresse null : " + nssSalarie);
                getMemoryLog().logMessage(getSession().getLabel("ERROR_GETTING_ADRESSE") + nssSalarie,
                        FWMessage.ERREUR, this.getClass().toString());
                this.abort();
            }

            dateNaissanceSalarie = tiers.getDateNaissance();

            // calcul des taux de l'impot à la source cantonal en fonction du canton du salarié
            if (!JadeStringUtil.isBlankOrZero(tiers.getIdCantonDomicile())) {
                AFAssuranceManager assuranceManager = new AFAssuranceManager();
                assuranceManager.setSession(getSession());
                assuranceManager.setForTypeAssurance(CodeSystem.TYPE_ASS_IMPOT_SOURCE);
                assuranceManager.setForCanton(tiers.getIdCantonDomicile());
                assuranceManager.find();

                if (assuranceManager.size() >= 1) {
                    assuranceImpotCantonal = (AFAssurance) assuranceManager.getFirstEntity();
                    tauxCantonal = assuranceImpotCantonal.getTaux(dateFin);
                } else {
                    throw new Exception(getSession().getLabel("AUCUNE_ASSURANCE_LTN_POUR_TIERS"));
                }
            }
            if (tauxCantonal == null) {
                throw new Exception(getSession().getLabel("AUCUN_TAUX_POUR_CANTON"));
            }

            impotCantonal = new BigDecimal(
                    AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, tauxCantonal, new Double(
                            JANumberFormatter.deQuote(salaireBrutAvsSalarie.toString())).doubleValue(), getSession()));
            impotCantonal = JANumberFormatter.formatBigD(impotCantonal);// arrondie le montant au plus proche

            // calcul de l'impot fédéral
            AFAssuranceManager assuranceManager = new AFAssuranceManager();
            assuranceManager.setSession(getSession());
            assuranceManager.setForTypeAssurance(CodeSystem.TYPE_ASS_IMPOT_SOURCE);
            assuranceManager.setForCanton("0");// canton = 0 pour le fédéral
            assuranceManager.find();
            if (assuranceManager.size() >= 1) {
                assuranceImpotFederal = (AFAssurance) assuranceManager.getFirstEntity();
                tauxFederal = assuranceImpotFederal.getTaux(dateFin);
            } else {
                throw new Exception(getSession().getLabel("AUCUNE_ASSURANCE_LTN_POUR_TIERS"));
            }
            if (tauxFederal == null) {
                throw new Exception("Aucun taux pour l'impot fédéral");
            }

            impotFederal = new BigDecimal(
                    AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, tauxFederal, new Double(
                            JANumberFormatter.deQuote(salaireBrutAvsSalarie.toString())).doubleValue(), getSession()));
            impotFederal = JANumberFormatter.formatBigD(impotFederal);// arrondie le montant au plus proche

            // calcul du total de l'impot
            totalImpotSource = new BigDecimal(0);
            totalImpotSource = totalImpotSource.add(impotCantonal);
            totalImpotSource = totalImpotSource.add(impotFederal);
            totalImpotSource = JANumberFormatter.formatBigD(totalImpotSource);// arrondie le montant au plus

            indiceDocument++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

    public void setProcessMenu(boolean processMenu) {
        this.processMenu = processMenu;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

}
