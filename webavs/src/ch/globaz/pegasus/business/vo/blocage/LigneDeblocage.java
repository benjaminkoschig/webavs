package ch.globaz.pegasus.business.vo.blocage;

import java.math.BigDecimal;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;

public class LigneDeblocage {
    private EPCEtatDeblocage etatDeblocage = null;
    private String idDeBlocage = null;
    private BigDecimal montant = new BigDecimal(0);

    public EPCEtatDeblocage getEtatDeblocage() {
        return etatDeblocage;
    }

    public String getIdDeBlocage() {
        return idDeBlocage;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setEtatDeblocage(EPCEtatDeblocage etatDeblocage) {
        this.etatDeblocage = etatDeblocage;
    }

    public void setIdDeBlocage(String idDeBlocage) {
        this.idDeBlocage = idDeBlocage;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}
