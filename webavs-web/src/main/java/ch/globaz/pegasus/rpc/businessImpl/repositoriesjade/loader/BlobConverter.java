package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
import ch.globaz.common.sql.ConverterDb;

public class BlobConverter implements ConverterDb<Object> {

    @Override
    public Object convert(Object value, String fieldName, String alias) {
        // Output stream utilisé pour stocker l'ensemble du contenu de
        // l'objet sérialisé
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Map zipFiles = null;

        // Dézip le contenu
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(baos.toByteArray());
            try {
                zipFiles = JadeZipUtil.unzip(bais);
            } catch (Exception e) {
                JadeLogger.error(this, e.getMessage());
            }
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                JadeLogger.error(this, e.getMessage());
            }
        }
        // Première version, on stocke un seul fichier dans le blob
        byte[] content = null;
        Iterator it = zipFiles.keySet().iterator();
        if (it.hasNext()) {
            content = (byte[]) zipFiles.get((it.next()));
        }
        try {
            if (content != null) {
                ByteArrayInputStream unzipBais = null;
                try {
                    unzipBais = new ByteArrayInputStream(content);
                    ObjectInputStream ois = null;
                    try {
                        ois = new ObjectInputStream(unzipBais);
                        return ois.readObject();
                    } catch (ClassNotFoundException e) {
                        JadeLogger.error(this, e.getMessage());
                    } finally {
                        ois.close();
                    }
                } finally {
                    unzipBais.close();
                }
            }
        } catch (IOException e) {
            JadeLogger.error(this, e.getMessage());
        }
        return null;
    }

    @Override
    public Class<Object> forType() {
        return Object.class;
    }
}
