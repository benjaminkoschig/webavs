package globaz.naos.db.ide;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFIDEUtil;

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
            numIDE = numIdeRemplacement + "<BR>(" + numIDE + ")";
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

    public String getRaisonSociale(final int index) {
        String raison = ((AFIdeAnnonce) getEntity(index)).getHistRaisonSociale();
        if (JadeStringUtil.isBlankOrZero(raison)) {
            raison = ((AFIdeAnnonce) getEntity(index)).getRaisonSociale();
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

}
