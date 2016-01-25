package globaz.osiris.db.comptes;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APISlave;
import globaz.osiris.api.APIVersement;

/**
 * Insérez la description du type ici. Date de création : (13.02.2002 10:03:41)
 * 
 * @author: Administrator
 */
public class CAVersement extends CAPaiement implements APIVersement {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CAOperationOrdreVersement ordreVersement = null;

    public CAVersement() {
        super();
        setIdTypeOperation(APIOperation.CAVERSEMENT);
    }

    public CAVersement(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAVERSEMENT);
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
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
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
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
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
        setIdTypeOperation(APIOperation.CAVERSEMENT);
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(BTransaction, String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Création d'un double de l'écriture
        CAVersement extourne = new CAVersement();
        extourne.dupliquer(this);
        // Libellé du texte si saisi
        if (!JadeStringUtil.isBlank(text) && (text.length() > 40)) {
            extourne.setLibelle(text.substring(0, 40));
        } else {
            extourne.setLibelle(text);
        }
        // Inverser le signe
        CAEcriture.inverserCodeDebitCredit(extourne);
        // Retourner l'opération
        return extourne;
    }

    /**
     * Chargement des valeurs par défaut par utilisateur
     */
    protected void _synchroChgValUtili(globaz.globall.db.BTransaction transaction) {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector(10);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAVersement", getNomEcran());
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
                        setDate(valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        setIdExterneRubriqueEcran(valeurUtilisateur.elementAt(5));
                    }
                    if (valeurUtilisateur.size() >= 7) {
                        setCodeDebitCredit(valeurUtilisateur.elementAt(6));
                    }
                    if (valeurUtilisateur.size() >= 8) {
                        setLibelle(valeurUtilisateur.elementAt(7));
                    }
                    if (valeurUtilisateur.size() >= 9) {
                        setPiece(valeurUtilisateur.elementAt(8));
                    }
                    if (valeurUtilisateur.size() >= 10) {
                        setIdExterneCompteCourantEcran(valeurUtilisateur.elementAt(9));
                    }
                }
            }
        }
    }

    /**
     * mise à jour du fichier AJPPVUT pour les valeur par défaut par utilisateur
     */
    @Override
    protected void _synchroValUtili(globaz.globall.db.BTransaction transaction) {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector(10);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des données à mémoriser dans le vecteur
            valeurUtilisateur.removeAllElements();
            valeurUtilisateur.add(0, getIdExterneRoleEcran());
            valeurUtilisateur.add(1, getIdRoleEcran());
            valeurUtilisateur.add(2, getIdExterneSectionEcran());
            valeurUtilisateur.add(3, getIdTypeSectionEcran());
            valeurUtilisateur.add(4, getDate());
            valeurUtilisateur.add(5, getIdExterneRubriqueEcran());
            valeurUtilisateur.add(6, getCodeDebitCredit());
            valeurUtilisateur.add(7, getLibelle());
            valeurUtilisateur.add(8, getPiece());
            valeurUtilisateur.add(9, getIdExterneCompteCourantEcran());

            // mise à jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAVersement", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2002 08:50:10)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // Vérifier le type d'opération
        if (!isInstanceOrSubClassOf(APIOperation.CAVERSEMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("5164"));
        }

    }

    /**
     * Validation des données Date de création : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les données de la superclasse
        super._valider(transaction);

        // Fin s'il y a des erreurs
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            return;
        }

        // Vérifier qu'il s'agit d'un débit / extourne débit
        if (!getCodeDebitCredit().equals(APIEcriture.DEBIT) && !getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
            getMemoryLog().logMessage("5153", getIdCompte(), FWMessage.ERREUR, this.getClass().getName());
        }

        // Vérifier l'id ordre de versement
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreVersement())) {
            getMemoryLog().logMessage("5154", getIdCompte(), FWMessage.ERREUR, this.getClass().getName());
        } else if (getOrdreVersement() == null) {
            getMemoryLog().logMessage("5155", getIdCompte(), FWMessage.ERREUR, this.getClass().getName());
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.03.2002 09:29:02)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._writeProperties(statement);

        // Traitement interne
        statement.writeField(CAOperation.FIELD_IDORDRE,
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreVersement(), "idOrdre"));

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 17:39:24)
     */
    @Override
    protected APISlave createSlave() {
        CAVersement ecr = new CAVersement();
        ecr.dupliquer(this);
        return ecr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:53:53)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAVersement
     */
    public void dupliquer(CAVersement oper) {

        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);

        // Copier les autres paramètres
        if (oper != null) {
            setIdOrdreVersement(oper.getIdOrdreVersement());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:09:40)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdOrdreVersement() {
        return idOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:14:40)
     * 
     * @return globaz.osiris.db.comptes.CAOperationOrdreVersement
     */
    @Override
    public APIOperationOrdreVersement getOrdreVersement() {
        // Chargement si null
        if (ordreVersement == null) {
            ordreVersement = new CAOperationOrdreVersement();
            ordreVersement.setSession(getSession());
            ordreVersement.setIdOperation(getIdOrdreVersement());
            try {
                ordreVersement.retrieve();
                if (ordreVersement.isNew() || ordreVersement.hasErrors()) {
                    ordreVersement = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                ordreVersement = null;
            }
        }

        // Restituer l'ordre
        return ordreVersement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:09:55)
     * 
     * @param newIdOrdre
     *            java.lang.String
     */
    @Override
    public void setIdOrdreVersement(String newIdOrdreVersement) {
        // if (isUpdatable()) {
        idOrdre = newIdOrdreVersement;
        ordreVersement = null;
        // } else
        // _addError(null, getSession().getLabel("5133"));
    }

}
