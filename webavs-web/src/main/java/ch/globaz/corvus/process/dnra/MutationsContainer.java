package ch.globaz.corvus.process.dnra;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutationsContainer {
    private static final Logger LOG = LoggerFactory.getLogger(MutationsContainer.class);

    private final List<Mutation> mutations = new ArrayList<Mutation>();
    private String fichierMutationName;

    public String getFichierMutationName() {
        return fichierMutationName;
    }

    public void setFichierMutationName(String fichierMutationName) {
        this.fichierMutationName = fichierMutationName;
    }

    public boolean add(Mutation mutation) {
        return mutations.add(mutation);
    }

    public List<String> extractNssActuel() {
        List<String> listNss = new ArrayList<String>();
        for (Mutation mutation : mutations) {
            if (listNss.contains(mutation.getNewNss())) {
                LOG.warn("This nss {} is allready present in the list !", mutation.getNewNss());
            } else {
                listNss.add(mutation.getNewNss());
            }
        }
        return listNss;
    }

    public int size() {
        return mutations.size();
    }

    public List<Mutation> getList() {
        return mutations;
    }
}
