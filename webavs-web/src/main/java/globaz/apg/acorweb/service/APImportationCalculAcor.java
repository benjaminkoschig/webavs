package globaz.apg.acorweb.service;

import acor.rentes.xsd.fcalcul.FCalcul;
import ch.globaz.common.persistence.EntityService;
import globaz.globall.db.BSessionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APImportationCalculAcor {

    private final EntityService entityService;

    public APImportationCalculAcor() {
        entityService = EntityService.of(BSessionUtil.getSessionFromThreadContext());
    }

    public void importCalculAcor(String idDroit, String genreService, FCalcul fCalcul) throws Exception {
        LOG.info("Importation des données calculées.");
        // TODO WS ACOR APG IMPLEMENT IMPORT DU STYLE IJImportationCalculAcor.importCalculAcor ou REImportationCalculAcor.actionImporterScriptACOR
    }
}
