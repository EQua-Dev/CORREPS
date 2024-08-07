package com.schoolprojects.corrreps.holder

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.schoolprojects.corrreps.screens.auth.LoginScreen
import com.schoolprojects.corrreps.providers.LocalNavHost
import com.schoolprojects.corrreps.utils.Common.mAuth
import com.schoolprojects.corrreps.navigation.Screen
import com.schoolprojects.corrreps.screens.auth.ForgotPasswordScreen
import com.schoolprojects.corrreps.screens.auth.SignUpScreen
import com.schoolprojects.corrreps.screens.feespayment.PaysFees
import com.schoolprojects.corrreps.screens.lecturer.LecturerHomeScreen
import com.schoolprojects.corrreps.screens.lecturer.LecturerSemesterScreen
import com.schoolprojects.corrreps.screens.lecturer.StudentCoursesScreen
import com.schoolprojects.corrreps.screens.student.SemesterScreen
import com.schoolprojects.corrreps.screens.student.StudentHomeScreen
import com.schoolprojects.corrreps.utils.Common
import org.devstrike.persacg.presentation.screens.holder.HolderViewModel
import com.schoolprojects.corrreps.utils.getDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HolderScreen(
    onStatusBarColorChange: (color: Color) -> Unit,
    holderViewModel: HolderViewModel = hiltViewModel(),
) {
    /*  val destinations = remember {
          listOf(Screen.Home, Screen.Notifications, Screen.Bookmark, Screen.Profile)
      }*/

    /** Our navigation controller that the MainActivity provides */
    val controller = LocalNavHost.current

    /** The current active navigation route */
    val currentRouteAsState = getActiveRoute(navController = controller)

    /** The current logged user, which is null by default */

    /** The main app's scaffold state */
    val scaffoldState = rememberBottomSheetScaffoldState()

    /** The coroutine scope */
    val scope = rememberCoroutineScope()

    /** Dynamic snack bar color */
    val (snackBarColor, setSnackBarColor) = remember {
        mutableStateOf(Color.White)
    }

    /** SnackBar appear/disappear transition */
    val snackBarTransition = updateTransition(
        targetState = scaffoldState.snackbarHostState,
        label = "SnackBarTransition"
    )

    /** SnackBar animated offset */
    val snackBarOffsetAnim by snackBarTransition.animateDp(
        label = "snackBarOffsetAnim",
        transitionSpec = {
            TweenSpec(
                durationMillis = 300,
                easing = LinearEasing,
            )
        }
    ) {
        when (it.currentSnackbarData) {
            null -> {
                100.getDp()
            }

            else -> {
                0.getDp()
            }
        }
    }

    Box {
        /** Cart offset on the screen */
        val (cartOffset, setCartOffset) = remember {
            mutableStateOf(IntOffset(0, 0))
        }
        ScaffoldSection(
            controller = controller,
            scaffoldState = scaffoldState,
            onStatusBarColorChange = onStatusBarColorChange,
            onNavigationRequested = { route, removePreviousRoute ->
                if (removePreviousRoute) {
                    controller.popBackStack()
                }
                controller.navigate(route)
            },
            onBackRequested = {
                controller.popBackStack()
            },
            onAuthenticated = { userType ->
                var navRoute = ""
                when (userType) {
                    /*Common.UserTypes.STUDENT.userType -> navRoute = Screen.StudentLanding.route
                    Common.UserTypes.LECTURER.userType -> navRoute =
                        Screen.LecturerLandingScreen.route*/
                }
                controller.navigate(navRoute) {
                    /* popUpTo(Screen.Login.route) {
                         inclusive = true
                     }*/
                }
            },
            onAccountCreated = {
                //nav to register courses
                /*controller.navigate(Screen.CourseRegistration.route) {
                    popUpTo(Screen.Signup.route) {
                        inclusive = true
                    }
                }*/
            },
            onCourseRegistered = {
                //nav to set course goals
                //controller.navigate(Screen.SetCourseGoal.route)
            },
            onGoalsSet = {
                //nav to student home
                //controller.navigate(Screen.StudentLanding.route)
            },
            onSemesterSelected = { level, semester, userType ->
                controller.navigate(
                    Screen.SemesterScreen.route.replace("{level}", level)
                        .replace("{semester}", semester).replace("{userType}", userType)
                )

            },
            onViewStudent = { studentId, level, semester ->
                controller.navigate(
                    Screen.StudentCourses.route.replace("{studentId}", studentId)
                        .replace("{level}", level)
                        .replace("{semester}", semester)
                )
                //controller.navigate(Screen.StudentDetail.route.replace("{studentId}", studentId))
            },
            onNewScreenRequest = { route, patientId ->
                controller.navigate(route.replace("{patientId}", "$patientId"))
            },
            onLogoutRequested = {
                mAuth.signOut()
                /*controller.navigate(Screen.Login.route) {
                    popUpTo(Screen.Signup.route) {
                        inclusive = true
                    }
                }*/
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldSection(
    controller: NavHostController,
    scaffoldState: BottomSheetScaffoldState,
    onStatusBarColorChange: (color: Color) -> Unit,
    onNavigationRequested: (route: String, removePreviousRoute: Boolean) -> Unit,
    onBackRequested: () -> Unit,
    onAuthenticated: (userType: String) -> Unit,
    onAccountCreated: () -> Unit,
    onCourseRegistered: () -> Unit,
    onGoalsSet: () -> Unit,
    onSemesterSelected: (level: String, semester: String, userType: String) -> Unit,
    onViewStudent: (studentId: String, level: String, semester: String) -> Unit,
    onNewScreenRequest: (route: String, id: String?) -> Unit,
    onLogoutRequested: () -> Unit
) {
    Scaffold(
        //scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        },
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues)
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = controller,
                startDestination = Screen.Login.route
            ) {
                composable(Screen.Login.route) {
                    onStatusBarColorChange(Color(0xFFDE502C))
                    LoginScreen(
                        onNavigationRequested = onNavigationRequested,
                        onAuthenticated = onAuthenticated
                    )
                }
                composable(Screen.Signup.route) {
                    onStatusBarColorChange(Color(0xFFDE502C))
                    SignUpScreen(
                        onNavigationRequested = onNavigationRequested,
                        onAccountCreated = onAccountCreated,
                    )
                }
                /*
                    composable(Screen.Login.route) {
                        onStatusBarColorChange(MaterialTheme.colorScheme.background)
                       *//* LoginScreen(
                        onSignUpClicked = onSignUpClicked,
                        onForgotPasswordClicked = onForgotPasswordClicked,
                        onAuthenticated = onAuthenticated
                    )
                }*/
                composable(Screen.ForgotPassword.route) {
                    onStatusBarColorChange(Color(0xFFDE502C))
                    ForgotPasswordScreen(
                        onNavigationRequested = onNavigationRequested
                    )
                }

                composable(Screen.StudentHome.route) {
                    onStatusBarColorChange(Color(0xFFDE502C))
                    StudentHomeScreen(
                        baseNavHostController = controller,
                        onNavigationRequested = onNavigationRequested,
                        onSemesterSelected = onSemesterSelected
                    )
                }
                /*
                    composable(Screen.CourseRegistration.route) {
                        onStatusBarColorChange(MaterialTheme.colorScheme.background)
                        *//*CourseRegistrationScreen(
                        onBackRequested = onBackRequested,
                        onCourseRegistered = onCourseRegistered

                    )
                }*/
                composable(
                    Screen.SemesterScreen.route,
                    arguments = listOf(
                        navArgument(name = "level") { type = NavType.StringType },
                        navArgument(name = "semester") { type = NavType.StringType }
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val level = it.arguments?.getString("level")
                    val semester = it.arguments?.getString("semester")
                    val userType = it.arguments?.getString("userType")
                    if (userType == Common.UserTypes.STUDENT.userType) {
                        SemesterScreen(
                            level = level!!, semester = semester!!
                            //onBackRequested = onBackRequested,
                        )
                    } else {
                        LecturerSemesterScreen(
                            level = level!!, semester = semester!!,
                            onViewStudent = onViewStudent,
                            baseNavHostController = controller,
                            //onBackRequested = onBackRequested,
                        )
                    }

                }
                composable(
                    Screen.FeesPayment.route,
                    /*arguments = listOf(
                        navArgument(name = "level") { type = NavType.StringType },
                        navArgument(name = "semester") { type = NavType.StringType }
                    ),*/
                ) {
                    onStatusBarColorChange(Color(0xFFDE502C))
                    val level = it.arguments?.getString("level")
                    val semester = it.arguments?.getString("semester")
                    PaysFees(
                        //level = level!!, semester = semester!!
                        //onBackRequested = onBackRequested,

                    )
                }

                composable(
                    Screen.LecturerHome.route,
                ) {
                    onStatusBarColorChange(Color(0xFFDE502C))
                    LecturerHomeScreen(
                        onLogoutRequested = onLogoutRequested,
                        onNavigationRequested = onNavigationRequested,
                        onSemesterSelected = onSemesterSelected
                    )
                }
                composable(
                    Screen.StudentCourses.route,
                    arguments = listOf(
                        navArgument(name = "studentId") { type = NavType.StringType },
                        navArgument(name = "level") { type = NavType.StringType },
                        navArgument(name = "semester") { type = NavType.StringType }
                    ),
                ) {
                    onStatusBarColorChange(Color(0xFFDE502C))
                    val level = it.arguments?.getString("level")
                    val semester = it.arguments?.getString("semester")
                    val studentId = it.arguments?.getString("studentId")
                    StudentCoursesScreen(
                        studentId = studentId!!,
                        level = level!!, semester = semester!!,
                        //onBackRequested = onBackRequested,
                    )


                }
                /*
                composable(Screen.LecturerLandingScreen.route) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    *//*LecturerLandingScreen(
                        onLogoutRequested = onLogoutRequested,
                        onViewStudentList = onViewStudentList
                    )*//*
                }
                composable(Screen.StudentLanding.route) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    *//*StudentHomeScreen(
                        onLogoutRequested = onLogoutRequested
                    )*//*
                }
                composable(
                    Screen.StudentList.route,
                    arguments = listOf(
                        navArgument(name = "level") { type = NavType.StringType }
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val level = it.arguments?.getString("level")
                    *//*StudentListScreen(
                        level = level!!,
                        onViewStudent = onViewStudent,
                        onBackRequested = onBackRequested
                    )*//*
                }
                composable(
                    Screen.StudentDetail.route,
                    arguments = listOf(
                        navArgument(name = "studentId") { type = NavType.StringType }
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val studentId = it.arguments?.getString("studentId")
                    *//*StudentDetail(
                        studentId = studentId!!
                    )*//*
                }*/
                /*composable(
                    Screen.PatientOverView.route,
                    arguments = listOf(
                        navArgument(name = "patientId") { type = NavType.StringType }
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val patientId = it.arguments?.getString("patientId")
                    PatientOverview(
                        patientId!!,
                        onNewScreenRequest = onNewScreenRequest
                    )
                }
                composable(
                    Screen.NewPatientRecord.route,
                    arguments = listOf(
                        navArgument(name = "patientId") { type = NavType.StringType }
                    ),
                ) {
                    onStatusBarColorChange(MaterialTheme.colorScheme.background)
                    val patientId = it.arguments?.getString("patientId")
                    NewPatientRecordScreen(
                        patientId = patientId!!,
                        onToastRequested = onToastRequested,
                        onPatientRecordUpdated = onBackRequested
                        *//*onNewScreenRequest = onNewScreenRequest*//*
                    )
                }*/

            }
        }
    }
}

/**
 * A function that is used to get the active route in our Navigation Graph , should return the splash route if it's null
 */
@Composable
fun getActiveRoute(navController: NavHostController): String {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route ?: "splash"
}
