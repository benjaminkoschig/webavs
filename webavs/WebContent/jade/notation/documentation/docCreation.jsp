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
		    <h1 id="prerqui" class="explication">Pr�s requis</h1>
		    <div id="pre" class="description">
		    	<ul>
			    	<li>Il ne faut jamais utiliser le nom <strong>"globazNotation"</strong> pour la cr�ation de variables javascript!!</li>
			    	<li>Il est important que tous les fichiers javascript qui d�finissent les notations se trouvent dans le fichier notation</li>
			    	<li>Pour l'inclusion du fichier javascript, utilisez le fichier <strong>notationLibJs.jspf</strong></li>
			    	<li><h2>Convention de nommage:</h2> 
			    		<ul class="liWithStyle">
				    		<li><code><strong>$xyz</strong></code> : Objet jquery</li>
				    		<li><code><strong>o_xyz</strong></code> : contient un objet(g�n�rique ou un �l�ment DOM) </li> 
				    		<li><code><strong>b_xyz</strong></code> : contient un boolean</li>
				    		<li><code><strong>d_xyz</strong></code> : contient une date</li>
				    		<li><code><strong>s_xyz</strong></code> : contient un string</li>
				    		<li><code><strong>n_xyz</strong></code> : contient un num�rique</li>
				    		<li><code><strong>t_xyz</strong></code> : contient un tableau</li>
				    		<li><code><strong>f_xyz</strong></code> : contient une fonction</li>
				    		<li><code><strong>_nomDeFonction</strong></code> : indique que l'on a une fonction priv�e</li>
				    		<li><code><strong>_nomAttribut</strong></code> : indique que l'on a un attribut priv�</li>
				    	</ul>
			    	</li>
			    </ul>
		    </div>
		    <h1 id="utilisation" class="explication">Utilisation des notations</h1>
		    <div id="utisationHtml" class="description">  
		    	<div>	
		          Pour utiliser une notation, il suffit de l'int�grer de la mani�re suivante : <br />
		      	  <div class="docExemple"> 
		      	  	"data-g-notation='param1:1,parm2:2'"
		      	  </div>
		      	  On place la notation sur l'�l�ment html qui doit avoir le comportement d�sir�. <br />
		      	  <strong>Toutes les options qui ne sont pas du type string doivent �tre d�finies avec le symbole "�" </strong> 
		      	   <div class="docExemple">opt1:<strong>�</strong>valeur1,valeur2<strong>�</strong>
		      	  </div>
		      	   
		      	   Il est possible d'utiliser des structures conditionnelles dans la notation pour les options boolean <br />
		      	   Si une notation n'a pas besoin de param�tre, il faut mettre un  <strong>espace entre les guillemets</strong><br />
		      	   Exemple: 
		      	 </div>
		      	 
				 <pre class="brush: html; toolbar: false">
				   //par d�faut (avec un espace)
				  &lt; input type="text"  value="" data-g-integer=" " /&gt;
				   //avec parametre
				  &lt; input type="text"  value="" data-g-integer="sizeMax:3" /&gt;
				  // Structures conditionnelles
				  &lt; input type="text"  value="" data-g-amount="mandatory:true==true&&1&gt;3" /&gt;
				  //Avec plusieur valeurs
				  &lt; input type="text"  value="" data-g-test="mandatory:true,params:�valeu1,value2,�,params3:�[1,2,3,'string']�" /&gt;
				 </pre>
				 <div> 
				 	<h2>Utilisation des notations dans les cutomTags </h2>
				 	<div>
				 		Un attribut "notation" a �t� ajout� aux customTags susceptibles d'avoir une notation. 
				 	</div>
				 	<code><div class="docExemple">
				 	  &lt;customTag  <strong>notation="data-g-select='madandatory:true'"</strong> customTag /&gt; 
				 	</div></code>
				 </div>
				 <h2>Il est possible de conna�tre le temps d'ex�cution de chaque objet.</h2>
				 <ul class="liWithStyle">
					<li>En ajoutant cette attribut sur n'import quel tag html : <strong><code>showInfos="false"</code></strong>
					</li>
					<li>En mettant la cha�ne suivante
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
			    		Il est possible d'internationaliser un texte utilis� dans une notation.<br/>
			    		La structure utilis�e est la m�me que les .properties de java.<br/>
			    		La cl� pour les textes doit �tre compos�e de la mani�re suivante pour les objets de la notation.<br/>
			    		On met le nom "notation", le nom de l'objet o� se trouve la traduction, puis un identifiant pour le texte.
					</p>
					 <div class="docExemple">
					     notation.nomObjet.identifiantText <br/>
					     notation.amount.pasUnNombre
					  </div>
					  <p>
					  	Pour internationaliser un texte dans un objet de la notation il faut utiliser la fonction <strong><code>i18n(s_key)</code></strong><br/>
						<strong>ATTENTION:</strong> la cl� � utiliser ne doit pas �tre pr�c�d�e de "notation.nomObjet", il faut utilsier l'identifiant du texte
					  </p>
					  <div class="docExemple">
					    properties => notation.nomObjet.hello = hello <br/>
						js => this.i18n('hello');
					  </div>
					<p>
						Il est possible d'utiliser des variables dans un texte � traduir.
					</p>
					  <div class="docExemple">
					    properties => notation.nomObjet.bonneJournee = bonne journ�e {0} !<br/>
						js => this.i18n('bonneJournee',['jean']);
					  </div>
		      	</div>
			</div>
		    <h1 id="GestionErreur"" class="explication">Gestion des erreurs</h1>
		   	<div class="description">  
		    	<div>	
			    	<p>Le moteur des notations g�re une partie des erreurs de syntaxe li�e � la notation, 
			    	ainsi que les types de donn�es qui sont utilis�s dans les options des objets et aussi les erreurs JavaScripts.
					</p>
					<p>Par contre il ne g�re pas les erreurs qui peuvent arriver dans les objets des notations. <br />
					 C'est pour cette raison que le l'on peut utiliser <code><strong>this.putError( s_messageHtml )</strong></code><br />
					 Cette fonction ajoutera les erreurs au moteur des notations;
					 On peut aussi utiliser la m�thode <code><strong>this.putFormattedError( s_typeError, s_elementInError, s_messageHtml )</strong></code> qui formate automatiquement les erreurs.
					</p>
					<p><strong>Dans un souci d'uniformisation, si vous utilisez <code><strong>this.putError( s_messageHtml)</strong></code> <br /> 
					 	merci d'utiliser le canevas suivant pour g�rer les erreurs dans les notations.</strong></p>
					<p>Dans les fichiers utilitaires il faut lancer des exceptions et ne pas utiliser les fonctions du moteur. <br />
					   Si une exception est soulev�e dans un fichier utilitaire lors de l'initialisation des notations, 
					   le moteur de notation l'interceptera dans une clause "try catch". Sinon c'est l'objet de la notations qui doit g�rer ces exceptions</p>
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
					 <li><code><strong>this.$elementToPutObject</strong></code> : Objet jquery o� se trouve la notation</li>
					 <li><code><strong>this.i18n( s_key )</strong></code> : Permet de traduire un texte dans les objets notation</li>
					 <li><code><strong>this.putError( s_message )</strong></code> : Permet d'ajouter des messages d'erreur li�s � l'objet message non format�. <br />
					     Merci d'utiliser le canevas. (voir Gestion des erreurs)
					 </li>
					 <li><code><strong>this.putFormattedError( s_typeError, s_elementInError, s_messageHtml )</strong></code> : Message d'erreur format�</li>
					 <li><code><strong>this.createParams( s_chaineDeParametre )</strong></code> : 
					 <br/>Parcours la cha�ne de caract�res qui doit �tre dans le m�me format que les param�tres de la notation ("param1:true,params2:toto"). 
					 <br/>Retourne un objet(JSON) contenant ces param�tres.
					 </li> 
					 <li><code><strong>this.utils</strong></code> : 
					 	 <span> Objet contenant diff�rentes fonctions utilitaires et sous objets.</span>
					 	  <ul>
						 	  <li><code><strong>this.utils</strong></code> :  voir le javaScript globazNotation.utils</li>
						 	  <li><code><strong>this.utils.input</strong></code> :  voir le javaScript globazNotation.utilsInput</li>
						 	  <li><code><strong>this.utils.formatter</strong></code> :  voir le javaScript globazNotation.utilsFormatter</li>
						 	  <li><code><strong>this.utils.date</strong></code> :  voir le javaScript globazNotation.date</li>
					 	  </ul>
					 </li>
				 </ul>
		    </div>
		    
			<h1 id="creationJs" class="explication">Cr�ation d'un objet de notation</h1>
				<div class="description">
				 <p>
				 	Il est possible d'ajouter des messages d'erreur en utilisant cette fonction: <code><strong>this.putFormattedError( s_typeError, s_elementInError, s_messageHtml )</strong></code>
				 </p>
				<pre  id="creationJsNotation" class="brush: js; toolbar: false">
					/**
					* le nom du nouvelle objet(json) doit �tre �crit en MINUSCUL.
					* Nous allons ajouter un nouvelle objet "nouvellenotation" � notre espace de nom "globazNotation"
					* Toutes les fonctions et attributs d�crit ci-dessous sont obligatoires...
					*/
					globazNotation.nouvellenotation = {
					    /******************************DOCUMENTATION***************************************/
					   author: "Visa du cr�ateur",
					   
					   forTagHtml: "On d�finit sur quelle element html on peut utiliser notre objet.\n\
					                S�par� par des virgules (table,div,span,...).On peut utiliser * pour d�finir tous les �l�ments\n\
					                On peut aussi exclure des �l�ments avec ! (*,!input,!h1)",
					    
					   description: "C'est ici que l'on d�crit notre nouvelle objet on peut utiliser des tags htmls pour la mise en page",
					 
					   descriptionOptions: {
					                option1: {
					                     desc: "d�finit le param�tre option1",
					                    param: "C'est ici que l'on d�finit les dif�rentes valeurs possible pour notre option1 (true|false(default)|...)",
										 type: String,
					                mandatory: "Ce param�tre n'est pas obligatoire. D�finit si l'option est obligatoire. Valeur possible ture, false."
					                },
					                option2: {
					                     desc: "...",
					                    param: "..."
					                }
					            },
					              
					    /******************************UTILISATION*****************************************/
					      
					    /**
					     * Param�tre de l'objet qui vont �tre utilis� pour son initialisation.
					     * Cet �l�ment est obligatoire.
					     * C'est dans ces options que l'on peut mettre les param�tres par d�fauts.
					     * L'appel de l'objet dans le html se fera de cette mani�re :
					     *   globaz:nouvellenotation="option1:valeur,option2:valeur" ou  globaz:nouvellenotation="option2:valeur"
					     * S'il n'y pas d'option pour l'objet il faut quand m�me le cr�er, mais vide (options:{})
					     *
					     *IMPORTANT: Toutes options booelan doivent avoir une valeur par d�faut.
						 *           Si il n'y a pa de valeur par d�faut il est pr�f�rable de d�finir sont type ('',[]'{})
					     */
					    options: {
					             s_option1:'valeurParDefaut',
					             t_option2:[],
					             b_boolean:true
					    },
					    
					    /**
					    *C'est dans cette map qu'il faut d�clarer les variables que l'on veut utiliser dans la notation.
					    *Si possible eviter d'avoir trop de variables. Le mieux c'est de ne pas en avoir.
					    *Utilisation des variables dans le code : this.vars.maVaraible;
					    */
					    vars: {
					    
					    },
					    
					    /**
						 * Ce param�tre est facultatif.<br/>
						 * Il permet des lancer des fonctions sur diff�rent types d'�venements.<br/>
						 * Liste des �v�nements :<br/>
						 * 	<ul>
						 * 		<li>boutons standard de l'application. Les �v�nements se lancent sur le clique du bouton</li>
						 * 		<ul>
						 * 			<li>btnCancel</li>
						 * 			<li>btnAdd</li>
						 * 			<li>btnValidate</li>
						 * 			<li>btnUpdate</li>
						 * 			<li>btnDelete</li>
						 * 		</ul>
						 * 		<li>AJAX: toutes ces fonctions se lancent � la fin de la fonction dans AJAX</li>
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
					     * $elementToPutObject : Correspond � un objet Jquery qui contient le noeud ou se trouve notre notation
					     * 
					     */
					    init: function($elementToPutObject){
					    },
					        
						maSuperFonction : function (param1, param2) {
						}
					}
				</pre>
			</div>
			
			<h1 id="integrationJS" class="explication">Int�gration d'un objet dans le moteurs</h1>
			<div id="integrationJS2" class="description">
				<p>
					Pour que le moteur des notations d�t�cte la nouvelle notation il suffit d'inculre le script de la notation dans votre page!'
				</p>
				<p>Pour inclure la notation de mani�re permanante il faut ajoute l'apelle du scritp dans le fichier notationLibJs.jspf
				<p>Pour tester la notation il faut editer le fichier fragementDemo.jspf</p>
				<p>Css: TODO</p>
			</div>
			    
			<h1 id="jsManager" class="explication">JavaScript (initialisation) dans les pages html</h1>
		    <div id="jsManager2" class="description">
		    	<p>Toute initialisation de JavaScript dans les pages html devrait passer par l'objet jsManager.<br />
		    		Cette objet nous permet d'avoir un meilleur contr�le de l'ordre d'ex�cution de l'initialisation du javascript.<br />
		    		En utilisant cette objet, il n' y plus besion d'utiliser la fonction jQuery du dom ready 
		    	</p>
		    	<div class="docExemple"> $(function(){js.int}) // replac� par jsManager.add(...)  </div>
		    	
		    	<h2>L'objet jsManager pos�de trois fonctions avec la m�me signature</h2>
		    	
		    	<ul>
		    		<li><code><strong>addAfter( f_function, s_message )</strong></code> : Execute la fonction pass�e en param�tre apr�s l'initialisation des notations</li>
		    		<li><code><strong>addBefore( f_function, s_message )</strong></code> :Execute la fonction pass�e en param�tre avant l'initialisation des notations</li>
		    		<li><code><strong>add( f_function, s_message )</strong></code> : Identique � la fonction addBefore</li>
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
		    	<p>Pour avoir un couplage faible du JavaScript et des objets de la notation, des custom events ont �t� ajout�s.<br />
		    		Tous les custom events ont �t� d�finis en constante dans l'objet <code><strong>eventConstant</strong></code>
				</p>
				<h3>Auto-compl�tion (widget)</h3>
		    	<ul>
			    	<li><span class="nameCustomEvent">AJAX_SELECT_SUGGESTION: </span> 
			    	    <span class="descCustomEvent">Cet �v�nement se d�clenche quand on s�lectionne le r�sultat dans l'auto-compl�tion.</span> 
			    	    <span class="bindCustomEvent">Li� � l'input de l'auto-compl�tion</span> 
			    	</li>
			    </ul>
			    <h3>Ajax</h3>
			    <ul>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_STOP_EDITION:</span> 
			    		<span class="descCustomEvent">Se d�clenche � la fin de l'appel de la fonction <code><strong>stopEdition</strong></code></span>
						<span class="bindCustomEvent">Li� au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li> 
			    	<li>
			    		<span class="nameCustomEvent">AJAX_LOAD_DATA</span> 
			    		<span class="descCustomEvent">Se d�clenche apr�s le chargements des donn�es (D�tails) </span>
						<span class="bindCustomEvent">Li� au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_DETAIIL_REFRESH:</span> 
			    		<span class="descCustomEvent">Se d�clenche lorqu'un d�tail est mis � jour et aussi sur les bouttons "nouveau" et "cancel"</span>
						<span class="bindCustomEvent">Li� au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_STOP_SHOW_DETAIL</span> 
			    		<span class="descCustomEvent">Se d�clenche apr�s l'affichage du d�tail </span>
						<span class="bindCustomEvent">Li� au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_INIT_DONE</span> 
			    		<span class="descCustomEvent">Se d�clenche apr�s l'initilisation de l'ajax </span>
						<span class="bindCustomEvent">Li� au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_VALIDATE_EDITION</span> 
			    		<span class="descCustomEvent">Se d�clenche apr�s la validation (boutton) </span>
						<span class="bindCustomEvent">Li� au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li>
			    	<li>
			    		<span class="nameCustomEvent">AJAX_UPDATE_COMPLETE</span> 
			    		<span class="descCustomEvent">Se d�clenche apr�s la mise � jour des composants ajax</span>
						<span class="bindCustomEvent">Li� au conteneur principal (<code><strong>mainContainer</strong></code>)</span>
			    	</li> 
			    </ul>
			    <h3>Moteur notation</h3>
			    <ul>
			    	<li>
			    		<span class="nameCustomEvent">NOTATION_MANAGER_DONE::</span> 
			    		<span class="descCustomEvent">Se d�clenche � la fin de l'ex�cution du moteur des notations</span>
						<span class="bindCustomEvent">Li� au tag <code><strong>&lt;html&gt;</strong></code></span>
			    	</li>
			    </ul>
		    </div>
		    
		</div>
	</div>
</body>

</html>