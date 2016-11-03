package ch.globaz.orion.businessimpl.services.pucs;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.fs.JadeFsFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.EtatPucsFile;
import ch.globaz.orion.business.models.pucs.PucsFile;
import com.google.common.io.Closer;

/**
 * Service de recherche des fichiers SwissDec
 */
public class FindPucsSwissDec {

    private BSession session;

    public FindPucsSwissDec(BSession session) {
        super();
        this.session = session;
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
        pucsFile.setFilename(JadeFilenameUtil.extractFilename(remotePucsFileUri).replace(".xml", ""));
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
            pucsFile.setFilename(JadeFilenameUtil.extractFilename(filePath).replace(".xml", ""));
            pucsFile.setProvenance(provenance);

            DeclarationSalaire ds = DeclarationSalaireBuilder.builOnlyHead(parser);
            pucsFile.setAnneeDeclaration(String.valueOf(ds.getAnnee()));
            pucsFile.setNbSalaires(String.valueOf(ds.getNbSalaire()));
            pucsFile.setNomAffilie(ds.getNom());
            pucsFile.setNumeroAffilie(ds.getNumeroAffilie());
            pucsFile.setTotalControle(ds.getMontantAvs().toStringFormat());
            if (ds.getTransmissionDate() != null) {
                pucsFile.setDateDeReception(ds.getTransmissionDate().getSwissValue());
            }
            pucsFile.setAfSeul(ds.isAfSeul());
            pucsFile.setForTest(ds.isTest());
            pucsFile.setCurrentStatus(EtatPucsFile.A_VALIDE);
            pucsFile.setDuplicate(ds.isDuplicate());
            pucsFile.setHandlingUser(session.getUserId());
            pucsFile.setFile(file);

            // List<AFAffiliation> affiliations = AFAffiliationServices.searchAffiliationByNumeros(
            // Arrays.asList(pucsFile.getNumeroAffilie().trim()), session);
            // if (affiliations.isEmpty()) {
            // pucsFile.setLock(true);
            // pucsFile.setIsAffiliationExistante(false);
            // } else {
            // pucsFile.setIsAffiliationExistante(true);
            // pucsFile.setLock(!PucsServiceImpl.userHasRight(affiliations.get(0), session));
            // }

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

}
