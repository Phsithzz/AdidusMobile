package com.example.adidusshop.services

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "adidus_session_pref"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_NAME = "user_name"
    private const val KEY_LASTNAME = "user_lastname"
    private const val KEY_EMAIL = "user_email"
    private const val KEY_ROLE = "user_role"
    private const val KEY_BASE_URL = "base_url"
    private const val KEY_MOCK_MODE = "mock_mode"

    const val DEFAULT_BASE_URL = "http://10.0.2.2:3000/"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun saveLogin(token: String?, name: String, lastname: String, email: String, role: String) {
        prefs?.edit()?.apply {
            putString(KEY_TOKEN, token)
            putString(KEY_NAME, name)
            putString(KEY_LASTNAME, lastname)
            putString(KEY_EMAIL, email)
            putString(KEY_ROLE, role)
            apply()
        }
    }

    fun getToken(): String? = prefs?.getString(KEY_TOKEN, null)

    fun getUserEmail(): String = prefs?.getString(KEY_EMAIL, "") ?: ""
    fun getUserName(): String = prefs?.getString(KEY_NAME, "") ?: ""
    fun getUserLastname(): String = prefs?.getString(KEY_LASTNAME, "") ?: ""
    fun getUserRole(): String = prefs?.getString(KEY_ROLE, "user") ?: "user"

    fun isLoggedIn(): Boolean {
        val token = getToken()
        val email = getUserEmail()
        return !token.isNullOrEmpty() || email.isNotEmpty()
    }

    fun logout() {
        prefs?.edit()?.apply {
            remove(KEY_TOKEN)
            remove(KEY_NAME)
            remove(KEY_LASTNAME)
            remove(KEY_EMAIL)
            remove(KEY_ROLE)
            apply()
        }
    }

    fun setBaseUrl(url: String) {
        val sanitized = if (!url.endsWith("/")) "$url/" else url
        prefs?.edit()?.putString(KEY_BASE_URL, sanitized)?.apply()
    }

    fun getBaseUrl(): String = prefs?.getString(KEY_BASE_URL, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL

    fun setMockMode(enabled: Boolean) {
        prefs?.edit()?.putBoolean(KEY_MOCK_MODE, enabled)?.apply()
    }

    fun isMockMode(): Boolean = prefs?.getBoolean(KEY_MOCK_MODE, false) ?: false
}
