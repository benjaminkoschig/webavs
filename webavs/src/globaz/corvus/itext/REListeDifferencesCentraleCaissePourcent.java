package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.annonces.REAnnonce51Adaptation;
import globaz.corvus.db.annonces.REAnnonce53Adaptation;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.lowagie.text.DocumentException;

/**
 * 
 * Pour cette liste, il faut comparer toutes les rentes en cours et les annonces 51/53 qui existent et correspondent
 * pour comparer les éléments de chaque côté.
 * 
 * Les comparaisons se font sur : - Sur le pourcentage
 * 
 * La liste imprimée contient les éléments suivants : - NSS - Nom, Prénom - Code prestation - Ancien montant - Nouveau
 * montant - Ecart - Pourcentage - Type d'erreur : Détail de la différence remarquée
 * 
 * Cette liste indique tous les cas qui dépasse une marge donnée par l'utilisateur (2 chiffres : plus petit que / plus
 * grand que)
 * 
 * @author HPE
 * 
 * 
 */
public class REListeDifferencesCentraleCaissePourcent extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static double arrondir(final double value, final int n) {
        double r = (Math.round(value * Math.pow(10, n))) / (Math.pow(10, n));
        return r;
    }

    private Map<String, ArrayList<Object>> mapDifferencesCentraleCaisse = new TreeMap<String, ArrayList<Object>>();
    private String moisAnnee = "";
    private String pourcentA = "";
    private String pourcentDe = "";

    public REListeDifferencesCentraleCaissePourcent() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des différences entre annonces centrale et rentes accordées (Pourcentage)",
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
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.ADAPTATION_LISTE_DIFFERENCES_POURCENT);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LISTE_ERR_2_OBJET_MAIL"));

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

    private void ajoutLigneTotal(final FWCurrency montantTotalAvant, final FWCurrency montantTotalApres)
            throws FWIException, DocumentException {

        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("");
        this._addDataTableRow();

        _addCell("");
        _addCell("");
        _addCell("");
        _addCell(montantTotalAvant.toStringFormat());
        _addCell(montantTotalApres.toStringFormat());
        _addCell("");
        _addCell("");
        _addCell("");
        this._addDataTableRow();

    }

    public Map<String, ArrayList<Object>> getMapDifferencesCentraleCaisse() {
        return mapDifferencesCentraleCaisse;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    public String getPourcentA() {
        return pourcentA;
    }

    public String getPourcentDe() {
        return pourcentDe;
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

        Map<String, Object> mapROAVS = new TreeMap<String, Object>();
        Map<String, Object> mapREOAVS = new TreeMap<String, Object>();
        Map<String, Object> mapROAI = new TreeMap<String, Object>();
        Map<String, Object> mapREOAI = new TreeMap<String, Object>();
        Map<String, Object> mapAPIAVS = new TreeMap<String, Object>();
        Map<String, Object> mapAPIAI = new TreeMap<String, Object>();

        for (String keyMapDiffCentraleCaisse : mapDifferencesCentraleCaisse.keySet()) {

            // on retourne une arrayList avec l'annonce correspondante et la ra
            ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(keyMapDiffCentraleCaisse);

            REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

            // Regrouper toutes les rentes par genre de rentes
            if (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_14.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_15.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_16.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_33.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_34.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_35.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_36.equals(ra.getCodePrestation())) {

                mapROAVS.put(keyMapDiffCentraleCaisse, listRaAnn);

            } else if (REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_45.equals(ra.getCodePrestation())) {

                mapREOAVS.put(keyMapDiffCentraleCaisse, listRaAnn);

            } else if (REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_53.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_54.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_55.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_56.equals(ra.getCodePrestation())) {

                mapROAI.put(keyMapDiffCentraleCaisse, listRaAnn);

            } else if (REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_72.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_73.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_74.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_75.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_76.equals(ra.getCodePrestation())) {

                mapREOAI.put(keyMapDiffCentraleCaisse, listRaAnn);

            } else if (REGenresPrestations.GENRE_85.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_86.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_87.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_89.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_94.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_95.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_96.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_97.equals(ra.getCodePrestation())) {

                mapAPIAVS.put(keyMapDiffCentraleCaisse, listRaAnn);

            } else if (REGenresPrestations.GENRE_81.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_82.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_83.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_84.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_88.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_91.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_92.equals(ra.getCodePrestation())
                    || REGenresPrestations.GENRE_93.equals(ra.getCodePrestation())) {

                mapAPIAI.put(keyMapDiffCentraleCaisse, listRaAnn);

            }

        }

        // Itération sur chaque map de genre de rente et remplir les pages
        FWCurrency montantTotalAvant = new FWCurrency();
        FWCurrency montantTotalApres = new FWCurrency();

        Set<String> set;
        Iterator<String> iter;

        // ROAVS
        if (!mapROAVS.isEmpty()) {
            set = mapROAVS.keySet();
            iter = set.iterator();

            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_ROAVS"), "", "");
            while (iter.hasNext()) {

                String keyMapDiffCentraleCaisse = iter.next();

                // on retourne une arrayList avec l'annonce correspondante et la
                // ra
                ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(keyMapDiffCentraleCaisse);

                // Faire les tests de différences et imprimer la ligne
                REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

                traitementLigne(iter, listRaAnn, ra, montantTotalApres, montantTotalAvant);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();

        // REOAVS
        if (!mapREOAVS.isEmpty()) {
            set = mapREOAVS.keySet();
            iter = set.iterator();

            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_REOAVS"), "", "");
            while (iter.hasNext()) {

                String keyMapDiffCentraleCaisse = iter.next();

                // on retourne une arrayList avec l'annonce correspondante et la
                // ra
                ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(keyMapDiffCentraleCaisse);

                // Faire les tests de différences et imprimer la ligne
                REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

                traitementLigne(iter, listRaAnn, ra, montantTotalApres, montantTotalAvant);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();

        // ROAI
        if (!mapROAI.isEmpty()) {
            set = mapROAI.keySet();
            iter = set.iterator();

            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_ROAI"), "", "");
            while (iter.hasNext()) {

                String keyMapDiffCentraleCaisse = iter.next();

                // on retourne une arrayList avec l'annonce correspondante et la
                // ra
                ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(keyMapDiffCentraleCaisse);

                // Faire les tests de différences et imprimer la ligne
                REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

                traitementLigne(iter, listRaAnn, ra, montantTotalApres, montantTotalAvant);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();

        // REOAI
        if (!mapREOAI.isEmpty()) {
            set = mapREOAI.keySet();
            iter = set.iterator();

            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_REOAI"), "", "");
            while (iter.hasNext()) {

                String keyMapDiffCentraleCaisse = iter.next();

                // on retourne une arrayList avec l'annonce correspondante et la
                // ra
                ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(keyMapDiffCentraleCaisse);

                // Faire les tests de différences et imprimer la ligne
                REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

                traitementLigne(iter, listRaAnn, ra, montantTotalApres, montantTotalAvant);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres);
            _addPageBreak();
        }
        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();

        // APIAI
        if (!mapAPIAI.isEmpty()) {
            set = mapAPIAI.keySet();
            iter = set.iterator();

            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_APIAI"), "", "");
            while (iter.hasNext()) {

                String keyMapDiffCentraleCaisse = iter.next();

                // on retourne une arrayList avec l'annonce correspondante et la
                // ra
                ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(keyMapDiffCentraleCaisse);

                // Faire les tests de différences et imprimer la ligne
                REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

                traitementLigne(iter, listRaAnn, ra, montantTotalApres, montantTotalAvant);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();

        // APIAVS
        if (!mapAPIAVS.isEmpty()) {
            set = mapAPIAVS.keySet();
            iter = set.iterator();

            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_APIAVS"), "", "");
            while (iter.hasNext()) {

                String keyMapDiffCentraleCaisse = iter.next();

                // on retourne une arrayList avec l'annonce correspondante et la
                // ra
                ArrayList<Object> listRaAnn = mapDifferencesCentraleCaisse.get(keyMapDiffCentraleCaisse);

                // Faire les tests de différences et imprimer la ligne
                REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

                traitementLigne(iter, listRaAnn, ra, montantTotalApres, montantTotalAvant);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();

    }

    private void remplirLignes(final REAnnonce51Adaptation ann51, final REPrestAccJointInfoComptaJointTiers ra,
            final String ccs) {

        _addCell(ra.getNss());
        _addCell(ra.getNom() + " " + ra.getPrenom());
        _addCell(ra.getCodePrestation() + "." + ra.getFractionRenteWithZeroWhenBlank());

        FWCurrency ancienMontant = new FWCurrency(ra.getMontantPrestation());
        _addCell(ancienMontant.toStringFormat());

        FWCurrency nouveauMontant = new FWCurrency(ann51.getMontantPrestation());
        _addCell(nouveauMontant.toString());

        FWCurrency ecart = new FWCurrency(nouveauMontant.toString());
        ecart.sub(ancienMontant.toString());

        _addCell(ecart.toStringFormat());

        double ancien = 0;
        double nouveau = nouveauMontant.doubleValue();
        double result = 100;
        if (!ancienMontant.isZero()) {
            ancien = ancienMontant.doubleValue();
            result = REListeDifferencesCentraleCaissePourcent.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));

        String ccsRA = "";
        String ccsAnn = "";
        if (ccs.equals("1")) {
            ccsAnn = ann51.getCodeCasSpecial1();
            ccsRA = ra.getCodeCasSpeciaux1();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("2")) {
            ccsAnn = ann51.getCodeCasSpecial2();
            ccsRA = ra.getCodeCasSpeciaux2();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("3")) {
            ccsAnn = ann51.getCodeCasSpecial3();
            ccsRA = ra.getCodeCasSpeciaux3();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("4")) {
            ccsAnn = ann51.getCodeCasSpecial4();
            ccsRA = ra.getCodeCasSpeciaux4();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("5")) {
            ccsAnn = ann51.getCodeCasSpecial5();
            ccsRA = ra.getCodeCasSpeciaux5();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else {
            _addCell("Pourcentage écart entre " + getPourcentDe() + " et " + getPourcentA());
        }
    }

    private void remplirLignes(final REAnnonce53Adaptation ann53, final REPrestAccJointInfoComptaJointTiers ra,
            final String ccs) {

        _addCell(ra.getNss());
        _addCell(ra.getNom() + " " + ra.getPrenom());
        _addCell(ra.getCodePrestation() + "." + ra.getFractionRenteWithZeroWhenBlank());

        FWCurrency ancienMontant = new FWCurrency(ra.getMontantPrestation());
        _addCell(ancienMontant.toStringFormat());

        FWCurrency nouveauMontant = new FWCurrency(ann53.getMontantPrestation());
        _addCell(nouveauMontant.toStringFormat());

        FWCurrency ecart = new FWCurrency(nouveauMontant.toString());
        ecart.sub(ancienMontant.toString());

        _addCell(ecart.toStringFormat());

        double ancien = 0;
        double nouveau = nouveauMontant.doubleValue();
        double result = 100;
        if (!ancienMontant.isZero()) {
            ancien = ancienMontant.doubleValue();
            result = REListeDifferencesCentraleCaissePourcent.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));

        String ccsRA = "";
        String ccsAnn = "";
        if (ccs.equals("1")) {
            ccsAnn = ann53.getCodeCasSpecial1();
            ccsRA = ra.getCodeCasSpeciaux1();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("2")) {
            ccsAnn = ann53.getCodeCasSpecial2();
            ccsRA = ra.getCodeCasSpeciaux2();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("3")) {
            ccsAnn = ann53.getCodeCasSpecial3();
            ccsRA = ra.getCodeCasSpeciaux3();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("4")) {
            ccsAnn = ann53.getCodeCasSpecial4();
            ccsRA = ra.getCodeCasSpeciaux4();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else if (ccs.equals("5")) {
            ccsAnn = ann53.getCodeCasSpecial5();
            ccsRA = ra.getCodeCasSpeciaux5();
            _addCell("CCS " + ccs + " : Rente =" + ccsRA + " | Annonce = " + ccsAnn);
        } else {
            _addCell("Pourcentage écart entre " + getPourcentDe() + " et " + getPourcentA());
        }
    }

    public void setMapDifferencesCentraleCaisse(final Map<String, ArrayList<Object>> mapDifferencesCentraleCaisse) {
        this.mapDifferencesCentraleCaisse = mapDifferencesCentraleCaisse;
    }

    public void setMoisAnnee(final String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    public void setPourcentA(final String pourcentA) {
        this.pourcentA = pourcentA;
    }

    public void setPourcentDe(final String pourcentDe) {
        this.pourcentDe = pourcentDe;
    }

    private void traitementLigne(final Iterator<String> iter, final ArrayList<Object> listRaAnn,
            final REPrestAccJointInfoComptaJointTiers ra, final FWCurrency montantTotalApres,
            final FWCurrency montantTotalAvant) throws FWIException, DocumentException {

        if (listRaAnn.get(0) instanceof REAnnonce51Adaptation) {
            REAnnonce51Adaptation ann51 = (REAnnonce51Adaptation) listRaAnn.get(0);

            // Comparaisons sur les pourcentages :
            FWCurrency ancienMontant = new FWCurrency(ra.getMontantPrestation());
            FWCurrency nouveauMontant = new FWCurrency(ann51.getMontantPrestation());

            double ancien = 0;
            double nouveau = nouveauMontant.doubleValue();
            double result = 100;
            if (!ancienMontant.isZero()) {
                ancien = ancienMontant.doubleValue();
                result = REListeDifferencesCentraleCaissePourcent.arrondir(((nouveau * 100) / ancien) - 100, 2);
                if (result < 0) {
                    FWCurrency res = new FWCurrency(result);
                    res.negate();
                    result = res.doubleValue();
                }
            }

            double pourcentDe = Double.valueOf(getPourcentDe());
            double pourcentA = Double.valueOf(getPourcentA());

            if ((result >= pourcentDe) && (result <= pourcentA)) {
                this.remplirLignes(ann51, ra, "0");
                montantTotalAvant.add(ra.getMontantPrestation());
                montantTotalApres.add(ann51.getMontantPrestation());
            }

        } else {
            REAnnonce53Adaptation ann53 = (REAnnonce53Adaptation) listRaAnn.get(0);

            // Comparaisons sur les pourcentages :
            FWCurrency ancienMontant = new FWCurrency(ra.getMontantPrestation());
            FWCurrency nouveauMontant = new FWCurrency(ann53.getMontantPrestation());

            double ancien = 0;
            double nouveau = nouveauMontant.doubleValue();
            double result = 100;
            if (!ancienMontant.isZero()) {
                ancien = ancienMontant.doubleValue();
                result = REListeDifferencesCentraleCaissePourcent.arrondir(((nouveau * 100) / ancien) - 100, 2);
                if (result < 0) {
                    FWCurrency res = new FWCurrency(result);
                    res.negate();
                    result = res.doubleValue();
                }
            }

            double pourcentDe = Double.valueOf(getPourcentDe());
            double pourcentA = Double.valueOf(getPourcentA());

            if ((result >= pourcentDe) && (result <= pourcentA)) {
                this.remplirLignes(ann53, ra, "0");
                montantTotalAvant.add(ra.getMontantPrestation());
                montantTotalApres.add(ann53.getMontantPrestation());
            }

        }

        this._addDataTableRow();

    }

}
