package ch.globaz.common.businessimpl.services;

import ch.globaz.common.business.interfaces.ParametrePlageValeurInterface;
import ch.globaz.common.business.services.ParametreService;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.util.Dates;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.jade.client.util.JadeDateUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
public class ParametreServiceImpl implements ParametreService {

    private FWFindParameter getParameter(ParametrePlageValeurInterface parametrePlageValeur, String date) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return this.getParameter(parametrePlageValeur, date, session, session.getApplicationId());
    }

    private FWFindParameter getParameter(ParametrePlageValeurInterface parametrePlageValeur,
                                         String date,
                                         BSession session, String applicationId)  {

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new CommonTechnicalException("Date parameter is mandatory or value " + date + " is not a valid date");
        }

        FWFindParameterManager param = new FWFindParameterManager() {

            private static final long serialVersionUID = 1L;

            @Override
            protected String _getOrder(BStatement statement) {
                return "PPADDE DESC";
            }

            @Override
            protected String _getWhere(BStatement statement) {
                String sqlWhere = "PPARAP=" + this._dbWriteString(statement.getTransaction(), getIdApplParametre())
                        + " AND PPACDI=" + this._dbWriteString(statement.getTransaction(), getIdCleDiffere());
                // Date
                sqlWhere = sqlWhere + " AND PPADDE<="
                        + this._dbWriteDateAMJ(statement.getTransaction(), getDateDebutValidite());

                // sqlWhere =
                return sqlWhere;
            }
        };
        param.setSession(session);
        param.setIdApplParametre(applicationId);
        param.setIdCleDiffere(parametrePlageValeur.getCle());
        param.setDateDebutValidite(date);
        Exceptions.checkedToUnChecked(()->param.find(1));

        if (param.getSize() == 0) {
            throw new CommonTechnicalException("Aucun param?tre d?fini pour la cl? " + parametrePlageValeur + " ? la date " + date);
        }

        return (FWFindParameter) param.getFirstEntity();
    }

    private FWFindParameter getParameter(ParametrePlageValeurInterface parametrePlageValeur, String date,
            String applicationId) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return this.getParameter(parametrePlageValeur, date, session, applicationId);
    }

    @Override
    public FWCurrency[] getPlageValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur,
            String date)  {
        FWFindParameter parametre = this.getParameter(parametrePlageValeur, date);

        FWCurrency[] result = new FWCurrency[3];
        result[0] = new FWCurrency(parametre.getPlageValDeParametre());
        result[1] = new FWCurrency(parametre.getPlageValFinParametre());
        result[2] = new FWCurrency(parametre.getValeurNumParametre());

        return result;

    }

    @Override
    public String getValeurAlpha(ParametrePlageValeurInterface parametrePlageValeur, String date, String applicationId) {
        FWFindParameter parametre = this.getParameter(parametrePlageValeur, date, applicationId);
        return parametre.getValeurAlphaParametre();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getValeurNumeriqueBigDecimal(ParametrePlageValeurInterface parametrePlageValeur, String date) {
        FWFindParameter parametre = this.getParameter(parametrePlageValeur, date);
        return new BigDecimal(parametre.getValeurNumParametre());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getInteger(final ParametrePlageValeurInterface parametrePlageValeur, final String date, final BSession session) {
        try {
            FWFindParameter parameter = this.getParameter(parametrePlageValeur, date, session, session.getApplicationId());
            return new BigDecimal(parameter.getValeurNumParametre()).intValue();
        } catch (Exception e) {
            LOG.error("Error for this parameter: " + parametrePlageValeur.getCle(), e);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FWCurrency getValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur, String date) {
        FWFindParameter parametre = this.getParameter(parametrePlageValeur, date);
        return new FWCurrency(parametre.getValeurNumParametre());
    }

    @Override
    public FWCurrency getValeurNumeriqueFWCurrency(ParametrePlageValeurInterface parametrePlageValeur, String date,
            String applicationId) throws Exception {
        FWFindParameter parametre = this.getParameter(parametrePlageValeur, date, applicationId);
        return new FWCurrency(parametre.getValeurNumParametre());
    }

    @Override
    public LocalDate getValeurDate(final ParametrePlageValeurInterface parametrePlageValeur, final String date, final BSession session) {
        FWFindParameter parameter = this.getParameter(parametrePlageValeur, date, session, session.getApplicationId());
        return Dates.toDate(parameter.getValeurNumParametre());
    }

    @Override
    public LocalDate getDateDebutValidite(final ParametrePlageValeurInterface parametrePlageValeur, final String date, final BSession session) {
        FWFindParameter parameter = this.getParameter(parametrePlageValeur, date, session, session.getApplicationId());
        return Dates.toDateFromDb(parameter.getDateDebutValidite());
    }
}
