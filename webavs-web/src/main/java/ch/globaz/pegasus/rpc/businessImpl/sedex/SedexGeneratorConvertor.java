package ch.globaz.pegasus.rpc.businessImpl.sedex;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.pegasus.rpc.businessImpl.converter.Converter;
import ch.globaz.pegasus.rpc.domaine.RpcData;

public class SedexGeneratorConvertor<T> implements Closeable {
    private final SedexMessageSender<T> sedex;
    private final Converter<RpcData, T> converter;

    public SedexGeneratorConvertor(SedexMessageSender<T> sedex, Converter<RpcData, T> converter) {
        this.sedex = sedex;
        this.converter = converter;
    }

    public void sendMessages() {
        sedex.sendMessages();
    }

    public File generateMessageFile(RpcData data) throws ValidationException {
        String nom = data.resolveDecisionsRequerant().list().get(0).getDecision().getTiersBeneficiaire().getNom();
        String prenom = data.resolveDecisionsRequerant().list().get(0).getDecision().getTiersBeneficiaire().getPrenom();
        return sedex.generateMessageFile(converter.convert(data), nom, prenom);
    }

    public File generateMessageFile(T t, RpcData data) throws ValidationException {
        String nom = data.resolveDecisionsRequerant().list().get(0).getDecision().getTiersBeneficiaire().getNom();
        String prenom = data.resolveDecisionsRequerant().list().get(0).getDecision().getTiersBeneficiaire().getPrenom();
        return sedex.generateMessageFile(t, nom, prenom);
    }

    public File generateMessageFileWithValidation(RpcData data) throws ValidationException {
        T content = this.convert(data);
        this.validate(content);
        return generateMessageFile(content, data);
    }

    public void validate(T t) throws ValidationException {
        this.sedex.validate(t);
    }

    public T convert(RpcData rpcData) {
        return this.converter.convert(rpcData);
    }

    @Override
    public void close() throws IOException {
        sedex.close();
    }

    public SedexInfo getSedexInfo() {
        return sedex.getSedexInfo();
    }

}
