package ch.globaz.orion.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;

public class EBDemandeModifAcompteMessageManager extends JadeManager<EBDemandeModifAcompteMessageEntity> {

    private static final long serialVersionUID = 1L;
    private Integer forIdDemande;
    private String fromClause;

    /**
     * Renvoie la clause FROM (par défaut, la clause FROM de l'entité).
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = EBDemandeModifAcompteMessageEntity.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new EBDemandeModifAcompteMessageEntity();
    }

    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        sqlWhere.and(EBDemandeModifAcompteMessageDefTable.IDDEMANDE).equal(forIdDemande);
    }

    public Integer getForIdDemande() {
        return forIdDemande;
    }

    public void setForIdDemande(Integer forIdDemande) {
        this.forIdDemande = forIdDemande;
    }
}
