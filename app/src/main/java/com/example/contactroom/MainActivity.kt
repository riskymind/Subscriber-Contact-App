package com.example.contactroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactroom.adapters.MyRecyclerviewAdapter
import com.example.contactroom.databinding.ActivityMainBinding
import com.example.contactroom.db.Subscriber
import com.example.contactroom.db.SubscriberDatabase
import com.example.contactroom.db.SubscriberRepository
import com.example.contactroom.viewmodel.SubscriberViewModel
import com.example.contactroom.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)

        val factory = SubscriberViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)

        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        initRecyclerview()

        viewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initRecyclerview() {
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)
        displaySubscribersList()
    }

    private fun displaySubscribersList() {
        viewModel.subscribers.observe(this, Observer {
            Log.i("MYTAG", it.toString())
            binding.subscriberRecyclerview.adapter = MyRecyclerviewAdapter(it) { selectedItem: Subscriber ->
                listItemClicked(
                    selectedItem
                )
            }
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
//        Toast.makeText(this, "selected name is ${subscriber.name}", Toast.LENGTH_LONG).show()
        viewModel.initUpdateAndDelete(subscriber)
    }
}