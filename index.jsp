<%-- 
    Document   : index
    Created on : 17-Jun-2016, 16:53:18
    Author     : Selvyn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="globalHelper" class="deutschebank.thebeans.ApplicationScopeHelper" scope="application"/>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="jquery-3.0.0.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>    
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular-route.min.js"></script>
        <link rel="stylesheet" href="https://unpkg.com/ng-table@2.0.2/bundles/ng-table.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://unpkg.com/ng-table@2.0.2/bundles/ng-table.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">			
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="userdetails_validator.js"></script>
        <script src="routingConfig.js"></script>
        <script src="userLogin.js"></script>
        <script src="mainPageCtrl.js"></script>
        <link rel="import" href="userMainPage.html">
        <link rel="import" href="loginBox.html">    
        <link rel="stylesheet" href="loginPage.css">
        <title>Deutsche Bank Case Study</title>
    </head>

    <body background="tradingBackground.jpg" ng-app="DBWebPage" ng-controller="UserLoginController as UCtrl">
        <%
            String  dbStatus = "NOT CONNECTED";

            globalHelper.setInfo("Set any value here for application level access");
            boolean connectionStatus = globalHelper.bootstrapDBConnection();
            
            if( connectionStatus )
            {
                dbStatus = "CONNECTED";
            }
        %>
        <%
            if( connectionStatus )
            {
        %>
            <!--This is a hidden jsp status variable-->
            <div ng-init="dbConnectStatus='CONNECTED'"><h2 style="visibility:hidden; z-index:1; position:absolute" ng-bind="dbConnectStatus"></h2></div>
        <%
            }
        %>
        
        <%
            if( !connectionStatus )
            {
        %>
            <!--This is a hidden jsp status variable-->
            <div ng-init="dbConnectStatus='NOT CONNECTED'"><h2 style="visibility:hidden; z-index:1; position:absolute" ng-bind="dbConnectStatus"></h2></div>
        <%
            }
        %>
        <img src="DeutscheBankLogo.svg.png" alt="HTML5 Icon" width="128" height="128" style="float:right;">		 
        <div class="container">
            <div class="row">
                <div class="col-md-4 col-sm-4"><iframe width="250" height="750" class="rssdog" src="http://www.rssdog.com/index.htm?url=http%3A%2F%2Ffeeds.reuters.com%2Freuters%2FUKWorldNews&mode=html&showonly=&maxitems=0&showdescs=1&desctrim=0&descmax=0&tabwidth=100%25&linktarget=_blank&textsize=inherit&bordercol=%23d4d0c8&headbgcol=%23999999&headtxtcol=%23ffffff&titlebgcol=%23f1eded&titletxtcol=%23000000&itembgcol=%23ffffff&itemtxtcol=%23000000&ctl=0"></iframe></div>    
                <div ng-view></div>
            </div>
        </div>
    </body>
</html>
