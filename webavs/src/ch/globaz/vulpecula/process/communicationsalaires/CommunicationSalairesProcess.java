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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.fichierPlat.CommunicationSalairesExporter;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class CommunicationSalairesProcess extends BProcessWithContext implements Observer {
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
        Deque<DecompteSalaire> listeSalairesFile = new LinkedList<DecompteSalaire>(listeSalaires);
        Deque<DecompteSalaire> listeSalairesMAJ = new LinkedList<DecompteSalaire>();

        setProgressScaleValue(listeSalaires.size());

        if (listeSalaires.size() != 0) {

            if (isMiseAJour()) {
                listeSalairesMAJ = new LinkedList<DecompteSalaire>(listeSalaires);
            }
            printListeExcel(listeSalaires);
            createFile(listeSalairesFile);
            if (isMiseAJour()) {
                majChampLigneDecompte(listeSalairesMAJ);
            }
        } else {
            getMemoryLog().logMessage(BSessionUtil.getSessionFromThreadContext().getLabel("AUCUN_SALAIRE_A_ANNONCER"),
                    FWMessage.INFORMATION, this.getClass().getName());
        }

        return true;
    }

    private void majChampLigneDecompte(Deque<DecompteSalaire> listeSalairesMAJ) {
        VulpeculaServiceLocator.getDecompteSalaireService().majDateAnnonceSalaire(listeSalairesMAJ);
    }

    private Deque<DecompteSalaire> retrieve() {
        return VulpeculaRepositoryLocator.getDecompteSalaireRepository().findSalairesPourAnnee(annee, getConvention(),
                getTypeDecompte());
    }

    private void printListeExcel(Deque<DecompteSalaire> listeSalaires) throws IOException {
        ListSalairesExcel listeSalairesExcel = new ListSalairesExcel(getSession(),
                DocumentConstants.LISTES_SALAIRES_NAME, DocumentConstants.LISTES_SALAIRES_DOC_NAME);
        listeSalairesExcel.setListDecompteSalaire(listeSalaires);
        listeSalairesExcel.addObserver(this);
        if (getConvention() != null) {
            listeSalairesExcel.setConvention(getConvention());
        }
        listeSalairesExcel.setTypeDecompte(getTypeDecompte());
        listeSalairesExcel.setAnnee(getAnnee());
        listeSalairesExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listeSalairesExcel.getOutputFile());
    }

    private void createFile(Deque<DecompteSalaire> listeSalaires) throws Exception {
        List<DecompteSalaire> decompteGroupeOrdreCumule = createListGroupeParTravailleurEmployeurPeriode(listeSalaires);

        String fileName = DocumentConstants.LISTES_SALAIRES_NAME + DocumentConstants.EXTENSION_TXT;

        CommunicationSalairesExporter.export(decompteGroupeOrdreCumule, Jade.getInstance().getPersistenceDir(),
                fileName);

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
                Montant montantTotal = Montant.ZERO;
                Date dateDebut = null;
                Date dateFin = null;

                for (DecompteSalaire salaireGroupe : salairesParTravailleurEtEmployeur.getValue()) {
                    if (salaireGroupe.getSalaireTotal() != Montant.ZERO) {

                        i = i + 1;
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

                        if (isPeriodeChevauchante(salaireGroupe.getPeriodeDebut(), salaireGroupe.getPeriodeFin(),
                                decompteSalairePrecedent.getPeriodeDebut(), decompteSalairePrecedent.getPeriodeFin())) {
                            dateFin = salaireGroupe.getPeriodeFin();
                            montantTotal = montantTotal.add(salaireGroupe.getSalaireTotal());
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
                            ajouterDecompte(decompteGroupeOrdreCumule, dateDebut, dateFin, montantTotal,
                                    decompteSalairePrecedent);
                        }
                    }
                }

            }
        }
        return decompteGroupeOrdreCumule;
    }

    private boolean isPeriodeChevauchante(Date periodeDebutActuelle, Date periodeFinActuelle,
            Date periodeDebutPrecedente, Date periodeFinPrecedente) {
        Periode periodeActuelle = new Periode(periodeDebutActuelle, periodeFinActuelle);
        Periode periodePrecedente = new Periode(periodeDebutPrecedente, periodeFinPrecedente);
        return periodeActuelle.chevaucheOuSuit(periodePrecedente);
    }

    private void ajouterDecompte(List<DecompteSalaire> decompteGroupeOrdreCumule, Date dateDebut, Date dateFin,
            Montant montant, DecompteSalaire salaireGroupe) {

        DecompteSalaire decompteSalaireAAjouter = new DecompteSalaire();
        decompteSalaireAAjouter.setPosteTravail(salaireGroupe.getPosteTravail());

        Periode periode = new Periode(dateDebut, dateFin);

        decompteSalaireAAjouter.setPeriode(periode);
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
