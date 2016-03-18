'use strict';

angular.module('carpoolingApp')
	.controller('CarDriverDeleteController', function($scope, $uibModalInstance, entity, CarDriver) {

        $scope.carDriver = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CarDriver.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
