package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.util.JadePersistenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class APImportationAPGHistorique extends BEntity {

    private static final Logger LOG = LoggerFactory.getLogger(APImportationAPGHistorique.class);

    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID = "ID";
    public static final String FIELDNAME_NSS = "NSS";
    public static final String FIELDNAME_FICHIER_XML = "FICHIER_XML";
    public static final String FIELDNAME_ETAT = "STATE";
    public static final String FIELDNAME_TYPE_APG = "TYPE_APG";
    public static final String FIELDNAME_PSPY = "PSPY";

    private static final String[] tupples = {FIELDNAME_ID, FIELDNAME_NSS, FIELDNAME_FICHIER_XML, FIELDNAME_ETAT, FIELDNAME_TYPE_APG, FIELDNAME_PSPY};

    public static final String TABLE_NAME = "HISTORIQUE_IMPORTATION_APG";


    private String idHistoriqueAPG;
    private String nss;
    private InputStream xmlFile;
    private String etatDemande;
    private String typeDemande;
    private String pspy;

    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idHistoriqueAPG = statement.dbReadNumeric(FIELDNAME_ID);
        nss = statement.dbReadString(FIELDNAME_NSS);
        etatDemande = statement.dbReadNumeric(FIELDNAME_ETAT);
        typeDemande = statement.dbReadString(FIELDNAME_TYPE_APG);
        pspy = statement.dbReadString(FIELDNAME_PSPY);

        // Récupération du BLOB en Db
        getXmlFileFromDb();
    }

    private void getXmlFileFromDb(){
        String sql = getSqlSelect(FIELDNAME_FICHIER_XML);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
            pstmt.setInt(1, Integer.valueOf(idHistoriqueAPG));
            rs = pstmt.executeQuery();
            if(rs.next()) {
                xmlFile = rs.getBinaryStream(FIELDNAME_FICHIER_XML);
            }
        } catch (SQLException  e) {
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

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdHistoriqueAPG(this._incCounter(transaction, idHistoriqueAPG, _getTableName()));
    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        PreparedStatement pstmt = null;
        String sql = getSqlUpdate();
        try {
            pstmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
            pstmt.setBinaryStream(1, xmlFile, xmlFile.available());
            pstmt.setInt(2, Integer.valueOf(idHistoriqueAPG));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.debug("Erreur à l'execution de la requête : " + sql, e);
        } catch (IOException e) {
            LOG.debug("Erreur lors de la lecture du XML", e);
        } catch (Exception e) {
            LOG.debug("Erreur lors de la lecture du XML", e);
        } finally {
            closeStatement(pstmt);
        }
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
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append(TABLE_NAME + " SET " + FIELDNAME_FICHIER_XML + " = ? ");
        sql.append("WHERE " + FIELDNAME_ID + " = ?");
        return sql.toString();
    }

    private static String getSqlSelect(String fieldName) {
//        StringBuffer sql = new StringBuffer("SELECT " + fieldName + " FROM ");
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append(TABLE_NAME);
        sql.append(" WHERE " + FIELDNAME_ID + " = ?");
        return sql.toString();
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID,
                this._dbWriteNumeric(statement.getTransaction(), idHistoriqueAPG, "idHistoriqueAPG"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID,
                this._dbWriteNumeric(statement.getTransaction(), idHistoriqueAPG, ""));
        statement.writeField(FIELDNAME_NSS,
                this._dbWriteString(statement.getTransaction(), nss, ""));
        statement.writeField(FIELDNAME_ETAT,
                this._dbWriteString(statement.getTransaction(), etatDemande, ""));
        statement.writeField(FIELDNAME_TYPE_APG,
                this._dbWriteString(statement.getTransaction(), typeDemande, ""));
    }

    public String getIdHistoriqueAPG() {
        return idHistoriqueAPG;
    }

    public void setIdHistoriqueAPG(String idHistoriqueAPG) {
        this.idHistoriqueAPG = idHistoriqueAPG;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getEtatDemande() {
        return etatDemande;
    }

    public String gettypeDemande() {
        return typeDemande;
    }

    public void setEtatDemande(String etatDemande) {
        this.etatDemande = etatDemande;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    public String getPspy() {
        return pspy;
    }

    public void setPspy(String pspy) {
        this.pspy = pspy;
    }

    public InputStream getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(InputStream xmlFile) {
        this.xmlFile = xmlFile;
    }
}
