/*
 * Créé le 5 mars 07
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
import globaz.naos.db.controleEmployeur.AFControleEmployeur;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.db.controleEmployeur.AFControlesAEffectuer;
import globaz.naos.db.controleEmployeur.AFControlesAEffectuerManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author HPE
 * 
 */

public class AFListeControlesAEffectuerIText extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Classe interne qui défini les clés pour le tri par canton et type de
    // prestations
    private class Key {
        public String idAffilie;

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         * 
         * @param o
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public int compareTo(Object o) {
            Key key = (Key) o;

            if (idAffilie.compareTo(key.idAffilie) != 0) {
                return idAffilie.compareTo(key.idAffilie);
            } else {
                return 0;
            }
        }

        /**
         * @param obj
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            return ((key.idAffilie.equals(idAffilie)));
        }

        /**
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public int hashCode() {
            return (idAffilie).hashCode();
        }
    }

    public final static String NUM_REF_INFOROM_LISTE_CONTROLE_AEFFECTUER = "0208CAF";
    private String annee = new String();
    private int compt = 0;
    private AFControlesAEffectuer controlePrecedent = null;

    private AFControlesAEffectuer controlePrecedent_2 = null;
    private String genreControle = new String();

    private String idControle = new String();

    private Boolean isAvecReviseur = null;
    Map map = new Hashtable();
    private String masseSalA = new String();
    private String masseSalDe = new String();

    private int nbLigne = 0;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private boolean verifAfter = false;

    /**
     * Crée une nouvelle instance de la classe APListePrestationsAPGControlees.
     */
    public AFListeControlesAEffectuerIText() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "", "", "Liste des contrôles à effectuer", new AFControlesAEffectuerManager(),
                AFApplication.DEFAULT_APPLICATION_NAOS);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFListeControlesAttribuesIText.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public AFListeControlesAEffectuerIText(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "", "", session.getLabel("NAOS_FICHIER_CONTROLES_AEFFECTUER"),
                new AFControlesAEffectuerManager(), AFApplication.DEFAULT_APPLICATION_NAOS);
    }

    /*
     * transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {

        getDocumentInfo().setDocumentTypeNumber(
                AFListeControlesAEffectuerIText.NUM_REF_INFOROM_LISTE_CONTROLE_AEFFECTUER);
        try {

            // Création du manager
            AFControlesAEffectuerManager manager = (AFControlesAEffectuerManager) _getManager();
            manager.setSession(getSession());

            manager.setForAnnee(getAnnee());
            manager.setForGenreControle(getGenreControle());
            manager.setForMasseSalA(getMasseSalA());
            manager.setForMasseSalDe(getMasseSalDe());
            manager.setIsAvecReviseur(isAvecReviseur);
            // manager.setPeriodicite(4);

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                abort();
                getMemoryLog().logMessage(getSession().getLabel("NAOS_CONTROLES_AUCUNE_IMPRESSION"), FWServlet.ERROR,
                        getSession().getLabel("NAOS_FICHIER_CONTROLES_AEFFECTUER"));
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

        if (JadeStringUtil.isEmpty(getMasseSalDe())
                || (Double.parseDouble(JANumberFormatter.deQuote(getMasseSalDe())) < 1)) {
            this._addError(getSession().getLabel("NAOS_CONTROLES_MASSE_MIN"));
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
        AFControlesAEffectuer controlesAEffectuer = (AFControlesAEffectuer) entity;

        try {

            // Définition de la périodicité
            int periodicite = 0;

            // on vérifie que le cas est bien le dernier control pour cet
            // affilié. si oui on ajoute la ligne, sinon on ne prend pas le cas.
            if ((controlePrecedent != null) && verifAfter
                    && !controlePrecedent.getIdAffilie().equals(controlesAEffectuer.getIdAffilie())) {
                Key k = new Key();
                k.idAffilie = controlesAEffectuer.getIdAffilie();
                if (((!isAvecReviseur.booleanValue() && JadeStringUtil.isEmpty(controlePrecedent.getVisaReviseur())) || (isAvecReviseur
                        .booleanValue())) && !(!map.containsKey(k.idAffilie))) {
                    AFAffiliation affiliePrecedent = new AFAffiliation();
                    affiliePrecedent.setId(controlePrecedent.getIdAffilie());
                    affiliePrecedent.setIdTiers(controlePrecedent.getIdTiers());
                    affiliePrecedent.setSession(getSession());
                    affiliePrecedent.retrieve();

                    map.put(controlePrecedent.getIdAffilie(), "");
                    nbLigne++;

                    // Reprise des données du tiers
                    TITiersViewBean tiers = affiliePrecedent.getTiers();

                    // Récupération du dernier control pour cet affilié
                    String dernierControlDate = "-";
                    String dernierControlReviseur = "-";
                    AFControleEmployeurManager dernierControlManager = new AFControleEmployeurManager();
                    dernierControlManager.setSession(getSession());
                    dernierControlManager.setForAffiliationId(controlePrecedent.getIdAffilie());
                    dernierControlManager.setForLastControlEffectif(true);
                    dernierControlManager.find();
                    if (dernierControlManager.size() != 0) {
                        AFControleEmployeur dernierControlEffectif = (AFControleEmployeur) dernierControlManager
                                .getFirstEntity();
                        if (!JadeStringUtil.isEmpty(dernierControlEffectif.getDateEffective())) {
                            dernierControlDate = dernierControlEffectif.getDateEffective().substring(3);
                        }
                        if (!JadeStringUtil.isEmpty(dernierControlEffectif.getControleurVisa())) {
                            dernierControlReviseur = dernierControlEffectif.getControleurVisa();
                        }
                    }

                    _addCell(formatNumNomAff50Length(affiliePrecedent.getAffilieNumero() + " - " + tiers.getNom()));
                    _addCell(formatLocalite19Length(tiers.getLocalite()));
                    _addCell(affiliePrecedent.getDateDebut() + " - " + affiliePrecedent.getDateFin());
                    _addCell(getAnnee());
                    _addCell(getSession().getCodeLibelle(controlePrecedent.getTypeControle()));
                    _addCell(controlePrecedent.getTempsJour());
                    _addCell(controlePrecedent.getNbInscCI());
                    _addCell(dernierControlDate + "/" + dernierControlReviseur);
                    _addCell(JANumberFormatter.format(controlePrecedent.getMontantMasse_2()));

                    if (!JadeStringUtil.isEmpty(controlePrecedent.getVisaReviseur())) {
                        _addCell(formatVisa8Length(controlePrecedent.getVisaReviseur()));
                    } else if ((controlePrecedent_2 != null)
                            && controlePrecedent_2.getIdAffilie().equals(controlesAEffectuer.getIdAffilie())
                            && !JadeStringUtil.isEmpty(controlePrecedent.getVisaReviseur())) {
                        _addCell(formatVisa8Length(controlePrecedent_2.getVisaReviseur()));
                    } else {
                        _addCell(formatVisa8Length("-"));
                    }

                    // System.out.println("ajout de la ligne d'avant pour : "+affiliePrecedent.getAffilieNumero());

                    // si le cas est pris il faut ajouter la ligne, incrémenter
                    // le compteur et surtout remettre newRow=null.
                    this._addDataTableRow();
                    incProgressCounter();
                    newRow = null;
                }
            }

            // Début des testes permettant de définir si l'entité doit être ou
            // prise en compte
            verifAfter = false;

            if (JadeStringUtil.isBlankOrZero(controlesAEffectuer.getPeriodiciteAff())) {
                periodicite = Integer.parseInt(getSession().getApplication().getProperty(
                        "periodiciteControleEmployeurCaisse", "0"));
            } else {
                periodicite = Integer.parseInt(controlesAEffectuer.getPeriodiciteAff());
            }

            // on prend l'entité si 1 ou 2 ou 3 ou 4 est vrai
            if (
            // 1 annee date prevue = annee
            (controlesAEffectuer.getDatePrevue().startsWith(getAnnee()))
            // 1 annee date prevue <= annee && date effective = vide
            // (Integer.parseInt(controlesAEffectuer.getDatePrevue().substring(0,4))<=Integer.parseInt(getAnnee())
            // &&
            // JadeStringUtil.isBlankOrZero((controlesAEffectuer.getDateEffective())))

                    ||

                    // 2 annee fin du control = annee-(periode-1)
                    ((controlesAEffectuer.getDateFinControle().startsWith(String.valueOf(Integer.parseInt(getAnnee())
                            - periodicite - 1))) ? verifAfter = true : false)

                    ||

                    // 3 annee fin d'affiliation = annee-1
                    (controlesAEffectuer.getAffDateFin().startsWith(getAnnee_1()))

                    ||

                    //
                    // 4 date precedent control = vide && annee debut
                    // affiliation = annee-periode && (date fin affiliation =
                    // vide || date fin affiliation >= 01.01.annee)
                    (JadeStringUtil.isEmpty(controlesAEffectuer.getDatePrecControle())
                            && (Integer.parseInt(controlesAEffectuer.getAffDateDebut().substring(0, 4)) == Integer
                                    .parseInt(getAnnee()) - periodicite) && ((JadeStringUtil
                            .isBlankOrZero(controlesAEffectuer.getAffDateFin())) || (Integer
                            .parseInt(controlesAEffectuer.getAffDateFin()) >= Integer.parseInt(getAnnee() + "0101"))))

                    ||

                    // 5 Si aucun contrôle n'a été effectué et que la date
                    // d'affiliation est <= annee-periode
                    (JadeStringUtil.isEmpty(controlesAEffectuer.getIdControle()) && (Integer
                            .parseInt(controlesAEffectuer.getAffDateFin()) <= Integer.parseInt(getAnnee())
                            - periodicite))) {
                if ((!isAvecReviseur.booleanValue() && JadeStringUtil.isEmpty(controlesAEffectuer.getVisaReviseur()))
                        || (isAvecReviseur.booleanValue())) {
                    // si on prend le cas
                    Key k = new Key();
                    k.idAffilie = controlesAEffectuer.getIdAffilie();

                    // permet de ne pas mettre 2x le même affilié dans la liste
                    if (!map.containsKey(k.idAffilie)) {

                        compt++;
                        setProgressDescription(controlesAEffectuer.getNumAffilie() + " <br>");
                        if (isAborted()) {
                            setProgressDescription("Traitement interrompu<br> sur l'affilié : "
                                    + controlesAEffectuer.getNumAffilie() + " <br>");
                            if ((getParent() != null) && getParent().isAborted()) {
                                getParent().setProcessDescription(
                                        "Traitement interrompu<br> sur l'affilié : "
                                                + controlesAEffectuer.getNumAffilie() + " <br>");
                            }

                        } else {
                            // dans le cas ou le test 2 passe, il est nécessaire
                            // de vérifier que le cas en question est bien le
                            // dernier control pour cet affilié.
                            // on ajoute donc pas tout de suite la ligne. Si il
                            // d'une des clauses 1,3 ou 4 on ajoute la ligne
                            // directement.
                            if (!verifAfter) {
                                map.put(controlesAEffectuer.getIdAffilie(), "");
                                nbLigne++;
                                // Reprise des données de l'affilié pour
                                // remplissage des cellules
                                AFAffiliation affilie = new AFAffiliation();
                                affilie.setId(controlesAEffectuer.getIdAffilie());
                                affilie.setIdTiers(controlesAEffectuer.getIdTiers());
                                affilie.setSession(getSession());
                                affilie.retrieve();
                                // Reprise des données du tiers
                                TITiersViewBean tiers = affilie.getTiers();

                                // Récupération du dernier control pour cet
                                // affilié
                                String dernierControlDate = "-";
                                String dernierControlReviseur = "-";
                                AFControleEmployeurManager dernierControlManager = new AFControleEmployeurManager();
                                dernierControlManager.setSession(getSession());
                                dernierControlManager.setForAffiliationId(controlesAEffectuer.getIdAffilie());
                                dernierControlManager.setForLastControlEffectif(true);
                                dernierControlManager.find();
                                if (dernierControlManager.size() != 0) {
                                    AFControleEmployeur dernierControlEffectif = (AFControleEmployeur) dernierControlManager
                                            .getFirstEntity();
                                    if (!JadeStringUtil.isEmpty(dernierControlEffectif.getDateEffective())) {
                                        dernierControlDate = dernierControlEffectif.getDateEffective().substring(3);
                                    }
                                    if (!JadeStringUtil.isEmpty(dernierControlEffectif.getControleurVisa())) {
                                        dernierControlReviseur = dernierControlEffectif.getControleurVisa();
                                    }
                                }

                                _addCell(formatNumNomAff50Length(affilie.getAffilieNumero() + " - " + tiers.getNom()));
                                _addCell(formatLocalite19Length(tiers.getLocalite()));
                                _addCell(affilie.getDateDebut() + " - " + affilie.getDateFin());
                                _addCell(getAnnee());
                                _addCell(getSession().getCodeLibelle(controlesAEffectuer.getTypeControle()));
                                _addCell(controlesAEffectuer.getTempsJour());
                                _addCell(controlesAEffectuer.getNbInscCI());
                                _addCell(dernierControlDate + "/" + dernierControlReviseur);
                                _addCell(JANumberFormatter.format(controlesAEffectuer.getMontantMasse_2()));
                                if (!JadeStringUtil.isEmpty(controlesAEffectuer.getVisaReviseur())) {
                                    _addCell(formatVisa8Length(controlesAEffectuer.getVisaReviseur()));
                                } else if ((controlePrecedent != null)
                                        && controlePrecedent.getIdAffilie().equals(controlesAEffectuer.getIdAffilie())
                                        && !JadeStringUtil.isEmpty(controlePrecedent.getVisaReviseur())) {
                                    _addCell(formatVisa8Length(controlePrecedent.getVisaReviseur()));
                                } else {
                                    _addCell(formatVisa8Length("-"));
                                }
                            }
                        }
                    }
                }

            } else {
                // sinon on fait rien
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // on garde des références sur les 2 entités précédentes
        controlePrecedent_2 = controlePrecedent;
        controlePrecedent = controlesAEffectuer;
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
        return String.valueOf(Integer.parseInt(getAnnee()) - 1);
    }

    public String getAnnee_2() {
        return String.valueOf(Integer.parseInt(getAnnee()) - 2);
    }

    /**
     * @return
     */
    public String getAnneeDebutPerioCtrl() {
        return String.valueOf(Integer.parseInt(getAnnee_1()) - 3);
    }

    /**
     * @return
     */
    public String getAnneeFinPerioCtrl() {
        return getAnnee_1();
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
        return getSession().getLabel("NAOS_FICHIER_CONTROLES_AEFFECTUER");
    }

    /**
     * @return
     */
    public String getGenreControle() {
        return genreControle;
    }

    public String getIdControle() {
        return idControle;
    }

    /**
     * @return
     */
    public Boolean getIsAvecReviseur() {
        return isAvecReviseur;
    }

    /**
     * @return
     */
    public String getMasseSalA() {
        return masseSalA;
    }

    /**
     * @return
     */
    public String getMasseSalDe() {
        return masseSalDe;
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
        this._addColumnRight("Nb CI (" + getAnnee_2() + ")", 7);
        this._addColumnRight("Dernier contrôle", 10);
        this._addColumnRight("Masse (" + getAnnee_2() + ")", 7);
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
    public void setGenreControle(String string) {
        genreControle = string;
    }

    public void setIdControle(String idControle) {
        this.idControle = idControle;
    }

    /**
     * @param boolean1
     */
    public void setIsAvecReviseur(Boolean boolean1) {
        isAvecReviseur = boolean1;
    }

    /**
     * @param string
     */
    public void setMasseSalA(String string) {
        masseSalA = string;
    }

    /**
     * @param string
     */
    public void setMasseSalDe(String string) {
        masseSalDe = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList #summary()
     */
    @Override
    protected void summary() throws FWIException {

        if (nbLigne == 0) {
            abort();
            getMemoryLog().logMessage(getSession().getLabel("NAOS_CONTROLES_AUCUNE_IMPRESSION"), FWServlet.ERROR,
                    getSession().getLabel("NAOS_FICHIER_CONTROLES_AEFFECTUER"));
        }

        super.summary();
    }

}
