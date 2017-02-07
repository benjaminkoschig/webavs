package ch.globaz.common.mail;

import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

public class UsersMail {
    /**
     * Permet de trouver les mails qui sont à attacher à un utilisateur qui lui et dans un groupe
     * 
     * @param idGroupe
     * @return List de mails
     * @throws Exception
     */
    public static List<String> resolveMailsByGroupId(String idGroupe) {

        if (JadeStringUtil.isBlankOrZero(idGroupe)) {
            throw new RuntimeException(
                    "Technical Error UsersMail.resolveMailsByGroupId : unable to find user group mail if user group is blank or zero ");
        }

        List<String> listUserMail = new ArrayList<String>();

        String[] tabUserVisa = null;
        try {
            tabUserVisa = JadeAdminServiceLocatorProvider.getLocator().getUserGroupService()
                    .findAllIdUserForIdGroup(idGroupe);
        } catch (Exception e) {
            new RuntimeException("Unalble to retrive users for this group: " + idGroupe, e);
        }

        if (tabUserVisa != null) {

            for (String userVisa : tabUserVisa) {
                JadeUser jadeUser = null;
                try {
                    jadeUser = JadeAdminServiceLocatorProvider.getLocator().getUserService().loadForVisa(userVisa);
                    String userMail = jadeUser.getEmail();
                    if (!JadeStringUtil.isBlankOrZero(userMail)) {
                        listUserMail.add(userMail);
                    }
                } catch (Exception e) {
                    new RuntimeException("Unalble to retrive user infos for this user: " + userVisa, e);
                }
            }
        }
        return listUserMail;
    }

}
