/**
 *   Copyright 2020 Geek Zone UK
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package zone.geek.membership.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_geek_zone.*
import kotlinx.android.synthetic.main.content_main.*
import zone.geek.membership.R
import zone.geek.membership.`interface`.OnEventSelectedListener
import zone.geek.membership.fragments.*


class GeekZoneActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OnEventSelectedListener {

    companion object {
        const val EVENT_DETAILS_URI = "URI"
        private const val WEB_FRAGMENT_TAG = "webFragmentTag"
        private const val EVENTS_FRAGMENT_TAG = "eventsFragmentTag"
        private const val SETTINGS_FRAGMENT_TAG = "settingsFragmentTag"
        private const val LATEST_NEWS_FRAGMENT_TAG = "latestFragmentTag"
        private const val LOGOUT_FRAGMENT_TAG = "logoutFragmentTag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geek_zone)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        title = getString(R.string.title_main)
        setSupportActionBar(gz_toolbar)
        addFragment()
        createNavigationDrawer()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_back)
    }

    private fun addFragment() {
        replaceFragment(EventsFragment(), EVENTS_FRAGMENT_TAG, R.string.title_main)
    }

    private fun createNavigationDrawer() {
        val drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            gz_toolbar,
            R.string.open,
            R.string.close
        ) {

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerToggle.syncState()
        drawerToggle.drawerArrowDrawable
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)

        nav_view.setNavigationItemSelectedListener(this)
    }

    // Handling of Overview menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_settings) {
            replaceFragment(SettingsFragment(), SETTINGS_FRAGMENT_TAG, R.string.title_settings)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Menu on Navigation Drawer
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.events -> {
                replaceFragment(EventsFragment(), EVENTS_FRAGMENT_TAG, R.string.title_events)
            }
            R.id.news -> {
                replaceFragment(
                    LatestNewsFragment(),
                    LATEST_NEWS_FRAGMENT_TAG,
                    R.string.title_latest_news
                )
                title = getString(R.string.title_latest_news)
            }
            R.id.settings -> {
                replaceFragment(SettingsFragment(), SETTINGS_FRAGMENT_TAG, R.string.title_settings)
            }
            R.id.logout -> {
                replaceFragment(LogoutFragment(), LOGOUT_FRAGMENT_TAG, R.string.title_logout)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // Managing of Home button.
    override fun onBackPressed() {
        clickBackButton()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is EventsFragment) {
            fragment.setOnEventSelectedListener(this)
        }
    }

    override fun onEventSelected(position: Int, uri: String) {
        val bundle = Bundle()
        bundle.putString(EVENT_DETAILS_URI, uri)

        supportFragmentManager.commit {
            replace(R.id.frame_layout, WebFragment::class.java, bundle, WEB_FRAGMENT_TAG)
            addToBackStack(null)
        }
        title = getString(R.string.title_event)
    }

    private fun replaceFragment(
        fragmentClass: Fragment,
        tag: String,
        titleResId: Int
    ) {
        supportFragmentManager.commit {
            replace(R.id.frame_layout, fragmentClass, tag)
            addToBackStack(null)
        }
        this.title = getString(titleResId)
    }

    private fun clickBackButton() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            val builder =
                AlertDialog.Builder(this)

            val imageResource = R.drawable.ic_error_outline_black_24dp
            val image = ResourcesCompat.getDrawable(resources, imageResource, theme)

            builder.setTitle(getString(R.string.exit_message_text))
                .setMessage(getString(R.string.exit_warning_alert_dialog_yes)).setIcon(image)
                .setCancelable(false)
                .setPositiveButton(
                    getString(R.string.exit_warning_alert_option_yes)
                ) { _, _ -> finish() }
                .setNegativeButton(
                    getString(R.string.exit_warning_alert_option_no)
                ) { _, _ -> }

            val alert = builder.create()
            alert.setCancelable(false)
            alert.show()
        }
    }
}