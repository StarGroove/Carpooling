'use strict';

angular.module('carpoolingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itinary', {
                parent: 'entity',
                url: '/itinarys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'carpoolingApp.itinary.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itinary/itinarys.html',
                        controller: 'ItinaryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itinary');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('itinary.detail', {
                parent: 'entity',
                url: '/itinary/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'carpoolingApp.itinary.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itinary/itinary-detail.html',
                        controller: 'ItinaryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itinary');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Itinary', function($stateParams, Itinary) {
                        return Itinary.get({id : $stateParams.id});
                    }]
                }
            })
            .state('itinary.new', {
                parent: 'itinary',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itinary/itinary-dialog.html',
                        controller: 'ItinaryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    locationStart: null,
                                    locationEnd: null,
                                    driver: null,
                                    car: null,
                                    listStep: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('itinary', null, { reload: true });
                    }, function() {
                        $state.go('itinary');
                    })
                }]
            })
            .state('itinary.edit', {
                parent: 'itinary',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itinary/itinary-dialog.html',
                        controller: 'ItinaryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Itinary', function(Itinary) {
                                return Itinary.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itinary', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('itinary.delete', {
                parent: 'itinary',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/itinary/itinary-delete-dialog.html',
                        controller: 'ItinaryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Itinary', function(Itinary) {
                                return Itinary.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itinary', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
