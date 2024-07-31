package com.users.ui.update_user

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.users.R
import com.users.data.model.User
import com.users.databinding.ActivityUpdateUserBinding
import com.users.ui.UserView
import com.users.ui.view_users.UserViewModel
import com.users.util.afterTextChanged
import com.users.util.parcelable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateUserActivity : AppCompatActivity() {
    private val updateUserViewModel: UpdateUserViewModel by viewModel()
    private lateinit var binding: ActivityUpdateUserBinding
    private var user: User? = null
    private lateinit var updateFirstnameLayout: TextInputLayout
    private lateinit var updateFirstname: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.updateEmail
        val firstname = binding.updateFirstname // binding.updateFirstname
        val lastname = binding.updateLastname
        val updateUserButton = binding.updateUserButton
        val loading = binding.updateProgressBar
        updateFirstnameLayout = findViewById(R.id.updateFirstnameLayout)

        updateFirstname = findViewById(R.id.updateFirstname)

        user = intent.parcelable("USER")

        updateUserViewModel.updateUserFormState.observe(this@UpdateUserActivity, Observer {
            val updateUserState = it ?: return@Observer

            updateUserButton.isEnabled = updateUserState.allFieldsFilled

            if (updateUserState.emailError != null) {
                email.error = getString(updateUserState.emailError)
            }
            if (updateUserState.firstnameError != null) {
                firstname.error = getString(updateUserState.firstnameError)
            }
            if (updateUserState.lastnameError != null) {
                lastname.error = getString(updateUserState.lastnameError)
            }
            if (updateUserState.isDataValid) {
                loading.visibility = View.VISIBLE
                updateUserButton.isEnabled = false
                lifecycleScope.launch {
                    val userUpdate = User(
                        user!!.id,
                        firstname.text.toString().trim(),
                        lastname.text.toString().trim(),
                        email.text.toString().trim()
                    )
                    updateUserViewModel.updateUser(userUpdate)
                }
            }
        })

        updateUserViewModel.updateUserResult.observe(this@UpdateUserActivity, Observer {
            val updatingUserResult = it ?: return@Observer

            loading.visibility = View.INVISIBLE
            if (updatingUserResult.error != null) {
                showUpdatingFailed(updatingUserResult.error)
            }
            if (updatingUserResult.success != null) {
                updateUiWithUser(updatingUserResult.success)
                setResult(RESULT_OK)
                finish()
            }
        })

        email.afterTextChanged {
            updateUserViewModel.addUserDataChanged(
                email.text.toString(),
                firstname.text.toString(),
                lastname.text.toString()
            )
        }
        updateFirstname.apply {
            afterTextChanged {
                updateUserViewModel.addUserDataChanged(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
        lastname.apply {
            afterTextChanged {
                updateUserViewModel.addUserDataChanged(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
        updateUserButton.setOnClickListener {
            lifecycleScope.launch {
                updateUserViewModel.canAddUserCheck(
                    email.text.toString(),
                    firstname.text.toString(),
                    lastname.text.toString()
                )
            }
        }
        updateFirstnameLayout.editText?.setText(user!!.firstName)
        user?.let {
            lastname.text = stringToEditable(it.lastName)
            email.text = stringToEditable(it.email)
        }
    }

    private fun updateUiWithUser(model: UserView) {
        val updated = getString(R.string.updated)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$displayName $updated",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showUpdatingFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun stringToEditable(text: String): Editable {
        return Editable.Factory.getInstance().newEditable(text)
    }
}