package ch.globaz.pegasus.rpc.plausi.calcul;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.pegasus.business.domaine.pca.PcaGenre;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiCalculData extends RpcPlausiHeader {

    private final Revenu revenu = new Revenu();
    private final Depense depense = new Depense();
    private Montant montantCalcule;
    private Montant sumDepense;
    private Montant sumRevenu;
    private Montant diff;
    Montant pca;
    Montant montantCalculateur;

    PcaEtatCalcul etat;
    boolean isCoupleSepare;
    PcaGenre pcaGenre;
    String desc;

    public RpcPlausiCalculData(RpcPlausi<RpcPlausiCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        sumDepense = depense.sum();
        sumRevenu = revenu.sum();
        montantCalcule = sumDepense.substract(sumRevenu).divide(12).arrondiAUnIntierSupperior();
        diff = pca.substract(montantCalcule);

        // on test l'ocroie partiel
        if (montantCalcule.isNegative() && montantCalcule.isNegative()) {

        }

        if (montantCalcule.isNegative() && etat.isRefus()) {
            return true;
        } else if (montantCalcule.isNegative() && etat.isOctroiePartiel()) {
            return true;
        }
        // IL n'y a pas de pc en dessous de 10.-
        if (montantCalcule.isPositive() && montantCalcule.less(10)) {
            return true;
        }
        return diff.isZero() || diff.abs().getBigDecimalValue().intValue() == 1;
    }

    class Revenu {
        Montant FC20;
        Montant FC21;
        Montant FC22;
        Montant FC23;
        Montant FC24;
        Montant FC41;

        Montant E2;
        Montant E3;
        Montant E4;
        Montant E5;
        Montant E12;
        Montant E13;

        Montant sum() {
            return FC20.add(FC21).add(FC22).add(FC23).add(FC24).add(FC41).add(E2).add(E3).add(E4).add(E5).add(E12)
                    .add(E13);
        }
    }

    class Depense {
        Montant FC19;
        Montant FC32;
        Montant FC33;
        Montant E20;
        Montant E22;
        Montant E23;
        Montant E26;

        Montant sum() {
            return FC19.add(FC32).add(FC33).add(E20).add(E22).add(E23).add(E26);
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

}
