package globaz.pavo.print.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.db.compte.CIRassemblementOuvertureEcritures;
import globaz.pavo.db.compte.CIRassemblementOuvertureEcrituresManager;
import globaz.pavo.db.compte.CIRassemblementOuvertureManager;
import globaz.pyxis.api.ITIAdministration;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Doc2_ExtraiCompte extends FWIDocumentManager {

    private static final long serialVersionUID = -6640635691239401915L;

    private class HeaderInfo {

        String m_caisseTexte = "";
        String m_dateNaissance = "";
        String m_etatOrigine = "";
        String m_lieuDate = "";
        String m_nomPrenom = "";
        String m_numeroAvs = "";

        HeaderInfo() {
        }

        protected ITIAdministration _getCaisseCompensation() throws Exception {
            ITIAdministration caisse = null;
            CIApplication applic = (CIApplication) getSession().getApplication();
            caisse = applic.getAdministrationLocale(getSession());
            return caisse;
        }

        void getHeaderInfoFor(CICompteIndividuel compte, BSession aSession) {
            getDocumentInfo().setDocumentTypeNumber(Doc2_ExtraiCompte.NUMERO_REFERENCE_INFOROM);
            getDocumentInfo().setDocumentType(Doc2_ExtraiCompte.NUMERO_REFERENCE_INFOROM);
            getDocumentInfo().setDocumentProperty("numero.avs.non.formatte",
                    NSUtil.unFormatAVS(m_headerInfo.m_numeroAvs));
            getDocumentInfo().setDocumentProperty("numero.avs.formatte",
                    NSUtil.formatAVSUnknown(m_headerInfo.m_numeroAvs));
            if (aSession.hasErrors()) {
                return;
            }
            m_nomPrenom = compte.getNomPrenom();
            try {
                m_numeroAvs = NSUtil.formatAVSUnknown(compte.getNumeroAvs());
            } catch (Exception e) {
            }

            ITIAdministration caisseAdmin = null;
            try {
                caisseAdmin = _getCaisseCompensation();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            m_caisseTexte = getTexteCaisseTenant(caisseAdmin.getCodeAdministration());
            m_dateNaissance = compte.getDateNaissance();
            m_etatOrigine = globaz.pavo.translation.CodeSystem.getCodeUtilisateur(compte.getPaysOrigineId(),
                    getSession());
            try {
                caisseAdmin.getLocalite();
            } catch (Exception ex) {
                // laisser vide
            }
            try {
                m_lieuDate = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE
                        + langueImp)
                        + JACalendar.todayJJsMMsAAAA();
            } catch (Exception e) {
                m_lieuDate = "n/a";
            }
        }

        String getTexteCaisseTenant(String idCaisse) {
            String companyName = new String();
            String temp = "";
            try {
                temp = _getCaisseCompensation().getCodeAdministration();
                if ((temp == null) || "null".equals(temp)) {
                    temp = "n/a";
                }
            } catch (Exception ex) {
                // laisser vide
                temp = "n/a";
            }
            try {
                CIApplication applic = (CIApplication) getSession().getApplication();
                companyName = FWMessageFormat.format(applic.getCaisseCompForExtrait(), temp);
            } catch (Exception e) {
            }
            return companyName;
        }
    }

    private static final String MODEL_NAME = "PAVO_EXTR_CI_MASTER"; // nom du template jasper
    private static final String NUMERO_REFERENCE_INFOROM = "0063CCI";

    /**
     * @param session
     * @param idLangue
     *            langue ISO
     * @param codeTitre
     *            code système du titrre à retourner (
     *            <code>IConstantes.CS_TIERS_TITRE_MONSIEUR ; IConstantes.CS_TIERS_TITRE_MADAME</code>)
     * @return
     */
    public static String titrePolitesse(BSession session, String idLangue, String codeTitre) {
        try {
            return CodeSystem.getLibelleIso(session, codeTitre, idLangue);
        } catch (Exception e) {
            JadeLogger.error(Doc2_ExtraiCompte.class, e);
        }
        return "";
    }

    private String adresseAssure = null;
    private ICaisseReportHelper caisseReportHelper = null;
    private CICompteIndividuel ci = null;
    private java.lang.String compteIndividuelId = new String();
    private String etatEcritures = new String();
    private String fromAnnee = "";
    private Boolean imprimerLies = new Boolean(false);
    private Iterator<String> itCi = null;
    private String langueImp = "";
    private HeaderInfo m_headerInfo = null;
    private String mailSubject = new String();
    private String rassemblementEcritureId = new String();
    private String titreAssure = null;

    private String untilAnnee = "";

    public Doc2_ExtraiCompte() throws Exception {
        this(new BSession(CIApplication.DEFAULT_APPLICATION_PAVO));
    }

    /**
     * @param session
     *            globaz.globall.db.BSession
     */
    public Doc2_ExtraiCompte(BProcess parent) throws Exception {
        super(parent, CIApplication.APPLICATION_PAVO_REP, parent.getSession().getLabel("MSG_EXTRAIT_CI"));
        super.setFileTitle("Extrait du compte Individuel");
    }

    /**
     * @param session
     *            globaz.globall.db.BSession
     */
    public Doc2_ExtraiCompte(BSession session) throws Exception {
        super(session, CIApplication.APPLICATION_PAVO_REP, session.getLabel("MSG_EXTRAIT_CI"));
        super.setFileTitle("Extrait du compte Individuel");
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText() {
        super.setParametres(Doc1_CI_Param.P_HEAD_1,
                "Auszug aus dem individuellen Konto\nExtrait du compte individuel\nEstratto del conto individuale");
        super.setParametres(Doc1_CI_Param.P_HEAD_2, m_headerInfo.m_nomPrenom);
        super.setParametres(Doc1_CI_Param.P_HEAD_3, m_headerInfo.m_numeroAvs);
        super.setParametres(Doc1_CI_Param.P_HEAD_4, m_headerInfo.m_caisseTexte);
        super.setParametres(Doc1_CI_Param.P_HEAD_5, m_headerInfo.m_dateNaissance);
        super.setParametres(Doc1_CI_Param.P_HEAD_6, m_headerInfo.m_etatOrigine);
        if ("DE".equals(langueImp)) {
            super.setParametres(Doc1_CI_Param.P_MOTIF, "Keine Buchung für diese Ausgleichskasse vorhanden");
        } else if ("FR".equals(langueImp)) {
            super.setParametres(Doc1_CI_Param.P_MOTIF, "Aucune inscription pour cette caisse de compensation");
        } else {
            super.setParametres(Doc1_CI_Param.P_MOTIF, "Nessun contributo registrato per questa cassa di compensazione");
        }
    }

    /**
     * Création du paramètre de référence pour les documents de type liste
     */
    protected void _setRefParam() {
        try {
            String text = getSession().getUserId() + " / 98";
            super.setParametres(Doc1_CI_Param.P_REFERENCE, text);
        } catch (Exception e) {
        }
    }

    protected void _summaryText() {
        super.setParametres(Doc1_CI_Param.L_1, "Abrechnungsnummer\nNuméro d'affilié\nNumero di affiliato");
        super.setParametres(Doc1_CI_Param.L_2, "Einkommenscode\nCode revenu\nCodice reddito");
        super.setParametres(Doc1_CI_Param.L_3,
                "Bruchteil der Betreuungsgutschrift\nPart aux bonifications d'assistance\nParte degli accrediti d'assistenza");
        super.setParametres(Doc1_CI_Param.L_4,
                "Beitragsmonate (Beginn/Ende)\nMois de cotisation (début/fin)\nMesi di contribuzione (inizio/fine)");
        super.setParametres(Doc1_CI_Param.L_5, "Beitragsjahr\nAnnée de cotisation\nAnno di contribuzione");
        super.setParametres(Doc1_CI_Param.L_6, "Einkommen\nRevenu\nReddito");
        super.setParametres(Doc1_CI_Param.L_7,
                "Beachten Sie das beigelegte Merkblatt\nVoir le mémento annexé\nVedasi il promemoria allegato");
        super.setParametres(Doc1_CI_Param.P_SUM_LIEU_DATE, m_headerInfo.m_lieuDate);
        _setRefParam();
    }

    /**
     * Commentaire relatif à la méthode _tableHeader.
     */
    protected void _tableHeader() {
        super.setParametres(Doc1_CI_Param.L_8,
                "Arbeitgeber oder Einkommensart\nEmployeurs ou genre de revenu\nDatori di lavoro o genere del reddito");
        super.setParametres(Doc1_CI_Param.L_9, "Heimatstaat/Etat d'origine/Stato d'origine:");
        super.setParametres(Doc1_CI_Param.L_10, "Kassen-Nr.\nN° caisse\nN° cassa");
    }

    @Override
    protected void _validate() {
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getSession().getLabel("MSG_EMAIL_INV"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("MSG_EMAIL_INV"));
            }
        }
        if (JadeStringUtil.isBlank(getCompteIndividuelId())) {
            this._addError("Compte Individuel invalide");
        }
        if (!hasExecRight()) {
            this._addError(getSession().getLabel("MSG_CI_NO_AUTH"));
        }
        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
        }
    }

    /**
     * Après la création de tous les documents
     */
    @Override
    public void afterExecuteReport() {
        try {
            fusionneDocuments();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    @Override
    public void afterPrintDocument() {
        try {
            super.afterPrintDocument();
        } catch (Exception e) {

        }
        if (m_headerInfo != null) {
            String numAvs = m_headerInfo.m_numeroAvs;
            if (!JadeStringUtil.isEmpty(numAvs)) {
                TIDocumentInfoHelper.fillNumAvsNss(getDocumentInfo(), numAvs, JadeStringUtil.change(numAvs, ".", ""));
            }
        }
    }

    @Override
    public void beforeBuildReport() {
        super.setDocumentTitle(NSUtil.formatAVSUnknown(getCi().getNumeroAvs()));
        getExporter().setExportFileName(getSession().getLabel("MSG_EXTRAIT_CI"));
    }

    @Override
    public void beforeExecuteReport() {
        super.setTemplateFile(Doc2_ExtraiCompte.MODEL_NAME);
        List<String> temp = new ArrayList<String>();
        CICompteIndividuel ci = getCi();
        setMailSubject(ci.getNssFormate() + " / " + ci.getNomPrenom());
        temp.add(getCompteIndividuelId());

        if (imprimerLies.booleanValue()) {
            Object[] CIlies = ci.getCILies();
            for (int i = 0; i < CIlies.length; i++) {
                String[] ids = (String[]) CIlies[i];
                if (!ids[0].equals("")) {
                    temp.add(ids[0]);
                }
            }

        }

        itCi = temp.iterator();

        // Génère la lettre d'accompagnement si une adresse a été renseignée
        if (!JadeStringUtil.isBlank(getAdresseAssure())) {
            try {
                createLettreAccompagnement(this, getSession(), getAdresseAssure());
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
    }

    @Override
    public void createDataSource() throws Exception {
        Doc1_ExtraiCompte_DS manager = null;

        try {
            m_headerInfo = new HeaderInfo();
            m_headerInfo.getHeaderInfoFor(getCi(), getSession());
            manager = new Doc1_ExtraiCompte_DS();
            manager.setLangueImp(getLangueImp());
            manager.setSession(getSession());
            manager.setCacherEcritureProtege(1);
            if (hasExecRight()) {
                if ((etatEcritures.length() != 0) && !"toutesEcritures".equals(etatEcritures)) {
                    if ("tous".equals(etatEcritures) || "seulementClotures".equals(etatEcritures)) {
                        // toutes écritures clôturées
                        manager.setForNotRassemblementOuvertureId("0");
                    } else if ("active".equals(etatEcritures)) {
                        // toutes les écriture actives
                        manager.setForRassemblementOuvertureId("0");
                    } else {
                        // écritures d'une clôture
                        manager.setForRassemblementOuvertureId(etatEcritures);
                    }
                }
                if (!JadeStringUtil.isBlank(getRassemblementEcritureId())) {
                    CIRassemblementOuvertureEcrituresManager mgrRass = new CIRassemblementOuvertureEcrituresManager();
                    mgrRass.setForRassemblementOuvertureId(getRassemblementEcritureId());
                    mgrRass.setSession(getSession());
                    mgrRass.find(getTransaction());
                    String idsEcritures = new String();
                    for (int i = 0; i < mgrRass.size(); i++) {
                        if (!JadeStringUtil.isBlank(idsEcritures)) {
                            idsEcritures += ", ";
                        }
                        idsEcritures += ((CIRassemblementOuvertureEcritures) mgrRass.getEntity(i))
                                .getIdEcritureAssure();
                    }
                    manager.setForIdIn(idsEcritures);
                }
                manager.setForIdTypeCompteCompta("true");
                manager.setForCompteIndividuelId(getCompteIndividuelId());
                manager.setFromAnnee(getFromAnnee());
                manager.setUntilAnnee(getUntilAnnee());
                manager.setOrderBy("KBNANN ASC, KBNMOD ASC");
                if (JadeStringUtil.isBlank(getRassemblementEcritureId())) {
                    CIRassemblementOuvertureManager rassemblFind = new CIRassemblementOuvertureManager();
                    rassemblFind.setForCompteIndividuelId(getCompteIndividuelId());
                    rassemblFind.setSession(getSession());
                    rassemblFind.setForDateOrdre(JACalendar.todayJJsMMsAAAA());
                    rassemblFind.setForTypeEnregistrement(CIRassemblementOuverture.CS_EXTRAIT);
                    rassemblFind.find(getTransaction());
                    if (rassemblFind.isEmpty()) {
                        CIRassemblementOuverture rassembl = new CIRassemblementOuverture();
                        rassembl.setSession(getSession());
                        // String dateOrdreBidouille =
                        // remoteAnnonceCompl.get(IHEAnnoncesViewBean.DATE_ORDRE_JJMMAA);
                        rassembl.setDateOrdre(JACalendar.todayJJsMMsAAAA());
                        rassembl.setTypeEnregistrement(CIRassemblementOuverture.CS_EXTRAIT);
                        rassembl.setMotifArc("98");
                        rassembl.setCaisseCommettante(m_headerInfo._getCaisseCompensation().getIdTiers());
                        // rassembl.setReferenceInterne(remoteAnnonce.get(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
                        rassembl.setCompteIndividuelId(getCompteIndividuelId());
                        rassembl.add(getTransaction());
                    }
                }
            } else {
                // ci n'existant pas
                manager.setForCompteIndividuelId("0");
            }
            // super.manager = (BManager) manager;
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        super.setDataSource(manager.getCollectionData());
        try {
            super.setParametres("SUBREPORT_SOURCE", new JRBeanCollectionDataSource(manager.getCollectionData()));
        } catch (Exception e) {
        }
        try {
            super.setTemplateFile("PAVO_EXTR_CI_MASTER");
        } catch (Exception e) {
        }

        _headerText();
        _summaryText();
        _tableHeader();
    }

    public CIExtraitLettreAccompagnement createLettreAccompagnement(BProcess process, BSession session, String adresse)
            throws Exception {
        // Instancie le document du plan de recouvrement : Décision
        CIExtraitLettreAccompagnement document = new CIExtraitLettreAccompagnement(process, "lettreAccompagnement");
        document.setSession(session);
        // Demander le traitement du document
        document.setEMailAddress(process.getEMailAddress());

        if (!JadeStringUtil.isBlank(getTitreAssure())) {
            adresse = CodeSystem.getLibelleIso(getSession(), getTitreAssure(), getLangueImp()) + "\n" + adresse;
            document.setTitrePolitesse(getTitreAssure());
        }

        document.setAdresseDest(adresse);
        document._setLangue(getLangueImp());
        document.setNSS(getCi().getNssFormate());

        document.executeProcess();

        if (document.getDocumentList().size() <= 0) {
            throw new Exception(document.getClass().getName() + "._executeProcess() : Error, document "
                    + document.getImporter().getDocumentTemplate() + " can not be created !");
        }

        return document;
    }

    /**
     * Fusionne les documents. <br>
     * Envoie un e-mail avec les pdf fusionnés. <br>
     * 
     * @author: sel Créé le : 16 nov. 06
     * @throws Exception
     */
    private void fusionneDocuments() throws Exception {
        // Fusionne les documents
        // Les documents fusionnés sont effacés (théoriquement!!)
        JadePublishDocumentInfo info = createDocumentInfo();
        // Envoie un e-mail avec les pdf fusionnés
        info.setDocumentTypeNumber(Doc2_ExtraiCompte.NUMERO_REFERENCE_INFOROM);
        info.setPublishDocument(true);
        info.setArchiveDocument(false);
        this.mergePDF(info, true, 500, false, null);
    }

    /**
     * @return the adresseAssure
     */
    public String getAdresseAssure() {
        return adresseAssure;
    }

    /**
     * @return the caisseReportHelper
     */
    public ICaisseReportHelper getCaisseReportHelper() {
        return caisseReportHelper;
    }

    /**
     * @return globaz.pavo.db.compte.CICompteIndividuel
     */
    public globaz.pavo.db.compte.CICompteIndividuel getCi() {
        ci = new CICompteIndividuel();
        ci.setSession(getSession());
        ci.setCompteIndividuelId(getCompteIndividuelId());
        try {
            ci.retrieve();
        } catch (Exception e) {
            ci = null;
        }
        return ci;
    }

    /**
     * Retourne la liste de log afin de l'utiliser pour {@link globaz.jsp.taglib.FWListSelectTag} Date de création :
     * (10.12.2002 08:55:41)
     * 
     * @return java.util.Vector
     */
    public Vector<String[]> getClotures() {
        Vector<String[]> list = new Vector<String[]>();

        // ajouter tous et écritures actives
        list.add(new String[] { "active", "" });
        list.add(new String[] { "tous", getSession().getLabel("MSG_CI_ETAT_TOUS") });

        // chercher toutes les clôtures du compte
        CIRassemblementOuvertureManager clotures = new CIRassemblementOuvertureManager();
        clotures.setSession(getSession());
        clotures.setForCompteIndividuelId(getCompteIndividuelId());
        try {
            clotures.find();
            for (int i = 0; i < clotures.size(); i++) {
                CIRassemblementOuverture cloture = (CIRassemblementOuverture) clotures.getEntity(i);
                // ne pas inclure les clôtures révoquées
                if (cloture.isCloture() && JadeStringUtil.isBlank(cloture.getDateRevocation())) {
                    list.add(new String[] {
                            cloture.getRassemblementOuvertureId(),
                            cloture.getDateCloture().substring(3) + " - " + cloture.getMotifArc() + " - "
                                    + cloture.getCaisseAgenceCommettante() });
                }
            }
        } catch (Exception ex) {
            // retourne liste vide
        }
        return list;
    }

    public java.lang.String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    @Override
    protected java.lang.String getEMailObject() {
        String message = "";
        if ("DE".equalsIgnoreCase(langueImp)) {
            message = " IK-Auszug " + ci.getNssFormate() + " / " + ci.getNomPrenom();
        } else {
            message = " Extrait de CI " + ci.getNssFormate() + " / " + ci.getNomPrenom();

        }
        if (isOnError()) {
            return message + " konnte nicht durchgeführt werden";
        } else {
            return message;
        }
    }

    /**
     * @return java.lang.String
     */
    public java.lang.String getEtatEcritures() {
        return etatEcritures;
    }

    /**
     * Returns the fromAnnee.
     * 
     * @return String
     */
    public String getFromAnnee() {
        return fromAnnee;
    }

    /**
     * @return
     */
    public Boolean getImprimerLies() {
        return imprimerLies;
    }

    /**
     * @return
     */
    public String getLangueImp() {
        return langueImp;
    }

    /**
     * @return
     */
    public Vector<String[]> getListeTitre() {
        Vector<String[]> vList = new Vector<String[]>();

        // vide
        String[] vide = new String[2];
        vide[0] = "";
        vide[1] = "";
        vList.add(vide);

        // Monsieur
        String[] monsieur = new String[2];
        monsieur[0] = IConstantes.CS_TIERS_TITRE_MONSIEUR;
        monsieur[1] = Doc2_ExtraiCompte.titrePolitesse(getSession(), getSession().getIdLangueISO(),
                IConstantes.CS_TIERS_TITRE_MONSIEUR);
        vList.add(monsieur);

        // Madame
        String[] madame = new String[2];
        madame[0] = IConstantes.CS_TIERS_TITRE_MADAME;
        madame[1] = Doc2_ExtraiCompte.titrePolitesse(getSession(), getSession().getIdLangueISO(),
                IConstantes.CS_TIERS_TITRE_MADAME);
        vList.add(madame);

        return vList;
    }

    /**
     * @return
     */
    public String getMailSubject() {
        return mailSubject;
    }

    /**
     * Returns the rassemblementEcritureId.
     * 
     * @return String
     */
    public String getRassemblementEcritureId() {
        return rassemblementEcritureId;
    }

    /**
     * @return the titreAssure
     */
    public String getTitreAssure() {
        return titreAssure;
    }

    /**
     * Returns the untilAnnee.
     * 
     * @return String
     */
    public String getUntilAnnee() {
        return untilAnnee;
    }

    /**
     * @return boolean
     */
    public boolean hasExecRight() {
        if (getCi() == null) {
            return false;
        } else {
            return getCi().hasUserShowRight(null);
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        boolean isOk = itCi.hasNext();
        if (isOk) {
            setCompteIndividuelId(itCi.next());
        }
        return isOk;
    }

    /**
     * @param adresseAssure
     *            the adresseAssure to set
     */
    public void setAdresseAssure(String adresseAssure) {
        this.adresseAssure = adresseAssure;
    }

    /**
     * @param caisseReportHelper
     *            the caisseReportHelper to set
     */
    public void setCaisseReportHelper(ICaisseReportHelper caisseReportHelper) {
        this.caisseReportHelper = caisseReportHelper;
    }

    /**
     * @param newCompteIndividuelId
     *            java.lang.String
     */
    public void setCompteIndividuelId(java.lang.String newCompteIndividuelId) {
        compteIndividuelId = newCompteIndividuelId;
    }

    /**
     * @param newEtatEcritures
     *            java.lang.String
     */
    public void setEtatEcritures(java.lang.String newEtatEcritures) {
        etatEcritures = newEtatEcritures;
    }

    /**
     * Sets the fromAnnee.
     * 
     * @param fromAnnee
     *            The fromAnnee to set
     */
    public void setFromAnnee(String fromAnnee) {
        this.fromAnnee = fromAnnee;
    }

    /**
     * @param boolean1
     */
    public void setImprimerLies(Boolean boolean1) {
        imprimerLies = boolean1;
    }

    /**
     * @param string
     */
    public void setLangueImp(String string) {
        langueImp = string;
    }

    /**
     * @param string
     */
    public void setMailSubject(String string) {
        mailSubject = string;
    }

    /**
     * Sets the rassemblementEcritureId.
     * 
     * @param rassemblementEcritureId
     *            The rassemblementEcritureId to set
     */
    public void setRassemblementEcritureId(String rassemblementEcritureId) {
        this.rassemblementEcritureId = rassemblementEcritureId;
    }

    /**
     * @param titreAssure
     *            the titreAssure to set
     */
    public void setTitreAssure(String titreAssure) {
        this.titreAssure = titreAssure;
    }

    /**
     * Sets the untilAnnee.
     * 
     * @param untilAnnee
     *            The untilAnnee to set
     */
    public void setUntilAnnee(String untilAnnee) {
        this.untilAnnee = untilAnnee;
    }

}
