package globaz.apg.acorweb.service;

import acor.apg.xsd.apg.out.FCalcul;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import globaz.apg.exceptions.APTechnicalRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APAcorService {

    public InHostType createInHostJson(String idDroit, String genreService) {
        try {
            APExportationCalculAcor inHostService = new APExportationCalculAcor(idDroit, genreService);
            InHostType inHost = inHostService.createInHost();
            inHost.setVersionSchema("7.0"); // TODO WS ACOR APG UTILE POUR APG OU NON?
            // TODO WS ACOR APG A cet endroit IJAcorService.createInHostCalcul lance une validation XSD après création de l'objet alors que REAcorService.createInHostJson ne fait pas de validation
            return inHost;
        } catch (Exception e) {
            throw new APTechnicalRuntimeException(e);
        }
    }

    public void importCalculAcor(FCalcul fCalcul, String idDroit, String genreService) {
        try {
            APImportationCalculAcor importer = new APImportationCalculAcor();
            importer.importCalculAcor(idDroit, genreService, fCalcul);
        } catch (Exception e) {
            throw new APTechnicalRuntimeException(e);
        }
    }

}
