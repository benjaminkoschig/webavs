--S170906_001 D0204-1 Avenant: Am�lioration du contr�le LAA/LPP
CREATE TABLE SCHEMA.AF_SUIVI_LPP_ANN_SALARIES
(
   ID decimal(15,0) PRIMARY KEY NOT NULL,
   ID_AFFILIATION decimal(15,0) NOT NULL,
   NUMERO_AFFILIE char(15) NOT NULL,
   NSS char(20) NOT NULL,
   NOM_SALARIE char(80),
   MOIS_DEBUT decimal(2,0),
   MOIS_FIN decimal(2,0),
   ANNEE decimal(4,0) NOT NULL,
   SALAIRE decimal(15,2) NOT NULL,   
   NIVEAU_SECURITE decimal(9,0),
   PSPY varchar(24) NOT NULL,
   CSPY varchar(24) NOT NULL
);



DELETE FROM SCHEMA.CTTEXTES WHERE CDIELE IN (SELECT CCIELE FROM SCHEMA.CTELEMEN WHERE CCIDOC IN (SELECT CBIDOC FROM SCHEMA.CTDOCUME WHERE CBITYD IN (SELECT CAITYD FROM SCHEMA.CTTYPDOC WHERE CATTYP=836400 AND CATDOM=835003)));
DELETE FROM SCHEMA.CTELEMEN WHERE CCIDOC IN (SELECT CBIDOC FROM SCHEMA.CTDOCUME WHERE CBITYD IN (SELECT CAITYD FROM SCHEMA.CTTYPDOC WHERE CATTYP=836400 AND CATDOM=835003));
DELETE FROM SCHEMA.CTDOCUME WHERE CBITYD IN (SELECT CAITYD FROM SCHEMA.CTTYPDOC WHERE CATTYP=836400 AND CATDOM=835003);



--

-- Dump de catalogue de texte: 30 oct. 2017 15:33:54
--
-- idTypeDocument		= 18000
-- csDomaine			= 835003
-- csTypeDocument		= 836400
-- nomDocument			= Contr�le LPP
-- borneInferieure		= 71500000005
--
-- - Les identifiants de tous les enregistrements sont recalcul�s
-- - La cha�ne 0 doit �tre remplac�e par la valeur du premier identifiant libre dans la base pour ce type de document
-- - La cha�ne 0 doit �tre remplac�e par la valeur du premier identifiant libre dans la base pour ce type de document
-- - La cha�ne 0 �tre remplac�e par la valeur du premier identifiant libre dans la base pour ce type de document

--

INSERT INTO SCHEMA.CTDOCUME (CBITYD, CBIDOC, CBDDES, CBBACT, CBLNOM, CBTEDI, CBTDES, CBBDEF, CBBSTY, PSPY) VALUES (18000, 71500000005+0+1, 0, '1', 'Contr�le LPP', 34000001, 34005001, '1', '2', '20171027103343ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+1, 1, 1, 'Titre', '2', '2', '2', '20171027103838ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+1, 71500000005+0+1, 'de', '[DE]Contr�le de l�affiliation en mati�re de pr�voyance professionnelle (LPP)', '20171027103838ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+1, 71500000005+0+2, 'fr', 'Contr�le de l�affiliation en mati�re de pr�voyance professionnelle (LPP)', '20171027103838ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+1, 71500000005+0+3, 'it', '', '20171027103838ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+2, 1, 2, '', '2', '2', '2', '20171027112342ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+2, 71500000005+0+4, 'de', '{0},', '20171027112342ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+2, 71500000005+0+5, 'fr', '{0},', '20171027112342ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+2, 71500000005+0+6, 'it', '', '20171027112342ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+3, 1, 3, '', '2', '2', '2', '20171030151307ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+3, 71500000005+0+7, 'de', '[DE]Conform�ment � la loi f�d�rale du 25 juin 1982 sur la pr�voyance professionnelle vieillesse, survivants et invalidit� (LPP), tous les employeurs occupant des salari�s soumis � l�assurance obligatoire doivent �tre affili�s � une institution de pr�voyance inscrite dans le registre de la pr�voyance professionnelle <style isItalic=�true�>(art. 11, al.1 LPP)</style>. Les Caisses de compensation AVS sont charg�es du contr�le de ces dispositions.', '20171030151307ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+3, 71500000005+0+8, 'fr', 'Conform�ment � la loi f�d�rale du 25 juin 1982 sur la pr�voyance professionnelle vieillesse, survivants et invalidit� (LPP), tous les employeurs occupant des salari�s soumis � l�assurance obligatoire doivent �tre affili�s � une institution de pr�voyance inscrite dans le registre de la pr�voyance professionnelle <style isItalic=�true�>(art. 11, al.1 LPP))</style>. Les Caisses de compensation AVS sont charg�es du contr�le de ces dispositions.', '20171030151307ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+3, 71500000005+0+9, 'it', '', '20171030151307ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+4, 1, 4, '', '2', '2', '2', '20171030130631ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+4, 71500000005+0+10, 'de', '[DE]Or vous avez occup� des salari�s soumis � l�assurance obligatoire, ainsi qu�en t�moigne l�extrait de votre (vos) d�claration(s) et ne nous avez pas remis l�attestation de votre assureur LPP.', '20171030130631ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+4, 71500000005+0+11, 'fr', 'Or vous avez occup� des salari�s soumis � l�assurance obligatoire, ainsi qu�en t�moigne l�extrait de votre (vos) d�claration(s) et ne nous avez pas remis l�attestation de votre assureur LPP.', '20171030130631ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+4, 71500000005+0+12, 'it', '', '20171030130631ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+5, 1, 5, '', '2', '2', '2', '20171030150449ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+5, 71500000005+0+13, 'de', '[DE]Si vous n�avez pas encore conclu un tel contrat, vous �tes en situation de violation de la LPP. Nous vous invitons donc � r�gulariser cette situation avec effet r�troactif dans les plus brefs d�lais et � nous retourner la pr�sente d�ment compl�t�e, <style isBold=�true�>en y joingnant une attestation de votre assureur LPP.</style>', '20171030150449ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+5, 71500000005+0+14, 'fr', 'Si vous n�avez pas encore conclu un tel contrat, vous �tes en situation de violation de la LPP. Nous vous invitons donc � r�gulariser cette situation avec effet r�troactif dans les plus brefs d�lais et � nous retourner la pr�sente d�ment compl�t�e, <style isBold=�true�>en y joingnant une attestation de votre assureur LPP.</style>', '20171030150449ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+5, 71500000005+0+15, 'it', '', '20171030150449ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+6, 1, 6, '', '2', '2', '2', '20171030130610ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+6, 71500000005+0+16, 'de', '[DE]Toutefois, si vous estimez que l�un de vos salari�s d�passant la limite LPP n�est pas soumis pour son engagement dans votre entreprise, vous voudrez bien nous en indiquer les raisons.', '20171030130610ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+6, 71500000005+0+17, 'fr', 'Toutefois, si vous estimez que l�un de vos salari�s d�passant la limite LPP n�est pas soumis pour son engagement dans votre entreprise, vous voudrez bien nous en indiquer les raisons.', '20171030130610ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+6, 71500000005+0+18, 'it', '', '20171030130610ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+7, 1, 7, '', '2', '2', '2', '20171030130600ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+7, 71500000005+0+19, 'de', '[DE]Dans l�attente de votre r�ponse, nous vous prions d�agr�er, {0}, l�expression de nos sentiments distingu�s.', '20171030130600ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+7, 71500000005+0+20, 'fr', 'Dans l�attente de votre r�ponse, nous vous prions d�agr�er, {0}, l�expression de nos sentiments distingu�s.', '20171030130600ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+7, 71500000005+0+21, 'it', '', '20171030130600ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+8, 1, 8, '', '2', '2', '2', '20171030130548ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+8, 71500000005+0+22, 'de', '[DE]SERVICE AFFILIATION/COTISATION', '20171030130548ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+8, 71500000005+0+23, 'fr', 'SERVICE AFFILIATION/COTISATION', '20171030130548ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+8, 71500000005+0+24, 'it', '', '20171030130548ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+9, 1, 9, '', '2', '2', '2', '20171030142932ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+9, 71500000005+0+25, 'de', '[DE]Annexe', '20171030142932ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+9, 71500000005+0+26, 'fr', 'Annexe', '20171030142932ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+9, 71500000005+0+27, 'it', '', '20171030142932ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+10, 1, 10, '', '2', '2', '2', '20171030142952ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+10, 71500000005+0+28, 'de', '[DE] : extrait(s) de votre (vos) d�claration(s) des salaires', '20171030142952ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+10, 71500000005+0+29, 'fr', ': extrait(s) de votre (vos) d�claration(s) des salaires', '20171030142952ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+10, 71500000005+0+30, 'it', '', '20171030142952ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+11, 1, 11, '', '2', '2', '2', '20171030130510ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+11, 71500000005+0+31, 'de', '[DE]L�employeur soussign� d�clare �tre affili� pour son personnel aupr�s de l�institution de pr�voyance professionnelle suivante :', '20171030130510ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+11, 71500000005+0+32, 'fr', 'L�employeur soussign� d�clare �tre affili� pour son personnel aupr�s de l�institution de pr�voyance professionnelle suivante :', '20171030130510ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+11, 71500000005+0+33, 'it', '', '20171030130510ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+12, 1, 12, '', '2', '2', '2', '20171030143023ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+12, 71500000005+0+34, 'de', '[DE]...............................................................................................................................................................', '20171030143023ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+12, 71500000005+0+35, 'fr', '...............................................................................................................................................................', '20171030143023ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+12, 71500000005+0+36, 'it', '', '20171030143023ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+13, 1, 13, '', '2', '2', '2', '20171030130441ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+13, 71500000005+0+37, 'de', '[DE]*)', '20171030130441ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+13, 71500000005+0+38, 'fr', '*)', '20171030130441ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+13, 71500000005+0+39, 'it', '', '20171030130441ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+14, 1, 14, '', '2', '2', '2', '20171030143946ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+14, 71500000005+0+40, 'de', '[DE]D�s le : ....................................................................', '20171030143946ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+14, 71500000005+0+41, 'fr', 'D�s le : ....................................................................', '20171030143946ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+14, 71500000005+0+42, 'it', '', '20171030143946ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+15, 1, 15, '', '2', '2', '2', '20171030143557ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+15, 71500000005+0+43, 'de', '[DE]Date : .....................................................................', '20171030143557ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+15, 71500000005+0+44, 'fr', 'Date : ......................................................................', '20171030143557ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+15, 71500000005+0+45, 'it', '', '20171030143557ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+16, 1, 16, '', '2', '2', '2', '20171030130411ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+16, 71500000005+0+46, 'de', '[DE]Signature : ..................................', '20171030130411ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+16, 71500000005+0+47, 'fr', 'Signature : ..................................', '20171030130411ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+16, 71500000005+0+48, 'it', '', '20171030130411ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+17, 1, 17, '', '2', '2', '2', '20171030130401ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+17, 71500000005+0+49, 'de', '[DE]NIP :', '20171030130401ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+17, 71500000005+0+50, 'fr', 'NIP :', '20171030130401ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+17, 71500000005+0+51, 'it', '', '20171030130401ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+18, 1, 18, '', '2', '2', '2', '20171030142900ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+18, 71500000005+0+52, 'de', '[DE]Attention', '20171030142900ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+18, 71500000005+0+53, 'fr', 'Attention', '20171030142900ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+18, 71500000005+0+54, 'it', '', '20171030142900ccjuglo');

INSERT INTO SCHEMA.CTELEMEN (CCIDOC, CCIELE, CCNPOS, CCNNIV, CCBDES, CCBSEL, CCBSBD, CCBEDI, PSPY) VALUES (71500000005+0+1, 71500000005+0+19, 1, 19, '', '2', '2', '2', '20171030142918ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+19, 71500000005+0+55, 'de', '[DE] : il est indispensable de joindre l�attestation de l�assureur LPP', '20171030142918ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+19, 71500000005+0+56, 'fr', ': il est indispensable de joindre l�attestation de l�assureur LPP', '20171030142918ccjuglo');
INSERT INTO SCHEMA.CTTEXTES (CDIELE, CDITXT, CDLCIL, CDLDES, PSPY) VALUES (71500000005+0+19, 71500000005+0+57, 'it', '', '20171030142918ccjuglo');

--
-- Insertion des types de documents dans la table CTTYPDOC
--

INSERT INTO SCHEMA.CTTYPDOC (CAITYD,CATDOM,CATTYP,PSPY,CANBIN) VALUES (18000,835003,836400,'',71500000005);

--
-- Requ�tes de mise � jour pour FWINCP --
--


--
-- Type de document: 836400
--

DELETE FROM SCHEMA.FWINCP WHERE PINCID = 'CTDOCUME' AND PCOSID = 836400;
INSERT INTO SCHEMA.FWINCP (PINCID,PCOSID,PINCAN,PINCLI,PINCVA,PSPY) VALUES ('CTDOCUME',836400,0,'',0,'');
UPDATE SCHEMA.FWINCP SET PINCVA= ((SELECT MAX(CBIDOC) FROM SCHEMA.CTDOCUME WHERE CBITYD = 18000) - (SELECT DISTINCT min(CANBIN) FROM SCHEMA.CTTYPDOC WHERE CATTYP = 836400 and  CATDOM = 835003)) WHERE PCOSID= 836400 AND PINCID= 'CTDOCUME';
DELETE FROM SCHEMA.FWINCP WHERE PINCID = 'CTELEMEN' AND PCOSID = 836400;
INSERT INTO SCHEMA.FWINCP (PINCID,PCOSID,PINCAN,PINCLI,PINCVA,PSPY) VALUES ('CTELEMEN',836400,0,'',0,'');
UPDATE SCHEMA.FWINCP SET PINCVA= ((SELECT MAX(CCIELE) FROM SCHEMA.CTELEMEN WHERE CCIDOC IN (SELECT CBIDOC FROM SCHEMA.CTDOCUME WHERE CBITYD = 18000)) - (SELECT DISTINCT min(CANBIN) FROM SCHEMA.CTTYPDOC WHERE CATTYP = 836400 and  CATDOM = 835003)) WHERE PCOSID= 836400 AND PINCID= 'CTELEMEN';
DELETE FROM SCHEMA.FWINCP WHERE PINCID = 'CTTEXTES' AND PCOSID = 836400;
INSERT INTO SCHEMA.FWINCP (PINCID,PCOSID,PINCAN,PINCLI,PINCVA,PSPY) VALUES ('CTTEXTES',836400,0,'',0,'');
UPDATE SCHEMA.FWINCP SET PINCVA= ((SELECT MAX (CDITXT) FROM SCHEMA.CTTEXTES WHERE CDIELE IN (SELECT CCIELE FROM SCHEMA.CTELEMEN WHERE CCIDOC IN (SELECT CBIDOC FROM SCHEMA.CTDOCUME WHERE CBITYD = 18000))) - (SELECT DISTINCT min(CANBIN) FROM SCHEMA.CTTYPDOC WHERE CATTYP = 836400 and  CATDOM = 835003)) WHERE PCOSID= 836400 AND PINCID= 'CTTEXTES';

INSERT INTO SCHEMA.FWCOSP (pcosid,pptygr,pconcs,pptycn,pptycl,pptysa,pcosli,pcosdf,pcosdm,pcosdp,pcoian,pcoide,pcodfi,pcoitc,pcoise, pspy) values ( 836400, 'VETYPDOCS', 1,1,0,0, 'LPP-Contr�le', 2,1,2,2,2,2,0,0,(replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user)); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 836400, 'F', '1', 'LPP-Contr�le', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) ); 
INSERT INTO SCHEMA.FWCOUP (pcosid,plaide,pcouid,pcolut, pspy) values ( 836400, 'D', '1', '[DE]LPP-Contr�le', (replace(char(current date), '-', '') concat replace(char(current time), '.', '') concat user) );
