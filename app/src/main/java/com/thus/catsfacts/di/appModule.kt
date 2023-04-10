package com.thus.catsfacts.di

import com.thus.catsfacts.db.AppDatabase
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.thus.catsfacts.BuildConfig.FACTS_BASE_URL
import com.thus.catsfacts.model.DataMapper
import com.thus.catsfacts.network.ApiService
import com.thus.catsfacts.views.RandomFactView
import com.thus.catsfacts.views.RandomFactViewImpl
import com.thus.catsfacts.views.RandomFactViewModel
import com.thus.catsfacts.views.adapter.FactsRecyclerViewAdapter
import com.thus.catsfacts.views.repo.RandomFactRepo
import com.thus.catsfacts.views.repo.RandomFactRepoImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    factory { FactsRecyclerViewAdapter() }
    factory<RandomFactView> { RandomFactViewImpl(adapter = get()) }
    factory { DataMapper(dao = get()) }
    factory<RandomFactRepo> { RandomFactRepoImpl(apiService = get(), dataMapper = get()) }

    viewModel { RandomFactViewModel(randomFactRepo = get()) }

    single { AppDatabase.getAppDatabase(context = get()).catDetailsDao() }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(FACTS_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(ApiService::class.java)
    }
}