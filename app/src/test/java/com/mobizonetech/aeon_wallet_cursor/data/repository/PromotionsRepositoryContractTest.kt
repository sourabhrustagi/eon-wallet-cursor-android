package com.mobizonetech.aeon_wallet_cursor.data.repository

import com.google.common.truth.Truth.assertThat
import com.mobizonetech.aeon_wallet_cursor.data.remote.api.PromotionsApiService
import com.mobizonetech.aeon_wallet_cursor.domain.util.Result
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PromotionsRepositoryContractTest {

    private lateinit var server: MockWebServer
    private lateinit var api: PromotionsApiService
    private lateinit var repo: PromotionsRepositoryImpl

    @Before
    fun setup() {
        server = MockWebServer().apply { start() }
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PromotionsApiService::class.java)
        repo = PromotionsRepositoryImpl(api)
    }

    @After
    fun teardown() { server.shutdown() }

    @Test
    fun syncPromotions_success_returnsSuccess() = runBlocking {
        val body = """{"success":true,"message":null,"data":[]}"""
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val res = repo.syncPromotions()
        assertThat(res).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun syncPromotions_httpError_returnsError() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(500))
        val res = repo.syncPromotions()
        assertThat(res).isInstanceOf(Result.Error::class.java)
    }
}


