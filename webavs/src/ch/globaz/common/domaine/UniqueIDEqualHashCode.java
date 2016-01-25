package ch.globaz.common.domaine;

import java.util.HashSet;

/**
 * <p>
 * Classe abstraite impl�mentant {@link #hashCode()} et {@link #equals(Object)}.
 * </p>
 * <p>
 * Cette implementation prend en compte, pour ces deux m�thodes, la classe final apr�s h�ritage de l'objet. Ceci permet
 * de ne pas avoir a r�-implementer ces deux m�thodes pour chaque objet ayant un ID unique et d'�tre s�r qu'il n'y aura
 * pas deux fois le m�me objet dans un {@link HashSet} par exemple
 * </p>
 */
public abstract class UniqueIDEqualHashCode {

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) {
            UniqueIDEqualHashCode uniqueIdObject = (UniqueIDEqualHashCode) obj;
            return getId().equals(uniqueIdObject.getId());
        }
        return super.equals(obj);
    }

    /**
     * @return l'identifiant unique de cette objet
     */
    public abstract Long getId();

    @Override
    public int hashCode() {
        StringBuilder hashCodeBuilder = new StringBuilder();
        hashCodeBuilder.append(this.getClass().getName()).append("(").append(getId()).append(")");
        return hashCodeBuilder.toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(this.getClass().getName()).append("(id:").append(getId()).append(")");
        return toStringBuilder.toString();
    }
}
