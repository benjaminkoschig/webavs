package globaz.vulpecula.vb.decompte;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.businessimpl.services.decompte.DecompteServiceImpl;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.web.views.decompte.DecompteViewService;

/**
 * ViewBean utilisé dans la page JSP "decompte_de.jsp"
 * 
 * @since Web@BMS 0.01.01
 */
public class PTDecompteViewBean extends BJadeSearchObjectELViewBean {
    public class SearchModel {
        private String forId;
        private String forNoDecompte;
        private String forIdConvention;
        private String likeNoAffilie;
        private Date forDateDe;
        private Date forDateAu;
        private String likeRaisonSocialeUpper;
        private Date forDateReception;
        private String inEtats;
        private String forEtatTaxation;
        private String forType;
        private String forTypeProvenance;

        public final String getForId() {
            return forId;
        }

        public final void setForId(String forId) {
            this.forId = forId;
        }

        public final String getForNoDecompte() {
            return forNoDecompte;
        }

        public final void setForNoDecompte(String forNoDecompte) {
            this.forNoDecompte = forNoDecompte;
        }

        public final String getForIdConvention() {
            return forIdConvention;
        }

        public final void setForIdConvention(String forIdConvention) {
            this.forIdConvention = forIdConvention;
        }

        public final String getLikeNoAffilie() {
            return likeNoAffilie;
        }

        public final void setLikeNoAffilie(String likeNoAffilie) {
            this.likeNoAffilie = likeNoAffilie;
        }

        public final String getForDateDe() {
            if (forDateDe != null) {
                return forDateDe.getMoisAnneeFormatte();
            } else {
                return null;
            }
        }

        public final void setForDateDe(String forDateDe) {
            if (!JadeStringUtil.isEmpty(forDateDe)) {
                this.forDateDe = new Date(forDateDe);
            }
        }

        public final String getForDateAu() {
            if (forDateAu != null) {
                return forDateAu.getMoisAnneeFormatte();
            } else {
                return null;
            }
        }

        public final void setForDateAu(String forDateAu) {
            if (!JadeStringUtil.isEmpty(forDateAu)) {
                this.forDateAu = new Date(forDateAu);
            }
        }

        public final String getLikeRaisonSocialeUpper() {
            return likeRaisonSocialeUpper;
        }

        public final void setLikeRaisonSocialeUpper(String likeRaisonSocialeUpper) {
            this.likeRaisonSocialeUpper = likeRaisonSocialeUpper;
        }

        public final String getForDateReception() {
            if (forDateReception != null) {
                return forDateReception.getSwissValue();
            } else {
                return null;
            }
        }

        public final void setForDateReception(String forDateReception) {
            if (!JadeStringUtil.isEmpty(forDateReception)) {
                this.forDateReception = new Date(forDateReception);
            }
        }

        public final String getInEtats() {
            return inEtats;
        }

        public final void setInEtats(String inEtats) {
            this.inEtats = inEtats;
        }

        public final String getForEtatTaxation() {
            return forEtatTaxation;
        }

        public final void setForEtatTaxation(String forEtatTaxation) {
            this.forEtatTaxation = forEtatTaxation;
        }

        public final String getForType() {
            return forType;
        }

        public final void setForType(String forType) {
            this.forType = forType;
        }

        /**
         * @return the forTypeProvenance
         */
        public String getForTypeProvenance() {
            return forTypeProvenance;
        }

        /**
         * @param forTypeProvenance the forTypeProvenance to set
         */
        public void setForTypeProvenance(String forTypeProvenance) {
            this.forTypeProvenance = forTypeProvenance;
        }
    }

    private List<Convention> conventions;
    private SearchModel searchModel = new SearchModel();

    @Override
    public void retrieve() throws Exception {
        conventions = VulpeculaRepositoryLocator.getConventionRepository().findAll();
    }

    public List<Convention> getConventions() {
        return conventions;
    }

    public List<String> getEtats() {
        return EtatDecompte.getList();
    }

    public String getEtatsEnSuspens() {
        return EtatDecompte.ERREUR.getValue() + "," + EtatDecompte.RECEPTIONNE.getValue() + ","
                + EtatDecompte.OUVERT.getValue() + "," + EtatDecompte.GENERE.getValue() + ","
                + EtatDecompte.A_TRAITER.getValue();
    }

    public String getCsTaxationOffice() {
        return EtatDecompte.TAXATION_DOFFICE.getValue();
    }

    public List<String> getTypesTaxationOffice() {
        return EtatTaxation.getList();
    }

    public List<String> getTypes() {
        return TypeDecompte.getList();
    }

    public List<String> getTypesProvenance() {
        return TypeProvenance.getList();
    }

    public String getDecompteService() {
        return DecompteServiceImpl.class.getName();
    }

    public String getDecompteViewService() {
        return DecompteViewService.class.getName();
    }

    public String getLibelleBoutonSommation() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LIBELLE_BOUTON_SOMMATION");
    }

    public String getLibelleConfirmeSommation() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("LIBELLE_CONFIRME_SOMMATION");
    }

    String protege = null;

    public String getProtege() {
        return protege;
    }

    public String getProtegeLibelle() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("PAS_DROIT_DECOMPTE");
    }

    public String getLibelleImpressionDemarree() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_IMPRESSION_DEMARREE");
    }

    public void setProtege(String protege) {
        this.protege = protege;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }
}
