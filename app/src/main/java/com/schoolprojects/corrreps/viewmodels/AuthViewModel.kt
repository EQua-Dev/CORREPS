package com.schoolprojects.corrreps.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.schoolprojects.corrreps.models.Student
import com.schoolprojects.corrreps.navigation.Screen
import com.schoolprojects.corrreps.utils.Common
import com.schoolprojects.corrreps.utils.Common.mAuth
import com.schoolprojects.corrreps.utils.Common.studentsCollectionRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    val email = mutableStateOf<String>("")
    val password = mutableStateOf<String>("")
    val passwordStrength =
        mutableStateOf<AuthViewModel.PasswordStrength>(PasswordStrength.TOO_SHORT)
    val studentFirstName = mutableStateOf<String>("")
    val studentLastName = mutableStateOf<String>("")
    val matricNo = mutableStateOf<String>("")
    val studentDepartment = mutableStateOf<String>("")

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
        this.passwordStrength.value = passwordStrength(value)
    }

    fun updateStudentDepartment(value: String) {
        this.studentDepartment.value = value
    }

    fun updateMatricNo(value: String) {
        this.matricNo.value = value
    }

    val currentSelectedGenderIndex = mutableIntStateOf(0)

    fun updateCurrentSelectedGenderId(index: Int) {
        currentSelectedGenderIndex.intValue = index
    }

    fun createStudent(
        onLoading: (isLoading: Boolean) -> Unit,
        onStudentCreated: () -> Unit,
        onStudentNotCreated: (error: String) -> Unit
    ) {
        onLoading(true)

        if (this.studentFirstName.value.isEmpty()) {
            onLoading(false)
            _errorLiveData.value = "First Name cannot be empty"
            onStudentNotCreated(errorLiveData.value!!)
        } else if (this.studentLastName.value.isEmpty()) {
            onLoading(false)
            _errorLiveData.value = "Last Name cannot be empty"
            onStudentNotCreated(errorLiveData.value!!)
        } else if (!isValidMatricNumberFormat(this.matricNo.value)) {
            onLoading(false)
            _errorLiveData.value = "Enter valid CST matric number format"
            onStudentNotCreated(errorLiveData.value!!)
        } else if (!isValidEmail(this.email.value)) {
            onLoading(false)
            _errorLiveData.value = "Enter valid email"
            onStudentNotCreated(errorLiveData.value!!)
        } else {
            when (passwordStrength(this.password.value)) {
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
                    mAuth.createUserWithEmailAndPassword(this.email.value, this.password.value)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val newUserId = mAuth.uid!!
                                //val user = mAuth.currentUser
                                val newStudent = Student(
                                    studentId = newUserId,
                                    studentFirstName = this.studentFirstName.value,
                                    studentLastName = this.studentLastName.value,
                                    studentRegNo = this.matricNo.value,
                                    studentEmail = this.email.value,
                                    studentGender = this.gender.value,
                                    studentDepartment = this.studentDepartment.value,
                                    studentCurrentLevel = "100",
                                    studentCurrentSemester = "First",
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

      fun login(
          onLoading: (isLoading: Boolean) -> Unit,
          onAuthenticated: (userType: String) -> Unit,
          onAuthenticationFailed: (error: String) -> Unit
      ) {
          onLoading(true)
          if (this.email.value.isEmpty() || this.password.value.isEmpty()) {
              onLoading(false)
              val error = "Some fields are missing"
              onAuthenticationFailed(error)
          } else {
              if (this.email.value == "admin@gmail.com" && this.password.value == "!Admin1234") {
                  onLoading(false)
                  onAuthenticated(Screen.LecturerHome.route)
              } else {
                  this.email.value.let { mAuth.signInWithEmailAndPassword(it, this.password.value) }
                      .addOnCompleteListener {
                          if (it.isSuccessful) {
                              onLoading(false)

                              onAuthenticated(Screen.StudentHome.route)
                          } else {
                              onLoading(false)

                              onAuthenticationFailed(it.exception?.message ?: "Some error occurred")
                          }
                      }
              }
          }
      }
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
        val customFormatRegex = Regex("^CS/\\d{4}/\\d+$")
        if (!customFormatRegex.matches(text)) {
            return false
        }

        val parts = text.split("/")
        val year = parts[1].toIntOrNull()

        // Check if the year is a valid 4-digit year
        return year != null && year in 1000..9999
    }

}