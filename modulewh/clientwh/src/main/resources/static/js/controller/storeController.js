/**
 * Controller for Store
 */

define(['require', 'angular'], function (require, angular) {
	app.aController(clientwh.prefix + 'storeController', ['$scope', '$mdToast', '$state', '$rootScope', '$mdDialog', '$http', '$log', '$window', '$location', '$filter', '$translate', '$translatePartialLoader', '$q',
		clientwh.prefix + 'storeService',
		clientwh.prefix + 'materialstoreService',
		clientwh.prefix + 'userService',

		function ($scope, $mdToast, $state, $rootScope, $mdDialog, $http, $log, $window, $location, $filter, $translate, $translatePartialLoader, $q,
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

			$scope.search = {};
			$scope.searchMaterialStore = {};
			$scope.store = {};
			$scope.manager = {};

			// Paging.
			$scope.page = {
				pageSize: 9,
				totalElements: 0,
				currentPage: 0
			}

			// Paging material store.
			$scope.pageStore = {
				pageSize: 9,
				totalElements: 0,
				currentPage: 0
			}

			// Close dialoglist.
			$scope.closeDialogList = function () {
				$mdToast.hide();
				$mdDialog.hide({ id: $scope.catalog.id });
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

			$scope.showMessageOnToast = function (message) {
				$mdToast.show({
					template: '<md-toast class="md-toast">' + message + '</md-toast>',
					hideDelay: 3000,
					position: 'top right'
				})
			}

			$scope.showMessageOnToastList = function (message) {
				$mdToast.show({
					template: '<md-toast class="md-toast">' + message + '</md-toast>',
					hideDelay: 3000,
					position: 'right'
				})
			}

			$scope.OkToast = function () {
				$mdToast.hide(true);
			};

			$scope.closeToast = function () {
				$mdToast.hide(false);
			};

			$scope.cofirmDeleteToastList = function (id, version) {
				$mdToast.show({
					templateUrl: clientwh.contextPath + '/view/toast.html',
					hideDelay: 5000,
					controller: 'clientwhstoreController',
					locals: { params: { showBtnClose: false } },
					position: 'right'
				}).then(function (response) {
					if (response) {
						$scope.delete(id, version);
					}
				});
			}

			$scope.cofirmDeleteToastForm = function () {
				$mdToast.show(
					{
						templateUrl: clientwh.contextPath + '/view/toast.html',
						hideDelay: 5000,
						controller: 'clientwhstoreController',
						locals: { params: { showBtnClose: false } },
						position: 'top right'
					}).then(function (response) {
						if (response) {
							$scope.deleteOnForm();
						}
					});
			}

			$scope.closeDialog = function () {
				$mdToast.hide();
				$mdDialog.hide();
			}

			// Promise list for select.
			var listAllSelectPromise;

			// Init for list.
			$scope.initList = function () {
				if (typeof (listAllSelectPromise) === 'undefined') {
					var listAllSelectDefered = $q.defer();
					listAllSelectPromise = listAllSelectDefered.promise;
					listAllSelectDefered.resolve([]);
				}
				listAllSelectPromise.then(
					// Success.
					function (response) {
						// Get permission.
						userService.definePermissionByTarget($scope, 'store').then(
							// success.
							function (response) {
								// List data.
								$scope.listWithCriteriasByPage(1);
							},
							// error.
							function (reponse) {
								$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
							}
						);
					},
					// Error.
					function (response) { }
				);
			}























			// Init for form.
			$scope.initForm = function (id) {
				if (typeof (listAllSelectPromise) === 'undefined') {
					var listAllSelectDefered = $q.defer();
					listAllSelectPromise = listAllSelectDefered.promise;
					listAllSelectDefered.resolve([]);
				}
				listAllSelectPromise.then(
					// Success.
					function (response) {
						$scope.createNew();
						$scope.store.id = id;
						$scope.manager.idmanager = ""
						if ($scope.store.id > -1) {
							$scope.getById($scope.store.id);
						}
						$scope.frmDirty = false;
					},
					// Error.
					function (response) {

					}
				);
			}

			// Call service: list all for select.
			$scope.listAllForSelect = function () {
				var listAllSelectDeferred = $q.defer();
				// Call service.
				var listUserForSelectDeferred = userService.listAllForSelect();
				// Response.
				$q.all([listUserForSelectDeferred]).then(
					// Successes.
					function (responses) {
						$scope.ctlentityfieldManager.items = responses[0].data;
						$scope.managers = responses[0].data;
						// Resolve promise.
						listAllSelectDeferred.resolve(responses);
					},
					// Errors.
					function (responses) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
						// Reject promise.
						listAllSelectDeferred.reject(responses);
					}

				);
				return listAllSelectDeferred.promise;
			}

			listAllSelectPromise = $scope.listAllForSelect();

			// Selected manager by id
			$scope.itemSelected = function () {
				for (var i = 0; i < $scope.ctlentityfieldManager.items.length; i++) {
					if ($scope.store.idmanager == $scope.ctlentityfieldManager.items[i].id) {
						$scope.ctlentityfieldManager.selectedItem = {};
						$scope.ctlentityfieldManager.selectedItem = $scope.ctlentityfieldManager.items[i];
						break;
					}
				}
			}

			// Show detail 
			$scope.showDetail = function (id) {
				$scope.getById(id);
				$scope.showDialogDetail();
			}

			//Show detail store
			$scope.showDialogDetail = function () {
				var htmlUrlTemplate = clientwh.contextPath + '/view/materialstore_list.html';
				clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
					console.log('closed');
				}, function (evt) {
					console.log('not closed');
				});
			}

			// Show a create screen.
			$scope.showCreate = function () {
				$scope.initForm(-1);
				$scope.showDialog();
				$scope.ctlentityfieldManager.selectedItem = null;
			}

			// Show a form screen.
			$scope.showUpdate = function (id) {
				$scope.initForm(id);
				$scope.showDialog();
				$scope.ctlentityfieldManager.selectedItem = null;
			}


			// Show edit view.
			$scope.showDialog = function () {
				$mdToast.hide();
				var htmlUrlTemplate = clientwh.contextPath + '/view/store_form.html';
				clientwh.showDialog($scope, $mdDialog, htmlUrlTemplate).then(function (evt) {
					console.log('closed');
				}, function (evt) {
					console.log('not closed');
				});
			}

			// Clear form
			$scope.resetValidate = function () {
				$scope.resetValidateDuplicate(0);

				// idmanager.
				$scope.frmStore.idmanager.$setPristine();
				$scope.frmStore.idmanager.$setUntouched();

				// code.
				$scope.frmStore.code.$setPristine();
				$scope.frmStore.code.$setUntouched();
				$scope.frmStore.code.$modelValue = null;

				// name.
				$scope.frmStore.name.$setPristine();
				$scope.frmStore.name.$setUntouched();
				$scope.frmStore.name.$modelValue = null;

				$scope.frmStore.$setPristine();
				$scope.frmStore.$setUntouched();

				$scope.ctlentityfieldManager.selectedItem = null;
				$scope.ctlentityfieldManager.searchText = null;

				$scope.frmDirty = false;
			}

			//Reset validate duplicate
			$scope.resetValidateDuplicate = function (number) {
				switch (number) {
					case 1:
						if ($scope.frmStore.code.$error.duplicate) {
							delete $scope.frmStore.code.$error.duplicate;
							$scope.frmStore.code.$invalid = false;
							$scope.frmStore.code.$valid = true;
						}
						break;
					case 2:
						if ($scope.frmStore.name.$error.duplicate) {
							delete $scope.frmStore.name.$error.duplicate;
							$scope.frmStore.name.$invalid = false;
							$scope.frmStore.name.$valid = true;
						}
						break;
					default:
						if ($scope.frmStore.code.$error.duplicate) {
							delete $scope.frmStore.code.$error.duplicate;
							$scope.frmStore.code.$invalid = false;
							$scope.frmStore.code.$valid = true;
						}
						if ($scope.frmStore.name.$error.duplicate) {
							delete $scope.frmStore.name.$error.duplicate;
							$scope.frmStore.name.$invalid = false;
							$scope.frmStore.name.$valid = true;
						}
						break;
				}
			}

			// Create new.
			$scope.create = function () {
				$scope.store = { id: -1 };
				$scope.resetValidate();
			}

			// Create new.
			$scope.createNew = function () {
				$scope.store = { id: -1 };
				$scope.ctlentityfieldManager.selectedItem = null;
				$scope.ctlentityfieldManager.searchText = null;
			}

			// Save.
			$scope.save = function () {
				if ($scope.ctlentityfieldManager.selectedItem == null) {
					$scope.frmStore.idmanager.$invalid = true;
					$scope.frmStore.idmanager.$touched = true;
				}
				if ($scope.frmStore.$invalid
					|| $scope.frmStore.code.$invalid
					|| $scope.frmStore.idmanager.$invalid
					|| $scope.frmStore.note.$invalid
				) {
					$scope.frmStore.$dirty = true;
					$scope.frmDirty = true;
					return;
				}
				$scope.showMessageOnToast($translate.instant('clientwh_home_saving'));

				var result;

				$scope.store.scope = 'store';
				$scope.store.code = $scope.store.code.trim();
				$scope.store.name = $scope.store.name.trim()
				
				if ($scope.store.id > -1) {
					result = storeService.updateWithLock($scope.store.id, $scope.store);
				} else {
					result = storeService.create($scope.store);
				}

				result.then(
					// success.
					function (response) {
						if (response.status === httpStatus.code.OK) {
							if ($scope.store.id > -1) {
								$scope.store.version = response.data;
							}
							else {
								$scope.store.id = response.data;
								$scope.store.version = 1;
							}
							$scope.showMessageOnToast($translate.instant('clientwh_home_saved'));
							$scope.listAllForSelect();
							$scope.listWithCriteriasByPage(1);
						}
						else {
							if (response.data.code == clientwh.serverCode.VERSIONDIFFERENCE) {
								$scope.showMessageOnToast($translate.instant('clientwh_servercode_' + response.data.code));
							}
							if (response.data.code == clientwh.serverCode.EXISTCODE) {
								$scope.frmStore.code.$error.duplicate = true;
							}
							if (response.data.code == clientwh.serverCode.EXISTNAME) {
								$scope.frmStore.name.$error.duplicate = true;
							}
							if (response.data.code == clientwh.serverCode.EXISTALL) {
								$scope.frmStore.code.$error.duplicate = true;
								$scope.frmStore.name.$error.duplicate = true;
							}
							$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
						}
					},
					// error.
					function (response) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					});
			}

			// Delete.
			$scope.delete = function (id, version) {
				storeService.updateForDeleteWithLock(id, version).then(
					// success.
					function (response) {
						if (response.status === httpStatus.code.OK) {
							$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
							$scope.listWithCriteriasByPage(1);
						}
						else {
							$scope.showMessageOnToastList($translate.instant('clientwh_home_deleted'));
						}
					},
					// error.
					function (response) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					});
			}

			// Delete with create.
			$scope.deleteOnForm = function () {
				storeService.updateForDeleteWithLock($scope.store.id, $scope.store.version).then(
					// success.
					function (response) {
						if (response.status === httpStatus.code.OK) {
							$scope.showMessageOnToast($translate.instant('clientwh_home_deleted'));
							$scope.createNew();
							$scope.listWithCriteriasByPage(1);
						}
						else {
							$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
						}
					},
					// error.
					function (response) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					});
			}

			// Get by Id.
			$scope.getById = function (id) {
				storeService.getById(id).then(
					// success
					function (response) {
						if (response.status === httpStatus.code.OK) {
							if (response.data != null) {
								var data = angular.fromJson(response.data);
								$scope.store = data;
								$scope.itemSelected();
							}
						}
						else {
							$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
						}
					},
					// error.
					function (response) {
						$scope.showMessageOnToast($translate.instant('clientwh_home_error'));
					});
			}

			// List for page and filter.
			$scope.listWithCriteriasByPage = function (pageNo) {
				$scope.page.currentPage = pageNo;
				storeService.listWithCriteriasByPage($scope.getSearch(), pageNo - 1, $scope.page.pageSize, $scope.getSort()).then(
					// success.
					function (response) {
						if (response.status === httpStatus.code.OK) {
							if ($scope.search.manager == 0) {
								$scope.search.manager = "";
							}
							if (response.data != null) {
								var result = angular.fromJson(response.data.content);
								$scope.stores = result;
								$scope.page.totalElements = 0;
								if (result.length > 0) {
									$scope.page.totalElements = response.data.totalElements;
								}
							}
							else {
								$scope.stores = [];
								$scope.page.totalElements = 0;
							}
						}
						else {
							$scope.showMessageOnToastList($translate.instant('clientwh_home_fail'));
						}
					},
					// error.
					function (response) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
					});
			}

			// Clear search
			$scope.clearSearch = function () {
				$scope.search = {};
				$scope.listWithCriteriasByPage(1);
			}

			// Clear search
			$scope.clearSearchMaterial = function () {
				$scope.searchMaterialStore = {};
				$scope.listWithCriteriasByIdStoreAndPage($scope.idstore, 1);
			}

			// List for page and filter.
			$scope.listWithCriteriasByIdStoreAndPage = function (idstore, pageNo) {
				$scope.pageStore.currentPage = pageNo;
				materialstoreService.listWithCriteriasByIdStoreAndPage(idstore, $scope.getSearchMaterialStore(), pageNo - 1, $scope.pageStore.pageSize).then(
					// success.
					function (response) {
						if (response.status === httpStatus.code.OK) {
							$scope.materialstores = [];
							$scope.pageStore.totalElements = 0;
							if (response.data.content && response.data.content.length > 0) {
								var result = angular.fromJson(response.data.content);
								$scope.materialstores = result;
								if (result.length > 0) {
									$scope.pageStore.totalElements = response.data.totalElements;
								}
							}
						} else {
							$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
						}
					},
					// error.
					function (response) {
						$scope.showMessageOnToastList($translate.instant('clientwh_home_error'));
					});
			}

			// Sort by.
			$scope.sortBy = function (keyName) {
				$scope.sortKey = keyName;
				$scope.reverse = !$scope.reverse;
				// Reload data.
				$scope.listWithCriteriasByPage($scope.page.currentPage);
			}

			$scope.clearSortBy = function () {
				$scope.sortKey = '';
				$scope.sortName = null;
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
					result.push(
						//{ key: 'idmanager', operation: 'like', value: $scope.search.content, logic: 'or' },
						{ key: 'code', operation: 'like', value: $scope.search.code, logic: 'or' },
						{ key: 'name', operation: 'like', value: $scope.search.code, logic: 'or' }
					);
				}
				// return.
				return result;
			}

			////////////////////////////////////////
			// Auto complete: idmanager
			////////////////////////////////////////
			$scope.ctlentityfieldManager = {};
			$scope.ctlentityfieldManager.isCallServer = false;
			$scope.ctlentityfieldManager.isDisabled = false;

			// New.
			$scope.ctlentityfieldManager.newState = function (item) {
				alert("Sorry! You'll need to create a Constitution for " + item + " first!");
			}
			// Search in array.
			$scope.ctlentityfieldManager.querySearch = function (query) {
				var results = query ? $scope.ctlentityfieldManager.items.filter($scope.ctlentityfieldManager.createFilterFor(query)) : $scope.ctlentityfieldManager.items,
					deferred;

				if ($scope.ctlentityfieldManager.isCallServer) {
					deferred = $q.defer();
					$timeout(function () { deferred.resolve(results); }, Math.random() * 1000, false);
					return deferred.promise;
				} else {
					return results;
				}
			}
			// Filter.
			$scope.ctlentityfieldManager.createFilterFor = function (query) {
				var lowercaseQuery = angular.lowercase(query);
				return function filterFn(item) {
					return (angular.lowercase(item.display).indexOf(lowercaseQuery) >= 0);
				};
			}
			// Text change.
			$scope.ctlentityfieldManager.searchTextChange = function (text) {
				$log.info('Text changed to ' + text);
			}
			// Item change.
			$scope.ctlentityfieldManager.selectedItemChange = function (item) {
				if (typeof (frmStore) === 'undefined' || frmStore.idmanager === undefined) {
					return;
				}
				$scope.store.idmanager = undefined;
				$scope.frmStore.idmanager.$invalid = true;
				if (item) {
					$scope.store.idmanager = item.id;
					$scope.frmStore.idmanager.$invalid = false;
				}
			}

			// Sort by.
			$scope.sortByMaterialStore = function (keyName) {
				$scope.sortKeyMaterialStore = keyName;
				$scope.reverseMaterialStore = !$scope.reverseMaterialStore;
				// Reload data.
				$scope.listWithCriteriasByIdStoreAndPage($scope.idstore, $scope.pageStore.currentPage);
			}

			// Get sort object.
			$scope.getSortMaterialStore = function () {
				var result = [];
				// name.
				if (typeof ($scope.sortKeyMaterialStore) !== 'undefined' && $scope.sortKeyMaterialStore !== '') {
					var order = 'desc';
					if ($scope.reverseMaterialStore) {
						order = 'asc';
					}
					result.push('sort=' + $scope.sortKeyMaterialStore + ',' + order);
				}
				// return.
				return result;
			}

			// Get search object.
			$scope.getSearchMaterialStore = function () {
				var result = [];
				// code.
				if (typeof ($scope.searchMaterialStore.content) !== 'undefined' && $scope.searchMaterialStore.content !== '') {
					result.push({ key: 'material.name', operation: 'like', value: $scope.searchMaterialStore.content, logic: 'or' });
					if (parseInt($scope.searchMaterialStore.content)) {
						result.push({ key: 'quantity', operation: '=', value: $scope.searchMaterialStore.content, logic: 'or' });
					}
				}
				// return.
				return result;
			}

		}]);
});
