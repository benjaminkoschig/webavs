package ch.globaz.pegasus.business.vo.decompte;

import java.math.BigDecimal;
import ch.globaz.pegasus.business.models.creancier.SimpleCreancier;

public class CreancierVO {
    private SimpleCreancier creancier = null;
    private String description = null;
    private BigDecimal montantVerse = null;

    public CreancierVO() {
        super();
    }

    public CreancierVO(String description, SimpleCreancier creancier) {
        super();
        this.description = description;
        this.creancier = creancier;
        montantVerse = new BigDecimal(0);
    }

    public SimpleCreancier getCreancier() {
        return creancier;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getMontantVerse() {
        return montantVerse;
    }

    public void setCreancier(SimpleCreancier creancier) {
        this.creancier = creancier;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMontantVerse(BigDecimal montantVerse) {
        this.montantVerse = montantVerse;
    }

}
