package com.users.ui.view_users

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.users.R
import com.users.data.model.User
import com.users.databinding.ActivityMainBinding
import com.users.ui.add_user.AddUserActivity
import com.users.ui.update_user.UpdateUserActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var adapter: Adapter = Adapter({ data -> this.onItemClicked(data) }, { id -> this.itemDelete(id) })
    private lateinit var recyclerView: RecyclerView
    private val userViewModel: UserViewModel by viewModel()
    private lateinit var linearProgressIndicator: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setSupportActionBar(binding.toolbar)
        recyclerView = findViewById(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        linearProgressIndicator = findViewById(R.id.getUsersLoader)

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
        observeViewModel()
    }

    // observes viewmodel users property
    private fun observeViewModel() {
        userViewModel.users.observe(this) {
            hideProgressBar()
            if (it.isNotEmpty()) {
                adapter.setData(it)
                recyclerView.adapter = adapter
            }
        }
        userViewModel.getUsersError.observe(this) {
            hideProgressBar()
            showToast(it)
        }
        userViewModel.deletedUser.observe(this) {
            // hideProgressBar()
            if (it.success) {
                showToast("User deleted")
                fetchUsers()
            }
        }
        userViewModel.deleteError.observe(this) {
            hideProgressBar()
            showToast(it)
        }
    }

    // fetches users
    private fun fetchUsers() {
        displayProgressBar()
        lifecycleScope.launch {
            userViewModel.getUsers()
        }
    }

    private fun displayProgressBar() {
        linearProgressIndicator.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        linearProgressIndicator.visibility = View.INVISIBLE
    }

    // user item click listener that opens update user activity
    private fun onItemClicked(user: User) {
        val intent = Intent(this, UpdateUserActivity::class.java).apply {
            putExtra("USER", user)
        }
        startActivity(intent)
    }

    // delete an item
    private fun itemDelete(id: String) {
        displayProgressBar()
        lifecycleScope.launch {
            userViewModel.deleteUser(id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        fetchUsers()
        // adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(errorString: String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}