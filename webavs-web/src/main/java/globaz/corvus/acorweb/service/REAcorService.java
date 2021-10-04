package globaz.corvus.acorweb.service;

import acor.ch.admin.zas.xmlns.acor_rentes9_out_resultat._0.Resultat9;
import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import acor.rentes.xsd.fcalcul.FCalcul;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class REAcorService {

    public InHostType createInHostJson(String idDemande) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REExportationCalculAcor inHostService = new REExportationCalculAcor(session, idDemande);
            InHostType inHost = inHostService.createInHost();
            inHost.setVersionSchema("5.0");
            return inHost;
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

    public void updateRente10afterAcorCalcul(FCalcul fCalcul, String idDemande, String idTiers) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REImportationCalculAcor exportProcess = new REImportationCalculAcor();
            exportProcess.actionImporterScriptACOR(idDemande, idTiers, fCalcul, session);
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

    public void updateRente9afterAcorCalcul(Resultat9 resultat9, String idDemande, String idTiers) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REImportationCalculAcor exportProcess = new REImportationCalculAcor();
            exportProcess.actionImporterScriptACOR9(idDemande, idTiers, resultat9, session);
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

}
