package ch.globaz.pegasus.businessimpl.services.doc;

import globaz.jade.exception.JadeApplicationException;
import java.io.File;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.listoutput.LoaderOuter;
import ch.globaz.common.listoutput.SimpleOutputListBuilderJade;
import ch.globaz.pegasus.business.domaine.revisionquadriennale.RevisionCsv;
import ch.globaz.pegasus.business.domaine.revisionquadriennale.RevisionQuadriennale;
import ch.globaz.pegasus.businessimpl.services.revisionquadriennale.RevisionQuadriennaleLoader;

public class FullListeExcelRevision {

    private String annee;

    private final static String OUTPUTNAME = "full_liste_revisions";

    public FullListeExcelRevision(String annee) {
        this.annee = annee;
    }

    public String generate() throws Exception {

        LoaderOuter<String, RevisionQuadriennale, String> loaderOuter = new LoaderOuter<String, RevisionQuadriennale, String>() {
            @Override
            public String out(RevisionQuadriennale revisionQuadriennale) {
                String nomDoc = OUTPUTNAME + "_"
                        + new Date(revisionQuadriennale.getPeriode().getDateDebut()).getValue() + "_"
                        + new Date(revisionQuadriennale.getPeriode().getDateFin()).getValue() + "_";
                createDoc(nomDoc, revisionQuadriennale);
                return nomDoc;
            }

            @Override
            public RevisionQuadriennale load(String annee) throws JadeApplicationException {
                return new RevisionQuadriennaleLoader().load(annee);
            }
        };

        String path = loaderOuter.run(annee);
        loaderOuter.getTime().setNombre(loaderOuter.getDataLoaded().getDemandesARevisers().size());
        return path;
    }

    public String createDoc(String name, RevisionQuadriennale revisionQuadriennale) {
        SimpleOutputListBuilderJade builder = SimpleOutputListBuilderJade.newInstance();
        File file = builder.outputNameAndAddPath(name).addList(revisionQuadriennale.getDemandesARevisers())
                .classElementList(RevisionCsv.class).asXls().build();
        builder.close();
        return file.getAbsolutePath();
    }
}
