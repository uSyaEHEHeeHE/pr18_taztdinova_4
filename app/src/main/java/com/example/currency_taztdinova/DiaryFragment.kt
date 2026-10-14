package com.example.currency_taztdinova

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DiaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary, container, false)

        val tvAccount: AppCompatTextView = view.findViewById(R.id.tv_account)
        val tvRecords: AppCompatTextView = view.findViewById(R.id.tv_records)
        val btBack: AppCompatButton = view.findViewById(R.id.bt_back)

        val settings = requireActivity().getSharedPreferences("currency_diary", Context.MODE_PRIVATE)
        val gson = Gson()

        val account = gson.fromJson(settings.getString("account", ""), Account::class.java)
        tvAccount.text = getString(R.string.account, account.name)

        val type = object : TypeToken<ArrayList<CurrencyRecord>>() {}.type
        val records: ArrayList<CurrencyRecord> =
            gson.fromJson(settings.getString("records_" + account.name, "[]"), type)

        if (records.isEmpty()) {
            tvRecords.setText(R.string.no_records)
        } else {
            val builder = StringBuilder()
            for (record in records) {
                builder.append(record.date)
                    .append("   ")
                    .append(record.currency)
                    .append(" = ")
                    .append(record.rate)
                    .append(" руб.\n\n")
            }
            tvRecords.text = builder.toString()
        }

        btBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
