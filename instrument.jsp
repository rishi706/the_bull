<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="deutschebank.dbutils.InstrumentHandler" %>
<%@ page import="deutschebank.dbutils.Instrument" %>
<%@ page import="deutschebank.dbutils.InstrumentIterator" %>

<jsp:useBean id="globalHelper" class="deutschebank.thebeans.ApplicationScopeHelper" scope="application"/>
<%--jsp:useBean id="InstrumentHandler" class="deutschebank.dbutils.InstrumentHandler" scope="application"/--%>


<%
        String  dbStatus = "DB NOT CONNECTED";
                    globalHelper.setInfo("Selvyn was here");
                    
                    if( globalHelper.bootstrapDBConnection() )
                        dbStatus = "DB Connected";

            out.flush();
            out.println(globalHelper.getInstruments());
            out.flush();

%>
