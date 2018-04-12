package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.process.byitem.ProcessItem;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireType;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.DeclarationSalaireBuilder;
import com.google.common.io.Closer;
import com.google.common.io.Files;

public class PucsSwissDecItem extends ProcessItem {
    private final String remotePathFileSourceUri;
    private final String remotePathDoneUri;
    private final String remotePathErrorUri;
    private final BSession session;
    private final String idJob;

    public PucsSwissDecItem(String remotePathFileSourceUri, String remotePathDoneUri, String remotePathErrorUri,
            String idJob, BSession session) {
        this.remotePathFileSourceUri = remotePathFileSourceUri;
        this.remotePathDoneUri = remotePathDoneUri;
        this.remotePathErrorUri = remotePathErrorUri;
        this.session = session;
        this.idJob = idJob;
    }

    @Override
    public void treat() throws Exception {
        String src = JadeFilenameUtil.extractPath(remotePathFileSourceUri) + "/";
        String name = JadeFilenameUtil.extractFilename(remotePathFileSourceUri);
        try {
            String nameConverted = JadeStringUtil.convertSpecialChars(name);
            if (!nameConverted.equals(name)) {
                JadeFsFacade.rename(remotePathFileSourceUri, JadeStringUtil.convertSpecialChars(name));
                name = nameConverted;
            }
            PucsFile pucsFile = buildPucsByFile(remotePathFileSourceUri, session);
            AFAffiliation affiliation = findAffiation(pucsFile);
            if (affiliation == null) {
                this.addErrors("PROCESS_IMPORT_PUCSINDB_AFFILIATION_NOT_FOUND", pucsFile.getNumeroAffilie(),
                        pucsFile.getAnneeDeclaration());
            } else {
                PucsItem.save(pucsFile, affiliation, idJob, session);
            }
        } catch (Exception e) {
            catchException(e);
        } finally {
            String path = remotePathDoneUri;
            if (hasErrorOrException()) {
                path = remotePathErrorUri;
            }
            JadeFsFacade.copyFile(remotePathFileSourceUri, path + Files.getNameWithoutExtension(name).replace(".", "_")
                    + ".xml");
            JadeFsFacade.delete(remotePathFileSourceUri);
        }
    }

    private AFAffiliation findAffiation(PucsFile pucsFile) {
        return AFAffiliationServices.getAffiliationParitaireByNumero(pucsFile.getNumeroAffilie(),
                pucsFile.getAnneeDeclaration(), session);
    }

    private PucsFile buildPucsByFile(String remotePucsFileUri, BSession session) throws JadeServiceLocatorException,
            JadeServiceActivatorException, JadeClassCastException {
        String filePath = JadeFsFacade.readFile(remotePucsFileUri);
        return buildPucsByFile(filePath, DeclarationSalaireProvenance.SWISS_DEC, session);
    }

    private static PucsFile buildPucsByFile(String filePath, DeclarationSalaireProvenance provenance, BSession session) {
        PucsFile pucsFile = new PucsFile();
        Closer closer = Closer.create();
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            closer.register(fileInputStream);
            ElementsDomParser parser = new ElementsDomParser(fileInputStream);

            double kilobytes = file.length() / 1024;
            pucsFile.setSizeFileInKo(kilobytes);
            pucsFile.setFilename(JadeFilenameUtil.extractFilename(filePath).replace(".xml", ""));
            pucsFile.setProvenance(provenance);

            DeclarationSalaire ds = DeclarationSalaireBuilder.builOnlyHead(parser);
            pucsFile.setAnneeDeclaration(String.valueOf(ds.getAnnee()));
            pucsFile.setAnneeVersement(String.valueOf(ds.getAnnee()));
            pucsFile.setNbSalaires(String.valueOf(ds.getNbSalaire()));
            pucsFile.setNomAffilie(ds.getNom());
            pucsFile.setNumeroAffilie(ds.getNumeroAffilie());
            pucsFile.setTotalControle(ds.getMontantAvs().toStringFormat());
            pucsFile.setDateDeReception(ds.getTransmissionDate().getSwissValue());
            pucsFile.setAfSeul(ds.isAfSeul());
            pucsFile.setForTest(ds.isTest());
            pucsFile.setCurrentStatus(EtatPucsFile.A_VALIDE);
            pucsFile.setDuplicate(ds.isDuplicate());
            pucsFile.setFile(file);
            pucsFile.setCertifieExact(Boolean.FALSE);
            if (ds.isAfSeul()) {
                pucsFile.setTotalControle(ds.getMontantCaf().toStringFormat());
            }
            pucsFile.setTypeDeclaration(DeclarationSalaireType.PRINCIPALE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return pucsFile;
    }

    @Override
    public String getDescription() {
        return JadeFilenameUtil.extractFilename(remotePathFileSourceUri);
    }

}
