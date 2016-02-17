package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.ordresversements.REOVJointDemandeJointDecisionJointTiers;
import globaz.corvus.db.ordresversements.REOVJointDemandeJointDecisionJointTiersManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.retenues.RERetenuesJointDemandeRenteJointTiers;
import globaz.corvus.db.retenues.RERetenuesJointDemandeRenteJointTiersManager;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRDateFormater;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Cette liste, va chercher toutes les créances (créancier) et retenues de type "Impôt à la source" pour toutes les
 * demandes de rente validées ou partielles.
 * </p>
 * <p>
 * Il faut séparer les cantons, donc 1 page par canton. Si aucun canton choisi dans la liste, ça sera tous les
 * cantons...
 * </p>
 * <p>
 * Dans les retenues, le canton et le taux sont dans l'entité, mais pour les créanciers il n'y a aucune des indications,
 * on prendra donc le canton de domicile du requérant et le montant de la créance...
 * </p>
 * <p>
 * Vu la grande différence des éléments (créanciers et retenues), on va charger deux manager différents et remplir une
 * map avec comme clé le canton et ensuite on va remplir chaque map d'objet commun aux retenues et créancier.
 * </p>
 * 
 * @author HPE
 */
public class REListeImpotSource extends FWIAbstractDocumentList {

    private class RELigne implements Comparable<RELigne> {
        public String canton = "";
        public String datePourTir = "";
        public String montant = "";
        public String noDemandeRente = "";
        public PRTiersWrapper tiers = null;
        public String type = "";
        private String communePolitique;

        @Override
        public int compareTo(RELigne o) {

            if (getAjouterCommunePolitique()) {
                int v1 = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (v1 != 0) {
                    return v1;
                }
            }

            int compareNom = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM).compareTo(
                    o.tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
            if (compareNom != 0) {
                return compareNom;
            }

            int comparePrenom = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM).compareTo(
                    o.tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            if (comparePrenom != 0) {
                return comparePrenom;
            }

            return type.compareTo(o.type);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RELigne) {
                RELigne uneAutreLigne = (RELigne) obj;

                return canton.equals(uneAutreLigne.canton)
                        && getDescriptionTiers().equals(uneAutreLigne.getDescriptionTiers())
                        && montant.equals(uneAutreLigne.montant) && noDemandeRente.equals(uneAutreLigne.noDemandeRente)
                        && type.equals(uneAutreLigne.type) && datePourTir.equals(uneAutreLigne.datePourTir);
            }
            return false;
        }

        public String getIdTiers() {
            return tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
        }

        public String getDescriptionTiers() {
            StringBuilder detailRequerant = new StringBuilder();
            if (tiers != null) {
                detailRequerant.append(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                detailRequerant.append(" / ");
                detailRequerant.append(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
                detailRequerant.append(" ");
                detailRequerant.append(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                detailRequerant.append(" / ");
                detailRequerant.append(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                detailRequerant.append(" / ");
                detailRequerant.append(tiersFormatter.getLibelleCourtSexe(tiers
                        .getProperty(PRTiersWrapper.PROPERTY_SEXE)));
                detailRequerant.append(" / ");
                detailRequerant.append(tiersFormatter.getLibellePays(tiers
                        .getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            } else {
                detailRequerant.append("Indéfini");
            }
            return detailRequerant.toString();
        }

        @Override
        public int hashCode() {
            StringBuilder contenu = new StringBuilder();
            contenu.append(this.getClass().getName());
            contenu.append(canton);
            contenu.append(getDescriptionTiers());
            contenu.append(montant);
            contenu.append(noDemandeRente);
            contenu.append(type);
            contenu.append(datePourTir);
            return contenu.toString().hashCode();
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

    }

    private static final long serialVersionUID = 1L;
    private static final String limiteInferieurMois = "01.01.2000";
    private static final String limiteSuperieurMois = "01.12.2099";
    private String canton = "";
    private String moisDebut = "";
    private String moisFin = "";
    private Map<String, Set<RELigne>> map = new HashMap<String, Set<REListeImpotSource.RELigne>>();
    private RETiersForJspUtils tiersFormatter;
    private boolean ajouterCommunePolitique;
    private Map<String, String> mapCommuneParIdTiers;

    public REListeImpotSource() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des impôts à la source", REApplication.DEFAULT_APPLICATION_CORVUS);
        tiersFormatter = RETiersForJspUtils.getInstance(getSession());
    }

    private void _addColumnLeft(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.LEFT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    private void _addColumnCenter(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.CENTER);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    private void _addColumnRight(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.RIGHT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    @Override
    public void _beforeExecuteReport() {
        try {
            Set<String> setIdTiers = new HashSet<String>();

            // on ajoute au doc info le numéro de référence inforom
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_IMPOTS_A_LA_SOURCE);

            // Création du tableau du document
            initializeTable();

            // Chargement de tous les créanciers pour toutes les demandes
            // validées ou partielles de type "Impôt Source"
            // --> Si canton != empty, prendre seulement pour un canton
            REOVJointDemandeJointDecisionJointTiersManager ovMgr = new REOVJointDemandeJointDecisionJointTiersManager();
            ovMgr.setSession(getSession());
            ovMgr.setForCsEtatDemandeRenteIN(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE + ","
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
            ovMgr.setForCsTypeOV(IREOrdresVersements.CS_TYPE_IMPOT_SOURCE);
            ovMgr.setForDateDebut((new JADate("01" + getMoisDebut()).toStrAMJ()));
            ovMgr.setForDateFin((new JADate("31" + getMoisFin()).toStrAMJ()));
            ovMgr.setForIdCanton(getCanton());
            ovMgr.find(BManager.SIZE_NOLIMIT);

            // Parcourir le manager pour insérer dans la map les objets qui
            // seront utilisés pour la création de la liste
            for (int i = 0; i < ovMgr.size(); i++) {
                REOVJointDemandeJointDecisionJointTiers ov = (REOVJointDemandeJointDecisionJointTiers) ovMgr.get(i);

                RELigne ligne = new RELigne();
                ligne.noDemandeRente = ov.getIdDemandeRente();
                ligne.type = getSession().getLabel("LISTE_IMS_DECISION_DU") + ov.getDateValidationDecision();
                ligne.datePourTir = ov.getDateValidationDecision();
                ligne.tiers = PRTiersHelper.getPersonneAVS(getSession(), ov.getIdTiersBeneficiaire());
                ligne.montant = ov.getMontant();
                ligne.canton = getSession().getCodeLibelle(ov.getIdCanton());

                Set<RELigne> liste = null;

                if (map.containsKey(ov.getIdCanton())) {
                    liste = map.get(ov.getIdCanton());
                } else {
                    liste = new HashSet<REListeImpotSource.RELigne>();
                }

                liste.add(ligne);
                setIdTiers.add(ligne.getIdTiers());
                map.put(ov.getIdCanton(), liste);

            }

            // Chargement de toutes les retenues pour toutes les demandes
            // validées ou partielles de type "Impôt Source"
            // --> Si canton != empty, prendre seulement pour un canton

            // !! ATTENTION !!
            // --> Si plusieurs mois dans la période, il faut afficher 1 ligne
            // par période, donc, on recherche pour chaque mois

            int nbMoisPeriode = PRDateFormater.nbrMoisEntreDates(new JADate(getMoisDebut()), new JADate(getMoisFin()));

            if (nbMoisPeriode <= 0) {
                nbMoisPeriode = 1;
            }

            List<RERetenuesJointDemandeRenteJointTiers> toutesRetenuesMgr = new ArrayList<RERetenuesJointDemandeRenteJointTiers>();
            JACalendar cal = new JACalendarGregorian();

            String mois = getMoisDebut();

            for (int i = 0; i < nbMoisPeriode; i++) {

                RERetenuesJointDemandeRenteJointTiersManager retenuesMgr = new RERetenuesJointDemandeRenteJointTiersManager();
                retenuesMgr.setSession(getSession());
                retenuesMgr.setForCsEtatDemandeRenteIN(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE + ","
                        + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
                retenuesMgr.setForCsTypeRetenues(IRERetenues.CS_TYPE_IMPOT_SOURCE);
                retenuesMgr.setForDateDebut(mois);
                retenuesMgr.setForDateFin(mois);
                retenuesMgr.setForIdCanton(getCanton());
                retenuesMgr.find(BManager.SIZE_NOLIMIT);

                for (int j = 0; j < retenuesMgr.size(); j++) {
                    RERetenuesJointDemandeRenteJointTiers retenues = (RERetenuesJointDemandeRenteJointTiers) retenuesMgr
                            .get(j);

                    retenues.setType(getSession().getLabel("LISTE_IMS_PMT_DU") + mois);
                    retenues.setMois(mois);

                    toutesRetenuesMgr.add(retenues);

                }

                mois = PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(cal.addMonths(mois, 1));

            }

            // Parcourir le manager pour insérer dans la map les objets qui
            // seront utilisés pour la création de la liste
            for (Iterator<RERetenuesJointDemandeRenteJointTiers> iterator = toutesRetenuesMgr.iterator(); iterator
                    .hasNext();) {
                RERetenuesJointDemandeRenteJointTiers retenues = iterator.next();

                RELigne ligne = new RELigne();
                ligne.noDemandeRente = retenues.getIdDemandeRente();
                ligne.type = retenues.getType();
                ligne.tiers = PRTiersHelper.getPersonneAVS(getSession(), retenues.getIdTiers());
                ligne.montant = getMontantRetenuMensuelSpecial(retenues);
                ligne.canton = getSession().getCodeLibelle(retenues.getCantonImposition());

                Set<RELigne> liste = null;

                if (map.containsKey(retenues.getCantonImposition())) {
                    liste = map.get(retenues.getCantonImposition());
                } else {
                    liste = new HashSet<REListeImpotSource.RELigne>();
                }

                liste.add(ligne);
                setIdTiers.add(ligne.getIdTiers());
                map.put(retenues.getCantonImposition(), liste);
            }

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

            if (getAjouterCommunePolitique()) {
                fillCommunePolitiques(setIdTiers);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("LISTE_IMS_ERROR_TITRE"));
            abort();
        }

    }

    private void fillCommunePolitiques(Set<String> setIdTiers) throws ParseException {
        String finalDate = JadeDateUtil.getLastDateOfMonth("01." + moisFin);
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(finalDate);
        mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date, getSession());
    }

    /**
     * Crée les lignes du document.
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

        // BZ 4785
        validerDates();
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    /**
     * Complète les dates de type MM.AAAA en DD.MM.AAAA (en y appondant 01.<i>xx.xxxx</i>)
     * 
     * @param date
     *            la date à compléter
     * @return la date mise en forme, si celle passée en entré est valide (MM.AAAA ou DD.MM.AAAA), <br/>
     *         sinon <code>null</code>
     */
    protected String formatDateIfNecessary(String date) {
        StringBuilder formatedDate = new StringBuilder();

        if (date.matches("\\d\\d\\W\\d\\d\\d\\d")) {
            formatedDate.append("01.").append(date);
        } else if (date.matches("\\d\\d\\W\\d\\d\\W\\d\\d\\d\\d")) {
            formatedDate.append(date);
        } else {
            return null;
        }

        return formatedDate.toString();
    }

    public String getCanton() {
        return canton;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_OBJET_EMAIL");
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
    }

    public String getMontantRetenuMensuelSpecial(RERetenuesJointDemandeRenteJointTiers retenues) throws Exception {

        String montantRA = "";

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(retenues.getIdRenteAccordee());
        ra.retrieve();

        montantRA = ra.getMontantPrestation();

        // Voir si le montant déjà retenu atteind le montant total à retenir ou
        // si le montant à retenir n'est pas plus grand que la différence des 2
        // premiers.

        FWCurrency montantARetenir = new FWCurrency(retenues.getMontantRetenuMensuel());
        FWCurrency montantTotalARetenir = new FWCurrency(retenues.getMontantTotalARetenir());
        FWCurrency montantDejaRetenu = new FWCurrency(retenues.getMontantDejaRetenu());

        // si sur adresse paiement

        if (retenues.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_ADRESSE_PMT)
                || retenues.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_COMPTE_SPECIAL)
                || retenues.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_FUTURE)
                || retenues.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_FACTURE_EXISTANTE)) {

            if (montantDejaRetenu.floatValue() >= montantTotalARetenir.floatValue()) {
                return "0.00";
            } else if (montantARetenir.floatValue() > (montantTotalARetenir.floatValue() - montantDejaRetenu
                    .floatValue())) {
                return new FWCurrency(montantTotalARetenir.floatValue() - montantDejaRetenu.floatValue()).toString();
            } else {
                return montantARetenir.toString();
            }

        } else if (retenues.getCsTypeRetenue().equals(IRERetenues.CS_TYPE_IMPOT_SOURCE)) {

            FWCurrency montantImpoSource = null;

            if (!JadeStringUtil.isDecimalEmpty(retenues.getTauxImposition())) {
                montantImpoSource = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                        * (new FWCurrency(retenues.getTauxImposition())).floatValue());
                montantImpoSource.round(FWCurrency.ROUND_ENTIER);
            } else if (!JadeStringUtil.isDecimalEmpty(retenues.getMontantRetenuMensuel())) {

                montantImpoSource = new FWCurrency(retenues.getMontantRetenuMensuel());

            } else {

                // recherche du taux
                PRTauxImpositionManager tManager = new PRTauxImpositionManager();
                tManager.setSession(getSession());
                tManager.setForCsCanton(retenues.getCantonImposition());
                tManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);

                JADate date = new JADate(retenues.getMois());

                JACalendar cal = new JACalendarGregorian();
                int last = cal.daysInMonth(date.getMonth(), date.getYear());

                String mois = String.valueOf(date.getMonth());
                if (mois.length() < 2) {
                    mois = "0" + mois;
                }

                String dateFin = String.valueOf(last) + "." + mois + "." + date.getYear();
                String dateDebut = "01" + "." + mois + "." + date.getYear();

                tManager.setForPeriode(dateDebut, dateFin);

                try {
                    tManager.find();

                    if (tManager.size() > 0) {
                        PRTauxImposition taux = (PRTauxImposition) tManager.getFirstEntity();

                        montantImpoSource = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                                * (new FWCurrency(taux.getTaux())).floatValue());
                        montantImpoSource.round(FWCurrency.ROUND_ENTIER);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    montantImpoSource = new FWCurrency("0.00");
                }

                return montantImpoSource.toString();
            }

            return montantImpoSource.toString();

        } else {
            return "";
        }

    }

    /**
     * Initialisation des colonnes et des groupes
     */
    protected void initializeTable() {
        // creation des colonnes du modele de table
        if (getAjouterCommunePolitique()) {
            _addColumnCenter(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 2);
        }
        _addColumnLeft(getSession().getLabel("LISTE_IMS_DETAIL_REQUERANT"), 6);
        _addColumnLeft(getSession().getLabel("LISTE_IMS_INFORMATION"), 3);
        _addColumnRight(getSession().getLabel("LISTE_IMS_MONTANT"), 1);
        _addColumnLeft(" ", 1);
        _addColumnLeft(getSession().getLabel("LISTE_IMS_CANTON"), 1);
        _addColumnRight(getSession().getLabel("LISTE_IMS_NO_DEMANDE"), 1);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Remplit les lignes de ce document.
     */
    private void populate() throws Exception {

        Set<String> set = map.keySet();

        if (set.isEmpty()) {
            getMemoryLog().logMessage(getSession().getLabel("LISTE_IMS_ERROR"), FWMessage.AVERTISSEMENT,
                    getSession().getLabel("LISTE_IMS_ERROR_TITRE"));
            abort();
        } else {

            for (String key : map.keySet()) {
                List<RELigne> liste = new ArrayList<RELigne>(map.get(key));
                if (getAjouterCommunePolitique()) {
                    for (RELigne ligne : liste) {
                        ligne.setCommunePolitique(mapCommuneParIdTiers.get(ligne.getIdTiers()));
                    }
                }
                Collections.sort(liste);

                String canton = "Aucun";

                if (!liste.isEmpty()) {
                    canton = liste.iterator().next().canton;
                }

                this._addLine(getSession().getLabel("LISTE_IMS_PERIODE_DE") + getMoisDebut() + " "
                        + getSession().getLabel("LISTE_IMS_PERIODE_AU") + " " + getMoisFin() + " "
                        + getSession().getLabel("LISTE_IMS_TITRE_CANTON") + canton, "", "");

                if (getAjouterCommunePolitique()) {
                    this._addLine(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey())
                            + " : " + getSession().getUserId(), "", "");
                }
                FWCurrency montantTotal = new FWCurrency();

                for (Iterator<RELigne> iterator2 = liste.iterator(); iterator2.hasNext();) {
                    RELigne ligne = iterator2.next();
                    if (getAjouterCommunePolitique()) {
                        _addCell(mapCommuneParIdTiers.get(ligne.getIdTiers()));
                    }
                    _addCell(ligne.getDescriptionTiers());
                    _addCell(ligne.type);
                    _addCell(ligne.montant);
                    _addCell(" ");
                    _addCell(ligne.canton);
                    _addCell(ligne.noDemandeRente);

                    this._addDataTableRow();

                    montantTotal.add(ligne.montant);
                    canton = ligne.canton;

                }
                if (getAjouterCommunePolitique()) {
                    _addCell("_________________________");
                    _addCell("________________________________________________________________________________________________");
                    _addCell("____________________________________________________________________________________");
                } else {
                    _addCell("______________________________________________________________________________________________________");
                    _addCell("_________________________________________________________________________________________________");
                }

                _addCell("");
                _addCell("");
                _addCell("");
                _addCell("");
                this._addDataTableRow();

                _addCell(getSession().getLabel("LISTE_IMS_MONTANT_TOTAL"));
                if (getAjouterCommunePolitique()) {
                    _addCell("");
                }
                _addCell("");
                _addCell(montantTotal.toStringFormat());
                _addCell("");
                _addCell("");
                _addCell("");
                this._addDataTableRow();

                _addPageBreak();

            }
        }
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    public final boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public final void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }

    /**
     * <p>
     * <i>Pour le BZ 4785</i><br/>
     * Vérification des dates (mois) de début et de fin de la période pour la génération de la liste.
     * </p>
     * 
     * @return <code>true</code> si la date de début est plus éloignée dans le temps que celle de fin, <br/>
     *         <code>false</code> si une des date est invalide, ou si celle de fin est plus éloignée dans le temps que
     *         celle de début
     * @author PBA
     */
    protected boolean validerDates() {
        if ((getMoisDebut() == null) || (getMoisDebut() == "") || (getMoisFin() == null) || (getMoisFin() == "")) {
            return false;
        }

        String dateDebut = formatDateIfNecessary(getMoisDebut());
        String dateFin = formatDateIfNecessary(getMoisFin());

        if ((dateDebut == null) || (dateFin == null)) {
            return false;
        }

        if (validerPlageDate(dateDebut)) {
            if (validerPlageDate(dateFin)) {
                if (JadeDateUtil.isDateBefore(dateDebut, dateFin) || JadeDateUtil.areDatesEquals(dateDebut, dateFin)) {
                    // OK
                } else {
                    this._addError(getSession().getLabel("ERR_LIS_DATES_INVALIDE"));
                    return false;
                }
            } else {
                this._addError(getSession().getLabel("ERR_LIS_DATE_FIN_INVALIDE"));
                return false;
            }
        } else {
            this._addError(getSession().getLabel("ERR_LIS_DATE_DEBUT_INVALIDE"));
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Vérifie si la date, donnée en paramètre dans une <code>String</code>, se situe entre le 01.01.2000 et le
     * 31.12.2099 <br/>
     * </p>
     * 
     * @param date
     *            une date au format <code>jj.mm.AAAA</code>
     * @return <code>true</code> si la date est comprise entre le 01.01.2000 et le 31.12.2099, sinon <code>false</code>
     * @author PBA
     */
    protected boolean validerPlageDate(String date) {
        String formatedDate = formatDateIfNecessary(date);
        if ((JadeDateUtil.isDateAfter(formatedDate, REListeImpotSource.limiteInferieurMois) || JadeDateUtil
                .areDatesEquals(formatedDate, REListeImpotSource.limiteInferieurMois))
                && (JadeDateUtil.isDateBefore(formatedDate, REListeImpotSource.limiteSuperieurMois) || JadeDateUtil
                        .areDatesEquals(formatedDate, REListeImpotSource.limiteSuperieurMois))) {
            return true;
        }
        return false;
    }
}
