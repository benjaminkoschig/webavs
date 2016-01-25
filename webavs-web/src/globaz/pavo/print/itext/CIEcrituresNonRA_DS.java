/*
 * Créé le 4 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.print.itext;

import globaz.globall.db.BProcess;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.util.CIJournalIdEcritureManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 * Renvoie toutes les données nécéssaires à la création du document des CI non connus au RA
 * 
 * @author sda
 */
public class CIEcrituresNonRA_DS extends CIJournalIdEcritureManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String AVS = null;
    private List container = null;
    private CIEcriture entity = null;
    private Iterator itcontainer = null;
    private BProcess process = null;

    private String _getAVS() {

        AVS = entity.getNssFormate();

        return AVS;
    }

    public Collection getCollectionData() throws JRException {
        if (container == null) {
            container = new ArrayList();
            while (next()) {
                Map map = new HashMap();
                map.put("COL_1", getFieldValue("COL_1"));
                map.put("COL_2", getFieldValue("COL_2"));
                container.add(map);
            }
            if (0 == size()) {
                Map map = new HashMap();
                map.put("COL_1", "");
                map.put("COL_2", "");
                container.add(map);
            }
        }
        return container;
    }

    private Object getFieldValue(String fieldName) throws JRException {
        // Verify si le compteIndividuel change -> nouveau document pour
        // l'impression de liste de document
        // retourne chaque champ
        if (fieldName.equals("COL_1")) {
            return _getAVS();
        }
        if (fieldName.equals("COL_2")) {
            return entity.getNomPrenom();
        }
        return "";
    }

    public boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            if (itcontainer == null) {
                find(0);
                itcontainer = getContainer().iterator();
            }
            // lit le nouveau entity
            if (itcontainer.hasNext()) {
                entity = (CIEcriture) itcontainer.next();
            }
        } catch (Exception e) {
        }
        // vrai : il existe une entity, faux fin du select
        if ((process != null) && process.isAborted()) {
            return false;
        } else {
            return (entity != null);
        }
    }
}
