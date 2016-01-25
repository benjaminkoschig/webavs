package globaz.campus.vb.process;

import globaz.campus.process.chargementLot.GEProcessChargementLot;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import java.util.Vector;

public class GEProcessChargementLotViewBean extends GEProcessChargementLot implements BIPersistentObject,
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final Vector getIdEtNomFormatFichier() throws Exception {
        Vector ecole = new Vector();
        // fichier plat
        ecole.add(new String[] { FORMAT_FICHIER_TEXTE, "Fichier plat (texte)" });
        // fichier excel
        ecole.add(new String[] { FORMAT_FICHIER_EXCEL, "Fichier Excel" });
        return ecole;
    }

    private String id = null;

    public GEProcessChargementLotViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void update() throws Exception {
    }
}
