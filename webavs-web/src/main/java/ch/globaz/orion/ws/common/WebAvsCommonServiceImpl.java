package ch.globaz.orion.ws.common;

import java.io.File;
import java.io.IOException;
import javax.jws.WebService;
import org.apache.commons.io.FileUtils;
import ch.globaz.common.business.models.InfosPersonResponseType;
import ch.globaz.common.business.services.UPIService;
import ch.globaz.common.businessimpl.CommonServiceLocator;
import ch.globaz.orion.ws.exceptions.WebAvsException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersHelper;

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
    public String findDateRetraite(String sexe, String dateNaissance) {
        if ((sexe != null && !JadeStringUtil.isBlankOrZero(sexe))
                && (dateNaissance != null && !JadeStringUtil.isBlankOrZero(dateNaissance))) {
            return PRTiersHelper.getDateDebutDroitAVS(dateNaissance, sexe);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public InfosPersonResponseType getInformationsPersonne(String nss, String numeroAffilie, String loginName,
            String userEmail, String langue) throws WebAvsException {
        if (JadeStringUtil.isBlankOrZero(nss)) {
            JadeLogger.error(this, "nss cannot be null or zero");
            throw new WebAvsException("nss cannot be null or zero");
        }

        try {
            UPIService upiService = CommonServiceLocator.getUPIService();
            return upiService.getPerson(nss, numeroAffilie, loginName, userEmail, langue);
        } catch (Exception e) {
            JadeLogger.error(this, "Problème de connexion aux services Web UPI");
            throw new WebAvsException("Problème de connexion aux services Web UPI");
        }
    }
}
