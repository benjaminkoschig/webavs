package globaz.perseus.vb.qd;

import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.perseus.utils.PFUserHelper;
import globaz.pyxis.constantes.IConstantes;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import ch.globaz.perseus.business.constantes.CSEtatFacture;
import ch.globaz.perseus.business.constantes.CSVariableMetier;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.SimpleLot;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.qd.QD;
import ch.globaz.perseus.business.models.qd.SimpleFacture;
import ch.globaz.perseus.business.models.qd.SimpleQDAnnuelle;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.qd.FactureService;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

/**
 * Viewbean permettant l'affichage des détails d'une facture.
 * 
 * @author JSI
 * 
 */
public class PFDetailfactureViewBean extends BJadePersistentObjectViewBean {

    public static String TYPE_VERSEMENT_ASSURE = "versementAssure";

    private String adresseCourrier = null;
    private String adresseVersement = null;
    private String etatComptabilisation = "";
    private Facture facture = null;
    private boolean isPaiementOKPourValidation = true;
    private Boolean isRestitutionPossible = false;
    private Float montantMaxValidationUser;

    public PFDetailfactureViewBean() {
        super();
        facture = new Facture();
    }

    public PFDetailfactureViewBean(Facture facture) {
        this();
        this.facture = facture;
    }

    @Override
    public void add() throws Exception {
    }

    public void actionValider() throws Exception {
        retrieve();
        String etatFacture = getSimpleFacture().getCsEtat();
        FactureService factureService = PerseusServiceLocator.getFactureService();

        if (CSEtatFacture.ENREGISTRE.getCodeSystem().equals(etatFacture)) {
            facture = factureService.valider(facture);
        }
    }

    public void actionRestituer() throws Exception {
        retrieve();
        String etatFacture = getSimpleFacture().getCsEtat();
        FactureService factureService = PerseusServiceLocator.getFactureService();

        if (CSEtatFacture.VALIDE.getCodeSystem().equals(etatFacture)) {
            facture = factureService.restituer(facture);
        }
    }

    @Override
    public void delete() throws Exception {
        facture = PerseusServiceLocator.getFactureService().delete(facture);
    }

    public String getAdresseCourrier() {
        return adresseCourrier;
    }

    /**
     * @return the adresseVersement
     */
    public String getAdresseVersement() {
        return adresseVersement;
    }

    /**
     * Extrait le libellé correspondant à un code donné
     * 
     * @param le
     *            code dont il faut extraire le libellé
     * @return le libellé correspondant
     * @throws RemoteException
     */
    private String getCodeLibelle(String code) throws RemoteException {
        return getISession().getCodeLibelle(code);
    }

    public String getEtatComptabilisation() {
        return etatComptabilisation;
    }

    /**
     * @return the facture
     */
    public Facture getFacture() {
        return facture;
    }

    /**
     * Retourne si la facture possède une date de validation (true) ou non (false)
     * 
     * @return le flag boolean correspondant
     */
    public boolean getHasDateValidation() {
        String dateValidation = getSimpleFacture().getDateValidation();

        return (dateValidation != null) && (dateValidation.length() > 0);
    }

    @Override
    public String getId() {
        return facture.getId();
    }

    /**
     * @return the isRestitutionPossible
     */
    public Boolean getIsRestitutionPossible() {
        return isRestitutionPossible;
    }

    /**
     * @return Le tiers lié à la QD
     */
    public TiersSimpleModel getMemberFamilleTiers() {
        return getQd().getMembreFamille().getPersonneEtendue().getTiers();
    }

    /**
     * @return the montantMaxValidationUser
     */
    public Float getMontantMaxValidationUser() {
        return montantMaxValidationUser;
    }

    /**
     * @return La QD lié à la facture
     */
    public QD getQd() {
        return facture.getQd();
    }

    /**
     * @return La facture simple
     */
    public SimpleFacture getSimpleFacture() {
        return facture.getSimpleFacture();
    }

    /**
     * @return La QD annuelle liée à la facture
     */
    public SimpleQDAnnuelle getSimpleQDAnnuelle() {

        return getQd().getQdAnnuelle().getSimpleQDAnnuelle();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(facture.getSpy());
    }

    public boolean isPaiementOKPourValidation() {
        return isPaiementOKPourValidation;
    }

    @Override
    public void retrieve() throws Exception {
        facture = PerseusServiceLocator.getFactureService().read(getId());

        setPaiementOKPourValidation(PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise());

        adresseVersement = PFUserHelper.getAdressePaiementAssure(getSimpleFacture().getIdTiersAdressePaiement(),
                getSimpleFacture().getIdApplicationAdressePaiement(), JadeDateUtil.getGlobazFormattedDate(new Date()))
                .getAdresseFormate();

        adresseCourrier = PFUserHelper.getAdresseAssure(facture.getSimpleFacture().getIdTiersAdresseCourrier(),
                IConstantes.CS_AVOIR_ADRESSE_COURRIER, facture.getSimpleFacture().getIdApplicationAdresseCourrier(),
                JACalendar.todayJJsMMsAAAA()).getAdresseFormate();

        getISession().setAttribute(
                "likeNss",

                getQd().getQdAnnuelle().getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel());

        VariableMetier montantMaxValidationUserVM = PerseusServiceLocator.getVariableMetierService().getFromCS(
                CSVariableMetier.FACTURES_LIMITE_VALIDATION_GESTIONNAIRE.getCodeSystem(),
                Calendar.getInstance().getTime());

        montantMaxValidationUser = montantMaxValidationUserVM.getMontant();

        isRestitutionPossible = false;
        // Récupérer le lot contenant la facture et retourner son état, sa date si comptabilisé et le numéro du
        Lot lot = PerseusServiceLocator.getLotService().getLotForFacture(facture.getId());
        if (lot != null) {
            SimpleLot simpleLot = lot.getSimpleLot();

            if (!JadeStringUtil.isEmpty(simpleLot.getDateEnvoi())
                    && !CSEtatFacture.RESTITUE.getCodeSystem().equals(getSimpleFacture().getCsEtat())) {
                isRestitutionPossible = true;
            }
            // Récupère l'état du lot
            etatComptabilisation += getCodeLibelle(simpleLot.getEtatCs());
            etatComptabilisation += " (" + simpleLot.getDescription() + ")";
        } else {
            etatComptabilisation += "-";
        }
    }

    public void setAdresseCourrier(String adresseCourrier) {
        this.adresseCourrier = adresseCourrier;
    }

    /**
     * @param adresseVersement
     *            the adresseVersement to set
     */
    public void setAdresseVersement(String adresseVersement) {
        this.adresseVersement = adresseVersement;
    }

    /**
     * @param facture
     *            the facture to set
     */
    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public void setId(String newId) {
        facture.setId(newId);
    }

    /**
     * @param isRestitutionPossible
     *            the isRestitutionPossible to set
     */
    public void setIsRestitutionPossible(Boolean isRestitutionPossible) {
        this.isRestitutionPossible = isRestitutionPossible;
    }

    public void setIdFacture(String newId) {
        facture.setId(newId);
    }

    /**
     * @param montantMaxValidationUser
     *            the montantMaxValidationUser to set
     */
    public void setMontantMaxValidationUser(Float montantMaxValidationUser) {
        this.montantMaxValidationUser = montantMaxValidationUser;
    }

    public void setPaiementOKPourValidation(boolean isPaiementOKPourValidation) {
        this.isPaiementOKPourValidation = isPaiementOKPourValidation;
    }

    @Override
    public void update() throws Exception {
        String etatFacture = getSimpleFacture().getCsEtat();
        FactureService factureService = PerseusServiceLocator.getFactureService();

        // Modification utilisé pour valider la facture
        if (CSEtatFacture.ENREGISTRE.getCodeSystem().equals(etatFacture)) {
            facture = factureService.update(facture);
        }
    }

}
