package globaz.osiris.vb.lettrage;

public class CAChercherPlageViewBean extends CALettrageViewBean {
    private String nbPlages = "";
    private String role = "";

    /*
     * Getter et Setter
     */
    public String getNbPlages() {
        return nbPlages;
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setNbPlages(String nbPlages) {
        this.nbPlages = nbPlages;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
}