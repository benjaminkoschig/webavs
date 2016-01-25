package globaz.osiris.db.contentieux;

import globaz.framework.util.FWCurrency;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperation;
import globaz.osiris.db.journal.comptecourant.CAJoinCompteCourantOperationManager;
import java.math.BigDecimal;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 09:55:51)
 * 
 * @author: Administrator
 */
public class CACalculTaxe extends globaz.globall.db.BEntity implements java.io.Serializable, IntTranslatable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String COMPTE_COURANT = "220002";
    public final static java.lang.String MONTANT = "219002";
    public final static java.lang.String MONTANT_TAUX = "219003";
    public final static java.lang.String RUBRIQUE = "220003";
    public final static java.lang.String SECTION = "220001";
    public final static java.lang.String TAUX = "219001";
    private globaz.osiris.db.comptes.CARubrique _rubrique;
    private java.lang.String baseTaxe = new String();

    // code systeme

    private CAParametreTaxeManager cacheParametreTaxeManager = null;
    private CATrancheTaxeManager cacheTrancheTaxeManager = null;
    private FWParametersSystemCode csBaseTaxe = null;
    private FWParametersSystemCodeManager csBaseTaxes = null;

    private FWParametersSystemCode csTypeTaxe = null;
    private FWParametersSystemCodeManager csTypeTaxes = null;
    private java.lang.String idCalculTaxe = new String();
    private java.lang.String idRubrique = new String();
    private java.lang.String idTraduction = new String();
    private java.lang.String montantFixe = new String();

    private PATraductionHelper trLibelles = null;
    private java.lang.String typeTaxe = new String();

    /**
     * Commentaire relatif au constructeur CACalculTaxe
     */
    public CACalculTaxe() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().add(transaction);
    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Suppression de tous les libellés
        getTraductionHelper().delete(transaction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().update(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CATXCTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        typeTaxe = statement.dbReadNumeric("TYPETAXE");
        montantFixe = statement.dbReadNumeric("MONTANTFIXE", 2);
        baseTaxe = statement.dbReadNumeric("BASETAXE");
        idCalculTaxe = statement.dbReadNumeric("IDCALCULTAXE");
        idRubrique = statement.dbReadNumeric("IDRUBRIQUE");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCALCULTAXE", this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("TYPETAXE", this._dbWriteNumeric(statement.getTransaction(), getTypeTaxe(), "typeTaxe"));
        statement.writeField("MONTANTFIXE",
                this._dbWriteNumeric(statement.getTransaction(), getMontantFixe(), "montantFixe"));
        statement.writeField("BASETAXE", this._dbWriteNumeric(statement.getTransaction(), getBaseTaxe(), "baseTaxe"));
        statement.writeField("IDCALCULTAXE",
                this._dbWriteNumeric(statement.getTransaction(), getIdCalculTaxe(), "idCalculTaxe"));
        statement.writeField("IDRUBRIQUE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField("IDTRADUCTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.06.2002 14:01:03)
     */
    public CATaxe calculTaxe(CASection section) {
        CATaxe taxe = new CATaxe();
        taxe.setCalculTaxe(this);

        // Chargement du manager de trancheTaxe dans le cache
        getTrancheTaxes();

        // Chargement du manager de ParametreTaxe dans le cache
        getParametreTaxes();

        // Montant déterminant de section
        if (getBaseTaxe().equalsIgnoreCase(CACalculTaxe.SECTION)) {
            taxe.setMontantBase(section.getSolde());
        } else {

            // Parcourir CAEciture si baseTaxe = Rubrique CACompteCourantSection
            // si baseTaxe = Compte_courant
            FWCurrency _total = new FWCurrency();
            if (getBaseTaxe().equalsIgnoreCase(CACalculTaxe.RUBRIQUE)) {
                CAEcritureManager manEcriture = new CAEcritureManager();
                manEcriture.setSession(getSession());
                manEcriture.setForIdSection(section.getIdSection());
                try {
                    manEcriture.find();
                } catch (Exception e) {
                    _addError(null, e.getMessage());
                    return null;
                }
                CAEcriture elemEcriture;
                for (int i = 0; i < manEcriture.size(); i++) {
                    elemEcriture = (CAEcriture) manEcriture.getEntity(i);
                    if (elemEcriture.getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_CREANCIER)
                            || elemEcriture.getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_COURANT_DEBITEUR)) {
                        continue;
                    }

                    // recherche de la rubrique dans cacheParametreTaxeManager
                    CAParametreTaxe elemCacheParamTaxe;
                    for (int j = 0; j < cacheParametreTaxeManager.size(); j++) {
                        elemCacheParamTaxe = (CAParametreTaxe) cacheParametreTaxeManager.getEntity(j);

                        if (elemCacheParamTaxe.getIdRubrique().equalsIgnoreCase(elemEcriture.getIdCompte())) {
                            _total.add(elemEcriture.getMontant());
                            elemCacheParamTaxe.setEstTrouve(Boolean.TRUE);
                        }
                    }
                }

            } else if (getBaseTaxe().equalsIgnoreCase(CACalculTaxe.COMPTE_COURANT)) {
                CAJoinCompteCourantOperationManager manager = new CAJoinCompteCourantOperationManager();
                manager.setSession(getSession());
                manager.setForIdSection(section.getIdSection());

                try {
                    manager.find();
                } catch (Exception e) {
                    _addError(null, e.getMessage());
                    return null;
                }

                for (int i = 0; i < manager.size(); i++) {
                    CAJoinCompteCourantOperation joinEntity = (CAJoinCompteCourantOperation) manager.get(i);

                    // Recherche de la rubrique dans cacheParametreTaxeManager
                    CAParametreTaxe elemCacheParamTaxe;
                    for (int j = 0; j < cacheParametreTaxeManager.size(); j++) {
                        elemCacheParamTaxe = (CAParametreTaxe) cacheParametreTaxeManager.getEntity(j);

                        CACompteCourant compteCourant = new CACompteCourant();
                        compteCourant.setSession(getSession());
                        compteCourant.setIdCompteCourant(joinEntity.getIdCompteCourant());

                        try {
                            compteCourant.retrieve();
                        } catch (Exception e1) {
                            _addError(null, e1.getMessage());
                            return null;
                        }

                        if (compteCourant.isNew()) {
                            _addError(null, "Compte courant non resolu (id = " + joinEntity.getIdCompteCourant() + ")");
                            return null;
                        }

                        if (elemCacheParamTaxe.getIdRubrique().equalsIgnoreCase(compteCourant.getIdRubrique())) {
                            _total.add(joinEntity.getMontant());
                            elemCacheParamTaxe.setEstTrouve(Boolean.TRUE);
                        }
                    }
                }
            }

            // contrôle que estRequis soit renseigné pour toutes les rubriques
            // dans parametreTaxe
            if (getBaseTaxe().equalsIgnoreCase(CACalculTaxe.RUBRIQUE)
                    || getBaseTaxe().equalsIgnoreCase(CACalculTaxe.COMPTE_COURANT)) {
                boolean nonRenseigne = false;
                CAParametreTaxe elemCacheParamTaxe;
                for (int j = 0; j < cacheParametreTaxeManager.size(); j++) {
                    elemCacheParamTaxe = (CAParametreTaxe) cacheParametreTaxeManager.getEntity(j);
                    if (elemCacheParamTaxe.getEstRequis().booleanValue()
                            && !elemCacheParamTaxe.getEstTrouve().booleanValue()) {
                        nonRenseigne = true;
                    }
                }

                if (nonRenseigne) {
                    return null;
                }

                if (_total.isZero() || _total.isNegative()) {
                    return null;
                } else {
                    taxe.setMontantBase(_total.toString());
                }
            }
        }
        // Montant fixe
        FWCurrency _montantFixe = new FWCurrency();
        _montantFixe.add(getMontantFixe());
        if (getTypeTaxe().equalsIgnoreCase(CACalculTaxe.MONTANT) && !_montantFixe.isZero()) {
            taxe.setMontantTaxe(getMontantFixe());
        } else
        // Taux fixe
        if (getTypeTaxe().equalsIgnoreCase(CACalculTaxe.TAUX) && !_montantFixe.isZero()) {
            taxe.setTaux(getMontantFixe());
            String resultTaux = new String();
            resultTaux = ""
                    + JANumberFormatter.round(
                            Float.parseFloat(taxe.getTaux()) * Float.parseFloat(taxe.getMontantBase()), 0.05, 2,
                            JANumberFormatter.NEAR);
            taxe.setMontantTaxe(resultTaux);
        } else {
            // Recherche de la tranche de taxe
            CATrancheTaxe elemTrancheTaxe;
            if (cacheTrancheTaxeManager != null) {
                int i = 0;
                do {
                    elemTrancheTaxe = (CATrancheTaxe) cacheTrancheTaxeManager.getEntity(i);
                    i++;
                } while ((i < cacheTrancheTaxeManager.size())
                        && (Float.parseFloat(taxe.getMontantBase()) > Float.parseFloat(elemTrancheTaxe
                                .getValeurPlafond())));
                if ((cacheTrancheTaxeManager.size() >= i)
                        && (Float.parseFloat(taxe.getMontantBase()) > Float.parseFloat(elemTrancheTaxe
                                .getValeurPlafond()))) {
                    _addError(null, getSession().getLabel("7175"));
                    return null;
                }
                // Montant
                if (getTypeTaxe().equalsIgnoreCase(CACalculTaxe.MONTANT)) {
                    taxe.setMontantTaxe(elemTrancheTaxe.getMontantVariable());
                } else {
                    // Taux ou montant fixe + taux
                    BigDecimal _tauxPlafond = new BigDecimal(elemTrancheTaxe.getTauxPlafond());
                    if (getTypeTaxe().equalsIgnoreCase(CACalculTaxe.TAUX)
                            || (getTypeTaxe().equalsIgnoreCase(CACalculTaxe.MONTANT_TAUX) && !_tauxPlafond
                                    .equals(new BigDecimal(0.0)))) {
                        taxe.setTaux(elemTrancheTaxe.getTauxPlafond());
                        String taxeResult = ""
                                + JANumberFormatter.round(
                                        Float.parseFloat(elemTrancheTaxe.getTauxPlafond())
                                                * Float.parseFloat(taxe.getMontantBase()), 0.05, 2,
                                        JANumberFormatter.NEAR);
                        if (getTypeTaxe().equalsIgnoreCase(CACalculTaxe.MONTANT_TAUX)) {
                            FWCurrency cMontant = new FWCurrency();
                            // Si il n'y a pas de taxe, on ne met pas de frais
                            // fixe
                            cMontant.add(taxeResult);
                            if (!cMontant.equals(new FWCurrency(0.0))) {
                                cMontant.add(getMontantFixe());
                            }
                            if (elemTrancheTaxe.getMontantVariable() != null) {
                                int compare = cMontant.compareTo(new FWCurrency(elemTrancheTaxe.getMontantVariable()));
                                if (compare == 1) {
                                    // Taxe plus grande que le montant variable
                                    // définit comme maximum
                                    // on prend dans ce cas le le montant
                                    // maximum
                                    taxe.setMontantTaxe(elemTrancheTaxe.getMontantVariable());
                                } else {
                                    // Taxe plus petite que le montant maximum,
                                    // on peut prendre
                                    // la taxe calculée
                                    taxe.setMontantTaxe(cMontant.toString());
                                }
                            } else {
                                taxe.setMontantTaxe(cMontant.toString());
                            }
                        } else {
                            taxe.setMontantTaxe(taxeResult);
                        }
                    }
                }
            } else {
                taxe = null;
            }
        }

        return taxe;
    }

    public java.lang.String getBaseTaxe() {
        return baseTaxe;
    }

    public FWParametersSystemCode getCsBaseTaxe() {

        if (csBaseTaxe == null) {
            // liste pas encore chargee, on la charge
            csBaseTaxe = new FWParametersSystemCode();
            csBaseTaxe.getCode(getBaseTaxe());
        }
        return csBaseTaxe;
    }

    /*
     * Insérez la description de la méthode ici. Date de création : (13.12.2001 11:19:02) @return
     * globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsBaseTaxes() {
        // liste déjà chargée ?
        if (csBaseTaxes == null) {
            // liste pas encore chargée, on la charge
            csBaseTaxes = new FWParametersSystemCodeManager();
            csBaseTaxes.setSession(getSession());
            csBaseTaxes.getListeCodesSup("OSIBASTAX", getSession().getIdLangue());
        }
        return csBaseTaxes;
    }

    public FWParametersSystemCode getCsTypeTaxe() {

        if (csTypeTaxe == null) {
            // liste pas encore chargee, on la charge
            csTypeTaxe = new FWParametersSystemCode();
            csTypeTaxe.getCode(getTypeTaxe());
        }
        return csTypeTaxe;
    }

    /*
     * Insérez la description de la méthode ici. Date de création : (13.12.2001 11:19:02) @return
     * globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsTypeTaxes() {
        // liste déjà chargée ?
        if (csTypeTaxes == null) {
            // liste pas encore chargée, on la charge
            csTypeTaxes = new FWParametersSystemCodeManager();
            csTypeTaxes.setSession(getSession());
            csTypeTaxes.getListeCodesSup("OSITYPTAX", getSession().getIdLangue());
        }
        return csTypeTaxes;
    }

    @Override
    public String getDescription() {
        // Description dans la langue de l'utilisateur
        return this.getDescription(null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription(String codeIsoLangue) {

        String s = "";
        try {
            s = PATraductionHelper.translate(getSession(), getIdTraduction(), codeIsoLangue);
        } catch (Exception e) {
            _addError(null, e.toString());
        }
        return s;
    }

    public java.lang.String getIdCalculTaxe() {
        return idCalculTaxe;
    }

    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    public java.lang.String getIdRubrique() {
        return idRubrique;
    }

    @Override
    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    public java.lang.String getMontantFixe() {
        return montantFixe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 08:52:50)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreTaxeManager
     */
    public CAParametreTaxeManager getParametreTaxes() {
        // liste déjà chargée ?
        if (cacheParametreTaxeManager == null) {
            // liste pas encore chargée, on la charge
            cacheParametreTaxeManager = new CAParametreTaxeManager();
            cacheParametreTaxeManager.setSession(getSession());
            cacheParametreTaxeManager.setForIdCalculTaxe(getIdCalculTaxe());
            try {
                cacheParametreTaxeManager.find();
                if (cacheParametreTaxeManager.isOnError()) {
                    cacheParametreTaxeManager = null;
                }

            } catch (Exception e) {
                cacheParametreTaxeManager = null;
            }
        }
        return cacheParametreTaxeManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.osiris.db.comptes.CARubrique getRubrique() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdRubrique())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_rubrique == null) {
            // Instancier un nouveau LOG
            _rubrique = new globaz.osiris.db.comptes.CARubrique();
            _rubrique.setSession(getSession());

            // Récupérer le log en question
            _rubrique.setIdRubrique(getIdRubrique());
            try {
                _rubrique.retrieve();
                if (_rubrique.isOnError()) {
                    _rubrique = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _rubrique = null;
            }
        }

        return _rubrique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 14:57:23)
     * 
     * @return globaz.norma.db.fondation.PATraductionHelper
     */
    private PATraductionHelper getTraductionHelper() {
        if (trLibelles == null) {
            try {
                trLibelles = new PATraductionHelper(this);
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return trLibelles;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.06.2002 07:42:50)
     * 
     * @return globaz.osiris.db.contentieux.CATrancheTaxeManager
     */
    public CATrancheTaxeManager getTrancheTaxes() {
        // liste déjà chargée ?
        if (cacheTrancheTaxeManager == null) {
            // liste pas encore chargée, on la charge
            cacheTrancheTaxeManager = new CATrancheTaxeManager();
            cacheTrancheTaxeManager.setSession(getSession());
            cacheTrancheTaxeManager.setForIdCalculTaxe(getIdCalculTaxe());
            try {
                cacheTrancheTaxeManager.find();
                if (cacheTrancheTaxeManager.isOnError()) {
                    cacheTrancheTaxeManager = null;
                }

            } catch (Exception e) {
                cacheTrancheTaxeManager = null;
            }
        }
        return cacheTrancheTaxeManager;
    }

    /**
     * Getter
     */
    public java.lang.String getTypeTaxe() {
        return typeTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.07.2002 07:53:19)
     * 
     * @return boolean
     */
    public boolean isFraisPoursuite() {
        CARubrique rubrique = new CARubrique();
        rubrique.setIdRubrique(getIdRubrique());
        rubrique.setSession(getSession());
        try {
            rubrique.retrieve();
            if (rubrique.getNatureRubrique().equals(APIRubrique.FRAIS_POURSUITES)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void setBaseTaxe(java.lang.String newBaseTaxe) {
        baseTaxe = newBaseTaxe;
    }

    @Override
    public void setDescription(String newDescription) throws Exception {
        this.setDescription(newDescription, null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription, String codeISOLangue) {
        getTraductionHelper().setDescription(newDescription, codeISOLangue);
        if (getTraductionHelper().getError() != null) {
            _addError(null, getTraductionHelper().getError().getMessage());
        }
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionDe(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "DE");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionFr(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "FR");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    public void setDescriptionIt(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "IT");
    }

    public void setIdCalculTaxe(java.lang.String newIdCalculTaxe) {
        idCalculTaxe = newIdCalculTaxe;
    }

    public void setIdRubrique(java.lang.String newIdRubrique) {
        idRubrique = newIdRubrique;

    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }

    public void setMontantFixe(java.lang.String newMontantFixe) {
        montantFixe = newMontantFixe;
    }

    /**
     * Setter
     */
    public void setTypeTaxe(java.lang.String newTypeTaxe) {
        typeTaxe = newTypeTaxe;
    }
}
