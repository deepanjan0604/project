

tmsApp.controller('userscontroller', [
                                   '$scope',
                                   '$rootScope',
                                   '$http',
                                   'tmsutility',
                                   'getusersservice',
                                   'toaster',
                                   'ngToast','$log','$timeout',
                                   function($scope, $rootScope, $http, tmsutility, getusersservice,
                                		   toaster, ngToast,$log,$timeout) {

                                	   tmsutility.method($scope);


                                	   $scope.addUser = function() {
                                		   if($scope.userdata.username.includes("@")){
                                			  var username=$scope.userdata.username.split("@");
                                			  $scope.userdata.username=username[0];
                                		   }
                                		   $http
                                		   .post('/admin/adduser', $scope.userdata)
                                		   .success(
                                				   
                                				   function(response) {
	
   													if (response.error!=null) {
   													 $scope.useralerticon = "glyphicon glyphicon-remove";
														$scope.alertmessage=response.error;
														$scope.alertClass="erroralert";
   														$scope.getUsers();
   													}  else {
   														$scope.resetAddingUserForm()
   														toaster
														.method(
																"success",
																"user added successfully",
																ngToast);
   														$scope.getUsers();
   													}
   												})
                                				   .error(
                                						   function(err) {
                                							   $log.info(err);
                                							   toaster
       														.method(
       																"danger",
       																err.message,
       																ngToast);
                                						   })
                                	   
                                		   
                                	   }

                                	   	$scope.resetAddingUserForm=function(){
                                	   		
                                	   		$scope.userdata="";
                     					   $scope.icnClass = "";
												$scope.mail = "";
												 $scope.mailalerticon=""
													 $scope.useralerticon = "";
													$scope.alertmessage="";
                                	   	}
                                	   	


                                	   $scope.checkForEmail = function(email) {
                                		 
                                		   if(email){
                                			  
                                		  $scope.user={
                                				  email:email
                                		  }
                                		  $http.post('/public/checkforemail',$scope.user)
                                		   .success(
                                				   function(response) {
                                					   
                                					   $scope.mail = response.message
                                					   if (response.status == "false") {
                                						   $scope.mailalerticon = "glyphicon glyphicon-remove";
                                						   $scope.mailalertClass="erroralert";
                                						   $scope.validEmail=true; 
                                						   $scope.mail = response.message
                                					   } else {
                                						   $scope.mailalerticon = "glyphicon glyphicon-ok";
                                						   $scope.mailalertClass="successalert";
                                						   $scope.mail = response.message
                                						   $scope.validEmail=false;
                                					   }
                                				   }).error(function() {

                                				   })
                                		   }
                                	   }

                                	 

                                
                                	   $scope.deactivate = function(userid) {

                                		   $http.post("/admin/deactivateuser", userid).success(
                                				   function() {
                                					   $scope.getUsers();
                                				   }).error(function(err) {
                                					   $log.info(err);
                                				   })

                                	   }

                                	   $http({
                                		   method : 'GET',
                                		   url : '/admin/getRoles'

                                	   }).then(function(response) {
                                		   $rootScope.userRoles = response.data;
                                	   })

                                	   $scope.edit = function(index) {

                                		   $scope.editedUser = angular.copy($scope.userList[index]);

                                	   }

                                	   $scope.updateUser = function() {

                                		   $scope.user = {
                                				   id : $scope.editedUser.data.id,
                                				   name : $scope.editedUser.data.name,
                                				   email : $scope.editedUser.data.email,
                                				   username : $scope.editedUser.data.username,
                                				   roles : [ {
                                					   roleType : $scope.editedUser.role.roleType
                                				   } ]
                                		   }

                                		   $http.post('/admin/updateUser', $scope.user).success(
                                				   function() {
                                					   
                                					   $scope.createuserdivn=true;
                                					   $scope.getUsers();

                                					   toaster.method('success', 'Well Done! Successfully saved',
                                							   ngToast);

                                				   }).error(function(err) {
                                					   toaster.method('danger', 'Error!!', ngToast)
                                				   })
                                	   }

                                	   $scope.getUsers = function() {
                                		   $http.get('/all/getUsers?value=').success(function(res) {
                                			  
                                			   $scope.userList = res.map(function(value) {
                                				   return {
                                					   data : value,
                                					   role : value.roles[0]
                                				   }
                                			   });

                                		   }).error(function(error) {
                                			   $log.info(error);
                                		   })
                                	   }

                                	   $scope.getUsers();
                                	   $scope.searchText = "";

                                	   $scope.loadUsers = function() {

                                		   $http.get(
                                				   '/all/getUsers?value='
                                				   + $scope.searchText).success(
                                						   function(response) {
                                							   $scope.userList = response.map(function(value) {
                                								   return {
                                									   data : value,
                                									   role : value.roles[0]
                                								   }
                                							   });

                                						   }).error(function(err) {
                                							   $log.info(err);
                                						   });
                                	   }

                                	   $scope.activate = function(userid) {
                                		   $http.post('/admin/activateuser', userid)
                                		   .success(function() {
                                			   $scope.getUsers();
                                		   }).error(function(err) {
                                			   $log.info(err);

                                		   })

                                	   }

                                	   $scope.resendActivationLink = function(id) {

                                		   $http.post('/admin/resend/act?user=' + id)
                                		   .success(function(response) {

                                			   toaster
                                			   .method(
                                					   response.status,
                                					   response.message,
                                					   ngToast);
											$scope.getUsers();

                                		   }).error(function(error) {
                                			   
                                			   toaster
                                			   .method(
                                					   "danger",
                                					   error.message,
                                					   ngToast);
                                		   })
                                	   }

                                	   $scope.resetPassword = function(id) {
                                		   $http.post('/admin/reset/pass?user=' + id)
                                		   .success(function(response) {
                                			   toaster
                                			   .method(
                                					   response.status,
                                					   response.message,
                                					   ngToast);
                                			   $scope.getUsers();
                                		   }).error(function(error) {
                                			   toaster
                                			   .method(
                                					   "danger",
                                					   error.message,
                                					   ngToast);
                                		   })
							                                	   }

						} ]);
