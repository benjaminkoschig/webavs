-- [WEBAVS-7125] QR Facture : Ajout du param�tre
INSERT INTO SCHEMA.JADEPROP (PROPNAME,PROPVAL,CSPY,PSPY) VALUES ('common.qrFacture','false','20191209150736ccjuglo   ','20191209150736ccjuglo   ');

-- [WEBAVS-7476] INFOROM - S200212_004 Explications sur envoi donn�es au REE / OFS
UPDATE SCHEMA.JADEPROP
SET PROPVAL='01.01', PSPY='20200224150736mmo' where PROPNAME = 'pavo.annonce.periode.debut';