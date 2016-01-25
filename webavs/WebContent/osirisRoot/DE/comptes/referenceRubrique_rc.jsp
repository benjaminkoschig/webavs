<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>

<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.osiris.db.comptes.CARubriqueViewBean"%>

<%-- tpl:put name="zoneInit" --%>

<%
	idEcran = "GCA0060"; 
	IFrameDetailHeight="250";

	CARubriqueViewBean viewBean = (CARubriqueViewBean)session.getAttribute ("viewBeanRC");
	actionNew = actionNew + "&idRubrique=" + viewBean.getIdRubrique();
	
	FWController controller = (FWController) session.getAttribute("objController");
	BSession objSession = (BSession)controller.getSession();
	String userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
	bButtonNew = objSession.hasRight(userActionNew, globaz.framework.secure.FWSecureConstants.ADD);

%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "osiris.comptes.referenceRubrique.lister";
	detailLink = servlet+"?userAction=osiris.comptes.referenceRubrique.afficher&_s_push=no&_method=add&idRubrique=<%=viewBean.getIdRubrique()%>";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA0060_TITRE_ECRAN_REFERENCE_RUBRIQUE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

      <TR>
        <TD nowrap width="90"><ct:FWLabel key="GCA0060_NUMERO"/></TD>
        <TD nowrap colspan="3">
          <input type="text" name="" value="<%=viewBean.getIdExterne()%>" tabindex="-1" class="libelleLongDisabled" readonly="readonly" />
          <input type="hidden" name="forIdRubrique" value="<%=viewBean.getIdRubrique()%>" />
          <input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>" />
        </TD>
      </TR>
      <TR>
        <TD nowrap width="90"><ct:FWLabel key="GCA0060_DESCRIPTION"/></TD>
        <TD nowrap colspan="3">
          <input type="text" name="" value="<%=viewBean.getDescription()%>" tabindex="-1" class="libelleLongDisabled" readonly="readonly" />
        </TD>
      </TR>
      <TR>
        <TD nowrap width="90"><ct:FWLabel key="GCA0060_NATURE"/></TD>
        <TD nowrap colspan="3">
          <input type="text" name="" value="<%=viewBean.getCsNatureRubrique().getLibelle()%>" tabindex="-1" class="libelleLongDisabled" readonly="readonly" />
        </TD>
      </TR>
      <TR>
        <TD nowrap width="90"><ct:FWLabel key="GCA0060_CONTREPARTIE"/></TD>
        <TD nowrap colspan="3">
          <input type="text" name="" value="<%if(!viewBean.getIdContrepartie().equalsIgnoreCase("0")){ %><%=viewBean.getCompteCourant().getRubrique().getIdExterne()%><% } %>" tabindex="-1" class="libelleLongDisabled" readonly="readonly" />
        </TD>
      </TR>
          
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>