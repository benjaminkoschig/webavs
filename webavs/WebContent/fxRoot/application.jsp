<%@page import="globaz.framework.utils.params.FWParamString"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="globaz.globall.http.JSPUtils"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@page import="globaz.framework.utils.urls.FWUrlsStack.FWLastUrlComparable"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.utils.urls.FWUrl"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.utils.urls.FWUrlsStack"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%
request.setAttribute(JSPUtils.FORCE_CHARSET_DECODE, "UTF-8");
BSession currentSession = (BSession) ((FWController)session.getAttribute(FWServlet.OBJ_CONTROLLER)).getSession();
BSession fxSession = BSessionUtil.createSession("fx", currentSession.getUserId());
%>
<div class="singleMenuHierarchy">
<div class="menuNormal withChilds" onclick="toggleMenuElement(this)"
	onmouseover="menuOnMouseHover(this);"
	onmouseout="menuOnMouseOut(this);"><span class="menuButton">6</span>
<%=new String(fxSession.getLabel("LAST_PAGE_BY_APPLICATION").getBytes("UTF-8"))%></div>
	<div class="child">

<%
FWUrlsStack stack = (FWUrlsStack) session.getAttribute(FWServlet.URL_STACK);
Iterator<FWLastUrlComparable> it = stack.getLastUrlByServlet();
while(it.hasNext()){
	FWLastUrlComparable currentUrl = it.next();
	String label = currentUrl.getServlet().toUpperCase() + "_NAME";
	FWUrl url = currentUrl.getUrl();
	String urlStr = url.getAbsPath() + url.getPageName();
	for(int i=0; i<url.getParamsCount(); i++){
		FWParamString param = url.getParamAt(i);
		if(i==0){
			urlStr+="?";
		} else {
			urlStr+="&";
		}
		urlStr+=URLEncoder.encode(param.getKey());
		urlStr+="=";
		urlStr+=URLEncoder.encode((String)param.getValue());
	}
 %>
<div class="singleMenuHierarchy">
<div class="menuNormal withAction"
	onclick="doAction('<%=urlStr%>', 'fr_main');"
	onmouseover="menuOnMouseHover(this);"
	onmouseout="menuOnMouseOut(this);"><%=new String(fxSession.getLabel(label).getBytes("UTF-8"))%></div>
</div>
<%
	}
%>
</div>
</div>