package com.homesoftwaretools.toptalproperty.core.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.homesoftwaretools.toptalproperty.FragmentLifecycleCallback
import com.homesoftwaretools.toptalproperty.R
import org.koin.android.ext.android.getKoin
import org.koin.androidx.scope.bindScope
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.ext.getScopeId

abstract class BaseActivity : AppCompatActivity() {

    private val fragmentLifecycleCallback: FragmentManager.FragmentLifecycleCallbacks by lazy { FragmentLifecycleCallback() }
    abstract fun fragmentBuilder(): Fragment
    lateinit var baseScope: Scope

    open val layoutId: Int = R.layout.main_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        baseScope = getKoin().createScope(getScopeId(), named("BaseActivity"), this)
        bindScope(baseScope)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallback, true)
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragmentBuilder())
                .apply { onFragmentTransaction(this) }
                .commit()

    }

    protected open fun onFragmentTransaction(transaction: FragmentTransaction) { }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallback)
    }

}

abstract class BaseScaffoldActivity : BaseActivity() {

    private val toolbar by lazy { findViewById<MaterialToolbar>(R.id.toolbar) }
    private val drawer by lazy { findViewById<DrawerLayout>(R.id.drawer) }
    private val bottom by lazy { findViewById<BottomNavigationView>(R.id.bottom_navigation) }

    override val layoutId = R.layout.scaffold_container
    abstract val titleResId: Int
    abstract val menuResId: Int
    abstract val bottomMenuResId: Int

    abstract fun drawerBuilder(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = getString(titleResId)
        }
        bottom.inflateMenu(bottomMenuResId)
        bottom.setOnNavigationItemSelectedListener(this::onBottomItemSelect)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(menuResId, menu)
        return true
    }

    protected fun openDrawer() {
        drawer.openDrawer(GravityCompat.END, true)
    }

    override fun onFragmentTransaction(transaction: FragmentTransaction) {
        transaction.replace(R.id.drawer_container, drawerBuilder())
    }

    protected fun onBottomItemSelect(item: MenuItem): Boolean {
        return true
    }
}