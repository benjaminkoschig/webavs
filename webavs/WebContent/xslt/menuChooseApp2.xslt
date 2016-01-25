<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
    

      <script language="javascript">
                  var keyselected = "";
                  var selected = "";
        function toggle(mId,pId) { 
            (mId.style.display!="none") ? collapse(mId,pId) : expand(mId,pId);
        }
        function expand(mId,pId) { 
        
          mId.style.display="block";
          var spanColl = pId.all.tags("SPAN");
          spanColl(0).innerHTML = '<font face="Webdings">6</font>';
      
        }
        function collapse(mId,pId) { 
          mId.style.display="none";
          var spanColl = pId.all.tags("SPAN");
          spanColl(0).innerHTML = '<font face="Webdings">4</font>';
      
            
        }
        function expandall(tId){
          var divColl = tId.all.tags("DIV");
          for (i=0; i&lt;divColl.length; i++) {
            if (divColl(i).className == "child") {
              expand(divColl(i),divColl(i-1));
            }
          }
        }
        function collapseall(tId){
          var divColl = tId.all.tags("DIV");
          for (i=0; i&lt;divColl.length; i++) {
            if (divColl(i).className == "child") {
              collapse(divColl(i),divColl(i-1));
            }
          }
        }
        function changeStyle(mId,color,flag) {

                mId.style.background=color;
                  if (flag == "1"){
                     mId.style.border="solid 1 #43546B";                 
                  } else {
                     mId.style.border="solid 1 #B3C4DB";
                }

        }
      </script>
      <head>
        <style type="text/css">
        body {
		    font-family : Verdana,sans-serif;
		    font-weight : normal;
		    font-size : 11px;
		    margin-top : 0px;
		    margin-left : 5px;
		    margin-right : 0px;
		    margin-bottom : 5px;
		    border-width : 0px 0px 0px 0px;
		}

		table {
		    font-family : Verdana,Arial;
		    font-size : 11px;
		    empty-cells : show;
		}
            .style1 {
              border : solid 1 #B3C4DB;
              padding : 2 2 2 2; 
              cursor : hand;
             
            }
            
            .style2 {
            border : solid 1 #B3C4DB;
            }
        
            .child{
              margin-left : 7;
              padding-left :10;
              
              border-left : dotted 2 gray;
              background-color : #B3C4DB;
            }
            
          A:link    { text-decoration: none; color:black }
          A:visited { text-decoration: none; color:black }
          A:active  { text-decoration: none; color:black }
          A:hover   { text-decoration: none; color:black;}
		.title{
		    font-size : x-small;
		    font-family : "Lucida Sans Unicode",Verdana,Arial;
		    color : white;
		    background-color : #226194;
		    font-weight : bold;
		}
        </style>
      </head>
<body style="border:solid 1; background-color:black; overflow:hidden; margin-top:2px; margin-right:2px;
margin-left: 2px; margin-bottom:2px;">
<div style="width=100%;height=100%" id="outilsMenu">
        <!-- 
  **************************************************************
  *
  * main menu table
  *
  ************************************************************** 
  -->
        <xsl:element name="table">
          <xsl:attribute name="border">0</xsl:attribute>
          <xsl:attribute name="width">100%</xsl:attribute>
          <xsl:attribute name="height">100%</xsl:attribute>
          <xsl:attribute name="CELLSPACING">1</xsl:attribute>
          <xsl:attribute name="CELLPADDING">0</xsl:attribute>
          <xsl:attribute name="bgcolor">black</xsl:attribute>
          <xsl:attribute name="id"><xsl:value-of select="/menu/@id"/></xsl:attribute>
          <tr>
            <td>
              <table CELLSPACING="0" CELLPADDING="0" width="100%" height="100%">
                <tr>
                  <td class="title">
                    <b>&#160;<xsl:value-of select="/menu/@name"/></b>
                  </td>
                </tr>

              </table>
            </td>
          </tr>
                       
          <tr height="100%">
            <td>
              <table height="100%" border="0" CELLSPACING="0" CELLPADDING="4" width="100%" bgcolor="#B3C4DB">
                <tr valign="top">
                  <td>
                    <xsl:apply-templates select="/menu/*"/>
                  </td>
                </tr>
                <tr>
                  <td bgcolor="#444444"/>
                </tr>
                <tr>
                  <td bgcolor="#888888"/>
                </tr>
                <tr>
                  <td bgcolor="#aaaaaa"/>
                </tr>
                <tr>
                  <td bgcolor="#cccccc"/>
                </tr>
                <tr>
                  <td bgcolor="#dddddd"/>
                </tr>
                <tr>
                  <td bgcolor="#eeeeee"/>
                </tr>
                <tr bgcolor="#eeeeee" height="100%" width="100%">
                  <td/>
                </tr>
              </table>
            </td>
          </tr>
        </xsl:element>
      </div>
      </body>
    </html>
  </xsl:template>
  <!-- 
**************************************************************
*
* first items
*
************************************************************** 
-->
  <xsl:template match="menu/*">
    <xsl:call-template name="items">
      <xsl:with-param name="mid" select="generate-id()"/>
    </xsl:call-template>
  </xsl:template>
  <!-- 
**************************************************************
*
* next items
*
************************************************************** 
-->
  <xsl:template name="items">
    <xsl:param name="mid"/>
    <xsl:choose>
      <!-- menu item -->
      <xsl:when test="item">
        <xsl:comment>dddddddddd</xsl:comment>
        <xsl:variable name="numItem" select="generate-id()"/>
        <a href="#" id="{$numItem}" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.{$numItem},'#D7E4FF',1);selected=this.children.{$numItem};" onblur="javascript:changeStyle(this.children.{$numItem},'#B3C4DB',0)" onkeypress="javascript:toggle(_{$mid},this)">
          <div id="{$numItem}">
            <!--  <div> -->
            <xsl:attribute name="class">style1</xsl:attribute>
            <xsl:attribute name="onClick">javascript:toggle(_<xsl:value-of select="$mid"/>,this)</xsl:attribute>
            <xsl:attribute name="onMouseMove">javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);</xsl:attribute>
            <xsl:attribute name="onMouseOut">javascript:changeStyle(this,'#B3C4DB',0)</xsl:attribute>
            <span>
              <font face="Webdings">4</font>
            </span>
            <b>&#160;<script>document.write("<xsl:value-of select="@name"/>")</script>
            </b>
          </div>
        </a>
        <div>
          <xsl:attribute name="id">_<xsl:value-of select="$mid"/></xsl:attribute>
          <xsl:attribute name="class">child</xsl:attribute>
          <xsl:attribute name="style">display:none</xsl:attribute>
          <xsl:attribute name="width">100%</xsl:attribute>
          <xsl:for-each select="./*">
            <xsl:call-template name="items">
              <xsl:with-param name="mid" select="generate-id()"/>
            </xsl:call-template>
          </xsl:for-each>
        </div>
        <div class="style1" style="display:none"/>
      </xsl:when>
      <!-- single item -->
      <xsl:otherwise>
        <xsl:variable name="numDiv" select="generate-id()"/>
        <a href="#" id="{$numDiv}" onfocus="javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);changeStyle(this.children.{$numDiv},'#D7E4FF',1);selected=this.children.{$numDiv};" onblur="javascript:changeStyle(this.children.{$numDiv},'#B3C4DB',0)">
          <xsl:choose>
            <xsl:when test="@frame='' or not (@frame) ">
              <xsl:attribute name="onkeypress">javascript:top.location.replace('<xsl:value-of select="@link"/>');</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="onkeypress">javascript:top.frames('<xsl:value-of select="@frame"/>').location.replace('<xsl:value-of select="@link"/>');</xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
          <div id="{$numDiv}">
            <xsl:attribute name="class">style1</xsl:attribute>
            <xsl:attribute name="onMouseMove">javascript:if(selected!='') changeStyle(selected,'#B3C4DB',0);selected=this;changeStyle(this,'#D7E4FF',1);</xsl:attribute>
            <xsl:attribute name="onMouseOut">javascript:changeStyle(this,'#B3C4DB',0)</xsl:attribute>
            <xsl:choose>
              <xsl:when test="@frame='' or not (@frame) ">
                <xsl:attribute name="onClick">javascript:top.location.replace('<xsl:value-of select="@link"/>');</xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="onClick">javascript:top.frames('<xsl:value-of select="@frame"/>').location.replace('<xsl:value-of select="@link"/>');</xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
            <script>document.write("<xsl:value-of select="@name"/>")</script>
          </div>
        </a>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
