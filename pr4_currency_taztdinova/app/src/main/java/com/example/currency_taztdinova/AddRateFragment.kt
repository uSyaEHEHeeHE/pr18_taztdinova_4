package com.example.currency_taztdinova

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddRateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        val tvAccount: AppCompatTextView = view.findViewById(R.id.tv_account)
        val spCurrency: AppCompatSpinner = view.findViewById(R.id.sp_currency)
        val etRate: AppCompatEditText = view.findViewById(R.id.et_rate)
        val etDate: AppCompatEditText = view.findViewById(R.id.et_date)
        val btAdd: AppCompatButton = view.findViewById(R.id.bt_add)
        val btDiary: AppCompatButton = view.findViewById(R.id.bt_diary)

        val settings = requireActivity().getSharedPreferences("currency_diary", Context.MODE_PRIVATE)
        val gson = Gson()

        val account = gson.fromJson(settings.getString("account", ""), Account::class.java)
        tvAccount.text = getString(R.string.account, account.name)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCurrency.adapter = adapter

        btAdd.setOnClickListener {
            val rateText = etRate.text.toString().trim().replace(',', '.')
            val date = etDate.text.toString().trim()

            if (rateText.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(), R.string.empty_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rate = rateText.toDoubleOrNull()
            if (rate == null) {
                Toast.makeText(requireContext(), R.string.wrong_rate, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val key = "records_" + account.name
            val type = object : TypeToken<ArrayList<CurrencyRecord>>() {}.type
            val records: ArrayList<CurrencyRecord> =
                gson.fromJson(settings.getString(key, "[]"), type)

            records.add(CurrencyRecord(spCurrency.selectedItem.toString(), rate, date))
            settings.edit().putString(key, gson.toJson(records)).apply()

            Toast.makeText(requireContext(), R.string.added, Toast.LENGTH_SHORT).show()
            etRate.setText("")
            etDate.setText("")
        }

        btDiary.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, DiaryFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
