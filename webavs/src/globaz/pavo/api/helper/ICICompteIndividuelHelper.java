package globaz.pavo.api.helper;

import globaz.globall.api.BITransaction;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.pavo.api.ICICompteIndividuel;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICICompteIndividuelHelper extends GlobazHelper implements ICICompteIndividuel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String[] methods = new String[] { "getAnneeOuverture", "getCompteIndividuelId", "getDateClotureMMAA",
            "getDateCreation", "getDateNaissance", "getDernierMotifArc", "getNomPrenom", "getNoNomDernierEmployeur",
            "getNumeroAvs", "getReferenceInterne", "getRegistre", "getSexe", "getCiOuvert", "getIsAPI" };

    /**
     * Constructeur du type ICICompteIndividuelHelper
     */
    public ICICompteIndividuelHelper() {
        super("globaz.pavo.db.compte.CICompteIndividuel");
        setMethodsToLoad(methods);
    }

    /**
     * Constructeur du type ICICompteIndividuelHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICICompteIndividuelHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(methods);
    }

    /**
     * Constructeur du type ICICompteIndividuelHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICICompteIndividuelHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(methods);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.02.2003 13:50:13)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public String getAnneeOuverture() {
        return (String) _getValueObject().getProperty("anneeOuverture");
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    @Override
    public String getCompteIndividuelId() {
        return (String) _getValueObject().getProperty("compteIndividuelId");
    }

    /**
     * Retourne la date de clôture dans le format MMAA. Date de création : (23.12.2002 12:25:04)
     * 
     * @return la date de clôture dans le format MMAA.
     */
    @Override
    public String getDateClotureMMAA() {
        return (String) _getValueObject().getProperty("dateClotureMMAA");
    }

    @Override
    public String getDateCreation() {
        return (String) _getValueObject().getProperty("dateCreation");
    }

    @Override
    public String getDateNaissance() {
        return (String) _getValueObject().getProperty("dateNaissance");
    }

    @Override
    public String getDernierMotifArc() {
        return (String) _getValueObject().getProperty("dernierMotifArc");
    }

    @Override
    public String getNomPrenom() {
        return (String) _getValueObject().getProperty("nomPrenom");
    }

    /**
     * Retourne le no et le nom du dernier employeur. Date de création : (15.01.2003 09:41:26)
     * 
     * @return le no et le nom du dernier employeur
     */
    @Override
    public String getNoNomDernierEmployeur() {
        return (String) _getValueObject().getProperty("noNomDernierEmployeur");
    }

    @Override
    public String getNumeroAvs() {
        return (String) _getValueObject().getProperty("numeroAvs");
    }

    @Override
    public String getReferenceInterne() {
        return (String) _getValueObject().getProperty("referenceInterne");
    }

    @Override
    public String getRegistre() {
        return (String) _getValueObject().getProperty("registre");
    }

    @Override
    public String getSexe() {
        return (String) _getValueObject().getProperty("sexe");
    }

    /**
     * à venir...
     */
    @Override
    public boolean isCiOuvert() {
        if (_getValueObject().getProperty("ciOuvert") != null) {
            return ((Boolean) _getValueObject().getProperty("ciOuvert")).booleanValue();
        }
        return false;
    }

    /**
     * Renvoie le CI de l'assuré
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    @Override
    public void load(String noAVS, BITransaction transaction) throws java.lang.Exception {
        this._invoke("load", new Object[] { noAVS, transaction });
    }
}
