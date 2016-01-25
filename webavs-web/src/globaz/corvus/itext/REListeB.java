package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.corvus.api.arc.downloader.REAnnonce50VO;
import globaz.corvus.api.arc.downloader.REAnnonces50Container;
import globaz.corvus.api.arc.downloader.REAnnonces50Container.KeyLevelNomPrenom;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Impression de la Liste B des augmentations ou diminutions d'un mois de rapport
 * 
 * @author BSC
 * 
 */
public class REListeB extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean isAugmentation = false;
    // Param�tres pour l'impression des lignes
    private Map<REAnnonces50Container.KeyLevelNomPrenom, List<REAnnonce50VO>> mapLevelNomPrenom = new HashMap<REAnnonces50Container.KeyLevelNomPrenom, List<REAnnonce50VO>>();
    private String moisRapport = "";

    public REListeB() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste A des rentes", REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    private void _addColumnLeft(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.LEFT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    /**
     * transf�re des param�tres au manager;
     */
    @Override
    public void _beforeExecuteReport() {

        try {
            // on ajoute au doc info le num�ro de r�f�rence inforom
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_B);

            // Cr�ation du tableau du document
            initializeTable();

            // set des donn�es g�n�rales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

            _setDocumentTitle(MessageFormat.format(getSession().getLabel("LISTE_BAD_TITRE"),
                    new Object[] { moisRapport }));
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("LISTE_IMS_ERROR_TITRE"));
            abort();
        }
    }

    /**
     * Cr�e les lignes du document.
     */
    @Override
    protected final void _bindDataTable() throws FWIException {
        try {
            // ajout du modele de table
            _setDataTableModel();

            // remplit les lignes
            populate();
        } catch (Exception e) {
            if (e instanceof FWIException) {
                throw (FWIException) e;
            } else {
                throw new FWIException(e);
            }
        }
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {

        setControleTransaction(true);
        setSendCompletionMail(false);
        setSendMailOnError(false);
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_OBJET_EMAIL");
    }

    public String getLibelleCategorie(String level) {

        if ("1".equals(level)) {
            return getSession().getLabel("API_AI");
        } else if ("2".equals(level)) {
            return getSession().getLabel("API_AVS");
        } else if ("3".equals(level)) {
            return getSession().getLabel("REO_AI");
        } else if ("4".equals(level)) {
            return getSession().getLabel("RO_AI");
        } else if ("5".equals(level)) {
            return getSession().getLabel("REO_AVS");
        } else if ("6".equals(level)) {
            return getSession().getLabel("RO_AVS");
        } else {
            return "";
        }
    }

    /**
     * M�thode qui retourne le libell� d'un code de traitement
     * 
     * @return le libell� d'un code de traitement
     */
    public String getLibelleCodeTraitement(String codeTraitement) {

        if ("0".equals(codeTraitement)) {
            return getSession().getLabel("LISTE_BAD_CODE_TRAITEMENT_0");
        } else if ("1".equals(codeTraitement)) {
            return getSession().getLabel("LISTE_BAD_CODE_TRAITEMENT_1");
        } else if ("2".equals(codeTraitement)) {
            return getSession().getLabel("LISTE_BAD_CODE_TRAITEMENT_2");
        } else {
            return "";
        }
    }

    /**
     * M�thode qui retourne le libell� court du sexe par rapport au csSexe qui est pass� en param�tre
     * 
     * @return le libell� court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    /**
     * M�thode qui retourne le libell� de la nationalit� par rapport au csNationalit� qui est pass� en param�tre
     * 
     * @return le libell� du pays (retourne une cha�ne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }
    }

    public String getMoisRapport() {
        return moisRapport;
    }

    /**
     * Initialisation des colonnes et des groupes
     */
    protected void initializeTable() {
        // creation des colonnes du modele de table
        _addColumnLeft(getSession().getLabel("LISTE_BAD_CATEGORIE"), 1);
        _addColumnLeft(getSession().getLabel("LISTE_BAD_DETAIL_ASSURE"), 3);
        _addColumnLeft(getSession().getLabel("LISTE_BAD_REMARQUE"), 1);
    }

    public boolean isAugmentation() {
        return isAugmentation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Remplit les lignes de ce document.
     */
    private void populate() throws Exception {

        if (isAugmentation) {
            this._addLine(getSession().getLabel("LISTE_BAD_AUGMENTATIONS"), "", "");
        } else {
            this._addLine(getSession().getLabel("LISTE_BAD_DIMINUTIONS"), "", "");
        }
        this._addLine("", "", "");

        // somme des annonces
        int nbAnnoncesTotal = 0;

        for (REAnnonces50Container.KeyLevelNomPrenom keyLevelNomPrenom : mapLevelNomPrenom.keySet()) {

            List<REAnnonce50VO> annonces = mapLevelNomPrenom.get(keyLevelNomPrenom);

            // il peut y avoir plusieurs annonces pour un level nom prenom
            for (REAnnonce50VO annonce50 : annonces) {

                // /////////////////////////////////////////////////////////////////////////////////
                // ajout de l'annonce
                // /////////////////////////////////////////////////////////////////////////////////
                _addCell(getLibelleCategorie(annonce50.getLevelRente()));

                // Recherche des information sur le tiers beneficiaire
                String detailAyantDroit = "";
                // via l'historique des no avs
                TIHistoriqueAvsManager hAvs = new TIHistoriqueAvsManager();
                hAvs.setSession(getSession());
                hAvs.setForNumAvs(NSUtil.formatAVSUnknown(annonce50.getNssAyantDroit()));
                hAvs.find();
                PRTiersWrapper ayantDroit1 = null;
                if (hAvs.getFirstEntity() != null) {
                    ayantDroit1 = PRTiersHelper.getTiersParId(getSession(),
                            ((TIHistoriqueAvs) hAvs.getFirstEntity()).getIdTiers());

                    detailAyantDroit = ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                            + ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                            + ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                            + getLibelleCourtSexe(ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / "
                            + getLibellePays(ayantDroit1.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));
                } else {
                    detailAyantDroit = getSession().getLabel("LISTE_IMS_TIERS_NON_DEFINI")
                            + annonce50.getNssAyantDroit();
                }
                _addCell(detailAyantDroit);

                _addCell(getLibelleCodeTraitement(annonce50.getCodeTraitement()));

                // mise a jour des compteurs
                nbAnnoncesTotal++;

                this._addDataTableRow();
            }
        }

        // Affichage du recapitulatif
        _addCell(getSession().getLabel("LISTE_BAD_TOTAL"));
        _addCell(Integer.toString(nbAnnoncesTotal));
        _addCell("");
        this._addDataTableGroupRow();

    }

    public void setAugmentation(boolean isAugmentation) {
        this.isAugmentation = isAugmentation;
    }

    public void setMapLevelNomPrenom(Map<KeyLevelNomPrenom, List<REAnnonce50VO>> mapLevelNomPrenom) {
        this.mapLevelNomPrenom = mapLevelNomPrenom;
    }

    public void setMoisRapport(String moisRapport) {
        this.moisRapport = moisRapport;
    }
}
