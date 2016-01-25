package ch.globaz.vulpecula.process.congepaye;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.congepaye.CompteurRepository;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class BouclementProcess extends BProcessWithContext {
    private static final long serialVersionUID = 693969594905635256L;

    private List<Compteur> compteurs;
    private Map<Convention, Collection<Compteur>> compteursByConvention;

    private CompteurRepository compteurRepository = VulpeculaRepositoryLocator.getCompteurRepository();

    private String idConvention;
    private Annee annee;
    private boolean miseAJour = false;

    private Convention convention;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        print();
        if (miseAJour) {
            setCompteursToZero();
        }
        return true;
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_BOUCLEMENT_CP_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void retrieve() {
        if (!JadeStringUtil.isEmpty(idConvention)) {
            convention = VulpeculaRepositoryLocator.getConventionRepository().findById(idConvention);
        }
        compteurs = compteurRepository.findCompteursForAnneeMoins5(convention, annee);
        compteursByConvention = Compteur.groupByConvention(compteurs);
    }

    private void print() throws Exception {
        BouclementExcel bouclementExcel = new BouclementExcel(getSession(),
                DocumentConstants.LISTES_BOUCLEMENT_CP_DOC_NAME, DocumentConstants.LISTES_BOUCLEMENT_CP_NAME,
                compteursByConvention, annee);
        bouclementExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), bouclementExcel.getOutputFile());
    }

    private void setCompteursToZero() {
        for (Compteur compteur : compteurs) {
            compteur.setMontantRestant(Montant.ZERO);
            compteurRepository.update(compteur);
        }
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public boolean isMiseAJour() {
        return miseAJour;
    }

    public void setMiseAJour(boolean miseAJour) {
        this.miseAJour = miseAJour;
    }

    public boolean getMiseAJour() {
        return miseAJour;
    }
}
