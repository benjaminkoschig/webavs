package globaz.osiris.db.comptes;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIPaiement;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISlave;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (06.02.2002 15:35:22)
 * 
 * @author: Administrator
 */
public class CAPaiement extends CAEcriture implements APIPaiement {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAPaiement() {
        super();
        setIdTypeOperation(APIOperation.CAPAIEMENT);
    }

    public CAPaiement(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAPAIEMENT);
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
        setIdTypeOperation(APIOperation.CAPAIEMENT);
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(BTransaction, String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Cr�ation d'un double de l'�criture
        CAPaiement extourne = new CAPaiement();
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
    @Override
    public void _synchroChgValUtili() {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector<String>(11);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAPaiement", getNomEcran());
                // chargement des propri�t�s internes si idExterneRoleEcran est
                // vide

                if (JadeStringUtil.isBlank(getIdExterneRoleEcran()) && !valeurUtilisateur.isEmpty()) {
                    if (valeurUtilisateur.size() >= 1) {
                        getMapValeurUtilisateur().put("idExterneRoleEcran", valeurUtilisateur.elementAt(0));
                    }
                    if (valeurUtilisateur.size() >= 2) {
                        setIdRoleEcran(valeurUtilisateur.elementAt(1));
                    }
                    if (valeurUtilisateur.size() >= 3) {
                        getMapValeurUtilisateur().put("idExterneSectionEcran", valeurUtilisateur.elementAt(2));
                    }
                    if (valeurUtilisateur.size() >= 4) {
                        setIdTypeSectionEcran(valeurUtilisateur.elementAt(3));
                    }
                    if (valeurUtilisateur.size() >= 5) {
                        getMapValeurUtilisateur().put("date", valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        getMapValeurUtilisateur().put("idExterneRubriqueEcran", valeurUtilisateur.elementAt(5));
                    }
                    if (valeurUtilisateur.size() >= 7) {
                        setCodeDebitCredit(valeurUtilisateur.elementAt(6));
                    }
                    if (valeurUtilisateur.size() >= 8) {
                        getMapValeurUtilisateur().put("libelle", valeurUtilisateur.elementAt(7));
                    }
                    if (valeurUtilisateur.size() >= 9) {
                        getMapValeurUtilisateur().put("piece", valeurUtilisateur.elementAt(8));
                    }

                    if (valeurUtilisateur.size() >= 10) {
                        getMapValeurUtilisateur().put("montantEcran", valeurUtilisateur.elementAt(9));
                    }

                    if (valeurUtilisateur.size() >= 11) {
                        getMapValeurUtilisateur().put("idExterneCompteCourantEcran", valeurUtilisateur.elementAt(10));
                    }
                }
            }
        }
    }

    /**
     * mise � jour du fichier AJPPVUT pour les valeur par d�faut par utilisateur
     */
    protected void _synchroValUtili(globaz.globall.db.BTransaction transaction) {
        // mise � jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector<String>(11);
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
            valeurUtilisateur.add(9, getMontantEcran());
            valeurUtilisateur.add(10, getIdExterneCompteCourantEcran());

            // mise � jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAPaiement", getNomEcran(), valeurUtilisateur);
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
        if (!isInstanceOrSubClassOf(APIOperation.CAPAIEMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("5165"));
        }

        // Test si c'est une operation auxiliaire et si la section auxiliaire
        // n'est pas saisie
        if (!getCodeMaster().equals(APIOperation.SLAVE) && isSectionPrincipale()
                && JadeStringUtil.isBlankOrZero(getIdSectionAux())
                && !isInstanceOrSubClassOf(APIOperation.CAPAIEMENTBVR)) {
            _addError(statement.getTransaction(), getSession().getLabel("7395"));
        }
    }

    /**
     * Validation des donn�es Date de cr�ation : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les donn�es de la superclasse
        super._valider(transaction);

        // Le rubrique doit �tre un compte financier
        if (getCompte() != null) {
            if (!getCompte().getNatureRubrique().equals(APIRubrique.COMPTE_FINANCIER)) {
                getMemoryLog().logMessage("5134", null, FWMessage.ERREUR, this.getClass().getName());
            }
        }

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.02.2002 17:41:49)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntSlave
     */
    @Override
    protected APISlave createSlave() {
        CAPaiement ecr = new CAPaiement();
        ecr.dupliquer(this);
        return ecr;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:55:05)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAPaiement
     */
    public void dupliquer(CAPaiement oper) {

        // Dupliquer les param�tres de la superclasse
        super.dupliquer(oper);
    }

    /**
     * V�rifie si c'est un paiement sur une section qui a des sections auxiliares. <br />
     * Si c'est une op�ration auxiliaire et que la section auxiliaire n'est pas renseign�e, <br />
     * retourne true.
     * 
     * @author: sel Cr�� le : 12 f�vr. 07
     * @param oper
     * @return true si c'est une op�ration auxiliaire ou s'il y a une erreu
     */
    protected boolean isSectionPrincipale() {
        // Test pour un paiement provenant d'op�rations auxiliaires
        if ((getSection() != null) && !JadeStringUtil.isIntegerEmpty(getSection().getIdSection())) {
            CASectionManager manager = new CASectionManager();
            manager.setSession(getSession());
            manager.setForIdSectionPrinc(getSection().getIdSection());

            try {
                return manager.getCount() > 0;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}
