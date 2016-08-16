package globaz.osiris.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APISection;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreNonVerseManager;
import globaz.osiris.db.ordres.CAOrdreVersement;
import globaz.osiris.db.ordres.format.CAOrdreFormateur;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import globaz.osiris.external.IntRole;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.osiris.TITiersOSI;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.util.ArrayList;

/**
 * Insérez la description du type ici. Date de création : (07.02.2002 11:13:25)
 * 
 * @author: Administrator
 */
public class CAOperationOrdreVersement extends CAOperation implements APIOperationOrdreVersement, Cloneable,
        APICommonOdreVersement {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String LABEL_MONTANT_NEGATIF = "MONTANT_NEGATIF";

    private static final String REMBOURSEMENT_DE_COTISATIONS = "209001";

    private CAAdressePaiementFormatter adressePaiementFormatter = null;
    private CAOrdreVersement ordreVersement = new CAOrdreVersement();

    /**
     * Commentaire relatif au constructeur CAOrdreVersement.
     */
    public CAOperationOrdreVersement() {
        super();
        setIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);
    }

    public CAOperationOrdreVersement(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);
    }

    /**
     * Effectue des traitements après un ajout dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après l'ajout de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Laisser la superclasse traiter l'événement
        super._afterAdd(transaction);

        // Mettre à jour l'ordre associé
        ordreVersement.setIdOrdre(getIdOperation());
        ordreVersement.add(transaction);
        if (ordreVersement.hasErrors()) {
            _addError(transaction, getSession().getLabel("7117"));
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.02.2002 14:04:58)
     * 
     * @exception java.lang.Exception La description de l'exception.
     */
    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._afterRetrieve(transaction);

        initOrdreVersement(transaction);
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la mise à jour de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);

        // Monnaie de dépôt par défaut
        if (JadeStringUtil.isBlank(getCodeISOMonnaieDepot())) {
            setCodeISOMonnaieDepot("CHF");
        }

        // Monnaie de bonification par défaut
        if (JadeStringUtil.isBlank(getCodeISOMonnaieBonification())) {
            setCodeISOMonnaieBonification("CHF");
        }

        // Type de virement par défaut
        if (JadeStringUtil.isIntegerEmpty(getTypeVirement())) {
            setTypeVirement(APIOperationOrdreVersement.VIREMENT);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 15:17:35)
     * 
     * @exception java.lang.Exception La description de l'exception.
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Laisser la superclasse traiter l'événement
        super._beforeDelete(transaction);

        // Supprimer l'ordre de versement
        if (!isNew() && !hasErrors()) {
            ordreVersement.delete(transaction);
            if (ordreVersement.hasErrors()) {
                _addError(transaction, getSession().getLabel("7119"));
            }
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws Exception {

        // Laisser la superclasse traiter l'événement
        super._beforeUpdate(transaction);

        // Mettre à jour l'ordre associé
        ordreVersement.update(transaction);
        if (ordreVersement.isNew() || ordreVersement.hasErrors()) {
            _addError(transaction, getSession().getLabel("7120"));
        }
    }

    /**
     * Chargement des valeurs par défaut par utilisateur
     */
    protected void _synchroChgValUtili(globaz.globall.db.BTransaction transaction) {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector(11);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAOperationOrdreVersement", getNomEcran());
                // chargement des propriétés internes si idExterneRoleEcran est
                // vide

                if (JadeStringUtil.isBlank(getIdExterneRoleEcran()) && !valeurUtilisateur.isEmpty()) {
                    if (valeurUtilisateur.size() >= 1) {
                        setIdExterneRoleEcran(valeurUtilisateur.elementAt(0));
                    }
                    if (valeurUtilisateur.size() >= 2) {
                        setIdRoleEcran(valeurUtilisateur.elementAt(1));
                    }
                    if (valeurUtilisateur.size() >= 3) {
                        setIdExterneSectionEcran(valeurUtilisateur.elementAt(2));
                    }
                    if (valeurUtilisateur.size() >= 4) {
                        setIdTypeSectionEcran(valeurUtilisateur.elementAt(3));
                    }
                    if (valeurUtilisateur.size() >= 5) {
                        setTypeVirement(valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        setDate(valeurUtilisateur.elementAt(5));
                    }
                    if (valeurUtilisateur.size() >= 7) {
                        setCodeISOMonnaieBonification(valeurUtilisateur.elementAt(6));
                    }
                    if (valeurUtilisateur.size() >= 8) {
                        setNatureOrdre(valeurUtilisateur.elementAt(7));
                    }
                    if (valeurUtilisateur.size() >= 9) {
                        setIdOrganeExecution(valeurUtilisateur.elementAt(8));
                    }
                    if (valeurUtilisateur.size() >= 10) {
                        setCodeISOMonnaieDepot(valeurUtilisateur.elementAt(9));
                    }
                    if (valeurUtilisateur.size() >= 11) {
                        setMotif(valeurUtilisateur.elementAt(10));
                    }
                }
            }
        }
    }

    /**
     * mise à jour du fichier AJPPVUT pour les valeur par défaut par utilisateur
     */
    protected void _synchroValUtili(globaz.globall.db.BTransaction transaction) {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector(11);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des données à mémoriser dans le vecteur
            valeurUtilisateur.removeAllElements();
            valeurUtilisateur.add(0, getIdExterneRoleEcran());
            valeurUtilisateur.add(1, getIdRoleEcran());
            valeurUtilisateur.add(2, getIdExterneSectionEcran());
            valeurUtilisateur.add(3, getIdTypeSectionEcran());
            valeurUtilisateur.add(4, getTypeVirement());
            valeurUtilisateur.add(5, getDate());
            valeurUtilisateur.add(6, getCodeISOMonnaieBonification());
            valeurUtilisateur.add(7, getNatureOrdre());
            valeurUtilisateur.add(8, getIdOrganeExecution());
            valeurUtilisateur.add(9, getCodeISOMonnaieDepot());
            valeurUtilisateur.add(10, getMotif());
            // mise à jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAOperationOrdreVersement", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2002 08:50:10)
     * 
     * @exception java.lang.Exception La description de l'exception.
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // Vérifier le type d'opération
        if (!isInstanceOrSubClassOf(APIOperation.CAOPERATIONORDREVERSEMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("5167"));
        }

    }

    /**
     * Validation des données Date de création : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les données de la superclasse
        super._valider(transaction);

        // Contrôler le montant
        if ((getMontant() == null) || JadeStringUtil.isDecimalEmpty(getMontant())) {
            getMemoryLog().logMessage("5129", null, FWMessage.ERREUR, this.getClass().getName());
        } else {
            float _mont = 0f;
            try {
                _mont = Float.parseFloat(getMontant());
            } catch (Exception e) {
            }

            if (_mont <= 0f) {
                getMemoryLog().logMessage("5136", null, FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // Vérifier la section
        if (!isNewSection()) {
            if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
                getMemoryLog().logMessage("5125", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (getSection() == null) {
                getMemoryLog().logMessage("5126", getIdSection(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // Adresse de paiement par défaut
        setDefaultIdAdressePaiement();

        if (getCompteAnnexe() != null) {
            // Valider l'ordre de versement
            ordreVersement.setMemoryLog(getMemoryLog());
            ordreVersement._valider();
        }

    }

    /**
     * Valider l'ordre avant la génération du paiement Date de création : (08.02.2002 15:44:08)
     */
    public void _validerAvantVersement(globaz.globall.db.BTransaction transaction) {
        if (!getEtat().equals(APIOperation.ETAT_COMPTABILISE) && !getEtat().equals(APIOperation.ETAT_ERREUR_VERSEMENT)
                && !getEtat().equals(APIOperation.ETAT_VERSE)) {
            getMemoryLog().logMessage("5149", null, FWMessage.ERREUR, this.getClass().getName());
        } else {
            _valider(transaction);

            // Vérifier le numéro de transaction
            if (JadeStringUtil.isIntegerEmpty(getNumTransaction())) {
                getMemoryLog().logMessage("5150", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Vérifier l'ordre groupé
            if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
                getMemoryLog().logMessage("5151", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // Vérification de la validité de l'adresse de paiement
            try {
                if (getAdressePaiementData() != null) {
                    CAAdressePaiementFormatter.checkAdressePaiementData(getAdressePaiementData(), getSession());
                } else {
                    // On regarde si il y a une adresse de courrier, afin de
                    // faire un mandat
                    TITiersOSI tiersOsi = new TITiersOSI();
                    tiersOsi.setISession(getISession());

                    if (tiersOsi.getDataSourceAdresseCourrierForIdTiers(getCompteAnnexe().getIdTiers()) == null) {
                        getMemoryLog().logMessage("5198", null, FWMessage.ERREUR, this.getClass().getName());
                    }
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // S'il y a des erreurs
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            setEtat(APIOperation.ETAT_ERREUR_VERSEMENT);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.03.2002 09:29:02)
     * 
     * @exception java.lang.Exception La description de l'exception.
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._writeProperties(statement);

        // Traitement interne
        statement.writeField("MONTANT", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));

    }

    /**
     * Ajout de l'ordre de versement automatique. Valeur par défaut + montant = montant absolu de la section.
     * 
     * @param transaction
     * @param journal
     */
    public void addOrdreVersementAutomatique(BTransaction transaction, CASection actualSection, CAJournal journal) {
        boolean useOwnTransaction = false;

        try {
            if (APISection.ID_CATEGORIE_SECTION_RETOUR.equals(actualSection.getCategorieSection())) {
                throw new Exception(getSession().getLabel(CASection.LABEL_RETOURS_GERER_AILLEURS));
            }

            FWCurrency solde = new FWCurrency(actualSection.getSolde());
            // On ne peut pas rembourser une section avec montant positif ou
            // zéro
            if (solde.isPositive() || solde.isZero()) {
                throw new Exception(getSession().getLabel(CAOperationOrdreVersement.LABEL_MONTANT_NEGATIF));
            }

            // Ouvrir une transaction si nécessaire
            if (transaction == null) {
                transaction = new BTransaction(getSession());
                transaction.openTransaction();
                useOwnTransaction = true;
            }

            hasOrdreNonExecute(transaction, actualSection.getIdSection());

            // Si nécessaire création d'un journal d'Ecritures journalières
            // Récupérer le journal des inscriptions journalières
            if (journal == null) {
                journal = CAJournal.fetchJournalJournalier(getSession(), transaction);
            }

            // Création de l'ordre de versement
            setDate(journal.getDate());
            setIdJournal(journal.getIdJournal());

            setIdCompteAnnexe(actualSection.getCompteAnnexe().getIdCompteAnnexe());
            setIdSection(actualSection.getIdSection());

            setDefaultIdAdressePaiement();

            if (JadeStringUtil.isBlank(getIdAdressePaiement())) {
                throw new Exception(getSession().getLabel("5137"));
            }

            solde.abs();
            setMontant(solde.toString());

            setNatureOrdre(CAOperationOrdreVersement.REMBOURSEMENT_DE_COTISATIONS);

            if (IntRole.ROLE_RENTIER.equalsIgnoreCase(actualSection.getCompteAnnexe().getIdRole())) {
                setNatureOrdre(CAOrdreGroupe.NATURE_RENTES_AVS_AI);
            }

            this.add(transaction);

            // Nécessaire pour l'affichage correcte à l'écran du détail du
            // versement
            setIdExterneRoleEcran(getCompteAnnexe().getIdExterneRole());
            setIdExterneSectionEcran(actualSection.getIdExterne());
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        } finally {
            if (useOwnTransaction) {
                try {
                    if (transaction.hasErrors()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        transaction.closeTransaction();
                        transaction = null;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * Annuler le versement de l'ordre Date de création : (29.04.2002 09:04:08)
     */
    public void annulerVerser() {

        // Vérifier l'état
        if (!getEtat().equals(APIOperation.ETAT_VERSE) && !getEtat().equals(APIOperation.ETAT_ERREUR_VERSEMENT)) {
            _addError(null, getSession().getLabel("5407"));
        } else {
            setEtat(APIOperation.ETAT_COMPTABILISE);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:44:09)
     * 
     * @param oper globaz.osiris.db.comptes.CAOperationOrdreVersement
     */
    public void dupliquer(CAOperationOrdreVersement oper) {

        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);

        // Copier les autres paramètres
        if (oper != null) {
            setCodeISOMonnaieBonification(oper.getCodeISOMonnaieBonification());
            setCodeISOMonnaieDepot(oper.getCodeISOMonnaieDepot());
            setCoursConversion(oper.getCoursConversion());
            setEstBloque(oper.getEstBloque());
            setEstRetire(oper.getEstRetire());
            setIdAdressePaiement(oper.getIdAdressePaiement());
            setIdOrdreGroupe(oper.getIdOrdreGroupe());
            setIdOrganeExecution(oper.getIdOrganeExecution());
            setMontant(oper.getMontant());
            setMotif(oper.getMotif());
            setNatureOrdre(oper.getNatureOrdre());
            setReferenceBVR(oper.getReferenceBVR());
            setTypeVirement(oper.getTypeVirement());
            setValeurConversion(oper.getValeurConversion());
        }
    }

    /**
     * Utiliser pour les jsps
     * 
     * @return
     */
    public String getAdresseCourrier() {
        try {
            TITiersOSI tiersOsi = new TITiersOSI();
            tiersOsi.setISession(getISession());

            TIAdresseDataSource d = tiersOsi.getDataSourceAdresseCourrierForIdTiers(getCompteAnnexe().getIdTiers());
            String adresse = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO) + " "
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
            return adresse;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 11:10:16)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    @Override
    public IntAdressePaiement getAdressePaiement() throws Exception {
        ordreVersement.setSession(getSession());
        return ordreVersement.getAdressePaiement();
    }

    public TIAdressePaiementData getAdressePaiementData() throws Exception {
        ordreVersement.setSession(getSession());
        return ordreVersement.getAdressePaiementData();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.03.2002 14:17:07)
     * 
     * @return globaz.osiris.db.utils.CAAdressePaiementFormatter
     */
    public globaz.osiris.db.utils.CAAdressePaiementFormatter getAdressePaiementFormatter() {
        if (adressePaiementFormatter == null) {

            adressePaiementFormatter = new CAAdressePaiementFormatter();
            try {
                adressePaiementFormatter.setAdressePaiement(getAdressePaiement());
            } catch (Exception e) {
                adressePaiementFormatter.setAdressePaiement(null);
            }
        }
        return adressePaiementFormatter;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:25:37)
     * 
     * @return String
     */
    @Override
    public String getCodeISOMonnaieBonification() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getCodeISOMonnaieBonification();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:25:06)
     * 
     * @return String
     */
    @Override
    public String getCodeISOMonnaieDepot() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getCodeISOMonnaieDepot();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:27:43)
     * 
     * @return String
     */
    @Override
    public String getCoursConversion() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getCoursConversion();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMonnaies() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getCsMonnaies();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsNatureOrdres() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getCsNatureOrdres();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeVirements() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getCsTypeVirements();
    }

    @Override
    public TIAdresseDataSource getDataSourceAdresseCourrier() throws Exception {
        TITiersOSI tiersOsi = new TITiersOSI();
        tiersOsi.setISession(getISession());

        return tiersOsi.getDataSourceAdresseCourrierForIdTiers(getCompteAnnexe().getIdTiers());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:30:04)
     * 
     * @return java.lang.Boolean
     */
    @Override
    public java.lang.Boolean getEstBloque() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getEstBloque();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:33:12)
     * 
     * @return java.lang.Boolean
     */
    @Override
    public java.lang.Boolean getEstRetire() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getEstRetire();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.04.2002 13:30:32)
     * 
     * @return boolean
     */
    public boolean getEstVerse() {
        return getEtat().equals(APIOperation.ETAT_VERSE);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:22:43)
     * 
     * @return String
     */
    @Override
    public String getIdAdressePaiement() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getIdAdressePaiement();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 13:35:47)
     * 
     * @return String
     */
    @Override
    public String getIdOrdreGroupe() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getIdOrdreGroupe();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 09:16:15)
     * 
     * @return String
     */
    @Override
    public String getIdOrganeExecution() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getIdOrganeExecution();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:19:02)
     * 
     * @return String
     */
    @Override
    public String getMontant() {
        return JANumberFormatter.deQuote(montant);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (31.05.2002 08:41:11)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMontantToCurrency() {
        return new FWCurrency(getMontant());
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:28:44)
     * 
     * @return String
     */
    @Override
    public String getMotif() {
        ordreVersement.setSession(getSession());
        return removeBlankLines(ordreVersement.getMotif());
    }

    /**
     * Cette méthode retourne le motif formatté pour mettre à jour les communications pour l'OPAE S'il n'y a pas un
     * numéro AVS au début du motif, on décale tout d'une ligne, càd que la première ligne de communication sera vide et
     * la suite viendra dans la ligne de communication 2.
     * 
     * @return String
     */
    @Override
    public String getMotifFormatOPAE() {
        String motif = removeBlankLines(getMotif());
        ;
        // revue pour NSS
        if (motif.length() >= 16) {
            // Contrôler que le début du numéro ressemble au format AVS
            String d = motif.substring(0, 16);
            // Si c'est le cas on prend le motif tel quel
            if ((d.charAt(3) == '.') && (d.charAt(8) == '.')) {
                return motif;
            } else {
                return getMotifFormatOPAEBloc2();
            }
        } else {
            return getMotifFormatOPAEBloc2();
        }
    }

    /**
     * Renovis le motif décalé sur le bloc 2.
     * 
     * @return
     */
    private String getMotifFormatOPAEBloc2() {
        // Sinon on part au bloc de communication 2
        String motif = new String();
        for (int i = 0; i <= 34; i++) {
            motif = motif + " ";
        }

        String tmp = removeBlankLines(getMotif());
        ;
        if (tmp.length() > 104) {
            motif = motif + tmp.substring(0, 104);
        } else {
            motif = motif + tmp.substring(0, tmp.length());
        }

        return motif;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:32:42)
     * 
     * @return String
     */
    @Override
    public String getNatureOrdre() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getNatureOrdre();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:34:10)
     * 
     * @return String
     */
    public String getNomCache() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getNomCache();
    }

    @Override
    public String getNomPrenom() throws Exception {
        return getCompteAnnexe().getTiers().getNomPrenom();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:31:49)
     * 
     * @return String
     */
    @Override
    public String getNumTransaction() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getNumTransaction();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 13:37:19)
     * 
     * @return globaz.osiris.db.ordres.CAOrdreGroupe
     */
    @Override
    public CAOrdreGroupe getOrdreGroupe() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getOrdreGroupe();
    }

    @Override
    public CAOrdreGroupe getOrdreGroupe(String idOrdeGroupe) {
        ordreVersement.setSession(getSession());
        return ordreVersement.getOrdreGroupe(idOrdeGroupe);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:29:17)
     * 
     * @return String
     */
    @Override
    public String getReferenceBVR() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getReferenceBVR();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:30:42)
     * 
     * @return String
     */
    @Override
    public String getTypeVirement() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getTypeVirement();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:26:59)
     * 
     * @return String
     */
    public String getValeurConversion() {
        ordreVersement.setSession(getSession());
        return ordreVersement.getValeurConversion();
    }

    /**
     * Recherche si pour la section actuelle un ordre de versement est déjà en cours (idordre = 0 ou (idordre > 0 et
     * estretire = false)).
     * 
     * @param transaction
     * @param forIdSection
     * @throws Exception
     */
    private void hasOrdreNonExecute(BTransaction transaction, String forIdSection) throws Exception {
        CAOrdreNonVerseManager manager = new CAOrdreNonVerseManager();
        manager.setSession(getSession());

        manager.setForIdSection(forIdSection);
        manager.setForIdTypeOperation(APIOperation.CAOPERATIONORDREVERSEMENT);

        ArrayList etatIn = new ArrayList();
        etatIn.add(APIOperation.ETAT_COMPTABILISE);
        etatIn.add(APIOperation.ETAT_PROVISOIRE);
        etatIn.add(APIOperation.ETAT_OUVERT);
        manager.setForEtatIn(etatIn);

        manager.find(transaction);

        if (!manager.isEmpty()) {
            throw new Exception(getSession().getLabel("ORDRE_DE_VERSEMENT_NON_EXECUTE"));
        }
    }

    /**
     * Récupérer l'ordre de versement associé
     * 
     * @param transaction
     */
    public void initOrdreVersement(BTransaction transaction) {
        ordreVersement = new CAOrdreVersement();
        ordreVersement.setSession(getSession());
        ordreVersement.setIdOrdre(getIdOperation());
        try {
            ordreVersement.retrieve(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7118"));
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }

        setDefaultIdAdressePaiement();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:25:37)
     * 
     * @param newCodeISOMonnaieBonification String
     */
    @Override
    public void setCodeISOMonnaieBonification(String newCodeISOMonnaieBonification) {
        // if (isUpdatable()) {
        ordreVersement.setCodeISOMonnaieBonification(newCodeISOMonnaieBonification);
        // } else {
        // // Si le codeISOMonnaieBonification n'a pas changer on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if
        // (!_ordreVersement.getCodeISOMonnaieBonification().equals(newCodeISOMonnaieBonification))
        // {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:25:06)
     * 
     * @param newCodeISOMonnaieDepot String
     */
    @Override
    public void setCodeISOMonnaieDepot(String newCodeISOMonnaieDepot) {
        // if (isUpdatable()) {
        ordreVersement.setCodeISOMonnaieDepot(newCodeISOMonnaieDepot);
        // } else {
        // // Si le codeISOMonnaieDepot n'a pas changer on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if
        // (!_ordreVersement.getCodeISOMonnaieDepot().equals(newCodeISOMonnaieDepot))
        // {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:27:43)
     * 
     * @param newCoursConverstion String
     */
    public void setCoursConversion(String newCoursConversion) {
        // if (isUpdatable())
        ordreVersement.setCoursConversion(newCoursConversion);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Adresse de paiement par défaut
     */
    public void setDefaultIdAdressePaiement() {
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            if (getCompteAnnexe() != null) {
                if (getCompteAnnexe().getTiers() != null) {
                    setIdAdressePaiement(getCompteAnnexe().getTiers().getIdAdressePaiement(
                            TITiersOSI.DOMAINE_REMBOURSEMENT, getCompteAnnexe().getIdExterneRole(),
                            JACalendar.today().toString()));
                }
            }
        }
        // if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
        // if (getCompteAnnexe() != null) {
        // if (getCompteAnnexe().getTiers() != null) {
        // IntAdressePaiement adr[] =
        // getCompteAnnexe().getTiers().getListeAdressesPaiement();
        // if (adr != null && adr.length > 0) {
        // for (int i = 0; i < adr.length; i++) {
        // try {
        // // Date de début vide, date de fin vide
        // if (JadeStringUtil.isBlank(adr[i].getDateDebutRelation()) &&
        // JadeStringUtil.isBlank(adr[i].getDateFinRelation())) {
        // setIdAdressePaiement(adr[i].getIdAdressePaiement());
        // break;
        // }
        // // Date de début renseignée, date de fin renseignée
        // if (!JadeStringUtil.isBlank(adr[i].getDateDebutRelation()) &&
        // !JadeStringUtil.isBlank(adr[i].getDateFinRelation())) {
        // // On contrôle que la date d'échéance de l'ordre de versement
        // // se trouve dans entre la date de début et de fin de la relation
        // if (BSessionUtil.compareDateBetweenOrEqual(getSession(),
        // adr[i].getDateDebutRelation(), adr[i].getDateFinRelation(),
        // getDate())) {
        // setIdAdressePaiement(adr[i].getIdAdressePaiement());
        // break;
        // }
        // }
        // // Date de début renseignée,date de fin vide
        // if (!JadeStringUtil.isBlank(adr[i].getDateDebutRelation()) &&
        // JadeStringUtil.isBlank(adr[i].getDateFinRelation())) {
        // // On compare la date de début de la relation avec la date d'échéance
        // de l'ordre de versement
        // if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
        // adr[i].getDateDebutRelation(), getDate())) {
        // setIdAdressePaiement(adr[i].getIdAdressePaiement());
        // break;
        // }
        // }
        // // Date de début vide, date de fin renseignée
        // if (JadeStringUtil.isBlank(adr[i].getDateDebutRelation()) &&
        // !JadeStringUtil.isBlank(adr[i].getDateFinRelation())) {
        // // On compare la date de fin de la relation avec la date d'échéance
        // de l'ordre de versement
        // if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(),
        // adr[i].getDateFinRelation(), getDate())) {
        // setIdAdressePaiement(adr[i].getIdAdressePaiement());
        // break;
        // }
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }
        // }
        // }
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:30:04)
     * 
     * @param newEstBloque java.lang.Boolean
     */
    @Override
    public void setEstBloque(java.lang.Boolean newEstBloque) {
        ordreVersement.setEstBloque(newEstBloque);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:33:12)
     * 
     * @param newEstRetire java.lang.Boolean
     */
    @Override
    public void setEstRetire(java.lang.Boolean newEstRetire) {
        ordreVersement.setEstRetire(newEstRetire);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:22:43)
     * 
     * @param newIdAdressePaiement String
     */
    @Override
    public void setIdAdressePaiement(String newIdAdressePaiement) {
        ordreVersement.setIdAdressePaiement(newIdAdressePaiement);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 13:36:40)
     * 
     * @param newIdOrdreGroupe String
     */
    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        ordreVersement.setIdOrdreGroupe(newIdOrdreGroupe);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.02.2002 09:15:23)
     * 
     * @param newIdOrganeExecution String
     */
    @Override
    public void setIdOrganeExecution(String newIdOrganeExecution) {
        // if (isUpdatable()) {
        ordreVersement.setIdOrganeExecution(newIdOrganeExecution);
        // } else {
        // // Si l'idOrganeExecution n'a pas changer on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if
        // (!_ordreVersement.getIdOrganeExecution().equals(newIdOrganeExecution))
        // {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.01.2002 07:36:40)
     * 
     * @param newMontant String
     */
    @Override
    public void setMontant(String newMontant) {
        // if (isUpdatable()) {
        montant = newMontant;
        // } else{
        // // Si le montant n'a pas changer on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if
        // (!JANumberFormatter.deQuote(montant).equals(JANumberFormatter.deQuote(newMontant)))
        // {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:28:44)
     * 
     * @param newMotif String
     */
    @Override
    public void setMotif(String newMotif) {
        if (newMotif != null) {
            newMotif = removeBlankLines(newMotif);
        }

        ordreVersement.setMotif(newMotif);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:32:42)
     * 
     * @param newNatureOrdre String
     */
    @Override
    public void setNatureOrdre(String newNatureOrdre) {
        ordreVersement.setNatureOrdre(newNatureOrdre);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:34:10)
     * 
     * @param newNomCache String
     */
    public void setNomCache(String newNomCache) {
        ordreVersement.setNomCache(newNomCache);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:31:49)
     * 
     * @param newNumTransaction String
     */
    public void setNumTransaction(String newNumTransaction) {
        ordreVersement.setNumTransaction(newNumTransaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:29:17)
     * 
     * @param newReference String
     */
    public void setReferenceBVR(String newReferenceBVR) {
        // if (isUpdatable()) {
        ordreVersement.setReferenceBVR(newReferenceBVR);
        // } else {
        // // Si la referenceBVR n'a pas changé on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if (!_ordreVersement.getReferenceBVR().equals(newReferenceBVR)) {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:30:42)
     * 
     * @param newTypeOrdre String
     */
    @Override
    public void setTypeVirement(String newTypeVirement) {
        // if (isUpdatable()) {
        ordreVersement.setTypeVirement(newTypeVirement);
        // } else{
        // // Si le typeVirement n'a pas changé on évite de faire
        // // l'erreur afin qu'on puisse modifier un état bloqué même si
        // l'opération est comptabilisée
        // if (!_ordreVersement.getTypeVirement().equals(newTypeVirement)) {
        // _addError(null, getSession().getLabel("5133"));
        // }
        // }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 11:26:59)
     * 
     * @param newValeurConversion String
     */
    public void setValeurConversion(String newValeurConversion) {
        // if (isUpdatable())
        ordreVersement.setValeurConversion(newValeurConversion);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Générer le versement de l'ordre Date de création : (13.02.2002 09:18:26)
     * 
     * @param of globaz.osiris.db.ordres.IntOrdreFormateur format à appliquer pour l'ordre de versement
     * @param journal CAJournal le journal sur lequel le versement doit être effectué
     * @return StringBuffer un string buffer correspondant à l'ordre formatté pour le versement
     */
    public StringBuffer verser(CAOrdreFormateur of, CAJournal journal, BTransaction transaction) throws Exception {

        // Initialiser
        StringBuffer sb = null;

        // Si l'on désire formatter l'ordre
        if (of != null) {
            // Générer le versement
            sb = of.format(this);
        }

        // Si l'on désire générer le versement
        if (journal != null) {
            // Si l'ordre n'est pas déjà versé
            if (!getEtat().equals(APIOperation.ETAT_VERSE)) {

                // Instancier une écriture de versement
                CAVersement ver;

                if (getIdTypeOperation().equals(APIOperation.CAOPERATIONORDREVERSEMENTAVANCE)) {
                    ver = new CAVersementAvance();
                } else {
                    ver = new CAVersement();
                }

                ver.setSession(getSession());

                // Charger les attributs
                ver.dupliquer(this);
                ver.setDate(this.getOrdreGroupe().getDateEcheance());
                ver.setIdJournal(journal.getIdJournal());
                ver.setIdCompte(this.getOrdreGroupe().getOrganeExecution().getIdRubrique());
                ver.setMontant(getMontant());
                ver.setCodeDebitCredit(APIEcriture.DEBIT);
                ver.setIdOrdreVersement(getIdOperation());

                // Insérer l'opération et piéger les erreurs
                try {
                    ver.add(transaction);
                    if (ver.hasErrors()) {
                        _addError(transaction, getSession().getLabel("5156"));
                    } else {
                        setEtat(APIOperation.ETAT_VERSE);
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                }
            }
        }

        // Retourer le string buffer
        return sb;
    }
}
