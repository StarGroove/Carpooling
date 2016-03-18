'use strict';

angular.module('carpoolingApp').controller('ItinaryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Itinary',
        function($scope, $stateParams, $uibModalInstance, entity, Itinary) {

        $scope.itinary = entity;
        $scope.load = function(id) {
            Itinary.get({id : id}, function(result) {
                $scope.itinary = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('carpoolingApp:itinaryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.itinary.id != null) {
                Itinary.update($scope.itinary, onSaveSuccess, onSaveError);
            } else {
                Itinary.save($scope.itinary, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
