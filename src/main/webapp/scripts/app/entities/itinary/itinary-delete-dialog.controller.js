'use strict';

angular.module('carpoolingApp')
	.controller('ItinaryDeleteController', function($scope, $uibModalInstance, entity, Itinary) {

        $scope.itinary = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Itinary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
