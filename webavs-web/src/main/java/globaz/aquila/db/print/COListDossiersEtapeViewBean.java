package globaz.aquila.db.print;

import globaz.aquila.api.ICOSequenceConstante;
import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.batch.COEtapeManager;
import globaz.aquila.db.access.batch.COSequence;
import globaz.aquila.db.access.batch.COSequenceManager;
import globaz.aquila.process.COProcessListDossierEtape;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAVector;
import globaz.jade.json.MultiSelectHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public class COListDossiersEtapeViewBean extends COProcessListDossierEtape implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public COListDossiersEtapeViewBean() {
    }

    public String createJsonDataMultiSelectSequenceEtapes() throws Exception {

        ArrayList<MultiSelectHandler.FirstLevelKey> listOptionsMultiSelectSequenceEtapes = new ArrayList<MultiSelectHandler.FirstLevelKey>();

        COSequenceManager sequenceManager = new COSequenceManager();
        sequenceManager.setSession(getSession());
        sequenceManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < sequenceManager.size(); i++) {
            COSequence aSequence = (COSequence) sequenceManager.getEntity(i);
            MultiSelectHandler aMultiSelectHandler = new MultiSelectHandler();
            MultiSelectHandler.FirstLevelKey aFirstLevelKey = aMultiSelectHandler.new FirstLevelKey(
                    aSequence.getIdSequence(), new ArrayList<MultiSelectHandler.SousMotifs>(),
                    aSequence.getLibSequenceLibelle(), null);

            COEtapeManager etapeManager = new COEtapeManager();
            etapeManager.setSession(getSession());
            etapeManager.setForIdSequence(aSequence.getIdSequence());
            etapeManager.setOrderByLibEtapeCSOrder("true");
            etapeManager.find(BManager.SIZE_NOLIMIT);

            aFirstLevelKey.getChildrens().add(aMultiSelectHandler.new SousMotifs("", ""));

            for (int j = 0; j < etapeManager.size(); j++) {
                COEtape aEtape = (COEtape) etapeManager.getEntity(j);
                MultiSelectHandler.SousMotifs aSousMotif = aMultiSelectHandler.new SousMotifs(aEtape.getIdEtape(),
                        aEtape.getLibEtapeLibelle());
                aFirstLevelKey.getChildrens().add(aSousMotif);
            }
            listOptionsMultiSelectSequenceEtapes.add(aFirstLevelKey);
        }

        return new Gson().toJson(listOptionsMultiSelectSequenceEtapes);

    }

    public String createJsonInitValueMultiSelectSequenceEtapes() throws Exception {
        Map<String, String> mapInitValueMultiSelectSequenceEtapes = new HashMap<String, String>();

        mapInitValueMultiSelectSequenceEtapes.put("masterValue", ICOSequenceConstante.ID_SEQUENCE_AVS);

        return new Gson().toJson(mapInitValueMultiSelectSequenceEtapes);
    }

    /**
     * Return la date actuelle.
     * 
     * @return date du jour au fomat JJ.MM.AAAA
     */
    public String getFormatedDateToday() {
        return JACalendar.todayJJsMMsAAAA();
    }

    /**
     * @return une collection des séquences
     */
    public JAVector getSequences() {
        COSequenceManager seq = new COSequenceManager();
        seq.setSession(getSession());
        try {
            seq.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seq.getContainer();
    }

}
