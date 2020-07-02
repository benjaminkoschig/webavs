package ch.globaz.pegasus.rpc.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.PcaSummer;
import ch.globaz.pegasus.rpc.plausi.core.PlausiResult;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiSummary;

class AnnonceItems {
    private List<AnnonceItem> annonces = new ArrayList<AnnonceItem>();
    private final PcaSummer summer = new PcaSummer();
    private int nbDecision = 0;

    public AnnonceItems(List<AnnonceItem> annonces) {
        this.annonces = new ArrayList<>();
        for(AnnonceItem annonceItem : annonces) {
            if(annonceItem.getAnnonce().getDecisions() != null) {
                this.annonces.add(annonceItem);
            }
        }
        for (AnnonceItem annonceItem : this.annonces) {
            nbDecision = nbDecision + annonceItem.getAnnonce().getDecisions().size();
            if (annonceItem.getRpcData() != null && !annonceItem.getAnnonce().getEtat().isError()) {
                summer.sum(annonceItem.getRpcData());
            }
        }
    }

    public Set<AnnonceItem> filtreAnnonceWithPlausiKo() {
        Set<AnnonceItem> list = new HashSet<AnnonceItem>();
        for (AnnonceItem annonceItem : annonces) {
            if (!annonceItem.getPlausisResults().isAllPlausiOk()) {
                list.add(annonceItem);
            }
        }
        return list;
    }

    public int countNbAnnonceKo() {
        int nb = 0;
        for (AnnonceItem annonceItem : annonces) {
            if (!annonceItem.getPlausisResults().isAllPlausiOk()) {
                nb++;
            }
        }
        return nb;
    }

    public int countAnnonce201() {
        int nb = 0;
        for (AnnonceItem annonceItem : annonces) {
            if (annonceItem.getAnnonce().getType().isPartiel()) {
                nb++;
            }
        }
        return nb;
    }

    public int countAnnonce301() {
        int nb = 0;
        for (AnnonceItem annonceItem : annonces) {
            if (annonceItem.getAnnonce().getType().isAnnulation()) {
                nb++;
            }
        }
        return nb;
    }

    public int countAnnonce101() {
        int nb = 0;
        for (AnnonceItem annonceItem : annonces) {
            if (annonceItem.getAnnonce().getType().isComplet()) {
                nb++;
            }
        }
        return nb;
    }

    public List<RpcPlausiResutForXls> generateAnnoncesForDisplay() {
        List<RpcPlausiResutForXls> listPlausis = new ArrayList<RpcPlausiResutForXls>();
        for (AnnonceItem annonceItem : annonces) {
            if (!annonceItem.getPlausisResults().isAllPlausiOk()) {
                for (Entry<PlausiResult, Boolean> entry : annonceItem.getPlausisResults().filtrePlausiKo().getPlausis()
                        .entrySet()) {
                    listPlausis.add(new RpcPlausiResutForXls(entry.getKey(), annonceItem.getDescription()));
                }
            }
        }
        return listPlausis;
    }

    public RpcPlausiSummary generatePlausiSummary() {
        RpcPlausiSummary summary = new RpcPlausiSummary();
        summary.setNbAnnonceKO(countNbAnnonceKo());
        summary.setNbPlausiAuto(countByCategory(RpcPlausiCategory.AUTO));
        summary.setNbPlausiBlocking(countByCategory(RpcPlausiCategory.BLOCKING));
        summary.setNbPlausiError(countByCategory(RpcPlausiCategory.ERROR));
        summary.setNbPlausiWarning(countByCategory(RpcPlausiCategory.WARNING));
        summary.setNbPlausiInfos(countByCategory(RpcPlausiCategory.INFO));
        summary.setNbPlausiManuel(countByCategory(RpcPlausiCategory.MANUAL));
        return summary;
    }

    private int countByCategory(RpcPlausiCategory category) {
        int nb = 0;
        for (AnnonceItem annonceItem : annonces) {
            nb = nb + annonceItem.getPlausisResults().countByCategory(category);
        }
        return nb;
    }

    public PcaSummer getSummer() {
        return summer;
    }

    public int getNbDecision() {
        return nbDecision;
    }

    public int getNbAnnonce() {
        return this.annonces.size();
    }

    public List<AnnonceItem> getAnnonces() {
        return annonces;
    }
}
