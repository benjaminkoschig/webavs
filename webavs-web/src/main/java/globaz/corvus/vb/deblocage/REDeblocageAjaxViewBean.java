package globaz.corvus.vb.deblocage;

import globaz.corvus.db.deblocage.RELibererDevaliderDeblocage;
import globaz.corvus.db.lignedeblocage.RELigneDeblocage;
import globaz.corvus.db.lignedeblocage.ReLigneDeclocageServices;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageEtat;
import globaz.corvus.db.lignedeblocage.constantes.RELigneDeblocageType;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Iterator;
import ch.globaz.common.domaine.AdressePaiement;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.businessimpl.utils.adresse.AdresseHandler;
import ch.globaz.pyxis.business.model.AvoirPaiementSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class REDeblocageAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface,
        BIPersistentObject {

    private static final long serialVersionUID = 1L;
    private String action = null;
    private RELigneDeblocageViewBean lingeDeblocage = new RELigneDeblocageViewBean();
    private String idAvoirPaiementUnique;
    private String idRenteAccordee;
    private String debloquerRente;

    // utilisé pour la serialization en ajax
    private AdressePaiement adressePaiement = null;
    private String designationTiers1 = null;
    private String designationTiers2 = null;

    public RELigneDeblocageViewBean getLingeDeblocage() {
        return lingeDeblocage;
    }

    public void setLingeDeblocage(RELigneDeblocageViewBean lingeDeblocage) {
        this.lingeDeblocage = lingeDeblocage;
    }

    @Override
    public void add() throws Exception {
        if ("liberer".equals(action)) {
            RELibererDevaliderDeblocage libererDevalider = new RELibererDevaliderDeblocage((BSession) getISession());
            libererDevalider.liberer(Long.valueOf(idRenteAccordee));
            if ("true".equalsIgnoreCase(debloquerRente)) {
                REPrestationsAccordees accordee = new REPrestationsAccordees();
                accordee.setSession((BSession) getISession());
                accordee.setId(idRenteAccordee);
                accordee.retrieve();
                // accordee.setIsAttenteMajBlocage(false);
                accordee.setIsPrestationBloquee(false);
                accordee.update();
            }
        } else if ("devalider".equals(action)) {
            RELibererDevaliderDeblocage libererDevalider = new RELibererDevaliderDeblocage((BSession) getISession());
            libererDevalider.devalider(Long.valueOf(idRenteAccordee));
        } else {
            RELigneDeblocage ligneDeblocage = toReLigneDeblocage(lingeDeblocage);
            ReLigneDeclocageServices services = new ReLigneDeclocageServices((BSession) getISession());

            lingeDeblocage = fromReLigneDeblocage(services.add(ligneDeblocage));
            readAdresse(ligneDeblocage);
        }
    }

    @Override
    public void update() throws Exception {
        RELigneDeblocage ligneDeblocage = toReLigneDeblocage(lingeDeblocage);
        ReLigneDeclocageServices services = new ReLigneDeclocageServices((BSession) getISession());
        if (ligneDeblocage.getMontant().isZero() && !ligneDeblocage.isCreancier()) {
            services.delete(ligneDeblocage);
            lingeDeblocage.setIdLigneDeblocage(null);
            lingeDeblocage.setSpy(null);
        } else {
            lingeDeblocage = fromReLigneDeblocage(services.update(ligneDeblocage));
        }
    }

    @Override
    public void delete() throws Exception {
        ReLigneDeclocageServices services = new ReLigneDeclocageServices((BSession) getISession());
        retrieve();

        RELigneDeblocage ligneDeblocage = toReLigneDeblocage(lingeDeblocage);
        if (ligneDeblocage.isCreancier()) {
            services.delete(ligneDeblocage);
        } else {
            lingeDeblocage = fromReLigneDeblocage(services.update(ligneDeblocage));
        }
    }

    @Override
    public void retrieve() throws Exception {
        if (getCurrentEntity() != null) {
            ReLigneDeclocageServices services = new ReLigneDeclocageServices((BSession) getISession());
            lingeDeblocage = fromReLigneDeblocage(services.read(lingeDeblocage.getIdLigneDeblocage()));
        }
    }

    private void readAdresse(RELigneDeblocage ligneDeblocage) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        if (!JadeStringUtil.isEmpty(idAvoirPaiementUnique)) {

            AvoirPaiementSimpleModel avoirPaiementSimpleModel = new AvoirPaiementSimpleModel();
            avoirPaiementSimpleModel.setId(idAvoirPaiementUnique);
            avoirPaiementSimpleModel = (AvoirPaiementSimpleModel) JadePersistenceManager.read(avoirPaiementSimpleModel);
            ligneDeblocage.setIdApplicationAdressePaiement(Long.valueOf(avoirPaiementSimpleModel.getIdApplication()));
            ligneDeblocage.setIdTiersAdressePaiement(Long.valueOf(avoirPaiementSimpleModel.getIdTiers()));

            adressePaiement = AdresseHandler.convertAdressePaiement(TIBusinessServiceLocator.getAdresseService()
                    .getAdressePaiementTiers(ligneDeblocage.getIdTiersAdressePaiement().toString(), false,
                            ligneDeblocage.getIdApplicationAdressePaiement().toString(), JACalendar.todayJJsMMsAAAA(),
                            avoirPaiementSimpleModel.getIdExterne()));

            TiersSimpleModel tiersCreancier = TIBusinessServiceLocator.getTiersService().read(
                    ligneDeblocage.getIdTiersCreancier().toString());

            designationTiers1 = tiersCreancier.getDesignation1();
            designationTiers2 = tiersCreancier.getDesignation2();
        }
    }

    private RELigneDeblocage toReLigneDeblocage(RELigneDeblocageViewBean vb) {
        RELigneDeblocage reLigneDeblocage = new RELigneDeblocage();
        reLigneDeblocage.setIdApplicationAdressePaiement(vb.getIdApplicationAdressePaiement());
        reLigneDeblocage.setIdEntity(vb.getIdLigneDeblocage());
        reLigneDeblocage.setIdRenteAccordee(vb.getIdRenteAccordee());
        reLigneDeblocage.setIdRoleSection(vb.getIdRoleSection());
        reLigneDeblocage.setIdSectionCompensee(vb.getIdSectionCompensee());
        reLigneDeblocage.setIdTiersAdressePaiement(vb.getIdTiersAdressePaiement());
        reLigneDeblocage.setIdTiersCreancier(vb.getIdTiersCreancier());
        reLigneDeblocage.setMontant(new Montant(vb.getMontant()));
        reLigneDeblocage.setEtat(RELigneDeblocageEtat.ENREGISTRE);
        reLigneDeblocage.setRefPaiement(vb.getRefPaiement());
        reLigneDeblocage.setType(RELigneDeblocageType.fromValue(vb.getTypeDeblocage()));
        if (vb.getSpy() != null) {
            reLigneDeblocage.setSpy(vb.getSpy());
        }
        return reLigneDeblocage;
    }

    private RELigneDeblocageViewBean fromReLigneDeblocage(RELigneDeblocage entity) {
        RELigneDeblocageViewBean vb = new RELigneDeblocageViewBean();
        if (entity.getIdApplicationAdressePaiement() != null) {
            vb.setIdApplicationAdressePaiement(entity.getIdApplicationAdressePaiement());
        }
        if (entity.getId() != null) {
            vb.setIdLigneDeblocage(entity.getId());
        }
        if (entity.getIdRenteAccordee() != null) {
            vb.setIdRenteAccordee(entity.getIdRenteAccordee());
        }
        if (entity.getIdRoleSection() != null) {
            vb.setIdRoleSection(entity.getIdRoleSection());
        }
        if (entity.getIdSectionCompensee() != null) {
            vb.setIdSectionCompensee(entity.getIdSectionCompensee());
        }
        if (entity.getIdTiersAdressePaiement() != null) {
            vb.setIdTiersAdressePaiement(entity.getIdTiersAdressePaiement());
        }
        if (entity.getIdTiersCreancier() != null) {
            vb.setIdTiersCreancier(entity.getIdTiersCreancier());
        }
        vb.setMontant(entity.getMontant().toStringFormat());
        vb.setEtatDeblocage(entity.getEtat().getValue());
        vb.setTypeDeblocage(entity.getType().getValue());
        vb.setRefPaiement(entity.getRefPaiement());
        if (entity.getSpy() != null) {
            vb.setSpy(entity.getSpy().toString());
        }
        return vb;
    }

    public void setCurrentEntity(RELigneDeblocageViewBean entite) {
        lingeDeblocage = entite;
    }

    public RELigneDeblocageViewBean getCurrentEntity() {
        return lingeDeblocage;
    }

    @Override
    public String getId() {
        return getCurrentEntity() != null ? getCurrentEntity().getIdLigneDeblocage() : null;
    }

    @Override
    public void setId(String newId) {
        if (getCurrentEntity() != null) {
            getCurrentEntity().setIdLigneDeblocage(newId);
        }
    }

    public String getIdAvoirPaiementUnique() {
        return idAvoirPaiementUnique;
    }

    public void setIdAvoirPaiementUnique(String idAvoirPaiementUnique) {
        this.idAvoirPaiementUnique = idAvoirPaiementUnique;
    }

    public AdressePaiement getAdressePaiement() {
        return adressePaiement;
    }

    public String getDesignationTiers1() {
        return designationTiers1;
    }

    public String getDesignationTiers2() {
        return designationTiers2;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void setGetListe(boolean getListe) {
        // rien, pas de liste
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        // rien, pas de liste
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public String getDebloquerRente() {
        return debloquerRente;
    }

    public void setDebloquerRente(String debloquerRente) {
        this.debloquerRente = debloquerRente;
    }

}
