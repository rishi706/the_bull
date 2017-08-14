app.controller('UserLoginController', function ($scope, $location, $http, $timeout) {
    $scope.loginMesHide = true;

    $scope.$watch("dbConnectStatus", function (newValue, oldValue) {
        if (newValue === "CONNECTED") {
            $scope.labelType = "label-success"; //Change the labelType to label-danger in case of failure to connect to database.
            $scope.loginInstruction = "Please Log in"; //Change it to Please try later/contact us in case of failure to connect to database.
            $scope.labelMessage = "Connected";
        } else {
            $scope.labelType = "label-danger";
            $scope.loginInstruction = "Please try later/Contact us";
            $scope.labelMessage = "Not Connected";
        }
    });

    this.submit = function() {
        $http({
            method: 'GET',
            url: '/DBankWebTier/validate.jsp?id=' + this.username + '&password=' + this.password
        }).then(function successCallback(response, status, headers, config) {
            if(response.data[0].loginResult === 'success'){
                $location.path('userMainPage');
            }else{
                $scope.loginMesHide = false;
                $scope.LoginMessage = "Login failed, your username/password may be incorrect!"
                $timeout(function() {
                   $scope.loginMesHide = true;
                }, 3000);
            }
        }, function errorCallback(response, status, headers, config) {
            $scope.loginMesHide = false;
            $scope.LoginMessage = "Login failed, your username/password may be incorrect!"
            $timeout(function() {
               $scope.loginMesHide = true;
            }, 3000);
        });
        this.username = '';
        this.password = '';
    };
});
