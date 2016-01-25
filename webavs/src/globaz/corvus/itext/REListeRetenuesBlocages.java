package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.REPrestationBloquee;
import globaz.corvus.db.adaptation.REPrestationBloqueeManager;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordee;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordeeManager;
import globaz.corvus.process.REGenererListesVerificationProcess;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.dynamique.FWIDocumentTable;
import globaz.framework.printing.itext.dynamique.FWITableModel;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRDateFormater;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.lowagie.text.DocumentException;

/**
 * @author HPE
 */
public class REListeRetenuesBlocages extends FWIAbstractDocumentList {

    private class POJO implements Comparable<POJO> {

        private String idTiers;
        private String prenom;
        private String nom;
        private String idPourTri;
        private String noAvsBeneficiaire;
        private String dateNaissance;
        private String csSexe;
        private String csPays;
        private String dateDebutDroit;
        private String dateEcheanceDroit;
        private String codePrestation;
        private FWCurrency montantPrestation;
        private String communePolitique;

        private RERetenuesJointPrestationAccordee retenue;

        public final String getIdTiers() {
            return idTiers;
        }

        public final void setIdTiers(String idTiers) {
            this.idTiers = idTiers;
        }

        public final String getPrenom() {
            return prenom;
        }

        public final void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public final String getNom() {
            return nom;
        }

        public final void setNom(String nom) {
            this.nom = nom;
        }

        public final String getIdPourTri() {
            return idPourTri;
        }

        public final void setIdPourTri(String idPrestationAccordee) {
            idPourTri = idPrestationAccordee;
        }

        public final String getNoAvsBeneficiaire() {
            return noAvsBeneficiaire;
        }

        public final void setNoAvsBeneficiaire(String noAvsBeneficiaire) {
            this.noAvsBeneficiaire = noAvsBeneficiaire;
        }

        public final String getDateNaissance() {
            return dateNaissance;
        }

        public final void setDateNaissance(String dateNaissance) {
            this.dateNaissance = dateNaissance;
        }

        public final String getCsSexe() {
            return csSexe;
        }

        public final void setCsSexe(String csSexe) {
            this.csSexe = csSexe;
        }

        public final String getCsPays() {
            return csPays;
        }

        public final void setCsPays(String csPays) {
            this.csPays = csPays;
        }

        public final String getDateDebutDroit() {
            return dateDebutDroit;
        }

        public final void setDateDebutDroit(String dateDebutDroit) {
            this.dateDebutDroit = dateDebutDroit;
        }

        public final String getDateEcheanceDroit() {
            return dateEcheanceDroit;
        }

        public final void setDateEcheanceDroit(String dateEcheanceDroit) {
            this.dateEcheanceDroit = dateEcheanceDroit;
        }

        public final String getCodePrestation() {
            return codePrestation;
        }

        public final void setCodePrestation(String codePrestation) {
            this.codePrestation = codePrestation;
        }

        public final FWCurrency getMontantPrestation() {
            return montantPrestation;
        }

        public final void setMontantPrestation(FWCurrency montantPrestation) {
            this.montantPrestation = montantPrestation;
        }

        public final RERetenuesJointPrestationAccordee getRetenue() {
            return retenue;
        }

        public final void setRetenue(RERetenuesJointPrestationAccordee retenue) {
            this.retenue = retenue;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        public String getDetailsAssure() {
            StringBuilder assure = new StringBuilder();
            assure.append(getNoAvsBeneficiaire()).append(" / ");
            assure.append(getNom()).append(" ").append(getPrenom()).append(" / ");
            assure.append(getDateNaissance()).append(" / ");
            assure.append(getLibelleCourtSexe(getCsSexe())).append(" / ");
            assure.append(getLibellePays(getCsPays()));
            return assure.toString();
        }

        @Override
        public int compareTo(POJO o) {
            int result = 0;
            if (getAjouterCommunePolitique()) {
                result = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (result != 0) {
                    return result;
                }
            }
            result = getNom().compareTo(o.getNom());
            if (result != 0) {
                return result;
            }
            result = getPrenom().compareTo(o.getPrenom());
            if (result != 0) {
                return result;
            }
            result = getCodePrestation().compareTo(o.getCodePrestation());
            if (result != 0) {
                return result;
            }
            return getIdPourTri().compareTo(o.getIdPourTri());
        }
    }

    private static final long serialVersionUID = 1L;

    private List<POJO> mapAPIAI;
    private List<POJO> mapAPIAVS;
    private List<POJO> mapPCAI;
    private List<POJO> mapPCAVS;
    private List<POJO> mapREOAI;
    private List<POJO> mapREOAVS;
    private List<POJO> mapRFMAI;
    private List<POJO> mapRFMAVS;
    private List<POJO> mapROAI;
    private List<POJO> mapROAVS;

    private String mois = null;
    private final FWCurrency totalRentesBloquees = new FWCurrency("0.00");
    private final Map<String, FWCurrency> totauxMap = new HashMap<String, FWCurrency>();
    private boolean ajouterCommunePolitique;

    public REListeRetenuesBlocages(final BSession session) {
        super(session, REApplication.APPLICATION_CORVUS_REP, "GLOBAZ", session.getLabel("LISTE_RET_NOM_DOC"),
                REApplication.DEFAULT_APPLICATION_CORVUS);

        miseAZeroMap();
    }

    private void __populate() throws Exception {

        // Blocages
        REPrestationBloqueeManager raManager = new REPrestationBloqueeManager();
        raManager.setSession(getSession());
        raManager.setForPrestatonEnCoursDansMois(getMois());
        raManager.setSeulementPrestationBloquee(true);
        raManager.find(BManager.SIZE_NOLIMIT);

        POJO pojo = null;
        Set<String> setIdTiers = new HashSet<String>();
        List<POJO> pojos = new ArrayList<REListeRetenuesBlocages.POJO>();

        // Ajout dans MAPS
        for (int i = 0; i < raManager.size(); i++) {
            REPrestationBloquee ra = (REPrestationBloquee) raManager.get(i);
            setIdTiers.add(ra.getIdTiersBeneficiaire());

            pojo = new POJO();
            pojo.setIdTiers(ra.getIdTiersBeneficiaire());
            pojo.setNom(ra.getNomBeneficiaire());
            pojo.setPrenom(ra.getPrenomBeneficiaire());
            pojo.setCodePrestation(ra.getCodePrestation());
            pojo.setCsPays(ra.getIdPaysBeneficiaire());
            pojo.setCsSexe(ra.getCsSexeBeneficiaire());
            pojo.setDateDebutDroit(ra.getDateDebutDroit());
            pojo.setDateEcheanceDroit(ra.getDateEcheance());
            pojo.setDateNaissance(ra.getDateNaissanceBeneficiaire());
            pojo.setIdPourTri(ra.getIdPrestationAccordee());
            pojo.setMontantPrestation(new FWCurrency(ra.getMontant()));
            pojo.setNoAvsBeneficiaire(ra.getNumeroAvsBeneficiaire());

            pojos.add(pojo);
        }

        // Ajout de la commune politique aux pojos
        if (getAjouterCommunePolitique()) {
            String finalDate = JadeDateUtil.getLastDateOfMonth("01." + mois);
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(finalDate);
            Map<String, String> communeParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date, getSession());
            for (POJO p : pojos) {
                p.setCommunePolitique(communeParIdTiers.get(p.getIdTiers()));
            }
        }
        // Tri dans les différentes listes
        for (POJO p : pojos) {
            triMap(p);
        }

        // Création du tableau
        FWIDocumentTable tblBlocages = creerTableauBlocages();
        // Insertion du tableau
        _addTable(tblBlocages);

        // Fin du traitement des blocages --> réinitialise
        miseAZeroMap();
        setIdTiers = new HashSet<String>();
        pojos = new ArrayList<REListeRetenuesBlocages.POJO>();
        this._addLine("", "", "");

        // Retenues
        RERetenuesJointPrestationAccordeeManager manager = new RERetenuesJointPrestationAccordeeManager();
        manager.setSession(getSession());
        manager.setForEnCoursAtDate(getMois());
        manager.setForRenteEnCoursAtDate(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getMois()));
        manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_VALIDE);
        manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_PARTIEL);
        manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_DIMINUE);
        manager.setForIsPrestationBloquee(Boolean.FALSE);
        manager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            RERetenuesJointPrestationAccordee retenue = (RERetenuesJointPrestationAccordee) manager.get(i);
            setIdTiers.add(retenue.getIdTiersBeneficiaireRA());

            pojo = new POJO();
            pojo.setIdTiers(retenue.getIdTiersBeneficiaireRA());
            pojo.setNom(retenue.getNom());
            pojo.setPrenom(retenue.getPrenom());
            pojo.setCodePrestation(retenue.getCodePrestation());
            pojo.setCsPays(retenue.getCsNationalite());
            pojo.setCsSexe(retenue.getCsSexe());
            pojo.setDateDebutDroit(retenue.getDateDebutRetenue()); // TODO VALIDER
            pojo.setDateEcheanceDroit(retenue.getDateFinRetenue()); // TODO VALIDER
            pojo.setDateNaissance(retenue.getDateNaissance());
            pojo.setIdPourTri(retenue.getIdRetenue());
            pojo.setNoAvsBeneficiaire(retenue.getNss());
            pojo.setRetenue(retenue);

            pojos.add(pojo);
            // String key = ret.getNom() + ret.getPrenom() + ret.getCodePrestation() + ret.getIdRetenue();
        }

        // Ajout de la commune politique aux pojos
        if (getAjouterCommunePolitique()) {
            String finalDate = JadeDateUtil.getLastDateOfMonth("01." + mois);
            Date date = new SimpleDateFormat("dd.MM.yyyy").parse(finalDate);
            Map<String, String> communeParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date, getSession());
            for (POJO p : pojos) {
                p.setCommunePolitique(communeParIdTiers.get(p.getIdTiers()));
            }
        }

        for (POJO p : pojos) {
            triMap(p);
        }

        // Création du tableau
        FWIDocumentTable tblRetenues = creerTableauRetenues();
        // Insertion du tableau
        _addTable(tblRetenues);

        FWIDocumentTable tblTotauxGenre = new FWIDocumentTable();
        tblTotauxGenre.addColumn(getSession().getLabel("LISTE_RET_TYPE_RETENUE"), FWITableModel.LEFT, 10);
        tblTotauxGenre.addColumn(getSession().getLabel("LISTE_RET_MONTANT_TOTAL"), FWITableModel.RIGHT, 1);
        tblTotauxGenre.endTableDefinition();

        // Construction des totaux des retenues par genre
        FWCurrency total = new FWCurrency("0.0");
        for (String key : totauxMap.keySet()) {
            // les retenues sur adresse de paiement ne sont pas prises dans le
            // total des retenues et ne sont pas affichées dans le récapitulatif par genre
            if (!IRERetenues.CS_TYPE_ADRESSE_PMT.equals(key)) {
                tblTotauxGenre.addCell(getSession().getCodeLibelle(key));
                tblTotauxGenre.addCell(totauxMap.get(key).toStringFormat());
                tblTotauxGenre.addRow();

                total.add(totauxMap.get(key));
            }
        }
        tblTotauxGenre.addRow();

        // Construction du total des retenues
        FWIDocumentTable tblTotalRetenues = new FWIDocumentTable();
        tblTotalRetenues.addColumn(getSession().getLabel("LISTE_RET_TOTAL_RETENUES"), FWITableModel.LEFT, 10);
        tblTotalRetenues.addColumn(total.toStringFormat(), FWITableModel.RIGHT, 1);
        tblTotalRetenues.endTableDefinition();
        tblTotalRetenues.addRow();

        // Construction du total des retenues sur adresse de paiement
        FWIDocumentTable tblTotalRetenuesAdrPai = new FWIDocumentTable();
        tblTotalRetenuesAdrPai.addColumn(getSession().getLabel("LISTE_RET_TOTAL_RETENUES_ADRESSE_PAIEMENT"),
                FWITableModel.LEFT, 10);
        tblTotalRetenuesAdrPai.addColumn(totauxMap.get(IRERetenues.CS_TYPE_ADRESSE_PMT).toStringFormat(),
                FWITableModel.RIGHT, 1);
        tblTotalRetenuesAdrPai.endTableDefinition();
        tblTotalRetenuesAdrPai.addRow();

        // Construction total des rentes bloquées
        FWIDocumentTable tblTotalBlocages = new FWIDocumentTable();
        tblTotalBlocages.addColumn(getSession().getLabel("LISTE_RET_TOTAL_BLOCAGES"), FWITableModel.LEFT, 10);
        tblTotalBlocages.addColumn(totalRentesBloquees.toStringFormat(), FWITableModel.RIGHT, 1);
        tblTotalBlocages.endTableDefinition();
        tblTotalBlocages.addRow();

        // Construction total des montants non OPAE
        FWCurrency montantNonOPAE = new FWCurrency("0.00");
        montantNonOPAE.add(totalRentesBloquees);
        montantNonOPAE.add(total);
        FWIDocumentTable tblTotalMontantNonOPAE = new FWIDocumentTable();
        tblTotalMontantNonOPAE.addColumn(getSession().getLabel("LISTE_RET_TOTAL_MONTANTS_NON_OPAE"),
                FWITableModel.LEFT, 10);
        tblTotalMontantNonOPAE.addColumn(montantNonOPAE.toStringFormat(), FWITableModel.RIGHT, 1);
        tblTotalMontantNonOPAE.endTableDefinition();
        tblTotalMontantNonOPAE.addRow();

        // Affichage du récapitulatif
        _addPageBreak();
        _addTable(tblTotauxGenre);
        _addTable(tblTotalRetenues);
        _addTable(tblTotalBlocages);
        _addTable(tblTotalMontantNonOPAE);
        _addTable(tblTotalRetenuesAdrPai);
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
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("LISTE_RET_TITRE") + " " + getMois());

            // Ajout de l'utilisateur
            if (getAjouterCommunePolitique()) {
                FWIDocumentTable tblBlocages = new FWIDocumentTable();
                tblBlocages.addColumn(
                        getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey() + " : "),
                        FWITableModel.LEFT, 1);
                tblBlocages.addColumn(getSession().getUserId(), FWITableModel.LEFT, 1);
                tblBlocages.endTableDefinition();
            }

            initTotauxMap();

            getDocumentInfo().setDocumentProperty(REGenererListesVerificationProcess.PROPERTY_DOCUMENT_ORDER,
                    REGenererListesVerificationProcess.LISTE_RETENUES_ORDER);

            // on ajoute au doc info le numéro de référence inforom
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_RETENUES_ET_BLOCAGES);

            initializeTable();

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("LISTE_IMS_ERROR_TITRE"));
            abort();
        }
    }

    @Override
    protected final void _bindDataTable() throws FWIException {
        try {
            // ajout du modèle de table
            _setDataTableModel();
            // remplit les lignes
            __populate();
        } catch (Exception e) {
            if (e instanceof FWIException) {
                throw (FWIException) e;
            } else {
                throw new FWIException(e);
            }
        }
    }

    private void addMontant(final String type, final FWCurrency montant) {
        FWCurrency total = totauxMap.get(type);
        total.add(montant);
    }

    private void ajouterGenreBlocage(final FWIDocumentTable tblBlocages, final String label, final List<POJO> pojos,
            final FWCurrency montantTotalBlocages, final FWCurrency nbTotalBlocages) throws Exception {

        // Ordonnancement des données
        Collections.sort(pojos);
        FWCurrency nbGenreBlocages = new FWCurrency();
        FWCurrency montantGenreBlocages = new FWCurrency();
        if (getAjouterCommunePolitique()) {
            tblBlocages.addCell("");
        }
        tblBlocages.addCell(label);
        tblBlocages.addRow();

        for (POJO pojo : pojos) {
            traitementLigneBlocages(pojo, montantGenreBlocages, montantTotalBlocages, nbGenreBlocages, nbTotalBlocages,
                    tblBlocages);
        }

        ajoutLigneTotalBlocages(montantGenreBlocages, nbGenreBlocages, tblBlocages);
        montantTotalBlocages.add(montantGenreBlocages);
        nbTotalBlocages.add(nbGenreBlocages);

        _addPageBreak();
    }

    private void ajouterGenreRetenue(final FWIDocumentTable tblRetenues, final String label, final List<POJO> pojos,
            final FWCurrency montantTotalRetenues, final FWCurrency nbTotalRetenues) throws Exception {

        // Ordonnancement des données
        Collections.sort(pojos);

        FWCurrency montantGenreRetenues = new FWCurrency();
        FWCurrency nbGenreRetenues = new FWCurrency();

        if (getAjouterCommunePolitique()) {
            tblRetenues.addCell("");
        }
        tblRetenues.addCell(label);
        tblRetenues.addRow();

        for (POJO pojo : pojos) {
            traitementLigneRetenues(pojo, montantGenreRetenues, montantTotalRetenues, nbGenreRetenues, nbTotalRetenues,
                    tblRetenues);
        }

        ajoutLigneTotalRetenues(montantGenreRetenues, nbGenreRetenues, tblRetenues);

        montantTotalRetenues.add(montantGenreRetenues);
        nbTotalRetenues.add(nbGenreRetenues);

        _addPageBreak();
    }

    private void ajoutLigneTotalBlocages(final FWCurrency montantGenre, final FWCurrency nbGenre,
            final FWIDocumentTable tblBlocages) throws FWIException, DocumentException {

        tblBlocages
                .addCell("________________________________________________________________________"
                        + "_______________________________________________________________________________________________________________________________");

        tblBlocages.addCell("________________________");
        tblBlocages.addCell("________________________");
        tblBlocages.addCell("________________________");
        tblBlocages.addCell("________________________");
        if (getAjouterCommunePolitique()) {
            tblBlocages.addCell("________________________");
        }
        tblBlocages.addRow();

        tblBlocages.addCell(getSession().getLabel("LISTE_RET_NOMBRE") + " : " + String.valueOf(nbGenre.intValue()));
        if (getAjouterCommunePolitique()) {
            tblBlocages.addCell("");
        }
        tblBlocages.addCell("");
        tblBlocages.addCell("");
        tblBlocages.addCell(getSession().getLabel("LISTE_RET_MONTANT") + " : ");
        tblBlocages.addCell(montantGenre.toStringFormat());
        tblBlocages.addRow();

        tblBlocages.addCell("");
        tblBlocages.addRow();

    }

    private void ajoutLigneTotalRetenues(final FWCurrency montantGenre, final FWCurrency nbGenre,
            final FWIDocumentTable tblRetenues) throws FWIException, DocumentException {

        tblRetenues
                .addCell("________________________________________________________________________"
                        + "_______________________________________________________________________________________________________________________________");

        tblRetenues.addCell("________________________");
        tblRetenues.addCell("________________________");
        tblRetenues.addCell("________________________");
        tblRetenues.addCell("________________________");
        if (getAjouterCommunePolitique()) {
            tblRetenues.addCell("________________________");
        }
        tblRetenues.addRow();

        tblRetenues.addCell(getSession().getLabel("LISTE_RET_NOMBRE") + " : " + String.valueOf(nbGenre.intValue()));
        if (getAjouterCommunePolitique()) {
            tblRetenues.addCell("");
        }
        tblRetenues.addCell("");
        tblRetenues.addCell("");
        tblRetenues.addCell(getSession().getLabel("LISTE_RET_MONTANT") + " : ");
        tblRetenues.addCell(montantGenre.toStringFormat());
        tblRetenues.addRow();

        tblRetenues.addRow();

    }

    private FWIDocumentTable creerTableauBlocages() throws Exception {

        FWIDocumentTable tblBlocages = new FWIDocumentTable();
        if (getAjouterCommunePolitique()) {
            tblBlocages
                    .addColumn(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()),
                            FWITableModel.CENTER, 1);
        }
        tblBlocages.addColumn(getSession().getLabel("LISTE_RET_DETAIL_ASSURE"), FWITableModel.LEFT, 10);
        tblBlocages.addColumn(getSession().getLabel("LISTE_RET_DROIT_DU"), FWITableModel.LEFT, 2);
        tblBlocages.addColumn(getSession().getLabel("LISTE_RET_DATE_ECH"), FWITableModel.LEFT, 2);
        tblBlocages.addColumn(getSession().getLabel("LISTE_RET_GENRE_RENTE"), FWITableModel.RIGHT, 2);
        tblBlocages.addColumn(getSession().getLabel("LISTE_RET_MONTANT"), FWITableModel.RIGHT, 2);
        tblBlocages.endTableDefinition();

        FWCurrency montantTotalBlocages = new FWCurrency();
        FWCurrency nbTotalBlocages = new FWCurrency();

        // ROAVS
        if (!mapROAVS.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_ROAVS"), mapROAVS,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // REOAVS
        if (!mapREOAVS.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_REOAVS"), mapREOAVS,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // ROAI
        if (!mapROAI.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_ROAI"), mapROAI,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // REOAI
        if (!mapREOAI.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_REOAI"), mapREOAI,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // APIAI
        if (!mapAPIAI.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_APIAI"), mapAPIAI,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // APIAVS
        if (!mapAPIAVS.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_APIAVS"), mapAPIAVS,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // PCAVS
        if (!mapPCAVS.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_PCAVS"), mapPCAVS,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // PCAI
        if (!mapPCAI.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_PCAI"), mapPCAI,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // RFMAVS
        if (!mapRFMAVS.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_RFMAVS"), mapRFMAVS,
                    montantTotalBlocages, nbTotalBlocages);
        }

        // RFMAI
        if (!mapRFMAI.isEmpty()) {
            ajouterGenreBlocage(tblBlocages, getSession().getLabel("PROCESS_LISTE_ERR_RFMAI"), mapRFMAI,
                    montantTotalBlocages, nbTotalBlocages);
        }
        if (getAjouterCommunePolitique()) {
            this._addLine(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()) + " : "
                    + getSession().getUserId(), "", "");
        }
        this._addLine(getSession().getLabel("LISTE_RET_BLOCAGES"), "", "");

        tblBlocages.addCell("");
        tblBlocages.addRow();

        ajoutLigneTotalBlocages(montantTotalBlocages, nbTotalBlocages, tblBlocages);

        return tblBlocages;
    }

    private FWIDocumentTable creerTableauRetenues() throws Exception {
        FWIDocumentTable tblRetenues = new FWIDocumentTable();
        if (getAjouterCommunePolitique()) {
            tblRetenues
                    .addColumn(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()),
                            FWITableModel.CENTER, 1);
        }
        tblRetenues.addColumn(getSession().getLabel("LISTE_RET_DETAIL_ASSURE"), FWITableModel.LEFT, 10);
        tblRetenues.addColumn(getSession().getLabel("LISTE_RET_TYPE_RETENUE"), FWITableModel.LEFT, 2);
        tblRetenues.addColumn(getSession().getLabel("LISTE_RET_RETENUE"), FWITableModel.RIGHT, 2);
        tblRetenues.addColumn(getSession().getLabel("LISTE_RET_TOTAL"), FWITableModel.RIGHT, 2);
        tblRetenues.addColumn(getSession().getLabel("LISTE_RET_DEJA_RETENU"), FWITableModel.RIGHT, 2);
        tblRetenues.endTableDefinition();

        // Remplissage du tableau
        FWCurrency montantTotalRetenues = new FWCurrency();
        FWCurrency nbTotalRetenues = new FWCurrency();

        // Rentes ordinaires AVS
        if (!mapROAVS.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_ROAVS"), mapROAVS,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Rentes extraordinaires AVS
        if (!mapREOAVS.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_REOAVS"), mapREOAVS,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Allocations pour impotent - AVS
        if (!mapAPIAVS.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_APIAVS"), mapAPIAVS,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Rentes ordinaires AI
        if (!mapROAI.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_ROAI"), mapROAI,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Rentes extraordinaires AI
        if (!mapREOAI.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_REOAI"), mapREOAI,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Allocations pour impotent - AI
        if (!mapAPIAI.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_APIAI"), mapAPIAI,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Prestations complémentaires - AVS
        if (!mapPCAVS.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_PCAVS"), mapPCAVS,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Prestations complémentaires - AI
        if (!mapPCAI.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_PCAI"), mapPCAI,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Remboursements des frais médicaux - AVS
        if (!mapRFMAVS.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_RFMAVS"), mapRFMAVS,
                    montantTotalRetenues, nbTotalRetenues);
        }

        // Remboursements des frais médicaux - AI
        if (!mapRFMAI.isEmpty()) {
            ajouterGenreRetenue(tblRetenues, getSession().getLabel("PROCESS_LISTE_ERR_RFMAI"), mapRFMAI,
                    montantTotalRetenues, nbTotalRetenues);
        }

        this._addLine(getSession().getLabel("LISTE_RET_RETENUES"), "", "");

        tblRetenues.addCell("");
        tblRetenues.addRow();

        ajoutLigneTotalRetenues(montantTotalRetenues, nbTotalRetenues, tblRetenues);

        return tblRetenues;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_RET_TITRE");
    }

    public String getMois() {
        return mois;
    }

    protected void initializeTable() {
        if (getAjouterCommunePolitique()) {
            _addColumnLeft(getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 1);
        }
        _addColumnLeft(getSession().getLabel("LISTE_RET_DETAIL_ASSURE"), 10);
        _addColumnLeft(getSession().getLabel("LISTE_RET_TYPE_RETENUE"), 2);
        _addColumnRight(getSession().getLabel("LISTE_RET_RETENUE"), 2);
        _addColumnRight(getSession().getLabel("LISTE_RET_TOTAL"), 2);
        _addColumnRight(getSession().getLabel("LISTE_RET_DEJA_RETENU"), 2);
    }

    private void initTotauxMap() {
        totauxMap.put(IRERetenues.CS_TYPE_ADRESSE_PMT, new FWCurrency("0.0"));
        totauxMap.put(IRERetenues.CS_TYPE_COMPTE_SPECIAL, new FWCurrency("0.0"));
        totauxMap.put(IRERetenues.CS_TYPE_FACTURE_EXISTANTE, new FWCurrency("0.0"));
        totauxMap.put(IRERetenues.CS_TYPE_FACTURE_FUTURE, new FWCurrency("0.0"));
        totauxMap.put(IRERetenues.CS_TYPE_IMPOT_SOURCE, new FWCurrency("0.0"));
    }

    private void traitementLigneBlocages(final POJO pojo, final FWCurrency montantGenreBlocages,
            final FWCurrency montantTotalBlocages, final FWCurrency nbGenreBlocages, final FWCurrency nbTotalBlocages,
            final FWIDocumentTable tblBlocages) throws Exception {

        if (getAjouterCommunePolitique()) {
            tblBlocages.addCell(pojo.getCommunePolitique());
        }
        tblBlocages.addCell(pojo.getDetailsAssure());
        tblBlocages.addCell(pojo.getDateDebutDroit());
        tblBlocages.addCell(pojo.getDateEcheanceDroit());
        tblBlocages.addCell(pojo.getCodePrestation());
        tblBlocages.addCell(pojo.getMontantPrestation().toStringFormat());
        tblBlocages.addRow();

        totalRentesBloquees.add(pojo.getMontantPrestation());
        montantGenreBlocages.add(pojo.getMontantPrestation());
        nbGenreBlocages.add(1);
    }

    private void traitementLigneRetenues(final POJO pojo, final FWCurrency montantGenreRetenues,
            final FWCurrency montantTotalRetenues, final FWCurrency nbGenreRetenues, final FWCurrency nbTotalRetenues,
            final FWIDocumentTable tblRetenues) throws Exception {

        RERetenuesJointPrestationAccordee retenue = pojo.getRetenue();

        if (getAjouterCommunePolitique()) {
            tblRetenues.addCell(pojo.getCommunePolitique());
        }

        tblRetenues.addCell(pojo.getDetailsAssure());
        tblRetenues.addCell(getSession().getCodeLibelle(retenue.getCsTypeRetenue()));

        // calcul pour la "prochaine retenue"
        FWCurrency montantTotalRetenue = new FWCurrency(retenue.getMontantTotalARetenir());
        FWCurrency montantDejaRetenu = new FWCurrency(retenue.getMontantDejaRetenu());
        FWCurrency montantRetenuMensuel = new FWCurrency(retenue.getMontantRetenuMensuel());
        FWCurrency prochaineRetenue = null;

        if (IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(retenue.getCsTypeRetenue())) {

            // pour l'imposition a la source, le taux peut être donne de plusieurs manières
            if (!JadeStringUtil.isDecimalEmpty(retenue.getTauxImposition())) {

                // donne par un taux fixe
                String montantRA = retenue.getRenteAccordee().getMontantPrestation();

                montantRetenuMensuel = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                        * (new FWCurrency(retenue.getTauxImposition())).floatValue());
                montantRetenuMensuel.round(FWCurrency.ROUND_ENTIER);

            } else if (!JadeStringUtil.isDecimalEmpty(retenue.getMontantRetenuMensuel())) {

                // un montant fixe
                montantRetenuMensuel = new FWCurrency(retenue.getMontantRetenuMensuel());

            } else {

                // donne par un canton
                String montantRA = retenue.getRenteAccordee().getMontantPrestation();

                // recherche du taux
                PRTauxImpositionManager tManager = new PRTauxImpositionManager();
                tManager.setSession(getSession());
                tManager.setForCsCanton(retenue.getCantonImposition());
                tManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
                tManager.find();

                PRTauxImposition t = (PRTauxImposition) tManager.getFirstEntity();
                String taux = "0.0";
                if (t != null) {
                    taux = t.getTaux();
                }

                montantRetenuMensuel = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                        * (new FWCurrency(taux)).floatValue());
                montantRetenuMensuel.round(FWCurrency.ROUND_ENTIER);
            }

            prochaineRetenue = montantRetenuMensuel;
            montantTotalRetenue = montantRetenuMensuel;
        } else {
            FWCurrency tmp = new FWCurrency(retenue.getMontantTotalARetenir());
            tmp.sub(montantDejaRetenu);
            // si le montant de la retenue est plus grand que tmp
            if (montantRetenuMensuel.compareTo(tmp) > 0) {
                prochaineRetenue = tmp;
            } else {
                prochaineRetenue = montantRetenuMensuel;
            }
        }

        // mise a jours du montant déjà retenu
        montantDejaRetenu.add(prochaineRetenue);

        tblRetenues.addCell(prochaineRetenue.toStringFormat());
        tblRetenues.addCell(montantTotalRetenue.toStringFormat());
        tblRetenues.addCell(montantDejaRetenu.toStringFormat());
        tblRetenues.addRow();

        addMontant(retenue.getCsTypeRetenue(), prochaineRetenue);
        montantGenreRetenues.add(prochaineRetenue);
        nbGenreRetenues.add(1);
    }

    private void triMap(final POJO pojo) {
        String genrePrestation = pojo.getCodePrestation();
        if (genrePrestation.equals(REGenresPrestations.GENRE_10)
                || genrePrestation.equals(REGenresPrestations.GENRE_12)
                || genrePrestation.equals(REGenresPrestations.GENRE_13)
                || genrePrestation.equals(REGenresPrestations.GENRE_14)
                || genrePrestation.equals(REGenresPrestations.GENRE_15)
                || genrePrestation.equals(REGenresPrestations.GENRE_16)
                || genrePrestation.equals(REGenresPrestations.GENRE_33)
                || genrePrestation.equals(REGenresPrestations.GENRE_34)
                || genrePrestation.equals(REGenresPrestations.GENRE_35)
                || genrePrestation.equals(REGenresPrestations.GENRE_36)) {
            mapROAVS.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_20)
                || genrePrestation.equals(REGenresPrestations.GENRE_22)
                || genrePrestation.equals(REGenresPrestations.GENRE_23)
                || genrePrestation.equals(REGenresPrestations.GENRE_24)
                || genrePrestation.equals(REGenresPrestations.GENRE_25)
                || genrePrestation.equals(REGenresPrestations.GENRE_26)
                || genrePrestation.equals(REGenresPrestations.GENRE_43)
                || genrePrestation.equals(REGenresPrestations.GENRE_44)
                || genrePrestation.equals(REGenresPrestations.GENRE_45)
                || genrePrestation.equals(REGenresPrestations.GENRE_46)) {
            mapREOAVS.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_85)
                || genrePrestation.equals(REGenresPrestations.GENRE_86)
                || genrePrestation.equals(REGenresPrestations.GENRE_87)
                || genrePrestation.equals(REGenresPrestations.GENRE_89)
                || genrePrestation.equals(REGenresPrestations.GENRE_94)
                || genrePrestation.equals(REGenresPrestations.GENRE_95)
                || genrePrestation.equals(REGenresPrestations.GENRE_96)
                || genrePrestation.equals(REGenresPrestations.GENRE_97)) {
            mapAPIAVS.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_50)
                || genrePrestation.equals(REGenresPrestations.GENRE_52)
                || genrePrestation.equals(REGenresPrestations.GENRE_53)
                || genrePrestation.equals(REGenresPrestations.GENRE_54)
                || genrePrestation.equals(REGenresPrestations.GENRE_55)
                || genrePrestation.equals(REGenresPrestations.GENRE_56)) {
            mapROAI.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_70)
                || genrePrestation.equals(REGenresPrestations.GENRE_72)
                || genrePrestation.equals(REGenresPrestations.GENRE_73)
                || genrePrestation.equals(REGenresPrestations.GENRE_74)
                || genrePrestation.equals(REGenresPrestations.GENRE_75)
                || genrePrestation.equals(REGenresPrestations.GENRE_76)) {
            mapREOAI.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_81)
                || genrePrestation.equals(REGenresPrestations.GENRE_82)
                || genrePrestation.equals(REGenresPrestations.GENRE_83)
                || genrePrestation.equals(REGenresPrestations.GENRE_84)
                || genrePrestation.equals(REGenresPrestations.GENRE_88)
                || genrePrestation.equals(REGenresPrestations.GENRE_91)
                || genrePrestation.equals(REGenresPrestations.GENRE_92)
                || genrePrestation.equals(REGenresPrestations.GENRE_93)) {
            mapAPIAI.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_110)
                || genrePrestation.equals(REGenresPrestations.GENRE_113)) {
            mapPCAVS.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_150)) {
            mapPCAI.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_210)
                || genrePrestation.equals(REGenresPrestations.GENRE_213)) {
            mapRFMAVS.add(pojo);
        } else if (genrePrestation.equals(REGenresPrestations.GENRE_250)) {
            mapRFMAI.add(pojo);
        }
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(false);
        setSendMailOnError(false);
    }

    private String getLibelleCourtSexe(final String csSexe) {
        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    private String getLibellePays(final String csNationalite) {
        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }
    }

    private void miseAZeroMap() {
        mapAPIAI = new ArrayList<POJO>();
        mapAPIAVS = new ArrayList<POJO>();
        mapPCAI = new ArrayList<POJO>();
        mapPCAVS = new ArrayList<POJO>();
        mapREOAI = new ArrayList<POJO>();
        mapREOAVS = new ArrayList<POJO>();
        mapRFMAI = new ArrayList<POJO>();
        mapRFMAVS = new ArrayList<POJO>();
        mapROAI = new ArrayList<POJO>();
        mapROAVS = new ArrayList<POJO>();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setMois(final String mois) {
        this.mois = mois;
    }

    public boolean getAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }
}
