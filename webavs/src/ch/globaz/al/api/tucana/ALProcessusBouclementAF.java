package ch.globaz.al.api.tucana;

import globaz.itucana.exception.TUModelInstanciationException;
import globaz.itucana.model.ITUModelBouclement;
import globaz.itucana.process.TUProcessusBouclementAF;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import ch.globaz.al.business.services.ALServiceLocator;

public class ALProcessusBouclementAF extends TUProcessusBouclementAF {

    /**
     * Constructeur
     * 
     * @param _annee
     * @param _mois
     * @param _eMail
     */
    public ALProcessusBouclementAF(String _annee, String _mois, String _eMail) {
        super(_annee, _mois, _eMail);
    }

    @Override
    protected void deleteBouclement(String numBouclement) throws TUModelInstanciationException {

        try {
            JadeThreadActivator.startUsingJdbcContext(this, getNewJadeThreadContext().getContext());

            ALServiceLocator.getTucanaBusinessService().deleteBouclement(numBouclement);

        } catch (Exception e) {
            throw new TUModelInstanciationException("ALProcessusBouclementAF#deleteBouclement", e);
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }

    // TODO placer cette méthode ailleurs
    private JadeThreadContext getNewJadeThreadContext() throws Exception {
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(getSession().getApplicationId());
        ctxtImpl.setLanguage(getSession().getIdLangue());
        ctxtImpl.setUserEmail(getSession().getUserEMail());
        ctxtImpl.setUserId(getSession().getUserId());
        ctxtImpl.setUserName(getSession().getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(getSession().getUserId());

        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }

        return new JadeThreadContext(ctxtImpl);
    }

    @Override
    protected void initBouclement(ITUModelBouclement bouclement) throws TUModelInstanciationException {
        try {
            JadeThreadActivator.startUsingJdbcContext(this, getNewJadeThreadContext().getContext());

            bouclement = ALServiceLocator.getTucanaBusinessService().initBouclement(bouclement, getAnnee(), getMois());

        } catch (Exception e) {
            throw new TUModelInstanciationException("ALProcessusBouclementAF#deleteBouclement", e);
        } finally {
            JadeThreadActivator.stopUsingContext(this);
        }
    }
}