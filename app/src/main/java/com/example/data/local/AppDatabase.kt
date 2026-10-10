package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.*
import com.example.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MemberEntity::class,
        FamilyMemberEntity::class,
        OfficeBearerEntity::class,
        MeetingEntity::class,
        AttendanceEntity::class,
        FundEntity::class,
        CollectionRecordEntity::class,
        ConsolidatedLedgerEntity::class,
        ExpenseEntity::class,
        BusinessFundEntity::class,
        BusinessFundReturnEntity::class,
        MemberLoanEntity::class,
        LoanRepaymentEntity::class,
        ChitFundEntity::class,
        ChitAuctionEntity::class,
        ChitMemberPaymentEntity::class,
        GiftDistributionEntity::class,
        GiftRecipientEntity::class,
        GalleryItemEntity::class,
        CommunitySettingsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun officeBearerDao(): OfficeBearerDao
    abstract fun meetingDao(): MeetingDao
    abstract fun fundDao(): FundDao
    abstract fun ledgerDao(): LedgerDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun businessFundDao(): BusinessFundDao
    abstract fun loanDao(): LoanDao
    abstract fun chitDao(): ChitDao
    abstract fun giftDao(): GiftDao
    abstract fun galleryDao(): GalleryDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sangamam_devangar_database"
                )
                .fallbackToDestructiveMigration()
                .build()

                INSTANCE = instance

                // Seed initially if database is fresh/empty
                scope.launch(Dispatchers.IO) {
                    try {
                        DatabaseSeeder.seedDatabaseIfEmpty(instance)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                instance
            }
        }
    }
}
