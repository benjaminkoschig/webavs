package globaz.hera.vb.famille;

import globaz.globall.db.BSession;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFApercuRequerantManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFMembreFamilleManager;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Vector;

public class SFLiantVO {

    // Renseigné si isRequerant == true
    private String idRequerant = "";
    private boolean isRequerant = false;
    private SFMembreVO membreFamille = null;
    private String provenance = "";
    private BSession session = null;

    public SFLiantVO(BSession session) {
        this.session = session;
    }

    /**
     * Retourne la liste de tous les domaines auquel appartient le membre de famille.
     * 
     * @return
     * @throws Exception
     */
    public Vector getCsDomaines() throws Exception {

        String[] elm;
        Vector v = new Vector();

        // Si le membre de famille n'a pas d'id tiers, il
        // n'a forcément qu'un seul domaine.
        if (JadeStringUtil.isBlankOrZero(membreFamille.getIdTiers())) {
            elm = new String[2];
            elm[0] = membreFamille.getCsDomaine();
            elm[1] = getSession().getCodeLibelle(membreFamille.getCsDomaine());
            v.add(elm);
            return v;
        } else {
            // Il est nécessaire de différencier les domaines du requérant des
            // membres de famille.
            // En effet, un requérant peut avoir 2 domaines en tant que
            // requérant et 3 en tant que membre de famille.
            // Cas du membre de famille
            if (!isRequerant()) {
                SFMembreFamilleManager mgr = new SFMembreFamilleManager();
                mgr.setSession(session);
                mgr.setForIdTiers(membreFamille.getIdTiers());
                mgr.find();
                for (int i = 0; i < mgr.size(); i++) {
                    SFMembreFamille m = (SFMembreFamille) mgr.getEntity(i);

                    elm = new String[2];
                    elm[0] = m.getCsDomaineApplication();
                    elm[1] = getSession().getCodeLibelle(m.getCsDomaineApplication());
                    v.add(elm);
                }

                return v;
            }
            // Cas du requérant.
            else {
                SFApercuRequerantManager mgr = new SFApercuRequerantManager();
                mgr.setSession(getSession());
                mgr.setForIdTiers(membreFamille.getIdTiers());
                mgr.find();
                for (int i = 0; i < mgr.size(); i++) {
                    SFApercuRequerant req = (SFApercuRequerant) mgr.getEntity(i);

                    elm = new String[2];
                    elm[0] = req.getIdDomaineApplication();
                    elm[1] = getSession().getCodeLibelle(req.getIdDomaineApplication());
                    v.add(elm);
                }
                return v;
            }
        }
    }

    public String getIdRequerant() {
        return idRequerant;
    }

    public SFMembreVO getMembreFamille() {
        return membreFamille;
    }

    public String getProvenance() {
        return provenance;
    }

    public BSession getSession() {
        return session;
    }

    public boolean isRequerant() {
        return isRequerant;
    }

    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

    public void setMembreFamille(SFMembreVO membreFamille) {
        this.membreFamille = membreFamille;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public void setRequerant(boolean isRequerant) {
        this.isRequerant = isRequerant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

}
