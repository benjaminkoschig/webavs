package globaz.perseus.vb.impotsource;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.Date;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSourceSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFListeImpotSourceViewBean extends BJadePersistentObjectViewBean {

    private String anneeGenerationListeCorrective = null;
    private String eMailAdresse = null;
    private String idPeriode = null;
    private String numeroDebiteur = null;
    private PeriodeImpotSource periode = null;
    private String periodeGenerationListeRecapitulative = "";
    private String typeListe = null;

    public PFListeImpotSourceViewBean() {
        super();
    }

    public PFListeImpotSourceViewBean(PeriodeImpotSource periode) {
        super();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub
    }

    public String getAnneeGenerationListeCorrective() {
        return anneeGenerationListeCorrective;
    }

    public String geteMailAdresse(String mail) {
        if (eMailAdresse == null) {
            eMailAdresse = mail;
        }
        return eMailAdresse;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdPeriode() {
        if (null != periode) {
            return periode.getId();
        }
        return idPeriode;
    }

    public String getNumeroDebiteur() {
        return numeroDebiteur;
    }

    public PeriodeImpotSource getPeriode() {
        return periode;
    }

    public String getPeriodeGenerationListeRecapitulative() {
        return periodeGenerationListeRecapitulative;
    }

    // TODO Récupération de la session user
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTypeListe() {
        return typeListe;
    }

    public void init() throws Exception {
        // TODO recuperer la propriété du numéro de débteur à fournir par la CCVD
        // afficher la liste recap ou corr
        numeroDebiteur = getSession().getApplication().getProperty("listeIP.numeroDebiteur");
        anneeGenerationListeCorrective = JadeDateUtil.addYears(JadeDateUtil.getGlobazFormattedDate(new Date()), -1)
                .substring(6);
        if (null != periode) {
            periodeGenerationListeRecapitulative = periode.getSimplePeriodeImpotSource().getDateDebut() + " - "
                    + periode.getSimplePeriodeImpotSource().getDateFin();
        }
    }

    @Override
    public void retrieve() throws Exception {
        PeriodeImpotSourceSearchModel periodeSearch = new PeriodeImpotSourceSearchModel();
        periodeSearch.setOrderKey(PeriodeImpotSourceSearchModel.ORDER_BY_DATE_DEBUT_ASC);
        periodeSearch.setPeriodeGeneree(false);

        periodeSearch = PerseusServiceLocator.getPeriodeImpotSourceService().search(periodeSearch);

        for (JadeAbstractModel model : periodeSearch.getSearchResults()) {
            setPeriode((PeriodeImpotSource) model);
            break;

        }
        init();
    }

    public void setAnneeGenerationListeCorrective(String anneeGenerationListeCorrective) {
        this.anneeGenerationListeCorrective = anneeGenerationListeCorrective;
    }

    public void seteMailAdresse(String eMailAdresse) {
        this.eMailAdresse = eMailAdresse;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub
    }

    public void setIdPeriode(String idPeriode) {
        this.idPeriode = idPeriode;
    }

    public void setNumeroDebiteur(String numeroDebiteur) {
        this.numeroDebiteur = numeroDebiteur;
    }

    public void setPeriode(PeriodeImpotSource periode) {
        this.periode = periode;
    }

    public void setPeriodeGenerationListeRecapitulative(String periodeGenerationListeRecapitulative) {
        this.periodeGenerationListeRecapitulative = periodeGenerationListeRecapitulative;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub
    }
}