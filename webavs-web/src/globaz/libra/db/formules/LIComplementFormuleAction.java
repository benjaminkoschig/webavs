package globaz.libra.db.formules;

import globaz.envoi.db.parametreEnvoi.access.ENComplementFormule;
import ch.globaz.libra.constantes.ILIConstantesExternes;

/**
 * Classe représentant l'action final d'une formule.
 * 
 * @author hpe
 */
public class LIComplementFormuleAction extends ENComplementFormule {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Constante définissant la famille cs dont fait partie la valeur de ce modèle.
     */
    public static final String CS_FAMILLE = ILIConstantesExternes.CS_ACTION_FORMULE_GRP;

    /**
     * Constructor for LIComplementFormuleAction.
     */
    public LIComplementFormuleAction() {
        super();
        setCsTypeFormule(CS_FAMILLE);
    }

}