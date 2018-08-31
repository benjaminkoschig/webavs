package ch.globaz.vulpecula.documents.listesinternes;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class CaisseKey implements Comparable<CaisseKey> {
    static enum Type {
        COT,
        MS
    }

    private final String idExterne;
    private final String libelle;
    private final Type type;

    public CaisseKey(String idExterne, String libelle, Type type) {
        Validate.notEmpty(idExterne);
        Validate.notEmpty(libelle);
        Validate.notNull(type);

        this.idExterne = idExterne;
        this.libelle = libelle;
        this.type = type;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getLibelle() {
        return libelle;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(CaisseKey o) {
        int r = idExterne.compareTo(o.idExterne);
        if (r == 0) {
            return type.compareTo(o.type);
        }
        return r;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CaisseKey) {
            CaisseKey o = (CaisseKey) other;
            return new EqualsBuilder().append(idExterne, o.idExterne).append(type, o.type).isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(idExterne).append(type).toHashCode();
    }

    @Override
    public String toString() {
        return "CaisseKey [idExterne=" + idExterne + ", libelle=" + libelle + ", type=" + type + "]";
    }
}
