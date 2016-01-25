/*
 * Créé le 3 juin 05
 */
package globaz.apg.itext;

import globaz.apg.application.APApplication;
import globaz.apg.db.stats.APStatsDroitAPGSitProManager;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APListeStatsOFAS extends FWIAbstractDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Contient le resultat d'une stat
     * 
     * @author bsc
     * 
     *         Pour changer le modèle de ce commentaire de type généré, allez à :
     *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
     */
    private class Stat {
        public String libelle = "";
        public String no = "";
        public String resultat = "";
    }

    private String forAnnee = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private List resultat = new ArrayList();

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APListePrestationsAPGControlees.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public APListeStatsOFAS() throws Exception {
        super(new BSession(APApplication.DEFAULT_APPLICATION_APG), APApplication.APPLICATION_APG_REP, "globaz",
                "Statistiques OFAS", APApplication.DEFAULT_APPLICATION_APG);
        _setDocumentTitle(getSession().getLabel("LISTE_STATS_OFAS"));
    }

    /**
     * 
     * @param columnName
     * @param width
     */
    private void _addColumnLeft(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, LEFT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    /**
     * transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_STATISTIQUES_OFAS_APG);

        Stat ligne = new Stat();

        initializeTable();

        // la stat 075
        APStatsDroitAPGSitProManager manager = new APStatsDroitAPGSitProManager();
        manager.setSession(getSession());
        manager.setForNbAllocExplTravAgr(getForAnnee());
        ligne.no = "075";
        ligne.libelle = getSession().getLabel("STAT_OFAS_075");
        try {
            ligne.resultat = String.valueOf(manager.getCount());
            resultat.add(ligne);
        } catch (Exception e) {
            _addError(getSession().getCurrentThreadTransaction(), e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, getSession().getLabel("LISTE_STATS_OFAS"));
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("LISTE_STATS_OFAS") + " " + getForAnnee());
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
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            _addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                _addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_STATS_OFAS");
    }

    /**
     * getter pour l'attribut for annee
     * 
     * @return la valeur courante de l'attribut for annee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Initialisation des colonnes et des groupes
     */
    protected void initializeTable() {
        // creation des colonnes du modele de table
        _addColumnLeft(getSession().getLabel("LISTE_STATS_OFAS_NO"), 1);
        _addColumnLeft(getSession().getLabel("LISTE_STATS_OFAS_LIBELLE"), 10);
        _addColumnLeft(getSession().getLabel("LISTE_STATS_OFAS_RESULTAT"), 1);
    }

    /**
     * Set la jobQueue
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Remplit les lignes de ce document.
     * 
     * @throws Exception
     */
    private void populate() throws Exception {

        Iterator iter = resultat.iterator();

        while (iter.hasNext()) {
            Stat currentStat = (Stat) iter.next();
            // ajouter une ligne
            _addCell(currentStat.no);
            _addCell(currentStat.libelle);
            _addCell(currentStat.resultat);
            _addDataTableRow();
        }
    }

    /**
     * setter pour l'attribut for annee
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForAnnee(String string) {
        forAnnee = string;
    }

}
