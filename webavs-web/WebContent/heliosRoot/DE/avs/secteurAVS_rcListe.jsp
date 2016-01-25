<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.helios.db.avs.*, globaz.helios.translation.*, globaz.globall.util.*, globaz.framework.util.*" %>
<%
    CGSecteurAVSManager viewBean = (CGSecteurAVSManager)request.getAttribute ("viewBean");
    size =viewBean.getSize();
    detailLink ="helios?userAction=helios.avs.secteurAVS.afficher&idMandat=" + viewBean.getForIdMandat() + "&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<Th width="16">&nbsp;</Th>
<Th width="">Bereich</Th>
<Th width="">Bezeichnung</Th>
<Th width="">Aufgabetyp</Th>
<Th width="">Manueller Abschluss</Th>
<Th width="">Satzaufteilung</Th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	CGSecteurAVS entity = (CGSecteurAVS)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdSecteurAVS()+"'";
	String tmp = detailLink+entity.getIdSecteurAVS();
%>

     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
     </TD>

     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getIdSecteurAVS()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getLibelle()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getTypeTacheLibelle()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%if (entity.isClotureManuelle().booleanValue()){%><img src="<%=request.getContextPath()%>/images/select.gif"><%}%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getTauxVentilation()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>