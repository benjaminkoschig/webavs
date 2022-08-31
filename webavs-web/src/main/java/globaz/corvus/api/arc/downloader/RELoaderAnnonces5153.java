package globaz.corvus.api.arc.downloader;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.external.arc.REDownloaderException;
import globaz.corvus.db.annonces.*;
import globaz.corvus.process.REComparaisonCentraleProcess;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RELoaderAnnonces5153 extends REReaderAnnonces51_53  {

    public static final String CODE_SPECIAL = "60";
    public static final String CODE_ENREGISTREMENT_01 = "01";
    public static final String CODE_ENREGISTREMENT_02 = "02";
    public static final String CODE_ENREGISTREMENT_03 = "03";

    public Map<REComparaisonCentraleProcess.KeyRAAnnComparaison, ArrayList<Object>> loadAnnonces(String dateMoisAdaptation) throws Exception {
        loadAnnonces51(dateMoisAdaptation);
        loadAnnonces53(dateMoisAdaptation);

        return annoncesCrees;
    }

    private void loadAnnonces51(String dateMoisAdaptation) throws Exception {


        REAnnonce51LevelManager mgr = new REAnnonce51LevelManager();
        mgr.setSession(getSession());
        mgr.setForMoisAdaptation(dateMoisAdaptation);
        mgr.find(BManager.SIZE_NOLIMIT);

        Map<String, REAnnonce51> map1 = reduceMapCodeEnregistrement(mgr, CODE_ENREGISTREMENT_01);
        Map<String, REAnnonce51> map2 = reduceMapCodeEnregistrement(mgr, CODE_ENREGISTREMENT_02);
        Map<String, REAnnonce51> map3 = reduceMapCodeEnregistrement(mgr, CODE_ENREGISTREMENT_03);

        if (map1.size() != map2.size() || map1.size() != map3.size()) {
            throw new REDownloaderException("Erreur lors de la récupération des enregistrements. " +
                    "Nombre d'enregistrement de sous niveaux différents entre les tables");
        }
        map1.values().stream().forEach(a -> genereMap51(a, map2, map3));
    }

    private void loadAnnonces53(String dateMoisAdaptation) throws Exception {

        REAnnonce53LevelManager mgr = new REAnnonce53LevelManager();
        mgr.setSession(getSession());
        mgr.setForMoisAdaptation(dateMoisAdaptation);
        mgr.find(BManager.SIZE_NOLIMIT);

        Map<String, REAnnonce53> map1 = reduceMapCodeEnregistrement(mgr, CODE_ENREGISTREMENT_01);
        Map<String, REAnnonce53> map2 = reduceMapCodeEnregistrement(mgr, CODE_ENREGISTREMENT_02);
        Map<String, REAnnonce53> map3 = reduceMapCodeEnregistrement(mgr, CODE_ENREGISTREMENT_03);

        if (map1.size() != map2.size() || map1.size() != map3.size()) {
            throw new REDownloaderException("Erreur lors de la récupération des enregistrements. " +
                    "Nombre d'enregistrement de sous niveaux différents entre les tables");
        }

        map1.values().stream().forEach(a -> genereMap53(a, map2, map3));
    }

    private static Map<String, REAnnonce51> reduceMapCodeEnregistrement(REAnnonce51LevelManager mgr, String codeEnregistrement) {
        return mgr.<REAnnonce51>getContainerAsList().stream()
                .filter(a -> codeEnregistrement.equals(a.getCodeEnregistrement01()))
                .collect(Collectors.toMap(REAnnonce51::getId, Function.identity()));
    }

    private static Map<String, REAnnonce53> reduceMapCodeEnregistrement(REAnnonce53LevelManager mgr, String codeEnregistrement) {
        return mgr.<REAnnonce53>getContainerAsList().stream()
                .filter(a -> codeEnregistrement.equals(a.getCodeEnregistrement01()))
                .collect(Collectors.toMap(REAnnonce53::getId, Function.identity()));
    }

    private void genereMap51(REAnnonce51 annonce1, Map<String, REAnnonce51> map2, Map<String, REAnnonce51> map3) {
        List<REAnnonce51> list3Annonces = new ArrayList<>();
        list3Annonces.add(annonce1);
        REAnnonce51 annonce2 = map2.get(annonce1.getIdLienAnnonce());
        list3Annonces.add(annonce2);
        list3Annonces.add(map3.get(annonce2.getIdLienAnnonce()));

        REComparaisonCentraleProcess proc = new REComparaisonCentraleProcess();
        REComparaisonCentraleProcess.KeyRAAnnComparaison key;
        key = getKeyRAAnnComparaison(annonce1, proc);

        annoncesCrees.put(key, (ArrayList<Object>)list3Annonces.stream().map(Object.class::cast).collect(Collectors.toList()));
    }

    private void genereMap53(REAnnonce53 annonce1, Map<String, REAnnonce53> map2, Map<String, REAnnonce53> map3) {
        List<REAnnonce53> list3Annonces = new ArrayList<>();
        list3Annonces.add(annonce1);
        REAnnonce53 annonce2 = map2.get(annonce1.getIdLienAnnonce());
        list3Annonces.add(annonce2);
        list3Annonces.add(map3.get(annonce2.getIdLienAnnonce()));

        REComparaisonCentraleProcess proc = new REComparaisonCentraleProcess();
        REComparaisonCentraleProcess.KeyRAAnnComparaison key;
        key = getKeyRAAnnComparaison(annonce1, proc);

        annoncesCrees.put(key, (ArrayList<Object>)list3Annonces.stream().map(Object.class::cast).collect(Collectors.toList()));
    }

    private REComparaisonCentraleProcess.KeyRAAnnComparaison getKeyRAAnnComparaison(REAnnonce51 annonce51, REComparaisonCentraleProcess proc) {
        if (containsCodeSpecial(annonce51, CODE_SPECIAL)) {
            return proc.new KeyRAAnnComparaison(annonce51.getGenrePrestation(),
                    NSUtil.formatAVSUnknown(annonce51.getNoAssAyantDroit()), (new FWCurrency(
                    annonce51.getMensualitePrestationsFrancs())).toString(), NSUtil.formatAVSUnknown(annonce51.getPremierNoAssComplementaire()));
        }
        return proc.new KeyRAAnnComparaison(annonce51.getGenrePrestation(),
                NSUtil.formatAVSUnknown(annonce51.getNoAssAyantDroit()), (new FWCurrency(
                annonce51.getMensualitePrestationsFrancs())).toString(), "");
    }

    private REComparaisonCentraleProcess.KeyRAAnnComparaison getKeyRAAnnComparaison(REAnnonce53 annonce53, REComparaisonCentraleProcess proc) {
        if (containsCodeSpecial(annonce53, CODE_SPECIAL)) {
            return proc.new KeyRAAnnComparaison(annonce53.getGenrePrestation(),
                    NSUtil.formatAVSUnknown(annonce53.getNoAssAyantDroit()), (new FWCurrency(
                    annonce53.getMensualitePrestationsFrancs())).toString(), NSUtil.formatAVSUnknown(annonce53.getPremierNoAssComplementaire()));
        }
        return proc.new KeyRAAnnComparaison(annonce53.getGenrePrestation(),
                    NSUtil.formatAVSUnknown(annonce53.getNoAssAyantDroit()), (new FWCurrency(
                annonce53.getMensualitePrestationsFrancs())).toString(), "");

    }

    @Override
    public BSession getSession() {
        return (BSession) super.getSession();
    }

}
