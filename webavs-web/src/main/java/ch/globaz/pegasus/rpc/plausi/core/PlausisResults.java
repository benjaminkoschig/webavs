package ch.globaz.pegasus.rpc.plausi.core;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PlausisResults {
    Map<PlausiResult, Boolean> results = new HashMap<PlausiResult, Boolean>();

    public void add(PlausiResult plausiResult) {
        results.put(plausiResult, plausiResult.isValide());
    }

    public void addAll(List<? extends PlausiResult> plausisResult) {
        for (PlausiResult plausiResult : plausisResult) {
            add(plausiResult);
        }
    }

    public boolean isAllPlausiOk() {
        return !results.containsValue(false);
    }

    public PlausisResults filtrePlausiKo() {
        PlausisResults plausisResults = new PlausisResults();
        for (Entry<PlausiResult, Boolean> entry : results.entrySet()) {
            if (!entry.getValue()) {
                plausisResults.results.put(entry.getKey(), entry.getValue());
            }
        }
        return plausisResults;
    }

    public String generateDeription() {
        StringWriter stringWriter = new StringWriter();
        for (Entry<PlausiResult, Boolean> entry : results.entrySet()) {
            stringWriter.append(entry.getKey().getPlausi().getID());
            stringWriter.append(" -> ");
        }
        return stringWriter.toString();
    }

    public int countInAuto() {
        return countByCategory(RpcPlausiCategory.AUTO);
    }

    public int countInBlocking() {
        return countByCategory(RpcPlausiCategory.BLOCKING);
    }

    public int countInError() {
        return countByCategory(RpcPlausiCategory.ERROR);
    }

    public int countInInactive() {
        return countByCategory(RpcPlausiCategory.INACTIVE);
    }

    public int countInInfo() {
        return countByCategory(RpcPlausiCategory.INFO);
    }

    public int countInWaning() {
        return countByCategory(RpcPlausiCategory.WARNING);
    }

    public int countByCategory(RpcPlausiCategory category) {
        int nb = 0;
        for (Entry<PlausiResult, Boolean> entry : results.entrySet()) {
            if (!entry.getValue() && category.equals(entry.getKey().getPlausi().getCategory())) {
                nb++;
            }
        }
        return nb;
    }

    public Map<PlausiResult, Boolean> getPlausis() {
        return results;
    }

    public boolean hasPlausiKo() {
        return !isAllPlausiOk();
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }
}
