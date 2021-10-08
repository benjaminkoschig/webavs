package globaz.apg.business.service;

import globaz.apg.db.droits.APEnfantAPG;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.prestation.beans.PRPeriode;
import java.util.List;

public interface APDroitAPGService extends JadeApplicationService {
    public List<PRPeriode> controlerPrestation(List<APPeriodeAPG> periodesAControler, List<APEnfantAPG> enfantsDuTiers)
            throws Exception;

    /**
     * Un découpage des périodes APPeriodeAPG d'un droit APDroitAPG est nécessaire si un enfant né en cours de période
     * 
     * @param periodesAControler
     * @param enfantsDuTiers
     * @return
     * @throws Exception
     */
    public boolean isDecoupageDesPeriodesAPGNecessaire(List<APPeriodeAPG> periodesAControler,
            List<APEnfantAPG> enfantsDuTiers) throws Exception;

}
