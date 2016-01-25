package ch.globaz.common.domaine;

import java.io.Serializable;

/**
 * "Tag" stipulant que l'objet représente un objet métier dans l'AVS
 */
public abstract class EntiteDeDomaine extends UniqueIDEqualHashCode implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final long ID_INVALIDE = -1l;

    private Long id;

    public EntiteDeDomaine() {
        super();

        id = EntiteDeDomaine.ID_INVALIDE;
    }

    public final boolean estInitialisee() {
        return !id.equals(EntiteDeDomaine.ID_INVALIDE);
    }

    /**
     * @return l'identifiant unique de cette objet
     */
    @Override
    public final Long getId() {
        return id;
    }

    /**
     * (re-)défini l'identifiant unique de cette objet
     * 
     * @param nouvelleIdentifiant
     *            un entier non null
     */
    public final void setId(Long nouvelleIdentifiant) {
        if (nouvelleIdentifiant == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        id = nouvelleIdentifiant;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getName()).append("(id:").append(getId()).append(")");
        return stringBuilder.toString();
    }
}
