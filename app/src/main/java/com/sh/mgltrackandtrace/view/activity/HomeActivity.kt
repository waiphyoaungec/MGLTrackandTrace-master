package com.sh.mgltrackandtrace.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.facebook.accountkit.Account
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.accountkit.AccountKitError
import com.sh.mgltrackandtrace.R
import com.sh.mgltrackandtrace.view.fragment.EventFragment
import com.sh.mgltrackandtrace.view.fragment.WaybillFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.navigationheader_view.*

class HomeActivity : AppCompatActivity() {
    var navigationPosition: Int = 0
    var phno : String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        try{
            initView()
        }catch (e:Exception){
            Log.d("exception", e.toString())
        }

    }
    override fun onResume() {
        super.onResume()
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(account: Account) {
                phno = account.phoneNumber.toString().split("+95")[1]
                txtprofilename.setText(phno)
            }

            override fun onError(error: AccountKitError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        //inflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_logout -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load Inbox fragment first
        navigationPosition = R.id.navItemNews
        navigateToFragment(EventFragment())
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = getString(R.string.title_activity_home)
        toolbar.setTitleTextColor(Color.WHITE)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navItemNews -> {
                    toolbar.title = getString(R.string.drawer_itemf)
                    navigationPosition = R.id.navItemNews
                    navigateToFragment(EventFragment())
                }
                R.id.navItemWbSearch -> {
                    toolbar.title = getString(R.string.drawer_items)
                    navigationPosition = R.id.navItemWbSearch
                    navigateToFragment(WaybillFragment())
                }
                R.id.navItemLogout->{
                    AccountKit.logOut()
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("phone", "").apply()
                    val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()
            true
        }
    }



    private fun setUpDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_opened, R.string.drawer_closed)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun navigateToFragment(fragmentToNavigate: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragmentToNavigate)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("WrongConstant")
    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START)
        }

        if (navigationPosition == R.id.navItemNews) {
            finish()
        } else {
            //Navigate to Inbox Fragment
            navigationPosition = R.id.navItemNews
            navigateToFragment(EventFragment())
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = getString(R.string.drawer_itemf)
        }
    }
}
