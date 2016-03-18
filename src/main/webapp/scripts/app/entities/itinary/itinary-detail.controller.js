'use strict';

angular.module('carpoolingApp')
    .controller('ItinaryDetailController', function ($scope, $rootScope, $stateParams, entity, Itinary) {
        $scope.itinary = entity;
        $scope.load = function (id) {
            Itinary.get({id: id}, function(result) {
                $scope.itinary = result;
            });
        };
        var unsubscribe = $rootScope.$on('carpoolingApp:itinaryUpdate', function(event, result) {
            $scope.itinary = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
