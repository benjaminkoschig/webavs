package globaz.common.util;

import globaz.globall.db.BBlobMetadataProvider;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.mapping.JadeModelMappingProvider;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.persistence.util.JadeSqlBlobProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

public class CommonBlobUtils {

    public static void addBlob(String id, Serializable object, BTransaction transaction)
            throws JadePersistenceException {

        if (transaction == null) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the transaction is null");
        } else if (transaction.getConnection() == null) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the connection is null");
        } else if (JadeStringUtil.isBlank(id)) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the id passed is blank");
        }

        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;
        try {
            try {
                try {
                    baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = null;
                    try {
                        oos = new ObjectOutputStream(baos);
                        oos.writeObject(object);
                    } finally {
                        oos.close();
                    }
                    ByteArrayOutputStream zipBaos = null;
                    try {
                        try {
                            bais = new ByteArrayInputStream(baos.toByteArray());
                            zipBaos = new ByteArrayOutputStream();
                            JadeZipUtil.zip(zipBaos, id, bais);
                        } finally {
                            bais.close();
                        }
                        bais = new ByteArrayInputStream(zipBaos.toByteArray());
                    } finally {
                        zipBaos.close();
                    }

                } finally {
                    baos.close();
                }
                byte[] content = new byte[(int) BBlobMetadataProvider.getInstance().getDefinedBlobSize(
                        transaction.getConnection())];
                String sql = CommonBlobUtils.getSqlAdd();
                PreparedStatement pstmt = null;
                long currentTime = System.currentTimeMillis();
                try {
                    pstmt = transaction.getConnection().prepareStatement(sql);
                    long iterator = 0;
                    while (bais.read(content) > 0) {
                        ByteArrayInputStream tempBais = null;
                        try {
                            tempBais = new ByteArrayInputStream(content);
                            pstmt.setString(1, id);
                            pstmt.setLong(2, iterator);
                            pstmt.setBinaryStream(3, tempBais, tempBais.available());
                            pstmt.setString(4, "spy");
                            pstmt.execute();
                        } finally {
                            tempBais.close();
                        }
                        iterator++;
                    }
                } catch (SQLException e) {
                    throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - " + "Unable to add blob",
                            e);
                } finally {
                    if (JadeModelMappingProvider.getInstance().isVerbose()) {
                        JadeLogger.info(CommonBlobUtils.class, "Exec. time ("
                                + (System.currentTimeMillis() - currentTime) + " ) - Perform add - Query : " + sql);
                    }

                    CommonBlobUtils.closeStatement(pstmt);
                }
            } finally {
                bais.close();
            }
        } catch (Exception e) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - " + "Unable to add blob", e);
        }
    }

    public static File byteArrayToFile(String fileName, byte[] bytes) throws Exception {
        FileOutputStream fileOutputStream = null;

        File file = new File(fileName);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);

        return file;

    }

    private static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                JadeLogger.warn(CommonBlobUtils.class, "Problem to close results set, reason : " + e.toString());
            }
        }
    }

    private static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                JadeLogger.warn(CommonBlobUtils.class, "Problem to close statement, reason : " + e.toString());
            }
        }
    }

    public static void deleteBlob(String id, BTransaction transaction) throws JadePersistenceException {

        if (transaction == null) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the transaction is null");
        } else if (transaction.getConnection() == null) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the connection is null");
        } else if (JadeStringUtil.isBlank(id)) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the id passed is blank");
        }

        // Execute l'instruction sql
        Statement stmt = null;
        String sql = CommonBlobUtils.getSqlDelete(id);
        long currentTime = System.currentTimeMillis();
        try {
            stmt = transaction.getConnection().createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new JadePersistenceException(
                    JadePersistenceManager.class.getName() + " - " + "Unable to delete blob", e);
        } finally {
            if (JadeModelMappingProvider.getInstance().isVerbose()) {
                JadeLogger.info(JadePersistenceManager.class, "Exec. time ("
                        + (System.currentTimeMillis() - currentTime) + "ms) - Perform delete  - Query : " + sql);
            }

            CommonBlobUtils.closeStatement(stmt);
        }
    }

    public static byte[] fileToByteArray(String fileLocation) throws Exception {

        File file = new File(fileLocation);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[(int) file.length()];

        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            baos.write(buf, 0, readNum);
        }

        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    private static String getSqlAdd() {
        StringBuffer sql = new StringBuffer("INSERT INTO ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append("FWBLOB (IDBLOB, SEQUENCE, CONTENT, PSPY) VALUES (?, ?, ?, ?)");
        return sql.toString();
    }

    private static String getSqlDelete(String id) throws JadePersistenceException {
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append("FWBLOB WHERE IDBLOB='");

        if (JadeStringUtil.isBlank(id)) {
            throw new JadePersistenceException("Unable to delete blob, the id passed is blank");
        }
        sql.append(id);
        sql.append('\'');
        return sql.toString();
    }

    private static String getSqlRead(String id) {
        StringBuffer sql = new StringBuffer("SELECT CONTENT FROM ");
        sql.append(JadePersistenceUtil.getDbSchema());
        sql.append('.');
        if (!JadeStringUtil.isEmpty(Jade.getInstance().getDefaultJdbcTablePrefix())) {
            sql.append(Jade.getInstance().getDefaultJdbcTablePrefix());
        }
        sql.append("FWBLOB WHERE IDBLOB='");
        sql.append(id);
        sql.append('\'');
        sql.append(" ORDER BY SEQUENCE");

        return sql.toString();
    }

    public static Object readBlob(String id, BTransaction transaction) throws JadePersistenceException {

        if (transaction == null) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the transaction is null");
        } else if (transaction.getConnection() == null) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the connection is null");
        } else if (JadeStringUtil.isBlank(id)) {
            throw new JadePersistenceException(CommonBlobUtils.class.getName() + " - "
                    + "Unable to add blob, the id passed is blank");
        }

        Statement stmt = null;
        String sql = CommonBlobUtils.getSqlRead(id);
        if (sql != null) {
            long currentTime = System.currentTimeMillis();
            ResultSet rs = null;
            try {
                // Execute la requête
                stmt = transaction.getConnection().createStatement();
                rs = stmt.executeQuery(sql);
                // Output stream utilisé pour stocker l'ensemble du contenu de
                // l'objet sérialisé
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                @SuppressWarnings("rawtypes")
                Map zipFiles = null;
                try {
                    while (rs.next()) {
                        InputStream is = null;
                        try {
                            is = rs.getBinaryStream(JadeSqlBlobProvider.CONTENT_FIELD);
                            byte[] buffer = new byte[is.available()];
                            is.read(buffer);
                            baos.write(buffer);
                        } finally {
                            if (is != null) {
                                is.close();
                            }
                        }
                    }
                    // Dézip le contenu
                    ByteArrayInputStream bais = null;
                    try {
                        bais = new ByteArrayInputStream(baos.toByteArray());
                        zipFiles = JadeZipUtil.unzip(bais);
                    } finally {
                        bais.close();
                    }
                } finally {
                    baos.close();
                }
                // Première version, on stocke un seul fichier dans le blob
                byte[] content = null;
                @SuppressWarnings("rawtypes")
                Iterator it = zipFiles.keySet().iterator();
                if (it.hasNext()) {
                    content = (byte[]) zipFiles.get((it.next()));
                }
                if (content != null) {
                    ByteArrayInputStream unzipBais = null;
                    try {
                        unzipBais = new ByteArrayInputStream(content);
                        ObjectInputStream ois = null;
                        try {
                            ois = new ObjectInputStream(unzipBais);
                            return ois.readObject();
                        } finally {
                            ois.close();
                        }
                    } finally {
                        unzipBais.close();
                    }
                }

            } catch (Exception e) {
                throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                        + "Unable to read blob!", e);
            } finally {
                if (JadeModelMappingProvider.getInstance().isVerbose()) {
                    JadeLogger.info(CommonBlobUtils.class, "Exec. time (" + (System.currentTimeMillis() - currentTime)
                            + "ms) - Perform read blob - Query : " + sql);
                }

                CommonBlobUtils.closeResultSet(rs);
                CommonBlobUtils.closeStatement(stmt);
            }
        }
        return null;
    }

    public static void updateBlob(String id, Serializable object, BTransaction transaction)
            throws JadePersistenceException {
        CommonBlobUtils.deleteBlob(id, transaction);
        CommonBlobUtils.addBlob(id, object, transaction);
    }
}
