app.controller('mainPageController', function($scope, $http) {
    $scope.dbConnectHide = true;
    $scope.hideTableDetails = true;
    $scope.detailTableSortType = '';
    $scope.sortType = '';
    $scope.sortReverse  = false;  // set the default sort order
    $scope.displayTable = {tName:'', name:[], data:[]};
    $scope.currentTable = '';
    callForAvaTable();
    
    $scope.displayTableDetails = function(tableName){
        $scope.currentTable = tableName;
        $scope.currentPage = 0;
        if(tableName === 'deal')
            callForData(tableName, '?page=0');
        else
            callForData(tableName, '');
    };
    
    $scope.changeSortType = function(sortField){
        $scope.sortType = sortField;
        $scope.sortReverse = !$scope.sortReverse;
    };

    $scope.updatePage = function(pageUpdate){
        if(pageUpdate === 'previous' && $scope.currentPage > 0){
            $scope.currentPage--;
            callForData($scope.currentTable, '?page='+$scope.currentPage);
        }
        if(pageUpdate === 'next' && $scope.currentPage < Number.MAX_SAFE_INTEGER){
            $scope.currentPage++;
            callForData($scope.currentTable, '?page='+$scope.currentPage);
        }
    };

    $scope.refreshTable = function(refreshRef){
        if(refreshRef === 'showTables')
            callForAvaTable();
        else{
            $scope.currentTable = refreshRef;
            $scope.currentPage = 0;
            if(refreshRef === 'deal')
                callForData(refreshRef, '?page=0');
            else
                callForData(refreshRef, '');
        }       
    };

    function callForAvaTable(){
        $http({
            method: 'GET',
            url: '/DBankWebTier/showtables.jsp'
        }).then(function successCallback(response, status, headers, config) {
            $scope.dbConnectHide = true;
            $scope.tableNames = [];
            var tableIndex = 1;
            response.data.forEach(function(each){
                if(filterUnnecssaryTable(each) === true){
                    $scope.tableNames.push({id:tableIndex++, name:each.tableName});
                }
            });
        }, function errorCallback(response, status, headers, config) {
            $scope.dbConnectHide = false;
            $scope.dbConnectMes = "Database cannot be connected!";
        });
    };
    
    function callForData(tableName, appending){
        var url = '/DBankWebTier/' + tableName + '.jsp' + appending; 
        $http({
        method: 'GET',
        url: url
        }).then(function successCallback(response, status, headers, config) {
            $scope.hideTableDetails = false;
            $scope.displayTable.tName = tableName;
            $scope.displayTable.name = Object.keys(response.data[0]);
            $scope.sortType = $scope.displayTable.name[0];
            $scope.displayTable.data = response.data;
        }, function errorCallback(response, status, headers, config) {
            $scope.hideTableDetails = true;
            $scope.dbConnectMes = tableName + " cannot be connected!";
        });
    };
      
    function filterUnnecssaryTable (tableNameObj){
        var unusedTable = ["anonymous_users", "login_trail", "users"];
        for (i=0; i<unusedTable.length; i++)
            if(tableNameObj.tableName === unusedTable[i]) return false;
        return true;
    };

});


