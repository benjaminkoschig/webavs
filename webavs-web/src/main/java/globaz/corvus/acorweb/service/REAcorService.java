package globaz.corvus.acorweb.service;

import acor.rentes.xsd.fcalcul.FCalcul;
import acor.ch.admin.zas.xmlns.acor_rentes9_out_resultat._0.Resultat9;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import globaz.corvus.acorweb.ws.token.REAcorToken;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class REAcorService {

    public InHostType createInHostJson(REAcorToken REAcor2020Token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REExportationCalculAcor inHostService = new REExportationCalculAcor(session, REAcor2020Token.getIdDemande());
            InHostType inHost = inHostService.createInHost();
            inHost.setVersionSchema("5.0");
            return inHost;
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

    public void updateRente10afterAcorCalcul(FCalcul fCalcul, REAcorToken REAcor2020Token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REImportationCalculAcor exportProcess = new REImportationCalculAcor();
            exportProcess.actionImporterScriptACOR(REAcor2020Token.getIdDemande(), REAcor2020Token.getIdTiers(), fCalcul, session);
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

    public void updateRente9afterAcorCalcul(Resultat9 resultat9, REAcorToken REAcor2020Token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REImportationCalculAcor exportProcess = new REImportationCalculAcor();
            exportProcess.actionImporterScriptACOR9(REAcor2020Token.getIdDemande(), REAcor2020Token.getIdTiers(), resultat9, session);
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

}
