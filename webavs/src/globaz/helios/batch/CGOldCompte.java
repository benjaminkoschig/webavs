package globaz.helios.batch;

public class CGOldCompte {
    private String idMandat;
    private String idExterne;
    private String type;
    private String libelleFr;

    public CGOldCompte() {

    }

    public String getIdMandat() {
        return idMandat;
    }

    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLibelleFr() {
        return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    public String getIdExterneFormatte() {
        return idExterne.substring(0, 4) + "." + idExterne.substring(4, 8) + "." + idExterne.substring(8);
    }
}
