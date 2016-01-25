package globaz.osiris.api;

/**
 * Insérez la description du type ici. Date de création : (08.02.2002 09:06:30)
 * 
 * @author: Administrator
 */
public interface APIOperationOrdreVersement extends APIOperation {
    java.lang.String BVR = "210002";
    java.lang.String VIREMENT = "210001";

    public String getCodeISOMonnaieBonification();

    public String getCodeISOMonnaieDepot();

    public Boolean getEstBloque();

    public Boolean getEstRetire();

    public String getIdAdressePaiement();

    public String getIdOrdreGroupe();

    public String getIdOrganeExecution();

    public String getMontant();

    public String getMotif();

    public String getNatureOrdre();

    public String getNumTransaction();

    public String getTypeVirement();

    public void setCodeISOMonnaieBonification(String newCodeISOMonnaieBonification);

    public void setCodeISOMonnaieDepot(String newCodeISOMonnaieDepot);

    public void setEstBloque(Boolean newEstBloque);

    public void setEstRetire(Boolean newEstRetire);

    public void setIdAdressePaiement(String newIdAdressePaiement);

    public void setIdOrganeExecution(String newIdOrganeExecution);

    public void setMontant(String newMontant);

    public void setMotif(String newMotif);

    public void setNatureOrdre(String newNatureOrdre);

    public void setTypeVirement(String newTypeVirement);
}
