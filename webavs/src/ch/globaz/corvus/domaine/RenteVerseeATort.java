package ch.globaz.corvus.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;

/**
 * <p>
 * Contient les informations, au moment de la cr�ation d'une demande, d'une rente pay�e en trop.
 * </p>
 * <p>
 * Cela arrive lorsque la m�me personne est au b�n�fice d'une rente dans la nouvelle demande (celle contenu dans cet
 * objet) ainsi que d�j� au b�n�fice d'une rente alors que celles-ci se chevauchent au niveau des mois de droit : et
 * donc la rentes de l'ancien droit (avant la cr�ation de la demande) a �t� pay�e en trop (� tort)
 * </p>
 */
public class RenteVerseeATort extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TypeRenteVerseeATort type;

    public RenteVerseeATort() {
        super();

        type = TypeRenteVerseeATort.SAISIE_MANUELLE;
    }

    /**
     * @return le type de cette rente vers�e � tort
     */
    public TypeRenteVerseeATort getType() {
        return type;
    }

    /**
     * (re-)d�fini le type de cette rente vers�e � tort
     * 
     * @param type
     *            une type de rente vers�e � tort
     * @throws NullPointerException
     *             si le type pass� en param�tre est null
     */
    public void setType(final TypeRenteVerseeATort type) {
        Checkers.checkNotNull(type, "renteVerseeATort.type");
        this.type = type;
    }
}
