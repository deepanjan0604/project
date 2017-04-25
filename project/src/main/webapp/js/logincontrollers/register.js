tmsApp
		.controller(
				"registercontroller",
				function($scope, $rootScope, $http, $timeout, $log, ngToast, toaster) {
					$rootScope.isAuthenticated = false;
					$rootScope.isActivatedUser = true;
					$scope.title = "Sign up";

					

					$scope.passwordDoNotMatchMsg = "";

					$scope.registeradmin = function() {
						$scope.userdata = {
								user:$scope.user,
								tenant:$scope.tenant
						};

						$http.post('/public/tenantregistration',
								$scope.userdata).success(
								function(response) {
									
									
									
									$timeout(function() {
										location.href = '#/users/tenant/'
												+ response.domain
									}, 500);
								}).error(function(err) {
									
									toaster.method("danger", err.message,
											ngToast);
							$log.info(err);
						})
								
					}
					
					$scope.isFormInvalid = function() {

						return $scope.confirmPassword == undefined
								|| $scope.user.password == undefined
								|| $scope.user.email == undefined
								|| $scope.user.name == undefined
								|| $scope.user.username == undefined
								|| $scope.tenant.tenantName == undefined
								|| $scope.tenant.domain == undefined
								|| $scope.matchPassword();
					}

					$scope.matchPassword = function() {
						if (!$scope.user.password)
							return '';
						if (!$scope.confirmPassword)
							return '';

						if ($scope.user.password != $scope.confirmPassword
								&& ($scope.user.password != '' && $scope.confirmPassword != '')) {
							$scope.passwordDoNotMatchMsg = "Does not match with the password";
							return 'has-error has-feedback';
						} else
							return '';
					}
				})