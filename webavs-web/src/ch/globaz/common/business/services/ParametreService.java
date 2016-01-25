package ch.globaz.common.business.services;

import globaz.framework.util.FWCurrency;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.math.BigDecimal;
import ch.globaz.common.business.interfaces.ParametrePlageValeurInterface;

// TODO la place de ce service serait plutôt dans l'impl car c'est un service
// technique qu'on ne va pas publier

public interface ParametreService extends JadeApplicationService {

    /**
     * Retourne une plage de valeurs numériques sous forme d'un tableau de trois éléments FWCurrency.
     * 
     * @param parametrePlageValeur
     *            l'énumération représentant la clé de la plage de valeur
     * @param date
     *            la date de validité
     * @return un tableau de trois éléments FWCurrency [0] : contient la valeur de début de la plage [1] : contient la
     *         valeur de fin de la plage [2] : contient la valeur numérique attribuée à cette plage
     * @throws Exception
     *             si un problème survient durant l'accès aux données
     */
    public FWCurrency[] getPlageValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur,
            String date) throws Exception;

    public String getValeurAlpha(ParametrePlageValeurInterface parametrePlageValeur, String date, String applicationId)
            throws Exception;

    /**
     * Retourne la valeur numérique d'un paramètre sous forme de BigDecimal
     * 
     * @param parametrePlageValeur
     *            l'énumération représentant la clé de la plage de valeur
     * @param date
     *            la date de validité
     * @return la valeur numérique du paramètre sous forme de BigDecimal
     * @throws Exception
     *             si un problème survient durant l'accès aux données
     */
    public BigDecimal getValeurNumeriqueBigDecimal(ParametrePlageValeurInterface parametrePlageValeur, String date)
            throws Exception;

    /**
     * Retourne la valeur numérique d'un paramètre sous forme de FWCurrency
     * 
     * @param parametrePlageValeur
     *            l'énumération représentant la clé de la plage de valeur
     * @param date
     *            la date de validité
     * @return la valeur numérique du paramètre sous forme de FWCurrency
     * @throws Exception
     *             si un problème survient durant l'accès aux données
     */
    public FWCurrency getValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur, String date)
            throws Exception;

    /**
     * Retourne la valeur numérique d'un paramètre sous forme de FWCurrency
     * 
     * @param parametrePlageValeur
     *            l'énumération représentant la clé de la plage de valeur
     * @param date
     *            la date de validité
     * @param applicationId
     *            l'identifiant de l'application à utiliser pour récupérer le paramètre
     * @return la valeur numérique du paramètre sous forme de FWCurrency
     * @throws Exception
     *             si un problème survient durant l'accès aux données
     */
    public FWCurrency getValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur, String date,
            String applicationId) throws Exception;
}
