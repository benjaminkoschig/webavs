package ch.globaz.pegasus.businessimpl.services.models.blocage;

import java.math.BigDecimal;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.vo.blocage.LigneDeblocage;

public class DeblocageUtils {
    public static void addValueDeblocage(SimpleLigneDeblocage simpleDeblocage, LigneDeblocage deblocage) {
        deblocage.setMontant(new BigDecimal(simpleDeblocage.getMontant()));
        deblocage.setIdDeBlocage(simpleDeblocage.getIdDeblocage());
        deblocage.setEtatDeblocage(EPCEtatDeblocage.getEnumByCsCode(simpleDeblocage.getCsEtat()));
    }
}
