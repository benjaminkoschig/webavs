package globaz.helios.api.consolidation;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

public interface ICGConsolidationSolde extends BIEntity {
    public void add(BITransaction transaction) throws Exception;

    public void delete(BITransaction transaction) throws Exception;

    public String getAvoir();

    public String getAvoirProvisoire();

    public String getDoit();

    public String getDoitProvisoire();

    public String getIdCompte();

    public String getIdExerComptable();

    public String getIdMandat();

    public String getIdPeriodeComptable();

    public String getSolde();

    public String getSoldeProvisoire();

    public Boolean isEstPeriode();

    public void setAvoir(String newAvoir);

    public void setAvoirProvisoire(String newAvoirProvisoire);

    public void setDoit(String newDoit);

    public void setDoitProvisoire(String newDoitProvisoire);

    public void setEstPeriode(Boolean newEstPeriode);

    public void setIdCompte(String newIdCompte);

    public void setIdExerComptable(String idExerciceComptable);

    public void setIdMandat(String newIdMandat);

    public void setIdPeriodeComptable(String newIdPeriodeComptable);

    public void setIdSuccursale(String newIdSuccursale);

    public void setSolde(String newSolde);

    public void setSoldeProvisoire(String newSoldeProvisoire);

    public void update(BITransaction transaction) throws Exception;
}
