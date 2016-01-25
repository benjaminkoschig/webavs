
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
        Vous trouverez ci-dessous les extraits des fonctionnalités des versions antérieures. Pour la liste détaillée des modifications, ainsi que la liste des bugs corrigés, prière de se référer à l'historique des <A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>release_notes/history/releasenoteshistory.html" target="_blank">releases notes...</A>
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
<h5>1. LTN : attestations fiscales et liste aux autorités fiscales (standard INFOROM)</h5>
<h5>2. Liste cumulée des cotisations par période et par année (standard INFOROM)</h5>
<h5>3. Etudiants : nouveau format EPFL/UNIL (spécifique, agence de Lausanne)</h5>
<h5>4. Nouvel enchaînement des masques pour la saisie d'un plan de paiement (standard INFOROM)</h5>
<h5>5. Traduction en allemand des masques de la partie "Cotisation" (standard INFOROM)</h5>
<h5>6. Champ "remarque" pour les contrôles d'employeurs (standard, INFOROM)</h5>
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
Cette deuxième livraison LTN permet les fonctionnalités suivantes :<br/>
-	Impression d'une déclaration de salaire de type LTN avec lettre d'accompagnement particulière<br/>
-	Saisie d'une déclaration de salaire de type LTN avec calcul et facturation de l'impôt à la source selon le barème fédéral et cantonal<br/>
-	Sommation pour non paiement du décompte LTN, puis blocage de la procédure (pas de poursuite engagée)<br/>

<p>
<h5>2.2.	APG / Maternité (Standard, INFOROM)</h5>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les APG / Maternité.<br/>
</p>
<p>
<h5>2.3.	IJAI (Standard, INFOROM)</h5>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les IJAI.<br/>

Dans les décisions IJAI, l'adresse de recours sera prise dans les administrations de type "Autorités de recours" au lieu de "Tribunal des assurances sociales" jusqu'à présent. Comme cela, vous n'aurez pas à maintenir les adresses de tribunaux à 2 place différentes.<br/>
</p>
<p>
<h5>2.4.	Optimisation des performances d'impression de la facturation (Standard, INFOROM)</h5>
Le processus d'impression de la facturation a été optimisé. Un gain de performance significatif peut être attendu lors de la génération des pdf. Le gain dépend de la plateforme d'exécution de l'application. La cadence varie entre 8'000 et 13'000 factures à l'heure selon les environnements et les plateformes.<br/>
</p>
<p>
<h5>2.5.	Impression des factures de plus d'une page (Standard, INFOROM)</h5>
Les factures de plus d'une page sont désormais imprimées séparément pour permettre une mise sous pli manuelle. S'il n'y a pas d'adresse valable au niveau du tiers, la facture est également imprimée séparément.<br/>
</p>
<p>
<h5>2.6.	Facturation des bénéficiaires PC (Spécifique, agence de Lausanne)</h5><br/>
La fonctionnalité de facturation des quittances pour bénéficiaires PC est désormais fonctionnelle.<br/>
</p>
<p>
<h5>2.7.	InfoRom027 - CAI-00014 - Inscription CI, charger 20 écritures dans l'ascenseur (Standard, Inforom)</h5>
Les 20 dernières inscriptions (au lieu des 5 dernières) sont désormais affichées au niveau du masque de détail d'une inscription CI.<br/>
</p>
<p>
<h5>2.8.	Nouveau masque de visualisation des ordres de versement (Standard, Inforom)</h5>
Ce nouveau masque permet de consulter les ordres versement en cours ou versés au niveau du compte annexe. Il permet également de bloquer un ordre non encore exécuté.<br/>
</p>
<p>
<h5>2.9.	Inforom031: Automatisation de la lecture des BVR (Standard, Inforom)</h5>
Cette nouvelle fonctionnalité permet de traiter automatiquement les BVR disponibles sur le serveur de Postfinance. <br/>

Le système se connecte durant la nuit, télécharge les BVR non encore traités, ouvre un journal en comptabilité auxiliaire et transmet le résultat au responsable du traitement des BVR. S'il n'y a pas d'erreur à traiter, le journal est automatiquement comptabilisé.<br/>
</p>
<p>
<h5>2.10.	Inforom032: FSFP, lot no 2 (Standard, Inforom)</h5>
Cette nouvelle fonctionnalité a été introduite dans le cadre de la nouvelle loi jurassienne sur la FSFP. Elle permet les options suivantes :<br/>
-	impression de la liste des factures en poursuites ou irrécouvrable<br/>
-	décompte annuel FSFP<br/>

Ces listes ont été par la même occasion généralisées à d'autres secteurs de type « autres tâches ». <br/>
</p>
<p>
<h5>2.11.	Situation familiale (Standard, Inforom)</h5>
Des liens ont été créés pour arriver directement dans le tiers.<br/>
</p>

<p>
<h5>2.1.	APG / Maternité (standard)</h5><br/>
Un message d'avertissement s'affiche si l'utilisateur insère un salaire de plus de CHF 100.00 dans le champ « salaire horaire ».<br/>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les APG / Maternité.<br/>
</p>

<p>
<h5>2.2.	Situation familiale (standard)</h5><br/>
Des liens ont été créés pour arriver directement dans le tiers.<br/>
</p>

<p>
<h5>2.3.    IJAI (standard)</h5><br/>
Modification de la rubrique de compensation si vous en avez ouverte une nouvelle selon la version 1.5.1 pour les IJAI.<br/>
Dans les décisions IJAI, l'adresse de recours sera prise dans les administrations de type "Autorités de recours" au lieu de "Tribunal des assurances sociales" jusqu'à présent. Comme cela, vous n'aurez pas à maintenir les adresses de tribunaux à 2 place différentes.<br/>
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
			<p><I>Vous trouverez ci-dessous un extrait des nouvelles fonctionnalités de cette version. Pour la liste détaillée des modifications, ainsi que la liste des bugs corrigï¿½s, priï¿½re de se rï¿½fï¿½rer ï¿½ la <A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>release_notes/globaz_webavs_release_notes.pdf" target="_blank">release note...</A></I></p>
			<p>Vous pouvez également consulter les <A href="whatsnewArchive.jsp">archives</A> des anciennes versions.</p>
<p>
<h5>2.1.	Contrôle d'employeurs, lot 3 (standard, Inforom)</h5><br/>
Le dernier lot de fonctionnalités pour le contrôle d'employeur est livré.<br/>
-	statistiques OFAS<br/>
-	lettres avec paragraphe " libre "<br/>
-	lettre d'avertissement du prochain contrôle<br/>

</p>

<p>
<h5>2.2.	Décision de remises de cotisations personnelles (facultatif, CCGC)</h5><br/>
Les remises de cotisations personnelles peuvent être encodées avec la commune à  laquelle la remise doit être facturée. Le traitement et la comptabilisation des remises a également été réactualisé. Les sorties avec remises sont désormais également gérées.

Cette fonctionnalité doit être configurée avant son utilisation.
</p>
<p>
<h5>2.3.    Refonte du masque de saisie des écritures en comptabilité générale (standard, Inforom)</h5><br/>
Le masque de saisie des écritures provoquait parfois et dans certaines conditions des erreurs dans les soldes. La saisie des écritures collectives n'était également pas très pratique. Le masque donc a été entièrement revu. 

Il permet de saisir sur un seul masque des écritures doubles ou collectives avec contrôle du solde. L'ancien masque de saisie des écritures collectives est supprimé.

Veuillez vous reporter au manuel utilisateur pour la prise en main de ce nouveau masque.

</p>
<p>
<h5>2.4.	Nouveau contentieux, lot de fonctionnalités FER (standard, Inforom)</h5><br/>
Un important lot de fonctionnalités pour la mise en production de la FER a été livré. Il constitue l'essentiel des nouveautés de cette version. Comme le module " nouveau contentieux " n'est pour l'instant disponible qu'à  la FER et à  la FVE, nous ne détaillerons pas ici ces fonctionnalités.
</p>

<p>
<h5>2.5.	Mémorisation des critères de recherches dans le bilan, PP et soldes (standard, Inforom)</h5><br/>
Les critères de recherches des masques " bilan ", " pertes et profits " et " soldes " sont désormais mémorisés pour éviter leur perte lors de l'utilisation de la fonction " retour arrière " ou " back ".
</p>

<p>
<h5>2.6.	Affiliés RMCAS (spécifique, CCGC)</h5><br/>
Les affiliés " RMCAS " sont traités comme les réfugiés et assistés. Leurs cotisations sont prises en charge au moment de la facturation.

</p>
<p>
<h5>2.7.	Recherche d'un compte grâce à  son libellé (standard, Inforom)</h5><br/>
Un compte de comptabilité générale peut être recherché avec une partie de son libellé. La recherche est sensible aux majuscules et minuscules (" AVS " n'est pas égale à  " avs " par exemple).
</p>

<p>
<h5>2.8.	Choix d'un modèle d'entête de lettre personnalisé pour l'impression des CA (facultatif, Inforom)</h5><br/>
Il est possible de configurer un modèle personnalisé pour l'impression des nouveaux CA.
</p>
<p>
<h5>2.9.	Frais d'administration avec contribution minimum (spécifique, CCGC)</h5><br/>
Une nouveau mode de calcul de la contribution aux frais d'administration utilisé en spécifique par la CCGC est disponible. Il permet de calculer la contribution par palier en appliquant une cotisation minimum pour chacun des paliers.
</p>
<p>
<h5>2.10.	Mode de compensation pour décomptes finaux (Standard, FER)</h5><br/>
Il est désormais possible de réserver un crédit disponible sur le compte de l'affilié pour compenser un type de décompte particulier. Par défaut, tous les décomptes émis sont compensés. <br/>
Les valeurs particulières sont les suivantes :<br/>
-	compensation avec décompte final (le crédit n'est compensé que sur un décompte 13 ou 14) <br/>
-	compensation avec décompte de cotisations personnelles (le crédit n'est compensé que sur un décompte 20 ou 22 (étudiants))<br/>
-	compensation avec contrôle d'employeur (le crédit n'est compensé que sur un décompte 17)<br/>
-	compensation avec décompte rectificatif / rétroactif (le crédit n'est compensé que sur un décompte 18)<br/>
</p>

<p>
<h5>2.11. APG, Maternité et IJAI (Standard)</h5><br/>
Selon une demande de la Commission Utilisateurs APG/IJAI, ajout dans la visualisation des prestations d'une colonne "Montant net" à droite de la colonne "Montant brut" avec des totaux de ces 2 colonnes. <br/>
Dans les critères de sélection, ajout d'une date de début et d'une date de fin pour visualiser les totaux des périodes voulues et pour imprimer la visualisation telle qu'elle est présentée. Pour imprimer, cliquer sur l'option "Imprimer" dont le document vient s'afficher directement. <br/>
 <br/>
Possibilité d'ajouter des notes (post-it) sur les modules APG / Maternité / IJAI <br/>
 <br/>
Quand un calcul passe par ACOR, les fenêtres (pop-up) n'apparaissent plus, vous arrivez directement dans l'interface d'ACOR.
</p>

<p>
<h5>2.12. ACM (Caisses horlogères)</h5><br/>
Calculer l'ACM pour un assuré marié sans enfant avec un taux de 75% et non pas de 50% comme actuellement.
</p>

<p>
<h5>2.13. Maternité cantonale (Spécifique)</h5><br/>
Prise en compte de l'impôt à la source dans le calcul de la maternité cantonale selon le taux défini dans la maternité fédérale. <br/>
 <br/>
Calculer la maternité cantonale pour une demande avec un montant garanti.
</p>

<p>
<h5>2.14. IJAI</h5><br/>
RAPPEL (pour les caisses qui ne les utilisent pas encore) : Les décisions IJAI (5ème révision) sont à disposition. Le catalogue de texte doit être mis à jour selon les directives insérées dans les bloc-notes des versions V1.4.13.1, V1.4.13.1sp1, V1.5.0, V1.5.00.01, V1.5.00.01sp6. <br/>
 <br/>
Insertion dans l'entête de la décision de l'Office AI concerné par le prononcé. <br/>
Suppression d'un tiret dans l'adresse de paiement. <br/>
 <br/>
Il est désormais possible de calculer les prestations IJAI sans passer par ACOR.<br/>
Par contre, le calcul des montants journaliers alloués aux IJ (écran : IJ calculées) se fait toujours par ACOR.
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
			<p><I>Vous trouverez ci-dessous un extrait des nouvelles fonctionnalités de cette version. Pour la liste détaillée des modifications, ainsi que la liste des bugs corrigés, prière de se référer à la <A href="<%=globaz.webavs.common.WebavsDocumentionLocator.getDocumentationLocation()%>release_notes/globaz_webavs_release_notes.pdf" target="_blank">release note...</A></I></p>
			<p>Vous pouvez également consulter les <A href="whatsnewArchive.jsp">archives</A> des anciennes versions.</p>
<p>
<h5>2.1. Saisie des ARC : codification du nombre-clé des états (Standard, Inforom)</h5><br/>
Lors de la saisie des ARC, les nombre-clés des états (ex. 100 pour la Suisse) peuvent être codifiés à l'aide du numéro sans forcément passer par la liste déroulante.
</p>

<p>
<h5>2.2. NNSS (Standard, Inforom)</h5><br/>
Avec la version 1-5-0 sont livrées les dernières modifications permettant la mise en service du NNSS au 1er juillet 2008. Les fonctionnalités ajoutées à cette version sont :
<ul>
<li>Impression du nouveau CA avec les derniers textes et le dernier formulaire</li>
<li>Impression de l'attestation d'assurance</li>
<li>Gestion du NNSS avec les supports de données</li>
<li>Gestion du NNSS lors de l'impression de masse des DS 2008</li>
<li>Format pour sous-traitance de l'impression en masse des CA au 3è/4è trimestre 2008</li>
</ul>
Cette version supporte entièrement l'introduction du NNSS au 1er juillet 2008.
</p>

<p>
<h5>2.3. Liste de détection des écritures à double des non-actifs et indépendants (Standard, CCVD)</h5><br/>
Cette liste permet de détecter les assurés qui ont une inscription CI en tant que non actif ou indépendant et qui la même année ont une inscription en tant que salarié.
</p>

<p>
<h5>2.4. Détection des RCI touchant un affilié irrécouvrable (Facultatif, Inforom)</h5><br/>
Cette fonctionnalité permet de signaler toute clôture touchant le CI d'un affilié irrécouvrable. La caisse commise peut ainsi contacter la caisse commettante pour obtenir un éventuel remboursement via une retenue sur la futur rente de tout ou partie des cotisations irrécouvrables.
<br/>
Mise en service : Cette fonction s'active en indiquant au niveau de l'entête CI de l'affilié concerné (ou des entêtes s'il y en a plusieurs) que le CI est irrécouvrable dans le champ réservé à cet effet.
<br/>
Effet : Durant le traitement journalier des ordres de la centrale, une liste s'imprime si un CI déclaré comme irrécouvrable est clôturé. Le liste peut être réimprimée via la gestion des lot en réception de la centrale.
</p>

<p>
<h5>2.5. Liste des inscriptions CI après clôture suite à un décès (Standard, CCVD)</h5><br/>
Cette liste à imprimer sur demande permet de signaler toutes les inscriptions CI qui suivent une clôture pour décès qui a lieu avant l'âge de 60 ans.
</p>

<p>
<h5>2.6. Tenir compte de l'historique des noms du tiers lors de la transmission d'un extrait de CI (Standard)</h5><br/>
Le nom de l'affilié transmis avec les extraits de CI est désormais extrait de l'historique des noms qui figure dans la gestion des tiers. Ceci permet d'éviter la création d'un nouvel affilié lorsqu'une entreprise change de nom. La localité est également recherchée dans l'historique.
</p>

<p>
<h5>2.7. Forcer un nom et un no d'affilié particulier pour une inscription CI (Standard)</h5><br/>
Cette fonctionnalité, utile essentiellement pour les reprises de données, permet de stocker directement avec l'inscription CI un nom et un numéro d'affilié forcés.
Cela permet ainsi d'inscrire au CI une inscription touchant un affilié inconnu ou inexistant dans le registre des affiliés.
</p>

<p>
<h5>2.8. Simuler un calcul d'intérêts moratoires pour paiement tardif (Standard, Inforom)</h5><br/>
Cette fonctionnalité permet de simuler un calcul d'intérêt moratoire pour paiement tardif jusqu'à une date indiquée par l'utilisateur, alors que la section n'est pas encore soldée.
Le calcul simulé peut être sauvegardé sur demande sous la forme d'une décision et facturé par la procédure standard des intérêts pour paiement tardif.
L'utilité principale de cette fonctionnalité réside dans la possibilité de calculer les intérêts jusqu'à la de mise en poursuite.
</p>

<p>
<h5>2.9. Compte annexe : lien direct vers la gestion des plans de paiements (Standard, CCJU)</h5><br/>
Un lien vers la gestion des plans de paiements a été ajouté dans les options du menu contextuel du compte annexe.
</p>

<p>
<h5>2.10. Transfert de soldes : possibilité de forcer le numéro de décompte de destination (Standard, CCJU)</h5><br/>
Il est désormais possible de forcer le numéro de décompte du compte de destination lors d'un transfert de soldes.
</p>

<p>
<h5>2.11. BVR : Attribution automatique d'un paiement à la section la plus ancienne (Standard, CCVD)</h5><br/>
Lorsqu'un paiement BVR touche une section déjà soldée, le système recherche automatiquement la section la plus ancienne dont le solde ouvert correspond exactement au montant du paiement et lui attribue le paiement. Si aucune section ouverte ne correspond au montant, le paiement est mis en suspens pour traitement manuel.
Le but est d'éviter de traiter manuellement les BVR pour lesquels l'affilié a effectué un ordre permanent en utilisant toujours le même numéro de référence BVR.
</p>

<p>
<h5>2.12. Nouvelle attribution des paiements sur plans de paiement (Standard, Inforom)</h5><br/>
Les acomptes payés sur la base d'un plan de paiement dont le montant ne correspond pas à l'acompte fixé sont ventilés entre les différentes échéances jusqu'à concurrence du montant payé.
Ainsi, une échéance peut être partiellement soldée. Si un paiement est plus élevé que le montant fixé dans l'acompte, l'échéance est soldée et le reste est reporté en déduction sur l'échéance suivante.
</p>

<p>
<h5>2.13. Plans de paiement et saisie de paiements manuels (standard, Inforom)</h5><br/>
Lorsqu'un paiement manuel est encodé sur une section qui fait partie d'un plan de paiement, une ligne d'information est affichée pour rendre attentif au fait que les échéances du plan doivent être adaptées manuellement.
</p>

<p>
<h5>2.14. Encodage de paiement provenant de l'OP (standard, Inforom)</h5><br/>
Si un paiement manuel provenant de l'OP est saisi, le champ "provenance du paiement" peut être renseigné avec les valeurs " acompte de l'OP " ou " solde de l'OP " afin d'indiquer à l'application que le paiement provient bien de l'OP. L'information sera utilisée par la suite sur les documents du contentieux, afin de ne pas communiquer à l'OP un paiement que lui-même a effectué.
</p>

<p>
<h5>2.15. Indiquer le montant payé des cotisations provenant de Net-Entreprise (spécifique, FER)</h5><br/>
Lorsqu'une section provenant de Net-Entreprise est soldée, les cotisations sont indiquées comme payées.
</p>

<p>
<h5>2.16. Annulation / report automatique des taxes de sommation impayées (Standard, Inforom)</h5><br/>
Cette nouvelle fonctionnalité permet d'extourner automatiquement la taxe de sommation si l'affilié paie son décompte dans les n jours (paramétrable) qui suivent la sommation sans payer la taxe.
Si les n jours sont dépassés, le système reporte automatiquement la taxe sur le prochain décompte ou acompte à facturer.
<br/>
Mise en service : le délai du nombre de jours n faisant suite à la sommation est à paramétrer (cf. guide de mise en service). L'utilisation est automatique lors de la lecture des BVR
</p>

<p>
<h5>2.17. Extrait de récapitulation ALFA : ajout des contributions AF indépendants (Spécifique, horlogerie)</h5><br/>
Les contributions ALFA des indépendants genevois figurent désormais sur l'extrait de récapitulation ALFA.
<br/>
Mise en service : automatique sur la base de la rubrique 5500.4030.6000
</p>

<p>
<h5>2.18. Saisie du numéro de compte en comptabilité générale avec ou sans les points (standard, CCJU)</h5><br/>
Le numéro de compte de comptabilité générale peut désormais être saisi indifféremment avec ou sans les points.
</p>

<p>
<h5>2.19. Révision de la présentation du grand livre (standard, Inforom)</h5><br/>
Suppression de la colonne livre, suppression du libellé de la contre-écriture, diminuer la zone " cours ", affichage au max. 999 milliards sur les 3 colonnes débit, crédit, solde.
</p>

<p>
<h5>2.20. Nouvelles directives pour le calcul des non-actifs avec période inférieure à 1 année (Standard, Inforom)</h5><br/>
Le calcul des cotisations pour les non-actifs lorsque la période de décision est inférieure à une année a été adapté selon les nouvelles directives entrées en vigueur au 1er janvier 2008.
<br/>
Mise en service : automatique
</p>

<p>
<h5>2.21. Communications fiscales : pas d'impression d'une décision définitive si le revenu et la fortune sont identiques aux éléments provisoires (Facultatif, Inforom)</h5><br/>
Cette fonctionnalité optionnelle permet d'éviter d'imprimer et de facturer une décision définitive provenant du traitement automatique des communications fiscales si le revenu et la fortune sont identiques aux données de la décision provisoire.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.22. Etudiants : inscription au CI après paiement intégral des cotisations (spécifique, agence de Lausanne et CCGC)</h5><br/>
L'inscription CI est effectuée lorsque l'étudiant s'est entièrement acquitté de ses cotisations annuelles.
</p>

<p>
<h5>2.23. Déclarations de salaires communes AGRIVIT/CCVD (spécifique, CCVD)</h5><br/>
Les déclarations de salaires mixtes AGRIVIT et CCVD (agriculteurs) sont imprimées avec une entête particulière mentionnant les deux caisses. Ces affiliés sont identifiés à l'aide de la valeur " DEM double entête " du champ " déclaration de salaires " de l'affilié.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.24. Générer une code barre sur les déclarations de salaires (facultatif, CCVD)</h5><br/>
Il est désormais possible d'imprimer un code barre sur la déclaration de salaires. Ce code barre peut être lu à la réception de la déclaration à l'aide d'un crayon optique par exemple, et permet d'enregistrer automatiquement le retour du document.
<br/>
Voir également le point suivant pour la réception.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.25. Réception des déclarations de salaires via code barre (facultatif, CCVD)</h5><br/>
Les déclarations de salaires générées avec un code barre peuvent être réceptionnées dans un masque spécial capable de gérer la lecture optique du code.
<br/>
Voir également le point précédent pour la génération du code barre.
</p>

<p>
<h5>2.26. Tracer chaque déclaration de salaire imprimée (facultatif, CCVD)</h5><br/>
La case à cocher " inscrire dans la gestion des envois " qui permet de lancer le suivi de l'attestation de salaires peut être bloquée de telle sorte qu'un utilisateur standard de l'application soit obligé dans lancer le suivi (la case est toujours cochée).
<br/>
Cette fonctionnalité s'active via la gestion des sécurités. Un ou plusieurs superuser ayant le choix de cocher ou de décocher la case peuvent être défini au besoin.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.27. Droit sur le champ " intérêts moratoires " dans la saisie des déclarations de salaires (facultatif, CCVD)</h5><br/>
La liste déroulante " intérêts moratoires " qui figure dans l'écran de saisie d'une attestation de salaire peut être figée sur le mode " automatique "  pour les utilisateurs standard du module. Le choix de forcer un calcul d'intérêt est alors réservé aux super utilisateurs du module.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.28. Séparation de l'encodage des décomptes avec saisie individuelle des décomptes de type bouclement d'acompte (Standard, Inforom)</h5><br/>
L'encodage d'une déclaration de salaire sans saisie individuelle doit désormais être effectué via les relevés. Cela touche essentiellement les décomptes 14 de type " bouclement d'acompte " pour lesquels on n'encode pas de salariés.
<br/>
Les champs " masse AVS, AC, AF " ont été bloqués dans le masque de saisie de la déclaration de salaires.
<br/>
Ceci permet de différencier clairement les décomptes avec saisie individuelle qui doivent être encodés dans le module " déclaration de salaires " (contrôles d'employeurs, décomptes finaux, décomptes rétroactifs / rectificatifs) des décomptes pour lesquels il n'y a pas de saisie individuelle (bouclement d'acompte, taxation d'office, rectificatif sur l'année en cours).
</p>

<p>
<h5>2.29. Taxation d'office pour déclaration de salaire non retournée (Standard, Inforom)</h5><br/>
La taxation d'office est encodée via la gestion des relevés. Une amende est automatiquement facturée ainsi qu'une taxe de sommation provenant de la sommation pour déclaration de salaire non retournée.
Par ailleurs, la saisie d'une taxation d'office génère un contrôle d'employeur obligatoire pour l'année indiquée.
</p>

<p>
<h5>2.30. Modification des règles de remboursement (standard, Inforom)</h5><br/>
Si le compte annexe est irrécouvrable, le remboursement doit être quittancé par l'utilisateur. Si l'utilisateur ne quittance pas le remboursement, le montant négatif est porté en compte.
<br/>
Si l'une ou l'autre des sections du compte annexe est au contentieux (si au moins 1 des sections ouvertes est au contentieux) ou si elle est au bénéficie d'un sursis au contentieux, tous les motifs sauf rentier (le motif et la date de début/fin du sursis sont renseignés), le remboursement doit être libéré par l'utilisateur en sélectionnant la note de crédit et en indiquant " remboursement " dans le champ " mode de recouvrement ".
</p>

<p>
<h5>2.31. Lettre aux rentiers non-actifs (facultatif, Inforom)</h5><br/>
Lors de la facturation trimestrielle, le système peut générer automatiquement une lettre aux non-actifs qui atteignent l'âge de la retraite pour les rendre attentifs sur le fait qu'ils doivent déposer une demande de rente.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.32. Indiquer le visa de l'utilisateur sur la liste des afacts manuels (standard, CCJU)</h5><br/>
Le visa de l'utilisateur figure désormais sur la liste des afacts manuels.
</p>

<p>
<h5>2.33. Unification des opérations comptabiliser/imprimer/GED lors de la facturation (standard, CCVD)</h5><br/>
La procédure de comptabilisation a été modifiée dans le but d'imprimer l'ensemble des documents nécessaires à la mise sous pli au moment de la comptabilisation.

Les documents touchés sont :
<ul>
<li>Les factures</li>
<li>Les décisions d'intérêts</li>
<li>Les bulletins de soldes</li>
</ul>

<br/>
L'opération de comptabilisation imprime automatiquement ces documents et effectue la mise en GED en une seule opération.

<br/>
Il est toujours possible d'imprimer les factures, les bulletins de soldes et les décisions d'intérêt avant de comptabiliser le journal de facturation. Cette opération est alors considérée comme un tirage de contrôle.

<br/>
L'operating de facturation est désormais modifié comme suit (résumé) :
<ul>
<li>générer</li>
<li>traiter les compensations</li>
<li>traiter les intérêts moratoires</li>
<li>imprimer un tirage de contrôle des factures et des décisions d'intérêts (facultatif)</li>
<li>comptabiliser (imprime automatiquement tous les documents et effectue la mise en GED si nécessaire)</li>
</ul>
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.34. Comptabilisation immédiate ou différée de la facturation (standard, CCVD)</h5><br/>
La comptabilisation d'un journal de facturation peut être effectuée en mode immédiat (comme actuellement), ou en différé. Dans ce cas, la comptabilisation a lieu la nuit.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.35. Report automatique des montants entre 2 et 20 francs issus de la facturation (facultatif, FER)</h5><br/>
Cette fonctionnalité permet de reporter automatiquement sur un prochain décompte les montants issus d'une facturation qui sont supérieurs à la limite définie comme montant minime (entre 2 et 5 francs selon les caisses) et inférieurs à un deuxième plafond (par exemple 20 francs).
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.36. Report manuel de solde de sections sur un prochain décompte (standard, Inforom)</h5><br/>
Cette nouvelle fonctionnalité permet de reporter sur demande une section non soldée sur un prochain décompte/acompte.
</p>

<p>
<h5>2.37. Différencier les compensations internes / externes (standard, CCJU)</h5><br/>
Les compensations internes - à savoir les compensations générées automatiquement par l'application lorsque dans le même journal figurent une note de crédit et une facture en faveur de la caisse - sont désormais plus clairement identifiées. Le numéro de décompte compensé figure sur la facture.
<br/>
Un nouveau type d'AFACT " compensation interne " doit également être utilisé pour différencier les compensations internes et externes.
</p>

<p>
<h5>2.38. Compensation avec les comptes annexes d'un même tiers (standard, CCVD)</h5><br/>
Le module de compensation examine désormais automatiquement les autres comptes annexes du tiers de même rôle (affilié), afin de déterminer s'il y a lieu de proposer une compensation.
<br/>
Ceci permet de compenser automatiquement des factures impayées qui figurent par exemple sous le compte "indépendant " de l'affilié avec une note de crédit émise sous le compte " employeur " du même tiers.
<br/>
Les caisses qui utilisent une affiliation de type " indépendant et employeur " pour ces cas sont moins touchées par cette fonctionnalité.
</p>

<p>
<h5>2.39. Tri des nationalités dans tiers (facultatif, Inforom)</h5><br/>
Tiers est désormais capable d'afficher en premier dans la liste déroulante les nationalités les plus utilisées.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.40. Recherche sur les personnes morales (standard, Inforom)</h5><br/>
Cette nouvelle fonctionnalité permet de limiter la recherche dans tiers aux personnes morales.
</p>

<p>
<h5>2.41. Fusion des banques, gestion de l'ancienne référence (standard, CCVD)</h5><br/>
Lors de la fusion des banques, la référence vers l'ancienne banque fusionnée est conservée et affichée à l'écran.
</p>

<p>
<h5>2.42. Impression du numéro de document et de liste sur l'ensemble des documents issus de l'application (facultatif, Inforom)</h5><br/>
Cette fonctionnalité permet d'afficher le numéro de document sur l'ensemble des documents et listes émises par l'application.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.43. Ancien contrôle d'employeur, lot de fonctionnalités no 2 (standard, Inforom)</h5><br/>
Le deuxième lot de fonctionnalités touchant le contrôle d'employeur ancienne version a été livré. Il couvre les points suivants :

<ul>
<li>saisie de toutes les années reprises sous un seul décompte (saisie individuelle dans la déclaration de salaire)</li>
<li>lien entre le décompte et le rapport de contrôle (nouveau champ de la déclaration de salaire)</li>
<li>impression du numéro de rapport sur la décision d'intérêt (cf. catalogue de textes)</li>
<li>impression du contenu de la saisie individuelle avec le même format que la déclaration de salaire pré-imprimée (nouvelle option du menu contextuel de la déclaration de salaires)</li>
<li>imprimer sur demande la mention " pré-rapport " sur le rapport de contrôle (option imprimer du menu contextuel du rapport de contrôle)</li>
<li>impression sur demande d'une lettre d'accompagnement au rapport avec paragraphe principal libre (option  du menu contextuel du rapport de contrôle)</li>
<li>calculer et afficher la date du prochain contrôle sur le masque de gestion des contrôles d'employeurs (masque de gestion des rapports de contrôle)</li>
<li>possibilité d'indiquer un taux de frais d'administration spécial pour les contrôles d'employeurs (cf. guide de mise en service)</li>
<li>possibilité de modifier manuellement le taux des frais d'administration</li>
<li>les frais d'administration ne sont plus remboursés par défaut si le contrôle d'employeur est en faveur de l'affilié (note de crédit). Si la caisse le désire, elle peut tout de même forcer le remboursement des frais</li>
<li>ajout de la colonne AC II</li>
</ul>
</p>

<p>
<h5>2.44. Liste des affiliés paritaires actifs trimestriels avec masse AVS supérieure à 200'000 (standard, Inforom)</h5><br/>
Cette liste permet de détecter les affiliés paritaires trimestriels dont la masse est supérieure à 200'000.
</p>

<p>
<h5>2.45. Ajout de la période du décompte et du libellé du plan d'affiliation dans la gestion des suivis (standard, FER)</h5><br/>
Cette fonctionnalité permet de différencier le suivi des relevés à blanc en fonction de la période et du plan d'affiliation.
</p>

<p>
<h5>2.46. Demande de revenus et bilans : 1 suivi par année (facultatif, FER)</h5><br/>
Un document par année est désormais généré.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.47. Masque de saisie des évaluations du nouveau contrôle d'employeur (standard, Inforom)</h5><br/>
Ce nouveau masque de saisie permet l'encodage des évaluations selon le principe du nouveau contrôle d'employeur.
</p>

<p>
<h5>2.48. Taux variables par pallier : utilisation du taux moyen (spécifique, FER)</h5><br/>
Stockage par année et par affilié du taux moyen de contributions au frais d'administration. Le taux moyen est utilisé pour calculer la contribution aux frais d'administration.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.49. Choix du plan d'affiliation pour l'encodage des relevés (standard, FER)</h5><br/>
Si l'affilié a plusieurs plans d'affiliation, le plan pour lequel le relevé est saisi peut désormais être sélectionné sur le premier masque.
</p>

<p>
<h5>2.50. Nouveaux codes affiliation (spécifique, CCGC)</h5><br/>
<br/>
Particularités : OCPA, hospice général, RMCAS
<br/>
Types d'affiliation : TSE volontaire, Employeur D/F
</p>

<p>
<h5>2.51. Lettre ou décision d'intérêts moratoires (standard, CCJU)</h5><br/>
Cette nouvelle fonctionnalité permet de traiter les décisions d'intérêts de deux manières différentes.
<br/>
Variante 1 : décision seule ou lettre seule
<br/>
La caisse choisit d'imprimer soit une lettre sans voie de droit, soit une décision avec moyens de droits. C'est le mode de fonctionnement actuel.
<br/>
Variante 2 : par défaut, la lettre est imprimée mais une décision peut être imprimée sur demande si l'affilié conteste les intérêts
<br/>
Dans cette variante, le catalogue de texte est dédoublé avec deux versions de chacun des documents (paiement tardif, cotisations arriérées, 25% cot.pers., rémunératoires, décompte final tardif). L'une des versions contient le texte de la lettre sans voie de droit, l'autre contient le texte de la décision.
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.52. Indemnité journalière : AIT (Standard)</h5><br/>
Nouveau contrôle sur le nombre de jours maximum admis pour les AIT (max 180 jours).
</p>

<p>
<h5>2.53. Indemnité journalière : IJ Calculées (Standard)</h5><br/>
Possibilité de modifier toutes les informations des IJ calculées. Utiles pour garantir des droits 3èmes révision notamment.
</p>

<p>
<h5>2.54. Indemnité journalière : Prononcé IJAI (Standard)</h5><br/>
Intégration de l'état 'Décidé' dans le prononcé IJAI (voir manuel utilisateur pour plus d'informations).
<br/>
Mise en service : cf. guide de mise en service
</p>

<p>
<h5>2.55. Indemnité journalière : Décisions IJAI (Standard)</h5><br/>
Possibilité de visualiser les décisions IJAI directement dans l'application (voir manuel utilisateur pour plus d'informations).
</p>


<p>
<h5>2.56. Indemnité journalière : No  décision AI (Standard)</h5><br/>
Permet de modifier le numéro de la décision d'un prononcé IJAI, qui serait déjà dans l'état 'Communiqué' (Payé).
Ce numéro sera ainsi pris en compte dans les annonces (voir manuel utilisateur pour plus d'informations).
<br/>
Ce champ sera rendu obligatoire dans les annonces dès 05.2008 par la centrale.
</p>

<p>
<h5>2.57. APG : Opération Argus (Standard)</h5><br/>
Prise en compte de l'opération Argus selon les remarques de la commission APG/Maternité.
</p>

<p>
<h5>2.58. Automatisation du traitement des communications fiscales VD/NE/GE/VS/JU (Standard)</h5><br/>
Automatisation du traitement des communications fiscales VD/NE/GE/VS/JU
</p>

<p>
<h5>3. Catalogue de textes</h5><br/>
<h5>3.1. Modification : Décisions d'intérêts moratoires / rémunératoire</h5><br/>
Les variables disponibles sont :
<br/>
Paragraphe niveau 1 :
<br/>{0} Libellé de l'intérêt (Exemple : Intérêts moratoires pour décompte final tardif)
<br/>{1} Genre d'affilé (Cotisations paritaires ou Cotisations personnelles), valable uniquement si la caisse sépare les affiliations paritaires et personnelles par un rôle différent.
<br/>{2} Genre de décompte (Description par rapport au type, par exemple)
<br/>{3} Numéro de rapport de contrôle d'employeur
<br/>
Paragraphe niveau 2 :
<br/>{0} Numéro de facture (Ex. 200813000)
<br/>{1} Numéro d'affilié (Ex. 100.1010)
<br/>{2} Genre d'affilé (Cotisations paritaires ou Cotisations personnelles), valable uniquement si la caisse sépare les affiliations paritaires et personnelles par un rôle différent.
<br/>{3} Genre de décompte (Description par rapport au type, par exemple)
<br/>{4} Numéro de rapport de contrôle d'employeur
<br/>
Paragraphes niveau 3 et 4 :
<br/>{0} Formule de politesse
<br/>{1} Genre d'affilé (Cotisations paritaires ou Cotisations personnelles), valable uniquement si la caisse sépare les affiliations paritaires et personnelles par un rôle différent.
<br/>{2} Genre de décompte (Description par rapport au type, par exemple)
<br/>{3} Numéro de rapport de contrôle d'employeur
</p>

<p>
<h5>3.2. Modification : Relevés à blanc, prévision pour acomptes, bouclement d'acompte</h5><br/>
Ajout de la date et de la signature pour le retour du document.
<br/>
Modification de la mise en page de ces documents.
</p>

<p>
<h5>3.3. Modification : Imputation (cotisations personnelles)</h5><br/>
Ajout d'une variable {0} représentant le code politesse (Madame, Monsieur, etc..) sur le document d'imputation. Cette variable est reconnue uniquement dans les paragraphes 2,1 et 5,1.
</p>

<p>
<h5>3.4. Nouveau : Lettre aux rentiers non-actifs (mandat 2.31)</h5><br/>
Ce nouveau document figure dans le catalogue de textes de la facturation. Une version initiale du document est proposée et doit être adaptée si la caisse décide d'utiliser la fonctionnalité (signature, texte, etc.).
</p>

<p>
<h5>3.5. Nouveau : Liste de concordance NNSS</h5><br/>
Ce document est utilisé pour accompagner la liste de concordance no AVS-> NNSS transmise à l'affilié. Elle sera à partir du 4è trimestre 2008. Une version initiale du document a été chargée et doit être adaptée par la caisse.
</p>

<p>
<h5>3.6. Modification : décision pour indépendants, bénéfice en capital</h5><br/>
Mise à jour du catalogue pour décision avec bénéfice en capital.
</p>

<p>
<h5>3.7. Nouveau : Lettre d'accompagnement pour certificat d'assurance (NNSS)</h5><br/>
Ce nouveau document figure dans le catalogue de texte des ARC/ZAS. Une version initiale du document est proposée et doit être adaptée pour la mise en production au 1er juillet 2008.
</p>

<p>
<h5>3.8. Nouveau : Lettre attestation certificat assurance (NNSS)</h5><br/>
Ce nouveau document figure dans le catalogue de texte des ARC/ZAS. Une version initiale du document est proposée et doit être adaptée pour la mise en production au 1er juillet 2008.
</p>

<p>
<h5>3.9. Modification : demande de revenus et bilans</h5><br/>
Les documents de demande de revenus et bilans ont été adaptés. Le texte est à contrôler et à modifier le cas échéant si la caisse utilise cette fonctionnalité.
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
				<DT>Eventail des régimes</DT>
				<DD>Ce nouveau masque affiche une vue d'ensemble de la situation d'un tiers, notamment ses données d'affiliation paritaire et personnelle, l'état de la dernière déclaration de salaires et le solde en compte.</DD>
				<DT>Blocage total des envois</DT>
				<DD>Cette fonctionnalité permet de bloquer tout envoi de document pour un tiers.</DD>
				</DL><H5>Déclarations de salaires</H5>
			<DL>
			<DT>Gestion de la catégorie de salarié</DT>
			<DD>Cette fonction disponible au niveau de la saisie individuelle permet de différencier à l'aide d'un code particulier les salariés soumis à une partie de plan d'assurance de l'employeur.</DD>
			<DT>Déclarations de salaires communes (spécifique CCVD)</DT>
			<DD>Cette fonctionnalité permet de générer un décompte final AGRIVIT sur la base des données de la déclaration de salaires saisie dans l'instance de la caisse cantonale.</DD>
			<DT></DT>
			</DL><H5>Comptes individuels</H5>
			<DL>
			<DT>NNSS - lot numéro 3</DT>
			<DD>3ème lot de fonctionnalité pour le NNSS. Ces fonctionnalités seront utilisées à partir du 1er juillet 2008.</DD>
			<DT>Nombre d'entêtes CI ouvertes</DT>
			<DD>La statistique CI donne désormais le nombre d'entêtes CI ouvertes.</DD>

			</DL>
			<H5>Affiliation</H5>
			<DL>
			<DT>Saisie des masses </DT>
			<DD>Le masque de saisie des masques (option &quot;modifier masses&quot;) a été amélioré. La masse AVS est notamment copiée automatiquement dans les autres assurances.</DD>
			<DT>Gestion des quittances pour bénéficiaires PC (spécifique agence de Lausanne, CCVD)</DT>
			<DD>Cette nouvelle fonctionnalité permet de saisir, de facturer et d'inscrire au CI les quittances des aides ménages octroyées aux bénéficiaires de PC.</DD>
			<DT>Acomptes &quot;à la carte&quot;</DT>
			<DD>Cette fonctionnalité permet de gérer des acomptes variables durant l'année. Elle est utile notamment pour les entreprises avec activité saisonnière.</DD>
			<DT>Suffixe pour assistés, réfugiés et bénéficiaires PC (spécifique agence de Lausanne)</DT>
			<DD>Un suffixe particulier est attribué automatiquement aux non-actifs et indépendants assistés, réfugiés et bénéficiaires de PC.</DD>
<DT>Synchronisation des taux entre le plan d'assurance et la comptabilité auxiliaire</DT>
			<DD>Cette fonctionnalité permet de synchroniser automatiquement les taux entre le plan d'assurance et la comptabilité auxiliaire.</DD>
			</DL><H5>Comptabilité auxiliaire</H5>

			<DL>
				<DT>Adresse de domicile sur les poursuites</DT>
				<DD>L'adresse de domicile figure systématiquement sur les documents de poursuites en lieu et place de l'adresse de courrier. </DD>
				<DT>Facturation des frais d'administration (spécifique caisse 51.10)</DT>
				<DT>Remboursement des allocations familiales (spécifique caisse 51.10)</DT>
				<DD>Cette nouvelle fonctionnalité permet de rembourser automatiquement toutes les factures dont le solde du secteur des allocations familiales est négatif .</DD>
				<DT>Nouvelle zone "numéro de référence BVR" pour reprise de données</DT>
				<DD>Cette nouvelle zone permet de migrer le numéro de référence BVR associé à une facture. Il est utile lors de la migration des données.</DD>

			</DL>
			<H5>Comptabilité générale</H5>
			<DL>
			<DT>Exportation du bilan, du compte P&amp;P et de la balance au format Excel</DT>
			<DD>Les 3 listes principales de la comptabilité générale peuvent désormais s'exporter au format Excel ou pdf.</DD>
			<DT>Ventilation du compte 2000.1102.0000</DT>
			<DD>L'application ventile désormais tous les comptes 200x.1102.0000 au bouclement au lieu de se limiter au compte 2000.1102.0000.</DD>
			<DT>Incrémentation automatique des pièces comptables</DT>
			<DD>Cette fonctionnalité permet de numéroter automatiquement les pièces comptables manuelles saisies en comptabilité générale.</DD>
			<DT>Séparation de la quote-part ACM maternité et militaire (spécifique caisses horlogères)</DT>
			<DD>Les quotes-parts ACM maternité et militaires s'enregistrent désormais sur deux comptes différents et figurent séparément sur l'extait récapitulatif ALFA.</DD>
			</DL>

			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>Cotisations AC II pour TSE</DT>
			<DD>Les cotisations AC II sont désormais calculées pour les TSE.</DD>
			<DT>Plausibilités pour traitement des communications fiscales</DT>
			<DD>Les plausibilités pour le traitement des communications fiscales genevoises et jurassiennes sont implémentées.</DD>
			<DT>Duplicata</DT>
			<DD>Cette fonctionnalité permet d'imprimer un duplicata d'une décision de cotisations personnelles.</DD>
			<DT>Gestion des étudiants (spécifique agence de Lausanne, CCGC)</DT>
			<DD>Ce nouveau module associé aux cotisations personnelles permet de traiter électroniquement les listes d'étudiants fournies par les universités et par l'école polytechnique et de produire un décompte de cotisations personnelles.</DD>
			</DL>
			<H5>IJAI</H5>
			<DL>
			<DT>Echéance</DT>
			<DD>Nouvelle échéance 'manuelle' pour la révision des cas IJAI.</DD>
			<DT>Bases indemnisations</DT>
			<DD>Possibilité de saisir une base d'indemnisation sur plusieurs mois.</DD>
			<DT>Listes</DT>
			<DD>Attestations fiscales IJAI : indique le détail des périodes des prestations.</DD>
			<DT>Compensations</DT>
			<DD>Possibilité de choisir manuellement une autre facture dans l'écran de gestion des compensations</DD>
			<DT>AIT</DT>
			<DD>Prise en compte des allocations d'initiation au travail</DD>
			<DT>Allocation d'assistance</DT>
			<DD>Prise en compte des allocations d'assistance</DD>
			<DT>Décision IJAI</DT>
			<DD>Possibilité de sortir les décisions IJ pour les grandes et petites IJ</DD>


			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Plausibilités</DT>
			<DD>Contrôle du nombre de jours de protection civile par année : Message d'avertissement si supérieur à 25.</DD>
			<DT>Calcul prestations</DT>
			<DD>Nouvelle règles pour le calcul des cotisations, selon validation CU.</DD>
			<DT>Listes</DT>
			<DD>Attestations fiscales : indique le détail des périodes des prestations</DD>
			<DT>Compensations</DT>
			<DD>Possibilité de choisir manuellement une autre facture dans l'écran de gestion des compensations</DD>
			<DT>Décision/communication d'allocation de maternité</DT>
			<DD>Avec / sans moyen de droit</DD>
			<DD>Si versement à l'employeur, une copie du document est envoyée à la mère avec lettre d'accompagnement (paramétrage).</DD>
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
				<DD>Deux nouvelles fonctionnalités sont disponibles et permettent d'imprimer un rappel pour un plan de paiement particulier ou en masse pour l'ensemble des échéances non respectées.</DD>
				<DT>Décisions de sursis au paiement</DT>
				<DD>Il est possible de gérer plusieurs décisions de sursis dans le catalogue de texte et de choisir au moment de l'impression quel formulaire utiliser.</DD>
				<DT>Suspension automatique du plan de paiement</DT>
				<DD>Une nouvelle prodécure automatisée permet de suspendre automatiquement les plans de paiement non respectés, de manière à ce que les étapes suivantes du contentieux se poursuivent normalement.</DD>
			</DL><H5>Déclarations de salaires</H5>
			<DL>
			<DT>Total AC I et AF</DT>
			<DD>Le masque de saisie individuelle affiche les totaux AC I et AF. </DD>
			<DT>Déclarations de salaires à zéro</DT>
			<DD>Lorsque le montant effectif des cotisations du décompte final correspond à l'acompte facturé durant l'année, une ligne de facture est tout de même générée pour indiquer à l'employeur que la cotisation en question est acquittée.</DD>
			<DT>Comptabilisation automatique des journaux CI provenant de la saisie individuelle</DT>
			<DD>Les journaux CI provenant de la saisie individuelle peuvent désormais être comptabilisé automatiquement lors de la comptabilisation du journal de facturation.</DD>
			</DL><H5>Comptes individuels</H5>
			<DL>
			<DT>NNSS - support complet du NNSS dans le batch</DT>
			<DD>Le batch CI supporte désormais toutes les annonces avec NNSS, soit les ouvertures, rassemblements, splittings et clôtures.</DD>
			<DT>Support du format PUCS 2</DT>
			<DD>Le format PUCS 2 est désormais supporté par l'application.</DD>

			</DL>
			<H5>Affiliation</H5>
			<DL>
			<DT>Suivi des relevés</DT>
			<DD>Les relevés sont désormais suivis par plan d'affiliation</DD>
			<DT>Post-it</DT>
			<DD>Divers améliorations ont été apportées.</DD>
			<DT>NNSS - ouverture des CI pour indépendants et non-actifs</DT>
			<DD>Dans le cadre du NNSS, cette procédure permettra l'ouverture en masse des CI des indépendants et non-actifs.  sera exécutée durant le 2ème semestre 2008</DD>
			<DT>Calcul rétroactif lors d'un changement de masse</DT>
			<DD>Cette fonctionnalité a été améliorée pour tenir compte de cas de figure tels qu'une affiliation rétroactive avec périodicité trimestrielle au cours d'un trimestre.</DD>
			<DT>Création automatique d'un journal pour les relevés</DT>
			<DD>Le journal de facturation de type relevé peut sur demande être automatiquement ouvert après comptabilisation du précédent.</DD>
			<DT>Support des affiliés dont les cotisations ont plusieurs périodicités</DT>
			<DD>Lorsque des cotisations de périodicités différentes figurent dans le plan d'assurance (cotisations mensuelles et trimestrielles), le système gère désormais correctement la situation.</DD>
			<DT>Formulaire de prévision pour acompte</DT>
			<DD>Ce nouveau formulaire permet de demander à l'employeur une estimation de sa masse salariale. Il tient compte du plan d'assurance. Une nouvelle option figure dans la définition des assurances et permet de paramétrer les cotisations que l'on désire voir figurer sur ce formulaire. Ce document peut également faire l'objet d'un envoi en masse.</DD><DT>Formulaire de bouclement d'acomptes</DT><DD>Ce nouveau formlaire permet à l'employeur de transmettre une estimation de la masse salariale effective de l'année pour permettre une facturation rapide sous la forme d'un bouclement d'acompte. Ce document peut faire l'objet d'un envoi de masse en fin d'année.</DD><DT>Formulaire de confirmation/décision d'acompte</DT><DD>Ce formulaire a été adapté afin de tenir compte des assurances que l'on désire voir figurer sur le document. Il peut également faire l'objet d'un envoi en masse.</DD><DT>Loi sur le travail au noir (LTN), lot no 1</DT><DD>Une nouveau type d'affiliation permet de gérer les affiliés déclarés selon la procédure LTN.</DD></DL><H5>Comptabilité auxiliaire</H5>

			<DL>
				<DT>Enquête sur les cotisations arriérées</DT>
				<DD>Cette nouvelle liste permet d'éditer le tableau des cotisations arriérées demandé par l'OFAS au bouclement annuel. </DD>
				<DT>Caisse métier</DT>
				<DD>La notion de caisse métier a été étendue aux rubriques de taxes, amendes et intérêts.</DD>
				<DT>Priorité de ventilation pour les taxes et amendes</DT>
				<DD>Les taxes et amendes peuvent être imputés sur un compte courant particulier du secteur 900, avec un priorité de ventilation inférieure à celle des cotisations. Cela permet un calcul d'intérêts moratoires plus rapide si l'affilié paye le décompte en omettant la taxe de sommation par exemple. Les taxes et amendes seront couverts après les cotisations et le frais d'administration si cette solution est choisie.</DD>

			</DL>
			<H5>Comptabilité générale</H5>
			<DL>
			<DT>Ecritures collectives</DT>
			<DD>Dans le détail d'une écriture dont la contrepartie est multiple, un lien a été ajouté permettant d'afficher l'ensemble des écritures collectives de la pièce.</DD>
			<DT>Modèles d'écritures</DT>
			<DD>Les centres de charges sont désormais supportés.</DD>
			<DT>Récapitulation des rentes</DT>
			<DD>Un nouveau masque permet - pour les caisses qui n'utilisent pas l'application Rentes de Globaz - de saisir la récapitulation des rentes.</DD>
			</DL>
			<H5>Facturation</H5>
			<DL>
			<DT>Liste des indépendants taxés définitivement ayant touché des APG</DT>
			<DD>Cette liste peut désormais être imprimée sur demande via l'option "liste" du journal de facturation.</DD>
			</DL>
			<H5>ARC/ZAS</H5>
			<DL>
			<DT>NNSS</DT>
			<DD>Une file d'attente différée pour la transmission en masse des demandes d'ouvertures et de CA dans le cadre de la procédure NNSS a été mise en place.</DD>
			<DT>TRAX</DT>
			<DD>TRAX est désormais supporté par l'application.</DD>
			</DL>
			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>Nouveau masque de recherche de décisions</DT>
			<DD>Ce nouveau masque permet de rechercher des décisions selon différents critères, notamment les décisions non validées ou en suspens.</DD>
			<DT>Comptabilisation automatique du journal CI</DT>
			<DD>Le journal CI provenant des décisions de cotisations personnelles peut être au choix comptabilisé automatiquement ou sur demande.</DD>
			<DT>Non-actifs : cotisation minimale acquittée par la collectivité publique</DT>
			<DD>Le code spécial 01 qui figure dans l'inscription CI est désormais géré pour ces cas via les décisions de cotisations personnelles.</DD>
			<DT>Catalogue de texte</DT>
			<DD>L'ensemble des décisions de cotisations personnelles ont été migrées dans le catalogue de textes. La caisse peut désormais adapter le recto et le verso des décisions.</DD>

			</DL>
			<H5>IJAI</H5>
			<DL>
			<DT>Recherche</DT>
			<DD>Recherche sur les noms prénoms des assurés indépendemment de la 'CASE' (fonctionne avec ou sans accent).</DD>
			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Recherche</DT>
			<DD>Recherche sur les noms prénoms des assurés indépendemment de la 'CASE' (fonctionne avec ou sans accent).</DD>
			<DT>Maternité Cantonale</DT>
			<DD>Prise en compte du nouveau montant LAA. Dès le 1er janvier 2008, l'indemnité journalière maximale LAMat sera de 280 CHF et non-plus de 237,60 CHF.</DD>
			</DL>
			<H5>HERA</H5>
			<DL>
			<DT>Recherche</DT>
			<DD>Recherche sur les noms prénoms des assurés indépendemment de la 'CASE' (fonctionne avec ou sans accent).</DD>
			<DD>Optimisation des temps de réponses dans les écrans de recherches.</DD>
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
				<DD>Les masques et les listes ont été adaptés à l'introduction du NNSS</DD>
			</DL><H5>Déclarations de salaires</H5>
			<DL>
			<DT>NNSS</DT>
			<DD>Les masques et les listes ont été adaptés à l'introduction du NNSS.</DD>
				<DT>Saisie du nom de l'assuré</DT>
				<DD>Lorsque le numéro d'assuré est inconnu ou incomplet ou n'existe pas au registre des assurés, on peut désormais saisir sont nom afin d'améliorer la gestion des suspens.</DD>
				<DD></DD>
			</DL>
			<H5>ARC/ZAS</H5>
			<DL>
			<DT>NNSS</DT>
			<DD>Les masques et les listes ont été adaptés à l'introduction du NNSS.</DD></DL><H5>Comptabilité auxiliaire</H5>
			<DL>
			<DT>Listes des soldes par secteur</DT>
			<DD>Cette nouvelle fonctionnalité permet d'imprimer une liste des soldes par secteur / compte-courant sous forme Excel.</DD>
				<DT>Liste des soldes ouvertes à une date donnée</DT>
				<DD>Cette liste permet d'extraire toutes les factures dont la date de création est antérieure à une date de référence (par exemple le 31.12.2006) qui sont encore ouverte à ce jour.</DD>
				<DT>Annulation de montants minimes par secteur</DT>
				<DD>Cette fonctionnalité pemet d'annuler des montants minimes par secteur / compte-courant.</DD>
				<DT>Recherche de montants</DT>
				<DD>Cette fonctionnalitlé permet de recherche des factures ouvertes dont le solde correspond à un montant donné, ou des écritures correspondant à un montant donné.</DD>
				<DT>Liste des comptes irrécouvrables et rentiers</DT>
				<DD>Cette liste permet d'extraire les factures dont le stade de sommation est dépassé pour les comptes dont le motif de blocage est irrécouvrable ou rentier.</DD>
				<DT>Réorganisation du menu liste</DT>
				<DD>Au vu du grand nombre de listes disponibles en comptabilité auxiliaire, le menu a été régoranisé en chapitres afin d'améliorer la lisibilité.</DD>
			</DL><H5>IJAI</H5>
			<DL>
			<DT>GED</DT>
			<DD>Possibilité d'ajouter les décomptes IJ dans la GED.</DD>
			<DT>Navigation</DT>
			<DD>Récupération des critères de recherches dans les retours arrières (backs)</DD>
			<DT>NNSS</DT>
			<DD>Migration écrans et listes aux NNSS</DD>
			<DT>Rentes AI en cours</DT>
			<DD>Prise en compte du RAM, de l'échelle et des codes cas spéciaux pour le rentes AI en cours.</DD>
			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Calcul des prestations</DT>
			<DD>Ajouté la possibilité de forcer le calcul des prestations LAMAT.</DD>
			<DD>Ajouté la possibilité de forcer le calcul des prestations ACM.</DD>
			<DD>Possibilité de modifier manuellement les répartitions de pmts.</DD>
			<DT>Compta</DT>
			<DD>Différentiation des rubriques dans la CA pour les prestations LAMAT NAISSANCE/ADOPTION.</DD>
			<DT>Navigation</DT>
			<DD>Récupération des critères de recherches dans les retours arrières (backs)</DD>
			<DT>NNSS</DT>
			<DD>Migration écrans et listes aux NNSS</DD>
			</DL>

			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>Liste de compraison entre les cotisations personnelles et la comptabilité</DT>
			<DD>Cette liste permet de comparer et d'extraire les différences entre l'historique des décisions de cotisations personnelles et la comptabilité auxiliaire.</DD>
			<DT>Traitement des communications fiscales</DT>
			<DD>Traitement et validation des CF en retour du fisc.</DD>
			<DT>Traduction des demandes de communications fiscales</DT>
			<DD>Les documents papier de demande de communications fiscales ont été traduits en allemand pour les organes allémaniques.</DD>
			<DT>Calculs particuliers pour les non-actifs</DT>
			<DD>L'application prend désormais en charge des cas particuliers de départ à l'étranger et de décès pour les non-actifs.</DD>
				<DT>NNSS</DT>
				<DD>Les masques et les listes ont été adaptées au NNSS. L'application gère également le changement de numéro AVS ou la bascule AVS/NSS lors de l'inscription CI en se basant sur l'historique des numéro AVS de l'application tiers.</DD>
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
				<DT>Détection des doublons</DT>
				<DD>Lorsque l'on désire créer une nouvelle personne physique, tiers affiche pour vérification toutes les personnes dont la date de naissance et le sexe sont identiques. L'utilisateur peut choisir l'une des personnes proposées et annuler la création, ou valider la création de la nouvelle personne.</DD>
			</DL>
			<H5>Affiliation</H5>
			<DL>
			<DT>Adhésion caisse principale</DT>
			<DD>Le masque de création d'un affilié a été modifié afin de permettre de désigner la caisse principale durant le choix des plans de caisse.</DD>
			<DT>Facturation avec plans d'affiliation multiples</DT>
			<DD>Un libellé particulier est désormais géré au niveau du plan d'affiliation. Si celui-ci est renseigné, il figure sur la facture en regard du numéro de décompte. Cette fonctionnalité est utile lorsque l'on génère deux décomptes périodiques pour un même affilié, l'un pour son personnel d'exploitation et l'autre pour son personnel d'administration.</DD>
			<DT>Contrôle d'employeur</DT>
			<DD>Le masque de saisie du contrôle d'employeur a été étendu selon les dernières spécifications. Un nouveau masque d'attribution des réviseurs a été ajouté, ainsi que deux nouvelles listes, l'une permettant de lister les contrôles prévus pour une année, l'autre permettant de lister les contrôles effectués.</DD>
				<DT>Facturation à d'autres institutions</DT>
				<DD>Cette nouvelle fonctionnalité permet la prise en charge de factures de cotisations par une autre institution, par exemple les services sociaux, une commune, le service des prestations complémentaires, etc. Elle permet également de prendre en charge une cotisation particulière (par exemple la cotisation FFPP) par une autre institution.</DD>
				<DT>Recherche sur un ancien numéro d'affilié</DT>
				<DD>Cette fonctionnalité permet de rechercher un affilié par un ancien numéro provenant par exemple d'une migration des données. Elle se base sur le champ &quot;ancien numéro d'affilié&quot; du masque principal de l'affiliation.</DD>
			<DT>Avis de mutations au caisses cantonales</DT>
			<DD>Une nouvelle fonctionnalité permet d'imprimer en une seule fois l'ensemble des mutations encodées.</DD>
			</DL>
			<H5>Attestations de salaires</H5>
			<DL>
			<DT>Attestation de salaire vide</DT>
			<DD>Cette nouvelle option permet d'imprimer une attestation de salaires sans mention des salariés.</DD>
			</DL>
			<H5>ARC/ZAS</H5>
			<DL>
			<DT>Critères de recherche</DT>
			<DD>Les critères de recherche des principaux masques sont désormais conservés lorsque l'on utilise la fonction "back". Veiller à n'utiliser que la fonction "back" de l'application et non pas celle du navigateur internet.</DD>
			<DT>CI additionnels</DT>
			<DD>Une nouvelle option permettant de rechercher des CI additionnels a été ajoutée. Il est également possible de les imprimer.</DD>
			<DT>Réimpression des documents du traitement journalier</DT>
			<DD>L'ensemble des documents produits par le traitement journalier des CI (extraits terminés, CI additionnels, accusé de réception, lettre aux affiliés) peuvent être réimprimés via la gestion des lots.</DD>
			</DL>
			<H5>Comptabilité auxiliaire</H5>
			<DL>
			<DT>Bulletins de solde</DT>
			<DD>Une nouvelle fonctionnalité permettant d'imprimer automatiquement des bulletins de soldes après un paiement partiel est disponible.</DD>
			</DL>
			<H5>Comptes individuels</H5>
			<DL>
			<DT>Historisation des inscriptions modifiées</DT>
			<DD>Lorsque l'on modifie une inscription CI (période, remarque, centimes), les anciennes valeurs sont sauvegardées et visibles dans l'historique.</DD>
			<DT>Format de déclarations de salaires électroniques</DT>
			<DD>Une nouvelle option permet de gérer différents format de déclarations de salaires électroniques</DD>
				<DT>NNSS</DT>
				<DD>Les masques écrans ont été adaptés à la future introduction du nouveau numéro de sécurité sociale. Le champ &quot;No AVS&quot; a été remplacé par NSS. La date de naissance, le sexe et la nationalité figurent sur tous les écrans lorsque ces informations sont connues. </DD>
			</DL>
			<H5>Facturation</H5>
			<DL>
			<DT>Date de valeur</DT>
			<DD>Un nouveau champ "date de valeur" permet de forcer une date de valeur particulière pour une écriture. Cette fonctionnalité est utile notamment pour les compensations.</DD>
			<DT>Date de réception de la déclaration de salaires</DT>
			<DD>Un nouveau champ "date de réception" peut être utilisé lors de la saisie de déclarations de salaires manuelles. Cette date est déterminante pour le calcul des intérêts moratoires et rémunératoires.</DD>
			</DL>
			<H5>IJAI</H5>
			<DL>
			<DT>Impression des attestations</DT>
			<DD>Une nouvelle option permet d'imprimer des attestations pour un prononcé.</DD>
			<DT>Echéances</DT>
			<DD>Nouvelle gestion des échéances avec historique (date de lancement, gestionnaire...). Une nouvelle liste est également disponible : liste des prononcés actifs depuis plus de 2 ans</DD>
			<DT>Annonces IJ</DT>
			<DD>Correction des annonces IJ (Etat civil, Rev. jour. déterminant pour les petites IJ, et Déduc. nourriture et logement).</DD>
			<DT>Impôts à la source</DT>
			<DD>Possibilité de modifier les taux d'impositions par canton (Barême D)</DD>
			<DT>Listes</DT>
			<DD>La liste des attestations non reçues est triées par nss.</DD>
			<DT>Prononce IJ</DT>
			<DD>Nouvelle saisie permettant de définir au niveau du prononcé le type d'indemnistation (interne ou externe). Cette information est affichée dans la saisie de la base d'indemnisation.</DD>
			</DL>
			<H5>APG/MAT</H5>
			<DL>
			<DT>Impôts à la source</DT>
			<DD>Possibilité de modifier les taux d'impositions par canton (Barême D)</DD>
			</DL>

			<H5>Cotisations personnelles</H5>
			<DL>
			<DT>Réductions et remises</DT>
			<DD>Deux nouveaux types de décision ont été introduits afin de gérer les remises et les réductions de cotisations.</DD>
			<DT>Catalogue de textes</DT>
			<DD>Le texte des décisions d'acompte ainsi que les lettres sont désormais gérées via le catalogue de texte.</DD>
			<DT>Liste de concordance</DT>
			<DD>Cette nouvelle liste permet de vérifier la concordance entre le CI et l'historique des décisions de cotisations personnelles.</DD>
			<DT>Uniformisation des francs</DT>
			<DD>L'abréviation CHF est utilisée sur l'ensemble des documents.</DD>
			<DT>Facturation à d'autres institutions</DT>
			<DD>Possiblité d'indiquer une remarque sur la décision si les cotisations de l'affilié sont prises en charge par une autre institution (cf. module affiliation).</DD>
			<DT>Communications fiscales</DT>
			<DD>Impression de des communications fiscales par canton sous Excel selon le tableau récapitulatif de l'OFAS.</DD>
			<DT>Cotisation minimum</DT>
			<DD>Indication de la cotisation minimum dans le tableau des cotisations de la décision.</DD>
			<DT>Comptabilisation des journaux CI</DT>
			<DD>Possibilité de comptabiliser automatiquement le journal CI.</DD>
			<DT>Statistique des décisions par journal</DT>
			<DD>Possibilité d'activer ou de désactiver l'impression automatique de la statistique des décisions par journal.</DD>
			</DL>
			</TD>
        </TR>
</TABLE>
</BODY>
</HTML>
