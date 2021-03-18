package com.badmitry.bluetoothtest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.badmitry.bluetoothtest.databinding.ActivitySplashBinding
import com.google.android.material.snackbar.Snackbar

class SplashActivity : AppCompatActivity() {
//    private val REQUEST_OAUTH_REQUEST_CODE = 1
    private val REQUEST_PERMISSIONS_REQUEST_CODE  = 34
    private val TAG = "!!!Splash"
    private var binding: ActivitySplashBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        checkAllPermissions()
    }

    private fun checkAllPermissions() {
        if (hasRuntimePermissions()) {
            findFitnessDataSourcesWrapper()
        } else {
            requestRuntimePermissions()
        }
    }

    private fun findFitnessDataSourcesWrapper() {
//        if (hasOAuthPermission()) {
        toMainScreen()
//        } else {
//            requestOAuthPermission()
//        }
    }

    private fun toMainScreen() {
        intent = Intent(this, DeviceScanActivity::class.java)
        startActivity(intent)
    }

//    private fun hasOAuthPermission(): Boolean {
//        val fitnessOptions: FitnessOptions = getFitnessSignInOptions()
//        return GoogleSignIn.hasPermissions(
//            GoogleSignIn.getLastSignedInAccount(this),
//            fitnessOptions
//        )
//    }
//
//    private fun getFitnessSignInOptions(): FitnessOptions {
//        return FitnessOptions.builder()
//            .addDataType(DataType.TYPE_HEART_POINTS)
//            .addDataType(DataType.TYPE_HEART_RATE_BPM)
//            .build()
//    }

//    private fun requestOAuthPermission() {
//        val fitnessOptions = getFitnessSignInOptions()
//        GoogleSignIn.requestPermissions(
//            this,
//            REQUEST_OAUTH_REQUEST_CODE,
//            GoogleSignIn.getLastSignedInAccount(this),
//            fitnessOptions
//        )
//    }

    private fun requestRuntimePermissions() {
//        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
//            this, Manifest.permission.BODY_SENSORS
//        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
        Log.i(
            TAG,
            "Displaying permission rationale to provide additional context."
        )
        Snackbar.make(
            findViewById(R.id.splash_fl),
            R.string.permission_rationale,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(
                R.string.ok,
                View.OnClickListener { // Request permission
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                })
            .show()
//        } else {
//            Log.i(TAG, "Requesting permission")
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.BODY_SENSORS),
//                REQUEST_PERMISSIONS_REQUEST_CODE
//            )
//        }
    }

    private fun hasRuntimePermissions(): Boolean {
        val permissionState1: Int =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val permissionState2: Int =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
        val permissionState3: Int =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
        val permissionState4: Int =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        Log.i(
            TAG,
            "" + (permissionState1 == PackageManager.PERMISSION_GRANTED) +
                    (permissionState2 == PackageManager.PERMISSION_GRANTED) +
                    (permissionState3 == PackageManager.PERMISSION_GRANTED) +
                    (permissionState4 == PackageManager.PERMISSION_GRANTED)
        )
        return permissionState1 == PackageManager.PERMISSION_GRANTED &&
                permissionState2 == PackageManager.PERMISSION_GRANTED &&
                permissionState3 == PackageManager.PERMISSION_GRANTED &&
                permissionState4 == PackageManager.PERMISSION_GRANTED
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
//                findFitnessDataSourcesWrapper()
//            }
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int, vararg permissions: String?, grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                Log.i(TAG, "grantResults")
                findFitnessDataSourcesWrapper()
            } else {
                Log.i(TAG, "else")
//                findFitnessDataSourcesWrapper()
                // Permission denied.

                // In this Activity we've chosen to notify the user that they
                // have rejected a core permission for the app since it makes the Activity useless.
                // We're communicating this message in a Snackbar since this is a sample app, but
                // core permissions would typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                Snackbar.make(
                    findViewById(R.id.splash_fl),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(
                        R.string.settings,
                        View.OnClickListener { // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        })
                    .show()
            }
        }
    }
}