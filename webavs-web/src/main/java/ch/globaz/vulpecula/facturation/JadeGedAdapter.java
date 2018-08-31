package ch.globaz.vulpecula.facturation;

import globaz.jade.common.JadeBusinessError;
import globaz.jade.common.JadeException;
import globaz.jade.common.JadeInitializationException;
import globaz.jade.ged.message.JadeGedCallDefinition;
import globaz.jade.ged.target.JadeGedAdapterTarget;
import globaz.jade.log.JadeLogger;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.w3c.dom.Node;
import ch.globaz.vulpecula.externalservices.RequestFactory;
import ch.globaz.vulpecula.web.servlet.PTConstants;
import com.google.gson.JsonObject;

public class JadeGedAdapter extends JadeGedAdapterTarget {

    @Override
    public JadeGedCallDefinition call(Properties properties) throws JadeException, JadeBusinessError {
        // TODO Auto-generated method stub
        System.out.println("call");
        return null;
    }

    @Override
    public List<String> createIndexFiles(Properties properties) throws JadeException, JadeBusinessError {
        // TODO Auto-generated method stub
        System.out.println("createIndexFiles");
        return null;
    }

    @Override
    public void initialize(Node node) throws JadeInitializationException {
        // TODO Auto-generated method stub
        super.initialize(node);
    }

    @Override
    public void importDocument(Properties properties) throws JadeException, JadeBusinessError {

        JsonObject json = new JsonObject();
        json.addProperty(PTConstants.COUNTER_ID, getDocumentsList().get(0).getGedDocumentType());
        json.addProperty(PTConstants.DOC_DATE, properties.getProperty("creation.date"));
        json.addProperty(PTConstants.DOC_LABEL, returnLabel(properties));
        json.addProperty(PTConstants.DOC_FILE_NAME, FilenameUtils.getName(properties.getProperty("filename")));
        try {
            json.addProperty(PTConstants.DOC_CONTENT, encodeFileToBase64Binary(properties.getProperty("filename")));
        } catch (Exception e) {
            JadeLogger.error(this, "Unable to convert file to 64 binary : " + e.getMessage());
        }

        try {
            Response response = Request.Post(RequestFactory.URL_GED_INDEXATION)
                    .bodyString(json.toString(), ContentType.APPLICATION_JSON).execute();
            if (response.returnResponse().getStatusLine().getStatusCode() != 200) {

                JadeLogger.error(this, "Error while accessing webService for GED : "
                        + response.returnResponse().getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Error while accessing webService for GED : " + e.getMessage());
        }

    }

    /**
     * Rectificatif décompte BM.<noMembre>/<Periode <noDecompte>
     * 
     * @param properties
     * @return un label pour la mise en GED
     */
    private String returnLabel(Properties properties) {

        return "BM." + properties.getProperty("numero.role.formatte") + "/"
                + properties.getProperty("decompte.periode") + " " + properties.getProperty("idDecompte");
    }

    private static String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded);
    }

    @Override
    public JadeGedCallDefinition propagate(Properties properties) throws JadeException, JadeBusinessError {
        // TODO Auto-generated method stub
        System.out.println("propagate");
        return null;
    }

}
