package globaz.osiris.api;

/**
 * API d�di�e au type d'op�ration bulletin neutre.
 * 
 * @author DDA
 * 
 */
public interface APIOperationBulletinNeutre extends APIOperation {

    public String getAnneeCotisation();

    public String getIdCaisseProfessionnelle();

    public String getIdCompte();

    public String getLibelle();

    public String getTaux();

    public void setAnneeCotisation(String newAnneeCotisation);

    public void setIdCaisseProfessionnelle(String idCaisseProfessionnelle);

    public void setIdCompte(String newIdCompte);

    public void setLibelle(String newLibelle);

    public void setTaux(String newTaux);

}
