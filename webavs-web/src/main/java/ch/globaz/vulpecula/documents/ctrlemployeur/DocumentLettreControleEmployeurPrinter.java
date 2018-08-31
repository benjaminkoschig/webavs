package ch.globaz.vulpecula.documents.ctrlemployeur;

import globaz.framework.printing.itext.FWIDocumentManager;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.ctrlemployeur.LettreControle;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

public class DocumentLettreControleEmployeurPrinter extends DocumentPrinter<LettreControle> {
    private static final long serialVersionUID = 7451949163803279932L;

    private LettreControle lettre;

    public DocumentLettreControleEmployeurPrinter() {
        super();
    }

    public DocumentLettreControleEmployeurPrinter(LettreControle lettreControle) {
        lettre = lettreControle;
    }

    public LettreControle getLettre() {
        return lettre;
    }

    public void setLettre(LettreControle lettre) {
        this.lettre = lettre;
    }

    @Override
    public String getNumeroInforom() {
        return (isAVS() ? DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_AVS_TYPE_NUMBER
                : DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_TYPE_NUMBER);
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        getCurrentElement().setAVS(isAVS());
        return new DocumentLettreControleEmployeur(getCurrentElement(), isAVS());
    }

    @Override
    protected String getEMailObject() {
        return (isAVS() ? DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_AVS_SUBJECT
                : DocumentConstants.LETTRE_CONTROLE_EMPLOYEUR_SUBJECT);
    }

    @Override
    public void retrieve() {
        List<LettreControle> lettres = new ArrayList<LettreControle>();
        lettre.setEmployeur(VulpeculaRepositoryLocator.getEmployeurRepository().findById(lettre.getIdEmployeur()));
        loadAdresseAndCotisations(lettre.getEmployeur());
        lettres.add(lettre);
        setElements(lettres);
    }

    private void loadAdresseAndCotisations(Employeur employeur) {
        if (employeur.getAdressePrincipale() == null) {
            employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(employeur.getIdTiers()));
        }
        employeur.setCotisations(VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(
                employeur.getId(), new Annee(lettre.getAnneeDebut()).getFirstDayOfYear(),
                new Annee(lettre.getAnneeFin()).getLastDayOfYear()));
    }

    private boolean isAVS() {
        boolean bool = false;
        try {
            bool = lettre.getEmployeur().isSoumisAVSPlus1JourPourPeriode(
                    new Periode(new Annee(lettre.getAnneeDebut()).getFirstDayOfYear(), new Annee(lettre.getAnneeFin())
                            .getLastDayOfYear()));
        } catch (IllegalArgumentException e) {
            bool = false;
        }
        return bool;
    }
}
