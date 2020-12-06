package com.acaproject.reminderapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_task_page.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface FragmentControl {

    fun openPage(title: String, hasBack: Boolean, chosenFragment: Fragment)
    fun updateToolBar(title: String, hasBack: Boolean)
    fun sendTask(task: Task)
    fun editTask(task: Task)

}

const val CHANNEL_DEFAULT = "channel"

class MainActivity() : AppCompatActivity(), FragmentControl {
    private val helpFragment = HelpFragment()
    private val settingsFragment = SettingsFragment()
    private val homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)
        floatingBtn()

        openPage("Home", false, homeFragment)

        floatingBtn()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.helpItem -> {
                if (!supportFragmentManager.fragments.contains(helpFragment)) {
                    supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
                    openPage("Help", true, helpFragment)
                }

                true
            }
            R.id.settingsItem -> {
                if (!supportFragmentManager.fragments.contains(settingsFragment)) {
                    supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
                    openPage("Settings", true, settingsFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun openPage(title: String, hasBack: Boolean, chosenFragment: Fragment) {

        if (hasBack) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, chosenFragment)
                .commit()

        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, chosenFragment)
                .commit()
        }

    }

    override fun updateToolBar(title: String, hasBack: Boolean) {
        toolBar.title = title
        if (hasBack) {
            toolBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolBar.setNavigationOnClickListener { onBackPressed() }
        } else {
            toolBar.navigationIcon = null
        }
    }

    override fun sendTask(task: Task) {
        homeFragment.addTask(task)

    }

    override fun editTask(task: Task) {

        homeFragment.edit(task)
    }

    private fun floatingBtn() {
        floatingBtn.setOnClickListener {

            supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
            val floatingFragment = FloatingFragment()
            openPage("Add Task", true, floatingFragment)


        }
    }


}