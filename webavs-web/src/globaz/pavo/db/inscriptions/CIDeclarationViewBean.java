package globaz.pavo.db.inscriptions;

import globaz.pavo.vb.CIAbstractPersistentViewBean;

/**
 * @author OCA
 * 
 */
public class CIDeclarationViewBean extends CIAbstractPersistentViewBean {

    private String accepteAnneeEnCours = "";
    private String accepteEcrituresNegatives = "";
    private String accepteLienDraco = "";
    private String anneeCotisation = "";
    private String dateReceptionForced = "";
    private String filename = "";
    private String forNumeroAffilie = "";
    private String nombreInscriptions = "";
    private String simulation = "";
    private String totalControle = "";
    private String type = "";

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getAccepteAnneeEnCours() {
        return accepteAnneeEnCours;
    }

    public String getAccepteEcrituresNegatives() {
        return accepteEcrituresNegatives;
    }

    public String getAccepteLienDraco() {
        return accepteLienDraco;
    }

    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    public String getDateReceptionForced() {
        return dateReceptionForced;
    }

    public String getFilename() {
        return filename;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public String getNombreInscriptions() {
        return nombreInscriptions;
    }

    public String getSimulation() {
        return simulation;
    }

    public String getTotalControle() {
        return totalControle;
    }

    public String getType() {
        return type;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setAccepteAnneeEnCours(String accepteAnneeEnCours) {
        this.accepteAnneeEnCours = accepteAnneeEnCours;
    }

    public void setAccepteEcrituresNegatives(String accepteEcrituresNegatives) {
        this.accepteEcrituresNegatives = accepteEcrituresNegatives;
    }

    public void setAccepteLienDraco(String accepteLienDraco) {
        this.accepteLienDraco = accepteLienDraco;
    }

    public void setAnneeCotisation(String anneeCotisation) {
        this.anneeCotisation = anneeCotisation;
    }

    public void setDateReceptionForced(String dateReceptionForced) {
        this.dateReceptionForced = dateReceptionForced;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public void setNombreInscriptions(String nombreInscriptions) {
        this.nombreInscriptions = nombreInscriptions;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    public void setTotalControle(String totalControle) {
        this.totalControle = totalControle;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
