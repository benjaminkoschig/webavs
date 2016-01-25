package globaz.naos.util;

import globaz.docinfo.ITIRoleSpecifier;
import globaz.globall.db.BSession;
import globaz.jade.common.JadeCodingUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.translation.CodeSystem;
import java.util.HashSet;
import java.util.Set;

public class AFRoleParInd implements ITIRoleSpecifier {

    @Override
    public String specify(String role, String numAff, BSession session) {
        if (CodeSystem.ROLE_AFFILIE.equals(role)) {
            try {
                AFAffiliationManager mgr = new AFAffiliationManager();
                mgr.setSession(session);
                mgr.setForAffilieNumero(numAff);
                mgr.find();
                Set set = new HashSet();
                for (int i = 0; i < mgr.size(); i++) {
                    AFAffiliation af = (AFAffiliation) mgr.getEntity(i);
                    set.add(af.getTypeAffiliation());
                }
                /*
                 * Je determine un type précis seulement si je suis sûr (toujour le même type...)
                 */
                if (set.size() == 1) {
                    role = AFAffiliationUtil.getRoleParInd((AFAffiliation) mgr.getFirstEntity()); // tj
                    // le
                    // même
                    // type...
                }
            } catch (Exception e) {
                JadeCodingUtil.catchException(this, "specify", e);
            }

        }
        return role;
    }

}