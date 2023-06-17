package db_room.UserInterface

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import db_room.R
import db_room.RoomDatabase.RDatabase
import db_room.RoomDatabase.Repository
import db_room.UserInterface.ActivityMain.Companion.cvBack
import db_room.UserInterface.ActivityMain.Companion.tvTitle
import kotlinx.coroutines.launch

class FragmentSecond(val adapterPosition: Int) : Fragment() {

    private lateinit var et_position: EditText
    private lateinit var et_id: EditText
    private lateinit var et_name: EditText
    private lateinit var et_description: EditText
    private lateinit var cv_edit: CardView
    private lateinit var tv_edit: TextView

    var model: LiveData<Model>? = null

    private val noteDatabase by lazy { RDatabase.getDatabase(requireContext()).contactDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tvTitle.text = "Edit object"
        cvBack.visibility = View.VISIBLE
        cvBack.setOnClickListener { requireActivity().onBackPressed() }

        et_position = requireView().findViewById(R.id.et_position)
        et_id = requireView().findViewById(R.id.et_id)
        et_name = requireView().findViewById(R.id.et_name)
        et_description = requireView().findViewById(R.id.et_description)
        cv_edit = requireView().findViewById(R.id.cv_edit)
        tv_edit = requireView().findViewById(R.id.tv_edit)

        getLoginDetails(requireContext(), adapterPosition)!!.observe(viewLifecycleOwner) {
            et_position.setText(it.position.toString())
            et_id.setText(it.id)
            et_name.setText(it.name)
            et_description.setText(it.description)
        }

        cv_edit.setOnClickListener {
            if (et_position.text.toString().trim { it <= ' ' }.isNotEmpty()) {

                val newNote = Model(
                    et_position.text.toString().toInt(),
                    et_id.text.toString(),
                    et_name.text.toString(),
                    et_description.text.toString(),
                )
                lifecycleScope.launch {
                    noteDatabase.update(newNote)
                }

                requireActivity().onBackPressed()
            }
        }

        et_position.addTextChangedListener(etWatcher)
        et_id.addTextChangedListener(etWatcher)
        et_name.addTextChangedListener(etWatcher)
    }

    private val etWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            cv_edit.isEnabled = false
            cv_edit.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.system_accent1_50))
            tv_edit.setTextColor(ContextCompat.getColor(context!!, R.color.system_accent1_100))
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val positionInput = et_position.text.toString().trim { it <= ' ' }
            val idInput = et_id.text.toString().trim { it <= ' ' }
            val nameInput = et_name.text.toString().trim { it <= ' ' }
            if (positionInput.isNotEmpty() && idInput.isNotEmpty() && nameInput.isNotEmpty()) {
                cv_edit.isEnabled = true
                cv_edit.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.system_accent1_300))
                tv_edit.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun getLoginDetails(context: Context, position: Int): LiveData<Model>? {
        model = Repository.getLoginDetails(context, position)
        return model
    }
}