package globaz.corvus.api.external.arc.rassemblement;

/**
 * Interface commune des VO pour le transfer des resultats des ARC
 * 
 * @author SCR
 * 
 */
public interface IArcVO {

    /**
     * Ajoute une paire cle-valeur
     * 
     * @param key
     * @param value
     */
    public void add(String key, String value);

    /**
     * Vrais si la cle a deja une valeur
     * 
     * @param key
     * @return
     */
    public boolean containsKey(String key);

    /**
     * Donne la valeur correspondant a la cle
     * 
     * @param key
     * @return
     */
    public String get(String key);
}
