package ch.globaz.orion.db;

import globaz.globall.db.BStatement;

public class EBDemandeModifAcompteJoinDecisionEntity extends EBDemandeModifAcompteEntity {
    private String typeDecision;
    private Integer id;

    @Override
    protected void readProperties() {
        super.readProperties();
        id = this.read(EBDemandeModifAcompteJoinDecisionDefTable.ID);
        typeDecision = readString(EBDemandeModifAcompteJoinDecisionDefTable.TYPE_DECISION);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement);
    }

    public String getTypeDecision() {
        return typeDecision;
    }
}
