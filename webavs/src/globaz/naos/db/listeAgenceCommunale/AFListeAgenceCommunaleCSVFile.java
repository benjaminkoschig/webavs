package globaz.naos.db.listeAgenceCommunale;

import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.naos.application.AFApplication;
import globaz.naos.listes.excel.AFXmlmlMappingAgenceCommunale;
import globaz.naos.process.AFListeExcelAgenceCommunaleProcess;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class AFListeAgenceCommunaleCSVFile {

    private ArrayList arrayCsv = null;
    private String filename = "";

    private ArrayList liste = null;

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

    public AFListeAgenceCommunaleCSVFile populateSheet(AFListeExcelAgenceCommunaleProcess process,
            AFListeAgenceCommunaleManager manager, String numCaisse) throws Exception {
        BStatement statement = null;
        AFListeAgenceCommunale agence = null;
        String line = null;
        arrayCsv = new ArrayList();
        String sep = ";";
        // Création entête colonne
        // this.arrayCsv = this.setTitleRow();
        // Pour information: indique le nombre d'annonces à charger
        process.setProgressScaleValue(manager.getCount());
        /*
         * définition du style et mise en place du titre ,des entêtes, des bordures...
         */
        statement = manager.cursorOpen(process.getTransaction());
        // parcours du manager et remplissage des cell
        while (((agence = (AFListeAgenceCommunale) manager.cursorReadNext(statement)) != null) && (!agence.isNew())) {
            if (agence != null) {

                String numCaisseAF = "";
                String numCaisseAVS = "";

                process.incProgressCounter();

                line = agence.getNumAffilie() + sep;
                line += agence.getNom() + sep;
                line += agence.getPrenom() + sep;

                /**
                 * recherche l'adresse dans le tiers put dans le container
                 */

                String ville = "";
                String rue = "";
                String numRue = "";
                String npa = "";
                String casePostale = "";

                TITiers tiers = AFApplication.retrieveTiers(process.getSession(), agence.getIdTiers());
                TIAdresseDataSource d;

                try {
                    d = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            AFXmlmlMappingAgenceCommunale.CS_AFFILIATION, JACalendar.todayJJsMMsAAAA(), true);
                } catch (Exception e) {
                    throw new Exception("Technical Exception, Unabled to retrieve the adresse ( idTiers = "
                            + agence.getIdTiers() + ")", e);
                }

                if (d != null) {
                    ville = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                    rue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE);
                    numRue = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                    npa = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
                    casePostale = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_CASE_POSTALE);
                }

                // Rue
                line += rue + " " + numRue + sep;
                // Case postale
                line += casePostale + sep;
                // NPA
                line += npa + sep;
                // Localité
                line += ville + sep;
                line += agence.getDateFormate(agence.getDateDebutAffiliation()) + sep;
                line += agence.getDateFormate(agence.getDateFinAffiliation()) + sep;
                line += process.getSession().getCodeLibelle(agence.getGenreAffiliation()) + sep;
                numCaisseAVS = AFXmlmlMappingAgenceCommunale.chargerNumeroCaisse(CodeSystem.GENRE_CAISSE_AVS, agence,
                        process, numCaisse);
                numCaisseAF = AFXmlmlMappingAgenceCommunale.chargerNumeroCaisse(CodeSystem.GENRE_CAISSE_AF, agence,
                        process, numCaisse);
                line += numCaisseAVS + sep;
                line += numCaisseAF + sep;
                String commentaire = "";
                if (agence.getProvisoire().booleanValue()
                        || CodeSystem.TYPE_AFFILI_PROVIS.equals(agence.getGenreAffiliation())) {
                    if (CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(agence.getGenreAffiliation())) {
                        commentaire = process.getSession().getLabel("LISTE_COMMENTAIRE_FICHIER_CENTRALE") + " - "
                                + process.getSession().getLabel("LISTE_COMMENTAIRE_PROVISOIRE");
                    } else {
                        commentaire = process.getSession().getLabel("LISTE_COMMENTAIRE_PROVISOIRE");
                    }
                } else if (CodeSystem.TYPE_AFFILI_FICHIER_CENT.equals(agence.getGenreAffiliation())) {
                    commentaire = process.getSession().getLabel("LISTE_COMMENTAIRE_FICHIER_CENTRALE");
                }
                line += commentaire + sep;

                // Ajout au tableau
                arrayCsv.add(line);
            } else {
                break;
            }
            process.incProgressCounter();
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
