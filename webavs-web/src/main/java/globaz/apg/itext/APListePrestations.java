/*
 * Créé le 3 juillet 08
 */
package globaz.apg.itext;

import java.awt.Color;
import com.lowagie.text.DocumentException;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.db.prestation.APPrestationsControlees;
import globaz.apg.vb.prestation.APPrestationJointLotTiersDroitListViewBean;
import globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.externe.IPRConstantesExternes;
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
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;

/**
 * <H1>Description</H1>
 *
 * @author bsc
 */
public class APListePrestations extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String derniereIdPrestation = null;
    private String forCsSexe = "";
    private String forDateNaissance = "";

    private String forEtat = "";
    private String forIdDroit = "";
    private String forNoLot = "";
    private String forTypeDroit = "";
    private String fromDateDebut = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    private FWCurrency montantTotalAllocationsListe = new FWCurrency("0.00");
    private FWCurrency montantTotalFraisGardeListe = new FWCurrency("0.00");
    private int nombreTotalCas = 0;
    private String orderBy = "";
    private String toDateFin = "";
    private FWCurrency totalMontantBrut = new FWCurrency("0.00");
    private FWCurrency totalMontantCotisation = new FWCurrency("0.00");
    private FWCurrency totalMontantNet = new FWCurrency("0.00");

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APListePrestations.
     */
    @Deprecated
    // Ce constructeur qui ne permet pas de remonter un label sans session, n'a pas lieu d'être.
    public APListePrestations() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, "PRESTATIONS", "GLOBAZ", "Liste des prestations", new APPrestationJointLotTiersDroitListViewBean(),
                "APG");
    }

    /**
     * Crée une nouvelle instance de la classe APListePrestations.
     *
     * @param session
     *            DOCUMENT ME!
     */
    public APListePrestations(BSession session) {

        // session, prefix, Compagnie, Titre, manager, application
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("JSP_LISTE_PRESTATIONS"),
                new APPrestationJointLotTiersDroitListViewBean(), "APG");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * transfère des paramètres au manager;
     */

    /**
     */
    @Override
    public void _beforeExecuteReport() {

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_PRESTATIONS_APG);

        APPrestationJointLotTiersDroitListViewBean manager = (APPrestationJointLotTiersDroitListViewBean) _getManager();
        manager.setSession(getSession());
        manager.setForCsSexe(getForCsSexe());
        manager.setForEtat(getForEtat());
        manager.setForDateNaissance(getForDateNaissance());
        manager.setForIdDroit(getForIdDroit());
        manager.setForIdLot(getForNoLot());
        manager.setForTypeDroit(getForTypeDroit());
        manager.setLikeNom(getLikeNom());
        manager.setLikePrenom(getLikePrenom());
        manager.setLikeNumeroAVS(getLikeNumeroAVS());
        manager.setLikeNumeroAVSNNSS(getLikeNumeroAVSNNSS());
        manager.setFromDateDebut(getFromDateDebut());
        manager.setToDateFin(getToDateFin());

        manager.setHasSumMontantNet(true);

        manager.setOrderBy(
                APPrestationJointLotTiersDroit.FIELDNAME_NOM + " , " + APPrestationJointLotTiersDroit.FIELDNAME_PRENOM
                        + " , " + APPrestation.FIELDNAME_DATEDEBUT + ", " + APPrestation.FIELDNAME_IDPRESTATIONAPG);

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new APPrestationsControlees());
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
        // Traitement de la prestation
        APPrestationJointLotTiersDroitViewBean prestation = (APPrestationJointLotTiersDroitViewBean) entity;

        if (!prestation.getIdPrestation().equals(derniereIdPrestation)) {

            PRTiersWrapper tier = null;

            try {
                tier = PRTiersHelper.getTiers(getSession(), prestation.getNoAVS());
            } catch (Exception e) {
                getSession().addError(getSession().getLabel("ERROR_TIERS_INTROUVABLE_PAR_NO_AVS"));
            }
            if (tier != null) {

                _addCell(prestation.getNoAVS() + " / " + formatNom(prestation.getNom() + " " + prestation.getPrenom())
                        + " / " + tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                        + getLibelleCourtSexe(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / "
                        + getLibellePays(tier.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            } else {
                _addCell("");
            }

            _addCell(prestation.getDateDebut() + " - " + prestation.getDateFin());
            _addCell(getSession().getCodeLibelle(prestation.getGenreService()));
            _addCell(prestation.getNombreJoursSoldes());
            derniereIdPrestation = prestation.getIdPrestation();

            _addCell(" ");
            _addCell(new FWCurrency(prestation.getMontantJournalier()).toStringFormat());

            // IR521 : Ajout du montant des frais de garde au montant brut
            FWCurrency montantBrut = new FWCurrency(prestation.getMontantBrut());
            montantBrut.add(new FWCurrency(prestation.getFraisGarde()));

            // calcul de la cotisation
            FWCurrency cotisations = new FWCurrency(prestation.getMontantNet());
            cotisations.sub(montantBrut);
            _addCell(montantBrut.toStringFormat());
            _addCell(cotisations.toStringFormat());
            _addCell(new FWCurrency(prestation.getMontantNet()).toStringFormat());

            // mise a jour des totaux
            if (!prestation.isAnnule()) {
                // mise a jour des totaux
                totalMontantBrut.add(montantBrut);
                totalMontantNet.add(prestation.getMontantNet());
                totalMontantCotisation.add(cotisations);
            } else {
                // Ligne grisée
                _getDataTableModel().setColorBackGround(new Color(173, 173, 173));
            }
            try {
                this._addDataTableRow();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            if (((!JadeStringUtil.isEmpty(prestation.getMontantTotalAllocExploitation()))
                    && !(new FWCurrency(prestation.getMontantTotalAllocExploitation()).isZero()))) {
                _addCell("");
                _addCell(getSession().getLabel("JSP_LISTE_PRESTATION_ALLOCATION_EXPLOITATION"));
                _addCell("");
                _addCell("");
                _addCell("");
                _addCell("");
                _addCell(new FWCurrency(prestation.getMontantTotalAllocExploitation()).toString());
                _addCell("");
                _addCell("");

                montantTotalAllocationsListe
                        .add(new FWCurrency(prestation.getMontantTotalAllocExploitation()).toString());

                try {
                    this._addDataTableRow();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }

            if ((!JadeStringUtil.isEmpty(prestation.getFraisGarde())
                    && !(new FWCurrency(prestation.getFraisGarde()).isZero()))) {

                _addCell("");
                _addCell(getSession().getLabel("JSP_LISTE_PRESTATION_FRAIS_DE_GARDE"));
                _addCell("");
                _addCell("");
                _addCell("");
                _addCell("");
                _addCell(new FWCurrency(prestation.getFraisGarde()).toString());
                _addCell("");
                _addCell("");

                montantTotalFraisGardeListe.add(new FWCurrency(prestation.getFraisGarde()).toString());

                try {
                    this._addDataTableRow();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }

            // Ajout du cas au nombre de cas totaux
            nombreTotalCas++;

        }

    }

    // Formate le nom pour qu'il ne déborde pas de sa cellule
    private String formatNom(String nom) {
        // TODO c pas bien fait du tout, mais au moins ça marche
        if (nom.length() > 20) {
            // on coupe suivant les espaces
            String[] nomCoupe = PRStringUtils.split(nom, ' ');
            char modeCoupage = ' ';

            // si ça n'a pas suffit, on le coupe avec le -
            if (nomCoupe.length == 1) {
                nomCoupe = PRStringUtils.split(nom, '-');
                modeCoupage = '-';
            }

            int size = 0;
            nom = "";

            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < nomCoupe.length; i++) {
                size += nomCoupe[i].length();

                if (size > 20) {
                    if (i == 0) {
                        nom = nomCoupe[i].substring(0, 20) + "...";
                        buffer.delete(0, 60);

                        break;
                    } else {
                        buffer.append(modeCoupage);
                        buffer.append("\n");
                        size = nomCoupe[i].length();

                        if (size > 20) {
                            buffer.append(nomCoupe[i].substring(0, 20) + "...");
                            nom += buffer.toString();
                            buffer.delete(0, 60);

                            break;
                        }

                        nom += buffer.toString();
                        buffer.delete(0, 30);
                        buffer.append(nomCoupe[i]);
                    }
                } else {
                    if (i != 0) {
                        buffer.append(modeCoupage);
                    }

                    buffer.append(nomCoupe[i]);
                    size += 1;
                }
            }

            if (buffer.length() != 0) {
                nom += buffer.toString();
            }
        }

        return nom;
    }

    public String getDerniereIdPrestation() {
        return derniereIdPrestation;
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
        return getSession().getLabel("LISTE_PRESTATIONS");
    }

    public String getForCsSexe() {
        return forCsSexe;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * getter pour l'attribut for etat
     *
     * @return la valeur courante de l'attribut for etat
     */
    public String getForEtat() {
        return forEtat;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForNoLot() {
        return forNoLot;
    }

    public String getForTypeDroit() {
        return forTypeDroit;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est passé en paramètre
     *
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est passé en paramètre
     *
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

    }

    public String getLikeNom() {
        return likeNom;
    }

    public String getLikeNumeroAVS() {
        return likeNumeroAVS;
    }

    public String getLikeNumeroAVSNNSS() {
        return likeNumeroAVSNNSS;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * getter pour l'attribut order by
     *
     * @return la valeur courante de l'attribut order by
     */
    public String getOrderBy() {
        return orderBy;
    }

    public String getToDateFin() {
        return toDateFin;
    }

    /*
     * Initialisation des colonnes et des groupes
     */
    /**
     */
    @Override
    protected void initializeTable() {

        // affichage des criteres de recherche non "vides"
        FWIDocumentTable tblTotauxGenre = new FWIDocumentTable();

        try {
            tblTotauxGenre.addColumn(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_SELECTION"), FWITableModel.LEFT,
                    10);
            tblTotauxGenre.endTableDefinition();

            StringBuffer criteres = new StringBuffer();

            if (!JadeStringUtil.isEmpty(likeNumeroAVS)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NNSS"));
                criteres.append(likeNumeroAVS);
            }

            if (!JadeStringUtil.isEmpty(likeNom)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NOM"));
                criteres.append(likeNom);
            }

            if (!JadeStringUtil.isEmpty(likePrenom)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_PRENOM"));
                criteres.append(likePrenom);
            }

            if (!JadeStringUtil.isIntegerEmpty(forDateNaissance)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_DATE_NAISSANCE"));
                criteres.append(forDateNaissance);
            }

            if (!JadeStringUtil.isIntegerEmpty(forCsSexe)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_SEXE"));
                criteres.append(getSession().getCodeLibelle(forCsSexe));
            }

            if (!JadeStringUtil.isEmpty(fromDateDebut)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_DATE_DEBUT"));
                criteres.append(fromDateDebut);
            }

            if (!JadeStringUtil.isEmpty(toDateFin)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_DATE_FIN"));
                criteres.append(toDateFin);
            }

            if (!JadeStringUtil.isIntegerEmpty(forEtat)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_ETAT"));

                if (APPrestationManager.ETAT_NON_DEFINITIF.equals(forEtat)) {
                    criteres.append(getSession().getLabel("JSP_NON_DEFINITIF"));
                } else {
                    criteres.append(getSession().getCodeLibelle(forEtat));
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(forNoLot)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NO_LOT"));
                criteres.append(forNoLot);
            }

            if (!JadeStringUtil.isIntegerEmpty(forIdDroit)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NO_DROIT"));
                criteres.append(forIdDroit);
            }

            tblTotauxGenre.addCell(criteres.toString());
            tblTotauxGenre.addRow();
            tblTotauxGenre.addRow();

            super._addTable(tblTotauxGenre);
        } catch (FWIException e) {
            e.printStackTrace();
        }

        // colonnes
        this._addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_DETAIL_ASSURE"), 30);
        this._addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_PERIODE"), 13);
        this._addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_GENRE"), 13);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_JOURS_SOLDES"), 8);
        this._addColumnRight(" ", 1);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_MONTANT_JOURNALIER"), 10);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_MONTANT_BRUT"), 8);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_COTISATIONS"), 8);
        this._addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_MONTANT_NET"), 8);
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

    public void setDerniereIdPrestation(String derniereIdPrestation) {
        this.derniereIdPrestation = derniereIdPrestation;
    }

    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * setter pour l'attribut for etat
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForEtat(String string) {
        forEtat = string;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForNoLot(String forNoLot) {
        this.forNoLot = forNoLot;
    }

    public void setForTypeDroit(String forTypeDroit) {
        this.forTypeDroit = forTypeDroit;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public void setLikeNumeroAVS(String likeNumeroAVS) {
        this.likeNumeroAVS = likeNumeroAVS;
    }

    public void setLikeNumeroAVSNNSS(String likeNumeroAVSNNSS) {
        this.likeNumeroAVSNNSS = likeNumeroAVSNNSS;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
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

    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
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

        _addCell("Totaux");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell(totalMontantBrut.toStringFormat());
        _addCell(totalMontantCotisation.toStringFormat());
        _addCell(totalMontantNet.toStringFormat());

        try {
            this._addDataTableGroupRow();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        if (!montantTotalAllocationsListe.isZero()) {
            _addCell("");
            _addCell(getSession().getLabel("JSP_LISTE_PRESTATION_ALLOCATION_EXPLOITATION"));
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell(montantTotalAllocationsListe.toStringFormat());
            _addCell("");
            _addCell("");

            try {
                this._addDataTableRow();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        if (!montantTotalFraisGardeListe.isZero()) {
            _addCell("");
            _addCell(getSession().getLabel("JSP_LISTE_PRESTATION_FRAIS_DE_GARDE"));
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell(montantTotalFraisGardeListe.toStringFormat());
            _addCell("");
            _addCell("");

            try {
                this._addDataTableRow();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        if (nombreTotalCas > 0) {
            _addCell(getSession().getLabel("JSP_LISTE_PRESTATION_NOMBRE_DE_CAS"));
            _addCell(String.valueOf(nombreTotalCas));
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");
            _addCell("");

            try {
                this._addDataTableRow();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

    }

}
