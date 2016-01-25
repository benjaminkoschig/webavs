package ch.globaz.pegasus.business.vo.decompte;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;

public class PCAccordeeDecompteVO {

    private String csGenrePca = null;
    private String dateDebutPeriode = null;
    private String dateFinPeriode = null;
    private String descTiers = null;
    private String idPca = null;
    private BigDecimal montantForPeriod = null;
    private BigDecimal montantPcaMensuel = null;
    private Integer nbreMois = null;
    private SimpleAllocationNoel simpleAllocationNoel = null;
    private SimpleJoursAppoint simpleJoursAppoint = null;
    private String csRoleBeneficiaire = null;

    public PCAccordeeDecompteVO() {
        montantForPeriod = new BigDecimal(0);
        montantPcaMensuel = new BigDecimal(0);
        simpleAllocationNoel = new SimpleAllocationNoel();
    }

    public PCAccordeeDecompteVO(String dateDebutPeriode, String dateFinPeriode, Integer nbreMois,
            BigDecimal montantPcaMensuel, BigDecimal montantForPeriod, String csGenrePca, String descTier,
            SimpleAllocationNoel simpleAllocationNoel) {

        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
        this.nbreMois = nbreMois;
        this.montantPcaMensuel = montantPcaMensuel;
        this.montantForPeriod = montantForPeriod;
        this.csGenrePca = csGenrePca;
        descTiers = descTier;
        this.simpleAllocationNoel = simpleAllocationNoel;
    }

    public SimpleJoursAppoint getSimpleJoursAppoint() {
        return simpleJoursAppoint;
    }

    public String getMontantTotalJourAppoint() {
        if (simpleJoursAppoint != null) {
            return new FWCurrency(simpleJoursAppoint.getMontantTotal().toString()).toStringFormat();
        }
        return null;
    }

    public void setSimpleJoursAppoint(SimpleJoursAppoint simpleJoursAppoint) {
        this.simpleJoursAppoint = simpleJoursAppoint;
    }

    public String getCsGenrePca() {
        return csGenrePca;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public String getDescTiers() {
        return descTiers;
    }

    public String getIdPca() {
        return idPca;
    }

    public BigDecimal getMontantForPeriod() {
        return montantForPeriod;
    }

    public BigDecimal getMontantPcaMensuel() {
        return montantPcaMensuel;
    }

    public Integer getNbreMois() {
        return nbreMois;
    }

    public SimpleAllocationNoel getSimpleAllocationNoel() {
        return simpleAllocationNoel;
    }

    public void setCsGenrePCA(String csGenrePCA) {
        csGenrePca = csGenrePCA;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setDescTiers(String descTiers) {
        this.descTiers = descTiers;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setMontantForPeriod(BigDecimal montantForPeriod) {
        this.montantForPeriod = montantForPeriod;
    }

    public void setMontantPcaMensuel(BigDecimal montantPcaMensuel) {
        this.montantPcaMensuel = montantPcaMensuel;
    }

    public void setNbreMois(Integer nbreMois) {
        this.nbreMois = nbreMois;
    }

    public void setSimpleAllocationNoel(SimpleAllocationNoel simpleAllocationNoel) {
        this.simpleAllocationNoel = simpleAllocationNoel;
    }

    public String getCsRoleBeneficiaire() {
        return csRoleBeneficiaire;
    }

    public void setCsRoleBeneficiaire(String csRoleBeneficiaire) {
        this.csRoleBeneficiaire = csRoleBeneficiaire;
    }

    public void setCsGenrePca(String csGenrePca) {
        this.csGenrePca = csGenrePca;
    }

    public boolean hasJourAppoint() {
        if (simpleJoursAppoint != null) {
            return !JadeStringUtil.isBlankOrZero(simpleJoursAppoint.getId());
        } else {
            return false;
        }
    }

}
