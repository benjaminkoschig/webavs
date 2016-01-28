package ch.globaz.corvus.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;

/**
 * <p>
 * Contient les informations, au moment de la création d'une demande, d'une rente payée en trop.
 * </p>
 * <p>
 * Cela arrive lorsque la même personne est au bénéfice d'une rente dans la nouvelle demande (celle contenu dans cet
 * objet) ainsi que déjà au bénéfice d'une rente alors que celles-ci se chevauchent au niveau des mois de droit : et
 * donc la rentes de l'ancien droit (avant la création de la demande) a été payée en trop (à tort)
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
     * @return le type de cette rente versée à tort
     */
    public TypeRenteVerseeATort getType() {
        return type;
    }

    /**
     * (re-)défini le type de cette rente versée à tort
     * 
     * @param type
     *            une type de rente versée à tort
     * @throws NullPointerException
     *             si le type passé en paramètre est null
     */
    public void setType(final TypeRenteVerseeATort type) {
        Checkers.checkNotNull(type, "renteVerseeATort.type");
        this.type = type;
    }
}
