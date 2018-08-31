package ch.globaz.vulpecula.domain.models.servicemilitaire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

public class ServicesMilitaires implements Iterable<ServiceMilitaire> {
    private final Collection<ServiceMilitaire> servicesMilitaires;

    public ServicesMilitaires(Collection<ServiceMilitaire> servicesMilitaires) {
        this.servicesMilitaires = servicesMilitaires;
    }

    @Override
    public Iterator<ServiceMilitaire> iterator() {
        return servicesMilitaires.iterator();
    }

    public double getSommeNbjours() {
        double nbJours = 0;
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            nbJours = nbJours + serviceMilitaire.getNbJours();
        }
        return nbJours;
    }

    public Montant getSommeCouvertureAPG() {
        Montant somme = Montant.ZERO;
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            somme = somme.add(serviceMilitaire.getMontantCouvertureAPG());
        }
        return somme;
    }

    public Montant getSommeVersementAPG() {
        Montant somme = Montant.ZERO;
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            somme = somme.add(serviceMilitaire.getVersementAPG());
        }
        return somme;
    }

    public Montant getSommeBruts() {
        Montant somme = Montant.ZERO;
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            somme = somme.add(serviceMilitaire.getMontantBrut());
        }
        return somme;
    }

    public Montant getSommeMontantAVS_AC() {
        Montant somme = Montant.ZERO;
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            somme = somme.add(serviceMilitaire.getMontantAVS_AC());
        }
        return somme;
    }

    public Montant getSommeMontantAF() {
        Montant somme = Montant.ZERO;
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            somme = somme.add(serviceMilitaire.getMontantAF());
        }
        return somme;
    }

    public Montant getSommeTotalVerse() {
        Montant somme = Montant.ZERO;
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            somme = somme.add(serviceMilitaire.getMontantAVerser());
        }
        return somme;
    }

    public static Map<Convention, SMsParType> groupByGenreSMByConvention(
            Collection<ServiceMilitaire> servicesMilitaires) {
        Map<Convention, SMsParType> smsParType = new TreeMap<Convention, SMsParType>();

        for (Map.Entry<Convention, Collection<ServiceMilitaire>> entry : groupByConvention(servicesMilitaires)
                .entrySet()) {
            Convention convention = entry.getKey();
            Collection<ServiceMilitaire> sms = entry.getValue();

            SMsParType smParType = new SMsParType(groupByGenreSM(sms));
            smsParType.put(convention, smParType);
        }

        return smsParType;
    }

    public static Map<GenreSM, ServicesMilitaires> groupByGenreSM(Collection<ServiceMilitaire> servicesMilitaires) {
        Map<GenreSM, ServicesMilitaires> smsParType = new HashMap<GenreSM, ServicesMilitaires>();

        TreeMultimap<GenreSM, ServiceMilitaire> sms = TreeMultimap.create();
        for (ServiceMilitaire sm : servicesMilitaires) {
            sms.put(sm.getGenre(), sm);
        }

        for (Map.Entry<GenreSM, Collection<ServiceMilitaire>> entry : sms.asMap().entrySet()) {
            ServicesMilitaires servicesMilitaires2 = new ServicesMilitaires(entry.getValue());
            smsParType.put(entry.getKey(), servicesMilitaires2);
        }

        return smsParType;
    }

    public static Map<Convention, Collection<ServiceMilitaire>> groupByConvention(
            Collection<ServiceMilitaire> servicesMilitaires) {
        TreeMultimap<Convention, ServiceMilitaire> abs = TreeMultimap.create();
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            abs.put(serviceMilitaire.getConventionEmployeur(), serviceMilitaire);
        }
        return abs.asMap();
    }

    public static List<SMParEmployeur> groupByEmployeur(Collection<ServiceMilitaire> servicesMilitaires) {
        List<SMParEmployeur> list = new ArrayList<SMParEmployeur>();
        Map<Employeur, Collection<ServiceMilitaire>> map = Multimaps
                .index(servicesMilitaires, new Function<ServiceMilitaire, Employeur>() {
                    @Override
                    public Employeur apply(ServiceMilitaire serviceMilitaire) {
                        return serviceMilitaire.getEmployeur();
                    }
                }).asMap();
        for (Map.Entry<Employeur, Collection<ServiceMilitaire>> entry : map.entrySet()) {
            ServicesMilitaires cps = new ServicesMilitaires(entry.getValue());
            list.add(new SMParEmployeur(entry.getKey(), cps));
        }
        return list;
    }

    public int size() {
        return servicesMilitaires.size();
    }

    public boolean add(ServiceMilitaire serviceMilitaire) {
        return servicesMilitaires.add(serviceMilitaire);
    }
}
