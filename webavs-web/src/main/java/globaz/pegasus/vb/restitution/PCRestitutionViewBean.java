package globaz.pegasus.vb.restitution;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.exceptions.models.restitution.PCRestitutionException;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.dossier.DossierSearch;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitution;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitutionSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.utils.PCApplicationUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import com.ibm.as400.access.Job;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournal;
import globaz.pegasus.process.comptabilisation.PCRestiPCLegalComptaProcess;

public class PCRestitutionViewBean extends BJadePersistentObjectViewBean {

    private SimpleRestitution simpleRestitution;
    private String idDossier;
    private PersonneEtendueComplexModel personne;
    private String mailGest = null;
    static final public String TYPE_REST_PC_AVS_FED  = "TYPE_REST_PC_AVS_FED";
    static final public String TYPE_REST_PC_AI_FED   = "TYPE_REST_PC_AI_FED";
    static final public String TYPE_REST_PC_AVS_SUB  = "TYPE_REST_PC_AVS_SUB";
    static final public String TYPE_REST_PC_AI_SUB   = "TYPE_REST_PC_AI_SUB";
    static final public String TYPE_REST_PC_AVS_CANT = "TYPE_REST_PC_AVS_CANT";
    static final public String TYPE_REST_PC_AI_CANT  = "TYPE_REST_PC_AI_CANT";
    static final public String TYPE_REST_PC_AVS_RFM  = "TYPE_REST_PC_AVS_RFM";
    static final public String TYPE_REST_PC_AI_RFM   = "TYPE_REST_PC_AI_RFM";
    public PCRestitutionViewBean() {
        super();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(simpleRestitution.getSpy());
    }

    @Override
    public void add() throws Exception {
        simpleRestitution = PegasusServiceLocator.getRestitutionService().create(simpleRestitution);
    }

    @Override
    public void delete() throws Exception {
    }
    public String getMailGestionnaire(BSession session) {
        return session.getUserEMail();
    }
    @Override
    public String getId() {
        return simpleRestitution.getId();
    }

    @Override
    public void retrieve() throws Exception {
        if (JadeStringUtil.isEmpty(idDossier)) {
            throw new PCRestitutionException("Unable to read restititon, the id dossier passed is null!");
        }

        SimpleRestitutionSearch restitutionSearch = new SimpleRestitutionSearch();
        restitutionSearch.setForIdDossier(idDossier);
        PegasusServiceLocator.getRestitutionService().search(restitutionSearch);

        if (restitutionSearch.getSize() == 0) {
            simpleRestitution = new SimpleRestitution();
            simpleRestitution.setIdDossier(idDossier);
        } else {
            simpleRestitution = (SimpleRestitution) restitutionSearch.getSearchResults()[0];
        }
        DossierSearch dossierSearch = new DossierSearch();
        dossierSearch.setForIdDossier(idDossier);
        PegasusServiceLocator.getDossierService().search(dossierSearch);
        Dossier dossier;
        if (dossierSearch.getSize() == 1) {
            dossier = (Dossier) dossierSearch.getSearchResults()[0];
        } else {
            throw new PCRestitutionException(
                    "Erreur lors de la récupération du dossier : " + idDossier);
        }

        personne = dossier.getDemandePrestation().getPersonneEtendue();

    }

    @Override
    public void setId(String selectedId) {
    }

    @Override
    public void update() throws Exception {
        simpleRestitution = PegasusServiceLocator.getRestitutionService().update(simpleRestitution);
    }


    public PersonneEtendueComplexModel getPersonne() {
        return personne;
    }

    public void setSimpleRestitution(SimpleRestitution simpleRestitution) {
        this.simpleRestitution = simpleRestitution;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setPersonne(PersonneEtendueComplexModel personne) {
        this.personne = personne;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public SimpleRestitution getSimpleRestitution() {
        return simpleRestitution;
    }
    public String getCaisse(BSession session) throws PropertiesException {
        return PCApplicationUtil.getCaisse();
    }
    public String getTypeResti(String type){
        String cs = "";
        switch (type){
            case TYPE_REST_PC_AVS_FED :
                return simpleRestitution.getTypeRestPCAVSFed();
            case TYPE_REST_PC_AI_FED :
                return simpleRestitution.getTypeRestPCAIFed();
            case TYPE_REST_PC_AVS_SUB :
                return simpleRestitution.getTypeRestPCAvsSubside();
            case TYPE_REST_PC_AI_SUB :
                return simpleRestitution.getTypeRestPCAISubside();
            case TYPE_REST_PC_AVS_CANT :
                return simpleRestitution.getTypeRestPCAvsCantonal();
            case TYPE_REST_PC_AI_CANT :
                return simpleRestitution.getTypeRestPCAICantonal();
            case TYPE_REST_PC_AVS_RFM :
                return simpleRestitution.getTypeRestPCRfmAvs();
            case TYPE_REST_PC_AI_RFM:
                return simpleRestitution.getTypeRestPCRfmAI();
            default:
                return cs;
        }
    }

    public String getMailGest() {
        return mailGest;
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    public String getJournalId() {
        String idJournal = simpleRestitution.getIdJournal();
        if(!JadeStringUtil.isBlankOrZero(idJournal)){
            return idJournal;
        }else{
            return "";
        }
    }

    public String getLienJournal() {
        String idJournal = simpleRestitution.getIdJournal();
        BSession session = BSessionUtil.getSessionFromThreadContext();
        if(!JadeStringUtil.isBlankOrZero(idJournal)){
            CAJournal journal = new CAJournal();
            journal.setSession(session);
            journal.setIdJournal(idJournal);
            try {
                journal.retrieve();
                String text = "Journal n°"+idJournal+" : "+session.getLabel("JSP_PCACCORDEE_D_ETAT")+" - "+ journal.getCsEtat().getLibelle();
                return  text;
            }catch (Exception e){
                return session.getLabel("JOURNAL_INCONNU");
            }

        }else{
            return session.getLabel("JOURNAL_INCONNU");
        }
    }
}
