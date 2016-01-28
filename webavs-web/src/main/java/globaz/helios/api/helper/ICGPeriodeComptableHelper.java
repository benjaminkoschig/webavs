package globaz.helios.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.helios.api.ICGPeriodeComptable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICGPeriodeComptableHelper extends GlobazHelper implements ICGPeriodeComptable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICGPeriodeComptableHelper
     */
    public ICGPeriodeComptableHelper() {
        super("globaz.helios.db.comptes.CGPeriodeComptable");
    }

    /**
     * Constructeur du type ICGPeriodeComptableHelper
     * 
     * @param valueObject
     *            le Value Object contenant les donn�es
     */
    public ICGPeriodeComptableHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICGPeriodeComptableHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'impl�mentation
     */
    public ICGPeriodeComptableHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public java.lang.String getCode() {
        return (java.lang.String) _getValueObject().getProperty("code");
    }

    @Override
    public java.lang.String getDateDebut() {
        return (java.lang.String) _getValueObject().getProperty("dateDebut");
    }

    @Override
    public java.lang.String getDateFin() {
        return (java.lang.String) _getValueObject().getProperty("dateFin");
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.11.2002 10:15:22)
     * 
     * @return java.lang.String
     */
    @Override
    public String getFullDescription() throws Exception {
        return (String) _getObject("getFullDescription", new Object[] {});
    }

    @Override
    public java.lang.String getIdJournal() {
        return (java.lang.String) _getValueObject().getProperty("idJournal");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdPeriodeComptable() {
        return (java.lang.String) _getValueObject().getProperty("idPeriodeComptable");
    }

    @Override
    public java.lang.String getIdTypePeriode() {
        return (java.lang.String) _getValueObject().getProperty("idTypePeriode");
    }

    /**
     * Setter
     */
    @Override
    public void setIdPeriodeComptable(java.lang.String newIdPeriodeComptable) {
        _getValueObject().setProperty("idPeriodeComptable", newIdPeriodeComptable);
    }

    /**
     * D�finit l'ID de la p�riode comptable via un code standard AVS et un ID d'exercice comptable.
     * 
     * @param code
     *            code
     * @param idExerciceComptable
     *            idExerciceComptable
     */
    @Override
    public void setIdPeriodeComptableFrom(java.lang.String code, java.lang.String idExerciceComptable)
            throws java.lang.Exception {
        this._invoke("setIdPeriodeComptableFrom", new Object[] { code, idExerciceComptable });
    }
}
