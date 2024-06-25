package com.schoolprojects.corrreps.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    val email = mutableStateOf<String>("")
    val password = mutableStateOf<String>("")
    val confirmPassword = mutableStateOf<String>("")
    val studentFirstName = mutableStateOf<String>("")
    val studentLastName = mutableStateOf<String>("")
    val matricNo = mutableStateOf<String>("")

    val gender = mutableStateOf<String>("")
    val showLoading = mutableStateOf<Boolean>(false)


    fun updateEmail(value: String) {
        this.email.value = value
    }

    fun updateFirstName(value: String) {
        this.studentFirstName.value = value
    }

    fun updateLastName(value: String) {
        this.studentLastName.value = value
    }

    fun updateGender(value: String) {

        this.gender.value = value
    }

    fun updateLoadingStatus(value: Boolean) {
        this.showLoading.value = value
    }

    fun updatePassword(value: String) {
        this.password.value = value
    }

    fun updateConfirmPassword(value: String) {
        this.confirmPassword.value = value
    }

    fun updateMatricNo(value: String) {
        this.matricNo.value = value
    }

    val currentSelectedGenderIndex = mutableIntStateOf(0)

    fun updateCurrentSelectedGenderId(index: Int) {
        currentSelectedGenderIndex.intValue = index
    }

   /* fun createStudent(
        //hospital: Hospital,
        email: String,
        matricNo: String,
        firstName: String,
        lastName: String,
        gender: String,
        password: String,
        confirmPassword: String,
        onLoading: (isLoading: Boolean) -> Unit,
        onStudentCreated: () -> Unit,
        onStudentNotCreated: (error: String) -> Unit
    ) {
        onLoading(true)

        if (firstName.isEmpty()) {
            onLoading(false)
            _errorLiveData.value = "First Name cannot be empty"
            onStudentNotCreated(errorLiveData.value!!)
        } else if (lastName.isEmpty()) {
            onLoading(false)
            _errorLiveData.value = "Last Name cannot be empty"
            onStudentNotCreated(errorLiveData.value!!)
        } else if (!isValidMatricNumberFormat(matricNo)) {
            onLoading(false)
            _errorLiveData.value = "Enter valid CST matric number format"
            onStudentNotCreated(errorLiveData.value!!)
        } else if (!isValidEmail(email)) {
            onLoading(false)
            _errorLiveData.value = "Enter valid email"
            onStudentNotCreated(errorLiveData.value!!)
        } else {
            when (passwordStrength(password)) {
                PasswordStrength.TOO_SHORT -> {
                    onLoading(false)
                    _errorLiveData.value = "Password must be at least 8 characters"
                    onStudentNotCreated(errorLiveData.value!!)
                }

                PasswordStrength.WEAK -> {
                    onLoading(false)
                    _errorLiveData.value =
                        "Password must contain at least 1 uppercase, 1 lowercase, 1 digit and 1 special character"
                    onStudentNotCreated(errorLiveData.value!!)
                }

                PasswordStrength.MEDIUM -> {
                    onLoading(false)
                    _errorLiveData.value =
                        "Password must contain at least 1 uppercase, 1 lowercase, 1 digit and 1 special character"
                    onStudentNotCreated(errorLiveData.value!!)
                }

                PasswordStrength.STRONG -> {

                    if (confirmPassword != password) {
                        onLoading(false)
                        _errorLiveData.value = "Password must match"
                        onStudentNotCreated(errorLiveData.value!!)
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val newUserId = mAuth.uid!!
                                    //val user = mAuth.currentUser
                                    val newStudent = Student(
                                        studentId = newUserId,
                                        matricNo = matricNo,
                                        studentFirstName = firstName,
                                        studentLastName = lastName,
                                        studentDepartment = "Computer Science",
                                        studentEmail = email,
                                        studentGender = gender,
                                        studentLevel = "100",
                                        studentSemester = "1st",
                                        CGPA = 0.00
                                    )
                                    saveStudent(
                                        newStudent,
                                        onLoading,
                                        onStudentCreated,
                                        onStudentNotCreated
                                    )
                                } else {
                                    it.exception?.message?.let { message ->
                                        onLoading(false)
                                        _errorLiveData.value = message
                                    }
                                }
                            }.addOnFailureListener {
                                it.message?.let { message ->
                                    onLoading(false)
                                    _errorLiveData.value = message
                                }
                            }
                    }
                }

            }
        }

    }*/

  /*  fun login(
        email: String,
        password: String,
        onLoading: (isLoading: Boolean) -> Unit,
        onAuthenticated: (userType: String) -> Unit,
        onAuthenticationFailed: (error: String) -> Unit
    ) {
        onLoading(true)
        if (email.isEmpty() || password.isEmpty()) {
            onLoading(false)
            val error = "Some fields are missing"
            onAuthenticationFailed(error)
        } else {
            if (email == "admin@gmail.com" && password == "!Admin1234") {
                onLoading(false)
                onAuthenticated(Common.UserTypes.LECTURER.userType)
            } else {
                email.let { mAuth.signInWithEmailAndPassword(it, password) }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            onLoading(false)
                            onAuthenticated(Common.UserTypes.STUDENT.userType)
                        } else {
                            onLoading(false)
                            Log.d("TAG", "login: ${it.exception?.message ?: "Some error occurred"}")
                            onAuthenticationFailed(it.exception?.message ?: "Some error occurred")
                        }
                    }
            }
        }
    }
*/
/*
    private fun saveStudent(
        studentData: Student,
        onLoading: (isLoading: Boolean) -> Unit,
        onStudentCreated: () -> Unit,
        onStudentNotCreated: (error: String) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        try {
            studentsCollectionRef.document(studentData.studentId).set(studentData).await()
            withContext(Dispatchers.Main) {
                onLoading(false)
                onStudentCreated()
            }
            //_authStateLiveData.value = DataResult.Success(true)

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorLiveData.value = e.message ?: "Some error occurred"
                onLoading(false)
                onStudentNotCreated(errorLiveData.value!!)
            }

        }
    }
*/

/*
    fun resetPassword(
        email: String,
        onLoading: (Boolean) -> Unit,
        onResetLinkSent: (String) -> Unit,
        onResetLinkNotSent: (String) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        onLoading(true)
        if (!isValidEmail(email)) {
            onLoading(false)
        } else {
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onLoading(false)
                        onResetLinkSent("Password Reset Link Sent\nCheck your email")
                    } else {
                        onLoading(false)
                        onResetLinkNotSent("Password Reset Link Sent\nCheck your email")
                    }
                }.addOnFailureListener { e ->
                    onLoading(false)
                    onResetLinkSent(e.message ?: "Some error occurred")
                }
        }
}
*/


private fun passwordStrength(password: String): PasswordStrength {
    // Minimum length requirement
    val minLength = 8

    // Check for minimum length
    if (password.length < minLength) {
        return PasswordStrength.TOO_SHORT
    }

    // Check for uppercase, lowercase, digit, and special character
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }

    // Calculate score based on conditions
    val score = listOf(
        hasUpperCase,
        hasLowerCase,
        hasDigit,
        hasSpecial
    ).count { it }

    // Determine strength based on score
    return when {
        score == 4 -> PasswordStrength.STRONG
        score >= 3 -> PasswordStrength.MEDIUM
        else -> PasswordStrength.WEAK
    }
}

// Enum to represent password strength
enum class PasswordStrength {
    WEAK,
    MEDIUM,
    STRONG,
    TOO_SHORT
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
    return emailRegex.matches(email)
}

fun isValidMatricNumberFormat(text: String): Boolean {
    val customFormatRegex = Regex("^CST/\\d{4}/\\d+$")
    if (!customFormatRegex.matches(text)) {
        return false
    }

    val parts = text.split("/")
    val year = parts[1].toIntOrNull()

    // Check if the year is a valid 4-digit year
    return year != null && year in 1000..9999
}

}