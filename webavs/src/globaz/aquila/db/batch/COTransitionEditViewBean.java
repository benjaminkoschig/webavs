package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.helpers.batch.COTransitionEditHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;

/**
 * <H1>Description</H1>
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class COTransitionEditViewBean extends COTransition implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 808573517045664481L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private List etapes;
    private String idEtapeRetour;
    private boolean versEtapeRetour;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _validate(BStatement statement) {
        super._validate(statement);

        if (JadeStringUtil.isBlank(getDuree())) {
            _addError(statement.getTransaction(), getSession().getLabel("AQUILA_DUREE_TRANSITION_REQUISE"));
        }

        try {
            Class.forName("globaz.aquila.db.access.batch.transition." + getTransitionAction());
        } catch (Exception e) {
            statement.getTransaction().addErrors(getSession().getLabel("AQUILA_ACTION_TRANSITION_INCORRECTE"));
        }

        if (getAuto().booleanValue() && JadeStringUtil.isBlank(getPriorite())) {
            statement.getTransaction().addErrors(getSession().getLabel("AQUILA_PRIORITE_TRANSITION_REQUISE"));
        }
    }

    /**
     * Retourne l'étape en cours d'édition lors de l'affichage de la page de détail de la transition.
     * <p>
     * Il s'agit soit de l' {@link #getIdEtape() étape de départ}, soit de l' {@link #getIdEtapeSuivante() l'étape
     * d'arrivée} de la transition. La méthode {@link #isVersEtapeRetour()} permet de répondre à cette question.
     * </p>
     * 
     * @return Une étape ou null si elle n'a pas été renseignée dans le {@link COTransitionEditHelper helper}.
     */

    public COEtape getEtapeRetour() {
        if (isVersEtapeRetour()) {
            return getEtapeSuivante();
        } else {
            return getEtape();
        }
    }

    /**
     * Retourne la liste des étapes pour la séquence de l' {@link #getEtapeRetour() étape de retour}.
     * 
     * @return Une liste d'instances de {@link COEtape} ou null si elle n'a pas été renseignée dans le
     *         {@link COTransitionEditHelper helper}.
     */
    public List getEtapes() {
        return etapes;
    }

    /**
     * L'identifiant de l'étape en cours d'édition lors de l'affichage de la page de détail de la transition.
     * 
     * @return DOCUMENT ME!
     */
    public String getIdEtapeRetour() {
        return idEtapeRetour;
    }

    /**
     * Retourne vrai si l'{@link #getIdEtapeRetour() étape} en cours d'édition lors de l'affichage de la page de détail
     * de la transition est l' {@link #getIdEtapeSuivante() étape d'arrivée} de cette transition.
     * 
     * @return DOCUMENT ME!
     */
    public boolean isVersEtapeRetour() {
        return versEtapeRetour;
    }

    /**
     * @see #getEtapes()
     */
    public void setEtapes(List etapes) {
        this.etapes = etapes;
    }

    /**
     * @see #getIdEtapeRetour()
     */
    public void setIdEtapeRetour(String idEtapeFrom) {
        idEtapeRetour = idEtapeFrom;
    }

    /**
     * @see #isVersEtapeRetour()
     */
    public void setVersEtapeRetour(boolean versEtapeRetour) {
        this.versEtapeRetour = versEtapeRetour;
    }
}
