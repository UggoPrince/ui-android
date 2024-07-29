package com.users.ui.adduser

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.users.databinding.ActivityAddUserBinding

import com.users.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUserActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModel()
    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email!!
        // val password = binding.password
        val firstname = binding.firstname!!
        val lastname = binding.lastname!!
        val addUserButton = binding.addUserButton!!
        val loading = binding.loading

//        userViewModel = ViewModelProvider(this, UserViewModelFactory())
//            .get(UserViewModel::class.java)

        userViewModel.addUserFormState.observe(this@AddUserActivity, Observer {
            val addUserState = it ?: return@Observer

            // disable login button unless both username / password is valid
            addUserButton.isEnabled = addUserState.allFieldsFilled

            if (addUserState.emailError != null) {
                email.error = getString(addUserState.emailError)
            }
            if (addUserState.firstnameError != null) {
                firstname.error = getString(addUserState.firstnameError)
            }
            if (addUserState.lastnameError != null) {
                lastname.error = getString(addUserState.lastnameError)
            }
            if (addUserState.isDataValid) {
                // loading.visibility = View.VISIBLE
                lifecycleScope.launch {
                    userViewModel.addUser(
                        email.text.toString(),
                        firstname.text.toString(),
                        lastname.text.toString()
                    )
                }
            }
        })

        userViewModel.addUserResult.observe(this@AddUserActivity, Observer {
            val addingUserResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (addingUserResult.error != null) {
                showLoginFailed(addingUserResult.error)
            }
            if (addingUserResult.success != null) {
                updateUiWithUser(addingUserResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        email.afterTextChanged {
            userViewModel.addUserDataChanged(
                email.text.toString(),
                firstname.text.toString(),
                lastname.text.toString()
            )
        }
        firstname.apply {
            afterTextChanged {
                userViewModel.addUserDataChanged(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
        lastname.apply {
            afterTextChanged {
                userViewModel.addUserDataChanged(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
        addUserButton.setOnClickListener {
            lifecycleScope.launch {
                userViewModel.canAddUserCheck(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }

//        password.apply {
//            afterTextChanged {
//                loginViewModel.loginDataChanged(
//                    username.text.toString(),
//                    password.text.toString()
//                )
//            }
//
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        loginViewModel.login(
//                            username.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }
//        }
    }

    private fun updateUiWithUser(model: AddedUserView) {
        val added = getString(R.string.added)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$displayName $added",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}