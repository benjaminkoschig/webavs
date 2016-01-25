/*
 * Créé le 1 mars 07
 */
package globaz.naos.itext.controleEmployeur;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.controleEmployeur.AFControlesAttribues;
import globaz.naos.db.controleEmployeur.AFControlesAttribuesManager;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * @author hpe
 * 
 */

public class AFListeControlesAttribuesIText extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String NUM_REF_INFOROM_LISTE_CONTROLE_ATTRIBUE = "0209CAF";
    private String annee = new String();
    private String annee_1 = new String();
    private int compt = 0;
    private String genreControle = new String();

    private String selectionGroupe = new String();

    private String visaReviseur = new String();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APListePrestationsAPGControlees.
     */
    public AFListeControlesAttribuesIText() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "", "", "Liste des contrôles attribués", new AFControlesAttribuesManager(),
                AFApplication.DEFAULT_APPLICATION_NAOS);
    }

    /**
     * Crée une nouvelle instance de la classe AFListeControlesAttribuesIText.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public AFListeControlesAttribuesIText(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "", "", session.getLabel("NAOS_FICHIER_CONTROLES_ATTRIBUES"), new AFControlesAttribuesManager(),
                AFApplication.DEFAULT_APPLICATION_NAOS);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {

        getDocumentInfo().setDocumentTypeNumber(AFListeControlesAttribuesIText.NUM_REF_INFOROM_LISTE_CONTROLE_ATTRIBUE);
        try {

            // Création du manager
            AFControlesAttribuesManager manager = (AFControlesAttribuesManager) _getManager();
            manager.setSession(getSession());

            manager.setForAnnee(getAnnee());
            manager.setForGenreControle(getGenreControle());
            manager.setForVisaReviseur(getVisaReviseur());

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                abort();
                getMemoryLog().logMessage(getSession().getLabel("NAOS_CONTROLES_AUCUNE_IMPRESSION"), FWServlet.ERROR,
                        getSession().getLabel("NAOS_FICHIER_CONTROLES_ATTRIBUES"));
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
    }

    /*
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    /**
     * @throws Exception
     *             DOCUMENT ME!
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

        if (JadeStringUtil.isEmpty(getAnnee())) {
            this._addError(getSession().getLabel("NAOS_CONTROLES_PAS_ANNEE"));
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /*
     * Contenu des cellules
     */
    /**
     * @param entity
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {
        // valeurs

        AFControlesAttribues controlesAttribues = (AFControlesAttribues) entity;

        compt++;
        setProgressDescription(controlesAttribues.getNumAffilie() + " <br>");
        if (isAborted()) {
            setProgressDescription("Traitement interrompu<br> sur l'affilié : " + controlesAttribues.getNumAffilie()
                    + " <br>");
            if ((getParent() != null) && getParent().isAborted()) {
                getParent().setProcessDescription(
                        "Traitement interrompu<br> sur l'affilié : " + controlesAttribues.getNumAffilie() + " <br>");
            }
        } else {
            try {
                // Reprise des données de l'affilié pour remplissage des
                // cellules
                AFAffiliation affilie = new AFAffiliation();
                affilie.setId(controlesAttribues.getIdAffilie());
                affilie.setIdTiers(controlesAttribues.getIdTiers());
                affilie.setSession(getSession());
                affilie.retrieve();

                // Reprise des données du tiers
                TITiersViewBean tiers = affilie.getTiers();

                _addCell(formatNumNomAff50Length(affilie.getAffilieNumero() + " - " + tiers.getNom()));
                _addCell(formatLocalite19Length(tiers.getLocalite()));
                _addCell(affilie.getDateDebut() + " - " + affilie.getDateFin());
                _addCell(getAnnee());
                _addCell(getSession().getCodeLibelle(controlesAttribues.getTypeControle()));
                _addCell(controlesAttribues.getTempsJour());
                _addCell(controlesAttribues.getNbInscCI());
                _addCell(controlesAttribues.getDatePrecControle());
                _addCell(JANumberFormatter.format(controlesAttribues.getMontantMasse_1()));
                _addCell(formatVisa8Length(controlesAttribues.getVisaReviseur()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String formatLocalite19Length(String nom) {

        if (nom.length() >= 19) {
            return nom.substring(0, 19);
        } else {
            return nom;
        }

    }

    protected String formatNumNomAff50Length(String nom) {

        if (nom.length() >= 50) {
            return nom.substring(0, 50);
        } else {
            return nom;
        }

    }

    protected String formatVisa8Length(String nom) {

        if (nom.length() >= 8) {
            return nom.substring(0, 8);
        } else {
            return nom;
        }

    }

    /**
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    public String getAnnee_1() {

        int Annee = Integer.parseInt(annee);
        annee_1 = String.valueOf(Annee - 1);

        return annee_1;
    }

    /*
     * Titre de l'email
     */
    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("NAOS_FICHIER_CONTROLES_ATTRIBUES");
    }

    /**
     * @return
     */
    public String getGenreControle() {
        return genreControle;
    }

    /**
     * @return
     */
    public String getSelectionGroupe() {
        return selectionGroupe;
    }

    /**
     * @return
     */
    public String getVisaReviseur() {
        return visaReviseur;
    }

    /*
     * Initialisation des colonnes et des groupes
     */
    /**
	 */
    @Override
    protected void initializeTable() {
        // colonnes
        this._addColumnLeft(getSession().getLabel("NAOS_COLONNE_NOAFFILIENOM"), 25);
        this._addColumnLeft(getSession().getLabel("NAOS_COLONNE_LOCALITE"), 10);
        this._addColumnLeft(getSession().getLabel("NAOS_COLONNE_PERIODEAFF"), 15);
        this._addColumnCenter("Période", 5);
        this._addColumnLeft(getSession().getLabel("NAOS_CONTROLES_TYPE"), 7);
        this._addColumnRight("Temps", 8);
        this._addColumnRight("Nb CI (" + getAnnee_1() + ")", 7);
        this._addColumnRight("Dernier contrôle", 10);
        this._addColumnRight("Masse (" + getAnnee_1() + ")", 7);
        this._addColumnLeft(getSession().getLabel("NAOS_COLONNE_REVISEUR"), 5);

    }

    /**
     * Set la jobQueue
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setAnnee_1(String string) {
        annee_1 = string;
    }

    /**
     * @param string
     */
    public void setGenreControle(String string) {
        genreControle = string;
    }

    /**
     * @param string
     */
    public void setSelectionGroupe(String string) {
        selectionGroupe = string;
    }

    /**
     * @param string
     */
    public void setVisaReviseur(String string) {
        visaReviseur = string;
    }

}
