package ch.globaz.corvus.process.dnra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.pyxis.loader.PaysLoader;

class MutationsContainer {

    private static final Logger LOG = LoggerFactory.getLogger(MutationsContainer.class);
    private final List<Mutation> mutations = new ArrayList<Mutation>();
    private final List<Mutation> mutationsInactive = new ArrayList<Mutation>();
    private final List<Mutation> mutationsInvalide = new ArrayList<Mutation>();

    private String fichierMutationName;
    private final PaysLoader loader;

    public MutationsContainer(PaysLoader loader) {
        this.loader = loader;
    }

    public void setFichierMutationName(String fichierMutationName) {
        this.fichierMutationName = fichierMutationName;
    }

    public String getFichierMutationName() {
        return fichierMutationName;
    }

    public void add(Mutation mutation) {
        mutation.setPays(loader.resolveByCodeCentrale(mutation.getCodeNationalite()));
        if (mutation.isValide()) {
            mutations.add(mutation);
        }
        if (mutation.getTypeMutation().isInactive()) {
            mutationsInactive.add(mutation);
        }
        if (mutation.getTypeMutation().isInvalide()) {
            mutationsInvalide.add(mutation);
        }
    }

    public Set<String> extractNssInvalide() {
        Set<String> listNss = new HashSet<String>();
        for (Mutation mutation : mutationsInvalide) {
            listNss.add(mutation.getNss());
        }
        return listNss;
    }

    public Set<String> extractNssActifEtInactif() {
        Set<String> listNss = new HashSet<String>();
        for (Mutation mutation : mutations) {
            if (listNss.contains(mutation.getNewNss())) {
                LOG.warn("This nss {} is allready present in the list !", mutation.getNewNss());
            } else {
                listNss.add(mutation.getNewNss());
            }
        }
        for (Mutation mutation : mutationsInactive) {
            listNss.add(mutation.getNss());
        }

        return listNss;
    }

    public int size() {
        return mutations.size();
    }

    public List<Mutation> getList() {
        Map<String, Mutation> map = new HashMap<String, Mutation>();
        for (Mutation mutation : mutationsInactive) {
            map.put(mutation.getNewNss(), mutation);
        }
        for (Mutation mutation : mutations) {
            if (map.containsKey(mutation.getNewNss())) {
                Mutation inactive = map.get(mutation.getNewNss());
                mutation.setNssInactive(inactive.getNss());
            }
        }
        return mutations;
    }
}
