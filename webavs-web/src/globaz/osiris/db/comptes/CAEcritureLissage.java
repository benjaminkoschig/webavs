package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcritureLissage;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APISlave;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAEcritureLissage extends CAEcriture implements APIEcritureLissage {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CAEcritureLissage() {
        super();
        setIdTypeOperation(APIOperation.CAECRITURELISSAGE);
    }

    public CAEcritureLissage(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAECRITURELISSAGE);
    }

    /**
     * @see globaz.osiris.db.comptes.CAEcriture#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // Let the super class handle the event
        super._beforeAdd(transaction);
        setIdTypeOperation(APIOperation.CAECRITURELISSAGE);
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(BTransaction, String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Création d'un double de l'écriture
        CAEcritureLissage extourne = new CAEcritureLissage();
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
     * @see globaz.osiris.db.comptes.CAEcriture#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) {
        // Laisser la superclasse effectuer son traitement
        super._validate(statement);
        // Vérifier le type d'opération
        if (!isInstanceOrSubClassOf(APIOperation.CAECRITURELISSAGE)) {
            _addError(statement.getTransaction(), getSession().getLabel("5165"));
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 17:41:49)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntSlave
     */
    @Override
    protected APISlave createSlave() {
        CAEcritureLissage ecr = new CAEcritureLissage();
        ecr.dupliquer(this);
        return ecr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 10:55:05)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAPaiement
     */
    public void dupliquer(CAEcritureLissage oper) {
        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);
    }

}
