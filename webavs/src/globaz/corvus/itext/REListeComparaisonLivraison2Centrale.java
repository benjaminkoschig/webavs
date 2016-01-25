package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.RERentesAdapteesJointRATiers;
import globaz.corvus.db.annonces.REAnnonce51;
import globaz.corvus.db.annonces.REAnnonce53;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.lowagie.text.DocumentException;

/**
 * 
 * <p>
 * Liste après comparaison de la deuxième livraison de la centrale des annonces 51/53 et les rentes adaptées
 * manuellement ou par le programme JAVA de la centrale. </p<
 * <p>
 * ==> Pour les annonces subséquentes
 * </p>
 * <p>
 * La liste imprimée contient les éléments suivants : - NSS - Nom, Prénom - Code prestation - Ancien montant - Nouveau
 * montant - Ecart - Pourcentage
 * </p>
 * 
 * @author HPE
 */
public class REListeComparaisonLivraison2Centrale extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, List<Object>> mapCorrespondances = new TreeMap<String, List<Object>>();

    private String moisAnnee = "";

    public REListeComparaisonLivraison2Centrale() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des différences entre deuxième livraison centrale et rentes adaptées manuellement",
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
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.ADAPTATION_LISTE_ANNONCES_SUBSEQUENTES);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LST_ANN_SUB_OBJET_MAIL_LST_1"));

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, toString());
            abort();
        }
    }

    /**
     * Crée les lignes du document.
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
            final FWCurrency nbSection) throws FWIException, DocumentException {

        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("_______________________________________________");
        _addCell("");
        this._addDataTableRow();

        _addCell("Nombre :");
        _addCell(String.valueOf(nbSection.intValue()));
        _addCell("");
        _addCell(montantTotalAvant.toStringFormat());
        _addCell(montantTotalApres.toStringFormat());
        _addCell("");
        _addCell("");
        this._addDataTableRow();

    }

    public Map<String, List<Object>> getMapCorrespondances() {
        return mapCorrespondances;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    protected void initializeTable() {
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_1"), 2);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_2"), 4);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_3"), 1);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_8"), 2);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_9"), 2);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_6"), 2);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_7"), 4);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void populate() throws Exception {

        // Pour les annonces correspondantes :
        if (!mapCorrespondances.isEmpty()) {

            Map<String, Object> mapROAVS = new TreeMap<String, Object>();
            Map<String, Object> mapREOAVS = new TreeMap<String, Object>();
            Map<String, Object> mapROAI = new TreeMap<String, Object>();
            Map<String, Object> mapREOAI = new TreeMap<String, Object>();
            Map<String, Object> mapAPIAVS = new TreeMap<String, Object>();
            Map<String, Object> mapAPIAI = new TreeMap<String, Object>();

            for (String key : mapCorrespondances.keySet()) {

                // le contenu de la liste : La liste des annonces (51_01-02-03) et la RERentesAdaptees
                List<Object> list = mapCorrespondances.get(key);

                // Faire les tests de différences et imprimer la ligne
                RERentesAdapteesJointRATiers ra = (RERentesAdapteesJointRATiers) list.get(0);

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

                    mapROAVS.put(key, ra);

                } else if (REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_45.equals(ra.getCodePrestation())) {

                    mapREOAVS.put(key, ra);

                } else if (REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_53.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_54.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_55.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_56.equals(ra.getCodePrestation())) {

                    mapROAI.put(key, ra);

                } else if (REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_72.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_73.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_74.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_75.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_76.equals(ra.getCodePrestation())) {

                    mapREOAI.put(key, ra);

                } else if (REGenresPrestations.GENRE_85.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_86.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_87.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_89.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_94.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_95.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_96.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_97.equals(ra.getCodePrestation())) {

                    mapAPIAVS.put(key, ra);

                } else if (REGenresPrestations.GENRE_81.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_82.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_83.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_84.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_88.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_91.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_92.equals(ra.getCodePrestation())
                        || REGenresPrestations.GENRE_93.equals(ra.getCodePrestation())) {

                    mapAPIAI.put(key, ra);

                }
            }

            // Itération sur chaque map de genre de rente et remplir les pages
            FWCurrency montantTotalAvant = new FWCurrency();
            FWCurrency montantTotalApres = new FWCurrency();
            FWCurrency nbSection = new FWCurrency();

            // ROAVS
            Set<String> set = mapROAVS.keySet();
            Iterator<String> iter2 = set.iterator();

            if (!mapROAVS.isEmpty()) {
                this._addLine(getSession().getLabel("PROCESS_LST_COMP_CENTRALE_TITRE_PAGE"), "", "");
                this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_ROAVS"), "", "");
                while (iter2.hasNext()) {

                    // la clé : key = ra.getCodePrestation() + ra.getIdPrestationAccordee();
                    String key = iter2.next();

                    // le contenu de la liste : La liste des annonces (51_01-02-03) et la RERentesAdaptees
                    traitementLigne(iter2, mapCorrespondances.get(key), montantTotalApres, montantTotalAvant);

                    nbSection.add(1);
                }

                ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbSection);
                _addPageBreak();

                montantTotalAvant = new FWCurrency();
                montantTotalApres = new FWCurrency();
                nbSection = new FWCurrency();
            }

            // REOAVS
            set = mapREOAVS.keySet();
            iter2 = set.iterator();

            if (!mapREOAVS.isEmpty()) {
                this._addLine(getSession().getLabel("PROCESS_LST_COMP_CENTRALE_TITRE_PAGE"), "", "");
                this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_REOAVS"), "", "");
                while (iter2.hasNext()) {

                    // la clé : key = ra.getCodePrestation() + ra.getIdPrestationAccordee();
                    String key = iter2.next();

                    // le contenu de la liste : La liste des annonces (51_01-02-03) et la RERentesAdaptees
                    traitementLigne(iter2, mapCorrespondances.get(key), montantTotalApres, montantTotalAvant);

                    nbSection.add(1);
                }

                ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbSection);
                _addPageBreak();

                montantTotalAvant = new FWCurrency();
                montantTotalApres = new FWCurrency();
                nbSection = new FWCurrency();
            }

            // ROAI
            set = mapROAI.keySet();
            iter2 = set.iterator();

            if (!mapROAI.isEmpty()) {
                this._addLine(getSession().getLabel("PROCESS_LST_COMP_CENTRALE_TITRE_PAGE"), "", "");
                this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_ROAI"), "", "");
                while (iter2.hasNext()) {

                    // la clé : key = ra.getCodePrestation() + ra.getIdPrestationAccordee();
                    String key = iter2.next();

                    // le contenu de la liste : La liste des annonces (51_01-02-03) et la RERentesAdaptees
                    traitementLigne(iter2, mapCorrespondances.get(key), montantTotalApres, montantTotalAvant);

                    nbSection.add(1);
                }

                ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbSection);
                _addPageBreak();

                montantTotalAvant = new FWCurrency();
                montantTotalApres = new FWCurrency();
                nbSection = new FWCurrency();
            }

            // REOAI
            set = mapREOAI.keySet();
            iter2 = set.iterator();

            if (!mapREOAI.isEmpty()) {
                this._addLine(getSession().getLabel("PROCESS_LST_COMP_CENTRALE_TITRE_PAGE"), "", "");
                this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_REOAI"), "", "");
                while (iter2.hasNext()) {

                    // la clé : key = ra.getCodePrestation() + ra.getIdPrestationAccordee();
                    String key = iter2.next();

                    // le contenu de la liste : La liste des annonces (51_01-02-03) et la RERentesAdaptees
                    traitementLigne(iter2, mapCorrespondances.get(key), montantTotalApres, montantTotalAvant);

                    nbSection.add(1);
                }

                ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbSection);
                _addPageBreak();

                montantTotalAvant = new FWCurrency();
                montantTotalApres = new FWCurrency();
                nbSection = new FWCurrency();
            }

            // APIAI
            set = mapAPIAI.keySet();
            iter2 = set.iterator();

            if (!mapAPIAI.isEmpty()) {
                this._addLine(getSession().getLabel("PROCESS_LST_COMP_CENTRALE_TITRE_PAGE"), "", "");
                this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_APIAI"), "", "");
                while (iter2.hasNext()) {

                    // la clé : key = ra.getCodePrestation() + ra.getIdPrestationAccordee();
                    String key = iter2.next();

                    // le contenu de la liste : La liste des annonces (51_01-02-03) et la RERentesAdaptees
                    traitementLigne(iter2, mapCorrespondances.get(key), montantTotalApres, montantTotalAvant);

                    nbSection.add(1);
                }

                ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbSection);
                _addPageBreak();

                montantTotalAvant = new FWCurrency();
                montantTotalApres = new FWCurrency();
                nbSection = new FWCurrency();
            }

            // APIAVS
            set = mapAPIAVS.keySet();
            iter2 = set.iterator();

            if (!mapAPIAVS.isEmpty()) {
                this._addLine(getSession().getLabel("PROCESS_LST_COMP_CENTRALE_TITRE_PAGE"), "", "");
                this._addLine(getSession().getLabel("PROCESS_LISTE_ERR_APIAVS"), "", "");
                while (iter2.hasNext()) {

                    // la clé : key = ra.getCodePrestation() + ra.getIdPrestationAccordee();
                    String key = iter2.next();

                    // le contenu de la liste : La liste des annonces (51_01-02-03) et la RERentesAdaptees
                    traitementLigne(iter2, mapCorrespondances.get(key), montantTotalApres, montantTotalAvant);

                    nbSection.add(1);
                }

                ajoutLigneTotal(montantTotalAvant, montantTotalApres, nbSection);
                _addPageBreak();

                montantTotalAvant = new FWCurrency();
                montantTotalApres = new FWCurrency();
                nbSection = new FWCurrency();
            }

        }

    }

    public void setMapCorrespondances(final Map<String, List<Object>> mapCorrespondances) {
        this.mapCorrespondances = mapCorrespondances;
    }

    public void setMoisAnnee(final String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

    private void traitementLigne(final Iterator<String> iter, final List<Object> list,
            final FWCurrency montantTotalApres, final FWCurrency montantTotalAvant) throws FWIException,
            DocumentException {

        RERentesAdapteesJointRATiers ra = (RERentesAdapteesJointRATiers) list.get(0);

        // 1) - NSS
        _addCell(ra.getNssRA());

        // 2) - Nom, prénom
        _addCell(ra.getNomRA() + " " + ra.getPrenomRA());

        // 3) - Genre + Fraction
        _addCell(ra.getCodePrestation() + "." + ra.getFractionRente());

        // 4) - Montant Annonce
        FWCurrency montantAnnonce = new FWCurrency();

        ArrayList<Object> listAnn = (ArrayList<Object>) list.get(1);

        if (listAnn.get(0) instanceof REAnnonce51) {

            REAnnonce51 ann51_01 = (REAnnonce51) listAnn.get(0);
            montantAnnonce.add(ann51_01.getMensualitePrestationsFrancs());

        } else if (listAnn.get(0) instanceof REAnnonce53) {

            REAnnonce53 ann53_01 = (REAnnonce53) listAnn.get(0);
            montantAnnonce.add(ann53_01.getMensualitePrestationsFrancs());

        }
        _addCell(montantAnnonce.toStringFormat());

        // 5) - Montant rente
        _addCell(ra.getNouveauMontantPrestation());

        // 6) - Ecart
        FWCurrency ecart = new FWCurrency(montantAnnonce.toString());
        ecart.sub(ra.getNouveauMontantPrestation());
        _addCell(ecart.toStringFormat());

        // 7) - Différent ?
        if (!ecart.isZero()) {
            _addCell("Différence !");
        } else {
            _addCell("");
        }

        this._addDataTableRow();

        montantTotalAvant.add(montantAnnonce);
        montantTotalApres.add(ra.getNouveauMontantPrestation());

    }
}
