package globaz.naos.services;

import globaz.globall.db.BSession;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;

public final class AFAssuranceServices {

    private AFAssuranceServices() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return la première assurance utilisant le type
     * 
     * @param session
     * @param typeAssurance
     * @return
     * @throws Exception
     */
    public static AFAssurance findFirstAssuranceSelonType(BSession session, String typeAssurance) throws Exception {
        AFAssuranceManager assMng = new AFAssuranceManager();
        assMng.setSession(session);
        assMng.setForTypeAssurance(typeAssurance);
        assMng.find();
        if (assMng.size() > 0) {
            return ((AFAssurance) assMng.getFirstEntity());
        }
        return null;
    }

}
