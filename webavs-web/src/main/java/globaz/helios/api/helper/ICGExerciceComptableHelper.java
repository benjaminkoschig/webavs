package globaz.helios.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.helios.api.ICGExerciceComptable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICGExerciceComptableHelper extends GlobazHelper implements ICGExerciceComptable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICGExerciceComptableHelper
     */
    public ICGExerciceComptableHelper() {
        super("globaz.helios.db.comptes.CGExerciceComptable");
    }

    /**
     * Constructeur du type ICGExerciceComptableHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICGExerciceComptableHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICGExerciceComptableHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICGExerciceComptableHelper(String implementationClassName) {
        super(implementationClassName);
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
     * Récupère la description de l'exercice dans la langue de l'utilisateur. La mention "Exercice" + dateDebut + un
     * tiret + dateFin + un asterix si l'Exercice comptable est clôturé. Exemple : Exercice 01.02.2002 - 01.02.2003 *.
     * si l'exercice s'étends sur l'année complète,(01.01-31.12) on simplifie la description en mentinnant uniquement
     * l'année. Exemple : Exercice 2002 *
     * 
     * Date de création : (11.11.2002 17:16:49)
     * 
     * @return java.lang.String
     */
    @Override
    public String getFullDescription() throws Exception {
        return (String) _getObject("getFullDescription", new Object[] {});
    }

    @Override
    public java.lang.String getIdExerciceComptable() {
        return (java.lang.String) _getValueObject().getProperty("idExerciceComptable");
    }

    @Override
    public java.lang.String getIdMandat() {
        return (java.lang.String) _getValueObject().getProperty("idMandat");
    }

    /**
     * Setter
     */
    @Override
    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable) {
        _getValueObject().setProperty("idExerciceComptable", newIdExerciceComptable);
    }

    /**
     * Getter
     */
    @Override
    public void setIdExerciceComptableFrom(java.lang.String date, java.lang.String idMandat) throws Exception {
        this._invoke("setIdExerciceComptableFrom", new Object[] { date, idMandat });
    }
}
