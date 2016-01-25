package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;

/**
 * Classe encapsulant les monnaies étrangères (variables métiers PC), pour le calculateur.
 * Pour un type de MonnaieEtrangeres <code>csTypeMonnaieEtrangere</code> correspond une Map de clé <code>Long</code> et
 * de valeur <code>String</code>.
 * La clé de la map est le <b>timestamp du début de la période</b> de la données financière et
 * <b>la valeur est le taux pour la devise concerné</b>.
 * 
 * Cette classe est utilisé par
 * {@link ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.MonnaieEtrangereBuilder},
 * afin de mettre en cache les valeurs pour le traitement du calcul PC.
 * 
 * @author sce
 * 
 */
public class MonnaieEtrangere {

    private String csTypeMonnaieEtrangere = null;

    private Map<Long, String> monnaiesEtrangeres = new HashMap<Long, String>();

    /**
     * Retourne le code systèmne de la monnaie
     * 
     * @return le codesystème de la monnaie
     */
    public String getCsTypeMonnaieEtrangere() {
        return csTypeMonnaieEtrangere;
    }

    /**
     * Retourne la map complète de la monnaie étrangère. C.à.d tous les taux de la mpnnaie concerné pour toute les
     * périodes.
     * 
     * @return la map des taux par période
     */
    public Map<Long, String> getMonnaiesEtrangeres() {
        return monnaiesEtrangeres;
    }

    /**
     * Retourne la valeur du taux pour une période (mois, année) donnée
     * 
     * @param month le mois recherché 1-12
     * @param year l'année recherchée
     * @return le taux de la monnaie
     * @throws CalculException si une erreur dans les paramètres
     */
    public String getValeurByDate(int month, int year) throws CalculException {

        if (month == 0 || year == 0) {
            throw new CalculException(
                    "The month or the year passed to find the taux of MonnaieEtrangere cannpt be set to 0");
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);

        long timeStamp = cal.getTimeInMillis();

        return (monnaiesEtrangeres.get(Long.valueOf(timeStamp)));
    }

    /**
     * Permet de définir le code système de la monnaie étrangère
     * 
     * @param csTypeMonnaieEtrangere
     */
    public void setCsTypeMonnaieEtrangere(String csTypeMonnaieEtrangere) {
        this.csTypeMonnaieEtrangere = csTypeMonnaieEtrangere;
    }

}
