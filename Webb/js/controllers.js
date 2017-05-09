(function(){

    angular
        .module('LoggedIn', ['firebase'])
        .controller('personCtrl', ["$scope", "$firebaseObject",
        function($scope, $firebaseObject){
            var ref = firebase.database().ref();
            $scope.data = $firebaseObject(ref.child('courses').child('2017'));
            
        }]);
}());