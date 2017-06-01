var app = angular.module('LoggedIn', ['firebase', 'ngAnimate', 'schemaForm', 'chart.js']);
var grades = [];


app.factory("Auth", ["$firebaseAuth",
    function ($firebaseAuth) {
        return $firebaseAuth();
    }
]);

app.controller('personCtrl', ["$scope", "$firebaseObject", "$firebaseArray", '$filter', "Auth",

    function ($scope, $firebaseObject, $firebaseArray, $filter, Auth) {
        var ref = firebase.database().ref();

        // Top level variables
        $scope.auth = Auth;
        $scope.globalGrades = [];
        $scope.globalAssignments = [];
        $scope.globalCourseNews = [];


        $scope.auth.$onAuthStateChanged(function (firebaseUser) {
            var userId = firebaseUser.uid;
            $scope.firebaseUser = firebaseUser;
            $scope.globalActiveAssignments = [];
            $scope.user = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            $scope.userId = firebaseUser.uid;
            $scope.user.$loaded().then(function () {

                // Get my class from scope and use it to get courses
                $scope.myClass = $scope.user.myClass;
                var klass = $scope.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + klass));
                $scope.myGrades = $firebaseArray(ref.child('users').child('students/' + userId).child('grades').child('courses'));

                // Get date for feelings
                $scope.date = new Date();
                $scope.myDate = new Date($scope.date.getFullYear(),
                    $scope.date.getMonth(),
                    $scope.date.getDate());
                $scope.myDate = $filter('date')($scope.myDate, 'yyyyMMdd');
                var date = $scope.myDate;

                // Register feelings to Firebase
                $scope.setFeeling = function (feeling) {
                    firebase.database().ref().child('feelings').child(klass).child(date).update({
                        [userId]: feeling
                    });
                }

                // Get the latest user's feeling
                ref.child('feelings').child(klass).child(date).on('value', function (snapshot) {
                    $scope.myLatestFeeling = [];
                    snapshot.forEach(function (childSnapshot) {
                        var childKey = childSnapshot.key;
                        var myFeelingsValue = childSnapshot.val();
                        $scope.myLatestFeeling.push({
                            key: childKey,
                            grade: myFeelingsValue
                        })

                    })
                });

                //function which gets path to database from input string (närvaro kod) and stores value to the db
                //attendance code should have following structure: courseCode/date(format:yyyymmdd)/code, ex: YAPP-APP/20170523/randomnumber
                //function checks if the last part of the code ('code') exists and if the user already reported his/her attendance
                $scope.attendance = function (courseAttended, attendanceCode) {

                    var pathToInfo = firebase.database().ref().child('coursesByClass').child(klass).child(courseAttended).child(date).child(attendanceCode);
                    var pathToCode = firebase.database().ref().child('coursesByClass').child(klass).child(courseAttended).child(date);
                    pathToCode.once('value', function (snapshot) {
                        if (snapshot.hasChild(attendanceCode)) {

                            pathToInfo.once('value', function (childSnapshot) {
                                var statusValue = childSnapshot.child("active").val();
                                if (statusValue === true) {
                                    if (childSnapshot.child("students").hasChild(userId)) {
                                        alert("Du har redan anmält din närvaro. Tack!");
                                    }
                                    else {
                                        firebase.database().ref().child('coursesByClass').child(klass).child(courseAttended).child(date).child(attendanceCode).child("students").update({
                                            [userId]: $scope.user.Name
                                        });
                                        alert('Din närvaro är registrerad. Tack!')
                                    }
                                }
                                else {
                                    alert('Tillfället är inte längre aktivt');
                                }

                            });
                        }
                        else {
                            alert('Fel kod. Kontrollera koden och försök igen')
                        }
                    })
                }

                // Loop through all active assignments under personal node and push to globalAssignments
                ref.child('coursesByClass').child(klass).once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var childKey
                        var childData
                        var courseKey = childSnapshot.key;

                        ref.child('coursesByClass').child(klass).child(courseKey).child('assignments').once('value', function (snapshot) {
                            snapshot.forEach(function (childSnapshot) {
                                childKey = childSnapshot.key;
                                childData = childSnapshot.val();

                                // For each child under each 
                                $scope.globalActiveAssignments.push({
                                    key: courseKey,
                                    childKey: childKey,
                                    assignment: childData
                                })
                            })
                        });
                    });
                });
                $scope.classNews = $firebaseObject(ref.child('newsByClass/' + klass));
                $scope.generalNews = $firebaseObject(ref.child('generalNews'));

                ref.child('coursesByClass').child(klass).once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var childKey
                        var childData
                        var courseKey = childSnapshot.key;

                        ref.child('coursesByClass').child(klass).child(courseKey).child('news').once('value', function (snapshot) {
                            snapshot.forEach(function (childSnapshot) {
                                childKey = childSnapshot.key;
                                childData = childSnapshot.val();

                                // For each child under each 
                                $scope.globalCourseNews.push({
                                    key: courseKey,
                                    childKey: childKey,
                                    news: childData
                                })
                            })
                        });
                    });
                });
            });
        });
    }
]);


app.controller("gradesCtrl", ["$scope", "$firebaseObject", "$firebaseArray", '$filter', "Auth",
    function ($scope, $firebaseObject, $firebaseArray, $filter, Auth) {

        // Top level variables
        var ref = firebase.database().ref();
        var userId;
        $scope.auth = Auth;


        $scope.auth.$onAuthStateChanged(function (firebaseUser) {
            userId = firebaseUser.uid;
            $scope.firebaseUser = firebaseUser;
            $scope.user = $firebaseObject(ref.child('users').child('students/' + userId).child('details'));
            $scope.globalGrades = [];
            $scope.globalAssignments = [];
            $scope.finishedNotRated = [];
            $scope.notRatedWeekly = [];

            // This will be executed after the user has been loaded
            $scope.user.$loaded().then(function () {

                // Get my class from scope and use it to get courses
                $scope.myClass = $scope.user.myClass;
                var klass = $scope.myClass;
                $scope.myCourses = $firebaseObject(ref.child('coursesByClass/' + klass));
                $scope.myGrades = $firebaseArray(ref.child('users').child('students/' + userId).child('grades').child('courses'));

                // Loop through all grades under personal node and push to globalGrades
                ref.child('grades').child(userId).child('final').once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var childKey = childSnapshot.key;
                        var childData = childSnapshot.val();
                        $scope.globalGrades.push({
                            key: childKey,
                            grade: childData
                        })
                    });
                });

                // Loop through all assignments under personal node and push to globalAssignments
                ref.child('grades').child(userId).child('assignments').once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var childKey
                        var childData
                        var courseKey = childSnapshot.key;

                        ref.child('grades').child(userId).child('assignments').child(courseKey).once('value', function (snapshot) {
                            snapshot.forEach(function (childSnapshot) {
                                childKey = childSnapshot.key;
                                childData = childSnapshot.val();

                                // For each child under each 
                                $scope.globalAssignments.push({
                                    key: courseKey,
                                    childKey: childKey,
                                    grade: childData
                                })
                            })
                        });
                    });
                });

                //kontrollera att kursen är avslutad och att studenten inte lämnat sin feedback. skapa array av avslutade kurser utan feedback
                ref.child('coursesByClass').child(klass).once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var isFinished = childSnapshot.child("details").child("status").val();
                        var userExists = childSnapshot.child("feedback").child('studentsVoted').child(userId).val();
                        if (isFinished === 'finished' && userExists === null) {
                            var childKey = childSnapshot.key;
                            //console.log(childSnapshot.key);
                            $scope.finishedNotRated.push({
                                key: childKey
                            })
                            console.log($scope.finishedNotRated + ' hej');
                        }
                    });
                });
                $scope.schema = {
                    "type": "object",
                    "properties": {
                        "radios": {
                            "title": "Allmänt sett, vilket är ditt omdöme om kursen?*",
                            "type": "number",
                            "enum": [
                                1,
                                2,
                                3,
                                4,
                                5
                            ]
                        },
                        "radios2": {
                            "title": "Hur tycker du att kursens innehåll överensstämmer med lärandemålen?*",
                            "type": "number",
                            "enum": [
                                1,
                                2,
                                3,
                                4,
                                5
                            ]
                        },
                        "radios3": {
                            "title": "Vad tycker du om kurslitteraturen?*",
                            "type": "number",
                            "enum": [
                                1,
                                2,
                                3,
                                4,
                                5
                            ]
                        },
                        "radios4": {
                            "title": "Kursen har givit mig god översikt av området*",
                            "type": "number",
                            "enum": [
                                1,
                                2,
                                3,
                                4,
                                5
                            ]
                        },
                        "radios5": {
                            "title": "Kursen har fördjupat min förståelse för området*",
                            "type": "number",
                            "enum": [
                                1,
                                2,
                                3,
                                4,
                                5
                            ]
                        },
                        "radios6": {
                            "title": "Kursen har hjälpt (kommer att hjälpa) mig lösa problem inom området*",
                            "type": "number",
                            "enum": [
                                1,
                                2,
                                3,
                                4,
                                5
                            ]
                        },
                        "comment1": {
                            "title": "Ge förslag till moment i kursen som behöver förbättras och på vilket sätt*",
                            "type": "string",
                            "minLength": 5
                        },
                        "comment2": {
                            "title": "Vilka moment i kursen är OK som de är och berätta gärna varför?*",
                            "type": "string",
                            "minLength": 5
                        },
                        "comment3": {
                            "title": "Övriga kommentarer och /eller synpunkter:*",
                            "type": "string",
                            "minLength": 5
                        }
                    },
                    "required": [
                        "radios",
                        "radios2",
                        "radios3",
                        "radios4",
                        "radios5",
                        "radios6",
                        "comment1",
                        "comment2",
                        "comment3"
                    ]
                };

                $scope.form = [
                    {
                        "key": "radios",
                        "type": "radios-inline",
                        "titleMap": [
                            {
                                "value": 1,
                                "name": 1
                            },
                            {
                                "value": 2,
                                "name": 2
                            },
                            {
                                "value": 3,
                                "name": 3
                            },
                            {
                                "value": 4,
                                "name": 4
                            },
                            {
                                "value": 5,
                                "name": 5
                            },

                        ]
                    },
                    {
                        "key": "radios2",
                        "type": "radios-inline",
                        "titleMap": [
                            {
                                "value": 1,
                                "name": 1
                            },
                            {
                                "value": 2,
                                "name": 2
                            },
                            {
                                "value": 3,
                                "name": 3
                            },
                            {
                                "value": 4,
                                "name": 4
                            },
                            {
                                "value": 5,
                                "name": 5
                            }

                        ]
                    },
                    {
                        "key": "radios3",
                        "type": "radios-inline",
                        "titleMap": [
                            {
                                "value": 1,
                                "name": 1
                            },
                            {
                                "value": 2,
                                "name": 2
                            },
                            {
                                "value": 3,
                                "name": 3
                            },
                            {
                                "value": 4,
                                "name": 4
                            },
                            {
                                "value": 5,
                                "name": 5
                            },

                        ]
                    },
                    {
                        "key": "radios4",
                        "type": "radios-inline",
                        "titleMap": [
                            {
                                "value": 1,
                                "name": 1
                            },
                            {
                                "value": 2,
                                "name": 2
                            },
                            {
                                "value": 3,
                                "name": 3
                            },
                            {
                                "value": 4,
                                "name": 4
                            },
                            {
                                "value": 5,
                                "name": 5
                            }

                        ]
                    },
                    {
                        "key": "radios5",
                        "type": "radios-inline",
                        "titleMap": [
                            {
                                "value": 1,
                                "name": 1
                            },
                            {
                                "value": 2,
                                "name": 2
                            },
                            {
                                "value": 3,
                                "name": 3
                            },
                            {
                                "value": 4,
                                "name": 4
                            },
                            {
                                "value": 5,
                                "name": 5
                            },

                        ]
                    },
                    {
                        "key": "radios6",
                        "type": "radios-inline",
                        "titleMap": [
                            {
                                "value": 1,
                                "name": 1
                            },
                            {
                                "value": 2,
                                "name": 2
                            },
                            {
                                "value": 3,
                                "name": 3
                            },
                            {
                                "value": 4,
                                "name": 4
                            },
                            {
                                "value": 5,
                                "name": 5
                            }

                        ]
                    },
                    {
                        "key": "comment1",
                        "type": "textarea"
                    },
                    {
                        "key": "comment2",
                        "type": "textarea"
                    },
                    {
                        "key": "comment3",
                        "type": "textarea"
                    },
                    {
                        "type": "submit",
                        "style": "btn-info",
                        "title": "Ge feedback"
                    }
                ];

                $scope.model = {};
                $scope.onSubmit = function (finishedCourseFeedback, form) {
                    location.reload();
                    // First we broadcast an event so all fields validate themselves
                    $scope.$broadcast('schemaFormValidate');

                    // Then we check if the form is valid
                    if (form.$valid) {
                        var radio1 = $scope.model.radios;
                        var radio2 = $scope.model.radios2;
                        var radio3 = $scope.model.radios3;
                        var radio4 = $scope.model.radios4;
                        var radio5 = $scope.model.radios5;
                        var radio6 = $scope.model.radios6;
                        var comment1 = $scope.model.comment1;
                        var comment2 = $scope.model.comment2;
                        var comment3 = $scope.model.comment3;
                        firebase.database().ref().child('coursesByClass').child(klass).child(finishedCourseFeedback).child('feedback').push({
                            radio1,
                            radio2,
                            radio3,
                            radio4,
                            radio5,
                            radio6,
                            comment1,
                            comment2,
                            comment3
                        });
                        firebase.database().ref().child('coursesByClass').child(klass).child(finishedCourseFeedback).child('feedback').child('studentsVoted').update({
                            [userId]: true
                        });
                    }
                }


                //kontrollera att kursen är pågående och att studenten inte lämnat sin feedback. skapa array av avslutade kurser utan feedback
                ref.child('coursesByClass').child(klass).once('value', function (snapshot) {
                    snapshot.forEach(function (childSnapshot) {
                        var isFinished = childSnapshot.child("details").child("status").val();
                        var courseKey = childSnapshot.key;
                        if (isFinished === 'progress') {
                            ref.child('coursesByClass').child(klass).child(courseKey).child('weeklyFeedback').once('value', function (snapshot) {
                                snapshot.forEach(function (childSnapshot) {
                                    var feedbackKey = childSnapshot.child('active').val();
                                    var studentKey = childSnapshot.child('studentsVoted').child(userId).val();
                                    if (feedbackKey === true && studentKey === null) {
                                        var childKey = childSnapshot.key;
                                        $scope.notRatedWeekly.push({
                                            key: courseKey,
                                            week: childKey
                                        })
                                    }
                                });
                            });
                        }
                    });
                });
                $scope.schemaWeekly = {
                    "type": "object",
                    "properties": {

                        "commentw1": {
                            "title": "Ge förslag till moment i kursen som behöver förbättras och på vilket sätt*",
                            "type": "string",
                            "minLength": 5
                        },
                        "commentw2": {
                            "title": "Vilka moment i kursen är OK som de är och berätta gärna varför?*",
                            "type": "string",
                            "minLength": 5
                        },
                        "commentw3": {
                            "title": "Övriga kommentarer och /eller synpunkter:*",
                            "type": "string",
                            "minLength": 5
                        }
                    },
                    "required": [
                        "commentw1",
                        "commentw2",
                        "commentw3"
                    ]
                };

                $scope.formWeekly = [
                    {
                        "key": "commentw1",
                        "type": "textarea"
                    },
                    {
                        "key": "commentw2",
                        "type": "textarea"
                    },
                    {
                        "key": "commentw3",
                        "type": "textarea"
                    },
                    {
                        "type": "submit",
                        "style": "btn-info",
                        "title": "Ge feedback"
                    }
                ];

                $scope.modelWeekly = {};
                $scope.onSubmitWeekly = function (activeCourseWeeklyFeedback, formWeekly) {
                    var courseArray = activeCourseWeeklyFeedback.split(' ');
                    var course = courseArray[0];
                    var week = courseArray[1];
                    console.log(course + ' ' + week);
                    location.reload();
                    // First we broadcast an event so all fields validate themselves
                    $scope.$broadcast('schemaFormValidate');

                    // Then we check if the form is valid
                    if (formWeekly.$valid) {
                        var commentw1 = $scope.modelWeekly.commentw1;
                        var commentw2 = $scope.modelWeekly.commentw2;
                        var commentw3 = $scope.modelWeekly.commentw3;

                        firebase.database().ref().child('coursesByClass').child(klass).child(course).child('weeklyFeedback').child(week).push({
                            commentw1,
                            commentw2,
                            commentw3
                        });
                        firebase.database().ref().child('coursesByClass').child(klass).child(course).child('weeklyFeedback').child(week).child('studentsVoted').update({
                            [userId]: true
                        });
                    }
                }
            })
        })
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
            }
            else if (feelingData === 0) {
                $scope.zeros++
            }
            else {
                $scope.minusOnes++
            }
            $scope.labels = ["GOOD", "NEUTRAL", "BAD"];
            $scope.data = [$scope.ones, $scope.zeros, $scope.minusOnes];
        });
    });
});