/**
 * 
 */
package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAnnonceSedexCOXML extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;
    private String idAnnonceSedexCOXML = null;
    private String idAnnonceSedex = null;
    private String messageId = null;
    private String xml = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdAnnonceSedexCOXML();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAnnonceSedexCOXML = id;
    }

    public String getIdAnnonceSedexCOXML() {
        return idAnnonceSedexCOXML;
    }

    public void setIdAnnonceSedexCOXML(String idAnnonceSedexCOXML) {
        this.idAnnonceSedexCOXML = idAnnonceSedexCOXML;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getIdAnnonceSedex() {
        return idAnnonceSedex;
    }

    public void setIdAnnonceSedex(String idAnnonceSedex) {
        this.idAnnonceSedex = idAnnonceSedex;
    }

}
