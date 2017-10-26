package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

class NbPersonneComprise {
    private String idPca;
    private String idPlanCal;
    private int nb;

    public String getIdPlanCal() {
        return idPlanCal;
    }

    public void setIdPlanCal(String idPlanCal) {
        this.idPlanCal = idPlanCal;
    }

    public String getIdPca() {
        return idPca;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }
}
