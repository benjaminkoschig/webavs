package ch.globaz.vulpecula.process.communicationsalaires;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.fichierPlat.CommunicationSalairesResorCSVExporter;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class CommunicationSalairesResorProcess extends AbstractCommunicationSalairesProcess implements Observer {
    private static final long serialVersionUID = -5915275758727848732L;

    private Annee annee;
    private String idConvention;
    private Convention convention;
    private String typeDecompte;
    private boolean miseAJour;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        Deque<DecompteSalaire> listeSalaires = retrieve();
        List<DecompteSalaire> decompteGroupeOrdreCumule = createListGroupeParTravailleurEmployeurPeriode(listeSalaires);

        List<SalairesAAnnoncer> listeSalairesAAnnoncer = RemplirListeSalaires(decompteGroupeOrdreCumule);

        setProgressScaleValue(listeSalaires.size());

        if (listeSalaires.size() != 0) {
            // printListeExcel(listeSalairesAAnnoncer);
            createFile(listeSalairesAAnnoncer);
        } else {
            getMemoryLog().logMessage(BSessionUtil.getSessionFromThreadContext().getLabel("AUCUN_SALAIRE_A_ANNONCER"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }
        return true;
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

    private Deque<DecompteSalaire> retrieve() {
        return VulpeculaRepositoryLocator.getDecompteSalaireRepository().findSalairesResorPourAnnee(annee);
    }

    private void printListeExcel(List<SalairesAAnnoncer> listeSalairesAAnnoncer) throws IOException {
        ListSalairesResorExcel listeSalairesExcel = new ListSalairesResorExcel(getSession(),
                DocumentConstants.LISTES_SALAIRES_NAME, DocumentConstants.LISTES_SALAIRES_DOC_NAME);
        listeSalairesExcel.setListDecompteSalaire(listeSalairesAAnnoncer);
        listeSalairesExcel.addObserver(this);
        listeSalairesExcel.setAnnee(getAnnee());
        listeSalairesExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listeSalairesExcel.getOutputFile());
    }

    private void createFile(List<SalairesAAnnoncer> listeSalairesAAnnoncer) throws Exception {

        String fileName = DocumentConstants.LISTES_SALAIRES_RESOR_NAME + DocumentConstants.EXTENSION_CSV;

        CommunicationSalairesResorCSVExporter.export(listeSalairesAAnnoncer, Jade.getInstance().getPersistenceDir(),
                fileName, getAnnee());

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
                    // if (!salaireGroupe.getSalaireTotal().isZero()) {
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

                    if (salaireGroupe.getPeriode().chevaucheOuSuit(decompteSalairePrecedent.getPeriode())
                            || decompteSalairePrecedent.getPeriode().chevaucheOuSuit(salaireGroupe.getPeriode())) {
                        Date dateDebutPeriode = null;
                        Date dateFinPeriode = null;

                        if (salaireGroupe.getPeriodeDebut().before(decompteSalairePrecedent.getPeriodeDebut())) {
                            dateDebutPeriode = salaireGroupe.getPeriodeDebut().getFirstDayOfMonth();
                        } else {
                            dateDebutPeriode = decompteSalairePrecedent.getPeriodeDebut().getFirstDayOfMonth();
                        }

                        if (salaireGroupe.getPeriodeFin().after(decompteSalairePrecedent.getPeriodeFin())) {
                            dateFinPeriode = salaireGroupe.getPeriodeFin().getLastDayOfMonth();
                        } else {
                            dateFinPeriode = decompteSalairePrecedent.getPeriodeFin().getLastDayOfMonth();
                        }

                        dateFin = dateFinPeriode;
                        montantTotal = montantTotal.add(salaireGroupe.getSalaireTotal());
                        salaireGroupe.setPeriode(new Periode(dateDebutPeriode, dateFinPeriode));
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

                    if (i == salairesParTravailleurEtEmployeur.getValue().size()) {
                        if (!montantTotal.isZero()) {
                            // TODO gérer les cas à 0 pour le code versement
                        }
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

    @Override
    public Annee getAnnee() {
        return annee;
    }

    @Override
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
        ListSalairesResorExcel listSalairesExcel = (ListSalairesResorExcel) o;
        setProgressCounter(listSalairesExcel.getCurrentElement());
    }
}
