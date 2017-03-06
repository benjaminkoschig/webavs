package ch.globaz.common.listoutput;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import java.util.Locale;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslaterNormal;
import ch.globaz.simpleoutputlist.configuration.Configuration;
import ch.globaz.simpleoutputlist.configuration.FontStyle;
import ch.globaz.simpleoutputlist.configuration.RowDecorator;
import ch.globaz.simpleoutputlist.configuration.RowStyle;
import ch.globaz.simpleoutputlist.outimpl.Configurations;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class SimpleOutputListBuilderJade extends SimpleOutputListBuilder {

    private boolean isContextInizialised = false;
    private BSession session;

    public SimpleOutputListBuilderJade() {
        super();
        session = BSessionUtil.getSessionFromThreadContext();
        if (session != null) {
            local(new Locale(session.getIdLangueISO()));
        }
    }

    public SimpleOutputListBuilderJade session(BSession session) {
        this.session = session;
        local(new Locale(session.getIdLangueISO()));
        return this;
    }

    public SimpleOutputListBuilderJade initializeContext(BSession session) {
        try {
            BSessionUtil.initContext(session, this);
            isContextInizialised = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public static SimpleOutputListBuilderJade newInstance() {
        return new SimpleOutputListBuilderJade();
    }

    public SimpleOutputListBuilderJade outputNameAndAddPath(String name) {
        String path = Jade.getInstance().getPersistenceDir() + name + "_" + JadeUUIDGenerator.createStringUUID();
        outputName(path);
        return this;
    }

    @Override
    public void close() {
        if (isContextInizialised) {
            BSessionUtil.stopUsingContext(this);
        }
    }

    @Override
    public SimpleOutputListBuilderJade configure(Configuration configuration) {
        super.configure(configuration);
        return this;
    }

    public SimpleOutputListBuilderJade addTranslater() {
        translater(new LabelTranslaterNormal(session, null));
        return this;
    }

    public SimpleOutputListBuilderJade addTranslater(String identifier) {
        translater(new LabelTranslaterNormal(session, identifier));
        return this;
    }

    public SimpleOutputListBuilderJade globazTheme() {
        String font = "Helvetica";
        Configuration configuration = Configurations.buildeDefault();
        configuration.setDocumentMargin(20, 20, 20, 30);
        configuration.setHeaderFont(9, "Times New Roman", FontStyle.BOLD, 0, 0, 0);
        configuration.setHeaderBackGroundColor(222, 222, 222);
        configuration.setRowFont(9, font, FontStyle.NORMAL, 0, 0, 0);
        // configuration.setRow
        configuration.setRowBorderColor(255, 255, 255);
        configuration.setTitreFont(13, font, FontStyle.BOLD, 0, 0, 0);
        configuration.setRowDecorator(new RowDecorator<Object>() {
            @Override
            public RowStyle decorat(Object objet, int rowNum, int size) {
                final RowStyle rowStyle = new RowStyle();
                if (rowNum % 2 == 0) {
                    rowStyle.setBackGroundColor(245, 245, 245);
                    rowStyle.setBorderColor(245, 245, 245);
                    // rowStyle.setFont(size, familly, fontStyle, red, green, blue);
                }
                return rowStyle;
            }
        });
        configure(configuration);
        return this;
    }
}
