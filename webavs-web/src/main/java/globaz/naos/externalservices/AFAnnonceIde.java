package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseViewBean;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Hashtable;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;

/**
 * Classe servant au trigger ExternalService utilisé par le FW</br>
 * Configuré, elle est s'associe aux add/update sur différents BEntity associés pour provoquer au besoin la création
 * d'<u>Annonce de mutation</u>
 */
public class AFAnnonceIde extends BAbstractEntityExternalService {

    private static final String PROVENANCE_AFTER_ADD = "PROVENANCE_AFTER_ADD";
    private static final String PROVENANCE_DEFAULT = "PROVENANCE_DEFAULT";

    @Override
    public void afterAdd(BEntity entity) throws Throwable {

        if (entity instanceof TIAvoirAdresseViewBean) {
            generateMutationAvoirAdresse(entity, PROVENANCE_AFTER_ADD);
        }

        if (entity.getSession().hasErrors()) {
            throw new Exception(entity.getSession().getErrors().toString());
        }
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        //

    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        //

    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        //
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        //
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        //

    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        //

    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        BTransaction transactionLecture = null;
        if (entity instanceof AFAffiliation) {
            generateMutationAffiliation(entity);
        } else if (entity instanceof TITiersViewBean) {
            // Mutation si la langue a été modifiée
            generateMutationTiers(entity, transactionLecture);
        } else if (entity instanceof TIAvoirAdresse) {
            generateMutationAvoirAdresse(entity, PROVENANCE_DEFAULT);
        }
        if (entity.getSession().hasErrors()) {
            throw new Exception(entity.getSession().getErrors().toString());
        }
    }

    private void generateMutationAvoirAdresse(BEntity entity, String provenance) throws Exception {

        TIAvoirAdresse avoirAdresse = (TIAvoirAdresse) entity;
        BSession theSession = avoirAdresse.getSession();

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(theSession);
        tiers.setIdTiers(avoirAdresse.getIdTiers());
        tiers.retrieve();

        TIAdresse adresse = new TIAdresse();
        adresse.setSession(theSession);
        adresse.setIdAdresseUnique(avoirAdresse.getIdAdresse());
        adresse.retrieve();

        TILocalite localite = new TILocalite();
        localite.setSession(theSession);
        localite.setIdLocalite(adresse.getIdLocalite());
        localite.retrieve();

        TIAvoirAdresse avoirAdresseAvantModif = new TIAvoirAdresse();
        avoirAdresseAvantModif.setSession(theSession);
        avoirAdresseAvantModif.setId(avoirAdresse.getId());
        avoirAdresseAvantModif.retrieve();

        TIAdresse adresseAvantModif = new TIAdresse();
        adresseAvantModif.setSession(theSession);
        adresseAvantModif.setIdAdresseUnique(avoirAdresseAvantModif.getIdAdresse());
        adresseAvantModif.retrieve();

        TILocalite localiteAvantModif = new TILocalite();
        localiteAvantModif.setSession(theSession);
        localiteAvantModif.setIdLocalite(adresseAvantModif.getIdLocalite());
        localiteAvantModif.retrieve();

        if (PROVENANCE_AFTER_ADD.equalsIgnoreCase(provenance)
                || !adresseAvantModif.getRue().equalsIgnoreCase(adresse.getRue())
                || !localiteAvantModif.getLocalite().equalsIgnoreCase(localite.getLocalite())
                || !adresseAvantModif.getNumeroRue().equalsIgnoreCase(adresse.getNumeroRue())
                || !localiteAvantModif.getNumPostal().equalsIgnoreCase(localite.getNumPostal())) {

            findAffiliatioAndGenerateMutation(tiers, avoirAdresseAvantModif.getIdAdresse());
        }

    }

    private void generateMutationTiers(BEntity entity, BTransaction transactionLecture) throws Exception {
        TITiersViewBean tiers = (TITiersViewBean) entity;
        // Extraction avec l'actuelle affiliation pour comparaison des champs modifiés
        if (!tiers.getSession().hasErrors()) {

            TITiersViewBean tiersAvantModif = new TITiersViewBean();
            tiersAvantModif.setSession(entity.getSession());
            tiersAvantModif.setIdTiers(tiers.getIdTiers());
            tiersAvantModif.retrieve(tiers.getSession().getCurrentThreadTransaction());
            if (!tiersAvantModif.isNew()) {
                // Champs modifiés devant être annoncé
                if (!tiersAvantModif.getLangue().equalsIgnoreCase(tiers.getLangue())
                        || (tiers.getPersonnePhysique() && (!tiersAvantModif.getDateNaissance().equalsIgnoreCase(
                                tiers.getDateNaissance()) || !tiersAvantModif.getNom().equalsIgnoreCase(tiers.getNom())))
                        || isChangementPersonnePhysiqueAndAdresseDifferente(tiersAvantModif, tiers)) {
                    // Création d'une mutation pour chaque les affiliations non provisoire qui ont un numéro IDE
                    // différent
                    findAffiliatioAndGenerateMutation(tiers, "");
                }

            }
        }
    }

    /***
     * Méthode qui permet de définir si un changement entre personne physique / morale à été effectué.
     * Si oui on va comparer les adresses récupérées via les cascades d'adresses, si elle sont différentes on retourne
     * true pour pouvoir générer une annonce IDE.
     * 
     * @param tiersAvantModif
     * @param tiers
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PropertiesException
     */
    private Boolean isChangementPersonnePhysiqueAndAdresseDifferente(TITiersViewBean tiersAvantModif,
            TITiersViewBean tiers) throws PropertiesException, JadeApplicationServiceNotAvailableException {

        // Si il y a eu un changement
        if (!tiersAvantModif.getPersonnePhysique().equals(tiers.getPersonnePhysique())) {
            TIAdresseDataSource adresseMorale = AFBusinessServiceLocator.getCascadeAdresseIdeService()
                    .getAdresseFromCascadeIde(true, tiers.getNumAffilieActuel(), tiers.getIdTiers());

            TIAdresseDataSource adressePhysique = AFBusinessServiceLocator.getCascadeAdresseIdeService()
                    .getAdresseFromCascadeIde(false, tiers.getNumAffilieActuel(), tiers.getIdTiers());

            // Si les deux ID adresses sont null (pas d'adresse trouvée) on génèe quand même une annonce
            if (adresseMorale.id_adresse.isEmpty() && adressePhysique.id_adresse.isEmpty()) {
                return true;
            }

            // Si les ID sont les mêmes, les adresses sont les mêmes, donc pas d'annonces à générer.
            return !adresseMorale.id_adresse.equals(adressePhysique.id_adresse);
        } else {
            return false;
        }
    }

    private void findAffiliatioAndGenerateMutation(TITiersViewBean tiers, String idAdresseModifie) throws Exception {
        AFAffiliationManager affMng = new AFAffiliationManager();
        affMng.setSession(tiers.getSession());
        affMng.changeManagerSize(BManager.SIZE_NOLIMIT);
        affMng.setForIdTiers(tiers.getIdTiers());
        affMng.forIsTraitement(false);
        affMng.setForNumeroIdeNotEmpty(true);
        affMng.setOrder("MALFED");
        affMng.find();
        String numroIDElu = "";
        for (int i = 0; i < affMng.size(); i++) {
            boolean modifcationPermise = true;
            AFAffiliation affiliation = (AFAffiliation) affMng.getEntity(i);
            if (!affiliation.getNumeroIDE().equalsIgnoreCase(numroIDElu)) {
                // Pour les mutations d'adresse, il faut tester si l'adresse utilisée par l'affiliation est celle que
                // l'on a modifie. Comme cette méthode est aussi appelé lors de la modification d'un tiers, ce test
                // n'est pas concerné (idAdresseModifie vide)
                if (!JadeStringUtil.isEmpty(idAdresseModifie)) {
                    TIAdresseDataSource adresseDataSource = AFIDEUtil.loadAdresseFromCascadeIde(affiliation);

                    if (adresseDataSource != null) {
                        Hashtable<?, ?> dataAdresse = adresseDataSource.getData();
                        String idAdresseUtiliseDansNaos = (String) dataAdresse
                                .get(TIAbstractAdresseDataSource.ADRESSE_ID_ADRESSE);
                        if (!idAdresseModifie.equalsIgnoreCase(idAdresseUtiliseDansNaos)) {
                            modifcationPermise = false;
                        }
                    }
                }
                if (modifcationPermise && !affiliation.isIdeAnnoncePassive()) {
                    // Création annonce mutation
                    AFIDEUtil.generateAnnonceMutationIde(affiliation.getSession(), affiliation);
                }
                numroIDElu = affiliation.getNumeroIDE();
            }
        }
    }

    private void generateMutationAffiliation(BEntity entity) throws Exception {
        AFAffiliation affiliation = (AFAffiliation) entity;
        // Extraction avec l'actuelle affiliation pour comparaison des champs modifiés
        // Mutation si l'affiliation n'est pas passive
        if (!affiliation.getSession().hasErrors() && !affiliation.isIdeAnnoncePassive()) {

            AFAffiliation affiliationAvantModif = new AFAffiliation();
            affiliationAvantModif.setAffiliationId(affiliation.getAffiliationId());
            affiliationAvantModif.retrieve(affiliation.getSession().getCurrentThreadTransaction());
            if (!affiliationAvantModif.isNew()) {
                // si je change le numero IDE
                if (!JadeStringUtil.isEmpty(affiliationAvantModif.getNumeroIDE())) {
                    // Champs modifiés devant être annoncés
                    if (!affiliationAvantModif.getNumeroIDE().equalsIgnoreCase(affiliation.getNumeroIDE())
                            || !affiliationAvantModif.getTypeAffiliation().equalsIgnoreCase(
                                    affiliation.getTypeAffiliation())
                            || !affiliationAvantModif.getBrancheEconomique().equalsIgnoreCase(
                                    affiliation.getBrancheEconomique())) {
                        // Création annonce mutation
                        AFIDEUtil.generateAnnonceMutationIde(affiliation.getSession(), affiliation);
                    } else {
                        // D0181 cas particulier de la raison sociale(exclure si personnes physique)
                        if (!affiliationAvantModif.getRaisonSociale().equalsIgnoreCase(affiliation.getRaisonSociale())) {
                            TITiersViewBean tiers = new TITiersViewBean();
                            tiers.setSession(entity.getSession());
                            tiers.setIdTiers(affiliation.getIdTiers());
                            tiers.retrieve(affiliation.getSession().getCurrentThreadTransaction());
                            if (!tiers.getPersonnePhysique()) {
                                AFIDEUtil.generateAnnonceMutationIde(affiliation.getSession(), affiliation);
                            }
                        } else {
                            // D0181 Si seul l'activité a changé (cas particulier de la popup de confirmation)
                            if (!affiliationAvantModif.getActivite().equalsIgnoreCase(affiliation.getActivite())
                                    && !JadeStringUtil.isBlankOrZero(affiliation.getActivite())) {
                                // vérifier que l'utilisateur a confirmé sa volonté de l'annoncer
                                if (affiliation.getConfirmerAnnonceActivite()) {
                                    AFIDEUtil.generateAnnonceMutationIde(affiliation.getSession(), affiliation);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public void init(BEntity entity) throws Throwable {
        //
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        //
    }

}
