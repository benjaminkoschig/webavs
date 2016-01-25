/*
 * Cr�� le 5 sept. 07
 */
package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.REAnnoncesGroupePrestations;
import globaz.corvus.db.annonces.REAnnoncesGroupePrestationsManager;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author BSC
 * 
 *         Imprime la liste des annonces pour un mois donn�
 */
public class REListeAnnonces extends FWIAbstractManagerDocumentList {

    // ---- fields -->

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            BSession session = new BSession(REApplication.DEFAULT_APPLICATION_CORVUS);
            session.connect("globazf", "ssiiadm");
            REListeRecapitulativePaiement process = new REListeRecapitulativePaiement();
            process.setSession(session);
            process.setForMoisAnnee("08.2007");
            process.setEMailAddress("bsc");
            JadeLogger.enableDebug(true);
            BProcessLauncher.start(process);

            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    FWIDocumentTable annoncesPourGroupePrestation = null;
    private Boolean isAnnoncesSubsequentes = false;
    private boolean isFirstRowAfterGroupBeginning = false;
    private String mois = null;
    FWCurrency montantTotalAugmentation = null;

    FWCurrency montantTotalDiminution = null;

    // ---- Constructeur -->
    /**
     * Constructeur par defaut
     */
    public REListeAnnonces() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
    }

    /**
     * Constructeur
     * 
     * @param session
     */
    public REListeAnnonces(BProcess parent) throws Exception {
        this();
        super.setParentWithCopy(parent);
    }

    // ---- Surcharge -->

    /**
     * Constructeur
     * 
     * @param session
     */
    public REListeAnnonces(BSession session) {
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("LISTE_ANN_TITRE"),
                new REAnnoncesGroupePrestationsManager(), REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    /**
     * M�thode appel�e avant l'ex�cution du rapport
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList#_beforeExecuteReport()
     **/
    @Override
    public void _beforeExecuteReport() {

        // on ajoute au doc info le num�ro de r�f�rence inforom
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_ANNONCES_PDF);

        REAnnoncesGroupePrestationsManager manager = (REAnnoncesGroupePrestationsManager) _getManager();
        manager.setSession(getSession());
        manager.setForMoisRapport(getMois());
        manager.setWantGroupePrestation(true);

        if (getIsAnnoncesSubsequentes()) {
            manager.setForAnnoncesSubsquentes(getIsAnnoncesSubsequentes());
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("LISTE_ANN_TITRE") + " de " + getMois());

        super._beforeExecuteReport();
    }

    /**
     * Ajoute une ligne dans la table de donn�es (utiliser <code>_addCell(Object)</code>)
     * 
     * @param entity
     *            l'entit� contenant les donn�es
     * @exception java.lang.Exception
     *                en cas s'erreur
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#addRow(globaz.globall.db.BEntity)
     **/
    @Override
    protected void addRow(BEntity value) throws FWIException {
        REAnnoncesGroupePrestations entity = (REAnnoncesGroupePrestations) value;

        try {

            // on affiche le titre
            if (isFirstRowAfterGroupBeginning) {
                isFirstRowAfterGroupBeginning = false;
                this._addLine(getFontHeaderPage(), getLibelleDebutGroupe(entity.getGroupePrestation()), null, "", null,
                        "");
            }

            // Recherche des information sur le tiers beneficiaire
            PRTiersWrapper assure = PRTiersHelper.getPersonneAVS(getSession(), entity.getIdTiers());
            if (assure != null) {
                annoncesPourGroupePrestation.addCell(assure.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                        + assure.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                        + assure.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                        + assure.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                        + getLibelleCourtSexe(assure.getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / "
                        + getLibellePays(assure.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            } else {
                annoncesPourGroupePrestation.addCell("Tiers non-d�finit : idTiers=" + entity.getIdTiers());
            }

            annoncesPourGroupePrestation.addCell(PRDateFormater.convertDate_MMAA_to_MMxAAAA(JadeStringUtil
                    .fillWithZeroes(entity.getDebutDroit(), 4)));
            annoncesPourGroupePrestation.addCell(PRDateFormater.convertDate_MMAA_to_MMxAAAA(JadeStringUtil
                    .fillWithZeroes(entity.getFinDroit(), 4)));
            annoncesPourGroupePrestation.addCell(entity.getCodeMutation());
            annoncesPourGroupePrestation.addCell(entity.getGenrePrestation());

            // en fonction du code d'application de l'annonce, on definit si
            // augmentation ou diminution
            if (entity.getCodeApplication().equals("41") || entity.getCodeApplication().equals("44")) {

                // si une date de fin, on force a zero, sinon on renseigne
                // l'augmentation
                if (JadeStringUtil.isEmpty(entity.getFinDroit())) {

                    annoncesPourGroupePrestation.addCell(new FWCurrency(entity.getMensualitePrestationsFrancs())
                            .toStringFormat());
                    annoncesPourGroupePrestation.addCell("0.00");
                    montantTotalAugmentation.add(entity.getMensualitePrestationsFrancs());

                } else {

                    annoncesPourGroupePrestation.addCell("0.00");
                    annoncesPourGroupePrestation.addCell("0.00");

                }

            }
            if (entity.getCodeApplication().equals("42") || entity.getCodeApplication().equals("45")) {

                annoncesPourGroupePrestation.addCell("0.00");
                annoncesPourGroupePrestation.addCell(new FWCurrency(entity.getMensualitePrestationsFrancs())
                        .toStringFormat());
                montantTotalDiminution.add(entity.getMensualitePrestationsFrancs());

            }
            if (entity.getCodeApplication().equals("43") || entity.getCodeApplication().equals("46")) {

                // Diminution
                if (entity.getCodeMutation().equals("77")) {
                    annoncesPourGroupePrestation.addCell("0.00");
                    annoncesPourGroupePrestation.addCell(new FWCurrency(entity.getMensualitePrestationsFrancs())
                            .toStringFormat());
                    montantTotalDiminution.add(entity.getMensualitePrestationsFrancs());

                    // Augmentation
                } else if (entity.getCodeMutation().equals("78")) {
                    annoncesPourGroupePrestation.addCell(new FWCurrency(entity.getMensualitePrestationsFrancs())
                            .toStringFormat());
                    annoncesPourGroupePrestation.addCell("0.00");
                    montantTotalAugmentation.add(entity.getMensualitePrestationsFrancs());

                } else {
                    annoncesPourGroupePrestation.addCell("0.00");
                    annoncesPourGroupePrestation.addCell("0.00");
                }
            }

            annoncesPourGroupePrestation.addRow();

        } catch (Exception e) {
            throw new FWIException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #beginGroup(int,
     * globaz.globall.db.BEntity, globaz.globall.db.BEntity)
     */
    @Override
    protected void beginGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {
        isFirstRowAfterGroupBeginning = true;

        // initialisation pour un nouveau groupe de prestation
        annoncesPourGroupePrestation = new FWIDocumentTable();
        annoncesPourGroupePrestation.addColumn(getSession().getLabel("LISTE_ANN_DETAIL_ASSURE"),
                FWIAbstractDocumentList.LEFT, 6);
        annoncesPourGroupePrestation.addColumn(getSession().getLabel("LISTE_ANN_DEBUT_DROIT"),
                FWIAbstractDocumentList.RIGHT, 1);
        annoncesPourGroupePrestation.addColumn(getSession().getLabel("LISTE_ANN_FIN_DROIT"),
                FWIAbstractDocumentList.RIGHT, 1);
        annoncesPourGroupePrestation.addColumn(getSession().getLabel("LISTE_ANN_CODE_MUTATION"),
                FWIAbstractDocumentList.RIGHT, 2);
        annoncesPourGroupePrestation.addColumn(getSession().getLabel("LISTE_ANN_CODE_PRESTATION"),
                FWIAbstractDocumentList.RIGHT, 2);
        annoncesPourGroupePrestation.addColumn(getSession().getLabel("LISTE_ANN_MONTANT_AUGMENTATION"),
                FWIAbstractDocumentList.RIGHT, 2);
        annoncesPourGroupePrestation.addColumn(getSession().getLabel("LISTE_ANN_MONTANT_DIMINUTION"),
                FWIAbstractDocumentList.RIGHT, 2);
        annoncesPourGroupePrestation.setDefaultGroupFont(getFontColumn());
        annoncesPourGroupePrestation.endTableDefinition();

        montantTotalAugmentation = new FWCurrency("0.0");
        montantTotalDiminution = new FWCurrency("0.0");
    }

    // <-- Properties -->

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #endGroup(int,
     * globaz.globall.db.BEntity, globaz.globall.db.BEntity)
     */
    @Override
    protected void endGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {

        REAnnoncesGroupePrestations entity = (REAnnoncesGroupePrestations) lastEntity;

        // on ajoute le total du groupe
        annoncesPourGroupePrestation.addCell("_______________________________________________");
        annoncesPourGroupePrestation.addCell("_______________________________________________");
        annoncesPourGroupePrestation.addCell("_______________________________________________");
        annoncesPourGroupePrestation.addCell("_______________________________________________");
        annoncesPourGroupePrestation.addCell("_______________________________________________");
        annoncesPourGroupePrestation.addCell("_______________________________________________");
        annoncesPourGroupePrestation.addCell("_______________________________________________");
        annoncesPourGroupePrestation.addRow();

        annoncesPourGroupePrestation.addCell(getLibelleTotalGroupe(entity.getGroupePrestation()));
        annoncesPourGroupePrestation.addCell("");
        annoncesPourGroupePrestation.addCell("");
        annoncesPourGroupePrestation.addCell("");
        annoncesPourGroupePrestation.addCell("");
        annoncesPourGroupePrestation.addCell(montantTotalAugmentation.toStringFormat());
        annoncesPourGroupePrestation.addCell(montantTotalDiminution.toStringFormat());
        annoncesPourGroupePrestation.addGroupRow();

        super._addTable(annoncesPourGroupePrestation);
        super._addPageBreak();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #getGroupValue(int,
     * globaz.globall.db.BEntity)
     */
    @Override
    protected Object getGroupValue(int level, BEntity entity) throws FWIException {
        return ((REAnnoncesGroupePrestations) entity).getGroupePrestation();
    }

    public Boolean getIsAnnoncesSubsequentes() {
        return isAnnoncesSubsequentes;
    }

    /**
     * M�thode qui retourne le libell� court du sexe par rapport au csSexe qui est pass� en param�tre
     * 
     * @return le libell� court du sexe (H ou F)
     */
    private String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    /**
     * Donne le titre du regroupement en fonction du groupe
     * 
     * @param groupe
     * @return
     */
    private String getLibelleDebutGroupe(String groupe) {
        int gp = Integer.parseInt(groupe);
        if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RO_AVS)) {
            return FWMessageFormat.format(getSession().getLabel("LISTE_ANNONCES_DU_POUR_RO_AVS"), getMois());
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RE_AVS)) {
            return FWMessageFormat.format(getSession().getLabel("LISTE_ANNONCES_DU_POUR_RE_AVS"), getMois());
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RO_AI)) {
            return FWMessageFormat.format(getSession().getLabel("LISTE_ANNONCES_DU_POUR_RO_AI"), getMois());
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RE_AI)) {
            return FWMessageFormat.format(getSession().getLabel("LISTE_ANNONCES_DU_POUR_RE_AI"), getMois());
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_API_AVS)) {
            return FWMessageFormat.format(getSession().getLabel("LISTE_ANNONCES_DU_POUR_API_AVS"), getMois());
        } else {
            return FWMessageFormat.format(getSession().getLabel("LISTE_ANNONCES_DU_POUR_API_AI"), getMois());
        }
    }

    /**
     * M�thode qui retourne le libell� de la nationalit� par rapport au csNationalit� qui est pass� en param�tre
     * 
     * @return le libell� du pays (retourne une cha�ne vide si pays inconnu)
     */
    private String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }
    }

    /**
     * Donne le titre du total du regroupement en fonction du groupe
     * 
     * @param groupe
     * @return
     */
    private String getLibelleTotalGroupe(String groupe) {
        int gp = Integer.parseInt(groupe);
        if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RO_AVS)) {
            return getSession().getLabel("LISTE_TOTAL_RO_AVS");
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RE_AVS)) {
            return getSession().getLabel("LISTE_TOTAL_RE_AVS");
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RO_AI)) {
            return getSession().getLabel("LISTE_TOTAL_RO_AI");
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_RE_AI)) {
            return getSession().getLabel("LISTE_TOTAL_RE_AI");
        } else if (gp == Integer.parseInt(REAnnoncesGroupePrestationsManager.GROUPE_API_AVS)) {
            return getSession().getLabel("LISTE_TOTAL_API_AVS");
        } else {
            return getSession().getLabel("LISTE_TOTAL_API_AI");
        }
    }

    /**
     * @return
     */
    public String getMois() {
        return mois;
    }

    /**
     * Initialise la table des donn�es
     * <p>
     * <u>Utilisation</u>:
     * <ul>
     * <li><code>_addColumn(..)</code> permet de d�clarer les colonnes
     * <li><code>_group...(..)</code> permet de d�clarer les groupages
     * </ul>
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#initializeTable()
     **/
    @Override
    protected void initializeTable() {
        this._addColumnCenter(getSession().getLabel("LISTE_ANN_DETAIL_ASSURE"));
        _groupManual();
    }

    /**
     * Renvoie la Job Queue � utiliser pour soumettre le process (constantes dans <code>GlobazJobQueue</code>).
     * 
     * @return la Job Queue � utiliser pour soumettre le process
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     **/
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setIsAnnoncesSubsequentes(Boolean isAnnoncesSubsequentes) {
        this.isAnnoncesSubsequentes = isAnnoncesSubsequentes;
    }

    /**
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }

}
