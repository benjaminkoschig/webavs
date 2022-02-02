package ch.globaz.eform.business.models;

import ch.globaz.common.util.Dates;
import globaz.apg.db.prestation.APPrestation;
import globaz.common.util.CommonBlobUtils;
import globaz.globall.db.*;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.util.JadePersistenceUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipOutputStream;

@Slf4j
@Getter
@Setter
public class GFFormulaireModel extends BEntity {

    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_FORMULAIRE_ID = "FORMULAIRE_ID";
    public static final String FIELDNAME_FORMULAIRE_SUBJECT = "FORMULAIRE_SUBJECT";
    public static final String FIELDNAME_FORMULAIRE_DATE = "FORMULAIRE_DATE";
    public static final String FIELDNAME_FORMULAIRE_TYPE = "FORMULAIRE_TYPE";
    public static final String FIELDNAME_FORMULAIRE_NOM = "FORMULAIRE_NOM";
    public static final String FIELDNAME_BENEFICIAIRE_NSS = "BENEFICIAIRE_NSS";
    public static final String FIELDNAME_BENEFICIAIRE_NOM = "BENEFICIAIRE_NOM";
    public static final String FIELDNAME_BENEFICIAIRE_PRENOM = "BENEFICIAIRE_PRENOM";
    public static final String FIELDNAME_BENEFICIAIRE_DATE_NAISSANCE = "BENEFICIAIRE_DATE_NAISSANCE";
    public static final String FIELDNAME_FICHIER_ZIP = "FICHIER_ZIP";
    public static final String TABLE_NAME_GF_FORMULAIRE = "GF_FORMULAIRE";

    private String formulaireId;
    private String typeFormulaire;
    private String nomFormulaire;
    private String formulaireSubject;
    private LocalDate formulaireDate;
    private String nssBeneficiaire;
    private String nomBeneficiaire;
    private String prenomBeneficiaire;
    private LocalDate dateNaissanceBeneficiaire;
    private InputStream fichierZip;

    @Override
    protected String _getTableName() {
        return TABLE_NAME_GF_FORMULAIRE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        formulaireId = statement.dbReadString(FIELDNAME_FORMULAIRE_ID);
        formulaireSubject = statement.dbReadString(FIELDNAME_FORMULAIRE_SUBJECT);
        formulaireDate = Dates.toDateFromDb(statement.dbReadString(FIELDNAME_FORMULAIRE_DATE));
        typeFormulaire = statement.dbReadString((FIELDNAME_FORMULAIRE_TYPE));
        nomFormulaire = statement.dbReadString(FIELDNAME_FORMULAIRE_NOM);
        nssBeneficiaire = statement.dbReadString(FIELDNAME_BENEFICIAIRE_NSS);
        nomBeneficiaire = statement.dbReadString(FIELDNAME_BENEFICIAIRE_NOM);
        prenomBeneficiaire = statement.dbReadString(FIELDNAME_BENEFICIAIRE_PRENOM);
        dateNaissanceBeneficiaire = Dates.toDate(FIELDNAME_BENEFICIAIRE_DATE_NAISSANCE);
        // Récupération du BLOB en Db
        getZipFileFromDb();
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(GFFormulaireModel.FIELDNAME_FORMULAIRE_ID,
                this._dbWriteString(statement.getTransaction(), formulaireId, "formulaireId"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_FORMULAIRE_ID,
                this._dbWriteString(statement.getTransaction(), formulaireId, ""));
        statement.writeField(FIELDNAME_FORMULAIRE_SUBJECT,
                this._dbWriteString(statement.getTransaction(), formulaireSubject, ""));
        statement.writeField(FIELDNAME_FORMULAIRE_DATE,
                this._dbWriteString(statement.getTransaction(), Dates.toDbDate(formulaireDate), ""));
        statement.writeField(FIELDNAME_FORMULAIRE_TYPE,
                this._dbWriteString(statement.getTransaction(),typeFormulaire, ""));
        statement.writeField(FIELDNAME_FORMULAIRE_NOM,
                this._dbWriteString(statement.getTransaction(),nomFormulaire, ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_NOM,
                this._dbWriteString(statement.getTransaction(), nomBeneficiaire, ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_PRENOM,
                this._dbWriteString(statement.getTransaction(), prenomBeneficiaire, ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_DATE_NAISSANCE,
                this._dbWriteString(statement.getTransaction(), Dates.toDbDate(dateNaissanceBeneficiaire), ""));
        statement.writeField(FIELDNAME_BENEFICIAIRE_NSS,
                this._dbWriteString(statement.getTransaction(), nssBeneficiaire, ""));
    }


    @Override
    protected void _validate(BStatement statement) throws Exception {

    }

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
//        PreparedStatement pstmt = null;
//
//        String sql = getSqlUpdate();
//        try {
//            pstmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
//            pstmt.setBinaryStream(1, fichierZip, fichierZip.available());
//            pstmt.setString(2, formulaireId);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            LOG.debug("Erreur à l'execution de la requête : " + sql, e);
//        } catch (Exception e) {
//            LOG.debug("Erreur lors de la lecture du ZIP", e);
//        } finally {
//            closeStatement(pstmt);
//        }
    }

    private void getZipFileFromDb(){
        String sql = getSqlSelect();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
            pstmt.setString(1, formulaireId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                fichierZip = rs.getBinaryStream(FIELDNAME_FICHIER_ZIP);
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
        sql.append(TABLE_NAME_GF_FORMULAIRE);
        sql.append(" WHERE " + FIELDNAME_FORMULAIRE_ID + " = ?");
        return sql.toString();
    }

    private static String getSqlUpdate() {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append(TABLE_NAME_GF_FORMULAIRE + " SET " + FIELDNAME_FICHIER_ZIP + " = ? ");
        sql.append("WHERE " + FIELDNAME_FORMULAIRE_ID + " = ?");
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

    public List<GFAttachementModel> getAttachement(BTransaction transaction) throws Exception {
        List<GFAttachementModel> attachements = new ArrayList<>();
        GFAttachementModelManager manager = new GFAttachementModelManager();
        manager.setForFormulaireId(this.formulaireId);
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < manager.size(); i++) {
            attachements.add((GFAttachementModel)manager.getEntity(i));
        }
        return attachements;
    }
}
