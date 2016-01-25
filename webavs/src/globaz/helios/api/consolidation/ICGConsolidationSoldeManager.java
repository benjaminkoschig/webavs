package globaz.helios.api.consolidation;

import globaz.globall.api.BIContainer;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObject;

public interface ICGConsolidationSoldeManager extends BIContainer {
    public void find(BITransaction transaction) throws Exception;

    public void find(BITransaction transaction, int resultBufferSize) throws Exception;

    public BIPersistentObject get(int idx);

    public StringBuffer getErrors();

    public BIEntity getFirstEntity();

    public Boolean getForEstPeriode();

    public String getForIdCompte();

    public String getForIdExerComptable();

    public String getForIdMandat();

    public String getForIdPeriodeComptable();

    public boolean hasErrors();

    public void setForEstPeriode(Boolean newForEstPeriode);

    public void setForIdCompte(String newIdCompte);

    public void setForIdExerComptable(String newIdExerComptable);

    public void setForIdMandat(String newIdMandat);

    public void setForIdPeriodeComptable(String newIdPeriodeComptable);

    public void setForIdSuccursale(String idSuccursale);
}
