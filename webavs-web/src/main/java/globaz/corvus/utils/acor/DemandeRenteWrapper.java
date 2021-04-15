package globaz.corvus.utils.acor;

import ch.globaz.corvus.domaine.DemandeRente;
import globaz.prestation.utils.PRDateUtils;

public class DemandeRenteWrapper extends CleWrapper implements Comparable<DemandeRenteWrapper> {
    private DemandeRente demandeRente;

    public DemandeRenteWrapper(DemandeRente demandeRente) {
        super(demandeRente);
        this.demandeRente = demandeRente;
    }

    public DemandeRente getDemandeRente() {
        return demandeRente;
    }

    @Override
    public int compareTo(DemandeRenteWrapper demande) {
        PRDateUtils.PRDateEquality result = null;
        try {
            result = PRDateUtils.compare(getDemandeRente().getDateDebutDuDroitInitial(),
                    demande.getDemandeRente().getDateDebutDuDroitInitial());
        } catch (Exception e) {
            return 0;
        }
        switch (result) {
            case BEFORE:
                return -1;
            case EQUALS:
                return 0;
            case AFTER:
                return 1;
            case INCOMPARABLE:

            default:
                break;
        }
        return 0;
    }

}


