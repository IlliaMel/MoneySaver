package com.example.moneysaver.di

import android.app.Application
import androidx.room.Room
import com.example.moneysaver.data.data_base.account_db.AccountDataBase
import com.example.moneysaver.data.data_base.category_db.CategoryDataBase
import com.example.moneysaver.data.data_base.currency_db.CurrencyDataBase
import com.example.moneysaver.data.data_base.transaction_db.TransactionDataBase
import com.example.moneysaver.data.remote.CurrencyApi
import com.example.moneysaver.data.repository.*
import com.example.moneysaver.domain.repository.*
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
    fun provideAccountDataBase(app: Application): AccountDataBase {
        return Room.databaseBuilder(
            app,
            AccountDataBase::class.java,
            AccountDataBase.DATABASE_NAME
        ).createFromAsset("databases/accounts_default.db").build()
    }

    @Provides
    @Singleton
    fun provideCategoryDataBase(app: Application): CategoryDataBase {
        return Room.databaseBuilder(
            app,
            CategoryDataBase::class.java,
            CategoryDataBase.DATABASE_NAME
        ).createFromAsset("databases/categories_default.db").build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDataBase(app: Application): CurrencyDataBase {
        return Room.databaseBuilder(
            app,
            CurrencyDataBase::class.java,
            CurrencyDataBase.DATABASE_NAME
        ).build()
    }


    @Provides
    @Singleton
    fun provideTransactionRepository(
        transactionDB: TransactionDataBase,
        categoryDB: CategoryDataBase,
        accountDB: AccountDataBase,
        currencyDB: CurrencyDataBase,
        currencyApi: CurrencyApi
    ): FinanceRepository {
        return FinanceRepositoryImpl(
            transactionDB.transactionDao,
            categoryDB.categoryDao,
            accountDB.accountDao,
            currencyDB.currencyDao,
            currencyApi
        )
    }



}
