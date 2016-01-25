package globaz.ij.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAVector;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationJointLotPrononce;
import globaz.ij.db.prestations.IJPrestationJointLotPrononceManager;
import globaz.ij.vb.prestations.IJPrestationJointLotPrononceListViewBean;
import globaz.ij.vb.prestations.IJPrestationJointLotPrononceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class IJListePrestations extends FWIAbstractManagerDocumentList {

    private static final long serialVersionUID = 1L;
    private String derniereIdPrestation = null;
    private String forCsSexe = "";

    private String forDateNaissance = "";
    private String forEtat = "";
    private String forNoBaseIndemnisation = "";
    private String forNoLot = "";
    private String fromDateDebutPrononce = "";
    private String fromDatePaiement = "";
    private String likeNom = "";
    private String likeNumeroAVS = "";
    private String likeNumeroAVSNNSS = "";
    private String likePrenom = "";

    private String orderBy = "";

    private FWCurrency totalMontantBrut = new FWCurrency("0.00");
    private FWCurrency totalMontantCotisation = new FWCurrency("0.00");
    private FWCurrency totalMontantNet = new FWCurrency("0.00");

    private boolean ajouterCommunePolitique = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public boolean isAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

    @Override
    protected void bindPageHeader() throws Exception {
        _addHeaderLine(getFontCompanyName(), _getCompanyName(), null, null, getFontDate(),
                JACalendar.format(JACalendar.today()));

        _addHeaderLine(null, null, getFontDocumentTitle(), _getDocumentTitle(), null, null);

        if (ajouterCommunePolitique) {
            String user = getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()) + " : "
                    + getSession().getUserId();
            _addHeaderLine(getFontDate(), user, null, null, null, null);
        }
    }

    /**
     * Crée une nouvelle instance de la classe IJListePrestations.
     * 
     * @param session
     *            DOCUMENT ME!
     */
    public IJListePrestations(BSession session) {
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("LIST_PRST_TITLE"),
                new IJPrestationJointLotPrononceListViewBean(), "IJ");
    }

    @Override
    public void _beforeExecuteReport() {

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.LISTE_PRESTATIONS_IJ);

        IJPrestationJointLotPrononceListViewBean manager = (IJPrestationJointLotPrononceListViewBean) _getManager();
        manager.setSession(getSession());
        manager.setForCsSexe(getForCsSexe());
        manager.setForCsEtat(getForEtat());
        manager.setForDateNaissance(getForDateNaissance());
        manager.setForIdLot(getForNoLot());
        manager.setForNoBaseIndemnisation(getForNoBaseIndemnisation());
        manager.setLikeNom(getLikeNom());
        manager.setLikePrenom(getLikePrenom());
        manager.setLikeNumeroAVS(getLikeNumeroAVS());
        manager.setLikeNumeroAVSNNSS(getLikeNumeroAVSNNSS());
        manager.setFromDateDebutPrononce(getFromDateDebutPrononce());
        manager.setHasSumMontantNet(true);

        manager.setOrderBy(IJPrestationJointLotPrononce.FIELDNAME_NOM + " , "
                + IJPrestationJointLotPrononce.FIELDNAME_PRENOM + " , " + IJPrestation.FIELDNAME_DATEDEBUT + ", "
                + IJPrestation.FIELDNAME_IDPRESTATION);

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new IJPrestationJointLotPrononce());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        try {
            ajouterCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, this.getClass().getName());
        }
    }

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

    @Override
    protected void addRow(BEntity entity) throws FWIException {
        // valeurs
        IJPrestationJointLotPrononceViewBean prestation = (IJPrestationJointLotPrononceViewBean) entity;

        if (!prestation.getIdPrestation().equals(derniereIdPrestation)) {

            if (ajouterCommunePolitique) {
                _addCell(prestation.getCommunePolitique());
            }

            PRTiersWrapper tier = null;

            try {
                tier = PRTiersHelper.getTiers(getSession(), prestation.getNoAVS());
            } catch (Exception e) {
                getSession().addError("Tiers introuvable par le n° AVS");
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
            derniereIdPrestation = prestation.getIdPrestation();

            _addCell(" ");
            _addCell(new FWCurrency(prestation.getMontantBrut()).toStringFormat());

            // calcul de la cotisation
            FWCurrency cotisations = new FWCurrency(prestation.getMontantNet());
            cotisations.sub(prestation.getMontantBrut());
            _addCell(cotisations.toStringFormat());

            _addCell(new FWCurrency(prestation.getMontantNet()).toStringFormat());

            // mise a jour des totaux
            totalMontantBrut.add(prestation.getMontantBrut());
            totalMontantNet.add(prestation.getMontantNet());
            totalMontantCotisation.add(cotisations);
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

    public String getForNoBaseIndemnisation() {
        return forNoBaseIndemnisation;
    }

    public String getForNoLot() {
        return forNoLot;
    }

    public String getFromDateDebutPrononce() {
        return fromDateDebutPrononce;
    }

    public String getFromDatePaiement() {
        return fromDatePaiement;
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

    @Override
    protected void initializeTable() {

        if (ajouterCommunePolitique) {
            IJPrestationJointLotPrononceListViewBean manager = (IJPrestationJointLotPrononceListViewBean) _getManager();
            Set<String> setIdTiers = new HashSet<String>();
            Map<String, String> mapIdTiersCommunePolitique = new HashMap<String, String>();

            for (int i = 0; i < manager.size(); i++) {
                IJPrestationJointLotPrononceViewBean prestation = (IJPrestationJointLotPrononceViewBean) manager
                        .getEntity(i);
                if (!JadeStringUtil.isBlankOrZero(prestation.getIdTiers())) {
                    setIdTiers.add(prestation.getIdTiers());
                }
            }

            mapIdTiersCommunePolitique = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(), getSession());

            JAVector vectorManagerContainer = _getManager().getContainer();
            for (Object aContainerElement : vectorManagerContainer) {
                IJPrestationJointLotPrononceViewBean prestation = (IJPrestationJointLotPrononceViewBean) aContainerElement;

                String communePolitique = mapIdTiersCommunePolitique.get(prestation.getIdTiers());
                if (!JadeStringUtil.isEmpty(communePolitique)) {
                    prestation.setCommunePolitique(communePolitique);
                }
            }

            Collections.sort(vectorManagerContainer);

            this._addColumnCenter(getSession()
                    .getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 10);
        }

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

            if (!JadeStringUtil.isEmpty(fromDateDebutPrononce)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_DATE_DEBUT"));
                criteres.append(fromDateDebutPrononce);
            }

            if (!JadeStringUtil.isIntegerEmpty(forEtat)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_ETAT"));
                if (IJPrestationJointLotPrononceManager.ETAT_NON_DEFINITIF.equals(forEtat)) {
                    criteres.append(IJPrestationJointLotPrononceManager.ETAT_NON_DEFINITIF);
                } else {
                    criteres.append(getSession().getCodeLibelle(forEtat));
                }
            }

            if (!JadeStringUtil.isIntegerEmpty(forNoBaseIndemnisation)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NO_BASE_INDEMNISATION"));
                criteres.append(forNoBaseIndemnisation);
            }

            if (!JadeStringUtil.isIntegerEmpty(forNoLot)) {
                if (criteres.length() > 0) {
                    criteres.append(", ");
                }
                criteres.append(getSession().getLabel("LISTE_PRESTATIONS_CRITERES_NO_LOT"));
                criteres.append(forNoLot);
            }

            tblTotauxGenre.addCell(criteres.toString());
            tblTotauxGenre.addRow();
            tblTotauxGenre.addRow();

            super._addTable(tblTotauxGenre);
        } catch (FWIException e) {
            e.printStackTrace();
        }

        // colonnes
        _addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_DETAIL_ASSURE"), 36);
        _addColumnLeft(getSession().getLabel("LISTE_PRESTATIONS_PERIODE"), 12);
        _addColumnRight(" ", 1);
        _addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_MONTANT_BRUT"), 10);
        _addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_COTISATIONS"), 10);
        _addColumnRight(getSession().getLabel("LISTE_PRESTATIONS_MONTANT_NET"), 10);
    }

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

    public void setForNoBaseIndemnisation(String forNoLot) {
        forNoBaseIndemnisation = forNoLot;
    }

    public void setForNoLot(String forNoLot) {
        this.forNoLot = forNoLot;
    }

    public void setFromDateDebutPrononce(String fromDateDebut) {
        fromDateDebutPrononce = fromDateDebut;
    }

    public void setFromDatePaiement(String fromDatePaiement) {
        this.fromDatePaiement = fromDatePaiement;
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

    /**
     * Surcharge.
     * 
     * @throws globaz.framework.printing.itext.exception.FWIException
     * 
     * @see globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList#summary()
     **/
    @Override
    protected void summary() throws FWIException {

        _addCell(getSession().getLabel("JSP_TOTAUX"));
        if (ajouterCommunePolitique) {
            _addCell("");
        }
        _addCell("");
        _addCell("");
        _addCell(totalMontantBrut.toStringFormat());
        _addCell(totalMontantCotisation.toStringFormat());
        _addCell(totalMontantNet.toStringFormat());

        super.summary();
    }

}
