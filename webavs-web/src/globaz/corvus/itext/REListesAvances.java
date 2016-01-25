package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.avances.IREAvances;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.avances.REAvance;
import globaz.corvus.db.avances.REAvanceJointTiers;
import globaz.corvus.db.avances.REAvanceManager;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.TreeMap;
import com.lowagie.text.DocumentException;

public class REListesAvances extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String datePaiement = "";

    public REListesAvances() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des avances", REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    private void _addColumnLeft(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.LEFT);
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
            // on ajoute au doc info le numéro de référence inforom
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_AVANCES_RENTES);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LISTE_AVANCES"));

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, toString());
            abort();
        }
    }

    @Override
    protected final void _bindDataTable() throws FWIException {
        try {
            _setDataTableModel();
            populate();
        } catch (Exception e) {
            if (e instanceof FWIException) {
                throw (FWIException) e;
            } else {
                throw new FWIException(e);
            }
        }
    }

    private void ajoutLigne(REAvanceJointTiers avTiers, boolean isMensuel, boolean isUnique) throws Exception {

        _addCell(avTiers.getNss());
        _addCell(avTiers.getNom() + " " + avTiers.getPrenom());
        _addCell(avTiers.getDateNaissance());

        // Recherche de la demande
        REDemandeRenteJointDemandeManager demMgr = new REDemandeRenteJointDemandeManager();
        demMgr.setSession(getSession());
        demMgr.setForIdTiersRequ(avTiers.getAvance().getIdTiersBeneficiaire());
        demMgr.setForCSEtatDemandeNotIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE + ","
                + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE + "," + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE
                + "," + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
        demMgr.find(getTransaction());

        if (demMgr.isEmpty()) {
            demMgr.setForCsEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
            demMgr.setForCSEtatDemandeNotIn(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE + ","
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE + ","
                    + IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
            demMgr.find(getTransaction());
            if (demMgr.isEmpty()) {
                _addCell(getSession().getLabel("LISTE_AVANCE_ERREUR_DEMANDE_INTROUVABLE"));
            } else {
                REDemandeRenteJointDemande demande = (REDemandeRenteJointDemande) demMgr.getFirstEntity();
                _addCell(getSession().getCodeLibelle(demande.getCsTypeDemande()));
            }
        } else {
            REDemandeRenteJointDemande demande = (REDemandeRenteJointDemande) demMgr.getFirstEntity();
            _addCell(getSession().getCodeLibelle(demande.getCsTypeDemande()));
        }

        if (isUnique) {
            _addCell(avTiers.getAvance().getDateDebutPmt1erAcompte());
            _addCell(avTiers.getAvance().getMontant1erAcompte());

            _addCell("");
            _addCell("");
        }

        if (isMensuel) {
            _addCell("");
            _addCell("");

            _addCell(avTiers.getAvance().getDateDebutAcompte());
            _addCell(avTiers.getAvance().getMontantMensuel());
        }

        this._addDataTableRow();
    }

    private void ajoutLigneTexte(String texte) throws FWIException, DocumentException {

        _addCell(texte);
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");

        this._addDataTableRow();

    }

    private void ajoutLigneTotal(FWCurrency montantAcompte, FWCurrency nbAcompte, boolean isMensuel, boolean isUnique)
            throws FWIException, DocumentException {

        // Ligne pour total
        _addCell("____________________________________________");
        _addCell("________________________________________________________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");

        this._addDataTableRow();

        // Ligne de total
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");

        if (isUnique) {
            _addCell(String.valueOf(nbAcompte.intValue()));
            _addCell(montantAcompte.toStringFormat());
            _addCell("");
            _addCell("");
        }

        if (isMensuel) {
            _addCell("");
            _addCell("");
            _addCell(String.valueOf(nbAcompte.intValue()));
            _addCell(montantAcompte.toStringFormat());
        }

        this._addDataTableRow();

    }

    private void ajoutLigneTotalFinal(FWCurrency montantAvances, FWCurrency nbAvances) throws FWIException,
            DocumentException {

        // Ligne pour total
        _addCell("____________________________________________");
        _addCell("________________________________________________________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");
        _addCell("____________________________________________");

        this._addDataTableRow();

        // Ligne de total
        _addCell(getSession().getLabel("LISTE_AVANCE_TOTAL") + " :");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");
        _addCell("");

        _addCell(String.valueOf(nbAvances.intValue()));
        _addCell(montantAvances.toStringFormat());

        this._addDataTableRow();

    }

    public String getDatePaiement() {
        return datePaiement;
    }

    protected void initializeTable() {
        _addColumnLeft(getSession().getLabel("LISTE_AVANCE_NSS"), 2);
        _addColumnLeft(getSession().getLabel("LISTE_AVANCE_NOM_PRENOM"), 4);
        _addColumnLeft(getSession().getLabel("LISTE_AVANCE_DATE_NAISSANCE"), 2);
        _addColumnLeft(getSession().getLabel("LISTE_AVANCE_TYPE_DEMANDE"), 2);
        _addColumnRight(getSession().getLabel("LISTE_AVANCE_VALEUR"), 2);
        _addColumnRight(getSession().getLabel("LISTE_AVANCE_ACOMPTE_UNIQUE"), 2);
        _addColumnRight(getSession().getLabel("LISTE_AVANCE_VALEUR"), 2);
        _addColumnRight(getSession().getLabel("LISTE_AVANCE_ACOMPTE_MENSUEL"), 2);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private void populate() throws Exception {

        TreeMap<String, REAvanceJointTiers> mapAvanceAcompteMensuel = new TreeMap<String, REAvanceJointTiers>();
        TreeMap<String, REAvanceJointTiers> mapAvanceAcompteUnique = new TreeMap<String, REAvanceJointTiers>();

        PRTiersWrapper tiers;

        /*
         * Impression par ordre alphabétique des renseignements suivants :
         * 
         * - NSS, Nom, prénom, date de naissance, type de demande, valeur, acompte, valeur, avance mensuelle
         * 
         * Avec : - Total des colonnes Acompte, Avance mensuelle et un comptage du nombre de cas.
         * 
         * Les colonnes "Valeur" concerne la date à partir de laquelle elles sont en vigueur ou valables.
         */

        // Recherche des avances et insertion dans les maps

        // Acomptes uniques
        REAvanceManager mgr = new REAvanceManager();
        mgr.setSession(getSession());
        mgr.setForCsEtat1erAcomptesDifferentDe(IREAvances.CS_ETAT_1ER_ACOMPTE_TERMINE);
        mgr.setForDateDebut1erAcompteNotZero(true);
        mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);

        // Regroupement dans les maps
        for (int i = 0; i < mgr.size(); i++) {
            REAvance avance = (REAvance) mgr.get(i);

            tiers = PRTiersHelper.getTiersParId(getSession(), avance.getIdTiersBeneficiaire());

            REAvanceJointTiers avTiers = new REAvanceJointTiers();
            avTiers.setAvance(avance);// = avance;
            avTiers.setNss(avance.getNss());// = ;

            if (tiers != null) {
                avTiers.setNom(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
                avTiers.setPrenom(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                avTiers.setDateNaissance(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
            } else {
                avTiers.setNom(getSession().getLabel("LISTE_AVANCE_ERREUR_TIERS_INTROUVABLE") + " = "
                        + avance.getIdTiersBeneficiaire());
            }

            mapAvanceAcompteUnique.put(avTiers.getNom() + avTiers.getPrenom() + avTiers.getAvance().getIdAvance(),
                    avTiers);

        }

        mgr = new REAvanceManager();
        mgr.setSession(getSession());
        mgr.setForCsEtatAcomptesDifferentDe(IREAvances.CS_ETAT_ACOMPTE_TERMINE);
        mgr.setForDateDebutAcompteMensuelNotZero(true);
        mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);

        // Acomptes mensuels
        for (int i = 0; i < mgr.size(); i++) {
            REAvance avance = (REAvance) mgr.get(i);

            tiers = PRTiersHelper.getTiersParId(getSession(), avance.getIdTiersBeneficiaire());

            REAvanceJointTiers avTiers = new REAvanceJointTiers();
            avTiers.setAvance(avance);
            avTiers.setNss(avance.getNss());

            if (tiers != null) {
                avTiers.setNom(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM));
                avTiers.setPrenom(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                avTiers.setDateNaissance(tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
            } else {
                avTiers.setNom(getSession().getLabel("LISTE_AVANCE_ERREUR_TIERS_INTROUVABLE") + " = "
                        + avance.getIdTiersBeneficiaire());
            }

            mapAvanceAcompteMensuel.put(avTiers.getNom() + avTiers.getPrenom() + avTiers.getAvance().getIdAvance(),
                    avTiers);

        }

        // Parcours des map et remplissage de la liste
        FWCurrency montantAcompteUnique = new FWCurrency();
        FWCurrency nbAcompteUnique = new FWCurrency();
        FWCurrency montantAcompteMensuel = new FWCurrency();
        FWCurrency nbAcompteMensuel = new FWCurrency();

        // Acomptes uniques
        if (mapAvanceAcompteUnique.size() > 0) {
            ajoutLigneTexte(getSession().getLabel("LISTE_AVANCE_ACOMPTE_UNIQUE") + " :");
        }

        for (String keyMapAvTiers : mapAvanceAcompteUnique.keySet()) {

            REAvanceJointTiers avTiers = mapAvanceAcompteUnique.get(keyMapAvTiers);

            ajoutLigne(avTiers, false, true);

            montantAcompteUnique.add(avTiers.getAvance().getMontant1erAcompte());
            nbAcompteUnique.add(1);

        }

        if (!nbAcompteUnique.isZero()) {
            ajoutLigneTotal(montantAcompteUnique, nbAcompteUnique, false, true);
        }

        // Acomptes mensuels
        if (mapAvanceAcompteMensuel.size() > 0) {
            ajoutLigneTexte(getSession().getLabel("LISTE_AVANCE_ACOMPTE_MENSUEL") + "  :");
        }

        for (String keyMapAvTiers : mapAvanceAcompteMensuel.keySet()) {

            REAvanceJointTiers avTiers = mapAvanceAcompteMensuel.get(keyMapAvTiers);

            ajoutLigne(avTiers, true, false);

            montantAcompteMensuel.add(avTiers.getAvance().getMontantMensuel());
            nbAcompteMensuel.add(1);

        }

        if (!nbAcompteMensuel.isZero()) {
            ajoutLigneTotal(montantAcompteMensuel, nbAcompteMensuel, true, false);
        }

        // Ligne total général
        FWCurrency nbAvances = new FWCurrency();
        nbAvances.add(nbAcompteMensuel);
        nbAvances.add(nbAcompteUnique);

        FWCurrency montantAvances = new FWCurrency();
        montantAvances.add(montantAcompteMensuel);
        montantAvances.add(montantAcompteUnique);

        if (!nbAvances.isZero()) {
            ajoutLigneTotalFinal(montantAvances, nbAvances);
        } else {
            ajoutLigneTexte(getSession().getLabel("LISTE_AVANCE_ERREUR_RIEN_A_IMPRIMER"));
        }

    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

}
