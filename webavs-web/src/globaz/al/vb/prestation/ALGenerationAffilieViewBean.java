package globaz.al.vb.prestation;

import globaz.al.process.generations.ALGenAffilieProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

/**
 * Viewbean gérant la génération par affilié
 * 
 * @author GMO
 * 
 */
public class ALGenerationAffilieViewBean extends BJadePersistentObjectViewBean {

    /**
     * le numéro affilié saisie
     */
    private String numAffilie = null;
    /**
     * la période à générer
     */
    private String periodeAGenerer = null;

    /**
     * constructeur du viewBean
     */
    public ALGenerationAffilieViewBean() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        ALGenAffilieProcess process = new ALGenAffilieProcess();
        process.setPeriodeAGenerer(periodeAGenerer);
        process.setNumAffilie(numAffilie);
        process.setSession(getSession());
        BProcessLauncher.start(process, false);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return null;
    }

    /**
     * 
     * @return numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return periodeAGenerer
     */
    public String getPeriodeAGenerer() {
        return periodeAGenerer;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");

    }

    @Override
    public void setId(String newId) {
        // DO NOTHING
    }

    /**
     * 
     * @param numAffilie
     *            le n° d'affilié
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @param periodeAGenerer
     *            la période à générer
     */
    public void setPeriodeAGenerer(String periodeAGenerer) {
        this.periodeAGenerer = periodeAGenerer;
    }

    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");

    }

}
