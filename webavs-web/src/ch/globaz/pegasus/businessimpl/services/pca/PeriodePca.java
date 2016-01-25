package ch.globaz.pegasus.businessimpl.services.pca;

import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;

public class PeriodePca {
    private int nbMont = 0;
    private PcaDecompte pcaConjointNew;
    private PcaDecompte pcaConjointReplaced;
    private PcaDecompte pcaRequeranReplaced;
    private PcaDecompte pcaRequerantNew;

    public int getNbMont() {
        return nbMont;
    }

    public PcaDecompte getPcaConjointNew() {
        return pcaConjointNew;
    }

    public PcaDecompte getPcaConjointReplaced() {
        return pcaConjointReplaced;
    }

    public PcaDecompte getPcaRequeranReplaced() {
        return pcaRequeranReplaced;
    }

    public PcaDecompte getPcaRequerantNew() {
        return pcaRequerantNew;
    }

    public void setNbMont(int nbMont) {
        this.nbMont = nbMont;
    }

    public void setPcaConjointNew(PcaDecompte pcaConjointNew) {
        this.pcaConjointNew = pcaConjointNew;
    }

    public void setPcaConjointReplaced(PcaDecompte pcaConjointReplaced) {
        this.pcaConjointReplaced = pcaConjointReplaced;
    }

    public void setPcaRequeranReplaced(PcaDecompte pcaRequeranReplaced) {
        this.pcaRequeranReplaced = pcaRequeranReplaced;
    }

    public void setPcaRequerantNew(PcaDecompte pcaRequerantNew) {
        this.pcaRequerantNew = pcaRequerantNew;
    }
}
