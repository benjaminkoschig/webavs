package globaz.hercule.db.groupement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author JMC
 * @since 2 juin 2010
 */
public class CEGroupe extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ANNEE_COUVERTURE_MINIMAL = "CEDCMI";

    public static final String FIELD_IDGROUPE = "CEIDGR";
    public static final String FIELD_LIBELLE = "CELGRP";
    public static final String TABLE_CEGRPP = "CEGRPP";

    private String anneeCouvertureMinimal = "";
    private String idGroupe = "";
    private String libelleGroupe = "";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {

        if (CEControleEmployeurService.retrieveIdGroupeWithLibelle(getSession(), getLibelleGroupe()) != null) {
            _addError(transaction, getSession().getLabel("ERR_GROUPE_EXISTANT"));
        }

        if (JadeStringUtil.isBlankOrZero(idGroupe)) {
            setIdGroupe(this._incCounter(transaction, "0"));
        }
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {

        if (CEControleEmployeurService.countMembreForIdGroupe(getSession(), getIdGroupe()) > 0) {
            _addError(transaction, getSession().getLabel("ERR_MEMBRE_PRESENT"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CEGroupe.TABLE_CEGRPP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idGroupe = statement.dbReadNumeric(CEGroupe.FIELD_IDGROUPE);
        libelleGroupe = statement.dbReadString(CEGroupe.FIELD_LIBELLE);
        anneeCouvertureMinimal = statement.dbReadNumeric(CEGroupe.FIELD_ANNEE_COUVERTURE_MINIMAL);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(idGroupe)) {
            _addError(statement.getTransaction(), getSession().getLabel("ERR_NO_LIBELLE_GROUPE_VIDE"));
        }
        if (JadeStringUtil.isBlank(libelleGroupe)) {
            _addError(statement.getTransaction(), getSession().getLabel("ERR_LIBELLE_GROUPE_VIDE"));
        }
        if (JadeStringUtil.isBlank(anneeCouvertureMinimal)) {
            _addError(statement.getTransaction(), getSession().getLabel("ERR_ANNEE_COUVERTURE_GROUPE_VIDE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + ".CEIDGR",
                this._dbWriteNumeric(statement.getTransaction(), getIdGroupe(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField(CEGroupe.FIELD_IDGROUPE,
                this._dbWriteNumeric(statement.getTransaction(), idGroupe, "idGroupe"));
        statement.writeField(CEGroupe.FIELD_LIBELLE,
                this._dbWriteString(statement.getTransaction(), libelleGroupe, "libelleGroupe"));
        statement.writeField(CEGroupe.FIELD_ANNEE_COUVERTURE_MINIMAL,
                this._dbWriteNumeric(statement.getTransaction(), anneeCouvertureMinimal, "anneeCouverture"));

    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getAnneeCouvertureMinimal() {
        return anneeCouvertureMinimal;
    }

    public String getIdGroupe() {
        return idGroupe;
    }

    public String getLibelleGroupe() {
        return libelleGroupe;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAnneeCouvertureMinimal(String anneeCouvertureMinimal) {
        this.anneeCouvertureMinimal = anneeCouvertureMinimal;
    }

    public void setIdGroupe(String idGroupe) {
        this.idGroupe = idGroupe;
    }

    public void setLibelleGroupe(String libelleGroupe) {
        this.libelleGroupe = libelleGroupe;
    }

}
