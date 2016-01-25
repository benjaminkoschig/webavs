package ch.globaz.common.business.models;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.babel.api.helper.ICTListeTextesImpl;
import globaz.babel.db.cat.CTDocumentAPIAdapter;
import globaz.babel.db.cat.CTTexte;
import globaz.globall.api.BISession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CTDocumentImpl implements ICTDocument {

    private HashMap<Integer, ICTListeTextesImpl> niveaux = new HashMap<Integer, ICTListeTextesImpl>();
    private CTDocumentAPIAdapter documentText;

    /**
     * Permet de créer une implémentation de ICTDocument
     * 
     * @param listTexte
     */
    public CTDocumentImpl(List<CTDocumentAPIAdapter> listTexte) {
        // regrouper les vo par niveau
        for (int id = 0; id < listTexte.size(); ++id) {
            CTTexte texte = listTexte.get(id);
            Integer niveau = new Integer(listTexte.get(id).getNiveau());
            ICTListeTextesImpl textes = niveaux.get(niveau);

            if (textes == null) {
                textes = new ICTListeTextesImpl();
                niveaux.put(niveau, textes);
            }
            textes.addTexte(createTexte(texte));
        }
        documentText = listTexte.get(0);
    }

    /**
     * Cette méthode permet de créer un texte de type ICTTexte
     * 
     * @param texte
     * @return un ICTTexte
     */
    private ICTTexte createTexte(final CTTexte texte) {
        return new ICTTexte() {

            @Override
            public void setISession(BISession newSession) {
            }

            @Override
            public boolean isNew() {
                return texte.isNew();
            }

            @Override
            public String getLastModifiedUser() {
                return texte.getLastModifiedUser();
            }

            @Override
            public String getLastModifiedTime() {
                return texte.getLastModifiedTime();
            }

            @Override
            public String getLastModifiedDate() {
                return texte.getLastModifiedDate();
            }

            @Override
            public String getId() {
                return texte.getId();
            }

            @Override
            public BISession getISession() {
                return null;
            }

            @Override
            public String getPosition() {
                return texte.getPosition();
            }

            @Override
            public String getNiveau() {
                return texte.getNiveau();
            }

            @Override
            public String getDescriptionBrut() {
                return texte.getDescription();
            }

            @Override
            public String getDescription() {
                return texte.getDescription();
            }

            @Override
            public String getCodeIsoLangue() {
                return texte.getCodeIsoLangue();
            }
        };

    }

    @Override
    public void setISession(BISession newSession) {
    }

    @Override
    public boolean isNew() {
        return isNew();
    }

    @Override
    public String getLastModifiedUser() {
        return null;
    }

    @Override
    public String getLastModifiedTime() {
        return null;
    }

    @Override
    public String getLastModifiedDate() {
        return null;
    }

    @Override
    public String getId() {
        return documentText.getIdDocument();
    }

    @Override
    public BISession getISession() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void setNomLike(String nom) {
    }

    @Override
    public void setNom(String nom) {
    }

    @Override
    public void setIdDocument(String idDocument) {
    }

    @Override
    public void setDefault(Boolean defaut) {
    }

    @Override
    public void setCsTypeDocument(String csTypeDocument) {
    }

    @Override
    public void setCsDomaine(String csDomaine) {
    }

    @Override
    public void setCsDestinataire(String csDestinataire) {
    }

    @Override
    public void setCodeIsoLangue(String codeIsoLangue) {
    }

    @Override
    public void setActif(Boolean actif) {
    }

    @Override
    public Map loadListeNoms() throws Exception {
        return null;
    }

    @Override
    public String getCodeIsoLangue() {

        return documentText.getCodeIsoLangue();
    }

    @Override
    public String getCsDestinataire() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCsDomaine() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCsEditable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCsTypeDocument() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDateDesactivation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getIdDocument() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNom() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNomLike() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ICTListeTextes getTextes(int idNiveau) {

        ICTListeTextes retValue = niveaux.get(new Integer(idNiveau));

        if (retValue == null) {
            throw new IndexOutOfBoundsException("pas de textes au niveau " + idNiveau);
        }

        return retValue;
    }

    @Override
    public Boolean isActif() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean isDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean isStyledDocument() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ICTDocument[] load() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}