package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.IREAnnonceAdaptation;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2B;
import globaz.corvus.db.annonces.REAnnoncesDiminution10EmeManager;
import globaz.corvus.db.annonces.REAnnoncesDiminution9EmeManager;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.prestation.domaine.CodePrestation;
import com.lowagie.text.DocumentException;

/**
 * <p>
 * Les prestations présentent dans la liste correspondent aux diminutions dont le mois de rapport est décembre ou
 * janvier.
 * </p>
 * <p>
 * Pour cette liste, il faut collecter toutes les rentes dans les annonces 51/53 qui ne sont pas dans les rentes en
 * cours.
 * </p>
 * <p>
 * Il faut trier les résultats par genre de rente.
 * </p>
 * <p>
 * Ensuite, faire un décompte des éléments trouvés avec les annonces de diminutions du mois en cours et mois en cours +1
 * et faire des sous-totaux pour informer l'utilisateur.
 * </p>
 * <p>
 * Ainsi, les erreurs sont présentées et expliquées.
 * </p>
 * <p>
 * La liste imprimée contient les éléments suivants : - NSS - Nom, Prénom - Code prestation - Ancien montant - Nouveau
 * montant - Ecart - Pourcentage - Type d'erreur : Rente accordée inexistante à la caisse
 * </p>
 * 
 * @author HPE
 */
public class REPrestationsNonTrouveesFichierCaisse extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static double arrondir(final double value, final int n) {
        double r = (Math.round(value * Math.pow(10, n))) / (Math.pow(10, n));
        return r;
    }

    private Map<String, IREAnnonceAdaptation> mapPrestationsNonTrouveesFichierCaisse = new TreeMap<String, IREAnnonceAdaptation>();

    private String moisAnnee = "";

    public REPrestationsNonTrouveesFichierCaisse() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des prestations non trouvées dans le fichier de la caisse",
                REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    private void _addColumnLeft(final String columnName, final int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.LEFT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    private void _addColumnRight(final String columnName, final int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.RIGHT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    @Override
    public void _beforeExecuteReport() {

        try {
            // on ajoute au doc info le numéro de référence inforom
            getDocumentInfo().setDocumentTypeNumber(
                    IRENoDocumentInfoRom.ADAPTATION_LISTE_PRESTATIONS_NON_TROUVEES_A_LA_CAISSE);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LISTE_ERR_4_OBJET_MAIL"));

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, toString());
            abort();
        }
    }

    /**
     * Crée les lignes du document.
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    protected final void _bindDataTable() throws FWIException {
        try {
            _setDataTableModel();
            populate();
        } catch (Exception e) {
            if (e instanceof FWIException) {
                throw (FWIException) e;
            } else {
                throw new FWIException(e);
            }
        }
    }

    private void ajoutLigneTotal(final FWCurrency mntTotal, final FWCurrency nbTotal) throws FWIException,
            DocumentException {

        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("");
        this._addDataTableRow();

        _addCell("Nombre :");
        _addCell(String.valueOf(nbTotal.intValue()));
        _addCell("");
        _addCell(mntTotal.toStringFormat());
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");
        this._addDataTableRow();

    }

    private void decompteAnnoncesDiminution(
            final Map<String, Map<String, REAnnoncesAbstractLevel2B>> mapAnnoncesDiminutionsParIdTiers)
            throws Exception {

        if (!mapAnnoncesDiminutionsParIdTiers.isEmpty()) {

            _addCell(" ");
            this._addDataTableRow();

            _addCell(getSession().getLabel("PROCESS_LISTE_ERR_ANN_DIM_TROU"));
            this._addDataTableRow();

            int nbAugDim = 0;
            FWCurrency montantAugDim = new FWCurrency();

            for (String unIdTiers : mapAnnoncesDiminutionsParIdTiers.keySet()) {
                for (REAnnoncesAbstractLevel2B uneAnnonceDiminution : mapAnnoncesDiminutionsParIdTiers.get(unIdTiers)
                        .values()) {

                    _addCell(NSUtil.formatAVSUnknown(uneAnnonceDiminution.getNoAssAyantDroit()));

                    PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                            NSUtil.formatAVSUnknown(uneAnnonceDiminution.getNoAssAyantDroit()));
                    if (tier != null) {
                        _addCell(tier.getNom() + " " + tier.getPrenom());
                    } else {
                        _addCell("");
                    }

                    _addCell(uneAnnonceDiminution.getGenrePrestation());

                    FWCurrency ancienMontant = new FWCurrency(uneAnnonceDiminution.getMensualitePrestationsFrancs());
                    _addCell(ancienMontant.toStringFormat());

                    nbAugDim++;
                    montantAugDim.add(ancienMontant);

                    this._addDataTableRow();
                }
            }

            ajoutLigneTotal(montantAugDim, new FWCurrency(nbAugDim));

        }
    }

    private boolean extraireAnnoncesPourAffichage(List<? extends REAnnoncesAbstractLevel2B> annoncesVenantDuManager,
            Map<String, Map<String, REAnnoncesAbstractLevel2B>> annoncesParIdTiers,
            IREAnnonceAdaptation annonceDeLaCentrale) throws Exception {

        for (REAnnoncesAbstractLevel2B uneAnnonceDiminutionDuManager : annoncesVenantDuManager) {

            PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                    NSUtil.formatAVSUnknown(uneAnnonceDiminutionDuManager.getNoAssAyantDroit()));

            /*
             * Clé qui permettra de trier les annonces par ordre alphabétique (car contenues dans une TreeMap).
             * L'ID tiers est ajouté au cas où deux personne auraient le même nom/prénom. Si le tiers n'existe pas dans
             * la base de donnée, l'ID de l'annonce est utilisé comme clé
             */
            String cleMap;

            if (tier != null) {
                cleMap = tier.getNom() + tier.getPrenom() + tier.getIdTiers();
            } else {
                cleMap = uneAnnonceDiminutionDuManager.getIdTiers();
            }

            Map<String, REAnnoncesAbstractLevel2B> annoncesDuTiers = null;
            if (annoncesParIdTiers.containsKey(cleMap)) {
                annoncesDuTiers = annoncesParIdTiers.get(cleMap);
            } else {
                annoncesDuTiers = new HashMap<String, REAnnoncesAbstractLevel2B>();
                annoncesParIdTiers.put(cleMap, annoncesDuTiers);
            }

            String montantAnnonceDiminution = uneAnnonceDiminutionDuManager.getMensualitePrestationsFrancs();
            String montantAnnonceCentrale = annonceDeLaCentrale.getAncienMontantMensuel();
            if (montantAnnonceDiminution.equals(montantAnnonceCentrale)) {
                if (!annoncesDuTiers.containsKey(uneAnnonceDiminutionDuManager.getIdAnnonce())) {
                    annoncesDuTiers.put(uneAnnonceDiminutionDuManager.getIdAnnonce(), uneAnnonceDiminutionDuManager);
                    return true; // on ne veut qu'une annonce de diminution pour une annonce de la centrale !
                }
            }

        }
        return false;
    }

    private void extraireAnnoncesDiminution9emeRevisionPourAffichage(IREAnnonceAdaptation annonceDeLaCentrale,
            Map<String, Map<String, REAnnoncesAbstractLevel2B>> annoncesParIdTiers) throws Exception {

        JACalendar cal = new JACalendarGregorian();

        REAnnoncesDiminution9EmeManager mgr = new REAnnoncesDiminution9EmeManager();
        mgr.setSession(getSession());
        mgr.setForCodePrestation(annonceDeLaCentrale.getGenrePrestation());
        mgr.setForNss(NSUtil.unFormatAVS(annonceDeLaCentrale.getNss()));
        boolean annonceTrouvee = false;

        // Pour le moi précédant : décembre
        mgr.setForMoisRapport(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(cal.addMonths(getMoisAnnee(), -1)));
        mgr.find(BManager.SIZE_NOLIMIT);
        if (!mgr.isEmpty()) {
            annonceTrouvee = extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers,
                    annonceDeLaCentrale);
        }

        if (!annonceTrouvee) {
            // Pour le mois courant : janvier
            mgr.setForMoisRapport(getMoisAnnee());
            mgr.find(BManager.SIZE_NOLIMIT);

            if (!mgr.isEmpty()) {
                extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers, annonceDeLaCentrale);
            }
        }
    }

    private void extraireAnnoncesDiminution10emeRevisionPourAffichage(IREAnnonceAdaptation annonceDeLaCentrale,
            Map<String, Map<String, REAnnoncesAbstractLevel2B>> annoncesParIdTiers) throws Exception {

        JACalendar cal = new JACalendarGregorian();

        REAnnoncesDiminution10EmeManager mgr = new REAnnoncesDiminution10EmeManager();
        mgr.setSession(getSession());
        mgr.setForCodePrestation(annonceDeLaCentrale.getGenrePrestation());
        mgr.setForNss(NSUtil.unFormatAVS(annonceDeLaCentrale.getNss()));
        boolean annonceTrouvee = false;

        // Pour le moi précédant : décembre
        mgr.setForMoisRapport(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(cal.addMonths(getMoisAnnee(), -1)));
        mgr.find(BManager.SIZE_NOLIMIT);
        if (!mgr.isEmpty()) {
            annonceTrouvee = extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers,
                    annonceDeLaCentrale);
        }

        if (!annonceTrouvee) {
            // Pour le mois courant : janvier
            mgr.setForMoisRapport(getMoisAnnee());
            mgr.find(BManager.SIZE_NOLIMIT);
            if (!mgr.isEmpty()) {
                extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers, annonceDeLaCentrale);
            }
        }
    }

    public Map<String, IREAnnonceAdaptation> getMapPrestationsNonTrouveesFichierCaisse() {
        return mapPrestationsNonTrouveesFichierCaisse;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    protected void initializeTable() {
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_1"), 2);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_2"), 4);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_3"), 1);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_4"), 2);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_5"), 2);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_6"), 2);
        _addColumnRight("%", 1);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_7"), 4);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void populate() throws Exception {

        Map<String, IREAnnonceAdaptation> mapROAVS = new TreeMap<String, IREAnnonceAdaptation>();
        Map<String, IREAnnonceAdaptation> mapREOAVS = new TreeMap<String, IREAnnonceAdaptation>();
        Map<String, IREAnnonceAdaptation> mapROAI = new TreeMap<String, IREAnnonceAdaptation>();
        Map<String, IREAnnonceAdaptation> mapREOAI = new TreeMap<String, IREAnnonceAdaptation>();
        Map<String, IREAnnonceAdaptation> mapAPIAVS = new TreeMap<String, IREAnnonceAdaptation>();
        Map<String, IREAnnonceAdaptation> mapAPIAI = new TreeMap<String, IREAnnonceAdaptation>();

        for (String unCleDeRente : mapPrestationsNonTrouveesFichierCaisse.keySet()) {

            IREAnnonceAdaptation uneAnnonce = mapPrestationsNonTrouveesFichierCaisse.get(unCleDeRente);

            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(uneAnnonce
                    .getGenrePrestation()));

            // Regrouper toutes les rentes par genre de rentes
            if (codePrestation.isVieillesse() || codePrestation.isSurvivant()) {

                if (codePrestation.isRenteOrdinaire()) {
                    mapROAVS.put(unCleDeRente, uneAnnonce);
                } else if (codePrestation.isRenteExtraordinaire()) {
                    mapREOAVS.put(unCleDeRente, uneAnnonce);
                }

            } else if (codePrestation.isAI()) {

                if (codePrestation.isRenteOrdinaire()) {
                    mapROAI.put(unCleDeRente, uneAnnonce);
                } else if (codePrestation.isRenteExtraordinaire()) {
                    mapREOAI.put(unCleDeRente, uneAnnonce);
                }

            } else if (codePrestation.isAPIAVS()) {

                mapAPIAVS.put(unCleDeRente, uneAnnonce);

            } else if (codePrestation.isAPIAI()) {

                mapAPIAI.put(unCleDeRente, uneAnnonce);

            }

        }

        // ROAVS
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_ROAVS"), mapROAVS);

        // REOAVS
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_REOAVS"), mapREOAVS);

        // ROAI
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_ROAI"), mapROAI);

        // REOAI
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_REOAI"), mapREOAI);

        // APIAVS
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_APIAVS"), mapAPIAVS);

        // APIAI
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_APIAI"), mapAPIAI);
    }

    private void remplirListePourUneCategorieDeRente(String titreCategorie,
            Map<String, IREAnnonceAdaptation> annoncesDeLaCategorie) throws Exception {

        if (!annoncesDeLaCategorie.isEmpty()) {

            Map<String, Map<String, REAnnoncesAbstractLevel2B>> mapAnnoncesDiminutionsParIdTiers = new TreeMap<String, Map<String, REAnnoncesAbstractLevel2B>>();

            FWCurrency mntTotal = new FWCurrency();
            FWCurrency nbTotal = new FWCurrency();

            this._addLine(titreCategorie, "", "");

            for (String uneCleAnnonceDeLaCategorie : annoncesDeLaCategorie.keySet()) {

                IREAnnonceAdaptation uneAnnonceDeLaCentrale = mapPrestationsNonTrouveesFichierCaisse
                        .get(uneCleAnnonceDeLaCategorie);

                remplirLignes(uneAnnonceDeLaCentrale);
                mntTotal.add(uneAnnonceDeLaCentrale.getAncienMontantMensuel());
                nbTotal.add(1);

                extraireAnnoncesDiminution9emeRevisionPourAffichage(uneAnnonceDeLaCentrale,
                        mapAnnoncesDiminutionsParIdTiers);
                extraireAnnoncesDiminution10emeRevisionPourAffichage(uneAnnonceDeLaCentrale,
                        mapAnnoncesDiminutionsParIdTiers);

                this._addDataTableRow();
            }

            ajoutLigneTotal(mntTotal, nbTotal);
            decompteAnnoncesDiminution(mapAnnoncesDiminutionsParIdTiers);
            mapAnnoncesDiminutionsParIdTiers.clear();
            _addPageBreak();
        }
    }

    private void remplirLignes(final IREAnnonceAdaptation uneAnnonce) throws Exception {
        _addCell(NSUtil.formatAVSUnknown(uneAnnonce.getNss()));

        PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(), NSUtil.formatAVSUnknown(uneAnnonce.getNss()));
        if (tier != null) {
            _addCell(tier.getNom() + " " + tier.getPrenom());
        } else {
            _addCell("");
        }

        _addCell(uneAnnonce.getGenrePrestation());

        FWCurrency ancienMontant = new FWCurrency(uneAnnonce.getAncienMontantMensuel());
        _addCell(ancienMontant.toStringFormat());

        FWCurrency nouveauMontant = new FWCurrency(uneAnnonce.getMontantPrestation());
        _addCell(nouveauMontant.toStringFormat());

        FWCurrency ecart = new FWCurrency(uneAnnonce.getMontantPrestation());
        ecart.sub(uneAnnonce.getAncienMontantMensuel());

        _addCell(ecart.toStringFormat());

        double ancien = 0;
        double nouveau = nouveauMontant.doubleValue();
        double result = 0;

        if (!ancienMontant.isZero() || !nouveauMontant.isZero()) {
            ancien = ancienMontant.doubleValue();
            result = REPrestationsNonTrouveesFichierCaisse.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));

        _addCell(getSession().getLabel("PROCESS_LISTE_ERR_RA_INEX_CAISSE"));
    }

    public void setMapPrestationsNonTrouveesFichierCaisse(
            final Map<String, IREAnnonceAdaptation> mapPrestationsNonTrouveesFichierCaisse) {
        this.mapPrestationsNonTrouveesFichierCaisse = mapPrestationsNonTrouveesFichierCaisse;
    }

    public void setMoisAnnee(final String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
