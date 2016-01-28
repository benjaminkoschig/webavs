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
 * Ins�rez la description du type ici. Date de cr�ation : (13.02.2002 10:03:41)
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
     * Effectue des traitements apr�s un ajout dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s l'ajout de l'entit� dans la BD
     * <p>
     * La transaction n'est pas valid�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_afterAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise � jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
    }

    /**
     * Effectue des traitements apr�s une mise � jour dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la mise � jour de l'entit� dans la BD
     * <p>
     * La transaction n'est pas valid�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise � jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        // Forcer le type d'op�ration
        setIdTypeOperation(APIOperation.CAVERSEMENT);
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(BTransaction, String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Cr�ation d'un double de l'�criture
        CAVersement extourne = new CAVersement();
        extourne.dupliquer(this);
        // Libell� du texte si saisi
        if (!JadeStringUtil.isBlank(text) && (text.length() > 40)) {
            extourne.setLibelle(text.substring(0, 40));
        } else {
            extourne.setLibelle(text);
        }
        // Inverser le signe
        CAEcriture.inverserCodeDebitCredit(extourne);
        // Retourner l'op�ration
        return extourne;
    }

    /**
     * Chargement des valeurs par d�faut par utilisateur
     */
    protected void _synchroChgValUtili(globaz.globall.db.BTransaction transaction) {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector(10);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAVersement", getNomEcran());
                // chargement des propri�t�s internes si idExterneRoleEcran est
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
     * mise � jour du fichier AJPPVUT pour les valeur par d�faut par utilisateur
     */
    @Override
    protected void _synchroValUtili(globaz.globall.db.BTransaction transaction) {
        // mise � jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector(10);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des donn�es � m�moriser dans le vecteur
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

            // mise � jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAVersement", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.02.2002 08:50:10)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // V�rifier le type d'op�ration
        if (!isInstanceOrSubClassOf(APIOperation.CAVERSEMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("5164"));
        }

    }

    /**
     * Validation des donn�es Date de cr�ation : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les donn�es de la superclasse
        super._valider(transaction);

        // Fin s'il y a des erreurs
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            return;
        }

        // V�rifier qu'il s'agit d'un d�bit / extourne d�bit
        if (!getCodeDebitCredit().equals(APIEcriture.DEBIT) && !getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
            getMemoryLog().logMessage("5153", getIdCompte(), FWMessage.ERREUR, this.getClass().getName());
        }

        // V�rifier l'id ordre de versement
        if (JadeStringUtil.isIntegerEmpty(getIdOrdreVersement())) {
            getMemoryLog().logMessage("5154", getIdCompte(), FWMessage.ERREUR, this.getClass().getName());
        } else if (getOrdreVersement() == null) {
            getMemoryLog().logMessage("5155", getIdCompte(), FWMessage.ERREUR, this.getClass().getName());
        }

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.03.2002 09:29:02)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.02.2002 17:39:24)
     */
    @Override
    protected APISlave createSlave() {
        CAVersement ecr = new CAVersement();
        ecr.dupliquer(this);
        return ecr;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:53:53)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAVersement
     */
    public void dupliquer(CAVersement oper) {

        // Dupliquer les param�tres de la superclasse
        super.dupliquer(oper);

        // Copier les autres param�tres
        if (oper != null) {
            setIdOrdreVersement(oper.getIdOrdreVersement());
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:09:40)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdOrdreVersement() {
        return idOrdre;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:14:40)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:09:55)
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
