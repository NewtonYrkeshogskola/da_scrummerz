var app = angular.module('LoggedIn', ['firebase', 'ngAnimate', "ja.qr", 'chart.js', 'zingchart-angularjs']);
var grades = [];
var globalCount = 0;

app.factory("Auth", ["$firebaseAuth",
    function ($firebaseAuth) {
        return $firebaseAuth();
    }
]);

app.controller("AdminUserCtrl", ["$scope", "$firebaseObject", "$firebaseArray", '$filter', "Auth",

    function ($scope, $firebaseObject, $firebaseArray, $filter, Auth) {
        // Top level variables
        $scope.auth = Auth;
        var ref = firebase.database().ref();
        var userId;
        var firebaseUser;
        var user;

        $scope.auth.$onAuthStateChanged(function (firebaseUser) {
            userId = firebaseUser.uid;
            $scope.firebaseUser = firebaseUser;
            firebaseUser = firebaseUser;
            $scope.user = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            user = $scope.user;

            $scope.user.$loaded().then(function () {
                $scope.myClass = $scope.user.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + $scope.myClass));
            })

        });


        $scope.createUser = function () {
            $scope.message = null;
            $scope.error = null;
            // Create a new user
            Auth.$createUserWithEmailAndPassword($scope.email, $scope.password)
                .then(function (firebaseUser) {
                    $scope.message = "Användare skapad med UID: " + firebaseUser.uid;
                }).catch(function (error) {
                    $scope.error = error;
                });
        };

        $scope.deleteUser = function () {
            $scope.message = null;
            $scope.error = null;
            // Delete the currently signed-in user
            Auth.$deleteUser().then(function () {
                $scope.message = "Användare raderad";
            }).catch(function (error) {
                $scope.error = error;
            });
        };

        // Get date for QR code
        $scope.date = new Date();
        $scope.myDate = new Date($scope.date.getFullYear(),
            $scope.date.getMonth(),
            $scope.date.getDate());
        $scope.myDate = $filter('date')($scope.myDate, 'yyyyMMdd');
        var date = $scope.myDate;

        // get a random code for the QR
        $scope.random = Math.floor((Math.random() * 10000) + 1);

        $scope.selectedCourse = "NONE"

        //generate QR code
        $scope.string = null;

        $scope.updateString = function () {
            $scope.random = Math.floor((Math.random() * 10000) + 10000);
            $scope.string = $scope.selectedCourse + "/" + date + "/" + $scope.random;
        }

        $scope.activatePresence = function (presenceName) {
            var localDate = new Date();
            $scope.time = localDate.getHours() + ":" + localDate.getMinutes();
            firebase.database().ref().child('coursesByClass').child($scope.selectedClass).child($scope.selectedCourse).child(date).child($scope.random).update({
                active: true,
                created: $scope.time,
                name: presenceName
            });
            alert("Nu är närvaron aktiverad. Du kan nu registera dig som närvarande.")

            var data = [23, 42]; //antal inloggade studenter



            $scope.studentCount = 0;
            var checkedStudents = firebase.database().ref().child('coursesByClass').child($scope.myClass).child($scope.selectedCourse).child(date).child($scope.random).child("students")
            checkedStudents.on('value', function (snapshot) {
                $scope.studentsLoggedIn = snapshot.val();
            });
            checkedStudents.on('child_added', function (snapshot) {
                $scope.studentCount++;
                globalCount = $scope.studentCount;
                $scope.$broadcast('UpdateCount');
            });

            $scope.myJson = {
                type: "bar",
                title: {
                    backgroundColor: "transparent",
                    fontColor: "black",
                    text: "Hello world"
                },
                backgroundColor: "white",
                series: [{
                    values: [1, 2, 3, 4],
                    backgroundColor: "#4DC0CF"
                }]
            };

            $scope.addValues = function () {
                var val = Math.floor((Math.random() * 10));
                console.log(val);
                $scope.myJson.series[0].values.push(val);
            }



        }
        $scope.deActivatePresence = function () {

            firebase.database().ref().child('coursesByClass').child($scope.myClass).child($scope.selectedCourse).child(date).child($scope.random).update({
                active: false
            });
            alert("Närvaron är nu stängd för nya registreringar")
        }
    }
]);

app.controller("DoughnutCtrl", function ($scope, $filter) {
    var ref = firebase.database().ref();
    $scope.today = new Date();
    $scope.myDate = new Date($scope.today.getFullYear(),
        $scope.today.getMonth(),
        $scope.today.getDate());
    $scope.myDate = $filter('date')($scope.myDate, 'yyyyMMdd');
    var date = $scope.myDate;
    $scope.ones = 0;
    $scope.zeros = 0;
    $scope.minusOnes = 0;
    ref.child('feelings').child('APPS1').child(date).on('value', function (snapshot) {
        snapshot.forEach(function (childSnapshot) {
            var feelingData = childSnapshot.val();
            if (feelingData === 1) {
                $scope.ones++
            } else if (feelingData === 0) {
                $scope.zeros++
            } else {
                $scope.minusOnes++
            }
            $scope.labels = ["GOOD", "NEUTRAL", "BAD"];
            $scope.data = [$scope.ones, $scope.zeros, $scope.minusOnes];
        });
    });
});

app.controller('MainController', function ($scope, $timeout) {

    $scope.pupilCount = globalCount;

    $scope.$on('UpdateCount', function (event) {
        $scope.pupilCount = globalCount;
    });

    // mimick your promise
    (function () {
        $scope.data = {};
        $scope.data.valuesOne = [$scope.pupilCount];
        $scope.aValues = [$scope.data.valuesOne, $scope.data.valuesTwo];
    })();

    $scope.myJson = {
        type: "bar",
        title: {
            backgroundColor: "transparent",
            fontColor: "black",
            text: "Närvarande elever"
        },
        backgroundColor: "white",
        series: [{
            backgroundColor: '#00baf2'
        }]
    };
    

    // // automatically wraps code in $apply
    // $timeout(function () {

    //     // wont reflect changes in aValues because its an object
    //     $scope.data.valuesOne = [$scope.pupilCount];

    //     // must force the reflection of the changes
    //     $scope.aValues = [$scope.data.valuesOne, $scope.data.valuesTwo];

    //     // will reflect changes with one line, not the above two lines
    //     //$scope.aValues[0] = [5];

    // }, 1500);

});