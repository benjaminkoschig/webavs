package ch.globaz.babel.businessimpl.services;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.globall.api.BISession;
import java.util.Map;
import ch.globaz.jade.business.models.Langues;

class Document implements ICTDocument {

    private String id;
    private String codeIsoLangue;

    public Document(String id, String codeIsoLangue) {
        this.id = id;
        this.codeIsoLangue = codeIsoLangue;
    }

    public Document() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BISession getISession() {
        return null;
    }

    @Override
    public String getLastModifiedDate() {
        return null;
    }

    @Override
    public String getLastModifiedTime() {

        return null;
    }

    @Override
    public String getLastModifiedUser() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public void setISession(BISession newSession) {

    }

    @Override
    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    @Override
    public String getCsDestinataire() {
        return null;
    }

    @Override
    public String getCsDomaine() {
        return null;
    }

    @Override
    public String getCsEditable() {
        return null;
    }

    @Override
    public String getCsTypeDocument() {
        return null;
    }

    @Override
    public String getDateDesactivation() {
        return null;
    }

    @Override
    public String getIdDocument() {
        return null;
    }

    @Override
    public String getNom() {
        return null;
    }

    @Override
    public String getNomLike() {
        return null;
    }

    @Override
    public ICTListeTextes getTextes(int idNiveau) {
        return null;
    }

    @Override
    public Boolean isActif() {
        return null;
    }

    @Override
    public Boolean isDefaut() {
        return null;
    }

    @Override
    public Boolean isStyledDocument() {
        return null;
    }

    @Override
    public ICTDocument[] load() throws Exception {
        ICTDocument[] documents = new Document[1000];
        for (int i = 0; i < 1000; i++) {
            documents[i] = new Document(String.valueOf(i), Langues.Francais.getCodeIso());
        }
        return documents;
    }

    @Override
    public Map loadListeNoms() throws Exception {
        return null;
    }

    @Override
    public void setActif(Boolean actif) {

    }

    @Override
    public void setCodeIsoLangue(String codeIsoLangue) {

    }

    @Override
    public void setCsDestinataire(String csDestinataire) {

    }

    @Override
    public void setCsDomaine(String csDomaine) {

    }

    @Override
    public void setCsTypeDocument(String csTypeDocument) {

    }

    @Override
    public void setDefault(Boolean defaut) {

    }

    @Override
    public void setIdDocument(String idDocument) {

    }

    @Override
    public void setNom(String nom) {

    }

    @Override
    public void setNomLike(String nom) {

    }

    @Override
    public int size() {
        return 0;
    }

}
