package ch.globaz.pegasus.rpc.plausi.intra.pi025;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

class RpcPlausiPI025Data extends RpcPlausiCommonCalculData {

    public enum ParSituationCouple {
        PAR0,
        PAR1,
        PAR2,
        PAR3,
        PAR4;
    }

    String idPca;
    boolean isDomicile;
    ParSituationCouple par;
    int nbTotalEnfants;
    Montant FC33;
    Montant besoinsVitaux;
    Montant par1;
    Montant par2;
    Montant par3;
    Montant par4;
    Montant par5;
    Montant par6;

    public RpcPlausiPI025Data(RpcPlausiMetier<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if(isReforme){
            return true;
        }
        resolveVitalNeeds();
        return isDomicile ? FC33.substract(besoinsVitaux).isZero() : true;
    }

    private void resolveVitalNeeds() {
        besoinsVitaux = new Montant(0);
        switch (par) {
            case PAR1:
                besoinsVitaux = besoinsVitaux.add(par1);
                break;
            case PAR2:
                besoinsVitaux = besoinsVitaux.add(par2);
                break;
            case PAR3:
                besoinsVitaux = besoinsVitaux.add(par3);
                break;
            case PAR4:
                // enfant requérant compris dans nbTotalEnfants
                break;
            case PAR0:
            default:
                break;
        }

        if (nbTotalEnfants > 0 && !ParSituationCouple.PAR0.equals(par)) {
            for (int i = 1; i <= nbTotalEnfants; i++) {
                besoinsVitaux = besoinsVitaux.add(parForChild(i));
            }
        }
    }

    private Montant parForChild(int numChild) {
        switch (numChild) {
            case 0:
                return new Montant(0);
            case 1:
            case 2:
                return par4;
            case 3:
            case 4:
                return par5;
            case 5:
            default:
                return par6;
        }
    }

}
