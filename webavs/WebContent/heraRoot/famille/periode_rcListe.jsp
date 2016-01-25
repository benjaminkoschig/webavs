<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.hera.enums.TypeDeDetenteur"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.hera.helpers.famille.*"%>  
<%
	globaz.hera.vb.famille.SFPeriodeListViewBean viewBean =(globaz.hera.vb.famille.SFPeriodeListViewBean) request.getAttribute("viewBean");
	String idMembreFamille = ((String)request.getParameter("idMembreFamille"));
	size = viewBean.getSize();
	
	globaz.framework.controller.FWController ctrl = (globaz.framework.controller.FWController) session.getAttribute("objController");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%@page import="globaz.globall.db.BSession"%>
<TH><ct:FWLabel key="JSP_PERIODE_TYPE"/></TH>    
	<TH><ct:FWLabel key="JSP_PERIODE_DATED"/></TH>
	<TH><ct:FWLabel key="JSP_PERIODE_DATEF"/></TH>
	<TH><ct:FWLabel key="JSP_PERIODE_TYPE_DETENTEUR"/></TH>
	<TH><ct:FWLabel key="JSP_PERIODE_DETENTEUR"/></TH>
	<TH><ct:FWLabel key="JSP_PERIODE_PAYS"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	globaz.hera.vb.famille.SFPeriodeViewBean line = (globaz.hera.vb.famille.SFPeriodeViewBean) viewBean.get(i);
	String idPeriode = line.getIdPeriode();
	idMembreFamille = line.getIdMembreFamille();
	detailLink = baseLink +"afficher&idMembreFamille=";
	String detail = targetLocation + "='" + detailLink + idMembreFamille+"&selectedId="+line.getIdPeriode()+"'";
	globaz.hera.helpers.famille.SFRequerantHelper rh = new globaz.hera.helpers.famille.SFRequerantHelper();
	String libelleTypeDetenteur = "";
	// Type de détenteur
	if(!JadeStringUtil.isBlankOrZero(line.getCsTypeDeDetenteur())){
	    TypeDeDetenteur typeDeDetenteur = TypeDeDetenteur.fromCodeSytem(line.getCsTypeDeDetenteur());
	    if(typeDeDetenteur != null){
	        libelleTypeDetenteur = objSession.getLabel(typeDeDetenteur.getLabelKey());
	    }
	}
	
%>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=ctrl.getSession().getCodeLibelle(line.getType())%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getDateDebut()%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=line.getDateFin()%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=libelleTypeDetenteur%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=(line.getIdDetenteurBTE().equals("0"))?"":rh.getNomPrenomMembre(line.getIdDetenteurBTE(), (BSession)ctrl.getSession())%></TD>
<TD class="mtd" nowrap onclick="<%=detail%>" align="center"><%=(line.getPays().equals("0"))?"":ctrl.getSession().getCodeLibelle(line.getPays())%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>