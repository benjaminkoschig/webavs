package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFAssurance;
import java.util.Hashtable;
import java.util.List;

/**
 * Interface sur l'assurance Date de création : (28.05.2002 09:11:43)
 * 
 * @author: David Girardin
 */
public class IAFAssuranceHelper extends GlobazHelper implements IAFAssurance {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String[] METHODS_TO_LOAD = new String[] { "getAssuranceCanton", "getAssuranceGenre",
            "getAssuranceId", "getAssuranceLibelleAl", "getAssuranceLibelleFr", "getAssuranceLibelleIt",
            "getAssuranceLibelleCourtAl", "getAssuranceLibelleCourtFr", "getAssuranceLibelleCourtIt", "getRubriqueId",
            "getTypeAssurance", "getIsAPI" };

    /**
     * Constructeur du type IAFCotisationHelper
     */
    public IAFAssuranceHelper() {
        super("globaz.naos.db.assurance.AFAssurance");
        setMethodsToLoad(IAFAssuranceHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFCotisationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFAssuranceHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFAssuranceHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFCotisationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFAssuranceHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFAssuranceHelper.METHODS_TO_LOAD);
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFAssurance[] findAssurance(Hashtable params) throws Exception {
        IAFAssurance[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFAssuranceHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject aff = (GlobazValueObject) objResult[i];
                result[i] = new IAFAssuranceHelper(aff);
            }
        }
        return result;
    }

    /**
     * Retourne le code système du canton lié à l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceCanton() {
        return (java.lang.String) _getValueObject().getProperty("assuranceCanton");
    }

    /**
     * Retourne le code système du genre de l'assurance (AVS, AF, etc) Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceGenre() {
        return (java.lang.String) _getValueObject().getProperty("assuranceGenre");
    }

    /**
     * Retourne l'id de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceId() {
        return (java.lang.String) _getValueObject().getProperty("assuranceId");
    }

    /**
     * Retourne le libellé allemand de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceLibelleAl() {
        return (java.lang.String) _getValueObject().getProperty("assuranceLibelleAl");
    }

    /**
     * Retourne le libellé allemand court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceLibelleCourtAl() {
        return (java.lang.String) _getValueObject().getProperty("assuranceLibelleCourtAl");
    }

    /**
     * Retourne le libellé français court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceLibelleCourtFr() {
        return (java.lang.String) _getValueObject().getProperty("assuranceLibelleCourtFr");
    }

    /**
     * Retourne le libellé italien court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceLibelleCourtIt() {
        return (java.lang.String) _getValueObject().getProperty("assuranceLibelleCourtIt");
    }

    /**
     * Retourne le libellé français de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceLibelleFr() {
        return (java.lang.String) _getValueObject().getProperty("assuranceLibelleFr");
    }

    /**
     * Retourne le libellé italien de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAssuranceLibelleIt() {
        return (java.lang.String) _getValueObject().getProperty("assuranceLibelleIt");
    }

    /**
     * Retourne le paramètre de l'assurance de type "typeParam" Date de création : (28.11.2007 07:18:44)
     * 
     * @return la valeur du paramètre ou vide
     */
    @Override
    public String getParametreAssuranceValeur(java.lang.String idAssurance, java.lang.String genre, String date,
            String sexe) throws Exception {
        setAssuranceId(idAssurance);
        Object res = _getObject("getParametreAssuranceValeur", new Object[] { genre, date, sexe });
        if (res == null) {
            return "";
        } else {
            return (String) res;
        }
    }

    /**
     * Retourne l'id de la rubrique comptable Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getRubriqueId() {
        return (java.lang.String) _getValueObject().getProperty("rubriqueId");
    }

    /**
     * Retourne une liste de taux Date de création : (26.02.2003 09:54:44)
     * 
     * @return
     */
    @Override
    public List getTauxList(String idAssurance, String sexe, String dateDebut, String dateFin) throws Exception {
        setAssuranceId(idAssurance);
        return (List) _getObject("getTauxList", new Object[] { sexe, dateDebut, dateFin });
    }

    /**
     * Retourne le code système du type de l'assurance (paritaire, personnel) Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTypeAssurance() {
        return (java.lang.String) _getValueObject().getProperty("typeAssurance");
    }

    /**
     * Définit le code système du canton lié à l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceCanton(java.lang.String newAssuranceCanton) {
        _getValueObject().setProperty("assuranceCanton", newAssuranceCanton);
    }

    /**
     * Définit le code système du genre de l'assurance (AVS, AF, etc) Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceGenre(java.lang.String newAssuranceGenre) {
        _getValueObject().setProperty("assuranceGenre", newAssuranceGenre);
    }

    /**
     * Définit l'id de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceId(java.lang.String newAssuranceId) {
        _getValueObject().setProperty("assuranceId", newAssuranceId);
    }

    /**
     * Définit le libellé allemand de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceLibelleAl(java.lang.String newAssuranceLibelleAl) {
        _getValueObject().setProperty("assuranceLibelleAl", newAssuranceLibelleAl);
    }

    /**
     * Définit le libellé allemand court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceLibelleCourtAl(java.lang.String newAssuranceLibelleCourtAl) {
        _getValueObject().setProperty("assuranceLibelleCourtAl", newAssuranceLibelleCourtAl);
    }

    /**
     * Définit le libellé français court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceLibelleCourtFr(java.lang.String newAssuranceLibelleCourtFr) {
        _getValueObject().setProperty("assuranceLibelleCourtFr", newAssuranceLibelleCourtFr);
    }

    /**
     * Définit le libellé italien court de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceLibelleCourtIt(java.lang.String newAssuranceLibelleCourtIt) {
        _getValueObject().setProperty("assuranceLibelleCourtIt", newAssuranceLibelleCourtIt);
    }

    /**
     * Définit le libellé français de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceLibelleFr(java.lang.String newAssuranceLibelleFr) {
        _getValueObject().setProperty("assuranceLibelleFr", newAssuranceLibelleFr);
    }

    /**
     * Définit le libellé italien de l'assurance Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setAssuranceLibelleIt(java.lang.String newAssuranceLibelleIt) {
        _getValueObject().setProperty("assuranceLibelleIt", newAssuranceLibelleIt);
    }

    /**
     * Définit l'id de la rubrique comptable Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setRubriqueId(java.lang.String newRubriqueId) {
        _getValueObject().setProperty("rubriqueId", newRubriqueId);
    }

    /**
     * Définit le code système du type de l'assurance (paritaire, personnel) Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public void setTypeAssurance(java.lang.String newTypeAssurance) {
        _getValueObject().setProperty("typeAssurance", newTypeAssurance);
    }
}
