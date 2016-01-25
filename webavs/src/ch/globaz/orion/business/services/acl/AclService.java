package ch.globaz.orion.business.services.acl;

import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.orion.business.exceptions.OrionAclException;
import ch.globaz.orion.business.models.acl.Acl;

public interface AclService extends JadeApplicationService {
    public void changeStatutToEnCours(String idAnnonceCollaborateur) throws OrionAclException;

    public void changeStatutToProbleme(String idAnnonceCollaborateur) throws OrionAclException;

    public void changeStatutToTermine(String idAnnonceCollaborateur) throws OrionAclException;

    public Acl[] listAclEnCours() throws OrionAclException;

    public Acl[] listAclSaisie() throws OrionAclException;

    /**
     * Mise à jour du nouveau numéro AVS depuis Web@avs
     * 
     * @param idAnnonceCollaborateur
     * @param nouveauNumeroAvs
     * @return AnnonceCollaborateurComplex updated
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void updateNouveauNumeroAvs(String idAnnonceCollaborateur, String nouveauNumeroAvs) throws OrionAclException;
}
