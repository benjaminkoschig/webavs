
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>What's new - Quoi de neuf (Archive)</TITLE>
</HEAD>
<BODY>
<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title">What's new ! Archives</TH>
        </TR>
        <TR>
        <TD bgcolor="#B3C4DB">
        Vous trouverez ci-dessous les extraits des fonctionnalit�s des versions ant�rieures. Pour la liste d�taill�e des modifications, ainsi que la liste des bugs corrig�s, pri�re de se r�f�rer � l'historique des <A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>release_notes/history/releasenoteshistory.html" target="_blank">releases notes...</A>
        </TD>
        </TR>
        <TR><TD bgcolor="#B3C4DB">&nbsp;</TD></TR>
        <TR><TD bgcolor="#B3C4DB">Archives WebAVS</TD></TR>
        <TR>
            <TD bgcolor="#B3C4DB">
            <ul>
            <li><a href="#1-5-03">Version 1-5-03</li>
            <li><a href="#1-5-02">Version 1-5-02</li>
            <li><a href="#1-5-01">Version 1-5-01</li>
            <li><a href="#1-5-00">Version 1-5-00</li>
            <li><a href="#1-4-13">Version 1-4-13</li>
            <li><a href="#1-4-12">Version 1-4-12</li>
            <li><a href="#1-4-11">Version 1-4-11</li>
            <li><a href="#1-4-10">Version 1-4-10</li>
            </ul>
            </TD>
        </TR>
</TABLE>
<TABLE class="find" cellspacing="0">

<TR>
            <TH class="title" id="#1-5-03">What's new ? Quoi de neuf dans l'application WebAVS version 1-5-03 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB" height="600" valign="top">
<h5>1. LTN : attestations fiscales et liste aux autorit�s fiscales (standard INFOROM)</h5>
<h5>2. Liste cumul�e des cotisations par p�riode et par ann�e (standard INFOROM)</h5>
<h5>3. Etudiants : nouveau format EPFL/UNIL (sp�cifique, agence de Lausanne)</h5>
<h5>4. Nouvel encha�nement des masques pour la saisie d'un plan de paiement (standard INFOROM)</h5>
<h5>5. Traduction en allemand des masques de la partie "Cotisation" (standard INFOROM)</h5>
<h5>6. Champ "remarque" pour les contr�les d'employeurs (standard, INFOROM)</h5>
<h5>7. Nouveaux comptes OFAS 380.5381, 380.5382, 910.6353 (standard INFOROM)</h5>
<h5>8. Inforom057 : date de valeur et date de comptabilisation sur extraits de compte (standard INFOROM)</h5>

            </TD>
        </TR>
</TABLE>
        <TR>
            <TH class="title" id="#1-5-02">What's new ? Quoi de neuf dans l'application WebAVS version 1-5-02 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB" height="600" valign="top">
 <p>
<h5>2.1.	LTN, lot no 2 (Standard, INFOROM)</h5>
Cette deuxi�me livraison LTN permet les fonctionnalit�s suivantes :<br/>
-	Impression d'une d�claration de salaire de type LTN avec lettre d'accompagnement particuli�re<br/>
-	Saisie d'une d�claration de salaire de type LTN avec calcul et facturation de l'imp�t � la source selon le bar�me f�d�ral et cantonal<br/>
-	Sommation pour non paiement du d�compte LTN, puis blocage de la proc�dure (pas de poursuite engag�e)<br/>

<p>
<h5>2.2.	APG / Maternit� (Standard, INFOROM)</h5>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les APG / Maternit�.<br/>
</p>
<p>
<h5>2.3.	IJAI (Standard, INFOROM)</h5>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les IJAI.<br/>

Dans les d�cisions IJAI, l'adresse de recours sera prise dans les administrations de type "Autorit�s de recours" au lieu de "Tribunal des assurances sociales" jusqu'� pr�sent. Comme cela, vous n'aurez pas � maintenir les adresses de tribunaux � 2 place diff�rentes.<br/>
</p>
<p>
<h5>2.4.	Optimisation des performances d'impression de la facturation (Standard, INFOROM)</h5>
Le processus d'impression de la facturation a �t� optimis�. Un gain de performance significatif peut �tre attendu lors de la g�n�ration des pdf. Le gain d�pend de la plateforme d'ex�cution de l'application. La cadence varie entre 8'000 et 13'000 factures � l'heure selon les environnements et les plateformes.<br/>
</p>
<p>
<h5>2.5.	Impression des factures de plus d'une page (Standard, INFOROM)</h5>
Les factures de plus d'une page sont d�sormais imprim�es s�par�ment pour permettre une mise sous pli manuelle. S'il n'y a pas d'adresse valable au niveau du tiers, la facture est �galement imprim�e s�par�ment.<br/>
</p>
<p>
<h5>2.6.	Facturation des b�n�ficiaires PC (Sp�cifique, agence de Lausanne)</h5><br/>
La fonctionnalit� de facturation des quittances pour b�n�ficiaires PC est d�sormais fonctionnelle.<br/>
</p>
<p>
<h5>2.7.	InfoRom027 - CAI-00014 - Inscription CI, charger 20 �critures dans l'ascenseur (Standard, Inforom)</h5>
Les 20 derni�res inscriptions (au lieu des 5 derni�res) sont d�sormais affich�es au niveau du masque de d�tail d'une inscription CI.<br/>
</p>
<p>
<h5>2.8.	Nouveau masque de visualisation des ordres de versement (Standard, Inforom)</h5>
Ce nouveau masque permet de consulter les ordres versement en cours ou vers�s au niveau du compte annexe. Il permet �galement de bloquer un ordre non encore ex�cut�.<br/>
</p>
<p>
<h5>2.9.	Inforom031: Automatisation de la lecture des BVR (Standard, Inforom)</h5>
Cette nouvelle fonctionnalit� permet de traiter automatiquement les BVR disponibles sur le serveur de Postfinance. <br/>

Le syst�me se connecte durant la nuit, t�l�charge les BVR non encore trait�s, ouvre un journal en comptabilit� auxiliaire et transmet le r�sultat au responsable du traitement des BVR. S'il n'y a pas d'erreur � traiter, le journal est automatiquement comptabilis�.<br/>
</p>
<p>
<h5>2.10.	Inforom032: FSFP, lot no 2 (Standard, Inforom)</h5>
Cette nouvelle fonctionnalit� a �t� introduite dans le cadre de la nouvelle loi jurassienne sur la FSFP. Elle permet les options suivantes :<br/>
-	impression de la liste des factures en poursuites ou irr�couvrable<br/>
-	d�compte annuel FSFP<br/>

Ces listes ont �t� par la m�me occasion g�n�ralis�es � d'autres secteurs de type � autres t�ches �. <br/>
</p>
<p>
<h5>2.11.	Situation familiale (Standard, Inforom)</h5>
Des liens ont �t� cr��s pour arriver directement dans le tiers.<br/>
</p>

<p>
<h5>2.1.	APG / Maternit� (standard)</h5><br/>
Un message d'avertissement s'affiche si l'utilisateur ins�re un salaire de plus de CHF 100.00 dans le champ � salaire horaire �.<br/>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les APG / Maternit�.<br/>
</p>

<p>
<h5>2.2.	Situation familiale (standard)</h5><br/>
Des liens ont �t� cr��s pour arriver directement dans le tiers.<br/>
</p>

<p>
<h5>2.3.    IJAI (standard)</h5><br/>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les IJAI.<br/>
Dans les d�cisions IJAI, l'adresse de recours sera prise dans les administrations de type "Autorit�s de recours" au lieu de "Tribunal des assurances sociales" jusqu'� pr�sent. Comme cela, vous n'aurez pas � maintenir les adresses de tribunaux � 2 place diff�rentes.<br/>
</p>
            </TD>
        </TR>
</TABLE>

<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title" id="#1-5-01">What's new ? Quoi de neuf dans l'application WebAVS version 1-5-01 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB" height="600" valign="top">
			<p><I>Vous trouverez ci-dessous un extrait des nouvelles fonctionnalit�s de cette version. Pour la liste d�taill�e des modifications, ainsi que la liste des bugs corrig�s, pri�re de se r�f�rer � la <A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>release_notes/globaz_webavs_release_notes.pdf" target="_blank">release note...</A></I></p>
			<p>Vous pouvez �galement consulter les <A href="whatsnewArchive.jsp">archives</A> des anciennes versions.</p>
<p>
<h5>2.1.	Contr�le d'employeurs, lot 3 (standard, Inforom)</h5><br/>
Le dernier lot de fonctionnalit�s pour le contr�le d'employeur est livr�.<br/>
-	statistiques OFAS<br/>
-	lettres avec paragraphe " libre "<br/>
-	lettre d'avertissement du prochain contr�le<br/>

</p>

<p>
<h5>2.2.	D�cision de remises de cotisations personnelles (facultatif, CCGC)</h5><br/>
Les remises de cotisations personnelles peuvent �tre encod�es avec la commune � laquelle la remise doit �tre factur�e. Le traitement et la comptabilisation des remises a �galement �t� r�actualis�. Les sorties avec remises sont d�sormais �galement g�r�es.

Cette fonctionnalit� doit �tre configur�e avant son utilisation.
</p>
<p>
<h5>2.3.    Refonte du masque de saisie des �critures en comptabilit� g�n�rale (standard, Inforom)</h5><br/>
Le masque de saisie des �critures provoquait parfois et dans certaines conditions des erreurs dans les soldes. La saisie des �critures collectives n'�tait �galement pas tr�s pratique. Le masque donc a �t� enti�rement revu. 

Il permet de saisir sur un seul masque des �critures doubles ou collectives avec contr�le du solde. L'ancien masque de saisie des �critures collectives est supprim�.

Veuillez vous reporter au manuel utilisateur pour la prise en main de ce nouveau masque.

</p>
<p>
<h5>2.4.	Nouveau contentieux, lot de fonctionnalit�s FER (standard, Inforom)</h5><br/>
Un important lot de fonctionnalit�s pour la mise en production de la FER a �t� livr�. Il constitue l'essentiel des nouveaut�s de cette version. Comme le module " nouveau contentieux " n'est pour l'instant disponible qu'� la FER et � la FVE, nous ne d�taillerons pas ici ces fonctionnalit�s.
</p>

<p>
<h5>2.5.	M�morisation des crit�res de recherches dans le bilan, PP et soldes (standard, Inforom)</h5><br/>
Les crit�res de recherches des masques " bilan ", " pertes et profits " et " soldes " sont d�sormais m�moris�s pour �viter leur perte lors de l'utilisation de la fonction " retour arri�re " ou " back ".
</p>

<p>
<h5>2.6.	Affili�s RMCAS (sp�cifique, CCGC)</h5><br/>
Les affili�s " RMCAS " sont trait�s comme les r�fugi�s et assist�s. Leurs cotisations sont prises en charge au moment de la facturation.

</p>
<p>
<h5>2.7.	Recherche d'un compte gr�ce � son libell� (standard, Inforom)</h5><br/>
Un compte de comptabilit� g�n�rale peut �tre recherch� avec une partie de son libell�. La recherche est sensible aux majuscules et minuscules (" AVS " n'est pas �gale � " avs " par exemple).
</p>

<p>
<h5>2.8.	Choix d'un mod�le d'ent�te de lettre personnalis� pour l'impression des CA (facultatif, Inforom)</h5><br/>
Il est possible de configurer un mod�le personnalis� pour l'impression des nouveaux CA.
</p>
<p>
<h5>2.9.	Frais d'administration avec contribution minimum (sp�cifique, CCGC)</h5><br/>
Une nouveau mode de calcul de la contribution aux frais d'administration utilis� en sp�cifique par la CCGC est disponible. Il permet de calculer la contribution par palier en appliquant une cotisation minimum pour chacun des paliers.
</p>
<p>
<h5>2.10.	Mode de compensation pour d�comptes finaux (Standard, FER)</h5><br/>
Il est d�sormais possible de r�server un cr�dit disponible sur le compte de l'affili� pour compenser un type de d�compte particulier. Par d�faut, tous les d�comptes �mis sont compens�s. <br/>
Les valeurs particuli�res sont les suivantes :<br/>
-	compensation avec d�compte final (le cr�dit n'est compens� que sur un d�compte 13 ou 14) <br/>
-	compensation avec d�compte de cotisations personnelles (le cr�dit n'est compens� que sur un d�compte 20 ou 22 (�tudiants))<br/>
-	compensation avec contr�le d'employeur (le cr�dit n'est compens� que sur un d�compte 17)<br/>
-	compensation avec d�compte rectificatif / r�troactif (le cr�dit n'est compens� que sur un d�compte 18)<br/>
</p>

<p>
<h5>2.11. APG, Maternit� et IJAI (Standard)</h5><br/>
Selon une demande de la Commission Utilisateurs APG/IJAI, ajout dans la visualisation des prestations d'une colonne "Montant net" � droite de la colonne "Montant brut" avec des totaux de ces 2 colonnes. <br/>
Dans les crit�res de s�lection, ajout d'une date de d�but et d'une date de fin pour visualiser les totaux des p�riodes voulues et pour imprimer la visualisation telle qu'elle est pr�sent�e. Pour imprimer, cliquer sur l'option "Imprimer" dont le document vient s'afficher directement. <br/>
 <br/>
Possibilit� d'ajouter des notes (post-it) sur les modules APG / Maternit� / IJAI <br/>
 <br/>
Quand un calcul passe par ACOR, les fen�tres (pop-up) n'apparaissent plus, vous arrivez directement dans l'interface d'ACOR.
</p>

<p>
<h5>2.12. ACM (Caisses horlog�res)</h5><br/>
Calculer l'ACM pour un assur� mari� sans enfant avec un taux de 75% et non pas de 50% comme actuellement.
</p>

<p>
<h5>2.13. Maternit� cantonale (Sp�cifique)</h5><br/>
Prise en compte de l'imp�t � la source dans le calcul de la maternit� cantonale selon le taux d�fini dans la maternit� f�d�rale. <br/>
 <br/>
Calculer la maternit� cantonale pour une demande avec un montant garanti.
</p>

<p>
<h5>2.14. IJAI</h5><br/>
RAPPEL (pour les caisses qui ne les utilisent pas encore) : Les d�cisions IJAI (5�me r�vision) sont � disposition. Le catalogue de texte doit �tre mis � jour selon les directives ins�r�es dans les bloc-notes des versions V1.4.13.1, V1.4.13.1sp1, V1.5.0, V1.5.00.01, V1.5.00.01sp6. <br/>
 <br/>
Insertion dans l'ent�te de la d�cision de l'Office AI concern� par le prononc�. <br/>
Suppression d'un tiret dans l'adresse de paiement. <br/>
 <br/>
Il est d�sormais possible de calculer les prestations IJAI sans passer par ACOR.<br/>
Par contre, le calcul des montants journaliers allou�s aux IJ (�cran : IJ calcul�es) se fait toujours par ACOR.
</p>

   </TD>
        </TR>
</TABLE>

<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title" id="1-5-00">What's new ? Quoi de neuf dans l'application WebAVS version 1-5-00 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB" height="600" valign="top">
			<p><I>Vous trouverez ci-dessous un extrait des nouvelles fonctionnalit�s de cette version. Pour la liste d�taill�e des modifications, ainsi que la liste des bugs corrig�s, pri�re de se r�f�rer � la <A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>release_notes/globaz_webavs_release_notes.pdf" target="_blank">release note...</A></I></p>
			<p>Vous pouvez �galement consulter les <A href="whatsnewArchive.jsp">archives</A> des anciennes versions.</p>
<p>
<h5>2.1. Saisie des ARC : codification du nombre-cl� des �tats (Standard, Inforom)</h5><br/>
Lors de la saisie des ARC, les nombre-cl�s des �tats (ex. 100 pour la Suisse) peuvent �tre codifi�s � l'aide du num�ro sans forc�ment passer par la liste d�roulante.
</p>

<p>
<h5>2.2. NNSS (Standard, Inforom)</h5><br/>
Avec la version 1-5-0 sont livr�es les derni�res modifications permettant la mise en service du NNSS au 1er juillet 2008. Les fonctionnalit�s ajout�es � cette version sont :
<ul>
<li>Impression du nouveau CA avec les derniers textes et le dernier formulaire</li>
<li>Impression de l'attestation d'assurance</li>
<li>Gestion du NNSS avec les supports de donn�es</li>
<li>Gestion du NNSS lors de l'impression de masse des DS 2008</li>
<li>Format pour sous-traitance de l'impression en masse des CA au 3�/4� trimestre 2008</li>
</ul>
Cette version supporte enti�rement l'introduction du NNSS au 1er juillet 2008.
</p>

<p>
<h5>2.3. Liste de d�tection des �critures � double des non-actifs et ind�pendants (Standard, CCVD)</h5><br/>
Cette liste permet de d�tecter les assur�s qui ont une inscription CI en tant que non actif ou ind�pendant et qui la m�me ann�e ont une inscription en tant que salari�.
</p>

<p>
<h5>2.4. D�tection des RCI touchant un affili� irr�couvrable (Facultatif, Inforom)</h5><br/>
Cette fonctionnalit� permet de signaler toute cl�ture touchant le CI d'un affili� irr�couvrable. La caisse commise peut ainsi contacter la caisse commettante pour obtenir un �ventuel remboursement via une retenue sur la futur rente de tout ou partie des cotisations irr�couvrables.
<br/>
Mise en service : Cette fonction s'active en indiquant au niveau de l'ent�te CI de l'affili� concern� (ou des ent�tes s'il y en a plusieurs) que le CI est irr�couvrable dans le champ r�serv� � cet effet.
<br/>
Effet : Durant le traitement journalier des ordres de la centrale, une liste s'imprime si un CI d�clar� comme irr�couvrable est cl�tur�. Le liste peut �tre r�imprim�e via la gestion des lot en r�ception de la centrale.
</p>

<p>
<h5>2.5. Liste des inscriptions CI apr�s cl�ture suite � un d�c�s (Standard, CCVD)</h5><br/>
Cette liste � imprimer sur demande permet de signaler toutes les inscriptions CI qui suivent une cl�ture pour d�c�s qui a lieu avant l'�ge de 60 ans.
</p>

<p>
<h5>2.6. Tenir compte de l'historique des noms du tiers lors de la transmission d'un extrait de CI (Standard)</h5><br/>
Le nom de l'affili� transmis avec les extraits de CI est d�sormais extrait de l'historique des noms qui figure dans la gestion des tiers. Ceci permet d'�viter la cr�ation d'un nouvel affili� lorsqu'une entreprise change de nom. La localit� est �galement recherch�e dans l'historique.
</p>

<p>
<h5>2.7. Forcer un nom et un no d'affili� particulier pour une inscription CI (Standard)</h5><br/>
Cette fonctionnalit�, utile essentiellement pour les reprises de donn�es, permet de stocker directement avec l'inscription CI un nom et un num�ro d'affili� forc�s.
Cela permet ainsi d'inscrire au CI une inscription touchant un affili� inconnu ou inexistant dans le registre des affili�s.
</p>

<p>
<h5>2.8. Simuler un calcul d'int�r�ts moratoires pour paiement tardif (Standard, Inforom)</h5><br/>
Cette fonctionnalit� permet de simuler un calcul d'int�r�t moratoire pour paiement tardif jusqu'� une date indiqu�e par l'utilisateur, alors que la section n'est pas encore sold�e.
Le calcul simul� peut �tre sauvegard� sur demande sous la forme d'une d�cision et factur� par la proc�dure standard des int�r�ts pour paiement tardif.
L'utilit� principale de cette fonctionnalit� r�side dans la possibilit� de calculer les int�r�ts jusqu'� la de mise en poursuite.
</p>

<p>
<h5>2.9. Compte annexe : lien direct vers la gestion des plans de paiements (Standard, CCJU)</h5><br/>
Un lien vers la gestion des plans de paiements a �t� ajout� dans les options du menu contextuel du compte annexe.
</p>

<p>
<h5>2.10. Transfert de soldes : possibilit� de forcer le num�ro de d�compte de destination (Standard, CCJU)</h5><br/>
Il est d�sormais possible de forcer le num�ro de d�compte du compte de destination lors d'un transfert de soldes.
</p>

<p>
<h5>2.11. BVR : Attribution automatique d'un paiement � la section la plus ancienne (Standard, CCVD)</h5><br/>
Lorsqu'un paiement BVR touche une section d�j� sold�e, le syst�me recherche automatiquement la section la plus ancienne dont le solde ouvert correspond exactement au montant du paiement et lui attribue le paiement. Si aucune section ouverte ne correspond au montant, le paiement est mis en suspens pour traitement manuel.
Le but est d'�viter de traiter manuellement les BVR pour lesquels l'affili� a effectu� un ordre permanent en utilisant toujours le m�me num�ro de r�f�rence BVR.
</p>

<p>
<h5>2.12. Nouvelle attribution des paiements sur plans de paiement (Standard, Inforom)</h5><br/>
Les acomptes pay�s sur la base d'un plan de paiement dont le montant ne correspond pas � l'acompte fix� sont ventil�s entre les diff�rentes �ch�ances jusqu'� concurrence du montant pay�.
Ainsi, une �ch�ance peut �tre partiellement sold�e. Si un paiement est plus �lev� que le montant fix� dans l'acompte, l'�ch�ance est sold�e et le reste est report� en d�duction sur l'�ch�ance suivante.
</p>

<p>
<h5>2.13. Plans de paiement et saisie de paiements manuels (standard, Inforom)</h5><br/>
Lorsqu'un paiement manuel est encod� sur une section qui fait partie d'un plan de paiement, une ligne d'information est affich�e pour rendre attentif au fait que les �ch�ances du plan doivent �tre adapt�es manuellement.
</p>

<p>
<h5>2.14. Encodage de paiement provenant de l'OP (standard, Inforom)</h5><br/>
Si un paiement manuel provenant de l'OP est saisi, le champ "provenance du paiement" peut �tre renseign� avec les valeurs " acompte de l'OP " ou " solde de l'OP " afin d'indiquer � l'application que le paiement provient bien de l'OP. L'information sera utilis�e par la suite sur les documents du contentieux, afin de ne pas communiquer � l'OP un paiement que lui-m�me a effectu�.
</p>

<p>
<h5>2.15. Indiquer le montant pay� des cotisations provenant de Net-Entreprise (sp�cifique, FER)</h5><br/>
Lorsqu'une section provenant de Net-Entreprise est sold�e, les cotisations sont indiqu�es comme pay�es.
</p>

<p>
<h5>2.16. Annulation / report automatique des taxes de sommation impay�es (Standard, Inforom)</h5><br/>
Cette nouvelle fonctionnalit� permet d'extourner automatiquement la taxe de sommation si l'affili� paie son d�compte dans les n jours (param�trable) qui suivent la sommation sans payer la taxe.
Si les n jours sont d�pass�s, le syst�me reporte automatiquement la taxe sur le prochain d�compte ou acompte � facturer.
<br/>
Mise en service : le d�lai du nombre de jours n faisant suite � la sommation est � param�trer (cf. guide de mise en service). L'utilisation est automatique lors de la lecture des BVR
</p>

<p>
<h5>2.17. Extrait de r�capitulation ALFA : ajout des contributions AF ind�pendants (Sp�cifique, horlogerie)</h5><br/>
Les contributions ALFA des ind�pendants genevois figurent d�sormais sur l'extrait de r�capitulation ALFA.
<br/>
Mise en service : automatique sur la base de la rubrique 5500.4030.6000
</p>

<p>
<h5>2.18. Saisie du num�ro de compte en comptabilit� g�n�rale avec ou sans les points (standard, CCJU)</h5><br/>
Le num�ro de compte de comptabilit� g�n�rale peut d�sormais �tre saisi indiff�remment avec ou sans les points.
</p>

<p>
<h5>2.19. R�vision de la pr�sentation du grand livre (standard, Inforom)</h5><br/>
Suppression de la colonne livre, suppression du libell� de la contre-�criture, diminuer la zone " cours ", affichage au max. 999 milliards sur les 3 colonnes d�bit, cr�dit, solde.
</p>

<p>
<h5>2.20. Nouvelles directives pour le calcul des non-actifs avec p�riode inf�rieure � 1 ann�e (Standard, Inforom)</h5><br/>
Le calcul des cotisations pour les non-actifs lorsque la p�riode de d�cision est inf�rieure � une ann�e a �t� adapt� selon les nouvelles directives entr�es en vigueur au 1er janvier 2008.
<br/>
Mise en service : automatique
</p>

<p>
<h5>2.21. Communications fiscales : pas d'impression d'une d�cision d�finitive si le revenu et la fortune sont identiques aux �l�ments provisoires (Facultatif, Inforom)</h5><br/>
Cette fonctionnalit� optionnelle permet d'�viter d'imprimer et de facturer une d�cision d�finitive provenant du traitement automatique des communications fiscales si le revenu et la fortune sont identiques aux donn�es de la d�cision provisoire.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.22. Etudiants : inscription au CI apr�s paiement int�gral des cotisations (sp�cifique, agence de Lausanne et CCGC)</h5><br/>
L'inscription CI est effectu�e lorsque l'�tudiant s'est enti�rement acquitt� de ses cotisations annuelles.
</p>

<p>
<h5>2.23. D�clarations de salaires communes AGRIVIT/CCVD (sp�cifique, CCVD)</h5><br/>
Les d�clarations de salaires mixtes AGRIVIT et CCVD (agriculteurs) sont imprim�es avec une ent�te particuli�re mentionnant les deux caisses. Ces affili�s sont identifi�s � l'aide de la valeur " DEM double ent�te " du champ " d�claration de salaires " de l'affili�.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.24. G�n�rer une code barre sur les d�clarations de salaires (facultatif, CCVD)</h5><br/>
Il est d�sormais possible d'imprimer un code barre sur la d�claration de salaires. Ce code barre peut �tre lu � la r�ception de la d�claration � l'aide d'un crayon optique par exemple, et permet d'enregistrer automatiquement le retour du document.
<br/>
Voir �galement le point suivant pour la r�ception.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.25. R�ception des d�clarations de salaires via code barre (facultatif, CCVD)</h5><br/>
Les d�clarations de salaires g�n�r�es avec un code barre peuvent �tre r�ceptionn�es dans un masque sp�cial capable de g�rer la lecture optique du code.
<br/>
Voir �galement le point pr�c�dent pour la g�n�ration du code barre.
</p>

<p>
<h5>2.26. Tracer chaque d�claration de salaire imprim�e (facultatif, CCVD)</h5><br/>
La case � cocher " inscrire dans la gestion des envois " qui permet de lancer le suivi de l'attestation de salaires peut �tre bloqu�e de telle sorte qu'un utilisateur standard de l'application soit oblig� dans lancer le suivi (la case est toujours coch�e).
<br/>
Cette fonctionnalit� s'active via la gestion des s�curit�s. Un ou plusieurs superuser ayant le choix de cocher ou de d�cocher la case peuvent �tre d�fini au besoin.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.27. Droit sur le champ " int�r�ts moratoires " dans la saisie des d�clarations de salaires (facultatif, CCVD)</h5><br/>
La liste d�roulante " int�r�ts moratoires " qui figure dans l'�cran de saisie d'une attestation de salaire peut �tre fig�e sur le mode " automatique "  pour les utilisateurs standard du module. Le choix de forcer un calcul d'int�r�t est alors r�serv� aux super utilisateurs du module.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.28. S�paration de l'encodage des d�comptes avec saisie individuelle des d�comptes de type bouclement d'acompte (Standard, Inforom)</h5><br/>
L'encodage d'une d�claration de salaire sans saisie individuelle doit d�sormais �tre effectu� via les relev�s. Cela touche essentiellement les d�comptes 14 de type " bouclement d'acompte " pour lesquels on n'encode pas de salari�s.
<br/>
Les champs " masse AVS, AC, AF " ont �t� bloqu�s dans le masque de saisie de la d�claration de salaires.
<br/>
Ceci permet de diff�rencier clairement les d�comptes avec saisie individuelle qui doivent �tre encod�s dans le module " d�claration de salaires " (contr�les d'employeurs, d�comptes finaux, d�comptes r�troactifs / rectificatifs) des d�comptes pour lesquels il n'y a pas de saisie individuelle (bouclement d'acompte, taxation d'office, rectificatif sur l'ann�e en cours).
</p>

<p>
<h5>2.29. Taxation d'office pour d�claration de salaire non retourn�e (Standard, Inforom)</h5><br/>
La taxation d'office est encod�e via la gestion des relev�s. Une amende est automatiquement factur�e ainsi qu'une taxe de sommation provenant de la sommation pour d�claration de salaire non retourn�e.
Par ailleurs, la saisie d'une taxation d'office g�n�re un contr�le d'employeur obligatoire pour l'ann�e indiqu�e.
</p>

<p>
<h5>2.30. Modification des r�gles de remboursement (standard, Inforom)</h5><br/>
Si le compte annexe est irr�couvrable, le remboursement doit �tre quittanc� par l'utilisateur. Si l'utilisateur ne quittance pas le remboursement, le montant n�gatif est port� en compte.
<br/>
Si l'une ou l'autre des sections du compte annexe est au contentieux (si au moins 1 des sections ouvertes est au contentieux) ou si elle est au b�n�ficie d'un sursis au contentieux, tous les motifs sauf rentier (le motif et la date de d�but/fin du sursis sont renseign�s), le remboursement doit �tre lib�r� par l'utilisateur en s�lectionnant la note de cr�dit et en indiquant " remboursement " dans le champ " mode de recouvrement ".
</p>

<p>
<h5>2.31. Lettre aux rentiers non-actifs (facultatif, Inforom)</h5><br/>
Lors de la facturation trimestrielle, le syst�me peut g�n�rer automatiquement une lettre aux non-actifs qui atteignent l'�ge de la retraite pour les rendre attentifs sur le fait qu'ils doivent d�poser une demande de rente.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.32. Indiquer le visa de l'utilisateur sur la liste des afacts manuels (standard, CCJU)</h5><br/>
Le visa de l'utilisateur figure d�sormais sur la liste des afacts manuels.
</p>

<p>
<h5>2.33. Unification des op�rations comptabiliser/imprimer/GED lors de la facturation (standard, CCVD)</h5><br/>
La proc�dure de comptabilisation a �t� modifi�e dans le but d'imprimer l'ensemble des documents n�cessaires � la mise sous pli au moment de la comptabilisation.

Les documents touch�s sont :
<ul>
<li>Les factures</li>
<li>Les d�cisions d'int�r�ts</li>
<li>Les bulletins de soldes</li>
</ul>

<br/>
L'op�ration de comptabilisation imprime automatiquement ces documents et effectue la mise en GED en une seule op�ration.

<br/>
Il est toujours possible d'imprimer les factures, les bulletins de soldes et les d�cisions d'int�r�t avant de comptabiliser le journal de facturation. Cette op�ration est alors consid�r�e comme un tirage de contr�le.

<br/>
L'operating de facturation est d�sormais modifi� comme suit (r�sum�) :
<ul>
<li>g�n�rer</li>
<li>traiter les compensations</li>
<li>traiter les int�r�ts moratoires</li>
<li>imprimer un tirage de contr�le des factures et des d�cisions d'int�r�ts (facultatif)</li>
<li>comptabiliser (imprime automatiquement tous les documents et effectue la mise en GED si n�cessaire)</li>
</ul>
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.34. Comptabilisation imm�diate ou diff�r�e de la facturation (standard, CCVD)</h5><br/>
La comptabilisation d'un journal de facturation peut �tre effectu�e en mode imm�diat (comme actuellement), ou en diff�r�. Dans ce cas, la comptabilisation a lieu la nuit.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.35. Report automatique des montants entre 2 et 20 francs issus de la facturation (facultatif, FER)</h5><br/>
Cette fonctionnalit� permet de reporter automatiquement sur un prochain d�compte les montants issus d'une facturation qui sont sup�rieurs � la limite d�finie comme montant minime (entre 2 et 5 francs selon les caisses) et inf�rieurs � un deuxi�me plafond (par exemple 20 francs).
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.36. Report manuel de solde de sections sur un prochain d�compte (standard, Inforom)</h5><br/>
Cette nouvelle fonctionnalit� permet de reporter sur demande une section non sold�e sur un prochain d�compte/acompte.
</p>

<p>
<h5>2.37. Diff�rencier les compensations internes / externes (standard, CCJU)</h5><br/>
Les compensations internes - � savoir les compensations g�n�r�es automatiquement par l'application lorsque dans le m�me journal figurent une note de cr�dit et une facture en faveur de la caisse - sont d�sormais plus clairement identifi�es. Le num�ro de d�compte compens� figure sur la facture.
<br/>
Un nouveau type d'AFACT " compensation interne " doit �galement �tre utilis� pour diff�rencier les compensations internes et externes.
</p>

<p>
<h5>2.38. Compensation avec les comptes annexes d'un m�me tiers (standard, CCVD)</h5><br/>
Le module de compensation examine d�sormais automatiquement les autres comptes annexes du tiers de m�me r�le (affili�), afin de d�terminer s'il y a lieu de proposer une compensation.
<br/>
Ceci permet de compenser automatiquement des factures impay�es qui figurent par exemple sous le compte "ind�pendant " de l'affili� avec une note de cr�dit �mise sous le compte " employeur " du m�me tiers.
<br/>
Les caisses qui utilisent une affiliation de type " ind�pendant et employeur " pour ces cas sont moins touch�es par cette fonctionnalit�.
</p>

<p>
<h5>2.39. Tri des nationalit�s dans tiers (facultatif, Inforom)</h5><br/>
Tiers est d�sormais capable d'afficher en premier dans la liste d�roulante les nationalit�s les plus utilis�es.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.40. Recherche sur les personnes morales (standard, Inforom)</h5><br/>
Cette nouvelle fonctionnalit� permet de limiter la recherche dans tiers aux personnes morales.
</p>

<p>
<h5>2.41. Fusion des banques, gestion de l'ancienne r�f�rence (standard, CCVD)</h5><br/>
Lors de la fusion des banques, la r�f�rence vers l'ancienne banque fusionn�e est conserv�e et affich�e � l'�cran.
</p>

<p>
<h5>2.42. Impression du num�ro de document et de liste sur l'ensemble des documents issus de l'application (facultatif, Inforom)</h5><br/>
Cette fonctionnalit� permet d'afficher le num�ro de document sur l'ensemble des documents et listes �mises par l'application.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.43. Ancien contr�le d'employeur, lot de fonctionnalit�s no 2 (standard, Inforom)</h5><br/>
Le deuxi�me lot de fonctionnalit�s touchant le contr�le d'employeur ancienne version a �t� livr�. Il couvre les points suivants :

<ul>
<li>saisie de toutes les ann�es reprises sous un seul d�compte (saisie individuelle dans la d�claration de salaire)</li>
<li>lien entre le d�compte et le rapport de contr�le (nouveau champ de la d�claration de salaire)</li>
<li>impression du num�ro de rapport sur la d�cision d'int�r�t (cf. catalogue de textes)</li>
<li>impression du contenu de la saisie individuelle avec le m�me format que la d�claration de salaire pr�-imprim�e (nouvelle option du menu contextuel de la d�claration de salaires)</li>
<li>imprimer sur demande la mention " pr�-rapport " sur le rapport de contr�le (option imprimer du menu contextuel du rapport de contr�le)</li>
<li>impression sur demande d'une lettre d'accompagnement au rapport avec paragraphe principal libre (option  du menu contextuel du rapport de contr�le)</li>
<li>calculer et afficher la date du prochain contr�le sur le masque de gestion des contr�les d'employeurs (masque de gestion des rapports de contr�le)</li>
<li>possibilit� d'indiquer un taux de frais d'administration sp�cial pour les contr�les d'employeurs (cf. guide de mise en service)</li>
<li>possibilit� de modifier manuellement le taux des frais d'administration</li>
<li>les frais d'administration ne sont plus rembours�s par d�faut si le contr�le d'employeur est en faveur de l'affili� (note de cr�dit). Si la caisse le d�sire, elle peut tout de m�me forcer le remboursement des frais</li>
<li>ajout de la colonne AC II</li>
</ul>
</p>

<p>
<h5>2.44. Liste des affili�s paritaires actifs trimestriels avec masse AVS sup�rieure � 200'000 (standard, Inforom)</h5><br/>
Cette liste permet de d�tecter les affili�s paritaires trimestriels dont la masse est sup�rieure � 200'000.
</p>

<p>
<h5>2.45. Ajout de la p�riode du d�compte et du libell� du plan d'affiliation dans la gestion des suivis (standard, FER)</h5><br/>
Cette fonctionnalit� permet de diff�rencier le suivi des relev�s � blanc en fonction de la p�riode et du plan d'affiliation.
</p>

<p>
<h5>2.46. Demande de revenus et bilans : 1 suivi par ann�e (facultatif, FER)</h5><br/>
Un document par ann�e est d�sormais g�n�r�.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.47. Masque de saisie des �valuations du nouveau contr�le d'employeur (standard, Inforom)</h5><br/>
Ce nouveau masque de saisie permet l'encodage des �valuations selon le principe du nouveau contr�le d'employeur.
</p>

<p>
<h5>2.48. Taux variables par pallier : utilisation du taux moyen (sp�cifique, FER)</h5><br/>
Stockage par ann�e et par affili� du taux moyen de contributions au frais d'administration. Le taux moyen est utilis� pour calculer la contribution aux frais d'administration.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.49. Choix du plan d'affiliation pour l'encodage des relev�s (standard, FER)</h5><br/>
Si l'affili� a plusieurs plans d'affiliation, le plan pour lequel le relev� est saisi peut d�sormais �tre s�lectionn� sur le premier masque.
</p>

<p>
<h5>2.50. Nouveaux codes affiliation (sp�cifique, CCGC)</h5><br/>
<br/>
Particularit�s : OCPA, hospice g�n�ral, RMCAS
<br/>
Types d'affiliation : TSE volontaire, Employeur D/F
</p>

<p>
<h5>2.51. Lettre ou d�cision d'int�r�ts moratoires (standard, CCJU)</h5><br/>
Cette nouvelle fonctionnalit� permet de traiter les d�cisions d'int�r�ts de deux mani�res diff�rentes.
<br/>
Variante 1 : d�cision seule ou lettre seule
<br/>
La caisse choisit d'imprimer soit une lettre sans voie de droit, soit une d�cision avec moyens de droits. C'est le mode de fonctionnement actuel.
<br/>
Variante 2 : par d�faut, la lettre est imprim�e mais une d�cision peut �tre imprim�e sur demande si l'affili� conteste les int�r�ts
<br/>
Dans cette variante, le catalogue de texte est d�doubl� avec deux versions de chacun des documents (paiement tardif, cotisations arri�r�es, 25% cot.pers., r�mun�ratoires, d�compte final tardif). L'une des versions contient le texte de la lettre sans voie de droit, l'autre contient le texte de la d�cision.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.52. Indemnit� journali�re : AIT (Standard)</h5><br/>
Nouveau contr�le sur le nombre de jours maximum admis pour les AIT (max 180 jours).
</p>

<p>
<h5>2.53. Indemnit� journali�re : IJ Calcul�es (Standard)</h5><br/>
Possibilit� de modifier toutes les informations des IJ calcul�es. Utiles pour garantir des droits 3�mes r�vision notamment.
</p>

<p>
<h5>2.54. Indemnit� journali�re : Prononc� IJAI (Standard)</h5><br/>
Int�gration de l'�tat 'D�cid�' dans le prononc� IJAI (voir manuel utilisateur pour plus d'informations).
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.55. Indemnit� journali�re : D�cisions IJAI (Standard)</h5><br/>
Possibilit� de visualiser les d�cisions IJAI directement dans l'application (voir manuel utilisateur pour plus d'informations).
</p>


<p>
<h5>2.56. Indemnit� journali�re : No  d�cision AI (Standard)</h5><br/>
Permet de modifier le num�ro de la d�cision d'un prononc� IJAI, qui serait d�j� dans l'�tat 'Communiqu�' (Pay�).
Ce num�ro sera ainsi pris en compte dans les annonces (voir manuel utilisateur pour plus d'informations).
<br/>
Ce champ sera rendu obligatoire dans les annonces d�s 05.2008 par la centrale.
</p>

<p>
<h5>2.57. APG : Op�ration Argus (Standard)</h5><br/>
Prise en compte de l'op�ration Argus selon les remarques de la commission APG/Maternit�.
</p>

<p>
<h5>2.58. Automatisation du traitement des communications fiscales VD/NE/GE/VS/JU (Standard)</h5><br/>
Automatisation du traitement des communications fiscales VD/NE/GE/VS/JU
</p>

<p>
<h5>3. Catalogue de textes</h5><br/>
<h5>3.1. Modification : D�cisions d'int�r�ts moratoires / r�mun�ratoire</h5><br/>
Les variables disponibles sont :
<br/>
Paragraphe niveau 1 :
<br/>{0} Libell� de l'int�r�t (Exemple : Int�r�ts moratoires pour d�compte final tardif)
<br/>{1} Genre d'affil� (Cotisations paritaires ou Cotisations personnelles), valable uniquement si la caisse s�pare les affiliations paritaires et personnelles par un r�le diff�rent.
<br/>{2} Genre de d�compte (Description par rapport au type, par exemple)
<br/>{3} Num�ro de rapport de contr�le d'employeur
<br/>
Paragraphe niveau 2 :
<br/>{0} Num�ro de facture (Ex. 200813000)
<br/>{1} Num�ro d'affili� (Ex. 100.1010)
<br/>{2} Genre d'affil� (Cotisations paritaires ou Cotisations personnelles), valable uniquement si la caisse s�pare les affiliations paritaires et personnelles par un r�le diff�rent.
<br/>{3} Genre de d�compte (Description par rapport au type, par exemple)
<br/>{4} Num�ro de rapport de contr�le d'employeur
<br/>
Paragraphes niveau 3 et 4 :
<br/>{0} Formule de politesse
<br/>{1} Genre d'affil� (Cotisations paritaires ou Cotisations personnelles), valable uniquement si la caisse s�pare les affiliations paritaires et personnelles par un r�le diff�rent.
<br/>{2} Genre de d�compte (Description par rapport au type, par exemple)
<br/>{3} Num�ro de rapport de contr�le d'employeur
</p>

<p>
<h5>3.2. Modification : Relev�s � blanc, pr�vision pour acomptes, bouclement d'acompte</h5><br/>
Ajout de la date et de la signature pour le retour du document.
<br/>
Modification de la mise en page de ces documents.
</p>

<p>
<h5>3.3. Modification : Imputation (cotisations personnelles)</h5><br/>
Ajout d'une variable {0} repr�sentant le code politesse (Madame, Monsieur, etc..) sur le document d'imputation. Cette variable est reconnue uniquement dans les paragraphes 2,1 et 5,1.
</p>

<p>
<h5>3.4. Nouveau : Lettre aux rentiers non-actifs (mandat 2.31)</h5><br/>
Ce nouveau document figure dans le catalogue de textes de la facturation. Une version initiale du document est propos�e et doit �tre adapt�e si la caisse d�cide d'utiliser la fonctionnalit� (signature, texte, etc.).
</p>

<p>
<h5>3.5. Nouveau : Liste de concordance NNSS</h5><br/>
Ce document est utilis� pour accompagner la liste de concordance no AVS-> NNSS transmise � l'affili�. Elle sera � partir du 4� trimestre 2008. Une version initiale du document a �t� charg�e et doit �tre adapt�e par la caisse.
</p>

<p>
<h5>3.6. Modification : d�cision pour ind�pendants, b�n�fice en capital</h5><br/>
Mise � jour du catalogue pour d�cision avec b�n�fice en capital.
</p>

<p>
<h5>3.7. Nouveau : Lettre d'accompagnement pour certificat d'assurance (NNSS)</h5><br/>
Ce nouveau document figure dans le catalogue de texte des ARC/ZAS. Une version initiale du document est propos�e et doit �tre adapt�e pour la mise en production au 1er juillet 2008.
</p>

<p>
<h5>3.8. Nouveau : Lettre attestation certificat assurance (NNSS)</h5><br/>
Ce nouveau document figure dans le catalogue de texte des ARC/ZAS. Une version initiale du document est propos�e et doit �tre adapt�e pour la mise en production au 1er juillet 2008.
</p>

<p>
<h5>3.9. Modification : demande de revenus et bilans</h5><br/>
Les documents de demande de revenus et bilans ont �t� adapt�s. Le texte est � contr�ler et � modifier le cas �ch�ant si la caisse utilise cette fonctionnalit�.
</p>


   </TD>
        </TR>
</TABLE>
<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title" id="1-4-13">What's new ! Quoi de neuf dans l'application WebAVS version 1-4-13 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB">
			<H5>Gestion des tiers</H5><DL>
				<DT>Eventail des r�gimes</DT>
				<DD>Ce nouveau masque affiche une vue d'ensemble de la situation d'un tiers, notamment ses donn�es d'affiliation paritaire et personnelle, l'�tat de la derni�re d�claration de salaires et le solde en compte.</DD>
				<DT>Blocage total des envois</DT>
				<DD>Cette fonctionnalit� permet de bloquer tout envoi de document pour un tiers.</DD>
				</DL><H5>D�clarations de salaires</H5>
			<DL>
			<DT>Gestion de la cat�gorie de salari�</DT>
			<DD>Cette fonction disponible au niveau de la saisie individuelle permet de diff�rencier � l'aide d'un code particulier les salari�s soumis � une partie de plan d'assurance de l'employeur.</DD>
			<DT>D�clarations de salaires communes (sp�cifique CCVD)</DT>
			<DD>Cette fonctionnalit� permet de g�n�rer un d�compte final AGRIVIT sur la base des donn�es de la d�claration de salaires saisie dans l'instance de la caisse cantonale.</DD>
			<DT></DT>
			</DL><H5>Comptes individuels</H5>
			<DL>
			<DT>NNSS - lot num�ro 3</DT>
			<DD>3�me lot de fonctionnalit� pour le NNSS. Ces fonctionnalit�s seront utilis�es � partir du 1er juillet 2008.</DD>
			<DT>Nombre d'ent�tes CI ouvertes</DT>
			<DD>La statistique CI donne d�sormais le nombre d'ent�tes CI ouvertes.</DD>

			</DL>
			<H5>Affiliation</H5>
			<DL>
			<DT>Saisie des masses </DT>
			<DD>Le masque de saisie des masques (option &quot;modifier masses&quot;) a �t� am�lior�. La masse AVS est notamment copi�e automatiquement dans les autres assurances.</DD>
			<DT>Gestion des quittances pour b�n�ficiaires PC (sp�cifique agence de Lausanne, CCVD)</DT>
			<DD>Cette nouvelle fonctionnalit� permet de saisir, de facturer et d'inscrire au CI les quittances des aides m�nages octroy�es aux b�n�ficiaires de PC.</DD>
			<DT>Acomptes &quot;� la carte&quot;</DT>
			<DD>Cette fonctionnalit� permet de g�rer des acomptes variables durant l'ann�e. Elle est utile notamment pour les entreprises avec activit� saisonni�re.</DD>
			<DT>Suffixe pour assist�s, r�fugi�s et b�n�ficiaires PC (sp�cifique agence de Lausanne)</DT>
			<DD>Un suffixe particulier est attribu� automatiquement aux non-actifs et ind�pendants assist�s, r�fugi�s et b�n�ficiaires de PC.</DD>
<DT>Synchronisation des taux entre le plan d'assurance et la comptabilit� auxiliaire</DT>
			<DD>Cette fonctionnalit� permet de synchroniser automatiquement les taux entre le plan d'assurance et la comptabilit� auxiliaire.</DD>
			</DL><H5>Comptabilit� auxiliaire</H5>

			<DL>
				<DT>Adresse de domicile sur les poursuites</DT>
				<DD>L'adresse de domicile figure syst�matiquement sur les documents de poursuites en lieu et place de l'adresse de courrier. </DD>
				<DT>Facturation des frais d'administration (sp�cifique caisse 51.10)</DT>
				<DT>Remboursement des allocations familiales (sp�cifique caisse 51.10)</DT>
				<DD>Cette nouvelle fonctionnalit� permet de rembourser automatiquement toutes les factures dont le solde du secteur des allocations familiales est n�gatif .</DD>
				<DT>Nouvelle zone "num�ro de r�f�rence BVR" pour reprise de donn�es</DT>
				<DD>Cette nouvelle zone permet de migrer le num�ro de r�f�rence BVR associ� � une facture. Il est utile lors de la migration des donn�es.</DD>

			</DL>
			<H5>Comptabilit� g�n�rale</H5>
			<DL>
			<DT>Exportation du bilan, du compte P&amp;P et de la balance au format Excel</DT>
			<DD>Les 3 listes principales de la comptabilit� g�n�rale peuvent d�sormais s'exporter au format Excel ou pdf.</DD>
			<DT>Ventilation du compte 2000.1102.0000</DT>
			<DD>L'application ventile d�sormais tous les comptes 200x.1102.0000 au bouclement au lieu de se limiter au compte 2000.1102.0000.</DD>
			<DT>Incr�mentation automatique des pi�ces comptables</DT>
			<DD>Cette fonctionnalit� permet de num�roter automatiquement les pi�ces comptables manuelles saisies en comptabilit� g�n�rale.</DD>
			<DT>S�paration de la quote-part ACM maternit� et militaire (sp�cifique caisses horlog�res)</DT>
			<DD>Les quotes-parts ACM maternit� et militaires s'enregistrent d�sormais sur deux comptes diff�rents et figurent s�par�ment sur l'extait r�capitulatif ALFA.</DD>
			</DL>

			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>Cotisations AC II pour TSE</DT>
			<DD>Les cotisations AC II sont d�sormais calcul�es pour les TSE.</DD>
			<DT>Plausibilit�s pour traitement des communications fiscales</DT>
			<DD>Les plausibilit�s pour le traitement des communications fiscales genevoises et jurassiennes sont impl�ment�es.</DD>
			<DT>Duplicata</DT>
			<DD>Cette fonctionnalit� permet d'imprimer un duplicata d'une d�cision de cotisations personnelles.</DD>
			<DT>Gestion des �tudiants (sp�cifique agence de Lausanne, CCGC)</DT>
			<DD>Ce nouveau module associ� aux cotisations personnelles permet de traiter �lectroniquement les listes d'�tudiants fournies par les universit�s et par l'�cole polytechnique et de produire un d�compte de cotisations personnelles.</DD>
			</DL>
			<H5>IJAI</H5>
			<DL>
			<DT>Ech�ance</DT>
			<DD>Nouvelle �ch�ance 'manuelle' pour la r�vision des cas IJAI.</DD>
			<DT>Bases indemnisations</DT>
			<DD>Possibilit� de saisir une base d'indemnisation sur plusieurs mois.</DD>
			<DT>Listes</DT>
			<DD>Attestations fiscales IJAI : indique le d�tail des p�riodes des prestations.</DD>
			<DT>Compensations</DT>
			<DD>Possibilit� de choisir manuellement une autre facture dans l'�cran de gestion des compensations</DD>
			<DT>AIT</DT>
			<DD>Prise en compte des allocations d'initiation au travail</DD>
			<DT>Allocation d'assistance</DT>
			<DD>Prise en compte des allocations d'assistance</DD>
			<DT>D�cision IJAI</DT>
			<DD>Possibilit� de sortir les d�cisions IJ pour les grandes et petites IJ</DD>


			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Plausibilit�s</DT>
			<DD>Contr�le du nombre de jours de protection civile par ann�e : Message d'avertissement si sup�rieur � 25.</DD>
			<DT>Calcul prestations</DT>
			<DD>Nouvelle r�gles pour le calcul des cotisations, selon validation CU.</DD>
			<DT>Listes</DT>
			<DD>Attestations fiscales : indique le d�tail des p�riodes des prestations</DD>
			<DT>Compensations</DT>
			<DD>Possibilit� de choisir manuellement une autre facture dans l'�cran de gestion des compensations</DD>
			<DT>D�cision/communication d'allocation de maternit�</DT>
			<DD>Avec / sans moyen de droit</DD>
			<DD>Si versement � l'employeur, une copie du document est envoy�e � la m�re avec lettre d'accompagnement (param�trage).</DD>
			</DL>
			</TD>
        </TR>
</TABLE>

<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title" id="1-4-12">What's new ! Quoi de neuf dans l'application WebAVS version 1-4-12 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB">
			<H5>Contentieux</H5><DL>
				<DT>Rappels sur plans de paiements</DT>
				<DD>Deux nouvelles fonctionnalit�s sont disponibles et permettent d'imprimer un rappel pour un plan de paiement particulier ou en masse pour l'ensemble des �ch�ances non respect�es.</DD>
				<DT>D�cisions de sursis au paiement</DT>
				<DD>Il est possible de g�rer plusieurs d�cisions de sursis dans le catalogue de texte et de choisir au moment de l'impression quel formulaire utiliser.</DD>
				<DT>Suspension automatique du plan de paiement</DT>
				<DD>Une nouvelle prod�cure automatis�e permet de suspendre automatiquement les plans de paiement non respect�s, de mani�re � ce que les �tapes suivantes du contentieux se poursuivent normalement.</DD>
			</DL><H5>D�clarations de salaires</H5>
			<DL>
			<DT>Total AC I et AF</DT>
			<DD>Le masque de saisie individuelle affiche les totaux AC I et AF. </DD>
			<DT>D�clarations de salaires � z�ro</DT>
			<DD>Lorsque le montant effectif des cotisations du d�compte final correspond � l'acompte factur� durant l'ann�e, une ligne de facture est tout de m�me g�n�r�e pour indiquer � l'employeur que la cotisation en question est acquitt�e.</DD>
			<DT>Comptabilisation automatique des journaux CI provenant de la saisie individuelle</DT>
			<DD>Les journaux CI provenant de la saisie individuelle peuvent d�sormais �tre comptabilis� automatiquement lors de la comptabilisation du journal de facturation.</DD>
			</DL><H5>Comptes individuels</H5>
			<DL>
			<DT>NNSS - support complet du NNSS dans le batch</DT>
			<DD>Le batch CI supporte d�sormais toutes les annonces avec NNSS, soit les ouvertures, rassemblements, splittings et cl�tures.</DD>
			<DT>Support du format PUCS 2</DT>
			<DD>Le format PUCS 2 est d�sormais support� par l'application.</DD>

			</DL>
			<H5>Affiliation</H5>
			<DL>
			<DT>Suivi des relev�s</DT>
			<DD>Les relev�s sont d�sormais suivis par plan d'affiliation</DD>
			<DT>Post-it</DT>
			<DD>Divers am�liorations ont �t� apport�es.</DD>
			<DT>NNSS - ouverture des CI pour ind�pendants et non-actifs</DT>
			<DD>Dans le cadre du NNSS, cette proc�dure permettra l'ouverture en masse des CI des ind�pendants et non-actifs.  sera ex�cut�e durant le 2�me semestre 2008</DD>
			<DT>Calcul r�troactif lors d'un changement de masse</DT>
			<DD>Cette fonctionnalit� a �t� am�lior�e pour tenir compte de cas de figure tels qu'une affiliation r�troactive avec p�riodicit� trimestrielle au cours d'un trimestre.</DD>
			<DT>Cr�ation automatique d'un journal pour les relev�s</DT>
			<DD>Le journal de facturation de type relev� peut sur demande �tre automatiquement ouvert apr�s comptabilisation du pr�c�dent.</DD>
			<DT>Support des affili�s dont les cotisations ont plusieurs p�riodicit�s</DT>
			<DD>Lorsque des cotisations de p�riodicit�s diff�rentes figurent dans le plan d'assurance (cotisations mensuelles et trimestrielles), le syst�me g�re d�sormais correctement la situation.</DD>
			<DT>Formulaire de pr�vision pour acompte</DT>
			<DD>Ce nouveau formulaire permet de demander � l'employeur une estimation de sa masse salariale. Il tient compte du plan d'assurance. Une nouvelle option figure dans la d�finition des assurances et permet de param�trer les cotisations que l'on d�sire voir figurer sur ce formulaire. Ce document peut �galement faire l'objet d'un envoi en masse.</DD><DT>Formulaire de bouclement d'acomptes</DT><DD>Ce nouveau formlaire permet � l'employeur de transmettre une estimation de la masse salariale effective de l'ann�e pour permettre une facturation rapide sous la forme d'un bouclement d'acompte. Ce document peut faire l'objet d'un envoi de masse en fin d'ann�e.</DD><DT>Formulaire de confirmation/d�cision d'acompte</DT><DD>Ce formulaire a �t� adapt� afin de tenir compte des assurances que l'on d�sire voir figurer sur le document. Il peut �galement faire l'objet d'un envoi en masse.</DD><DT>Loi sur le travail au noir (LTN), lot no 1</DT><DD>Une nouveau type d'affiliation permet de g�rer les affili�s d�clar�s selon la proc�dure LTN.</DD></DL><H5>Comptabilit� auxiliaire</H5>

			<DL>
				<DT>Enqu�te sur les cotisations arri�r�es</DT>
				<DD>Cette nouvelle liste permet d'�diter le tableau des cotisations arri�r�es demand� par l'OFAS au bouclement annuel. </DD>
				<DT>Caisse m�tier</DT>
				<DD>La notion de caisse m�tier a �t� �tendue aux rubriques de taxes, amendes et int�r�ts.</DD>
				<DT>Priorit� de ventilation pour les taxes et amendes</DT>
				<DD>Les taxes et amendes peuvent �tre imput�s sur un compte courant particulier du secteur 900, avec un priorit� de ventilation inf�rieure � celle des cotisations. Cela permet un calcul d'int�r�ts moratoires plus rapide si l'affili� paye le d�compte en omettant la taxe de sommation par exemple. Les taxes et amendes seront couverts apr�s les cotisations et le frais d'administration si cette solution est choisie.</DD>

			</DL>
			<H5>Comptabilit� g�n�rale</H5>
			<DL>
			<DT>Ecritures collectives</DT>
			<DD>Dans le d�tail d'une �criture dont la contrepartie est multiple, un lien a �t� ajout� permettant d'afficher l'ensemble des �critures collectives de la pi�ce.</DD>
			<DT>Mod�les d'�critures</DT>
			<DD>Les centres de charges sont d�sormais support�s.</DD>
			<DT>R�capitulation des rentes</DT>
			<DD>Un nouveau masque permet - pour les caisses qui n'utilisent pas l'application Rentes de Globaz - de saisir la r�capitulation des rentes.</DD>
			</DL>
			<H5>Facturation</H5>
			<DL>
			<DT>Liste des ind�pendants tax�s d�finitivement ayant touch� des APG</DT>
			<DD>Cette liste peut d�sormais �tre imprim�e sur demande via l'option "liste" du journal de facturation.</DD>
			</DL>
			<H5>ARC/ZAS</H5>
			<DL>
			<DT>NNSS</DT>
			<DD>Une file d'attente diff�r�e pour la transmission en masse des demandes d'ouvertures et de CA dans le cadre de la proc�dure NNSS a �t� mise en place.</DD>
			<DT>TRAX</DT>
			<DD>TRAX est d�sormais support� par l'application.</DD>
			</DL>
			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>Nouveau masque de recherche de d�cisions</DT>
			<DD>Ce nouveau masque permet de rechercher des d�cisions selon diff�rents crit�res, notamment les d�cisions non valid�es ou en suspens.</DD>
			<DT>Comptabilisation automatique du journal CI</DT>
			<DD>Le journal CI provenant des d�cisions de cotisations personnelles peut �tre au choix comptabilis� automatiquement ou sur demande.</DD>
			<DT>Non-actifs : cotisation minimale acquitt�e par la collectivit� publique</DT>
			<DD>Le code sp�cial 01 qui figure dans l'inscription CI est d�sormais g�r� pour ces cas via les d�cisions de cotisations personnelles.</DD>
			<DT>Catalogue de texte</DT>
			<DD>L'ensemble des d�cisions de cotisations personnelles ont �t� migr�es dans le catalogue de textes. La caisse peut d�sormais adapter le recto et le verso des d�cisions.</DD>

			</DL>
			<H5>IJAI</H5>
			<DL>
			<DT>Recherche</DT>
			<DD>Recherche sur les noms pr�noms des assur�s ind�pendemment de la 'CASE' (fonctionne avec ou sans accent).</DD>
			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Recherche</DT>
			<DD>Recherche sur les noms pr�noms des assur�s ind�pendemment de la 'CASE' (fonctionne avec ou sans accent).</DD>
			<DT>Maternit� Cantonale</DT>
			<DD>Prise en compte du nouveau montant LAA. D�s le 1er janvier 2008, l'indemnit� journali�re maximale LAMat sera de 280 CHF et non-plus de 237,60 CHF.</DD>
			</DL>
			<H5>HERA</H5>
			<DL>
			<DT>Recherche</DT>
			<DD>Recherche sur les noms pr�noms des assur�s ind�pendemment de la 'CASE' (fonctionne avec ou sans accent).</DD>
			<DD>Optimisation des temps de r�ponses dans les �crans de recherches.</DD>
			</DL>
			</TD>
        </TR>
</TABLE>

<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title" id="1-4-11">What's new ? Quoi de neuf dans l'application WebAVS version 1-4-11 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB">
			<H5>Gestion des tiers</H5><DL>
				<DT>NNSS</DT>
				<DD>Les masques et les listes ont �t� adapt�s � l'introduction du NNSS</DD>
			</DL><H5>D�clarations de salaires</H5>
			<DL>
			<DT>NNSS</DT>
			<DD>Les masques et les listes ont �t� adapt�s � l'introduction du NNSS.</DD>
				<DT>Saisie du nom de l'assur�</DT>
				<DD>Lorsque le num�ro d'assur� est inconnu ou incomplet ou n'existe pas au registre des assur�s, on peut d�sormais saisir sont nom afin d'am�liorer la gestion des suspens.</DD>
				<DD></DD>
			</DL>
			<H5>ARC/ZAS</H5>
			<DL>
			<DT>NNSS</DT>
			<DD>Les masques et les listes ont �t� adapt�s � l'introduction du NNSS.</DD></DL><H5>Comptabilit� auxiliaire</H5>
			<DL>
			<DT>Listes des soldes par secteur</DT>
			<DD>Cette nouvelle fonctionnalit� permet d'imprimer une liste des soldes par secteur / compte-courant sous forme Excel.</DD>
				<DT>Liste des soldes ouvertes � une date donn�e</DT>
				<DD>Cette liste permet d'extraire toutes les factures dont la date de cr�ation est ant�rieure � une date de r�f�rence (par exemple le 31.12.2006) qui sont encore ouverte � ce jour.</DD>
				<DT>Annulation de montants minimes par secteur</DT>
				<DD>Cette fonctionnalit� pemet d'annuler des montants minimes par secteur / compte-courant.</DD>
				<DT>Recherche de montants</DT>
				<DD>Cette fonctionnalitl� permet de recherche des factures ouvertes dont le solde correspond � un montant donn�, ou des �critures correspondant � un montant donn�.</DD>
				<DT>Liste des comptes irr�couvrables et rentiers</DT>
				<DD>Cette liste permet d'extraire les factures dont le stade de sommation est d�pass� pour les comptes dont le motif de blocage est irr�couvrable ou rentier.</DD>
				<DT>R�organisation du menu liste</DT>
				<DD>Au vu du grand nombre de listes disponibles en comptabilit� auxiliaire, le menu a �t� r�goranis� en chapitres afin d'am�liorer la lisibilit�.</DD>
			</DL><H5>IJAI</H5>
			<DL>
			<DT>GED</DT>
			<DD>Possibilit� d'ajouter les d�comptes IJ dans la GED.</DD>
			<DT>Navigation</DT>
			<DD>R�cup�ration des crit�res de recherches dans les retours arri�res (backs)</DD>
			<DT>NNSS</DT>
			<DD>Migration �crans et listes aux NNSS</DD>
			<DT>Rentes AI en cours</DT>
			<DD>Prise en compte du RAM, de l'�chelle et des codes cas sp�ciaux pour le rentes AI en cours.</DD>
			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Calcul des prestations</DT>
			<DD>Ajout� la possibilit� de forcer le calcul des prestations LAMAT.</DD>
			<DD>Ajout� la possibilit� de forcer le calcul des prestations ACM.</DD>
			<DD>Possibilit� de modifier manuellement les r�partitions de pmts.</DD>
			<DT>Compta</DT>
			<DD>Diff�rentiation des rubriques dans la CA pour les prestations LAMAT NAISSANCE/ADOPTION.</DD>
			<DT>Navigation</DT>
			<DD>R�cup�ration des crit�res de recherches dans les retours arri�res (backs)</DD>
			<DT>NNSS</DT>
			<DD>Migration �crans et listes aux NNSS</DD>
			</DL>

			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>Liste de compraison entre les cotisations personnelles et la comptabilit�</DT>
			<DD>Cette liste permet de comparer et d'extraire les diff�rences entre l'historique des d�cisions de cotisations personnelles et la comptabilit� auxiliaire.</DD>
			<DT>Traitement des communications fiscales</DT>
			<DD>Traitement et validation des CF en retour du fisc.</DD>
			<DT>Traduction des demandes de communications fiscales</DT>
			<DD>Les documents papier de demande de communications fiscales ont �t� traduits en allemand pour les organes all�maniques.</DD>
			<DT>Calculs particuliers pour les non-actifs</DT>
			<DD>L'application prend d�sormais en charge des cas particuliers de d�part � l'�tranger et de d�c�s pour les non-actifs.</DD>
				<DT>NNSS</DT>
				<DD>Les masques et les listes ont �t� adapt�es au NNSS. L'application g�re �galement le changement de num�ro AVS ou la bascule AVS/NSS lors de l'inscription CI en se basant sur l'historique des num�ro AVS de l'application tiers.</DD>
			</DL>
			</TD>
        </TR>
</TABLE>

<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title" id="1-4-10">What's new ? Quoi de neuf dans l'application WebAVS version 1-4-10 ?</TH>
        </TR>
        <TR>
            <TD bgcolor="#B3C4DB">
			<I></I><H5>Gestion des tiers</H5><DL>
				<DT>D�tection des doublons</DT>
				<DD>Lorsque l'on d�sire cr�er une nouvelle personne physique, tiers affiche pour v�rification toutes les personnes dont la date de naissance et le sexe sont identiques. L'utilisateur peut choisir l'une des personnes propos�es et annuler la cr�ation, ou valider la cr�ation de la nouvelle personne.</DD>
			</DL>
			<H5>Affiliation</H5>
			<DL>
			<DT>Adh�sion caisse principale</DT>
			<DD>Le masque de cr�ation d'un affili� a �t� modifi� afin de permettre de d�signer la caisse principale durant le choix des plans de caisse.</DD>
			<DT>Facturation avec plans d'affiliation multiples</DT>
			<DD>Un libell� particulier est d�sormais g�r� au niveau du plan d'affiliation. Si celui-ci est renseign�, il figure sur la facture en regard du num�ro de d�compte. Cette fonctionnalit� est utile lorsque l'on g�n�re deux d�comptes p�riodiques pour un m�me affili�, l'un pour son personnel d'exploitation et l'autre pour son personnel d'administration.</DD>
			<DT>Contr�le d'employeur</DT>
			<DD>Le masque de saisie du contr�le d'employeur a �t� �tendu selon les derni�res sp�cifications. Un nouveau masque d'attribution des r�viseurs a �t� ajout�, ainsi que deux nouvelles listes, l'une permettant de lister les contr�les pr�vus pour une ann�e, l'autre permettant de lister les contr�les effectu�s.</DD>
				<DT>Facturation � d'autres institutions</DT>
				<DD>Cette nouvelle fonctionnalit� permet la prise en charge de factures de cotisations par une autre institution, par exemple les services sociaux, une commune, le service des prestations compl�mentaires, etc. Elle permet �galement de prendre en charge une cotisation particuli�re (par exemple la cotisation FFPP) par une autre institution.</DD>
				<DT>Recherche sur un ancien num�ro d'affili�</DT>
				<DD>Cette fonctionnalit� permet de rechercher un affili� par un ancien num�ro provenant par exemple d'une migration des donn�es. Elle se base sur le champ &quot;ancien num�ro d'affili�&quot; du masque principal de l'affiliation.</DD>
			<DT>Avis de mutations au caisses cantonales</DT>
			<DD>Une nouvelle fonctionnalit� permet d'imprimer en une seule fois l'ensemble des mutations encod�es.</DD>
			</DL>
			<H5>Attestations de salaires</H5>
			<DL>
			<DT>Attestation de salaire vide</DT>
			<DD>Cette nouvelle option permet d'imprimer une attestation de salaires sans mention des salari�s.</DD>
			</DL>
			<H5>ARC/ZAS</H5>
			<DL>
			<DT>Crit�res de recherche</DT>
			<DD>Les crit�res de recherche des principaux masques sont d�sormais conserv�s lorsque l'on utilise la fonction "back". Veiller � n'utiliser que la fonction "back" de l'application et non pas celle du navigateur internet.</DD>
			<DT>CI additionnels</DT>
			<DD>Une nouvelle option permettant de rechercher des CI additionnels a �t� ajout�e. Il est �galement possible de les imprimer.</DD>
			<DT>R�impression des documents du traitement journalier</DT>
			<DD>L'ensemble des documents produits par le traitement journalier des CI (extraits termin�s, CI additionnels, accus� de r�ception, lettre aux affili�s) peuvent �tre r�imprim�s via la gestion des lots.</DD>
			</DL>
			<H5>Comptabilit� auxiliaire</H5>
			<DL>
			<DT>Bulletins de solde</DT>
			<DD>Une nouvelle fonctionnalit� permettant d'imprimer automatiquement des bulletins de soldes apr�s un paiement partiel est disponible.</DD>
			</DL>
			<H5>Comptes individuels</H5>
			<DL>
			<DT>Historisation des inscriptions modifi�es</DT>
			<DD>Lorsque l'on modifie une inscription CI (p�riode, remarque, centimes), les anciennes valeurs sont sauvegard�es et visibles dans l'historique.</DD>
			<DT>Format de d�clarations de salaires �lectroniques</DT>
			<DD>Une nouvelle option permet de g�rer diff�rents format de d�clarations de salaires �lectroniques</DD>
				<DT>NNSS</DT>
				<DD>Les masques �crans ont �t� adapt�s � la future introduction du nouveau num�ro de s�curit� sociale. Le champ &quot;No AVS&quot; a �t� remplac� par NSS. La date de naissance, le sexe et la nationalit� figurent sur tous les �crans lorsque ces informations sont connues. </DD>
			</DL>
			<H5>Facturation</H5>
			<DL>
			<DT>Date de valeur</DT>
			<DD>Un nouveau champ "date de valeur" permet de forcer une date de valeur particuli�re pour une �criture. Cette fonctionnalit� est utile notamment pour les compensations.</DD>
			<DT>Date de r�ception de la d�claration de salaires</DT>
			<DD>Un nouveau champ "date de r�ception" peut �tre utilis� lors de la saisie de d�clarations de salaires manuelles. Cette date est d�terminante pour le calcul des int�r�ts moratoires et r�mun�ratoires.</DD>
			</DL>
			<H5>IJAI</H5>
			<DL>
			<DT>Impression des attestations</DT>
			<DD>Une nouvelle option permet d'imprimer des attestations pour un prononc�.</DD>
			<DT>Ech�ances</DT>
			<DD>Nouvelle gestion des �ch�ances avec historique (date de lancement, gestionnaire...). Une nouvelle liste est �galement disponible : liste des prononc�s actifs depuis plus de 2 ans</DD>
			<DT>Annonces IJ</DT>
			<DD>Correction des annonces IJ (Etat civil, Rev. jour. d�terminant pour les petites IJ, et D�duc. nourriture et logement).</DD>
			<DT>Imp�ts � la source</DT>
			<DD>Possibilit� de modifier les taux d'impositions par canton (Bar�me D)</DD>
			<DT>Listes</DT>
			<DD>La liste des attestations non re�ues est tri�es par nss.</DD>
			<DT>Prononce IJ</DT>
			<DD>Nouvelle saisie permettant de d�finir au niveau du prononc� le type d'indemnistation (interne ou externe). Cette information est affich�e dans la saisie de la base d'indemnisation.</DD>
			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Imp�ts � la source</DT>
			<DD>Possibilit� de modifier les taux d'impositions par canton (Bar�me D)</DD>
			</DL>

			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>R�ductions et remises</DT>
			<DD>Deux nouveaux types de d�cision ont �t� introduits afin de g�rer les remises et les r�ductions de cotisations.</DD>
			<DT>Catalogue de textes</DT>
			<DD>Le texte des d�cisions d'acompte ainsi que les lettres sont d�sormais g�r�es via le catalogue de texte.</DD>
			<DT>Liste de concordance</DT>
			<DD>Cette nouvelle liste permet de v�rifier la concordance entre le CI et l'historique des d�cisions de cotisations personnelles.</DD>
			<DT>Uniformisation des francs</DT>
			<DD>L'abr�viation CHF est utilis�e sur l'ensemble des documents.</DD>
			<DT>Facturation � d'autres institutions</DT>
			<DD>Possiblit� d'indiquer une remarque sur la d�cision si les cotisations de l'affili� sont prises en charge par une autre institution (cf. module affiliation).</DD>
			<DT>Communications fiscales</DT>
			<DD>Impression de des communications fiscales par canton sous Excel selon le tableau r�capitulatif de l'OFAS.</DD>
			<DT>Cotisation minimum</DT>
			<DD>Indication de la cotisation minimum dans le tableau des cotisations de la d�cision.</DD>
			<DT>Comptabilisation des journaux CI</DT>
			<DD>Possibilit� de comptabiliser automatiquement le journal CI.</DD>
			<DT>Statistique des d�cisions par journal</DT>
			<DD>Possibilit� d'activer ou de d�sactiver l'impression automatique de la statistique des d�cisions par journal.</DD>
			</DL>
			</TD>
        </TR>
</TABLE>
</BODY>
</HTML>
