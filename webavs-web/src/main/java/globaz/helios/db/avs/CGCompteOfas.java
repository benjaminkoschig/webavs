package globaz.helios.db.avs;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.helios.db.avs.helper.CGHelperCompteOfasManager;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.process.helper.CGMontantHelper;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author user
 * @version 1.1
 * 
 */
public class CGCompteOfas extends globaz.globall.db.BEntity implements ITreeListable, CGLibelleInterface,
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_DOMAINE_ADMINISTRATION = globaz.helios.db.comptes.CGCompte.CS_COMPTE_ADMINISTRATION;
    // Domaine d'un compte Ofas
    public final static String CS_DOMAINE_BILAN = globaz.helios.db.comptes.CGCompte.CS_COMPTE_BILAN;
    public final static String CS_DOMAINE_EXPLOITATION = globaz.helios.db.comptes.CGCompte.CS_COMPTE_EXPLOITATION;
    public final static String CS_DOMAINE_INVESTISSEMENT = globaz.helios.db.comptes.CGCompte.CS_COMPTE_INVESTISSEMENT;
    public final static String CS_DOMAINE_TOUS = globaz.helios.db.comptes.CGCompte.CS_COMPTE_TOUS;
    // Genre d'un compte Ofas
    public final static String CS_GENRE_ACTIF = globaz.helios.db.comptes.CGCompte.CS_GENRE_ACTIF;
    public final static String CS_GENRE_CHARGE = globaz.helios.db.comptes.CGCompte.CS_GENRE_CHARGE;

    public final static String CS_GENRE_CLOTURE = globaz.helios.db.comptes.CGCompte.CS_GENRE_CLOTURE;
    public final static String CS_GENRE_OUVERTURE = globaz.helios.db.comptes.CGCompte.CS_GENRE_OUVERTURE;
    public final static String CS_GENRE_PASSIF = globaz.helios.db.comptes.CGCompte.CS_GENRE_PASSIF;
    public final static String CS_GENRE_PRODUIT = globaz.helios.db.comptes.CGCompte.CS_GENRE_PRODUIT;
    public final static String CS_GENRE_RESULTAT = globaz.helios.db.comptes.CGCompte.CS_GENRE_RESULTAT;
    public final static String CS_GENRE_TOUS = globaz.helios.db.comptes.CGCompte.CS_GENRE_TOUS;
    public final static String CS_NATURE_CC_AFFILIES = "705002";
    public final static String CS_NATURE_FICTIF = "705003";

    // Nature d'un compte Ofas
    public final static String CS_NATURE_STANDARD = "705001";
    private java.lang.String idCompteOfas = new String();
    private java.lang.String idDomaine = "";
    private java.lang.String idExterne = new String();
    private java.lang.String idGenre = "";

    private java.lang.String idMandat = new String();
    private java.lang.String idNature = "";
    private java.lang.String libelleDe = new String();

    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();
    private java.lang.Boolean nonAnnoncable = new Boolean(false);

    // code systeme

    /**
     * Commentaire relatif au constructeur CGCompteOfas
     */
    public CGCompteOfas() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdCompteOfas(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getFields(BStatement statement) {
        return _getCollection() + _getTableName() + ".idcompteofas, " + _getCollection() + _getTableName()
                + ".idmandat, " + _getCollection() + _getTableName() + ".libellefr, " + _getCollection()
                + _getTableName() + ".libellede, " + _getCollection() + _getTableName() + ".libelleit, "
                + _getCollection() + _getTableName() + ".idexterne, " + _getCollection() + _getTableName()
                + ".nonannoncable, " + _getCollection() + _getTableName() + ".pspy, " + _getCollection()
                + _getTableName() + ".idnature, " + _getCollection() + _getTableName() + ".iddomaine, "
                + _getCollection() + _getTableName() + ".idgenre";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGOFCPP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCompteOfas = statement.dbReadNumeric("IDCOMPTEOFAS");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        idExterne = statement.dbReadString("IDEXTERNE");
        nonAnnoncable = statement.dbReadBoolean("NONANNONCABLE");
        idDomaine = statement.dbReadNumeric("IDDOMAINE");
        idNature = statement.dbReadNumeric("IDNATURE");
        idGenre = statement.dbReadNumeric("IDGENRE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCOMPTEOFAS", this._dbWriteNumeric(statement.getTransaction(), getIdCompteOfas(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCOMPTEOFAS",
                this._dbWriteNumeric(statement.getTransaction(), getIdCompteOfas(), "idCompteOfas"));
        statement.writeField("IDMANDAT", this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("LIBELLEFR", this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("IDEXTERNE", this._dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField("NONANNONCABLE", this._dbWriteBoolean(statement.getTransaction(), isNonAnnoncable(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "nonAnnoncable"));
        statement
                .writeField("IDDOMAINE", this._dbWriteNumeric(statement.getTransaction(), getIdDomaine(), "idDomaine"));
        statement.writeField("IDNATURE", this._dbWriteNumeric(statement.getTransaction(), getIdNature(), "idNature"));
        statement.writeField("IDGENRE", this._dbWriteNumeric(statement.getTransaction(), getIdGenre(), "idGenre"));
    }

    /**
     * Method computeSoldeCumule.
     * 
     * Calcul le solde cumule.
     * 
     * @param idExerComptable
     *            String
     * @param idPeriode
     *            String
     * @param isProvisoire
     *            boolean
     * @return CGMontantHelper
     * @throws Exception
     *             Exception
     */
    public CGMontantHelper computeSoldeCumule(globaz.globall.db.BTransaction transaction, String idExerComptable,
            String idPeriode, String idCentreCharge, boolean isProvisoire) throws Exception {

        // Solde actuel
        globaz.framework.util.FWCurrency solde = new globaz.framework.util.FWCurrency();

        CGMontantHelper result = new CGMontantHelper();

        Iterator listComptes = null;
        listComptes = getListComptesIterator(transaction, null);

        if (listComptes != null) {
            CGCompte entity = null;
            while (listComptes.hasNext()) {
                CGCompte compte = (CGCompte) listComptes.next();
                if (entity == null) {
                    entity = compte;
                }
                // TODO Faire que les exceptions aux comptes fictifs ne soient
                // pas ajoutés ici
                // System.out.println("IdSecteurAVS : " + compte.getIdSecteurAVS() + " / NuméroCompteAVS : "
                // + compte.getNumeroCompteAVS());
                // if (!compte.belongsToCompteFictif()) {
                solde.add(CGSolde.computeSoldeCumule(idExerComptable, compte.getIdCompte(), idPeriode, idCentreCharge,
                        getSession(), isProvisoire).doubleValue());
                // }
            }

            // On ne prend pas en compte les comptes avec un solde à zéro.
            if (JadeStringUtil.isDecimalEmpty(solde.toString())) {
                // ||
                // solde.intValue()==0)
                return result;
            }

            // Les comptes contenu dans la liste de comptes sont tous du même
            // genre.
            if (entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_ACTIF)
                    || entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_CHARGE)) {
                result.actif = new FWCurrency(solde.toString());
            } else if (entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_PASSIF)
                    || entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_PRODUIT)) {
                result.passif = new FWCurrency(solde.toString());
            } else {
                if (solde.compareTo(new FWCurrency(0)) == 1) {
                    result.actif = new FWCurrency(solde.toString());
                } else if (solde.compareTo(new FWCurrency(0)) == -1) {
                    result.passif = new FWCurrency(solde.toString());
                }
            }
        }
        return result;
    }

    public CGMontantHelper computeSoldeFictifCumule(globaz.globall.db.BTransaction transaction, String idExerComptable,
            String idPeriode, String idCentreCharge, boolean isProvisoire) throws Exception {

        // Solde actuel
        globaz.framework.util.FWCurrency solde = new globaz.framework.util.FWCurrency();

        CGMontantHelper result = new CGMontantHelper();

        Iterator listComptes = null;
        listComptes = getListComptesIterator(transaction, null);

        if (listComptes != null) {
            CGCompte entity = null;
            while (listComptes.hasNext()) {
                CGCompte compte = (CGCompte) listComptes.next();
                if (entity == null) {
                    entity = compte;
                }
                // TODO Faire que les exceptions aux comptes fictifs ne soient
                // pas ajoutés ici
                // System.out.println("IdSecteurAVS : " + compte.getIdSecteurAVS() + " / NuméroCompteAVS : "
                // + compte.getNumeroCompteAVS());
                if (compte.belongsToCompteFictif()) {
                    solde.add(CGSolde.computeSoldeCumule(idExerComptable, compte.getIdCompte(), idPeriode,
                            idCentreCharge, getSession(), isProvisoire).doubleValue());
                }
            }

            // On ne prend pas en compte les comptes avec un solde à zéro.
            if (JadeStringUtil.isDecimalEmpty(solde.toString())) {
                // ||
                // solde.intValue()==0)
                return result;
            }

            // Les comptes contenu dans la liste de comptes sont tous du même
            // genre.
            if (entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_ACTIF)
                    || entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_CHARGE)) {
                result.actif = new FWCurrency(solde.toString());
            } else if (entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_PASSIF)
                    || entity.getIdGenre().equals(globaz.helios.db.comptes.CGCompte.CS_GENRE_PRODUIT)) {
                result.passif = new FWCurrency(solde.toString());
            } else {
                if (solde.compareTo(new FWCurrency(0)) == 1) {
                    result.actif = new FWCurrency(solde.toString());
                } else if (solde.compareTo(new FWCurrency(0)) == -1) {
                    result.passif = new FWCurrency(solde.toString());
                }
            }
        }
        return result;
    }

    @Override
    public BManager[] getChilds() {
        return null;
    }

    /**
     * Getter
     */
    public java.lang.String getIdCompteOfas() {
        return idCompteOfas;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:50:53)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDomaine() {
        return idDomaine;
    }

    public java.lang.String getIdExterne() {
        return idExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:51:22)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdGenre() {
        return idGenre;
    }

    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:51:09)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdNature() {
        return idNature;
    }

    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    @Override
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    @Override
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    @Override
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    public CGCompte[] getListComptes(globaz.globall.db.BTransaction transaction, String idDomaine) throws Exception {

        List result = new ArrayList();
        // Récupération des comptes apppartenant au(X) compte(s) OFAS(fictif)
        if (getIdNature().equals(CGCompteOfas.CS_NATURE_FICTIF)) {
            // Récupération de tous les comptes OFAS appartenant à ce compte
            // fictif.

            CGCompteOfasManager compteOfasMgr = new CGCompteOfasManager();
            compteOfasMgr.setSession(getSession());
            compteOfasMgr.setForIdMandat(idMandat);
            String idExterneStartWith = getIdExterne().substring(0, 5);

            compteOfasMgr.setBeginWithIdExterne(idExterneStartWith);
            compteOfasMgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < compteOfasMgr.size(); i++) {
                CGCompteOfas compteOfas = (CGCompteOfas) compteOfasMgr.getEntity(i);

                if (!compteOfas.getIdCompteOfas().equals(getIdCompteOfas())) {
                    // Parse la liste des liens compteOfas / comptes
                    CGCompteCompteOfasManager cptCptOfasMgr = new CGCompteCompteOfasManager();
                    cptCptOfasMgr.setSession(getSession());
                    cptCptOfasMgr.setForIdCompteOfas(compteOfas.getIdCompteOfas());
                    cptCptOfasMgr.find(transaction, BManager.SIZE_NOLIMIT);

                    for (int j = 0; j < cptCptOfasMgr.size(); j++) {
                        CGCompteCompteOfas cptCptOfas = (CGCompteCompteOfas) cptCptOfasMgr.getEntity(j);

                        CGCompte compte = new CGCompte();
                        compte.setSession(getSession());
                        compte.setIdCompte(cptCptOfas.getIdCompte());
                        if (idDomaine != null) {
                            compte.setIdDomaine(idDomaine);
                        }
                        compte.retrieve(transaction);
                        if (compte == null) {
                            throw (new Exception(getSession().getLabel("COMPTE_OFAS_COMPTE_INEXISTANT")
                                    + cptCptOfas.getIdCompte()));
                        }
                        result.add(compte);
                    }
                }
            }
        } else {
            // Parse la liste des liens compteOfas / comptes
            CGCompteCompteOfasManager cptCptOfasMgr = new CGCompteCompteOfasManager();
            cptCptOfasMgr.setSession(getSession());
            cptCptOfasMgr.setForIdCompteOfas(getIdCompteOfas());
            cptCptOfasMgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (int j = 0; j < cptCptOfasMgr.size(); j++) {
                CGCompteCompteOfas cptCptOfas = (CGCompteCompteOfas) cptCptOfasMgr.getEntity(j);

                CGCompte compte = new CGCompte();
                compte.setSession(getSession());
                compte.setIdCompte(cptCptOfas.getIdCompte());
                compte.retrieve(transaction);
                result.add(compte);
            }
        }
        return (CGCompte[]) result.toArray(new CGCompte[0]);
    }

    public Iterator getListComptesIterator(globaz.globall.db.BTransaction transaction, String idDomaine)
            throws Exception {
        CGHelperCompteOfasManager manager = new CGHelperCompteOfasManager();
        manager.setSession(getSession());
        manager.setForIdCompteOfas(getIdCompteOfas());
        manager.setForIdMandat(getIdMandat());
        String idExterneStartWith = getIdExterne().substring(0, 5);
        manager.setFromIdExterne(idExterneStartWith);
        manager.setIdNature(getIdNature());
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        return manager.getContainer().iterator();
    }

    public java.lang.Boolean isNonAnnoncable() {
        return nonAnnoncable;
    }

    /**
     * Setter
     */
    public void setIdCompteOfas(java.lang.String newIdCompteOfas) {
        idCompteOfas = newIdCompteOfas;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:50:53)
     * 
     * @param newIdDomaine
     *            java.lang.String
     */
    public void setIdDomaine(java.lang.String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    public void setIdExterne(java.lang.String newIdExterne) {
        idExterne = newIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:51:22)
     * 
     * @param newIdGenre
     *            java.lang.String
     */
    public void setIdGenre(java.lang.String newIdGenre) {
        idGenre = newIdGenre;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 16:51:09)
     * 
     * @param newIdNature
     *            java.lang.String
     */
    public void setIdNature(java.lang.String newIdNature) {
        idNature = newIdNature;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    public void setNonAnnoncable(java.lang.Boolean newNonAnnoncable) {
        nonAnnoncable = newNonAnnoncable;
    }

}
