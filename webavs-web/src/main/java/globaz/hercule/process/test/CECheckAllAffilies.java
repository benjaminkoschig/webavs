package globaz.hercule.process.test;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEAffilie;
import globaz.hercule.db.controleEmployeur.CEAffilieManager;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author JMC
 * @since 21 sept. 2010
 */
public class CECheckAllAffilies extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        ArrayList<String> noAff = new ArrayList<String>();
        BufferedReader br = null;
        FileInputStream fi = null;
        CEAffilieManager mgr = new CEAffilieManager();
        mgr.setSansDateFinAff("true");
        mgr.setSession(getSession());
        mgr.find(BManager.SIZE_NOLIMIT);
        fi = new FileInputStream("D:/nce/in.txt");
        br = new BufferedReader(new InputStreamReader(fi));
        String line = br.readLine();
        while (line != null) {
            noAff.add(line);
            line = br.readLine();
        }
        for (int i = 0; i < mgr.size(); i++) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            CEAffilie aff = (CEAffilie) mgr.get(i);
            if (BSessionUtil.compareDateFirstGreater(getSession(), aff.getDateDebutAffiliation(), "31.12.2009")) {
                continue;
            }
            if (noAff.contains(aff.getNumAffilie())) {
                continue;
            }
            if (checkInEnvoi(aff.getNumAffilie())) {
                continue;
            }
            System.out.println(aff.getNumAffilie());
        }
        return true;
    }

    /**
     * @param noAffilie
     * @return
     * @throws Exception
     */
    public boolean checkInEnvoi(String noAffilie) throws Exception {
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_TYPE_PROVENANCE_MODULE,
                CEApplication.DEFAULT_APPLICATION_HERCULE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_NUMERO, noAffilie);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_PERIODE, "2011");

        LUJournalListViewBean viewBean = new LUJournalListViewBean();

        viewBean.setSession(getSession());
        viewBean.setProvenance(provenanceCriteres);
        viewBean.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
        viewBean.setForValeurCodeSysteme(ILEConstantes.CS_CATEGORIE_SUIVI_DS_STRUCTURE);

        viewBean.find();

        // Si le viewBean retourne un enregistrement c'est que l'envoi a déjà
        // été journalisé donc on retourne true
        if (viewBean.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        return null;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

}
