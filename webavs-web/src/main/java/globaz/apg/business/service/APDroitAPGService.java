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
     * Un d�coupage des p�riodes APPeriodeAPG d'un droit APDroitAPG est n�cessaire si un enfant n� en cours de p�riode
     * 
     * @param periodesAControler
     * @param enfantsDuTiers
     * @return
     * @throws Exception
     */
    public boolean isDecoupageDesPeriodesAPGNecessaire(List<APPeriodeAPG> periodesAControler,
            List<APEnfantAPG> enfantsDuTiers) throws Exception;

}
