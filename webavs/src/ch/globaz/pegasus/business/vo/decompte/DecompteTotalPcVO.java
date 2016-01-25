package ch.globaz.pegasus.business.vo.decompte;

import java.math.BigDecimal;
import ch.globaz.pegasus.business.domaine.ListTotal;
import ch.globaz.pegasus.business.domaine.ListeTotalisable;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;

public class DecompteTotalPcVO {

    /* creanciers */
    private ListTotal<CreancierVO> creanciers = null;
    /* liste dette compta */
    private ListTotal<DetteEnComptaVO> dettesCompta = null;
    private BigDecimal montantVerifRetro = null;
    /* liste decompte des pca */
    private ListTotal<PCAccordeeDecompteVO> periodesPca = null;
    /* liste prestation verses */
    private ListTotal<PCAccordeeDecompteVO> prestationsVerses = null;
    private ListeTotalisable<SimpleAllocationNoel> simpleAllocationNoels = null;
    private BigDecimal sousTotalDeduction = null;
    private BigDecimal sousTotalPCA = null;

    /* total */
    private BigDecimal total = null;

    public DecompteTotalPcVO() {
        creanciers = new ListTotal<CreancierVO>();
        dettesCompta = new ListTotal<DetteEnComptaVO>();
        periodesPca = new ListTotal<PCAccordeeDecompteVO>();
        prestationsVerses = new ListTotal<PCAccordeeDecompteVO>();
        total = new BigDecimal(0);
        sousTotalPCA = new BigDecimal(0);
        sousTotalDeduction = new BigDecimal(0);
        simpleAllocationNoels = new ListeTotalisable<SimpleAllocationNoel>();
        setMontantVerifRetro(new BigDecimal(0));
    }

    public ListTotal<CreancierVO> getCreanciers() {
        return creanciers;
    }

    public ListTotal<DetteEnComptaVO> getDettesCompta() {
        return dettesCompta;
    }

    public BigDecimal getMontantAllocationNoelParPersonne() {

        BigDecimal nbrePersonnes = new BigDecimal(getNombrePersonnesAllocationDeNoel());

        if (nbrePersonnes.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        return simpleAllocationNoels.total().divide(new BigDecimal(getNombrePersonnesAllocationDeNoel()));
    }

    public BigDecimal getMontantTotalAllocationNoel() {
        if ((simpleAllocationNoels != null) && simpleAllocationNoels.size() != 0) {

            return simpleAllocationNoels.total();
        }
        return BigDecimal.ZERO;
    }

    public int getNombrePersonnesAllocationDeNoel() {

        int nbrePersonnes = 0;

        for (SimpleAllocationNoel alloc : simpleAllocationNoels.elements()) {
            nbrePersonnes += Integer.parseInt(alloc.getNbrePersonnes());
        }

        return nbrePersonnes;
    }

    public BigDecimal getMontantVerifRetro() {
        return montantVerifRetro;
    }

    public ListTotal<PCAccordeeDecompteVO> getPeriodesPca() {
        return periodesPca;
    }

    public ListTotal<PCAccordeeDecompteVO> getPrestationsVerses() {
        return prestationsVerses;
    }

    public ListeTotalisable<SimpleAllocationNoel> getSimpleAllocationNoels() {
        return simpleAllocationNoels;
    }

    public BigDecimal getSousTotalDeduction() {
        return sousTotalDeduction;
    }

    public BigDecimal getSousTotalPCA() {
        return sousTotalPCA;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setCreanciers(ListTotal<CreancierVO> creanciers) {
        this.creanciers = creanciers;
    }

    public void setDettesCompta(ListTotal<DetteEnComptaVO> dettesCompta) {
        this.dettesCompta = dettesCompta;
    }

    public void setMontantVerifRetro(BigDecimal montantVerifRetro) {
        this.montantVerifRetro = montantVerifRetro;
    }

    public void setPeriodesPca(ListTotal<PCAccordeeDecompteVO> periodesPca) {
        this.periodesPca = periodesPca;
    }

    public void setPrestationsVerses(ListTotal<PCAccordeeDecompteVO> prestationsVerses) {
        this.prestationsVerses = prestationsVerses;
    }

    public void setSousTotalDeduction(BigDecimal sousTotalDeduction) {
        this.sousTotalDeduction = sousTotalDeduction;
    }

    public void setSousTotalPCA(BigDecimal sousTotalPCA) {
        this.sousTotalPCA = sousTotalPCA;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void addSimpleAllocationNoel(SimpleAllocationNoel simpleAllocationNoel) {
        simpleAllocationNoels.addElement(simpleAllocationNoel);

    }

}
