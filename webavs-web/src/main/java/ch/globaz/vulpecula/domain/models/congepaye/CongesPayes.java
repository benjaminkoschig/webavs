package ch.globaz.vulpecula.domain.models.congepaye;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.Convention;

/**
 * Représente une collection de congés payés.
 * Cette classe ajoute un ensemble de méthode de calculs pour les congés payés.
 *
 */
public class CongesPayes implements Iterable<CongePaye> {
    private Collection<CongePaye> congesPayes;

    public CongesPayes(Collection<CongePaye> congesPayes) {
        this.congesPayes = congesPayes;
    }

    public Collection<CongePaye> getCongesPayes() {
        return congesPayes;
    }

    /**
     * Retourne la somme des salaires.
     *
     * @return
     */
    public Montant getSalaires() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getSalaires());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants bruts.
     *
     * @return
     */
    public Montant getSommeBruts() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantBrut());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants associés aux assurances de type Retaval.
     *
     * @return
     */
    public Montant getSommeRetaval() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantRetaval());
        }
        return montant;
    }

    /**
     * Retourne la sommes des montants associés aux assurances de type Maladie (FCFP)
     *
     * @return
     */
    public Montant getSommeMal() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantMal());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants associés aux assurances de type LPP.
     *
     * @return
     */
    public Montant getSommeLPP() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantLPP());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants associés aux assurances de type AVS.
     *
     * @return
     */
    public Montant getSommeAVS() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantAVS());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants associés aux assurance de type AC
     *
     * @return
     */
    public Montant getSommeAC() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantAC());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants associés aux assurance de type AF.
     *
     * @return
     */
    public Montant getSommeAF() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantAF());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants associés aux assurance de type FCFP.
     *
     * @return
     */
    public Montant getSommeFCFP() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantFCFP());
        }
        return montant;
    }

    /**
     * Retourne la somme des montants nets.
     *
     * @return
     */
    public Montant getSommeNets() {
        Montant montant = Montant.ZERO;
        for (CongePaye congePaye : congesPayes) {
            montant = montant.add(congePaye.getMontantNet());
        }
        return montant;
    }

    /**
     * Création d'une map Convention-CongePaye à partir d'une liste de congés payés.
     *
     * @param congesPayes Liste de congés payés
     * @return Map de Convention-CongePaye
     */
    public static Map<Convention, CongesPayes> groupByConvention(Collection<CongePaye> congesPayes) {
        Map<Convention, CongesPayes> mapConventionCongesPayes = new TreeMap<Convention, CongesPayes>();
        TreeMultimap<Convention, CongePaye> abs = TreeMultimap.create();
        for (CongePaye congePaye : congesPayes) {
            abs.put(congePaye.getConventionEmployeur(), congePaye);
        }

        for (Map.Entry<Convention, Collection<CongePaye>> conventionCongesPayes : abs.asMap().entrySet()) {
            Convention convention = conventionCongesPayes.getKey();
            CongesPayes congesPayess = new CongesPayes(conventionCongesPayes.getValue());
            mapConventionCongesPayes.put(convention, congesPayess);
        }
        return mapConventionCongesPayes;
    }

    public static List<CPParEmployeur> groupByEmployeur(Collection<CongePaye> congesPayes) {
        List<CPParEmployeur> list = new ArrayList<CPParEmployeur>();
        Map<Employeur, Collection<CongePaye>> map = Multimaps.index(congesPayes, new Function<CongePaye, Employeur>() {
            @Override
            public Employeur apply(CongePaye congePaye) {
                return congePaye.getEmployeur();
            }
        }).asMap();
        for (Map.Entry<Employeur, Collection<CongePaye>> entry : map.entrySet()) {
            CongesPayes cps = new CongesPayes(entry.getValue());
            list.add(new CPParEmployeur(entry.getKey(), cps));
        }
        return list;
    }

    @Override
    public Iterator<CongePaye> iterator() {
        return congesPayes.iterator();
    }

    public int size() {
        return congesPayes.size();
    }
}
