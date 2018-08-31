package ch.globaz.vulpecula.process.revision;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.comparators.DecompteSalairePourRevisionComparator;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.is.TauxImpositionNotFoundException;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.decompte.PrestationsAFPourRevision;
import ch.globaz.vulpecula.domain.repositories.decompte.TravailleurPourRevision;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.osiris.CompteAnnexe;
import ch.globaz.vulpecula.services.PTAFServices;

public class ListRevisionExcelProcess extends BProcessWithContext {
    private static final long serialVersionUID = 4673022589446026422L;
    private String anneeDebut = "";
    private String anneeFin = "";
    private String idEmployeur = "";
    private String affilieNumero = "";

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        setProgressScaleValue(4);
        TreeMap<String, TravailleurPourRevision> listeTravailleur = retrieve();
        setProgressCounter(1);

        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(idEmployeur);
        affilieNumero = employeur.getAffilieNumero();

        CompteAnnexe ca = VulpeculaServiceLocator.getCompteAnnexeService().findByNumAffilieAndIdTiersAndCategorie(
                employeur.getAffilieNumero(), employeur.getIdTiers(), employeur.getTypeAffiliation());

        if (!listeTravailleur.isEmpty()) {

            // On enlève les travailleurs qui n'ont rien pour l'année
            listeTravailleur = removeTravailleurAZero(listeTravailleur);

            ListRevisionRecapExcel recap = new ListRevisionRecapExcel(getSession(),
                    DocumentConstants.LISTES_REVISION_NAME + " " + affilieNumero + " " + anneeDebut,
                    DocumentConstants.LISTES_REVISION_DOC_NAME, ca);
            ListRevisionExcel list = new ListRevisionExcel(recap);
            trierDecompteLignes(listeTravailleur);

            setProgressCounter(2);

            recap.setListeTravailleur(listeTravailleur);
            recap.setIdEmployeur(idEmployeur);
            recap.setEmployeur(employeur);
            recap.setAnnee(anneeDebut);
            recap.create();

            setProgressCounter(3);

            list.setListeTravailleur(listeTravailleur);
            list.setAnnee(anneeDebut);
            list.setIdEmployeur(idEmployeur);
            list.create();
            registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), list.getOutputFile());

        } else {
            ListRevisionExcel list = new ListRevisionExcel(getSession(), DocumentConstants.LISTES_REVISION_NAME + " "
                    + affilieNumero + " " + anneeDebut, DocumentConstants.LISTES_REVISION_DOC_NAME);
            list.setListeTravailleur(listeTravailleur);
            list.setAnnee(anneeDebut);
            list.setIdEmployeur(idEmployeur);
            list.create();
            registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), list.getOutputFile());
        }

        setProgressCounter(4);
        return true;
    }

    private TreeMap<String, TravailleurPourRevision> removeTravailleurAZero(
            TreeMap<String, TravailleurPourRevision> listeTravailleur) {

        TreeMap<String, TravailleurPourRevision> listeTravailleurOk = new TreeMap<String, TravailleurPourRevision>();

        for (Map.Entry<String, TravailleurPourRevision> travailleurRevision : listeTravailleur.entrySet()) {
            TravailleurPourRevision trav = travailleurRevision.getValue();
            String key = travailleurRevision.getKey();

            if (!(trav.getDecomptesSalaireCP().isEmpty() && trav.getDecomptesSalaireCT().isEmpty()
                    && trav.getDecomptesSalairePR().isEmpty() && trav.getDecomptesSalaires().isEmpty()
                    && trav.getDecomptesSalaireSP().isEmpty() && trav.getListeAJ().isEmpty()
                    && trav.getListeCP().isEmpty() && trav.getListeSM().isEmpty() && trav.getListePrestationsAF()
                    .isEmpty() && trav.getDecomptesSalaireCPP().isEmpty())) {
                listeTravailleurOk.put(key, trav);
            }
        }

        return listeTravailleurOk;
    }

    private void calculMontantTotal(TravailleurPourRevision travailleur) {
        List<DecompteSalaire> decomptesSalaires = travailleur.getDecomptesSalaires();
        travailleur.setMontantAF(getMontantAF(decomptesSalaires, travailleur));
        travailleur.setMontantAVS(getMontantAVS(decomptesSalaires, travailleur));
        travailleur.setMontantCP(getMontantCP(decomptesSalaires));
        travailleur.setMontantBase(getMontantBase(decomptesSalaires));

        try {
            List<EntetePrestationComplexModel> listePrestations = PTAFServices.getPrestationsForAlloc(travailleur
                    .getTravailleur().getIdTiers(), new Date("01.01." + anneeDebut), new Date("31.12." + anneeFin),
                    idEmployeur);

            if (listePrestations != null && listePrestations.size() > 0) {
                // Collections.sort(listePrestations, new EntetePrestationPourRevisionComparator());
                // listePrestations;
                List<PrestationsAFPourRevision> listeToSave = new ArrayList<PrestationsAFPourRevision>();
                for (EntetePrestationComplexModel entetePrestationComplexModel : listePrestations) {
                    PrestationsAFPourRevision prestation = new PrestationsAFPourRevision();
                    prestation.setDateDebut(new Date(entetePrestationComplexModel.getPeriodeDe()));
                    prestation.setDateFin(new Date(entetePrestationComplexModel.getPeriodeA()));
                    prestation.setMontant(new Montant(entetePrestationComplexModel.getMontantTotal()));
                    prestation.setDateVersement(entetePrestationComplexModel.getDateComptableVersement());
                    listeToSave.add(prestation);
                }
                travailleur.setListePrestationsAF(listeToSave);
            }
        } catch (TauxImpositionNotFoundException e) {
        }
    }

    private Montant getMontantAVS(List<DecompteSalaire> decomptesSalaires, TravailleurPourRevision travailleur) {
        Montant montant = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            montant = montant.add(decompteSalaire.getMasseAVS());
        }
        for (AbsenceJustifiee absence : travailleur.getListeAJ()) {
            if (absence.isAnnoncableAVS()) {
                montant = montant.add(absence.getMasseAVS());
            }
        }
        for (CongePaye conge : travailleur.getListeCP()) {
            if (conge.isAnnoncableAVS()) {
                montant = montant.add(conge.getMasseAVS());
            }
        }
        return montant;
    }

    private Montant getMontantAF(List<DecompteSalaire> decomptesSalaires, TravailleurPourRevision travailleur) {
        Montant montant = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            montant = montant.add(decompteSalaire.getMasseAFPourReviseur());
        }

        for (AbsenceJustifiee absence : travailleur.getListeAJ()) {
            if (absence.isAnnoncableAVS()) {
                montant = montant.add(absence.getMasseAVS());
            }
        }
        for (CongePaye conge : travailleur.getListeCP()) {
            if (conge.isAnnoncableAVS()) {
                montant = montant.add(conge.getMasseAVS());
            }
        }

        return montant;
    }

    /**
     * Le montant de base concerne tous les décomptes salaires autre que CP.
     * 
     * @param decomptesSalaires Décomptes salaires
     * @return Montant de base
     */
    private Montant getMontantBase(List<DecompteSalaire> decomptesSalaires) {
        Montant montant = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            if (!decompteSalaire.isComplementaire()) {
                montant = montant.add(decompteSalaire.getSalaireTotal());
            }
        }
        return montant;
    }

    /**
     * Le montant concernant le décompte complémentaire
     * 
     * @param decomptesSalaires Décomptes salaires
     * @return Montant (masse) des décomptes complémentaires
     */
    private Montant getMontantCP(List<DecompteSalaire> decomptesSalaires) {
        Montant montant = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            if (decompteSalaire.isComplementaire()) {
                montant = montant.add(decompteSalaire.getSalaireTotal());
            }
        }
        return montant;
    }

    private void trierDecompteLignes(TreeMap<String, TravailleurPourRevision> listeTravailleur) {
        rechercheTravailleursAvecUniquementPrestations(listeTravailleur);
        Set<String> cles = listeTravailleur.keySet();
        Iterator<String> it = cles.iterator();
        List<Object> clesToRemove = new ArrayList<Object>();
        while (it.hasNext()) {
            Object cle = it.next();
            TravailleurPourRevision travailleur = listeTravailleur.get(cle);
            recherchePrestations(travailleur);
            calculMontantTotal(travailleur);
            Collections.sort(travailleur.getDecomptesSalairePR(), new DecompteSalairePourRevisionComparator());
            if (travailleur.getDecomptesSalaireCP().isEmpty() && travailleur.getDecomptesSalaireCPP().isEmpty()
                    && travailleur.getDecomptesSalaireCT().isEmpty() && travailleur.getDecomptesSalairePR().isEmpty()
                    && travailleur.getDecomptesSalaires().isEmpty() && travailleur.getDecomptesSalaireSP().isEmpty()
                    && travailleur.getListeAJ().isEmpty() && travailleur.getListeCP().isEmpty()
                    && travailleur.getListeSM().isEmpty() && travailleur.getListePrestationsAF().isEmpty()) {
                clesToRemove.add(cle);
            }
        }

        for (Iterator iterator = clesToRemove.iterator(); iterator.hasNext();) {
            Object cle = iterator.next();
            listeTravailleur.remove(cle);
        }
    }

    private void rechercheTravailleursAvecUniquementPrestations(
            TreeMap<String, TravailleurPourRevision> listeTravailleur) {
        List<PosteTravail> listePostes = VulpeculaRepositoryLocator.getPosteTravailRepository().findPosteActifInAnnee(
                idEmployeur, new Annee(anneeDebut));
        for (PosteTravail posteTravail : listePostes) {
            String key = posteTravail.getTravailleur().getDesignation1()
                    + posteTravail.getTravailleur().getDesignation2() + posteTravail.getTravailleur().getId();
            if (!listeTravailleur.containsKey(key)) {
                List<PosteTravail> listePostesTravail = new ArrayList<PosteTravail>();
                listePostesTravail.add(posteTravail);
                TravailleurPourRevision nouveauTravailleur = new TravailleurPourRevision();
                Travailleur travailleur = posteTravail.getTravailleur();
                travailleur.setPostesTravail(listePostesTravail);
                nouveauTravailleur.setTravailleur(posteTravail.getTravailleur());
                listeTravailleur.put(key, nouveauTravailleur);
            }
        }

    }

    private void recherchePrestations(TravailleurPourRevision travailleur) {
        rechercheAbsences(travailleur);

        rechercheCongePayes(travailleur);

        rechercheServiceMilitaire(travailleur);
    }

    private void rechercheServiceMilitaire(TravailleurPourRevision travailleur) {

        List<ServiceMilitaire> listeSM1 = VulpeculaRepositoryLocator.getServiceMilitaireRepository()
                .findByIdTravailleurForRevision(travailleur.getTravailleur().getId(), idEmployeur,
                        "01.01." + anneeDebut, "31.12." + anneeFin);

        travailleur.setListeSM(listeSM1);
    }

    private void rechercheCongePayes(TravailleurPourRevision travailleur) {
        List<CongePaye> listeCPPassageFacturation = VulpeculaRepositoryLocator.getCongePayeRepository()
                .findByIdTravailleurAndDatePassageFacturationAndIdEmployeur(travailleur.getTravailleur().getId(),
                        "01.01." + anneeDebut, "31.12." + anneeFin, idEmployeur);

        List<CongePaye> listeCPDateVersement = VulpeculaRepositoryLocator.getCongePayeRepository()
                .findByIdTravailleurForDateVersementAndIdEmployeur(travailleur.getTravailleur().getId(),
                        "01.01." + anneeDebut, "31.12." + anneeFin, idEmployeur);

        listeCPPassageFacturation.addAll(listeCPDateVersement);
        travailleur.setListeCP(listeCPPassageFacturation);
    }

    private boolean isAuMoinsUnCongePayeAVS(String idTravailleur) {
        List<CongePaye> listeCPPassageFacturation = VulpeculaRepositoryLocator
                .getCongePayeRepository()
                .findByIdTravailleurAndDatePassageFacturation(idTravailleur, "01.01." + anneeDebut, "31.12." + anneeFin);

        List<CongePaye> listeCPDateVersement = VulpeculaRepositoryLocator.getCongePayeRepository()
                .findByIdTravailleurForDateVersement(idTravailleur, "01.01." + anneeDebut, "31.12." + anneeFin);

        listeCPPassageFacturation.addAll(listeCPDateVersement);
        return !listeCPPassageFacturation.isEmpty();
    }

    private void rechercheAbsences(TravailleurPourRevision travailleur) {
        // On recherche par rapport à la date de versement
        List<AbsenceJustifiee> listeAJDateVersement = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository()
                .findByIdTravailleurForDateVersementAndIdEmployeur(travailleur.getTravailleur().getId(),
                        "01.01." + anneeDebut, "31.12." + anneeFin, idEmployeur);

        List<AbsenceJustifiee> listeAJDatePassage = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository()
                .findByIdTravailleurAndDatePassageFacturationAndIdEmployeur(travailleur.getTravailleur().getId(),
                        "01.01." + anneeDebut, "31.12." + anneeFin, idEmployeur);

        listeAJDateVersement.addAll(listeAJDatePassage);

        travailleur.setListeAJ(listeAJDateVersement);
    }

    private boolean isAuMoinsUneAbsenceJustifieeAVS(String idTravailleur) {
        // On recherche par rapport à la date de versement
        List<AbsenceJustifiee> listeAJDateVersement = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository()
                .findByIdTravailleurForDateVersement(idTravailleur, "01.01." + anneeDebut, "31.12." + anneeFin);

        List<AbsenceJustifiee> listeAJDatePassage = VulpeculaRepositoryLocator
                .getAbsenceJustifieeRepository()
                .findByIdTravailleurAndDatePassageFacturation(idTravailleur, "01.01." + anneeDebut, "31.12." + anneeFin);

        listeAJDateVersement.addAll(listeAJDatePassage);
        return !listeAJDateVersement.isEmpty();
    }

    public TreeMap<String, TravailleurPourRevision> retrieve() {
        TreeMap<String, TravailleurPourRevision> liste = new TreeMap<String, TravailleurPourRevision>();
        List<DecompteSalaire> decompteSalaireListe = new ArrayList<DecompteSalaire>();
        Annee anneeDeFin = null;
        if (!JadeStringUtil.isEmpty(anneeFin)) {
            anneeDeFin = new Annee(anneeFin);
            decompteSalaireListe = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(idEmployeur,
                            new Date(anneeDebut + "0101"), new Date(anneeFin + "1231"));
        } else {
            decompteSalaireListe = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(idEmployeur,
                            new Date(anneeDebut + "0101"), new Date(anneeDebut + "1231"));
        }

        List<DecompteSalaire> decomptesCPP = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findCPPByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(idEmployeur, new Annee(anneeDebut),
                        anneeDeFin);
        for (DecompteSalaire decompteSalaire : decompteSalaireListe) {
            TravailleurPourRevision travailleurPourRevision = getOrCreateTravailleur(liste, decompteSalaire);
            switch (decompteSalaire.getTypeDecompte()) {
                case PERIODIQUE:
                    travailleurPourRevision.getDecomptesSalairePR().add(decompteSalaire);
                    break;
                case SPECIAL_SALAIRE:
                    travailleurPourRevision.getDecomptesSalaireSP().add(decompteSalaire);
                    break;
                case COMPLEMENTAIRE:
                    travailleurPourRevision.getDecomptesSalaireCP().add(decompteSalaire);
                    break;
                case CONTROLE_EMPLOYEUR:
                    travailleurPourRevision.getDecomptesSalaireCT().add(decompteSalaire);
                    break;
                default:
                    break;
            }
        }

        for (DecompteSalaire decompteSalaire : decomptesCPP) {
            TravailleurPourRevision travailleurPourRevision = getOrCreateTravailleur(liste, decompteSalaire);
            travailleurPourRevision.getDecomptesSalaireCPP().add(decompteSalaire);
        }

        // On boucle sur les travailleurs de l'employeurs
        List<PosteTravail> listePoste = VulpeculaRepositoryLocator.getPosteTravailRepository().findByIdEmployeur(
                idEmployeur);
        for (PosteTravail posteTravail : listePoste) {
            // On ne tient pas compte des travailleurs déjà contenu dans la liste (ayant des décomptes)
            if (!liste.containsKey(posteTravail.getTravailleur().getDesignation1()
                    + posteTravail.getTravailleur().getDesignation2() + posteTravail.getIdTravailleur())) {
                if (isAuMoinsUnCongePayeAVS(posteTravail.getIdTravailleur())
                        || isAuMoinsUneAbsenceJustifieeAVS(posteTravail.getIdTravailleur())) {
                    // On enlève ceux non AVS
                    // On les rajoute à la liste
                    TravailleurPourRevision travailleur = new TravailleurPourRevision();
                    Travailleur trav = posteTravail.getTravailleur();
                    List<PosteTravail> poste = new ArrayList<PosteTravail>();
                    poste.add(posteTravail);
                    trav.setPostesTravail(poste);
                    travailleur.setTravailleur(trav);

                    liste.put(posteTravail.getTravailleur().getDesignation1()
                            + posteTravail.getTravailleur().getDesignation2() + posteTravail.getTravailleur().getId(),
                            travailleur);
                }
            }
        }
        return liste;
    }

    private TravailleurPourRevision getOrCreateTravailleur(TreeMap<String, TravailleurPourRevision> liste,
            DecompteSalaire decompteSalaire) {
        if (!containsTravailleur(liste, decompteSalaire)) {
            Travailleur travailleur = decompteSalaire.getTravailleur();
            TravailleurPourRevision nouveauTravailleur = new TravailleurPourRevision();
            if (!JadeStringUtil.isEmpty(decompteSalaire.getIdPosteTravail())) {
                PosteTravail poste = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                        decompteSalaire.getIdPosteTravail());

                List<PosteTravail> listePoste = new ArrayList<PosteTravail>();
                listePoste.add(poste);
                travailleur.setPostesTravail(listePoste);
            }
            liste.put(decompteSalaire.getTravailleur().getDesignation1()
                    + decompteSalaire.getTravailleur().getDesignation2() + decompteSalaire.getTravailleur().getId(),
                    nouveauTravailleur);
            nouveauTravailleur.setTravailleur(travailleur);
        }
        return liste.get(getKey(decompteSalaire));
    }

    private boolean containsTravailleur(TreeMap<String, TravailleurPourRevision> liste, DecompteSalaire decompteSalaire) {
        return liste.containsKey(getKey(decompteSalaire));
    }

    private String getKey(DecompteSalaire decompteSalaire) {
        // Clé composé d'une concaténation de String... à modifier à l'occasion
        return decompteSalaire.getTravailleur().getDesignation1() + decompteSalaire.getTravailleur().getDesignation2()
                + decompteSalaire.getTravailleur().getId();
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_REVISION_NAME + " " + affilieNumero + " " + anneeDebut;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }
}
