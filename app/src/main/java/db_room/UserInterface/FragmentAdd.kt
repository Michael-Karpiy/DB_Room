package db_room.UserInterface

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
import androidx.lifecycle.lifecycleScope
import db_room.R
import db_room.RoomDatabase.RDatabase
import db_room.UserInterface.ActivityMain.Companion.cvBack
import db_room.UserInterface.ActivityMain.Companion.tvTitle
import kotlinx.coroutines.launch

class FragmentAdd : Fragment() {

    private lateinit var et_position: EditText
    private lateinit var et_id: EditText
    private lateinit var et_name: EditText
    private lateinit var et_description: EditText
    private lateinit var cv_accept: CardView
    private lateinit var tv_accept: TextView

    private val noteDatabase by lazy { RDatabase.getDatabase(requireContext()).contactDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tvTitle.text = "Add new object"
        cvBack.visibility = View.VISIBLE
        cvBack.setOnClickListener { requireActivity().onBackPressed() }

        et_position = requireView().findViewById(R.id.et_position)
        et_id = requireView().findViewById(R.id.et_id)
        et_name = requireView().findViewById(R.id.et_name)
        et_description = requireView().findViewById(R.id.et_description)
        cv_accept = requireView().findViewById(R.id.cv_accept)
        tv_accept = requireView().findViewById(R.id.tv_accept)

        cv_accept.setOnClickListener {
            if (et_position.text.toString().trim { it <= ' ' }.isNotEmpty()) {

                val newNote = Model(
                    et_position.text.toString().toInt(),
                    "@" + et_id.text.toString(),
                    et_name.text.toString(),
                    et_description.text.toString(),
                )
                lifecycleScope.launch {
                    noteDatabase.insert(newNote)
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
            cv_accept.isEnabled = false
            cv_accept.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.system_accent1_50))
            tv_accept.setTextColor(ContextCompat.getColor(context!!, R.color.system_accent1_100))
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val positionInput = et_position.text.toString().trim { it <= ' ' }
            val idInput = et_id.text.toString().trim { it <= ' ' }
            val nameInput = et_name.text.toString().trim { it <= ' ' }
            if (positionInput.isNotEmpty() && idInput.isNotEmpty() && nameInput.isNotEmpty()) {
                cv_accept.isEnabled = true
                cv_accept.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.system_accent1_300))
                tv_accept.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}