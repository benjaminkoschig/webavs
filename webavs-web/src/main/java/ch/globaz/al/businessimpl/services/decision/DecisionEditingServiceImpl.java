package ch.globaz.al.businessimpl.services.decision;

import globaz.editing.EditingHelper;
import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.text.ParseException;
import java.util.*;
import javax.xml.datatype.DatatypeConfigurationException;
import ch.ech.xmlns.ech_0010._4.MailAddressType;
import ch.ech.xmlns.ech_0044._2.DatePartiallyKnownType;
import ch.ech.xmlns.ech_0044._2.PersonIdentificationPartnerType;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstEditingDecision;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.decision.ALDecisionEditingException;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.decision.DecisionEditingService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALEditingUtils;
import ch.globaz.common.util.EditingCommon;
import ch.globaz.naos.business.data.AssuranceInfo;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.inforom.xmlns.editing_common._1.AffilieType;
import ch.inforom.xmlns.editing_common._1.CoordoneesPaiementType;
import ch.inforom.xmlns.editing_common._1.GenreType;
import ch.inforom.xmlns.editing_env._1.AdresseType;
import ch.inforom.xmlns.editing_env._1.DestinatairesType;
import ch.inforom.xmlns.editing_env._1.EditionType;
import ch.inforom.xmlns.editing_env._1.EnteteEditionType;
import ch.inforom.xmlns.editing_env._1.EnteteGlobaleType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.AllocDiversType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.AllocataireType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.AllocationsType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.DecisionAFRootType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.DecisionType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.DroitsDiversType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.DroitsEnfantsType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.EtatDossierType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.FinMotifType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.MotifEcheanceType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.ObjectFactory;
import ch.inforom.xmlns.editing_fam_decisions_af._1.StatutDossierType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.TotalType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.TypeBeneficiaireType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.TypeDroitType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.TypePaiementType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.VersementARealiserType;

/**
 * Implémentation des service editing pour les décisions des allocations familiales
 * 
 * @author pta
 * 
 */
public class DecisionEditingServiceImpl implements DecisionEditingService {

    @Override
    public CoordoneesPaiementType buildAdressePaiement(String idTiers, ch.ech.xmlns.ech_0010._4.ObjectFactory ech10of)
            throws JadeApplicationException, JadePersistenceException {

        AdresseTiersDetail adress = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers, true,
                ALCSTiers.DOMAINE_AF, JadeDateUtil.getGlobazFormattedDate(new Date()), "");

        if (adress.getFields() == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#buildAdressePaiement: adress is null");
        }

        return EditingCommon.buildCoordoneesPaiementType(adress);
    }

    @Override
    public EnteteGlobaleType buildEntetesDecisions(EditingHelper h, EnteteGlobaleType enteteGlobale,
            DossierComplexModel dossier, String uuid) throws JadeApplicationException, JadePersistenceException {
        String today = JadeDateUtil.getGlobazFormattedDate(new Date());
        String noSuccursaleAF = ParamServiceLocator.getParameterModelService()
                .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.NUMERO_SUCCURSALE_CAISSE, today)
                .getValeurAlphaParametre();

        String noCaisseAF = ParamServiceLocator.getParameterModelService()
                .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.NUMERO_CAISSE, today)
                .getValeurAlphaParametre();

        /*
         * Entête global du fichier
         */

        enteteGlobale.setTraitementDeMasse(false);
        enteteGlobale.setIDUtilisateur(JadeThread.currentUserName());
        enteteGlobale.setNoAgence(JadeStringUtil.parseInt(JadeStringUtil.removeChar(noSuccursaleAF, '.'), 0));
        enteteGlobale.setNoCaisse(JadeStringUtil.parseInt(noCaisseAF, 0));
        enteteGlobale.setNomProcessus(this.getClass().getName()); // ou numéro doc inforom ?
        enteteGlobale.setIDProcessus(uuid); // cle de correlation
        enteteGlobale.setAttenteFlagFinTraitement(false);
        enteteGlobale.setTraitementPilote(false);
        enteteGlobale.setSecteurActivite("AF");
        enteteGlobale.setFamilleDocument("FAM_DECISIONS_AF");

        return enteteGlobale;

    }

    @Override
    public DestinatairesType builDestinataires(DossierComplexModel dossier, DestinatairesType destinatairType)
            throws JadeApplicationException, JadePersistenceException {

        CopieComplexSearchModel listeCopies = searchCopies(dossier);

        for (int i = 0; i < listeCopies.getSize(); i++) {

            if (((CopieComplexModel) listeCopies.getSearchResults()[i]).getCopieModel().getOrdreCopie().equals("1")) {
                AdresseType principal = new AdresseType();

                // id tiers destiantaire
                principal.setIDDestinataire(((CopieComplexModel) listeCopies.getSearchResults()[i]).getCopieModel()
                        .getIdTiersDestinataire());
                // langue du document (FIXME voir si modifier et mettre la langue de idTiers destinataire
                principal.setLangue(langueCodeIso(dossier));

                // beneficiaire adresse

                MailAddressType benef = buildMailAdressType(((CopieComplexModel) listeCopies.getSearchResults()[i])
                        .getCopieModel().getIdTiersDestinataire());
                principal.setAdresse(benef);
                destinatairType.setPrincipal(principal);
            }
            // FIXME pour l'instant reprendre l'adresse de l'affiliée si c'est un paiement indirect et que
            // la position de la copie est deux
            else if (JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getIdTiersBeneficiaire())
                    && ((CopieComplexModel) listeCopies.getSearchResults()[i]).getCopieModel().getOrdreCopie()
                            .equals("2")) {

                AdresseType copie = new AdresseType();
                // reprend l'id tiers de l'affilié FIXME pour l'instant
                copie.setIDDestinataire(AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                        dossier.getDossierModel().getNumeroAffilie()));

                // copie.setIDDestinataire(((CopieComplexModel) listeCopies.getSearchResults()[i]).getCopieModel()
                // .getIdTiersDestinataire());
                // langue du document (FIXME voir si modifier et mettre la langue de idTiers destinataire
                copie.setLangue(langueCodeIso(dossier));

                // beneficiaire adresse

                // reprend l'id tiers de l'affilié FIXME pour l'instant
                MailAddressType benef = buildMailAdressType(AFBusinessServiceLocator.getAffiliationService()
                        .findIdTiersForNumeroAffilie(dossier.getDossierModel().getNumeroAffilie()));

                // benef = this.buildMailAdressType(((CopieComplexModel) listeCopies.getSearchResults()[i])
                // .getCopieModel().getIdTiersDestinataire(), benef);
                copie.setAdresse(benef);

                destinatairType.getCopie().add(copie);

            } else {

                AdresseType copie = new AdresseType();
                // id tiers destiantaire
                copie.setIDDestinataire(((CopieComplexModel) listeCopies.getSearchResults()[i]).getCopieModel()
                        .getIdTiersDestinataire());
                // langue du document (FIXME voir si modifier et mettre la langue de idTiers destinataire
                copie.setLangue(langueCodeIso(dossier));

                // beneficiaire adresse
                MailAddressType benef = buildMailAdressType(((CopieComplexModel) listeCopies.getSearchResults()[i])
                        .getCopieModel().getIdTiersDestinataire());
                copie.setAdresse(benef);

                destinatairType.getCopie().add(copie);

            }

        }
        return destinatairType;
    }

    /**
     * méthode qui remplit une adresse de courrier (MailAdressType)
     * 
     * @param idTiers : id du tiers
     * @return l'adresse de courrier
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private MailAddressType buildMailAdressType(String idTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        // adresse de courrier
        AdresseTiersDetail addressCourrier = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTiers,
                new Boolean(true), JadeDateUtil.getGlobazFormattedDate(new Date()), ALCSTiers.DOMAINE_AF,
                ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_COURRIER, "");

        return EditingCommon.buildMailAdressType(idTiers, addressCourrier);

    }

    @Override
    public void getAllocationsType(DecisionAFRootType decAF, DossierComplexModel dossier, ObjectFactory bof,
                                   Map<?, ?> total, List<CalculBusinessModel> calcul) throws JadeApplicationException,
            JadePersistenceException {
        // contrôles des paramètres
        if (dossier == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getAllocationsType: dossier is null");
        }
        if (decAF == null) {
            throw new ALDecisionEditingException(
                    "DecisionEditingServiceImpl#getAllocationsType: DecisionAFRootType decAf is null");
        }

        if (bof == null) {
            throw new ALDecisionEditingException(
                    "DecisionEditingServiceImpl#getAllocationsType: ObjectFactory bof is null");
        }
        AllocationsType allocations = bof.createAllocationsType();
        decAF.setAllocations(allocations);
        // total des allocations
        TotalType totalType = new TotalType();
        // montant total
        totalType.setMontantMensuel(new FWCurrency((String) total.get(ALConstCalcul.TOTAL_EFFECTIF))
                .getBigDecimalValue());
        // montant journalier
        totalType.setMontantJournalier(new FWCurrency((String) total.get(ALConstCalcul.TOTAL_UNITE_EFFECTIF))
                .getBigDecimalValue());
        // monnaie
        totalType.setMonnaie(ALConstEditingDecision.MONNAIE_CH);

        allocations.setTotal(totalType);

        // si nbre jour début ou nbre jour fin
        if (!JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getNbJoursDebut())
                || !JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getNbJoursFin())) {
            VersementARealiserType versementARealiser = new VersementARealiserType();
            // si nombre de jour début
            String date = null;
            if (!JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getNbJoursDebut())) {
                versementARealiser.setNombreJoursDebut(JadeStringUtil.parseInt(dossier.getDossierModel()
                        .getNbJoursDebut(), 0));

                date = getDateCalcul(dossier);
                total = ALServiceLocator.getCalculBusinessService().getTotal(dossier, calcul,
                        ALCSDossier.UNITE_CALCUL_JOUR, dossier.getDossierModel().getNbJoursDebut(), false, date);
                versementARealiser.setMontantTotalVersementDebut(new FWCurrency((String) total
                        .get(ALConstCalcul.TOTAL_EFFECTIF)).getBigDecimalValue());
            }
            // si nombre de jour fin
            if (!JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getNbJoursFin())) {
                versementARealiser.setNombreJoursFin(JadeStringUtil.parseInt(dossier.getDossierModel().getNbJoursFin(),
                        0));

                date = getDateCalcul(dossier);
                total = ALServiceLocator.getCalculBusinessService().getTotal(dossier, calcul,
                        ALCSDossier.UNITE_CALCUL_JOUR, dossier.getDossierModel().getNbJoursFin(), false, date);
                versementARealiser.setMontantTotalVersementFin(new FWCurrency((String) total
                        .get(ALConstCalcul.TOTAL_EFFECTIF)).getBigDecimalValue());
            }

            // monnaie
            versementARealiser.setMonnaie(ALConstEditingDecision.MONNAIE_CH);

            allocations.setVersementARealiser(versementARealiser);
        }

    }

    @Override
    public DecisionAFRootType getContent(DecisionAFRootType decAF, DossierComplexModel dossierModel)
            throws JadeApplicationException, JadePersistenceException {

        // Contrôle des paramètres
        if (decAF == null) {
            throw new ALDecisionEditingException(
                    "DecisionEditingServiceImpl#getContent: DecisionAFRootType decAf is null");
        }
        if (dossierModel == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getContent: dossierModel is null");
        }

        ObjectFactory bof = new ObjectFactory();
        ch.ech.xmlns.ech_0010._4.ObjectFactory ech10of = new ch.ech.xmlns.ech_0010._4.ObjectFactory();
        // ch.ech.xmlns.ech_0044._2.ObjectFactory ech44of = new ch.ech.xmlns.ech_0044._2.ObjectFactory();
        ch.inforom.xmlns.editing_common._1.ObjectFactory comof = new ch.inforom.xmlns.editing_common._1.ObjectFactory();

        // DecisionAFRootType decAF = bof.createDecisionAFRootType();
        /*
         * Décision
         */
        DecisionType dec = bof.createDecisionType();
        decAF.setDecision(dec);
        dec.setNoDossier(dossierModel.getDossierModel().getId());
        dec.setDateDebut(dossierModel.getDossierModel().getDebutValidite());
        if (!JadeStringUtil.isBlankOrZero(dossierModel.getDossierModel().getFinValidite())) {
            dec.setDateFin(dossierModel.getDossierModel().getFinValidite());
        }

        // activité du bénéficiaire du dossier (allcoataire)
        dec.setTypeBeneficiaire(TypeBeneficiaireType.fromValue(ALEditingUtils
                .getValueEditingTypeBeneficiaire(dossierModel.getDossierModel().getActiviteAllocataire())));
        // etat du dossier
        dec.setEtatDossier(EtatDossierType.fromValue(ALEditingUtils.getValueEditingEtatDossier(dossierModel
                .getDossierModel().getEtatDossier())));
        // statut du dossier
        dec.setStatutDossier(StatutDossierType.fromValue(ALEditingUtils.getValueEditingStatutDossier(dossierModel
                .getDossierModel().getStatut())));
        // type de paiement au niveau du dossier
        dec.setTypePaiement(TypePaiementType.fromValue(getTypePaiementDossierDecisionEditing(dossierModel)));
        // référence de la loi du dossier
        dec.setLoi(getLoiDecision(dossierModel));

        // créer un type affilié les données de l'affiliés
        AffilieType aff = comof.createAffilieType();
        ALServiceLocator.getDecisionEditingService().getContentAffilie(aff, dossierModel.getDossierModel());
        // setter les données de l'affilié
        decAF.setAffilie(aff);

        // // créer un typeAfflocataire
        AllocataireType alloc = bof.createAllocataireType();

        // ajouter les données de l'allocataire
        alloc = ALServiceLocator.getDecisionEditingService().getContentAllocataire(alloc, dossierModel, bof, ech10of,
                comof);
        // setter les données de l'allocataire
        decAF.setAllocataire(alloc);

        // charger les calculs des droits
        List<CalculBusinessModel> resultatCalcul = ALServiceLocator.getCalculBusinessService().getCalcul(
                dossierModel, getDateCalcul(dossierModel));
        // calcul du montant total de la décision
        Map<?, ?> total = ALServiceLocator.getCalculBusinessService().getTotal(dossierModel,
                resultatCalcul, ALCSDossier.UNITE_CALCUL_MOIS, "1", false, getDateCalcul(dossierModel));

        resultatCalcul = (List<CalculBusinessModel>) total.get(ALConstCalcul.DROITS_CALCULES);

        // ajouter les données des droits
        ALServiceLocator.getDecisionEditingService().getDroitsEnfantType(decAF, dossierModel, bof, resultatCalcul);

        // ajouter les données de droits divers (naissances et accueil)

        ALServiceLocator.getDecisionEditingService().getDroitsDivers(decAF, dossierModel, bof, resultatCalcul);

        // ajouter les données générales des allocations des allocations
        ALServiceLocator.getDecisionEditingService()
                .getAllocationsType(decAF, dossierModel, bof, total, resultatCalcul);

        return decAF;
    }

    @Override
    public AffilieType getContentAffilie(AffilieType affilieType, DossierModel dossier)
            throws JadeApplicationException, JadePersistenceException {

        // contrôle des paramètres
        if (affilieType == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getContentAffilie affilieType is null");
        }
        if (dossier == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getContentAffilie dossier is null");
        }
        // numéro d'affilié
        affilieType.setNoAffilie(dossier.getNumeroAffilie());

        AssuranceInfo assInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier,
                dossier.getDebutValidite());
        // genre d'affiliation
        affilieType.setGenre(GenreType.fromValue(ALEditingUtils.getValueEditingGenreAffiliation(assInfo
                .getGenreAffiliation())));
        // nom de l'affilié
        affilieType.setNomSociete(assInfo.getDesignation());
        return affilieType;

    }

    @Override
    public AllocataireType getContentAllocataire(AllocataireType allocType, DossierComplexModel dossierModel,
            ObjectFactory bof, ch.ech.xmlns.ech_0010._4.ObjectFactory ech10of,
            ch.inforom.xmlns.editing_common._1.ObjectFactory comof) throws JadeApplicationException,
            JadePersistenceException {

        // contrôle des paramètres
        if (allocType == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getContentAllocataire allocType is null");
        }
        if (dossierModel == null) {
            throw new ALDecisionEditingException(
                    "DecisionEditingServiceImpl#getContentAllocataire dossierModel is null");
        }
        // récupération du genre personne (madame, monsieur, mademoiselle)

        if (!JadeStringUtil.isBlankOrZero(EditingCommon.getGenrePersonne(dossierModel.getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getTiers().getTitreTiers()))) {
            allocType.setGenre(EditingCommon.getGenrePersonne(dossierModel.getAllocataireComplexModel()
                    .getPersonneEtendueComplexModel().getTiers().getTitreTiers()));

        }
        // récupération des données de la personne
        PersonIdentificationPartnerType persType = new PersonIdentificationPartnerType();
        allocType.setPersonne(ALServiceLocator.getDecisionEditingService().getContentPersonneType(persType,
                dossierModel.getAllocataireComplexModel().getPersonneEtendueComplexModel()));

        // si paiement autre que indirect récpérer l'adresse de paiement au niveau du dossierr
        if (!JadeStringUtil.isBlankOrZero(dossierModel.getDossierModel().getIdTiersBeneficiaire())) {

            comof.createCoordoneesPaiementType();

            CoordoneesPaiementType donneePaiement = ALServiceLocator.getDecisionEditingService().buildAdressePaiement(
                    dossierModel.getDossierModel().getIdTiersBeneficiaire(), ech10of);
            allocType.setCoordoneesPaiement(donneePaiement);

        }

        return allocType;
    }

    @Override
    public PersonIdentificationPartnerType getContentPersonneType(PersonIdentificationPartnerType persType,
            PersonneEtendueComplexModel personne) throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (persType == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getContentPersonneType persType is null");
        }
        if (personne == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getContentPersonneType personne is null");
        }
        ch.ech.xmlns.ech_0044._2.ObjectFactory ech44of = new ch.ech.xmlns.ech_0044._2.ObjectFactory();

        persType = ech44of.createPersonIdentificationPartnerType();
        // nss si pas vide
        if (!JadeStringUtil.isBlankOrZero(personne.getPersonneEtendue().getNumAvsActuel())) {
            persType.setVn(Long.parseLong(JadeStringUtil.removeChar(personne.getPersonneEtendue().getNumAvsActuel(),
                    '.')));
        }
        // nom de la personne
        persType.setOfficialName(personne.getTiers().getDesignation1());
        // prénom de la personne
        persType.setFirstName(personne.getTiers().getDesignation2());
        // si pas vide date de naissance
        if (!JadeStringUtil.isBlankOrZero(personne.getPersonne().getDateNaissance())) {
            DatePartiallyKnownType date = ech44of.createDatePartiallyKnownType();
            try {
                date.setYearMonthDay(EditingHelper.toXmlDate(personne.getPersonne().getDateNaissance()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (DatatypeConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            persType.setDateOfBirth(date);

        }
        // si pas vide sexe de la personne
        if (!JadeStringUtil.isBlankOrZero(personne.getPersonne().getSexe())) {
            if (JadeStringUtil.equals(personne.getPersonne().getSexe(), ALConstEditingDecision.PERS_CS_SEXE_HOMME,
                    false)) {
                persType.setSex(ALConstEditingDecision.PERS_SEXE_HOMME);
            } else if (JadeStringUtil.equals(personne.getPersonne().getSexe(),
                    ALConstEditingDecision.PERS_CS_SEXE_FEMME, false)) {
                persType.setSex(ALConstEditingDecision.PERS_SEXE_FEMME);
            } else {
                throw new ALDecisionEditingException(personne.getPersonne().getSexe() + " is not valid");
            }

        }

        return persType;
    }

    /**
     * Retourne la date à utiliser pour le calcul de la décision selon le dossier
     * 
     * @param dossierComplexModel
     *            dossier
     * @return la date à utiliser pour le calcul dans la décision
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private final String getDateCalcul(DossierComplexModel dossierComplexModel) throws JadeApplicationException {

        if (JadeDateUtil.isGlobazDate(dossierComplexModel.getDossierModel().getFinValidite())) {
            return dossierComplexModel.getDossierModel().getFinValidite();

        } else {
            return dossierComplexModel.getDossierModel().getDebutValidite();
        }
    }

    @Override
    public void getDroitsDivers(DecisionAFRootType decAf, DossierComplexModel dossier, ObjectFactory bof,
            List<CalculBusinessModel> resultatCalcul) throws JadeApplicationException, JadePersistenceException {
        for (int i = 0; i < resultatCalcul.size(); i++) {

            // prendre en compte uniquement les prestations naissances ou acceuil
            if (ALCSDroit.TYPE_NAIS.equals((resultatCalcul.get(i)).getType())
                    || ALCSDroit.TYPE_ACCE.equals((resultatCalcul.get(i)).getType())) {
                DroitsDiversType droitsDiversType = bof.createDroitsDiversType();

                // récupère le type
                droitsDiversType.setType(AllocDiversType.fromValue(ALEditingUtils
                        .getValueEditingTypePrestaDivers(resultatCalcul.get(i).getType())));
                // récupère la personne
                PersonIdentificationPartnerType persType = new PersonIdentificationPartnerType();
                droitsDiversType.setPersonne(ALServiceLocator.getDecisionEditingService().getContentPersonneType(
                        persType,
                        resultatCalcul.get(i).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()));
                // récupère le montant effectif
                droitsDiversType.setMontantAllocEffectif(new FWCurrency((resultatCalcul.get(i))
                        .getCalculResultMontantEffectif()).getBigDecimalValue());

                // pour les inter cantonal supplétif
                if (JadeStringUtil.equals(dossier.getDossierModel().getStatut(), ALCSDossier.STATUT_CS, false)) {
                    // montant Allocataire
                    droitsDiversType.setMontantAllocAF(new FWCurrency((resultatCalcul.get(i)).getMontantAllocataire())
                            .getBigDecimalValue());
                    // montant autre parent

                    droitsDiversType.setMontantAllocAFAutreParent(new FWCurrency((resultatCalcul.get(i))
                            .getMontantAutreParent()).getBigDecimalValue());
                }
                // récupère la monnaie
                // ajout la monnaie
                droitsDiversType.setMonnaie(ALConstEditingDecision.MONNAIE_CH);

                // ajouter le droit
                decAf.getDroitsDivers().add(droitsDiversType);

            }

        }

    }

    @Override
    public void getDroitsEnfantType(DecisionAFRootType decAF, DossierComplexModel dossier, ObjectFactory bof,
            List<CalculBusinessModel> resultatCalcul) throws JadeApplicationException, JadePersistenceException {

        // contrôle des paramètres
        if (decAF == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getDroitsEnfantType: decAF is null");

        }
        if (bof == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getDroitsEnfantType: bof is null");

        }
        if (dossier == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getDroitsEnfantType: dossier is null");

        }
        for (int i = 0; i < resultatCalcul.size(); i++) {

            // pas prendre en compte les prestations accueils et naissances
            if (!ALCSDroit.TYPE_NAIS.equals((resultatCalcul.get(i)).getType())
                    && !ALCSDroit.TYPE_ACCE.equals((resultatCalcul.get(i)).getType())) {
                // pour chaque droit généré un droit enfant et l'ajouter
                DroitsEnfantsType droitsEnfantType = bof.createDroitsEnfantsType();
                // récupération du genre
                if (!JadeStringUtil.isBlankOrZero(EditingCommon.getGenrePersonne(resultatCalcul.get(i).getDroit()
                        .getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getTitreTiers()))) {
                    droitsEnfantType.setGenre(EditingCommon.getGenrePersonne(resultatCalcul.get(i).getDroit()
                            .getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getTitreTiers()));

                }
                // récupération des données de la personne
                PersonIdentificationPartnerType persType = new PersonIdentificationPartnerType();
                if (JadeStringUtil.equals(ALCSDroit.TYPE_MEN, resultatCalcul.get(i).getDroit().getDroitModel()
                        .getTypeDroit(), false)) {
                    droitsEnfantType.setPersonne(ALServiceLocator.getDecisionEditingService().getContentPersonneType(
                            persType, dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()));
                } else {
                    droitsEnfantType.setPersonne(ALServiceLocator.getDecisionEditingService().getContentPersonneType(
                            persType,
                            resultatCalcul.get(i).getDroit().getEnfantComplexModel().getPersonneEtendueComplexModel()));
                }

                // récupération de l'adresse de paiement si nécessaire
                if (!JadeStringUtil.isBlankOrZero(resultatCalcul.get(i).getDroit().getDroitModel()
                        .getIdTiersBeneficiaire())) {

                    // comof.createCoordoneesPaiementType();
                    ch.ech.xmlns.ech_0010._4.ObjectFactory ech10of = new ch.ech.xmlns.ech_0010._4.ObjectFactory();
                    CoordoneesPaiementType donneePaiement = ALServiceLocator.getDecisionEditingService()
                            .buildAdressePaiement(
                                    resultatCalcul.get(i).getDroit().getDroitModel().getIdTiersBeneficiaire(), ech10of);
                    droitsEnfantType.setCoordoneesPaiement(donneePaiement);

                }

                // ajout de la date d'échéance
                droitsEnfantType.setDateEcheance(resultatCalcul.get(i).getDroit().getDroitModel().getFinDroitForcee());
                // ajout du motif d'échéance
                droitsEnfantType.setMotifEcheance(getMotifEcheance(resultatCalcul.get(i).getDroit()));
                // ajout montant mensuel/jour/heure
                droitsEnfantType.setDroitMensuel(new FWCurrency((resultatCalcul.get(i))
                        .getCalculResultMontantEffectif()).getBigDecimalValue());
                // ajout la monnaie
                droitsEnfantType.setMonnaie(ALConstEditingDecision.MONNAIE_CH);

                // pour les inter cantonal supplétif
                if (JadeStringUtil.equals(dossier.getDossierModel().getStatut(), ALCSDossier.STATUT_CS, false)) {

                    // montant Allocataire
                    droitsEnfantType.setMontantAF(new FWCurrency((resultatCalcul.get(i)).getMontantAllocataire())
                            .getBigDecimalValue());
                    // montant autre parent
                    droitsEnfantType.setMontantAFAutreParent(new FWCurrency((resultatCalcul.get(i))
                            .getMontantAutreParent()).getBigDecimalValue());

                }

                // si supplément horloger ajouter le montant
                if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_SUP_HORLO, resultatCalcul.get(i).getTarif(), false)) {
                    droitsEnfantType.setMontantComplementHorloger(new FWCurrency((resultatCalcul.get(i))
                            .getCalculResultMontantEffectif()).getBigDecimalValue());

                }
                // ajout du tarif
                if (!JadeStringUtil.isBlankOrZero(resultatCalcul.get(i).getTarif())) {
                    droitsEnfantType.setTarif(JadeCodesSystemsUtil.getCodeLibelle(resultatCalcul.get(i).getTarif()));

                }
                // ajouter le droit
                decAF.getDroitsEnfants().add(droitsEnfantType);
            }

        }

    }

    @Override
    public void getEnteteEditionType(EditionType edition, DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle du paramètre

        if (edition == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getEnteteEditionType edition is null");

        }

        EnteteEditionType enteteEdition = edition.getEnteteEdition();
        enteteEdition.setReference("3006WAF"); // num doc inforom
        enteteEdition.setDateAffichee(JadeDateUtil.getGlobazFormattedDate(new Date()));
        enteteEdition.setDateCreation(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // récupére le groupe alpha du dossier
        enteteEdition.setGroupeAlphabetiqueDossier(groupeAlphaDossier(dossier.getAllocataireComplexModel()
                .getPersonneEtendueComplexModel()));

        Date date = new Date();

        enteteEdition.setHeureCreation(getFormatteHHMMSS(date));
        enteteEdition.setNoModule("AF");
        // tiers de l'affilié
        String tiersAffilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                dossier.getDossierModel().getNumeroAffilie());
        AdresseTiersDetail addressCourrier = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(tiersAffilie,
                new Boolean(true), JadeDateUtil.getGlobazFormattedDate(new Date()), ALCSTiers.DOMAINE_AF,
                ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_COURRIER, "");

        enteteEdition.setLieu(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
        enteteEdition.setNoCommuneAffilie(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID));
        enteteEdition.setCodePostalAffilie(addressCourrier.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA));

    }

    private String getFormatteHHMMSS(Date date) {
        if (date == null) {
            return "";
        }
        StringBuffer hhMMSS = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (hour < 10) {
            hhMMSS.append('0');
        }

        hhMMSS.append(hour);
        hhMMSS.append('.');
        if (minute < 10) {
            hhMMSS.append('0');
        }
        hhMMSS.append(minute);
        hhMMSS.append('.');
        if (second < 10) {
            hhMMSS.append('0');
        }
        hhMMSS.append(second);
        return hhMMSS.toString();
    }

    /**
     * Méthode qui retourne le canton de l'affilié ou loi fédérale agricole
     * 
     * @param dossier
     *            dossier pour lequel on recherche la loi
     * @return String : loi (canton ou loi agricole de l'affilié)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception
     */
    private String getLoiDecision(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {
        // Contrôle du paramètres
        if (dossier == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getLoiDecision dossier is null");
        }

        String loiDecision = null;

        AssuranceInfo assInfo = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(
                dossier.getDossierModel(), dossier.getDossierModel().getDebutValidite());
        if (JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(), ALCSDossier.ACTIVITE_AGRICULTEUR,
                false)
                || JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_TRAVAILLEUR_AGRICOLE, false)
                || JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_COLLAB_AGRICOLE, false)
                || JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_EXPLOITANT_ALPAGE, false)
                || JadeStringUtil.equals(dossier.getDossierModel().getActiviteAllocataire(),
                        ALCSDossier.ACTIVITE_PECHEUR, false)) {

            loiDecision = ALConstEditingDecision.LOI_FED_AGRICOLE;

        } else {

            loiDecision = ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(assInfo.getCanton());

            loiDecision = JadeCodesSystemsUtil.getCode(loiDecision);

        }

        return loiDecision;
    }

    /**
     * Méthode qui remplit les éléments pour un MotifEcheanceType
     * 
     * @param droit
     *            : droit pour lequel il faut remplir MotifEcheanceType
     * @return MotifEcheanceType
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     */

    private MotifEcheanceType getMotifEcheance(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle du paramètre
        if (droit == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getMotifEcheance: droit is null");
        }
        MotifEcheanceType motifEcheance = new MotifEcheanceType();
        // si c'est un droit enfant ou formation on met l'âge de l'enfant
        if (JadeStringUtil.equals(ALCSDroit.TYPE_ENF, droit.getDroitModel().getTypeDroit(), false)
                || JadeStringUtil.equals(ALCSDroit.TYPE_FORM, droit.getDroitModel().getTypeDroit(), false)) {
            motifEcheance.setAgeEnfant(JadeStringUtil.toInt(ALImplServiceLocator.getDatesEcheancePrivateService()
                    .getAgeEnfant(
                            droit.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonne()
                                    .getDateNaissance(), droit.getDroitModel().getFinDroitForcee())));

        }

        // fin de motif
        // si droit ménage pas de fin, mettre droit radié FIXME à voir
        if (JadeStringUtil.equals(ALCSDroit.TYPE_MEN, droit.getDroitModel().getTypeDroit(), false)) {
            motifEcheance.setFinMotif(FinMotifType.fromValue(ALConstEditingDecision.MOTIF_FIN_DROIT_RADIE));
        } else {
            motifEcheance.setFinMotif(FinMotifType.fromValue(ALEditingUtils.getValueEditingMotifEcheance(droit
                    .getDroitModel().getMotifFin())));
        }
        // capacité d'exercer
        motifEcheance.setCapableExercer(droit.getEnfantComplexModel().getEnfantModel().getCapableExercer());
        // type de droit
        motifEcheance.setTypeDroit(TypeDroitType.fromValue(ALEditingUtils.getValueEditingTypeDroit(droit
                .getDroitModel().getTypeDroit())));

        return motifEcheance;
    }

    /**
     * 
     * @param dossier
     * @return
     * @throws JadeApplicationException
     */

    private String getTypePaiementDossierDecisionEditing(DossierComplexModel dossier) throws JadeApplicationException {
        // Contrôle du paramètres
        if (dossier == null) {
            throw new ALDecisionEditingException(
                    "DecisionEditingServiceImpl#getTypePaiementDossierDecisionEditing dossier is null");
        }

        String typePaiementDossier = null;
        if (JadeStringUtil.isBlankOrZero(dossier.getDossierModel().getIdTiersBeneficiaire())) {
            typePaiementDossier = ALConstEditingDecision.TYPE_PAIEMENT_IND;

        } else if (JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(), dossier
                .getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), false)) {
            typePaiementDossier = ALConstEditingDecision.TYPE_PAIEMENT_DIR_ALLOC;
        } else if (!JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(), dossier
                .getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), false)) {

            typePaiementDossier = ALConstEditingDecision.TYPE_PAIEMENT_DIR_TIERS;

        } else {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#getTypePaiementDossierDecisionEditing "
                    + typePaiementDossier + " not found");

        }
        return typePaiementDossier;

    }

    /**
     * Méthode qui retourne les 3 premiers caractères du nom
     * 
     * @param personne
     * @return
     * @throws JadeApplicationException
     */
    private String groupeAlphaDossier(PersonneEtendueComplexModel personne) throws JadeApplicationException {
        // contrôle du paramètre
        if (personne == null) {
            throw new ALDecisionEditingException("DecisionEditingServiceImpl#groupeAlphaDossier personne is null");
        }

        String groupeAlpha = new String();
        groupeAlpha = personne.getTiers().getDesignation1();
        groupeAlpha = JadeStringUtil.convertSpecialChars(groupeAlpha);
        String groupeAlphaNew = new String();

        for (int i = 0; i < groupeAlpha.length(); i++) {
            char lettre = groupeAlpha.charAt(i);
            if (((lettre >= 'a') && (lettre <= 'z')) || ((lettre >= 'A') && (lettre <= 'Z'))) {

                groupeAlphaNew = groupeAlphaNew + lettre;
            }
        }

        if (groupeAlphaNew.length() > 3) {
            groupeAlphaNew = JadeStringUtil.toUpperCase(JadeStringUtil.substring(groupeAlphaNew, 0, 3));
        } else {
            groupeAlphaNew = JadeStringUtil.toUpperCase(groupeAlphaNew);
        }

        return groupeAlphaNew;
    }

    /**
     * méthode qui retorune la langue de la décision
     * 
     * @param dossier
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private String langueCodeIso(DossierComplexModel dossier) throws JadeApplicationServiceNotAvailableException,
            JadeApplicationException, JadePersistenceException {

        String langueDocument = null;

        // FIXME pour l'instant reprende la langue de l'affilié
        langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
                dossier.getDossierModel().getNumeroAffilie());

        // // Si langue reprise langue affilié
        // if (dossier.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
        // langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
        // dossier.getDossierModel().getNumeroAffilie());
        // }// si reprise langue allocataire
        // else {
        // langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(
        // dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
        // dossier.getDossierModel().getNumeroAffilie());
        // }
        return langueDocument;
    }

    /**
     * Charge les copies liées au dossier passé en paramètre
     * 
     * @param dossier
     *            Dossier pour lequel chercher les copies
     * @return Résultat de la recherche des copies
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private CopieComplexSearchModel searchCopies(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException {

        CopieComplexSearchModel copieComplexSearchModel = new CopieComplexSearchModel();
        copieComplexSearchModel.setForIdDossier(dossier.getId());
        copieComplexSearchModel.setForTypeCopie(ALCSCopie.TYPE_DECISION);
        return ALServiceLocator.getCopieComplexModelService().search(copieComplexSearchModel);
    }

}
