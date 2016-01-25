package ch.globaz.vulpecula.util;

/**
 * POJO contenant les informations des codes systèmes
 */
public class CodeSystem {
    private String id;
    private String code;
    private String libelle;
    private String ordre;
    private boolean actif;

    public CodeSystem() {

    }

    public CodeSystem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CodeSystem) {
            CodeSystem other = (CodeSystem) obj;
            return other.id.equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
