package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSpy;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;

public class CIEcritureGenre6Process extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String avs = "";
    private String idEcriture = null;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        if (getTransaction().hasErrors()) {
            abort();
        }
        if (JadeStringUtil.isBlank(avs)) {
            getSession().addError(getSession().getLabel("MSG_REGISTRE_ASSURE_OBL"));
            abort();
            return false;
        }
        CICompteIndividuelManager mgr = new CICompteIndividuelManager();
        mgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        mgr.setSession(getSession());
        mgr.setForNumeroAvs(NSUtil.unFormatAVS(getAvs()));
        mgr.find();
        if (mgr.size() < 1) {
            getSession().addError(getSession().getLabel("MSG_REGISTRE_ASSURE_OBL"));
            abort();
            return false;
        }
        CICompteIndividuel ci = (CICompteIndividuel) mgr.getFirstEntity();
        CIEcriture ec = new CIEcriture();
        ec.setSession(getSession());
        ec.setEcritureId(getIdEcriture());
        ec.retrieve();
        // Extourne
        CIEcriture copieExt = new CIEcriture();
        copieExt.setSession(getSession());
        ec.copyDataToEntity(copieExt);
        copieExt.setEcritureId("");
        copieExt.setExtourne(CIEcriture.CS_EXTOURNE_1);
        copieExt.setDateAnnonceCentrale("");
        copieExt.setForAffilieParitaire(true);

        String dateCompta = JACalendar.today().toStrAMJ();

        String time = new JATime(JACalendar.now()).toString();
        BSpy spy = new BSpy(dateCompta + time + getSession().getUserId());
        copieExt.setEspionSaisie(spy.getFullData());
        copieExt.simpleAdd(getTransaction());
        // Ajout code genre 1
        CIEcriture copie = new CIEcriture();
        copie.setSession(getSession());
        ec.copyDataToEntity(copie);
        copie.setEcritureId("0");
        copie.setAvs(avs);
        copie.setCompteIndividuelId(ci.getCompteIndividuelId());
        copie.setIdTypeCompte(CIEcriture.CS_CI);
        copie.setGenreEcriture(CIEcriture.CS_CIGENRE_1);
        copie.setDateAnnonceCentrale("0");
        copie.setEspionSaisie(spy.getFullData());

        copie.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
        copie.updTypeCommptePrecedant(CIEcriture.CS_TEMPORAIRE);
        copie.setForAffilieParitaire(true);
        copie.add(getTransaction());
        copie.updTypeCommptePrecedant(copie.getIdTypeCompte());
        copie.comptabiliser(getTransaction(), true, true);
        if (getTransaction().hasErrors()) {
            abort();
        }
        // TODO Raccord de méthode auto-généré
        return !isAborted();
    }

    public String getAvs() {
        return avs;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        if (isOnError() || isAborted()) {
            return getSession().getLabel("MSG_AFFECTATION_ECHEC");
        } else {
            return getSession().getLabel("MSG_AFFECTATION_OK");
        }
    }

    public String getIdEcriture() {
        return idEcriture;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_SHORT;
    }

    public void setAvs(String avs) {
        this.avs = avs;
    }

    public void setIdEcriture(String idEcriture) {
        this.idEcriture = idEcriture;
    }

}
