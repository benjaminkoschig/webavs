package ch.globaz.vulpecula.businessimpl.services.users;

import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.users.UsersService;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * Implémentation Jade du service <code>TravailleurService</code>.
 * 
 */
public class UsersServiceImpl implements UsersService {

    /**
     * indique si un user à le droit d'imprimer un document SecureCode = 9
     * 
     * @return vrai si le user à le droit
     */
    @Override
    public boolean hasRightForPrinting(BSession session) {
        FWSecureUserDetail user = new FWSecureUserDetail();

        user.setSession(session);
        user.setUser(session.getUserId());
        user.setLabel(AFAffiliation.SECURE_CODE);

        try {
            user.retrieve();
        } catch (Exception e) {
            return false;
        }

        // si le SecureCode existe et a une valeur
        if (!user.isNew() && !JadeStringUtil.isEmpty(user.getData())) {
            String userLevelSecurity = user.getData();

            if (Integer.parseInt(userLevelSecurity) == 9) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * indique si un user à un niveau de droit (complément codeSecure) suffisant par rapport au niveau de sécurité sur
     * l'affiliation
     * 
     * @return vrai si le user à le droit >= à la sécurité de l'affiliation
     */
    @Override
    public boolean hasRightAccesSecurity(String valeurSecuriteEmployeurCS) {
        try {
            String valeurSecuriteEmployeur = BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                    valeurSecuriteEmployeurCS);
            // recherche du complément "secureCode" du user
            BSession session = BSessionUtil.getSessionFromThreadContext();
            FWSecureUserDetail user = new FWSecureUserDetail();

            user.setSession(session);
            user.setUser(session.getUserId());
            user.setLabel(AFAffiliation.SECURE_CODE);

            user.retrieve();

            int valeurEmployeur = 0;
            if (!JadeStringUtil.isEmpty(valeurSecuriteEmployeur)) {
                valeurEmployeur = Integer
                        .valueOf(valeurSecuriteEmployeur.substring(valeurSecuriteEmployeur.length() - 1));
            }

            // si le SecureCode existe et a une valeur
            if (!user.isNew() && !JadeStringUtil.isEmpty(user.getData())) {
                String userLevelSecurity = user.getData();

                if (valeurEmployeur <= Integer.parseInt(userLevelSecurity)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // si le SecureCode n'existe pas ou n'as pas de valeur => alors
                // userLevelSecurity=0
                String userLevelSecurity = "0";
                if (valeurEmployeur <= Integer.parseInt(userLevelSecurity)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasRightAccesSecurityForTravailleur(String idTravailleur) {
        List<PosteTravail> listePostes = VulpeculaRepositoryLocator.getPosteTravailRepository()
                .findByIdTravailleurWithDependencies(idTravailleur);

        for (PosteTravail posteTravail : listePostes) {
            if (!hasRightAccesSecurity(posteTravail.getEmployeur().getAccesSecurite())) {
                return false;
            }
        }
        return true;
    }

}
