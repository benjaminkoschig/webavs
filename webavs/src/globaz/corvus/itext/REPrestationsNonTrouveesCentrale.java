package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10EmeManager;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9EmeManager;
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
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.utils.PRStringFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ch.globaz.prestation.domaine.CodePrestation;
import com.lowagie.text.DocumentException;

/**
 * <p>
 * Les prestations présentent dans cette liste correspondent aux augmentations de décembre.
 * </p>
 * <p>
 * Pour cette liste, il faut collecter toutes les annonces 51/53 qui n'ont pas de correspondant dans les rentes
 * accordées en cours.
 * </p>
 * <p>
 * Il faut trier les résultats par genre de rente.
 * </p>
 * <p>
 * Ensuite, faire un décompte des éléments trouvés avec les annonces d'augmentations du mois en cours et faire des
 * sous-totaux pour informer l'utilisateur.
 * </p>
 * <p>
 * Ainsi, les erreurs sont présentées et expliquées.
 * </p>
 * <p>
 * La liste imprimée contient les éléments suivants : - NSS - Nom, Prénom - Code prestation - Ancien montant - Nouveau
 * montant - Ecart - Pourcentage - Type d'erreur : Rente à la centrale inexistante
 * </p>
 * 
 * @author HPE
 */
public class REPrestationsNonTrouveesCentrale extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, REPrestAccJointInfoComptaJointTiers> mapPrestationsNonTrouveesCentrale = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();

    private String moisAnnee = "";

    public REPrestationsNonTrouveesCentrale() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des prestations non trouvées à la centrale", REApplication.DEFAULT_APPLICATION_CORVUS);
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
                    IRENoDocumentInfoRom.ADAPTATION_LISTE_PRESTATIONS_NON_TROUVEES_A_LA_CENTRALE);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LISTE_ERR_3_OBJET_MAIL"));

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

    private void decompteAnnoncesAugmentation(final Map<String, REAnnonceDuTiers> annoncesAugmentationParIdTiers)
            throws Exception {

        if (annoncesAugmentationParIdTiers.size() > 0) {
            _addCell(" ");
            this._addDataTableRow();

            _addCell(getSession().getLabel("PROCESS_LISTE_ERR_ANN_AUG_TROU"));
            this._addDataTableRow();

            int nbAugDim = 0;
            FWCurrency montantAugDim = new FWCurrency();

            for (String unIdTiers : annoncesAugmentationParIdTiers.keySet()) {

                // Une liste de montants par tiers pour le filtrage
                REAnnonceDuTiers annoncesDuTiers = annoncesAugmentationParIdTiers.get(unIdTiers);

                for (REAnnoncesAbstractLevel2A uneAnnoncesDuTiers : annoncesDuTiers.getAnnonces()) {

                    _addCell(NSUtil.formatAVSUnknown(uneAnnoncesDuTiers.getNoAssAyantDroit()));

                    PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                            NSUtil.formatAVSUnknown(uneAnnoncesDuTiers.getNoAssAyantDroit()));

                    if (tier != null) {
                        _addCell(tier.getNom() + " " + tier.getPrenom());
                    } else {
                        _addCell(getSession().getLabel("PROCESS_LISTE_ERR_TIERS_INC"));
                    }

                    _addCell(uneAnnoncesDuTiers.getGenrePrestation());

                    FWCurrency ancienMontant = new FWCurrency(uneAnnoncesDuTiers.getMensualitePrestationsFrancs());
                    _addCell(ancienMontant.toStringFormat());

                    nbAugDim++;
                    montantAugDim.add(ancienMontant);

                    this._addDataTableRow();
                }
            }
            ajoutLigneTotal(montantAugDim, new FWCurrency(nbAugDim));
        }

    }

    public Map<String, REPrestAccJointInfoComptaJointTiers> getMapPrestationsNonTrouveesCentrale() {
        return mapPrestationsNonTrouveesCentrale;
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

        Map<String, REPrestAccJointInfoComptaJointTiers> mapROAVS = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();
        Map<String, REPrestAccJointInfoComptaJointTiers> mapREOAVS = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();
        Map<String, REPrestAccJointInfoComptaJointTiers> mapROAI = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();
        Map<String, REPrestAccJointInfoComptaJointTiers> mapREOAI = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();
        Map<String, REPrestAccJointInfoComptaJointTiers> mapAPIAVS = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();
        Map<String, REPrestAccJointInfoComptaJointTiers> mapAPIAI = new TreeMap<String, REPrestAccJointInfoComptaJointTiers>();

        for (String unCleDeRente : mapPrestationsNonTrouveesCentrale.keySet()) {

            REPrestAccJointInfoComptaJointTiers ra = mapPrestationsNonTrouveesCentrale.get(unCleDeRente);

            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(ra.getCodePrestation()));

            // Regrouper toutes les rentes par genre de rentes
            if (codePrestation.isVieillesse() || codePrestation.isSurvivant()) {

                if (codePrestation.isRenteOrdinaire()) {
                    mapROAVS.put(unCleDeRente, ra);
                } else if (codePrestation.isRenteExtraordinaire()) {
                    mapREOAVS.put(unCleDeRente, ra);
                }

            } else if (codePrestation.isAI()) {

                if (codePrestation.isRenteOrdinaire()) {
                    mapROAI.put(unCleDeRente, ra);
                } else if (codePrestation.isRenteExtraordinaire()) {
                    mapREOAI.put(unCleDeRente, ra);
                }

            } else if (codePrestation.isAPIAVS()) {

                mapAPIAVS.put(unCleDeRente, ra);

            } else if (codePrestation.isAPIAI()) {

                mapAPIAI.put(unCleDeRente, ra);

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

        // REAPIAVS
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_APIAVS"), mapAPIAVS);

        // REAPIAI
        remplirListePourUneCategorieDeRente(getSession().getLabel("PROCESS_LISTE_ERR_APIAI"), mapAPIAI);
    }

    /**
     * Simple classse
     * 
     * @author lga
     * 
     */
    private class REGenreRenteEtMontant {
        private String montant;
        private String genreRente;

        public REGenreRenteEtMontant(String genreRente, String montantAnnonce) {
            montant = montantAnnonce;
            this.genreRente = genreRente;
        }

        /**
         * @return the montant
         */
        public final String getMontant() {
            return montant;
        }

        /**
         * @return the genreRente
         */
        public final String getGenreRente() {
            return genreRente;
        }
    }

    private class REAnnonceDuTiers {

        List<REGenreRenteEtMontant> montantEtGenreRentes;
        List<REAnnoncesAbstractLevel2A> annonces;

        public REAnnonceDuTiers() {
            annonces = new ArrayList<REAnnoncesAbstractLevel2A>();
            montantEtGenreRentes = new ArrayList<REGenreRenteEtMontant>();
        }

        /**
         * Retourne true si le genre de rente est déjà connus pour le tiers
         * 
         * @param genreRente Le genre de rente
         * @return true si le genre de rente est déjà connus pour le tiers
         */
        public boolean hasGenreRente(String genreRente) {
            for (REGenreRenteEtMontant meg : montantEtGenreRentes) {
                if (meg.getGenreRente().equals(genreRente)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Retourne true si le genre de rente avec le montant renseigné est déjà connus pour le tiers
         * 
         * @param genreRente Le genre de rente
         * @param montant le montant mensuel de la rente
         * @return true si le genre de rente avec le montant renseigné est déjà connus pour le tiers
         */
        public boolean hasGenreRenteEtMontan(String genreRente, String montant) {
            if (hasGenreRente(genreRente)) {
                for (REGenreRenteEtMontant meg : montantEtGenreRentes) {
                    if (meg.getMontant().equals(montant)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private String formatMontantCommeAnnonce(String montantRenteAccordee) {
            String montant = null;
            if (montantRenteAccordee.contains(".")) {
                String[] values = montantRenteAccordee.split("\\.");
                montant = PRStringFormatter.indentLeft(values[0], 5, "0");
            } else {
                // On ne devrais jamais arriver ici..
                montant = PRStringFormatter.indentLeft(montantRenteAccordee, 5, "0");
            }
            return montant;
        }

        /**
         * @return the annonces
         */
        public final List<REAnnoncesAbstractLevel2A> getAnnonces() {
            return annonces;
        }

        /**
         * @param annonces the annonces to set
         */
        public final void setAnnonces(List<REAnnoncesAbstractLevel2A> annonces) {
            this.annonces = annonces;
        }

        // public void add(REAnnoncesAbstractLevel2A annonce2A) {
        // annonces.add(annonce2A);
        // }
        //
        // public void add(MontantEtGenreAnnonce montantEtGenreRente) {
        // montantEtGenreRentes.add(montantEtGenreRente);
        // }

        public void add(REAnnoncesAbstractLevel2A annonce2A, REGenreRenteEtMontant montantEtGenreRente,
                String montantRenteAccordee) {
            // Le montant de la rente accordée doit être identique
            if (montantEtGenreRente.getMontant().equals(formatMontantCommeAnnonce(montantRenteAccordee))) {
                // Si le genre de rente n'est pas encore renseigné
                if (!hasGenreRente(montantEtGenreRente.getGenreRente())) {
                    annonces.add(annonce2A);
                    montantEtGenreRentes.add(montantEtGenreRente);
                }
            }
        }
    }

    private void remplirListePourUneCategorieDeRente(String titreCategorie,
            Map<String, REPrestAccJointInfoComptaJointTiers> rentesDeLaCategorie) throws Exception {

        Map<String, REAnnonceDuTiers> annoncesAugmentationsParIdTiers = new TreeMap<String, REAnnonceDuTiers>();

        if (!rentesDeLaCategorie.isEmpty()) {

            FWCurrency mntTotal = new FWCurrency();
            FWCurrency nbTotal = new FWCurrency();

            Set<String> clesDeLaCategorie = rentesDeLaCategorie.keySet();

            this._addLine(titreCategorie, "", "");
            for (String uneCleDeLaCategorie : clesDeLaCategorie) {

                REPrestAccJointInfoComptaJointTiers ra = mapPrestationsNonTrouveesCentrale.get(uneCleDeLaCategorie);

                if (ra != null
                        && (ra.getCsEtat().equals(IREPrestationAccordee.CS_ETAT_VALIDE)
                                || ra.getCsEtat().equals(IREPrestationAccordee.CS_ETAT_PARTIEL) || ra.getCsEtat()
                                .equals(IREPrestationAccordee.CS_ETAT_DIMINUE))) {
                    traitementLigne(ra, nbTotal, mntTotal, annoncesAugmentationsParIdTiers);
                }
            }

            ajoutLigneTotal(mntTotal, nbTotal);

            decompteAnnoncesAugmentation(annoncesAugmentationsParIdTiers);
            annoncesAugmentationsParIdTiers.clear();

            _addPageBreak();
        }
    }

    private void remplirLignes(final REPrestAccJointInfoComptaJointTiers ra) throws Exception {
        _addCell(ra.getNss());
        _addCell(ra.getNom() + " " + ra.getPrenom());
        _addCell(ra.getCodePrestation() + "." + ra.getFractionRenteWithZeroWhenBlank());

        FWCurrency ancienMontant = new FWCurrency(ra.getMontantPrestation());
        _addCell(ancienMontant.toStringFormat());
        _addCell("-");
        _addCell("-");
        _addCell("-");

        _addCell(getSession().getLabel("PROCESS_LISTE_ERR_RA_INEX_CENTR"));
    }

    public void setMapPrestationsNonTrouveesCentrale(
            final Map<String, REPrestAccJointInfoComptaJointTiers> mapPrestationsNonTrouveesCentrale) {
        this.mapPrestationsNonTrouveesCentrale = mapPrestationsNonTrouveesCentrale;

    }

    public void setMoisAnnee(final String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    private void extraireAnnoncesPourAffichage(List<? extends REAnnoncesAbstractLevel2A> annoncesVenantDuManager,
            Map<String, REAnnonceDuTiers> annoncesParIdTiers, REPrestAccJointInfoComptaJointTiers renteAccordee)
            throws Exception {

        for (REAnnoncesAbstractLevel2A uneAnnonce : annoncesVenantDuManager) {

            // on ne prend en compte que les annonces d'augmentation sans date de fin (car si une date de fin est
            // présente, c'est que c'est du rétro pur, le montant de la prestation ne sera pas augmenté)
            if (JadeStringUtil.isBlankOrZero(uneAnnonce.getFinDroit())) {

                PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(),
                        NSUtil.formatAVSUnknown(uneAnnonce.getNoAssAyantDroit()));
                /*
                 * Clé qui permettra de trier les annonces par ordre alphabétique (car contenues dans une TreeMap).
                 * L'ID tiers est ajouté au cas où deux personne auraient le même nom/prénom. Si le tiers n'existe pas
                 * dans
                 * la base de donnée, l'ID de l'annonce est utilisé comme clé
                 */
                String cleMap;

                if (tier != null) {
                    cleMap = tier.getNom() + tier.getPrenom() + tier.getIdTiers();
                } else {
                    cleMap = uneAnnonce.getIdTiers();
                }

                REAnnonceDuTiers annoncesPourLeTiers = null;
                if (annoncesParIdTiers.containsKey(cleMap)) {
                    annoncesPourLeTiers = annoncesParIdTiers.get(cleMap);
                } else {
                    annoncesPourLeTiers = new REAnnonceDuTiers();
                    annoncesParIdTiers.put(cleMap, annoncesPourLeTiers);
                }

                annoncesPourLeTiers.add(uneAnnonce, new REGenreRenteEtMontant(renteAccordee.getCodePrestation(),
                        uneAnnonce.getMensualitePrestationsFrancs()), renteAccordee.getMontantPrestation());

            }
        }
    }

    private void extraireAnnoncesAugmentation9emeRevisionPourAffichage(
            REPrestAccJointInfoComptaJointTiers renteAccordee, Map<String, REAnnonceDuTiers> annoncesParIdTiers)
            throws Exception {

        JACalendar cal = new JACalendarGregorian();

        // Trouver les éventuelles diminutions correspondantes 9ème
        REAnnoncesAugmentationModification9EmeManager mgr = new REAnnoncesAugmentationModification9EmeManager();
        mgr.setSession(getSession());
        mgr.setForCodePrestation(renteAccordee.getCodePrestation());
        mgr.setForNss(NSUtil.unFormatAVS(renteAccordee.getNss()));

        // Pour le moi précédant : décembre
        mgr.setForMoisRapport(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(cal.addMonths(getMoisAnnee(), -1)));
        mgr.find(BManager.SIZE_NOLIMIT);

        if (!mgr.isEmpty()) {
            extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers, renteAccordee);
        }

        // Pour le mois courant : janvier
        mgr.setForMoisRapport(getMoisAnnee());
        mgr.find(BManager.SIZE_NOLIMIT);

        if (!mgr.isEmpty()) {
            extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers, renteAccordee);
        }
    }

    private void extraireAnnoncesAugmentation10emeRevisionPourAffichage(
            REPrestAccJointInfoComptaJointTiers renteAccordee, Map<String, REAnnonceDuTiers> annoncesParIdTiers)
            throws Exception {

        JACalendar cal = new JACalendarGregorian();

        REAnnoncesAugmentationModification10EmeManager mgr = new REAnnoncesAugmentationModification10EmeManager();
        mgr.setSession(getSession());
        mgr.setForCodePrestation(renteAccordee.getCodePrestation());
        mgr.setForNss(NSUtil.unFormatAVS(renteAccordee.getNss()));

        // Pour le moi précédant : décembre
        mgr.setForMoisRapport(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(cal.addMonths(getMoisAnnee(), -1)));
        mgr.find(BManager.SIZE_NOLIMIT);

        if (!mgr.isEmpty()) {
            extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers, renteAccordee);
        }

        // Pour le mois courant : janvier
        mgr.setForMoisRapport(getMoisAnnee());
        mgr.find(BManager.SIZE_NOLIMIT);

        if (!mgr.isEmpty()) {
            extraireAnnoncesPourAffichage(mgr.getContainerAsList(), annoncesParIdTiers, renteAccordee);
        }
    }

    private void traitementLigne(final REPrestAccJointInfoComptaJointTiers ra, final FWCurrency nbTotal,
            final FWCurrency mntTotal, final Map<String, REAnnonceDuTiers> annoncesParIdTiers) throws Exception {

        remplirLignes(ra);

        this._addDataTableRow();

        extraireAnnoncesAugmentation9emeRevisionPourAffichage(ra, annoncesParIdTiers);
        extraireAnnoncesAugmentation10emeRevisionPourAffichage(ra, annoncesParIdTiers);

        mntTotal.add(ra.getMontantPrestation());
        nbTotal.add(1);

    }

}
