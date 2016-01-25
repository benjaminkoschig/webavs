package ch.globaz.pegasus.businessimpl.utils.topazbuilder.util;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions.DecisionApresCalculBuilder;

/**
 * Outil de construction rapide d'un objet {@link JadePublishDocumentInfo} selon des param�tres souvent utilis�s. Il
 * permet des chainages de methodes.<br>
 * <br>
 * Usage: pour obtenir un pubInfo sans publish mais avec archivage ged et gestion recto verso sur la derni�re page <br>
 * <code>pubInfo = new {@link PegasusPubInfoBuilder}().ged().rectoVersoLast().getPubInfo();</code>
 * 
 * @author eco
 * 
 */
public class PegasusPubInfoBuilder {

    private JadePublishDocumentInfo _pubInfo = new JadePublishDocumentInfo();

    public PegasusPubInfoBuilder() {
        _pubInfo.setArchiveDocument(Boolean.FALSE);
        _pubInfo.setPublishDocument(Boolean.FALSE);
        _pubInfo.setDuplex(Boolean.FALSE);
    }

    /**
     * Active l'option d'archivage
     * 
     * @return le builder
     */
    public PegasusPubInfoBuilder ged() {
        _pubInfo.setArchiveDocument(Boolean.TRUE);
        return this;
    }

    /**
     * Retourne l'objet {@link JadePublishDocumentInfo} construit. Dans une construction en chainage de m�thode, c'est
     * g�n�ralement la derni�re m�thode appell�e. Elle peut toutefois �tre appell�e avant, mais toute modification par
     * la suite par le builder se repercutera sur le pubInfo g�n�r�.
     * 
     * @return l'objet {@link JadePublishDocumentInfo} construit
     */
    public JadePublishDocumentInfo getPubInfo() {
        return _pubInfo;
    }

    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    /**
     * Active l'option de publication ftp � la validation Force la d�sactivation de la pr�validation
     * 
     * @return
     */
    public PegasusPubInfoBuilder preValidFtp() {
        _pubInfo.setDocumentProperty(DecisionApresCalculBuilder.IS_FTP_PREVALID_AUTO, Boolean.TRUE.toString());
        _pubInfo.setDocumentProperty(DecisionApresCalculBuilder.IS_FTP_VALID_AUTO, Boolean.FALSE.toString());
        return this;
    }

    /**
     * Active l'option publish
     * 
     * @return le builder
     */
    public PegasusPubInfoBuilder publish() {
        _pubInfo.setPublishDocument(Boolean.TRUE);
        return this;
    }

    /**
     * Active les options duplex et DUPLEX_ON_FIRST
     * 
     * @return le builder
     */
    public PegasusPubInfoBuilder rectoVersoFirst() {
        _pubInfo.setDuplex(Boolean.TRUE);
        _pubInfo.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_FIRST);
        return this;
    }

    /**
     * Active les options duplex et DUPLEX_ON_LAST
     * 
     * @return le builder
     */
    public PegasusPubInfoBuilder rectoVersoLast() {
        _pubInfo.setDuplex(Boolean.TRUE);
        _pubInfo.setDuplexRule(JadePublishDocumentInfo.DUPLEX_ON_LAST);
        return this;
    }

    /**
     * Active l'option de publication ftp � la validation Force la d�sactivation de la pr�validation
     * 
     * @return
     */
    public PegasusPubInfoBuilder validFtp() {
        _pubInfo.setDocumentProperty(DecisionApresCalculBuilder.IS_FTP_VALID_AUTO, Boolean.TRUE.toString());
        _pubInfo.setDocumentProperty(DecisionApresCalculBuilder.IS_FTP_PREVALID_AUTO, Boolean.FALSE.toString());
        return this;
    }

}
