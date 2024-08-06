package com.synrgy7team4.feature_auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.synrgy7team4.feature_auth.data.remote.response.DataX
import com.synrgy7team4.feature_auth.data.remote.response.LoginResponse
import com.synrgy7team4.feature_auth.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImplementLocalSource(private val dataStore: DataStore<Preferences>): AuthLocalSource {

    companion object {

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USER_ID = stringPreferencesKey("id")
        private val NAME = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    val name: Flow<String?> = dataStore.data.map { preferences ->
        preferences[NAME]
    }

//    override suspend fun saveToken(token: String) {
//        dataStore.edit {
//            it[DATASTORE_KEY] = token
//        }
//    }

    override suspend fun saveSession(dataX: DataX) {
        dataStore.edit {
            it[EMAIL_KEY] = dataX.email
            it[USER_ID] = dataX.id
            it[NAME] = dataX.name
            it[TOKEN_KEY] = dataX.jwtToken
            it[REFRESH_TOKEN] = dataX.jwtToken

        }
    }



    override suspend fun loadSession(): Flow<DataX> {

        return dataStore.data.map {
            DataX(
                it[EMAIL_KEY] ?: "",
                it[USER_ID] ?: "",
                it[TOKEN_KEY] ?: "",
                it[NAME] ?: "",
                it[REFRESH_TOKEN] ?: ""
            )

        }

    }

    override suspend fun deleteSession() {

        dataStore.edit {
            it.clear()
        }

    }


}