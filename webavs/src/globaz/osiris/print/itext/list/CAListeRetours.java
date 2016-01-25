/*
 * Créé le 3 fevrier 09
 */
package globaz.osiris.print.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.retours.CARetours;
import globaz.osiris.db.retours.CARetoursListViewBean;
import globaz.osiris.db.retours.CARetoursViewBean;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class CAListeRetours extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCsEtatRetour = "";

    private String forCsMotifRetour = "";
    private String forDateRetour = "";
    private String forIdLot = "";
    private String forMontantRetour = "";
    private String likeLibelleRetour = "";
    private String likeNumNom = "";
    private String orderBy = "";

    private FWCurrency totalRetours = new FWCurrency("0.00");

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APListePrestations.
     */
    public CAListeRetours() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, CAApplication.DEFAULT_OSIRIS_NAME, "GLOBAZ", "Liste des retours", new CARetoursListViewBean(),
                CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     * Crée une nouvelle instance de la classe APListePrestations.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public CAListeRetours(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, CAApplication.DEFAULT_OSIRIS_NAME, "GLOBAZ", session.getLabel("JSP_LISTE_RETOURS"),
                new CARetoursListViewBean(), CAApplication.DEFAULT_APPLICATION_OSIRIS);
    }

    /**
     */
    @Override
    public void _beforeExecuteReport() {

        // on ajoute au doc info le numéro de référence inforom
        // TODO getDocumentInfo().setDocumentTypeNumber("");

        CARetoursListViewBean manager = (CARetoursListViewBean) _getManager();
        manager.setSession(getSession());

        manager.setForIdLot(getForIdLot());
        manager.setLikeNumNom(getLikeNumNom());
        manager.setForMontantRetour(getForMontantRetour());
        manager.setForCsEtatRetour(getForCsEtatRetour());
        manager.setForCsMotifRetour(getForCsMotifRetour());
        manager.setForDateRetour(getForDateRetour());
        manager.setLikeLibelleRetour(getLikeLibelleRetour());

        manager.setOrderBy(CACompteAnnexe.FIELD_DESCUPCASE);

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new CARetoursViewBean());
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

        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * transfère des paramètres au manager;
     */

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
        CARetoursViewBean retour = (CARetoursViewBean) entity;

        _addCell(retour.getCompteAnnexe().getDescription());
        _addCell(retour.getCsMotifRetourLibelle() + " - " + retour.getLibelleRetour());
        _addCell(retour.getDateRetour());
        _addCell(new FWCurrency(retour.getMontantRetour()).toStringFormat());
        _addCell(" ");
        _addCell(retour.getCsEtatRetourLibelle());
        _addCell(retour.getIdRetour());

        // mise a jour du total des retours
        totalRetours.add(retour.getMontantRetour());
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
        return getSession().getLabel("JSP_LISTE_RETOURS");
    }

    public String getForCsEtatRetour() {
        return forCsEtatRetour;
    }

    public String getForCsMotifRetour() {
        return forCsMotifRetour;
    }

    public String getForDateRetour() {
        return forDateRetour;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForMontantRetour() {
        return forMontantRetour;
    }

    public String getLikeLibelleRetour() {
        return likeLibelleRetour;
    }

    public String getLikeNumNom() {
        return likeNumNom;
    }

    /**
     * getter pour l'attribut order by
     * 
     * @return la valeur courante de l'attribut order by
     */
    public String getOrderBy() {
        return orderBy;
    }

    public FWCurrency getTotalRetours() {
        return totalRetours;
    }

    /**
     * Initialisation des colonnes et des groupes
     * 
     */
    @Override
    protected void initializeTable() {

        // affichage des criteres de recherche non "vides"
        FWIDocumentTable tblTotauxGenre = new FWIDocumentTable();

        try {
            tblTotauxGenre.addColumn(getSession().getLabel("LISTE_RETOURS_CRITERES_SELECTION"), FWITableModel.LEFT, 10);
            tblTotauxGenre.endTableDefinition();

            StringBuffer criteres = new StringBuffer();

            if (!JadeStringUtil.isEmpty(forIdLot)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_RETOURS_CRITERES_ID_LOT"));
                criteres.append(forIdLot);
            }

            if (!JadeStringUtil.isIntegerEmpty(likeNumNom)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_RETOURS_CRITERES_NO_NOM_CA"));
                criteres.append(likeNumNom);
            }

            if (!JadeStringUtil.isIntegerEmpty(forMontantRetour)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_RETOURS_CRITERES_MONTANT_RETOUR"));
                criteres.append(new FWCurrency(forMontantRetour).toStringFormat());
            }

            if (!JadeStringUtil.isIntegerEmpty(forCsEtatRetour)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_RETOURS_CRITERES_ETAT_RETOUR"));

                if (CARetours.CLE_ETAT_RETOUR_NON_LIQUIDE.equals(forCsEtatRetour)) {
                    criteres.append(getSession().getLabel("LIBELLE_NON_LIQUIDE"));
                } else {
                    criteres.append(getSession().getCodeLibelle(forCsEtatRetour));
                }
            }

            if (!JadeStringUtil.isEmpty(forCsMotifRetour)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_RETOURS_CRITERES_MOTIF_RETOUR"));
                criteres.append(getSession().getCodeLibelle(forCsMotifRetour));
            }

            if (!JadeStringUtil.isEmpty(forDateRetour)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_RETOURS_CRITERES_DATE_RETOUR"));
                criteres.append(forDateRetour);
            }

            if (!JadeStringUtil.isEmpty(likeLibelleRetour)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_RETOURS_CRITERES_LIBELLE_RETOUR"));
                criteres.append(likeLibelleRetour);
            }

            if (criteres.length() == 0) {
                criteres.append(" - ");
            }

            tblTotauxGenre.addCell(criteres.toString());
            tblTotauxGenre.addRow();
            tblTotauxGenre.addRow();

            super._addTable(tblTotauxGenre);
        } catch (FWIException e) {
            e.printStackTrace();
        }

        // colonnes
        this._addColumnLeft(getSession().getLabel("LISTE_RETOURS_NO_NOM_CA"), 25);
        this._addColumnLeft(getSession().getLabel("LISTE_RETOURS_MOTIF_REMARQUE"), 25);
        this._addColumnLeft(getSession().getLabel("LISTE_RETOURS_DATE"), 6);
        this._addColumnRight(getSession().getLabel("LISTE_RETOURS_MONTANT"), 10);
        this._addColumnRight(" ", 1);
        this._addColumnLeft(getSession().getLabel("LISTE_RETOURS_ETAT"), 10);
        this._addColumnLeft(getSession().getLabel("LISTE_RETOURS_NO"), 4);
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

    public void setForCsEtatRetour(String forCsEtatRetour) {
        this.forCsEtatRetour = forCsEtatRetour;
    }

    public void setForCsMotifRetour(String forCsMotifRetour) {
        this.forCsMotifRetour = forCsMotifRetour;
    }

    public void setForDateRetour(String forDateRetour) {
        this.forDateRetour = forDateRetour;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForMontantRetour(String forMontantRetour) {
        this.forMontantRetour = forMontantRetour;
    }

    public void setLikeLibelleRetour(String likeLibelleRetour) {
        this.likeLibelleRetour = likeLibelleRetour;
    }

    public void setLikeNumNom(String likeNumNom) {
        this.likeNumNom = likeNumNom;
    }

    /**
     * setter pour l'attribut order by
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

    public void setTotalRetours(FWCurrency totalRetours) {
        this.totalRetours = totalRetours;
    }

    /**
     * Surcharge.
     * 
     * @throws globaz.framework.printing.itext.exception.FWIException
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#summary()
     **/
    @Override
    protected void summary() throws FWIException {

        _addCell(getSession().getLabel("LISTE_RETOURS_TOTAUX"));
        _addCell("");
        _addCell("");
        _addCell(totalRetours.toStringFormat());
        _addCell("");
        _addCell("");
        _addCell("");

        super.summary();
    }

}
