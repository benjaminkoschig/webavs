package globaz.ij.acor2020.service;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.InHostType;
import ch.globaz.common.acor.Acor2020Token;
import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IJAcor2020Service {

    public InHostType createInHost(Acor2020Token token) {
        try {
            BSession session = BSessionUtil.getSessionFromThreadContext();
            InHostType inHost = new InHostType();
            inHost.setVersionSchema("5.0");
            return inHost;
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }
}
