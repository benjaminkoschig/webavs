package ch.globaz.orion.db;

import globaz.globall.db.BStatement;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class EBDemandeModifAcompteMessageEntity extends JadeEntity {

    private static final long serialVersionUID = 1L;

    public static final int AK_ID_DEMANDE = 1;

    private String id;
    private String idDemande;
    private String messageErreur;

    @Override
    protected void writeProperties() {
        writeStringAsNumeric(EBDemandeModifAcompteMessageDefTable.IDDEMANDE, idDemande);
        write(EBDemandeModifAcompteMessageDefTable.MESSAGE, messageErreur);

    }

    @Override
    protected void readProperties() {
        id = this.readString(EBDemandeModifAcompteMessageDefTable.ID);
        idDemande = this.readString(EBDemandeModifAcompteMessageDefTable.IDDEMANDE);
        messageErreur = this.readString(EBDemandeModifAcompteMessageDefTable.MESSAGE);
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        switch (alternateKey) {
            case AK_ID_DEMANDE:
                statement.writeKey("ID_DEMANDE",
                        _dbWriteString(statement.getTransaction(), String.valueOf(getIdDemande())));
                break;
        }
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return EBDemandeModifAcompteMessageDefTable.class;
    }

    @Override
    public String getIdEntity() {
        return id;
    }

    @Override
    public void setIdEntity(String id) {
        this.id = id;

    }

    public String getIdErreur() {
        return id;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdErreur(String idErreur) {
        id = idErreur;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * Génération de la clause from pour la requête > Jointure depuis les demandes de modification vers les messages
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(EBDemandeModifAcompteDefTable.TABLE);

        // jointure entre la table des dossiers et la table des demandes
        // prestation
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(EBDemandeModifAcompteMessageDefTable.TABLE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(EBDemandeModifAcompteDefTable.TABLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(EBDemandeModifAcompteDefTable.ID);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(EBDemandeModifAcompteMessageDefTable.TABLE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(EBDemandeModifAcompteMessageDefTable.IDDEMANDE);

        return fromClauseBuffer.toString();
    }

}
