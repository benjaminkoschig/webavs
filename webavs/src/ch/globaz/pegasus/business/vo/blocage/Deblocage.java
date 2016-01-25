package ch.globaz.pegasus.business.vo.blocage;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.models.blocage.PcaBloque;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;

public class Deblocage {

    private CompteAnnexeSimpleModel compteAnnexe;
    private Boolean isDevalidable = false;
    private Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lingesDeblocages;
    private PCAccordee pca = null;
    private PcaBloque pcaBloque = null;
    private List<SectionSimpleModel> sectionDeblocage;
    private BigDecimal sumTotalDeblocage = new BigDecimal(0);
    private BigDecimal sumTotatEtatEnregistre = new BigDecimal(0);

    public CompteAnnexeSimpleModel getCompteAnnexe() {
        return compteAnnexe;
    }

    public Boolean getIsDevalidable() {
        return isDevalidable;
    }

    public Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> getLingesDeblocages() {
        return Collections.unmodifiableMap(lingesDeblocages);
    }

    public PCAccordee getPca() {
        return pca;
    }

    public PcaBloque getPcaBloque() {
        return pcaBloque;
    }

    public List<SectionSimpleModel> getSectionDeblocage() {
        return sectionDeblocage;
    }

    public BigDecimal getSumTotalDeblocage() {
        return sumTotalDeblocage;
    }

    public BigDecimal getSumTotatEtatEnregistre() {
        return sumTotatEtatEnregistre;
    }

    public void setCompteAnnexe(CompteAnnexeSimpleModel compteAnnexe) {
        this.compteAnnexe = compteAnnexe;
    }

    public void setIsDevalidable(Boolean isDevalidable) {
        this.isDevalidable = isDevalidable;
    }

    public void setLingesDeblocages(Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> lingesDeblocages) {
        this.lingesDeblocages = lingesDeblocages;
    }

    public void setPca(PCAccordee pca) {
        this.pca = pca;
    }

    public void setPcaBloque(PcaBloque pcaBloque) {
        this.pcaBloque = pcaBloque;
    }

    public void setSectionDeblocage(List<SectionSimpleModel> sectionDeblocage) {
        this.sectionDeblocage = sectionDeblocage;
    }

    public void setSumTotalDeblocage(BigDecimal sumTotalDeblocage) {
        this.sumTotalDeblocage = sumTotalDeblocage;
    }

    public void setSumTotatEtatEnregistre(BigDecimal sumTotatEtatEnregistre) {
        this.sumTotatEtatEnregistre = sumTotatEtatEnregistre;
    }
}
