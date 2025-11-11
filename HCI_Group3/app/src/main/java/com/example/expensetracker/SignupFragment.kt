package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.expensetracker.data.AppDatabase
import com.example.expensetracker.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val username = view.findViewById<EditText>(R.id.editTextUsername)
        val password = view.findViewById<EditText>(R.id.editTextPassword)
        val confirm = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        val signupButton = view.findViewById<Button>(R.id.buttonSignup)

        // Entry animation
        view.alpha = 0f
        view.translationY = 30f
        view.animate().alpha(1f).translationY(0f).setDuration(350).start()

        val db = AppDatabase.getInstance(requireContext())

        signupButton.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString()
            val conf = confirm.text.toString()

            if (user.isEmpty() || pass.isEmpty() || conf.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass != conf) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val exists = withContext(Dispatchers.IO) { db.userDao().findByUsername(user) }
                if (exists != null) {
                    Toast.makeText(requireContext(), "Username already taken", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                val id = withContext(Dispatchers.IO) { db.userDao().insert(User(username = user, passwordHash = hash(pass))) }
                if (id > 0) {
                    Toast.makeText(requireContext(), "Signup successful", Toast.LENGTH_SHORT).show()
                    // Optionally switch to Login tab
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Signup failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun hash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
