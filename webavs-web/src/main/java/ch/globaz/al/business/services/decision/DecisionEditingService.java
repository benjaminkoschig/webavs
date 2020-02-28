package ch.globaz.al.business.services.decision;

import globaz.editing.EditingHelper;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.ech.xmlns.ech_0044._2.PersonIdentificationPartnerType;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.inforom.xmlns.editing_common._1.AffilieType;
import ch.inforom.xmlns.editing_common._1.CoordoneesPaiementType;
import ch.inforom.xmlns.editing_env._1.DestinatairesType;
import ch.inforom.xmlns.editing_env._1.EditionType;
import ch.inforom.xmlns.editing_env._1.EnteteGlobaleType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.AllocataireType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.DecisionAFRootType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.ObjectFactory;

/**
 * Service editing pour les décisions AF
 * 
 * @author pta
 * 
 */
public interface DecisionEditingService extends JadeApplicationService {
    /**
     * Méthode qui reprend les données d'une adresse de paiement
     */

     CoordoneesPaiementType buildAdressePaiement(String idTiers, ch.ech.xmlns.ech_0010._4.ObjectFactory ech10of)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * méthode qui génère les entêtes des documents
     * 
     * @param h
     * @param enteteGlobale
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
     EnteteGlobaleType buildEntetesDecisions(EditingHelper h, EnteteGlobaleType enteteGlobale,
            DossierComplexModel dossier, String uuid) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui remplit les données des destinatraire des copies
     * 
     * @param listTiersCopie
     * @param idTiersOriginal
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

    public DestinatairesType builDestinataires(DossierComplexModel dossier, DestinatairesType destinatairType)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui génère les montants généraux pour l'ensebles des droits
     * 
     * @param decAF
     * @param dossier
     * @param bof
     * @param resultatCalcul
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
     void getAllocationsType(DecisionAFRootType decAF, DossierComplexModel dossier, ObjectFactory bof,
                             Map<?, ?> total, List<CalculBusinessModel> calcul) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * @param decAF
     * @param dossierModel
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    DecisionAFRootType getContent(DecisionAFRootType decAF, DossierComplexModel dossierModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param dossier
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

    AffilieType getContentAffilie(AffilieType affilieType, DossierModel dossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param allocType
     * @param dossierModel
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
     AllocataireType getContentAllocataire(AllocataireType allocType, DossierComplexModel dossierModel,
            ObjectFactory bof, ch.ech.xmlns.ech_0010._4.ObjectFactory ech10of,
            ch.inforom.xmlns.editing_common._1.ObjectFactory comof) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * 
     * @param persType
     * @param personne
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
     PersonIdentificationPartnerType getContentPersonneType(PersonIdentificationPartnerType persType,
            PersonneEtendueComplexModel personne) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui génère les données des droits divers pour le docuemnt .xml
     * 
     * @param dossier
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

     void getDroitsDivers(DecisionAFRootType decAf, DossierComplexModel dossier, ObjectFactory bof,
            List<CalculBusinessModel> resultatCalcul) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode qui génère les données des droits pour le docuemnt .xml
     * 
     * @param dossier
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
     void getDroitsEnfantType(DecisionAFRootType decAF, DossierComplexModel dossier, ObjectFactory bof, List<CalculBusinessModel> resultatCalcul) throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * @param edition
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

     void getEnteteEditionType(EditionType edition, DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;
}
