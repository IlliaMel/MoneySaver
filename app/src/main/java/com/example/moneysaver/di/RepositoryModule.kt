package com.example.moneysaver.di

import android.app.Application
import androidx.room.Room
import com.example.moneysaver.data.data_base.transaction_dp.TransactionDataBase
import com.example.moneysaver.data.repository.TransactionRepositoryImpl
import com.example.moneysaver.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTransactionDataBase(app: Application): TransactionDataBase {
        return Room.databaseBuilder(
            app,
            TransactionDataBase::class.java,
            TransactionDataBase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db: TransactionDataBase): TransactionRepository {
        return TransactionRepositoryImpl(db.transactionDao)
    }
}