package com.example.currency_taztdinova

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_auth, container, false)

        val etName: AppCompatEditText = view.findViewById(R.id.et_name)
        val etPass: AppCompatEditText = view.findViewById(R.id.et_pass)
        val btEnter: AppCompatButton = view.findViewById(R.id.bt_enter)

        val settings = requireActivity().getSharedPreferences("currency_diary", Context.MODE_PRIVATE)
        val gson = Gson()

        val savedJson = settings.getString("account", null)
        if (savedJson != null) {
            val saved = gson.fromJson(savedJson, Account::class.java)
            etName.setText(saved.name)
        }

        btEnter.setOnClickListener {
            val name = etName.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (name.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), R.string.empty_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val oldJson = settings.getString("account", null)
            if (oldJson != null) {
                val old = gson.fromJson(oldJson, Account::class.java)
                if (old.name == name && old.password != pass) {
                    Toast.makeText(requireContext(), R.string.wrong_pass, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val account = Account(name, pass)
            settings.edit().putString("account", gson.toJson(account)).apply()

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, AddRateFragment())
                .commit()
        }

        return view
    }
}
