package globaz.apg.module.calcul;

import java.math.BigDecimal;

public class APCotisationData {

    private String idAssurance;
    private BigDecimal montantBrut = null;
    private BigDecimal montantCotisation = null;
    private BigDecimal taux;
    private String type;

    public APCotisationData(BigDecimal montantCotisation, BigDecimal montantBrut, BigDecimal taux, String type,
            String idAssurance) {
        super();
        this.montantCotisation = montantCotisation;
        this.montantBrut = montantBrut;
        this.taux = taux;
        this.type = type;
        this.idAssurance = idAssurance;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public BigDecimal getMontantBrut() {
        return montantBrut;
    }

    public BigDecimal getMontantCotisation() {
        return montantCotisation;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public String getType() {
        return type;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    public void setMontantBrut(BigDecimal montantBrut) {
        this.montantBrut = montantBrut;
    }

    public void setMontantCotisation(BigDecimal montantCotisation) {
        this.montantCotisation = montantCotisation;
    }

    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }

    public void setType(String type) {
        this.type = type;
    }

}