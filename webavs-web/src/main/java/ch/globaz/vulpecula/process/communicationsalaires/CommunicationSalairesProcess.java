package ch.globaz.vulpecula.process.communicationsalaires;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.common.sql.QueryUpdateExecutor;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.fichierPlat.CommunicationSalairesExporter;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class CommunicationSalairesProcess extends BProcessWithContext implements Observer {
    private static final long serialVersionUID = -5915275758727848732L;
    private final static String libelleTraitementNonAVS = "NON_AVS";
    private final static String libelleTraitement = "COMMUNICATION_SALAIRES";

    private Annee annee;
    private String idConvention;
    private Convention convention;
    private String typeDecompte;
    private boolean miseAJour;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        Map<Pair<String, String>, List<AbsenceJustifiee>> mapAJ = new HashMap<Pair<String, String>, List<AbsenceJustifiee>>();
        Map<Pair<String, String>, List<CongePaye>> mapCP = new HashMap<Pair<String, String>, List<CongePaye>>();

        Deque<DecompteSalaire> listeSalaires = retrieve();
        List<DecompteSalaire> decompteGroupeOrdreCumule = createListGroupeParTravailleurEmployeurPeriode(listeSalaires);

        if (!getTypeDecompte().equals(TypeDecompte.CONTROLE_EMPLOYEUR.getValue())) {
            mapAJ = retrieveAJ();
            mapCP = retrieveCP();
        }

        List<AbsenceJustifiee> listeAJNonAVS = new ArrayList<AbsenceJustifiee>();
        List<CongePaye> listeCPNonAVS = new ArrayList<CongePaye>();

        List<AbsenceJustifiee> listeAJAAnnoncer = new ArrayList<AbsenceJustifiee>();
        List<CongePaye> listeCPAAnnoncer = new ArrayList<CongePaye>();

        // remove cas sans avs et maj de ces derniers avec traitement = hors AVS
        for (Map.Entry<Pair<String, String>, List<AbsenceJustifiee>> map : mapAJ.entrySet()) {
            for (AbsenceJustifiee absenceJustifiee : map.getValue()) {
                if (!absenceJustifiee.isAnnoncableAVS()) {
                    listeAJNonAVS.add(absenceJustifiee);
                } else {
                    listeAJAAnnoncer.add(absenceJustifiee);
                }
            }
        }

        for (Map.Entry<Pair<String, String>, List<CongePaye>> map2 : mapCP.entrySet()) {
            for (CongePaye congePaye : map2.getValue()) {
                if (!congePaye.isAnnoncableAVS()) {
                    listeCPNonAVS.add(congePaye);
                } else {
                    listeCPAAnnoncer.add(congePaye);
                }
            }
        }

        List<SalairesAAnnoncer> listeSalairesAAnnoncer = RemplirListeSalaires(decompteGroupeOrdreCumule);
        listeSalairesAAnnoncer = RemplirAbsencesConges(listeSalairesAAnnoncer, mapAJ, mapCP);

        setProgressScaleValue(listeSalaires.size());

        if (listeSalaires.size() != 0 || mapAJ.size() != 0 || mapCP.size() != 0) {
            printListeExcel(listeSalairesAAnnoncer);
            createFile(listeSalairesAAnnoncer);
            if (isMiseAJour()) {
                majChampLigneDecompte(listeSalaires);
                majAJNonAVS(listeAJNonAVS);
                majCPNonAVS(listeCPNonAVS);
                majAJ(listeAJAAnnoncer);
                majCP(listeCPAAnnoncer);
            }
        } else {
            getMemoryLog().logMessage(BSessionUtil.getSessionFromThreadContext().getLabel("AUCUN_SALAIRE_A_ANNONCER"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

        return true;
    }

    private void majCP(List<CongePaye> listeCPAAnnoncer) {
        for (CongePaye congePaye : listeCPAAnnoncer) {
            congePaye.setDateTraitementSalaires(Date.now());
            congePaye.setTraitementSalaires(libelleTraitement);
            VulpeculaRepositoryLocator.getCongePayeRepository().update(congePaye);
        }
    }

    private void majAJ(List<AbsenceJustifiee> listeAJAAnnoncer) {
        for (AbsenceJustifiee absenceJustifiee : listeAJAAnnoncer) {
            absenceJustifiee.setDateTraitementSalaires(Date.now());
            absenceJustifiee.setTraitementSalaires(libelleTraitement);
            VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().update(absenceJustifiee);
        }
    }

    private void majCPNonAVS(List<CongePaye> listeCPNonAVS) {
        for (CongePaye congePaye : listeCPNonAVS) {
            congePaye.setDateTraitementSalaires(Date.now());
            congePaye.setTraitementSalaires(libelleTraitementNonAVS);
            VulpeculaRepositoryLocator.getCongePayeRepository().update(congePaye);
        }
    }

    private void majAJNonAVS(List<AbsenceJustifiee> listeAJNonAVS) {
        for (AbsenceJustifiee absenceJustifiee : listeAJNonAVS) {
            absenceJustifiee.setDateTraitementSalaires(Date.now());
            absenceJustifiee.setTraitementSalaires(libelleTraitementNonAVS);
            VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().update(absenceJustifiee);
        }
    }

    private List<SalairesAAnnoncer> RemplirAbsencesConges(List<SalairesAAnnoncer> listeSalairesAAnnoncer,
            Map<Pair<String, String>, List<AbsenceJustifiee>> mapAJ, Map<Pair<String, String>, List<CongePaye>> mapCP) {
        for (SalairesAAnnoncer salairesAAnnoncer : listeSalairesAAnnoncer) {
            String idEmployeur = salairesAAnnoncer.getIdEmployeur();
            String idTravailleur = salairesAAnnoncer.getIdTravailleur();
            Pair<String, String> pair = new Pair<String, String>(idEmployeur, idTravailleur);
            List<DecompteSalaire> listeDecompte = salairesAAnnoncer.getListeDecomptes();
            if (mapAJ.containsKey(pair)) {
                List<AbsenceJustifiee> listeAJ = mapAJ.remove(pair);
                List<AbsenceJustifiee> listeAJRestante = new ArrayList<AbsenceJustifiee>();
                for (AbsenceJustifiee absenceJustifiee : listeAJ) {
                    listeAJRestante.add(absenceJustifiee);
                }
                List<AbsenceJustifiee> listeAJAutresAnnees = new ArrayList<AbsenceJustifiee>();
                for (AbsenceJustifiee absenceJustifiee : listeAJ) {
                    if (Integer.parseInt(listeDecompte.get(0).getPeriodeDebut().getAnnee()) != absenceJustifiee
                            .getAnneeDateVersement().getValue()) {
                        listeAJAutresAnnees.add(absenceJustifiee);
                        listeAJRestante.remove(absenceJustifiee);
                    }
                }
                if (!listeAJAutresAnnees.isEmpty()) {
                    mapAJ.put(pair, listeAJAutresAnnees);
                }

                listeAJ = new ArrayList<AbsenceJustifiee>();
                for (AbsenceJustifiee absenceJustifiee : listeAJRestante) {
                    listeAJ.add(absenceJustifiee);
                }

                for (AbsenceJustifiee absenceJustifiee : listeAJ) {
                    for (DecompteSalaire decompte : listeDecompte) {
                        if (Integer.valueOf(decompte.getPeriodeDebut().getAnnee()).intValue() == absenceJustifiee
                                .getAnneeDateVersement().getValue()) {
                            Montant salaire = decompte.getSalaireTotal();
                            salaire = salaire.add(absenceJustifiee.getMasseAVS());
                            decompte.setSalaireTotal(salaire);
                            listeAJRestante.remove(absenceJustifiee);
                        }
                    }
                    salairesAAnnoncer.setListeAJ(listeAJRestante);
                }
            }
            if (mapCP.containsKey(pair)) {
                List<CongePaye> listeCP = mapCP.remove(pair);

                List<CongePaye> listeCPRestante = new ArrayList<CongePaye>();
                for (CongePaye congePaye : listeCP) {
                    // On ne prend que les congés payés avec un montant brut plus grand que le montant net
                    if (congePaye.getMontantBrut().greater(congePaye.getMontantNet())) {
                        listeCPRestante.add(congePaye);
                    }
                }

                List<CongePaye> listeCPAutresAnnees = new ArrayList<CongePaye>();
                for (CongePaye congePaye : listeCP) {
                    if (Integer.parseInt(listeDecompte.get(0).getPeriodeDebut().getAnnee()) != congePaye
                            .getAnneeDateVersement().getValue()) {
                        listeCPAutresAnnees.add(congePaye);
                        listeCPRestante.remove(congePaye);
                    }
                }
                if (!listeCPAutresAnnees.isEmpty()) {
                    mapCP.put(pair, listeCPAutresAnnees);
                }

                listeCP = new ArrayList<CongePaye>();
                for (CongePaye congePaye : listeCPRestante) {
                    listeCP.add(congePaye);
                }
                // Pour chaque congé payé, si un de ces décompte a la même année que la date de versement du congé payé
                // on ajoute la masse AVS du congé payé au salaire total du décompte
                // on enlève le congé payé de la liste restante
                for (CongePaye congePaye : listeCP) {
                    for (DecompteSalaire decompte : listeDecompte) {
                        if (Integer.parseInt(decompte.getPeriodeDebut().getAnnee()) == congePaye
                                .getAnneeDateVersement().getValue()) {
                            Montant salaire = decompte.getSalaireTotal();
                            salaire = salaire.add(congePaye.getMasseAVS());
                            decompte.setSalaireTotal(salaire);
                            listeCPRestante.remove(congePaye);
                        }
                    }
                    salairesAAnnoncer.setListeCP(listeCPRestante);
                }
            }
        }
        // On boucle sur les cp et aj restant qui n'ont pas de décomptes
        for (Map.Entry<Pair<String, String>, List<AbsenceJustifiee>> aj : mapAJ.entrySet()) {
            List<AbsenceJustifiee> absence = aj.getValue();
            AbsenceJustifiee absence1 = absence.get(0);
            SalairesAAnnoncer salaire = new SalairesAAnnoncer();
            salaire.setIdEmployeur(absence1.getIdEmployeur());
            salaire.setIdTravailleur(absence1.getIdTravailleur());
            salaire.setListeAJ(absence);
            listeSalairesAAnnoncer.add(salaire);
        }
        for (Map.Entry<Pair<String, String>, List<CongePaye>> cp : mapCP.entrySet()) {
            List<CongePaye> conge = cp.getValue();
            CongePaye conge1 = conge.get(0);
            SalairesAAnnoncer salaire = new SalairesAAnnoncer();
            salaire.setIdEmployeur(conge1.getIdEmployeur());
            salaire.setIdTravailleur(conge1.getIdTravailleur());
            salaire.setListeCP(conge);
            listeSalairesAAnnoncer.add(salaire);
        }

        return listeSalairesAAnnoncer;
    }

    private Map<Pair<String, String>, List<AbsenceJustifiee>> retrieveAJ() {
        Map<Pair<String, String>, List<AbsenceJustifiee>> listeSalaires = new HashMap<Pair<String, String>, List<AbsenceJustifiee>>();
        List<AbsenceJustifiee> listeAJ = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository()
                .findSalairesPourAnnee(annee, idConvention);
        for (AbsenceJustifiee absenceJustifiee : listeAJ) {
            if (!absenceJustifiee.isCotisationsDeduites()) {
                // On exclut les cotisations postives
                continue;
            }
            String idEmployeur = absenceJustifiee.getIdEmployeur();
            String idTravailleur = absenceJustifiee.getIdTravailleur();
            Pair<String, String> pair = new Pair<String, String>(idEmployeur, idTravailleur);
            if (listeSalaires.containsKey(pair)) {
                List<AbsenceJustifiee> listeAbsence = listeSalaires.get(pair);
                listeAbsence.add(absenceJustifiee);
                listeSalaires.put(pair, listeAbsence);
            } else {
                List<AbsenceJustifiee> listeAbsence = new ArrayList<AbsenceJustifiee>();
                listeAbsence.add(absenceJustifiee);
                listeSalaires.put(pair, listeAbsence);
            }
        }
        return listeSalaires;
    }

    private Map<Pair<String, String>, List<CongePaye>> retrieveCP() {
        Map<Pair<String, String>, List<CongePaye>> listeSalaires = new HashMap<Pair<String, String>, List<CongePaye>>();
        List<CongePaye> listeCP = VulpeculaRepositoryLocator.getCongePayeRepository().findSalairesPourAnnee(annee,
                idConvention);
        for (CongePaye congePaye : listeCP) {
            if (!congePaye.isCotisationsDeduites()) {
                continue;
            }
            String idEmployeur = congePaye.getIdEmployeur();
            String idTravailleur = congePaye.getIdTravailleur();
            Pair<String, String> pair = new Pair<String, String>(idEmployeur, idTravailleur);
            if (listeSalaires.containsKey(pair)) {
                List<CongePaye> listeConge = listeSalaires.get(pair);
                listeConge.add(congePaye);
                listeSalaires.put(pair, listeConge);
            } else {
                List<CongePaye> listeConge = new ArrayList<CongePaye>();
                listeConge.add(congePaye);
                listeSalaires.put(pair, listeConge);
            }
        }
        return listeSalaires;
    }

    private List<SalairesAAnnoncer> RemplirListeSalaires(List<DecompteSalaire> decompteGroupeOrdreCumule) {
        String idTravailleur = "";
        String idEmployeur = "";
        List<DecompteSalaire> listeDecompte = new ArrayList<DecompteSalaire>();
        List<SalairesAAnnoncer> listeSalairesAAnnoncer = new ArrayList<SalairesAAnnoncer>();
        SalairesAAnnoncer salaires = new SalairesAAnnoncer();
        for (DecompteSalaire decompteSalaire : decompteGroupeOrdreCumule) {
            salaires = new SalairesAAnnoncer();
            listeDecompte = new ArrayList<DecompteSalaire>();
            idTravailleur = decompteSalaire.getIdTravailleur();
            idEmployeur = decompteSalaire.getIdEmployeur();
            salaires.setIdEmployeur(idEmployeur);
            salaires.setIdTravailleur(idTravailleur);
            listeDecompte.add(decompteSalaire);
            salaires.setListeDecomptes(listeDecompte);
            listeSalairesAAnnoncer.add(salaires);
        }
        return listeSalairesAAnnoncer;
    }

    private void majChampLigneDecompte(Deque<DecompteSalaire> listeSalaires) throws Exception {
        // VulpeculaServiceLocator.getDecompteSalaireService().majDateAnnonceSalaire(listeSalaires);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date dateAnnonce = new java.util.Date();
        String dateSpy = format.format(dateAnnonce) + "000000" + getSession().getUserId();
        for (DecompteSalaire salaire : listeSalaires) {
            QueryUpdateExecutor.executeUpdate("update  schema.PT_DECOMPTE_LIGNES set DATE_ANNONCE = '" + dateSpy
                    + "' where ID = " + salaire.getId(), DecompteSalaire.class, getSession());
        }
    }

    private Deque<DecompteSalaire> retrieve() {
        return VulpeculaRepositoryLocator.getDecompteSalaireRepository().findSalairesPourAnnee(annee, getConvention(),
                getTypeDecompte());
    }

    private void printListeExcel(List<SalairesAAnnoncer> listeSalairesAAnnoncer) throws IOException {
        ListSalairesExcel listeSalairesExcel = new ListSalairesExcel(getSession(),
                DocumentConstants.LISTES_SALAIRES_NAME, DocumentConstants.LISTES_SALAIRES_DOC_NAME);
        listeSalairesExcel.setListDecompteSalaire(listeSalairesAAnnoncer);
        listeSalairesExcel.addObserver(this);
        if (getConvention() != null) {
            listeSalairesExcel.setConvention(getConvention());
        }
        listeSalairesExcel.setTypeDecompte(getTypeDecompte());
        listeSalairesExcel.setAnnee(getAnnee());
        listeSalairesExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listeSalairesExcel.getOutputFile());
    }

    private void createFile(List<SalairesAAnnoncer> listeSalairesAAnnoncer) throws Exception {

        String fileName = DocumentConstants.LISTES_SALAIRES_NAME + DocumentConstants.EXTENSION_TXT;

        CommunicationSalairesExporter.export(listeSalairesAAnnoncer, Jade.getInstance().getPersistenceDir(), fileName);

        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), Jade.getInstance()
                .getPersistenceDir() + fileName);
    }

    private List<DecompteSalaire> createListGroupeParTravailleurEmployeurPeriode(Deque<DecompteSalaire> listeSalaires) {
        List<DecompteSalaire> decompteGroupeOrdreCumule = new ArrayList<DecompteSalaire>();

        Map<String, Collection<DecompteSalaire>> salairesGroupesParTravailleur = grouperParSalarie(listeSalaires);
        for (Map.Entry<String, Collection<DecompteSalaire>> salairesParTravailleur : salairesGroupesParTravailleur
                .entrySet()) {
            Map<String, Collection<DecompteSalaire>> salairesGroupesParTravailleurEtEmployeur = grouperParEmployeur(salairesParTravailleur
                    .getValue());
            for (Map.Entry<String, Collection<DecompteSalaire>> salairesParTravailleurEtEmployeur : salairesGroupesParTravailleurEtEmployeur
                    .entrySet()) {

                boolean first = true;
                DecompteSalaire decompteSalairePrecedent = null;
                int i = 0;
                // On groupe par période

                // On tri par période
                // salairesParTravailleurEtEmployeur = trierParPeriode(salairesParTravailleurEtEmployeur);

                Montant montantTotal = Montant.ZERO;
                Date dateDebut = null;
                Date dateFin = null;

                for (DecompteSalaire salaireGroupe : salairesParTravailleurEtEmployeur.getValue()) {
                    i = i + 1;
                    if (!salaireGroupe.getSalaireTotal().isZero()) {
                        if (first) {
                            if (salairesParTravailleurEtEmployeur.getValue().size() == 1) {
                                decompteGroupeOrdreCumule.add(salaireGroupe);
                            } else {
                                dateDebut = salaireGroupe.getPeriodeDebut();
                                dateFin = salaireGroupe.getPeriodeFin();
                                decompteSalairePrecedent = salaireGroupe;
                                montantTotal = montantTotal.add(salaireGroupe.getSalaireTotal());
                                first = false;
                                if (i == salairesParTravailleurEtEmployeur.getValue().size()) {
                                    ajouterDecompte(decompteGroupeOrdreCumule, dateDebut, dateFin, montantTotal,
                                            decompteSalairePrecedent);
                                }
                            }
                            continue;
                        }

                        if (salaireGroupe.getPeriodeDebut().getAnnee()
                                .equals(decompteSalairePrecedent.getPeriodeDebut().getAnnee())) {
                            dateFin = salaireGroupe.getPeriodeFin();
                            montantTotal = montantTotal.add(salaireGroupe.getSalaireTotal());
                            salaireGroupe.setPeriode(new Periode(dateDebut, dateFin));
                            salaireGroupe.setSalaireTotal(montantTotal);
                            decompteSalairePrecedent = salaireGroupe;
                        } else {
                            ajouterDecompte(decompteGroupeOrdreCumule, dateDebut, dateFin, montantTotal,
                                    decompteSalairePrecedent);
                            dateDebut = salaireGroupe.getPeriodeDebut();
                            dateFin = salaireGroupe.getPeriodeFin();
                            montantTotal = salaireGroupe.getSalaireTotal();
                            decompteSalairePrecedent = salaireGroupe;
                        }
                    }
                    if (i == salairesParTravailleurEtEmployeur.getValue().size() && !montantTotal.isZero()) {
                        ajouterDecompte(decompteGroupeOrdreCumule, dateDebut, dateFin, montantTotal,
                                decompteSalairePrecedent);
                    }
                }

            }
        }
        return decompteGroupeOrdreCumule;
    }

    private void ajouterDecompte(List<DecompteSalaire> decompteGroupeOrdreCumule, Date dateDebut, Date dateFin,
            Montant montant, DecompteSalaire salaireGroupe) {

        DecompteSalaire decompteSalaireAAjouter = new DecompteSalaire();
        decompteSalaireAAjouter.setPosteTravail(salaireGroupe.getPosteTravail());
        if (dateDebut.before(dateFin)) {
            Periode periode = new Periode(dateDebut, dateFin);
            decompteSalaireAAjouter.setPeriode(periode);
        } else {
            Periode periode = new Periode(dateDebut, new Date(dateFin.getJour() + "." + dateFin.getMois() + "."
                    + dateDebut.getAnnee()));
            decompteSalaireAAjouter.setPeriode(periode);
        }
        decompteSalaireAAjouter.setSalaireTotal(montant);
        decompteGroupeOrdreCumule.add(decompteSalaireAAjouter);
    }

    private Map<String, Collection<DecompteSalaire>> grouperParEmployeur(
            Collection<DecompteSalaire> salairesParTravailleur) {
        return Multimaps.index(salairesParTravailleur, new Function<DecompteSalaire, String>() {
            @Override
            public String apply(DecompteSalaire salaire) {
                return salaire.getIdEmployeur();
            }
        }).asMap();
    }

    private Map<String, Collection<DecompteSalaire>> grouperParSalarie(Collection<DecompteSalaire> listeSalaires) {
        return Multimaps.index(listeSalaires, new Function<DecompteSalaire, String>() {
            @Override
            public String apply(DecompteSalaire salaire) {
                return salaire.getIdTravailleur();
            }
        }).asMap();
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_SALAIRES_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public Convention getConvention() {
        if (!JadeStringUtil.isEmpty(idConvention) && convention == null) {
            convention = VulpeculaRepositoryLocator.getConventionRepository().findById(getIdConvention());
        }
        return convention;
    }

    public void setConvention(Convention convention) {
        this.convention = convention;
    }

    public String getTypeDecompte() {
        return typeDecompte;
    }

    public void setTypeDecompte(String typeDecompte) {
        this.typeDecompte = typeDecompte;
    }

    public boolean isMiseAJour() {
        return miseAJour;
    }

    public boolean getMiseAJour() {
        return miseAJour;
    }

    public void setMiseAJour(boolean miseAJour) {
        this.miseAJour = miseAJour;
    }

    @Override
    public void update(Observable o, Object arg) {
        ListSalairesExcel listSalairesExcel = (ListSalairesExcel) o;
        setProgressCounter(listSalairesExcel.getCurrentElement());
    }
}
