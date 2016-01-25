package ch.globaz.common.FusionTiersMultiple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.common.FusionTiersMultiple.TIFusionTiersMultipleProcess.TiersInfo;

public class TIFusionTiersMultipleAControlerCSVFile {

    private List arrayCsv = null;
    private String filename = "";

    private List liste = null;

    public String getFilename() {
        return filename;
    }

    public String getOutputFile() {
        try {
            File f = new File(getFilename() + ".csv");
            f.deleteOnExit();
            FileOutputStream out = new FileOutputStream(f);
            write(out);
            out.close();
            return f.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    public TIFusionTiersMultipleAControlerCSVFile populateSheet(TIFusionTiersMultipleProcess process,
            List<TiersInfo> tiersAControler) throws Exception {

        TIFusionTiersMultipleProcess.TiersInfo tiersControle = null;
        String line = null;
        arrayCsv = new ArrayList();
        String sep = ";";
        // Pour information: indique le nombre de Tiers à charger
        process.setProgressScaleValue(tiersAControler.size());

        // parcours de l'ArrayList et remplissage des cellules
        for (int i = 0; i < tiersAControler.size(); i++) {
            tiersControle = tiersAControler.get(i);
            if (tiersControle != null) {
                process.incProgressCounter();

                line = tiersControle.getidTiersAffilie() + sep;
                line += tiersControle.getidTiersRentier() + sep;
                line += tiersControle.getNom() + sep;
                line += tiersControle.getPrenom() + sep;
                line += tiersControle.getDateNaissance() + sep;
                line += tiersControle.getNumAffilie() + sep;
                line += tiersControle.getNss() + sep;
                line += tiersControle.getRemarque() + sep;
                line += tiersControle.getResultFusion() + sep;
                arrayCsv.add(line);
            }
        }
        setData(arrayCsv);
        return this;
    }

    public void setData(Object obj) {
        liste = (ArrayList) obj;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private void write(FileOutputStream out) {

        PrintStream ps = new PrintStream(out);
        try {

            for (Iterator it = liste.iterator(); it.hasNext();) {
                ps.println((String) it.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
    }

}
