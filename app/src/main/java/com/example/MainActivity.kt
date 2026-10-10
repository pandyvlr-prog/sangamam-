package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.CommunityViewModel
import com.example.ui.components.AppBottomNavigation
import com.example.ui.components.AppHeader
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.WarmCreamBackground
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: CommunityViewModel = viewModel()
                SangamamMainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SangamamMainApp(viewModel: CommunityViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userNotification by viewModel.userNotification.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle back button presses gracefully
    val isRootScreen = currentScreen in listOf("HOME", "MEMBERS", "ADD", "FUNDS", "MORE")
    BackHandler(enabled = !isRootScreen) {
        viewModel.navigateBack()
    }

    LaunchedEffect(userNotification) {
        userNotification?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
                viewModel.userNotification.value = null
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("sangamam_main_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (isRootScreen) {
                AppHeader(
                    onNotificationClick = {
                        viewModel.navigateTo("GIFT_DISTRIBUTION")
                    },
                    onProfileClick = {
                        viewModel.navigateTo("SETTINGS")
                    }
                )
            }
        },
        bottomBar = {
            AppBottomNavigation(
                currentScreen = when (currentScreen) {
                    "HOME" -> "HOME"
                    "MEMBERS", "MEMBER_DETAILS", "MEMBER_TRANSACTIONS", "REGISTER_MEMBER" -> "MEMBERS"
                    "ADD" -> "ADD"
                    "FUNDS", "COMMUNITY_FUND_DETAILS", "SAVINGS_FUNDS", "CHIT_FUNDS", "CREATE_CHIT", "BUSINESS_FUNDS", "MEMBER_LOANS", "LEDGER", "EXPENSES", "MONTHLY_COLLECTION" -> "FUNDS"
                    "MORE", "SETTINGS", "OFFICE_BEARERS", "ATTENDANCE", "GALLERY", "GIFT_DISTRIBUTION" -> "MORE"
                    else -> "HOME"
                },
                onNavigate = { targetScreen ->
                    viewModel.navigateTo(targetScreen)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmCreamBackground)
        ) {
            when (currentScreen) {
                "HOME" -> HomeScreen(viewModel = viewModel)
                "MEMBERS" -> MembersScreen(viewModel = viewModel)
                "REGISTER_MEMBER" -> RegisterMemberScreen(viewModel = viewModel)
                "MEMBER_DETAILS" -> MemberDetailsScreen(viewModel = viewModel)
                "MEMBER_TRANSACTIONS" -> MemberTransactionsScreen(viewModel = viewModel)
                "ADD" -> CreateAddScreen(viewModel = viewModel)
                "FUNDS" -> FundsScreen(viewModel = viewModel)
                "COMMUNITY_FUND_DETAILS" -> CommunityFundDetailsScreen(viewModel = viewModel)
                "SAVINGS_FUNDS" -> SavingsFundsScreen(viewModel = viewModel)
                "CHIT_FUNDS" -> ChitFundsScreen(viewModel = viewModel)
                "CREATE_CHIT" -> CreateChitFundScreen(viewModel = viewModel)
                "MONTHLY_COLLECTION" -> MonthlyCollectionScreen(viewModel = viewModel)
                "EXPENSES" -> ExpensesScreen(viewModel = viewModel)
                "LEDGER" -> LedgerScreen(viewModel = viewModel)
                "GIFT_DISTRIBUTION" -> FestivalGiftScreen(viewModel = viewModel)
                "MEMBER_LOANS" -> MemberLoansScreen(viewModel = viewModel)
                "BUSINESS_FUNDS" -> BusinessFundsScreen(viewModel = viewModel)
                "ATTENDANCE" -> AttendanceScreen(viewModel = viewModel)
                "GALLERY" -> GalleryScreen(viewModel = viewModel)
                "SETTINGS" -> SettingsScreen(viewModel = viewModel)
                "OFFICE_BEARERS" -> OfficeBearersScreen(viewModel = viewModel)
                "MORE" -> MoreScreen(viewModel = viewModel)
                else -> HomeScreen(viewModel = viewModel)
            }
        }
    }
}
