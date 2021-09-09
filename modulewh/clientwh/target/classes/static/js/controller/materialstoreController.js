/**
 * Controller for Material Store
 */

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'materialstoreController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader',
		clientwh.prefix + 'materialstoreService',

		function ($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader,
			materialstoreService,
		) {

			if (typeof (clientwh.translate.materialstore) === 'undefined' || clientwh.translate.materialstore.indexOf($translate.use()) < 0) {
				console.log(clientwh.translate.materialstore);
				if (typeof (clientwh.translate.materialstore) === 'undefined') {
					clientwh.translate.materialstore = '';
				}
				clientwh.translate.materialstore += $translate.use() + ';';
				$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/materialstore');
				$translate.refresh();
			}

			var unRegister = $rootScope.$on('$translateChangeSuccess', function () {
				console.log('clientwh_store_title');
				$scope.title = $translate.instant('clientwh_store_title');
			});

			// Unregister
			$scope.$on('$destroy', function () {
				unRegister();
			});

			$translate.onReady().then(function () {
				console.log('materialstore onReady');
				$scope.title = $translate.instant('clientwh_store_title');
				$translate.refresh();
			});

			//////////////////// VARIABLE ////////////////////
			$scope.search = {};
			$scope.materialstore = {};

			// Paging.
			$scope.page = {
				pageSize: 9,
				totalElements: 0,
				currentPage: 0
			}

			$scope.show = function () {
				alert($scope.title);
			}

			$scope.goto = function (state) {
				$state.go(clientwh.prefix + state);
			}

			$scope.changeLanguage = function (language) {
				$translate.refresh();
				$translate.use(language);
				$translate.refresh();
				$translate.use(language);
				$translate.refresh();
			}

			//////////////////// INIT ////////////////////
			// Init for list.
			$scope.initList = function () {
				$scope.listWithCriteriasByPage(1);
			}

			////////////////// SHOW ////////////////////
			// Show a create screen.
			$scope.showCreate = function () {
				$scope.initForm(-1);
				$scope.showDialog();
			}

			////////////////// CLEAR ////////////////////			
			// Clear search
			$scope.clearSearch = function () {
				$scope.search = {};
			}

			////////////////// FUNCTION ////////////////////
			// Get by Id.
			$scope.getById = function (id) {
				storeService.getById(id)
					// success.
					.then(function (response) {
						if (response.status === httpStatus.code.OK) {
							var data = angular.fromJson(response.data);
							$scope.materialstore = data;
						}
						else {
							$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
						}
					},
						// error.
						function (response) {
							$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
						});
			}

			// List for page and filter.
			$scope.listWithCriteriasByPage = function (pageNo) {
				$scope.page.currentPage = pageNo;
				materialstoreService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
					// success.
					.then(function (response) {
						if (response.status === httpStatus.code.OK) {
							var result = angular.fromJson(response.data.content);
							$scope.materialstore = result;
							$scope.page.totalElements = 0;
							if (result.length > 0)
								$scope.page.totalElements = response.data.totalElements;
						}
						else if (response.status === httpStatus.code.OK) {
							$scope.stores = [];
							$scope.page.totalElements = 0;
						}
						else {
							$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
						}
					},
						// error.
						function (response) {
							$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
						});
			}
			
			// List for page and filter.
			$scope.listWithCriteriasByIdStoreAndPage = function(idstore, pageNo) {
				$scope.page.currentPage = pageNo;
				materialstoreService.listWithCriteriasByIdStoreAndPage(idstore, $scope.getSearch(), pageNo - 1, $scope.page.pageSize).then(
				// success.
				function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.materialstores = [];
						$scope.page.totalElements = 0;
						if(response.data.content && response.data.content.length > 0) {
							var result = angular.fromJson(response.data.content);
							$scope.materialstores = result;
							if(result.length > 0) {
								$scope.page.totalElements = response.data.totalElements;
							}
						}
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
			}
			
			// List for page and filter.
			$scope.listWithCriteriasByIdMaterialAndPage = function(idmaterial, pageNo) {
				$scope.page.currentPage = pageNo;
				materialstoreService.listWithCriteriasByIdMaterialAndPage(idmaterial, $scope.getSearch(), pageNo - 1, $scope.page.pageSize).then(
				// success.
				function(response) {
					if(response.status === httpStatus.code.OK) {
						$scope.materialstores = [];
						$scope.page.totalElements = 0;
						if(response.data.content && response.data.content.length > 0) {
							var result = angular.fromJson(response.data.content);
							$scope.materialstores = result;
							if(result.length > 0) {
								$scope.page.totalElements = response.data.totalElements;
							}
						}
					} else {
						$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
					}
				},
				// error.
				function(response) {
					$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
				});
			}
			////////////////// Extension ////////////////////
			// Sort by.
			$scope.sortBy = function (keyName) {
				$scope.sortKey = keyName;
				$scope.reverse = !$scope.reverse;
				// Reload data.
				$scope.listWithCriteriasByPage($scope.page.currentPage);
			}

			// Get sort object.
			$scope.getSort = function () {
				var result = [];
				// name.
				if (typeof ($scope.sortKey) !== 'undefined' && $scope.sortKey !== '') {
					var order = 'desc';
					if ($scope.reverse) {
						order = 'asc';
					}
					result.push('sort=' + $scope.sortKey + ',' + order);
				}
				// return.
				return result;
			}

			// Get search object.
			$scope.getSearch = function () {
				var result = [];
				// code.
				if (typeof ($scope.search.code) !== 'undefined' && $scope.search.code !== '') {
					result.push({ key: 'code', operation: 'like', value: $scope.search.code, logic: 'or' },
								{ key: 'name', operation: 'like', value: $scope.search.code, logic: 'or' }
					);
				}
				// return.
				return result;
			}

			// Show message.
			$scope.showMessage = function (message, cssName, autoHide) {
				$scope.alertMessage = message;
				$('#alertMessage').removeClass('alert-danger');
				$('#alertMessage').removeClass('alert-success');
				$('#alertMessage').addClass(cssName);
				$('#alertMessage').slideDown(500, function () {
					if (autoHide) {
						$window.setTimeout(function () {
							$('#alertMessage').slideUp(500, function () {
								$('#alertMessage').removeClass(cssName);
							});
						}, 1000);
					}
				});
			}
	}]);
});
