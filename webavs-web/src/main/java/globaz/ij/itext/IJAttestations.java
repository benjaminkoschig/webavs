/*
 */
package globaz.ij.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.ij.api.codesystem.IIJCatalogueTexte;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJRepartitionPaiements;
import globaz.ij.process.IJGenererAttestationsProcess;
import globaz.ij.process.IJGenererAttestationsProcess.AttestationsInfos;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import java.io.File;
import java.rmi.RemoteException;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * <H1>Description</H1> Classe de documentManager qui crée le fichier pdf pour l'attestation fiscale.
 * 
 * @author hpe
 */
public class IJAttestations extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class InnerDataHelper {
        String descr = "";
        FWCurrency montant = new FWCurrency(0);
    }

    private static final String FICHIER_MODELE = "IJ_ATTESTATION_FISCALE";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final String FICHIER_RESULTAT = "attestation";
    private static final String ORDER_PRINTING_BY = "orderPrintingBy";
    public String annee = "";
    public Map attestationsMap = new Hashtable();
    private ICaisseReportHelper caisseHelper;
    private AttestationsInfos currentAI;
    public String dateDebut = "";
    public String dateFin = "";
    private ICTDocument document;
    private ICTDocument documentHelper;

    public String idBaseInd = "";

    private Boolean isSendToGED = Boolean.FALSE;
    Iterator iter;
    String keySyso = "";
    private List lignes;
    public ArrayList list = new ArrayList();
    private Locale locale;
    public int nb = 0;

    private IJPrestation prestation;

    PRTiersWrapper tiers;

    public FWCurrency totalMontant = new FWCurrency();
    public FWCurrency totalMontantCoti = new FWCurrency();

    public String totalMontantIJ = "";

    public FWCurrency totalMontantImpot = new FWCurrency();

    public FWCurrency totalVentilation = new FWCurrency();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public IJAttestations() throws FWIException {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJAttestations.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public IJAttestations(BProcess parent) throws FWIException {
        super(parent, IJApplication.APPLICATION_IJ_REP, IJAttestations.FICHIER_RESULTAT);
    }

    /**
     * Crée une nouvelle instance de la classe IJAttestations.
     * 
     * @param session
     *            DOCUMENT ME!
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public IJAttestations(BSession session) throws FWIException {
        super(session, IJApplication.APPLICATION_IJ_REP, IJAttestations.FICHIER_RESULTAT);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    @Override
    public void afterBuildReport() {

        JadePublishDocumentInfo docInfo = getDocumentInfo();
        // on ajoute au doc info le critere de tri pour les impressions
        // ORDER_PRINTING_BY
        try {
            docInfo.setDocumentProperty(IJAttestations.ORDER_PRINTING_BY,
                    buildOrderPrintingByKey("", tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS)));
            docInfo.setDocumentSubject("Depuis N° AVS " + tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            docInfo.setDocumentProperty("annee", getAnnee());

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.afterBuildReport();
    }

    @Override
    public void afterExecuteReport() {

        try {

            JadePublishDocumentInfo docInfo = createDocumentInfo();
            // on ajoute au doc info le numéro de référence inforom
            docInfo.setDocumentTypeNumber(IPRConstantesExternes.ATTESTATION_FISCALE_IJ);

            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
            docInfo.setDocumentTitle(getSession().getLabel("DOC_ATTEST_FISCAL_TITLE"));
            docInfo.setDocumentDate(JACalendar.todayJJsMMsAAAA());
            docInfo.setDocumentSubject(getSession().getLabel("ATTEST_FISC_ANNEE") + " " + annee);
            docInfo.setDocumentProperty("annee", getAnnee());

            // Pour les decomptes definitifs et les client qui possedent une GED
            if (getIsSendToGED().booleanValue()) {
                // on genere le doc pour impression (mail) et on set les
                // proprietes DocInfo
                // on ne supprime pas les documents individuels car on doit les
                // envoies à la GED
                // on trie les documents sur le critère "orderPrintBy"
                this.mergePDF(docInfo, false, 0, false, IJAttestations.ORDER_PRINTING_BY);
            } else {
                // on genere le doc pour impression (mail) et on set les
                // proprietes DocInfo
                // on supprime pas les documents individuels car on ne les
                // envoies pasà la GED
                // on trie les documents sur le critère "orderPrintBy"
                this.mergePDF(docInfo, true, 0, false, IJAttestations.ORDER_PRINTING_BY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("APAttestations.afterExecuteReport():" + e.toString(), FWMessage.ERREUR,
                    "APAttestations");
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {

        try {
            Map parametres = getImporter().getParametre();

            if (parametres == null) {
                parametres = new HashMap();
                getImporter().setParametre(parametres);
            } else {
                parametres.clear();
            }

            prestation = new IJPrestation();
            prestation.setIdBaseIndemnisation(getIdBaseInd());
            prestation.setSession(getSession());

            // remplissage de l'entete
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            String adresse;

            // creation des parametres pour l'en-tete
            // ---------------------------------------------------------------------

            adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), "", IJApplication.CS_DOMAINE_ADRESSE_IJAI);
            String date = JACalendar.todayJJsMMsAAAA();

            String codeIsoLangue = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            crBean.setDate(JACalendar.format(date, codeIsoLangue));
            // Ne plus affiché NSS / Nom + prénom dans l'entête du document
            // bz - 3736
            // crBean.setNoAvs(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)
            // + "\n" + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " " +
            // tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            crBean.setAdresse(adresse);
            if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DOC_NOMCOLABO))) {
                // nom du collaborateur
                crBean.setNomCollaborateur(getSession().getUserFullName());
            }
            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DOC_CONFIDENTIEL))) {
                crBean.setConfidentiel(true);
            }

            caisseHelper.addHeaderParameters(getImporter(), crBean);

            String idTiers = tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);

            if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DISPLAY_NIP))) {
                    parametres.put("P_HEADER_NIP_LIB", getSession().getLabel("NIP") + " :");
                    parametres.put("P_HEADER_NIP", idTiers);
                }
            }

            setDocumentTitle(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            // le titre
            // ---------------------------------------------------------------------------------------------------
            StringBuffer buffer = new StringBuffer();

            buffer.setLength(0);
            String title = document.getTextes(1).getTexte(1).toString();
            title = PRStringUtils.replaceString(title, "{nomPrenom}", (tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM)));
            title = PRStringUtils.replaceString(title, "{NSS}",
                    (tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
            parametres.put("PARAM_TITRE", title);

            // l'intro
            // ------------------------------------------------------------------------------------------------

            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable params = new Hashtable();
            params.put(ITITiers.FIND_FOR_IDTIERS, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            String titre = tiersTitre.getFormulePolitesse(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));

            for (Iterator textes = document.getTextes(2).iterator(); textes.hasNext();) {
                ICTTexte texte = (ICTTexte) textes.next();

                buffer.append(texte.getDescription());
                if ("1".equals(texte.getPosition())) {
                    buffer.append("\n\n");
                }
            }

            FWMessageFormat message = createMessageFormat(buffer);

            buffer.setLength(0);
            parametres.put("PARAM_CORPS",
                    message.format(new Object[] { titre, getAnnee() }, buffer, new FieldPosition(0)).toString());

            // le "détail"
            // --------------------------------------------------------------------------------------
            buffer.setLength(0);

            parametres.put("PARAM_FIELD_COTISATIONS", document.getTextes(3).getTexte(2).toString());
            parametres.put("PARAM_FIELD_TOTAL", document.getTextes(3).getTexte(3).toString());

            parametres.put("PARAM_DEVISE", document.getTextes(3).getTexte(4).toString());

            buffer.setLength(0);
            buffer.append(document.getTextes(3).getTexte(6));
            message = createMessageFormat(buffer);
            buffer.setLength(0);
            parametres.put("PARAM_FIELD_MONTANT_COTISATIONS",
                    message.format(new Object[] { totalMontantCoti.toStringFormat() }, buffer, new FieldPosition(0))
                            .toString());

            if (!totalMontantImpot.isZero()) {

                parametres.put("PARAM_FIELD_IMPOT", document.getTextes(3).getTexte(12).toString());

                buffer.setLength(0);
                message = createMessageFormat(buffer);
                buffer.setLength(0);
                parametres.put("PARAM_FIELD_MONTANT_IMPOT", totalMontantImpot.toStringFormat());

            }

            buffer.setLength(0);
            buffer.append(document.getTextes(3).getTexte(7));
            message = createMessageFormat(buffer);
            buffer.setLength(0);
            parametres.put("PARAM_FIELD_MONTANT_TOTAL",
                    message.format(new Object[] { totalMontant.toStringFormat() }, buffer, new FieldPosition(0))
                            .toString());

            if (!totalVentilation.isZero()) {

                String chf = document.getTextes(3).getTexte(4).toString();
                Set s = new TreeSet();

                for (Iterator iterator = getList().iterator(); iterator.hasNext();) {
                    AttestationsInfos ai = (AttestationsInfos) iterator.next();

                    Iterator iter2 = ai.idsRPVentilations.iterator();

                    while (iter2.hasNext()) {
                        String idReparp = (String) iter2.next();
                        if (!s.contains(idReparp)) {
                            s.add(idReparp);
                        }
                    }
                }
                Iterator iter2 = s.iterator();
                String versementA = document.getTextes(3).getTexte(9).toString();

                Map mapVentilations = new HashMap();

                String crochet0 = document.getTextes(3).getTexte(6).toString();
                while (iter2.hasNext()) {
                    String idReparp = (String) iter2.next();
                    IJRepartitionPaiements rp = new IJRepartitionPaiements();
                    rp.setSession(getSession());
                    rp.setIdRepartitionPaiement(idReparp);
                    rp.retrieve(getTransaction());

                    if (mapVentilations.containsKey(rp.getIdTiers())) {
                        InnerDataHelper dh = (InnerDataHelper) mapVentilations.get(rp.getIdTiers());
                        FWCurrency mnt = new FWCurrency(rp.getMontantVentile());
                        mnt.negate();

                        dh.montant.add(mnt);
                        mapVentilations.put(rp.getIdTiers(), dh);
                    } else {
                        PRTiersWrapper tw = PRTiersHelper.getTiersAdresseParId(getSession(), rp.getIdTiers());
                        if (tw == null) {
                            tw = PRTiersHelper.getAdministrationParId(getSession(), rp.getIdTiers());
                        }

                        // Au cas ou le tiers n'est pas trouvé !!!!
                        // Ne devrait jamais arrivé.
                        if (tw == null) {
                            String ventilations = PRStringUtils.replaceString(versementA, "{0}",
                                    "TIERS (" + rp.getIdTiers() + ")");
                            FWCurrency mnt = new FWCurrency(rp.getMontantVentile());
                            mnt.negate();
                            InnerDataHelper dh = new InnerDataHelper();
                            dh.descr = ventilations;
                            dh.montant = new FWCurrency(mnt.toString());
                            mapVentilations.put(rp.getIdTiers(), dh);
                        } else {
                            String loc = ", " + tw.getProperty(PRTiersWrapper.PROPERTY_NPA);
                            TILocaliteManager mgr = new TILocaliteManager();
                            mgr.setSession(getSession());
                            mgr.setForNumPostalLike(tw.getProperty(PRTiersWrapper.PROPERTY_NPA));
                            mgr.setForIdPays(tw.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));
                            mgr.find(getTransaction());
                            String desc = "";
                            if (!mgr.isEmpty()) {
                                loc += " " + ((TILocalite) mgr.getFirstEntity()).getLocalite();
                            }
                            desc = tw.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                    + tw.getProperty(PRTiersWrapper.PROPERTY_PRENOM);

                            // String ventilations = versementA + " " + desc +
                            // loc;
                            String ventilations = PRStringUtils.replaceString(versementA, "{0}", desc + loc);
                            FWCurrency mnt = new FWCurrency(rp.getMontantVentile());
                            mnt.negate();
                            InnerDataHelper dh = new InnerDataHelper();
                            dh.descr = ventilations;
                            dh.montant = new FWCurrency(mnt.toString());
                            mapVentilations.put(rp.getIdTiers(), dh);
                        }
                    }

                }

                String textVentilations = "";
                String textMontantVentilations = "";
                String textDeviseVentilations = "";

                Set keys = mapVentilations.keySet();

                for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    InnerDataHelper dh = (InnerDataHelper) mapVentilations.get(key);
                    textVentilations += dh.descr + "\n";

                    String m = PRStringUtils.replaceString(crochet0, "{0}", dh.montant.toStringFormat());
                    textMontantVentilations += m + "\n";
                    textDeviseVentilations += chf + "\n";

                }

                parametres.put("PARAM_FIELD_TOTAL_NET", document.getTextes(3).getTexte(10).toString());
                parametres.put("PARAM_FIELD_VENTILATIONS", textVentilations);
                parametres.put("PARAM_FIELD_MONTANT_VENTILATIONS", textMontantVentilations);
                parametres.put("PARAM_DEVISE2", textDeviseVentilations);

                totalVentilation.negate();
                totalMontant.add(totalVentilation);

                buffer.setLength(0);
                buffer.append(document.getTextes(3).getTexte(11));
                message = createMessageFormat(buffer);
                buffer.setLength(0);
                parametres.put("PARAM_FIELD_MONTANT_TOTAL_NET",
                        message.format(new Object[] { totalMontant.toStringFormat() }, buffer, new FieldPosition(0))
                                .toString());

            }

            // les paragraphes de fin
            // ---------------------------------------------------------------------------------
            buffer.setLength(0);

            for (Iterator textes = document.getTextes(4).iterator(); textes.hasNext();) {
                ICTTexte texte = (ICTTexte) textes.next();

                buffer.append(texte.getDescription());
            }

            buffer = new StringBuffer(PRStringUtils.formatMessage(buffer, titre));
            buffer.append("\n");

            parametres.put("PARAM_PIED", buffer.toString());

            // ajouter la signature
            // ---------------------------------------------------------------------------------------
            try {

                caisseHelper.addSignatureParameters(getImporter());
            } catch (Exception e) {
                throw new FWIException("Impossible de charger le pied de page", e);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "IJAttestations");
            abort();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
    }

    private String buildOrderPrintingByKey(String idAffilie, String idTiers) throws Exception {

        String nom = "";
        String prenom = "";

        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            PRTiersWrapper tierWrapper = PRTiersHelper.getTiersParId(getSession(), idTiers);

            if (tierWrapper == null) {
                tierWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
            }

            if (tierWrapper != null) {
                nom = tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM);
                prenom = tierWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
            }
        }

        return JadeStringUtil.fillWithSpaces(nom, 40) + "_" + JadeStringUtil.fillWithSpaces(prenom, 40);
    }

    private void chargerCatalogueTexte() throws FWIException {
        // Effacer les pdf a la fin
        setDeleteOnExit(true);

        try {
            // le modele
            String extensionModelCaisse = getSession().getApplication().getProperty("extensionModelITextCaisse");
            if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                setTemplateFile(IJAttestations.FICHIER_MODELE + extensionModelCaisse);
                FWIImportManager im = getImporter();
                File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                        + FWITemplateType.TEMPLATE_JASPER.toString());
                if ((sourceFile != null) && sourceFile.exists()) {
                    ;
                } else {
                    setTemplateFile(IJAttestations.FICHIER_MODELE);
                }
            } else {
                setTemplateFile(IJAttestations.FICHIER_MODELE);
            }
        } catch (Exception e) {
            setTemplateFile(IJAttestations.FICHIER_MODELE);
        }

        try {

            String codeIsoLangue1 = getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue1 = PRUtil.getISOLangueTiers(codeIsoLangue1);

            // creation du helper pour les entetes et pieds de page
            caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                    getSession().getApplication(), codeIsoLangue1);

            // chargement du catalogue de texte
            if (documentHelper == null) {
                documentHelper = PRBabelHelper.getDocumentHelper(getISession());
                documentHelper.setCsDomaine(IIJCatalogueTexte.CS_IJ);
                documentHelper.setCsTypeDocument(IIJCatalogueTexte.CS_ATTESTATION_FISCALE);
                documentHelper.setDefault(Boolean.TRUE);
                documentHelper.setActif(Boolean.TRUE);
            }

            documentHelper.setCodeIsoLangue(getSession().getCode(tiers.getProperty(PRTiersWrapper.PROPERTY_LANGUE)));

            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                getMemoryLog().logMessage("impossible de charger le catalogue de texte", FWMessage.ERREUR,
                        "IJAttestations");
                abort();
            } else {
                document = documents[0];
                setDocument(document);
            }
        } catch (Exception e) {
            throw new FWIException("impossible de charger le catalogue de texte", e);
        }
    }

    @Override
    public void createDataSource() throws Exception {

        // Chargement du tiers
        IJBaseIndemnisation bi = new IJBaseIndemnisation();
        bi.setIdBaseIndemisation(getIdBaseInd());
        bi.setSession(getSession());
        bi.retrieve();
        tiers = bi.loadPrononce(getTransaction()).loadDemande(getTransaction()).loadTiers(); // assure

        chargerCatalogueTexte();
        createDocumentInfoIjAttestation();

        lignes = new LinkedList();

        ArrayList liste = getList();

        totalMontant = new FWCurrency();
        totalMontantCoti = new FWCurrency();
        totalMontantImpot = new FWCurrency();
        totalVentilation = new FWCurrency();

        for (Iterator iterator = liste.iterator(); iterator.hasNext();) {
            AttestationsInfos ai = (AttestationsInfos) iterator.next();

            totalMontant.add(ai.montantTotal.doubleValue());
            totalMontantCoti.add(ai.totalMontantCotisations.doubleValue());
            totalMontantImpot.add(ai.totalMontantImpotSource.doubleValue());
            totalVentilation.add(ai.montantVentilations.doubleValue());

            StringBuffer buffer = new StringBuffer();
            FWMessageFormat message = createMessageFormat(buffer);
            HashMap champs = new HashMap();

            String dateDebut = PRDateFormater.formatDateFrom(ai.dateDebut);
            String dateFin = PRDateFormater.formatDateFrom(ai.dateFin);

            buffer.append(document.getTextes(3).getTexte(1));

            message = createMessageFormat(buffer);
            buffer.setLength(0);

            String p = message.format(new Object[] { dateDebut, dateFin }, buffer, new FieldPosition(0)).toString();

            champs.put("FIELD_PERIODE", p);

            buffer.setLength(0);
            buffer.append(document.getTextes(3).getTexte(5));
            message = createMessageFormat(buffer);
            buffer.setLength(0);

            String m = message.format(new Object[] { JANumberFormatter.formatNoRound(ai.totalMontantIJ.toString()) },
                    buffer, new FieldPosition(0)).toString();

            champs.put("FIELD_MONTANT_IJ", m);

            lignes.add(champs);

        }

        if (lignes.size() > 0) {
            this.setDataSource(lignes);
        }
    }

    /**
     * Set les proprietes du JadePublishDocumentInfo pour archivaghe du document dans la GED
     */
    public void createDocumentInfoIjAttestation() {
        JadePublishDocumentInfo docInfo = getDocumentInfo();
        docInfo.setPublishDocument(false);
        docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        docInfo.setDocumentTitle(getSession().getLabel("DOC_ATTEST_FISCAL_TITLE"));
        docInfo.setDocumentDate(JACalendar.todayJJsMMsAAAA());
        docInfo.setDocumentType(IPRConstantesExternes.ATTESTATION_FISCALE_IJ);
        docInfo.setDocumentTypeNumber(IPRConstantesExternes.ATTESTATION_FISCALE_IJ);
        docInfo.setDocumentProperty("annee", getAnnee());

        try {

            TIDocumentInfoHelper.fill(docInfo, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getSession(),
                    IntRole.ROLE_IJAI, "", "");

            // BZ 8076
            // docInfo.setDocumentType(IPRConstantesExternes.ATTESTATION_FISCALE_APG);
            // docInfo.setDocumentTypeNumber(IPRConstantesExternes.ATTESTATION_FISCALE_APG);

            docInfo.setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, documentHelper.getCsTypeDocument());

            // on set les proprietes du DocInfo pour l'archivage uniquement
            // - pour les client qui possedent une GED
            if (getIsSendToGED().booleanValue()) {
                docInfo.setArchiveDocument(true);
            } else {
                docInfo.setArchiveDocument(false);
            }

            // on ajoute au doc info le critere de tri pour les impressions
            // ORDER_PRINTING_BY
            docInfo.setDocumentProperty(IJAttestations.ORDER_PRINTING_BY,
                    buildOrderPrintingByKey("", tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS)));

        } catch (RemoteException e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJDecompte afterPrintDocument():" + e.getMessage(), FWMessage.ERREUR,
                    "IJDecomptes");
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJDecompte afterPrintDocument():" + e.getMessage(), FWMessage.ERREUR,
                    "IJDecomptes");
        }
    }

    private FWMessageFormat createMessageFormat(StringBuffer pattern) {
        // doubler les apostrophes pour eviter que MessageFormat se trompe
        for (int idChar = pattern.length(); --idChar >= 0;) {
            if (pattern.charAt(idChar) == '\'') {
                pattern.insert(idChar, '\'');
            }
        }

        // créer un formatteur pour la langue de la session
        FWMessageFormat retValue = new FWMessageFormat(pattern.toString());

        if (locale == null) {
            locale = new Locale(getSession().getIdLangueISO(), "CH");
        }

        retValue.setLocale(locale);

        return retValue;
    }

    public String getAnnee() {
        return annee;
    }

    public Map getAttestationsMap() {
        return attestationsMap;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public ICTDocument getDocument() {
        return document;
    }

    @Override
    protected String getEMailObject() {
        return super.getEMailObject();
    }

    public String getIdBaseInd() {
        return idBaseInd;
    }

    public Boolean getIsSendToGED() {
        return isSendToGED;
    }

    public ArrayList getList() {
        return list;
    }

    public FWCurrency getTotalMontant() {
        return totalMontant;
    }

    public FWCurrency getTotalMontantCoti() {
        return totalMontantCoti;
    }

    public String getTotalMontantIJ() {
        return totalMontantIJ;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return null;
    }

    @Override
    public boolean next() throws FWIException {

        attestationsMap = getAttestationsMap();

        Set keys = attestationsMap.keySet();
        iter = keys.iterator();

        if (iter.hasNext()) {

            IJGenererAttestationsProcess.Key key = (IJGenererAttestationsProcess.Key) iter.next();
            ArrayList list = (ArrayList) attestationsMap.get(key);

            keySyso = key.idTiers;

            // Traitement de regroupement des objets de la liste
            list = (ArrayList) traitementRegroupementPeriode(list);

            // Si la liste est vide, on prend la suivante...
            if (iter.hasNext()) {
                while (list.size() == 0) {

                    if (iter.hasNext()) {
                        key = (IJGenererAttestationsProcess.Key) iter.next();
                        list = (ArrayList) attestationsMap.get(key);
                        keySyso = key.idTiers;

                        // Traitement de regroupement des objets de la liste
                        list = (ArrayList) traitementRegroupementPeriode(list);
                    } else {
                        // On sort de la boucle
                        break;
                    }
                }
            }

            setList(list);

            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                AttestationsInfos ai = (AttestationsInfos) iterator.next();

                setIdBaseInd(ai.idBaseInd);

            }

            setEMailAddress(getEMailAddress());
            setDateDebut(dateDebut);
            setDateFin(dateFin);
            setAnnee(annee);
            setImpressionParLot(true);

            iter.remove();

            return true;

        } else {
            return false;
        }
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setAttestationsMap(Map attestationsMap) {
        this.attestationsMap = attestationsMap;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDocument(ICTDocument document) {
        this.document = document;
    }

    public void setIdBaseInd(String idBaseInd) {
        this.idBaseInd = idBaseInd;
    }

    public void setIsSendToGED(Boolean isSendToGED) {
        this.isSendToGED = isSendToGED;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }

    public void setTotalMontant(FWCurrency totalMontant) {
        this.totalMontant = totalMontant;
    }

    public void setTotalMontantCoti(FWCurrency totalMontantCoti) {
        this.totalMontantCoti = totalMontantCoti;
    }

    public void setTotalMontantIJ(String totalMontantIJ) {
        this.totalMontantIJ = totalMontantIJ;
    }

    private List traitementRegroupementPeriode(List listObjects) {

        try {

            // Itération sur une copie de la liste
            ArrayList listCopy = new ArrayList();
            listCopy.addAll(listObjects);

            // On itère sur les objets dans la liste une première fois pour
            // supprimer tous les objets dont le montant est égal à zéro
            for (Iterator iterator = listCopy.iterator(); iterator.hasNext();) {
                AttestationsInfos ai = (AttestationsInfos) iterator.next();

                boolean isMontantBrutZero = false;
                boolean isMontantNetZero = false;
                boolean isMontantCotZero = false;

                if ((ai.totalMontantIJ.doubleValue() < 0.05) && (ai.totalMontantIJ.doubleValue() > -0.05)) {
                    isMontantBrutZero = true;
                }

                if ((ai.montantTotal.doubleValue() < 0.05) && (ai.montantTotal.doubleValue() > -0.05)) {
                    isMontantNetZero = true;
                }

                if ((ai.totalMontantCotisations.doubleValue() < 0.05)
                        && (ai.totalMontantCotisations.doubleValue() > -0.05)) {
                    isMontantCotZero = true;
                }

                if (isMontantBrutZero && isMontantCotZero && isMontantNetZero) {
                    listObjects.remove(ai);
                }

            }

            java.util.Collections.sort(listObjects);

            // On itère sur les objets dans la liste une deuxième fois pour
            // regrouper les périodes selon ceci :
            listCopy = new ArrayList();
            listCopy.addAll(listObjects);

            AttestationsInfos aiPrec = null;

            JACalendar cal = new JACalendarGregorian();

            for (Iterator iterator = listCopy.iterator(); iterator.hasNext();) {
                AttestationsInfos ai = (AttestationsInfos) iterator.next();

                // Traitement de fusion de la date de début et la date de fin
                //
                // Exemples :
                //
                // Si dateFinPrec = 31.07.07 et nouvelle dateDebutCourant =
                // 01.08.07 --> Fusionner les périodes
                // Si dateDebutPrec = 31.07.07 et nouvelle dateDebutCourant =
                // 01.09.07 --> Créer nouvelle période

                if (aiPrec != null) {

                    if ((cal.daysBetween(PRDateFormater.formatDateFrom(aiPrec.dateFin),
                            PRDateFormater.formatDateFrom(ai.dateDebut)) < 2)
                            && (cal.daysBetween(PRDateFormater.formatDateFrom(aiPrec.dateFin),
                                    PRDateFormater.formatDateFrom(ai.dateDebut)) > -2)) {

                        // on regarde que ce soit durant la même année !
                        int anneePrec = (new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(aiPrec.dateFin))
                                .getYear());
                        int annee = (new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(ai.dateDebut))
                                .getYear());

                        if (annee == anneePrec) {
                            // Fusion de aiPrec dans ai
                            ai.dateDebut = aiPrec.dateDebut;
                            ai.dateFin = ai.dateFin;
                            ai.idBaseInd = ai.idBaseInd;
                            ai.montantTotal = ai.montantTotal.add(aiPrec.montantTotal);
                            ai.totalMontantIJ = ai.totalMontantIJ.add(aiPrec.totalMontantIJ);
                            ai.totalMontantImpotSource = ai.totalMontantImpotSource.add(aiPrec.totalMontantImpotSource);
                            ai.totalMontantCotisations = ai.totalMontantCotisations.add(aiPrec.totalMontantCotisations);
                            ai.montantVentilations = ai.montantVentilations.add(aiPrec.montantVentilations);

                            for (Iterator iterator2 = aiPrec.idsRPVentilations.iterator(); iterator2.hasNext();) {
                                String id = (String) iterator2.next();
                                if (!ai.idsRPVentilations.contains(id)) {
                                    ai.idsRPVentilations.add(id);
                                }
                            }

                            // mise à jour automatique

                            // suppression de aiPrec dans la liste
                            listObjects.remove(aiPrec);
                        }

                        // Sinon, on fait rien.
                    }

                }

                aiPrec = ai;
            }

        } catch (JAException e) {
            e.printStackTrace();
        }

        return listObjects;
    }

}
