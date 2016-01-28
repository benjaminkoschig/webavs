package globaz.pavo.process;

import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationPUCSIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationRecord;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator;
import java.util.ArrayList;

public class CIGenereDifferencePUCS extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String fileName = "";
    String idAffilie = "";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {

        ICIDeclarationIterator itDec = null;
        itDec = new CIDeclarationPUCSIterator();
        itDec.setSession(getSession());
        itDec.setTypeImport(CIDeclaration.CS_PUCS_II);
        itDec.setFilename(getFileName());
        ArrayList<String> nss = new ArrayList<String>();
        ArrayList<String> doublon = new ArrayList<String>();
        while (itDec.hasNext()) {
            CIDeclarationRecord rec = itDec.next();
            if (nss.contains(rec.getNumeroAvs())) {
                doublon.add(rec.getNumeroAvs());
            } else {
                nss.add(rec.getNumeroAvs());
            }
        }
        System.out.println(doublon);
        itDec = new CIDeclarationPUCSIterator();
        itDec.setSession(getSession());
        itDec.setTypeImport(CIDeclaration.CS_PUCS_II);
        itDec.setFilename(getFileName());
        while (itDec.hasNext()) {
            CIDeclarationRecord rec = itDec.next();

            DSInscriptionsIndividuellesManager mgr = new DSInscriptionsIndividuellesManager();
            mgr.setSession(getSession());
            mgr.setLikeNumeroAvs(String.valueOf(rec.getNumeroAvs()));
            mgr.setForAnnee(rec.getAnnee());
            mgr.setForIdAffiliation(idAffilie);
            mgr.setForMontantSigne("=");
            mgr.setForMontantSigneValue(rec.getMontantEcr());
            mgr.find();

            if (mgr.size() > 1) {
                System.out.println("Assuré à double => traitement manuel : " + rec.getNumeroAvs() + " montant : "
                        + rec.getMontantEcr() + " année : " + rec.getAnnee());
            } else if (mgr.size() < 1) {
                System.out.println("Assuré à inexistant : " + rec.getNumeroAvs() + " montant : " + rec.getMontantEcr()
                        + " année : " + rec.getAnnee());
            }
        }
        return true;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFileName() {
        return fileName;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getMontantInscIdentique(CIDeclarationRecord iter) throws Exception {

        return "";

    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_LONG;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

}
