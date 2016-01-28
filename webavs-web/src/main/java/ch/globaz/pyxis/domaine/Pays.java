package ch.globaz.pyxis.domaine;

import java.util.HashMap;
import java.util.Map;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pyxis.domaine.constantes.CodeIsoPays;

public class Pays extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CodeIsoPays codeIso;
    private Map<Langues, String> traductionParLangue;

    public Pays() {
        super();

        codeIso = CodeIsoPays.SUISSE;
        traductionParLangue = new HashMap<Langues, String>();
    }

    public CodeIsoPays getCodeIso() {
        return codeIso;
    }

    public Map<Langues, String> getTraductionParLangue() {
        return traductionParLangue;
    }

    /**
     * (re-)d�fini le code ISO pour ce pays
     * 
     * @param codeIso
     * @throws NullPointerException
     *             si le code ISO pass� en param�tre est null
     */
    public void setCodeIso(final CodeIsoPays codeIso) {
        Checkers.checkNotNull(codeIso, "pays.codeIso");
        this.codeIso = codeIso;
    }

    /**
     * (re-)d�fini les traductions pour ce pays
     * 
     * @param traductionParLangue
     *            les traductions de ce pays
     * @throws NullPointerException
     *             si la liste des traductions pass�e en param�tres est null
     */
    public void setTraductionParLangue(final Map<Langues, String> traductionParLangue) {
        Checkers.checkNotNull(traductionParLangue, "pays.traductions");
        this.traductionParLangue = traductionParLangue;
    }
}
