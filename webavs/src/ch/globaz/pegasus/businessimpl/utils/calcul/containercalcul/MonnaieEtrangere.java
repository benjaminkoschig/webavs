package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;

/**
 * Classe encapsulant les monnaies �trang�res (variables m�tiers PC), pour le calculateur.
 * Pour un type de MonnaieEtrangeres <code>csTypeMonnaieEtrangere</code> correspond une Map de cl� <code>Long</code> et
 * de valeur <code>String</code>.
 * La cl� de la map est le <b>timestamp du d�but de la p�riode</b> de la donn�es financi�re et
 * <b>la valeur est le taux pour la devise concern�</b>.
 * 
 * Cette classe est utilis� par
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
     * Retourne le code syst�mne de la monnaie
     * 
     * @return le codesyst�me de la monnaie
     */
    public String getCsTypeMonnaieEtrangere() {
        return csTypeMonnaieEtrangere;
    }

    /**
     * Retourne la map compl�te de la monnaie �trang�re. C.�.d tous les taux de la mpnnaie concern� pour toute les
     * p�riodes.
     * 
     * @return la map des taux par p�riode
     */
    public Map<Long, String> getMonnaiesEtrangeres() {
        return monnaiesEtrangeres;
    }

    /**
     * Retourne la valeur du taux pour une p�riode (mois, ann�e) donn�e
     * 
     * @param month le mois recherch� 1-12
     * @param year l'ann�e recherch�e
     * @return le taux de la monnaie
     * @throws CalculException si une erreur dans les param�tres
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
     * Permet de d�finir le code syst�me de la monnaie �trang�re
     * 
     * @param csTypeMonnaieEtrangere
     */
    public void setCsTypeMonnaieEtrangere(String csTypeMonnaieEtrangere) {
        this.csTypeMonnaieEtrangere = csTypeMonnaieEtrangere;
    }

}
