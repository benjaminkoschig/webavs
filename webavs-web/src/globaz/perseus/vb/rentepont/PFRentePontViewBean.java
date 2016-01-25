/**
 * 
 */
package globaz.perseus.vb.rentepont;

import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author DDE
 * 
 */
public class PFRentePontViewBean extends BJadePersistentObjectViewBean {

    private String etatRentePont = null;
    private RentePont rentePont = null;

    public PFRentePontViewBean() {
        super();
        rentePont = new RentePont();
    }

    public PFRentePontViewBean(RentePont rentePont) {
        super();
        this.rentePont = rentePont;
    }

    @Override
    public void add() throws Exception {
        rentePont.getSimpleRentePont().setOnError(false);
        rentePont.getSimpleRentePont().setCsEtat(CSEtatRentePont.ENREGISTRE.getCodeSystem());
        rentePont = PerseusServiceLocator.getRentePontService().create(rentePont);
    }

    @Override
    public void delete() throws Exception {
        rentePont = PerseusServiceLocator.getRentePontService().delete(rentePont);

    }

    /**
     * Récupère l'adresse de paiement du bénéficiaire de la rente pont.
     * 
     * @return adresse de paiement formatée
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (JadeStringUtil.isEmpty(rentePont.getSimpleRentePont().getIdTiersAdressePaiement())
                || JadeStringUtil.isEmpty(rentePont.getSimpleRentePont().getIdDomaineApplicatifAdressePaiement())) {
            rentePont.getSimpleRentePont().setIdDomaineApplicatifAdressePaiement(IPFConstantes.CS_DOMAINE_ADRESSE);
            rentePont.getSimpleRentePont().setIdTiersAdressePaiement(
                    rentePont.getDossier().getDemandePrestation().getDemandePrestation().getIdTiers());
        }

        detailTiers = PFUserHelper.getAdressePaiementAssure(rentePont.getSimpleRentePont().getIdTiersAdressePaiement(),
                rentePont.getSimpleRentePont().getIdDomaineApplicatifAdressePaiement(), JACalendar.todayJJsMMsAAAA());
        return detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebutConverter() {
        return (!JadeStringUtil.isEmpty(rentePont.getSimpleRentePont().getDateDebut())) ? rentePont
                .getSimpleRentePont().getDateDebut().substring(3) : "";
    }

    /**
     * @return the dateFin
     */
    public String getDateFinConverter() {
        return (!JadeStringUtil.isEmpty(rentePont.getSimpleRentePont().getDateFin())) ? rentePont.getSimpleRentePont()
                .getDateFin().substring(3) : "";
    }

    public String getEtatRentePont() {
        return etatRentePont;
    }

    @Override
    public String getId() {
        return rentePont.getId();
    }

    /**
     * @return the rentePont
     */
    public RentePont getRentePont() {
        return rentePont;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(rentePont.getSpy());
    }

    public boolean hasDossierRentePontValidee() {
        try {
            return PerseusServiceLocator.getRentePontService().hasDossierRentePontValidee(
                    rentePont.getSimpleRentePont().getIdDossier());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void init() throws Exception {

    }

    @Override
    public void retrieve() throws Exception {
        rentePont = PerseusServiceLocator.getRentePontService().read(getId());
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebutConverter(String dateDebut) {
        if (!JadeStringUtil.isEmpty(dateDebut)) {
            if (JadeDateUtil.isGlobazDateMonthYear(dateDebut)) {
                dateDebut = "01." + dateDebut;
                rentePont.getSimpleRentePont().setDateDebut(dateDebut);
            } else {
                rentePont.getSimpleRentePont().setDateDebut(dateDebut);
            }
        } else {
            rentePont.getSimpleRentePont().setDateDebut(null);
        }
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFinConverter(String dateFin) {
        if (!JadeStringUtil.isEmpty(dateFin)) {
            if (JadeDateUtil.isGlobazDateMonthYear(dateFin)) {

                dateFin = "01." + dateFin;

                dateFin = JadeDateUtil.addMonths(dateFin, 1);
                dateFin = JadeDateUtil.addDays(dateFin, -1);
                rentePont.getSimpleRentePont().setDateFin(dateFin);
            } else {
                rentePont.getSimpleRentePont().setDateFin(dateFin);
            }

        } else {
            rentePont.getSimpleRentePont().setDateFin(null);
        }
    }

    public void setEtatRentePont(String etatRentePont) {
        this.etatRentePont = etatRentePont;
    }

    @Override
    public void setId(String newId) {
        rentePont.setId(newId);
    }

    /**
     * @param rentePont
     *            the rentePont to set
     */
    public void setRentePont(RentePont rentePont) {
        this.rentePont = rentePont;
    }

    @Override
    public void update() throws Exception {
        if (!CSEtatRentePont.VALIDE.getCodeSystem().equals(etatRentePont)) {
            rentePont = PerseusServiceLocator.getRentePontService().update(rentePont);
        } else {
            rentePont = PerseusServiceLocator.getRentePontService().validate(rentePont);
        }
    }

}
