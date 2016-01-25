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
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Les comparaisons se font sur : - NSS - Genre prestation - Montant avant augmentation - Fraction de rente - Code cas
 * spéciaux (1,2,3,4,5)
 * 
 * La liste imprimée contient les éléments suivants : - NSS - Nom, Prénom - Code prestation - Ancien montant - Nouveau
 * montant - Ecart - Pourcentage - Type d'erreur : Détail de la différence remarquée
 * 
 * @author HPE
 */
public class REListeDifferencesCentraleCaisse extends FWIAbstractDocumentList {

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

    public REListeDifferencesCentraleCaisse() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des différences entre annonces centrale et rentes accordées",
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
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.ADAPTATION_LISTE_DIFFERENCES);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LISTE_ERR_1_OBJET_MAIL"));

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

    private void ajoutLigneTotal(final FWCurrency montantTotalAvant, final FWCurrency montantTotalApres,
            final FWCurrency nbTotal) throws FWIException, DocumentException {

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
        FWCurrency nbTotal = new FWCurrency();

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

                traitementLigne(iter, listRaAnn, ra, montantTotalAvant, montantTotalApres, nbTotal);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        nbTotal = new FWCurrency();

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

                traitementLigne(iter, listRaAnn, ra, montantTotalAvant, montantTotalApres, nbTotal);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        nbTotal = new FWCurrency();

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

                traitementLigne(iter, listRaAnn, ra, montantTotalAvant, montantTotalApres, nbTotal);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        nbTotal = new FWCurrency();

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

                traitementLigne(iter, listRaAnn, ra, montantTotalAvant, montantTotalApres, nbTotal);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        nbTotal = new FWCurrency();

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

                traitementLigne(iter, listRaAnn, ra, montantTotalAvant, montantTotalApres, nbTotal);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        nbTotal = new FWCurrency();

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

                traitementLigne(iter, listRaAnn, ra, montantTotalAvant, montantTotalApres, nbTotal);

            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        nbTotal = new FWCurrency();

    }

    private void remplirLignesBase(final REAnnonce51Adaptation ann51, final REPrestAccJointInfoComptaJointTiers ra) {

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
            result = REListeDifferencesCentraleCaisse.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));
    }

    private void remplirLignesBase(final REAnnonce53Adaptation ann53, final REPrestAccJointInfoComptaJointTiers ra) {

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
            result = REListeDifferencesCentraleCaisse.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));

    }

    public void setMapDifferencesCentraleCaisse(final Map<String, ArrayList<Object>> mapDifferencesCentraleCaisse) {
        this.mapDifferencesCentraleCaisse = mapDifferencesCentraleCaisse;
    }

    public void setMoisAnnee(final String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    private void traitementLigne(final Iterator<String> iter, final ArrayList<Object> listRaAnn,
            final REPrestAccJointInfoComptaJointTiers ra, final FWCurrency montantTotalAvant,
            final FWCurrency montantTotalApres, final FWCurrency nbTotal) throws FWIException, DocumentException {

        boolean isPrint = false;

        if (listRaAnn.get(0) instanceof REAnnonce51Adaptation) {
            REAnnonce51Adaptation ann51 = (REAnnonce51Adaptation) listRaAnn.get(0);

            // Comparaisons sur les codes cas spéciaux :

            // Note : Les codes cas spéciaux peuvent être dans un autre ordre...

            Map<String, String> listCcsAnn = new HashMap<String, String>();
            Map<String, String> listCcsRa = new HashMap<String, String>();

            if (!JadeStringUtil.isBlankOrZero(ann51.getCodeCasSpecial1().trim())) {
                listCcsAnn.put(ann51.getCodeCasSpecial1().trim(), ann51.getCodeCasSpecial1().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann51.getCodeCasSpecial2().trim())) {
                listCcsAnn.put(ann51.getCodeCasSpecial2().trim(), ann51.getCodeCasSpecial2().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann51.getCodeCasSpecial3().trim())) {
                listCcsAnn.put(ann51.getCodeCasSpecial3().trim(), ann51.getCodeCasSpecial3().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann51.getCodeCasSpecial4().trim())) {
                listCcsAnn.put(ann51.getCodeCasSpecial4().trim(), ann51.getCodeCasSpecial4().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann51.getCodeCasSpecial5().trim())) {
                listCcsAnn.put(ann51.getCodeCasSpecial5().trim(), ann51.getCodeCasSpecial5().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux1())) {
                listCcsRa.put(ra.getCodeCasSpeciaux1(), ra.getCodeCasSpeciaux1());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux2())) {
                listCcsRa.put(ra.getCodeCasSpeciaux2(), ra.getCodeCasSpeciaux2());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux3())) {
                listCcsRa.put(ra.getCodeCasSpeciaux3(), ra.getCodeCasSpeciaux3());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux4())) {
                listCcsRa.put(ra.getCodeCasSpeciaux4(), ra.getCodeCasSpeciaux4());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux5())) {
                listCcsRa.put(ra.getCodeCasSpeciaux5(), ra.getCodeCasSpeciaux5());
            }

            if (listCcsAnn.size() != listCcsRa.size()) {
                isPrint = true;
                this.remplirLignesBase(ann51, ra);

                Set<String> set = listCcsAnn.keySet();
                Iterator<String> iterCcs = set.iterator();

                String ccsAnn = "";
                while (iterCcs.hasNext()) {
                    String ccs = iterCcs.next();

                    if (!ccs.equals("")) {
                        ccsAnn += ccs;

                        if (iterCcs.hasNext()) {
                            ccsAnn += ",";
                        }
                    }
                }

                Set<String> set1 = listCcsRa.keySet();
                Iterator<String> iterCcs1 = set1.iterator();

                String ccsRA = "";
                while (iterCcs1.hasNext()) {
                    String ccs = iterCcs1.next();

                    if (!ccs.equals("")) {
                        ccsRA += ccs;

                        if (iterCcs1.hasNext()) {
                            ccsRA += ",";
                        }
                    }
                }

                _addCell("CCS : Rente =" + ccsRA + " | Annonce = " + ccsAnn);

            }

            if (isPrint) {
                montantTotalAvant.add(ra.getMontantPrestation());
                montantTotalApres.add(ann51.getMontantPrestation());
                nbTotal.add(1);
            }

        } else {
            REAnnonce53Adaptation ann53 = (REAnnonce53Adaptation) listRaAnn.get(0);

            // Comparaisons sur les codes cas spéciaux :

            // Note : Les codes cas spéciaux peuvent être dans un autre ordre...

            Map<String, String> listCcsAnn = new HashMap<String, String>();
            Map<String, String> listCcsRa = new HashMap<String, String>();

            if (!JadeStringUtil.isBlankOrZero(ann53.getCodeCasSpecial1().trim())) {
                listCcsAnn.put(ann53.getCodeCasSpecial1().trim(), ann53.getCodeCasSpecial1().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann53.getCodeCasSpecial2().trim())) {
                listCcsAnn.put(ann53.getCodeCasSpecial2().trim(), ann53.getCodeCasSpecial2().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann53.getCodeCasSpecial3().trim())) {
                listCcsAnn.put(ann53.getCodeCasSpecial3().trim(), ann53.getCodeCasSpecial3().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann53.getCodeCasSpecial4().trim())) {
                listCcsAnn.put(ann53.getCodeCasSpecial4().trim(), ann53.getCodeCasSpecial4().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ann53.getCodeCasSpecial5().trim())) {
                listCcsAnn.put(ann53.getCodeCasSpecial5().trim(), ann53.getCodeCasSpecial5().trim());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux1())) {
                listCcsRa.put(ra.getCodeCasSpeciaux1(), ra.getCodeCasSpeciaux1());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux2())) {
                listCcsRa.put(ra.getCodeCasSpeciaux2(), ra.getCodeCasSpeciaux2());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux3())) {
                listCcsRa.put(ra.getCodeCasSpeciaux3(), ra.getCodeCasSpeciaux3());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux4())) {
                listCcsRa.put(ra.getCodeCasSpeciaux4(), ra.getCodeCasSpeciaux4());
            }

            if (!JadeStringUtil.isBlankOrZero(ra.getCodeCasSpeciaux5())) {
                listCcsRa.put(ra.getCodeCasSpeciaux5(), ra.getCodeCasSpeciaux5());
            }

            if (listCcsAnn.size() != listCcsRa.size()) {
                isPrint = true;
                this.remplirLignesBase(ann53, ra);

                Set<String> set = listCcsAnn.keySet();
                Iterator<String> iterCcs = set.iterator();

                String ccsAnn = "";
                while (iterCcs.hasNext()) {
                    String ccs = iterCcs.next();
                    if (!ccs.equals("")) {
                        ccsAnn += ccs;

                        if (iterCcs.hasNext()) {
                            ccsAnn += ",";
                        }
                    }
                }

                Set<String> set1 = listCcsRa.keySet();
                Iterator<String> iterCcs1 = set1.iterator();

                String ccsRA = "";
                while (iterCcs1.hasNext()) {
                    String ccs = iterCcs1.next();
                    if (!ccs.equals("")) {
                        ccsRA += ccs;

                        if (iterCcs1.hasNext()) {
                            ccsRA += ",";
                        }
                    }
                }

                _addCell("CCS : Rente =" + ccsRA + " | Annonce = " + ccsAnn);

            }

            if (isPrint) {
                montantTotalAvant.add(ra.getMontantPrestation());
                montantTotalApres.add(ann53.getMontantPrestation());
                nbTotal.add(1);
            }

        }

        this._addDataTableRow();

    }

}
