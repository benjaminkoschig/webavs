package globaz.helios.db.classifications;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGPlanComptableManager;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.interfaces.ITreeListable;
import java.util.ArrayList;

public class CGLiaisonCompteClasse extends CGNeedExerciceComptable implements ITreeListable, java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idClasseCompte = new String();
    private java.lang.String idCompte = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGLiaisonCompteClasse
     */
    public CGLiaisonCompteClasse() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

        // On contrôle que la liaison que l'on veut créer entre le compte la
        // classe de compte
        // soit unique par classification.

        CGLiaisonCompteClasseManager mgr = new CGLiaisonCompteClasseManager();
        mgr.setSession(getSession());
        mgr.setForIdCompte(getIdCompte());
        mgr.find(transaction);

        ArrayList classifications = new ArrayList();
        for (int i = 0; i < mgr.size(); i++) {
            CGLiaisonCompteClasse entity = (CGLiaisonCompteClasse) mgr.getEntity(i);

            CGClasseCompte cc = new CGClasseCompte();
            cc.setSession(getSession());
            cc.setIdClasseCompte(entity.getIdClasseCompte());
            cc.retrieve(transaction);
            classifications.add(cc.getIdClassification());
        }

        CGClasseCompte cc = new CGClasseCompte();
        cc.setSession(getSession());
        cc.setIdClasseCompte(getIdClasseCompte());
        cc.retrieve(transaction);

        // Si ce compte porte déjà sur la même classification, -> erreur
        if (classifications.contains(cc.getIdClassification())) {
            _addError(transaction, getSession().getLabel("LIAISON_CC_CPTES_CLASSIFICATION_ERROR"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGCPGRP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idClasseCompte = statement.dbReadNumeric("IDCLASSECOMPTE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
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
        statement.writeKey("IDCLASSECOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdClasseCompte(), ""));
        statement.writeKey("IDCOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdCompte(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCLASSECOMPTE",
                _dbWriteNumeric(statement.getTransaction(), getIdClasseCompte(), "idClasseCompte"));
        statement.writeField("IDCOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
    }

    @Override
    public BManager[] getChilds() {

        CGClasseCompteManager classeCompteManager = new CGClasseCompteManager();
        classeCompteManager.setForIdClasseCompte(idClasseCompte);
        CGPlanComptableManager compteManager = new CGPlanComptableManager();
        compteManager.setForIdCompte(idCompte);
        return new BManager[] { classeCompteManager, compteManager };
    }

    /**
     * Getter
     */
    public java.lang.String getIdClasseCompte() {
        return idClasseCompte;
    }

    public java.lang.String getIdCompte() {
        return idCompte;
    }

    @Override
    public String getLibelle() {
        return "idClasseCompte:" + getIdClasseCompte() + " - idCompte:" + getIdCompte();
    }

    /**
     * Setter
     */
    public void setIdClasseCompte(java.lang.String newIdClasseCompte) {
        idClasseCompte = newIdClasseCompte;
    }

    public void setIdCompte(java.lang.String newIdCompte) {
        idCompte = newIdCompte;
    }
}
