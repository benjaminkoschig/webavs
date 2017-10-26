package ch.globaz.pegasus.rpc.plausi.intra.pi027;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcVitalNeedsCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

class RpcPlausiPI027Data extends RpcPlausiHeader {

    public enum ParSituationCouple {
        PAR0,
        PAR1,
        PAR2,
        PAR3
    }

    String idPca;
    ParSituationCouple par;
    RpcVitalNeedsCategory category;
    int nbTotalEnfants;
    Montant FC16;
    Montant abattement;
    Montant par1;
    Montant par2;
    Montant par3;
    boolean isCoupleSepare;

    public RpcPlausiPI027Data(RpcPlausiMetier<RpcPlausiPI027Data> plausi) {
        super(plausi);
    }

    /**
     * SI COUPLE NON SEPARE ALORS
     * -- SI VITALNEEDS = NO NEEDS ALORS
     * ---- par1 (si adulte home)
     * ---- OU
     * ---- par3 (enfant home)
     * -- SI VITALNEEDS = PERSONNE SEUL ALORS
     * ---- par1 + nbEnfant * par3
     * ---- OU
     * ---- par3 + nbEnfant * par3 (Personne seule ayant un montant de l’abattement d’un enfant)
     * -- SI VITALNEEDS = COUPLE ALORS
     * ---- par2 + nbEnfant * par3
     * -- SI VITAL NEEDS = ORPHELIN/ENFANT ALORS
     * ---- par3 + nbEnfant * par3
     * ---- OU
     * ---- par1 + nbEnfant * par3 (Enfant ayant un montant de l'abattement d'une personne seul)
     * SINON SI COUPLE SEPARE ALORS
     * -- (par2 + somme des enfants des deux personnes * par3) / 2
     */
    @Override
    public boolean isValide() {
        resolveVitalNeeds();
        return FC16.substract(abattement).isZero();
    }

    private void resolveVitalNeeds() {
        abattement = new Montant(0);
        switch (par) {
            case PAR1:
                abattement = abattement.add(par1);
                break;
            case PAR2:
                abattement = abattement.add(par2);
                break;
            case PAR3:
                abattement = abattement.add(par3);
                break;
            case PAR0:
            default:
                break;
        }

        if (nbTotalEnfants > 0 && !ParSituationCouple.PAR0.equals(par)) {
            abattement = abattement.add(par3.multiply(nbTotalEnfants));
        }
    }

}
