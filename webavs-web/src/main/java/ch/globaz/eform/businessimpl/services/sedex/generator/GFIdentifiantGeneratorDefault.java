package ch.globaz.eform.businessimpl.services.sedex.generator;

import globaz.jade.client.util.JadeUUIDGenerator;

import java.util.UUID;

public class GFIdentifiantGeneratorDefault implements GFIdentifiantSedexGenerator{
    @Override
    public String generateMessageId() {
        return generateUUID();
    }

    @Override
    public String generateReferenceMessageId() {
        return generateUUID();
    }

    @Override
    public String generateBusinessProcessId() {
        return generateUUID();
    }

    @Override
    public String generateBusinessReferenceId() {
        return JadeUUIDGenerator.createLongUID().toString();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
