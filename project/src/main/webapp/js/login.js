/**
 * 
 * 
 * @author darkhorseboa
 */

angular
		.module('tmsApp')
		.controller(
				'loginController',
				[
						'$scope',
						'$rootScope',
						'$http',
						'$location',
						function($scope, $rootScope, $http, $location) {

							$rootScope.isSuccess = true;
							$scope.title = "Login Page";
							$scope.users = {};
							$scope.login = function() {
								var authData = $scope.users.username + ':'
										+ $scope.users.password;
								var encodedAuthData = btoa(authData);
								headers = {
									'Authorization' : 'Basic '
											+ encodedAuthData
								}
								$http({
									method : 'GET',
									url : '/public/login',
									headers : headers
								})
										.then(
												function(response) {
													$rootScope.response = response.data;
													if (response.status===200) {
														$rootScope.isAuthenticated = true;
														if ($rootScope.response.roles[0].roleType == "admin") {

															$rootScope.admin = true;
															$rootScope.user = false;
															$rootScope.sa = false;
															$rootScope
																	.getProjects();
															location.href = '#/admin';

														} else if ($rootScope.response.roles[0].roleType == "user") {

															$rootScope.user = true;
															$rootScope.admin = false;
															$rootScope.sa = false;
															$rootScope
																	.getProjects();
															location.href = '#/task';

														} else if ($rootScope.response.roles[0].roleType == "sa") {

															$rootScope.sa = true;
															$rootScope.user = false;
															$rootScope.admin = false;
															location.href = '#/viewtenant';

														}
														$scope.reset();
													}

												}, function() {
													$scope.error = true;
												})

							}

							$scope.changeState = function() {
								$scope.error = false;
							}

							$scope.reset = function() {
								$scope.users.username = "";
								$scope.users.password = "";
							}
						} ]);