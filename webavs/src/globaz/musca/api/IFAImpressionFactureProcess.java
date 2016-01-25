package globaz.musca.api;

import globaz.globall.api.BIContainer;
import globaz.musca.external.IntModuleImpression;
import java.util.HashMap;

/**
 * @author dda
 */
public interface IFAImpressionFactureProcess {

    // public boolean _createDocument(IFAPrintManageDoc manager,
    // IntModuleImpression interface_moduleImpression);

    public boolean _createDocument(BIContainer container, IntModuleImpression interface_moduleImpression,
            String impressionClassName);

    public boolean _doWrapperDocument();

    public boolean _executeImpressionProcess(IFAPassage passage);

    public IntModuleImpression _getInterfaceImpressionFromCache(String nomClasse);

    public IFAModuleImpression _getModuleImpressionByCritere(IFAPrintDoc intEnteteFacture);

    public boolean _passageIsValid(IFAPassage passage);

    public boolean _passageModuleGenerate(IFAPassage passage);

    public void _setInterfaceImpressionFromCache(String nomClasse, IntModuleImpression module);

    public String getAImprimer();

    public String getDateImpression();

    public String getDocumentType();

    public String getFactureType();

    public String getFromIdExterneRole();

    public String getIdSousType();

    public String getIdTri();

    public String getIdTriDecompte();

    public String getImpressionClassName();

    public HashMap getInterfaceImpressionContainer();

    public String getLibelle();

    public HashMap getModImpMap();

    public int getNbImprimer();

    public int getSizeManager();

    public String getTillIdExterneRole();

    public boolean isFlagCodeMaj();

    public void setAImprimer(String string);

    public void setDateImpression(String newDateImpression);

    public void setDocumentType(String documentType);

    public void setFactureType(String string);

    public void setFlagCodeMaj(boolean newFlagCodeMaj);

    public void setFromIdExterneRole(String newFromIdExterneRole);

    public void setIdSousType(String newIdSousType);

    public void setIdTri(String newIdTri);

    public void setIdTriDecompte(String newIdTriDecompte);

    public void setImpressionClassName(String impressionClassName);

    public void setInterfaceImpressionContainer(HashMap newInterfaceImpressionContainer);

    public void setLibelle(String newLibelle);

    public void setModImpMap(HashMap newModImpMap);

    public void setNbImprimer(int newNbImprimer);

    public void setSizeManager(int newSizeManager);

    public void setTillIdExterneRole(String newTillIdExterneRole);
}
