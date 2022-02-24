package com.jk.codez

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jk.codez.ad.*
import com.jk.codez.connection.Connection
import com.jk.codez.databinding.ActivityMainBinding
import com.jk.codez.room.CodezDb
import com.jk.codez.ui.CodezPagerAdapter
import com.mapbox.mapboxsdk.Mapbox

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CodezViewModel
    private var dialogManager: DialogManager? = null;

    val requestPermissionLauncher = registerForActivityResult(
        RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            println("is granted")
        } else {
            println("is not granted")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        dialogManager = DialogManager(this)
        Settings.load(this)
        Mapbox.getInstance(this, resources.getString(R.string.access_token))
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[CodezViewModel::class.java]
        dialogManager?.initialize(viewModel, resources)
        viewModel.initialize(CodezDb.getDatabase(this)?.codezDao(), dialogManager)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.viewPager
        val codezPagerAdapter = CodezPagerAdapter(this)
        viewPager.adapter = codezPagerAdapter

        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.setText(
                TITLES[position]
            )
        }.attach()

        binding.fab.setOnClickListener {
            dialogManager?.showItemDialog(null, viewPager.currentItem == 1)
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onLocationChanged(p0: Location) {

    }

    companion object {
        private val TITLES = arrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}