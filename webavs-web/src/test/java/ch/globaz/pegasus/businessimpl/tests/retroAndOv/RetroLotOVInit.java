package ch.globaz.pegasus.businessimpl.tests.retroAndOv;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import java.util.Arrays;
import java.util.List;
import ch.globaz.pegasus.tests.util.PegasusPersistanceUtils;

public class RetroLotOVInit /* extends Init */{

    public static void clearConsole() {
        String cmd = "cls";
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            // Attente que le process soit fini
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearTablePC() throws JadePersistenceException, Exception {
        String tables = "RELOTS,PCADEPR,PCAFORMO,PCALFAM,PCALOCIM,PCANNDEC,PCASREVI,PCASVIE,PCAUREV,PCAUTAPI,PCAUTREN,PCBETAIL,PCBINHA,PCBISHP,PCBISPH,PCCALPP,PCCBCCP,PCCENVI,PCCOMOCC,PCCONREV,PCCOPDEC,PCCPSAL,PCCREACC,PCCREANC,PCDECACA,PCDECHEA,PCDECREF,PCDECSUP,PCDEMPC,PCDESFOR,PCDESREV,PCDOFINH,PCDONPERS,PCDOSSI,PCDRMBRFA,PCDROIT,PCFOPRAM,PCHOME,PCIJAPG,PCINJOAI,PCJOUAPP,PCLIZOLO,PCLOYER,PCMARSTO,PCMONETR,PCNUMER,PCORDVER,PCPCACC,PCPEALI,PCPERSET,PCPLCAL,PCPRCHAM,PCPRENTI,PCPRESTA,PCRALDE,PCRALIN,PCRAVSAI,PCREHYP,PCTAJOHO,PCTCEVI,PCTFORE,PCTITRE,PCTYCHAM,PCVALDEC,PCVARMET,PCVEHICU,PCVERDRO,PCZONFOR";
        String tableComptat = "CASECTP,CAOPERP";

        List<String> tableNamesCompat = Arrays.asList(tableComptat.split(","));
        for (String name : tableNamesCompat) {
            PegasusPersistanceUtils.deletwByPsPY(name, RetroLotOVInit.class);
        }
        /*
         * delete from ccjuweb.CASECTP where IDCOMPTEANNEXE = 33872
         * 
         * delete from ccjuweb.CAOPERP where IDCOMPTEANNEXE = 33872;
         */
        List<String> tableNames = Arrays.asList(tables.split(","));
        for (String name : tableNames) {
            PegasusPersistanceUtils.deletwByCsPY(name, RetroLotOVInit.class);
        }

        StringBuffer sql = new StringBuffer("DELETE FROM ").append(JadePersistenceUtil.getDbSchema())
                .append(".RELOTS WHERE YTTTYP = 52833001 AND CSPY like '%").append(JadeThread.currentUserId())
                .append("%'");

        PegasusPersistanceUtils.executeUpdate(sql.toString(), RetroLotOVInit.class);
    }

    // @Override
    // public void setUp() throws Exception {
    // //
    // // StringBuffer sql = new StringBuffer("DELETE FROM ").append(JadePersistenceUtil.getDbSchema()).append(
    // // ".PCORDVER");
    // // PegasusPersistanceUtils.executeUpdate(sql.toString(), this.getClass());
    // // sql = new StringBuffer("DELETE FROM ").append(JadePersistenceUtil.getDbSchema()).append(".PCPRESTA");
    // // PegasusPersistanceUtils.executeUpdate(sql.toString(), this.getClass());
    // super.setUp();
    // // PCPERPCA Pas de spy
    // // RetroLotOVInit.clearConsole();
    // RetroLotOVInit.clearTablePC();
    // JadeThread.commitSession();
    // }

}
