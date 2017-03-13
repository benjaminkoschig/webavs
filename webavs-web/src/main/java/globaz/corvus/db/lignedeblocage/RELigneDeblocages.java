/*
 * Globaz SA.
 */
package globaz.corvus.db.lignedeblocage;

import java.util.ArrayList;
import java.util.List;

public class RELigneDeblocages {

    private List<RELigneDeblocage> lignesDeblocage;

    public RELigneDeblocages() {
        lignesDeblocage = new ArrayList<RELigneDeblocage>();
    }

    public boolean isEmpty() {
        return lignesDeblocage.isEmpty();
    }

    public void addLigneDeblocage(RELigneDeblocage ligneDeblocage) {
        lignesDeblocage.add(ligneDeblocage);
    }

}
