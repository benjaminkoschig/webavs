package globaz.hermes.db.gestion;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.hermes.db.access.HEAnnoncesOrphelines;
import globaz.hermes.db.access.HEAnnoncesOrphelinesManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (13.05.2003 12:45:30)
 * 
 * @author: ado
 */
public class HEAnnoncesOrphelinesViewBean extends HEAnnoncesOrphelines implements FWViewBeanInterface {

    private static final long serialVersionUID = 1L;

    /**
     * Commentaire relatif au constructeur HEAnnoncesOrphelinesViewBean.
     */
    public HEAnnoncesOrphelinesViewBean() {
        super();
    }

    /**
     * Method getPossibleArc.
     * 
     * @return Vector
     * @throws Exception
     */
    public Vector<String[]> getPossibleArc(int casDeFigure) throws Exception {
        Vector<String[]> vec = new Vector<String[]>();
        String[] newLine = new String[2];
        // Sauvegarde de la ref unique pour ne pas avoir de doublon dans la jsp.
        List<String> listRefUnique = new ArrayList<String>();
        newLine[0] = "";
        newLine[1] = "-----";
        vec.addElement(newLine);
        HEAnnoncesOrphelinesManager orphMgr = getArcPossible(casDeFigure);
        for (int i = 0; i < orphMgr.size(); i++) {
            HEAnnoncesOrphelines arcPossible = (HEAnnoncesOrphelines) orphMgr.getEntity(i);
            // PO 8128
            if (!listRefUnique.contains(arcPossible.getRefUnique())) {
                listRefUnique.add(arcPossible.getRefUnique());
                // Nécessairfe d'avoir un vecteur de string pour la jsp (FWListSelectTag)
                newLine = new String[2];
                newLine[0] = arcPossible.getRefUnique();
                newLine[1] = arcPossible.getTypeAnnonce()
                        + " - ("
                        + globaz.commons.nss.NSUtil.formatAVSNew(arcPossible.getNumAvs(), arcPossible
                                .getNumeroAvsNNSS().equals("true")) + ") " + arcPossible.GetStatusLibelle();
                vec.addElement(newLine);
            }
        }
        return vec;
    }
}
