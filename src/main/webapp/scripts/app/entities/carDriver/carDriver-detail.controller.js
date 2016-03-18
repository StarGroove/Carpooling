'use strict';

angular.module('carpoolingApp')
    .controller('CarDriverDetailController', function ($scope, $rootScope, $stateParams, entity, CarDriver) {
        $scope.carDriver = entity;
        $scope.load = function (id) {
            CarDriver.get({id: id}, function(result) {
                $scope.carDriver = result;
            });
        };
        var unsubscribe = $rootScope.$on('carpoolingApp:carDriverUpdate', function(event, result) {
            $scope.carDriver = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
