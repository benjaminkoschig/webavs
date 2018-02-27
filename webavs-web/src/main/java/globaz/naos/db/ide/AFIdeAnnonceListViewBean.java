package globaz.naos.db.ide;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.List;

public class AFIdeAnnonceListViewBean extends AFIdeAnnonceManager implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 7452809907717412564L;

    public String getIdAnnonce(final int index) {
        return ((AFIdeAnnonce) getEntity(index)).getIdeAnnonceIdAnnonce();
    }

    public String getNumeroIde(final int index) {

        String numIDE = AFIDEUtil.formatNumIDE(((AFIdeAnnonce) getEntity(index)).getHistNumeroIde());
        if (JadeStringUtil.isBlankOrZero(numIDE)) {
            numIDE = AFIDEUtil.formatNumIDE(((AFIdeAnnonce) getEntity(index)).getNumeroIde());
        }

        String numIdeRemplacement = AFIDEUtil
                .formatNumIDE(((AFIdeAnnonce) getEntity(index)).getNumeroIdeRemplacement());

        if (!JadeStringUtil.isBlankOrZero(numIdeRemplacement)) {
            numIDE = numIdeRemplacement + (JadeStringUtil.isBlankOrZero(numIDE) ? "" : "<BR>(" + numIDE + ")");
        }

        return numIDE;

    }

    public String getStatutIde(final int index) {

        String libStatutIDE = getCodeLibelle(((AFIdeAnnonce) getEntity(index)).getHistStatutIde());
        if (JadeStringUtil.isBlankOrZero(libStatutIDE)) {
            libStatutIDE = getCodeLibelle(((AFIdeAnnonce) getEntity(index)).getStatutIde());
        }
        return libStatutIDE;

    }

    public String getNumeroAffilie(final int index) {

        return AFIDEUtil.giveMeAllNumeroAffilieInAnnonceSeparatedByVirgul((AFIdeAnnonce) getEntity(index));
    }

    /**
     * Since RaisonSociale must be also PrenomNom for PersonnePhysique in Tiers, this return more complicate
     * if Annonce has HistRaisonSociale is empty, check (retrieve) the linked Tiers to know if Tiers is PersonnePhysique
     * RaisonSociale is replaced by PrenomNom from Tiers if PersonnePhysique
     * 
     * @param index
     * @return histRaisonSociale > PersonnePhysique? Tiers.PrenonNom : IDEAnnonce.RaisonSociale
     */
    public String getRaisonSociale(final int index) {
        String raison = ((AFIdeAnnonce) getEntity(index)).getHistRaisonSocialeONLY();
        if (JadeStringUtil.isBlankOrZero(raison)) {
            raison = ((AFIdeAnnonce) getEntity(index)).getRaisonSociale();
            try {
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setSession(getSession());
                tiers.setIdTiers(((AFIdeAnnonce) getEntity(index)).getIdTiers());
                tiers.retrieve();
                if (tiers.getPersonnePhysique()) {
                    raison = tiers.getPrenomNom();
                }
            } catch (Exception e) {
                // unable to find tiers, RaisonSocial = affilation
            }
        }
        return raison;

    }

    public String getDateCreation(final int index) {
        return ((AFIdeAnnonce) getEntity(index)).getIdeAnnonceDateCreation();
    }

    public String getCategorie(final int index) {
        return getCodeLibelle(((AFIdeAnnonce) getEntity(index)).getIdeAnnonceCategorie());
    }

    public String getType(final int index) {
        return getCodeLibelle(((AFIdeAnnonce) getEntity(index)).getIdeAnnonceType());
    }

    public String getEtat(final int index) {
        return getCodeLibelle(((AFIdeAnnonce) getEntity(index)).getIdeAnnonceEtat());
    }

    public boolean isErreur(final int index) {
        return CodeSystem.ETAT_ANNONCE_IDE_ERREUR.equals(((AFIdeAnnonce) getEntity(index)).getIdeAnnonceEtat());
    }

    public String getDateTraitement(final int index) {
        return ((AFIdeAnnonce) getEntity(index)).getIdeAnnonceDateTraitement();
    }

    private String getCodeLibelle(String idCode) {
        try {
            return CodeSystem.getLibelle(getSession(), idCode);
        } catch (Exception e) {
            return "";
        }

    }

    public String getId(final int index) {
        return getCodeLibelle(((AFIdeAnnonce) getEntity(index)).getIdeAnnonceIdAnnonce());
    }

    public static List<AFIdeListErrorAnnonce> getListError(BSession session) throws Exception {
        AFIdeListErrorManager manager = new AFIdeListErrorManager();
        manager.setSession(session);
        manager.find(BManager.SIZE_NOLIMIT);
        List<AFIdeListErrorAnnonce> list = new ArrayList<AFIdeListErrorAnnonce>();
        for (int i = 0; i < manager.size(); i++) {
            list.add((AFIdeListErrorAnnonce) manager.getEntity(i));
        }
        return list;
    }

}
