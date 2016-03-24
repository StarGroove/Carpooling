'use strict';

angular.module('carpoolingApp')
    .controller('ItinaryController', function ($scope, $state, Itinary, ItinarySearch) {

        $scope.itinarys = [];
        $scope.loadAll = function() {
            Itinary.query(function(result) {
               $scope.itinarys = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ItinarySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.itinarys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.itinary = {
                locationStart: null,
                locationEnd: null,
                driver: null,
                car: null,
                listStep: null,
                id: null
            };
        };
    });
