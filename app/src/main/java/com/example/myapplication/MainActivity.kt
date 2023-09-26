package com.example.myapplication

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData

import com.example.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import il.co.syntax.finalkotlinproject.utils.Error
import il.co.syntax.finalkotlinproject.utils.Loading
import il.co.syntax.finalkotlinproject.utils.Resource
import il.co.syntax.finalkotlinproject.utils.Success
import java.lang.ref.WeakReference


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainPageViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      viewModel.eventsStatus.observe(this) {
            when (it.status) {
                is Loading ->{
                    Log.d("Recycler loading", "Trying to load data.")
                }
                is Success -> {
                    binding.airCondSwitch.isChecked = it.status.data!![0].service_status == 1
                    binding.LightsSwitch.isChecked = it.status.data!![2].service_status == 1
                    when(it.status.data!![1].service_status){
                        1 -> {
                            binding.statusTrouble.text = "Issue found"
                            binding.statusTrouble.setTextColor(ContextCompat.getColor(this, R.color.red))
                            val dialog = Dialog(this)
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog.setCancelable(false)
                            dialog.setContentView(R.layout.custom_dialog)

                            val titleTextView = dialog.findViewById<TextView>(R.id.dialog_title)
                            titleTextView.text = "Alert!"

                            val messageTextView = dialog.findViewById<TextView>(R.id.dialog_message)
                            messageTextView.text = it.status.data!![1].service_description.toString()

                            val okButton = dialog.findViewById<Button>(R.id.ok_button)
                            okButton.setOnClickListener {
                                dialog.dismiss()
                            }

                            dialog.show()
                        }
                        else -> {
                            binding.statusTrouble.text = "All Right"
                            binding.statusTrouble.setTextColor(ContextCompat.getColor(this, R.color.green))
                        }
                    }
                }
                is Error -> {
                    Log.d("Recycler view Error", "Error in getting data.")
                }
            }
        }

        binding.resetButton.setOnClickListener {
            viewModel.updateService("1")
        }
     }
    }

