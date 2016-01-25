package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ch.globaz.prestation.domaine.CodePrestation;
import com.lowagie.text.DocumentException;

/**
 * @author HPE
 */
public class REListePrestationsAugmentees extends FWIAbstractDocumentList {

    private static final long serialVersionUID = 1L;

    private static double arrondir(final double value, final int n) {
        double r = (Math.round(value * Math.pow(10, n))) / (Math.pow(10, n));
        return r;
    }

    private Boolean isLstPrestAugManuellement = false;
    private Boolean isLstPrestNonAdapte = false;
    private Boolean isLstPrestProgrammeCentrale = false;
    private Boolean isLstPrestTraitementAutomatique = false;

    private Map<String, Object> mapAPIAI = new TreeMap<String, Object>();
    private Map<String, Object> mapAPIAVS = new TreeMap<String, Object>();
    private Map<String, RERentesAdapteesJointRATiers> mapAutomatique = new HashMap<String, RERentesAdapteesJointRATiers>();
    private Map<String, RERentesAdapteesJointRATiers> mapJavaCentrale = new HashMap<String, RERentesAdapteesJointRATiers>();

    private Map<String, RERentesAdapteesJointRATiers> mapManuellement = new HashMap<String, RERentesAdapteesJointRATiers>();
    private Map<String, RERentesAdapteesJointRATiers> mapNonAdapte = new HashMap<String, RERentesAdapteesJointRATiers>();
    private Map<String, Object> mapREOAI = new TreeMap<String, Object>();
    private Map<String, Object> mapREOAVS = new TreeMap<String, Object>();
    private Map<String, Object> mapROAI = new TreeMap<String, Object>();
    private Map<String, Object> mapROAVS = new TreeMap<String, Object>();

    private String moisAnnee = "";

    public REListePrestationsAugmentees() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des prestations augmentées", REApplication.DEFAULT_APPLICATION_CORVUS);
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
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.ADAPTATION_LISTE_PRESTATIONS_AUGMENTEES);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LISTE_PRST_AUG_OBJET_MAIL"));

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, toString());
            abort();
        }
    }

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
            final FWCurrency nbTotal, final FWCurrency totalEcart) throws FWIException, DocumentException {

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
        _addCell(totalEcart.toStringFormat());
        _addCell("");
        this._addDataTableRow();

    }

    public Boolean getIsLstPrestAugManuellement() {
        return isLstPrestAugManuellement;
    }

    public Boolean getIsLstPrestNonAdapte() {
        return isLstPrestNonAdapte;
    }

    public Boolean getIsLstPrestProgrammeCentrale() {
        return isLstPrestProgrammeCentrale;
    }

    public Boolean getIsLstPrestTraitementAutomatique() {
        return isLstPrestTraitementAutomatique;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapAutomatique() {
        return mapAutomatique;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapJavaCentrale() {
        return mapJavaCentrale;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapManuellement() {
        return mapManuellement;
    }

    public Map<String, RERentesAdapteesJointRATiers> getMapNonAdapte() {
        return mapNonAdapte;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    protected void initializeTable() {
        _addColumnLeft("NSS", 2);
        _addColumnLeft("Nom, Prénom", 4);
        _addColumnLeft("Genre", 1);
        _addColumnRight("Ancien montant", 2);
        _addColumnRight("Nouveau montant", 2);
        _addColumnRight("Ecart", 2);
        _addColumnRight("%", 1);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void populate() throws Exception {

        // Créer la liste des prestations augmentées par le traitement automatique
        if (getIsLstPrestTraitementAutomatique() && !mapAutomatique.isEmpty()) {

            String titre = "Liste des prestations augmentées automatiquement";
            traitement(mapAutomatique, titre);

        }

        // Créer la liste des prestations augmentées par le programme JAVA de la centrale
        if (getIsLstPrestProgrammeCentrale() && !mapJavaCentrale.isEmpty()) {

            String titre = "Liste des prestations augmentées par le programme de la centrale";
            traitement(mapJavaCentrale, titre);

        }

        // Créer la liste des prestations augmentées manuellement
        if (getIsLstPrestAugManuellement() && !mapManuellement.isEmpty()) {

            String titre = "Liste des prestations augmentées manuellement";
            traitement(mapManuellement, titre);

        }

        // Créer la liste des prestations non adaptées
        if (getIsLstPrestNonAdapte() && !mapNonAdapte.isEmpty()) {

            String titre = "Liste des prestations non augmentées";
            traitement(mapNonAdapte, titre);

        }

    }

    public void setIsLstPrestAugManuellement(final Boolean isLstPrestAugManuellement) {
        this.isLstPrestAugManuellement = isLstPrestAugManuellement;
    }

    public void setIsLstPrestNonAdapte(final Boolean isLstPrestNonAdapte) {
        this.isLstPrestNonAdapte = isLstPrestNonAdapte;
    }

    public void setIsLstPrestProgrammeCentrale(final Boolean isLstPrestProgrammeCentrale) {
        this.isLstPrestProgrammeCentrale = isLstPrestProgrammeCentrale;
    }

    public void setIsLstPrestTraitementAutomatique(final Boolean isLstPrestTraitementAutomatique) {
        this.isLstPrestTraitementAutomatique = isLstPrestTraitementAutomatique;
    }

    public void setMapAutomatique(final Map<String, RERentesAdapteesJointRATiers> mapAutomatique) {
        this.mapAutomatique = mapAutomatique;
    }

    public void setMapJavaCentrale(final Map<String, RERentesAdapteesJointRATiers> mapJavaCentrale) {
        this.mapJavaCentrale = mapJavaCentrale;
    }

    public void setMapManuellement(final Map<String, RERentesAdapteesJointRATiers> mapManuellement) {
        this.mapManuellement = mapManuellement;
    }

    public void setMapNonAdapte(final Map<String, RERentesAdapteesJointRATiers> mapNonAdapte) {
        this.mapNonAdapte = mapNonAdapte;
    }

    public void setMoisAnnee(final String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    private void traitement(final Map<String, RERentesAdapteesJointRATiers> map, final String titre) throws Exception {

        // Tri dans les map
        for (String keyIdRenteAdap : map.keySet()) {

            RERentesAdapteesJointRATiers renteAdap = map.get(keyIdRenteAdap);
            triRA(keyIdRenteAdap, renteAdap);

        }

        // Itération sur chaque map de genre de rente et remplir les pages
        FWCurrency montantTotalAvant = new FWCurrency();
        FWCurrency montantTotalApres = new FWCurrency();
        FWCurrency totalEcart = new FWCurrency();
        FWCurrency nbTotal = new FWCurrency();

        Set<String> set;
        Iterator<String> iter;

        // ROAVS
        if (!mapROAVS.isEmpty()) {
            set = mapROAVS.keySet();
            iter = set.iterator();

            this._addLine(titre, "", "");
            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_ROAVS") + " (" + mapROAVS.size() + ")", "", "");

            while (iter.hasNext()) {

                String key = iter.next();
                RERentesAdapteesJointRATiers renteAdap = map.get(key);
                traitementLigne(iter, renteAdap, montantTotalApres, montantTotalAvant, map, nbTotal, totalEcart);
            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal, totalEcart);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        totalEcart = new FWCurrency();
        nbTotal = new FWCurrency();

        // REOAVS
        if (!mapREOAVS.isEmpty()) {
            set = mapREOAVS.keySet();
            iter = set.iterator();

            this._addLine(titre, "", "");
            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_REOAVS") + " (" + mapREOAVS.size() + ")", "", "");
            while (iter.hasNext()) {

                String key = iter.next();
                RERentesAdapteesJointRATiers renteAdap = map.get(key);
                traitementLigne(iter, renteAdap, montantTotalApres, montantTotalAvant, map, nbTotal, totalEcart);
            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal, totalEcart);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        totalEcart = new FWCurrency();
        nbTotal = new FWCurrency();

        // ROAI
        if (!mapROAI.isEmpty()) {
            set = mapROAI.keySet();
            iter = set.iterator();

            this._addLine(titre, "", "");
            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_ROAI") + " (" + mapROAI.size() + ")", "", "");
            while (iter.hasNext()) {

                String key = iter.next();
                RERentesAdapteesJointRATiers renteAdap = map.get(key);
                traitementLigne(iter, renteAdap, montantTotalApres, montantTotalAvant, map, nbTotal, totalEcart);
            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal, totalEcart);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        totalEcart = new FWCurrency();
        nbTotal = new FWCurrency();

        // REOAI
        if (!mapREOAI.isEmpty()) {
            set = mapREOAI.keySet();
            iter = set.iterator();

            this._addLine(titre, "", "");
            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_REOAI") + " (" + mapREOAI.size() + ")", "", "");
            while (iter.hasNext()) {

                String key = iter.next();
                RERentesAdapteesJointRATiers renteAdap = map.get(key);
                traitementLigne(iter, renteAdap, montantTotalApres, montantTotalAvant, map, nbTotal, totalEcart);
            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal, totalEcart);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        totalEcart = new FWCurrency();
        nbTotal = new FWCurrency();

        // APIAI
        if (!mapAPIAI.isEmpty()) {
            set = mapAPIAI.keySet();
            iter = set.iterator();

            this._addLine(titre, "", "");
            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_APIAI") + " (" + mapAPIAI.size() + ")", "", "");
            while (iter.hasNext()) {

                String key = iter.next();
                RERentesAdapteesJointRATiers renteAdap = map.get(key);
                traitementLigne(iter, renteAdap, montantTotalApres, montantTotalAvant, map, nbTotal, totalEcart);
            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal, totalEcart);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        totalEcart = new FWCurrency();
        nbTotal = new FWCurrency();

        // APIAVS
        if (!mapAPIAVS.isEmpty()) {
            set = mapAPIAVS.keySet();
            iter = set.iterator();

            this._addLine(titre, "", "");
            this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_APIAVS") + " (" + mapAPIAVS.size() + ")", "", "");
            while (iter.hasNext()) {

                String key = iter.next();
                RERentesAdapteesJointRATiers renteAdap = map.get(key);
                traitementLigne(iter, renteAdap, montantTotalApres, montantTotalAvant, map, nbTotal, totalEcart);
            }

            ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbTotal, totalEcart);
            _addPageBreak();
        }

        montantTotalAvant = new FWCurrency();
        montantTotalApres = new FWCurrency();
        totalEcart = new FWCurrency();
        nbTotal = new FWCurrency();

        // Remise à zéro des map
        mapROAVS = new TreeMap<String, Object>();
        mapREOAVS = new TreeMap<String, Object>();
        mapROAI = new TreeMap<String, Object>();
        mapREOAI = new TreeMap<String, Object>();
        mapAPIAVS = new TreeMap<String, Object>();
        mapAPIAI = new TreeMap<String, Object>();

    }

    private void traitementLigne(final Iterator<String> iter, final RERentesAdapteesJointRATiers renteAdap,
            final FWCurrency montantTotalApres, final FWCurrency montantTotalAvant,
            final Map<String, RERentesAdapteesJointRATiers> mapAutomatique2, final FWCurrency nbTotal,
            final FWCurrency totalEcart) throws FWIException, DocumentException {

        _addCell(renteAdap.getNssRA());
        _addCell(renteAdap.getNomRA() + " " + renteAdap.getPrenomRA());
        _addCell(renteAdap.getCodePrestation() + "." + renteAdap.getFractionRente());

        FWCurrency nouveauMontant = new FWCurrency(renteAdap.getNouveauMontantPrestation());
        FWCurrency ancienMontant = new FWCurrency(renteAdap.getAncienMontantPrestation());

        _addCell(ancienMontant.toStringFormat());
        _addCell(nouveauMontant.toStringFormat());

        FWCurrency ecart = new FWCurrency(nouveauMontant.toStringFormat());
        ecart.sub(ancienMontant.toString());

        _addCell(ecart.toStringFormat());

        double ancien = 0;
        double nouveau = nouveauMontant.doubleValue();
        double result = 100;
        if (!ancienMontant.isZero()) {
            ancien = ancienMontant.doubleValue();
            result = REListePrestationsAugmentees.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));

        this._addDataTableRow();

        montantTotalAvant.add(renteAdap.getAncienMontantPrestation());
        montantTotalApres.add(renteAdap.getNouveauMontantPrestation());
        totalEcart.add(ecart);
        nbTotal.add(1);

    }

    private void triRA(final String key, final RERentesAdapteesJointRATiers ra) throws Exception {

        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(ra.getCodePrestation()));

        // Regrouper toutes les rentes par genre de rentes
        if (codePrestation.isVieillesse() || codePrestation.isSurvivant()) {

            if (codePrestation.isRenteOrdinaire()) {
                mapROAVS.put(key, ra);
            } else if (codePrestation.isRenteExtraordinaire()) {
                mapREOAVS.put(key, ra);
            }

        } else if (codePrestation.isAI()) {

            if (codePrestation.isRenteOrdinaire()) {
                mapROAI.put(key, ra);
            } else if (codePrestation.isRenteExtraordinaire()) {
                mapREOAI.put(key, ra);
            }

        } else if (codePrestation.isAPIAVS()) {
            mapAPIAVS.put(key, ra);
        } else if (codePrestation.isAPIAI()) {
            mapAPIAI.put(key, ra);
        }
    }
}
