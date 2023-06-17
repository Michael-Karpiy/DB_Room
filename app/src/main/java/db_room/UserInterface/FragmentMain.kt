package db_room.UserInterface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import db_room.R
import db_room.RoomDatabase.RDatabase
import db_room.UserInterface.ActivityMain.Companion.cvBack
import db_room.UserInterface.ActivityMain.Companion.tvTitle
import kotlinx.coroutines.launch

class FragmentMain : Fragment() {

    private lateinit var add: CardView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter

    private val noteDatabase by lazy { RDatabase.getDatabase(requireContext()).contactDao() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cvBack.visibility = View.GONE
        tvTitle.setText(R.string.app_name)
        add = requireView().findViewById(R.id.add)
        add.setOnClickListener { (context as ActivityMain).showFragment(FragmentAdd()) }

        setRecyclerView()
        observeData()
    }


    private fun setRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = Adapter(requireContext())
        adapter.setItemListener(object : RecyclerClickListener {

            override fun onItemRemoveClick(position: Int) {
                val model = adapter.currentList.toMutableList()
                val positions = model[position].position
                val id = model[position].id
                val name = model[position].name
                val description = model[position].description
                val removeNote = Model(positions, id, name, description)
                model.removeAt(position)
                adapter.submitList(model)
                lifecycleScope.launch {
                    noteDatabase.delete(removeNote)
                }
            }

            override fun onItemClick(position: Int) {
                val model = adapter.currentList.toMutableList()
                (context as ActivityMain).showFragment(FragmentSecond(model[position].position))
            }
        })
        recyclerView.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launch {
            noteDatabase.getData().collect { notesList ->
                if (notesList.isNotEmpty()) {
                    adapter.submitList(notesList)
                }
            }
        }
    }
}