package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import globaz.jade.client.zip.JadeZipUtil;
import globaz.jade.persistence.util.JadeSqlBlobProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.queryexc.converters.Mapper;
import ch.globaz.queryexec.bridge.jade.SCM;
import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

class BlobLoader {
    private static final Logger LOG = LoggerFactory.getLogger(BlobLoader.class);

    public Map<String, byte[]> loadBlob(Collection<String> ids) {
        Mapper<CalculeBlob> mapper = createBlobMapper();

        String idsForIn = createIdsForIn(ids);

        List<CalculeBlob> blobs = SCM.newInstance(CalculeBlob.class).mapper(mapper)
                .query("SELECT IDBLOB, CONTENT, SEQUENCE FROM SCHEMA.FWBLOB where IDBLOB in (" + idsForIn + ")")
                .execute();

        Map<String, Collection<CalculeBlob>> mapBlob = groupByidBlob(blobs);
        Map<String, byte[]> mapBlobInputStraem = new HashMap<String, byte[]>();
        for (Entry<String, Collection<CalculeBlob>> entry : mapBlob.entrySet()) {
            Collection<CalculeBlob> list = entry.getValue();
            mapBlobInputStraem.put(entry.getKey(), unZip(list));
        }
        return mapBlobInputStraem;
    }

    /**
     * Return the unziped contraction of blob content of the blob entities's list
     * 
     * @param sequenceList List of sequences for the same idBlob
     * @return unique entity with unziped full blob's sequences content
     */
    static byte[] unZip(Collection<CalculeBlob> list) throws RuntimeException {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("No content to unzip");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            try {
                for (CalculeBlob blob : list) {
                    baos.write(blob.getBytes());
                }
            } catch (IOException e) {
                LOG.error("Blob sequences failed to fill in into the ByteArrayOutputStream", e);
            }
            byte[] content = null;
            ByteArrayInputStream bais = null;
            try {
                bais = new ByteArrayInputStream(baos.toByteArray());

                try {
                    Map zipFiles = JadeZipUtil.unzip(bais);
                    Iterator it = zipFiles.keySet().iterator();
                    if (it.hasNext()) {
                        content = (byte[]) zipFiles.get(it.next());
                    }
                } catch (Exception e) {
                    throw new CommonTechnicalException(e);
                }

            } finally {
                IOUtils.closeQuietly(bais);
            }
            if (content != null) {
                ByteArrayInputStream unzipBais = null;
                try {
                    unzipBais = new ByteArrayInputStream(content);
                    ObjectInputStream ois = null;
                    try {
                        ois = new ObjectInputStream(unzipBais);
                        return (byte[]) ois.readObject();
                    } catch (Exception e) {
                        LOG.error("Blob sequences failed to fill in into the ByteArrayOutputStream", e);
                    } finally {
                        IOUtils.closeQuietly(ois);
                    }
                } finally {
                    IOUtils.closeQuietly(unzipBais);
                }
            }

        } finally {
            IOUtils.closeQuietly(baos);
        }
        return new byte[0];
    }

    private String createIdsForIn(Collection<String> ids) {
        StringBuilder builder = new StringBuilder();
        for (String id : ids) {
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append("'ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul_resultatCalcul_")
                    .append(id).append("'");
        }
        return builder.toString();
    }

    private Mapper<CalculeBlob> createBlobMapper() {
        Mapper<CalculeBlob> mapper;
        mapper = new Mapper<CalculeBlob>() {

            @Override
            public CalculeBlob map(ResultSet rs, int index) {
                try {
                    String idBlob = rs.getString("IDBLOB");
                    int sequence = rs.getInt("SEQUENCE");
                    byte[] bytes = rs.getBytes(JadeSqlBlobProvider.CONTENT_FIELD);
                    String idPlanCalcul = idBlob.split("_")[2];
                    return new CalculeBlob(idPlanCalcul, bytes, sequence);
                } catch (SQLException e) {
                    throw new CommonTechnicalException(e);
                }
            }
        };
        return mapper;
    }

    private Map<String, Collection<CalculeBlob>> groupByidBlob(List<CalculeBlob> blobs) {
        ListMultimap<String, CalculeBlob> multimap = Multimaps.index(blobs, new Function<CalculeBlob, String>() {

            @Override
            public String apply(CalculeBlob blob) {
                return blob.getIdPlanCalcul();
            }
        });

        return multimap.asMap();
    }

}
