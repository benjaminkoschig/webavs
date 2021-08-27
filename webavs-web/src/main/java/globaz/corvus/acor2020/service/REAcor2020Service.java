package globaz.corvus.acor2020.service;

import acor.rentes.xsd.fcalcul.FCalcul;
import ch.admin.zas.xmlns.acor_rentes9_out_resultat._0.Resultat9;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import globaz.corvus.acor2020.ws.token.REAcor2020Token;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class REAcor2020Service {

    public InHostType createInHostJson(REAcor2020Token REAcor2020Token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REExportationCalculAcor2020 inHostService = new REExportationCalculAcor2020(session, REAcor2020Token.getIdDemande());
            InHostType inHost = inHostService.createInHost();
            inHost.setVersionSchema("5.0");
            return inHost;
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

    public void updateRente10afterAcorCalcul(FCalcul fCalcul, REAcor2020Token REAcor2020Token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REImportationCalculAcor2020 exportProcess = new REImportationCalculAcor2020();
            exportProcess.actionImporterScriptACOR(REAcor2020Token.getIdDemande(), REAcor2020Token.getIdTiers(), fCalcul, session);
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

    public void updateRente9afterAcorCalcul(Resultat9 resultat9, REAcor2020Token REAcor2020Token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            REImportationCalculAcor2020 exportProcess = new REImportationCalculAcor2020();
            exportProcess.actionImporterScriptACOR9(REAcor2020Token.getIdDemande(), REAcor2020Token.getIdTiers(), resultat9, session);
        } catch (Exception e) {
            throw new RETechnicalException(e);
        }
    }

}
