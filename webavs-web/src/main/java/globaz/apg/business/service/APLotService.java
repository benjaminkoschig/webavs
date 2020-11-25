package globaz.apg.business.service;

import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

public interface APLotService extends JadeApplicationService {

    public String getTotauxOPAE(BSession session, String idLot) throws Exception;
}