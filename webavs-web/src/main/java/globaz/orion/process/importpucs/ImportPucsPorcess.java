package globaz.orion.process.importpucs;

import globaz.globall.db.BSessionUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.process.byitem.ProcessItemsHandlerJadeJob;
import ch.globaz.orion.business.models.pucs.PucsFile;

public abstract class ImportPucsPorcess extends ProcessItemsHandlerJadeJob<PucsItem> {

    private static final long serialVersionUID = 1L;
    protected transient List<PucsFile> pucsFiles;
    private Map<String, List<AFAffiliation>> affiliations;

    public abstract List<PucsFile> loadPucs() throws FileNotFoundException;

    @Override
    public final void before() {
        try {
            BSessionUtil.initContext(getSession(), this);
            pucsFiles = loadPucs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        affiliations = findAffiliations(pucsFiles);
    }

    @Override
    public List<PucsItem> resolveItems() {
        List<PucsItem> list = new ArrayList<PucsItem>();
        for (PucsFile pucsFile : pucsFiles) {
            list.add(new PucsItem(pucsFile, affiliations.get(pucsFile.getNumeroAffilie()), getSession(), getJobInfos()
                    .getIdJob()));
        }
        return list;
    }

    @Override
    public void after() {
        sendMailIfHasError();
        BSessionUtil.stopUsingContext(this);
    }

    private Map<String, List<AFAffiliation>> findAffiliations(List<PucsFile> list) {
        Set<String> numAffiliations = new HashSet<String>();

        for (PucsFile pucsFile : list) {
            numAffiliations.add(pucsFile.getNumeroAffilie());
        }

        List<AFAffiliation> listAffiliations = AFAffiliationServices.searchAffiliationByNumeros(numAffiliations,
                getSession());

        Map<String, List<AFAffiliation>> map = new HashMap<String, List<AFAffiliation>>();

        for (AFAffiliation afAffiliation : listAffiliations) {
            if (!map.containsKey(afAffiliation.getAffilieNumero())) {
                map.put(afAffiliation.getAffilieNumero(), new ArrayList<AFAffiliation>());
            }
            map.get(afAffiliation.getAffilieNumero()).add(afAffiliation);
        }
        return map;
    }

}