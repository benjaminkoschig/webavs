/**
 * 
 */
package globaz.vulpecula.vb.postetravaildetail;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.travailleur.TravailleurServiceCRUD;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PermisTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.pyxis.PersonneEtendue;

/**
 * @author JPA
 * 
 */
public class PTTravailleurdetailViewBean extends DomainPersistentObjectViewBean<Travailleur> {

    private Travailleur travailleur = new Travailleur();

    public PTTravailleurdetailViewBean() {
        super();
        travailleur = new Travailleur();
    }

    @Override
    public void add() throws Exception {
        PersonneEtendue tiers = VulpeculaRepositoryLocator.getPersonneEtendueRepository().findById(
                travailleur.getIdTiers());
        if (!JadeStringUtil.isEmpty(tiers.getDateNaissance())) {
            travailleur = VulpeculaServiceLocator.getTravailleurService().create(travailleur);
        } else {
            throw new IllegalStateException(BSessionUtil.getSessionFromThreadContext().getLabel(
                    "TIERS_DATE_NAISSANCE_ABSENT"));
        }
    }

    @Override
    public void delete() throws Exception {
        VulpeculaServiceLocator.getTravailleurService().delete(travailleur);
    }

    public String getDateAnnonceMeroba() {
        if ((travailleur != null) && (travailleur.getDateAnnonceMeroba() != null)) {
            return travailleur.getDateAnnonceMeroba().getSwissValue();
        }
        return "";
    }

    public String getIdTiers() {
        if (travailleur != null) {
            return travailleur.getIdTiers();
        }
        return null;
    }

    public String getIsAnnonceMeroba() {
        if ((travailleur != null) && (travailleur.getAnnonceMeroba() != null)) {
            if (travailleur.getAnnonceMeroba()) {
                return "checked=\"checked\"";
            }
        }
        return "";
    }

    public String getNomPrenomTravailleur() {
        if (travailleur != null) {
            if (travailleur.getDesignation1() != null) {
                if (travailleur.getDesignation2() != null) {
                    return travailleur.getDesignation1() + " " + travailleur.getDesignation2();
                }
                return travailleur.getDesignation1();
            }
        }
        return "";
    }

    public String getPermisTravail() {
        if ((travailleur != null) && (travailleur.getPermisTravail() != null)) {
            return travailleur.getPermisTravail().getValue();
        }
        return "";
    }

    public String getReferencePermis() {
        return travailleur.getReferencePermis();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(travailleur.getSpy());
    }

    public Travailleur getTravailleur() {
        return travailleur;
    }

    @Override
    public void retrieve() throws Exception {
        travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(travailleur.getId());
    }

    public void setAnnonceMeroba(final boolean annonceMeroba) {
        travailleur.setAnnonceMeroba(annonceMeroba);
    }

    public void setDateAnnonceMeroba(final String dateAnnonceMeroba) {
        if (Date.isValid(dateAnnonceMeroba)) {
            travailleur.setDateAnnonceMeroba(new Date(dateAnnonceMeroba));
        }
    }

    public void setIdTiers(final String idTiers) {
        travailleur.setIdTiers(idTiers);
    }

    public void setPermisTravail(final String permisTravail) {
        if (JadeStringUtil.isEmpty(permisTravail)) {
            travailleur.setPermisTravail((PermisTravail) null);
        } else {
            travailleur.setPermisTravail(permisTravail);
        }
    }

    public void setReferencePermis(final String referencePermis) {
        travailleur.setReferencePermis(referencePermis);
    }

    public void setTravailleur(final Travailleur travailleur) {
        this.travailleur = travailleur;
    }

    public String getTravailleurServiceCRUD() {
        return TravailleurServiceCRUD.class.getName();
    }

    @Override
    public void update() throws Exception {
        VulpeculaRepositoryLocator.getTravailleurRepository().update(travailleur);
    }

    @Override
    public Travailleur getEntity() {
        return travailleur;
    }
}
