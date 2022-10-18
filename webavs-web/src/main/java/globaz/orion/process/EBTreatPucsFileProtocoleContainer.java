package globaz.orion.process;

import ch.globaz.orion.business.models.pucs.PucsFileMerge;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;
import lombok.AllArgsConstructor;


@AllArgsConstructor(staticName = "of")
public class EBTreatPucsFileProtocoleContainer {

    private PucsFileMerge file;
    private String message;
    private String affilie;
    private boolean erreur;

    @Column(name = "BATCH_PROTOCOLE_COL_NOM_FICHIER", order = 1)
    @ColumnStyle(align = Align.LEFT)
    public String getFileName() {
        return file.getPucsFile().getFilename();
    }

    @Column(name = "BATCH_PROTOCOLE_COL_STATUT", order = 2)
    @ColumnStyle(align = Align.LEFT)
    public String getStatut() {
        return erreur ? "KO": "OK";
    }

    @Column(name = "BATCH_PROTOCOLE_COL_AFFILIE", order = 3)
    @ColumnStyle(align = Align.LEFT)
    public String getAffilie() {
        return affilie;
    }

    @Column(name = "BATCH_PROTOCOLE_COL_DETAIL", order = 4)
    @ColumnStyle(align = Align.LEFT)
    public String getDetail() {
        return message;
    }

}
