package globaz.helios.itext.list.releveAVS;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public interface CGIReleveAVS_Bean {

    public String getCOL_0();

    public String getCOL_1();

    public Double getCOL_2();

    public Double getCOL_3();

    public Double getCOL_4();

    /**
     * Returns the idExerciceComptable.
     * 
     * @return String
     */
    public String getIdExerciceComptable();

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat();

    /**
     * Returns the idPeriodeComptable.
     * 
     * @return String
     */
    public String getIdPeriodeComptable();

    /**
     * Returns the isProvisoire.
     * 
     * @return boolean
     */
    public boolean isProvisoire();

    public boolean prepareValue(BEntity entity, BTransaction transaction, BSession session);

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(String idExerciceComptable);

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat);

    /**
     * Sets the idPeriodeComptable.
     * 
     * @param idPeriodeComptable
     *            The idPeriodeComptable to set
     */
    public void setIdPeriodeComptable(String idPeriodeComptable);

    /**
     * Sets the isProvisoire.
     * 
     * @param isProvisoire
     *            The isProvisoire to set
     */
    public void setIsProvisoire(boolean isProvisoire);
}
