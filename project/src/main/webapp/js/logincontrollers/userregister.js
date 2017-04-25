tmsApp
	.controller(
				'userregistercontroller',
				function($rootScope, $scope, $http, $timeout, $routeParams,
						$log, toaster, ngToast) {
					$rootScope.isAuthenticated = false;
					$rootScope.isActivatedUser = true;
					$scope.title = "New User Register";
					$scope.userdata = {};

					$scope.passwordDoNotMatchMsg = "";
					$http.get(
							'/public/tenant/domain?tenantName='
									+ $routeParams.tenantname).success(
							function(response) {

								$scope.domain = "@"+response.domain;
							}).error(function(err) {
						$log.info(err);
					})
					$scope.registeruser = function() {
						
						if($scope.userdata.username.includes("@")){
              			  var username=$scope.userdata.username.split("@");
              			  $scope.userdata.username=username[0];
              		   }
						$http
								.post('/public/userregistration?domain='+$routeParams.tenantname,
										$scope.userdata)
								.success(
										function(response) {
											
										
												$timeout(function() {
													location.href = "/";
												}, 1000);
											
										}).error(function(err) {
											toaster
												.method(
														"danger",
														err.message,
														ngToast);
									$log.info(err);
								})
					}
					$scope.checkForEmail = function() {
						$http
								.post(
										'/public/checkforemail?tenant='
												+ $routeParams.tenantname,
										$scope.userdata)
								.success(
										function(response) {
											
											$scope.mail = response.message
											if (response.status=="false") {
												$scope.icnClass="glyphicon glyphicon-remove";
												$scope.mail = response.message
											} else {
												$scope.icnClass="glyphicon glyphicon-ok";
												$scope.mail = response.message
											}
											
										}).error(function() {

								})
					}
					$scope.isFormInvalid = function() {

						return $scope.confirmPassword == undefined
								|| $scope.userdata.password == undefined
								|| $scope.userdata.email == undefined
								|| $scope.userdata.name == undefined
								|| $scope.userdata.username == undefined
								|| $scope.matchPassword();
					}

					$scope.matchPassword = function() {
						if (!$scope.userdata.password)
							return '';
						if (!$scope.confirmPassword)
							return '';

						if ($scope.userdata.password != $scope.confirmPassword
								&& ($scope.userdata.password != '' && $scope.confirmPassword != '')) {
							$scope.passwordDoNotMatchMsg = "Does not match with the password";
							return 'has-error has-feedback';
						} else
							return '';
					}
				})