package globaz.cygnus.process.financementSoin.step1;

import globaz.cygnus.service.RFimportService;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.fs.JadeFsFacade;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import ch.globaz.jade.process.annotation.BusinessKey;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationByFileCsv;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;

public class RFImportFinancementSoinPopulation extends JadeProcessPopulationByFileCsv implements
        JadeProcessPopulationNeedProperties<RFProcessImportFinancementSoinEnum> {

    private Map<RFProcessImportFinancementSoinEnum, String> properties = null;

    @Override
    @BusinessKey(unique = true)
    public String getBusinessKey() {

        // List<JadeProcessEntity> list = new LinkedList<JadeProcessEntity>();
        // int i = 0;
        String line;
        BufferedReader reader;
        String fileName = "";
        String md5 = null;
        try {
            String file = getFileName();

            if (JadeStringUtil.isEmpty(getFileName())) {
                file = properties
                        .get(globaz.cygnus.helpers.process.RFProcessImportFinancementSoinEnum.FILE_PATH_FOR_POPULATION);
            }

            fileName = JadeFsFacade.readFile(file);

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            StringBuilder s = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                s.append(line);
            }

            md5 = DigestUtils.md5Hex(s.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JadeProcessCommonUtils.addError(e);
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
        /*
         * JadeProcessAbstractJob job = new
         * JadeProcessServiceLocator.getJadeProcessCommonService().startJadeProcess(job, true);
         */

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
