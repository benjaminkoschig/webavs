/*
 * Créé le 1 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.data;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEnvoiDataSource implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // public static String NOM_DOC = "nomDoc";
    public static String CS_CATEGORIE = "csCategorie";
    public static String CS_ETAPE_SUIVANTE = "etapeSuivante";
    public static String CS_MANU_AUTO = "isManuAuto";
    public static String CS_TYPE_DOCUMENT = "csTypeDocument";
    public static String DATE_RAPPEL = "dateRappel";
    public static String NOM_CLASSE = "nomClasse";
    public static String PARAM_ENVOI = "paramEnvoi";

    private Hashtable source = new Hashtable();

    public void addParamEnvoi(LEParamEnvoiDataSource paramsList) {
        if (paramsList == null) {
            return;
        }
        source.put(PARAM_ENVOI, paramsList);
    }

    public String getField(String key) {
        if (!PARAM_ENVOI.equals(key)) {
            if (source.get(key) == null) {
                return null;
            } else {
                return String.valueOf(source.get(key));
            }
        } else {
            return null;
        }
    }

    public LEParamEnvoiDataSource getParamEnvoi() {
        return (LEParamEnvoiDataSource) source.get(PARAM_ENVOI);
    }

    public void put(String key, String value) {
        source.put(key, value);
    }

    public int size() {
        return source.size();
    }
}
