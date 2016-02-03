package ch.globaz.pegasus.businessimpl.utils.topazbuilder.pagedegarde;

import ch.globaz.common.document.TextGiver;
import ch.globaz.common.document.babel.BabelTextDefinition;

public interface IPageDeGardeDefinition {

    public BabelTextDefinition getPolitesse();

    public BabelTextDefinition getSalutationPart1();

    public BabelTextDefinition getSalutationPart2();

    public BabelTextDefinition getIntro();

    public BabelTextDefinition getSignatureNomCaisse();

    public BabelTextDefinition getSignataire();

    public BabelTextDefinition getSignatureNomService();

    public BabelTextDefinition getAnnexes();

    public BabelTextDefinition getAnnexesMent();

    public BabelTextDefinition getPersonneDeReference();

    public BabelTextDefinition getNomCollaborateur(String nomCollaborateur);

    public BabelTextDefinition getTelCollaborateur();

    public BabelTextDefinition getTelCollaborateur(String tel);

    public BabelTextDefinition getNReference();

    public BabelTextDefinition getNReference(String nRef);

    public BabelTextDefinition getVReference();

    public BabelTextDefinition getVReference(String vRef);

    public BabelTextDefinition getSalutations(String formulePolitesse, String langueIso);

    public BabelTextDefinition getSignature(String signature);

    public BabelTextDefinition getSignatureGestionnaire(String gestionnaire);

    public BabelTextDefinition getServiceCollaborateur(String serviceCollaborateur);

    public BabelTextDefinition getEmailCollaborateur(String emailCollaborateur);

    public BabelTextDefinition getTelGestionnaire(String telGestionnaire);

    public BabelTextDefinition getGestionnaire(String gestionnaire);

    public BabelTextDefinition getIdUser(String idUser);

    public BabelTextDefinition getNSSBeneficiare(String nssBeneficiaire);

    public BabelTextDefinition getDateEtLieu();

    public BabelTextDefinition getDateEtLieu(String dateLieu);

    public BabelTextDefinition getAdresse(String adresse);

    public BabelTextDefinition getHeader(String headerDoc);

    public TextGiver<BabelTextDefinition> getTextGiver();

    public BabelTextDefinition getNomCollabo(String nomCollabo);

    public BabelTextDefinition getTelCollabo(String tel);
}
