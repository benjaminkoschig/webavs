/*
 * Créé le 1 juin 05
 */
package globaz.leo.listes;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.leo.application.LEApplication;
import globaz.leo.db.envoi.LEEtapesSuivantesListViewBean;
import globaz.leo.db.envoi.LEEtapesSuivantesViewBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Liste des formules en attente
 * 
 * @author : jpa
 */
public class LEListeEnvoi extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static int COL_DATERAPPEL_WIDTH = 10;
    private static int COL_DEST_WIDTH = 30;
    private static int COL_ETAPESUIVANTE_WIDTH = 30;
    private static int COL_LIBELLE_WIDTH = 30;
    private static int COL_NUM_WIDTH = 10;
    private static int MAX_CHAR_LONG = 55;
    private static int MAX_CHAR_MOYEN = 40;
    private static int MAX_CHAR_SHORT = 16;
    private static final String NUMERO_REFERENCE_INFOROM = "0234GEN";
    private static int ROW_MAX_BY_PAGE = 40;

    /**
     * Main de test pour créer la liste des envois en attente
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            BSession session = new BSession(LEApplication.DEFAULT_APPLICATION_LEO);
            session = (BSession) GlobazServer.getCurrentSystem().getApplication(LEApplication.DEFAULT_APPLICATION_LEO)
                    .newSession("globazf", "ssiiadm");

            LEListeEnvoi liste = new LEListeEnvoi();
            liste.setEMailAddress("jpa@globaz.ch");
            liste.setSession(session);
            liste.executeProcess();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    private String categorie = new String();
    private int crtByPage = 0;
    private List csFormule = new ArrayList();
    // Params
    private String orderBy1;
    private String orderBy2;

    private List orderByProvenance = new ArrayList();

    private String untilDate = "";

    /**
     * Constructeur
     */
    public LEListeEnvoi() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "Envoi", "GLOBAZ", "Liste des étapes suivantes des envois", new LEEtapesSuivantesListViewBean(),
                "LEO");
    }

    public LEListeEnvoi(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "Envoi", "GLOBAZ", "Liste des formules en attente ", new LEEtapesSuivantesListViewBean(), "LEO");
    };

    /*
     * transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {
        LEEtapesSuivantesListViewBean manager = (LEEtapesSuivantesListViewBean) _getManager();
        manager.setSession(getSession());
        if (!JadeStringUtil.isEmpty(getOrderBy1())) {
            manager.setOrderBy1(getOrderBy1());
        }
        if (!JadeStringUtil.isEmpty(getOrderBy2())) {
            manager.setOrderBy2(getOrderBy2());
        }
        if (!JadeStringUtil.isEmpty(getUntilDate())) {
            manager.setDatePriseEnCompte(getUntilDate());
        }
        if (getCsFormule() != null) {
            manager.setForCsFormule(getCsFormule());
        }
        if (!JadeStringUtil.isEmpty(getCategorie())) {
            manager.setForCategories(getCategorie());
        }

        try {
            if (_getManager().getCount() == 0) {
                getTransaction().addErrors("Aucun document trouvé pour les critères sélectionnés");
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, "Générer formules en attente");
        }
        getDocumentInfo().setDocumentTypeNumber(LEListeEnvoi.NUMERO_REFERENCE_INFOROM);
        _setCompanyName(FWIImportProperties.getInstance().getProperty(new JadePublishDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    /*
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /*
     * Contenu des cellules
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        // ne mettre que ROW_MAX_BY_PAGE par page
        if (crtByPage > LEListeEnvoi.ROW_MAX_BY_PAGE) {
            _addDataTableBreak();
            crtByPage = 0;
        }
        crtByPage++;
        // valeurs
        LEEtapesSuivantesViewBean envoi = (LEEtapesSuivantesViewBean) entity;
        if (envoi.getLibelle().length() > LEListeEnvoi.MAX_CHAR_SHORT) {
            _addCell(envoi.getLibelle().substring(0, LEListeEnvoi.MAX_CHAR_SHORT));
        } else {
            _addCell(envoi.getLibelle());
        }
        if (envoi.getDestinataire().length() > LEListeEnvoi.MAX_CHAR_MOYEN) {
            _addCell(envoi.getDestinataire().substring(0, LEListeEnvoi.MAX_CHAR_MOYEN) + ".");
        } else {
            _addCell(envoi.getDestinataire());
        }
        String libelleAff = JadeStringUtil.change(envoi.getLibelleAffichage(), "<BR>", "");
        if (libelleAff.length() > LEListeEnvoi.MAX_CHAR_LONG) {
            _addCell(libelleAff.substring(0, LEListeEnvoi.MAX_CHAR_LONG));
        } else {
            _addCell(libelleAff);
        }
        try {
            if (getSession().getCodeLibelle(envoi.getEtapeSuivante()).length() > LEListeEnvoi.MAX_CHAR_LONG) {
                _addCell(getSession().getCodeLibelle(envoi.getEtapeSuivante()).substring(0, LEListeEnvoi.MAX_CHAR_LONG));
            } else {
                _addCell(getSession().getCodeLibelle(envoi.getEtapeSuivante()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (envoi.getDateRappel().length() > LEListeEnvoi.MAX_CHAR_SHORT) {
            _addCell(envoi.getDateRappel().substring(0, LEListeEnvoi.MAX_CHAR_SHORT));
        } else {
            _addCell(envoi.getDateRappel());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.printing.itext.dynamique.FWIAbstractDocumentList# bindReportFooter()
     */
    @Override
    protected void bindReportFooter() throws Exception {
        // TODO Raccord de méthode auto-généré
        super.bindReportFooter();
    }

    /**
     * @return
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * @return
     */
    public List getCsFormule() {
        return csFormule;
    }

    /*
     * Titre de l'email
     */
    @Override
    protected String getEMailObject() {
        // titre de l'email
        return "Liste des envois en attente";
    }

    /**
     * @return
     */
    public String getOrderBy1() {
        return orderBy1;
    }

    /**
     * @return
     */
    public String getOrderBy2() {
        return orderBy2;
    }

    /**
     * @return
     */
    public List getOrderByProvenance() {
        return orderByProvenance;
    }

    /**
     * @return
     */
    public String getUntilDate() {
        return untilDate;
    }

    /*
     * Initialisation des colonnes et des groupes
     */
    @Override
    protected void initializeTable() {
        // colonnes
        this._addColumnLeft("Numéro", LEListeEnvoi.COL_NUM_WIDTH);
        this._addColumnLeft("Destinataire", LEListeEnvoi.COL_DEST_WIDTH);
        this._addColumnLeft("Libellé", LEListeEnvoi.COL_LIBELLE_WIDTH);
        this._addColumnLeft("Etape suivante", LEListeEnvoi.COL_ETAPESUIVANTE_WIDTH);
        this._addColumnLeft("Date de Rappel", LEListeEnvoi.COL_DATERAPPEL_WIDTH);
    }

    /**
     * Set la jobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param list
     */
    public void setCategorie(String list) {
        categorie = list;
    }

    /**
     * @param list
     */
    public void setCsFormule(List list) {
        csFormule = list;
    }

    /**
     * @param string
     */
    public void setOrderBy1(String string) {
        orderBy1 = string;
    }

    /**
     * @param string
     */
    public void setOrderBy2(String string) {
        orderBy2 = string;
    }

    /**
     * @param list
     */
    public void setOrderByProvenance(List list) {
        orderByProvenance = list;
    }

    /**
     * @param string
     */
    public void setUntilDate(String string) {
        untilDate = string;
    }

}
