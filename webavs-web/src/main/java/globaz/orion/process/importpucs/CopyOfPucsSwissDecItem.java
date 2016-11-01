package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.fs.JadeFsFacade;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.services.AFAffiliationServices;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.process.byitem.ProcessItem;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.DeclarationSalaireBuilder;
import com.google.common.io.Closer;

public class CopyOfPucsSwissDecItem extends ProcessItem {
    private final String remotePucsFileUri;
    private final BSession session;
    private final String idJob;

    public CopyOfPucsSwissDecItem(String remotePucsFileUri, BSession session, String idJob) {
        super();
        this.remotePucsFileUri = remotePucsFileUri;
        this.session = session;
        this.idJob = idJob;
    }

    @Override
    public void treat() throws Exception {

        String path = (JadeFilenameUtil.extractPath(remotePucsFileUri)) + "/";
        String name = JadeFilenameUtil.extractFilename(remotePucsFileUri);
        String nameConverted = JadeStringUtil.convertSpecialChars(name);
        if (!nameConverted.equals(name)) {
            JadeFsFacade.rename(remotePucsFileUri, JadeStringUtil.convertSpecialChars(name));
            name = nameConverted;
        }
        PucsFile pucsFile = buildPucsByFile(path + name, session);
        AFAffiliation affiliation = AFAffiliationServices.getAffiliationParitaireByNumero(pucsFile.getNumeroAffilie(),
                pucsFile.getAnneeDeclaration(), session);
        PucsItem.save(pucsFile, affiliation, idJob, session);
        String dest = EBProperties.PUCS_SWISS_DEC_DIRECTORY_OK.getValue() + pucsFile.getId().replace(".", "") + ".xml";
        JadeFsFacade.copyFile(remotePucsFileUri, dest);
        JadeFsFacade.delete(remotePucsFileUri);
    }

    /**
     * Permet de charger un fichier Pucs en passant l'adresse et le nom de celui-ci une
     * 
     * @param remotePucsFileUri Une chemin vers le fichier du type ftp://login:pass/pucs/atraiter/monfichier.xml
     * @return Objet représentant les caractéristique du fichier
     */
    private PucsFile buildPucsByFile(String remotePucsFileUri, BSession session) throws Exception {
        String path = JadeFsFacade.readFile(remotePucsFileUri);
        PucsFile pucsFile = buildPucsByFileBy(path, remotePucsFileUri, session);
        return pucsFile;
    }

    private PucsFile buildPucsByFileBy(String filePath, String remotePucsFileUri, BSession session) {
        PucsFile pucsFile = buildPucsByFile(filePath, DeclarationSalaireProvenance.SWISS_DEC, session);
        pucsFile.setId(JadeFilenameUtil.extractFilename(remotePucsFileUri).replace(".xml", ""));
        return pucsFile;

    }

    public static PucsFile buildPucsByFile(String filePath, DeclarationSalaireProvenance provenance, BSession session) {
        PucsFile pucsFile = new PucsFile();
        Closer closer = Closer.create();
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            closer.register(fileInputStream);
            ElementsDomParser parser = new ElementsDomParser(fileInputStream);

            double kilobytes = file.length() / 1024;
            pucsFile.setSizeFileInKo(kilobytes);
            pucsFile.setId(JadeFilenameUtil.extractFilename(filePath).replace(".xml", ""));
            pucsFile.setProvenance(provenance);

            DeclarationSalaire ds = DeclarationSalaireBuilder.builOnlyHead(parser);
            pucsFile.setAnneeDeclaration(String.valueOf(ds.getAnnee()));
            pucsFile.setNbSalaires(String.valueOf(ds.getNbSalaire()));
            pucsFile.setNomAffilie(ds.getNom());
            pucsFile.setNumeroAffilie(ds.getNumeroAffilie());
            pucsFile.setTotalControle(ds.getMontantAvs().toStringFormat());
            pucsFile.setDateDeReception(ds.getTransmissionDate().getSwissValue());
            pucsFile.setAfSeul(ds.isAfSeul());
            pucsFile.setForTest(ds.isTest());
            pucsFile.setCurrentStatus(EtatPucsFile.A_VALIDE);
            pucsFile.setDuplicate(ds.isDuplicate());
            pucsFile.setHandlingUser(session.getUserId());
            pucsFile.setFile(file);
            if (ds.isAfSeul()) {
                pucsFile.setTotalControle(ds.getMontantCaf().toStringFormat());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                closer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return pucsFile;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

}
