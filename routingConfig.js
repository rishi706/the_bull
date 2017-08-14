var app = angular.module("DBWebPage", ["ngRoute", "ngTable"]);
app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "loginBox.html"
    })
    .when("/userMainPage", {
        templateUrl : "userMainPage.html"
    });
}]);