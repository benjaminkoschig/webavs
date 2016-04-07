package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
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

/**
 * Classe servant au trigger ExternalService utilis� par le FW</br>
 * Configur�, elle est s'associe aux add/update sur diff�rents BEntity associ�s pour provoquer au besoin la cr�ation
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
            // Mutation si la langue a �t� modifi�e
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
        // Extraction avec l'actuelle affiliation pour comparaison des champs modifi�s
        if (!tiers.getSession().hasErrors()) {

            TITiersViewBean tiersAvantModif = new TITiersViewBean();
            tiersAvantModif.setSession(entity.getSession());
            tiersAvantModif.setIdTiers(tiers.getIdTiers());
            tiersAvantModif.retrieve(tiers.getSession().getCurrentThreadTransaction());
            if (!tiersAvantModif.isNew()) {
                // Champs modifi�s devant �tre annonc�
                if (!tiersAvantModif.getLangue().equalsIgnoreCase(tiers.getLangue())) {
                    // Cr�ation d'une mutation pour chaque les affiliations non provisoire qui ont un num�ro IDE
                    // diff�rent
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
                // Pour les mutations d'adresse, il faut tester si l'adresse utilis�e par l'affiliation est celle que
                // l'on a modifie. Comme cette m�thode est aussi appel� lors de la modification d'un tiers, ce test
                // n'est pas concern� (idAdresseModifie vide)
                if (!JadeStringUtil.isEmpty(idAdresseModifie)) {
                    // Test si l'adresse modifi�e est celle utilis�e par l'affiliation
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
                    // Cr�ation annonce mutation
                    AFIDEUtil.generateAnnonceMutationIde(affiliation.getSession(), affiliation);
                }
                numroIDElu = affiliation.getNumeroIDE();
            }
        }
    }

    private void generateMutationAffiliation(BEntity entity) throws Exception {
        AFAffiliation affiliation = (AFAffiliation) entity;
        // Extraction avec l'actuelle affiliation pour comparaison des champs modifi�s
        // Mutation si l'affiliation n'est pas passive
        if (!affiliation.getSession().hasErrors() && !affiliation.isIdeAnnoncePassive()) {

            AFAffiliation affiliationAvantModif = new AFAffiliation();
            affiliationAvantModif.setAffiliationId(affiliation.getAffiliationId());
            affiliationAvantModif.retrieve(affiliation.getSession().getCurrentThreadTransaction());
            if (!affiliationAvantModif.isNew()) {
                // si je change le numero IDE
                if (!JadeStringUtil.isEmpty(affiliationAvantModif.getNumeroIDE())) {
                    // Champs modifi�s devant �tre annonc�s
                    if (!affiliationAvantModif.getRaisonSociale().equalsIgnoreCase(affiliation.getRaisonSociale())
                            || !affiliationAvantModif.getNumeroIDE().equalsIgnoreCase(affiliation.getNumeroIDE())
                            || !affiliationAvantModif.getTypeAffiliation().equalsIgnoreCase(
                                    affiliation.getTypeAffiliation())
                            || !affiliationAvantModif.getBrancheEconomique().equalsIgnoreCase(
                                    affiliation.getBrancheEconomique())) {
                        // Cr�ation annonce mutation
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
