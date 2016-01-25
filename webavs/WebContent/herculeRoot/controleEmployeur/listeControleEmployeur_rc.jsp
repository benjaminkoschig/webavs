<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	rememberSearchCriterias = true;
	idEcran ="CCE0003";
	actionNew =  servletContext + mainServletPath + "?userAction=hercule.controleEmployeur.controleEmployeur.afficher&_method=add";
	String likeNumAffilie = request.getParameter("likeNumAffilie");
	
	//Ce qui suitpermet la gestion de l'écran de recherche car on vient de draco
	String _fromDraco = request.getParameter("_fromDraco");	
	if (!JadeStringUtil.isBlank(_fromDraco))  { 
		bButtonNew = false;
	}else {
		_fromDraco = "";
	}
	 
	String idAff = request.getParameter("_idAff");	
	if (!JadeStringUtil.isBlank(idAff))  { 
		likeNumAffilie = idAff;
	}
	if (!globaz.jade.client.util.JadeStringUtil.isEmpty((String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX))){
		likeNumAffilie = (String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX);
	}
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<SCRIPT language="JavaScript">
	usrAction = "hercule.controleEmployeur.listeControleEmployeur.lister";
	top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
	
	<%if (!JadeStringUtil.isBlank(_fromDraco) || !JadeStringUtil.isBlank(likeNumAffilie)) {%> 
		bFind = true;
	<%}%>
	
	
</SCRIPT>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>

<%if (JadeStringUtil.isBlank(_fromDraco))  {%>
<ct:menuChange displayId="menu" menuId="CE-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CE-OptionsDefaut"/>
<%}%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_CE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>	
	<%if (!JadeStringUtil.isBlank(_fromDraco))  { %>
		<INPUT type="hidden" name="_fromDraco" value="<%=request.getParameter("_fromDraco")%>"/>
	<%}%>

	<TR>
		<TD nowrap width="100"><ct:FWLabel key="NUMERO_AFFILIE" /></TD>
		<TD nowrap width="200"><INPUT name="likeNumAffilie" type="text" size="17" maxlength="15" value="<%=!JadeStringUtil.isEmpty(likeNumAffilie) ? likeNumAffilie : ""%>"/></TD>
		<TD nowrap width="140"><ct:FWLabel key="ANNEE"/></TD>
		<TD nowrap width="150"><INPUT name="forAnnee" type="text" size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);"/></TD>
		<TD nowrap width="140"><ct:FWLabel key="TRIER_PAR"/></TD>
		<TD nowrap width="150">
			<select name="orderBy" size="1">
  				<option value="cont.malnaf asc" selected="selected"><ct:FWLabel key="AFFILIE"/>&nbsp;<ct:FWLabel key="CROISSANT"/></option>
  				<option value="cont.malnaf desc"><ct:FWLabel key="AFFILIE"/>&nbsp;<ct:FWLabel key="DECROISSANT"/></option>
  				<option value="cont.mddpre asc"><ct:FWLabel key="DATE_PREVUE"/>&nbsp;<ct:FWLabel key="CROISSANT"/></option>
  				<option value="cont.mddpre desc"><ct:FWLabel key="DATE_PREVUE"/>&nbsp;<ct:FWLabel key="DECROISSANT"/></option>
  				<option value="cont.mddeff asc"><ct:FWLabel key="DATE_EFFECTIVE"/>&nbsp;<ct:FWLabel key="CROISSANT"/></option>
  				<option value="cont.mddeff desc"><ct:FWLabel key="DATE_EFFECTIVE"/>&nbsp;<ct:FWLabel key="DECROISSANT"/></option>
			</select> 
		</TD>
	</TR>
	<TR>
		<TD nowrap width="100" height="31"><ct:FWLabel key="REVISEUR"/></TD>
		<TD nowrap width="200"><INPUT name="likeVisaReviseur" type="text" size="10" /></TD>
		<TD nowrap width="140" height="31"><ct:FWLabel key="NUMERO_RAPPORT"/></TD>
		<TD nowrap width="150"><INPUT name="likeNouveauNumRapport" type="text" size="12" /></TD>
		<TD nowrap width="140" height="31"><ct:FWLabel key="GENRE_CONTROLE"/></TD>
		<TD nowrap width="150"><ct:FWCodeSelectTag name="forGenreControle" codeType="VEGENRECON" defaut="" wantBlank="false"/></TD>
		<script type="text/javascript"> 
			$("#forGenreControle").append('<option value="" selected="selected"><ct:FWLabel key="TOUS"/></option>');
		</script>
	</TR>
		<TD nowrap width="100" height="31"><ct:FWLabel key="NOM_GROUPE"/></TD>
		<TD nowrap width="200"><INPUT name="likeLibelleGroupe" type="text" size="10" /></TD>
        <TD nowrap width="100"><ct:FWLabel key="ONLY_ACTIVE_CONTROLE"/></TD>
        <TD nowrap><INPUT type="checkbox" name="forActif" checked="checked"/></TD>
        <TD nowrap width="100"><ct:FWLabel key="ONLY_SANS_ATTRIBUTION"/></TD>
        <TD nowrap><INPUT type="checkbox" name="forSansAttributionPts"/></TD>
	<TR>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>