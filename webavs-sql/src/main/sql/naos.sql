-- Update des traduction en langue Allemande

UPDATE SCHEMA.FWCOUP SET PCOLUT='Nachfolge J�hrliche BVG' WHERE PCOSID = 6700013 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Nachfolge BGS' WHERE PCOSID = 6700010 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Verfolgung strukturierte AGA' WHERE PCOSID = 6700012 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Nachfolge J�hrliche BVG - Ausdruck Fragebogen' WHERE PCOSID = 6200075 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Nachfolge J�hrliche BVG - Ausdruck Mahnung' WHERE PCOSID = 6200076 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Nachfolge J�hrliche BVG - Ausdruck Aufforderung' WHERE PCOSID = 6200077 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Nachfolge J�hrliche BVG - Vorbereitung Anzeige' WHERE PCOSID = 6200078 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='AGA ST - Versand der strukturierten AGA' WHERE PCOSID = 6200069 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='AGA ST - Arbeitgeberkontrolle der strukturierten AGA' WHERE PCOSID = 6200072 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Ausdruck BGS Wiederholung' WHERE PCOSID = 6200058 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Mahnung BGS Wiederholung' WHERE PCOSID = 6200059 AND PLAIDE = 'D';
UPDATE SCHEMA.FWCOUP SET PCOLUT='Nicht erhaltene BGS Wiederholung behandeln' WHERE PCOSID = 6200060 AND PLAIDE = 'D';

--K181002_001 Impression de la lettre de bienvenue apr�s validation du serveur interactif
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL) VALUES ('naos.lettre.bienvenue.wait.interactive.validation','false');
