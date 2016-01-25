package ch.globaz.common.domaine;

import java.util.HashSet;

/**
 * <p>
 * Classe abstraite implémentant {@link #hashCode()} et {@link #equals(Object)}.
 * </p>
 * <p>
 * Cette implementation prend en compte, pour ces deux méthodes, la classe final après héritage de l'objet. Ceci permet
 * de ne pas avoir a ré-implementer ces deux méthodes pour chaque objet ayant un ID unique et d'être sûr qu'il n'y aura
 * pas deux fois le même objet dans un {@link HashSet} par exemple
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
