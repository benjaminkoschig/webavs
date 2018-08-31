package globaz.vulpecula.vb.decomptedetail;

import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.hercule.db.controleEmployeur.CEAffilieForAttrPts;
import globaz.hercule.db.controleEmployeur.CEAffilieForAttrPtsManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.businessimpl.services.decompte.DecompteServiceImpl;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DifferenceControle;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.MotifProlongation;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.external.models.hercule.InteretsMoratoires;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteSalaireRepositoryJade;
import ch.globaz.vulpecula.services.PTAFServices;
import ch.globaz.vulpecula.web.views.decompte.DecompteViewService;

/**
 * ViewBean utilisé dans la page JSP "decompte_de.jsp"
 * 
 * @since Web@BMS 0.01.01
 */
public class PTDecomptedetailViewBean extends DomainPersistentObjectViewBean<Decompte> {

    private Decompte decompte;
    private List<HistoriqueDecompte> historiqueDecompte;
    private TaxationOffice taxationOffice;
    private boolean pasDeControle;

    private boolean isEdition = false;

    private final static String ETAT_ANNULE = EtatDecompte.ANNULE.getValue();
    private final static String ETAT_COMPTABILISE = EtatDecompte.COMPTABILISE.getValue();
    private final static String ETAT_ERREUR = EtatDecompte.ERREUR.getValue();
    private final static String ETAT_GENERE = EtatDecompte.GENERE.getValue();
    private final static String ETAT_OUVERT = EtatDecompte.OUVERT.getValue();
    private final static String ETAT_RECEPTIONNE = EtatDecompte.RECEPTIONNE.getValue();
    private final static String ETAT_RECTIFIE = EtatDecompte.RECTIFIE.getValue();
    private final static String ETAT_TAXATION_DOFFICE = EtatDecompte.TAXATION_DOFFICE.getValue();
    private final static String ETAT_VALIDE = EtatDecompte.VALIDE.getValue();

    /**
     * Définit si le décompte doit être contrôler au réaffichage de la page.
     */
    private boolean controler;

    private DecompteService decompteService = VulpeculaServiceLocator.getDecompteService();

    private DifferenceControle differenceControle = DifferenceControle.VALIDE;

    public PTDecomptedetailViewBean() {
        decompte = new Decompte();
        historiqueDecompte = new ArrayList<HistoriqueDecompte>();
        taxationOffice = new TaxationOffice();
    }

    @Override
    public void add() throws Exception {
        // Not used !
    }

    @Override
    public void delete() throws ViewException {
        VulpeculaRepositoryLocator.getDecompteRepository().delete(decompte);
    }

    public String getBoutonModifierDialogLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DIALOG_BOUTON_MODIFIER");
    }

    public String getBoutonRectifierDialogLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DIALOG_BOUTON_RECTIFIER");
    }

    public String getDialogDevaliderLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DIALOG_DEVALIDER");
    }

    public String getDialogAnnulerLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_DIALOG_ANNULER");
    }

    public String getMessageSuppression() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_MESSAGE_SUPPRESSION");
    }

    public String getControleEmployeurService() {
        return CEAffilieForAttrPtsManager.class.getName();
    }

    /**
     * Retourne la liste des controles d'employeur
     * 
     * @return une liste des controles d'employeur pour un employeur
     * @throws Exception
     */
    public List<CEAffilieForAttrPts> getControlesEmployeur() throws Exception {
        if (decompte.getIdEmployeur() == null) {
            return new ArrayList<CEAffilieForAttrPts>();
        }

        List<CEAffilieForAttrPts> trucs = new ArrayList<CEAffilieForAttrPts>();

        CEAffilieForAttrPtsManager ceAffilieForAttrPtsManager = new CEAffilieForAttrPtsManager();
        ceAffilieForAttrPtsManager.setForIdAffiliation(String.valueOf(decompte.getIdEmployeur()));
        ceAffilieForAttrPtsManager.find(BManager.SIZE_NOLIMIT);

        for (Object bmanager : ceAffilieForAttrPtsManager.getContainer()) {
            CEAffilieForAttrPts ceAffilieForAttrPts = (CEAffilieForAttrPts) bmanager;
            trucs.add(ceAffilieForAttrPts);
        }

        return trucs;
    }

    /**
     * @return le code système du type de décompte
     */
    public String getCsTypeDecompte() {
        if (decompte.getType() != null) {
            return decompte.getType().getValue();
        }
        return null;
    }

    /**
     * @return
     */
    public String getDateDecompte() {
        return decompte.getDateEtablissementAsSwissValue();
    }

    /**
     * @return la date de réception au format dd.MM.yyyy
     */
    public String getDateReception() {
        return decompte.getDateReceptionAsSwissValue();
    }

    public String getDateRappel() {
        return decompte.getDateRappel().getSwissValue();
    }

    public Decompte getDecompte() {
        return decompte;
    }

    public String getDecompteService() {
        return DecompteServiceImpl.class.getName();
    }

    public String getDecompteViewService() {
        return DecompteViewService.class.getName();
    }

    /**
     * @return
     */
    public Employeur getEmployeur() {
        if (JadeStringUtil.isBlank(decompte.getEmployeur().getSpy())) {
            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(
                    decompte.getEmployeur().getId());
            decompte.setEmployeur(employeur);
        }

        return decompte.getEmployeur();
    }

    public boolean isEbusiness() {
        return getEmployeur().isEBusiness();
    }

    @Override
    public Decompte getEntity() {
        return decompte;
    }

    public String getEtat() {
        return decompte.getEtat().getValue();
    }

    public List<HistoriqueDecompte> getHistoriqueDecompte() {
        return historiqueDecompte;
    }

    public String getLigneDecompteService() {
        return DecompteSalaireRepositoryJade.class.getName();
    }

    /**
     * @return
     */
    public String getMontantControle() {
        if (decompte.getMontantControle() != null) {
            return decompte.getMontantControle().toStringFormat();
        }
        return null;
    }

    /**
     * @return
     */
    public String getNumeroDecompte() {
        if (decompte.getNumeroDecompte() != null) {
            return decompte.getNumeroDecompte().toString();
        }
        return null;
    }

    /**
     * @return
     */
    public String getPeriodeAu() {
        if (decompte.getPeriode() != null) {
            return decompte.getPeriode().getPeriodeFinFormat();
        }
        return null;
    }

    /**
     * @return
     */
    public String getPeriodeDu() {
        if (decompte.getPeriode() != null) {
            return decompte.getPeriode().getPeriodeDebutFormat();
        }
        return null;
    }

    public String getMotifProlongation() {
        return decompte.getMotifProlongation().getValue();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(decompte.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(getDecompte().getId());
        historiqueDecompte = VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().findByIdDecompte(
                decompte.getId());
        taxationOffice = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findByIdDecompte(decompte.getId());
    }

    /**
     * @return la date de réception au format dd.MM.yyyy
     */
    public void setDateReception(final String dateReception) {
        if (dateReception != null && dateReception.length() > 0) {
            decompte.setDateReception(new Date(dateReception));
        } else {
            decompte.setDateReception(null);
        }
    }

    public void setDateRappel(final String dateRappel) {
        if (dateRappel != null && dateRappel.length() > 0) {
            decompte.setDateRappel(new Date(dateRappel));
        }
    }

    public void setHistoriqueDecompte(final List<HistoriqueDecompte> historiqueDecompte) {
        this.historiqueDecompte = historiqueDecompte;
    }

    public void setIdControle(final String idControle) {
        decompte.setIdRapportControle(idControle);
    }

    /**
     * @param idEmployeur
     */
    public void setIdEmployeur(final String idEmployeur) {
        Employeur employeur = new Employeur();
        employeur.setId(idEmployeur);
        decompte.setEmployeur(employeur);
    }

    public void setInteretsMoratoires(final String interetsMoratoires) {
        if (interetsMoratoires != null && interetsMoratoires.length() > 0) {
            decompte.setInteretsMoratoires(InteretsMoratoires.fromValue(interetsMoratoires));
        }
    }

    /**
     * @param montant
     */
    public void setMontantControle(final String montant) {
        if (montant != null && montant.length() > 0) {
            decompte.setMontantVerse(new Montant(montant));
        }
    }

    public void setMotifProlongation(final String motifProlongation) {
        if (motifProlongation != null && motifProlongation.length() > 0) {
            decompte.setMotifProlongation(MotifProlongation.fromValue(motifProlongation));
        } else {
            decompte.setMotifProlongation(MotifProlongation.fromValue("0"));
        }
    }

    @Override
    public void update() throws ViewException, JadePersistenceException {
        if (controler) {
            if (decompte.getDateReception() == null) {
                throw new ViewException(SpecificationMessage.DECOMPTE_DATE_RECEPTION_INEXISTANTE);
            }
        }
        // On stocke le spy que l'on réassignera dans le cas où une étape dans le processus ne fonctionne pas
        // correctement.
        String oldSpy = decompte.getSpy();
        // dans cet ordre, parce que le spy est changé lors de la réception du décompte
        try {
            decompte = decompteService.update(decompte);
        } catch (UnsatisfiedSpecificationException e) {
            throw new ViewException(e);
        }

        if (EtatDecompte.RECEPTIONNE.equals(decompte.getEtat())
                && (decompte.getDateReception() == null || "0".equals(decompte.getDateReception()))) {
            decompteService.reinitialiseDecompte(decompte);
        } else if (decompte.getDateReception() != null && decompte.isReceptionnable()) {
            decompteService.receptionner(decompte.getId(), decompte.getDateReception());
        }
        // Si c'est le bouton contrôler, on effectue un contrôle
        if (controler) {
            differenceControle = decompteService.controler(decompte.getId(), isPasDeControle());
            if (differenceControle.isValid()) {
                boolean result = PTAFServices.genererPrestationsAFPeriodique(getEmployeur(), decompte.getPeriode(),
                        decompte.getType());
                if (!result) {
                    decompte.setSpy(oldSpy);
                }
            }
        }
    }

    public String getETAT_ANNULE() {
        return ETAT_ANNULE;
    }

    public String getETAT_COMPTABILISE() {
        return ETAT_COMPTABILISE;
    }

    public String getETAT_ERREUR() {
        return ETAT_ERREUR;
    }

    public String getETAT_GENERE() {
        return ETAT_GENERE;
    }

    public String getETAT_OUVERT() {
        return ETAT_OUVERT;
    }

    public String getETAT_RECEPTIONNE() {
        return ETAT_RECEPTIONNE;
    }

    public String getETAT_RECTIFIE() {
        return ETAT_RECTIFIE;
    }

    public String getETAT_TAXATION_DOFFICE() {
        return ETAT_TAXATION_DOFFICE;
    }

    public String getETAT_VALIDE() {
        return ETAT_VALIDE;
    }

    /**
     * Définit si l'on souhaite directement apparaître en mode edition
     * 
     * @param value true si mode edition
     * @return La valeur du mode
     */
    public void setEdition(boolean isEdition) {
        this.isEdition = isEdition;
    }

    /**
     * Retourne on est en mode édition
     * 
     * @return true si édition
     */
    public boolean isEdition() {
        return isEdition;
    }

    public boolean getControleAC2() {
        return decompte.getControleAC2();
    }

    public boolean isControleAC2() {
        return decompte.getControleAC2();
    }

    public void setControleAC2(boolean controleAC2) {
        decompte.setControleAC2(controleAC2);
    }

    public void setControler(boolean controler) {
        this.controler = controler;
    }

    public boolean isControler() {
        return controler;
    }

    public boolean isEchecControler() {
        return !differenceControle.isValid();
    }

    public boolean isEchecEBusinessControler() {
        return differenceControle.isErreurEBusiness();
    }

    public DifferenceControle getDifferenceControle() {
        return differenceControle;
    }

    public void setDifferenceControle(DifferenceControle differenceControle) {
        this.differenceControle = differenceControle;
    }

    /**
     * @return the pasDeControle
     */
    public boolean isPasDeControle() {
        return pasDeControle;
    }

    /**
     * Pour determiner s'il faut ou non cocher la case "PasDeControle"
     */
    public boolean isPasDeControleDisplay() {
        return decompte.getMontantControle().intValue() == -1;
    }

    /**
     * @param pasDeControle the pasDeControle to set
     */
    public void setPasDeControle(boolean pasDeControle) {
        this.pasDeControle = pasDeControle;
    }

    /**
     * @return les types d'interets à ne pas afficher dans la liste déroulante
     */
    public HashSet<String> getExceptionInteret() {
        HashSet<String> exceptMode = new HashSet<String>();
        exceptMode.add(InteretsMoratoires.A_CONTROLER.getValue());
        exceptMode.add(InteretsMoratoires.MANUEL.getValue());

        return exceptMode;
    }

    /**
     * @return type de décompte
     */
    public boolean isPeriodique() {
        return getDecompte().isPeriodique();
    }

    public TaxationOffice getTaxationOffice() {
        return taxationOffice;
    }

}