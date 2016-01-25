<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <% String servletContext = "../../../";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
<head>
<meta name="User-Lang" content="FR"/>
<meta name="Context_URL" content="../../../"/> 

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>Documentation for notation</title>
	<script type="text/javascript" src="<%=servletContext%>scripts/jquery.js"> </script>
	<script type="text/javascript" src="<%=servletContext%>scripts/jquery-ui.js"> </script>
	
	<script type="text/javascript" src="../javascriptLibs/syntaxhighlighte/shCore.js"></script>
	<script type="text/javascript" src="../javascriptLibs/syntaxhighlighte/shBrushJScript.js"></script>
	<script type="text/javascript" src="../javascriptLibs/syntaxhighlighte/shBrushXml.js"></script>
	<script type="text/javascript" src="<%=servletContext%>scripts/globazJqueryPlugin.js"></script>
	<script type="text/javascript" src="<%=servletContext%>scripts/widget/globazwidget.js"> </script>

    
  <%@ include file="../notationLibJs.jspf" %> 
  <script type="text/javascript" src="notationDocumentation.js"></script>
  <script type="text/javascript" src="documentationNotation.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=servletContext%>theme/jquery/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="<%=servletContext%>theme/master.css" />
	<link rel="stylesheet" type="text/css" href="../javascriptLibs/syntaxhighlighte/shCore.css"/>
	<link rel="stylesheet" type="text/css" href="../javascriptLibs/syntaxhighlighte/shThemeDefault.css"/>
	<link rel="stylesheet" type="text/css" href="documentationNotation.css"/>
</head>
<body>
	<script language="JavaScript">

	</script>
	<div id="documentationNotationCreation">
		<div id="explication" class="ui-widget-content ui-corner-all">
		    <h1 id="prerqui" class="explication">Prés requis</h1>
		    <div id="pre" class="description">
		    	<ul>
			    	<li>Il ne faut jamais utiliser le nom <strong>"globazNotation"</strong> pour la création de variables javascript!!</li>
			    	<li>Il est important que tous les fichiers javascript qui définissent les notations se trouvent dans le fichier notation</li>
			    	<li>Pour l'inclusion du fichier javascript, utilisez le fichier <strong>notationLibJs.jspf</strong></li>
			    	<li><h2>Convention de nommage:</h2> 
			    		<ul class="liWithStyle">
				    		<li><code><strong>$xyz</strong></code> : Objet jquery</li>
				    		<li><code><strong>o_xyz</strong></code> : contient un objet(générique ou un élément DOM) </li> 
				    		<li><code><strong>b_xyz</strong></code> : contient un boolean</li>
				    		<li><code><strong>d_xyz</strong></code> : contient une date</li>
				    		<li><code><strong>s_xyz</strong></code> : contient un string</li>
				    		<li><code><strong>n_xyz</strong></code> : contient un numérique</li>
				    		<li><code><strong>t_xyz</strong></code> : contient un tableau</li>
				    		<li><code><strong>f_xyz</strong></code> : contient une fonction</li>
				    		<li><code><strong>_nomDeFonction</strong></code> : indique que l'on a une fonction privée</li>
				    		<li><code><strong>_nomAttribut</strong></code> : indique que l'on a un attribut privé</li>
				    	</ul>
			    	</li>
			    </ul>
		    </div>
		    <h1 id="utilisation" class="explication">Utilisation des notations</h1>
		    <div id="utisationHtml" class="description">  
		    	<div>	
		          Pour utiliser une notation, il suffit de l'intégrer de la manière suivante : <br />
		      	  <div class="docExemple"> 
		      	  	"data-g-notation='param1:1,parm2:2'"
		      	  </div>
		      	  On place la notation sur l'élément html qui doit avoir le comportement désiré. <br />
		      	  <strong>Toutes les options qui ne sont pas du type string doivent être définies avec le symbole "¦" </strong> 
		      	   <div class="docExemple">opt1:<strong>¦</strong>valeur1,valeur2<strong>¦</strong>
		      	  </div>
		      	   
		      	   Il est possible d'utiliser des structures conditionnelles dans la notation pour les options boolean <br />
		      	   Si une notation n'a pas besoin de paramètre, il faut mettre un  <strong>espace entre les guillemets</strong><br />
		      	   Exemple: 
		      	 </div>
		      	 
				 <pre class="brush: html; toolbar: false">
				   //par défaut (avec un espace)
				  &lt; input type="text"  value="" data-g-integer=" " /&gt;
				   //avec parametre
				  &lt; input type="text"  value="" data-g-integer="sizeMax:3" /&gt;
				  // Structures conditionnelles
				  &lt; input type="text"  value="" data-g-amount="mandatory:true==true&&1&gt;3" /&gt;
				  //Avec plusieur valeurs
				  &lt; input type="text"  value="" data-g-test="mandatory:true,params:¦valeu1,value2,¦,params3:¦[1,2,3,'string']¦" /&gt;
				 </pre>
				 <div> 
				 	<h2>Utilisation des notations dans les cutomTags </h2>
				 	<div>
				 		Un attribut "notation" a été ajouté aux customTags susceptibles d'avoir une notation. 
				 	</div>
				 	<code><div class="docExemple">
				 	  &lt;customTag  <strong>notation="data-g-select='madandatory:true'"</strong> customTag /&gt; 
				 	</div></code>
				 </div>
				 <h2>Il est possible de connaître le temps d'exécution de chaque objet.</h2>
				 <ul class="liWithStyle">
					<li>En ajoutant cette attribut sur n'import quel tag html : <strong><code>showInfos="false"</code></strong>
					</li>
					<li>En mettant la chaîne suivante
						<ul>  
							<li>dans fireBug sur firefox : <strong><code>notationManager.showInfos();</code></strong></li>
							<li>dans la barre d'adresse : <strong><code>javascript:notationManager.showInfos();</code></strong></li>
						</ul>
					</li>  
				 </ul>
				 <br />
		    </div>
		    <h1 id="i18n"" class="explication">I18n</h1>
		   	<div class="description">  
		    	<div>	
			    	<p>
			    		Il est possible d'internationaliser un texte utilisé dans une notation.<br/>
			    		La structure utilisée est la même que les .properties de java.<br/>
			    		La clé pour les textes doit être composée de la manière suivante pour les objets de la notation.<br/>
			    		On met le nom "notation", le nom de l'objet où se trouve la traduction, puis un identifiant pour le texte.
					</p>
					 <div class="docExemple">
					     notation.nomObjet.identifiantText <br/>
					     notation.amount.pasUnNombre
					  </div>
					  <p>
					  	Pour internationaliser un texte dans un objet de la notation il faut utiliser la fonction <strong><code>i18n(s_key)</code></strong><br/>
						<strong>ATTENTION:</strong> la clé à utiliser ne doit pas être précédée de "notation.nomObjet", il faut utilsier l'identifiant du texte
					  </p>
					  <div class="docExemple">
					    properties => notation.nomObjet.hello = hello <br/>
						js => this.i18n('hello');
					  </div>
					<p>
						Il est possible d'utiliser des variables dans un texte à traduir.
					</p>
					  <div class="docExemple">
					    properties => notation.nomObjet.bonneJournee = bonne journée {0} !<br/>
						js => this.i18n('bonneJournee',['jean']);
					  </div>
		      	</div>
			</div>
		    <h1 id="GestionErreur"" class="explication">Gestion des erreurs</h1>
		   	<div class="description">  
		    	<div>	
			    	<p>Le moteur des notations gère une partie des erreurs de syntaxe liée à la notation, 
			    	ainsi que les types de données qui sont utilisés dans les options des objets et aussi les erreurs JavaScripts.
					</p>
					<p>Par contre il ne gère pas les erreurs qui peuvent arriver dans les objets des notations. <br />
					 C'est pour cette raison que le l'on peut utiliser <code><strong>this.putError( s_messageHtml )</strong></code><br />
					 Cette fonction ajoutera les erreurs au moteur des notations;
					 On peut aussi utiliser la méthode <code><strong>this.putFormattedError( s_typeError, s_elementInError, s_messageHtml )</strong></code> qui formate automatiquement les erreurs.
					</p>
					<p><strong>Dans un souci d'uniformisation, si vous utilisez <code><strong>this.putError( s_messageHtml)</strong></code> <br /> 
					 	merci d'utiliser le canevas suivant pour gérer les erreurs dans les notations.</strong></p>
					<p>Dans les fichiers utilitaires il faut lancer des exceptions et ne pas utiliser les fonctions du moteur. <br />
					   Si une exception est soulevée dans un fichier utilitaire lors de l'initialisation des notations, 
					   le moteur de notation l'interceptera dans une clause "try catch". Sinon c'est l'objet de la notations qui doit gérer ces exceptions</p>
		      	</div>

				<h2>Caneva</h2>
		    	<pre class="brush: html; toolbar: false">
				   	&lt;div&gt;stronggt;Type d'erreur &lt;i class='ui-state-error-text'&gt; 
					&lt;/i&gt;Element en erreur&lt;/strong&gt;&lt;br /&gt;
					Info erreur en html.
					&lt;/div&gt;
				 </pre>
			</div>
		    <h1 id="objetNotation"" class="explication">Objets notation (heritage)</h1>
		   	<div class="description">  
		    	<div>	
		    		Le moteur des notation ajoute plusieurs attributs aux objets de la notation. <br /> 
		    		On a en faite un principe de pseudo heritage:
		      	</div>

				 <h2>Liste des attributs </h2>
				 <ul class="liWithStyle">
				 	 <li><code><strong>this.s_contextUrl</strong></code> : Indique l'URL de la l'application</li> 
					 <li><code><strong>this.$elementToPutObject</strong></code> : Objet jquery où se trouve la notation</li>
					 <li><code><strong>this.i18n( s_key )</strong></code> : Permet de traduire un texte dans les objets notation</li>
					 <li><code><strong>this.putError( s_message )</strong></code> : Permet d'ajouter des messages d'erreur liés à l'objet message non formaté. <br />
					     Merci d'utiliser le canevas. (voir Gestion des erreurs)
					 </li>
					 <li><code><strong>this.putFormattedError( s_typeError, s_elementInError, s_messageHtml )</strong></code> : Message d'erreur formaté</li>
					 <li><code><strong>this.createParams( s_chaineDeParametre )</strong></code> : 
					 <br/>Parcours la chaîne de caractères qui doit être dans le même format que les paramètres de la notation ("param1:true,params2:toto"). 
					 <br/>Retourne un objet(JSON) contenant ces paramètres.
					 </li> 
					 <li><code><strong>this.utils</strong></code> : 
					 	 <span> Objet contenant différentes fonctions utilitaires et sous objets.</span>
					 	  <ul>
						 	  <li><code><strong>this.utils</strong></code> :  voir le javaScript globazNotation.utils</li>
						 	  <li><code><strong>this.utils.input</strong></code> :  voir le javaScript globazNotation.utilsInput</li>
						 	  <li><code><strong>this.utils.formatter</strong></code> :  voir le javaScript globazNotation.utilsFormatter</li>
						 	  <li><code><strong>this.utils.date</strong></code> :  voir le javaScript globazNotation.date</li>
					 	  </ul>
					 </li>
				 </ul>
		    </div>
		    
			<h1 id="creationJs" class="explication">Création d'un objet de notation</h1>
				<div class="description">
				 <p>
				 	Il est possible d'ajouter des messages d'erreur en utilisant cette fonction: <code><strong>this.putFormattedError( s_typeError, s_elementInError, s_messageHtml )</strong></code>
				 </p>
				<pre  id="creationJsNotation" class="brush: js; toolbar: false">
					/**
					* le nom du nouvelle objet(json) doit être écrit en MINUSCUL.
					* Nous allons ajouter un nouvelle objet "nouvellenotation" à notre espace de nom "globazNotation"
					* Toutes les fonctions et attributs décrit ci-dessous sont obligatoires...
					*/
					globazNotation.nouvellenotation = {
					    /******************************DOCUMENTATION***************************************/
					   author: "Visa du créateur",
					   
					   forTagHtml: "On définit sur quelle element html on peut utiliser notre objet.\n\
					                Séparé par des virgules (table,div,span,...).On peut utiliser * pour définir tous les éléments\n\
					                On peut aussi exclure des éléments avec ! (*,!input,!h1)",
					    
					   description: "C'est ici que l'on décrit notre nouvelle objet on peut utiliser des tags htmls pour la mise en page",
					 
					   descriptionOptions: {
					                option1: {
					                     desc: "définit le paramétre option1",
					                    param: "C'est ici que l'on définit les diférentes valeurs possible pour notre option1 (true|false(default)|...)",
										 type: String,
					                mandatory: "Ce paramètre n'est pas obligatoire. Définit si l'option est obligatoire. Valeur possible ture, false."
					                },
					                option2: {
					                     desc: "...",
					                    param: "..."
					                }
					            },
					              
					    /******************************UTILISATION*****************************************/
					      
					    /**
					     * Paramètre de l'objet qui vont être utilisé pour son initialisation.
					     * Cet élément est obligatoire.
					     * C'est dans ces options que l'on peut mettre les paramétres par défauts.
					     * L'appel de l'objet dans le html se fera de cette manière :
					     *   globaz:nouvellenotation="option1:valeur,option2:valeur" ou  globaz:nouvellenotation="option2:valeur"
					     * S'il n'y pas d'option pour l'objet il faut quand même le créer, mais vide (options:{})
					     *
					     *IMPORTANT: Toutes options booelan doivent avoir une valeur par défaut.
						 *           Si il n'y a pa de valeur par défaut il est préférable de définir sont type ('',[]'{})
					     */
					    options: {
					             s_option1:'valeurParDefaut',
					             t_option2:[],
					             b_boolean:true
					    },
					    
					    /**
					    *C'est dans cette map qu'il faut déclarer les variables que l'on veut utiliser dans la notation.
					    *Si possible eviter d'avoir trop de variables. Le mieux c'est de ne pas en avoir.
					    *Utilisation des variables dans le code : this.vars.maVaraible;
					    */
					    vars: {
					    
					    },
					    
					    /**
						 * Ce paramètre est facultatif.<br/>
						 * Il permet des lancer des fonctions sur différent types d'évenements.<br/>
						 * Liste des événements :<br/>
						 * 	<ul>
						 * 		<li>boutons standard de l'application. Les événements se lancent sur le clique du bouton</li>
						 * 		<ul>
						 * 			<li>btnCancel</li>
						 * 			<li>btnAdd</li>
						 * 			<li>btnValidate</li>
						 * 			<li>btnUpdate</li>
						 * 			<li>btnDelete</li>
						 * 		</ul>
						 * 		<li>AJAX: toutes ces fonctions se lancent à la fin de la fonction dans AJAX</li>
						 * 		<ul>
						 * 			<li>ajaxShowDetailRefresh</li>
						 * 			<li>ajaxLoadData</li>
						 * 			<li>ajaxShowDetail</li>
						 * 			<li>ajaxStopEdition</li>
						 * 			<li>ajaxValidateEditon</li>
						 * 			<li>ajaxUpdateComplete</li>
						 * 		</ul>
						 * 	</ul>
						 */
					    bindEvent: {
						    	btnAdd:  function(){this.onClickBtAdd()},
					      		btnValidate:  function(){this.maSuperFonction()}
					   	},
					      
					    /**
					     * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet.
					     * $elementToPutObject : Correspond à un objet Jquery qui contient le noeud ou se trouve notre notation
					     * 
					     */
					    init: function($elementToPutObject){
					    },
					        
						maSuperFonction : function (param1, param2) {
						}
					}
				</pre>
			</div>
			
			<h1 id="integrationJS" class="explication">Intégration d'un objet dans le moteurs</h1>
			<div id="integrationJS2" class="description">
				<p>
					Pour que le moteur des notations détécte la nouvelle notation il suffit d'inculre le script de la notation dans votre page!'
				</p>
				<p>Pour inclure la notation de manière permanante il faut ajoute l'apelle du scritp dans le fichier notationLibJs.jspf
				<p>Pour tester la notation il faut editer le fichier fragementDemo.jspf</p>
				<p>Css: TODO</p>
			</div>
			    
			<h1 id="jsManager" class="explication">JavaScript (initialisation) dans les pages html</h1>
		    <div id="jsManager2" class="description">
		    	<p>Toute initialisation de JavaScript dans les pages html devrait passer par l'objet jsManager.<br />
		    		Cette objet nous permet d'avoir un meilleur contrôle de l'ordre d'exécution de l'initialisation du javascript.<br />
		    		En utilisant cette objet, il n' y plus besion d'utiliser la fonction jQuery du dom ready 
		    	</p>
		    	<div class="docExemple"> $(function(){js.int}) // replacé par jsManager.add(...)  </div>
		    	
		    	<h2>L'objet jsManager posède trois fonctions avec la même signature</h2>
		    	
		    	<ul>
		    		<li><code><strong>addAfter( f_function, s_message )</strong></code> : Execute la fonction passée en paramètre après l'initialisation des notations</li>
		    		<li><code><strong>addBefore( f_function, s_message )</strong></code> :Execute la fonction passée en paramètre avant l'initialisation des notations</li>
		    		<li><code><strong>add( f_function, s_message )</strong></code> : Identique à la fonction addBefore</li>
		    	</ul>
		 		
		 		<h2>Exemple:</h2> 
		 		
		    	<pre class="brush: js toolbar: false">
					jsManager.add( function () {
							notationDocumentation.createDocumentation()
						},"Create the documentation");		
				</pre>
		    </div>
			
		    <h1 id="event" class="explication">CustomEvents</h1>
		    <div id="explicatiEvent" class="description">
		    	<p>Pour avoir un couplage faible du JavaScript et des objets de la notation, des custom events ont été ajoutés.<br />
		    		Tous les custom events ont été définis en constante dans l'objet <code><strong>eventConstant</strong></code>
				</p>
				<h3>Auto-complétion (widget)</h3>
		    	<ul>
			    	<li><span class="nameCustomEvent">AJAX_SELECT_SUGGESTION: </span> 
			    	    <span class="descCustomEvent">Cet événement se déclenche quand on sélectionne le résultat dans l'auto-complétion.</span> 
			    	    <span class="bindCustomEvent">Lié à l'input de l'auto-complétion</span> 
			    	</li>
			    </ul>
			    <h3>Ajax</h3>
			    <ul>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_STOP_EDITION:</span> 
			    		<span class="descCustomEvent">Se déclenche à la fin de l'appel de la fonction <code><strong>stopEdition</strong></code></span>
						<span class="bindCustomEvent">Lié au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li> 
			    	<li>
			    		<span class="nameCustomEvent">AJAX_LOAD_DATA</span> 
			    		<span class="descCustomEvent">Se déclenche après le chargements des données (Détails) </span>
						<span class="bindCustomEvent">Lié au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_DETAIIL_REFRESH:</span> 
			    		<span class="descCustomEvent">Se déclenche lorqu'un détail est mis à jour et aussi sur les bouttons "nouveau" et "cancel"</span>
						<span class="bindCustomEvent">Lié au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_STOP_SHOW_DETAIL</span> 
			    		<span class="descCustomEvent">Se déclenche après l'affichage du détail </span>
						<span class="bindCustomEvent">Lié au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_INIT_DONE</span> 
			    		<span class="descCustomEvent">Se déclenche après l'initilisation de l'ajax </span>
						<span class="bindCustomEvent">Lié au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_VALIDATE_EDITION</span> 
			    		<span class="descCustomEvent">Se déclenche après la validation (boutton) </span>
						<span class="bindCustomEvent">Lié au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_UPDATE_COMPLETE</span> 
			    		<span class="descCustomEvent">Se déclenche après la mise à jour des composants ajax</span>
						<span class="bindCustomEvent">Lié au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li> 
			    </ul>
			    <h3>Moteur notation</h3>
			    <ul>
			    	<li>
			    		<span class="nameCustomEvent">NOTATION_MANAGER_DONE::</span> 
			    		<span class="descCustomEvent">Se déclenche à la fin de l'exécution du moteur des notations</span>
						<span class="bindCustomEvent">Lié au tag <code><strong>&lt;html&gt;</strong></code></span>
			    	</li>
			    </ul>
		    </div>
		    
		</div>
	</div>
</body>

</html>