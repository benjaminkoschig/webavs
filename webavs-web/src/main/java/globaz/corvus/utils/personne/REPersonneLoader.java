package globaz.corvus.utils.personne;


import ch.globaz.common.sql.SQLWriter;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.queryexec.bridge.jade.SCM;
import ch.globaz.vulpecula.ws.utils.UtilsService;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;


import java.sql.SQLException;
import java.util.List;

public class REPersonneLoader  {

    public static String searchEtatByNss(String nss,BSession session){
        JadeThreadContext threadContext = initThreadContext(session);
        try {
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            List<String> list = SCM.newInstance(String.class).query(getSqlEtatCivil(nss)).execute();
            if(!list.isEmpty()){
                return list.get(0);
            }else{
                return "";
            }
        } catch (SQLException throwables) {
            return "";
        } finally {
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }
    private static String getSqlEtatCivil(String nss){
        SQLWriter writer = SQLWriter
                .write()
                .select()
                .fields("pers.HPTETC as EtatCivil")
                .from("SCHEMA.TIPERSP pers")
                .join("SCHEMA.TIPAVSP avs ON (avs.HTITIE = pers.HTITIE)")
                .where("avs.HXNAVS = TRIM('")
                .append(nss)
                .append("') group by  pers.HPTETC ");
        return writer.toSql();
    }
    protected static JadeThreadContext initThreadContext(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }
}
