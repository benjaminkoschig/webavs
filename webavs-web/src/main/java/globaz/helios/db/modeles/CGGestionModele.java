package globaz.helios.db.modeles;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.jade.log.JadeLogger;
import java.util.ArrayList;

public class CGGestionModele extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEnteteModeleEcriture = "";
    private String idMandat = "";
    // L'id du modèle
    private String idModeleEcriture = "";

    private String libelleModele = "";

    private ArrayList lignes = new ArrayList();

    private String pieceModele = "";

    @Override
    protected String _getTableName() {
        // Not used here
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // Not used here
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not used here
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not used here
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not used here
    }

    public String getIdEnteteModeleEcriture() {
        return idEnteteModeleEcriture;
    }

    public String getIdMandat() {
        return idMandat;
    }

    public String getIdModeleEcriture() {
        return idModeleEcriture;
    }

    public CGExerciceComptable getLastExercice() {
        CGExerciceComptableManager mgr = new CGExerciceComptableManager();
        mgr.setForIdMandat(getIdMandat());
        mgr.setSession(getSession());
        mgr.setOrderBy(CGExerciceComptableManager.TRI_DATE_FIN_DESC);

        try {
            mgr.find(null, 1);
        } catch (Exception e) {
            return null;
        }

        if (mgr.size() > 0) {
            return (CGExerciceComptable) mgr.getFirstEntity();
        } else {
            return null;
        }

    }

    public String getLibelleModele() {
        return libelleModele;
    }

    public ArrayList getLignes() {
        return lignes;
    }

    public CGMandat getMandat() {
        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());

        try {
            mandat.retrieve();

            if (mandat.isNew()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return mandat;
    }

    public String getModeleIdMandat() {
        CGModeleEcriture modele = new CGModeleEcriture();
        modele.setSession(getSession());

        modele.setIdModeleEcriture(getIdModeleEcriture());

        try {
            modele.retrieve();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        if (modele.hasErrors() || modele.isNew()) {
            return "";
        } else {
            return modele.getIdMandat();
        }
    }

    /**
     * @deprecated Use modele piece while executing
     * @return
     */
    @Deprecated
    public String getPieceModele() {
        return pieceModele;
    }

    public void setDefaultIdMandat() {
        CGMandatManager manager = new CGMandatManager();
        manager.setSession(getSession());

        try {
            manager.find();
        } catch (Exception e) {
            return;
        }

        if (manager.hasErrors() || manager.size() == 0) {
            return;
        } else {
            setIdMandat(((CGMandat) manager.getFirstEntity()).getIdMandat());
        }
    }

    public void setIdEnteteModeleEcriture(String idEnteteModeleEcriture) {
        this.idEnteteModeleEcriture = idEnteteModeleEcriture;
    }

    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    public void setIdModeleEcriture(String idModeleEcriture) {
        this.idModeleEcriture = idModeleEcriture;
    }

    public void setLibelleModele(String libelleModele) {
        this.libelleModele = libelleModele;
    }

    public void setLignes(ArrayList lignes) {
        this.lignes = lignes;
    }

    /**
     * @deprecated Use modele piece while executing
     * @param pieceModele
     */
    @Deprecated
    public void setPieceModele(String pieceModele) {
        this.pieceModele = pieceModele;
    }

}
