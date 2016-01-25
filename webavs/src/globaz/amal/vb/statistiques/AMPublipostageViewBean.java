package globaz.amal.vb.statistiques;

import globaz.amal.process.statistiques.AMStatistiquesPublipostageProcess;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class AMPublipostageViewBean extends BJadePersistentObjectViewBean {
    private String inNumeroContribuable = new String();
    private String inTypeDemande = new String();
    private boolean isCodeActif = false;
    private boolean isContribuable = false;
    private int recordsSize = 0;
    private String wantedFields = new String();
    private String wantedNpa = new String();
    private String yearSubside = new String();

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getInNumeroContribuable() {
        return inNumeroContribuable;
    }

    public String getInTypeDemande() {
        return inTypeDemande;
    }

    public int getRecordsSize() {
        return recordsSize;
    }

    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getWantedFields() {
        return wantedFields;
    }

    public String getWantedNpa() {
        return wantedNpa;
    }

    public String getYearSubside() {
        return yearSubside;
    }

    public boolean isCodeActif() {
        return isCodeActif;
    }

    public boolean isContribuable() {
        return isContribuable;
    }

    public void launchListePublipostage() {
        AMStatistiquesPublipostageProcess publipostageProcess = new AMStatistiquesPublipostageProcess();
        publipostageProcess.setSession(BSessionUtil.getSessionFromThreadContext());
        publipostageProcess.setYearSubside(yearSubside);
        publipostageProcess.setWantedFields(wantedFields);
        publipostageProcess.setRecordsSize(recordsSize);
        publipostageProcess.setWantedNpa(wantedNpa);
        publipostageProcess.setInTypeDemande(inTypeDemande);
        publipostageProcess.setInNumeroContribuable(inNumeroContribuable);
        publipostageProcess.setIsContribuable(isContribuable);
        publipostageProcess.setCodeActif(isCodeActif);
        publipostageProcess.run();
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setCodeActif(boolean isCodeActif) {
        this.isCodeActif = isCodeActif;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    public void setInNumeroContribuable(String inNumeroContribuable) {
        this.inNumeroContribuable = inNumeroContribuable;
    }

    public void setInTypeDemande(String inTypeDemande) {
        this.inTypeDemande = inTypeDemande;
    }

    public void setIsContribuable(boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    public void setRecordsSize(int recordsSize) {
        this.recordsSize = recordsSize;
    }

    public void setWantedFields(String wantedFields) {
        this.wantedFields = wantedFields;
    }

    public void setWantedNpa(String wantedNpa) {
        this.wantedNpa = wantedNpa;
    }

    public void setYearSubside(String yearSubside) {
        this.yearSubside = yearSubside;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
