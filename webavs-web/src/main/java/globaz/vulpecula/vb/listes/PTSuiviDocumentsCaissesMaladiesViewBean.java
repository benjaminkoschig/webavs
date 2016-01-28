package globaz.vulpecula.vb.listes;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;

public class PTSuiviDocumentsCaissesMaladiesViewBean extends PTListeProcessViewBean {
    private final Map<Integer, String> typesListes = new HashMap<Integer, String>();

    // Critères
    private int typeListe;
    private boolean simulation;

    @Override
    public void retrieve() throws Exception {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        String typeListe1 = session.getLabel("LISTE_SUIVI_CAISSES_TYPE_LISTE1");
        String typeListe2 = session.getLabel("LISTE_SUIVI_CAISSES_TYPE_LISTE2");

        typesListes.put(SuiviCaisseMaladie.TYPE_01_05, typeListe1);
        typesListes.put(SuiviCaisseMaladie.TYPE_06_FA, typeListe2);
    }

    public Map<Integer, String> getTypesListes() {
        return typesListes;
    }

    public int getTypeListe() {
        return typeListe;
    }

    public void setTypeListe(int typeListe) {
        this.typeListe = typeListe;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public boolean getSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }
}
