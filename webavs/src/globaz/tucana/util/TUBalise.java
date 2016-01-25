package globaz.tucana.util;

import globaz.jade.client.util.JadeStringUtil;
import globaz.tucana.transfert.config.ITUExportConfigXmlTags;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Repr�sentation d'une balise lors de transfert
 * 
 * @author fgo
 * 
 */
public class TUBalise {
    private String dependance = null;
    private Map listAttributes = null;
    private String nom = null;

    /**
     * Constructeur
     * 
     * @param _nom
     * @param _dependance
     */
    public TUBalise(String _nom, String _dependance) {
        nom = _nom;
        dependance = _dependance;
        listAttributes = new TreeMap();
    }

    /**
     * Ajout les attributs de la balise
     * 
     * @param attrs
     */
    public void addAttributes(Map attrs) {
        listAttributes.putAll(attrs);
    }

    /**
     * R�cup�re la d�pendance
     * 
     * @return
     */
    public String getDependance() {
        return dependance;
    }

    /**
     * R�cup�re la liste des attributs de la balise
     * 
     * @return
     */
    public Map getListAttributes() {
        return listAttributes;
    }

    /**
     * R�cup�re le nom
     * 
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne vrai si une d�pendance existe
     * 
     * @return
     */
    public boolean hasDependance() {
        return !JadeStringUtil.isEmpty(getDependance());

    }

    /**
     * Retourne vrai si une cl� �trang�re existe
     * 
     * @return
     */
    public boolean hasForeignKey() {
        return !JadeStringUtil.isEmpty((String) getListAttributes().get(ITUExportConfigXmlTags.ATTRIBUTE_PK));
    }

    /**
     * Retourne vrai si une cl� primaire existe
     * 
     * @return
     */
    public boolean hasPrimaryKey() {
        return !JadeStringUtil.isEmpty((String) getListAttributes().get(ITUExportConfigXmlTags.ATTRIBUTE_PK));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("\n **********************************");
        str.append("\n nom :").append(nom);
        str.append("\n dependance :").append(dependance);
        str.append("\n attributs");
        Iterator it = listAttributes.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            str.append("\n -- cle : ").append(key);
            str.append("\n -- valeur : ").append(listAttributes.get(key));
        }
        str.append("\n **********************************");
        return str.toString();
    }

}
