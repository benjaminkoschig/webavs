package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.util.AFIDEUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAdresseViewBean;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseManager;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseViewBean;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Hashtable;

/**
 * Classe servant au trigger ExternalService utilisé par le FW</br>
 * Configuré, elle est s'associe aux add/update sur différents BEntity associés pour provoquer au besoin la création
 * d'<u>Annonce de mutation</u>
 */
public class AFAnnonceIde extends BAbstractEntityExternalService {

    @Override
    public void afterAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        if (entity instanceof TIAdresseViewBean) {
            generateMutationAdresse(entity);
        } else if (entity instanceof TIAvoirAdresseViewBean) {
            generateMutationAvoirAdresse(entity);
        }
        if (entity.getSession().hasErrors()) {
            throw new Exception(entity.getSession().getErrors().toString());
        }
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
        BTransaction transactionLecture = null;
        if (entity instanceof AFAffiliation) {
            generateMutationAffiliation(entity);
        } else if (entity instanceof TITiersViewBean) {
            // Mutation si la langue a été modifiée
            generateMutationTiers(entity, transactionLecture);
        } else if (entity instanceof TIAdresseViewBean) {
            generateMutationAdresse(entity);
        } else if (entity instanceof TIAvoirAdresse) {
            generateMutationAvoirAdresse(entity);
        }
        if (entity.getSession().hasErrors()) {
            throw new Exception(entity.getSession().getErrors().toString());
        }
    }

    private void generateMutationAvoirAdresse(BEntity entity) throws Exception {
        TIAvoirAdresse avoirAdresse = (TIAvoirAdresse) entity;
        if (!JadeStringUtil.isEmpty(avoirAdresse.getIdAdresseIntUnique())) {
            TIAvoirAdresse avoirAdresseAvantModif = new TIAvoirAdresse();
            avoirAdresseAvantModif.setSession(avoirAdresse.getSession());
            avoirAdresseAvantModif.setIdAdresse(avoirAdresse.getIdAdresseIntUnique());
            avoirAdresseAvantModif.retrieve(avoirAdresse.getSession().getCurrentThreadTransaction());
            // Modification si changement d'adresse
            if (avoirAdresseAvantModif.getIdAdresse().equalsIgnoreCase(avoirAdresse.getIdAdresse())) {
                TITiersViewBean tiers = new TITiersViewBean();
                tiers.setIdTiers(avoirAdresse.getIdTiers());
                tiers.setSession(avoirAdresse.getSession());
                tiers.retrieve();
                findAffiliatioAndGenerateMutation(tiers, avoirAdresse.getIdAdresseIntUnique());
            }
        }
    }

    private void generateMutationAdresse(BEntity entity) throws Exception {
        TIAdresseViewBean adresse = (TIAdresseViewBean) entity;
        TIAdresse adresseAvantModif = new TIAdresse();
        adresseAvantModif.setSession(adresse.getSession());
        adresseAvantModif.setIdAdresseUnique(adresse.getOldIdAdresse());
        adresseAvantModif.retrieve(adresse.getSession().getCurrentThreadTransaction());
        TILocalite localiteAvantModif = new TILocalite();
        localiteAvantModif.setSession(adresse.getSession());
        localiteAvantModif.setIdLocalite(adresseAvantModif.getIdLocalite());
        localiteAvantModif.retrieve(adresse.getSession().getCurrentThreadTransaction());
        if (!adresseAvantModif.isNew() && !localiteAvantModif.isNew()) {
            // Champs modifiés devant être annoncé
            if (!adresseAvantModif.getRue().equalsIgnoreCase(adresse.getRue())
                    || !localiteAvantModif.getLocalite().equalsIgnoreCase(adresse.getLocalite())
                    || !adresseAvantModif.getNumeroRue().equalsIgnoreCase(adresse.getNumeroRue())
                    || !localiteAvantModif.getNumPostal().equalsIgnoreCase(adresse.getLocaliteCode())) {
                // Recherche tiers
                TIAvoirAdresseManager avoirAdresseMng = new TIAvoirAdresseManager();
                avoirAdresseMng.setSession(adresse.getSession());
                avoirAdresseMng.setForIdAdresse(adresse.getOldIdAdresse());
                avoirAdresseMng.changeManagerSize(BManager.SIZE_NOLIMIT);
                avoirAdresseMng.setOrderBy(Jade.getInstance().getDefaultJdbcSchema() + ".TITIERP.HTITIE");
                avoirAdresseMng.find();
                for (int i = 0; i < avoirAdresseMng.getSize(); i++) {
                    TIAvoirAdresse avoirAdresse = (TIAvoirAdresse) avoirAdresseMng.getEntity(i);
                    TITiersViewBean tiers = new TITiersViewBean();
                    tiers.setIdTiers(avoirAdresse.getIdTiers());
                    tiers.setSession(adresse.getSession());
                    tiers.retrieve();
                    findAffiliatioAndGenerateMutation(tiers, adresse.getOldIdAdresse());
                }
            }
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
                if (!tiersAvantModif.getLangue().equalsIgnoreCase(tiers.getLangue())) {
                    // Création d'une mutation pour chaque les affiliations non provisoire qui ont un numéro IDE
                    // différent
                    findAffiliatioAndGenerateMutation(tiers, "");
                }
            }
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
                    // Test si l'adresse modifiée est celle utilisée par l'affiliation
                    TIAdresseDataSource adresseUtiliseDansNaos = AFIDEUtil.loadAdresseForIde(tiers.getSession(),
                            affiliation);
                    if (adresseUtiliseDansNaos != null) {
                        Hashtable<?, ?> dataAdresse = adresseUtiliseDansNaos.getData();
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
                    if (!affiliationAvantModif.getRaisonSociale().equalsIgnoreCase(affiliation.getRaisonSociale())
                            || !affiliationAvantModif.getNumeroIDE().equalsIgnoreCase(affiliation.getNumeroIDE())
                            || !affiliationAvantModif.getTypeAffiliation().equalsIgnoreCase(
                                    affiliation.getTypeAffiliation())
                            || !affiliationAvantModif.getBrancheEconomique().equalsIgnoreCase(
                                    affiliation.getBrancheEconomique())) {
                        // Création annonce mutation
                        AFIDEUtil.generateAnnonceMutationIde(affiliation.getSession(), affiliation);
                    }
                }
            }
        }
    }

    @Override
    public void init(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

    @Override
    public void validate(BEntity entity) throws Throwable {
        // TODO Auto-generated method stub

    }

}
