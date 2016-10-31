package ch.globaz.orion.db;

import globaz.globall.db.BBlobMetadataProvider;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jdbc.JadeJdbcDriverManager;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.mapping.JadeModelMappingProvider;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.persistence.util.JadeSqlBlobProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

class BlobManager {
    public static void addBlob(String id, Serializable object) throws JadePersistenceException {

        // Contrôle que le thread context permette d'effectuer des modifications en db
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(BlobManager.class.getName() + " - "
                    + "Unable to add blob without a thread context");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(BlobManager.class.getName() + " - "
                    + "Unable to add blob  without an opened connection in the current thread context");
        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            // Si une erreur est survenu dans le contexte d'exécution du
            // thread, ne charge pas l'entité, inutile, le traitement est en
            // erreur
            return;
        }

        if (JadeStringUtil.isBlank(id)) {
            throw new JadePersistenceException(BlobManager.class.getName() + " - "
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
                        JadeThread.currentJdbcConnection())];
                String sql = BlobManager.getSqlAdd();
                PreparedStatement pstmt = null;
                long currentTime = System.currentTimeMillis();
                try {
                    pstmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
                    long iterator = 0;
                    while (bais.read(content) > 0) {
                        ByteArrayInputStream tempBais = null;
                        try {
                            tempBais = new ByteArrayInputStream(content);
                            pstmt.setString(1, id);
                            pstmt.setLong(2, iterator);
                            pstmt.setBinaryStream(3, tempBais, tempBais.available());
                            pstmt.setString(4, JadePersistenceUtil.newSpy());
                            pstmt.executeUpdate();
                        } finally {
                            tempBais.close();
                        }
                        iterator++;
                    }
                } catch (SQLException e) {
                    throw new JadePersistenceException(BlobManager.class.getName() + " - " + "Unable to add blob", e);
                } finally {
                    if (JadeModelMappingProvider.getInstance().isVerbose()) {
                        JadeLogger.info(BlobManager.class, "Exec. time (" + (System.currentTimeMillis() - currentTime)
                                + " ) - Perform add - Query : " + sql);
                    }

                    BlobManager.closeStatement(pstmt);
                }
            } finally {
                bais.close();
            }
        } catch (Exception e) {
            throw new JadePersistenceException(BlobManager.class.getName() + " - " + "Unable to add blob", e);
        }
    }

    public static byte[] fileToByteArray(String fileLocation) throws Exception {

        File file = new File(fileLocation);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            byte[] buf = new byte[(int) file.length()];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                baos.write(buf, 0, readNum);
            }

            byte[] bytes = baos.toByteArray();
            return bytes;

        } finally {
            fis.close();
            baos.close();
        }
    }

    private static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                JadeLogger.warn(BlobManager.class, "Problem to close results set, reason : " + e.toString());
            }
        }
    }

    private static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                JadeLogger.warn(BlobManager.class, "Problem to close statement, reason : " + e.toString());
            }
        }
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                JadeLogger.warn(BlobManager.class, "Problem to close connection, reason : " + e.toString());
            }
        }
    }

    public static void deleteBlob(String id) throws JadePersistenceException {

        // Contrôle que le thread context permette d'effectuer des modifications
        // en db
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to delete blob for the model without a thread context");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to delete blob for the model without an opened connection in the current thread context");
        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            // Si une erreur est survenu dans le contexte d'exécution du
            // thread, ne charge pas l'entité, inutile, le traitement est en
            // erreur
            return;
        }

        // Execute l'instruction sql
        Statement stmt = null;
        String sql = BlobManager.getSqlDelete(id);
        long currentTime = System.currentTimeMillis();
        try {
            stmt = JadeThread.currentJdbcConnection().createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new JadePersistenceException(
                    JadePersistenceManager.class.getName() + " - " + "Unable to delete blob", e);
        } finally {
            if (JadeModelMappingProvider.getInstance().isVerbose()) {
                JadeLogger.info(JadePersistenceManager.class, "Exec. time ("
                        + (System.currentTimeMillis() - currentTime) + "ms) - Perform delete  - Query : " + sql);
            }

            BlobManager.closeStatement(stmt);
        }
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

    public static Object readBlob(String id) throws JadePersistenceException {

        // Contrôle que le thread context permette d'effectuer des modifications
        // en db
        if (JadeThread.currentContext() == null) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to read blob without a thread context");
        } else if (!JadeThread.currentContext().isJdbc() || (JadeThread.currentJdbcConnection() == null)) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to read blob without an opened connection in the current thread context");
        }

        Statement stmt = null;
        String sql = BlobManager.getSqlRead(id);
        if (sql != null) {
            long currentTime = System.currentTimeMillis();
            ResultSet rs = null;
            try {
                // Execute la requête
                stmt = JadeThread.currentJdbcConnection().createStatement();
                return readBlob(sql, stmt);

            } catch (Exception e) {
                throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                        + "Unable to read blob!", e);
            } finally {
                if (JadeModelMappingProvider.getInstance().isVerbose()) {
                    JadeLogger.info(BlobManager.class, "Exec. time (" + (System.currentTimeMillis() - currentTime)
                            + "ms) - Perform read blob - Query : " + sql);
                }

                BlobManager.closeResultSet(rs);
                BlobManager.closeStatement(stmt);
            }
        }
        return null;
    }

    public static Object readBlobWithoutContext(String id) throws JadePersistenceException {

        if (JadeStringUtil.isBlank(Jade.getInstance().getDefaultJdbcUrl())) {
            throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                    + "Unable to read blob without a default JDBC url");
        }

        String sql = BlobManager.getSqlRead(id);
        if (sql != null) {
            long currentTime = System.currentTimeMillis();
            ResultSet rs = null;
            Statement stmt = null;
            Connection connection = null;

            try {
                // Execute la requête
                connection = JadeJdbcDriverManager.getInstance().getConnection(Jade.getInstance().getDefaultJdbcUrl());
                stmt = connection.createStatement();
                return readBlob(sql, stmt);

            } catch (Exception e) {
                throw new JadePersistenceException(JadePersistenceManager.class.getName() + " - "
                        + "Unable to read blob!", e);
            } finally {
                if (JadeModelMappingProvider.getInstance().isVerbose()) {
                    JadeLogger.info(BlobManager.class, "Exec. time (" + (System.currentTimeMillis() - currentTime)
                            + "ms) - Perform read blob - Query : " + sql);
                }

                BlobManager.closeResultSet(rs);
                BlobManager.closeStatement(stmt);
                BlobManager.closeConnection(connection);
            }
        }
        return null;
    }

    private static Object readBlob(String sql, Statement stmt) throws SQLException, IOException, Exception,
            ClassNotFoundException {
        ResultSet rs;
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

        return null;
    }

    public static void updateBlob(String id, Serializable object) throws JadePersistenceException {
        BlobManager.deleteBlob(id);
        BlobManager.addBlob(id, object);
    }

}
