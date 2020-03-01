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
    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME1 = 4000 // ms
    private val JOB_TIME2 = 3000 // ms
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressbar1.scaleY = 5f
        progressbar2.scaleY = 5f
        btn_job.setOnClickListener {
            scope = CoroutineScope(Dispatchers.Main)
            startTask()
        }
    }

    fun startTask(){
        scope.launch {
            println("debug startTask: ${Thread.currentThread().name}")
            val time = measureTimeMillis {
                val result1:Deferred<String> = async {
                    println("debug starting async job 1: ${Thread.currentThread().name}")
                    getDataFromNetwork1()
                }

                val result2:Deferred<String> = async {
                    println("debug starting async job 2: ${Thread.currentThread().name}")
                    getDataFromNetwork2()
                }
                updateUI(result1.await())
                updateUI(result2.await())
            }
            println("debug Total elapsed time: ${time}")
        }
    }

    suspend fun getDataFromNetwork1():String{
        withContext(Dispatchers.IO) {
            for (i in PROGRESS_START..PROGRESS_MAX){
                delay((JOB_TIME1/PROGRESS_MAX).toLong())
                println("debug getDataFromNetwork 1: ${i} :  ${Thread.currentThread().name}")
                showProgress1(i)
            }
        }
        return "Task#1 Completed"
    }

    suspend fun getDataFromNetwork2():String{
        withContext(Dispatchers.IO) {
            for (i in PROGRESS_START..PROGRESS_MAX){
                delay((JOB_TIME2/PROGRESS_MAX).toLong())
                println("debug getDataFromNetwork 2: ${i} :  ${Thread.currentThread().name}")
                showProgress2(i)
            }
        }
        return "Task#2 Completed"
    }



    suspend fun showProgress1(i:Int) {
        withContext(Dispatchers.Main){
            progressbar1.progress=i
            println("debug showProgress 1: ${i}:  ${Thread.currentThread().name}")
        }

    }

    suspend fun showProgress2(i:Int) {
        withContext(Dispatchers.Main){
            progressbar2.progress=i
            println("debug showProgress 2: ${i}:  ${Thread.currentThread().name}")
        }

    }

    fun updateUI(message:String){
        txt_result.text = txt_result.text.toString()+"\n"+message
    }

}
