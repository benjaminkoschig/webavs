package globaz.helios.db.interfaces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGMandat;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Gestion de l'exerice comptable mise en session !!!
 * 
 * @author DDA
 * 
 */
public abstract class CGNeedExerciceComptable extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /* Destination ou se rendre apres choix de l'exercice comptable */
    public final static String SESSION_DESTINATION = "CGExerciceComptableDestination";

    /* Execrice comptable (qui contient aussi un objet mandat) */
    public final static String SESSION_EXERCICECOMPTABLE = "CGExerciceComptable";

    protected CGExerciceComptable exerciceComptable = null;
    protected java.lang.String idExerciceComptable = "";

    public CGNeedExerciceComptable() {
        super();
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        checkExerciceComptable();
    }

    /**
     * Permet de controller que l'exerice comptable contenu dans l'objet est toujours synchronise avec celui qui est
     * dans la base de donnee.
     */
    public void checkExerciceComptable() throws Exception {
        // controle la validite de l'exercice comptable content dans cet objet
        if (isMandatEmpty()) {
            // préventif, ne doit pas arriver !
            throw new Exception(getSession().getLabel("NEED_EXERCICE_COMPTABLE_ERREUR"));
        }

    }

    /**
     * Retrouve l'exercice comptable.
     * 
     * @return CGExerciceComptable
     */
    public CGExerciceComptable getExerciceComptable() throws Exception {
        if (exerciceComptable == null) {
            if (!JadeStringUtil.isIntegerEmpty(idExerciceComptable)) {
                exerciceComptable = new CGExerciceComptable();
                exerciceComptable.setSession(getSession());
                exerciceComptable.setIdExerciceComptable(idExerciceComptable);
                exerciceComptable.retrieve();
                if (exerciceComptable.getMandat() == null) {
                    exerciceComptable.setMandat(new CGMandat());
                }
                exerciceComptable.getMandat().setIdMandat(exerciceComptable.getIdMandat());
                exerciceComptable.getMandat().setSession(getSession());
                exerciceComptable.getMandat().retrieve();
            }
        }
        return exerciceComptable;
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * L'exercice comptable existe-t'il ?
     * 
     * @return boolean
     */
    public boolean isExerciceComptableEmpty() throws Exception {
        if (getExerciceComptable() == null) {
            return true;
        }
        return false;
    }

    /**
     * Le mandat doit exister dans l'exercice.
     * 
     * @return boolean
     */
    public boolean isMandatEmpty() throws Exception {
        if (isExerciceComptableEmpty() || (getExerciceComptable().getMandat() == null)) {
            return true;
        }
        return false;
    }

    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }
}
