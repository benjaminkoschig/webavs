package ch.globaz.eform.business.models;

import globaz.apg.db.droits.APDroitPanSituation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.util.JadePersistenceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Slf4j
public class GFAttachementModel extends BEntity {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME_GF_ATTACHEMENT = "GF_ATTACHEMENT";
    public static final String FIELDNAME_ATTACHEMENT_ID = "ATTACHEMENT_ID";
    public static final String FIELDNAME_FORMULAIRE_ID = "FORMULAIRE_ID";
    public static final String FIELDNAME_NOM = "NOM";
    public static final String FIELDNAME_FICHIER = "FICHIER";

    private String attachementId = "";
    private String attachementNom = "";
    private InputStream attachementFichier;
    private String formulaireId = "";

    @Override
    protected String _getTableName() {
        return TABLE_NAME_GF_ATTACHEMENT;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        attachementId = statement.dbReadNumeric(FIELDNAME_ATTACHEMENT_ID);
        attachementNom = statement.dbReadString(FIELDNAME_NOM);
        formulaireId = statement.dbReadString(statement.dbReadString(FIELDNAME_FORMULAIRE_ID));
        getFichierFromDb();
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ATTACHEMENT_ID,
                this._dbWriteNumeric(statement.getTransaction(), attachementId, "attachementId"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_FORMULAIRE_ID,
                this._dbWriteString(statement.getTransaction(), formulaireId, "formulaireId"));
        statement.writeField(FIELDNAME_NOM,
                this._dbWriteString(statement.getTransaction(), attachementNom, "attachementNom"));
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setAttachementId(this._incCounter(transaction, attachementId, "attachementId"));
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
//        PreparedStatement pstmt = null;
//        String sql = getSqlUpdate();
//        try {
//            pstmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
//            pstmt.setBinaryStream(1, attachementFichier, attachementFichier.available());
//            pstmt.setString(2, attachementId);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            LOG.debug("Erreur à l'execution de la requête : " + sql, e);
//        } catch (Exception e) {
//            LOG.debug("Erreur lors de la lecture du ZIP", e);
//        } finally {
//            closeStatement(pstmt);
//        }
    }

    private void getFichierFromDb(){
        String sql = getSqlSelect();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
            pstmt.setString(1, formulaireId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                attachementFichier = rs.getBinaryStream(FIELDNAME_FICHIER);
            }
        } catch (SQLException e) {
            LOG.debug("Erreur à l'execution de la requête : " + sql, e);
        } finally {
            closeStatement(pstmt);
            try {
                if (Objects.nonNull(rs)) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.debug("Erreur lors de la fermeture du resultSet : " + sql, e);
            }
        }
    }

    private static String getSqlSelect() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append(TABLE_NAME_GF_ATTACHEMENT);
        sql.append(" WHERE " + FIELDNAME_ATTACHEMENT_ID + " = ?");
        return sql.toString();
    }

    private static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.warn("Problem to close statement, reason : " + e);
            }
        }
    }

    private static String getSqlUpdate() {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append(TABLE_NAME_GF_ATTACHEMENT + " SET " + FIELDNAME_FICHIER + " = ? ");
        sql.append("WHERE " + FIELDNAME_ATTACHEMENT_ID + " = ?");
        return sql.toString();
    }

    public void setAttachementId(String attachementId){
        this.attachementId = attachementId;
    }

    public void setNom(String nomAttachement){
        attachementNom = nomAttachement;
    }

    public void setFichier(InputStream file){
        attachementFichier = file;
    }

    public void setFormulaireId(String id){
        formulaireId = id;
    }

    public String getAttachementId(){
        return attachementId;
    }

    public String getNom(){
        return attachementNom;
    }

    public InputStream getFichier(){
        return attachementFichier;
    }

    public String getFormulaireId(){
        return formulaireId;
    }
}
