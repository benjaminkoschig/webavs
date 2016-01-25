package globaz.orion.vb.swissdec;

import globaz.orion.vb.EBAbstractViewBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.FindPucsSwissDec;

public class EBPucsValidationListViewBean extends EBAbstractViewBean {

    private List<PucsFile> listPucsFile = null;
    private String likeNumAffilie = "";
    private String contenu;

    @Override
    public void retrieve() throws Exception {

        if (listPucsFile == null) {
            listPucsFile = new ArrayList<PucsFile>();
        }
        if ("refuser".equals(getContenu())) {
            FindPucsSwissDec swissDec = new FindPucsSwissDec(getSession());
            listPucsFile = swissDec.loadPucsSwissDecRefuser();
        } else {
            FindPucsSwissDec swissDec = new FindPucsSwissDec(getSession());
            listPucsFile = swissDec.loadPucsSwissDecAValider();
        }

        sortByNumAffilie(listPucsFile);

        getSession().setAttribute("lstIdFichier", returnListIdFichierATraiter(listPucsFile));
    }

    public List<PucsFile> getListPucsFile() {

        return listPucsFile;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public void sortByNumAffilie(List<PucsFile> listToSort) {

        Collections.sort(listToSort, new Comparator<PucsFile>() {
            @Override
            public int compare(PucsFile k1, PucsFile k2) {
                return k1.getNumeroAffilie().compareTo(k2.getNumeroAffilie());
            }
        });

    }

    public void sortByNomAffilie(List<PucsFile> listToSort) {

        Collections.sort(listToSort, new Comparator<PucsFile>() {
            @Override
            public int compare(PucsFile k1, PucsFile k2) {
                return k1.getNomAffilie().compareTo(k2.getNomAffilie());
            }
        });

    }

    private List<String> returnListIdFichierATraiter(List<PucsFile> lstPucsFile) {

        List<String> lst = new ArrayList<String>();

        for (PucsFile pucs : lstPucsFile) {
            if (!pucs.isLock()) {
                lst.add(pucs.getId());
            }
        }

        return lst;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
