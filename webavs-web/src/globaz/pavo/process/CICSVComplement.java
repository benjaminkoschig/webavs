package globaz.pavo.process;

import globaz.commons.nss.NSUtil;
import globaz.commons.nss.db.NSSinfo;
import globaz.commons.nss.db.NSSinfoManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.compte.CIExceptions;
import globaz.pavo.db.compte.CIExceptionsManager;
import java.io.File;
import java.io.FileWriter;

public class CICSVComplement extends BProcess {

    private static final long serialVersionUID = 2434925698991069400L;

    public static String getNssOrNavs(String numero, BSession session) {

        NSSinfo info = null;
        try {
            NSSinfoManager mgr = new NSSinfoManager();
            mgr.setSession(session);
            boolean numeroNss = false;
            if (NSUtil.unFormatAVS(numero).trim().length() == 13) {
                numeroNss = true;
                mgr.setForNNSS(numero);
            } else {
                mgr.setForNAVS(numero);
            }

            mgr.setForCodeMutation("0");
            mgr.setForValidite("1");
            mgr.find();

            if (mgr.size() > 0) {
                if (numeroNss) {
                    boolean navsFound = false;
                    for (int i = 0; i < mgr.size(); i++) {
                        if (!navsFound) {
                            info = (NSSinfo) mgr.get(i);
                            if (info.getNAVS().length() == 11) {
                                navsFound = true;
                            } else {
                                info = null;
                            }
                        }
                    }
                } else {
                    info = (NSSinfo) mgr.getFirstEntity();
                }
            } else {
                mgr.setForValidite("0");
                mgr.find();
                if (mgr.size() > 0) {
                    if (numeroNss) {
                        boolean navsFound = false;
                        for (int i = 0; i < mgr.size(); i++) {
                            if (!navsFound) {
                                info = (NSSinfo) mgr.get(i);
                                if (info.getNAVS().length() == 11) {
                                    navsFound = true;
                                } else {
                                    info = null;
                                }
                            }
                        }
                    }
                } else {
                    info = (NSSinfo) mgr.getFirstEntity();
                }
            }
            if (NSUtil.unFormatAVS(numero).trim().length() == 13) {
                if (info != null) {
                    return info.getNAVS();
                }
                return "";
            } else {
                if (info != null) {
                    return info.getNNSS();
                }
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String path = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        CIExceptionsManager mgr = new CIExceptionsManager();
        mgr.setSession(getSession());
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.setForIdAffilie("52131,52554");
        mgr.setForDateEngagement("20090000");
        mgr.find();
        File f = null;
        FileWriter out = null;
        for (int i = 0; i < mgr.size(); i++) {

            CIExceptions exc = (CIExceptions) mgr.get(i);
            String nss = NSUtil.returnNNSS(getSession(), exc.getNumeroAvs());
            f = new File("d://Comparaison/conc.csv");

            out = new FileWriter(f);
            out.write(NSUtil.formatAVSUnknown(getNssOrNavs(exc.getNumeroAvs(), getSession())) + ";");
            String nnss = exc.getNumeroAvs();
            out.write(NSUtil.formatAVSUnknown(nnss + ";"));
            out.write(exc.getNomPrenom() + ";");
            out.write(exc.getDateNaissance() + ";");
            out.write(exc.getSexeForNNSS() + "\n");

        }

        CIEcritureManager ecMgr = new CIEcritureManager();
        ecMgr.setSession(getSession());
        ecMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        ecMgr.setForAffilie("000.6423");
        ecMgr.setForAnnee("2008");
        ecMgr.find();
        for (int i = 0; i < ecMgr.size(); i++) {
            CIEcriture exc = (CIEcriture) ecMgr.get(i);
            out.write(NSUtil.formatAVSUnknown(getNssOrNavs(exc.getAvs(), getSession())) + ";");
            String nnss = exc.getAvs();
            out.write(NSUtil.formatAVSUnknown(nnss + ";"));
            out.write(exc.getNomPrenom() + ";");
            out.write(exc.getDateNaissance() + ";");
            out.write(exc.getSexeForNNSS() + "\n");
        }
        ecMgr.setSession(getSession());
        ecMgr.setForAffilie("000.6785");
        ecMgr.setForAnnee("2008");
        ecMgr.find();
        for (int i = 0; i < ecMgr.size(); i++) {
            CIEcriture exc = (CIEcriture) ecMgr.get(i);
            out.write(NSUtil.formatAVSUnknown(getNssOrNavs(exc.getAvs(), getSession())) + ";");
            String nnss = exc.getAvs();
            out.write(NSUtil.formatAVSUnknown(nnss + ";"));
            out.write(exc.getNomPrenom() + ";");
            out.write(exc.getDateNaissance() + ";");
            out.write(exc.getSexeForNNSS() + "\n");
        }
        out.close();

        return true;
    }

    @Override
    protected String getEMailObject() {
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
