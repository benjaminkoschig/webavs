package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculAcomptesInd {
    BigDecimal revenuDeterminant = BigDecimal.ZERO;
    List<LigneCotisation> listCotisations = new ArrayList<LigneCotisation>();
    BigDecimal totalCotistations = BigDecimal.ZERO;
    String periodicite = null;

    public BigDecimal getRevenuDeterminant() {
        return revenuDeterminant;
    }

    public void setRevenuDeterminant(BigDecimal revenuDeterminant) {
        this.revenuDeterminant = revenuDeterminant;
    }

    public List<LigneCotisation> getListCotisations() {
        return listCotisations;
    }

    public void setListCotisations(List<LigneCotisation> listCotisations) {
        this.listCotisations = listCotisations;
    }

    public BigDecimal getTotalCotistations() {
        return totalCotistations;
    }

    public void setTotalCotistations(BigDecimal totalCotistations) {
        this.totalCotistations = totalCotistations;
    }

    public void computeTotalCotisation() {
        BigDecimal total = BigDecimal.ZERO;
        if (!listCotisations.isEmpty()) {
            for (LigneCotisation ligne : listCotisations) {
                if (ligne != null) {
                    if (ligne.getMontant() != null) {
                        total = total.add(ligne.getMontant());
                    }
                }
            }
        }

        setTotalCotistations(total);
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

}
