/**
 * Controller for Store
 */

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'storeController', ['$scope', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader',
		clientwh.prefix + 'storeService',
		clientwh.prefix + 'materialstoreService',
		clientwh.prefix + 'userService',

		function ($scope, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader,
			storeService,
			materialstoreService,
			userService
		) {

			if (typeof (clientwh.translate.store) === 'undefined' || clientwh.translate.store.indexOf($translate.use()) < 0) {
				console.log(clientwh.translate.store);
				if (typeof (clientwh.translate.store) === 'undefined') {
					clientwh.translate.store = '';
				}
				clientwh.translate.materialstore += $translate.use() + ';';
				$translatePartialLoader.addPart(clientwh.contextPath + '/js/common/message/store');
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
				console.log('store onReady');
				$scope.title = $translate.instant('clientwh_store_title');
				$translate.refresh();
			});

			/*
			 * Get permission
			 * 
			 * */ 
			userService.getPermissionByTarget('store').then(
				// success.
				function (response) {
					$scope.permission = response.data;
					$scope.isPermisCreate = ($scope.permission.admins.length == 0 || $scope.permission.admins.indexOf($scope.permission.id) > -1) || ($scope.permission.creates.length == 0 || $scope.permission.creates.indexOf($scope.permission.id) > -1);
					$scope.isPermisUpdate = ($scope.permission.admins.length == 0 || $scope.permission.admins.indexOf($scope.permission.id) > -1) || ($scope.permission.updates.length == 0 || $scope.permission.updates.indexOf($scope.permission.id) > -1);
					$scope.isPermisDelete = ($scope.permission.admins.length == 0 || $scope.permission.admins.indexOf($scope.permission.id) > -1) || ($scope.permission.deletes.length == 0 || $scope.permission.deletes.indexOf($scope.permission.id) > -1)
				},
				// error.
				function (reponse) {
					$scope.permission = {};
					$scope.isPermisCreate = false;
					$scope.isPermisUpdate = false;
					$scope.isPermisDelete = false;
				}
			);

			//////////////////// VARIABLE //////////////////
			$scope.search = {};
			$scope.store = {};
			$scope.detail = {};

			// Paging.
			$scope.page = {
				pageSize: 6,
				totalElements: 0,
				currentPage: 0
			}
			
			/*
			 * Detail Paging
			 * 
			 * */ 
			$scope.pageDetail = {
				pageSize: 20,
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

			//////////////////// INIT //////////////////
			// Init for list.
			$scope.initList = function () {
				$scope.listWithCriteriasByPage(1);
			}

			// Init for form.
			$scope.initForm = function (id) {
				$scope.create();
				$scope.store.id = id;
				if ($scope.store.id > -1) {
					$scope.getById($scope.store.id);
				}
				$scope.frmDirty = false;
			}
			
			/*
			 * Init for detail list
			 * 
			 * */ 
			$scope.initDetail = function (id) {
				$scope.detail.idstore = id;
				$scope.listDetailWithCriteriasByPage(1);
			}
			
			////////////////// SHOW	//////////////////
			// Show a create screen.
			$scope.showCreate = function () {
				$scope.initForm(-1);
				$scope.showDialog();
			}

			// Show a form screen.
			$scope.showUpdate = function (id) {
				$scope.initForm(id);
				$scope.showDialog();
			}
			
			/*
			 * Show store detail
			 * 
			 * */ 
			$scope.showDetail = function (id){
				$scope.initDetail(id);
				$scope.showDetailDialog();
			}

			// Show edit view.
			$scope.showDialog = function () {
				var htmlUrlTemplate = clientwh.contextPath + '/view/store_form.html';
				clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
					console.log('closed');
				}, function (evt) {
					console.log('not closed');
				});
			}
			
			/*
			 * Show detail view
			 * 
			 * */ 
			$scope.showDetailDialog = function () {
				var htmlUrlTemplate = clientwh.contextPath + '/view/materialstore_list.html';
				clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
					console.log('closed');
				}, function (evt) {
					console.log('not closed');
				});
			}

			////////////////// CLEAR //////////////////
			// Clear search
			$scope.clearSearch = function () {
				$scope.search = {};
				$scope.listWithCriteriasByPage(1);
			}
			
			/*
			 * Clear search detail
			 * 
			 * */ 
			$scope.clearSearchDetail = function (){
				$scope.search = {};
				$scope.listDetailWithCriteriasByPage(1);
			}

			// Clear form
			$scope.clearForm = function () {
				// Clear all field
				$scope.store = { id: -1 };

				$scope.frmStore.code.$setPristine();
				$scope.frmStore.code.$setUntouched();
				$scope.frmStore.name.$setPristine();
				$scope.frmStore.name.$setUntouched();

				$scope.frmStore.$setPristine();
				$scope.frmStore.$setUntouched();
				$scope.frmDirty = false;
			}

			/*
			 * Reset validate duplicate
			 * 
			 * */
			$scope.resetValidateDuplicate = function (number) {
				switch (number) {
					case 1:
						delete $scope.frmStore.code.$error.duplicate;
						break;
					case 2:
						delete $scope.frmStore.name.$error.duplicate;
						break;
					default:
						delete $scope.frmStore.name.$error.duplicate;
						delete $scope.frmStore.code.$error.duplicate;
						break;
				}
			}

			////////////////// FUNCTION //////////////////
			// Create
			$scope.create = function () {
				$scope.store = { id: -1 };
			}
			// Save.
			$scope.save = function () {
				$scope.resetValidateDuplicate(0);
				if ($scope.frmStore.$invalid) {
					$scope.frmStore.$dirty = true;
					$scope.frmDirty = true;
					return;
				}
				$scope.showMessage('Saving!', 'alert-success', false);

				var result;

				if ($scope.store.id > -1) {
					result = storeService.updateWithLock($scope.store.id, $scope.store);
				}
				else {
					result = storeService.create($scope.store);
				}

				result
					// success.
					.then(function (response) {
						if (response.status === httpStatus.code.OK) {
							if ($scope.store.id > -1) {
								$scope.store.version += 1;
							}
							else {
								$scope.store.version = 1;
							}
							$scope.store.id = response.data;
							$scope.showMessage($translate.instant('clientwh_home_saved'), 'alert-success', true);
							$scope.listWithCriteriasByPage(1);
						}
						else {
							if (response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
								$scope.showMessage($translate.instant('clientwh_servercode_' + response.data.code), 'alert-danger', false);
							}
							else if (response.data.code == clientwh.serverCode.EXISTCODE) {
								$scope.frmStore.code.$error.duplicate = true;
							}
							else if (response.data.code == clientwh.serverCode.EXISTNAME) {
								$scope.frmStore.name.$error.duplicate = true;
							}
							else if (response.data.code == clientwh.serverCode.EXISTALL) {
								$scope.frmStore.code.$error.duplicate = true;
								$scope.frmStore.name.$error.duplicate = true;
							}
							$scope.showMessage($translate.instant('clientwh_home_fail'), 'alert-danger', true);
						}
					},
					// error.
					function (response) {
						$scope.showMessage($translate.instant('clientwh_home_error'), 'alert-danger', true);
					});
			}

			// Delete.
			$scope.delete = function (id, version) {
				if ($window.confirm('Are you sure to delete?')) {
					storeService.updateForDeleteWithLock(id)
						// success.
						.then(function (response) {
							if (response.status === httpStatus.code.OK) {
								$scope.showMessage('Deleted!', 'alert-success', true);
								$scope.listWithCriteriasByPage(1);
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
			}

			// Get by Id.
			$scope.getById = function (id) {
				storeService.getById(id)
					// success.
					.then(function (response) {
						if (response.status === httpStatus.code.OK) {
							var data = angular.fromJson(response.data);
							$scope.store = data;
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
			
			/*
			 * Get Detail by Id.
			 * 
			 * */ 
			$scope.getById = function (id) {
				materialstoreService.getById(id)
					// success.
					.then(function (response) {
						if (response.status === httpStatus.code.OK) {
							var data = angular.fromJson(response.data);
							$scope.detail = data;
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
				storeService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort())
					// success.
					.then(function (response) {
						if (response.status === httpStatus.code.OK) {
							var result = angular.fromJson(response.data.content);
							$scope.stores = result;
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

			/*
			 * list Detail With Criterias By Page
			 * 
			 * */
			$scope.listDetailWithCriteriasByPage = function (pageNo) {
				$scope.pageDetail.currentPage = pageNo;
				materialstoreService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.pageDetail.pageSize, $scope.getSort())
					// success.
					.then(function (response) {
						if (response.status === httpStatus.code.OK) {
							var result = angular.fromJson(response.data.content);
							$scope.materialstores = result;
							$scope.pageDetail.totalElements = 0;
							if (result.length > 0)
								$scope.pageDetail.totalElements = response.data.totalElements;
						}
						else if (response.status === httpStatus.code.OK) {
							$scope.materialstores = [];
							$scope.pageDetail.totalElements = 0;
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
			
			////////////////// Extension //////////////////
			// Sort by.
			$scope.sortBy = function (keyName) {
				$scope.sortKey = keyName;
				$scope.reverse = !$scope.reverse;
				// Reload data.
				$scope.listWithCriteriasByPage($scope.page.currentPage);
			}

			/*
			 * Detail Sort by
			 * 
			 * */ 
			$scope.sortDetailBy = function (keyName) {
				$scope.sortKey = keyName;
				$scope.reverse = !$scope.reverse;
				// Reload data.
				$scope.listDetailWithCriteriasByPage($scope.pageDetail.currentPage);
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
								{ key: 'name', operation: 'like', value: $scope.search.code, logic: 'or' },
								{ key: 'quantity', operation: 'like', value: $scope.search.code, logic: 'or' }
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
