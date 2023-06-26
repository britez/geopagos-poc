package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentFirstBinding
import com.geopagos.reader.qpos.SDKQposReader
import com.google.android.material.snackbar.Snackbar
import com.mpos.payments.transaction.configuration.SDKPayments
import com.mpos.payments.transaction.configuration.SDKPaymentsConfiguration
import com.mpos.payments.transaction.logger.SDKLogger
import com.mpos.payments.transaction.model.reader.SDKDevice
import com.mpos.payments.transaction.model.scanService.SDKScannerResultListener
import com.mpos.payments.transaction.scanService.SDKDeviceScanServiceFactory

class MainActivity : AppCompatActivity(), SDKScannerResultListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.buttonConfig.setOnClickListener {
            val sdkPaymentsConfiguration = SDKPaymentsConfiguration.Builder()
                .setLogger(object : SDKLogger {

                    override fun log(tag: String, message: String, level: SDKLogger.LogLevel) {
                        Log.i(tag, message)
                    }
                })
                .build(this@MainActivity,
                    "http://example.com", setOf(SDKQposReader.getInstaller()))

            SDKPayments.init(sdkPaymentsConfiguration)
        }

        binding.buttonSearch.setOnClickListener { view ->
            val readerService = SDKDeviceScanServiceFactory.create()
            readerService.scan(this, 120000)
        }

        binding.buttonPermission.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT), 0)
        }
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show()
//        }
    }

    override fun onDeviceListUpdated(list: List<SDKDevice>) {
        // TODO: Implement the logic to handle the list of devices found by the scanner
        list.size
    }

    override fun onScanFinish() {
        // TODO: Implement the logic to handle the scanning process being stopped
        binding.buttonSearch
    }

    override fun onError(type: SDKScannerResultListener.ErrorType, message: String?) {
        // TODO: Implement the logic to handle the different errors that may happen when scanning
        type.toString()
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

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }

}