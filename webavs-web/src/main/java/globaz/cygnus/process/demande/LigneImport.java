package globaz.cygnus.process.demande;

import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Montant;

public interface LigneImport extends Comparable<LigneImport> {

    public Integer getNumeroLigne();

    public Exception getException();

    public Map<String, List<String>> getErrors();

    public String getDescription();

    public Montant getTotal();
}
