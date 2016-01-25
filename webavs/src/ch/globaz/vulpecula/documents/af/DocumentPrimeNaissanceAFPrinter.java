package ch.globaz.vulpecula.documents.af;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.af.DocumentPrimeNaissanceAF.Element;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import com.google.common.base.Preconditions;

public class DocumentPrimeNaissanceAFPrinter extends DocumentPrinter<Element> {
    private static final long serialVersionUID = 3721912632239314835L;

    private String idTravailleur;
    private String nomEnfant;
    private Date dateNaissance;
    private String objet = "";

    public DocumentPrimeNaissanceAFPrinter() {
        super();
    }

    @Override
    public void retrieve() {
        Preconditions.checkArgument(!JadeStringUtil.isEmpty(idTravailleur));

        Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur);
        Element element = new Element();
        element.travailleur = travailleur;
        element.travailleur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdressePrioriteCourrierByIdTiers(travailleur.getIdTiers()));
        element.nomEnfant = nomEnfant;
        element.dateNaissance = dateNaissance;
        setElements(Arrays.asList(element));
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.PRIME_NAISSANCE_AF_TYPE_NUMBER;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentPrimeNaissanceAF(getCurrentElement(), DocumentConstants.PRIME_NAISSANCE_AF_CT_NAME,
                DocumentConstants.PRIME_NAISSANCE_AF_TYPE_NUMBER);
    }

    public void setNomEnfant(String nomEnfant) {
        this.nomEnfant = nomEnfant;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public final String getIdTravailleur() {
        return idTravailleur;
    }

    public final String getNomEnfant() {
        return nomEnfant;
    }

    public final Date getDateNaissance() {
        return dateNaissance;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("EMAIL_OBJECT_PRIME_NAISSANCE_AF");
    }
}
