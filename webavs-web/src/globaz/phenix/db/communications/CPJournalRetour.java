/*
 * Créé le 10 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.interfaces.ICommunicationrRetourManager;
import globaz.phenix.translation.CodeSystem;
import globaz.pyxis.constantes.IConstantes;

/**
 * @author mmu Entité representant un journal de récéption
 */
public class CPJournalRetour extends BEntity {

    private static final long serialVersionUID = -2605490667366801848L;
    public final static String CS_ABANDONNE = "611009";
    // Canton Sedex
    public final static String CS_CANTON_SEDEX = "620001";
    public final static String CS_COMPTABILISE_PARTIEL = "611007";
    public final static String CS_COMPTABILISE_TOTAL = "611008";
    public final static String CS_GENERE_PARTIEL = "611005";
    public final static String CS_GENERE_TOTAL = "611006";
    public final static String CS_RECEPTION_PARTIEL = "611003";
    public final static String CS_RECEPTION_TOTAL = "611004";
    // EtatJournalRetour
    public final static String CS_SOURCE_INVALIDE = "611002";
    public final static String CS_TYPE_JOURNAL_ANCIEN = "621002";
    // TYPE JOURNAL
    public final static String CS_TYPE_JOURNAL_SEDEX = "621001";
    public final static String CS_VALIDE_PARTIEL = "611010";
    public final static String CS_VALIDE_TOTAL = "611011";

    private String canton = "";
    private String dateReception = "";
    private String idJournalFacturation = "";
    private String idJournalRetour = "";
    private Boolean journalEnCours = new Boolean(false);
    private String libelleJournal = "";
    private String nbCommunication = "";
    private String nbFichierReussit = "0";
    private String nbFichierTotal = "0";
    private String nomFichier = "";
    private String status = "";

    private Boolean succes = new Boolean(false);
    private String typeJournal = "";

    private String zoneRecherche = "";

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        // Si il n'y a plus de journaux Sedex à l'état ouvert, on en créé un
        CPJournalRetourListViewBean jrnRetourManager = new CPJournalRetourListViewBean();
        jrnRetourManager.setSession(getSession());
        jrnRetourManager.setForTypeJournal(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX);
        jrnRetourManager.setIsJournalEnCours(new Boolean(true));
        jrnRetourManager.find();
        if (jrnRetourManager.getSize() == 0) {
            CPJournalRetour.ouvertureNouveauJournal(getSession());
        }
        super._afterDelete(transaction);
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdJournalRetour(this._incCounter(transaction, idJournalRetour));
        majNbCommunication(transaction);
        updateStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction) Supprime toutes les communications
     * contenues dans le journal
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        ICommunicationrRetourManager comManager = null;
        comManager = determinationManager();
        comManager.setSession(getSession());
        comManager.setForIdJournalRetour(getIdJournalRetour());
        comManager.orderByIdCommunicationRetour();
        BStatement statement = comManager.cursorOpen(transaction);
        CPCommunicationFiscaleRetourViewBean communication = null;
        while (((communication = (CPCommunicationFiscaleRetourViewBean) comManager.cursorReadNext(statement)) != null)
                && (!communication.isNew())) {
            communication.delete(transaction);
        }
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Met à jour le status du journal
        majNbCommunication(transaction);
        updateStatus();
        // Si c'est le journal en cours Sedex
        fermetureJournal();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CPJOURP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idJournalRetour = statement.dbReadNumeric("IWRJOU");
        nbCommunication = statement.dbReadNumeric("IWRCOM");
        dateReception = statement.dbReadDateAMJ("IWRDAT");
        nomFichier = statement.dbReadString("IWRNFI");
        libelleJournal = statement.dbReadString("IWRLIB");
        nbFichierTotal = statement.dbReadNumeric("IWRNBF");
        nbFichierReussit = statement.dbReadNumeric("IWRNBR");
        succes = statement.dbReadBoolean("IWRSUC");
        status = statement.dbReadNumeric("IWRSTAT");
        idJournalFacturation = statement.dbReadNumeric("IWRFAP");
        canton = statement.dbReadNumeric("IWACAN");
        typeJournal = statement.dbReadString("IWTYPJ");
        journalEnCours = statement.dbReadBoolean("IWJRNC");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement) le libelle et le status sont obligatoire
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getLibelleJournal(), getSession().getLabel("CP_MSG_0012"));

        if (JadeStringUtil.isIntegerEmpty(getStatus())) {
            _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0013"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IWRJOU", this._dbWriteNumeric(statement.getTransaction(), getIdJournalRetour(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IWRJOU",
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalRetour(), "IdJournalRetour"));
        statement.writeField("IWRCOM",
                this._dbWriteNumeric(statement.getTransaction(), getNbCommunication(), "nbCommunication"));
        statement.writeField("IWRDAT",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateReception(), "dateReception"));
        statement.writeField("IWRNFI", this._dbWriteString(statement.getTransaction(), getNomFichier(), "nomFichier"));
        statement.writeField("IWRLIB",
                this._dbWriteString(statement.getTransaction(), getLibelleJournal(), "libelleJournal"));
        statement.writeField("IWRNBF",
                this._dbWriteNumeric(statement.getTransaction(), getNbFichierTotal(), "nbFichier"));
        statement.writeField("IWRNBR",
                this._dbWriteNumeric(statement.getTransaction(), getNbFichierReussit(), "nbFichierReussit"));
        statement.writeField("IWRSUC", this._dbWriteBoolean(statement.getTransaction(), getSucces(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "succes"));
        statement.writeField("IWJRNC", this._dbWriteBoolean(statement.getTransaction(), isJournalEnCours(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "journalEnCours"));
        statement.writeField("IWRSTAT", this._dbWriteNumeric(statement.getTransaction(), getStatus(), "status"));
        statement.writeField("IWRFAP",
                this._dbWriteNumeric(statement.getTransaction(), getIdJournalFacturation(), "idJournalFacturation"));
        statement.writeField("IWACAN", this._dbWriteNumeric(statement.getTransaction(), getCanton(), "canton"));
        statement
                .writeField("IWTYPJ", this._dbWriteString(statement.getTransaction(), getTypeJournal(), "typeJournal"));
    }

    /**
     * Determine si le journal a le droit de pouvoir ajouter une communication, En fonction des droits déjà existant
     * 
     * @return boolean
     */
    public boolean canAddCommunication(boolean can) {
        String status = getStatus();
        if (status.equals(CPJournalRetour.CS_RECEPTION_PARTIEL) || status.equals(CPJournalRetour.CS_RECEPTION_TOTAL)) {
            return can;
        } else {
            return false;
        }
    }

    /**
     * Determine si le journal a le droit (d'après le workflow) de supprimer une communication ouvert, receptionné-p,
     * receptionné-t, sans stataus
     * 
     * @return boolean
     */
    public boolean canDeleteCommunication(boolean can) {
        String status = getStatus();
        if (status.equals(CPJournalRetour.CS_RECEPTION_PARTIEL) || status.equals(CPJournalRetour.CS_RECEPTION_TOTAL)
                || status.equals(CPJournalRetour.CS_GENERE_PARTIEL) || status.equals(CPJournalRetour.CS_GENERE_TOTAL)
                || status.equals(CPJournalRetour.CS_ABANDONNE) || JadeStringUtil.isIntegerEmpty(status)) {
            return can;
        } else {
            return false;
        }
    }

    /**
     * Determine si le journal a le droit (d'après le workflow) de supprimer un journal ouvert, receptionné-p,
     * receptionné-t, validé-p, validé-t, sans stataus
     * 
     * @return boolean
     */
    public boolean canDeleteJournal(boolean can) {
        String status = getStatus();
        if (status.equals(CPJournalRetour.CS_COMPTABILISE_PARTIEL)
                || status.equals(CPJournalRetour.CS_COMPTABILISE_TOTAL)) {
            return false;
        } else {
            return can;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    // Compte le nombre de communciations du journal dans un status donné
    private int countCommunication(String status) throws Exception {
        CPCommunicationFiscaleRetourManager comManager = new CPCommunicationFiscaleRetourManager();
        comManager.setSession(getSession());
        comManager.setForIdJournalRetour(getIdJournalRetour());
        comManager.setForStatus(status);
        return comManager.getCount();
    }

    public int decNbCommunication() {
        int i = Integer.parseInt(getNbCommunication());
        setNbCommunication(String.valueOf(--i));
        return i;
    }

    public ICommunicationRetour determinationEntity() {
        if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourJUViewBean();
        } else if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourNEViewBean();
        } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourGEViewBean();
        } else if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourVDViewBean();
        } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourVSViewBean();
        } else if (CPJournalRetour.CS_CANTON_SEDEX.equalsIgnoreCase(getCanton())) {
            CPCommunicationFiscaleRetourSEDEXViewBean retour = new CPCommunicationFiscaleRetourSEDEXViewBean();
            retour.setWantDonneeBase(true);
            retour.setWantDonneeContribuable(true);
            return retour;
        }
        return null;
    }

    /**
     * Détermination, selon le canton, du manger à utiliser pour les process
     * 
     * @author hna
     */
    public ICommunicationrRetourManager determinationManager() {
        if (IConstantes.CS_LOCALITE_CANTON_JURA.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourJUManager();
        } else if (IConstantes.CS_LOCALITE_CANTON_NEUCHATEL.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourNEManager();
        } else if (IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourGEManager();
        } else if (IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourVDManager();
        } else if (IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourVSManager();
        } else if (CPJournalRetour.CS_CANTON_SEDEX.equalsIgnoreCase(getCanton())) {
            return new CPCommunicationFiscaleRetourSEDEXManager();
        }
        return null;
    }

    public void fermerJournal() {
        try {
            if (isJournalEnCours().booleanValue() && getTypeJournal().equals(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX)) {
                setJournalEnCours(new Boolean(false));
                this.update();
                setJournalEnCours(new Boolean(false));
                CPJournalRetour.ouvertureNouveauJournal(getSession());
            }
        } catch (Exception ex) {
            setJournalEnCours(new Boolean(true));
        }
    }

    public void fermetureJournal() {
        try {
            if (isJournalEnCours().booleanValue() && getTypeJournal().equals(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX)) {
                // Et que le status est différent de réceptionné, on ferme le
                // journal et on ouvre un autre journal
                if (!(getStatus().equals(CPJournalRetour.CS_SOURCE_INVALIDE)
                        || getStatus().equals(CPJournalRetour.CS_RECEPTION_PARTIEL) || getStatus().equals(
                        CPJournalRetour.CS_RECEPTION_TOTAL))) {
                    setJournalEnCours(new Boolean(false));
                    CPJournalRetour.ouvertureNouveauJournal(getSession());
                }
            }
        } catch (Exception ex) {
            setJournalEnCours(new Boolean(true));
        }
    }

    /**
     * @return
     */
    public String getCanton() {
        return canton;
    }

    /**
     * @return
     */
    public String getCodeCanton() throws Exception {
        return CodeSystem.getCodeUtilisateur(getSession(), canton);
    }

    /**
     * @return
     */
    public String getDateReception() {
        return dateReception;
    }

    /**
     * @return
     */
    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    /**
     * @return
     */
    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    /**
     * @return
     */
    public String getLibelleJournal() {
        return libelleJournal;
    }

    /**
     * @return
     */
    public String getNbCommunication() {
        return nbCommunication;
    }

    /**
     * @return
     */
    public String getNbFichierReussit() {
        return nbFichierReussit;
    }

    /**
     * @return
     */
    public String getNbFichierTotal() {
        return nbFichierTotal;
    }

    /**
     * @return
     */
    public String getNomFichier() {
        return nomFichier;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return
     */
    public Boolean getSucces() {
        return succes;
    }

    public String getTypeJournal() {
        return typeJournal;
    }

    public String getVisibleStatus() {
        return getSession().getCodeLibelle(getStatus());
    }

    // Recherche de la zone de traitement pour la recherche du tiers
    public String getZoneRecherche() throws Exception {
        if (JadeStringUtil.isEmpty(zoneRecherche)) {
            CPReceptionReaderManager recrepManager = new CPReceptionReaderManager();
            recrepManager.setSession(getSession());
            recrepManager.setForCanton(getCodeCanton());
            recrepManager.find();
            CPReceptionReader recrep = (CPReceptionReader) recrepManager.getFirstEntity();
            zoneRecherche = recrep.getRechercheTiers();
        }
        return zoneRecherche;
    }

    public int incNbCommunication() {
        int i = Integer.parseInt(getNbCommunication());
        setNbCommunication(String.valueOf(++i));
        return i;
    }

    public int incNbFichierReussit() {
        int i = Integer.parseInt(getNbFichierReussit());
        setNbFichierReussit(String.valueOf(++i));
        return i;
    }

    public Boolean isJournalEnCours() {
        return journalEnCours;
    }

    public int majNbCommunication(BTransaction transaction) throws Exception {
        CPCommunicationFiscaleRetourManager comManager = new CPCommunicationFiscaleRetourManager();
        comManager.setSession(getSession());
        comManager.setForIdJournalRetour(getIdJournalRetour());
        int nbCom = comManager.getCount(transaction);
        setNbCommunication(String.valueOf(nbCom));
        return nbCom;
    }

    public static CPJournalRetour ouvertureNouveauJournal(BSession session) throws Exception {
        CPJournalRetour jrn = new CPJournalRetour();
        jrn.setSession(session);
        jrn.setCanton(CPJournalRetour.CS_CANTON_SEDEX);
        jrn.setTypeJournal(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX);
        jrn.setJournalEnCours(new Boolean(true));
        jrn.setLibelleJournal(session.getLabel("LIBELLE_JOURNAL_SEDEX_AUTOMATIQUE") + " : "
                + JACalendar.todayJJsMMsAAAA());
        jrn.setStatus(CPJournalRetour.CS_RECEPTION_TOTAL);
        jrn.add();
        return jrn;
    }

    /**
     * @param string
     */
    public void setCanton(String string) {
        canton = string;
    }

    /**
     * @param string
     */
    public void setDateReception(String string) {
        dateReception = string;
    }

    /**
     * @param string
     */
    public void setIdJournalFacturation(String string) {
        idJournalFacturation = string;
    }

    /**
     * @param string
     */
    public void setIdJournalRetour(String string) {
        idJournalRetour = string;
    }

    public void setJournalEnCours(Boolean isJournalEnCours) {
        journalEnCours = isJournalEnCours;
    }

    /**
     * @param string
     */
    public void setLibelleJournal(String string) {
        libelleJournal = string;
    }

    /**
     * @param string
     */
    public void setNbCommunication(String string) {
        nbCommunication = string;
    }

    /**
     * @param string
     */
    public void setNbFichierReussit(String string) {
        nbFichierReussit = string;
    }

    /**
     * @param string
     */
    public void setNbFichierTotal(String string) {
        nbFichierTotal = string;
    }

    /**
     * @param string
     */
    public void setNomFichier(String string) {
        nomFichier = string;
    }

    /**
     * @param string
     */
    public void setStatus(String string) {
        status = string;
    }

    public void setSucces(Boolean boolean1) {
        succes = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setSucces(String bool) {
        succes = new Boolean(bool);
    }

    public void setTypeJournal(String typeJournal) {
        this.typeJournal = typeJournal;
    }

    public void setZoneRecherche(String zoneRecherche) {
        this.zoneRecherche = zoneRecherche;
    }

    /**
     * Met à jour le status du journal en fonction des communcations il contient. Cette méthode peut être appelée après
     * que des modification aient été effectuée sur le journal ou ses communication Le update() (persistant) n'est pas
     * exéctuté --Il se peut que cette méthodes soit un peu lente, du au nombre de requêtes qu'elle effectue, --peut
     * aisément être optimisée!
     */
    public void updateStatus() throws Exception {
        int nbComTotal = Integer.parseInt(getNbCommunication());
        // Si c'est un journal sedex et que le nombre max de communications est
        // atteint, il faut fermer le journal
        if (getTypeJournal().equals(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX) && isJournalEnCours().booleanValue()) {
            CPApplication app = (CPApplication) GlobazServer.getCurrentSystem().getApplication(
                    CPApplication.DEFAULT_APPLICATION_PHENIX);
            int nbreMax = app.nombreMaxCommunicationsParJournalSedex();
            if (nbComTotal >= nbreMax) {
                fermetureJournal();
                setJournalEnCours(new Boolean(false));
                CPJournalRetour.ouvertureNouveauJournal(getSession());
            }
        }
        /*
         * Calcule le nb de communications du journal dans chaque état (pourrait utiliser un group by...)
         */
        int nbComReceptionne = countCommunication(CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE);
        int nbComAbandonne = countCommunication(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
        int nbComSansAnomalie = countCommunication(CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE);
        int nbComAvertissement = countCommunication(CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT);
        int nbComAControler = countCommunication(CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER);
        int nbComComptabilise = countCommunication(CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
        int nbValide = countCommunication(CPCommunicationFiscaleRetourViewBean.CS_VALIDE);

        if (nbComComptabilise > 0) {
            if (nbComComptabilise + nbComAbandonne == nbComTotal) {
                setStatus(CPJournalRetour.CS_COMPTABILISE_TOTAL);
            } else {
                setStatus(CPJournalRetour.CS_COMPTABILISE_PARTIEL);
            }
        } else if (nbValide > 0) {
            if (nbValide + nbComAbandonne == nbComTotal) {
                setStatus(CPJournalRetour.CS_VALIDE_TOTAL);
            } else {
                setStatus(CPJournalRetour.CS_VALIDE_PARTIEL);
            }
        } else if ((nbComSansAnomalie > 0) || (nbComAControler > 0)) {
            if (nbComSansAnomalie + nbComAControler + nbComAbandonne == nbComTotal) {
                setStatus(CPJournalRetour.CS_GENERE_TOTAL);
            } else {
                setStatus(CPJournalRetour.CS_GENERE_PARTIEL);
            }
        } else if (nbComAbandonne == nbComTotal) {
            if (getTypeJournal().equals(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX) && (nbComTotal == 0)) {
                // On laisse le status à réception, cas création journal sedex
            } else {
                setStatus(CPJournalRetour.CS_ABANDONNE);
            }
        } else if (nbComReceptionne + nbComAvertissement + nbComAbandonne == nbComTotal) {
            setStatus(CPJournalRetour.CS_RECEPTION_TOTAL);
        } else {
            setStatus(CPJournalRetour.CS_RECEPTION_PARTIEL);
        }
    }

}
