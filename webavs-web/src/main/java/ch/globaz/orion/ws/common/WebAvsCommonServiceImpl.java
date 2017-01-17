package ch.globaz.orion.ws.common;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.io.File;
import java.io.IOException;
import javax.jws.WebService;
import org.apache.commons.io.FileUtils;

@WebService(endpointInterface = "ch.globaz.orion.ws.common.WebAvsCommonService")
public class WebAvsCommonServiceImpl implements WebAvsCommonService {

    @Override
    public byte[] downloadFile(String filepath) {
        byte[] byteFile = null;
        File docFile = new File(filepath);
        try {
            byteFile = FileUtils.readFileToByteArray(docFile);
        } catch (IOException e) {
            JadeLogger.error("Unabled to download file : " + filepath, e);
        }

        return byteFile;
    }

    @Override
    public String findAgeRetraite(String sexe, String dateNaissance) {
        if ((sexe != null && !JadeStringUtil.isBlankOrZero(sexe))
                && (dateNaissance != null && !JadeStringUtil.isBlankOrZero(dateNaissance))) {
            String ageRetraite = null;

            return null;
        } else {
            throw new IllegalArgumentException();
        }

    }
}
