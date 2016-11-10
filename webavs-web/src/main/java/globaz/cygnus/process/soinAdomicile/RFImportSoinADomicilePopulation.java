package globaz.cygnus.process.soinAdomicile;

import globaz.cygnus.process.financementSoin.step1.RFProcessImportFinancementSoinEnum;
import globaz.cygnus.service.RFimportService;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import ch.globaz.jade.process.annotation.BusinessKey;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationByFileCsv;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;

public class RFImportSoinADomicilePopulation extends JadeProcessPopulationByFileCsv implements
        JadeProcessPopulationNeedProperties<RFProcessImportFinancementSoinEnum> {

    private Map<RFProcessImportFinancementSoinEnum, String> properties = null;

    @Override
    @BusinessKey(unique = true)
    public String getBusinessKey() {
        String line;
        BufferedReader reader;
        String md5 = null;
        FileInputStream fileInputStream = null;
        String file = getFileName();
        if (JadeStringUtil.isEmpty(file)) {
            file = properties
                    .get(globaz.cygnus.helpers.process.RFProcessImportFinancementSoinEnum.FILE_PATH_FOR_POPULATION);
        }
        try {
            String filePath = JadeFsFacade.readFile(file);
            fileInputStream = new FileInputStream(filePath);
        } catch (Exception e1) {
            JadeProcessCommonUtils.addError(e1);
        }
        if (fileInputStream != null) {
            try {
                reader = new BufferedReader(new InputStreamReader(fileInputStream));
                StringBuilder s = new StringBuilder();
                try {
                    while ((line = reader.readLine()) != null) {
                        s.append(line);
                    }
                } finally {
                    reader.close();
                }
                md5 = DigestUtils.md5Hex(s.toString());
            } catch (Exception e) {
                e.printStackTrace();
                JadeProcessCommonUtils.addError(e);
            } finally {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return md5;
    }

    @Override
    public Class<RFProcessImportFinancementSoinEnum> getEnumForProperties() {
        return RFProcessImportFinancementSoinEnum.class;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        return null;
    }

    @Override
    public JadeProcessEntity getPopulation(String line, int i) {

        JadeProcessEntity entity = new JadeProcessEntity();

        String[] str = line.split(";");
        if ((i > 0) && (str.length > 3)) {
            entity.setIdRef(String.valueOf(i));
            entity.setDescription(str[2] + " " + str[3]);
            entity.setValue1(line);

            return entity;

        } else { // aucune ligne ds le fichier
            if (i == 1) {
                RFimportService importService = new RFimportService();
                BSession session = BSessionUtil.getSessionFromThreadContext();
                importService.sendMailFichierEnErreur(session, "fichier vide", "",
                        properties.get(RFProcessImportFinancementSoinEnum.GESTIONNAIRE));
            }
            return null;
        }
    }

    @Override
    public void setProperties(Map<RFProcessImportFinancementSoinEnum, String> map) {
        properties = map;
    }
}
