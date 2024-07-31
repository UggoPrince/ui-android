package com.users.ui.add_user

import android.app.Activity
import android.graphics.Color
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
import com.users.R
import com.users.databinding.ActivityAddUserBinding
import com.users.ui.UserView
import com.users.util.afterTextChanged

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddUserActivity : AppCompatActivity() {
    private val addUserViewModel: AddUserViewModel by viewModel()
    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val firstname = binding.firstname
        val lastname = binding.lastname
        val addUserButton = binding.addUserButton
        val loading = binding.loading

        addUserViewModel.addUserFormState.observe(this@AddUserActivity, Observer {
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
                loading.visibility = View.VISIBLE
                lifecycleScope.launch {
                    addUserViewModel.addUser(
                        email.text.toString().trim(),
                        firstname.text.toString().trim(),
                        lastname.text.toString().trim()
                    )
                }
            }
        })

        addUserViewModel.addUserResult.observe(this@AddUserActivity, Observer {
            val addingUserResult = it ?: return@Observer

            loading.visibility = View.INVISIBLE
            if (addingUserResult.error != null) {
                showAddingFailed(addingUserResult.error)
                addUserButton.isEnabled = true
            }
            if (addingUserResult.success != null) {
                updateUiWithUser(addingUserResult.success)
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        email.afterTextChanged {
            addUserViewModel.addUserDataChanged(
                email.text.toString(),
                firstname.text.toString(),
                lastname.text.toString()
            )
        }
        firstname.apply {
            afterTextChanged {
                addUserViewModel.addUserDataChanged(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
        lastname.apply {
            afterTextChanged {
                addUserViewModel.addUserDataChanged(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
        addUserButton.setOnClickListener {
            lifecycleScope.launch {
                addUserViewModel.canAddUserCheck(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
    }

    private fun updateUiWithUser(model: UserView) {
        val added = getString(R.string.added)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$displayName $added",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showAddingFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
