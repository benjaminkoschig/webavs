package ch.globaz.vulpecula.process.salaires;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireParTravailleur;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListSalairesAVSExcelProcess extends BProcessWithContext {
    private static final long serialVersionUID = -1292989805297661709L;
    protected String idEmployeur;
    protected Collection<String> inTypeDecompte;

    protected int annee;
    protected Annee anneeTraitement;

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(String string) {
        annee = Integer.parseInt(string);
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public Collection<String> getInTypeDecompte() {
        return inTypeDecompte;
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        anneeTraitement = new Annee(annee);
        final Employeur employeur = VulpeculaServiceLocator.getEmployeurService().findByIdAffilie(idEmployeur);

        ListSalairesAVSExcel listSalairesAVSExcel = new ListSalairesAVSExcel(getSession(),
                DocumentConstants.LISTES_SALAIRES_AVS_NAME + " " + employeur.getAffilieNumero() + " " + annee,
                DocumentConstants.LISTES_SALAIRES_AVS_DOC_NAME);
        listSalairesAVSExcel.setEmployeur(employeur);
        listSalairesAVSExcel.setTypesdecomptes(Arrays.toString(getInTypeDecompteHR().toArray()));
        listSalairesAVSExcel.setAnnee(annee);
        listSalairesAVSExcel.setSalairesAVS(retrieve());

        listSalairesAVSExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listSalairesAVSExcel.getOutputFile());
        return true;
    }

    public Map<Pair<String, Annee>, DecompteSalaireParTravailleur> retrieve() {
        Map<Pair<String, Annee>, DecompteSalaireParTravailleur> liste = new HashMap<Pair<String, Annee>, DecompteSalaireParTravailleur>();
        List<AbsenceJustifiee> absenceJustifiees = findAbsencesJustifiees(idEmployeur,
                anneeTraitement.getFirstDayOfYear(), anneeTraitement.getLastDayOfYear());
        List<CongePaye> congesPayes = findCongesPayes(idEmployeur, anneeTraitement.getFirstDayOfYear(),
                anneeTraitement.getLastDayOfYear());

        List<DecompteSalaire> listeDecomptes = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(idEmployeur,
                        anneeTraitement.getFirstDayOfYear(), anneeTraitement.getLastDayOfYear());

        // Parcourir le résultat de décomptes et les repartir par travailleur et par année

        for (DecompteSalaire decompteSalaire : listeDecomptes) {
            Pair<String, Annee> key = new Pair<String, Annee>(decompteSalaire.getIdTravailleur(),
                    decompteSalaire.getAnnee());
            if (!liste.containsKey(key)) {
                DecompteSalaireParTravailleur nouveauTravailleur = new DecompteSalaireParTravailleur();
                nouveauTravailleur.setTravailleur(decompteSalaire.getTravailleur());
                nouveauTravailleur.setAnnee(decompteSalaire.getAnnee());
                liste.put(key, nouveauTravailleur);
            }
            DecompteSalaireParTravailleur travailleur = liste.get(key);
            if (inTypeDecompte.contains(decompteSalaire.getTypeDecompte().getValue())) {
                travailleur.add(decompteSalaire);
            }
        }

        addMontantsAJ(liste, absenceJustifiees);
        addMontantsCP(liste, congesPayes);
        return sortByValue(liste);
    }

    /**
     * Ajout des absences justifiées cotisants à l'AVS ou AC dans l'objet {@link DecompteSalaireParTravailleur}
     * 
     * @param salairesParTravailleur Map contenant les salaires par travailleur
     * @param absenceJustifiees Absences justifiées à potentiellement ajoutés
     */
    private void addMontantsAJ(Map<Pair<String, Annee>, DecompteSalaireParTravailleur> salairesParTravailleur,
            List<AbsenceJustifiee> absenceJustifiees) {
        for (AbsenceJustifiee absenceJustifiee : absenceJustifiees) {
            Annee anneeAbsence = absenceJustifiee.getAnneeDateVersement();
            Travailleur travailleur = absenceJustifiee.getTravailleur();
            DecompteSalaireParTravailleur salaireParTravailleur = getOrCreateFromMap(travailleur, anneeAbsence,
                    salairesParTravailleur);
            if (absenceJustifiee.isAnnoncableAVS()) {
                salaireParTravailleur.add(absenceJustifiee);
            }
        }
    }

    /**
     * Ajout des absences justifiées cotisants à l'AVS ou AC dans l'objet {@link DecompteSalaireParTravailleur}
     * 
     * @param salairesParTravailleur Map contenant les salaires par travailleur
     * @param absenceJustifiees Absences justifiées à potentiellement ajoutés
     */
    private void addMontantsCP(Map<Pair<String, Annee>, DecompteSalaireParTravailleur> salairesParTravailleur,
            List<CongePaye> congesPayes) {
        for (CongePaye congePaye : congesPayes) {
            Annee anneeAbsence = congePaye.getAnneeDateVersement();
            Travailleur travailleur = congePaye.getTravailleur();
            DecompteSalaireParTravailleur salaireParTravailleur = getOrCreateFromMap(travailleur, anneeAbsence,
                    salairesParTravailleur);
            if (congePaye.isAnnoncableAVS()) {
                salaireParTravailleur.add(congePaye);
            }
        }
    }

    /**
     * Recherche du {@link DecompteSalaireParTravailleur} dans la map pour le travailleur et l'année passée en
     * paramètre. Si l'objet est inexistant, on le créé
     * 
     * @param travailleur Travailleur à rechercher
     * @param annee Année à rechercher
     * @param salairesParTravailleur Map contenant les salaires par travailleru
     * @return {@link DecompteSalaireParTravailleur} correspondant à la pair travailleur et année
     */
    private DecompteSalaireParTravailleur getOrCreateFromMap(Travailleur travailleur, Annee annee,
            Map<Pair<String, Annee>, DecompteSalaireParTravailleur> salairesParTravailleur) {
        Pair<String, Annee> pair = new Pair<String, Annee>(travailleur.getId(), annee);
        if (!salairesParTravailleur.containsKey(pair)) {
            createDecompteSalaireParTravailleur(travailleur, annee, salairesParTravailleur);
        }
        return salairesParTravailleur.get(pair);

    }

    /**
     * Création de l'objet {@link DecompteSalaireParTravailleur} sur la base de la pair travailleur et année
     * 
     * @param travailleur
     * @param annee
     * @param salairesParTravailleur
     */
    private void createDecompteSalaireParTravailleur(Travailleur travailleur, Annee annee,
            Map<Pair<String, Annee>, DecompteSalaireParTravailleur> salairesParTravailleur) {
        DecompteSalaireParTravailleur nouveauTravailleur = new DecompteSalaireParTravailleur();
        nouveauTravailleur.setTravailleur(travailleur);
        nouveauTravailleur.setAnnee(annee);
        salairesParTravailleur.put(new Pair<String, Annee>(travailleur.getId(), annee), nouveauTravailleur);
    }

    private List<AbsenceJustifiee> findAbsencesJustifiees(String idEmployeur, Date dateDebut, Date dateFin) {
        return VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().findByIdEmployeurForDateVersementInAnnee(
                idEmployeur, dateDebut, dateFin);
    }

    private List<CongePaye> findCongesPayes(String idEmployeur, Date dateDebut, Date dateFin) {
        return VulpeculaRepositoryLocator.getCongePayeRepository().findByIdEmployeurForDateVersementInAnnee(
                idEmployeur, dateDebut, dateFin);
    }

    /**
     * Map utility (can be moved and shared into utility class) to sort a map by values comparator
     * thanks to smart community http://stackoverflow.com/a/2581754
     * 
     * @param map
     * @return map sorted by values
     */
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_SALAIRES_AVS_TITLE");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setInTypeDecompte(Collection<String> inTypeDecompte) {
        this.inTypeDecompte = inTypeDecompte;
    }

    @Override
    protected void _executeCleanUp() {
        // DO NOTHING
    }

    /**
     * HumanReading
     */
    private Collection<String> getInTypeDecompteHR() {
        return translateCodeSysCollection("PTTYPEDECO", getInTypeDecompte());
    }

    /**
     * Human Readable list
     * 
     * @return the list human readable (codesys)
     */
    private Collection<String> translateCodeSysCollection(String idGroupe, Collection<String> inListe) {

        Collection<String> hrList = new HashSet<String>();
        for (String i : inListe) {
            hrList.add(getSession().getCode(i));
        }
        return hrList;
    }
}
