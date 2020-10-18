package com.novyapp.test.jobschedulercodelab

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.pm.ComponentInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class MainActivity : AppCompatActivity() {
    var scheduler: JobScheduler? = null
    val JOB_ID = 0

    private lateinit var deviceIdleSwitch: SwitchCompat
    private lateinit var deviceChargingSwitch: SwitchCompat
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deviceChargingSwitch = findViewById(R.id.chargingSwitch)
        deviceIdleSwitch = findViewById(R.id.idleSwitch)
        seekBar = findViewById(R.id.seekBar)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress > 0) {
                    seekBarProgress.setText("${progress}s");
                } else {
                    seekBarProgress.setText(getString(R.string.not_set));
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                TODO("Not yet implemented")
            }
        })
    }

    fun scheduleJob(view: View) {
        val networkOptions = findViewById<RadioGroup>(R.id.networkOptions)
        val selectedNetworkOption = when (networkOptions.checkedRadioButtonId) {
            R.id.wifiNetwork -> JobInfo.NETWORK_TYPE_UNMETERED
            R.id.anyNetwork -> JobInfo.NETWORK_TYPE_ANY
            else -> JobInfo.NETWORK_TYPE_NONE
        }
        val seekBarInteger = seekBar.progress
        val seekBarSet = seekBarInteger > 0
        scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceName =
            ComponentName(packageName, NotificationJobService::class.java.name)
        val builder = JobInfo.Builder(JOB_ID, serviceName)
        builder.setRequiredNetworkType(selectedNetworkOption)
        builder.setRequiresDeviceIdle(deviceIdleSwitch.isChecked)
        builder.setRequiresCharging(deviceChargingSwitch.isChecked)
        if (seekBarSet) {
            builder.setOverrideDeadline((seekBarInteger * 1000L))
        }


        val myJobInfo: JobInfo = builder.build()
        val constraintSet =
            (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE) ||
                    (deviceIdleSwitch.isChecked) ||
                    (deviceChargingSwitch.isChecked) ||
                    seekBarSet


        if (constraintSet) {
            scheduler!!.schedule(myJobInfo)
            Toast.makeText(
                this, "Job Scheduled, job will run when " +
                        "the constraints are met.", Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(this, "Please set at least one constraint", Toast.LENGTH_LONG).show()
        }
    }


    fun cancelJobs(view: View) {
        scheduler?.let {
            it.cancelAll()
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
        scheduler = null
    }

}


