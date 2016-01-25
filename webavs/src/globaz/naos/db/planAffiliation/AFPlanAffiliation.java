/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.planAffiliation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean;
import globaz.naos.api.helper.IAFPlanAffiliationHelper;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisationListViewBean;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * La classe définissant l'entité PlanAffiliation.
 * 
 * @author sau
 */
public class AFPlanAffiliation extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;
    // Foreign Key
    private java.lang.String affiliationId = new String();
    private Boolean blocageEnvoi = new Boolean(false);
    private java.lang.String domaineCourrier = new String();
    private String domaineRecouvrement = "";
    private String domaineRemboursement = "";
    private java.lang.String idTiersAffiliation = new String();
    private java.lang.String idTiersFacturation = new String();
    private Boolean inactif = new Boolean(false);
    // Fields
    private java.lang.String libelle = new String();

    private java.lang.String libelleFacture = new String();

    // DB Table AFPLAFP
    // Primary Key
    private java.lang.String planAffiliationId = new String();
    private String saveLibelle = new String(); // pour detecter un changement

    // (PO4750 - maj des suivis liés
    // au plan)
    private String saveLibelleFacture = new String();// pour detecter un
    private java.lang.String typeAdresse = new String();

    // changement (PO4750 -
    // maj des suivis liés
    // au plan)

    /**
     * Constructeur d'AFPlanAffiliation.
     */
    public AFPlanAffiliation() {
        super();
        setMethodsToLoad(IAFPlanAffiliationHelper.METHODS_TO_LOAD);
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        super._afterUpdate(transaction);
        _updatePlanSuivi(transaction);

    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

        setPlanAffiliationId(this._incCounter(transaction, "0"));
    }

    /**
     * Effectue des traitements avant une suppression de la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        AFCotisationListViewBean cotisationList = new AFCotisationListViewBean();
        cotisationList.setForPlanAffiliationId(getPlanAffiliationId());
        cotisationList.setSession(transaction.getSession());
        cotisationList.find();

        if (cotisationList.size() > 0) {
            _addError(transaction, transaction.getSession().getLabel("1270"));
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFPLAFP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        planAffiliationId = statement.dbReadNumeric("MUIPLA");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        libelle = statement.dbReadString("MULLIB");
        libelleFacture = statement.dbReadString("MULFAC");
        blocageEnvoi = statement.dbReadBoolean("MUBBLO");
        inactif = statement.dbReadBoolean("MUBINA");
        typeAdresse = statement.dbReadNumeric("HETTAD");
        domaineCourrier = statement.dbReadNumeric("HFIAPP");
        domaineRecouvrement = statement.dbReadNumeric("HFIAPL");
        domaineRemboursement = statement.dbReadNumeric("HFIAPR");
        idTiersFacturation = statement.dbReadNumeric("HTITIE");

        saveLibelle = libelle; // voir _updatePlanSuivi(BTransaction transaction
        // )
        saveLibelleFacture = libelleFacture; // voir
        // _updatePlanSuivi(BTransaction
        // transaction )
    }

    /*
     * oca Met à jour (si nécessaire) le libelle du plan utilisé comme clé de correlation dans la gestion des suivis
     * (dans les ref. prrovenance)
     */
    @SuppressWarnings("unchecked")
    private void _updatePlanSuivi(BTransaction transaction) throws Exception {

        /*
         * PO4750 - oca
         * 
         * Lors d'un changement de libelle du plan d'affiliation, mettre à jour le libelle du plan dans les ref. prov
         * des journalisations, car le libelle du plan est utilisé comme clé de correlation.
         * 
         * A noter qu'il y a deux libelles dans le plan : - le libelle du plan (obligatoire) - le libelle de facturation
         * (facultatif, mais qui surcharge le libelle du plan si il est renseigné)
         * 
         * voir getLibelleFactureNotEmpty()
         * 
         * Pour détecter un changement de plan, chaqun des deux libellés est également stoqué dans les champs
         * saveLibelleFacturation et saveLibelle.
         * 
         * Le libelle à utilier pour la recherche dans les ref prov. est identifié selon la règle suivante :
         * 
         * Si changement du libelle de facturation du plan { Si ancien libelle de facturation n'est pas vide, le prendre
         * (cas 1), Sinon prendre le libelle du plan (cas 2) (prendre avant modif au cas ou le libelle du plan aurait
         * changé aussi..)
         * 
         * } Sinon Si changement du libelle du plan et pas de libelle de facturation { prendre ancien libelle du plan
         * (cas 3) } Sinon { rien à faire, pas de changement de libelle... }
         * 
         * 
         * Cas 1 : c'est le cas le plus fréquent, un libelle de factu (libelle1) existe au moment ou le suivi est
         * généré, puis qq le change (libelle2)...
         * 
         * Cas 2 : le libelle du plan est renseigné (libelle1) , mais le libelle de factu est vide au moment ou le suivi
         * est généré, ensuite, un libelle de facturation est renseigné (libelle2)
         * 
         * Cas 3: (Idem cas 2 mais c'est le libelle qui change) donc : Le libelle du plan est renseigné (libelle1) ,
         * mais le libelle de factu est vide au moment ou le suivi est généré. ensuite, le libelle du plan change
         * (libelle2)
         * 
         * 
         * Dans les trois cas, il faut alors rechercher le suivi avec le libelle1 et le mettre à jour avec libelle2
         */

        /*
         * Etape 1 - identification du libelle à utiliser le but est de renseigné nouveauLibelle et libelleAChanger
         */
        String libelleAChanger = "";
        String nouveauLibelle = "";
        if (saveLibelleFacture == null) {
            saveLibelleFacture = ""; // preventif
        }
        if (!saveLibelleFacture.equals(libelleFacture)) {
            nouveauLibelle = libelleFacture;
            libelleAChanger = (!JadeStringUtil.isEmpty(saveLibelleFacture)) ? saveLibelleFacture : saveLibelle;
        } else if (!saveLibelle.equals(libelle) && JadeStringUtil.isEmpty(libelleFacture)) {
            // le libelle du plan change, et il n'y a pas de libelle de facture
            // (qui serait alors prioritaire...)
            nouveauLibelle = libelle;
            libelleAChanger = saveLibelle;
        }

        /*
         * Etape 2 (si nécessaire) - mise à jour des ref prov en fonction de nouveauLibelle et de libelleAChanger
         */
        if (!JadeStringUtil.isEmpty(libelleAChanger)) {
            // prépare la req. d'intérogation des ref. prov concernées
            String col = TIToolBox.getCollection();
            String fields = "p.JREPID PK";
            String query = " from " + col + "JOJPREP a" + " inner join " + col
                    + "JOJPREP p on (a.jjouid = p.jjouid and p.jrepty='" + ILEConstantes.CS_PARAM_GEN_PLAN + "')"
                    + " where a.JREPTY = '" + ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION + "' and a.JREPIP = '"
                    + getAffiliationId() + "'";

            // Effectue la requête et met les resultats dans une liste de map,
            // la map contenant la colonne comme clé et la valeur
            List<Map<String, String>> res = TISQL.query(getSession(), fields, query);

            // pour chaque journalisation, mettre à jour le libelle de la ref
            // prov. avec celui qui a été déterminé selon la règle ci-dessus
            for (Map<String, String> row : res) {
                String idReferenceProvenance = row.get("PK"); // primary key de
                // la ref prov.
                LUReferenceProvenanceViewBean refProv = new LUReferenceProvenanceViewBean();
                refProv.setSession(getSession());
                refProv.setIdReferenceProvenance(idReferenceProvenance);
                refProv.retrieve(transaction);
                refProv.setIdCleReferenceProvenance(nouveauLibelle);
                refProv.update(transaction);
            }
        }

        /*
         * FIN PO 4750
         */
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean validationOK = true;

        // Test que les champs obligatoires soit renseignés
        validationOK &= _propertyMandatory(statement.getTransaction(), getLibelle(), getSession().getLabel("900"));
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MUIPLA", this._dbWriteNumeric(statement.getTransaction(), getPlanAffiliationId(), ""));

    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MUIPLA",
                this._dbWriteNumeric(statement.getTransaction(), getPlanAffiliationId(), "planAffiliationId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("MULLIB", this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("MULFAC",
                this._dbWriteString(statement.getTransaction(), getLibelleFacture(), "libelleFacture"));
        statement.writeField("MUBBLO", this._dbWriteBoolean(statement.getTransaction(), isBlocageEnvoi(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "blocageEnvoi"));
        statement.writeField("MUBINA", this._dbWriteBoolean(statement.getTransaction(), isInactif(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "inactif"));
        statement.writeField("HETTAD",
                this._dbWriteNumeric(statement.getTransaction(), getTypeAdresse(), "typeAdresse"));
        statement.writeField("HFIAPP",
                this._dbWriteNumeric(statement.getTransaction(), getDomaineCourrier(), "domaineCourrier"));
        statement.writeField("HFIAPL",
                this._dbWriteNumeric(statement.getTransaction(), getDomaineRecouvrement(), "domaineRecouvrement"));
        statement.writeField("HFIAPR",
                this._dbWriteNumeric(statement.getTransaction(), getDomaineRemboursement(), "domaineRemboursement"));
        statement.writeField("HTITIE",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiersFacturation(), "idTiersFacturation"));
    }

    /**
     * Methode utilisée par les API.
     * 
     * @param params
     * @return
     * @throws Exception
     */
    public BManager find(Hashtable params) throws Exception {
        BManager manager = getManager();
        manager.setSession(getSession());
        if (params != null) {
            Enumeration methods = params.keys();
            while (methods.hasMoreElements()) {
                String methodName = (String) methods.nextElement();
                String value = (String) params.get(methodName);
                Method m = manager.getClass().getMethod(methodName, new Class[] { String.class });
                if (m != null) {
                    m.invoke(manager, new Object[] { value });
                }
            }
        }
        manager.find();
        return manager;
    }

    /**
     * Rechercher l'Affiliation de l'adhésion en fonction de son ID.
     * 
     * @return l'Affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public Boolean getBlocageEnvoi() {
        return blocageEnvoi;
    }

    public java.lang.String getDomaineCourrier() {
        return domaineCourrier;
    }

    public String getDomaineRecouvrement() {
        return domaineRecouvrement;
    }

    public String getDomaineRemboursement() {
        return domaineRemboursement;
    }

    public java.lang.String getIdTiers() {
        if (!JadeStringUtil.isIntegerEmpty(getIdTiersFacturation())) {
            return getIdTiersFacturation();
        } else {
            return getIdTiersAffiliation();
        }
    }

    // Other
    public java.lang.String getIdTiersAffiliation() {
        return idTiersAffiliation;
    }

    public java.lang.String getIdTiersFacturation() {
        return idTiersFacturation;
    }

    public Boolean getInactif() {
        return inactif;
    }

    public java.lang.String getLibelle() {
        return libelle;
    }

    /**
     * @return
     */
    public java.lang.String getLibelleFacture() {
        return libelleFacture;
    }

    public java.lang.String getLibelleFactureNotEmpty() {
        if (JadeStringUtil.isEmpty(getLibelleFacture())) {
            return getLibelle();
        }
        return libelleFacture;
    }

    /*
     * public FWParametersSystemCode getCsTypeAdresse() { return csTypeAdresse; }
     */

    /*
     * public FWParametersSystemCode getCsDomaineAdresse() { return csDomaineAdresse; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * Renvoie le Manager de l'entité.
     * 
     * @return
     */
    protected BManager getManager() {
        return new AFPlanAffiliationManager();
    }

    public java.lang.String getPlanAffiliationId() {
        return planAffiliationId;
    }

    /**
     * Rechercher le tiers de facturation pour le plan d'affiliation en fonction de son ID ou du tiers par default.
     * 
     * @return le tiers de facturation
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    public java.lang.String getTypeAdresse() {
        return typeAdresse;
    }

    public Boolean isBlocageEnvoi() {
        return blocageEnvoi;
    }

    public Boolean isInactif() {
        return inactif;
    }

    public void setAffiliationId(java.lang.String string) {
        affiliationId = string;
    }

    public void setBlocageEnvoi(Boolean boolean1) {
        blocageEnvoi = boolean1;
    }

    public void setDomaineCourrier(java.lang.String string) {
        domaineCourrier = string;
    }

    public void setDomaineRecouvrement(String string) {
        domaineRecouvrement = string;
    }

    public void setDomaineRemboursement(String string) {
        domaineRemboursement = string;
    }

    // Other
    public void setIdTiersAffiliation(java.lang.String string) {
        idTiersAffiliation = string;
    }

    public void setIdTiersFacturation(java.lang.String string) {
        idTiersFacturation = string;
    }

    public void setInactif(Boolean inactif) {
        this.inactif = inactif;
    }

    public void setLibelle(java.lang.String string) {
        libelle = string;
    }

    /**
     * @param string
     */
    public void setLibelleFacture(java.lang.String string) {
        libelleFacture = string;
    }

    public void setPlanAffiliationId(java.lang.String string) {
        planAffiliationId = string;
    }

    public void setTypeAdresse(java.lang.String string) {
        typeAdresse = string;
    }

}
