package ch.globaz.pegasus.businessimpl.services.doc;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import java.util.Locale;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.LoaderOuter;
import ch.globaz.pegasus.business.domaine.revisionquadriennale.RevisionCsv;
import ch.globaz.pegasus.business.domaine.revisionquadriennale.RevisionQuadriennale;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.RevisionQuadriennaleLoader;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class FullListeExcelRevision {

    private String annee;

    private final static String OUTPUTNAME = "full_liste_revisions";
    private final static String CSV = ".csv";

    public FullListeExcelRevision(String annee) {
        this.annee = annee;
    }

    public String generate() throws Exception {

        LoaderOuter<String, RevisionQuadriennale, String> loaderOuter = new LoaderOuter<String, RevisionQuadriennale, String>() {
            @Override
            public String out(RevisionQuadriennale revisionQuadriennale) {
                String nomDoc = Jade.getInstance().getPersistenceDir() + OUTPUTNAME + "_"
                        + new Date(revisionQuadriennale.getPeriode().getDateDebut()).getValue() + "_"
                        + new Date(revisionQuadriennale.getPeriode().getDateFin()).getValue()
                        + JadeUUIDGenerator.createStringUUID() + CSV;
                createDoc(nomDoc, revisionQuadriennale);
                return nomDoc;
            }

            @Override
            public RevisionQuadriennale load(String annee) throws JadeApplicationException {
                RevisionQuadriennaleLoader loader = new RevisionQuadriennaleLoader();
                RevisionQuadriennale revisionQuadriennale = loader.load(annee);
                return revisionQuadriennale;
            }
        };

        String path = loaderOuter.run(annee);
        System.out.println("Path: " + path);

        loaderOuter.getTime().setNombre(loaderOuter.getDataLoaded().getDemandesARevisers().size());
        return path;
    }

    public String createDoc(String nomDoc, RevisionQuadriennale revisionQuadriennale) {
        Locale locale = new Locale(BSessionUtil.getSessionFromThreadContext().getIdLangueISO());

        SimpleOutputListBuilder.newInstance().local(locale).addList(revisionQuadriennale.getDemandesARevisers())
                .classElementList(RevisionCsv.class).asCsv().outputName(nomDoc).build();

        return nomDoc;
    }
}
