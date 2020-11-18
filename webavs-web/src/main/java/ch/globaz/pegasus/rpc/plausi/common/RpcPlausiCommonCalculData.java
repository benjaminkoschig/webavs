package ch.globaz.pegasus.rpc.plausi.common;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.pegasus.business.domaine.pca.PcaGenre;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public abstract class RpcPlausiCommonCalculData extends RpcPlausiHeader {
    protected Revenu revenu = new Revenu();
    protected Depense depense = new Depense();
    protected Montant montantCalcule;
    protected Montant sumDepense;
    protected Montant sumRevenu;
    protected Montant diff;
    protected Montant pca;
    protected boolean isReforme;
    // protected Montant montantCalculateur;

    protected PcaEtatCalcul etat;
    protected boolean isCoupleSepare;
    protected PcaGenre pcaGenre;
    protected String desc;

    public RpcPlausiCommonCalculData(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    public Montant getDiff() {
        return diff;
    }

    public void setDiff(Montant diff) {
        this.diff = diff;
    }

    public Montant getPca() {
        return pca;
    }

    public void setPca(Montant pca) {
        this.pca = pca;
    }

    // public Montant getMontantCalculateur() {
    // return montantCalculateur;
    // }
    //
    // public void setMontantCalculateur(Montant montantCalculateur) {
    // this.montantCalculateur = montantCalculateur;
    // }

    public PcaEtatCalcul getEtat() {
        return etat;
    }

    public void setEtat(PcaEtatCalcul etat) {
        this.etat = etat;
    }

    public boolean isCoupleSepare() {
        return isCoupleSepare;
    }

    public void setCoupleSepare(boolean isCoupleSepare) {
        this.isCoupleSepare = isCoupleSepare;
    }

    public PcaGenre getPcaGenre() {
        return pcaGenre;
    }

    public void setPcaGenre(PcaGenre pcaGenre) {
        this.pcaGenre = pcaGenre;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setRevenu(Revenu revenu) {
        this.revenu = revenu;
    }

    public void setDepense(Depense depense) {
        this.depense = depense;
    }

    public void setMontantCalcule(Montant montantCalcule) {
        this.montantCalcule = montantCalcule;
    }

    public void setSumDepense(Montant sumDepense) {
        this.sumDepense = sumDepense;
    }

    public void setSumRevenu(Montant sumRevenu) {
        this.sumRevenu = sumRevenu;
    }

    protected class Revenu {
        public Montant FC20;
        public Montant FC21;
        public Montant FC22;
        public Montant FC23;
        public Montant FC24;
        public Montant FC41;

        public Montant E2;
        public Montant E3;
        public Montant E4;
        public Montant E5;
        public Montant E12;
        public Montant E13;

        public Montant sum() {
            return FC20.add(FC21).add(FC22).add(FC23).add(FC24).add(FC41).add(E2).add(E3).add(E4).add(E5).add(E12)
                    .add(E13);
        }
    }

    protected class Depense {
        public Montant FC19;
        public Montant FC32;
        public Montant FC33;
        public Montant E20;
        public Montant E22;
        public Montant E23;
        public Montant E24;
        public Montant E26;

        public Montant sum() {
            return FC19.add(FC32).add(FC33).add(E20).add(E22).add(E23).add(E24).add(E26);
        }
    }

    public Revenu getRevenu() {
        return revenu;
    }

    public Depense getDepense() {
        return depense;
    }

    public Montant getMontantCalcule() {
        return montantCalcule;
    }

    public Montant getSumDepense() {
        return sumDepense;
    }

    public Montant getSumRevenu() {
        return sumRevenu;
    }

    public boolean isReforme() {
        return isReforme;
    }

    public void setReforme(boolean reforme) {
        isReforme = reforme;
    }
}
