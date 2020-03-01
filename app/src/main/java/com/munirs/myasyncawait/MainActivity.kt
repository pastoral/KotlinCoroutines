package com.munirs.myasyncawait

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window.PROGRESS_START
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    lateinit var scope: CoroutineScope
    private val JOB_TIME = 4000 // ms
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_job.setOnClickListener {
            scope = CoroutineScope(Dispatchers.Main)
            startTask()
        }
    }

    fun startTask(){
       val job = scope.launch {
           var result = ""
           var time = measureTimeMillis {
               println("startTask: ${Thread.currentThread().name}")
                result = getDataNetwork()
           }
           println("Elapsed Time: ${time}")

            updateUI(result)
        }
    }

    private suspend fun getDataNetwork():String {
        withContext(Dispatchers.IO) {
            delay(JOB_TIME.toLong())
            println("getDataNetwork: ${Thread.currentThread().name}")
        }
        return "Task complete"
    }

    private suspend fun updateUI(message:String){
        withContext(Dispatchers.Main){
            txt_result.text= txt_result.text.toString()+"\n"+message
            println("updateUI: ${Thread.currentThread().name}")
        }
    }


}
