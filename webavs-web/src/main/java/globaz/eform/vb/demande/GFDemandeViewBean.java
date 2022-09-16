package globaz.eform.vb.demande;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFDemandeViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFDemandeViewBean.class);

    GFDaDossierModel daDossier;

    private String lastName;
    private String firstName;
    public String birthday;

    public GFDemandeViewBean() {
        super();
        daDossier = new GFDaDossierModel();
    }

    public GFDemandeViewBean(GFDaDossierModel model) {
        super();
        this.daDossier = model;
    }

    public GFDaDossierModel getDaDossier() {
        return daDossier;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public String getNssAffilier() {
        String unformatNss = NSSUtils.unFormatNss(daDossier.getNssAffilier());

        if (!StringUtils.isBlank(unformatNss)) {
            PersonneEtendueSearchComplexModel ts = new PersonneEtendueSearchComplexModel();
            ts.setForNumeroAvsActuel(NSSUtils.formatNss(unformatNss));
            try {
                ts = TIBusinessServiceLocator.getPersonneEtendueService().find(ts);

                if (ts.getSize() == 1) {
                    PersonneEtendueComplexModel model = (PersonneEtendueComplexModel) ts.getSearchResults()[0];

                    lastName = model.getTiers().getDesignation1();
                    firstName = model.getTiers().getDesignation2();
                    birthday = model.getPersonne().getDateNaissance();
                }
            } catch (JadePersistenceException | JadeApplicationException e) {
                throw new RuntimeException(e);
            }
        }
        return unformatNss;
    }

    public void setNssAffilier(String nssAffilier) {
        daDossier.setNssAffilier(NSSUtils.unFormatNss(nssAffilier));
    }

    public String getLastName() {
        return lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getBirthday() {
        return birthday;
    }

    public String getCodeCaisse() {
        return daDossier.getCodeCaisse();
    }

    public void setCodeCaisse(String codeCaisse) {
        daDossier.setCodeCaisse(org.apache.commons.lang3.StringUtils.isBlank(codeCaisse) ? null : codeCaisse.split(" - ")[0]);
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {


    }

    public BSession getSession() {
        return (BSession) getISession();
    }
}
