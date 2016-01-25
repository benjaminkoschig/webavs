<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<!--<jsp:useBean id="viewBean" class="globaz.pegasus.vb.variablemetier.PCVariableMetierViewBean" scope="request" /> -->

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%><html>
<head>
<style type="text/css">
body{
font-size: 12px;
}
h2, h1{
font-size: 1em;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Jstl</title>
</head>
<body>
	<ul>
		<li>
			<h2> ForEach </h2>
			<c:forEach var="i" begin="1" end="5" step="1">
				Bonjour forEach ${i} <br />
			</c:forEach>
			
		</li>
		<li>
			<h2> Création variable </h2>
			<!-- scope: (page, request, session ou application) (défaut : page)-->
			<c:set var="nameVariable" value="La super valeur de ma variable (& / > < ö ä é ü è é)" />
				Valeur de ma variable ${nameVariable} <br />
				
				<c:set var="nameVariable" value="La super valeur de ma variable (& / > < ö ä é ü è é)" />
				Valeur de ma variable ${nameVariable} <br />
		</li>
		<li>
			<h2> Utilisation Out (encode les carctpres XML résérvés) </h2>
			<c:out value="${nameVariable}"></c:out>
		</li>
		<li>
			<h2> Superssion de la varibale et utilisation de valuer par défaut </h2>
			Avant supression : <br />
			<c:out value="${nameVariable}" />
			Supression : <br />
			<c:remove var="nameVariable" />
			<c:out value="${nameVariable}" default="nameVariable absent"></c:out>;
		</li>
		<li>
			<h2> Récupération de paramétre  (test1) </h2>
				${param["test1"]}
		</li>
	</ul>
	<h1> FMT </h1>
	
	<ul>
		<li>
			<h2>Format date </h2>
				<fmt:setLocale value="fr_CH"/>
				<fmt:parseDate value="20100920" pattern="yyyymmdd" var="date"/><br/>
				<fmt:formatDate value="${date}" dateStyle="full"/>
		</li>
		<li>
			<h2>Local avec la date </h2>
				<fmt:setLocale value="de_CH" />
				DE suisse: <fmt:formatDate value="${date}" dateStyle="full"/>  <br />
				<fmt:setLocale value="fr_CH" />
				FR suisse: <fmt:formatDate value="${date}" dateStyle="full"/>  <br />
				<fmt:setLocale value="it_CH" />
				IT suisse: <fmt:formatDate value="${date}" dateStyle="full"/>  <br />
				<fmt:setLocale value="en_US" />
				USA: <fmt:formatDate value="${date}" dateStyle="full"/>
		</li>
		<li>
			<h2>Devise</h2>
				<fmt:setLocale value="fr_CH" />
				<fmt:formatNumber value="15000000.26" type="currency" currencySymbol=""/>
		</li>
	</ul>
	<h1>Les objets implicites </h1> 
	
	    <!--  
	    http://www.oracle.com/technetwork/java/index-jsp-135995.html
	    http://adiguba.developpez.com/tutoriels/j2ee/jsp/jstl/
	    http://adiguba.developpez.com/tutoriels/j2ee/jsp/taglib/
	    http://adiguba.developpez.com/tutoriels/j2ee/jsp/el/#L1.6
	    http://fr.wikipedia.org/wiki/JavaServer_Pages
	    install
	    http://www.objis.com/formation-java/tutoriel-jstl-installation-jakarta-taglib.html
	    http://www.developer.com/java/ejb/article.php/1447551/An-Introduction-to-JSP-Standard-Template-Library-JSTL.htm
		
		EL
		http://download.oracle.com/javaee/5/tutorial/doc/bnahq.html#bnaij
		
		
		
		
    *  pageContext : Accès à l'objet PageContext de la page JSP.
    * pageScope : Map permettant d'accéder aux différents attributs du scope 'page'.
    * requestScope : Map permettant d'accéder aux différents attributs du scope 'request'.
    * sessionScope : Map permettant d'accéder aux différents attributs du scope 'session'.
    * applicationScope : Map permettant d'accéder aux différents attributs du scope 'application'.
    * param : Map permettant d'accéder aux paramètres de la requête HTTP sous forme de String.
    * paramValues : Map permettant d'accéder aux paramètres de la requête HTTP sous forme de tableau de String.
    * header : Map permettant d'accéder aux valeurs du Header HTTP sous forme de String.
    * headerValues : Map permettant d'accéder aux valeurs du Header HTTP sous forme de tableau de String.
    * cookie : Map permettant d'accéder aux différents Cookies.
    * initParam : Map permettant d'accéder aux init-params du web.xml.
		
-->
	
	<ul>
		<li>
			<h2>pageContext.response.contentType </h2>
			${pageContext.response.contentType}
		</li>
		<li>
			<h2>header </h2>
			${headerValues}
		</li>
		<li>
			<h2>headerValues </h2>
			${header["user-agent"]}
		</li>
		<li>
			<h2>cookie </h2>
			${cookie}
		</li>
		<li>
			<h2>initParam </h2>
			${initParam}
		</li>
		<li>
			<h2>param </h2>
			${param["test1"]}
		</li>
		<li>
			<h2>paramValues </h2>
			${paramValues["test1"]}
		</li>
	</ul>
	
	<h1>Test autocompete </h1>
	
	<input type="hidden" class="employeur" />	
	<ct:widget name='employeurWidget' id='employeurWidget' styleClass="libelleLong selecteurEmployeur">
		<ct:widgetService methodName="findAdresse" className="<%=PersonneEtendueService.class.getName()%>">																												
			<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_PARAM_HOMES_W_TIERS_NPA"/>
			<ct:widgetCriteria criteria="forLocaliteLike" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>								
			<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{localite.numPostal} #{localite.localite})"/>
			<ct:widgetJSReturnFunction>
				<script type="text/javascript">
				function(element){												
					$(this).parents('.areaMembre').find('.employeur').val($(element).attr('tiers.id'));
					this.value=$(element).attr('tiers.designation1');
				}
				</script>										
			</ct:widgetJSReturnFunction>
		</ct:widgetService>
	</ct:widget>	
	
	
</body>
</html>