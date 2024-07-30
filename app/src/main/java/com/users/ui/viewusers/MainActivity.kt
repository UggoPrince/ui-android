package com.users.ui.viewusers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.users.R
import com.users.databinding.ActivityMainBinding
import com.users.ui.adduser.AddUserActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var adapter: Adapter = Adapter()
    private lateinit var recyclerView: RecyclerView
    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        recyclerView = findViewById(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.users.observe(this) {
            if (it.isNotEmpty()) {
                adapter.setData(it)
                recyclerView.adapter = adapter
            }
        }
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            userViewModel.getUsers()
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
}