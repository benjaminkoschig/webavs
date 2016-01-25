/*
 * Cr�� le 20 mai 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.draco.print.itext;

import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.globall.db.BProcess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 * Permet de cr�er la lettre d'acompagnement pour liste de pr�-impression des d�clarations de salaires
 * 
 * @author sda
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class Doc1_PreImpLettre_DS extends DSDeclarationListViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List container = null;
    private DSDeclarationViewBean entity = null;
    private Iterator itcontainer = null;
    private BProcess process = null;

    public Doc1_PreImpLettre_DS() {
        super();
    }

    public Collection getCollectionData() throws JRException {
        if (container == null) {
            container = new ArrayList();
            while (next()) {
                Map map = new HashMap();
                map.put("COL_1", getFieldValue("COL_1"));
                container.add(map);
            }
            if (0 == size()) {
                Map map = new HashMap();
                map.put("COL_1", "");
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
            return String.valueOf(Integer.parseInt(entity.getAnnee()));
        }
        return "";
    }

    /**
     * Remet itcontainer � null
     * 
     * @see net.sf.jasperreports.engine.JRRewindableDataSource#moveFirst()
     */
    public void moveFirst() throws JRException {
        itcontainer = null;
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
                entity = (DSDeclarationViewBean) itcontainer.next();
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

    /**
     * remet container � null
     * 
     * @param list
     */
    public void setContainer() {
        container = null;
    }
}
