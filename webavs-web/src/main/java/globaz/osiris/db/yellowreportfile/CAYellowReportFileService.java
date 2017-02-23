package globaz.osiris.db.yellowreportfile;

import globaz.common.util.CommonBlobUtils;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.osiris.db.ordres.sepa.exceptions.CAYellowReportFileException;
import java.util.Date;

public class CAYellowReportFileService {

    private BSession session;

    public CAYellowReportFileService(final BSession session) {
        this.session = session;
    }

    /**
     * Cr�ation d'une identification de fichier ISO mis en DB.
     * 
     * @param session La session.
     * @param fileName L'emplacement et le nom du fichier.
     * @param type Le type de fichier ISO, voir @CAISOFileType.
     * @param content Le contenu du fichier ISO.
     * @return Un iso file cr�e et persist�.
     * @throws Exception Une exception d'acc�s � la DB.
     */
    public CAYellowReportFile create(final String fileName, final CAYellowReportFileType type, final byte[] content)
            throws Exception {

        CAYellowReportFile reportFile = createWithoutDb(fileName, type);
        reportFile.setSession(session);
        reportFile.add(session.getCurrentThreadTransaction());

        final String idBlob = getGeneratedIdBlob(reportFile.getId());
        CommonBlobUtils.addBlob(idBlob, content, session.getCurrentThreadTransaction());
        reportFile.setIdBlobContent(idBlob);
        reportFile.update(session.getCurrentThreadTransaction());

        return reportFile;
    }

    /**
     * R�cup�re le contenu � partir de l'id d'une identification de fichier ISO.
     * 
     * @param id L'id d'une identification de fichier ISO.
     * @throws Exception Une exception lors de la recherche du fichier en DB.
     */
    public byte[] readContent(final String id) throws Exception {
        if (id == null) {
            return null;
        }
        CAYellowReportFile reportFile = read(id);
        return readContentFromIdBlob(reportFile.getIdBlobContent());
    }

    /**
     * R�cup�re le contenu � partir de l'id d'une identification de fichier ISO.
     * 
     * @param id L'id d'une identification de fichier ISO.
     * @throws Exception Une exception lors de la recherche du fichier en DB.
     */
    public byte[] readContentFromIdBlob(final String idBlobContent) throws Exception {
        return (byte[]) CommonBlobUtils.readBlob(idBlobContent, session.getCurrentThreadTransaction());
    }

    /**
     * R�cup�re une identification ISO.
     * 
     * @param id L'id d'une identification de fichier ISO.
     * @return Une identification ISO.
     * @throws Exception Une exception d'acc�s � la DB.
     */
    public CAYellowReportFile read(final String id) throws Exception {
        if (id == null) {
            return null;
        }
        return readIsoFile(id);
    }

    /**
     * Savoir si un fichier a d�j� �t� pris par le processus d'identification.
     * 
     * @param fileName Un nom de fichier � rechercher.
     * @return True si il a d�j� �t� pris, sinon False.
     * @throws Exception Une exception d'acc�s � la DB.
     */
    public boolean hasBeenAlreadyIdentified(final String fileName) throws Exception {
        boolean hasBeenAlreadyTaken = false;

        final CAYellowReportFileManager manager = new CAYellowReportFileManager();
        manager.setSession(session);
        manager.setForFileName(fileName);
        manager.find(1);

        if (manager.getSize() > 0) {
            hasBeenAlreadyTaken = true;
        }

        return hasBeenAlreadyTaken;
    }

    /**
     * Cr�ation d'une identification de fichier ISO sans aller en DB.
     * 
     * @param fileName L'emplacement et le nom du fichier.
     * @param type Le type de fichier ISO, voir @CAISOFileType.
     * @return Un simple iso file pr�-rempli.
     */
    public CAYellowReportFile createWithoutDb(final String fileName, final CAYellowReportFileType type) {
        CAYellowReportFile reportFile = new CAYellowReportFile();
        // Valeur par d�faut
        reportFile.setDateCreated(new Date());
        reportFile.setState(CAYellowReportFileState.IDENTIFIED);
        reportFile.setRemark(null);
        // Valeur pass�e en param�tre
        reportFile.setType(type);
        reportFile.setFileName(fileName);

        return reportFile;
    }

    private String getGeneratedIdBlob(final String id) {
        return CAYellowReportFile.class.getName() + "_" + JadeUUIDGenerator.createStringUUID() + "_" + id;
    }

    /**
     * Change l'�tat d'un iso file sans persist� en DB.
     * 
     * @param reportFile Un iso file.
     * @param stateToUpdate Le status � devoir changer.
     * @param message Un message selon besoin, null => ne va pas faire de SET.
     */
    protected void changeStateWithoutDB(final CAYellowReportFile reportFile,
            final CAYellowReportFileState stateToUpdate, final String message) {
        reportFile.setState(stateToUpdate);
        if (message != null) {
            reportFile.setRemark(message);
        }
    }

    /**
     * Mettre � jour le status.
     * 
     * @param session La session.
     * @param state L'�tat � changer.
     * @param id L'id de l'entit�.
     * @return Le iso file mis � jour.
     * @throws Exception Une exception d'acc�s � la DB.
     */
    public CAYellowReportFile updateState(final CAYellowReportFileState state, final String id, final String message)
            throws Exception {
        if (id == null) {
            return null;
        }
        CAYellowReportFile reportFile = read(id);
        changeStateWithoutDB(reportFile, state, message);
        reportFile.update();

        return reportFile;
    }

    private CAYellowReportFile readIsoFile(final String id) throws Exception {
        CAYellowReportFile reportFile = new CAYellowReportFile();
        reportFile.setSession(session);
        reportFile.setId(id);
        reportFile.retrieve();

        if (reportFile.isNew()) {
            throw new CAYellowReportFileException("No entity found for id :" + id);
        }

        return reportFile;
    }
}
