package ch.globaz.vulpecula.ws.bean;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import ch.globaz.vulpecula.models.decompte.TableauContributions.EntreeContribution;

import com.google.common.base.Preconditions;

// @XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="entreeContribution")
public class EntreeContributionEbu implements Comparable<EntreeContributionEbu> {
    private BigDecimal taux;
    private BigDecimal masse;

    public EntreeContributionEbu() {
    	// Constructeur par defaut obligatoire pour le bon fonctionnement du framework
    }

    public EntreeContributionEbu(EntreeContribution entree) {
        masse = entree.getMasse().getBigDecimalValue();
        taux = entree.getTaux().getBigDecimal();
    }

    public EntreeContributionEbu(BigDecimal taux, BigDecimal masse) {
        Preconditions.checkNotNull(taux);
        Preconditions.checkNotNull(masse);

        this.taux = taux;
        this.masse = masse;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public BigDecimal getMasse() {
        return masse;
    }

    public BigDecimal getMontant() {
        return getBigDecimalNormalisee(masse.multiply(taux));
    }

    @Override
    public int compareTo(EntreeContributionEbu o) {
        return o.taux.compareTo(taux);
    }

    private BigDecimal getBigDecimalNormalisee(BigDecimal currency) {
        BigDecimal increment = new BigDecimal("0.05");

        BigDecimal valueNormalisee = new BigDecimal(Double.toString(currency.doubleValue()));
        valueNormalisee = valueNormalisee.divide(increment, 0, BigDecimal.ROUND_HALF_UP);
        valueNormalisee = valueNormalisee.multiply(increment);
        return valueNormalisee.setScale(2);
    }
}
