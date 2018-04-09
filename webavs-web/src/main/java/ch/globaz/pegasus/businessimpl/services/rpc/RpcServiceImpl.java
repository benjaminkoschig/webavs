package ch.globaz.pegasus.businessimpl.services.rpc;

import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.pegasus.business.services.rpc.RpcService;
import ch.globaz.pegasus.rpc.businessImpl.converter.RpcBusinessException;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.RpcDataLoader;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.plausi.core.PlausiContainer;
import ch.globaz.pegasus.rpc.plausi.core.PlausiResult;
import ch.globaz.pegasus.rpc.plausi.core.PlausisResults;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;

public class RpcServiceImpl implements RpcService {

    @Override
    public String loadXmlByIdAnnonce(String idAnnonce) throws ValidationException {
        RpcAnnonceGenerator annonceGenerator = new RpcAnnonceGenerator();
        try {
            return annonceGenerator.generateByIdAnnonce(idAnnonce);
        } catch (RpcBusinessException e) {
            String[] params = new String[e.getParams().size()];

            for (int i = 0; i < e.getParams().size(); i++) {
                params[i] = String.valueOf(e.getParams().get(i).toString());
            }

            JadeThread.logError(this.getClass().getName(), e.getLabelMessage(), params);
        } catch (RuntimeException e) {
            String[] params = new String[e.getStackTrace().length];
            for (int i = 0; i < e.getStackTrace().length; i++) {
                params[i] = String.valueOf(e.getStackTrace()[i].getClassName() + " : "
                        + e.getStackTrace()[i].getMethodName());
            }
            JadeThread.logError(this.getClass().getName(), e.getMessage(), params);
        } finally {
            // IOUtils.closeQuietly(annonceGenerator);
        }
        return null;
    }

    @Override
    public void testPlausiForDecision(String idDecision) {
        RpcDataLoader loader = new RpcDataLoader();
        RpcData rpcData = loader.loadByIdDecision(idDecision);
        Set<RpcPlausiCategory> inCategory = new HashSet<RpcPlausiCategory>();
        inCategory.add(RpcPlausiCategory.BLOCKING);
        inCategory.add(RpcPlausiCategory.ERROR);
        PlausisResults results = PlausiContainer.buildPlausisInCategory(rpcData, inCategory, true);
        if (!results.isAllPlausiOk()) {

            for (Entry<PlausiResult, Boolean> entry : results.filtrePlausiKo().getPlausis().entrySet()) {
                if (RpcPlausiCategory.BLOCKING.equals(entry.getKey().getPlausi().getCategory())) {
                    String msg = BSessionUtil.getSessionFromThreadContext().getLabel(
                            "JSP_PC_PREVALIDATION_DECISION_PLAUSIBLOCANTE")
                            + entry.getKey().getPlausi().getID()
                            + " "
                            + entry.getKey().getPlausi().getCategory()
                            + " "
                            + entry.getKey().getPlausi().getType();
                    JadeThread.logError(this.getClass().getName(), msg);
                } else {
                    JadeThread.logWarn(this.getClass().getName(), entry.getKey().getPlausi().getID() + " "
                            + entry.getKey().getPlausi().getCategory() + " " + entry.getKey().getPlausi().getType());
                }
            }
        }

    }
}
