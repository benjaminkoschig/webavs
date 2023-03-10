package ch.globaz.common.business.services;

import ch.globaz.common.business.interfaces.ParametrePlageValeurInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ParametreService extends JadeApplicationService {

    /**
     * Retourne une plage de valeurs num?riques sous forme d'un tableau de trois ?l?ments FWCurrency.
     *
     * @param parametrePlageValeur
     *            l'?num?ration repr?sentant la cl? de la plage de valeur
     * @param date
     *            la date de validit?
     * @return un tableau de trois ?l?ments FWCurrency [0] : contient la valeur de d?but de la plage [1] : contient la
     *         valeur de fin de la plage [2] : contient la valeur num?rique attribu?e ? cette plage
     * @throws Exception
     *             si un probl?me survient durant l'acc?s aux donn?es
     */
     FWCurrency[] getPlageValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur, String date);

     String getValeurAlpha(ParametrePlageValeurInterface parametrePlageValeur, String date, String applicationId);

    /**
     * Retourne la valeur num?rique d'un param?tre sous forme de BigDecimal
     *
     * @param parametrePlageValeur
     *            l'?num?ration repr?sentant la cl? de la plage de valeur
     * @param date
     *            la date de validit?
     * @return la valeur num?rique du param?tre sous forme de BigDecimal
     * @throws Exception
     *             si un probl?me survient durant l'acc?s aux donn?es
     */
    BigDecimal getValeurNumeriqueBigDecimal(ParametrePlageValeurInterface parametrePlageValeur, String date);

    /**
     * Retourne la valeur num?rique d'un param?tre sous forme de FWCurrency
     *
     * @param parametrePlageValeur
     *            l'?num?ration repr?sentant la cl? de la plage de valeur
     * @param date
     *            la date de validit?
     * @return la valeur num?rique du param?tre sous forme de FWCurrency
     * @throws Exception
     *             si un probl?me survient durant l'acc?s aux donn?es
     */
    public FWCurrency getValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur, String date)
            throws Exception;

    /**
     * Retourne la valeur num?rique d'un param?tre sous forme de FWCurrency
     *
     * @param parametrePlageValeur
     *            l'?num?ration repr?sentant la cl? de la plage de valeur
     * @param date
     *            la date de validit?
     * @param applicationId
     *            l'identifiant de l'application ? utiliser pour r?cup?rer le param?tre
     * @return la valeur num?rique du param?tre sous forme de FWCurrency
     * @throws Exception
     *             si un probl?me survient durant l'acc?s aux donn?es
     */
    public FWCurrency getValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur, String date,
            String applicationId) throws Exception;

    Integer getInteger(ParametrePlageValeurInterface parametrePlageValeur, String date, BSession session);

    LocalDate getDateDebutValidite(ParametrePlageValeurInterface parametrePlageValeur, String date, BSession session);

    LocalDate getValeurDate(ParametrePlageValeurInterface parametrePlageValeur, String date, BSession session);
}
