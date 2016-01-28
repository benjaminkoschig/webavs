package globaz.vulpecula.vb.taxationoffice;

import globaz.globall.db.BSessionUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.passagefacturation.PassageFacturationServiceCRUD;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.businessimpl.services.taxationoffice.TaxationOfficeServiceImpl;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.LigneTaxationRepository;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.TaxationOfficeRepository;
import ch.globaz.vulpecula.util.I18NUtil;
import ch.globaz.vulpecula.web.servlet.PTTaxationOfficeAction;
import ch.globaz.vulpecula.web.views.taxationoffice.LigneTaxationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PTTaxationofficeViewBean extends DomainPersistentObjectViewBean<TaxationOffice> {

    // Objets métier
    private TaxationOffice taxationOffice;
    private List<LigneTaxation> lignesTaxation;

    // Représentation des lignes de taxation pour l'écran
    private List<LigneTaxationView> lignesTaxationViews;

    // Repositories
    private TaxationOfficeRepository taxationOfficeRepository;
    private LigneTaxationRepository ligneTaxationRepository;

    private TaxationOfficeService taxationOfficeService;

    private String idDecompte;

    private Gson gson = new Gson();

    public PTTaxationofficeViewBean() {
        taxationOffice = new TaxationOffice();
        lignesTaxationViews = new ArrayList<LigneTaxationView>();

        taxationOfficeRepository = VulpeculaRepositoryLocator.getTaxationOfficeRepository();
        ligneTaxationRepository = VulpeculaRepositoryLocator.getLigneTaxationRepository();

        taxationOfficeService = VulpeculaServiceLocator.getTaxationOfficeService();
    }

    @Override
    public TaxationOffice getEntity() {
        return taxationOffice;
    }

    @Override
    public void add() throws Exception {
        taxationOfficeRepository.create(taxationOffice);
    }

    @Override
    public void delete() throws Exception {
        taxationOfficeRepository.delete(taxationOffice);
    }

    @Override
    public void retrieve() throws Exception {
        taxationOffice = taxationOfficeRepository.findByIdDecompte(idDecompte);
        lignesTaxation = ligneTaxationRepository.findByIdTaxationOffice(taxationOffice.getId());
        taxationOffice.setLignes(lignesTaxation);

        for (LigneTaxation ligneTaxation : lignesTaxation) {
            LigneTaxationView ligneTaxationView = new LigneTaxationView();
            ligneTaxationView.setId(ligneTaxation.getId());
            ligneTaxationView.setCotisation(ligneTaxation.getAssuranceLibelle(I18NUtil.getUserLocale()));
            ligneTaxationView.setMasse(ligneTaxation.getMasseValue());
            ligneTaxationView.setTaux(ligneTaxation.getTaux().getValue());
            ligneTaxationView.setMontant(ligneTaxation.getMontantValue());
            lignesTaxationViews.add(ligneTaxationView);
        }
    }

    @Override
    public void update() throws Exception {
        taxationOffice.valide();
        taxationOfficeService.update(taxationOffice);
    }

    /**
     * Utilisé dans le {@link PTTaxationOfficeAction#beforeAfficher} pour setter le paramètre idDecompte.
     * 
     * @param idDecompte String représentant l'id décompte à rechercher
     */
    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public String getIdDecompte() {
        return idDecompte;
    }

    /**
     * Retourne le décompte sur la taxation d'office actuellement utilisée.
     * 
     * @return Le décompte ou null si inexistant
     */
    public Decompte getDecompte() {
        if (taxationOffice == null) {
            return null;
        }
        return taxationOffice.getDecompte();
    }

    /**
     * Retourne l'employeur auquel la taxation d'office est rattachée.
     * 
     * @return L'employeur ou null si inexistant
     */
    public Employeur getEmployeur() {
        if (taxationOffice == null) {
            return null;
        }
        return taxationOffice.getEmployeur();
    }

    /**
     * Retourne la taxation d'office
     * 
     * @return La taxation d'office
     */
    public TaxationOffice getTaxationOffice() {
        return taxationOffice;
    }

    /**
     * Retourne les lignes de taxations.
     * 
     * @return lignes de taxation
     */
    public List<LigneTaxationView> getLignesTaxationViews() {
        return lignesTaxationViews;
    }

    /**
     * Utilisé par reflection par le framework pour setter l'état lors de la validation.
     * 
     * @param etat
     */
    public void setEtat(String etat) {
        taxationOffice.setEtat(EtatTaxation.fromValue(etat));
    }

    public boolean isAnnulee() {
        return taxationOffice.isAnnulee();
    }

    public String getDesignationPassageFacturation() {
        return taxationOffice.getIdPassageFacturation();
    }

    public void setIdPassageFacturation(String idPassage) {
        taxationOffice.setIdPassageFacturation(idPassage);
    }

    public void setLignesTaxation(String lignes) {
        List<LigneTaxationView> newLignesTaxationView = gson.fromJson(lignes, new TypeToken<List<LigneTaxationView>>() {
        }.getType());
        for (LigneTaxationView newLigneTaxationView : newLignesTaxationView) {
            for (LigneTaxation ligneTaxation : lignesTaxation) {
                if (newLigneTaxationView.getId().equals(ligneTaxation.getId())) {
                    ligneTaxation.setId(newLigneTaxationView.getId());
                    ligneTaxation.setMasse(new Montant(newLigneTaxationView.getMasse()));
                    ligneTaxation.setMontant(new Montant(newLigneTaxationView.getMontant()));
                }
            }
        }

        // On set les nouvelles lignes de vues afin que la page puissent être reconstruites en cas d'erreurs
        lignesTaxationViews = newLignesTaxationView;
    }

    public String getEtatSaisi() {
        return EtatTaxation.SAISI.getValue();
    }

    public String getEtatValide() {
        return EtatTaxation.VALIDE.getValue();
    }

    public String getBoutonDevalideLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_BOUTON_DEVALIDE");
    }

    public String getboutonPrintLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_BOUTON_PRINT");
    }

    public String getTaxationOfficeService() {
        return TaxationOfficeService.class.getName();
    }

    public String getTaxationOfficeServiceImpl() {
        return TaxationOfficeServiceImpl.class.getName();
    }

    public String getPassageViewService() {
        return PassageFacturationServiceCRUD.class.getName();
    }
}
