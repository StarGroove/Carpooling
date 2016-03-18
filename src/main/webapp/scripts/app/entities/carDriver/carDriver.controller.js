'use strict';

angular.module('carpoolingApp')
    .controller('CarDriverController', function ($scope, $state, CarDriver, CarDriverSearch, ParseLinks) {

        $scope.carDrivers = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            CarDriver.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.carDrivers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CarDriverSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.carDrivers = result;
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
            $scope.carDriver = {
                name: null,
                adresse: null,
                id: null
            };
        };
    });
