package globaz.vulpecula.vb.decomptesalaire;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.context.JadeThread;

public class PTControlerMyProdisViewBean extends BJadePersistentObjectViewBean {

    private String email;
    private String filename;
    private String destination;
    private Boolean wantControleCP;
    private Boolean wantControleSalaires;
    private String controleCP;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    @Override
    public void update() throws Exception {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return (email == null) ? JadeThread.currentUserEmail() : email;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getWantControleCP() {
        return wantControleCP;
    }

    public void setWantControleCP(Boolean wantControleCP) {
        this.wantControleCP = wantControleCP;
    }

    public Boolean getWantControleSalaires() {
        return wantControleSalaires;
    }

    public void setWantControleSalaires(Boolean wantControleSalaires) {
        this.wantControleSalaires = wantControleSalaires;
    }

    public String getControleCP() {
        return controleCP;
    }

    public void setControleCP(String controleCP) {
        this.controleCP = controleCP;
    }

}
